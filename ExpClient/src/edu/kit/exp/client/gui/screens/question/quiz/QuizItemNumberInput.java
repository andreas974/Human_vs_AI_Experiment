package edu.kit.exp.client.gui.screens.question.quiz;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * The class QuizItemNumberInput provides a QuizItem where clients can enter
 * numbers as an answer.
 * 
 * @see QuizItem
 */
public class QuizItemNumberInput extends QuizItem {

	public static final Font FONT = new Font("Tahoma", Font.PLAIN, 17);
	public static final Dimension PREFERRED_SIZE = new Dimension(300, 50);
	/** The input field. */
	private JTextField inputField;

	/** The answer. */
	private double answer;

	/**
	 * This method gets the answer.
	 * 
	 * @return the answer
	 */
	public double getAnswer() {
		return answer;
	}

	/**
	 * This method sets the answer.
	 * 
	 * @param answer
	 *            A double which contains the answer.
	 */
	public void setAnswer(double answer) {
		this.answer = answer;
	}

	/**
	 * This constructor instantiates a new number input quiz item.
	 * 
	 * @param question
	 *            A String which contains the question to be shown to a client.
	 * @param answer
	 *            A double which contains the number the client answered.
	 */
	public QuizItemNumberInput(String question, double answer) {
		super(question);

		this.answer = answer;
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
		try {
			Pattern pattern = Pattern.compile("[\\d]+([\\.,][\\d]+)?");
			if (pattern.matcher(inputField.getText()).matches()) {
				return Double.valueOf(inputField.getText().replace(",", ".")).equals(this.answer);
			}
		} catch (Exception e) {
		}
		return false;
	}

}
