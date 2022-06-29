package edu.kit.exp.server.communication;

/**
 * The class QuizProtocolMessage can be used to send information about a quiz.
 * 
 * @see ClientMessage
 */
public class QuizProtocolMessage extends ClientMessage {

	/** The passed. */
	private Boolean passed;

	/** The quiz solution. */
	private String quizSolution;

	/**
	 * This constructor instantiates a new quiz protocol message.
	 * 
	 * @param clientId
	 *            A String which contains the client ID.
	 * @param passed
	 *            A boolean that indicates if a quiz was passed or not.
	 * @param quizSolution
	 *            The String that contains the solution for the quiz.
	 */
	protected QuizProtocolMessage(String clientId, Boolean passed, String quizSolution) {
		super(clientId);

		this.passed = passed;
		this.quizSolution = quizSolution;
	}

	/**
	 * This method gets result of the quiz.
	 * 
	 * @return <code>true</code> if a client passed the quiz, otherwise it
	 *         returns <code>false</code>.
	 */
	public Boolean getPassed() {
		return passed;
	}

	/**
	 * This method sets the result of the Quiz
	 * 
	 * @param passed
	 *            A Boolean that indicates if a client passed the quiz or not.
	 */
	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	/**
	 * This method gets the quiz solution.
	 * 
	 * @return the quiz solution
	 */
	public String getQuizSolution() {
		return quizSolution;
	}

	/**
	 * Sets the quiz solution.
	 * 
	 * @param quizSolution
	 *            The String that contains the quiz solution.
	 */
	public void setQuizSolution(String quizSolution) {
		this.quizSolution = quizSolution;
	}

	/**
	 * This method hashes whether a client passed and the quiz solution.
	 * 
	 * @return some hashed parameters
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((passed == null) ? 0 : passed.hashCode());
		result = prime * result + ((quizSolution == null) ? 0 : quizSolution.hashCode());
		return result;
	}

	/**
	 * Compares an <code>Object</code> to the current
	 * <code>QuizProtocolMessage</code> object.
	 * 
	 * @param obj
	 *            A Object parameter.
	 * @return <code>true</code> if the given object is this
	 *         <code>QuizProtocolMessage</code>,<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuizProtocolMessage other = (QuizProtocolMessage) obj;
		if (passed == null) {
			if (other.passed != null)
				return false;
		} else if (!passed.equals(other.passed))
			return false;
		if (quizSolution == null) {
			if (other.quizSolution != null)
				return false;
		} else if (!quizSolution.equals(other.quizSolution))
			return false;
		return true;
	}

}
