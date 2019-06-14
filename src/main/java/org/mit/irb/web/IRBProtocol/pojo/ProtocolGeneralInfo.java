package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/*
 * Class to fetch general information of IRB protocol persons 
 * @author anu
*/

@Entity
@Table(name = "IRB_PROTOCOL")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolGeneralInfo {
	@Id
	/*@GenericGenerator(name = "ProtocolIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProtocolIdGenerator")*/
	@Column(name = "PROTOCOL_ID")
	private Integer protocolId;

	@Column(name = "PROTOCOL_TYPE_CODE")
	private String protocolTypeCode;

	@Column(name = "START_DATE")
	private Date protocolStartDate;

	@Column(name = "END_DATE")
	private Date protocolEndDate;

	@Column(name = "TITLE")
	private String protocolTitle;

	@Column(name = "DESCRIPTION")
	private String prtocolDescription;

	@Column(name = "EXPIRATION_DATE")
	private Date prtocolExpirationDate;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "CREATE_TIMESTAMP")
	private Date createTimestamp;

	@Column(name = "INITIAL_SUBMISSION_DATE")
	private Date initialSubmissionDate;

	@Column(name = "LAST_APPROVAL_DATE")
	private Date lastApprovalDate;

	@Column(name = "APPROVAL_DATE")
	private Date approvalDate;

	@Column(name = "PROTOCOL_STATUS_CODE")
	private String protocolStatusCode;

	@Column(name = "ACTIVE")
	private String active;

	@Column(name = "IS_LATEST")
	private String islatest;
	
	@Column(name = "IS_CANCELLED")
	private String isCancelled;

	@Column(name = "FDA_APPLICATION_NUMBER")
	private String fdaApplicationNumber;
	
	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "UPDATE_USER")
	private String updateUser;
	
	@Column(name = "RISK_LEVEL_CODE")
	private String riskLevelCode;
	
	@Column(name = "RISK_LEVEL_COMMENTS")
	private String riskLevelComments;
	
	@Column(name = "RISK_LVL_DATE_ASSIGNED")
	private Date riskLvlDateAssigned;
	
	@Column(name = "FDA_RISK_LVL_DATE_ASSIGNED")
	private Date fdaRiskLvlDateAssigned;
	
	@Column(name = "FDA_RISK_LEVEL_CODE")
	private String fdaRiskLevelCode;
	
	@Column(name = "FDA_RISK_LVL_COMMENTS")
	private String fdaRiskLevelComments;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTOCOL_FK1"), name = "PROTOCOL_TYPE_CODE", referencedColumnName = "PROTOCOL_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolType protocolType;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTOCOL_FK2"), name = "PROTOCOL_STATUS_CODE", referencedColumnName = "PROTOCOL_STATUS_CODE", insertable = false, updatable = false)
	private ProtocolStatus protocolStatus;

	@JsonManagedReference
	@OneToMany(mappedBy = "protocolGeneralInfo", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProtocolPersonnelInfo> personnelInfos;

	@JsonIgnore 
	@OneToMany(mappedBy = "protocolGeneralInfo", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<IRBAttachmentProtocol> attachmentProtocols;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "protocolGeneralInfo", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProtocolLeadUnits> protocolUnits;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTOCOL_FK3"), name = "FDA_RISK_LEVEL_CODE", referencedColumnName = "FDA_RISK_LEVEL_CODE", insertable = false, updatable = false)
	private FDARiskLevel fdaRiskLevel;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTOCOL_FK4"), name = "RISK_LEVEL_CODE", referencedColumnName = "RISK_LEVEL_CODE", insertable = false, updatable = false)
	private RiskLevel riskLevel;

	@Transient
	ProtocolSubmissionStatuses protocolSubmissionStatuses;
	
	@Transient
	private String aniticipatedStartDate;
	
	@Transient
	private String aniticipatedEndDate;
	
	@Transient
	private String stringRiskLvlDate;
	
	@Transient
	private String stringFDARiskLvlDate;
	
	public String getAniticipatedStartDate() {
		return aniticipatedStartDate;
	}

	public void setAniticipatedStartDate(String aniticipatedStartDate) {
		this.aniticipatedStartDate = aniticipatedStartDate;
	}

	public String getAniticipatedEndDate() {
		return aniticipatedEndDate;
	}

	public void setAniticipatedEndDate(String aniticipatedEndDate) {
		this.aniticipatedEndDate = aniticipatedEndDate;
	}
	
	public FDARiskLevel getFdaRiskLevel() {
		return fdaRiskLevel;
	}

	public void setFdaRiskLevel(FDARiskLevel fdaRiskLevel) {
		this.fdaRiskLevel = fdaRiskLevel;
	}

	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}
	
	public List<ProtocolLeadUnits> getProtocolUnits() {
		return protocolUnits;
	}

	public void setProtocolUnits(List<ProtocolLeadUnits> protocolUnits) {
		this.protocolUnits = protocolUnits;
	}
	
	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public String getProtocolTypeCode() {
		return protocolTypeCode;
	}

	public void setProtocolTypeCode(String protocolTypeCode) {
		this.protocolTypeCode = protocolTypeCode;
	}

	public Date getProtocolStartDate() {
		return protocolStartDate;
	}

	public void setProtocolStartDate(Date protocolStartDate) {
		this.protocolStartDate = protocolStartDate;
	}

	public Date getProtocolEndDate() {
		return protocolEndDate;
	}

	public void setProtocolEndDate(Date protocolEndDate) {
		this.protocolEndDate = protocolEndDate;
	}

	public String getProtocolTitle() {
		return protocolTitle;
	}

	public void setProtocolTitle(String protocolTitle) {
		this.protocolTitle = protocolTitle;
	}

	public String getPrtocolDescription() {
		return prtocolDescription;
	}

	public void setPrtocolDescription(String prtocolDescription) {
		this.prtocolDescription = prtocolDescription;
	}

	public Date getPrtocolExpirationDate() {
		return prtocolExpirationDate;
	}

	public void setPrtocolExpirationDate(Date prtocolExpirationDate) {
		this.prtocolExpirationDate = prtocolExpirationDate;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Date getInitialSubmissionDate() {
		return initialSubmissionDate;
	}

	public void setInitialSubmissionDate(Date initialSubmissionDate) {
		this.initialSubmissionDate = initialSubmissionDate;
	}

	public Date getLastApprovalDate() {
		return lastApprovalDate;
	}

	public void setLastApprovalDate(Date lastApprovalDate) {
		this.lastApprovalDate = lastApprovalDate;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getProtocolStatusCode() {
		return protocolStatusCode;
	}

	public void setProtocolStatusCode(String protocolStatusCode) {
		this.protocolStatusCode = protocolStatusCode;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getIslatest() {
		return islatest;
	}

	public void setIslatest(String islatest) {
		this.islatest = islatest;
	}

	public String getFdaApplicationNumber() {
		return fdaApplicationNumber;
	}

	public void setFdaApplicationNumber(String fdaApplicationNumber) {
		this.fdaApplicationNumber = fdaApplicationNumber;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public ProtocolType getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(ProtocolType protocolType) {
		this.protocolType = protocolType;
	}

	public ProtocolStatus getProtocolStatus() {
		return protocolStatus;
	}

	public void setProtocolStatus(ProtocolStatus protocolStatus) {
		this.protocolStatus = protocolStatus;
	}

	public List<ProtocolPersonnelInfo> getPersonnelInfos() {
		return personnelInfos;
	}

	public void setPersonnelInfos(List<ProtocolPersonnelInfo> personnelInfos) {
		this.personnelInfos = personnelInfos;
	}

	public List<IRBAttachmentProtocol> getAttachmentProtocols() {
		return attachmentProtocols;
	}

	public void setAttachmentProtocols(List<IRBAttachmentProtocol> attachmentProtocols) {
		this.attachmentProtocols = attachmentProtocols;
	}

	public String getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(String isCancelled) {
		this.isCancelled = isCancelled;
	}

	public ProtocolSubmissionStatuses getProtocolSubmissionStatuses() {
		return protocolSubmissionStatuses;
	}

	public void setProtocolSubmissionStatuses(ProtocolSubmissionStatuses protocolSubmissionStatuses) {
		this.protocolSubmissionStatuses = protocolSubmissionStatuses;
	}

	public String getRiskLevelCode() {
		return riskLevelCode;
	}

	public void setRiskLevelCode(String riskLevelCode) {
		this.riskLevelCode = riskLevelCode;
	}

	public String getRiskLevelComments() {
		return riskLevelComments;
	}

	public void setRiskLevelComments(String riskLevelComments) {
		this.riskLevelComments = riskLevelComments;
	}

	public Date getRiskLvlDateAssigned() {
		return riskLvlDateAssigned;
	}

	public void setRiskLvlDateAssigned(Date riskLvlDateAssigned) {
		this.riskLvlDateAssigned = riskLvlDateAssigned;
	}

	public Date getFdaRiskLvlDateAssigned() {
		return fdaRiskLvlDateAssigned;
	}

	public void setFdaRiskLvlDateAssigned(Date fdaRiskLvlDateAssigned) {
		this.fdaRiskLvlDateAssigned = fdaRiskLvlDateAssigned;
	}

	public String getFdaRiskLevelCode() {
		return fdaRiskLevelCode;
	}

	public void setFdaRiskLevelCode(String fdaRiskLevelCode) {
		this.fdaRiskLevelCode = fdaRiskLevelCode;
	}

	public String getFdaRiskLevelComments() {
		return fdaRiskLevelComments;
	}

	public void setFdaRiskLevelComments(String fdaRiskLevelComments) {
		this.fdaRiskLevelComments = fdaRiskLevelComments;
	}

	public String getStringRiskLvlDate() {
		return stringRiskLvlDate;
	}

	public void setStringRiskLvlDate(String stringRiskLvlDate) {
		this.stringRiskLvlDate = stringRiskLvlDate;
	}

	public String getStringFDARiskLvlDate() {
		return stringFDARiskLvlDate;
	}

	public void setStringFDARiskLvlDate(String stringFDARiskLvlDate) {
		this.stringFDARiskLvlDate = stringFDARiskLvlDate;
	}
}
