package org.mit.irb.web.IRBProtocol.pojo;

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
@Table(name = "IRB_FILE_DATA")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class IRBFileData {
	@Id
	@Column(name = "ID")
	private Integer fileId;
	
	@JsonIgnore
	@Basic(fetch=FetchType.LAZY)
	@Column(name = "DATA")
	private byte[] fileData;

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	
}
