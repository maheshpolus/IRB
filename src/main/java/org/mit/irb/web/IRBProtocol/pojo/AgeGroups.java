package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "VULNERABLE_SUBJECT_AGE_GROUP")
public class AgeGroups {
	@Id
	@Column(name = "AGE_GROUP_CODE")
	private Integer ageGroupCode;

	@Column(name = "AGE_GROUP")
	private String ageGroup;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_VULNERABLE_SUBJ_AGE_GROUP"), name = "VULNERABLE_SUBJECT_TYPE_CODE", referencedColumnName = "VULNERABLE_SUBJECT_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolSubjectTypes protocolSubjectTypes;

	public Integer getAgeGroupCode() {
		return ageGroupCode;
	}

	public void setAgeGroupCode(Integer ageGroupCode) {
		this.ageGroupCode = ageGroupCode;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
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

	public ProtocolSubjectTypes getProtocolSubjectTypes() {
		return protocolSubjectTypes;
	}

	public void setProtocolSubjectTypes(ProtocolSubjectTypes protocolSubjectTypes) {
		this.protocolSubjectTypes = protocolSubjectTypes;
	}
}
