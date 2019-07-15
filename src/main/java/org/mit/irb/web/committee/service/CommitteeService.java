package org.mit.irb.web.committee.service;

import java.text.ParseException;

import org.springframework.stereotype.Service;
import org.mit.irb.web.committee.pojo.CommitteeMemberships;
import org.mit.irb.web.committee.vo.CommitteeVo;

@Service("committeeService")
public interface CommitteeService {

	/**
	 * This method is used to create committee.
	 * @param committeeTypeCode - Type code of committee.
	 * @return committee
	 */
	public CommitteeVo createCommittee();

	/**
	 * This method is used to save committee.
	 * @param vo - Object of CommitteeVo.
	 * @return Response object of committee.
	 */
	public CommitteeVo saveCommittee(CommitteeVo vo);

	/**
	 * This method is used to fetch initial datas for committee.
	 * @return a CommitteeVo of details for committee.
	 */
	public CommitteeVo fetchInitialDatas();

	/**
	 * This method is used to load a committee by Id.
	 * @param committeeId - Id of the committee.
	 * @return committee - a CommitteeVo of details of committee.
	 */
	public CommitteeVo loadCommitteeById(String committeeId);

	/**
	 * This method is used to create a schedule.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return  a CommitteeVo of details of committee schedule.
	 * @throws ParseException
	 */
	public CommitteeVo addSchedule(CommitteeVo committeeVo) throws ParseException;

	/**
	 * This method is used to save area of research.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details of committee with area of research.
	 */
	public CommitteeVo saveAreaOfResearch(CommitteeVo committeeVo);

	/**
	 * This method is used to delete area of research.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details of committee with updated area of research list.
	 */
	public CommitteeVo deleteAreaOfResearch(CommitteeVo committeeVo);

	/**
	 * This method is used to delete Schedule.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details of committee with updated list of committee schedules.
	 */
	public CommitteeVo deleteSchedule(CommitteeVo committeeVo);

	/**
	 * This method is used to filter CommitteeScheduleDates.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details with filtered list of committee schedule.
	 */
	public CommitteeVo filterCommitteeScheduleDates(CommitteeVo committeeVo);

	/**
	 * This method is used to reset CommitteeScheduleDates.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details with list of committee schedule.
	 */
	public CommitteeVo resetCommitteeScheduleDates(CommitteeVo committeeVo);

	/**
	 * This method is used to update CommitteeSchedule.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a CommitteeVo of details with updated list of committee schedule.
	 */
	public CommitteeVo  updateCommitteeSchedule(CommitteeVo committeeVo);

	/**
	 * This method is used to get homeUnits when searchString is passed.
	 * @param homeUnitSearchString
	 * @return
	 */
	public CommitteeVo loadHomeUnits(String homeUnitSearchString);

	/**
	 * This method is used to get researchArea when researchSearchString is passed.
	 * @param researchSearchString
	 * @return
	 */
	public CommitteeVo loadResearchAreas(String researchSearchString);

	/**
	 * This method is used to get schedule details  for a particular committee id
	 * @param committeeId
	 * @param acType 
	 * @return
	 */
	public CommitteeVo loadScheduleDetailsById(String committeeId, String acType);

	/**
	 * @param committeeId
	 * @return
	 */
	public CommitteeVo loadCommitteeMembers(String committeeId);
}
