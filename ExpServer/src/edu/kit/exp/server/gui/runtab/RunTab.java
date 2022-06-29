package edu.kit.exp.server.gui.runtab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.gui.runtab.clientviewer.ClientViewer;
import edu.kit.exp.server.gui.starttab.StartTab;
import edu.kit.exp.server.gui.structuretab.StructureTab;
import edu.kit.exp.server.gui.structuretab.StructureTreeBuilder;
import edu.kit.exp.server.run.RunStateLogEntry;
import edu.kit.exp.server.run.SessionRunException;

/**
 * Tab for running an experiment.
 * 
 * @author Martin Caspari
 * 
 */
public class RunTab extends JPanel implements Observer {

	private static final String CONTINUE_BUTTON_TEXT = "Continue";
	private static final long serialVersionUID = -4150123067575255277L;

    /** The gui controller */
    private RunTabController guiController = RunTabController.getInstance();

    /** The RunTab instance */
    private static RunTab instance;

    // Panels
    private JPanel textPanel;
    private JPanel buttonPanel;
    private JPanel statementPanel;
    private JScrollPane scrollPane;
	private JPanel sideBar;

    // Text area
    private JTextArea textArea;

    // Buttons
	private JButton continueButton;

    private Object[][] tableData;
	private String[] tableHeadings = { "Time", "Sender", "Event", "Name", "Value" };
	private JTable table;
	private DefaultTableModel tableModel;

    /**
     * This method gets a single instance of RunTab. If no instance exists, a
     * new one is created by invoking {@link RunTab#RunTab()}.
     *
     * @return a single instance of RunTab
     */
    public static RunTab getInstance() {

        if (instance == null) {
            instance = new RunTab();
        }

        return instance;
    }

    /**
     * This constructor instantiates a new run tab for the server.
     */
    private RunTab() {
        super();
        setBorder(new EmptyBorder(20, 20, 20, 20));
        guiController.addObserver(this);
        init();
    }

    /**
     * Initializes the RunTab.
     */
	private void init() {

		setLayout(new BorderLayout(0, 0));

        /* Text panel */
		textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout(0, 0));
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		textPanel.add(scrollPane, BorderLayout.CENTER);
        createStatementTable();
        textPanel.add(statementPanel, BorderLayout.CENTER);

		sideBar = ClientViewer.getInstance();
		sideBar.setPreferredSize(new Dimension(200, this.getHeight()));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sideBar, textPanel);
		this.add(splitPane, BorderLayout.CENTER);

        /* Button panel */
		buttonPanel = new JPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);

        // Setup continue button
		continueButton = new JButton(CONTINUE_BUTTON_TEXT);
		continueButton.setIcon(new ImageIcon(MainFrame.class.getResource("/edu/kit/exp/server/resources/session.gif")));
		continueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					if (guiController.startSession()) {
						RunTab.getInstance().disableComponents();
						StartTab.getInstance().disableComponents();
						disableComponents(StructureTab.getInstance(), false);
						disableComponents(StructureTreeBuilder.getInstance().getExperimentBuilder(), false);
						StructureTreeBuilder.getInstance().disableComponents();
					}
				} catch (SessionRunException e) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    LogHandler.printException(e, "Error when trying to continue session");
                }
			}
		});
		buttonPanel.add(continueButton);

	}

    /**
     * Update information
     * @param o
     * @param arg
     */
	public void update(Observable o, Object arg) {

		if (arg instanceof RunStateLogEntry){
			RunStateLogEntry logEntry = (RunStateLogEntry) arg;
			if (logEntry.isOverwriteLatestEntry() && tableModel.getRowCount() > 0){
				tableModel.removeRow(tableModel.getRowCount() - 1);
			}
			tableModel.addRow(logEntry.getValues());
		}
		else if (arg instanceof String) {
			try {
				throw new Exception("Not supposed to send only Strings to Server RunTab log!");
			} catch (Exception e) {
				LogHandler.printException(e);
			}
		}
	}

	public void disableComponents(Container container, boolean enable) {
		Component[] components = container.getComponents();
		for (Component component : components) {
			component.setEnabled(false);
			component.setFocusable(false);
			if (component instanceof Container) {
				disableComponents((Container) component, enable);
			}
		}
	}

	public void disableComponents() {

		continueButton.setEnabled(false);
	}

	private void createStatementTable() {
		statementPanel = new JPanel();
		statementPanel.setBackground(Color.WHITE);
		statementPanel.setLayout(new GridLayout(1, 0, 0, 0));

		tableModel = new DefaultTableModel(tableData, tableHeadings);
		table = new StatementTable(tableModel);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setBackground(Color.WHITE);

		statementPanel.add(new JScrollPane(table));

		setColumnWidth(new int[] {0, 30, 50, 80, 400});
	}

	private void setColumnWidth(int[] columnWidths) {
		for (int i = 0; i < columnWidths.length; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
		}
	}

}
