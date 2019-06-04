package org.mit.irb.web.IRBProtocol.pojo;

import java.util.List;

public class IRBReviewComment {
	private Integer submissionId;
	private Integer sequenceNumber;
	private String commentDescription;
	private String protocolNumber;
	private String udpateUser;
	private String fullName;
	private Integer commentId;
	private Integer adminReviewId;
	private String updateTimestamp;
	private Integer protocolId;
	private String actionType;
	private List<IRBReviewAttachment> reviewAttachments;
	
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getUdpateUser() {
		return udpateUser;
	}

	public void setUdpateUser(String udpateUser) {
		this.udpateUser = udpateUser;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public String getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public List<IRBReviewAttachment> getReviewAttachments() {
		return reviewAttachments;
	}

	public void setReviewAttachments(List<IRBReviewAttachment> reviewAttachments) {
		this.reviewAttachments = reviewAttachments;
	}

	public String getCommentDescription() {
		return commentDescription;
	}

	public void setCommentDescription(String commentDescription) {
		this.commentDescription = commentDescription;
	}
}
