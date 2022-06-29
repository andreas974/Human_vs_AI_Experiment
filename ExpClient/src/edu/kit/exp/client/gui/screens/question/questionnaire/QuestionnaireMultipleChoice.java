package edu.kit.exp.client.gui.screens.question.questionnaire;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * The Class QuestionnaireItemMultipleChoice represents an implementation of a QuestionnaireItem;
 * a multiple choice question.
 *
 * @author tonda roder
 */
public class QuestionnaireMultipleChoice extends QuestionnaireItem {

	private static final Font ANSWER_FONT = new Font("Tahoma", Font.PLAIN, 17);
	public static final Dimension PREFERRED_SIZE = new Dimension(300, 200);
	public static final String SPLIT_SIGN = ";";

	/** The answer text. */
	private ArrayList<String> answerText;

	/** The answer option. */
	private ArrayList<AbstractButton> answerOption;

	/** Indicates whether multiple answers where selected. */
	private boolean selectMultiple;

	private ArrayList<String> Text;

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
	public QuestionnaireMultipleChoice(String question) {
		super(question);

		answerText = new ArrayList<>();
		selectMultiple = false;
		Text = new ArrayList<>();
	}


	/**
	 * This method provides the experimenter with the option to add possible
	 * answers for the question.
	 *
	 * @param text
	 *            A String which contains the client answer.
	 */
	public void addAnswer(String text) {
		answerText.add(text);
	}

	public void addText(String text) {
		Text.add(text);
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

		for (String anAnswerText : answerText) {
			if (selectMultiple) {
				answer = new JCheckBox(anAnswerText, false);
			} else {
				answer = new JRadioButton(anAnswerText, false);
				answerGroup.add(answer);
			}
			answer.setFont(ANSWER_FONT);
			answerOption.add(answer);

			answer.setBackground(returnPanel.getBackground());
			answerListPanel.add(answer);
		}

		return returnPanel;
	}

	@Override
	public boolean isValid() {
		for (AbstractButton anAnswerOption : answerOption) {
			if (anAnswerOption.isSelected()) {
				return true;
			}
		}
		return false;
	}

	@Override public String getAnswer() {
		String answer = "";
		int selectedCounter = 0;
		for (AbstractButton anAnswerOption : answerOption) {
			if (anAnswerOption.isSelected()) {
				if (selectedCounter == 0) {
					answer = anAnswerOption.getText();
				} else {
					answer += (SPLIT_SIGN + anAnswerOption.getText());
				}
				selectedCounter++;
			}
		}
		return answer;
	}

}
