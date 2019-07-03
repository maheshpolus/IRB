package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "QUEST_ANSWER")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class IRBQuestionnaireAnswer {
	@Id
	@Column(name = "QUESTIONNAIRE_ANSWER_ID")
	private Integer questionnaireAnswerId;
	
	@Column(name = "QUESTIONNAIRE_ANS_HEADER_ID")
	private Integer questionnaireAnsHeaderId;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "QUEST_ANSWER_FK1"), name = "QUESTIONNAIRE_ANS_HEADER_ID", referencedColumnName = "QUESTIONNAIRE_ANS_HEADER_ID", insertable = false, updatable = false)
	private IRBQuestionnaireAnswerHeader questionnaireAnswerHeader;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "questionnaireAnswer", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<IRBQuestionnaireAnswerAttachment> irbQuestionnaireAnswerAttach;
	
	@Column(name = "QUESTION_ID")
	private Integer questionId;
	
	@Column(name = "OPTION_NUMBER")
	private Integer optionNumber;
	
	@Column(name = "ANSWER_NUMBER")
	private Integer answerNumber;
	
	@Column(name = "ANSWER")
	private String answer;
	
	@Column(name = "ANSWER_LOOKUP_CODE")
	private String answerLookupCode;
	
	@Column(name = "EXPLANATION")
	private String explanation;
	
	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimeStamp;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getQuestionnaireAnswerId() {
		return questionnaireAnswerId;
	}

	public void setQuestionnaireAnswerId(Integer questionnaireAnswerId) {
		this.questionnaireAnswerId = questionnaireAnswerId;
	}

	public Integer getQuestionnaireAnsHeaderId() {
		return questionnaireAnsHeaderId;
	}

	public void setQuestionnaireAnsHeaderId(Integer questionnaireAnsHeaderId) {
		this.questionnaireAnsHeaderId = questionnaireAnsHeaderId;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getOptionNumber() {
		return optionNumber;
	}

	public void setOptionNumber(Integer optionNumber) {
		this.optionNumber = optionNumber;
	}

	public Integer getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(Integer answerNumber) {
		this.answerNumber = answerNumber;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswerLookupCode() {
		return answerLookupCode;
	}

	public void setAnswerLookupCode(String answerLookupCode) {
		this.answerLookupCode = answerLookupCode;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public Date getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Date updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public IRBQuestionnaireAnswerHeader getQuestionnaireAnswerHeader() {
		return questionnaireAnswerHeader;
	}

	public void setQuestionnaireAnswerHeader(IRBQuestionnaireAnswerHeader questionnaireAnswerHeader) {
		this.questionnaireAnswerHeader = questionnaireAnswerHeader;
	}
}
