package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Clob;
import java.sql.Date;

public class PersonTraining {

	 private Integer personTrainingID;
	 private String personID;
	 private char activeFlag; 
	 private Clob comments;	 
	 private Date dateAcknowledged;	 
	 private Date dateRequested;
	 private Date dateSubmitted;	 
	 private Date followUpDate;	 
	 private String isEmployee;	 
	 private String score;	 
	 private Integer trainingCode;	 
	 private Integer trainingNumber;
	 private Date updateTimeStamp;	 
	 private String updateUser;

	public String getPersonID() {
		return personID;
	}

	public void setPersonID(String personID) {
		this.personID = personID;
	}

	public char getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(char activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Clob getComments() {
		return comments;
	}

	public void setComments(Clob comments) {
		this.comments = comments;
	}

	public Date getDateAcknowledged() {
		return dateAcknowledged;
	}

	public void setDateAcknowledged(Date dateAcknowledged) {
		this.dateAcknowledged = dateAcknowledged;
	}

	public Date getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(Date dateRequested) {
		this.dateRequested = dateRequested;
	}

	public Date getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public Date getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}

	public String getIsEmployee() {
		return isEmployee;
	}

	public void setIsEmployee(String isEmployee) {
		this.isEmployee = isEmployee;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Integer getTrainingCode() {
		return trainingCode;
	}

	public void setTrainingCode(Integer trainingCode) {
		this.trainingCode = trainingCode;
	}

	public Integer getTrainingNumber() {
		return trainingNumber;
	}

	public void setTrainingNumber(Integer trainingNumber) {
		this.trainingNumber = trainingNumber;
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

	public Integer getPersonTrainingID() {
		return personTrainingID;
	}

	public void setPersonTrainingID(Integer personTrainingID) {
		this.personTrainingID = personTrainingID;
	}
	 
}
