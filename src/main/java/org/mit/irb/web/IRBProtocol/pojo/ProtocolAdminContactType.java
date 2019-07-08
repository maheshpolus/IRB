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
@Table(name="IRB_ADMIN_CONTACT_TYPE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolAdminContactType {
	@Id
	@Column(name = "ADMIN_CONTACT_TYPE_CODE")
	private Integer adminContactTypeCode;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAdminContactTypeCode() {
		return adminContactTypeCode;
	}

	public void setAdminContactTypeCode(Integer adminContactTypeCode) {
		this.adminContactTypeCode = adminContactTypeCode;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
