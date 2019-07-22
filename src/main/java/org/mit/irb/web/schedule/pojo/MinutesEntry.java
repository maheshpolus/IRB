package org.mit.irb.web.schedule.pojo;

public class MinutesEntry {	
	
	private String otherTitle;
	private String minuteEntry;

	public String getMinuteEntry() {
		return minuteEntry;
	}

	public void setMinuteEntry(String minuteEntry) {
		this.minuteEntry = minuteEntry;
	}

	public String getOtherTitle() {
		return otherTitle;
	}

	public void setOtherTitle(String otherTitle) {
		this.otherTitle = otherTitle;
	}

	public MinutesEntry(String otherTitle, String minuteEntry) {
		this.otherTitle = otherTitle;
		this.minuteEntry = minuteEntry;
	}
}
