package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.ForeignKey;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "IRB_PROTOCOL_UNITS")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolLeadUnits {
	@Id
	/*@GenericGenerator(name = "ProtocolLeadUnitIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProtocolLeadUnitIdGenerator")*/
	@Column(name = "PROTOCOL_UNITS_ID")
	private Integer protocolUnitsId;

	/*@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTOCOL_UNITS_FK1"), name = "PROTOCOL_PERSON_ID", referencedColumnName = "PROTOCOL_PERSON_ID")
	private ProtocolPersonnelInfo personnelInfo;*/

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTOCOL_UNITS_FK1"), name = "UNIT_TYPE_CODE", referencedColumnName = "UNIT_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolUnitType unitType;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTOCOL_UNITS_FK2"), name = "UNIT_NUMBER", referencedColumnName = "UNIT_NUMBER", insertable = false, updatable = false)
	private ProtocolPersonLeadUnits protocolPersonLeadUnits;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTOCOL_UNITS_FK3"), name = "PROTOCOL_ID", referencedColumnName = "PROTOCOL_ID")
	private ProtocolGeneralInfo protocolGeneralInfo;
	
	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "UNIT_NUMBER")
	private String unitNumber;

	@Column(name = "UNIT_NAME")
	private String unitName;
	/*@Column(name = "LEAD_UNIT_FLAG")
	private String leadUnitFlag;*/

	/*@Column(name = "PERSON_ID")
	private String person_id;*/

	@Column(name = "UNIT_TYPE_CODE")
	private String unitTypeCode;
	
	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Transient
	private String acType;
	/*@Column(name = "PROTOCOL_ID")
	private Integer protocolId;*/

	public String getDescription() {
		return description;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getProtocolUnitsId() {
		return protocolUnitsId;
	}

	public void setProtocolUnitsId(Integer protocolUnitsId) {
		this.protocolUnitsId = protocolUnitsId;
	}

	public ProtocolGeneralInfo getProtocolGeneralInfo() {
		return protocolGeneralInfo;
	}

	public void setProtocolGeneralInfo(ProtocolGeneralInfo protocolGeneralInfo) {
		this.protocolGeneralInfo = protocolGeneralInfo;
	}

	public String getUnitTypeCode() {
		return unitTypeCode;
	}

	public void setUnitTypeCode(String unitTypeCode) {
		this.unitTypeCode = unitTypeCode;
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

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	/*public String getLeadUnitFlag() {
		return leadUnitFlag;
	}

	public void setLeadUnitFlag(String leadUnitFlag) {
		this.leadUnitFlag = leadUnitFlag;
	}*/

	/*public String getPerson_id() {
		return person_id;
	}

	public void setPerson_id(String person_id) {
		this.person_id = person_id;
	}*/

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

	/*public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}*/

	/*public ProtocolPersonnelInfo getPersonnelInfo() {
		return personnelInfo;
	}

	public void setPersonnelInfo(ProtocolPersonnelInfo personnelInfo) {
		this.personnelInfo = personnelInfo;
	}*/

	public ProtocolPersonLeadUnits getProtocolPersonLeadUnits() {
		return protocolPersonLeadUnits;
	}

	public void setProtocolPersonLeadUnits(ProtocolPersonLeadUnits protocolPersonLeadUnits) {
		this.protocolPersonLeadUnits = protocolPersonLeadUnits;
	}
	
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public ProtocolUnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(ProtocolUnitType unitType) {
		this.unitType = unitType;
	}
	
}
