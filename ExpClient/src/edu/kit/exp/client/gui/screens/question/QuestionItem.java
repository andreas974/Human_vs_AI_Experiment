package edu.kit.exp.client.gui.screens.question;

import javax.swing.*;

/**
 * The interface QuestionItem represents all question items that can be displayed to a
 * client. All new quiz and questionnaire items have to implement this interface (indirectly).
 */
public interface QuestionItem {

	/** return the question that should be asked as a String */
	String getQuestion();

	/** returns the possible answers / text field as a JPanel */
	JPanel getAnswerPanel();

	/** returns if a possible input is valid or not */
	boolean isValid();

}
