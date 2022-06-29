package edu.kit.exp.impl.trustgame.client;

import edu.kit.exp.client.gui.screens.question.QuestionScreen;
import edu.kit.exp.client.gui.screens.question.questionnaire.QuestionnaireMultipleChoice;
import edu.kit.exp.client.gui.screens.question.questionnaire.QuestionnaireNumberInput;
import edu.kit.exp.client.gui.screens.question.questionnaire.QuestionnaireScreen;
import edu.kit.exp.client.gui.screens.question.questionnaire.QuestionnaireStringInput;

/**
 * Created by tondaroder on 30.01.17.
 */
public class TrustGameQuestionnaire extends QuestionnaireScreen {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8841977439730389225L;

	public TrustGameQuestionnaire(String gameId, QuestionScreen.ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		QuestionnaireStringInput stringInput = new QuestionnaireStringInput("Please enter your first name");
		this.addQuestionnaireItem(stringInput);

		stringInput = new QuestionnaireStringInput("Please enter your last name");
		this.addQuestionnaireItem(stringInput);

		QuestionnaireMultipleChoice question;
		question = new QuestionnaireMultipleChoice("Please enter your gender");
		question.addAnswer("male");
		question.addAnswer("female");
		this.addQuestionnaireItem(question);

		QuestionnaireNumberInput numberInput = new QuestionnaireNumberInput("Please enter your age");
		this.addQuestionnaireItem(numberInput);

		question = new QuestionnaireMultipleChoice("Which of the following colors do you like?");
		question.setSelectMultiple(true);
		question.addAnswer("red");
		question.addAnswer("blue");
		question.addAnswer("yellow");
		question.addAnswer("green");
		question.addAnswer("brown");
		question.addAnswer("orange");
		this.addQuestionnaireItem(question);
	}
}
