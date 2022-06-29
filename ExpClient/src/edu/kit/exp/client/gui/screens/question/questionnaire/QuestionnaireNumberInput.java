package edu.kit.exp.client.gui.screens.question.questionnaire;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * The class QuestionnaireItemNumberInput provides a QuestionnaireItem where clients can enter
 * numbers as an answer.
 *
 * @author tonda roder
 */
public class QuestionnaireNumberInput extends QuestionnaireItem {

	public static final String PATTERN_FOR_VALIDATION = "[\\d]+([\\.,][\\d]+)?";
	public static final Font FONT = new Font("Tahoma", Font.PLAIN, 17);
	public static final Dimension PREFERRED_SIZE = new Dimension(300, 50);
	/** The input field. */
	private JTextField inputField;

	/**
	 * This constructor instantiates a new number input quiz item.
	 *
	 * @param question
	 *            A String which contains the question to be shown to a client.
	 */
	public QuestionnaireNumberInput(String question) {
		super(question);

	}

	@Override
	public JPanel getAnswerPanel() {
		JPanel returnPanel = basePanel;
		returnPanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));

		JPanel answerListPanel = new JPanel();
		returnPanel.add(answerListPanel);
		answerListPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		answerListPanel.setBackground(returnPanel.getBackground());
		answerListPanel.setLayout(new BoxLayout(answerListPanel, BoxLayout.Y_AXIS));
		answerListPanel.setPreferredSize(PREFERRED_SIZE);
		answerListPanel.setMinimumSize(PREFERRED_SIZE);
		answerListPanel.setMaximumSize(PREFERRED_SIZE);

		inputField = new JTextField();
		inputField.setFont(FONT);
		inputField.setHorizontalAlignment(JTextField.CENTER);
		answerListPanel.add(inputField);

		return returnPanel;
	}

	@Override
	public boolean isValid() {
		Pattern pattern = Pattern.compile(PATTERN_FOR_VALIDATION);
		String inputText = inputField.getText();
		return (inputText.isEmpty() == false && pattern.matcher(inputText).matches());
	}

	@Override
	public String getAnswer() {
		return inputField.getText();
	}

}
