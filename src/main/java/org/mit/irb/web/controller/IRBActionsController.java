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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IRBActionsController {

	@Autowired
	@Qualifier(value = "irbActionsService")
	IRBActionsService irbActionsService;
	
	protected static Logger logger = Logger.getLogger(IRBController.class.getName());
	
	@RequestMapping(value = "/getPersonRight", method = RequestMethod.POST)
	public @ResponseBody IRBActionsVO getPersonRight(@RequestBody IRBActionsVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.getPersonRight(vo);
		return vo;
	}
	
	@RequestMapping(value = "/performProtocolActions", method = RequestMethod.POST)
	public @ResponseBody IRBActionsVO performProtocolActions(@RequestBody IRBActionsVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.performProtocolActions(vo);
		return vo;
	}
	
}
