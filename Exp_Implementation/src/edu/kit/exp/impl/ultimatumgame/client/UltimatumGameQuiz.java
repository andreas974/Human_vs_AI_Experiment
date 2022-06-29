package edu.kit.exp.impl.ultimatumgame.client;

import edu.kit.exp.client.gui.screens.question.quiz.QuizItem;
import edu.kit.exp.client.gui.screens.question.quiz.QuizItemMultipleChoice;
import edu.kit.exp.client.gui.screens.question.quiz.QuizItemNumberInput;
import edu.kit.exp.client.gui.screens.question.quiz.QuizScreen;

/**
 * The class UltimatumGameQuiz provides a <code>QuizScreen</code> for displaying
 * an ultimatum game quiz to clients.
 * 
 * @see QuizScreen
 */
public class UltimatumGameQuiz extends QuizScreen {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8841977439730389225L;

	/**
	 * This constructor instantiates a new ultimatum game quiz. </br>
	 * It consists of multiple <code>QuizItems</code>, e.g.
	 * <code>QuizItemMultipleChoice</code>
	 * 
	 * 
	 * @param gameId
	 *            A String which contains the ID of the running game.
	 * @param parameter
	 *            A ParamObject list of all parameters used in this screen.
	 * @param screenId
	 *            A String which contains the global screen ID of this screen.
	 * @param showUpTime
	 *            A Long which indicates how long the Screen will be shown to a
	 *            client.
	 * 
	 * @see {@link QuizItem QuizItem}
	 */
	public UltimatumGameQuiz(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		QuizItemMultipleChoice question;
		question = new QuizItemMultipleChoice("A simple question with one correct answer?");
		question.addAnswer("2");
		question.addAnswer("4 (true)", true);
		question.addAnswer("8");
		question.addAnswer("10");
		this.addQuizItem(question);

		question = new QuizItemMultipleChoice("A simple question with more correct answers?");
		question.addAnswer("2");
		question.addAnswer("3 (true)", true);
		question.addAnswer("6 (true, too)", true);
		question.addAnswer("9");
		this.addQuizItem(question);

		question = new QuizItemMultipleChoice("A simple question with one correct answer but multiple options?");
		question.setSelectMultiple(true);
		question.addAnswer("2");
		question.addAnswer("3 (only this is true)", true);
		question.addAnswer("6");
		question.addAnswer("9");
		this.addQuizItem(question);

		QuizItemNumberInput inputQuestion = new QuizItemNumberInput(
				"A simple question with an integer input? (5.447 is correct)", 5.447);
		this.addQuizItem(inputQuestion);

		inputQuestion = new QuizItemNumberInput("A simple question with an integer input? (5 is correct)", 5);
		this.addQuizItem(inputQuestion);
	}
}
