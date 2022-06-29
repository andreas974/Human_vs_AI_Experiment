package edu.kit.exp.client.gui.screens.question.questionnaire;

import edu.kit.exp.client.gui.screens.question.QuestionItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by tondaroder on 30.01.17.
 *
 */
public abstract class QuestionnaireItem implements QuestionItem {
	/** The question. */
	private String question;

	/** The base panel. */
	protected JPanel basePanel;

	/** answer to the question **/
	private String answer;

	/**
	 * This method gets the question.
	 *
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * This method sets the question.
	 *
	 * @param question
	 *            A String which contains the new question.
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * This constructor instantiates a new quiz item by setting a question and
	 * creating a panel.
	 *
	 * @param question
	 *            the question
	 */
	public QuestionnaireItem(String question) {
		this.question = question;

		basePanel = new JPanel();
		basePanel.setBackground(Color.WHITE);
		basePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
	}

	/**
	 * This method gets the answer panel where a client can enter his answer.
	 *
	 * @return the answer panel
	 */
	public abstract JPanel getAnswerPanel();

	/**
	 * This method checks if the client answer is valid.
	 *
	 * @return true, if the answer is valid
	 */
	public abstract boolean isValid();

	public abstract String getAnswer();


}
