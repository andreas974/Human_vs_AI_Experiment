package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the protocol database table.
 *
 */
@Entity
@Table(name = "questionnaireprotocol", schema = "exp")
@NamedQueries({ @NamedQuery(name = "QuestionnaireProtocol.findAll", query = "SELECT t FROM QuestionnaireProtocol t"), @NamedQuery(name = "QuestionnaireProtocol.findByIdQuestionnaireProtocol", query = "SELECT t FROM QuestionnaireProtocol t WHERE t.idQuestionnaireProtocol = :idQuestionnaireProtocol") })
public class QuestionnaireProtocol implements Serializable, IEntry {
	private static final long serialVersionUID = -2707012121750032945L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, table = "questionnaireprotocol")
	private Integer idQuestionnaireProtocol;

	@Column
	private String question;

	@Column
	private String questionResponse;

	@Column
	private long questionResponseTime;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id_subject", nullable = false)
	private Subject subject;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "questionnaire_sequence_element_idsequence_element", nullable = false)
	private Questionnaire questionnaire;

	public QuestionnaireProtocol() {

	}

	public String getQuestionResponse() {
		return questionResponse;
	}

	public void setQuestionResponse(String questionResponse) {
		this.questionResponse = questionResponse;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public long getQuestionResponseTime() {
		return questionResponseTime;
	}

	public void setQuestionResponseTime(long questionResponseTime) {
		this.questionResponseTime = questionResponseTime;
	}

	public Integer getIdQuestionnaireProtocol() {
		return idQuestionnaireProtocol;
	}

	public void setIdQuestionnaireProtocol(Integer idQuestionnaireProtocol) {
		this.idQuestionnaireProtocol = idQuestionnaireProtocol;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Questionnaire getQuestionnaire() {
		return this.questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	@Override
	public Object getId() {
		return idQuestionnaireProtocol;
	}

}