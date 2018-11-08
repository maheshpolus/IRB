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

import org.mit.irb.web.committee.service.CommitteeMemberService;
import org.mit.irb.web.committee.vo.CommitteeVo;

@Controller
public class CommitteeMemberController {

	protected static Logger logger = Logger.getLogger(CommitteeMemberController.class.getName());

	@Autowired
	@Qualifier(value = "committeeMemberService")
	private CommitteeMemberService committeeMemberService;

	@RequestMapping(value = "/addCommitteeMembership", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> addCommitteeMembership(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addCommitteeMembership");
		HttpStatus status = HttpStatus.OK;
		String committeeDatas = committeeMemberService.addCommitteeMembership(vo);
		return new ResponseEntity<String>(committeeDatas,status);
	}

	@RequestMapping(value = "/saveCommitteeMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> saveCommitteeMembers(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommitteeMembers");
		HttpStatus status = HttpStatus.OK;
		String committeeDatas =  committeeMemberService.saveCommitteeMembers(vo);
		return new ResponseEntity<String>(committeeDatas,status);
	}

	@RequestMapping(value = "/deleteCommitteeMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> deleteCommitteeMembers(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteCommitteeMembers");
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		logger.info("CommitteeId : " + vo.getCommitteeId());
		HttpStatus status = HttpStatus.OK;
		String committeeDatas =   committeeMemberService.deleteCommitteeMembers(vo);
		return new ResponseEntity<String>(committeeDatas,status);
	}

	@RequestMapping(value = "/saveCommitteeMembersRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> saveCommitteeMembersRole(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommitteeMembersRole");
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("MembershipRoleCode : " + vo.getCommitteeMemberRole().getMembershipRoleCode());
		logger.info("MembershipRoleDescription : " + vo.getCommitteeMemberRole().getMembershipRoleDescription());
		logger.info("StartDate : " + vo.getCommitteeMemberRole().getStartDate());
		logger.info("EndDate : " + vo.getCommitteeMemberRole().getEndDate());
		HttpStatus status = HttpStatus.OK;
		String committeeDatas =   committeeMemberService.saveCommitteeMembersRole(vo);
		return new ResponseEntity<String>(committeeDatas,status);
	}

	@RequestMapping(value = "/updateMemberRoles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public  ResponseEntity<String> updateMemberRoles(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateMemberRoles");
		logger.info("CommMemberRolesId : " + vo.getCommMemberRolesId());
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		HttpStatus status = HttpStatus.OK;
		String committeeDatas =   committeeMemberService.updateMemberRoles(vo);
		return new ResponseEntity<String>(committeeDatas,status);
	}

	@RequestMapping(value = "/deleteMemberRoles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> deleteMemberRoles(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteMemberRoles");
		logger.info("CommMemberRolesId : " + vo.getCommMemberRolesId());
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		HttpStatus status = HttpStatus.OK;
		String committeeDatas =  committeeMemberService.deleteMemberRoles(vo);
		return new ResponseEntity<String>(committeeDatas,status);
	}

	@RequestMapping(value = "/saveCommitteeMembersExpertise", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> saveCommitteeMembersExpertise(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommitteeMembersExpertise");
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ResearchAreaCode : " + vo.getCommitteeMemberExpertise().getResearchAreaCode());
		logger.info("ResearchAreaDescription : " + vo.getCommitteeMemberExpertise().getResearchAreaDescription());
		HttpStatus status = HttpStatus.OK;
		String committeeDatas =  committeeMemberService.saveCommitteeMembersExpertise(vo);
		return new ResponseEntity<String>(committeeDatas,status);
	}

	@RequestMapping(value = "/deleteMemberExpertise", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> deleteExpertise(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteMemberExpertise");
		logger.info("CommMemberExpertiseId : " + vo.getCommMemberExpertiseId());
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommMembershipId : " + vo.getCommMembershipId());
		HttpStatus status = HttpStatus.OK;
		String committeeDatas =  committeeMemberService.deleteExpertise(vo);
		return new ResponseEntity<String>(committeeDatas,status);
	}

}
