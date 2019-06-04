package org.mit.irb.web.IRBProtocol.pojo;

public class IRBReviewAttachment {
	private String description;
	private Integer fileDataId;
	private String updateUser;
	private String fullName;
	private Integer adminReviewId;
	private Integer commentId;
	private Integer attachmentId;
	private String FileName;
	private String mimeType;
	private String updateTimeStamp;
	private String actionType;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getFileDataId() {
		return fileDataId;
	}
	public void setFileDataId(Integer fileDataId) {
		this.fileDataId = fileDataId;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Integer getAdminReviewId() {
		return adminReviewId;
	}
	public void setAdminReviewId(Integer adminReviewId) {
		this.adminReviewId = adminReviewId;
	}
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public Integer getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getUpdateTimeStamp() {
		return updateTimeStamp;
	}
	public void setUpdateTimeStamp(String updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

}
