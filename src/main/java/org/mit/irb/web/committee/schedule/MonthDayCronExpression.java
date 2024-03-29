package org.mit.irb.web.committee.schedule;

import java.text.ParseException;
import java.util.Date;


/**
* This class extends CronExpression and provides MonthDayCronExpression implementation.
* <p>
* This implementation generates expression for monthly scheduling dates using day of month and month.
* 
* day of month: 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31
* 
* month: 1,2,3,4,5,6,7,8,9,10,11,12
* 
* Note: Start day is skipped, date boundary is handled by ScheduleSequence implementation  during schedule generation.
*  
* e.g. Start Date : 02/24/09, Time : 10:10 (hh:mm) Format (second minute hour day month year) Generated Expression :0 10 10 6 2/1 ?
* Explanation: Generate dates for 6th day of each month at 10:10 (hh:mm), starting from month of February.
* 
* e.g. Start Date : 02/24/09, Time : 10:10 (hh:mm) Format (second minute hour day month year) Generated Expression :0 10 10 6 2/2 ?
* Explanation: Generate dates for 6th day of every other month at 10:10 (hh:mm), starting from month of February.
*/
public class MonthDayCronExpression extends CronExpression {

	private Integer day;

	private Integer frequencyInMonth;

	public MonthDayCronExpression(Date startDate, Time24HrFmt time, Integer day, Integer frequencyInMonth)
			throws ParseException {
		super(startDate, time);
		this.day = day;
		this.frequencyInMonth = frequencyInMonth;
	}

	@Override
	public String getExpression() {
		StringBuilder exp = new StringBuilder();
		exp.append(SECONDS).append(CronSpecialChars.SPACE);
		exp.append(getTime().getMinutes()).append(CronSpecialChars.SPACE);
		exp.append(getTime().getHours()).append(CronSpecialChars.SPACE);
		exp.append(day).append(CronSpecialChars.SPACE);
		exp.append(CronSpecialChars.FIRST).append(CronSpecialChars.SLASH).append(frequencyInMonth)
				.append(CronSpecialChars.SPACE);
		exp.append(CronSpecialChars.QUESTION);
		return exp.toString();
	}

}
