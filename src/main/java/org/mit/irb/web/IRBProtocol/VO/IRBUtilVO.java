package org.mit.irb.web.IRBProtocol.VO;

import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.pojo.PersonTraining;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;


public class IRBUtilVO {
private List<HashMap<String, Object>> personTrainingList;
private HashMap<String, Object> personnelTrainingInfo;
private List<HashMap<String, Object>> personnelTrainingComment;
private List<HashMap<String, Object>> personnelTrainingAttachments;
private List<HashMap<String, Object>> trainingDesc;
private PersonTraining personTraining;
private PersonTrainingComments personTrainingComments;
private Integer personTrainingId;
private String updateUser;
private String searchMode;
private Integer trainingCode;
private String personId;
private String dateAcknowledged;
private String followUpDate;

public List<HashMap<String, Object>> getPersonTrainingList() {
	return personTrainingList;
}

public void setPersonTrainingList(List<HashMap<String, Object>> personTrainingList) {
	this.personTrainingList = personTrainingList;
}

public PersonTraining getPersonTraining() {
	return personTraining;
}

public void setPersonTraining(PersonTraining personTraining) {
	this.personTraining = personTraining;
}

public String getUpdateUser() {
	return updateUser;
}

public void setUpdateUser(String updateUser) {
	this.updateUser = updateUser;
}

public Integer getPersonTrainingId() {
	return personTrainingId;
}

public void setPersonTrainingId(Integer personTrainingId) {
	this.personTrainingId = personTrainingId;
}

public List<HashMap<String, Object>> getPersonnelTrainingComment() {
	return personnelTrainingComment;
}

public void setPersonnelTrainingComment(List<HashMap<String, Object>> personnelTrainingComment) {
	this.personnelTrainingComment = personnelTrainingComment;
}

public List<HashMap<String, Object>> getPersonnelTrainingAttachments() {
	return personnelTrainingAttachments;
}

public void setPersonnelTrainingAttachments(List<HashMap<String, Object>> personnelTrainingAttachments) {
	this.personnelTrainingAttachments = personnelTrainingAttachments;
}

public List<HashMap<String, Object>> getTrainingDesc() {
	return trainingDesc;
}

public void setTrainingDesc(List<HashMap<String, Object>> trainingDesc) {
	this.trainingDesc = trainingDesc;
}

public String getSearchMode() {
	return searchMode;
}

public void setSearchMode(String searchMode) {
	this.searchMode = searchMode;
}

public Integer getTrainingCode() {
	return trainingCode;
}

public void setTrainingCode(Integer trainingCode) {
	this.trainingCode = trainingCode;
}

public String getPersonId() {
	return personId;
}

public void setPersonId(String personId) {
	this.personId = personId;
}

public PersonTrainingComments getPersonTrainingComments() {
	return personTrainingComments;
}

public void setPersonTrainingComments(PersonTrainingComments personTrainingComments) {
	this.personTrainingComments = personTrainingComments;
}

public HashMap<String, Object> getPersonnelTrainingInfo() {
	return personnelTrainingInfo;
}

public void setPersonnelTrainingInfo(HashMap<String, Object> personnelTrainingInfo) {
	this.personnelTrainingInfo = personnelTrainingInfo;
}

public String getDateAcknowledged() {
	return dateAcknowledged;
}

public void setDateAcknowledged(String dateAcknowledged) {
	this.dateAcknowledged = dateAcknowledged;
}

public String getFollowUpDate() {
	return followUpDate;
}

public void setFollowUpDate(String followUpDate) {
	this.followUpDate = followUpDate;
}
}
