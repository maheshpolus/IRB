package org.mit.irb.web.correspondence.dto;

import org.mit.irb.web.common.pojo.IRBExemptForm;

public class CorrespondenceDataBus {
	
	private Integer sequnceNumber;
	private String moduleItemKey;
	private Integer moduleCode;
	private IRBExemptForm irbExemptFrom;
	
	private String outputDataFormat;
	private Integer sequenceNumber;
	private String committeAction;
	private String actionCode;
	
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
	public String getOutputDataFormat() {
		return outputDataFormat;
	}
	public void setOutputDataFormat(String outputDataFormat) {
		this.outputDataFormat = outputDataFormat;
	}
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getCommitteAction() {
		return committeAction;
	}
	public void setCommitteAction(String committeAction) {
		this.committeAction = committeAction;
	}
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
}
