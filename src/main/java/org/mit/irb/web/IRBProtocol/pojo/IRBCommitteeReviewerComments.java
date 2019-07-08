package org.mit.irb.web.IRBProtocol.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IRBCommitteeReviewerComments {
	private String committeeScheduleMinutesId;
	private String comments;
	private String flag;
	private String contingencyCode;
	private String letterFlag;
	
	public String getCommitteeScheduleMinutesId() {
		return committeeScheduleMinutesId;
	}
	public void setCommitteeScheduleMinutesId(String committeeScheduleMinutesId) {
		this.committeeScheduleMinutesId = committeeScheduleMinutesId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getContingencyCode() {
		return contingencyCode;
	}
	public void setContingencyCode(String contingencyCode) {
		this.contingencyCode = contingencyCode;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getLetterFlag() {
		return letterFlag;
	}
	public void setLetterFlag(String letterFlag) {
		this.letterFlag = letterFlag;
	}
	
	public IRBCommitteeReviewerComments(@JsonProperty("myStringVar")String comments) {
		this.comments = comments;
	}
}
