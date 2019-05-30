package org.mit.irb.web.IRBProtocol.pojo;

public class IRBProtocolRiskLevel {

	private String riskLevelCode;
	private String comment;
	private String updateUser;
	private String date;
	private String riskLevelDescription;
	
	public String getRiskLevelCode() {
		return riskLevelCode;
	}
	public void setRiskLevelCode(String riskLevelCode) {
		this.riskLevelCode = riskLevelCode;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getRiskLevelDescription() {
		return riskLevelDescription;
	}
	public void setRiskLevelDescription(String riskLevelDescription) {
		this.riskLevelDescription = riskLevelDescription;
	}
}
