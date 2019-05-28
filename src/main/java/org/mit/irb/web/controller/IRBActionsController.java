package org.mit.irb.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.service.IRBActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class IRBActionsController {

	@Autowired
	@Qualifier(value = "irbActionsService")
	IRBActionsService irbActionsService;
	
	protected static Logger logger = Logger.getLogger(IRBController.class.getName());
	
	@RequestMapping(value = "/getActionList", method = RequestMethod.POST)
	public @ResponseBody IRBActionsVO getActionList(@RequestBody IRBActionsVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.getActionList(vo);
		return vo;
	}
	
	@RequestMapping(value = "/performProtocolActions", method = RequestMethod.POST)
	public @ResponseBody IRBActionsVO performProtocolActions(@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam("formDataJson") String formDataJson,HttpServletRequest request,HttpServletResponse response)
	{
		IRBActionsVO vo=new IRBActionsVO();
		ObjectMapper mapper = new ObjectMapper();
		try {
			 vo = mapper.readValue(formDataJson,IRBActionsVO.class);
		} catch (Exception e) {			
			e.printStackTrace();
		} 
		vo = irbActionsService.performProtocolActions(vo,files);
		return vo;
	}
	
	@RequestMapping(value = "/getAmendRenwalSummary", method = RequestMethod.POST)
	public @ResponseBody IRBActionsVO getAmendRenwalSummary(@RequestBody IRBActionsVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.getAmendRenwalSummary(vo);
		return vo;
	}
	
	@RequestMapping(value = "/updateAmendRenwalSummary", method = RequestMethod.POST)
	public @ResponseBody IRBActionsVO updateAmendRenwalSummary(@RequestBody IRBActionsVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.updateAmendRenwalSummary(vo);
		return vo;
	}
	
	@RequestMapping(value = "/getActionLookup", method = RequestMethod.POST)
	public @ResponseBody IRBActionsVO getActionLookup(@RequestBody IRBActionsVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.getActionLookup(vo);
		return vo;
	}
	
	@RequestMapping(value = "/getCommitteeScheduledDates", method = RequestMethod.POST)
	public @ResponseBody IRBActionsVO getCommitteeScheduledDates(HttpServletRequest request,HttpServletResponse response)
	{
		String committeeId = request.getParameter("committeeId");
		IRBActionsVO vo = irbActionsService.getCommitteeScheduledDates(committeeId);
		return vo;
	}
}
