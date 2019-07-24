package org.mit.irb.web.schedule.dao;

public class MemberAttandance {

	private String memName;
	private String memComment;
	private String memRole;
	private String memAlternate;
	
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public String getMemComment() {
		return memComment;
	}
	public void setMemComment(String memComment) {
		this.memComment = memComment;
	}
	public String getMemRole() {
		return memRole;
	}
	public void setMemRole(String memRole) {
		this.memRole = memRole;
	}
	public String getMemAlternate() {
		return memAlternate;
	}
	public void setMemAlternate(String memAlternate) {
		this.memAlternate = memAlternate;
	}
	public MemberAttandance(String memName, String memComment, String memRole, String memAlternate) {
		super();
		this.memName = memName;
		this.memComment = memComment;
		this.memRole = memRole;
		this.memAlternate = memAlternate;
	}
}
