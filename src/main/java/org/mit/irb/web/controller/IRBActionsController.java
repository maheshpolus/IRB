package org.mit.irb.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.SubmissionDetailVO;
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
	
	@RequestMapping(value = "/getCommitteeList", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO getCommitteeList(@RequestBody SubmissionDetailVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.getCommitteeList(vo);
		return vo;
	}
	
	@RequestMapping(value = "/getIRBAdminList", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO getIRBAdminList(HttpServletRequest request,HttpServletResponse response)
	{
		SubmissionDetailVO vo = new SubmissionDetailVO();
		vo = irbActionsService.getIRBAdminList(vo); 
		return vo;
	}
	
	@RequestMapping(value = "/updateIRBAdmin", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO updateIRBAdmin(@RequestBody SubmissionDetailVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.updateIRBAdmin(vo); 
		return vo;
	}
	
	@RequestMapping(value = "/updateIRBAdminReviewer", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO updateIRBAdminReviewer(@RequestBody SubmissionDetailVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.updateIRBAdminReviewer(vo); 
		return vo;
	}
	
	@RequestMapping(value = "/getSubmissionLookups", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO getSubmissionLookups(HttpServletRequest request,HttpServletResponse response)
	{
		SubmissionDetailVO vo = new SubmissionDetailVO();
		vo = irbActionsService.getSubmissionLookups(vo); 
		return vo;
	}
	
	@RequestMapping(value = "/getCommitteeMembers", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO getCommitteeMembers(@RequestBody SubmissionDetailVO vo,HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.getCommitteeMembers(vo); 
		return vo;
	}
	
	@RequestMapping(value = "/getSubmissionHistory", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO getSubmissionHistory(@RequestBody SubmissionDetailVO vo,HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.getSubmissionHistory(vo); 
		return vo;
	}
	
	@RequestMapping(value = "/getIRBAdminReviewDetails", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO getIRBAdminReviewDetails(@RequestBody SubmissionDetailVO vo,HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.getIRBAdminReviewDetails(vo); 
		return vo;
	}
	@RequestMapping(value = "/getSubmissionBasicDetails", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO getSubmissionBasicDetails(@RequestBody SubmissionDetailVO vo,HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.getSubmissionBasicDetails(vo);  
		return vo;
	}
	
	@RequestMapping(value = "/updateIRBAdminComment", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO updateIRBAdminComment(@RequestBody SubmissionDetailVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.updateIRBAdminComment(vo); 
		return vo;
	}
	
	@RequestMapping(value = "/updateIRBAdminAttachments", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO updateIRBAdminAttachments(@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam("formDataJson") String formDataJson, HttpServletRequest request,HttpServletResponse response)
	{
		SubmissionDetailVO submissionDetailVO=new SubmissionDetailVO();
		ObjectMapper mapper = new ObjectMapper();
		try {
			submissionDetailVO = mapper.readValue(formDataJson,SubmissionDetailVO.class);
		} catch (Exception e) {			
			e.printStackTrace();
		} 
		submissionDetailVO = irbActionsService.updateIRBAdminAttachments(submissionDetailVO,files); 
		return submissionDetailVO;
	}
	
	@RequestMapping(value = "/updateCommitteeVotingDetail", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO updateCommitteeVotingDetail(@RequestBody SubmissionDetailVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.updateCommitteeVotingDetail(vo); 
		return vo;
	}
	
	@RequestMapping(value = "/updateBasicSubmissionDetail", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO updateBasicSubmissionDetail(@RequestBody SubmissionDetailVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.updateBasicSubmissionDetail(vo); 
		return vo;
	}
	
	@RequestMapping(value = "/getIRBAdminReviewers", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO getIRBAdminReviewers(@RequestBody SubmissionDetailVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.getIRBAdminReviewers(vo); 
		return vo;
	}
	
	@RequestMapping(value = "/updateIRBAdminCheckList", method = RequestMethod.POST)
	public @ResponseBody SubmissionDetailVO updateIRBAdminCheckList(@RequestBody SubmissionDetailVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbActionsService.updateIRBAdminCheckList(vo); 
		return vo;
	}
}
