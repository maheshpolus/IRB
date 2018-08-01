package org.mit.irb.web.common.pojo;

public class IRBExemptForm {

	private Integer exemptFormID; 
	private String exemptTitle; 
	private String personId; 
	private String personName; 
	private Integer exemptFormNumber; 
	private Integer exemptQuestionnaireAnswerHeaderId; 
	private String updateUser;
	private String updateTimestamp;
	private String isExempt;
	private String status;
	private String statusCode;
	private String facultySponsorPersonId;
	private String facultySponsorPerson;
	private String unitNumber;
	private String unitName;
	private String summary;
	private boolean isFacultySponsor;
	private String comment;
	private String actionTypesCode;
	private String checkListDescription;
	private String checkListAcType;
	private Integer checkListId;
	private String exemptProtocolStartDate;
	private String exemptProtocolEndDate;
	private String facultySponsorJobTitle;
	private String PIJobTitle;
	
	public String getCheckListAcType() {
		return checkListAcType;
	}

	public void setCheckListAcType(String checkListAcType) {
		this.checkListAcType = checkListAcType;
	}

	public String getCheckListDescription() {
		return checkListDescription;
	}

	public void setCheckListDescription(String checkListDescription) {
		this.checkListDescription = checkListDescription;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getFacultySponsorPersonId() {
		return facultySponsorPersonId;
	}
	public void setFacultySponsorPersonId(String facultySponsorPersonId) {
		this.facultySponsorPersonId = facultySponsorPersonId;
	}
	
	public Integer getExemptFormID() {
		return exemptFormID;
	}
	public void setExemptFormID(Integer exemptFormID) {
		this.exemptFormID = exemptFormID;
	}
	public String getExemptTitle() {
		return exemptTitle;
	}
	public void setExemptTitle(String exemptTitle) {
		this.exemptTitle = exemptTitle;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public Integer getExemptFormNumber() {
		return exemptFormNumber;
	}
	public void setExemptFormNumber(Integer exemptFormNumber) {
		this.exemptFormNumber = exemptFormNumber;
	}
	public Integer getExemptQuestionnaireAnswerHeaderId() {
		return exemptQuestionnaireAnswerHeaderId;
	}
	public void setExemptQuestionnaireAnswerHeaderId(Integer exemptQuestionnaireAnswerHeaderId) {
		this.exemptQuestionnaireAnswerHeaderId = exemptQuestionnaireAnswerHeaderId;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getIsExempt() {
		return isExempt;
	}
	public void setIsExempt(String isExempt) {
		this.isExempt = isExempt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getFacultySponsorPerson() {
		return facultySponsorPerson;
	}
	public void setFacultySponsorPerson(String facultySponsorPerson) {
		this.facultySponsorPerson = facultySponsorPerson;
	}

	public String getUnitNumber() {
		return unitNumber;
	}
	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}
	public String getUnitName() {
		return unitName;
	}
	
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	} 
	
	public boolean getIsFacultySponsor() {
		return isFacultySponsor;
	}
	public void setIsFacultySponsor(boolean b) {
		this.isFacultySponsor = b;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getActionTypesCode() {
		return actionTypesCode;
	}
	public void setActionTypesCode(String actionTypesCode) {
		this.actionTypesCode = actionTypesCode;
	}

	public String getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Integer getCheckListId() {
		return checkListId;
	}

	public void setCheckListId(Integer checkListId) {
		this.checkListId = checkListId;
	}

	public String getExemptProtocolStartDate() {
		return exemptProtocolStartDate;
	}

	public void setExemptProtocolStartDate(String exemptProtocolStartDate) {
		this.exemptProtocolStartDate = exemptProtocolStartDate;
	}

	public String getExemptProtocolEndDate() {
		return exemptProtocolEndDate;
	}

	public void setExemptProtocolEndDate(String exemptProtocolEndDate) {
		this.exemptProtocolEndDate = exemptProtocolEndDate;
	}

	public String getFacultySponsorJobTitle() {
		return facultySponsorJobTitle;
	}

	public void setFacultySponsorJobTitle(String facultySponsorJobTitle) {
		this.facultySponsorJobTitle = facultySponsorJobTitle;
	}

	public String getPIJobTitle() {
		return PIJobTitle;
	}

	public void setPIJobTitle(String pIJobTitle) {
		PIJobTitle = pIJobTitle;
	}
}
