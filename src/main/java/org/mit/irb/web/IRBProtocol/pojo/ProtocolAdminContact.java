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

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "IRB_PROTOCOL_ADMIN_CONTACTS") 
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolAdminContact {
	@Id
	/*@GenericGenerator(name = "ProtocolAdminContactIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProtocolAdminContactIdGenerator")*/
	@Column(name = "ADMIN_CONTACT_ID")
	private Integer adminContactId;
	
	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTO_ADMIN_CONTACTS_FK2"), name = "PROTOCOL_ID", referencedColumnName = "PROTOCOL_ID")
	private ProtocolGeneralInfo protocolGeneralInfo;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTO_ADMIN_CONTACTS_FK1"), name = "ADMIN_CONTACT_TYPE_CODE", referencedColumnName = "ADMIN_CONTACT_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolAdminContactType adminContactType;
	
	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name="SEQUENCE_NUMBER")
	private Integer sequenceNumber;
	
	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;
	
	@Column(name = "PERSON_ID")
	private String personId;
	
	@Column(name = "PERSON_NAME")
	private String personName;

	@Column(name = "NON_EMPLOYEE_FLAG")
	private String nonEmployeeFlag;
	
	@Column(name = "ADMIN_CONTACT_TYPE_CODE")
	private Integer adminContactTypeCode;
	
	@Transient
	private String acType;
	
	public Integer getAdminContactTypeCode() {
		return adminContactTypeCode;
	}

	public void setAdminContactTypeCode(Integer adminContactTypeCode) {
		this.adminContactTypeCode = adminContactTypeCode;
	}

	public String getNonEmployeeFlag() {
		return nonEmployeeFlag;
	}

	public void setNonEmployeeFlag(String nonEmployeeFlag) {
		this.nonEmployeeFlag = nonEmployeeFlag;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
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

	public Integer getAdminContactId() {
		return adminContactId;
	}

	public void setAdminContactId(Integer adminContactId) {
		this.adminContactId = adminContactId;
	}

	public ProtocolGeneralInfo getProtocolGeneralInfo() {
		return protocolGeneralInfo;
	}

	public void setProtocolGeneralInfo(ProtocolGeneralInfo protocolGeneralInfo) {
		this.protocolGeneralInfo = protocolGeneralInfo;
	}

	public ProtocolAdminContactType getAdminContactType() {
		return adminContactType;
	}

	public void setAdminContactType(ProtocolAdminContactType adminContactType) {
		this.adminContactType = adminContactType;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
}
