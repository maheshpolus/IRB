package org.mit.irb.web.IRBProtocol.VO;

import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.pojo.PersonTraining;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolRenewalDetails;


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
private HashMap<String, Object> protocolSubmissionDetails;
private List<HashMap<String, Object>> protocolSubmissionReviewers;
private ProtocolRenewalDetails protocolRenewalDetail;
private List<HashMap<String, Object>> protocolRenewalComments;
private List<HashMap<String, Object>> protocolReviewerComments;
private List<HashMap<String, Object>> submissionCheckListData;

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

public HashMap<String, Object> getProtocolSubmissionDetails() {
	return protocolSubmissionDetails;
}

public void setProtocolSubmissionDetails(HashMap<String, Object> protocolSubmissionDetails) {
	this.protocolSubmissionDetails = protocolSubmissionDetails;
}

public List<HashMap<String, Object>> getProtocolSubmissionReviewers() {
	return protocolSubmissionReviewers;
}

public void setProtocolSubmissionReviewers(List<HashMap<String, Object>> protocolSubmissionReviewers) {
	this.protocolSubmissionReviewers = protocolSubmissionReviewers;
}

public ProtocolRenewalDetails getProtocolRenewalDetail() {
	return protocolRenewalDetail;
}

public void setProtocolRenewalDetail(ProtocolRenewalDetails protocolRenewalDetail) {
	this.protocolRenewalDetail = protocolRenewalDetail;
}

public List<HashMap<String, Object>> getProtocolRenewalComments() {
	return protocolRenewalComments;
}

public void setProtocolRenewalComments(List<HashMap<String, Object>> protocolRenewalComments) {
	this.protocolRenewalComments = protocolRenewalComments;
}

public List<HashMap<String, Object>> getProtocolReviewerComments() {
	return protocolReviewerComments;
}

public void setProtocolReviewerComments(List<HashMap<String, Object>> protocolReviewerComments) {
	this.protocolReviewerComments = protocolReviewerComments;
}

public List<HashMap<String, Object>> getSubmissionCheckListData() {
	return submissionCheckListData;
}

public void setSubmissionCheckListData(List<HashMap<String, Object>> submissionCheckListData) {
	this.submissionCheckListData = submissionCheckListData;
}
}
