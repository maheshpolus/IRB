package org.mit.irb.web.roles.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ROLE_RIGHTS")
public class RoleRights implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "ROLE_RIGHTS_ID")
	private Integer roleRightId;
	
	@Column(name = "RIGHT_ID")
	private Integer rightId;
	
	@Column(name = "ROLE_ID")
	private Integer roleId;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "ROLE_RIGHTS_FK1"), name = "ROLE_ID", referencedColumnName = "ROLE_ID", insertable = false, updatable = false)
	private Role role;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "ROLE_RIGHTS_FK2"), name = "RIGHT_ID", referencedColumnName = "RIGHT_ID", insertable = false, updatable = false)
	private Rights rights;

	public Integer getRoleRightId() {
		return roleRightId;
	}

	public void setRoleRightId(Integer roleRightId) {
		this.roleRightId = roleRightId;
	}

	public Integer getRightId() {
		return rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Rights getRights() {
		return rights;
	}

	public void setRights(Rights rights) {
		this.rights = rights;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	}
