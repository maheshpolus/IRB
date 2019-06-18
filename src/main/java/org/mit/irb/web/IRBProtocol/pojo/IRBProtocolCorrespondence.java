package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "IRB_PROTOCOL_CORRESPONDENCE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class IRBProtocolCorrespondence {
	
	@Id
	@Column(name = "PROTOCOL_CORRESPONDENCE_ID")
	private Integer protocolCorrespondenceId;
	
	@Column(name = "PROTOCOL_ID")
	private Integer protocolId;
	
	@Column(name="PROTOCOL_NUMBER")
	private String protocolNumber;
	
	@Column(name="SEQUENCE_NUMBER")
	private Integer sequenceNumber;
	
	@Column(name="PROTOCOL_ACTION_ID")
	private Integer protocolActionId;
	
	@Column(name="PROTO_CORRESP_TYPE_CODE")
	private String protoCorrespTypeCode;
	
	@Column(name="FINAL_FLAG")
	private String finalFlag;
	
	@JsonIgnore
	@Basic(fetch=FetchType.LAZY)
	@Column(name="CORRESPONDENCE")
	private byte[] fileData;

	@Column(name="CREATE_TIMESTAMP")
	private Date createTimeStamp;
	
	@Column(name="CREATE_USER")
	private String createUser;
	
	@Column(name="UPDATE_TIMESTAMP")
	private Date updateTimeStamp;
	
	@Column(name="UPDATE_USER")
	private String updateUser;
	
	@Column(name="FINAL_FLAG_TIMESTAMP")
	private Date finalFlagTimeStamp;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="MIME_TYPE")
	private String mimeType;

	public Integer getProtocolCorrespondenceId() {
		return protocolCorrespondenceId;
	}

	public void setProtocolCorrespondenceId(Integer protocolCorrespondenceId) {
		this.protocolCorrespondenceId = protocolCorrespondenceId;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Integer getProtocolActionId() {
		return protocolActionId;
	}

	public void setProtocolActionId(Integer protocolActionId) {
		this.protocolActionId = protocolActionId;
	}

	public String getProtoCorrespTypeCode() {
		return protoCorrespTypeCode;
	}

	public void setProtoCorrespTypeCode(String protoCorrespTypeCode) {
		this.protoCorrespTypeCode = protoCorrespTypeCode;
	}

	public String getFinalFlag() {
		return finalFlag;
	}

	public void setFinalFlag(String finalFlag) {
		this.finalFlag = finalFlag;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public Date getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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

	public Date getFinalFlagTimeStamp() {
		return finalFlagTimeStamp;
	}

	public void setFinalFlagTimeStamp(Date finalFlagTimeStamp) {
		this.finalFlagTimeStamp = finalFlagTimeStamp;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
