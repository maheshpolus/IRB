package org.mit.irb.web.schedule.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.schedule.service.ScheduleService;
import org.mit.irb.web.schedule.vo.ScheduleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ScheduleController {

	protected static Logger logger = Logger.getLogger(ScheduleController.class.getName());

	@Autowired
	@Qualifier(value = "scheduleService")
	private ScheduleService scheduleService;
	
	@RequestMapping(value = "/loadScheduleById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo loadScheduleById(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {		logger.info("Requesting for loadScheduleById");
		logger.info("scheduleId : " + vo.getScheduleId());
		ScheduleVo ScheduleVo = scheduleService.loadScheduleById(vo);
		return ScheduleVo;
	}
	
	@RequestMapping(value = "/loadScheduleHeaderDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo loadScheduleHeaderDetails(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadScheduleById");
		logger.info("scheduleId : " + vo.getScheduleId());
		ScheduleVo ScheduleVo = null ; /*scheduleService.loadScheduleById(vo.getScheduleId());*/
		return ScheduleVo;
	}	

	@RequestMapping(value = "/updateSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo updateSchedule(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateSchedule");
		ScheduleVo scheduleVo = scheduleService.updateSchedule(vo);
		return scheduleVo;
	}

	@RequestMapping(value = "/addOtherActions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo addOtherActions(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addOtherActions");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("scheduleId : " + vo.getScheduleId());
		logger.info("scheduleActItemTypecode : " + vo.getCommitteeScheduleActItems().getScheduleActItemTypecode());
		logger.info("scheduleActItemTypeDescription : " + vo.getCommitteeScheduleActItems().getScheduleActItemTypeDescription());
		logger.info("itemDescription : " + vo.getCommitteeScheduleActItems().getItemDescription());
		ScheduleVo scheduleVo = scheduleService.addOtherActions(vo);
		return scheduleVo;
	}

	@RequestMapping(value = "/deleteOtherActions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo deleteOtherActions(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteOtherActions");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("actionItemId : " + vo.getCommScheduleActItemsId());
		ScheduleVo scheduleVo = scheduleService.deleteOtherActions(vo);
		return scheduleVo;
	}

	@RequestMapping(value = "/addCommitteeScheduleMinute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo addCommitteeScheduleMinute(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addCommitteeScheduleMinute");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		ScheduleVo scheduleVo =  scheduleService.addCommitteeScheduleMinute(vo);
		return scheduleVo;
	}

	@RequestMapping(value = "/updateScheduleAttendance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo updateCommitteeSchedule(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateScheduleAttendance");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ScheduleId : " + vo.getScheduleId());
		/*logger.info("AttendanceId : " + vo.getUpdatedAttendance().getCommitteeScheduleAttendanceId());
		logger.info("MemberPresent : " + vo.getUpdatedAttendance().getMemberPresent());*/
		ScheduleVo scheduleVo =  scheduleService.updateScheduleAttendance(vo);
		return scheduleVo;
	}

	@RequestMapping(value = "/addOthersPresent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo addOthersPresent(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addOthersPresent");
		logger.info("ScheduleId : " + vo.getScheduleId());
		ScheduleVo scheduleVo =  scheduleService.addOthersPresent(vo);
		return scheduleVo;
	}

	@RequestMapping(value = "/deleteScheduleMinute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo deleteScheduleMinute(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteScheduleMinute");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("CommitteeScheduleMinuteId : " + vo.getCommScheduleMinuteId());
		ScheduleVo scheduleVo =  scheduleService.deleteScheduleMinute(vo);
		return scheduleVo;
	}

	@RequestMapping(value = "/deleteScheduleAttachment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo deleteScheduleAttachment(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteScheduleAttachment");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("CommitteeScheduleAttachId : " + vo.getCommScheduleAttachId());
		ScheduleVo scheduleVo =  scheduleService.deleteScheduleAttachment(vo);
		return scheduleVo;
	}

	@RequestMapping(value = "/addScheduleAttachment", method = RequestMethod.POST)
	public @ResponseBody ScheduleVo addScheduleAttachment(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestParam("formDataJson") String formDataJson) {
		logger.info("Requesting for addScheduleAttachment");
		ScheduleVo scheduleVo =  scheduleService.addScheduleAttachment(files, formDataJson);
		return scheduleVo;
	}

	@RequestMapping(value = "/updateScheduleAttachment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo updateScheduleAttachment(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateScheduleAttendance");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ScheduleId : " + vo.getScheduleId());
		logger.info("AttachmentId : " + vo.getNewCommitteeScheduleAttachment().getCommScheduleAttachId());
		ScheduleVo scheduleVo = scheduleService.updateScheduleAttachment(vo);
		return scheduleVo;
	}

	@RequestMapping(value = "/deleteScheduleAttendance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo deleteScheduleAttendance(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteScheduleAttachment");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("CommitteeScheduleAttendanceId : " + vo.getCommScheduleAttendanceId());
		ScheduleVo ScheduleVo =  scheduleService.deleteScheduleAttendance(vo);
		return ScheduleVo;
	}

	@RequestMapping(value = "/downloadScheduleAttachment", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadScheduleAttachment(HttpServletResponse response, @RequestHeader("commScheduleAttachId") String commScheduleAttachId) {
		logger.info("Requesting for downloadScheduleAttachment");
		logger.info("commScheduleAttachId : " + commScheduleAttachId);
		Integer attachmentid = Integer.parseInt(commScheduleAttachId);
		return scheduleService.downloadScheduleAttachment(attachmentid);
	}

	@RequestMapping(value = "/updateCommitteeScheduleMinute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo updateCommitteeScheduleMinute(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateCommitteeScheduleMinute");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		ScheduleVo ScheduleVo =  scheduleService.updateCommitteeScheduleMinute(vo);
		return ScheduleVo;
	}
	
	@RequestMapping(value = "/loadScheduledProtocols", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo loadScheduledProtocols(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadScheduledProtocols");
		ScheduleVo scheduleVo = scheduleService.loadScheduledProtocols(vo);
		return scheduleVo;
	}
	
	@RequestMapping(value = "/loadScheduleMeetingComments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo loadScheduleMeetingComments(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadScheduleMeetingComments");
		ScheduleVo scheduleVo = scheduleService.loadScheduleMeetingComments(vo);
		return scheduleVo;
	}
	
	@RequestMapping(value = "/loadScheduleProtocolComments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo loadScheduleProtocolComments(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadScheduleMeetingComments");
		ScheduleVo scheduleVo = scheduleService.loadScheduleProtocolComments(vo);
		return scheduleVo;
	}
	
	@RequestMapping(value = "/createAgendaForSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo createAgendaForSchedule(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for createAgendaForSchedule");
		ScheduleVo scheduleVo = scheduleService.createAgendaForSchedule(vo);
		return scheduleVo;
	}
		
	@RequestMapping(value = "/loadMeetingAttendence", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo loadMeetingAttendence(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadScheduledProtocols");
		ScheduleVo scheduleVo = scheduleService.loadMeetingAttendence(vo);
		return scheduleVo;
	}
	
	@RequestMapping(value = "/updateMeetingAttendence", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo updateMeetingAttendence(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadScheduledProtocols");
		ScheduleVo scheduleVo = scheduleService.updateMeetingAttendence(vo);
		return scheduleVo;
	}
		
	@RequestMapping(value = "/loadMeetingAttachmentById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo loadMeetingAttachmentById(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {		logger.info("Requesting for loadScheduleById");
		logger.info("scheduleId : " + vo.getScheduleId());
		ScheduleVo ScheduleVo = scheduleService.loadMeetingAttachmentById(vo.getScheduleId());
		return ScheduleVo;
	}
	
	@RequestMapping(value = "/saveOrUpdateMeetingAttachment", method = RequestMethod.POST)
	public @ResponseBody ScheduleVo saveOrUpdateMeetingAttachment(
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam("formDataJson") String formDataJson) {
		logger.info("Request for saveOrUpdateMeetingAttachment");
		ScheduleVo protocolVO = new ScheduleVo();
		protocolVO = scheduleService.saveOrUpdateMeetingAttachment(files, formDataJson);
		return protocolVO;
	}
		
	@RequestMapping(value = "/downloadMeetingAttachment", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadMeetingAttachment(HttpServletResponse response,
			@RequestHeader("attachmentId") String attachmentId) {
		return scheduleService.downloadMeetingAttachment(attachmentId);
	}
	
	@RequestMapping(value = "/downloadScheduleAgenda", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadScheduleAgenda(HttpServletResponse response,
			@RequestHeader("scheduleId") String scheduleId) {
		return scheduleService.downloadScheduleAgenda(scheduleId);
	}
	
	@RequestMapping(value = "/loadAllScheduleAgenda", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo loadAllScheduleAgenda(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {		logger.info("Requesting for loadScheduleById");
		logger.info("scheduleId : " + vo.getScheduleId());
		ScheduleVo ScheduleVo = scheduleService.loadAllScheduleAgenda(vo.getScheduleId());
		return ScheduleVo;
	}
	
	@RequestMapping(value = "/loadMeetingOtherActions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo loadMettingOtherActions(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {		logger.info("Requesting for loadMettingOtherActions");
		logger.info("scheduleId : " + vo.getScheduleId());
		ScheduleVo ScheduleVo = scheduleService.loadMeetingOtherActions(vo.getScheduleId());
		return ScheduleVo;
	}
		
	@RequestMapping(value = "/updateMeetingOtherActions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo updateMeetingOtherActions(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addMeetingOtherActions");
		logger.info("scheduleId : " + vo.getScheduleId());
		ScheduleVo scheduleVo = scheduleService.updateMeetingOtherActions(vo);
		return scheduleVo;
	}
	
	@RequestMapping(value = "/createMinuteForSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo createMinuteForSchedule(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for createMinuteForSchedule");
		ScheduleVo scheduleVo = scheduleService.createMinuteForSchedule(vo);
		return scheduleVo;
	}
	
	@RequestMapping(value = "/downloadScheduleAgendaById", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadScheduleAgendaById(HttpServletResponse response,
			@RequestHeader("scheduleAgendaId") String scheduleAgendaId) {
		return scheduleService.downloadScheduleAgendaById(scheduleAgendaId);
	}
		
	@RequestMapping(value = "/downloadScheduleMinute", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadScheduleMinute(HttpServletResponse response,
			@RequestHeader("scheduleId") String scheduleId) {
		return scheduleService.downloadScheduleMinute(scheduleId);
	}
	
	@RequestMapping(value = "/downloadScheduleMinuteById", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadScheduleMinuteById(HttpServletResponse response,
			@RequestHeader("scheduleMinuteDocId") String scheduleMinuteDocId) {
		return scheduleService.downloadScheduleMinuteById(scheduleMinuteDocId);
	}
	
	@RequestMapping(value = "/loadAllScheduleMinutes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo loadAllScheduleMinutes(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {		logger.info("Requesting for loadScheduleById");
		logger.info("scheduleId : " + vo.getScheduleId());
		ScheduleVo ScheduleVo = scheduleService.loadAllScheduleMinutes(vo.getScheduleId());
		return ScheduleVo;
	}
	
	@RequestMapping(value = "/showAllMeetingAttendence", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ScheduleVo showAllMeetingAttendence(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadScheduledProtocols");
		ScheduleVo scheduleVo = scheduleService.showAllMeetingAttendence(vo);
		return scheduleVo;
	}
}
