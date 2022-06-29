package edu.kit.exp.server.gui.starttab;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.structure.StructureManagementException;
import net.miginfocom.swing.MigLayout;

/**
 * This class opens a frame where new experiments can be created.</br>
 * An experiment name and a description for the experiment can be entered.
 */
public class ExperimentCreationFrame extends JDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3826698217592532154L;

	/** The instance. */
	private static ExperimentCreationFrame instance;

	/** The text field experiment name. */
	private JTextField textFieldExperimentName;

	/** The editor pane. */
	private JEditorPane editorPane;

	/** The gui controller. */
	private StartTabController guiController = StartTabController.getInstance();

	/**
	 * Gets the single instance of ExperimentCreationFrame.
	 * 
	 * @return a single instance of ExperimentCreationFrame
	 */
	public static ExperimentCreationFrame getInstance() {

		if (instance == null) {
			instance = new ExperimentCreationFrame();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new experiment creation frame.
	 */
	private ExperimentCreationFrame() {

		setTitle("New Experiment");
		this.setBounds(150, 150, 450, 350);
		this.setModal(true);
		this.setLocationRelativeTo(MainFrame.getInstance());

		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panelInput = new JPanel();
		panelInput.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(panelInput, BorderLayout.CENTER);
		panelInput.setLayout(new MigLayout("", "[][grow]", "[][grow]"));

		JLabel lblExperimentName = new JLabel("Experiment Name:");
		panelInput.add(lblExperimentName, "cell 0 0,alignx trailing");

		textFieldExperimentName = new JTextField();
		addKeyBinding(textFieldExperimentName);
		panelInput.add(textFieldExperimentName, "cell 1 0,growx");

		JLabel lblDescription = new JLabel("Description:");
		panelInput.add(lblDescription, "cell 0 1");

		editorPane = new JEditorPane();
		addKeyBinding(editorPane);
		panelInput.add(editorPane, "cell 1 1,grow");

		JPanel panelButton = new JPanel();
		getContentPane().add(panelButton, BorderLayout.SOUTH);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				createNewExperimentAndClose();
			}

		});
		panelButton.add(btnSave);

	}

	private void addKeyBinding(JComponent component) {
		component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");
		component.getActionMap().put("ENTER", new AbstractAction() {
			private static final long serialVersionUID = 7092975354991077097L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				createNewExperimentAndClose();
			}
		});
	}

	private void createNewExperimentAndClose() {
		String name = textFieldExperimentName.getText();
		String description = editorPane.getText();
		try {
			guiController.createNewExperiment(name, description);
			ExperimentCreationFrame.getInstance().dispose();
		} catch (DataInputException | StructureManagementException e) {
			JOptionPane.showMessageDialog(ExperimentCreationFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LogHandler.printException(e, "Could not create experiment");
		}
	}
}
