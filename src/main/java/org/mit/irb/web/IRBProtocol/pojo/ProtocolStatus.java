package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="MITKC_IRB_PROTOCOL_STATUS")
public class ProtocolStatus {
	@Id
	@Column(name = "PROTOCOL_STATUS_CODE")
	private String protocolStatusCode;
	
	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProtocolStatusCode() {
		return protocolStatusCode;
	}

	public void setProtocolStatusCode(String protocolStatusCode) {
		this.protocolStatusCode = protocolStatusCode;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
