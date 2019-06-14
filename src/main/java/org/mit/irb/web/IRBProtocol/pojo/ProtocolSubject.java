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


@Entity
@Table(name="IRB_PROTO_VULNERABLE_SUB")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolSubject {
	@Id
	/*@GenericGenerator(name = "ProtocolSubjectIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProtocolSubjectIdGenerator")*/
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
	
	@Column(name="MIN_AGE")
	private String minAge;
	
	@Column(name="MAX_AGE")
	private String maxAge;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_SUBJECT_FK1"), name = "VULNERABLE_SUBJECT_TYPE_CODE", referencedColumnName = "VULNERABLE_SUBJECT_TYPE_CODE", insertable = false, updatable = false)
	ProtocolSubjectTypes protocolSubjectTypes;
	
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

	public String getMinAge() {
		return minAge;
	}

	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}

	public String getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}
}

