package org.mit.irb.web.correspondence.dto;

import org.mit.irb.web.common.pojo.IRBExemptForm;

public class CorrespondenceDataBus {
	
	private Integer sequnceNumber;
	private String moduleItemKey;
	private Integer moduleCode;
	private IRBExemptForm irbExemptFrom;
	
	public Integer getSequnceNumber() {
		return sequnceNumber;
	}
	public void setSequnceNumber(Integer sequnceNumber) {
		this.sequnceNumber = sequnceNumber;
	}
	public String getModuleItemKey() {
		return moduleItemKey;
	}
	public void setModuleItemKey(String moduleItemKey) {
		this.moduleItemKey = moduleItemKey;
	}
	public Integer getModuleCode() {
		return moduleCode;
	}
	public void setModuleCode(Integer moduleCode) {
		this.moduleCode = moduleCode;
	}
	public IRBExemptForm getIrbExemptFrom() {
		return irbExemptFrom;
	}
	public void setIrbExemptFrom(IRBExemptForm irbExemptFrom) {
		this.irbExemptFrom = irbExemptFrom;
	}
}
