package edu.kit.exp.server.gui.starttab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * This class creates a JPanel where experiments can be generated, opened or
 * deleted. </br>
 * It is integrated into the mainframe and implements the Observer interface.
 * 
 * @see Observer
 * @see MainFrame
 */
public class StartTab extends JPanel implements Observer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4150123067575255277L;

	/** The instance. */
	private static StartTab instance = new StartTab();

	/* Panels */
	/** The info panel. */
	private JPanel infoPanel;

	/** The table panel. */
	private JPanel tablePanel;

	/** The button panel. */
	private JPanel buttonPanel;

	/* ScollPane */
	/** The j scroll pane table. */
	private JScrollPane jScrollPaneTable;

	/* Table */
	/** The table model. */
	private DefaultTableModel tableModel;

	/** The table. */
	private JTable table;

	/* Label */
	/** The Jlabel information. */
	private JLabel jLabelInformation;

	/* Button */
	/** The Jbutton open experiment. */
	private JButton jButtonOpenExperiment;

	/** The Jbutton new experiment. */
	private JButton jButtonNewExperiment;

	/** The gui controller. */
	private StartTabController guiController = StartTabController.getInstance();

	/** The table data. */
	private Object[][] tableData;

	/** The table headings. */
	private String[] tableHeadings = { "ID", "Name", "Description" };

	/** The button delete experiment. */
	private JButton btnDeleteExperiment;

	/**
	 * This constructor instantiates a new start tab.
	 */
	private StartTab() {
		super();
		setBorder(new EmptyBorder(20, 20, 20, 20));
		guiController.addObserver(this);
		init();
	}

	/**
	 * This method gets the single instance of StartTab.
	 * 
	 * @return single instance of StartTab
	 */
	public static StartTab getInstance() {

		return instance;
	}

	/**
	 * This method initializes the start tab.
	 */
	private void init() {

		// Table Pane
		List<Experiment> list = null;

		try {
			list = guiController.getAllExperiments();
		} catch (DataManagementException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LogHandler.printException(e, "Could not retrieve all experiments");
		}
		tableData = transFormTableData(list);

		this.setLayout(new BorderLayout(0, 0));

		tablePanel = new JPanel();
		add(tablePanel);
		
		createExperimentTable();

		buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		add(buttonPanel, BorderLayout.SOUTH);

		jButtonNewExperiment = new JButton("New Experiment");
		jButtonNewExperiment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ExperimentCreationFrame.getInstance().setVisible(true);

			}
		});

		jButtonOpenExperiment = new JButton("Open Experiment");
		jButtonOpenExperiment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openSelectedExperiment();
				} catch (StructureManagementException | DataInputException e1) {
					JOptionPane.showMessageDialog(StartTab.getInstance(), e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
                    LogHandler.printException(e1, "Could not open experiment");
				}
			}
		});

		buttonPanel.add(jButtonNewExperiment);
		buttonPanel.add(jButtonOpenExperiment);

		btnDeleteExperiment = new JButton("Delete Experiment");
		btnDeleteExperiment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//
				int answer = JOptionPane.showConfirmDialog(StartTab.getInstance(),
						"Do you want to delete this experiment? All date linked to this experiment will be lost.");

				if (answer == JOptionPane.YES_OPTION) {

					int selectedRow = table.getSelectedRow();
					Integer idExperiment = null;

					if (selectedRow >= 0) {
						Vector<?> rowVector = (Vector<?>) tableModel.getDataVector().elementAt(selectedRow);
						idExperiment = Integer.valueOf(rowVector.elementAt(0).toString());
					}
					try {
						guiController.deleteExperiment(idExperiment);
					} catch (DataInputException | StructureManagementException e) {
						JOptionPane.showMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
                        LogHandler.printException(e, "Could not delete experiment");
					}
				}
			}
		});

		buttonPanel.add(btnDeleteExperiment);

		// Info Panel
		infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
		infoPanel.setLayout(new GridLayout(0, 1, 0, 0));
		add(infoPanel, BorderLayout.NORTH);

		jLabelInformation = new JLabel("Please choose an experiment from the list below.");
		infoPanel.add(jLabelInformation);

	}

	/**
	 * This method transforms the table data.
	 * 
	 * @param list
	 *            A List of Experiments
	 * @return the object[][] which contains data of the experiments.
	 */
	private Object[][] transFormTableData(List<Experiment> list) {

		int rows = list.size();
		int collumns = 3;

		Object[][] result = new Object[rows][collumns];

		Experiment experiment;

		for (int i = 0; i < list.size(); i++) {

			experiment = list.get(i);
			result[i][0] = experiment.getIdExperiment();
			result[i][1] = experiment.getName();
			result[i][2] = experiment.getDescription();

		}

		return result;

	}

	/**
	 * This method updates the table.
	 * 
	 * @param e
	 *            The List of Experiments.
	 */
	private void updateTable(List<Experiment> e) {

		tableData = transFormTableData(e);
		tablePanel.removeAll();
		createExperimentTable();
		tablePanel.revalidate();

	}

	/**
	 * This method creates the experiment table.
	 */
	private void createExperimentTable() {

		tablePanel.setBackground(Color.WHITE);
		tablePanel.setLayout(new GridLayout(1, 0, 0, 0));

		tableModel = new DefaultTableModel(tableData, tableHeadings);
		table = new ExperimentTable(tableModel);

		addKeyBindingToTable();
		
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setBackground(Color.WHITE);
		jScrollPaneTable = new JScrollPane(table);
		tablePanel.add(jScrollPaneTable);

		table.getColumnModel().getColumn(0).setPreferredWidth(0);
		table.getColumnModel().getColumn(1).setPreferredWidth(90);
		table.getColumnModel().getColumn(2).setPreferredWidth(411);
	}

	/**
	 * This method is called to update the experiment table.
	 * 
	 * @param o
	 *            The Observable.
	 * @param arg
	 *            An Object argument.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {

		updateTable((List<Experiment>) arg);

	}

	// Liang
	public void disableComponents() {
		table.setEnabled(false);
		jButtonNewExperiment.setEnabled(false);
		jButtonOpenExperiment.setEnabled(false);
		btnDeleteExperiment.setEnabled(false);
	}

	private void addKeyBindingToTable() {
		table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");
		table.getActionMap().put("ENTER", new AbstractAction() {

			private static final long serialVersionUID = 7092975354991077097L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					openSelectedExperiment();
				} catch (StructureManagementException | DataInputException e1) {
					JOptionPane.showMessageDialog(StartTab.getInstance(), e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
                    LogHandler.printException(e1);
				}
			}
		});
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					try {
						openSelectedExperiment();
					} catch (StructureManagementException | DataInputException e1) {
						JOptionPane.showMessageDialog(StartTab.getInstance(), e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
                        LogHandler.printException(e1);
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}
		});
	}

	private void openSelectedExperiment() throws StructureManagementException, DataInputException {
		int selectedRow = table.getSelectedRow();
		Integer idExperiment = null;
		if (selectedRow >= 0) {
			Vector<?> rowVector = (Vector<?>) tableModel.getDataVector().elementAt(selectedRow);
			idExperiment = Integer.valueOf(rowVector.elementAt(0).toString());
		} else {
			if (tableModel.getDataVector().size() > 0) {
				Vector<?> rowVector = (Vector<?>) tableModel.getDataVector().elementAt(0);
				idExperiment = Integer.valueOf(rowVector.elementAt(0).toString());
			}
		}
		guiController.openExperiment(idExperiment);
	}
}
