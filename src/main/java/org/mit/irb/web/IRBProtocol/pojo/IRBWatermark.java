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
@Table(name = "IRB_WATERMARK")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class IRBWatermark {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "WATERMARK_ID")
	private Integer watermarkId;
	
	@Column(name = "STATUS_CODE")
	private String statusCode;
	
	@Column(name = "WATERMARK_TEXT")
	private String watermarkText;
	
	@Column(name = "WATERMARK_STATUS")
	private String watermarkStatus;
	
    @Column(name = "FONT_SIZE")
    private String fontSize;
    
    @Column(name = "FONT_COLOUR")
    private String fontColour;
    
    @Column(name = "UPDATE_TIMESTAMP")
    private Date updateTimeStamp;
    
    @Column(name = "UPDATE_USER")
    private String updateUser;
    
    @Column(name = "WATERMARK_TYPE")
    private String watermarkType;
    
    @JsonIgnore
	@Basic(fetch=FetchType.LAZY)
    @Column(name = "IMAGE_FILE")
    private byte[] imageFile;
    
    @Column(name = "FILE_NAME")
    private String fileName;
    
    @Column(name = "CONTENT_TYPE")
    private String contentType;
    
    @Column(name = "POSITION")
    private String position;
    
    @Column(name = "ALIGNMENT")
    private String alignment;
    
    @Column(name = "POSITION_FONT")
    private String positionFont;
    
    @Column(name = "MODULE_CODE")
    private Integer moduleCode;

	public Integer getWatermarkId() {
		return watermarkId;
	}

	public void setWatermarkId(Integer watermarkId) {
		this.watermarkId = watermarkId;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getWatermarkText() {
		return watermarkText;
	}

	public void setWatermarkText(String watermarkText) {
		this.watermarkText = watermarkText;
	}

	public String getWatermarkStatus() {
		return watermarkStatus;
	}

	public void setWatermarkStatus(String watermarkStatus) {
		this.watermarkStatus = watermarkStatus;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontColour() {
		return fontColour;
	}

	public void setFontColour(String fontColour) {
		this.fontColour = fontColour;
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

	public String getWatermarkType() {
		return watermarkType;
	}

	public void setWatermarkType(String watermarkType) {
		this.watermarkType = watermarkType;
	}

	public byte[] getImageFile() {
		return imageFile;
	}

	public void setImageFile(byte[] imageFile) {
		this.imageFile = imageFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAlignment() {
		return alignment;
	}

	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	public String getPositionFont() {
		return positionFont;
	}

	public void setPositionFont(String positionFont) {
		this.positionFont = positionFont;
	}

	public Integer getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(Integer moduleCode) {
		this.moduleCode = moduleCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}    	    	
}
