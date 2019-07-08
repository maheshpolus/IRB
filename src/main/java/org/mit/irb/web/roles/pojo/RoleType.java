package org.mit.irb.web.roles.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "IRB_ROLE_TYPE")
public class RoleType implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="ROLE_TYPE_CODE")
	private String roleTypeCode;
	
	@Column(name = "DESCRIPTION")
	private String roleType;

	public String getRoleTypeCode() {
		return roleTypeCode;
	}

	public void setRoleTypeCode(String roleTypeCode) {
		this.roleTypeCode = roleTypeCode;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
 