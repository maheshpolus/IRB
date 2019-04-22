package org.mit.irb.web.IRBProtocol.pojo;

public class PersonTrainingComments {

	private String acType;
	private Integer commentId;
	private Integer personTrainingId;
	private String commentDescription;
	private String updateUser;
	
	public String getAcType() {
		return acType;
	}
	public void setAcType(String acType) {
		this.acType = acType;
	}
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public Integer getPersonTrainingId() {
		return personTrainingId;
	}
	public void setPersonTrainingId(Integer personTrainingId) {
		this.personTrainingId = personTrainingId;
	}
	public String getCommentDescription() {
		return commentDescription;
	}
	public void setCommentDescription(String commentDescription) {
		this.commentDescription = commentDescription;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
