package edu.kit.exp.client.gui.screens.question;

import edu.kit.exp.client.gui.screens.Screen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Abstract question screen for Quiz and Questionnaire Screen classes. Main UI-Components are defined here.
 *
 * @author tonda roder
 */
public abstract class QuestionScreen extends Screen{

	private static final Font REGULAR_FONT = new Font("Tahoma", Font.PLAIN, 17);
	private static final Font BOLD_FONT = new Font("Tahoma", Font.BOLD, 17);
	private static final Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 35);
	private static final String BUTTON_TEXT = "Next";
	private static final String DEFAULT_TITLE_TEXT = "Title Text";
	private static final String DEFAULT_PRE_TEXT = "Pre-Text";
	private static final String DEFAULT_POST_TEXT = "Post-Text";
	private static final String DEFAULT_WRONG_ANSWER_TEXT = "Wrong! / Not valid!";
	private static final String ICON_PATH = "/edu/kit/exp/common/resources/kit_logo.png";
	private static final Dimension RETURN_BUTTON_SIZE = new Dimension(200, 50);
	private static final Dimension QUESTION_SIZE = new Dimension(1000, 100);
	private static final Dimension TITLE_SIZE = new Dimension(1000, 100);

	/**
	 * The Class ParamObject.
	 */
	public static class ParamObject extends Screen.ParamObject {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -1982991126040461821L;

	}

	/**
	 * The Class ResponseObject.
	 */
	public static class ResponseObject extends Screen.ResponseObject {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -2424981455361805781L;

	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7399745553763013231L;

	/** The question items. */
	protected ArrayList<QuestionItem> questionItems;

	/** The title text panel. */
	private JLabel titleTextPanel;

	/** The title text. */
	private String titleText;

	/** The return button. */
	private JButton returnButton;

	/** The question text. */
	protected JLabel questionText;

	/** The answer panel. */
	protected JPanel answerPanel;

	/** The current question. */
	protected int currentQuestion;

	/** The pre text. */
	protected String preText;

	/** The post text. */
	protected String postText;

	/** The wrong answer text. */
	protected String wrongAnswerText;

	/** The logo. */
	private ImageIcon logo;

	/** The logo label. */
	private JLabel logoLabel;

	/**
	 * This method gets the text which will be displayed if a wrong answer was
	 * chosen.
	 *
	 * @return the wrong answer text
	 */
	public String getWrongAnswerText() {
		return wrongAnswerText;
	}

	/**
	 * This method sets the text which will be displayed if a wrong answer was
	 * chosen.
	 *
	 * @param wrongAnswerText
	 *            A String which contains the text that will be displayed if a
	 *            wrong answer was chosen.
	 */
	public void setWrongAnswerText(String wrongAnswerText) {
		this.wrongAnswerText = wrongAnswerText;
	}

	/**
	 * This method gets the text before questions.
	 *
	 * @return the pre text
	 */
	public String getPreText() {
		return preText;
	}

	/**
	 * This method sets the text before questions.
	 *
	 * @param preText
	 *            The String containing the new pre text.
	 */
	public void setPreText(String preText) {
		this.preText = preText;
		this.questionText.setText(preText);
	}

	/**
	 * This method gets text after questions.
	 *
	 * @return the post text
	 */
	public String getPostText() {
		return postText;
	}

	/**
	 * This method sets the text after questions.
	 *
	 * @param postText
	 *            The new post text.
	 */
	public void setPostText(String postText) {
		this.postText = postText;
	}

	/**
	 * This method gets the title text.
	 *
	 * @return the title text
	 */
	public String getTitleText() {
		return titleText;
	}

	/**
	 * This method sets the title text.
	 *
	 * @param titleText
	 *            The String which provides the new title text.
	 */
	public void setTitleText(String titleText) {
		this.titleText = titleText;
		updateTitle();
	}

	/**
	 * This method gets the button text.
	 *
	 * @return the button text
	 */
	public String getButtonText() {
		return returnButton.getText();
	}

	/**
	 * This method sets the button text.
	 *
	 * @param text
	 *            The String which provides the new button text.
	 */
	public void setButtonText(String text) {
		returnButton.setText(text);
	}

	/**
	 * This method gets a list that contains the question items.
	 *
	 * @return the question items
	 * @see QuestionItem
	 */
	public ArrayList<QuestionItem> getQuestionItems() {
		return questionItems;
	}

	/**
	 * This method sets the question items.
	 *
	 * @param questionItems
	 *            A QuizItem list that provides the new question items.
	 * @see QuestionItem
	 */
	public void setQuestionItems(ArrayList<QuestionItem> questionItems) {
		this.questionItems = questionItems;
	}

	/**
	 * This method gets the logo image.
	 *
	 * @return the logo
	 */
	public ImageIcon getLogo() {
		return logo;
	}

	/**
	 * This method sets the logo image.
	 *
	 * @param logo
	 *            The ImageIcon of the new logo.
	 */
	public void setLogo(ImageIcon logo) {
		this.logo = logo;
		logoLabel.setIcon(logo);
	}

	/**
	 * This constructor instantiates a new question screen.
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
	 */
	public QuestionScreen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		questionItems = new ArrayList<>();
		currentQuestion = -1;
		titleText = DEFAULT_TITLE_TEXT;
		preText = DEFAULT_PRE_TEXT;
		postText = DEFAULT_POST_TEXT;
		wrongAnswerText = DEFAULT_WRONG_ANSWER_TEXT;
		logo = new ImageIcon(getClass().getResource(ICON_PATH));

		createLayout();

		createLogo();

		JPanel mainContentPanel = createMainContenPanel();

		createTitle(mainContentPanel);

		createQuestionPanel(mainContentPanel);

		createAnswerPanel(mainContentPanel);

		createButtonPanel(mainContentPanel);
	}

	private void createButtonPanel(JPanel mainContentPanel) {
		JPanel buttonPanel = new JPanel();
		mainContentPanel.add(buttonPanel);
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		returnButton = new JButton(BUTTON_TEXT);
		buttonPanel.add(returnButton);
		returnButton.setFont(REGULAR_FONT);
		returnButton.setBackground(new Color(192, 192, 192));
		returnButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		returnButton.setPreferredSize(RETURN_BUTTON_SIZE);
		returnButton.setMaximumSize(returnButton.getPreferredSize());
		returnButton.setMinimumSize(returnButton.getPreferredSize());
		returnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showNextQuestion();
			}
		});

		setDefaultButton(returnButton);
	}

	private void createAnswerPanel(JPanel mainContentPanel) {
		answerPanel = new JPanel();
		mainContentPanel.add(answerPanel);
		resetAnswerPanel();
	}

	private void createQuestionPanel(JPanel mainContentPanel) {
		JPanel questionPanel = new JPanel();
		mainContentPanel.add(questionPanel);
		questionPanel.setBackground(Color.WHITE);
		questionPanel.setLayout(new BorderLayout(0, 0));
		questionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		questionPanel.setPreferredSize(QUESTION_SIZE);
		questionPanel.setMaximumSize(questionPanel.getPreferredSize());
		questionPanel.setMinimumSize(questionPanel.getPreferredSize());
		questionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Show "question is starting" info
		questionText = new JLabel("<html><center>" + preText + "</center></html>", SwingConstants.CENTER);
		questionPanel.add(questionText);
		questionText.setFont(BOLD_FONT);
		questionText.setForeground(Color.BLACK);

		mainContentPanel.add(Box.createRigidArea(new Dimension(1, 10)));
	}

	private void createTitle(JPanel mainContentPanel) {
		JPanel titlePanel = new JPanel();
		mainContentPanel.add(titlePanel);
		titlePanel.setBackground(Color.WHITE);
		titlePanel.setLayout(new BorderLayout(0, 0));
		titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		titlePanel.setPreferredSize(TITLE_SIZE);
		titlePanel.setMaximumSize(titlePanel.getPreferredSize());
		titlePanel.setMinimumSize(titlePanel.getPreferredSize());
		titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		titleTextPanel = new JLabel(titleText, SwingConstants.LEFT);
		titlePanel.add(titleTextPanel);
		titleTextPanel.setFont(TITLE_FONT);
		titleTextPanel.setForeground(Color.BLACK);
	}

	private JPanel createMainContenPanel() {
		JPanel mainContentPanel = new JPanel();
		add(mainContentPanel, BorderLayout.CENTER);
		mainContentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainContentPanel.setBackground(Color.WHITE);
		mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
		return mainContentPanel;
	}

	private void createLogo() {
		JPanel kitLogoPanel = new JPanel();
		add(kitLogoPanel, BorderLayout.NORTH);
		kitLogoPanel.setBackground(Color.WHITE);
		kitLogoPanel.setLayout(new BorderLayout(0, 0));

		logoLabel = new JLabel("");
		logoLabel.setIcon(logo);
		kitLogoPanel.add(logoLabel, BorderLayout.EAST);
	}

	private void createLayout() {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
	}

	protected abstract void showNextQuestion();

	/**
	 * This method updates the title.
	 */
	protected void updateTitle() {
		titleTextPanel.setText("<html>" + titleText + (currentQuestion > -1 && currentQuestion < questionItems.size() ? " (" + (currentQuestion + 1) + "/" + questionItems.size() + ")" : "") + "</html>");
	}

	/**
	 * Rest answer panel.
	 */
	protected void resetAnswerPanel() {
		answerPanel.removeAll();
		answerPanel.setBackground(Color.WHITE);
		answerPanel.setLayout(new BorderLayout(0, 0));
		answerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		//answerPanel.setPreferredSize(new Dimension(500, 200));
		answerPanel.setPreferredSize(new Dimension(1000, 400));
		answerPanel.setMaximumSize(answerPanel.getPreferredSize());
		answerPanel.setMinimumSize(answerPanel.getPreferredSize());
		answerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		answerPanel.revalidate();
		answerPanel.repaint();
	}

}
