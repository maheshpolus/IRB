package org.mit.irb.web.committee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.committee.service.CommitteeService;
import org.mit.irb.web.committee.vo.CommitteeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommitteeController {

	protected static Logger logger = Logger.getLogger(CommitteeController.class.getName());

	@Autowired
	@Qualifier(value = "committeeService")
	private CommitteeService committeeService;

	@RequestMapping(value = "/createCommittee", method = RequestMethod.POST)
	public @ResponseBody CommitteeVo createCommittee(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for createCommittee");
		CommitteeVo committeeVo = committeeService.createCommittee();
		return committeeVo;
	}

	@RequestMapping(value = "/saveCommittee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo saveCommittee(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommittee");
		CommitteeVo committeeVo = committeeService.saveCommittee(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/loadCommitteeById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo loadCommitteeById(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response)  {
		logger.info("Requesting for loadCommitteeById");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		CommitteeVo committeeVo = committeeService.loadCommitteeById(vo.getCommitteeId());
		return committeeVo;
	}

	@RequestMapping(value = "/addSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo addSchedule(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addSchedule");
		CommitteeVo committeeVo = null;
		try {
			committeeVo = committeeService.addSchedule(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return committeeVo;
	}

	/*@RequestMapping(value = "/fetchInitialDatas", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchInitialDatas(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for fetchInitialDatas");
		String initialDatas = committeeService.fetchInitialDatas();
		return initialDatas;
	}*/

	@RequestMapping(value = "/saveAreaOfResearch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo saveAreaOfResearch(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveAreaOfResearch");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ResearchAreaCode : " + vo.getCommitteeResearchArea().getResearchAreaCode());
		logger.info("ResearchAreaDescription : " + vo.getCommitteeResearchArea().getResearchAreaDescription());
		CommitteeVo committeeVo = committeeService.saveAreaOfResearch(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/deleteAreaOfResearch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo deleteAreaOfResearch(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteAreaOfResearch");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("researchAreaId : " + vo.getCommResearchAreasId());
		CommitteeVo committeeVo = committeeService.deleteAreaOfResearch(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/deleteSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo deleteSchedule(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteSchedule");
		logger.info("scheduleId : " + vo.getScheduleId());
		logger.info("committeeId : " + vo.getCommitteeId());
		CommitteeVo committeeVo =  committeeService.deleteSchedule(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/filterCommitteeScheduleDates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo filterCommitteeScheduleDates(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for filterCommitteeScheduleDates");
		CommitteeVo committeeVo =  committeeService.filterCommitteeScheduleDates(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/resetCommitteeScheduleDates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo resetCommitteeScheduleDates(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for resetCommitteeScheduleDates");
		CommitteeVo committeeVo =  committeeService.resetCommitteeScheduleDates(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/updateCommitteeSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo updateCommitteeSchedule(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateCommitteeSchedule");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ScheduleId : " + vo.getCommitteeSchedule().getScheduleId());
		CommitteeVo committeeVo = committeeService.updateCommitteeSchedule(vo);
		return committeeVo;
	}
	@RequestMapping(value = "/loadHomeUnits", method = RequestMethod.POST)
	public @ResponseBody CommitteeVo loadHomeUnits(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadHomeUnits");
		String homeUnitSearchString = request.getParameter("homeUnitSearchString");
		CommitteeVo committeeVo = committeeService.loadHomeUnits(homeUnitSearchString);
		return committeeVo;
	}
	@RequestMapping(value = "/loadResearchAreas", method = RequestMethod.POST)
	public @ResponseBody CommitteeVo loadResearchAreas(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadResearchAreas");
		String researchSearchString = request.getParameter("researchSearchString");
		CommitteeVo  committeeVo = committeeService.loadResearchAreas(researchSearchString);
		return committeeVo;
	}
	
	@RequestMapping(value = "/loadScheduleDetailsById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo loadScheduleDetailsById(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response)  {
		CommitteeVo committeeVo = committeeService.loadScheduleDetailsById(vo.getCommitteeId(),vo.getAcType());
		return committeeVo;
	}
	
	@RequestMapping(value = "/loadCommitteeMemberById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo loadCommitteeMembers(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response)  {
		CommitteeVo committeeVo = committeeService.loadCommitteeMembers(vo.getCommitteeId());
		return committeeVo;
	}
	
	@RequestMapping(value = "/loadCommitteeMemberDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo loadCommitteeMemberDetails(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response)  {
		CommitteeVo committeeVo = committeeService.loadCommitteeMemberDetails(vo.getCommitteeMemberships());
		return committeeVo;
	}
}
