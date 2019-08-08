package org.mit.irb.web.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

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
@Table(name = "IRB_SCHEDULE_AGENDA")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ScheduleAgenda implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "agendaIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "agendaIdGererator")
	@Column(name = "SCHEDULE_AGENDA_ID")
	private Integer scheduleAgendaId;

	@Column(name = "SCHEDULE_ID")
	private Integer scheduleId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_IRB_COMM_SCHEDULE_AGENDA"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID", insertable = false, updatable = false)
	private CommitteeSchedule committeeSchedule;

	@Column(name = "AGENDA_NUMBER")
	private Integer agendaNumber;

	@Column(name = "AGENDA_NAME")
	private Integer agendaName;

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

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Integer getAgendaNumber() {
		return agendaNumber;
	}

	public void setAgendaNumber(Integer agendaNumber) {
		this.agendaNumber = agendaNumber;
	}

	public Integer getAgendaName() {
		return agendaName;
	}

	public void setAgendaName(Integer agendaName) {
		this.agendaName = agendaName;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getScheduleAgendaId() {
		return scheduleAgendaId;
	}

	public void setScheduleAgendaId(Integer scheduleAgendaId) {
		this.scheduleAgendaId = scheduleAgendaId;
	}

	public ScheduleAgenda(Integer scheduleAgendaId, Integer scheduleId, Integer agendaNumber, java.util.Date createTimestamp,
			String createUser) {
		super();
		this.scheduleAgendaId = scheduleAgendaId;
		this.scheduleId = scheduleId;
		this.agendaNumber = agendaNumber;
		this.createTimestamp = createTimestamp;
		this.createUser = createUser;
	}

	public ScheduleAgenda() {
	}

	public java.util.Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(java.util.Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

}
