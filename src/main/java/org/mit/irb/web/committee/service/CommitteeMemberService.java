package org.mit.irb.web.committee.service;

import org.springframework.stereotype.Service;
import org.mit.irb.web.committee.pojo.CommitteeMemberships;
import org.mit.irb.web.committee.vo.CommitteeVo;

@Service
public interface CommitteeMemberService {

	/**
	 * This method is used to add committee members.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details of committee with CommitteeMembership.
	 */
	public CommitteeVo addCommitteeMembership(CommitteeVo committeeVo);

	/**
	 * This method is used to save committee member.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details of committee with CommitteeMembership.
	 */
	
	public CommitteeVo saveCommitteeMembers(CommitteeVo committeeVo);

	/**
	 * This method is used to delete committee members.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details of committee with updated list of CommitteeMembership.
	 */
	public CommitteeVo deleteCommitteeMembers(CommitteeVo committeeVo);

	/**
	 * This method is used to save committee member roles. 
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details of committee with list of CommitteeMembershipRoles.
	 */
	public CommitteeVo saveCommitteeMembersRole(CommitteeVo committeeVo);

	/**
	 * This method is used to delete member roles.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details of committee with updated list of CommitteeMembershipRoles.
	 */
	public CommitteeVo deleteMemberRoles(CommitteeVo committeeVo);

	/**
	 * This method is used to update member roles.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details of committee with updated list of CommitteeMembershipRoles.
	 */
	public CommitteeVo updateMemberRoles(CommitteeVo committeeVo);

	/**
	 * This method is used to delete expertise.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details of committee with updated list of CommitteeMemberExpertise.
	 */
	public CommitteeVo deleteExpertise(CommitteeVo committeeVo);

	/**
	 * This method is used to save committee members expertise.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return  a CommitteeVo of details of committee with list of CommitteeMemberExpertise.
	 */
	public CommitteeVo saveCommitteeMembersExpertise(CommitteeVo committeeVo);


	/**
	 * @param vo
	 * @return add,update,delete committee members
	 */
	public CommitteeVo updateCommitteeMemberDetails(CommitteeVo vo);

	/**
	 * @param vo
	 * @return load Committee member term history
	 */
	public CommitteeVo loadTerHistory(CommitteeVo vo);

	/**
	 * @param vo
	 * @return load committee members details
	 */
	public CommitteeVo loadCommitteeMemberDetails(CommitteeVo vo);

	/**
	 * @param vo
	 * @return update committee members role
	 */
	public CommitteeVo updateMemberRole(CommitteeVo vo);

	/**
	 * @param vo
	 * @return
	 */
	public CommitteeVo updateMemberExpertise(CommitteeVo vo);
}
