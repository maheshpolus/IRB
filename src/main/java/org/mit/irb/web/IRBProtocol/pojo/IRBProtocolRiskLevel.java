package org.mit.irb.web.IRBProtocol.pojo;

public class IRBProtocolRiskLevel {

	private String riskLevelCode;
	private String fdaRiskLevelCode;
	private String riskLevelComment;
	private String fdaRiskLevelComment;
	private String updateUser;
	private String riskLevelDateAssigned;
	private String fdariskLevelDateAssigned;
	private Integer submissionId;
	private Integer protocolId;
	private String protocolNumber;
	
	public String getRiskLevelCode() {
		return riskLevelCode;
	}
	public void setRiskLevelCode(String riskLevelCode) {
		this.riskLevelCode = riskLevelCode;
	}
	public String getFdaRiskLevelCode() {
		return fdaRiskLevelCode;
	}
	public void setFdaRiskLevelCode(String fdaRiskLevelCode) {
		this.fdaRiskLevelCode = fdaRiskLevelCode;
	}
	public String getRiskLevelComment() {
		return riskLevelComment;
	}
	public void setRiskLevelComment(String riskLevelComment) {
		this.riskLevelComment = riskLevelComment;
	}
	public String getFdaRiskLevelComment() {
		return fdaRiskLevelComment;
	}
	public void setFdaRiskLevelComment(String fdaRiskLevelComment) {
		this.fdaRiskLevelComment = fdaRiskLevelComment;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getRiskLevelDateAssigned() {
		return riskLevelDateAssigned;
	}
	public void setRiskLevelDateAssigned(String riskLevelDateAssigned) {
		this.riskLevelDateAssigned = riskLevelDateAssigned;
	}
	public String getFdariskLevelDateAssigned() {
		return fdariskLevelDateAssigned;
	}
	public void setFdariskLevelDateAssigned(String fdariskLevelDateAssigned) {
		this.fdariskLevelDateAssigned = fdariskLevelDateAssigned;
	}
	public Integer getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}
	public Integer getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}
	public String getProtocolNumber() {
		return protocolNumber;
	}
	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}
}
