package org.mit.irb.web.IRBProtocol.pojo;

public class IRBActionsReviewerComments {
	private String committeeScheduleMinutesId;
	private String comments;
	private String flag;
	private String contingencyCode;
	
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
}
