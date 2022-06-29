package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.ALL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * The persistent class for the questionnaire database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Questionnaire.findAll", query = "SELECT t FROM Questionnaire t"), @NamedQuery(name = "Questionnaire.findByIdQuestionnaire", query = "SELECT t FROM Questionnaire t WHERE t.idSequenceElement = :idQuestionnaire") })
@Table(name = "questionnaire", schema = "exp")
@PrimaryKeyJoinColumn(name = "sequence_element_idsequence_element", referencedColumnName = "idsequence_element")
@DiscriminatorValue("quest")
public class Questionnaire extends SequenceElement implements Serializable {
	private static final long serialVersionUID = -3675384234891004318L;

	@Column(name = "questionnaire_factory_key", length = 255)
	private String questionnaireFactoryKey;

	@OneToMany(mappedBy = "questionnaire", cascade = ALL)
	private List<QuestionnaireProtocol> protocols;

	public Questionnaire() {
		protocols = new ArrayList<QuestionnaireProtocol>();
	}

	public String getQuestionnaireFactoryKey() {
		return this.questionnaireFactoryKey;
	}

	public void setQuestionnaireFactoryKey(String questionnaireFactoryKey) {
		this.questionnaireFactoryKey = questionnaireFactoryKey;
	}

	public List<QuestionnaireProtocol> getProtocols() {
		return this.protocols;
	}

	public void setProtocols(List<QuestionnaireProtocol> protocols) {
		this.protocols = protocols;
	}

	@Override
	public String toString() {
		return "Questionnaire [Questionnaire class: " + questionnaireFactoryKey + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((questionnaireFactoryKey == null) ? 0 : questionnaireFactoryKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Questionnaire other = (Questionnaire) obj;
		if (questionnaireFactoryKey == null) {
			if (other.questionnaireFactoryKey != null)
				return false;
		} else if (!questionnaireFactoryKey.equals(other.questionnaireFactoryKey))
			return false;
		return true;
	}

}