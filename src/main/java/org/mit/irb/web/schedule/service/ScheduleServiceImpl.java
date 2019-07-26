package org.mit.irb.web.schedule.service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.service.IRBUtilService;
import org.mit.irb.web.committee.constants.Constants;
import org.mit.irb.web.committee.dao.CommitteeDao;
import org.mit.irb.web.committee.pojo.Committee;
import org.mit.irb.web.committee.pojo.CommitteeMemberRoles;
import org.mit.irb.web.committee.pojo.CommitteeMemberships;
import org.mit.irb.web.committee.pojo.CommitteeSchedule;
import org.mit.irb.web.committee.pojo.CommitteeScheduleActItems;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttachType;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttachment;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttendance;
import org.mit.irb.web.committee.pojo.CommitteeScheduleMinuteDoc;
import org.mit.irb.web.committee.pojo.CommitteeScheduleMinutes;
import org.mit.irb.web.committee.pojo.MinuteEntryType;
import org.mit.irb.web.committee.pojo.ProtocolContingency;
import org.mit.irb.web.committee.pojo.ProtocolSubmission;
import org.mit.irb.web.committee.pojo.Rolodex;
import org.mit.irb.web.committee.pojo.ScheduleActItemType;
import org.mit.irb.web.committee.pojo.ScheduleAgenda;
import org.mit.irb.web.committee.pojo.ScheduleStatus;
import org.mit.irb.web.committee.schedule.Time12HrFmt;
import org.mit.irb.web.committee.view.PersonDetailsView;
import org.mit.irb.web.schedule.dao.ScheduleDao;
import org.mit.irb.web.schedule.vo.ScheduleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service(value = "scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	protected static Logger logger = Logger.getLogger(ScheduleServiceImpl.class.getName());

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	IRBUtilService irbUtilService;
	
	@Autowired
	private ScheduleDao scheduleDao;
	
	@Autowired
	private MinutesAgendaService minutesAgendaService;

	@Override
	public ScheduleVo loadScheduleById(ScheduleVo scheduleVo) {
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleVo.getScheduleId());
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
		scheduleVo.setCommittee(committee);
		List<ScheduleStatus> scheduleStatus = committeeDao.fetchAllScheduleStatus();
		scheduleVo.setScheduleStatus(scheduleStatus);
		ScheduleAgenda agendaDetails = minutesAgendaService.getPrevAgendaDetails(scheduleVo.getScheduleId());
		scheduleVo.setAgendaDetails(agendaDetails);
		CommitteeScheduleMinuteDoc minuteDetails = minutesAgendaService.getPrevMinuteDetails(scheduleVo.getScheduleId());
		scheduleVo.setMinuteDetails(minuteDetails);		
		return scheduleVo;
	}

	/*
	 * Init attendance if this meeting schedule is maintained for the first time.
	 */
	protected void initAttendance(List<CommitteeScheduleAttendance> committeeScheduleAttendances, CommitteeSchedule commSchedule) {
		List<CommitteeMemberships> committeeMemberships = commSchedule.getCommittee().getCommitteeMemberships();
		committeeMemberships.forEach(committeeMembership -> {
			if (isActiveMembership(committeeMembership, commSchedule.getScheduledDate())) {
				CommitteeScheduleAttendance committeeScheduleAttendance = new CommitteeScheduleAttendance();
				if (StringUtils.isBlank(committeeMembership.getPersonId())) {
					committeeScheduleAttendance.setPersonId(committeeMembership.getRolodexId().toString());
					committeeScheduleAttendance.setNonEmployeeFlag(true);
				} else {
					committeeScheduleAttendance.setPersonId(committeeMembership.getPersonId());
					committeeScheduleAttendance.setNonEmployeeFlag(false);
				}
				committeeScheduleAttendance.setPersonName(committeeMembership.getPersonName());
				if (isAlternate(committeeMembership, commSchedule.getScheduledDate())) {
					committeeScheduleAttendance.setAlternateFlag(true);
				} else {
					committeeScheduleAttendance.setAlternateFlag(false);
				}
				// MemberAbsentBean memberAbsentBean = new MemberAbsentBean();
				committeeScheduleAttendance.setRoleName(getRoleNameForMembership(committeeMembership, commSchedule.getScheduledDate()));
				// memberAbsentBean.setAttendance(committeeScheduleAttendance);
				//committeeScheduleAttendance.setMemberPresent(false);
				committeeScheduleAttendance.setCommitteeSchedule(commSchedule);
				committeeScheduleAttendance = scheduleDao.addCommitteeScheduleAttendance(committeeScheduleAttendance);
				committeeScheduleAttendances.add(committeeScheduleAttendance);
			}
		});
	}

	protected void loadAttendance(List<CommitteeScheduleAttendance> committeeScheduleAttendances, CommitteeSchedule commSchedule) {
		List<CommitteeMemberships> committeeMemberships = commSchedule.getCommittee().getCommitteeMemberships();
		List<CommitteeMemberships> updatedlist = new ArrayList<CommitteeMemberships>(committeeMemberships);
		Collections.copy(updatedlist, committeeMemberships);
		List<CommitteeScheduleAttendance> commScheduleAttendances = commSchedule.getCommitteeScheduleAttendances();
		committeeMemberships.forEach(committeeMembership -> {
			if (isActiveMembership(committeeMembership, commSchedule.getScheduledDate())) {
				for (CommitteeScheduleAttendance attendance : commScheduleAttendances) {
					if ((attendance.getNonEmployeeFlag() && committeeMembership.getRolodexId() != null && 
							attendance.getPersonId().equals(committeeMembership.getRolodexId().toString()))
							|| (!attendance.getNonEmployeeFlag() && attendance.getPersonId().equals(committeeMembership.getPersonId()))) {
						//committeeScheduleAttendances.add(attendance);
						updatedlist.remove(committeeMembership);
					}
				}
			} else {
				updatedlist.remove(committeeMembership);
			}
		});
		for (CommitteeMemberships updated : updatedlist) {
			CommitteeScheduleAttendance committeeScheduleAttendance = new CommitteeScheduleAttendance();
			if (StringUtils.isBlank(updated.getPersonId())) {
				committeeScheduleAttendance.setPersonId(updated.getRolodexId().toString());
				committeeScheduleAttendance.setNonEmployeeFlag(true);
			} else {
				committeeScheduleAttendance.setPersonId(updated.getPersonId());
				committeeScheduleAttendance.setNonEmployeeFlag(false);
			}
			committeeScheduleAttendance.setPersonName(updated.getPersonName());
			if (isAlternate(updated, commSchedule.getScheduledDate())) {
				committeeScheduleAttendance.setAlternateFlag(true);
			} else {
				committeeScheduleAttendance.setAlternateFlag(false);
			}
			committeeScheduleAttendance.setRoleName(getRoleNameForMembership(updated, commSchedule.getScheduledDate()));
		//	committeeScheduleAttendance.setMemberPresent(false);
			committeeScheduleAttendance.setCommitteeSchedule(commSchedule);
			committeeScheduleAttendance = scheduleDao.addCommitteeScheduleAttendance(committeeScheduleAttendance);
			committeeScheduleAttendances.add(committeeScheduleAttendance);
		}
	}

	protected boolean isActiveMembership(CommitteeMemberships committeeMembership, Date scheduledDate) {
		return isActiveForScheduledDate(scheduledDate, committeeMembership.getTermStartDate(), committeeMembership.getTermEndDate())
				&& hasActiveMembershipRoleForScheduledDate((List<CommitteeMemberRoles>) committeeMembership.getCommitteeMemberRoles(), scheduledDate);
	}

	private boolean isActiveForScheduledDate(java.util.Date scheduledDate, java.util.Date startDate, java.util.Date endDate) {
		return startDate.before(scheduledDate) && endDate.after(scheduledDate);
	}

	private boolean hasActiveMembershipRoleForScheduledDate(List<CommitteeMemberRoles> committeeMembershipRoles,
			Date scheduledDate) {
		for (CommitteeMemberRoles membershipRole : committeeMembershipRoles) {
			if (!membershipRole.getMembershipRoleCode().equals(Constants.INACTIVE_ROLE)
					&& isActiveForScheduledDate(scheduledDate, membershipRole.getStartDate(), membershipRole.getEndDate())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * get rolename, concatenated with ',' separator if multiple roles exist for
	 * this membership
	 */
	protected String getRoleNameForMembership(CommitteeMemberships committeeMembership, Date scheduledDate) {
		String roleName = "";
		for (CommitteeMemberRoles membershipRole : committeeMembership.getCommitteeMemberRoles()) {
			if (isActiveForScheduledDate(scheduledDate, membershipRole.getStartDate(), membershipRole.getEndDate())) {
				roleName = roleName + "," + membershipRole.getMembershipRoleDescription();
			}
		}
		if (StringUtils.isNotBlank(roleName)) {
			roleName = roleName.substring(1); // remove ","
		}
		return roleName;
	}

	/*
	 * check if this membership has alternate role based on schedule date.
	 */
	protected boolean isAlternate(CommitteeMemberships committeeMembership, Date scheduledDate) {
		boolean isAlternate = false;
		for (CommitteeMemberRoles membershipRole : committeeMembership.getCommitteeMemberRoles()) {
			if (membershipRole.getMembershipRoleCode().equals(Constants.ALTERNATE_ROLE)
					&& !membershipRole.getStartDate().after(scheduledDate)
					&& !membershipRole.getEndDate().before(scheduledDate)) {
				isAlternate = true;
				break;
			}
		}
		return isAlternate;
	}

	/*
	 * populate 3 attendance form beans
	 */
	protected void populateAttendanceToForm(ScheduleVo scheduleVo, List<CommitteeMemberships> committeeMemberships, CommitteeSchedule commSchedule) {
		//populatePresentBean(scheduleVo, committeeMemberships, commSchedule);
		populateMemberAbsentBean(scheduleVo, committeeMemberships, commSchedule);
	}

	/*
	 * populate memberpresentbean &amp; otherpresentbean
	 */
	protected void populatePresentBean(ScheduleVo scheduleVo, List<CommitteeMemberships> committeeMemberships,
			CommitteeSchedule commSchedule) {
		//scheduleVo.setOtherPresents(new ArrayList<CommitteeScheduleAttendance>());
		// commSchedule.setMemberPresents(new ArrayList<>());
		for (CommitteeScheduleAttendance committeeScheduleAttendance : commSchedule.getCommitteeScheduleAttendances()) {
			getRoleName(committeeScheduleAttendance, committeeMemberships, commSchedule.getScheduledDate());
			if (committeeScheduleAttendance.getGuestFlag()) {
				// OtherPresentBeanBase otherPresentBean =
				// getNewOtherPresentBeanInstanceHook();
				// otherPresentBean.setAttendance(committeeScheduleAttendance);
				committeeScheduleAttendance.setGuestMemberActive(isActiveMember(committeeScheduleAttendance, committeeMemberships, commSchedule.getScheduledDate()));
				if (StringUtils.isBlank(committeeScheduleAttendance.getRoleName())) {
					committeeScheduleAttendance.setRoleName("Guest");
				}
				//scheduleVo.getOtherPresents().add(committeeScheduleAttendance);
				// otherPresentBean.setAttendance(committeeScheduleAttendance);
			}
			/*
			 * else { MemberPresentBean memberPresentBean = new
			 * MemberPresentBean();
			 * memberPresentBean.setAttendance(committeeScheduleAttendance);
			 * meetingHelper.getMemberPresentBeans().add(memberPresentBean); }
			 */
		}
	}

	/*
	 * get a person's role name within this committee memberships based on
	 * schedule date.
	 */
	protected void getRoleName(CommitteeScheduleAttendance committeeScheduleAttendance, List<CommitteeMemberships> committeeMemberships, Date scheduleDate) {
		String roleName = "";
		for (CommitteeMemberships committeeMembership : committeeMemberships) {
			if ((committeeScheduleAttendance.getNonEmployeeFlag() && committeeMembership.getRolodexId() != null
					&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getRolodexId().toString()))
					|| (!committeeScheduleAttendance.getNonEmployeeFlag()
							&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getPersonId()))) {
				roleName = getRoleNameForMembership(committeeMembership, scheduleDate);
				break;
			}
		}
		committeeScheduleAttendance.setRoleName(roleName);
	}

	/*
	 * Check if this member is active in this committee. Inactive scenario : -
	 * not defined in membership. - in membership, but non of the memberships
	 * period cover schedule date - an 'Inactive' role period cover schedule
	 * date.
	 */
	protected boolean isActiveMember(CommitteeScheduleAttendance committeeScheduleAttendance, List<CommitteeMemberships> committeeMemberships, Date scheduleDate) {
		boolean isActiveMember = false;
		for (CommitteeMemberships committeeMembership : committeeMemberships) {
			if ((committeeScheduleAttendance.getNonEmployeeFlag() && committeeMembership.getRolodexId() != null
					&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getRolodexId().toString()))
					|| (!committeeScheduleAttendance.getNonEmployeeFlag()
							&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getPersonId()))) {
				if (isActiveForScheduledDate(scheduleDate, committeeMembership.getTermStartDate(), committeeMembership.getTermEndDate())) {
					isActiveMember = isActiveMembership(committeeMembership, scheduleDate);
				}
			}
		}
		return isActiveMember;
	}

	/*
	 * populate memberabsentbean
	 */
	protected void populateMemberAbsentBean(ScheduleVo scheduleVo, List<CommitteeMemberships> committeeMemberships, CommitteeSchedule commSchedule) {
		//scheduleVo.setMemberAbsents(new ArrayList<CommitteeScheduleAttendance>());
		committeeMemberships.forEach(committeeMembership -> {
			if (!isInMemberPresent(commSchedule.getCommitteeScheduleAttendances(), committeeMembership)
					&& !isInOtherPresent(commSchedule.getCommitteeScheduleAttendances(), committeeMembership)) {
				// MemberAbsentBean memberAbsentBean = new MemberAbsentBean();
				CommitteeScheduleAttendance attendance = new CommitteeScheduleAttendance();
				attendance.setRoleName(getRoleNameForMembership(committeeMembership, commSchedule.getScheduledDate()));
				if (StringUtils.isBlank(committeeMembership.getPersonId())) {
					attendance.setPersonId(committeeMembership.getRolodexId().toString());
				} else {
					attendance.setPersonId(committeeMembership.getPersonId());
				}
				if (isActiveMemberAbsent(attendance, committeeMembership, commSchedule.getScheduledDate())) {
					attendance.setPersonName(committeeMembership.getPersonName());
					attendance.setAlternateFlag(false);
					attendance.setNonEmployeeFlag(StringUtils.isBlank(committeeMembership.getPersonId()));
					// memberAbsentBean.setAttendance(attendance);
					//scheduleVo.getMemberAbsents().add(attendance);
				}
			}
		});
	}

	/*
	 * check if person is in member present.
	 */
	protected boolean isInMemberPresent(List<CommitteeScheduleAttendance> memberPresents,
			CommitteeMemberships committeeMembership) {
		boolean isPresent = false;
		for (CommitteeScheduleAttendance memberPresentBean : memberPresents) {
			if (memberPresentBean.getNonEmployeeFlag() && StringUtils.isBlank(committeeMembership.getPersonId())
					&& memberPresentBean.getPersonId().equals(committeeMembership.getRolodexId().toString())) {
				isPresent = true;
				break;
			} else if (!memberPresentBean.getNonEmployeeFlag()
					&& StringUtils.isNotBlank(committeeMembership.getPersonId())
					&& memberPresentBean.getPersonId().equals(committeeMembership.getPersonId())) {
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}

	/*
	 * check if person is in other present
	 */
	protected boolean isInOtherPresent(List<CommitteeScheduleAttendance> otherPresentBeans,
			CommitteeMemberships committeeMembership) {
		boolean isPresent = false;
		for (CommitteeScheduleAttendance otherPresentBean : otherPresentBeans) {
			if (otherPresentBean.getNonEmployeeFlag() && StringUtils.isBlank(committeeMembership.getPersonId())
					&& otherPresentBean.getPersonId().equals(committeeMembership.getRolodexId().toString())) {
				isPresent = true;
				break;
			} else if (!otherPresentBean.getNonEmployeeFlag()
					&& StringUtils.isNotBlank(committeeMembership.getPersonId())
					&& otherPresentBean.getPersonId().equals(committeeMembership.getPersonId())) {
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}

	protected boolean isActiveMemberAbsent(CommitteeScheduleAttendance committeeScheduleAttendance,
			CommitteeMemberships committeeMembership, Date scheduleDate) {
		boolean isActiveMember = false;
		if ((committeeScheduleAttendance.getNonEmployeeFlag() && committeeMembership.getRolodexId() != null
				&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getRolodexId().toString()))
				|| (!committeeScheduleAttendance.getNonEmployeeFlag()
						&& committeeScheduleAttendance.getPersonId() != null
						&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getPersonId()))) {
			if (!committeeMembership.getTermStartDate().after(scheduleDate)
					&& !committeeMembership.getTermEndDate().before(scheduleDate)) {
				isActiveMember = isActiveMembership(committeeMembership, scheduleDate);
			}
		}
		return isActiveMember;
	}

	@Override
	public ScheduleVo updateSchedule(ScheduleVo scheduleVo) {
		Committee committee =scheduleVo.getCommittee(); /*committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());*/
		CommitteeSchedule committeeSchedule = scheduleVo.getCommitteeSchedule();
		committeeSchedule.setMeetingDate(irbUtilService.adjustTimezone(committeeSchedule.getMeetingDate()));
		committeeSchedule.setProtocolSubDeadline(irbUtilService.adjustTimezone(committeeSchedule.getProtocolSubDeadline()));
		committeeSchedule.setStartTime(addHrMinToDate(committeeSchedule.getViewStartTime()));
        committeeSchedule.setEndTime(addHrMinToDate(committeeSchedule.getViewEndTime()));
        committeeSchedule.setTime(addHrMinToDate(committeeSchedule.getViewTime()));
        committeeSchedule.setCommittee(committee);
		committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		return scheduleVo;
	}

	/*
     * utility methods by adding minutes to date
     */
	protected Timestamp addHrMinToDate(Time12HrFmt viewTime) {
		java.util.Date dt = new java.util.Date(0); // this is actually 12-31-1969 19:00. its GMT time
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");
		try {
			// support localization.
			dt = dateFormat.parse("01/01/1970 " + viewTime.getTime() + " " + viewTime.getMeridiem());
			return new Timestamp(dt.getTime());
		} catch (Exception e) {
			// folowing may convert date to 07-02-1970 iftz is gmt+12 or more
			dt = DateUtils.round(dt, Calendar.DAY_OF_MONTH);
			return new Timestamp(DateUtils.addMinutes(dt, viewTime.findMinutes()).getTime());
		}
	}

	@Override
	public ScheduleVo addOtherActions(ScheduleVo scheduleVo) {
		List<ScheduleActItemType> scheduleActItemTypes = scheduleDao.fetchAllScheduleActItemType();
		scheduleVo.setScheduleActItemTypes(scheduleActItemTypes);//for other actions
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleVo.getScheduleId());
		CommitteeScheduleActItems committeeScheduleActItems = scheduleVo.getCommitteeScheduleActItems();
		CommitteeScheduleActItems scheduleActItem = new CommitteeScheduleActItems();
		scheduleActItem.setCommitteeSchedule(committeeSchedule);
		scheduleActItem.setScheduleActItemTypecode(committeeScheduleActItems.getScheduleActItemTypecode());
		scheduleActItem.setItemDescription(committeeScheduleActItems.getItemDescription());
		scheduleActItem.setScheduleActItemTypeDescription(committeeScheduleActItems.getScheduleActItemTypeDescription());
		scheduleActItem.setActionItemNumber(getNextActionItemNumber(committeeSchedule));
		scheduleActItem.setUpdateTimestamp(committeeScheduleActItems.getUpdateTimestamp());
		scheduleActItem.setUpdateUser(committeeScheduleActItems.getUpdateUser());
		scheduleActItem = scheduleDao.addOtherActions(scheduleActItem);
		committeeSchedule.getCommitteeScheduleActItems().add(scheduleActItem);
		Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
		committeeSchedule.setCommittee(committee);
		committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		return scheduleVo;
	}

	public Integer getNextActionItemNumber(CommitteeSchedule committeeSchedule) {
		Integer nextActionItemNumber = committeeSchedule.getCommitteeScheduleActItems().size();
		for (CommitteeScheduleActItems commScheduleActItem : committeeSchedule.getCommitteeScheduleActItems()) {
			if (commScheduleActItem.getActionItemNumber() > nextActionItemNumber) {
				nextActionItemNumber = commScheduleActItem.getActionItemNumber();
			}
		}
		return nextActionItemNumber + 1;
	}

	@Override
	public ScheduleVo deleteOtherActions(ScheduleVo scheduleVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
			List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
			for (CommitteeSchedule committeeSchedule : committeeSchedules) {
				if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
					List<CommitteeScheduleActItems> list = committeeSchedule.getCommitteeScheduleActItems();
					List<CommitteeScheduleActItems> updatedlist = new ArrayList<CommitteeScheduleActItems>(list);
					Collections.copy(updatedlist, list);
					for (CommitteeScheduleActItems actionItem : list) {
						if (actionItem.getCommScheduleActItemsId().equals(scheduleVo.getCommScheduleActItemsId())) {
							updatedlist.remove(actionItem);
						}
					}
					committeeSchedule.getCommitteeScheduleActItems().clear();
					committeeSchedule.getCommitteeScheduleActItems().addAll(updatedlist);
					scheduleVo.setCommitteeSchedule(committeeSchedule);
				}
			}
			committee = committeeDao.saveCommittee(committee);
			scheduleVo.setCommittee(committee);
			scheduleVo.setStatus(true);
			scheduleVo.setMessage("Schedule other action item deleted successfully");
		} catch (Exception e) {
			scheduleVo.setStatus(false);
			scheduleVo.setMessage("Problem occurred in deleting Schedule other action item");
			e.printStackTrace();
		}
		return scheduleVo;
	}

	@Override
	public ScheduleVo addCommitteeScheduleMinute(ScheduleVo scheduleVo) {
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleVo.getScheduleId());
		CommitteeScheduleMinutes committeeScheduleMinute = scheduleVo.getNewCommitteeScheduleMinute();
		Integer submissionNumber = null;	
/*		Integer entryNumber = getNextMinuteEntryNumber(committeeSchedule);
*/		String minuteEntryTypeCode = committeeScheduleMinute.getMinuteEntryTypeCode().toString();
		scheduleVo.setSubmissionId(committeeScheduleMinute.getSubmissionId());
		committeeScheduleMinute.setSubmissionId(committeeScheduleMinute.getSubmissionId());
		committeeScheduleMinute.setSubmissionNumber(submissionNumber);
		committeeScheduleMinute.setProtocolNumber(committeeScheduleMinute.getProtocolNumber());
	//	committeeScheduleMinute.setEntryNumber(entryNumber);
		committeeScheduleMinute.setCommitteeSchedule(committeeSchedule);
		if (Constants.ATTENDANCE.equals(minuteEntryTypeCode)) {
			addAttendanceMinuteEntry(committeeSchedule, committeeScheduleMinute);
		} else if (Constants.ACTION_ITEM.equals(minuteEntryTypeCode)) {
			List<CommitteeScheduleActItems> list = scheduleDao.getCommitteeScheduleActItemsById(scheduleVo.getScheduleId());
			committeeSchedule.setCommitteeScheduleActItems(list);
			addActionItem(committeeSchedule, committeeScheduleMinute);
		} else if (Constants.PROTOCOL.equals(minuteEntryTypeCode) || Constants.PROTOCOL_REVIEWER_COMMENT.equals(minuteEntryTypeCode)) {
			resetActionItemFields(committeeScheduleMinute);
		} else {
			resetProtocolFields(committeeScheduleMinute);
			resetActionItemFields(committeeScheduleMinute);
		}
		committeeScheduleMinute = scheduleDao.addCommitteeScheduleMinute(committeeScheduleMinute);
		scheduleVo.setNewCommitteeScheduleMinute(committeeScheduleMinute);
		if(minuteEntryTypeCode.equals("3")){
			List<CommitteeScheduleMinutes> scheduleMinutes = scheduleDao.getProtocolCommitteeComments(scheduleVo.getSubmissionId(),scheduleVo.getScheduleId());
			scheduleVo.setScheduleMinutes(scheduleMinutes);			
		}else{
			List<CommitteeScheduleMinutes> scheduleMinutes = scheduleDao.getScheduleMinutes(scheduleVo);
			scheduleVo.setScheduleMinutes(scheduleMinutes);	
		}			
		return scheduleVo;
	}

	protected Integer getNextMinuteEntryNumber(CommitteeSchedule committeeSchedule) {
		Integer nextMinuteEntryNumber = committeeSchedule.getCommitteeScheduleMinutes().size();
		for (CommitteeScheduleMinutes committeeScheduleMinute : committeeSchedule.getCommitteeScheduleMinutes()) {
			if (committeeScheduleMinute.getEntryNumber() > nextMinuteEntryNumber) {
				nextMinuteEntryNumber = committeeScheduleMinute.getEntryNumber();
			}
		}
		return nextMinuteEntryNumber + 1;
	}

	protected void addAttendanceMinuteEntry(CommitteeSchedule committeeSchedule,
			CommitteeScheduleMinutes committeeScheduleMinutes) {
		if (committeeScheduleMinutes.isGenerateAttendance()) {
			committeeScheduleMinutes.setMinuteEntry(generateAttendanceComment(committeeSchedule.getCommitteeScheduleAttendances(), committeeSchedule));
		}
		resetProtocolFields(committeeScheduleMinutes);
		resetActionItemFields(committeeScheduleMinutes);
	}

	protected String generateAttendanceComment(List<CommitteeScheduleAttendance> memberPresentList, CommitteeSchedule committeeSchedule) {
		String comment = "";
		String eol = System.getProperty("line.separator");
		for (CommitteeScheduleAttendance memberPresent : memberPresentList) {
			if (StringUtils.isNotBlank(comment)) {
				comment = comment + eol;
			}
			comment = comment + memberPresent.getPersonName();
			if (StringUtils.isNotBlank(memberPresent.getAlternateFor())) {
				comment = comment + " Alternate For: "
						+ getAlternateForName(committeeSchedule, memberPresent.getAlternateFor());
			}
			if (memberPresent.getGuestFlag()) {
				comment = comment + memberPresent.getPersonName() + " Guest ";
			}
		}
		return comment;
	}

	protected String getAlternateForName(CommitteeSchedule committeeSchedule, String alternateFor) {
		String personName = "";
		for (CommitteeMemberships committeeMembership : committeeSchedule.getCommittee().getCommitteeMemberships()) {
			if ((StringUtils.isNotBlank(committeeMembership.getPersonId())
					&& committeeMembership.getPersonId().equals(alternateFor))
					|| (StringUtils.isBlank(committeeMembership.getPersonId())
							&& committeeMembership.getRolodexId().toString().equals(alternateFor))) {
				personName = committeeMembership.getPersonName();
				break;
			}
		}
		return personName;
	}

	protected void resetProtocolFields(CommitteeScheduleMinutes committeeScheduleMinutes) {
		committeeScheduleMinutes.setProtocolId(null);
		committeeScheduleMinutes.setProtocolNumber(null);
		// committeeScheduleMinutes.setProtocol(null);
	}

	protected void resetActionItemFields(CommitteeScheduleMinutes committeeScheduleMinutes) {
		committeeScheduleMinutes.setCommScheduleActItemsId(null);
		committeeScheduleMinutes.setScheduleActItems(null);
	}

	protected void addActionItem(CommitteeSchedule committeeSchedule, CommitteeScheduleMinutes committeeScheduleMinutes) {
		if (committeeScheduleMinutes.getCommScheduleActItemsId() != null) {
			// in case adding non-persisted action item
			committeeScheduleMinutes.setScheduleActItems(getActionItem(committeeScheduleMinutes.getCommScheduleActItemsId(), committeeSchedule.getCommitteeScheduleActItems()));
		}
		resetProtocolFields(committeeScheduleMinutes);
	}

	protected CommitteeScheduleActItems getActionItem(Integer commScheduleActItemsId, List<CommitteeScheduleActItems> commScheduleActItems) {
		CommitteeScheduleActItems actionItem = null;

		for (CommitteeScheduleActItems commScheduleActItem : commScheduleActItems) {
			if (commScheduleActItem.getCommScheduleActItemsId().equals(commScheduleActItemsId)) {
				actionItem = commScheduleActItem;
				break;
			}
		}
		return actionItem;
	}

	@Override
	public ScheduleVo updateScheduleAttendance(ScheduleVo scheduleVo) {
		Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
		List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
		//CommitteeScheduleAttendance scheduleAttendance = scheduleVo.getUpdatedAttendance();
		for (CommitteeSchedule committeeSchedule : committeeSchedules) {
			if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
				@SuppressWarnings("unused")
				List<CommitteeScheduleAttendance> attendances = committeeSchedule.getCommitteeScheduleAttendances();			
				scheduleVo.setCommitteeSchedule(committeeSchedule);
			}
		}
		committeeDao.saveCommittee(committee);
		scheduleVo.setCommittee(committee);
		return scheduleVo;
	}

	@Override
	public ScheduleVo addOthersPresent(ScheduleVo scheduleVo) {
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleVo.getScheduleId());
		//CommitteeScheduleAttendance scheduleAttendance = scheduleVo.getUpdatedAttendance();
		//scheduleAttendance.setCommitteeSchedule(committeeSchedule);
		//scheduleAttendance = scheduleDao.addCommitteeScheduleAttendance(scheduleAttendance);
		//committeeSchedule.getCommitteeScheduleAttendances().add(scheduleAttendance);
		committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		return scheduleVo;
	}

	@Override
	public ScheduleVo deleteScheduleMinute(ScheduleVo scheduleVo) {
		try {
			scheduleDao.deleteScheduleMinute(scheduleVo.getCommScheduleMinuteId());
			List<CommitteeScheduleMinutes> scheduleMinutes = null;
			if(scheduleVo.getProtocolNumber() != null){
				 scheduleMinutes =	scheduleDao.getProtocolCommitteeComments(scheduleVo.getSubmissionId(),scheduleVo.getScheduleId());	
			}else{
				 scheduleMinutes = scheduleDao.getScheduleMinutes(scheduleVo);
			}
			scheduleVo.setScheduleMinutes(scheduleMinutes);
			scheduleVo.setStatus(true);
			scheduleVo.setMessage("Schedule minute deleted successfully");
		} catch (Exception e) {
			scheduleVo.setStatus(false);
			scheduleVo.setMessage("Problem occurred in deleting Schedule minute");
			e.printStackTrace();
		}
		return scheduleVo;
	}

	@Override
	public ScheduleVo deleteScheduleAttachment(ScheduleVo scheduleVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
			List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
			for (CommitteeSchedule committeeSchedule : committeeSchedules) {
				if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
					List<CommitteeScheduleAttachment> list = committeeSchedule.getCommitteeScheduleAttachments();
					List<CommitteeScheduleAttachment> updatedlist = new ArrayList<CommitteeScheduleAttachment>(list);
					Collections.copy(updatedlist, list);
					for (CommitteeScheduleAttachment attachment : list) {
						if (attachment.getCommScheduleAttachId().equals(scheduleVo.getCommScheduleAttachId())) {
							updatedlist.remove(attachment);
						}
					}
					committeeSchedule.getCommitteeScheduleAttachments().clear();
					committeeSchedule.getCommitteeScheduleAttachments().addAll(updatedlist);
					scheduleVo.setCommitteeSchedule(committeeSchedule);
				}
			}
			committee = committeeDao.saveCommittee(committee);
			scheduleVo.setCommittee(committee);
			scheduleVo.setStatus(true);
			scheduleVo.setMessage("Schedule attachment deleted successfully");
		} catch (Exception e) {
			scheduleVo.setStatus(false);
			scheduleVo.setMessage("Problem occurred in deleting Schedule attachment");
			e.printStackTrace();
		}
		return scheduleVo;
	}

	@Override
	public ScheduleVo addScheduleAttachment(MultipartFile[] files, String formDataJSON) {
		ScheduleVo scheduleVo = new ScheduleVo();
		try {
			ObjectMapper mapper = new ObjectMapper();
			ScheduleVo jsonObj = mapper.readValue(formDataJSON, ScheduleVo.class);
			List<CommitteeScheduleAttachType> committeeScheduleAttachTypes = scheduleDao.fetchAllCommitteeScheduleAttachType();
			scheduleVo.setAttachmentTypes(committeeScheduleAttachTypes);//for attachments
			CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(jsonObj.getScheduleId());
			CommitteeScheduleAttachment newAttachment = jsonObj.getNewCommitteeScheduleAttachment();
			List<CommitteeScheduleAttachment> attachments = new ArrayList<CommitteeScheduleAttachment>();
			for (int i = 0; i < files.length; i++) {
				CommitteeScheduleAttachment committeeScheduleAttachment = new CommitteeScheduleAttachment();
				committeeScheduleAttachment.setAttachmentType(newAttachment.getAttachmentType());
				committeeScheduleAttachment.setCommitteeSchedule(committeeSchedule);
				committeeScheduleAttachment.setAttachmentTypeCode(newAttachment.getAttachmentTypeCode());
				committeeScheduleAttachment.setDescription(newAttachment.getDescription());
				committeeScheduleAttachment.setUpdateTimestamp(newAttachment.getUpdateTimestamp());
				committeeScheduleAttachment.setUpdateUser(newAttachment.getUpdateUser());
				committeeScheduleAttachment.setAttachment(files[i].getBytes());
				committeeScheduleAttachment.setFileName(files[i].getOriginalFilename());
				committeeScheduleAttachment.setMimeType(files[i].getContentType());
				committeeScheduleAttachment = scheduleDao.addScheduleAttachment(committeeScheduleAttachment);
				attachments.add(committeeScheduleAttachment);
			}
			committeeSchedule.getCommitteeScheduleAttachments().addAll(attachments);
			committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
			scheduleVo.setCommitteeSchedule(committeeSchedule);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scheduleVo;
	}

	@Override
	public ScheduleVo deleteScheduleAttendance(ScheduleVo scheduleVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
			List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
			for (CommitteeSchedule committeeSchedule : committeeSchedules) {
				if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
					List<CommitteeScheduleAttendance> list = committeeSchedule.getCommitteeScheduleAttendances();
					List<CommitteeScheduleAttendance> updatedlist = new ArrayList<CommitteeScheduleAttendance>(list);
					Collections.copy(updatedlist, list);
					for (CommitteeScheduleAttendance attachment : list) {
						if (attachment.getCommitteeScheduleAttendanceId().equals(scheduleVo.getCommScheduleAttendanceId())) {
							updatedlist.remove(attachment);
						}
					}
					committeeSchedule.getCommitteeScheduleAttendances().clear();
					committeeSchedule.getCommitteeScheduleAttendances().addAll(updatedlist);
					scheduleVo.setCommitteeSchedule(committeeSchedule);
				}
			}
			committee = committeeDao.saveCommittee(committee);
			scheduleVo.setCommittee(committee);
			scheduleVo.setStatus(true);
			scheduleVo.setMessage("Schedule attendance deleted successfully");
		} catch (Exception e) {
			scheduleVo.setStatus(false);
			scheduleVo.setMessage("Problem occurred in deleting Schedule attendance");
			e.printStackTrace();
		}
		return scheduleVo;
	}

	@Override
	public ScheduleVo updateScheduleAttachment(ScheduleVo scheduleVo) {
		Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
		List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
		CommitteeScheduleAttachment scheduleAttachment = scheduleVo.getNewCommitteeScheduleAttachment();
		for (CommitteeSchedule committeeSchedule : committeeSchedules) {
			if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
				List<CommitteeScheduleAttachment> attachments = committeeSchedule.getCommitteeScheduleAttachments();
				for (CommitteeScheduleAttachment attachment : attachments) {
					if (attachment.getCommScheduleAttachId().equals(scheduleAttachment.getCommScheduleAttachId())) {
						attachment.setDescription(scheduleAttachment.getDescription());
					}
				}
				scheduleVo.setCommitteeSchedule(committeeSchedule);
			}
		}
		committeeDao.saveCommittee(committee);
		scheduleVo.setCommittee(committee);
		return scheduleVo;
	}

	@Override
	public ResponseEntity<byte[]> downloadScheduleAttachment(Integer attachmentId) {
		CommitteeScheduleAttachment attachment = scheduleDao.fetchAttachmentById(attachmentId);
		ResponseEntity<byte[]> attachmentData = null;
		try {
			byte[] data = attachment.getAttachment();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(attachment.getMimeType()));
			String filename = attachment.getFileName();
			headers.setContentDispositionFormData(filename, filename);
			headers.setContentLength(data.length);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setPragma("public");
			attachmentData = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachmentData;
	}

	@Override
	public ScheduleVo updateCommitteeScheduleMinute(ScheduleVo vo) {
		
		CommitteeSchedule committeeSchedules = committeeDao.getCommitteeScheduleById(vo.getScheduleId());
		CommitteeScheduleMinutes scheduleMinutes = new CommitteeScheduleMinutes();
		scheduleMinutes = vo.getNewCommitteeScheduleMinute();
		scheduleMinutes.setCommitteeSchedule(committeeSchedules);
		scheduleMinutes= scheduleDao.updateScheduleMinutes(scheduleMinutes);
		vo.setNewCommitteeScheduleMinute(scheduleMinutes);
		return vo;
	}

	@Override
	public ScheduleVo loadScheduledProtocols(ScheduleVo vo) {
		ArrayList<HashMap<String, Object>>  protocolSubmission = scheduleDao.loadScheduledProtocols(vo.getScheduleId());//for protocol submission				
		List<ProtocolSubmission> submissions = new ArrayList<>();
		for (HashMap<String, Object> hashMap : protocolSubmission) {
			ProtocolSubmission singleSubmission = new ProtocolSubmission();
			singleSubmission.setCommitteeId(hashMap.get("COMMITTEE_ID") == null ? null : hashMap.get("COMMITTEE_ID").toString());
			singleSubmission.setProtocolNumber(hashMap.get("PROTOCOL_NUMBER") == null ? null : hashMap.get("PROTOCOL_NUMBER").toString());
			singleSubmission.setSubmissionDate(hashMap.get("SUBMISSION_DATE") == null ? null : generateSqlDate(hashMap.get("SUBMISSION_DATE").toString()));
			singleSubmission.setProtocolTitle(hashMap.get("TITLE") == null ? null : hashMap.get("TITLE").toString());
			singleSubmission.setPersonName(hashMap.get("PERSON_NAME") == null ? null : hashMap.get("PERSON_NAME").toString());
			singleSubmission.getSubmissionStatus().setDescription(hashMap.get("SUBMISSION_STATUS") == null ? null : hashMap.get("SUBMISSION_STATUS").toString());
			singleSubmission.getProtocolReviewType().setDescription(hashMap.get("PROTOCOL_REVIEW_TYPE") == null ? null : hashMap.get("PROTOCOL_REVIEW_TYPE").toString());
			singleSubmission.getQualifierType().setDescription(hashMap.get("SUBMISSION_QUAL_TYPE") == null ? null : hashMap.get("SUBMISSION_QUAL_TYPE").toString());			
			singleSubmission.setSubmissionTypeDescription(hashMap.get("SUBMISSION_TYPE") == null ? null : hashMap.get("SUBMISSION_TYPE").toString());			
			singleSubmission.setProtocolId(hashMap.get("PROTOCOL_ID") == null ? null :Integer.parseInt(hashMap.get("PROTOCOL_ID").toString()));
			singleSubmission.setAdminName(hashMap.get("ASSIGNEE_PERSON_NAME") == null ? null : hashMap.get("ASSIGNEE_PERSON_NAME").toString());			
			singleSubmission.setSubmissionId(hashMap.get("SUBMISSION_ID") == null ? null : Integer.parseInt(hashMap.get("SUBMISSION_ID").toString()));			
			singleSubmission.setSubmissionTypeCode(hashMap.get("SUBMISSION_TYPE_CODE") == null ? null : hashMap.get("SUBMISSION_TYPE_CODE").toString());			
			singleSubmission.setProtocolReviewTypeCode(hashMap.get("PROTOCOL_REVIEW_TYPE_CODE") == null ? null : hashMap.get("PROTOCOL_REVIEW_TYPE_CODE").toString());			
			singleSubmission.setExpirationDate(hashMap.get("EXPIRATION_DATE") == null ? null : hashMap.get("EXPIRATION_DATE").toString());			
			singleSubmission.setYesVoteCount(hashMap.get("YES_VOTE_COUNT") == null ? null : Integer.parseInt(hashMap.get("YES_VOTE_COUNT").toString()));			
			singleSubmission.setNoVoteCount(hashMap.get("NO_VOTE_COUNT") == null ? null : Integer.parseInt(hashMap.get("NO_VOTE_COUNT").toString()));			
			singleSubmission.setAbstainerCount(hashMap.get("ABSTAINER_COUNT") == null ? null : Integer.parseInt(hashMap.get("ABSTAINER_COUNT").toString()));			
			singleSubmission.getCommitteeSchedule().setScheduleId(hashMap.get("SCHEDULE_ID") == null ? null : Integer.parseInt(hashMap.get("SCHEDULE_ID").toString()));			
			submissions.add(singleSubmission);
		}	
		vo.setSubmittedProtocolsList(protocolSubmission);
		vo.setSubmittedProtocols(submissions);
		return vo;
	}
	
	public Date generateSqlDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date utilDate = null;
		java.sql.Date sqlDate = null;
		if (date != null) {
			try {
				utilDate = sdf.parse(date);
				sqlDate = new java.sql.Date(utilDate.getTime());
			} catch (Exception e) {
				logger.info("Exception in generateSqlActionDate:" + e);
			}
		}
		return sqlDate;
	}

	@Override
	public ScheduleVo loadScheduleMeetingComments(ScheduleVo vo) {	
		List<CommitteeScheduleMinutes> scheduleMinutes = scheduleDao.getScheduleMinutes(vo);
		vo.setScheduleMinutes(scheduleMinutes);		
		List<MinuteEntryType> minuteEntrytypes = scheduleDao.fetchAllMinuteEntryTypes();
		vo.setMinuteEntrytypes(minuteEntrytypes);	
		List<CommitteeScheduleActItems> list = scheduleDao.getCommitteeScheduleActItemsById(vo.getScheduleId());
		vo.setCommitteeScheduleActItemsList(list);
		return vo;
	}

	@Override
	public ScheduleVo loadScheduleProtocolComments(ScheduleVo vo) {
		if(vo.getProtocolNumber() != null){
			 List<CommitteeScheduleMinutes> protocolCommitteeCommmets =	scheduleDao.getProtocolCommitteeComments(vo.getSubmissionId(),vo.getScheduleId());
			 vo.setScheduleMinutes(protocolCommitteeCommmets);
		}
		List<ProtocolContingency> protocolContingencies = scheduleDao.fetchAllProtocolContingency();
		vo = loadScheduledProtocols(vo);
		vo.setProtocolContingencies(protocolContingencies);		
		return vo;
	}

	@Override
	public ScheduleVo createAgendaForSchedule(ScheduleVo vo) {
		vo = minutesAgendaService.generateAgenda(vo);
		ScheduleAgenda agendaDetails = minutesAgendaService.getPrevAgendaDetails(vo.getScheduleId());
		vo.setAgendaDetails(agendaDetails);
		return vo;
	}
	
	
	@Override
	public ScheduleVo showAllMeetingAttendence(ScheduleVo vo) {
		try{
			List<CommitteeMemberships> committeeMembershipList = scheduleDao.fetchMeetingMembers(vo);
			CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(vo.getScheduleId());
			List<CommitteeScheduleAttendance> scheduleAttendanceList = new ArrayList<CommitteeScheduleAttendance>();
			for(CommitteeMemberships committeeMemberships :committeeMembershipList){			
				CommitteeScheduleAttendance scheduleAttendance = new CommitteeScheduleAttendance();
				if (committeeMemberships.getTermStartDate() != null && committeeMemberships.getTermEndDate() != null && (committeeSchedule.getScheduledDate().before(committeeMemberships.getTermEndDate()) || committeeSchedule.getScheduledDate().equals(committeeMemberships.getTermEndDate()))) {
					String committeePersonId = null;
					String personName = null;
					Boolean alternateFlag = false;
					Boolean nonEmployeeFlag = false;
					if (committeeMemberships.getNonEmployeeFlag()) {
			    		 committeePersonId = String.valueOf(committeeMemberships.getRolodexId());
						 Rolodex rolodex = committeeDao.getRolodexById(committeeMemberships.getRolodexId());
						 personName = rolodex.getFullName();
						 nonEmployeeFlag = true;
					} else {
						 committeePersonId = committeeMemberships.getPersonId();
						 PersonDetailsView personDetails = committeeDao.getPersonDetailsById(committeeMemberships.getPersonId());
						 personName = personDetails.getFullName();			 
					}
					List<CommitteeMemberRoles> committeeMemberRoles  = scheduleDao.fetchCommitteeMemberRoles(committeeMemberships);
					committeeMemberships.setCommitteeMemberRoles(committeeMemberRoles);
					if(committeeMemberRoles.size() == 1 && committeeMemberRoles.get(0).getMembershipRoleCode().equalsIgnoreCase("12")){
						alternateFlag = true;
					}
					scheduleAttendance.setAcType("I");
					scheduleAttendance.setAlternateFlag(alternateFlag);
					scheduleAttendance.setCommitteeSchedule(committeeSchedule);
					scheduleAttendance.setPersonId(committeePersonId);
					scheduleAttendance.setPersonName(personName);
					scheduleAttendance.setNonEmployeeFlag(nonEmployeeFlag);
					scheduleAttendance.setUpdateUser(vo.getUpdateUser());
					java.util.Date date = new java.util.Date();				 
					long time = date.getTime(); 
					Timestamp ts = new Timestamp(time);
					scheduleAttendance.setUpdateTimestamp(ts);
					scheduleAttendanceList.add(scheduleAttendance);
				}
			}
			vo.setUpdatedAttendance(scheduleAttendanceList);
			vo = updateMeetingAttendence(vo);
		}catch (Exception e) {
			logger.error("Error in showAllMeetingAttendence: ", e);
		}
		return vo;
	}
	
	@Override
	public ScheduleVo loadMeetingAttendence(ScheduleVo vo) {
		try{
			List<CommitteeScheduleAttendance> alternateMember = new ArrayList<CommitteeScheduleAttendance>();		
			List<CommitteeScheduleAttendance> committeeMember = new ArrayList<CommitteeScheduleAttendance>();
			List<CommitteeScheduleAttendance> guestMember = new ArrayList<CommitteeScheduleAttendance>();
			List<CommitteeScheduleAttendance> scheduleAttendanceList =  scheduleDao.fetchAttendenceData(vo.getScheduleId());
			for(CommitteeScheduleAttendance scheduleAttendance : scheduleAttendanceList){
				if(scheduleAttendance.getAlternateFlag() != null && scheduleAttendance.getAlternateFlag()){
					alternateMember.add(scheduleAttendance);
				}else if (scheduleAttendance.getGuestFlag() != null && scheduleAttendance.getGuestFlag()) {
					guestMember.add(scheduleAttendance);
				}else {
					committeeMember.add(scheduleAttendance);
				}
			}
	 
			vo.setAlternateMember(alternateMember);
			vo.setCommitteeMember(committeeMember);
			vo.setGuestMembers(guestMember);
			vo.setCommitteeScheduleAttendance(new CommitteeScheduleAttendance());
		}catch (Exception e) {
			logger.info("Exception in loadMeetingAttendence:" + e);
		}
		return vo;
	}

	@Override
	public ScheduleVo updateMeetingAttendence(ScheduleVo vo) {
		try{
			List<CommitteeScheduleAttendance> scheduleAttendanceList = vo.getUpdatedAttendance();
			for(CommitteeScheduleAttendance scheduleAttendance:scheduleAttendanceList){
				if(scheduleAttendance.getAcType().equalsIgnoreCase("D")){
					scheduleDao.deleteMeetingAttendence(scheduleAttendance.getCommitteeScheduleAttendanceId());
				}else{
					CommitteeSchedule committeeSchedule = scheduleAttendance.getCommitteeSchedule() != null ? scheduleAttendance.getCommitteeSchedule() : vo.getCommitteeSchedule();
					scheduleAttendance.setCommitteeSchedule(committeeSchedule);
					scheduleDao.updateScheduleAttendance(scheduleAttendance);
				}		
			}
			vo = loadMeetingAttendence(vo);
		}catch (Exception e) {
			logger.info("Exception in updateMeetingAttendence:" + e);
		}
		return vo;
	}
	
	@Override
	public ScheduleVo loadMeetingAttachmentById(Integer scheduleId) {
		ScheduleVo scheduleVo = new ScheduleVo();
		List<CommitteeScheduleAttachType> committeeScheduleAttachTypes = scheduleDao.fetchAllCommitteeScheduleAttachType();
		scheduleVo.setAttachmentTypes(committeeScheduleAttachTypes);
		List<CommitteeScheduleAttachment> list = scheduleDao.getCommitteeScheduleAttachementById(scheduleId);
		scheduleVo.setCommitteeScheduleAttachmentList(list);
		return scheduleVo;
	}

	@Override
	public ScheduleVo saveOrUpdateMeetingAttachment(MultipartFile[] files, String formDataJson) {	
		ScheduleVo scheduleVo = new ScheduleVo();	
		try {
			ObjectMapper mapper = new ObjectMapper();
			ScheduleVo jsonObj = mapper.readValue(formDataJson, ScheduleVo.class);
			CommitteeScheduleAttachment committeeScheduleAttachment = jsonObj.getNewCommitteeScheduleAttachment();	
			Integer scheduleId = jsonObj.getScheduleId();
			
		    if(committeeScheduleAttachment.getAcType().equals("D")){
		    	scheduleVo = scheduleDao.deleteMeetingAttachment(committeeScheduleAttachment,scheduleId);
			}else if(committeeScheduleAttachment.getAcType() != null){
				scheduleVo = scheduleDao.saveOrUpdateMeetingAttachment(files,committeeScheduleAttachment,scheduleId);
			}
		} catch (Exception e) {
			logger.error("Error in saveAttachement: ", e);
		}
		return scheduleVo;
	}

	@Override
	public ResponseEntity<byte[]> downloadMeetingAttachment(String attachmentId) {
		ResponseEntity<byte[]> attachments = scheduleDao.downloadMeetingAttachment(attachmentId);
		return attachments;
	}
	
	@Override
	public ScheduleVo loadScheduleIdsForAgenda(ScheduleVo vo) {
		ArrayList<HashMap<String, Object>>  scheduleIds = scheduleDao.loadScheduleIdsForAgenda(vo.getCommitteeSchedule() ,vo.getCommitteeId());//for protocol submission						
		vo.setAgendaScheduleIds(scheduleIds);
		return vo;
	}

	@Override
	public ResponseEntity<byte[]> downloadScheduleAgenda(String scheduleId) {
		ResponseEntity<byte[]> attachments = scheduleDao.downloadScheduleAgenda(scheduleId);
		return attachments;
	}

	@Override
	public ScheduleVo loadAllScheduleAgenda(Integer scheduleId) {
		ScheduleVo scheduleVo = new ScheduleVo();		
		List<ScheduleAgenda> list = scheduleDao.loadAllScheduleAgenda(scheduleId);
		scheduleVo.setAgendaList(list);
		return scheduleVo;
	}
	
	@Override
	public ScheduleVo loadMeetingOtherActions(Integer scheduleId) {
		ScheduleVo scheduleVo = new ScheduleVo();	
		List<ScheduleActItemType> scheduleActItemTypes = scheduleDao.fetchAllScheduleActItemType();
		scheduleVo.setScheduleActItemTypes(scheduleActItemTypes);
		
		List<CommitteeScheduleActItems> list = scheduleDao.getCommitteeScheduleActItemsById(scheduleId);
		scheduleVo.setCommitteeScheduleActItemsList(list);
		return scheduleVo;
	}

	@Override
	public ScheduleVo updateMeetingOtherActions(ScheduleVo scheduleVo) {		
		 if(scheduleVo.getAcType().equals("D")){
			 scheduleDao.deleteMeetingOtherActions(scheduleVo.getCommScheduleActItemsId());
			 List<CommitteeScheduleActItems> list = scheduleDao.getCommitteeScheduleActItemsById(scheduleVo.getScheduleId());
			 scheduleVo.setCommitteeScheduleActItemsList(list);
		 }
		 else if(scheduleVo.getAcType() != null){	
			CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleVo.getScheduleId());
			CommitteeScheduleActItems committeeScheduleActItems = scheduleVo.getCommitteeScheduleActItems();
			committeeScheduleActItems.setCommitteeSchedule(committeeSchedule);
			//scheduleActItem.setActionItemNumber(getNextActionItemNumber(committeeSchedule));
			committeeScheduleActItems.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
			committeeScheduleActItems = scheduleDao.addOtherActions(committeeScheduleActItems);	
			List<CommitteeScheduleActItems> list = scheduleDao.getCommitteeScheduleActItemsById(scheduleVo.getScheduleId());
			scheduleVo.setCommitteeScheduleActItemsList(list);	
		}
			return scheduleVo;
		}

	@Override
	public ScheduleVo createMinuteForSchedule(ScheduleVo vo) {
		vo = minutesAgendaService.generateMintes(vo);
		CommitteeScheduleMinuteDoc minuteDetails = minutesAgendaService.getPrevMinuteDetails(vo.getScheduleId());
		vo.setMinuteDetails(minuteDetails);
		return vo;
	}

	@Override
	public ResponseEntity<byte[]> downloadScheduleAgendaById(String scheduleAgendaId) {
		ResponseEntity<byte[]> attachments = scheduleDao.downloadScheduleAgendaById(scheduleAgendaId);
		return attachments;
	}

	@Override
	public ResponseEntity<byte[]> downloadScheduleMinute(String scheduleId) {
		ResponseEntity<byte[]> attachments = scheduleDao.downloadScheduleMinute(scheduleId);
		return attachments;
	}

	@Override
	public ResponseEntity<byte[]> downloadScheduleMinuteById(String scheduleMinuteDocId) {
		ResponseEntity<byte[]> attachments = scheduleDao.downloadScheduleMinuteById(scheduleMinuteDocId);
		return attachments;
	}

	@Override
	public ScheduleVo loadAllScheduleMinutes(Integer scheduleId) {
		ScheduleVo scheduleVo = new ScheduleVo();		
		List<CommitteeScheduleMinuteDoc> list = scheduleDao.loadAllScheduleMinutes(scheduleId);
		scheduleVo.setMinuteList(list);
		return scheduleVo;
	}
}
