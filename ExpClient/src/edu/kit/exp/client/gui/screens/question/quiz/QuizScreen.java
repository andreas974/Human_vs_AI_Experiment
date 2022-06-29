package edu.kit.exp.client.gui.screens.question.quiz;

import edu.kit.exp.client.gui.ClientGuiController;
import edu.kit.exp.client.gui.MainFrame;
import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.client.gui.screens.question.QuestionScreen;

import javax.swing.*;
import java.util.Date;

/**
 * The class QuizScreen provides a <code>Screen</code> for displaying quizzes to
 * clients.
 * 
 * @see Screen
 */
public class QuizScreen extends QuestionScreen {

	private static final String TITLE_TEXT = "Quiz";
	private static final String PRE_TEXT = "Click the button to start the quiz.";
	private static final String POST_TEXT = "You completed the quiz. Click the button to continue the experiment.";
	private static final String WRONG_ANSWER_TEXT = "The answer is not correct. Please check instructions again.";

	/** The current quiz. */
	private QuizItem currentItem;

	/** The total quiz time start. */
	private long totalQuizTimeStart = 0;

	/** The total quiz time end. */
	private long totalQuizTimeEnd = 0;

	/** The total error count. */
	private int totalErrorCount = 0;

	/**
	 * This constructor instantiates a new quiz screen.
	 *
	 * @param gameId     A String which contains the ID of the running game.
	 * @param parameter  A ParamObject list of all parameters used in this screen.
	 * @param screenId   A String which contains the global screen ID of this screen.
	 * @param showUpTime A Long which indicates how long the Screen will be shown to a
	 */
	public QuizScreen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);
		setTitleText(TITLE_TEXT);
		setPreText(PRE_TEXT);
		setPostText(POST_TEXT);
		setWrongAnswerText(WRONG_ANSWER_TEXT);
	}

	/**
	 * Show next question.
	 */
	public void showNextQuestion() {
		if (currentQuestion == questionItems.size() - 1 && (currentItem == null || currentItem.isValid()) && !(mainFrame.getClientId().startsWith("agent"))) {
			showQuizOverInfo();
		} else if (currentQuestion >= questionItems.size() || mainFrame.getClientId().startsWith("agent")) {
			exitQuiz();
		} else {
			if (currentItem == null || currentItem.isValid()) {
				receiveCorrectAnswerAndShowNextQuestion();
			} else {
				receiveWrongAnswer();
			}
		}
		updateTitle();
	}

	private void showQuizOverInfo() {
		totalQuizTimeEnd = new Date().getTime();
		questionText.setText("<html><center>" + postText + "</center></html>");
		resetAnswerPanel();
		currentQuestion++;
	}

	private void exitQuiz() {
		ClientGuiController.getInstance().sendQuizProtocolAndWait(true, totalQuizTimeStart + "," + totalQuizTimeEnd + "," + totalErrorCount);
	}

	private void receiveCorrectAnswerAndShowNextQuestion() {
		if (currentQuestion == -1) {
			// Start time with first question
			totalQuizTimeStart = new Date().getTime();
		}

		currentQuestion++;
		currentItem = (QuizItem) questionItems.get(currentQuestion);
		questionText.setText("<html><center>" + currentItem.getQuestion() + "</center></html>");
		resetAnswerPanel();
		answerPanel.add(currentItem.getAnswerPanel());
	}

	private void receiveWrongAnswer() {
		totalErrorCount++;
		JDialog dialog = new JOptionPane(wrongAnswerText, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION).createDialog("Advise");
		dialog.setLocationRelativeTo(this);
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
		dialog.dispose();
	}

	/**
	 * This method adds a quiz item.
	 * 
	 * @param item
	 *            The QuizItem to be added.
	 */
	protected void addQuizItem(QuizItem item) {
		questionItems.add(item);
	}

}
