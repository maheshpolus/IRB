package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="MITKC_IRB_PROTO_VULNERABLE_SUB")
public class ProtocolSubject {
	@Id
	@GenericGenerator(name = "ProtocolSubjectIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProtocolSubjectIdGenerator")
	@Column(name="PROTOCOL_VULNERABLE_SUB_ID")
	private Integer protocolVulnerableSubId;
	
	@Column(name="PROTOCOL_ID")
	private Integer protocolId;
	
	@Transient
	private String acType;
	
	@Column(name="PROTOCOL_NUMBER")
	private String protocolNumber;
	
	@Column(name="SEQUENCE_NUMBER")
	private Integer sequenceNumber;
	
	@Column(name="VULNERABLE_SUBJECT_TYPE_CODE")
	private String vulnerableSubjectTypeCode;
	
	@Column(name="SUBJECT_COUNT")
	private Integer subjectCount;
	
	@Column(name="UPDATE_TIMESTAMP")
	private Date updateTimestamp;
	
	@Column(name="UPDATE_USER")
	private String updateUser;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="CRITERIA")
	private String criteria;
	
	@Column(name="AGE_GROUP_CODE")
	private Integer ageGroupCode;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "MITKC_IRB_SUBJECT_FK1"), name = "VULNERABLE_SUBJECT_TYPE_CODE", referencedColumnName = "VULNERABLE_SUBJECT_TYPE_CODE", insertable = false, updatable = false)
	ProtocolSubjectTypes protocolSubjectTypes;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "SYS_C002823877"), name = "AGE_GROUP_CODE", referencedColumnName = "AGE_GROUP_CODE", insertable = false, updatable = false)
	AgeGroups ageGroups;
	
	public Integer getAgeGroupCode() {
		return ageGroupCode;
	}

	public void setAgeGroupCode(Integer ageGroupCode) {
		this.ageGroupCode = ageGroupCode;
	}
	
	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AgeGroups getAgeGroups() {
		return ageGroups;
	}

	public void setAgeGroups(AgeGroups ageGroups) {
		this.ageGroups = ageGroups;
	}

	public Integer getProtocolVulnerableSubId() {
		return protocolVulnerableSubId;
	}

	public void setProtocolVulnerableSubId(Integer protocolVulnerableSubId) {
		this.protocolVulnerableSubId = protocolVulnerableSubId;
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

	public String getVulnerableSubjectTypeCode() {
		return vulnerableSubjectTypeCode;
	}

	public void setVulnerableSubjectTypeCode(String vulnerableSubjectTypeCode) {
		this.vulnerableSubjectTypeCode = vulnerableSubjectTypeCode;
	}

	public Integer getSubjectCount() {
		return subjectCount;
	}

	public void setSubjectCount(Integer subjectCount) {
		this.subjectCount = subjectCount;
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

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public ProtocolSubjectTypes getProtocolSubjectTypes() {
		return protocolSubjectTypes;
	}

	public void setProtocolSubjectTypes(ProtocolSubjectTypes protocolSubjectTypes) {
		this.protocolSubjectTypes = protocolSubjectTypes;
	}
}

