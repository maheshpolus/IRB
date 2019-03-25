package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="IRB_PROTOCOL_TYPE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolType {
	@Id
	@Column(name = "PROTOCOL_TYPE_CODE")
	private String protocolTypeCode;
	
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

	public String getProtocolTypeCode() {
		return protocolTypeCode;
	}

	public void setProtocolTypeCode(String protocolTypeCode) {
		this.protocolTypeCode = protocolTypeCode;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
