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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@RestController
public class ScheduleController {

	protected static Logger logger = Logger.getLogger(ScheduleController.class.getName());

	@Autowired
	@Qualifier(value = "scheduleService")
	private ScheduleService scheduleService;
	
	@RequestMapping(value = "/loadScheduleById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> loadScheduleById(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadScheduleById");
		HttpStatus status= HttpStatus.OK;
		logger.info("scheduleId : " + vo.getScheduleId());
		String committeeDatas = scheduleService.loadScheduleById(vo.getScheduleId());
		return new ResponseEntity<String> (committeeDatas,status);
	}

	@RequestMapping(value = "/updateSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> updateSchedule(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateSchedule");
		HttpStatus status= HttpStatus.OK;
		String scheduleDatas = scheduleService.updateSchedule(vo);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}

	@RequestMapping(value = "/addOtherActions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> addOtherActions(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status= HttpStatus.OK;
		logger.info("Requesting for addOtherActions");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("scheduleId : " + vo.getScheduleId());
		logger.info("scheduleActItemTypecode : " + vo.getCommitteeScheduleActItems().getScheduleActItemTypecode());
		logger.info("scheduleActItemTypeDescription : " + vo.getCommitteeScheduleActItems().getScheduleActItemTypeDescription());
		logger.info("itemDescription : " + vo.getCommitteeScheduleActItems().getItemDescription());
		String scheduleDatas = scheduleService.addOtherActions(vo);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}

	@RequestMapping(value = "/deleteOtherActions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> deleteOtherActions(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status= HttpStatus.OK;
		logger.info("Requesting for deleteOtherActions");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("actionItemId : " + vo.getCommScheduleActItemsId());
		String scheduleDatas = scheduleService.deleteOtherActions(vo);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}

	@RequestMapping(value = "/addCommitteeScheduleMinute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> addCommitteeScheduleMinute(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status= HttpStatus.OK;
		logger.info("Requesting for addCommitteeScheduleMinute");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		String scheduleDatas =  scheduleService.addCommitteeScheduleMinute(vo);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}

	@RequestMapping(value = "/updateScheduleAttendance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> updateCommitteeSchedule(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateScheduleAttendance");
		HttpStatus status= HttpStatus.OK;
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ScheduleId : " + vo.getScheduleId());
		logger.info("AttendanceId : " + vo.getUpdatedAttendance().getCommitteeScheduleAttendanceId());
		logger.info("MemberPresent : " + vo.getUpdatedAttendance().getMemberPresent());
		String scheduleDatas =  scheduleService.updateScheduleAttendance(vo);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}

	@RequestMapping(value = "/addOthersPresent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> addOthersPresent(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addOthersPresent");
		HttpStatus status= HttpStatus.OK;
		logger.info("ScheduleId : " + vo.getScheduleId());
		String scheduleDatas =  scheduleService.addOthersPresent(vo);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}

	@RequestMapping(value = "/deleteScheduleMinute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> deleteScheduleMinute(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteScheduleMinute");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("CommitteeScheduleMinuteId : " + vo.getCommScheduleMinuteId());
		HttpStatus status= HttpStatus.OK;
		String scheduleDatas =  scheduleService.deleteScheduleMinute(vo);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}

	@RequestMapping(value = "/deleteScheduleAttachment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> deleteScheduleAttachment(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteScheduleAttachment");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("CommitteeScheduleAttachId : " + vo.getCommScheduleAttachId());
		HttpStatus status= HttpStatus.OK;
		String scheduleDatas =  scheduleService.deleteScheduleAttachment(vo);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}

	@RequestMapping(value = "/addScheduleAttachment", method = RequestMethod.POST)
	public ResponseEntity<String> addScheduleAttachment(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestParam("formDataJson") String formDataJson) {
		logger.info("Requesting for addScheduleAttachment");
		HttpStatus status= HttpStatus.OK;
		String scheduleDatas =  scheduleService.addScheduleAttachment(files, formDataJson);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}

	@RequestMapping(value = "/updateScheduleAttachment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> updateScheduleAttachment(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateScheduleAttendance");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ScheduleId : " + vo.getScheduleId());
		logger.info("AttachmentId : " + vo.getNewCommitteeScheduleAttachment().getCommScheduleAttachId());
		HttpStatus status= HttpStatus.OK;
		String scheduleDatas = scheduleService.updateScheduleAttachment(vo);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}

	@RequestMapping(value = "/deleteScheduleAttendance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> deleteScheduleAttendance(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteScheduleAttachment");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("CommitteeScheduleAttendanceId : " + vo.getCommScheduleAttendanceId());
		HttpStatus status= HttpStatus.OK;
		String scheduleDatas =  scheduleService.deleteScheduleAttendance(vo);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}

	@RequestMapping(value = "/downloadScheduleAttachment", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadScheduleAttachment(HttpServletResponse response, @RequestHeader("commScheduleAttachId") String commScheduleAttachId) {
		logger.info("Requesting for downloadScheduleAttachment");
		logger.info("commScheduleAttachId : " + commScheduleAttachId);
		Integer attachmentid = Integer.parseInt(commScheduleAttachId);
		return scheduleService.downloadScheduleAttachment(attachmentid);
	}

	@RequestMapping(value = "/updateCommitteeScheduleMinute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> updateCommitteeScheduleMinute(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateCommitteeScheduleMinute");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		HttpStatus status= HttpStatus.OK;
		String scheduleDatas =  scheduleService.updateCommitteeScheduleMinute(vo);
		return  new ResponseEntity<String> (scheduleDatas,status);
	}
}
