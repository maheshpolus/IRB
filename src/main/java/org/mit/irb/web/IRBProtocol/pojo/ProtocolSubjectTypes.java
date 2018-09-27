package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="VULNERABLE_SUBJECT_TYPE")
public class ProtocolSubjectTypes {
	@Id
	@Column(name="VULNERABLE_SUBJECT_TYPE_CODE")
	private String vulnerableSubjectTypeCode;
	
	@Column(name="UPDATE_TIMESTAMP")
	private Date updateTimestamp;
	
	@Column(name="VER_NBR")
	private Integer verNbr;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="OBJ_ID")
	private String objId;
	
	@Column(name="UPDATE_USER")
	private String updateUser;

	public String getVulnerableSubjectTypeCode() {
		return vulnerableSubjectTypeCode;
	}

	public void setVulnerableSubjectTypeCode(String vulnerableSubjectTypeCode) {
		this.vulnerableSubjectTypeCode = vulnerableSubjectTypeCode;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Integer getVerNbr() {
		return verNbr;
	}

	public void setVerNbr(Integer verNbr) {
		this.verNbr = verNbr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
