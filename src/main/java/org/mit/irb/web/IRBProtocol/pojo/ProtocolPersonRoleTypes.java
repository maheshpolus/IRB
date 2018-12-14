package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="IRB_PERSON_ROLES")
public class ProtocolPersonRoleTypes {
	@Id
	@Column(name = "PROTOCOL_PERSON_ROLE_ID")
	private String protocolPersonRoleId;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;
	
	@Column(name = "UNIT_DETAILS_REQUIRED")
	private String unitDetailsRequired;
	
	@Column(name = "ACTIVE_FLAG")
	private String activeFlag;
	
	@Column(name = "AFFILIATION_DETAILS_REQUIRED")
	private String affiliationDetailsRequired;
	
	@Column(name = "TRAINING_DETAILS_REQUIRED")
	private String trainingDetailsRequired;
	
	@Column(name = "COMMENTS_DETAILS_REQUIRED")
	private String commentsDetailsRequired;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getUnitDetailsRequired() {
		return unitDetailsRequired;
	}

	public void setUnitDetailsRequired(String unitDetailsRequired) {
		this.unitDetailsRequired = unitDetailsRequired;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getAffiliationDetailsRequired() {
		return affiliationDetailsRequired;
	}

	public void setAffiliationDetailsRequired(String affiliationDetailsRequired) {
		this.affiliationDetailsRequired = affiliationDetailsRequired;
	}

	public String getTrainingDetailsRequired() {
		return trainingDetailsRequired;
	}

	public void setTrainingDetailsRequired(String trainingDetailsRequired) {
		this.trainingDetailsRequired = trainingDetailsRequired;
	}

	public String getCommentsDetailsRequired() {
		return commentsDetailsRequired;
	}

	public void setCommentsDetailsRequired(String commentsDetailsRequired) {
		this.commentsDetailsRequired = commentsDetailsRequired;
	}

	public String getProtocolPersonRoleId() {
		return protocolPersonRoleId;
	}

	public void setProtocolPersonRoleId(String protocolPersonRoleId) {
		this.protocolPersonRoleId = protocolPersonRoleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
