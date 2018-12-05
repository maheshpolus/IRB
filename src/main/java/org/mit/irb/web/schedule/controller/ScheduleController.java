package org.mit.irb.web.schedule.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.schedule.service.ScheduleService;
import org.mit.irb.web.schedule.vo.ScheduleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
	public @ResponseBody ScheduleVo loadScheduleById(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadScheduleById");
		logger.info("scheduleId : " + vo.getScheduleId());
		ScheduleVo ScheduleVo = scheduleService.loadScheduleById(vo.getScheduleId());
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
		logger.info("AttendanceId : " + vo.getUpdatedAttendance().getCommitteeScheduleAttendanceId());
		logger.info("MemberPresent : " + vo.getUpdatedAttendance().getMemberPresent());
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
}
