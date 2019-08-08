package org.mit.irb.web.IRBProtocol.VO;

import java.util.List;

import org.mit.irb.web.IRBProtocol.pojo.IRBProtocolPersonRoles;
import org.mit.irb.web.roles.pojo.Role;

public class IRBPermissionVO {
	//private List<String> permissions;
	private String department;
	private String personId;
	private List<Role> protocolRoles;
	private IRBProtocolPersonRoles protocolRolePerson;
	private List<IRBProtocolPersonRoles> protocolRolePersonList;
	private boolean successCode;
	private String successMessage;
	private Integer protocolId;
	private String protocolNumber;
	private String acType;
	
	/*public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}*/
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
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
	public List<Role> getProtocolRoles() {
		return protocolRoles;
	}
	public void setProtocolRoles(List<Role> protocolRoles) {
		this.protocolRoles = protocolRoles;
	}
	public IRBProtocolPersonRoles getProtocolRolePerson() {
		return protocolRolePerson;
	}
	public void setProtocolRolePerson(IRBProtocolPersonRoles protocolRolePerson) {
		this.protocolRolePerson = protocolRolePerson;
	}
	public List<IRBProtocolPersonRoles> getProtocolRolePersonList() {
		return protocolRolePersonList;
	}
	public void setProtocolRolePersonList(List<IRBProtocolPersonRoles> protocolRolePersonList) {
		this.protocolRolePersonList = protocolRolePersonList;
	}
	public Integer getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}
	public String getAcType() {
		return acType;
	}
	public void setAcType(String acType) {
		this.acType = acType;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getProtocolNumber() {
		return protocolNumber;
	}
	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}
}
