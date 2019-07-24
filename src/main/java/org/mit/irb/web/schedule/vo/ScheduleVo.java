package org.mit.irb.web.schedule.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.mit.irb.web.committee.pojo.Committee;
import org.mit.irb.web.committee.pojo.CommitteeMemberships;
import org.mit.irb.web.committee.pojo.CommitteeSchedule;
import org.mit.irb.web.committee.pojo.CommitteeScheduleActItems;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttachType;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttachment;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttendance;
import org.mit.irb.web.committee.pojo.CommitteeScheduleMinuteDoc;
import org.mit.irb.web.committee.pojo.CommitteeScheduleMinutes;
import org.mit.irb.web.committee.pojo.MinuteEntryType;
import org.mit.irb.web.committee.pojo.ProtocolContingency;
import org.mit.irb.web.committee.pojo.ProtocolSubmission;
import org.mit.irb.web.committee.pojo.ScheduleActItemType;
import org.mit.irb.web.committee.pojo.ScheduleAgenda;
import org.mit.irb.web.committee.pojo.ScheduleStatus;


public class ScheduleVo {

	private Integer scheduleId;

	private CommitteeSchedule committeeSchedule;

	private Boolean status;

	private String message;

	private Committee committee;

	private String committeeId;

	private List<ScheduleStatus> scheduleStatus;

	private List<ScheduleActItemType> scheduleActItemTypes;

	private CommitteeScheduleActItems committeeScheduleActItems;

	private Integer commScheduleActItemsId;

	private List<MinuteEntryType> minuteEntrytypes;

	private List<ProtocolContingency> protocolContingencies;

	private CommitteeScheduleAttachment newCommitteeScheduleAttachment;

	private CommitteeScheduleMinutes newCommitteeScheduleMinute;

	private List<CommitteeScheduleAttachType> attachmentTypes;

	private List<CommitteeScheduleAttendance> updatedAttendance;

	private Integer commScheduleMinuteId;

	private Integer commScheduleAttachId;

	private Integer commScheduleAttendanceId;
	
	private List<ProtocolSubmission> submittedProtocols;
	
	private List<CommitteeScheduleMinutes> scheduleMinutes;		

	private String protocolNumber;
	
	private Integer submissionId;
	
	private ArrayList<HashMap<String, Object>> submittedProtocolsList;
	
	private String acType;

	private List<CommitteeScheduleAttendance> alternateMember;
	
	private List<CommitteeScheduleAttendance> committeeMember;
	
	private List<CommitteeScheduleAttendance> guestMembers;
	
	private List<CommitteeScheduleAttachment> committeeScheduleAttachmentList;
	
	private ArrayList<HashMap<String, Object>> agendaScheduleIds;
	
	private String updateUser;
		
	private List<ScheduleAgenda> agendaList;
	
	private boolean flag;
	
	private String templateCode;
	
	private List<CommitteeScheduleActItems> committeeScheduleActItemsList;
	
	private List<CommitteeScheduleMinuteDoc> minuteList;
	
	private ScheduleAgenda agendaDetails;
	
	private CommitteeScheduleMinuteDoc minuteDetails;
	
	private CommitteeScheduleAttendance committeeScheduleAttendance;
	
	private List<CommitteeMemberships> alternateMemberMinutes;
	
	private List<CommitteeMemberships> committeeMemberMinutes;
	
	private List<CommitteeScheduleAttendance> guestMembersMinutes;
	
	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public String getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public List<ScheduleStatus> getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(List<ScheduleStatus> scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public List<ScheduleActItemType> getScheduleActItemTypes() {
		return scheduleActItemTypes;
	}

	public void setScheduleActItemTypes(List<ScheduleActItemType> scheduleActItemTypes) {
		this.scheduleActItemTypes = scheduleActItemTypes;
	}

	public CommitteeScheduleActItems getCommitteeScheduleActItems() {
		return committeeScheduleActItems;
	}

	public void setCommitteeScheduleActItems(CommitteeScheduleActItems committeeScheduleActItems) {
		this.committeeScheduleActItems = committeeScheduleActItems;
	}

	public Integer getCommScheduleActItemsId() {
		return commScheduleActItemsId;
	}

	public void setCommScheduleActItemsId(Integer commScheduleActItemsId) {
		this.commScheduleActItemsId = commScheduleActItemsId;
	}

	public List<MinuteEntryType> getMinuteEntrytypes() {
		return minuteEntrytypes;
	}

	public void setMinuteEntrytypes(List<MinuteEntryType> minuteEntrytypes) {
		this.minuteEntrytypes = minuteEntrytypes;
	}

	public List<ProtocolContingency> getProtocolContingencies() {
		return protocolContingencies;
	}

	public void setProtocolContingencies(List<ProtocolContingency> protocolContingencies) {
		this.protocolContingencies = protocolContingencies;
	}

	public CommitteeScheduleAttachment getNewCommitteeScheduleAttachment() {
		return newCommitteeScheduleAttachment;
	}

	public void setNewCommitteeScheduleAttachment(CommitteeScheduleAttachment newCommitteeScheduleAttachment) {
		this.newCommitteeScheduleAttachment = newCommitteeScheduleAttachment;
	}

	public CommitteeScheduleMinutes getNewCommitteeScheduleMinute() {
		return newCommitteeScheduleMinute;
	}

	public void setNewCommitteeScheduleMinute(CommitteeScheduleMinutes newCommitteeScheduleMinute) {
		this.newCommitteeScheduleMinute = newCommitteeScheduleMinute;
	}

	public List<CommitteeScheduleAttachType> getAttachmentTypes() {
		return attachmentTypes;
	}

	public void setAttachmentTypes(List<CommitteeScheduleAttachType> attachmentTypes) {
		this.attachmentTypes = attachmentTypes;
	}

	public Integer getCommScheduleMinuteId() {
		return commScheduleMinuteId;
	}

	public void setCommScheduleMinuteId(Integer commScheduleMinuteId) {
		this.commScheduleMinuteId = commScheduleMinuteId;
	}

	public Integer getCommScheduleAttachId() {
		return commScheduleAttachId;
	}

	public void setCommScheduleAttachId(Integer commScheduleAttachId) {
		this.commScheduleAttachId = commScheduleAttachId;
	}

	public Integer getCommScheduleAttendanceId() {
		return commScheduleAttendanceId;
	}

	public void setCommScheduleAttendanceId(Integer commScheduleAttendanceId) {
		this.commScheduleAttendanceId = commScheduleAttendanceId;
	}

	public List<ProtocolSubmission> getSubmittedProtocols() {
		return submittedProtocols;
	}

	public void setSubmittedProtocols(List<ProtocolSubmission> submittedProtocols) {
		this.submittedProtocols = submittedProtocols;
	}

	public List<CommitteeScheduleMinutes> getScheduleMinutes() {
		return scheduleMinutes;
	}

	public void setScheduleMinutes(List<CommitteeScheduleMinutes> scheduleMinutes) {
		this.scheduleMinutes = scheduleMinutes;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	public ArrayList<HashMap<String, Object>> getSubmittedProtocolsList() {
		return submittedProtocolsList;
	}

	public void setSubmittedProtocolsList(ArrayList<HashMap<String, Object>> submittedProtocolsList) {
		this.submittedProtocolsList = submittedProtocolsList;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public List<CommitteeScheduleAttendance> getGuestMembers() {
		return guestMembers;
	}

	public void setGuestMembers(List<CommitteeScheduleAttendance> guestMembers) {
		this.guestMembers = guestMembers;
	}

	public List<CommitteeScheduleAttachment> getCommitteeScheduleAttachmentList() {
		return committeeScheduleAttachmentList;
	}

	public void setCommitteeScheduleAttachmentList(List<CommitteeScheduleAttachment> committeeScheduleAttachmentList) {
		this.committeeScheduleAttachmentList = committeeScheduleAttachmentList;
	}

	public ArrayList<HashMap<String, Object>> getAgendaScheduleIds() {
		return agendaScheduleIds;
	}

	public void setAgendaScheduleIds(ArrayList<HashMap<String, Object>> agendaScheduleIds) {
		this.agendaScheduleIds = agendaScheduleIds;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public List<ScheduleAgenda> getAgendaList() {
		return agendaList;
	}

	public void setAgendaList(List<ScheduleAgenda> agendaList) {
		this.agendaList = agendaList;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public List<CommitteeScheduleActItems> getCommitteeScheduleActItemsList() {
		return committeeScheduleActItemsList;
	}

	public void setCommitteeScheduleActItemsList(List<CommitteeScheduleActItems> committeeScheduleActItemsList) {
		this.committeeScheduleActItemsList = committeeScheduleActItemsList;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public List<CommitteeScheduleMinuteDoc> getMinuteList() {
		return minuteList;
	}

	public void setMinuteList(List<CommitteeScheduleMinuteDoc> minuteList) {
		this.minuteList = minuteList;
	}

	public ScheduleAgenda getAgendaDetails() {
		return agendaDetails;
	}

	public void setAgendaDetails(ScheduleAgenda agendaDetails) {
		this.agendaDetails = agendaDetails;
	}

	public CommitteeScheduleMinuteDoc getMinuteDetails() {
		return minuteDetails;
	}

	public void setMinuteDetails(CommitteeScheduleMinuteDoc minuteDetails) {
		this.minuteDetails = minuteDetails;
	}

	public List<CommitteeScheduleAttendance> getUpdatedAttendance() {
		return updatedAttendance;
	}

	public void setUpdatedAttendance(List<CommitteeScheduleAttendance> updatedAttendance) {
		this.updatedAttendance = updatedAttendance;
	}

	public CommitteeScheduleAttendance getCommitteeScheduleAttendance() {
		return committeeScheduleAttendance;
	}

	public void setCommitteeScheduleAttendance(CommitteeScheduleAttendance committeeScheduleAttendance) {
		this.committeeScheduleAttendance = committeeScheduleAttendance;
	}

	public List<CommitteeScheduleAttendance> getAlternateMember() {
		return alternateMember;
	}

	public void setAlternateMember(List<CommitteeScheduleAttendance> alternateMember) {
		this.alternateMember = alternateMember;
	}

	public List<CommitteeScheduleAttendance> getCommitteeMember() {
		return committeeMember;
	}

	public void setCommitteeMember(List<CommitteeScheduleAttendance> committeeMember) {
		this.committeeMember = committeeMember;
	}

	public List<CommitteeMemberships> getAlternateMemberMinutes() {
		return alternateMemberMinutes;
	}

	public void setAlternateMemberMinutes(List<CommitteeMemberships> alternateMemberMinutes) {
		this.alternateMemberMinutes = alternateMemberMinutes;
	}

	public List<CommitteeMemberships> getCommitteeMemberMinutes() {
		return committeeMemberMinutes;
	}

	public void setCommitteeMemberMinutes(List<CommitteeMemberships> committeeMemberMinutes) {
		this.committeeMemberMinutes = committeeMemberMinutes;
	}

	public List<CommitteeScheduleAttendance> getGuestMembersMinutes() {
		return guestMembersMinutes;
	}

	public void setGuestMembersMinutes(List<CommitteeScheduleAttendance> guestMembersMinutes) {
		this.guestMembersMinutes = guestMembersMinutes;
	}
	
}
