package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Clob;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/*
 * Class to fetch personnel information of IRB protocol persons 
 * @author anu
*/

@Entity
@Table(name = "MITKC_IRB_PROTOCOL_PERSONS")
public class ProtocolPersonnelInfo {
	@Id
	@GenericGenerator(name = "ProtocolPersonnelInfoIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProtocolPersonnelInfoIdGenerator")
	@Column(name = "PROTOCOL_PERSON_ID")
	private Integer protocolPersonId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "MITKC_IRB_PROTOCOL_PERSONS_FK1"), name = "PROTOCOL_ID", referencedColumnName = "PROTOCOL_ID")
	private ProtocolGeneralInfo protocolGeneralInfo;

	@Column(name = "AFFILIATION_TYPE_CODE")
	private Integer affiliationTypeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "MITKC_IRB_PROTOCOL_PERSONS_FK3"), name = "AFFILIATION_TYPE_CODE", referencedColumnName = "AFFILIATION_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolAffiliationTypes protocolAffiliationTypes;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "MITKC_IRB_ROLE_FK1"), name = "PROTOCOL_PERSON_ROLE_ID", referencedColumnName = "PROTOCOL_PERSON_ROLE_ID", insertable = false, updatable = false)
	private ProtocolPersonRoleTypes protocolPersonRoleTypes;

	@JsonManagedReference
	@OneToMany(mappedBy = "personnelInfo", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProtocolLeadUnits> protocolLeadUnits;

	@Transient
	private String acType;
	
	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;
	
	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "PROTOCOL_PERSON_ROLE_ID")
	private String protocolPersonRoleId;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "DIRECTORY_TITLE")
	private String directoryTitle;

	@Column(name = "PRIMARY_TITLE")
	private String primaryTitle;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "HOME_UNIT")
	private String homeUnit;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "PERSON_NAME")
	private String personName;

	public Integer getProtocolPersonId() {
		return protocolPersonId;
	}

	public void setProtocolPersonId(Integer protocolPersonId) {
		this.protocolPersonId = protocolPersonId;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Integer getAffiliationTypeCode() {
		return affiliationTypeCode;
	}

	public void setAffiliationTypeCode(Integer affiliationTypeCode) {
		this.affiliationTypeCode = affiliationTypeCode;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getProtocolPersonRoleId() {
		return protocolPersonRoleId;
	}

	public void setProtocolPersonRoleId(String protocolPersonRoleId) {
		this.protocolPersonRoleId = protocolPersonRoleId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getDirectoryTitle() {
		return directoryTitle;
	}

	public void setDirectoryTitle(String directoryTitle) {
		this.directoryTitle = directoryTitle;
	}

	public String getPrimaryTitle() {
		return primaryTitle;
	}

	public void setPrimaryTitle(String primaryTitle) {
		this.primaryTitle = primaryTitle;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getHomeUnit() {
		return homeUnit;
	}

	public void setHomeUnit(String homeUnit) {
		this.homeUnit = homeUnit;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}
	
	public ProtocolAffiliationTypes getProtocolAffiliationTypes() {
		return protocolAffiliationTypes;
	}

	public void setProtocolAffiliationTypes(ProtocolAffiliationTypes protocolAffiliationTypes) {
		this.protocolAffiliationTypes = protocolAffiliationTypes;
	}

	public ProtocolGeneralInfo getProtocolGeneralInfo() {
		return protocolGeneralInfo;
	}

	public void setProtocolGeneralInfo(ProtocolGeneralInfo protocolGeneralInfo) {
		this.protocolGeneralInfo = protocolGeneralInfo;
	}

	public List<ProtocolLeadUnits> getProtocolLeadUnits() {
		return protocolLeadUnits;
	}

	public void setProtocolLeadUnits(List<ProtocolLeadUnits> protocolLeadUnits) {
		this.protocolLeadUnits = protocolLeadUnits;
	}

	public ProtocolPersonRoleTypes getProtocolPersonRoleTypes() {
		return protocolPersonRoleTypes;
	}

	public void setProtocolPersonRoleTypes(ProtocolPersonRoleTypes protocolPersonRoleTypes) {
		this.protocolPersonRoleTypes = protocolPersonRoleTypes;
	}
}
