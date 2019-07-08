package org.mit.irb.web.IRBProtocol.pojo;

public class AdminCheckListDetail {
	private String groupName;
	private String description;
	private Integer adminRevChecklistCode;
	private Boolean statusFlag;
	private String updateUser;
	private String updateTimestamp;
	private Integer adminCheckListId;
	private String userDescription;
	private String actype;
	
	public Integer getAdminCheckListId() {
		return adminCheckListId;
	}
	public void setAdminCheckListId(Integer adminCheckListId) {
		this.adminCheckListId = adminCheckListId;
	}
	public String getActype() {
		return actype;
	}
	public void setActype(String actype) {
		this.actype = actype;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getAdminRevChecklistCode() {
		return adminRevChecklistCode;
	}
	public void setAdminRevChecklistCode(Integer adminRevChecklistCode) {
		this.adminRevChecklistCode = adminRevChecklistCode;
	}
	public Boolean getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(Boolean statusFlag) {
		this.statusFlag = statusFlag;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	public String getUserDescription() {
		return userDescription;
	}
	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}
}
