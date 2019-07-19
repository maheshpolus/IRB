package org.mit.irb.web.committee.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.mit.irb.web.IRBProtocol.pojo.IRBAdminReviewerComment;

@Entity
@Table(name="IRB_PROTOCOL_SUBMISSION")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolSubmission implements Serializable {	

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "protocolSubmissionIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "protocolSubmissionIdGererator")
	@Column(name = "SUBMISSION_ID", updatable = false, nullable = false)
	private Integer submissionId;

	@JsonBackReference
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name ="IRB_PROTO_SUBMISSION_FK8"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID")
	private CommitteeSchedule committeeSchedule;

	@Column(name = "SUBMISSION_NUMBER")
	private Integer submissionNumber;

	@Column(name = "COMMITTEE_ID")
	private String committeeId;

	@Column(name = "PROTOCOL_ID")
	private Integer protocolId;

	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Transient
	private String personName;
	
	@Transient
	private String piPersonId;
	
/*	@Column(name = "PI_PERSON_NAME")
	private String piPersonName;*/

/*	@Column(name = "PI_PERSON_ID")
	private String piPersonId;*/

	@Transient
	private String protocolTitle;

	@Column(name = "SUBMISSION_TYPE_CODE")
	private String submissionTypeCode;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey =@ForeignKey(name = "IRB_PROTO_SUBMISSION_FK6"), name = "SUBMISSION_TYPE_CODE", referencedColumnName = "SUBMISSION_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolSubmissionType protocolSubmissionType;

	@Column(name = "SUBMISSION_TYPE_QUAL_CODE")
	private String submissionTypeQualifierCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey =@ForeignKey(name = "IRB_PROTO_SUBMISSION_FK7"), name = "SUBMISSION_TYPE_QUAL_CODE", referencedColumnName = "SUBMISSION_TYPE_QUAL_CODE", insertable = false, updatable = false)
	private ProtocolSubmissionQualifierType qualifierType;

	@Column(name = "SUBMISSION_STATUS_CODE")
	private String submissionStatusCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey =@ForeignKey(name = "IRB_PROTO_SUBMISSION_FK5"),name = "SUBMISSION_STATUS_CODE", referencedColumnName = "SUBMISSION_STATUS_CODE", insertable = false, updatable = false)
	private ProtocolSubmissionStatus submissionStatus;

	@Column(name = "PROTOCOL_REVIEW_TYPE_CODE")
	private String protocolReviewTypeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey =@ForeignKey(name = "IRB_PROTO_SUBMISSION_FK4"),name = "PROTOCOL_REVIEW_TYPE_CODE", referencedColumnName = "PROTOCOL_REVIEW_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolReviewType protocolReviewType;

	@Column(name = "SUBMISSION_DATE")
	private Date submissionDate;

/*	@Column(name = "PROTOCOL_ACTIVE")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean protocolActive;*/
	
	@Transient
	private String adminName;
	
	@Transient
	private String documentNumber;
	
	//for templates
	@Transient
	private List<IRBAdminReviewerComment> adminComments;
	
	@Transient
	private String committeePriRev;
	
	@Transient
	private String committeeSecRev;
	
	@Transient
	private String submissionTypeDescription;
	//
	
	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}	

	public String getProtocolTitle() {
		return protocolTitle;
	}

	public void setProtocolTitle(String protocolTitle) {
		this.protocolTitle = protocolTitle;
	}

	public String getSubmissionTypeCode() {
		return submissionTypeCode;
	}

	public void setSubmissionTypeCode(String submissionTypeCode) {
		this.submissionTypeCode = submissionTypeCode;
	}

	public ProtocolSubmissionType getProtocolSubmissionType() {
		return protocolSubmissionType;
	}

	public void setProtocolSubmissionType(ProtocolSubmissionType protocolSubmissionType) {
		this.protocolSubmissionType = protocolSubmissionType;
	}

	public String getSubmissionTypeQualifierCode() {
		return submissionTypeQualifierCode;
	}

	public void setSubmissionTypeQualifierCode(String submissionTypeQualifierCode) {
		this.submissionTypeQualifierCode = submissionTypeQualifierCode;
	}

	public ProtocolSubmissionQualifierType getQualifierType() {
		return qualifierType;
	}

	public void setQualifierType(ProtocolSubmissionQualifierType qualifierType) {
		this.qualifierType = qualifierType;
	}

	public String getSubmissionStatusCode() {
		return submissionStatusCode;
	}

	public void setSubmissionStatusCode(String submissionStatusCode) {
		this.submissionStatusCode = submissionStatusCode;
	}

	public ProtocolSubmissionStatus getSubmissionStatus() {
		return submissionStatus;
	}

	public void setSubmissionStatus(ProtocolSubmissionStatus submissionStatus) {
		this.submissionStatus = submissionStatus;
	}

	public String getProtocolReviewTypeCode() {
		return protocolReviewTypeCode;
	}

	public void setProtocolReviewTypeCode(String protocolReviewTypeCode) {
		this.protocolReviewTypeCode = protocolReviewTypeCode;
	}

	public ProtocolReviewType getProtocolReviewType() {
		return protocolReviewType;
	}

	public void setProtocolReviewType(ProtocolReviewType protocolReviewType) {
		this.protocolReviewType = protocolReviewType;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}

	/*public Boolean getProtocolActive() {
		return protocolActive;
	}

	public void setProtocolActive(Boolean protocolActive) {
		this.protocolActive = protocolActive;
	}*/

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public String getPiPersonId() {
		return piPersonId;
	}

	public void setPiPersonId(String piPersonId) {
		this.piPersonId = piPersonId;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Integer getSubmissionNumber() {
		return submissionNumber;
	}

	public void setSubmissionNumber(Integer submissionNumber) {
		this.submissionNumber = submissionNumber;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public ProtocolSubmission() {
		setProtocolReviewType(new ProtocolReviewType());
		setSubmissionStatus(new ProtocolSubmissionStatus());
        setQualifierType(new ProtocolSubmissionQualifierType());
        setProtocolSubmissionType(new ProtocolSubmissionType()); 
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public List<IRBAdminReviewerComment> getAdminComments() {
		return adminComments;
	}

	public void setAdminComments(List<IRBAdminReviewerComment> adminComments) {
		this.adminComments = adminComments;
	}

	public String getSubmissionTypeDescription() {
		return submissionTypeDescription;
	}

	public void setSubmissionTypeDescription(String submissionTypeDescription) {
		this.submissionTypeDescription = submissionTypeDescription;
	}

	public void setCommitteeSecRev(String committeeSecRev) {
		this.committeeSecRev = committeeSecRev;
	}

	public String getCommitteePriRev() {
		return committeePriRev;
	}

	public void setCommitteePriRev(String committeePriRev) {
		this.committeePriRev = committeePriRev;
	}

	public String getCommitteeSecRev() {
		return committeeSecRev;
	}

	public ProtocolSubmission(String protocolNumber, String personName, String protocolTitle, String committeePriRev,
			String committeeSecRev, String submissionTypeDescription) {
		super();
		this.protocolNumber = protocolNumber;
		this.personName = personName;
		this.protocolTitle = protocolTitle;
		this.committeePriRev = committeePriRev;
		this.committeeSecRev = committeeSecRev;
		this.submissionTypeDescription = submissionTypeDescription;
	}
}
