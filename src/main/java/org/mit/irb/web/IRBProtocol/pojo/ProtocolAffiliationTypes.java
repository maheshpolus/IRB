package org.mit.irb.web.IRBProtocol.pojo;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="AFFILIATION_TYPE")
public class ProtocolAffiliationTypes implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author anujoseph
	 */

	@Id
	@Column(name = "AFFILIATION_TYPE_CODE")
	private Integer affiliationTypeCode;

	@Column(name = "ACTIVE_FLAG")
	private char activeFlag;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "OBJ_ID")
	private String objevtId;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "VER_NBR")
	private Integer versionNumber;

	public char getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(char activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Integer getAffiliationTypeCode() {
		return affiliationTypeCode;
	}

	public void setAffiliationTypeCode(Integer affiliationTypeCode) {
		this.affiliationTypeCode = affiliationTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObjevtId() {
		return objevtId;
	}

	public void setObjevtId(String objevtId) {
		this.objevtId = objevtId;
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

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

}
