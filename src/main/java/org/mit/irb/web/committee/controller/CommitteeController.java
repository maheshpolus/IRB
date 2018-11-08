package org.mit.irb.web.committee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.mit.irb.web.committee.service.CommitteeService;
import org.mit.irb.web.committee.vo.CommitteeVo;

@Controller
public class CommitteeController {

	protected static Logger logger = Logger.getLogger(CommitteeController.class.getName());

	@Autowired
	@Qualifier(value = "committeeService")
	private CommitteeService committeeService;

	@RequestMapping(value = "/createCommittee", method = RequestMethod.POST)
	public ResponseEntity<String> createCommittee(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = HttpStatus.OK;
		logger.info("Requesting for createCommittee");
		logger.info("Committee Type Code : " + vo.getCommitteeTypeCode());
		String committeeDatas = committeeService.createCommittee(vo.getCommitteeTypeCode());
		return new ResponseEntity<String>(committeeDatas, status);
	}

	@RequestMapping(value = "/saveCommittee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> saveCommittee(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommittee");
		HttpStatus status = HttpStatus.OK;
		String committeeDatas = committeeService.saveCommittee(vo);
		return new ResponseEntity<String>(committeeDatas, status);
	}

	@RequestMapping(value = "/loadCommitteeById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> loadCommitteeById(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadCommitteeById");
		HttpStatus status = HttpStatus.OK;
		logger.info("CommitteeId : " + vo.getCommitteeId());
		String committeeDatas = committeeService.loadCommitteeById(vo.getCommitteeId());
		return new ResponseEntity<String>(committeeDatas, status);
	}

	@RequestMapping(value = "/addSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> addSchedule(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addSchedule");
		HttpStatus status = HttpStatus.OK;
		String committeeDatas = "";
		try {
			committeeDatas = committeeService.addSchedule(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(committeeDatas, status);
	}

	/*@RequestMapping(value = "/fetchInitialDatas", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchInitialDatas(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for fetchInitialDatas");
		String initialDatas = committeeService.fetchInitialDatas();
		return initialDatas;
	}*/

	@RequestMapping(value = "/saveAreaOfResearch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> saveAreaOfResearch(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = HttpStatus.OK;
		logger.info("Requesting for saveAreaOfResearch");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ResearchAreaCode : " + vo.getCommitteeResearchArea().getResearchAreaCode());
		logger.info("ResearchAreaDescription : " + vo.getCommitteeResearchArea().getResearchAreaDescription());
		String committeeDatas = committeeService.saveAreaOfResearch(vo);
		return new ResponseEntity<String> (committeeDatas, status);
	}

	@RequestMapping(value = "/deleteAreaOfResearch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> deleteAreaOfResearch(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = HttpStatus.OK;
		logger.info("Requesting for deleteAreaOfResearch");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("researchAreaId : " + vo.getCommResearchAreasId());
		String committeeDatas = committeeService.deleteAreaOfResearch(vo);
		return new ResponseEntity<String> (committeeDatas,status);
	}

	@RequestMapping(value = "/deleteSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> deleteSchedule(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteSchedule");
		HttpStatus status = HttpStatus.OK;
		logger.info("scheduleId : " + vo.getScheduleId());
		logger.info("committeeId : " + vo.getCommitteeId());
		String committeeDatas =  committeeService.deleteSchedule(vo);
		return new ResponseEntity<String> (committeeDatas,status);
	}

	@RequestMapping(value = "/filterCommitteeScheduleDates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> filterCommitteeScheduleDates(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for filterCommitteeScheduleDates");
		HttpStatus status = HttpStatus.OK;
		String committeeDatas =  committeeService.filterCommitteeScheduleDates(vo);
		return new ResponseEntity<String> (committeeDatas,status);
	}

	@RequestMapping(value = "/resetCommitteeScheduleDates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> resetCommitteeScheduleDates(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for resetCommitteeScheduleDates");
		HttpStatus status = HttpStatus.OK;
		String committeeDatas =  committeeService.resetCommitteeScheduleDates(vo);
		return new ResponseEntity<String> (committeeDatas,status);
	}

	@RequestMapping(value = "/updateCommitteeSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> updateCommitteeSchedule(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateCommitteeSchedule");
		HttpStatus status = HttpStatus.OK;
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ScheduleId : " + vo.getCommitteeSchedule().getScheduleId());
		String committeeDatas = committeeService.updateCommitteeSchedule(vo);
		return new ResponseEntity<String> (committeeDatas,status);
	}

}
