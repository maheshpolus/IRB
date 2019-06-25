package org.mit.irb.web.roles.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//import com.polus.fibicomp.grantcall.pojo.GrantCallStatus;

@Entity
@Table(name = "IRB_ROLE")
public class Role implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ROLE_ID")
    private Integer roleId;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name="DESCRIPTION")
	private String description;

    @Column(name="STATUS_FLAG")
	private String statusFlag;
    
	@Column(name="ROLE_TYPE_CODE")
	private String roleTypeCode;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_ROLE_FK1"), name = "ROLE_TYPE_CODE", referencedColumnName = "ROLE_TYPE_CODE", insertable = false, updatable = false)
	private RoleType roleType;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String getRoleTypeCode() {
		return roleTypeCode;
	}

	public void setRoleTypeCode(String roleTypeCode) {
		this.roleTypeCode = roleTypeCode;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
