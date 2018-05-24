package org.mit.irb.web.common.VO;

/**
 * Value object for service arguement.
 *
 */
public class CommonVO {

	private String username;

	private String password;
	
	private String personId;
	
	private String person_role_type;
	
	private String protocol_number;
	
	private String title;
	
	private String lead_unit_number;
	
	private String protocol_type_code;
	
	private String dashboard_type;

	private String av_person_id;
	
	private String attachmentId;
	
	private String av_summary_type;
	
	public String getAv_summary_type() {
		return av_summary_type;
	}

	public void setAv_summary_type(String av_summary_type) {
		this.av_summary_type = av_summary_type;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPerson_role_type() {
		return person_role_type;
	}

	public void setPerson_role_type(String person_role_type) {
		this.person_role_type = person_role_type;
	}

	public String getProtocol_number() {
		return protocol_number;
	}

	public void setProtocol_number(String protocol_number) {
		this.protocol_number = protocol_number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLead_unit_number() {
		return lead_unit_number;
	}

	public void setLead_unit_number(String lead_unit_number) {
		this.lead_unit_number = lead_unit_number;
	}

	public String getProtocol_type_code() {
		return protocol_type_code;
	}

	public void setProtocol_type_code(String protocol_type_code) {
		this.protocol_type_code = protocol_type_code;
	}

	public String getDashboard_type() {
		return dashboard_type;
	}

	public void setDashboard_type(String dashboard_type) {
		this.dashboard_type = dashboard_type;
	}

	public String getAv_person_id() {
		return av_person_id;
	}

	public void setAv_person_id(String av_person_id) {
		this.av_person_id = av_person_id;
	}
}
