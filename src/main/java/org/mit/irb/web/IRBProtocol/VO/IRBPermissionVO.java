package org.mit.irb.web.IRBProtocol.VO;

import java.util.List;

public class IRBPermissionVO {
	private List<String> permissions;
	private String department;
	private String username;
	private boolean successCode;
	private String successMessage;
	
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
}
