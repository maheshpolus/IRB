package org.mit.irb.web.IRBProtocol.pojo;

import java.io.Serializable;
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
@Table(name = "ATTACHMENT_FILE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolAttachments implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FILE_ID")
	private Integer fileId;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "FILE_NAME")
	private String fileName;
	
	@JsonIgnore
	@Basic(fetch=FetchType.LAZY)
	@Column(name = "FILE_DATA")
	private byte[] fileData;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "CONTENT_TYPE")
	private String contentType;

	@Column(name = "VER_NBR")
	private Integer verNumbr;

	@Column(name = "OBJ_ID")
	private String objId;

	@Column(name = "FILE_DATA_ID")
	private String fileDataId;

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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

	public String getFileDataId() {
		return fileDataId;
	}

	public void setFileDataId(String fileDataId) {
		this.fileDataId = fileDataId;
	}
}
