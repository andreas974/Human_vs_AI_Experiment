package edu.kit.exp.impl.continuousCompetition.client.Questionnaire;

import edu.kit.exp.client.gui.screens.question.questionnaire.*;

import javax.swing.*;

/**
 * Created by tondaroder on 30.01.17.
 */
public class ContinuousCompetitionQuestionnaire extends QuestionnaireScreen {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8841977439730389225L;

	public ContinuousCompetitionQuestionnaire(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		/*this.setTitleText("Zusätzlicher Fragebogen");
		this.setPreText("Klicken Sie \"Weiter\", um mit dem Fragebogen zu beginnen.");
		this.setPostText("Sie haben die Fragen erfolgreich abgeschlossen.<br>Klicken Sie \"Weiter\", um das Experiment zu beenden.");*/

		QuestionnaireStringInput stringInput = new QuestionnaireStringInput("Welchen Studiengang studieren Sie aktuell?");
		this.addQuestionnaireItem(stringInput);

		QuestionnaireMultipleChoice question;
		question = new QuestionnaireMultipleChoice("Welches Geschlecht haben Sie?");
		question.addAnswer("männlich");
		question.addAnswer("weiblich");
		question.addAnswer("nicht-binär");
		this.addQuestionnaireItem(question);

		QuestionnaireNumberInput numberInput = new QuestionnaireNumberInput("Bitte geben Sie Ihr Alter an");
		this.addQuestionnaireItem(numberInput);

		//Relative performance instead of own profit
		//Own characteristics

		question = new QuestionnaireMultipleChoice("<i>Wie würden Sie sich selbst beschreiben?</i><br>Ich sehe mich selbst als jemanden, der gerne gewinnt und es hasst zu verlieren.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		question = new QuestionnaireMultipleChoice("<i>Wie würden Sie sich selbst beschreiben?</i><br>Ich sehe mich als jemanden, der gerne mit anderen konkurriert, unabhängig davon, ob ich gewinne oder verliere.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		question = new QuestionnaireMultipleChoice("<i>Wie würden Sie sich selbst beschreiben?</i><br>Ich sehe mich selbst als einen wettbewerbsorientierten Menschen.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		question = new QuestionnaireMultipleChoice("<i>Wie würden Sie sich selbst beschreiben?</i><br>Wettbewerb bringt das Beste aus mir heraus.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		//Human AI Cooperation
		//AI Characteristics

		question = new QuestionnaireMultipleChoice("<i>Beschreiben Sie inwiefern die folgenden Aussagen über das Verhalten Ihres Gegenüber zutreffen?</i><br>Mein Gegenüber verhielt sich intelligent.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		question = new QuestionnaireMultipleChoice("<i>Beschreiben Sie inwiefern die folgenden Aussagen über das Verhalten Ihres Gegenüber zutreffen?</i><br>Mein Gegenüber war bemüht ein vorgegebenes Ziel zu erreichen.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		question = new QuestionnaireMultipleChoice("<i>Beschreiben Sie inwiefern die folgenden Aussagen über das Verhalten Ihres Gegenüber zutreffen?</i><br>Das Verhalten meines Gegenüber habe ich als angenehm wahrgenommen.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		question = new QuestionnaireMultipleChoice("<i>Beschreiben Sie inwiefern die folgenden Aussagen über das Verhalten Ihres Gegenüber zutreffen?</i><br>Mein Gegenüber verhielt sich sehr außergewöhnlich.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		//Satisfaction with AI as competitor

		question = new QuestionnaireMultipleChoice("Mein Gegenüber hat verstanden, auf welches Ziel ich hinarbeiten wollte.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		question = new QuestionnaireMultipleChoice("Ich konnte meinem Gegenüber in seinen Aktionen vertrauen.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		question = new QuestionnaireMultipleChoice("Mein Gegenüber und ich haben gut zusammengearbeitet.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		//Ability to collude

		question = new QuestionnaireMultipleChoice("Ich konnte zusammen mit meinem Gegenüber ein gemeinsames Ziel erreichen.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		question = new QuestionnaireMultipleChoice("Es fiel mir leicht, mit meinem Gegenüber zu einem gemeinsamen Ziel hinzuarbeiten.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		question = new QuestionnaireMultipleChoice("Das Verhalten meines Gegenüber war für mich einfach nachvollziehbar.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		question = new QuestionnaireMultipleChoice("Das Verhalten meines Gegenüber war für mich verständlich.");
		question.addAnswer("Ich stimme voll und ganz zu");
		question.addAnswer("Ich stimme zu");
		question.addAnswer("Ich stimme weder zu noch lehne ab");
		question.addAnswer("Ich stimme nicht zu");
		question.addAnswer("Ich stimme überhaupt nicht zu");
		this.addQuestionnaireItem(question);

		QuestionnaireTextInput textInput = new QuestionnaireTextInput("Haben Sie weitere Anmerkungen zum Verhalten Ihres Gegenüber?");
		this.addQuestionnaireItem(textInput);

		textInput = new QuestionnaireTextInput("Haben Sie weitere Anmerkungen allgemein zum Experiment?");
		this.addQuestionnaireItem(textInput);
	}
}
