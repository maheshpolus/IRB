package org.mit.irb.web.schedule.pojo;

public class ProtocolDetails {
	
	private String protocolNumber;
	private String personName;
	private String protocolTitle;
	private String committeePriRev;
	private String committeeSecRev;
	private String submissionTypeDescription;
	private String adminComments;
	private String expirationDate;
	private String previousSchMeetingDate;
	private String nextSchMeetingDate;
	private String nextMeetingPlace;
	private Integer noVote;
	private Integer yesVote;
	private Integer abstainers;
	private String minuteEntry;
	
	public ProtocolDetails(String protocolNumber, String personName, String protocolTitle, String committeePriRev,
			String committeeSecRev, String submissionTypeDescription, String adminComments,String expirationDate, Integer noVote, Integer yesVote, Integer abstainers, String minuteEntry) {
		super();
		this.protocolNumber = protocolNumber;
		this.personName = personName;
		this.protocolTitle = protocolTitle;
		this.committeePriRev = committeePriRev;
		this.committeeSecRev = committeeSecRev;
		this.submissionTypeDescription = submissionTypeDescription;
		this.adminComments = adminComments;
		this.expirationDate = expirationDate;
		this.noVote = noVote;
		this.yesVote = yesVote;
		this.abstainers = abstainers;
		this.minuteEntry = minuteEntry;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getProtocolTitle() {
		return protocolTitle;
	}

	public void setProtocolTitle(String protocolTitle) {
		this.protocolTitle = protocolTitle;
	}

	public String getCommitteePriRev() {
		return committeePriRev;
	}

	public void setCommitteePriRev(String committeePriRev) {
		this.committeePriRev = committeePriRev;
	}

	public String getCommitteeSecRev() {
		return committeeSecRev;
	}

	public void setCommitteeSecRev(String committeeSecRev) {
		this.committeeSecRev = committeeSecRev;
	}

	public String getSubmissionTypeDescription() {
		return submissionTypeDescription;
	}

	public void setSubmissionTypeDescription(String submissionTypeDescription) {
		this.submissionTypeDescription = submissionTypeDescription;
	}

	public String getAdminComments() {
		return adminComments;
	}

	public void setAdminComments(String adminComments) {
		this.adminComments = adminComments;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getPreviousSchMeetingDate() {
		return previousSchMeetingDate;
	}

	public void setPreviousSchMeetingDate(String previousSchMeetingDate) {
		this.previousSchMeetingDate = previousSchMeetingDate;
	}

	public String getNextSchMeetingDate() {
		return nextSchMeetingDate;
	}

	public void setNextSchMeetingDate(String nextSchMeetingDate) {
		this.nextSchMeetingDate = nextSchMeetingDate;
	}

	public String getNextMeetingPlace() {
		return nextMeetingPlace;
	}

	public void setNextMeetingPlace(String nextMeetingPlace) {
		this.nextMeetingPlace = nextMeetingPlace;
	}

	public ProtocolDetails() {
		super();
	}

	public Integer getNoVote() {
		return noVote;
	}

	public void setNoVote(Integer noVote) {
		this.noVote = noVote;
	}

	public Integer getYesVote() {
		return yesVote;
	}

	public void setYesVote(Integer yesVote) {
		this.yesVote = yesVote;
	}

	public Integer getAbstainers() {
		return abstainers;
	}

	public void setAbstainers(Integer abstainers) {
		this.abstainers = abstainers;
	}

	public String getMinuteEntry() {
		return minuteEntry;
	}

	public void setMinuteEntry(String minuteEntry) {
		this.minuteEntry = minuteEntry;
	}

}
