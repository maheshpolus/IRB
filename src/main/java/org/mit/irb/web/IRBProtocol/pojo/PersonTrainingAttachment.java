package org.mit.irb.web.IRBProtocol.pojo;

public class PersonTrainingAttachment {

	private String acType;
	private Integer attachmentId;
	private Integer personTrainingId;
	private String description;
	private String fileName;
	private String mimeType;
	private String updateUser;
	
	public String getAcType() {
		return acType;
	}
	public void setAcType(String acType) {
		this.acType = acType;
	}
	public Integer getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}
	public Integer getPersonTrainingId() {
		return personTrainingId;
	}
	public void setPersonTrainingId(Integer personTrainingId) {
		this.personTrainingId = personTrainingId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
