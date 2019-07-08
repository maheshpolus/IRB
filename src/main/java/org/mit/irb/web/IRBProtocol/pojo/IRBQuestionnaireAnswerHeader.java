package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "QUEST_ANSWER_HEADER")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class IRBQuestionnaireAnswerHeader {
	@Id
	@Column(name = "QUESTIONNAIRE_ANS_HEADER_ID")
	private Integer questionnaireAnswerHeaderId;
	
	@Column(name = "QUESTIONNAIRE_ID")
	private Integer questionnaireId;
	
	@Column(name = "MODULE_ITEM_CODE")
	private Integer moduleItemCode;
	
	@Column(name = "MODULE_SUB_ITEM_CODE")
	private Integer moduleSubItemCode;
	
	@Column(name = "MODULE_ITEM_KEY")
	private String moduleItemKey;
	
	@Column(name = "MODULE_SUB_ITEM_KEY")
	private String moduleSubItemKey;
	
	@Column(name = "QUESTIONNAIRE_COMPLETED_FLAG")
	private String questionnaireCompletedFlag;
	
	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimeStamp;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getQuestionnaireAnswerHeaderId() {
		return questionnaireAnswerHeaderId;
	}

	public void setQuestionnaireAnswerHeaderId(Integer questionnaireAnswerHeaderId) {
		this.questionnaireAnswerHeaderId = questionnaireAnswerHeaderId;
	}

	public Integer getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Integer questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public Integer getModuleItemCode() {
		return moduleItemCode;
	}

	public void setModuleItemCode(Integer moduleItemCode) {
		this.moduleItemCode = moduleItemCode;
	}

	public Integer getModuleSubItemCode() {
		return moduleSubItemCode;
	}

	public void setModuleSubItemCode(Integer moduleSubItemCode) {
		this.moduleSubItemCode = moduleSubItemCode;
	}

	public String getModuleItemKey() {
		return moduleItemKey;
	}

	public void setModuleItemKey(String moduleItemKey) {
		this.moduleItemKey = moduleItemKey;
	}

	public String getModuleSubItemKey() {
		return moduleSubItemKey;
	}

	public void setModuleSubItemKey(String moduleSubItemKey) {
		this.moduleSubItemKey = moduleSubItemKey;
	}

	public String getQuestionnaireCompletedFlag() {
		return questionnaireCompletedFlag;
	}

	public void setQuestionnaireCompletedFlag(String questionnaireCompletedFlag) {
		this.questionnaireCompletedFlag = questionnaireCompletedFlag;
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
}
