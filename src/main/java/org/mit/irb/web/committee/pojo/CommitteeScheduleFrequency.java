package org.mit.irb.web.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "IRB_COMM_SCHEDULE_FREQUENCY")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class CommitteeScheduleFrequency implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FREQUENCY_CODE")
	private Integer frequencyCode;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "NO_OF_DAYS")
	private Integer noOfDays;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getFrequencyCode() {
		return frequencyCode;
	}

	public void setFrequencyCode(Integer frequencyCode) {
		this.frequencyCode = frequencyCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Integer noOfDays) {
		this.noOfDays = noOfDays;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
