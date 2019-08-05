package org.mit.irb.web.IRBProtocol.VO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.pojo.AdminCheckListDetail;
import org.mit.irb.web.IRBProtocol.pojo.IRBCommitteeReviewerComments;
import org.mit.irb.web.IRBProtocol.pojo.IRBReviewComment;

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
	private String statusFlag;
	private Integer commentId;
	private Integer adminReviewId;
	private String publicFLag;
	private Integer attachmentId;
	private Integer yesVotingCount;
	private Integer noVotingCount;
	private Integer abstainCount;
	private Integer submissionTypeCode;
	private Integer sceduleId;
	private String submissionQualifierCode;
	private String submissionType;
	private String submissionReviewType;
	private String committeeName;
	private Integer scheduleId;
	private String includeInLetter;
	
	private ArrayList<HashMap<String, Object>> irbAdminsList;
	private ArrayList<HashMap<String, Object>> irbAdminsReviewers;
	private ArrayList<HashMap<String, Object>> irbAdminsReviewerType;
	private ArrayList<HashMap<String, Object>> submissionTypeList;
	private ArrayList<HashMap<String, Object>> submissionRewiewTypeList;
	private ArrayList<HashMap<String, Object>> committeeRewiewTypeList;
	private ArrayList<HashMap<String, Object>> committeeList;
	private ArrayList<HashMap<String, Object>> typeQualifierList;
	private ArrayList<HashMap<String, Object>> committeeMemberList;
	private ArrayList<HashMap<String, Object>> committeeReviewType;
	private ArrayList<HashMap<String, Object>> recomendedActionType;
	private ArrayList<HashMap<String, Object>> scheduleDates;
	
	private List<AdminCheckListDetail> irbAdminCheckList;
	private ArrayList<HashMap<String, Object>> pastSubmission;
	private ArrayList<HashMap<String, Object>> irbAdminAttachmentList;
	private ArrayList<HashMap<String, Object>> irbAdminCommentList;
	private ArrayList<HashMap<String, Object>> submissionHistory;
	private ArrayList<HashMap<String, Object>> committeeReviewers;
	private HashMap<String, Object> irbViewHeader;
	private HashMap<String, Object> submissionDetail;
	private HashMap<String, Object> committeeVotingDetails;
	private List<IRBReviewComment> irbAdminCommentAttachment;
	private ArrayList<HashMap<String, Object>> committeeReviewerCommentsandAttachment;

	private String flag;
	private String attachmentDescription;
	private Integer reviewerAttachmentId;
	private String commMinutesScheduleId;
	private IRBCommitteeReviewerComments irbCommitteeReviewerComments;
	private Integer protocolReviewerId;
	private Integer selectedRecommendedAction;
	private Integer committeeMemberOnlineReviewerId;
	private String committeeReviewerStatusCode;
	private String committeeMemberDueDate;
	private String committeeMemberAssignedDate;
	private String committeeReviewerTypeCode;
	private String markCompletedBy;
	private String completeTimeStamp;
	
	public ArrayList<HashMap<String, Object>> getPastSubmission() {
		return pastSubmission;
	}
	public void setPastSubmission(ArrayList<HashMap<String, Object>> pastSubmission) {
		this.pastSubmission = pastSubmission;
	}
	public List<AdminCheckListDetail> getIrbAdminCheckList() {
		return irbAdminCheckList;
	}
	public void setIrbAdminCheckList(List<AdminCheckListDetail> irbAdminCheckList) {
		this.irbAdminCheckList = irbAdminCheckList;
	}
	public String getSubmissionQualifierCode() {
		return submissionQualifierCode;
	}
	public void setSubmissionQualifierCode(String submissionQualifierCode) {
		this.submissionQualifierCode = submissionQualifierCode;
	}
	public Integer getSceduleId() {
		return sceduleId;
	}
	public void setSceduleId(Integer sceduleId) {
		this.sceduleId = sceduleId;
	}
	public Integer getSubmissionTypeCode() {
		return submissionTypeCode;
	}
	public void setSubmissionTypeCode(Integer submissionTypeCode) {
		this.submissionTypeCode = submissionTypeCode;
	}
	public ArrayList<HashMap<String, Object>> getRecomendedActionType() {
		return recomendedActionType;
	}
	public void setRecomendedActionType(ArrayList<HashMap<String, Object>> recomendedActionType) {
		this.recomendedActionType = recomendedActionType;
	}
	public Integer getAbstainCount() {
		return abstainCount;
	}
	public void setAbstainCount(Integer abstainCount) {
		this.abstainCount = abstainCount;
	}
	public Integer getYesVotingCount() {
		return yesVotingCount;
	}
	public void setYesVotingCount(Integer yesVotingCount) {
		this.yesVotingCount = yesVotingCount;
	}
	public Integer getNoVotingCount() {
		return noVotingCount;
	}
	public void setNoVotingCount(Integer noVotingCount) {
		this.noVotingCount = noVotingCount;
	}
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public Integer getAdminReviewId() {
		return adminReviewId;
	}
	public void setAdminReviewId(Integer adminReviewId) {
		this.adminReviewId = adminReviewId;
	}
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
	public List<IRBReviewComment> getIrbAdminCommentAttachment() {
		return irbAdminCommentAttachment;
	}
	public void setIrbAdminCommentAttachment(List<IRBReviewComment> irbAdminCommentAttachment) {
		this.irbAdminCommentAttachment = irbAdminCommentAttachment;
	}
	public ArrayList<HashMap<String, Object>> getCommitteeReviewType() {
		return committeeReviewType;
	}
	public void setCommitteeReviewType(ArrayList<HashMap<String, Object>> committeeReviewType) {
		this.committeeReviewType = committeeReviewType;
	}
	public ArrayList<HashMap<String, Object>> getScheduleDates() {
		return scheduleDates;
	}
	public void setScheduleDates(ArrayList<HashMap<String, Object>> scheduleDates) {
		this.scheduleDates = scheduleDates;
	}
	public HashMap<String, Object> getSubmissionDetail() {
		return submissionDetail;
	}
	public void setSubmissionDetail(HashMap<String, Object> submissionDetail) {
		this.submissionDetail = submissionDetail;
	}
	public ArrayList<HashMap<String, Object>> getCommitteeReviewers() {
		return committeeReviewers;
	}
	public void setCommitteeReviewers(ArrayList<HashMap<String, Object>> committeeReviewers) {
		this.committeeReviewers = committeeReviewers;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	public String getPublicFLag() {
		return publicFLag;
	}
	public void setPublicFLag(String publicFLag) {
		this.publicFLag = publicFLag;
	}
	public Integer getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}
	public HashMap<String, Object> getCommitteeVotingDetails() {
		return committeeVotingDetails;
	}
	public void setCommitteeVotingDetails(HashMap<String, Object> committeeVotingDetails) {
		this.committeeVotingDetails = committeeVotingDetails;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getAttachmentDescription() {
		return attachmentDescription;
	}
	public void setAttachmentDescription(String attachmentDescription) {
		this.attachmentDescription = attachmentDescription;
	}
	public Integer getReviewerAttachmentId() {
		return reviewerAttachmentId;
	}
	public void setReviewerAttachmentId(Integer reviewerAttachmentId) {
		this.reviewerAttachmentId = reviewerAttachmentId;
	}
	public String getCommMinutesScheduleId() {
		return commMinutesScheduleId;
	}
	public void setCommMinutesScheduleId(String commMinutesScheduleId) {
		this.commMinutesScheduleId = commMinutesScheduleId;
	}	
	public String getSubmissionType() {
		return submissionType;
	}
	public void setSubmissionType(String submissionType) {
		this.submissionType = submissionType;
	}
	public String getSubmissionReviewType() {
		return submissionReviewType;
	}
	public void setSubmissionReviewType(String submissionReviewType) {
		this.submissionReviewType = submissionReviewType;
	}
	public String getCommitteeName() {
		return committeeName;
	}
	public void setCommitteeName(String committeeName) {
		this.committeeName = committeeName;
	}
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public ArrayList<HashMap<String, Object>> getCommitteeReviewerCommentsandAttachment() {
		return committeeReviewerCommentsandAttachment;
	}
	public void setCommitteeReviewerCommentsandAttachment(
			ArrayList<HashMap<String, Object>> committeeReviewerCommentsandAttachment) {
		this.committeeReviewerCommentsandAttachment = committeeReviewerCommentsandAttachment;
	}
	public Integer getProtocolReviewerId() {
		return protocolReviewerId;
	}
	public void setProtocolReviewerId(Integer protocolReviewerId) {
		this.protocolReviewerId = protocolReviewerId;
	}
	public Integer getSelectedRecommendedAction() {
		return selectedRecommendedAction;
	}
	public void setSelectedRecommendedAction(Integer selectedRecommendedAction) {
		this.selectedRecommendedAction = selectedRecommendedAction;
	}
	public Integer getCommitteeMemberOnlineReviewerId() {
		return committeeMemberOnlineReviewerId;
	}
	public void setCommitteeMemberOnlineReviewerId(Integer committeeMemberOnlineReviewerId) {
		this.committeeMemberOnlineReviewerId = committeeMemberOnlineReviewerId;
	}
	public String getCommitteeReviewerStatusCode() {
		return committeeReviewerStatusCode;
	}
	public void setCommitteeReviewerStatusCode(String committeeReviewerStatusCode) {
		this.committeeReviewerStatusCode = committeeReviewerStatusCode;
	}
	public String getCommitteeMemberDueDate() {
		return committeeMemberDueDate;
	}
	public void setCommitteeMemberDueDate(String committeeMemberDueDate) {
		this.committeeMemberDueDate = committeeMemberDueDate;
	}
	public String getCommitteeMemberAssignedDate() {
		return committeeMemberAssignedDate;
	}
	public void setCommitteeMemberAssignedDate(String committeeMemberAssignedDate) {
		this.committeeMemberAssignedDate = committeeMemberAssignedDate;
	}
	public String getCommitteeReviewerTypeCode() {
		return committeeReviewerTypeCode;
	}
	public void setCommitteeReviewerTypeCode(String committeeReviewerTypeCode) {
		this.committeeReviewerTypeCode = committeeReviewerTypeCode;
	}
	public IRBCommitteeReviewerComments getIrbCommitteeReviewerComments() {
		return irbCommitteeReviewerComments;
	}
	public void setIrbCommitteeReviewerComments(IRBCommitteeReviewerComments irbCommitteeReviewerComments) {
		this.irbCommitteeReviewerComments = irbCommitteeReviewerComments;
	}
	public String getMarkCompletedBy() {
		return markCompletedBy;
	}
	public void setMarkCompletedBy(String markCompletedBy) {
		this.markCompletedBy = markCompletedBy;
	}
	public String getCompleteTimeStamp() {
		return completeTimeStamp;
	}
	public void setCompleteTimeStamp(String completeTimeStamp) {
		this.completeTimeStamp = completeTimeStamp;
	}
	public String getIncludeInLetter() {
		return includeInLetter;
	}
	public void setIncludeInLetter(String includeInLetter) {
		this.includeInLetter = includeInLetter;
	}
	
}