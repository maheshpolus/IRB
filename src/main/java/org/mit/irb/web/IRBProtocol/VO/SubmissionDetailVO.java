package org.mit.irb.web.IRBProtocol.VO;

import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubmissionStatuses;

public class SubmissionDetailVO {
	/*private Integer personID;
	private String protocolNumber;
	private Integer protocolId; 
	private String submissionId;
	private Integer submissionNumber; 
	private String submissionStatus;
	private String protocolStatus;
	private Integer sequenceNumber;*/
	private String updateUser;
	private boolean successCode;
	private String successMessage;
	private ProtocolSubmissionStatuses protocolSubmissionStatuses;
	private String comment;
	private String acType;
	private ArrayList<HashMap<String, Object>> irbAdminsList;
	
	/*public Integer getPersonID() {
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
	public Integer getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}
	public String getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}
	public Integer getSubmissionNumber() {
		return submissionNumber;
	}
	public void setSubmissionNumber(Integer submissionNumber) {
		this.submissionNumber = submissionNumber;
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
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}*/
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
	public ProtocolSubmissionStatuses getProtocolSubmissionStatuses() {
		return protocolSubmissionStatuses;
	}
	public void setProtocolSubmissionStatuses(ProtocolSubmissionStatuses protocolSubmissionStatuses) {
		this.protocolSubmissionStatuses = protocolSubmissionStatuses;
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
}
