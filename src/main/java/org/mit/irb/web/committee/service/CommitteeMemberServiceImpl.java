package org.mit.irb.web.committee.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.mit.irb.web.committee.dao.CommitteeDao;
import org.mit.irb.web.committee.pojo.Committee;
import org.mit.irb.web.committee.pojo.CommitteeMemberExpertise;
import org.mit.irb.web.committee.pojo.CommitteeMemberRoles;
import org.mit.irb.web.committee.pojo.CommitteeMemberships;
import org.mit.irb.web.committee.vo.CommitteeVo;
import org.mit.irb.web.committee.pojo.Rolodex;
import org.mit.irb.web.committee.view.PersonDetailsView;

@Transactional
@Service(value = "committeeMemberService")
public class CommitteeMemberServiceImpl implements CommitteeMemberService {

	protected static Logger logger = Logger.getLogger(CommitteeMemberServiceImpl.class.getName());

	@Autowired
	private CommitteeDao committeeDao;

	@Override
	public CommitteeVo addCommitteeMembership(CommitteeVo committeeVo) {
		CommitteeMemberships membership = new CommitteeMemberships();
		Committee committee = committeeVo.getCommittee();
		membership.setNonEmployeeFlag(committeeVo.isNonEmployeeFlag());
		if (committeeVo.isNonEmployeeFlag()) {
			Rolodex rolodex = committeeDao.getRolodexById(committeeVo.getRolodexId());
			membership.setRolodex(rolodex);
			membership.setRolodexId(rolodex.getRolodexId());
			membership.setPersonName(rolodex.getFullName());
		} else {
			PersonDetailsView personDetails = committeeDao.getPersonDetailsById(committeeVo.getPersonId());
			membership.setPersonDetails(personDetails);
			membership.setPersonId(personDetails.getPersonId());
			membership.setPersonName(personDetails.getFullName());
		}
		membership.setMembershipId("0");

		committee.getCommitteeMemberships().add(membership);
		committeeVo.setCommittee(committee);
		committeeVo.setCommitteeMembershipTypes(committeeDao.getMembershipTypes());
		committeeVo.setMembershipRoles(committeeDao.getMembershipRoles());
		return committeeVo;
	}

	@Override
	public CommitteeVo saveCommitteeMembers(CommitteeVo committeeVo) {
		Committee committee = committeeVo.getCommittee();
		List<CommitteeMemberships> committeeMemberships = committee.getCommitteeMemberships();
		if (committeeMemberships != null && !committeeMemberships.isEmpty()) {
			for (CommitteeMemberships committeeMember : committeeMemberships) {
				if (committeeMember.getCommMembershipId() == null) {
					committeeMember.setCommittee(committee);
					committeeMember.setCommitteeMembershipType(committeeDao.getCommitteeMembershipTypeById(committeeMember.getMembershipTypeCode()));
					List<CommitteeMemberRoles> committeeMemberRoles = committeeMember.getCommitteeMemberRoles();
					if (committeeMemberRoles != null && !committeeMemberRoles.isEmpty()) {
						for (CommitteeMemberRoles memberRole : committeeMemberRoles) {
							if (memberRole.getCommMemberRolesId() == null) {
								memberRole.setCommitteeMemberships(committeeMember);
							}
						}
					}
					List<CommitteeMemberExpertise> committeeMemberExpertises = committeeMember.getCommitteeMemberExpertises();
					if (committeeMemberExpertises != null && !committeeMemberExpertises.isEmpty()) {
						for (CommitteeMemberExpertise memberExpertise : committeeMemberExpertises) {
							if (memberExpertise.getCommMemberExpertiseId() == null) {
								memberExpertise.setCommitteeMemberships(committeeMember);
							}
						}
					}
				}
			}
			committee = committeeDao.saveCommittee(committee);
			committeeVo.setCommittee(committee);
		}
		return committeeVo;
	}

	@Override
	public CommitteeVo deleteCommitteeMembers(CommitteeVo committeeVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
			List<CommitteeMemberships> list = committee.getCommitteeMemberships();
			List<CommitteeMemberships> updatedlist = new ArrayList<CommitteeMemberships>(list);
			Collections.copy(updatedlist, list);
			for (CommitteeMemberships committeeMembership : list) {
				if (committeeMembership.getCommMembershipId().equals(committeeVo.getCommMembershipId())) {
					updatedlist.remove(committeeMembership);
				}
				if (committeeMembership.getNonEmployeeFlag()) {
					Rolodex rolodex = committeeDao.getRolodexById(committeeMembership.getRolodexId());
					committeeMembership.setRolodex(rolodex);
				} else {
					PersonDetailsView personDetails = committeeDao.getPersonDetailsById(committeeMembership.getPersonId());
					committeeMembership.setPersonDetails(personDetails);
				}
			}
			committee.getCommitteeMemberships().clear();
			committee.getCommitteeMemberships().addAll(updatedlist);
			committeeDao.saveCommittee(committee);
			committeeVo.setCommittee(committee);
			committeeVo.setStatus(true);
			committeeVo.setMessage("Committee member deleted successfully");
		} catch (Exception e) {
			committeeVo.setStatus(true);
			committeeVo.setMessage("Problem occurred in deleting committee member");
			e.printStackTrace();
		}
		return committeeVo;
	}

	@Override
	public CommitteeVo saveCommitteeMembersRole(CommitteeVo committeeVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
			CommitteeMemberRoles role = committeeVo.getCommitteeMemberRole();
			List<CommitteeMemberships> memberships = committee.getCommitteeMemberships();
			for (CommitteeMemberships committeeMembership : memberships) {
				if (committeeMembership.getCommMembershipId().equals(committeeVo.getCommMembershipId())) {
					CommitteeMemberRoles memberRole = new CommitteeMemberRoles();
					memberRole.setCommitteeMemberships(committeeMembership);
					memberRole.setEndDate(role.getEndDate());
					memberRole.setMembershipRoleCode(role.getMembershipRoleCode());
					memberRole.setMembershipRoleDescription(role.getMembershipRoleDescription());
					memberRole.setStartDate(role.getStartDate());
					memberRole.setUpdateTimestamp(role.getUpdateTimestamp());
					memberRole.setUpdateUser(role.getUpdateUser());
					memberRole = committeeDao.saveCommitteeMemberRole(memberRole);
					committeeMembership.getCommitteeMemberRoles().add(memberRole);
				}
				if (committeeMembership.getNonEmployeeFlag()) {
					Rolodex rolodex = committeeDao.getRolodexById(committeeMembership.getRolodexId());
					committeeMembership.setRolodex(rolodex);
				} else {
					PersonDetailsView personDetails = committeeDao.getPersonDetailsById(committeeMembership.getPersonId());
					committeeMembership.setPersonDetails(personDetails);
				}
			}
			committee = committeeDao.saveCommittee(committee);
			committeeVo.setCommittee(committee);
			committeeVo.setStatus(true);
			committeeVo.setMessage("Committee member role saved successfully");
		} catch (Exception e) {
			committeeVo.setStatus(false);
			committeeVo.setMessage("Problem occurred in saving member role");
			e.printStackTrace();
		}
		return committeeVo;
	}

	@Override
	public CommitteeVo deleteMemberRoles(CommitteeVo committeeVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
			List<CommitteeMemberships> memberships = committee.getCommitteeMemberships();
			for (CommitteeMemberships committeeMembership : memberships) {
				if (committeeMembership.getCommMembershipId().equals(committeeVo.getCommMembershipId())) {
					List<CommitteeMemberRoles> list = committeeMembership.getCommitteeMemberRoles();
					List<CommitteeMemberRoles> updatedlist = new ArrayList<CommitteeMemberRoles>(list);
					Collections.copy(updatedlist, list);
					for (CommitteeMemberRoles role : list) {
						if (role.getCommMemberRolesId().equals(committeeVo.getCommMemberRolesId())) {
							updatedlist.remove(role);
						}
					}
					committeeMembership.getCommitteeMemberRoles().clear();
					committeeMembership.getCommitteeMemberRoles().addAll(updatedlist);
				}
				if (committeeMembership.getNonEmployeeFlag()) {
					Rolodex rolodex = committeeDao.getRolodexById(committeeMembership.getRolodexId());
					committeeMembership.setRolodex(rolodex);
				} else {
					PersonDetailsView personDetails = committeeDao.getPersonDetailsById(committeeMembership.getPersonId());
					committeeMembership.setPersonDetails(personDetails);
				}
			}
			committeeDao.saveCommittee(committee);
			committeeVo.setCommittee(committee);
			committeeVo.setStatus(true);
			committeeVo.setMessage("Committee member role deleted successfully");
		} catch (Exception e) {
			committeeVo.setStatus(false);
			committeeVo.setMessage("Problem occurred in deleting member role");
			e.printStackTrace();
		}
		return committeeVo;
	}

	@Override
	public CommitteeVo updateMemberRoles(CommitteeVo committeeVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
			List<CommitteeMemberships> memberships = committee.getCommitteeMemberships();
			for (CommitteeMemberships committeeMembership : memberships) {
				if (committeeMembership.getCommMembershipId().equals(committeeVo.getCommMembershipId())) {
					List<CommitteeMemberRoles> memberRoles = committeeMembership.getCommitteeMemberRoles();
					for (CommitteeMemberRoles role : memberRoles) {
						if (role.getCommMemberRolesId().equals(committeeVo.getCommMemberRolesId())) {
							role.setStartDate(committeeVo.getCommitteeMemberRole().getStartDate());
							role.setEndDate(committeeVo.getCommitteeMemberRole().getEndDate());
						}
					}
				}
				if (committeeMembership.getNonEmployeeFlag()) {
					Rolodex rolodex = committeeDao.getRolodexById(committeeMembership.getRolodexId());
					committeeMembership.setRolodex(rolodex);
				} else {
					PersonDetailsView personDetails = committeeDao.getPersonDetailsById(committeeMembership.getPersonId());
					committeeMembership.setPersonDetails(personDetails);
				}
			}
			committeeDao.saveCommittee(committee);
			committeeVo.setCommittee(committee);
			committeeVo.setStatus(true);
			committeeVo.setMessage("Committee member role updated successfully");
		} catch (Exception e) {
			committeeVo.setStatus(false);
			committeeVo.setMessage("Problem occurred in updating member role");
			e.printStackTrace();
		}
		return committeeVo;
	}

	@Override
	public CommitteeVo deleteExpertise(CommitteeVo committeeVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
			List<CommitteeMemberships> memberships = committee.getCommitteeMemberships();
			for (CommitteeMemberships committeeMembership : memberships) {
				if (committeeMembership.getCommMembershipId().equals(committeeVo.getCommMembershipId())) {
					List<CommitteeMemberExpertise> list = committeeMembership.getCommitteeMemberExpertises();
					List<CommitteeMemberExpertise> updatedlist = new ArrayList<CommitteeMemberExpertise>(list);
					Collections.copy(updatedlist, list);
					for (CommitteeMemberExpertise expertise : list) {
						if (expertise.getCommMemberExpertiseId().equals(committeeVo.getCommMemberExpertiseId())) {
							updatedlist.remove(expertise);
						}
					}
					committeeMembership.getCommitteeMemberExpertises().clear();
					committeeMembership.getCommitteeMemberExpertises().addAll(updatedlist);
				}
				if (committeeMembership.getNonEmployeeFlag()) {
					Rolodex rolodex = committeeDao.getRolodexById(committeeMembership.getRolodexId());
					committeeMembership.setRolodex(rolodex);
				} else {
					PersonDetailsView personDetails = committeeDao
							.getPersonDetailsById(committeeMembership.getPersonId());
					committeeMembership.setPersonDetails(personDetails);
				}
			}
			committeeDao.saveCommittee(committee);
			committeeVo.setCommittee(committee);
			committeeVo.setStatus(true);
			committeeVo.setMessage("Committee member expertise deleted successfully");
		} catch (Exception e) {
			committeeVo.setStatus(false);
			committeeVo.setMessage("Problem occurred in deleting member expertise");
			e.printStackTrace();
		}
		return committeeVo;
	}

	@Override
	public CommitteeVo saveCommitteeMembersExpertise(CommitteeVo committeeVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
			CommitteeMemberExpertise expertise = committeeVo.getCommitteeMemberExpertise();
			List<CommitteeMemberships> memberships = committee.getCommitteeMemberships();
			for (CommitteeMemberships committeeMembership : memberships) {
				if (committeeMembership.getCommMembershipId().equals(committeeVo.getCommMembershipId())) {
					CommitteeMemberExpertise memberExpertise = new CommitteeMemberExpertise();
					memberExpertise.setCommitteeMemberships(committeeMembership);
					memberExpertise.setResearchAreaCode(expertise.getResearchAreaCode());
					memberExpertise.setResearchAreaDescription(expertise.getResearchAreaDescription());
					memberExpertise.setUpdateTimestamp(expertise.getUpdateTimestamp());
					memberExpertise.setUpdateUser(expertise.getUpdateUser());
					memberExpertise = committeeDao.saveCommitteeMemberExpertise(memberExpertise);
					committeeMembership.getCommitteeMemberExpertises().add(memberExpertise);
				}
				if (committeeMembership.getNonEmployeeFlag()) {
					Rolodex rolodex = committeeDao.getRolodexById(committeeMembership.getRolodexId());
					committeeMembership.setRolodex(rolodex);
				} else {
					PersonDetailsView personDetails = committeeDao.getPersonDetailsById(committeeMembership.getPersonId());
					committeeMembership.setPersonDetails(personDetails);
				}
			}
			committeeDao.saveCommittee(committee);
			committeeVo.setCommittee(committee);
			committeeVo.setStatus(true);
			committeeVo.setMessage("Committee member expertise saved successfully");
		} catch (Exception e) {
			committeeVo.setStatus(false);
			committeeVo.setMessage("Problem occurred in saving member expertise");
			e.printStackTrace();
		}
		return committeeVo;
	}

}
