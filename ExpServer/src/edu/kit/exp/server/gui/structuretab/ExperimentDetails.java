package edu.kit.exp.server.gui.structuretab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.gui.sensor.SensorConfiguratorDialog;
import edu.kit.exp.server.gui.sensor.SensorImplementationFinder;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.jpa.entity.SensorEntry;
import edu.kit.exp.server.structure.StructureManagementException;
import net.miginfocom.swing.MigLayout;

/**
 * This class generates a container for editing experiments.</br>
 * It is a child panel of the structure tab and allows users to edit the details
 * of their experiments.
 * 
 * @see StructureTab
 */
public class ExperimentDetails extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6433621850611921076L;

	/** The gui controller. */
	private StructureTabController guiController = StructureTabController.getInstance();

	/** The text field name. */
	private JTextField textFieldName;

	/** The editor pane. */
	private JEditorPane editorPane;

	/** The updated experiment. */
	private Experiment updatedExperiment;

	private DefaultListModel<SensorEntry> listModelForSupportedSensors = new DefaultListModel<SensorEntry>();
	private DefaultListModel<SensorEntry> listModelForUsedSensors = new DefaultListModel<SensorEntry>();

	private JScrollPane supportedSensorsScroller;
	private JScrollPane usedSensorsScroller;

	private JList<SensorEntry> listUsedSensors;
	private JList<SensorEntry> listSupportedSensors;

	/**
	 * This constructor instantiates a new JPanel that shows the details of an
	 * experiment. All experiments are persisted on the server side.
	 * 
	 * @param experiment
	 *            An Experiment variable which contains a certain experiment.
	 */
	public ExperimentDetails(Experiment experiment) {

		this.updatedExperiment = experiment;

		setBorder(new EmptyBorder(10, 3, 10, 3));
		setBackground(UIManager.getColor("Button.background"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel inputPanel = new JPanel();
		panel.add(inputPanel);
		inputPanel.setBackground(UIManager.getColor("Button.background"));
		inputPanel.setLayout(new MigLayout("", "[][grow]", "[][][grow]"));

		JLabel labelId = new JLabel("ID:");
		inputPanel.add(labelId, "cell 0 0");

		JLabel labelIdInfo = new JLabel(experiment.getIdExperiment().toString());
		inputPanel.add(labelIdInfo, "cell 1 0");

		JLabel lblExperimentName = new JLabel("Experiment Name:");
		inputPanel.add(lblExperimentName, "cell 0 1");

		textFieldName = new JTextField(experiment.getName());
		inputPanel.add(textFieldName, "cell 1 1,growx");
		textFieldName.setColumns(10);

		JLabel lblDescription = new JLabel("Description:");
		inputPanel.add(lblDescription, "cell 0 2");

		editorPane = new JEditorPane();
		editorPane.setBackground(Color.WHITE);
		editorPane.setText(experiment.getDescription());
		inputPanel.add(editorPane, "cell 1 2,grow");

		JLabel labelHeading = new JLabel("Details of Experiment");
		panel.add(labelHeading, BorderLayout.NORTH);
		labelHeading.setHorizontalAlignment(SwingConstants.LEFT);
		labelHeading.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel labelSupportedSensors = new JLabel("Supported Sensors:");
		inputPanel.add(labelSupportedSensors, "cell 0 3");

		listSupportedSensors = new JList<SensorEntry>();
		try {
			addActualSensors(listModelForSupportedSensors, listModelForUsedSensors);
		} catch (ClassNotFoundException e) {
            LogHandler.printException(e, "Could not list supported sensors");
		}
		listSupportedSensors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSupportedSensors.setVisibleRowCount(-1);
		listSupportedSensors.setModel(listModelForSupportedSensors);
		listSupportedSensors.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
            if (event.getClickCount() == 2) {
                int index = listSupportedSensors.locationToIndex(event.getPoint());
                listModelForUsedSensors.addElement(listModelForSupportedSensors.getElementAt(index));
                listModelForSupportedSensors.remove(index);
                listUsedSensors.setSelectedIndex(listModelForUsedSensors.getSize() - 1);

                try {
                    guiController.createNewSensorEntry(listModelForUsedSensors.getElementAt(listModelForUsedSensors.getSize() - 1));
                } catch (StructureManagementException e) {
                    LogHandler.printException(e, "Could not create new sensor entry");
                }
            }

			}
		});
		supportedSensorsScroller = new JScrollPane();
		supportedSensorsScroller.setViewportView(listSupportedSensors);
		supportedSensorsScroller.setPreferredSize(new Dimension(250, 150));
		inputPanel.add(supportedSensorsScroller, "cell 1 3,growx");

		JLabel labelUsedSensors = new JLabel("Used Sensors:");
		inputPanel.add(labelUsedSensors, "cell 0 4");

		listUsedSensors = new JList<SensorEntry>();
		listUsedSensors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listUsedSensors.setVisibleRowCount(-1);
		listUsedSensors.setModel(listModelForUsedSensors);
		listUsedSensors.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
            if (event.getClickCount() == 2) {
                if (JOptionPane.showConfirmDialog(null, "Removing Entry will delete current sensor configurations. Continue?", "Information", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    int index = listUsedSensors.locationToIndex(event.getPoint());
                    listModelForSupportedSensors.addElement(listModelForUsedSensors.getElementAt(index));
                    listModelForUsedSensors.remove(index);
                    listSupportedSensors.setSelectedIndex(listModelForSupportedSensors.getSize() - 1);

                    try {
                        guiController.removeSensorEntry(listModelForSupportedSensors.getElementAt(listModelForSupportedSensors.getSize() - 1));
                    } catch (StructureManagementException e) {
                        LogHandler.printException(e, "Could not remove sensor entry");
                    }
                }
            }

			}
		});
		usedSensorsScroller = new JScrollPane();
		usedSensorsScroller.setViewportView(listUsedSensors);
		usedSensorsScroller.setPreferredSize(new Dimension(250, 150));
		inputPanel.add(usedSensorsScroller, "cell 1 4,growx");

		JButton buttonConfigSensor = new JButton("Configure Selected Sensor");
		buttonConfigSensor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (listUsedSensors.getSelectedIndex() != -1) {
					SensorEntry selectedEntry = listUsedSensors.getSelectedValue();
					selectedEntry.setConfiguration(SensorConfiguratorDialog.getInstance().showSensorConfiguratorDialog(selectedEntry.getName(), selectedEntry.getConfiguration()));
					try {
						guiController.updateSensorEntry(selectedEntry);
					} catch (StructureManagementException e) {
                        LogHandler.printException(e, "Could not update sensor entry");
					}
				} else {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), "Please select a used sensor.");
				}
			}
		});
		inputPanel.add(buttonConfigSensor, "cell 1 5,growx");

		JPanel buttonPanel = new JPanel();
		panel.add(buttonPanel, BorderLayout.SOUTH);

		JButton buttonApplyChanges = new JButton("Apply Changes");
		buttonApplyChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				updatedExperiment.setDescription(editorPane.getText());
				updatedExperiment.setName(textFieldName.getText());

				try {
					guiController.updateExperiment(updatedExperiment);
				} catch (StructureManagementException e1) {
					JOptionPane.showMessageDialog(null, e1, "Error", JOptionPane.ERROR_MESSAGE);
                    LogHandler.printException(e1, "Could not update experiment");
				}
			}
		});
		buttonPanel.add(buttonApplyChanges);
	}

	private void addActualSensors(DefaultListModel<SensorEntry> listModelSupported, DefaultListModel<SensorEntry> listModelUsed) throws ClassNotFoundException {
		List<SensorEntry> supportedSensors = SensorImplementationFinder.getInstance().getSensorEntries();
		List<SensorEntry> usedSensors = guiController.getCurrentExperiment().getUsedSensors();
		
		for (SensorEntry sensorEntry : usedSensors) {
			// check if Element exists in supported list
			if (supportedSensors.contains(sensorEntry)) {
				listModelUsed.addElement(sensorEntry);
			}
		}
		
		for (SensorEntry sensorEntry : supportedSensors) {
			// check if Element exists in used List
			if (!usedSensors.contains(sensorEntry)) {
				listModelSupported.addElement(sensorEntry);
			}
		}
	}
}
