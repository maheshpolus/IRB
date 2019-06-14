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
@Table(name="IRB_FDA_RISK_LEVEL")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class FDARiskLevel {
	@Id
	@Column(name="FDA_RISK_LEVEL_CODE")
	private String fdaRiskLevelCode;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="UPDATE_USER")
	private String updateUser;
	
	@Column(name="UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	public String getFdaRiskLevelCode() {
		return fdaRiskLevelCode;
	}

	public void setFdaRiskLevelCode(String fdaRiskLevelCode) {
		this.fdaRiskLevelCode = fdaRiskLevelCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
