package org.mit.irb.web.roles.vo;

import java.util.List;

import org.mit.irb.web.committee.view.PersonDetailsView;

public class RoleManagementVO{
	
	private String personId;
	
	private String unitNumber;
		
	private List<RoleVO> roles;
	
	private String updateUser;

	private Integer roleId;
	
	private PersonDetailsView person;
	
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public List<RoleVO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVO> roles) {
		this.roles = roles;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public PersonDetailsView getPerson() {
		return person;
	}

	public void setPerson(PersonDetailsView person) {
		this.person = person;
	}
	
}
