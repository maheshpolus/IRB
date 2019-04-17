package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "WHOSP_PERSON_TRAINING")
public class PersonTraining1 {
	 @Id	
	 @Column(name = "PERSON_ID")
	 private String personID;
	 
	 @Column(name="TRAINING_NUMBER")
	 private Integer trainingNumber;
	 
	 @Column(name="TRAINING_CODE")
	 private Integer trainingCode;
	 
	 @Column(name="TRAINING_DESCRIPTION")
	 private String trainingDescription;
	 
	 @Column(name="DATE_REQUESTED")
	 private Date dateRequested;
	 
	 @Column(name="DATE_SUBMITTED")
	 private Date dateSubmitted;
	 
	 @Column(name="DATE_ACKNOWLEDGED")
	 private Date dateAcknowledged;
	 
	 @Column(name="FOLLOWUP_DATE")
	 private Date followUpDate;
	 
	 @Column(name="SCORE")
	 private String score;
	 
	 @Column(name="UPDATE_TIMESTAMP")
	 private Date updatetimestamp;
	 
	 @Column(name="UPDATE_USER")
	 private String updateUser;
	 
	 @Column(name="WAREHOUSE_LOAD_DATE")
	 private Date warehouseLoadDate;

	public String getPersonID() {
		return personID;
	}

	public void setPersonID(String personID) {
		this.personID = personID;
	}

	public Integer getTrainingNumber() {
		return trainingNumber;
	}

	public void setTrainingNumber(Integer trainingNumber) {
		this.trainingNumber = trainingNumber;
	}

	public Integer getTrainingCode() {
		return trainingCode;
	}

	public void setTrainingCode(Integer trainingCode) {
		this.trainingCode = trainingCode;
	}

	public String getTrainingDescription() {
		return trainingDescription;
	}

	public void setTrainingDescription(String trainingDescription) {
		this.trainingDescription = trainingDescription;
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

	public Date getDateAcknowledged() {
		return dateAcknowledged;
	}

	public void setDateAcknowledged(Date dateAcknowledged) {
		this.dateAcknowledged = dateAcknowledged;
	}

	public Date getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Date getUpdatetimestamp() {
		return updatetimestamp;
	}

	public void setUpdatetimestamp(Date updatetimestamp) {
		this.updatetimestamp = updatetimestamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getWarehouseLoadDate() {
		return warehouseLoadDate;
	}

	public void setWarehouseLoadDate(Date warehouseLoadDate) {
		this.warehouseLoadDate = warehouseLoadDate;
	}

}
