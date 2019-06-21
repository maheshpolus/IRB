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
@Table(name="IRB_ATTACHMENT_SUB_CATEGORY")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class IRBAttachmentSubCategory {	
	@Id
	@Column(name="SUB_CATEGORY_CODE")
	private String subCategoryCode;
	
	@Column(name = "CATEGORY_CODE")
	private String categoryCode;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="UPDATE_TIMESTAMP") 
	private Date updateTimeStamp;
	
	@Column(name="UPDATE_USER") 
	private String updateUser;

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getSubCategoryCode() {
		return subCategoryCode;
	}

	public void setSubCategoryCode(String subCategoryCode) {
		this.subCategoryCode = subCategoryCode;
	}
}
