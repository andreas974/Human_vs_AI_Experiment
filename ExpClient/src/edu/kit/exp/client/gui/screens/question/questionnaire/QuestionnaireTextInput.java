package edu.kit.exp.client.gui.screens.question.questionnaire;

import javax.swing.*;
import java.awt.*;

/**
 * Created by tondaroder on 03.02.17.
 */

public class QuestionnaireTextInput extends QuestionnaireItem {

	public static final Font FONT = new Font("Tahoma", Font.PLAIN, 15);
	private JTextField inputField;
	private double minimumLength = 1;
	private Dimension preferredSize = new Dimension(600, 100);

	/**
	 * This constructor instantiates a new number input quiz item.
	 *
	 * @param question
	 *            A String which contains the question to be shown to a client.
	 */
	public QuestionnaireTextInput(String question) {
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
		answerListPanel.setPreferredSize(preferredSize);
		answerListPanel.setMinimumSize(preferredSize);
		answerListPanel.setMaximumSize(preferredSize);

		inputField = new JTextField();
		inputField.setFont(FONT);
		inputField.setHorizontalAlignment(JTextField.CENTER);
		answerListPanel.add(inputField);

		return returnPanel;
	}

	@Override
	public boolean isValid() {
		String inputText = inputField.getText();
		return (inputText.isEmpty() == false && inputText.length() >= minimumLength);
	}

	@Override
	public String getAnswer() {
		return inputField.getText();
	}

	public void setMinimumLength(double minimumLength) {
		this.minimumLength = minimumLength;
	}

	public void setTextFieldSize(int width, int height) {
		preferredSize = new Dimension(width, height);
	}

}
