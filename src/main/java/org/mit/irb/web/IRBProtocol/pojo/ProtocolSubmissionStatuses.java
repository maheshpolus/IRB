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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.mit.irb.web.committee.pojo.ProtocolSubmissionStatus;

@Entity
@Table(name="IRB_PROTOCOL_SUBMISSION")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class ProtocolSubmissionStatuses {

	@Id
	@Column(name = "SUBMISSION_ID")
	Integer submission_Id;
	
	@Column(name = "PROTOCOL_NUMBER")
	String protocolNumber;
	
	@Column(name = "SEQUENCE_NUMBER")
	Integer sequenceNumber;
	
	@Column(name = "SUBMISSION_NUMBER")
	Integer submissionNumber;
	
	@Column(name = "COMMITTEE_ID")
	String committeeId;
	
	@Column(name = "PROTOCOL_ID")
	Integer protocolId ;
	
	@Column(name = "SUBMISSION_TYPE_CODE")
	String submissionTypeCode;
	
	@Column(name = "SUBMISSION_TYPE_QUAL_CODE")
	String submissionTypeQualCode;
	
	@Column(name = "SUBMISSION_STATUS_CODE")
	String submissionStatusCode;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTO_SUBMISSION_FK5"), name = "SUBMISSION_STATUS_CODE", referencedColumnName = "SUBMISSION_STATUS_CODE", insertable = false, updatable = false)
	private ProtocolSubmissionStatus protocolSubmissionStatus;
	
	@Column(name = "PROTOCOL_REVIEW_TYPE_CODE")
	String protocolReviewTypeCode;
	
	@Column(name = "SUBMISSION_DATE")
	Date submissionDate;
	
	@Column(name = "COMMENTS")
	String comments;
	
	@Column(name = "YES_VOTE_COUNT")
	Integer yesVoteCount;
	
	@Column(name = "NO_VOTE_COUNT")
	Integer noVoteCount;
	
	@Column(name = "ABSTAINER_COUNT")
	Integer abstainerCount;
	
	@Column(name = "VOTING_COMMENTS")
	String votingComments;
	
	@Column(name = "UPDATE_TIMESTAMP")
	Date updateTimeStamp;
	
	@Column(name = "UPDATE_USER")
	String updateUser;
	
	@Column(name = "RECUSED_COUNT")
	Integer recusedCount;
	
	@Column(name = "IS_BILLABLE")
	String isBillable;

	@Column(name = "COMM_DECISION_MOTION_TYPE_CODE")
	String commDecisionMotionTypeCode;
	
	@Column(name = "SCHEDULE_ID")
	Integer scheduleId;

	@Column(name = "ASSIGNEE_PERSON_ID")
	String assigneePersonId;
	
	@Column(name = "ASSIGNEE_PERSON_NAME")
	String assigneePersonName;
	
	public Integer getSubmission_Id() {
		return submission_Id;
	}

	public void setSubmission_Id(Integer submission_Id) {
		this.submission_Id = submission_Id;
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

	public Integer getSubmissionNumber() {
		return submissionNumber;
	}

	public void setSubmissionNumber(Integer submissionNumber) {
		this.submissionNumber = submissionNumber;
	}

	public String getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public String getSubmissionTypeCode() {
		return submissionTypeCode;
	}

	public void setSubmissionTypeCode(String submissionTypeCode) {
		this.submissionTypeCode = submissionTypeCode;
	}

	public String getSubmissionTypeQualCode() {
		return submissionTypeQualCode;
	}

	public void setSubmissionTypeQualCode(String submissionTypeQualCode) {
		this.submissionTypeQualCode = submissionTypeQualCode;
	}

	public String getSubmissionStatusCode() {
		return submissionStatusCode;
	}

	public void setSubmissionStatusCode(String submissionStatusCode) {
		this.submissionStatusCode = submissionStatusCode;
	}

	public String getProtocolReviewTypeCode() {
		return protocolReviewTypeCode;
	}

	public void setProtocolReviewTypeCode(String protocolReviewTypeCode) {
		this.protocolReviewTypeCode = protocolReviewTypeCode;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getYesVoteCount() {
		return yesVoteCount;
	}

	public void setYesVoteCount(Integer yesVoteCount) {
		this.yesVoteCount = yesVoteCount;
	}

	public Integer getNoVoteCount() {
		return noVoteCount;
	}

	public void setNoVoteCount(Integer noVoteCount) {
		this.noVoteCount = noVoteCount;
	}

	public Integer getAbstainerCount() {
		return abstainerCount;
	}

	public void setAbstainerCount(Integer abstainerCount) {
		this.abstainerCount = abstainerCount;
	}

	public String getVotingComments() {
		return votingComments;
	}

	public void setVotingComments(String votingComments) {
		this.votingComments = votingComments;
	}

	public Date getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Date updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Integer getRecusedCount() {
		return recusedCount;
	}

	public void setRecusedCount(Integer recusedCount) {
		this.recusedCount = recusedCount;
	}

	public String getIsBillable() {
		return isBillable;
	}

	public void setIsBillable(String isBillable) {
		this.isBillable = isBillable;
	}

	public String getCommDecisionMotionTypeCode() {
		return commDecisionMotionTypeCode;
	}

	public void setCommDecisionMotionTypeCode(String commDecisionMotionTypeCode) {
		this.commDecisionMotionTypeCode = commDecisionMotionTypeCode;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public ProtocolSubmissionStatus getProtocolSubmissionStatus() {
		return protocolSubmissionStatus;
	}

	public void setProtocolSubmissionStatus(ProtocolSubmissionStatus protocolSubmissionStatus) {
		this.protocolSubmissionStatus = protocolSubmissionStatus;
	}

	public String getAssigneePersonId() {
		return assigneePersonId;
	}

	public void setAssigneePersonId(String assigneePersonId) {
		this.assigneePersonId = assigneePersonId;
	}

	public String getAssigneePersonName() {
		return assigneePersonName;
	}

	public void setAssigneePersonName(String assigneePersonName) {
		this.assigneePersonName = assigneePersonName;
	}
}
