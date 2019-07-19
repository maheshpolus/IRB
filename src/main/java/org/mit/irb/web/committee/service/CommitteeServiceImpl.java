package org.mit.irb.web.committee.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.mit.irb.web.IRBProtocol.service.IRBUtilService;
import org.mit.irb.web.committee.dao.CommitteeDao;
import org.mit.irb.web.committee.pojo.Committee;
import org.mit.irb.web.committee.pojo.CommitteeMemberships;
import org.mit.irb.web.committee.pojo.CommitteeResearchAreas;
import org.mit.irb.web.committee.pojo.CommitteeSchedule;
import org.mit.irb.web.committee.pojo.CommitteeType;
import org.mit.irb.web.committee.pojo.ResearchArea;
import org.mit.irb.web.committee.pojo.ScheduleStatus;
import org.mit.irb.web.committee.schedule.CronSpecialChars;
import org.mit.irb.web.committee.schedule.DailyScheduleDetails;
import org.mit.irb.web.committee.schedule.DefaultScheduleSequence;
import org.mit.irb.web.committee.schedule.MonthlyScheduleDetails;
import org.mit.irb.web.committee.schedule.ScheduleData;
import org.mit.irb.web.committee.schedule.ScheduleSequence;
import org.mit.irb.web.committee.schedule.StyleKey;
import org.mit.irb.web.committee.schedule.Time24HrFmt;
import org.mit.irb.web.committee.schedule.TrimDatesScheduleSequenceDecorator;
import org.mit.irb.web.committee.schedule.WeekScheduleSequenceDecorator;
import org.mit.irb.web.committee.schedule.YearlyScheduleDetails;
import org.mit.irb.web.committee.vo.CommitteeVo;
import org.mit.irb.web.committee.pojo.Rolodex;
import org.mit.irb.web.committee.pojo.Unit;
import org.mit.irb.web.committee.view.PersonDetailsView;

@Transactional
@Service(value = "committeeService")
public class CommitteeServiceImpl implements CommitteeService {

	protected static Logger logger = Logger.getLogger(CommitteeServiceImpl.class.getName());

	private static final String COLON = ":";
	private static final String SCHEDULED = "Scheduled";

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private CommitteeScheduleService committeeScheduleService;
	
	@Autowired
	IRBUtilService irbUtilService;

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Override
	public CommitteeVo fetchInitialDatas() {
		CommitteeVo committeeVo = new CommitteeVo();
		/*List<ProtocolReviewType> reviewTypes = committeeDao.fetchAllReviewType();
		committeeVo.setReviewTypes(reviewTypes);*/
		List<Unit> units = committeeDao.fetchAllHomeUnits();
		committeeVo.setHomeUnits(units);
		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		committeeVo.setResearchAreas(researchAreas);
		List<ScheduleStatus> scheduleStatus = committeeDao.fetchAllScheduleStatus();
		committeeVo.setScheduleStatus(scheduleStatus);
		return committeeVo;
	}

	@Override
	public CommitteeVo createCommittee(){
		CommitteeVo committeeVo = new CommitteeVo();
		Committee committee = new Committee();
		try{
			List<CommitteeType> committeeTypeList = committeeDao.fetchCommitteeType();
			committeeVo.setCommitteeTypeList(committeeTypeList);
			committeeVo.setCommittee(committee);			
		}catch (Exception e) {
			logger.error("Error in createCommittee method : "+e.getMessage());	
		}
		return committeeVo;
	}

	@Override
	public CommitteeVo saveCommittee(CommitteeVo vo) {
		Committee committee = new Committee();
		committee = vo.getCommittee();
		String updateType = vo.getUpdateType();		
		if ( updateType != null &&  updateType.equals("UPDATE")) {
			committee = committeeDao.saveCommittee(committee);
			vo.setStatus(true);
		}else{ 
			Boolean isIDUnique = committeeDao.checkUniqueCommitteeId(committee);
			if(isIDUnique){
				committee = committeeDao.saveCommittee(committee);
				vo.setStatus(true);			
			}else{	
				vo.setStatus(false);			
				vo.setMessage("Committee ID already exist");				
			}
		}
		vo.setCommittee(committee);
		return vo;
	}

	@Override
	public CommitteeVo loadCommitteeById(String committeeId) {
		CommitteeVo committeeVo = new CommitteeVo();
		try {
		    Committee committee = committeeDao.fetchCommitteeById(committeeId);		 
		    committeeVo.setCommittee(committee);
		    List<CommitteeType> committeeTypeList = committeeDao.fetchCommitteeType();
			committeeVo.setCommitteeTypeList(committeeTypeList);
		}catch (Exception e) {
		logger.error("Error in loadCommitteeById method : "+e.getMessage());		
		}
		return committeeVo;
	}

	@Override
	public CommitteeVo addSchedule(CommitteeVo committeeVo) throws ParseException {

		ScheduleData scheduleData = committeeVo.getScheduleData();
		Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommittee().getCommitteeId());
		Committee com = committeeDao.loadScheduleDetailsById(committeeVo.getCommittee().getCommitteeId(),null);
		committee.setCommitteeSchedules(com.getCommitteeSchedules());
		List<Date> dates = null;
		Date dtEnd = null;
		int frequency = 0;
		int day = 0;
		CronSpecialChars[] weekdays = null;
		CronSpecialChars weekOfMonth = null;
		CronSpecialChars dayOfWeek = null;
		CronSpecialChars month = null;
		scheduleData.setScheduleStartDate(irbUtilService.adjustTimezone(scheduleData.getScheduleStartDate()));
		Time24HrFmt time = getTime24hFmt(scheduleData.getScheduleStartDate(), scheduleData.getTime().findMinutes());
		Date dt = scheduleData.getScheduleStartDate();

		StyleKey key = StyleKey.valueOf(scheduleData.getRecurrenceType());
		logger.info("RecurrenceType : " + scheduleData.getRecurrenceType());
		logger.info("ScheduleStartDate : " + scheduleData.getScheduleStartDate());
		switch (key) {
		case NEVER:
			dates = committeeScheduleService.getScheduledDates(dt, dt, time, null);
			break;
		case DAILY:
			DailyScheduleDetails.optionValues dailyoption = DailyScheduleDetails.optionValues
					.valueOf(scheduleData.getDailySchedule().getDayOption());
			switch (dailyoption) {
			case XDAY:
				dtEnd = scheduleData.getDailySchedule().getScheduleEndDate();
				day = scheduleData.getDailySchedule().getDay();
				dates = committeeScheduleService.getIntervalInDaysScheduledDates(dt, dtEnd, time, day);
				break;
			case WEEKDAY:
				dtEnd = scheduleData.getDailySchedule().getScheduleEndDate();
				weekdays = ScheduleData.convertToWeekdays(scheduleData.getDailySchedule().getDaysOfWeek());
				ScheduleSequence scheduleSequence = new WeekScheduleSequenceDecorator(
						new TrimDatesScheduleSequenceDecorator(new DefaultScheduleSequence()), 1, weekdays.length);
				dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, weekdays, scheduleSequence);
				break;
			}
			break;
		case WEEKLY:
			dtEnd = scheduleData.getWeeklySchedule().getScheduleEndDate();
			if (CollectionUtils.isNotEmpty(scheduleData.getWeeklySchedule().getDaysOfWeek())) {
				weekdays = ScheduleData.convertToWeekdays(scheduleData.getWeeklySchedule().getDaysOfWeek()
						.toArray(new String[scheduleData.getWeeklySchedule().getDaysOfWeek().size()]));
			}

			ScheduleSequence scheduleSequence = new WeekScheduleSequenceDecorator(
					new TrimDatesScheduleSequenceDecorator(new DefaultScheduleSequence()),
					scheduleData.getWeeklySchedule().getWeek(), weekdays.length);
			dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, weekdays, scheduleSequence);
			break;
		case MONTHLY:
			MonthlyScheduleDetails.optionValues monthOption = MonthlyScheduleDetails.optionValues
					.valueOf(scheduleData.getMonthlySchedule().getMonthOption());
			switch (monthOption) {
			case XDAYANDXMONTH:
				dtEnd = scheduleData.getMonthlySchedule().getScheduleEndDate();
				day = scheduleData.getMonthlySchedule().getDay();
				frequency = scheduleData.getMonthlySchedule().getOption1Month();
				dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, day, frequency, null);
				break;
			case XDAYOFWEEKANDXMONTH:
				dtEnd = scheduleData.getMonthlySchedule().getScheduleEndDate();
				weekOfMonth = ScheduleData.getWeekOfMonth(scheduleData.getMonthlySchedule().getSelectedMonthsWeek());
				dayOfWeek = ScheduleData.getDayOfWeek(scheduleData.getMonthlySchedule().getSelectedDayOfWeek());
				frequency = scheduleData.getMonthlySchedule().getOption2Month();
				dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, dayOfWeek, weekOfMonth, frequency, null);
				break;
			}
			break;
		case YEARLY:
			YearlyScheduleDetails.yearOptionValues yearOption = YearlyScheduleDetails.yearOptionValues
					.valueOf(scheduleData.getYearlySchedule().getYearOption());
			switch (yearOption) {
			case XDAY:
				dtEnd = scheduleData.getYearlySchedule().getScheduleEndDate();
				month = ScheduleData.getMonthOfWeek(scheduleData.getYearlySchedule().getSelectedOption1Month());
				day = scheduleData.getYearlySchedule().getDay();
				frequency = scheduleData.getYearlySchedule().getOption1Year();
				dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, month, day, frequency, null);
				break;
			case CMPLX:
				dtEnd = scheduleData.getYearlySchedule().getScheduleEndDate();
				weekOfMonth = ScheduleData.getWeekOfMonth(scheduleData.getYearlySchedule().getSelectedMonthsWeek());
				dayOfWeek = ScheduleData.getDayOfWeek(scheduleData.getYearlySchedule().getSelectedDayOfWeek());
				month = ScheduleData.getMonthOfWeek(scheduleData.getYearlySchedule().getSelectedOption2Month());
				frequency = scheduleData.getYearlySchedule().getOption2Year();
				dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, weekOfMonth, dayOfWeek, month, frequency,
						null);
				break;
			}
			break;
		}
		List<java.sql.Date> skippedDates = new ArrayList<java.sql.Date>();
		addScheduleDatesToCommittee(dates, committee, scheduleData.getPlace(), skippedDates, committeeVo);
		scheduleData.setDatesInConflict(skippedDates);
		logger.info("skippedDates : " + skippedDates);
		Committee comSche=committeeDao.loadScheduleDetailsById(committeeVo.getCommittee().getCommitteeId(),"F");
		committeeVo.getCommittee().setCommitteeSchedules(comSche.getCommitteeSchedules());
		/*committee = committeeDao.saveCommittee(committee);
		committeeVo.setCommittee(committee);*/
		return committeeVo;
	}

	protected Time24HrFmt getTime24hFmt(Date date, int min) throws ParseException {
		Date dt = DateUtils.round(date, Calendar.DAY_OF_MONTH);
		dt = DateUtils.addMinutes(dt, min);
		Calendar cl = new GregorianCalendar();
		cl.setTime(dt);
		StringBuffer sb = new StringBuffer();
		String str = sb.append(cl.get(Calendar.HOUR_OF_DAY)).append(COLON).append(cl.get(Calendar.MINUTE)).toString();
		return new Time24HrFmt(str);
	}

	protected void addScheduleDatesToCommittee(List<Date> dates, Committee committee, String location,
			List<java.sql.Date> skippedDates, CommitteeVo committeeVo) {
		for (Date date : dates) {
			java.sql.Date sqldate = new java.sql.Date(date.getTime());

			if (!isDateAvailable(committee.getCommitteeSchedules(), sqldate)) {
				skippedDates.add(sqldate);
				continue;
			}

			CommitteeSchedule committeeSchedule = new CommitteeSchedule();
			committeeSchedule.setCommittee(committee);
			committeeSchedule.setScheduledDate(sqldate);
			committeeSchedule.setPlace(location);
			committeeSchedule.setTime(new Timestamp(date.getTime()));

			int daysToAdd = committee.getAdvSubmissionDaysReq();
			java.sql.Date sqlDate = calculateAdvancedSubmissionDays(date, daysToAdd);
			committeeSchedule.setProtocolSubDeadline(sqlDate);

			committeeSchedule.setCommittee(committee);

			ScheduleStatus defaultStatus = getDefaultScheduleStatus();
			committeeSchedule.setScheduleStatusCode(defaultStatus.getScheduleStatusCode());
			committeeSchedule.setScheduleStatus(defaultStatus);
			committeeSchedule.setUpdateTimestamp(committeeDao.getCurrentTimestamp());
			committeeSchedule.setUpdateUser(committeeVo.getCurrentUser());
			hibernateTemplate.saveOrUpdate(committeeSchedule); //code is changes since schedule is not saved properly
			/*committee.getCommitteeSchedules().add(committeeSchedule);*/
		}
	}

	protected Boolean isDateAvailable(List<CommitteeSchedule> committeeSchedules, java.sql.Date date) {
		boolean retVal = true;
		for (CommitteeSchedule committeeSchedule : committeeSchedules) {
			Date scheduledDate = committeeSchedule.getScheduledDate();
			if ((scheduledDate != null) && DateUtils.isSameDay(scheduledDate, date)) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}

	protected Boolean isDateAvailableForUpdate(List<CommitteeSchedule> committeeSchedules, CommitteeSchedule schedule) {
		boolean retVal = true;
		for (CommitteeSchedule committeeSchedule : committeeSchedules) {
			Date scheduledDate = committeeSchedule.getScheduledDate();
			if ((scheduledDate != null) && DateUtils.isSameDay(scheduledDate, schedule.getScheduledDate())) {
				if (!committeeSchedule.getScheduleId().equals(schedule.getScheduleId())) {
					retVal = false;
					break;
				}
			}
		}
		return retVal;
	}

	protected java.sql.Date calculateAdvancedSubmissionDays(Date startDate, Integer days) {
		Date deadlineDate = DateUtils.addDays(startDate, -days);
		return new java.sql.Date(deadlineDate.getTime());
	}

	protected ScheduleStatus getDefaultScheduleStatus() {
		return committeeDao.fetchScheduleStatusByStatus(SCHEDULED);
	}

	@Override
	public CommitteeVo saveAreaOfResearch(CommitteeVo committeeVo) {
		Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
		CommitteeResearchAreas researchAreas = new CommitteeResearchAreas();
		researchAreas.setCommittee(committee);
		researchAreas.setResearchAreaCode(committeeVo.getCommitteeResearchArea().getResearchAreaCode());
		researchAreas.setResearchAreaDescription(committeeVo.getCommitteeResearchArea().getResearchAreaDescription());
		researchAreas.setUpdateTimestamp(committeeVo.getCommitteeResearchArea().getUpdateTimestamp());
		researchAreas.setUpdateUser(committeeVo.getCommitteeResearchArea().getUpdateUser());
		committee.getResearchAreas().add(researchAreas);
		researchAreas = committeeDao.saveCommitteeResearchAreas(researchAreas);
		committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
		committeeVo.setCommittee(committee);
		return committeeVo;
	}

	@Override
	public CommitteeVo deleteAreaOfResearch(CommitteeVo committeeVo) {
		try {
			committeeDao.deleteCommitteeResearchArea(committeeVo.getCommResearchAreasId());
			Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
			committeeVo.setCommittee(committee);
			committeeVo.setStatus(true);
			committeeVo.setMessage("Committee research area deleted successfully");
		} catch (Exception e) {
			committeeVo.setStatus(true);
			committeeVo.setMessage("Problem occurred in deleting committee research area");
			e.printStackTrace();
		}
		return committeeVo;
	}

	@Override
	public CommitteeVo deleteSchedule(CommitteeVo committeeVo) {
		try {
		/*	Committee committee = committeeDao.loadScheduleDetailsById(committeeVo.getCommitteeId(),null);
			List<CommitteeSchedule> list = committee.getCommitteeSchedules();
			List<CommitteeSchedule> updatedlist = new ArrayList<CommitteeSchedule>(list);
			Collections.copy(updatedlist, list);
			for (CommitteeSchedule schedule : list) {
				if (schedule.getScheduleId().equals(committeeVo.getScheduleId())) {
					committeeDao.deleteCommitteeSchedule(schedule);
				}
			}
			committee=committeeDao.loadScheduleDetailsById(committeeVo.getCommitteeId(),null);
			committeeVo.setCommittee(committee);*/
			committeeDao.deleteCommitteeSchedule(committeeVo.getCommitteeSchedule());			
			Committee committee = new Committee();
			committee = committeeDao.loadScheduleDetailsById(committeeVo.getCommitteeId(),"F");
			committeeVo.setCommittee(committee);
			committeeVo.setCommitteeSchedule(null);
			committeeVo.setStatus(true);
			committeeVo.setMessage("Committee schedule deleted successfully");
		} catch (Exception e) {
			committeeVo.setStatus(true);
			committeeVo.setMessage("Problem occurred in deleting committee schedule");
			e.printStackTrace();
		}
		return committeeVo;
	}

	@Override
	public CommitteeVo filterCommitteeScheduleDates(CommitteeVo committeeVo) {
		ScheduleData scheduleData = committeeVo.getScheduleData();
		Committee committee = committeeVo.getCommittee();
		Date startDate = scheduleData.getFilterStartDate();
		Date endDate = scheduleData.getFilerEndDate();
		logger.info("FilterStartDate : " + startDate);
		logger.info("FilerEndDate : " + endDate);
		startDate = DateUtils.addDays(startDate, -1);
		endDate = DateUtils.addDays(endDate, 1);
		java.util.Date scheduleDate = null;
		for (CommitteeSchedule schedule : getSortedCommitteeScheduleList(committee)) {
			scheduleDate = schedule.getScheduledDate();
			if ((scheduleDate != null) && scheduleDate.after(startDate) && scheduleDate.before(endDate)) {
				schedule.setFilter(true);
			} else {
				schedule.setFilter(false);
			}
		}
		return committeeVo;
	}

	private List<CommitteeSchedule> getSortedCommitteeScheduleList(Committee committee) {
        List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
        Collections.sort(committeeSchedules);
        return committeeSchedules;
    }

	@Override
	public CommitteeVo resetCommitteeScheduleDates(CommitteeVo committeeVo) {
		ScheduleData scheduleData = committeeVo.getScheduleData();
		Committee committee = committeeVo.getCommittee();
		for (CommitteeSchedule schedule : getSortedCommitteeScheduleList(committee)) {
			schedule.setFilter(true);
		}
		scheduleData.setFilterStartDate(null);
		scheduleData.setFilerEndDate(null);
		return committeeVo;
	}

	@Override
	public CommitteeVo updateCommitteeSchedule(CommitteeVo committeeVo) {
		Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
		Committee com=committeeDao.loadScheduleDetailsById(committeeVo.getCommitteeId(),null);
		List<CommitteeSchedule> committeeSchedule = com.getCommitteeSchedules();		
		CommitteeSchedule schedule = new CommitteeSchedule();
		schedule = committeeVo.getCommitteeSchedule();
		boolean isDateExist = isDateAvailableForUpdate(committeeSchedule, schedule);
		String response = "";
		if (!isDateExist) {
			committeeVo.setStatus(false);
			response = "Scheduled date already exist";
			committeeVo.setMessage(response);
		} else {			
			for (CommitteeSchedule committeeSchedules : committeeSchedule) {
				if (committeeSchedules.getScheduleId().equals(schedule.getScheduleId())) {
					committeeSchedules.setScheduledDate(schedule.getScheduledDate());
					committeeSchedules.setPlace(schedule.getPlace());
					committeeSchedules.setTime(schedule.getTime());
					committeeSchedules.setScheduleStatus(schedule.getScheduleStatus());
					committeeSchedules.setScheduleStatusCode(schedule.getScheduleStatusCode());
					int daysToAdd = committee.getAdvSubmissionDaysReq();
					java.sql.Date sqlDate = calculateAdvancedSubmissionDays(schedule.getScheduledDate(), daysToAdd);
					committeeSchedules.setProtocolSubDeadline(sqlDate);
					committeeSchedules.setCommittee(committee);
					schedule = committeeDao.updateCommitteeSchedule(committeeSchedules);
					committeeVo.setCommitteeSchedule(schedule);
					committeeVo.setStatus(true);
					break;
				}
			}			
	  }
		return committeeVo;
	}
	
	@Override
	public CommitteeVo loadHomeUnits(String homeUnitSearchString) {	
		CommitteeVo vo=new CommitteeVo();
	    vo.setHomeUnits(committeeDao.loadhomeUnits(homeUnitSearchString));
		return vo;
	}
	
	@Override
	public CommitteeVo loadResearchAreas(String researchSearchString) {
		CommitteeVo vo=new CommitteeVo();
		vo.setResearchAreas(committeeDao.loadResearchAreas(researchSearchString));
		return vo;
	}

	@Override
	public CommitteeVo loadScheduleDetailsById(String committeeId, String acType) {
		Committee committee=committeeDao.loadScheduleDetailsById(committeeId,acType);	   
		List<ScheduleStatus> scheduleStatus = committeeDao.fetchAllScheduleStatus();
		CommitteeVo vo=new CommitteeVo();
		vo.setCommittee(committee);
		vo.setScheduleStatus(scheduleStatus);
		return vo;
	}

	@Override
	public CommitteeVo loadCommitteeMembers(String committeeId) {
		Committee committee=committeeDao.loadCommitteeMembers(committeeId);
		List<CommitteeMemberships> committeeMemberships = committee.getCommitteeMemberships();
		if (committeeMemberships != null && !committeeMemberships.isEmpty()) {
			for (CommitteeMemberships membership : committeeMemberships) {
				if (membership.getNonEmployeeFlag()) {
					Rolodex rolodex = committeeDao.getRolodexById(membership.getRolodexId());
					membership.setRolodex(rolodex);
				} else {
					PersonDetailsView personDetails = committeeDao.getPersonDetailsById(membership.getPersonId());
					membership.setPersonDetails(personDetails);
				}
			}
		}
		CommitteeVo vo=new CommitteeVo();
		vo.setCommittee(committee);
		return vo;
	}
}
