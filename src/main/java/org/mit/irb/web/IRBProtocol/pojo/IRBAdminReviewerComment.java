package org.mit.irb.web.IRBProtocol.pojo;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "IRB_PROTO_ADMIN_REVIEW_COMMENT")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class IRBAdminReviewerComment {

	@Id
	@Column(name="COMMENT_ID")
	private Integer commentId;
	
	@Column(name="ADMIN_REVIEWER_ID")
	private Integer adminReviewerId;
	
	@Column(name="PROTOCOL_NUMBER")
	private String protocolNumber;
	
	@Column(name="SEQUENCE_NUMBER")
	private String sequenceNumber;
	
	@Column(name="PROTOCOL_ID")
	private Integer protocolId;
	
	@Column(name="SUBMISSION_ID")
	private Integer submissionId;
	
	@Column(name="COMMENT_DESCRIPTION")
	private String comment;
	
	@Column(name="UPDATE_TIMESTAMP")
	private Date updateTimestamp;
	
	@Column(name="UPDATE_USER")
	private String updateUser;
	
	@Column(name="PUBLIC_FLAG")
	private String publicFlag;
	
	@Column(name="PERSON_ID")
	private String personId;

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getAdminReviewerId() {
		return adminReviewerId;
	}

	public void setAdminReviewerId(Integer adminReviewerId) {
		this.adminReviewerId = adminReviewerId;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getPublicFlag() {
		return publicFlag;
	}

	public void setPublicFlag(String publicFlag) {
		this.publicFlag = publicFlag;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	public IRBAdminReviewerComment(String comment) {
		super();
		this.comment = comment;
	}
}
