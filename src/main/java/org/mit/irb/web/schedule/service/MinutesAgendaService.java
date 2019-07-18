package org.mit.irb.web.schedule.service;

import org.mit.irb.web.schedule.vo.ScheduleVo;
import org.springframework.stereotype.Service;

@Service
public interface MinutesAgendaService {

	public ScheduleVo generateAgenda(ScheduleVo vo);
}
