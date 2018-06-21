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
	public String getUpdatetimstamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
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
}
