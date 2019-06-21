package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.mit.irb.web.roles.pojo.Role;

@Entity
@Table(name = "IRB_PROTOCOL_PERSON_ROLES")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class IRBProtocolPersonRoles {
	@Id
	@Column(name = "PROTO_PERSON_ROLE_ID")
	private Integer protocolPersonRoleId;
	
	@Column(name = "PROTOCOL_ID")
	private Integer protocolId; 
	
	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;
	
	@Column(name = "PERSON_ID")
	private String personId;
	
	@Column(name = "PERSON_NAME")
	private String personName;
	
	@Column(name = "ROLE_ID")
	private Integer roleId;
	
	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTO_PERSON_ROLE_FK1"), name = "ROLE_ID", referencedColumnName = "ROLE_ID", insertable = false, updatable = false)
	private Role role;
	
	@Transient
	private String acType;
	
	public Integer getProtocolPersonRoleId() {
		return protocolPersonRoleId;
	}

	public void setProtocolPersonRoleId(Integer protocolPersonRoleId) {
		this.protocolPersonRoleId = protocolPersonRoleId;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}
}
