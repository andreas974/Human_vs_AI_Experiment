package edu.kit.exp.client.gui.screens.question.quiz;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The Class QuizItemMultipleChoice represents an implementation of a QuizItem;
 * a multiple choice question.
 * 
 * @see QuizItem
 */
public class QuizItemMultipleChoice extends QuizItem {

	public static final Font FONT = new Font("Tahoma", Font.PLAIN, 17);
	public static final Dimension PREFERRED_SIZE = new Dimension(300, 200);
	/** The answer text. */
	private ArrayList<String> answerText;

	/** The answer valid. */
	private ArrayList<Boolean> answerValid;

	/** The answer option. */
	private ArrayList<AbstractButton> answerOption;

	/** Indicates whether multiple answers where selected. */
	private boolean selectMultiple;

	/**
	 * This method checks if multiple answers can be selected.
	 * 
	 * @return true, if multiple answers can be selected.
	 */
	public boolean isSelectMultiple() {
		return selectMultiple;
	}

	/**
	 * This method sets a true or false value that indicates whether multiple
	 * answers can be selected in a quiz.
	 * 
	 * @param selectMultiple
	 *            A boolean which regulates whether multiple or just one answer
	 *            can be selected.
	 */
	public void setSelectMultiple(boolean selectMultiple) {
		this.selectMultiple = selectMultiple;
	}

	/**
	 * This constructor instantiates a new multiple choice quiz item.
	 * 
	 * @param question
	 *            A String which contains the question that will be displayed to
	 *            the client.
	 */
	public QuizItemMultipleChoice(String question) {
		super(question);

		answerText = new ArrayList<>();
		answerValid = new ArrayList<>();
		selectMultiple = false;
	}

	/**
	 * This method provides the experimenter with the option to add possible
	 * answers for the question.
	 * 
	 * @param text
	 *            A String which contains the client answer.
	 */
	public void addAnswer(String text) {
		addAnswer(text, false);
	}

	/**
	 * This method provides the experimenter with the option to add possible
	 * answers for the question.</br> Additionally, it provides the parameter
	 * isCorrect, so an experiment creator can indicated if this possibility is
	 * right or wrong.
	 * 
	 * @param text
	 *            A String which contains the client answer.
	 * @param isCorrect
	 *            A Boolean which shows if the answer is correct or not.
	 */
	public void addAnswer(String text, boolean isCorrect) {
		answerText.add(text);
		answerValid.add(isCorrect);
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

		answerOption = new ArrayList<>();
		AbstractButton answer;
		ButtonGroup answerGroup = new ButtonGroup();

		int validAnswerCount = 0;
		for (int i = 0; i < answerValid.size(); i++) {
			if (answerValid.get(i)) {
				validAnswerCount++;
			}
		}
		boolean multipleCorrectAnswers = validAnswerCount > 1;

		for (int i = 0; i < answerText.size(); i++) {
			if (multipleCorrectAnswers || selectMultiple) {
				answer = new JCheckBox(answerText.get(i), false);
			} else {
				answer = new JRadioButton(answerText.get(i), false);
				answerGroup.add(answer);
			}
			answer.setFont(FONT);
			answerOption.add(answer);

			answer.setBackground(returnPanel.getBackground());
			answerListPanel.add(answer);
		}

		return returnPanel;
	}

	@Override
	public boolean isValid() {
		for (int i = 0; i < answerOption.size(); i++) {
			if (answerOption.get(i).isSelected() != answerValid.get(i)) {
				return false;
			}
		}
		return true;
	}
}
