package org.mit.irb.web.common.view;

/*
 * Class for setting snapshot details in Dashboard
*/
public class SnapshotData {

	String ammendRenewCount;
	String revisionReqCount;
	String awaitingRespCount;
	String committeNamePrev;
	String committeeNameNext;
	String scheduledDatePrev;
	String scheduledDayPrev;
	String scheduledDateNext;
	String scheduledDayNext;
	Integer unassignedAdminCount;
	Integer newSubmissionCount;

	public String getAmmendRenewCount() {
		return ammendRenewCount;
	}

	public void setAmmendRenewCount(String ammendRenewCount) {
		this.ammendRenewCount = ammendRenewCount;
	}

	public String getRevisionReqCount() {
		return revisionReqCount;
	}

	public void setRevisionReqCount(String revisionReqCount) {
		this.revisionReqCount = revisionReqCount;
	}

	public String getAwaitingRespCount() {
		return awaitingRespCount;
	}

	public void setAwaitingRespCount(String awaitingRespCount) {
		this.awaitingRespCount = awaitingRespCount;
	}

	public String getCommitteNamePrev() {
		return committeNamePrev;
	}

	public void setCommitteNamePrev(String committeNamePrev) {
		this.committeNamePrev = committeNamePrev;
	}

	public String getCommitteeNameNext() {
		return committeeNameNext;
	}

	public void setCommitteeNameNext(String committeeNameNext) {
		this.committeeNameNext = committeeNameNext;
	}

	public String getScheduledDatePrev() {
		return scheduledDatePrev;
	}

	public void setScheduledDatePrev(String scheduledDatePrev) {
		this.scheduledDatePrev = scheduledDatePrev;
	}

	public String getScheduledDayPrev() {
		return scheduledDayPrev;
	}

	public void setScheduledDayPrev(String scheduledDayPrev) {
		this.scheduledDayPrev = scheduledDayPrev;
	}

	public String getScheduledDateNext() {
		return scheduledDateNext;
	}

	public void setScheduledDateNext(String scheduledDateNext) {
		this.scheduledDateNext = scheduledDateNext;
	}

	public String getScheduledDayNext() {
		return scheduledDayNext;
	}

	public void setScheduledDayNext(String scheduledDayNext) {
		this.scheduledDayNext = scheduledDayNext;
	}

	public Integer getUnassignedAdminCount() {
		return unassignedAdminCount;
	}

	public void setUnassignedAdminCount(Integer unassignedAdminCount) {
		this.unassignedAdminCount = unassignedAdminCount;
	}

	public Integer getNewSubmissionCount() {
		return newSubmissionCount;
	}

	public void setNewSubmissionCount(Integer newSubmissionCount) {
		this.newSubmissionCount = newSubmissionCount;
	}
}
