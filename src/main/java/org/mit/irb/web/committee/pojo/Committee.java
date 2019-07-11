package org.mit.irb.web.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "IRB_COMMITTEE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Committee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	/*@GenericGenerator(name = "seq_comm_id", strategy = "org.mit.irb.web.generator.CommitteeIdGenerator")
	@GeneratedValue(generator = "seq_comm_id")  */
	@Column(name = "COMMITTEE_ID")
	private String committeeId;

	@Column(name = "COMMITTEE_NAME")
	private String committeeName;

	public Committee(String committeeId, String committeeName, String homeUnitNumber, String description,
			String scheduleDescription, Integer committeeTypeCode, CommitteeType committeeType,
			Integer minimumMembersRequired, Integer maxProtocols, Integer advSubmissionDaysReq,
			java.util.Date updateTimestamp, String updateUser,
			String homeUnitName) {
		super();
		this.committeeId = committeeId;
		this.committeeName = committeeName;
		this.homeUnitNumber = homeUnitNumber;
		this.description = description;
		this.scheduleDescription = scheduleDescription;
		this.committeeTypeCode = committeeTypeCode;
		this.committeeType = committeeType;
		this.minimumMembersRequired = minimumMembersRequired;
		this.maxProtocols = maxProtocols;
		this.advSubmissionDaysReq = advSubmissionDaysReq;
		this.updateTimestamp = updateTimestamp;
		this.updateUser = updateUser;
		this.homeUnitName = homeUnitName;
	}

	@Column(name = "HOME_UNIT_NUMBER")
	private String homeUnitNumber;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "SCHEDULE_DESCRIPTION")
	private String scheduleDescription;

	@Column(name = "COMMITTEE_TYPE_CODE")
	private Integer committeeTypeCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_IRB_COMMITTEE"), name = "COMMITTEE_TYPE_CODE", referencedColumnName = "COMMITTEE_TYPE_CODE", insertable = false, updatable = false)
	private CommitteeType committeeType;

	@Column(name = "MINIMUM_MEMBERS_REQUIRED")
	private Integer minimumMembersRequired;

	@Column(name = "MAX_PROTOCOLS")
	private Integer maxProtocols;

	@Column(name = "ADV_SUBMISSION_DAYS_REQ")
	private Integer advSubmissionDaysReq;

	@Column(name = "CREATE_TIMESTAMP")
	private Timestamp createTimestamp;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "UPDATE_TIMESTAMP")
	private java.util.Date updateTimestamp;

	@Transient
	private String updatedDate;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;

	@JsonManagedReference
	@OneToMany(mappedBy = "committee", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<CommitteeResearchAreas> researchAreas;

	@JsonManagedReference
	@OneToMany(mappedBy = "committee", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<CommitteeSchedule> committeeSchedules;

	@JsonManagedReference
	@OneToMany(mappedBy = "committee", orphanRemoval = true, cascade = { CascadeType.ALL })
	@Fetch(FetchMode.JOIN)
	private List<CommitteeMemberships> committeeMemberships;

	@Column(name = "HOME_UNIT_NAME")
	private String homeUnitName;	

	@Transient
	private java.util.Date scheduleStartDate;
	
	@Transient
	private java.util.Date scheduleEndDate;

	public Committee() {
		/*setResearchAreas(new ArrayList<CommitteeResearchAreas>());
		setCommitteeMemberships(new ArrayList<CommitteeMemberships>());
        setCommitteeSchedules(new ArrayList<CommitteeSchedule>());*/
		setCommitteeTypeCode(1);
	}

	public String getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}

	public String getCommitteeName() {
		return committeeName;
	}

	public void setCommitteeName(String committeeName) {
		this.committeeName = committeeName;
	}

	public String getHomeUnitNumber() {
		return homeUnitNumber;
	}

	public void setHomeUnitNumber(String homeUnitNumber) {
		this.homeUnitNumber = homeUnitNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScheduleDescription() {
		return scheduleDescription;
	}

	public void setScheduleDescription(String scheduleDescription) {
		this.scheduleDescription = scheduleDescription;
	}

	public CommitteeType getCommitteeType() {
		return committeeType;
	}

	public void setCommitteeType(CommitteeType committeeType) {
		this.committeeType = committeeType;
	}

	public Integer getMinimumMembersRequired() {
		return minimumMembersRequired;
	}

	public void setMinimumMembersRequired(Integer minimumMembersRequired) {
		this.minimumMembersRequired = minimumMembersRequired;
	}

	public Integer getMaxProtocols() {
		return maxProtocols;
	}

	public void setMaxProtocols(Integer maxProtocols) {
		this.maxProtocols = maxProtocols;
	}

	public Integer getAdvSubmissionDaysReq() {
		return advSubmissionDaysReq;
	}

	public void setAdvSubmissionDaysReq(Integer advSubmissionDaysReq) {
		this.advSubmissionDaysReq = advSubmissionDaysReq;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Integer getCommitteeTypeCode() {
		return committeeTypeCode;
	}

	public void setCommitteeTypeCode(Integer committeeTypeCode) {
		this.committeeTypeCode = committeeTypeCode;
	}

	public List<CommitteeSchedule> getCommitteeSchedules() {
		return committeeSchedules;
	}

	public void setCommitteeSchedules(List<CommitteeSchedule> committeeSchedules) {
		this.committeeSchedules = committeeSchedules;
	}

	public String getHomeUnitName() {
		return homeUnitName;
	}

	public void setHomeUnitName(String homeUnitName) {
		this.homeUnitName = homeUnitName;
	}

	public List<CommitteeMemberships> getCommitteeMemberships() {
		return committeeMemberships;
	}

	public void setCommitteeMemberships(List<CommitteeMemberships> committeeMemberships) {
		this.committeeMemberships = committeeMemberships;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public java.util.Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(java.util.Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public List<CommitteeResearchAreas> getResearchAreas() {
		return researchAreas;
	}

	public void setResearchAreas(List<CommitteeResearchAreas> researchAreas) {
		this.researchAreas = researchAreas;
	}

	public java.util.Date getScheduleStartDate() {
		return scheduleStartDate;
	}

	public void setScheduleStartDate(java.util.Date scheduleStartDate) {
		this.scheduleStartDate = scheduleStartDate;
	}

	public java.util.Date getScheduleEndDate() {
		return scheduleEndDate;
	}

	public void setScheduleEndDate(java.util.Date scheduleEndDate) {
		this.scheduleEndDate = scheduleEndDate;
	}
}
