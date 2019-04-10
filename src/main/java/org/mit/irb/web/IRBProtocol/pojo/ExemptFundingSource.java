package org.mit.irb.web.IRBProtocol.pojo;

public class ExemptFundingSource {
	private Integer exemptFundingSourceId;
	private String fundingSource;
	private String fundingSourceTypeCode;
	private Integer irbExemptFormId;
	private String updateTimestamp;
	private String description;
	private String updateUser;
	private String fundingSourceName;
	private String acType;
	
	public Integer getExemptFundingSourceId() {
		return exemptFundingSourceId;
	}
	public void setExemptFundingSourceId(Integer exemptFundingSourceId) {
		this.exemptFundingSourceId = exemptFundingSourceId;
	}
	public String getFundingSource() {
		return fundingSource;
	}
	public void setFundingSource(String fundingSource) {
		this.fundingSource = fundingSource;
	}
	public String getFundingSourceTypeCode() {
		return fundingSourceTypeCode;
	}
	public void setFundingSourceTypeCode(String fundingSourceTypeCode) {
		this.fundingSourceTypeCode = fundingSourceTypeCode;
	}
	public Integer getIrbExemptFormId() {
		return irbExemptFormId;
	}
	public void setIrbExemptFormId(Integer irbExemptFormId) {
		this.irbExemptFormId = irbExemptFormId;
	}
	public String getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getAcType() {
		return acType;
	}
	public void setAcType(String acType) {
		this.acType = acType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFundingSourceName() {
		return fundingSourceName;
	}
	public void setFundingSourceName(String fundingSourceName) {
		this.fundingSourceName = fundingSourceName;
	}
}
