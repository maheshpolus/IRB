package org.mit.irb.web.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name ="SUBMISSION_TYPE_QUALIFIER")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolSubmissionQualifierType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "protocolSubmissionQualifierTypeIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "protocolSubmissionQualifierTypeIdGererator")
	@Column(name = "SUBMISSION_TYPE_QUAL_CODE", updatable = false, nullable = false)
	private String submissionQualifierTypeCode;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public String getSubmissionQualifierTypeCode() {
		return submissionQualifierTypeCode;
	}

	public void setSubmissionQualifierTypeCode(String submissionQualifierTypeCode) {
		this.submissionQualifierTypeCode = submissionQualifierTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
