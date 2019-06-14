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
@Table(name="IRB_RISK_LEVEL")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class RiskLevel {
	@Id
	@Column(name="RISK_LEVEL_CODE")
	private String riskLevelCode;
	
	@Column(name="VER_NBR")
	private Integer verNbr;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="OBJ_ID")
	private String objId;
	
	@Column(name="UPDATE_USER")
	private String updateUser;
	
	@Column(name="UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	public String getRiskLevelCode() {
		return riskLevelCode;
	}

	public void setRiskLevelCode(String riskLevelCode) {
		this.riskLevelCode = riskLevelCode;
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

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
}
