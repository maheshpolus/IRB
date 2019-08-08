package org.mit.irb.web.IRBProtocol.VO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.pojo.IRBCommitteeReviewerComments;
import org.mit.irb.web.IRBProtocol.pojo.IRBProtocolRiskLevel;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolRenewalDetails;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubmissionStatuses;

public class IRBActionsVO {
	private ArrayList<HashMap<String, Object>> personActionsList;
	private HashMap<String, Object> personAction;
	private String personID;
	private String protocolNumber;
	private String submissionStatus;
	private String protocolStatus;
	private String actionTypeCode;
	private String prevSubmissonStatusCode;
	private String prevProtocolStatusCode;
	private String followUpActionCode;
	private String createUser;
	private String updateUser;
	private String acType;
	private boolean successCode;
	private String successMessage;
	private ProtocolSubmissionStatuses protocolSubmissionStatuses;
	private Integer sequenceNumber;
	private Integer submissionNumber; 
	private String comment;
	private Integer protocolId; 
	private ArrayList<HashMap<String, Object>> moduleAvailableForAmendment;
	private ArrayList<HashMap<String, Object>> notifyTypeQualifier;
	private ArrayList<HashMap<String, Object>> amendRenewalModules;
	private String selectedTypeQualifier;
	private String submissionId;
	private String actionDate;
	private String templateTypeCode;
	private String decisionDate;
	private String approvalDate;
	private String expirationDate;
	private ArrayList<HashMap<String, Object>> scheduleDates;
	private ArrayList<HashMap<String, Object>> committeeList;
	private ArrayList<HashMap<String, Object>> riskLevelType;
	private ArrayList<HashMap<String, Object>> fdaRiskLevelType;
	private ArrayList<HashMap<String, Object>> expeditedApprovalCheckList;
	private List<String> expeditedCheckListSelectedCode;
	private IRBProtocolRiskLevel riskLevelDetail;
	private String selectedCommitteeId;
	private String selectedScheduleId;	
	private String publicFlag;
	private ArrayList<HashMap<String, Object>> expeditedCannedComments;
	private List<IRBCommitteeReviewerComments> irbActionsReviewerComments;
	private String attachmentDescription;
	private HashMap<String, Object> protocolHeaderDetails;
	private String correspTemplateTypeCode;
	private String correspTypeDescription;
	private Integer actionId;
	private ProtocolGeneralInfo protocolInfo;
	private boolean flag; 
	private ProtocolRenewalDetails protocolRenewalDetails;
	
	public ArrayList<HashMap<String, Object>> getFdaRiskLevelType() {
		return fdaRiskLevelType;
	}

	public void setFdaRiskLevelType(ArrayList<HashMap<String, Object>> fdaRiskLevelType) {
		this.fdaRiskLevelType = fdaRiskLevelType;
	}

	public ArrayList<HashMap<String, Object>> getPersonActionsList() {
		return personActionsList;
	}

	public void setPersonActionsList(ArrayList<HashMap<String, Object>> personActionsList) {
		this.personActionsList = personActionsList;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getSubmissionStatus() {
		return submissionStatus;
	}

	public void setSubmissionStatus(String submissionStatus) {
		this.submissionStatus = submissionStatus;
	}

	public String getProtocolStatus() {
		return protocolStatus;
	}

	public void setProtocolStatus(String protocolStatus) {
		this.protocolStatus = protocolStatus;
	}

	public String getActionTypeCode() {
		return actionTypeCode;
	}

	public void setActionTypeCode(String actionTypeCode) {
		this.actionTypeCode = actionTypeCode;
	}

	public String getPrevSubmissonStatusCode() {
		return prevSubmissonStatusCode;
	}

	public void setPrevSubmissonStatusCode(String prevSubmissonStatusCode) {
		this.prevSubmissonStatusCode = prevSubmissonStatusCode;
	}

	public String getFollowUpActionCode() {
		return followUpActionCode;
	}

	public void setFollowUpActionCode(String followUpActionCode) {
		this.followUpActionCode = followUpActionCode;
	}
	
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public String getPrevProtocolStatusCode() {
		return prevProtocolStatusCode;
	}

	public void setPrevProtocolStatusCode(String prevProtocolStatusCode) {
		this.prevProtocolStatusCode = prevProtocolStatusCode;
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

	public ProtocolSubmissionStatuses getProtocolSubmissionStatuses() {
		return protocolSubmissionStatuses;
	}

	public void setProtocolSubmissionStatuses(ProtocolSubmissionStatuses protocolSubmissionStatuses) {
		this.protocolSubmissionStatuses = protocolSubmissionStatuses;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Integer getSubmissionNumber() {
		return submissionNumber;
	}

	public void setSubmissionNumber(Integer submissionNumber) {
		this.submissionNumber = submissionNumber;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public ArrayList<HashMap<String, Object>> getModuleAvailableForAmendment() {
		return moduleAvailableForAmendment;
	}

	public void setModuleAvailableForAmendment(ArrayList<HashMap<String, Object>> moduleAvailableForAmendment) {
		this.moduleAvailableForAmendment = moduleAvailableForAmendment;
	}

	public ArrayList<HashMap<String, Object>> getNotifyTypeQualifier() {
		return notifyTypeQualifier;
	}

	public void setNotifyTypeQualifier(ArrayList<HashMap<String, Object>> notifyTypeQualifier) {
		this.notifyTypeQualifier = notifyTypeQualifier;
	}

	public ArrayList<HashMap<String, Object>> getAmendRenewalModules() {
		return amendRenewalModules;
	}

	public void setAmendRenewalModules(ArrayList<HashMap<String, Object>> amendRenewalModules) {
		this.amendRenewalModules = amendRenewalModules;
	}

	public String getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}

	public HashMap<String, Object> getPersonAction() {
		return personAction;
	}

	public void setPersonAction(HashMap<String, Object> personAction) {
		this.personAction = personAction;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public String getTemplateTypeCode() {
		return templateTypeCode;
	}

	public void setTemplateTypeCode(String templateTypeCode) {
		this.templateTypeCode = templateTypeCode;
	}

	public String getDecisionDate() {
		return decisionDate;
	}

	public void setDecisionDate(String decisionDate) {
		this.decisionDate = decisionDate;
	}

	public String getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public ArrayList<HashMap<String, Object>> getScheduleDates() {
		return scheduleDates;
	}

	public void setScheduleDates(ArrayList<HashMap<String, Object>> scheduleDates) {
		this.scheduleDates = scheduleDates;
	}

	public ArrayList<HashMap<String, Object>> getCommitteeList() {
		return committeeList;
	}

	public void setCommitteeList(ArrayList<HashMap<String, Object>> committeeList) {
		this.committeeList = committeeList;
	}

	public ArrayList<HashMap<String, Object>> getExpeditedApprovalCheckList() {
		return expeditedApprovalCheckList;
	}

	public void setExpeditedApprovalCheckList(ArrayList<HashMap<String, Object>> expeditedApprovalCheckList) {
		this.expeditedApprovalCheckList = expeditedApprovalCheckList;
	}

	public List<String> getExpeditedCheckListSelectedCode() {
		return expeditedCheckListSelectedCode;
	}

	public void setExpeditedCheckListSelectedCode(List<String> expeditedCheckListSelectedCode) {
		this.expeditedCheckListSelectedCode = expeditedCheckListSelectedCode;
	}

	public String getSelectedCommitteeId() {
		return selectedCommitteeId;
	}

	public void setSelectedCommitteeId(String selectedCommitteeId) {
		this.selectedCommitteeId = selectedCommitteeId;
	}

	public String getSelectedScheduleId() {
		return selectedScheduleId;
	}

	public void setSelectedScheduleId(String selectedScheduleId) {
		this.selectedScheduleId = selectedScheduleId;
	}

	public ArrayList<HashMap<String, Object>> getExpeditedCannedComments() {
		return expeditedCannedComments;
	}

	public void setExpeditedCannedComments(ArrayList<HashMap<String, Object>> expeditedCannedComments) {
		this.expeditedCannedComments = expeditedCannedComments;
	}

	public String getSelectedTypeQualifier() {
		return selectedTypeQualifier;
	}

	public void setSelectedTypeQualifier(String selectedTypeQualifier) {
		this.selectedTypeQualifier = selectedTypeQualifier;
	}

	public List<IRBCommitteeReviewerComments> getIrbActionsReviewerComments() {
		return irbActionsReviewerComments;
	}

	public void setIrbActionsReviewerComments(List<IRBCommitteeReviewerComments> irbActionsReviewerComments) {
		this.irbActionsReviewerComments = irbActionsReviewerComments;
	}

	public ArrayList<HashMap<String, Object>> getRiskLevelType() {
		return riskLevelType;
	}

	public void setRiskLevelType(ArrayList<HashMap<String, Object>> riskLevelType) {
		this.riskLevelType = riskLevelType;
	}

	public IRBProtocolRiskLevel getRiskLevelDetail() {
		return riskLevelDetail;
	}

	public void setRiskLevelDetail(IRBProtocolRiskLevel riskLevelDetail) {
		this.riskLevelDetail = riskLevelDetail;
	}

	public String getAttachmentDescription() {
		return attachmentDescription;
	}

	public void setAttachmentDescription(String attachmentDescription) {
		this.attachmentDescription = attachmentDescription;
	}

	public String getPublicFlag() {
		return publicFlag;
	}

	public void setPublicFlag(String publicFlag) {
		this.publicFlag = publicFlag;
	}

	public HashMap<String, Object> getProtocolHeaderDetails() {
		return protocolHeaderDetails;
	}

	public void setProtocolHeaderDetails(HashMap<String, Object> protocolHeaderDetails) {
		this.protocolHeaderDetails = protocolHeaderDetails;
	}

	public String getCorrespTemplateTypeCode() {
		return correspTemplateTypeCode;
	}

	public void setCorrespTemplateTypeCode(String correspTemplateTypeCode) {
		this.correspTemplateTypeCode = correspTemplateTypeCode;
	}

	public String getCorrespTypeDescription() {
		return correspTypeDescription;
	}

	public void setCorrespTypeDescription(String correspTypeDescription) {
		this.correspTypeDescription = correspTypeDescription;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public String getPersonID() {
		return personID;
	}

	public void setPersonID(String personID) {
		this.personID = personID;
	}

	public ProtocolGeneralInfo getProtocolInfo() {
		return protocolInfo;
	}

	public void setProtocolInfo(ProtocolGeneralInfo protocolInfo) {
		this.protocolInfo = protocolInfo;
	}
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public ProtocolRenewalDetails getProtocolRenewalDetails() {
		return protocolRenewalDetails;
	}

	public void setProtocolRenewalDetails(ProtocolRenewalDetails protocolRenewalDetails) {
		this.protocolRenewalDetails = protocolRenewalDetails;
	}
}
