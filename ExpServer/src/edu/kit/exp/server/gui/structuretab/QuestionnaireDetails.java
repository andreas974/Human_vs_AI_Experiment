package edu.kit.exp.server.gui.structuretab;

import edu.kit.exp.client.gui.screens.question.questionnaire.QuestionnaireScreen;
import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.ReflectionPackageManager;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.jpa.entity.Questionnaire;
import edu.kit.exp.server.structure.StructureManagementException;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * This class generates a container for editing quizzes.</br> It is a child
 * panel of the structure tab and allows users to edit the details of the
 * quizzes.</br> Questionnaires can be added to the structure tree for sessions.
 *
 * @see StructureTab
 * @see StructureTreeBuilder
 */
public class QuestionnaireDetails extends JPanel {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -6433621850611921076L;

	private static final String EXP_IMPLEMENTATION_PATH = "edu.kit.exp.impl";

	/**
	 * The gui controller.
	 */
	private StructureTabController guiController = StructureTabController.getInstance();

	/**
	 * The text field factory key.
	 */
	private JComboBox<String> textQuestionnaireFactoryKey;

	/**
	 * The updated quiz.
	 */
	private Questionnaire updatedQuestionnaire;

	/**
	 * This constructor instantiates a new JPanel that shows the details of a
	 * quiz. All quizzes are persisted on the server side.
	 *
	 * @param questionnaire A Questionnaire variable which contains a certain quiz.
	 */
	public QuestionnaireDetails(Questionnaire questionnaire) {

		this.updatedQuestionnaire = questionnaire;

		setBorder(new EmptyBorder(10, 3, 10, 3));
		setBackground(UIManager.getColor("Button.background"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel inputPanel = new JPanel();
		panel.add(inputPanel);
		inputPanel.setBackground(UIManager.getColor("Button.background"));
		inputPanel.setLayout(new MigLayout("", "[][grow]", "[][][][grow]"));

		JLabel labelId = new JLabel("ID:");
		inputPanel.add(labelId, "cell 0 0");

		JLabel labelIdInfo = new JLabel(questionnaire.getIdsequenceElement().toString());
		inputPanel.add(labelIdInfo, "cell 1 0");

		JLabel lblSequenceNumber = new JLabel("Sequence Number:");
		inputPanel.add(lblSequenceNumber, "cell 0 1,alignx trailing");

		JLabel labelSequenceNumberInfo = new JLabel(questionnaire.getSequenceNumber().toString());
		inputPanel.add(labelSequenceNumberInfo, "cell 1 1");

		JLabel lblFactoryKey = new JLabel("Quest. Class:");
		inputPanel.add(lblFactoryKey, "cell 0 2,alignx left");

		textQuestionnaireFactoryKey = new JComboBox<>();
		textQuestionnaireFactoryKey.addItem(questionnaire.getQuestionnaireFactoryKey());
		List<Class<Object>> extendingClasses = null;
		try {
			extendingClasses = ReflectionPackageManager.getExtendingClasses(EXP_IMPLEMENTATION_PATH, QuestionnaireScreen.class);
		} catch (URISyntaxException | IOException | ClassNotFoundException e) {
			LogHandler.printException(e);
		}
		for (Class<Object> element : extendingClasses) {
			if (!element.getName().equals(questionnaire.getQuestionnaireFactoryKey())) {
				textQuestionnaireFactoryKey.addItem(element.getName());
			}
		}
		textQuestionnaireFactoryKey.setMaximumSize(new Dimension(300, textQuestionnaireFactoryKey.getPreferredSize().height));
		textQuestionnaireFactoryKey.setEditable(true);
		inputPanel.add(textQuestionnaireFactoryKey, "cell 1 2");

		JLabel labelHeading = new JLabel("Details of Questionnaire");
		panel.add(labelHeading, BorderLayout.NORTH);
		labelHeading.setHorizontalAlignment(SwingConstants.LEFT);
		labelHeading.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel buttonPanel = new JPanel();
		panel.add(buttonPanel, BorderLayout.SOUTH);

		JButton buttonApplyChanges = new JButton("Apply Changes");
		buttonApplyChanges.addActionListener(new ActionListener() {

			@Override public void actionPerformed(ActionEvent arg0) {

				updatedQuestionnaire.setQuestionnaireFactoryKey(textQuestionnaireFactoryKey.getSelectedItem().toString());

				try {
					guiController.updateSequenceElement(updatedQuestionnaire);
				} catch (StructureManagementException | DataInputException e1) {
					JOptionPane.showMessageDialog(null, e1, "Error", JOptionPane.ERROR_MESSAGE);
					LogHandler.printException(e1, "Could not update questionnaire");
				}
			}
		});
		buttonPanel.add(buttonApplyChanges);

	}
}
