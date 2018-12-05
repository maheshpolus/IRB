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
import org.springframework.web.bind.annotation.ResponseBody;
import org.mit.irb.web.committee.service.CommitteeMemberService;
import org.mit.irb.web.committee.vo.CommitteeVo;

@Controller
public class CommitteeMemberController {

	protected static Logger logger = Logger.getLogger(CommitteeMemberController.class.getName());

	@Autowired
	@Qualifier(value = "committeeMemberService")
	private CommitteeMemberService committeeMemberService;

	@RequestMapping(value = "/addCommitteeMembership", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo addCommitteeMembership(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addCommitteeMembership");
		CommitteeVo committeeVo = committeeMemberService.addCommitteeMembership(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/saveCommitteeMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo saveCommitteeMembers(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommitteeMembers");
		CommitteeVo committeeVo =  committeeMemberService.saveCommitteeMembers(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/deleteCommitteeMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo deleteCommitteeMembers(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteCommitteeMembers");
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		logger.info("CommitteeId : " + vo.getCommitteeId());
		CommitteeVo committeeVo =   committeeMemberService.deleteCommitteeMembers(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/saveCommitteeMembersRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo saveCommitteeMembersRole(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommitteeMembersRole");
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("MembershipRoleCode : " + vo.getCommitteeMemberRole().getMembershipRoleCode());
		logger.info("MembershipRoleDescription : " + vo.getCommitteeMemberRole().getMembershipRoleDescription());
		logger.info("StartDate : " + vo.getCommitteeMemberRole().getStartDate());
		logger.info("EndDate : " + vo.getCommitteeMemberRole().getEndDate());
		CommitteeVo committeeVo =   committeeMemberService.saveCommitteeMembersRole(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/updateMemberRoles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public  @ResponseBody CommitteeVo updateMemberRoles(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateMemberRoles");
		logger.info("CommMemberRolesId : " + vo.getCommMemberRolesId());
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		CommitteeVo committeeVo =   committeeMemberService.updateMemberRoles(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/deleteMemberRoles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo deleteMemberRoles(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteMemberRoles");
		logger.info("CommMemberRolesId : " + vo.getCommMemberRolesId());
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		CommitteeVo committeeVo =  committeeMemberService.deleteMemberRoles(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/saveCommitteeMembersExpertise", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo saveCommitteeMembersExpertise(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommitteeMembersExpertise");
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ResearchAreaCode : " + vo.getCommitteeMemberExpertise().getResearchAreaCode());
		logger.info("ResearchAreaDescription : " + vo.getCommitteeMemberExpertise().getResearchAreaDescription());
		CommitteeVo committeeVo =  committeeMemberService.saveCommitteeMembersExpertise(vo);
		return committeeVo;
	}

	@RequestMapping(value = "/deleteMemberExpertise", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody CommitteeVo deleteExpertise(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteMemberExpertise");
		logger.info("CommMemberExpertiseId : " + vo.getCommMemberExpertiseId());
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		CommitteeVo committeeVo =  committeeMemberService.deleteExpertise(vo);
		return committeeVo;
	}

}
