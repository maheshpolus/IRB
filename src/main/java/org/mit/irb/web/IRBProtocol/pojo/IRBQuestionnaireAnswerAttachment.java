package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "QUEST_ANSWER_ATTACHMENT")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class IRBQuestionnaireAnswerAttachment {
	@Id
	@Column(name = "QUESTIONNAIRE_ANSWER_ATT_ID")
	private Integer questionnaireAnswerAttachId;
	
	@Column(name = "QUESTIONNAIRE_ANSWER_ID")
	private Integer questionnaireAnswerId;
	
	@JsonBackReference
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "QUEST_ANSWER_ATTACHMENT_FK1"), name = "QUESTIONNAIRE_ANSWER_ID", referencedColumnName = "QUESTIONNAIRE_ANSWER_ID",insertable = false, updatable = false)
	private IRBQuestionnaireAnswer questionnaireAnswer;
	
	@JsonIgnore
	@Basic(fetch=FetchType.LAZY)
	@Column(name = "ATTACHMENT")
	private Integer attachmentData;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "CONTENT_TYPE")
	private String mimeType;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimeStamp;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getQuestionnaireAnswerAttachId() {
		return questionnaireAnswerAttachId;
	}

	public void setQuestionnaireAnswerAttachId(Integer questionnaireAnswerAttachId) {
		this.questionnaireAnswerAttachId = questionnaireAnswerAttachId;
	}

	public Integer getAttachmentData() {
		return attachmentData;
	}

	public void setAttachmentData(Integer attachmentData) {
		this.attachmentData = attachmentData;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
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

	public Integer getQuestionnaireAnswerId() {
		return questionnaireAnswerId;
	}

	public void setQuestionnaireAnswerId(Integer questionnaireAnswerId) {
		this.questionnaireAnswerId = questionnaireAnswerId;
	}

	public IRBQuestionnaireAnswer getQuestionnaireAnswer() {
		return questionnaireAnswer;
	}

	public void setQuestionnaireAnswer(IRBQuestionnaireAnswer questionnaireAnswer) {
		this.questionnaireAnswer = questionnaireAnswer;
	}
}
