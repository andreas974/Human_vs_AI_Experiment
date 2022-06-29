package edu.kit.exp.client.gui.screens.question.questionnaire;

import edu.kit.exp.client.gui.ClientGuiController;
import edu.kit.exp.client.gui.screens.question.QuestionScreen;

import javax.swing.*;
import java.util.Date;

/**
 * Created by tondaroder on 30.01.17.
 */
public class QuestionnaireScreen extends QuestionScreen {

	private static final String TITLE_TEXT = "Fragebogen";
	private static final String PRE_TEXT = "Klicken Sie den Button, um mit dem Fragebogen zu starten.";
	private static final String POST_TEXT = "Sie haben den Fragebogen abgeschlossen. Klicken Sie den Button, um das Experiment zu beenden.";
	private static final String WRONG_ANSWER_TEXT = "Wert ungültig";

	/** The current questionnaire. */
	private QuestionnaireItem currentQuestionnaire;

	/** The total questionnaire time start. */
	private long questionTimeStart = 0;

	/** The total questionnaire time end. */
	private long questionTimeEnd = 0;

	/**
	 * This constructor instantiates a new questionnaire screen.
	 *
	 * @param gameId     A String which contains the ID of the running game.
	 * @param parameter  A ParamObject list of all parameters used in this screen.
	 * @param screenId   A String which contains the global screen ID of this screen.
	 * @param showUpTime A Long which indicates how long the Screen will be shown to a
	 */
	public QuestionnaireScreen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
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
		boolean isLastQuestion = currentQuestion == questionItems.size() - 1;
		boolean questionnaireIsValid = currentQuestionnaire == null || currentQuestionnaire.isValid();
		if (isLastQuestion && questionnaireIsValid && !(mainFrame.getClientId().startsWith("agent"))) {
			receiveCorrectAnswerAndShowInfo();
		} else {
			boolean isInfoScreen = currentQuestion == questionItems.size();
			if (isInfoScreen || mainFrame.getClientId().startsWith("agent")) {
				exitQuiz();
			} else {
				if (questionnaireIsValid) {
					receiveCorrectAnswerAndShowNextQuestion();
				} else {
					receiveWrongAnswer();
				}
			}
		}
		updateTitle();
	}

	private void exitQuiz() {
		ClientGuiController.getInstance().sendQuestionnaireProtocolAndWait(true, null, null, 0L);
	}

	private void receiveCorrectAnswerAndShowInfo() {
		questionText.setText("<html><center>" + postText + "</center></html>");
		resetAnswerPanel();
		currentQuestion++;
		QuestionnaireItem questionnaireItem = (QuestionnaireItem) questionItems.get(questionItems.size() - 1);
		String answer = questionnaireItem.getAnswer();
		questionTimeEnd = new Date().getTime();
		ClientGuiController.getInstance().sendQuestionnaireProtocolAndWait(false, currentQuestionnaire.getQuestion(), answer, questionTimeEnd - questionTimeStart);
	}

	private void receiveCorrectAnswerAndShowNextQuestion() {
		if (currentQuestion != -1) {
			QuestionnaireItem questionnaireItem = (QuestionnaireItem) questionItems.get(currentQuestion);
			String answer = questionnaireItem.getAnswer();
			questionTimeEnd = new Date().getTime();
			ClientGuiController.getInstance().sendQuestionnaireProtocolAndWait(false, currentQuestionnaire.getQuestion(), answer, questionTimeEnd - questionTimeStart);
		}
		currentQuestion++;
		currentQuestionnaire = (QuestionnaireItem) questionItems.get(currentQuestion);
		questionText.setText("<html><center>" + currentQuestionnaire.getQuestion() + "</center></html>");
		resetAnswerPanel();
		answerPanel.add(currentQuestionnaire.getAnswerPanel());
		questionTimeStart = new Date().getTime();
	}

	private void receiveWrongAnswer() {
		JDialog dialog = new JOptionPane(wrongAnswerText, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION).createDialog("Advise");
		dialog.setLocationRelativeTo(this);
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
		dialog.dispose();
	}

	/**
	 * This method adds a questionnaire item.
	 *
	 * @param item
	 *            The QuestionnaireItem to be added.
	 */
	protected void addQuestionnaireItem(QuestionnaireItem item) {
		questionItems.add(item);
	}
}
