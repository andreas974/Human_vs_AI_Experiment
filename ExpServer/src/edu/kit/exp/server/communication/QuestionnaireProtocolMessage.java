package edu.kit.exp.server.communication;

/**
 * Created by tondaroder on 31.01.17.
 */
public class QuestionnaireProtocolMessage extends ClientMessage {

	private String question;
	private String questionResponse;
	private long questionResponseTime;
	private boolean isLast;

	protected QuestionnaireProtocolMessage(String clientId, boolean isLast, String question, String questionResponse, long questionResponseTime) {
		super(clientId);

		this.isLast = isLast;
		this.question = question;
		this.questionResponse = questionResponse;
		this.questionResponseTime = questionResponseTime;
	}

	/**
	 * This method hashes whether a client passed and the questionnaire solution.
	 *
	 * @return some hashed parameters
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((questionResponse == null) ? 0 : questionResponse.hashCode());
		Long value = new Long(questionResponseTime);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		QuestionnaireProtocolMessage other = (QuestionnaireProtocolMessage) obj;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (questionResponse == null) {
			if (other.questionResponse != null)
				return false;
		} else if (!questionResponse.equals(other.questionResponse))
			return false;
		if (questionResponseTime != other.questionResponseTime)
			return false;
		return true;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestionResponse() {
		return questionResponse;
	}

	public void setQuestionResponse(String questionResponse) {
		this.questionResponse = questionResponse;
	}

	public long getQuestionResponseTime() {
		return questionResponseTime;
	}

	public void setQuestionResponseTime(long questionResponseTime) {
		this.questionResponseTime = questionResponseTime;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean last) {
		isLast = last;
	}
}
