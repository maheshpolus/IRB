package org.mit.irb.web.schedule.service;

import org.mit.irb.web.schedule.vo.ScheduleVo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public interface ScheduleService {

	/**
	 * This method is used to load schedule by Id.
	 * @param vo - Id of a schedule.
	 * @return a ScheduleVo of details of schedule.
	 */
	public ScheduleVo loadScheduleById(ScheduleVo vo);

	/**
	 * This method is used to update schedule.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a ScheduleVo of details of updated schedule.
	 */
	public ScheduleVo updateSchedule(ScheduleVo scheduleVo);

	/**
	 * This method is used to add other actions for a schedule.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a ScheduleVo of details of schedule data with a list of other actions.
	 */
	public ScheduleVo addOtherActions(ScheduleVo scheduleVo);

	/**
	 * This method is used to delete other actions.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a ScheduleVo of details of schedule data with updated list of other actions.
	 */
	public ScheduleVo deleteOtherActions(ScheduleVo scheduleVo);

	/**
	 * This method is used to add committee schedule minute.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a ScheduleVo of details of schedule data with minutes.
	 */
	public ScheduleVo addCommitteeScheduleMinute(ScheduleVo scheduleVo);

	/**
	 * This method is used to update schedule attendance.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a ScheduleVo of details of schedule data with list of attendance.
	 */
	public ScheduleVo updateScheduleAttendance(ScheduleVo scheduleVo);

	/**
	 * This method is used for attendance to mark present member.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a ScheduleVo of details of schedule data with present member.
	 */
	public ScheduleVo addOthersPresent(ScheduleVo scheduleVo);

	/**
	 * This method is used to delete schedule minute.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of schedule data with updated list of minutes.
	 */
	public ScheduleVo deleteScheduleMinute(ScheduleVo scheduleVo);

	/**
	 * This method is used to delete schedule attachment.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of schedule data with updated list of attachment.
	 */
	public ScheduleVo deleteScheduleAttachment(ScheduleVo scheduleVo);

	/**
	 * This method is used to add schedule attachment.
	 * @param files - attached files.
	 * @param formDataJSON - form data for the attachment.
	 * @return a ScheduleVo of details of schedule data with list of attachments.
	 */
	public ScheduleVo addScheduleAttachment(MultipartFile[] files, String formDataJSON);

	/**
	 * This method is used to delete schedule attendance.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a ScheduleVo of details of schedule data with updated list of attendance.
	 */
	public ScheduleVo deleteScheduleAttendance(ScheduleVo scheduleVo);

	/**
	 * This method is used to update schedule attachment.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a ScheduleVo of details of schedule data with updated list of attachments.
	 */
	public ScheduleVo updateScheduleAttachment(ScheduleVo scheduleVo);

	/**
	 * This method is used to download schedule attachment.
	 * @param attachmentId - Id of the attachment to download.
	 * @return attachmentData.
	 */
	public ResponseEntity<byte[]> downloadScheduleAttachment(Integer attachmentId);

	/**
	 * This method is used to update committee schedule minute.
	 * @param vo - Object of ScheduleVo.
	 * @return a ScheduleVo of details of schedule data with updated minute.
	 */
	public ScheduleVo updateCommitteeScheduleMinute(ScheduleVo vo);

	/**
	 * @param scheduleId
	 * @return
	 */
	public ScheduleVo loadScheduleBasicDetail(Integer scheduleId);

	/**
	 * @param vo
	 * @return
	 */
	public ScheduleVo loadScheduledProtocols(ScheduleVo vo);

	/**
	 * @param vo
	 * @return
	 */
	public ScheduleVo loadScheduleMeetingComments(ScheduleVo vo);

	/**
	 * @param vo
	 * @return
	 */
	public ScheduleVo loadScheduleProtocolComments(ScheduleVo vo);

	/**
	 * @param vo
	 * @return
	 */
	public ScheduleVo createAgendaForSchedule(ScheduleVo vo);
	
	/**
	 * @param vo
	 * @return load attendance detail of meeting
	 */
	public ScheduleVo loadMeetingAttendence(ScheduleVo vo);

	/**
	 * @param vo
	 * @return
	 */
	public ScheduleVo updateMeetingAttendence(ScheduleVo vo);
	/**
	 * This method is used to load meeting attachment by Id.
	 * @param scheduleId - Id of a schedule.
	 * @return a ScheduleVo of details of schedule.
	 */
	public ScheduleVo loadMeetingAttachmentById(Integer scheduleId);
	
	/**
	 * This method is used to saveOrUpdateMeetingAttachment.
	 * @param files.
	 * @return a ScheduleVo of details of schedule.
	 */
	public ScheduleVo saveOrUpdateMeetingAttachment(MultipartFile[] files, String formDataJson);
	
	
	/**
	 * @param attachmentId
	 * Service to download the attachments
	 */
	ResponseEntity<byte[]> downloadMeetingAttachment(String attachmentId);
	
	/**
	 * @param vo
	 * @return
	 */
	public ScheduleVo loadScheduleIdsForAgenda(ScheduleVo vo);

	/**
	 * @param scheduleId
	 * @return
	 */
	public ResponseEntity<byte[]> downloadScheduleAgenda(String scheduleId);

	/**
	 * @param scheduleId
	 * @return
	 */
	public ScheduleVo loadAllScheduleAgenda(Integer scheduleId);
	
	/**
	 * This method is used to loadMeetingOtherActions.
	 * @param scheduleId - Id of a schedule.
	 * @return a ScheduleVo of details of schedule.
	 */
	public ScheduleVo loadMeetingOtherActions(Integer scheduleId);
	
	/**
	 * This method is used to addMeetingOtherActions.
	 * @param scheduleId - Id of a schedule.
	 * @return a ScheduleVo of details of schedule.
	 */
	public ScheduleVo updateMeetingOtherActions(ScheduleVo scheduleVo);

	/**
	 * @param vo
	 * @return
	 */
	public ScheduleVo createMinuteForSchedule(ScheduleVo vo);

	/**
	 * @param scheduleAgendaId
	 * @return
	 */
	public ResponseEntity<byte[]> downloadScheduleAgendaById(String scheduleAgendaId);

	/**
	 * @param scheduleId
	 * @return
	 */
	public ResponseEntity<byte[]> downloadScheduleMinute(String scheduleId);

	/**
	 * @param scheduleMinuteDocId
	 * @return
	 */
	public ResponseEntity<byte[]> downloadScheduleMinuteById(String scheduleMinuteDocId);

	/**
	 * @param scheduleId
	 * @return
	 */
	public ScheduleVo loadAllScheduleMinutes(Integer scheduleId);
}
