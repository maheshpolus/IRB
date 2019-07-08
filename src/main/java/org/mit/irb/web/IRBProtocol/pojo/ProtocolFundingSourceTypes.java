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
@Table(name="FUNDING_SOURCE_TYPE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolFundingSourceTypes {
	@Id
	@Column(name="FUNDING_SOURCE_TYPE_CODE")
	private String fundingSourceTypeCode;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="UPDATE_TIMESTAMP")
	private Date updateTimestamp;
	
	@Column(name="UPDATE_USER")
	private String updateUser;
	
	@Column(name="FUNDING_SOURCE_TYPE_FLAG")
	private String fundingSourceTypeFlag;
	
	@Column(name="VER_NBR")
	private Integer verNumbr;
	
	@Column(name="OBJ_ID")
	private String objId;

	public String getFundingSourceTypeCode() {
		return fundingSourceTypeCode;
	}

	public void setFundingSourceTypeCode(String fundingSourceTypeCode) {
		this.fundingSourceTypeCode = fundingSourceTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getFundingSourceTypeFlag() {
		return fundingSourceTypeFlag;
	}

	public void setFundingSourceTypeFlag(String fundingSourceTypeFlag) {
		this.fundingSourceTypeFlag = fundingSourceTypeFlag;
	}

	public Integer getVerNumbr() {
		return verNumbr;
	}

	public void setVerNumbr(Integer verNumbr) {
		this.verNumbr = verNumbr;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}
}
