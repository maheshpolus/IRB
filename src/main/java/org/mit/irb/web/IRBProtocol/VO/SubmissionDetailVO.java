package org.mit.irb.web.IRBProtocol.VO;

import java.util.ArrayList;
import java.util.HashMap;

public class SubmissionDetailVO {
	private Integer personID;
	private String personName;
	private String protocolNumber;
	private Integer protocolId; 
	private Integer submissionId;
	private Integer submissionNumber; 
	private Integer sequenceNumber;
	private String updateUser;
	private boolean successCode;
	private String successMessage;
	private String comment;
	private String acType;
	private String reviewTypeCode;
	private Integer adminReviewerId;
	private String selectedDate;
	private String committeeId;
	private ArrayList<HashMap<String, Object>> irbAdminsList;
	private ArrayList<HashMap<String, Object>> irbAdminsReviewers;
	private ArrayList<HashMap<String, Object>> irbAdminsReviewerType;
	private ArrayList<HashMap<String, Object>> submissionTypeList;
	private ArrayList<HashMap<String, Object>> submissionRewiewTypeList;
	private ArrayList<HashMap<String, Object>> committeeRewiewTypeList;
	private ArrayList<HashMap<String, Object>> committeeList;
	private ArrayList<HashMap<String, Object>> typeQualifierList;
	private ArrayList<HashMap<String, Object>> committeeMemberList;
	
	private ArrayList<HashMap<String, Object>> irbAdminAttachmentList;
	private ArrayList<HashMap<String, Object>> irbAdminCommentList;
	private ArrayList<HashMap<String, Object>> submissionHistory;
	private HashMap<String, Object> irbViewHeader;
	
	public ArrayList<HashMap<String, Object>> getIrbAdminAttachmentList() {
		return irbAdminAttachmentList;
	}
	public void setIrbAdminAttachmentList(ArrayList<HashMap<String, Object>> irbAdminAttachmentList) {
		this.irbAdminAttachmentList = irbAdminAttachmentList;
	}
	public ArrayList<HashMap<String, Object>> getIrbAdminCommentList() {
		return irbAdminCommentList;
	}
	public void setIrbAdminCommentList(ArrayList<HashMap<String, Object>> irbAdminCommentList) {
		this.irbAdminCommentList = irbAdminCommentList;
	}	
	public String getSelectedDate() {
		return selectedDate;
	}
	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}
	public String getCommitteeId() {
		return committeeId;
	}
	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}
	public ArrayList<HashMap<String, Object>> getSubmissionTypeList() {
		return submissionTypeList;
	}
	public void setSubmissionTypeList(ArrayList<HashMap<String, Object>> submissionTypeList) {
		this.submissionTypeList = submissionTypeList;
	}
	public ArrayList<HashMap<String, Object>> getCommitteeRewiewTypeList() {
		return committeeRewiewTypeList;
	}
	public void setCommitteeRewiewTypeList(ArrayList<HashMap<String, Object>> committeeRewiewTypeList) {
		this.committeeRewiewTypeList = committeeRewiewTypeList;
	}
	public ArrayList<HashMap<String, Object>> getCommitteeList() {
		return committeeList;
	}
	public void setCommitteeList(ArrayList<HashMap<String, Object>> committeeList) {
		this.committeeList = committeeList;
	}
	public ArrayList<HashMap<String, Object>> getTypeQualifierList() {
		return typeQualifierList;
	}
	public void setTypeQualifierList(ArrayList<HashMap<String, Object>> typeQualifierList) {
		this.typeQualifierList = typeQualifierList;
	}
	public String getProtocolNumber() {
		return protocolNumber;
	}
	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}
	public Integer getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}
	public Integer getSubmissionNumber() {
		return submissionNumber;
	}
	public void setSubmissionNumber(Integer submissionNumber) {
		this.submissionNumber = submissionNumber;
	}
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public Integer getPersonID() {
		return personID;
	}
	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public boolean isSuccessCode() {
		return successCode;
	}
	public void setSuccessCode(boolean successCode) {
		this.successCode = successCode;
	}
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAcType() {
		return acType;
	}
	public void setAcType(String acType) {
		this.acType = acType;
	}
	public ArrayList<HashMap<String, Object>> getIrbAdminsList() {
		return irbAdminsList;
	}
	public void setIrbAdminsList(ArrayList<HashMap<String, Object>> irbAdminsList) {
		this.irbAdminsList = irbAdminsList;
	}
	public HashMap<String, Object> getIrbViewHeader() {
		return irbViewHeader;
	}
	public void setIrbViewHeader(HashMap<String, Object> irbViewHeader) {
		this.irbViewHeader = irbViewHeader;
	}
	public String getReviewTypeCode() {
		return reviewTypeCode;
	}
	public void setReviewTypeCode(String reviewTypeCode) {
		this.reviewTypeCode = reviewTypeCode;
	}
	public ArrayList<HashMap<String, Object>> getIrbAdminsReviewers() {
		return irbAdminsReviewers;
	}
	public void setIrbAdminsReviewers(ArrayList<HashMap<String, Object>> irbAdminsReviewers) {
		this.irbAdminsReviewers = irbAdminsReviewers;
	}
	public Integer getAdminReviewerId() {
		return adminReviewerId;
	}
	public void setAdminReviewerId(Integer adminReviewerId) {
		this.adminReviewerId = adminReviewerId;
	}
	public ArrayList<HashMap<String, Object>> getSubmissionRewiewTypeList() {
		return submissionRewiewTypeList;
	}
	public void setSubmissionRewiewTypeList(ArrayList<HashMap<String, Object>> submissionRewiewTypeList) {
		this.submissionRewiewTypeList = submissionRewiewTypeList;
	}
	public ArrayList<HashMap<String, Object>> getIrbAdminsReviewerType() {
		return irbAdminsReviewerType;
	}
	public void setIrbAdminsReviewerType(ArrayList<HashMap<String, Object>> irbAdminsReviewerType) {
		this.irbAdminsReviewerType = irbAdminsReviewerType;
	}
	public ArrayList<HashMap<String, Object>> getCommitteeMemberList() {
		return committeeMemberList;
	}
	public void setCommitteeMemberList(ArrayList<HashMap<String, Object>> committeeMemberList) {
		this.committeeMemberList = committeeMemberList;
	}
	public ArrayList<HashMap<String, Object>> getSubmissionHistory() {
		return submissionHistory;
	}
	public void setSubmissionHistory(ArrayList<HashMap<String, Object>> submissionHistory) {
		this.submissionHistory = submissionHistory;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public Integer getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}
}
