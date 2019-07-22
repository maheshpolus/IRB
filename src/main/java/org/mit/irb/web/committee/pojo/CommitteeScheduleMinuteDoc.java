package org.mit.irb.web.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "IRB_COMM_SCHEDULE_MINUTE_DOC")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class CommitteeScheduleMinuteDoc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "minuteIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "minuteIdGererator")
	@Column(name = "SCHEDULE_MINUTE_DOC_ID")
	private Integer scheduleMinuteDocId;

	@Column(name = "SCHEDULE_ID")
	private Integer scheduleId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_IRB_COMM_SCH_MINUTE_DOC"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID", insertable = false, updatable = false)
	private CommitteeSchedule committeeSchedule;

	@Column(name = "MINUTE_NUMBER")
	private Integer minuteNumber;

	@Column(name = "MINUTE_NAME")
	private String minuteName;

	@Column(name = "PDF_STORE")
	private byte[] pdfStore;

	@Column(name = "CREATE_TIMESTAMP")
	private java.util.Date createTimestamp;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getScheduleMinuteDocId() {
		return scheduleMinuteDocId;
	}

	public void setScheduleMinuteDocId(Integer scheduleMinuteDocId) {
		this.scheduleMinuteDocId = scheduleMinuteDocId;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Integer getMinuteNumber() {
		return minuteNumber;
	}

	public void setMinuteNumber(Integer minuteNumber) {
		this.minuteNumber = minuteNumber;
	}

	public String getMinuteName() {
		return minuteName;
	}

	public void setMinuteName(String minuteName) {
		this.minuteName = minuteName;
	}

	public byte[] getPdfStore() {
		return pdfStore;
	}

	public void setPdfStore(byte[] pdfStore) {
		this.pdfStore = pdfStore;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public java.util.Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(java.util.Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public CommitteeScheduleMinuteDoc(Integer scheduleMinuteDocId, Integer scheduleId, Integer minuteNumber,
			Date createTimestamp,String createUser) {
		super();
		this.scheduleMinuteDocId = scheduleMinuteDocId;
		this.scheduleId = scheduleId;
		this.minuteNumber = minuteNumber;
		this.createTimestamp = createTimestamp;
		this.createUser = createUser;
	}

	public CommitteeScheduleMinuteDoc() {
		// TODO Auto-generated constructor stub
	}

}
