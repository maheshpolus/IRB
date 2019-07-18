package org.mit.irb.web.schedule.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.committee.pojo.CommitteeMemberRoles;
import org.mit.irb.web.committee.pojo.CommitteeMemberships;
import org.mit.irb.web.committee.pojo.CommitteeSchedule;
import org.mit.irb.web.committee.pojo.CommitteeScheduleActItems;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttachType;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttachment;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttendance;
import org.mit.irb.web.committee.pojo.CommitteeScheduleMinutes;
import org.mit.irb.web.committee.pojo.MinuteEntryType;
import org.mit.irb.web.committee.pojo.ProtocolContingency;
import org.mit.irb.web.committee.pojo.ScheduleActItemType;
import org.mit.irb.web.committee.view.ProtocolView;
import org.mit.irb.web.schedule.vo.ScheduleVo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@Service
public interface ScheduleDao {

	/**
	 * This method is used to get a protocol based on parameters.
	 * @param protocolId - Id of the submitted protocol.
	 * @param personId - personId of the PI of submitted protocol.
	 * @param fullName - fullName of the PI of submitted protocol.
	 * @return an object of protocol.
	 */
	public ProtocolView fetchProtocolViewByParams(Integer protocolId, String personId, String fullName);

	/**
	 * This method is used to fetch all schedule actionItem type.
	 * @return a list of Schedule actionItem type.
	 */
	public List<ScheduleActItemType> fetchAllScheduleActItemType();

	/**
	 * This method is used to update committee schedule.
	 * @param committeeSchedule - Object of CommitteeSchedule.
	 * @return an object of updated CommitteeSchedule.
	 */
	public CommitteeSchedule updateCommitteeSchedule(CommitteeSchedule committeeSchedule);

	/**
	 * This method is used to add other actions.
	 * @param committeeScheduleActItems - Object of CommitteeScheduleActItems.
	 * @return an object of updated CommitteeScheduleActItems.
	 */
	public CommitteeScheduleActItems addOtherActions(CommitteeScheduleActItems committeeScheduleActItems);

	/**
	 * This method is used to fetch all minute entry types.
	 * @return a list of MinuteEntryType.
	 */
	public List<MinuteEntryType> fetchAllMinuteEntryTypes();

	/**
	 * This method is used to fetch all protocol contingency.
	 * @return a list of ProtocolContingency.
	 */
	public List<ProtocolContingency> fetchAllProtocolContingency();

	/**
	 * This method is used to fetch all committee schedule attachment type.
	 * @return a list of CommitteeScheduleAttachmentType.
	 */
	public List<CommitteeScheduleAttachType> fetchAllCommitteeScheduleAttachType();

	/**
	 * This method is used to add committee schedule minute.
	 * @param committeeScheduleMinutes - Object of CommitteeScheduleMinutes.
	 * @return an object of updated CommitteeScheduleMinutes.
	 */
	public CommitteeScheduleMinutes addCommitteeScheduleMinute(CommitteeScheduleMinutes committeeScheduleMinutes);

	/**
	 * This method is used to add committee schedule attendance.
	 * @param scheduleAttendance - Object of CommitteeScheduleAttendance.
	 * @return an object of updated CommitteeScheduleAttendance.
	 */
	public CommitteeScheduleAttendance addCommitteeScheduleAttendance(CommitteeScheduleAttendance scheduleAttendance);

	/**
	 * This method is used to add Schedule attachment.
	 * @param committeeScheduleAttachment - Object of CommitteeScheduleAttachment.
	 * @return an object of updated CommitteeScheduleAttachment.
	 */
	public CommitteeScheduleAttachment addScheduleAttachment(CommitteeScheduleAttachment committeeScheduleAttachment);

	/**
	 * This method is used to fetch attachment by Id.
	 * @param attachmentId - Id of the attachment.
	 * @return an object of CommitteeScheduleAttachment.
	 */
	public CommitteeScheduleAttachment fetchAttachmentById(Integer attachmentId);

	/**
	 * @param scheduleId
	 * @return get scedule details
	 */
	public CommitteeSchedule getCommitteeScheduleDetail(Integer scheduleId);

	/**
	 * @param scheduleId
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> loadScheduledProtocols(Integer scheduleId);

	/**
	 * @param vo
	 * @return
	 */
	public List<CommitteeScheduleMinutes> getScheduleMinutes(ScheduleVo vo);

	/**
	 * @param vo
	 * @return 
	 */
	public List<CommitteeScheduleMinutes> getProtocolCommitteeComments(ScheduleVo vo);
	
	/**
	 * @param vo
	 * @return
	 */
	public List<CommitteeMemberships> fetchMeetingMembers(ScheduleVo vo);

	/**
	 * @param committeeMemberships
	 * @return
	 */
	public List<CommitteeMemberRoles> fetchCommitteeMemberRoles(CommitteeMemberships committeeMemberships);

	/**
	 * @param scheduleId
	 * @return
	 */
	public List<CommitteeScheduleAttendance> fetchGuestMembers(Integer scheduleId);

	/**
	 * @param scheduleId
	 * @param committeePersonId
	 * @return
	 */
	public Boolean fetchPresentFlag(Integer scheduleId, String committeePersonId);

	/**
	 * @param scheduleAttendance
	 */
	public void updateScheduleAttendance(CommitteeScheduleAttendance scheduleAttendance);

	/**
	 * This method is used to getCommitteeScheduleAttachementById.
	 * @param scheduleId - Id of the CommitteeSchedule.
	 * @return committeeSchedule - committeeSchedule object.
	 */
	public List<CommitteeScheduleAttachment> getCommitteeScheduleAttachementById(Integer scheduleId);
	
	
	ScheduleVo deleteMeetingAttachment(CommitteeScheduleAttachment committeeScheduleAttachment);

	ScheduleVo saveOrUpdateMeetingAttachment(MultipartFile[] files,
			CommitteeScheduleAttachment committeeScheduleAttachment, Integer scheduleId);
	
	ResponseEntity<byte[]> downloadMeetingAttachment(String attachmentId);

}
