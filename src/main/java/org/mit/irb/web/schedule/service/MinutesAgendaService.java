package org.mit.irb.web.schedule.service;

import org.mit.irb.web.committee.pojo.CommitteeScheduleMinuteDoc;
import org.mit.irb.web.committee.pojo.ScheduleAgenda;
import org.mit.irb.web.schedule.vo.ScheduleVo;
import org.springframework.stereotype.Service;

@Service
public interface MinutesAgendaService {

	/**
	 * @param vo
	 * @return
	 */
	public ScheduleVo generateAgenda(ScheduleVo vo);
	
	/**
	 * @param vo
	 * @return
	 */
	public ScheduleVo generateMintes(ScheduleVo vo);
	
	/**
	 * @param scheduleId
	 * @return
	 */
	public CommitteeScheduleMinuteDoc getPrevMinuteDetails(Integer scheduleId);
	
	/**
	 * @param scheduleId
	 * @return
	 */
	public ScheduleAgenda getPrevAgendaDetails(Integer scheduleId);
}
