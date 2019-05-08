package org.mit.irb.web.IRBProtocol.VO;

import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubmissionStatuses;

public class IRBActionsVO {
	private ArrayList<HashMap<String, Object>> personActionsList;
	private Integer personID;
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
	
	public ArrayList<HashMap<String, Object>> getPersonActionsList() {
		return personActionsList;
	}

	public void setPersonActionsList(ArrayList<HashMap<String, Object>> personActionsList) {
		this.personActionsList = personActionsList;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
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
}
