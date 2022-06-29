package edu.kit.exp.server.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.CascadeType.*;

/**
 * The persistent class for the protocol database table.
 *
 */
@Entity
@Table(name = "quizprotocol", schema = "exp")
@NamedQueries({ @NamedQuery(name = "QuizProtocol.findAll", query = "SELECT t FROM QuizProtocol t"), @NamedQuery(name = "QuizProtocol.findByIdQuizProtocol", query = "SELECT t FROM QuizProtocol t WHERE t.idQuizProtocol = :idQuizProtocol") })
public class QuizProtocol implements Serializable, IEntry {
	private static final long serialVersionUID = -2707012121750032945L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, table = "quizprotocol")
	private Integer idQuizProtocol;

	private Boolean passed;

	@Column(length = 2147483647)
	private String solution;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id_subject", nullable = false)
	private Subject subject;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "quiz_sequence_element_idsequence_element", nullable = false)
	private Quiz quiz;

	public QuizProtocol() {

	}

	public Integer getIdQuizProtocol() {
		return idQuizProtocol;
	}

	public void setIdQuizProtocol(Integer idQuizProtocol) {
		this.idQuizProtocol = idQuizProtocol;
	}

	public Boolean getPassed() {
		return this.passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	public String getSolution() {
		return this.solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Quiz getQuiz() {
		return this.quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	@Override
	public Object getId() {
		return idQuizProtocol;
	}

}