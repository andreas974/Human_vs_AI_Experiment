package edu.kit.exp.server.gui.structuretab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.gui.treatment.TreatmentDrawType;
import edu.kit.exp.server.gui.treatment.TreatmentManagementDialogController;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;
import edu.kit.exp.server.structure.StructureManagementException;
import net.miginfocom.swing.MigLayout;

/**
 * This class generates a container for editing treatment blocks.</br>
 * It is a child panel of the structure tab and allows users to edit the details
 * of a treatment block.</br>
 * Treatment blocks can be added to the structure tree for sessions.
 * 
 * @see StructureTab
 * @see StructureTreeBuilder
 */
public class TreatmentBlockDetails extends JPanel implements Observer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6433621850611921076L;
	public static final String HEADING_FONT = "Tahoma";
	public static final String MARK_PRACTICE_LABEL = "Mark as practice treatment block.";
	public static final String BIOFEEDBACK_LABEL = "Biofeedback:";
	public static final String HEADING_LABEL = "Details of Treatment Block";
	public static final String NAME_LABEL = "Name:";
	public static final String ID_LABEL = "ID:";
	public static final String CHECKBOX_MATCH_RANDOM = "Match completely random";
	public static final String CHECKBOX_MATCH_FACTORIAL = "Match factorial";
	public static final String DESCRIPTION_LABEL = "Description:";
	public static final String APPLY_CHANGES_BUTTON_LABEL = "Apply Changes";
	public static final String SEQUENCE_NUMBER_LABEL = "Sequence Number:";
	public static final String PRACTICE_LABEL = "Practice:";
	public static final String TREATMENTS_LABEL = "Treatments:";
	public static final String ADD_REMOVE_BUTTON_LABEL = "Add / Remove";

	/** The text field name. */
	private JTextField textFieldName;

	/** The gui controller. */
	private StructureTabController guiController = StructureTabController.getInstance();

	/** The treatment dialog controler. */
	private TreatmentManagementDialogController treatmentDialogController = TreatmentManagementDialogController.getInstance();

	/** The updated treatment block. */
	private TreatmentBlock updatedTreatmentBlock;

	/** The editor pane. */
	private JEditorPane editorPane;

	/** The check box practice. */
	private JCheckBox chckbxPractice;

	/** The check box cr. */
	private JCheckBox checkBoxCR;

	/** The check box factorial. */
	private JCheckBox checkBoxFactorial;
	private final JPanel inputPanel;

	/**
	 * This constructor instantiates a new JPanel that shows the details of a
	 * treatment block. All sessions are persisted on the server side.
	 * 
	 * @param treatmentBlock
	 *            A TreatmentBlock variable that contains informations about a
	 *            treatment block.
	 */
	public TreatmentBlockDetails(TreatmentBlock treatmentBlock) {

		updatedTreatmentBlock = treatmentBlock;
		treatmentDialogController.addObserver(this);

		try {
			treatmentDialogController.automaticallyLoadTreatments();
		} catch (StructureManagementException | DataInputException exception) {
			JOptionPane.showMessageDialog(StructureTab.getInstance(), exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		setBorder(new EmptyBorder(10, 3, 10, 3));
		setBackground(UIManager.getColor("Button.background"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		inputPanel = new JPanel();
		panel.add(inputPanel);
		inputPanel.setBackground(UIManager.getColor("Button.background"));
		inputPanel.setLayout(new MigLayout("", "[][grow][]", "[][][][][][][][][][][grow][][]"));

		JLabel labelHeading = new JLabel(HEADING_LABEL);
		panel.add(labelHeading, BorderLayout.NORTH);
		labelHeading.setHorizontalAlignment(SwingConstants.LEFT);

		labelHeading.setFont(new Font(HEADING_FONT, Font.BOLD, 14));

		initializeIdRow(treatmentBlock, inputPanel);

		initializeNameRow(treatmentBlock, inputPanel);

		initializeSequenceNumberRow(treatmentBlock, inputPanel);

		initializePracticeRow(treatmentBlock, inputPanel);

		initializeTreatmentRow(treatmentBlock, inputPanel);

		initializeMatchPanel(treatmentBlock, inputPanel);

		initializeDescriptionRow(treatmentBlock, inputPanel);

		initializeButtonPanel(panel);

	}

	private void initializeNameRow(TreatmentBlock treatmentBlock, JPanel inputPanel) {
		JLabel lblExperimentName = new JLabel(NAME_LABEL);
		inputPanel.add(lblExperimentName, "cell 0 1,alignx left");

		textFieldName = new JTextField(treatmentBlock.getName());
		inputPanel.add(textFieldName, "cell 1 1,growx");
		textFieldName.setColumns(10);
	}

	private void initializeIdRow(TreatmentBlock treatmentBlock, JPanel inputPanel) {
		JLabel labelId = new JLabel(ID_LABEL);
		inputPanel.add(labelId, "cell 0 0");

		JLabel labelIdInfo = new JLabel(treatmentBlock.getIdsequenceElement().toString());
		inputPanel.add(labelIdInfo, "cell 1 0");
	}

	private void initializeMatchPanel(TreatmentBlock treatmentBlock, JPanel inputPanel) {
		checkBoxCR = new JCheckBox(CHECKBOX_MATCH_RANDOM);
		if (treatmentBlock.getRandomization() != null && treatmentBlock.getRandomization().equals(TreatmentDrawType.DRAW_WITH_REPLACEMENT.toString())) {
			checkBoxCR.setSelected(true);

		}
		inputPanel.add(checkBoxCR, "cell 1 5");

		checkBoxFactorial = new JCheckBox(CHECKBOX_MATCH_FACTORIAL);
		if (treatmentBlock.getRandomization() != null && treatmentBlock.getRandomization().equals(TreatmentDrawType.DRAW_WITHOUT_REPLACEMENT.toString())) {
			checkBoxFactorial.setSelected(true);

		}
		inputPanel.add(checkBoxFactorial, "cell 1 6");
	}

	private void initializeDescriptionRow(TreatmentBlock treatmentBlock, JPanel inputPanel) {
		JLabel lblDescription = new JLabel(DESCRIPTION_LABEL);
		inputPanel.add(lblDescription, "cell 0 10");

		editorPane = new JEditorPane();
		editorPane.setBackground(Color.WHITE);
		editorPane.setText(treatmentBlock.getDescription());
		inputPanel.add(editorPane, "cell 1 10 2 3,grow");
	}

	private void initializeButtonPanel(JPanel panel) {
		JPanel ButtonPanel = new JPanel();
		panel.add(ButtonPanel, BorderLayout.SOUTH);

		JButton buttonApplyChanges = new JButton(APPLY_CHANGES_BUTTON_LABEL);
		buttonApplyChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				updatedTreatmentBlock.setDescription(editorPane.getText());
				updatedTreatmentBlock.setName(textFieldName.getText());

				if (checkBoxCR.isSelected()) {
					updatedTreatmentBlock.setRandomization(TreatmentDrawType.DRAW_WITH_REPLACEMENT.toString());
				} else {

					if (checkBoxFactorial.isSelected()) {
						updatedTreatmentBlock.setRandomization(TreatmentDrawType.DRAW_WITHOUT_REPLACEMENT.toString());
					} else {
						updatedTreatmentBlock.setRandomization(null);
					}

				}

				updatedTreatmentBlock.setPractice(chckbxPractice.isSelected());

				try {
					guiController.updateSequenceElement(updatedTreatmentBlock);
				} catch (StructureManagementException e1) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (DataInputException e1) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		ButtonPanel.add(buttonApplyChanges);
	}

	private void initializeSequenceNumberRow(TreatmentBlock treatmentBlock, JPanel inputPanel) {
		JLabel labelSequenceNumber = new JLabel(SEQUENCE_NUMBER_LABEL);
		inputPanel.add(labelSequenceNumber, "cell 0 2,alignx left");

        JLabel labelSequenceNumberInfo = new JLabel(treatmentBlock.getSequenceNumber().toString());
        inputPanel.add(labelSequenceNumberInfo, "cell 1 2");
	}

	private void initializePracticeRow(TreatmentBlock treatmentBlock, JPanel inputPanel) {
		JLabel lblPractice = new JLabel(PRACTICE_LABEL);
		inputPanel.add(lblPractice, "cell 0 3");

		chckbxPractice = new JCheckBox(MARK_PRACTICE_LABEL);
		chckbxPractice.setSelected(treatmentBlock.getPractice());
		inputPanel.add(chckbxPractice, "cell 1 3");
	}

	private void initializeTreatmentRow(TreatmentBlock treatmentBlock, JPanel inputPanel) {
		JLabel labelTreatments = new JLabel(TREATMENTS_LABEL);
		inputPanel.add(labelTreatments, "cell 0 4,alignx left");

		JButton btnNewButton = new JButton(ADD_REMOVE_BUTTON_LABEL);
		btnNewButton.addActionListener(arg0 -> {

			try {
				guiController.showTreatmentDialog();
			} catch (StructureManagementException | DataInputException e) {
                LogHandler.printException(e);
			}

		});

		JPanel treatmentPanel = new JPanel();
		treatmentPanel.setLayout(new BorderLayout());
		treatmentPanel.add(btnNewButton, BorderLayout.CENTER);
		JList<Treatment> treatmentNames = new JList<>(treatmentBlock.getTreatments().toArray(new Treatment[0]));
		treatmentNames.setLayoutOrientation(JList.VERTICAL);
		treatmentNames.setVisibleRowCount(5);
		JScrollPane scroller = new JScrollPane(treatmentNames);
		treatmentPanel.add(scroller, BorderLayout.SOUTH);
		inputPanel.add(treatmentPanel, "cell 1 4,growx");
	}

	@Override
	public void update(Observable o, Object arg) {

	}

}
