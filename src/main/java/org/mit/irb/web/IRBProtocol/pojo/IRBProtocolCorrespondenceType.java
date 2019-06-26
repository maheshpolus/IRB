package org.mit.irb.web.IRBProtocol.pojo;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "IRB_PROTOCOL_CORRESP_TYPE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class IRBProtocolCorrespondenceType {

	@Id	
	@Column(name = "PROTO_CORRESP_TYPE_CODE")
	private String protoCorrespTypeCode;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "MODULE_ID")
	private String moduleId;
	
	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimeStamp;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;

	public String getProtoCorrespTypeCode() {
		return protoCorrespTypeCode;
	}

	public void setProtoCorrespTypeCode(String protoCorrespTypeCode) {
		this.protoCorrespTypeCode = protoCorrespTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public Date getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Date updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
