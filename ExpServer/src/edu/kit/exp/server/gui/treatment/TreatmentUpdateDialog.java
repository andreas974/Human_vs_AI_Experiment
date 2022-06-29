package edu.kit.exp.server.gui.treatment;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.ReflectionPackageManager;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.Institution;
import edu.kit.exp.server.structure.StructureManagementException;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * This class provides a dialog to update treatments.
 */
public class TreatmentUpdateDialog extends JDialog {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -8947312817509913541L;

	/**
	 * The path where the classes implementing the treatments are
	 */
	private static final String EXP_IMPLEMENTATION_PATH = "edu.kit.exp.impl";
	private static final String DOT = ".";
	private static final String DESCRIPTION_LABEL = "Description:";
	private static final String TREATMENT_ENVIRONMENT_LABEL = "Treatment Environment:";
	private static final String TREATMENT_NAME_LABEL = "Treatment Name:";
	private static final String TREATMENT_INSTITUTION_LABEL = "Treatment Institution:";
	private static final String OK_BUTTON_LABEL = "OK";

	/**
	 * The gui controller.
	 */
	private TreatmentManagementDialogController guiController = TreatmentManagementDialogController.getInstance();

	/**
	 * The text field name.
	 */
	private JTextField textFieldTreatmentName;

	/**
	 * The combo box institution key.
	 */
	private JComboBox<String> comboBoxInstitutionKey;

	/**
	 * The combo box environment key.
	 */
	private JComboBox<String> comboBoxEnvironmentKey;

	/**
	 * The editor pane.
	 */
	private JEditorPane editorPane;

	/**
	 * The updated treatment.
	 */
	private Treatment updatedTreatment;

	/**
	 * This constructor instantiates a new treatment update dialog.
	 *
	 * @param treatment A {@link edu.kit.exp.server.jpa.entity.Treatment Treatment}
	 *                  variable which contains the treatment to be updated.
	 * @param title     The String title of the dialog frame.
	 */
	public TreatmentUpdateDialog(Treatment treatment, String title) {
		List<String> environmentKeys = null;
		List<String> institutionKeys = null;
		try {
			environmentKeys = ReflectionPackageManager.getExtendingClassNames(EXP_IMPLEMENTATION_PATH, Environment.class);
			institutionKeys = ReflectionPackageManager.getExtendingClassNames(EXP_IMPLEMENTATION_PATH, Institution.class);
		} catch (URISyntaxException | IOException | ClassNotFoundException e) {
			LogHandler.printException(e);
		}

		this.updatedTreatment = treatment;
		this.setBounds(50, 50, 650, 250);
		this.setLocationRelativeTo(this.getParent());
		this.setModal(true);
		this.setTitle(title);

		JPanel inputPanel = new JPanel();
		getContentPane().add(inputPanel, BorderLayout.CENTER);
		inputPanel.setLayout(new MigLayout("", "[][grow]", "[][][][grow]"));

		JLabel lblTreatmentName = new JLabel(TREATMENT_NAME_LABEL);
		inputPanel.add(lblTreatmentName, "cell 0 0,alignx trailing");

		textFieldTreatmentName = new JTextField(treatment.getName());
		inputPanel.add(textFieldTreatmentName, "cell 1 0,growx");
		textFieldTreatmentName.setColumns(10);
		textFieldTreatmentName.addKeyListener(new KeyAdapter() {

			@Override public void keyReleased(KeyEvent event) {
				int keyCode = event.getKeyCode();
				if (keyCode == KeyEvent.VK_ENTER) {
					try {
						updateTreatmentWithActualInput();
					} catch (StructureManagementException | DataInputException e) {
						LogHandler.printException(e);
					}
				}
			}
		});

		JLabel lblInstitutionFactoryKey = new JLabel(TREATMENT_INSTITUTION_LABEL);
		inputPanel.add(lblInstitutionFactoryKey, "cell 0 1,alignx trailing");

		comboBoxInstitutionKey = new JComboBox<String>();
		comboBoxInstitutionKey.setEditable(true);
		for (String institutionKey : institutionKeys) {
			comboBoxInstitutionKey.addItem(institutionKey);
		}
		if (treatment.getInstitutionFactoryKey() != null) {
			comboBoxInstitutionKey.setSelectedItem(treatment.getInstitutionFactoryKey().substring(EXP_IMPLEMENTATION_PATH.length() + 1));
		}
		inputPanel.add(comboBoxInstitutionKey, "cell 1 1,growx");

		JLabel lblEnvironmentFactoryKey = new JLabel(TREATMENT_ENVIRONMENT_LABEL);
		inputPanel.add(lblEnvironmentFactoryKey, "cell 0 2,alignx trailing");

		comboBoxEnvironmentKey = new JComboBox<>();
		comboBoxEnvironmentKey.setEditable(true);
		for (String environmentKey : environmentKeys) {
			comboBoxEnvironmentKey.addItem(environmentKey);
		}
		if (treatment.getEnvironmentFactoryKey() != null) {
			comboBoxEnvironmentKey.setSelectedItem(treatment.getEnvironmentFactoryKey().substring(EXP_IMPLEMENTATION_PATH.length() + 1));
		}
		inputPanel.add(comboBoxEnvironmentKey, "cell 1 2,growx");

		JLabel lblDescription = new JLabel(DESCRIPTION_LABEL);
		inputPanel.add(lblDescription, "cell 0 3");

		editorPane = new JEditorPane();
		editorPane.setText(treatment.getDescription());
		inputPanel.add(editorPane, "cell 1 3,grow");
		editorPane.addKeyListener(new KeyAdapter() {
			@Override public void keyReleased(KeyEvent event) {
				int keyCode = event.getKeyCode();
				if (keyCode == KeyEvent.VK_ENTER) {
					try {
						updateTreatmentWithActualInput();
					} catch (StructureManagementException | DataInputException e) {
						LogHandler.printException(e);
					}
				}
			}
		});

		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		JButton btnOK = new JButton(OK_BUTTON_LABEL);
		btnOK.addActionListener(arg0 -> {
			try {
				updateTreatmentWithActualInput();
			} catch (StructureManagementException | DataInputException e) {
				LogHandler.printException(e);
			}
		});
		buttonPanel.add(btnOK);
	}

	private void updateTreatmentWithActualInput() throws StructureManagementException, DataInputException {
		updatedTreatment.setDescription(editorPane.getText());
		updatedTreatment.setEnvironmentFactoryKey(EXP_IMPLEMENTATION_PATH + DOT + comboBoxEnvironmentKey.getSelectedItem().toString());
		updatedTreatment.setInstitutionFactoryKey(EXP_IMPLEMENTATION_PATH + DOT + comboBoxInstitutionKey.getSelectedItem().toString());
		updatedTreatment.setName(textFieldTreatmentName.getText());

		if (updatedTreatment.getIdTreatment() == null) {

			try {
				guiController.createTreatment(updatedTreatment);
				dispose();
			} catch (StructureManagementException | DataInputException e) {
				JOptionPane.showMessageDialog(TreatmentManagementDialog.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				updatedTreatment.setIdTreatment(null);
			}

		} else {

			try {
				guiController.updateTreatment(updatedTreatment);
				dispose();
			} catch (StructureManagementException | DataInputException e) {
				JOptionPane.showMessageDialog(TreatmentManagementDialog.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				LogHandler.printException(e);
			}
		}
	}
}
