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
@Table(name = "SPONSOR")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Sponsor {

	@Id
	@Column(name = "SPONSOR_CODE")
	private String sponsorCode;
	
	@Column(name = "SPONSOR_NAME")
	private String sponsorName;

	@Column(name = "ACRONYM")
	private String acronym;

	@Column(name = "SPONSOR_TYPE_CODE")
	private String sponsorTypeCode;

	@Column(name = "DUN_AND_BRADSTREET_NUMBER")
	private String dunAndBradstreetNumber;

	@Column(name = "DUNS_PLUS_FOUR_NUMBER")
	private String dunsPlusFourNumber;

	@Column(name = "DODAC_NUMBER")
	private String dodacNumber;

	@Column(name = "CAGE_NUMBER")
	private String cageNumber;	

	@Column(name = "POSTAL_CODE")
	private String postalCode;

	@Column(name = "STATE")
	private String state;

	@Column(name = "COUNTRY_CODE")
	private String countryCode;

	@Column(name = "ROLODEX_ID")
	private Integer rolodexId;

	@Column(name = "AUDIT_REPORT_SENT_FOR_FY")
	private String auditReportSentForFY;

	@Column(name = "OWNED_BY_UNIT")
	private String ownedByUnit;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "VER_NBR")
	private Integer versionNumber;

	@Column(name = "OBJ_ID")
	private String objectId;

	@Column(name = "ACTV_IND")
	private String actvInd;

	@Column(name = "DUNNING_CAMPAIGN_ID")
	private String dunningCampaignId;

	@Column(name = "CUSTOMER_NUMBER")
	private String customerNumber;

	public String getSponsorCode() {
		return sponsorCode;
	}

	public void setSponsorCode(String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getSponsorTypeCode() {
		return sponsorTypeCode;
	}

	public void setSponsorTypeCode(String sponsorTypeCode) {
		this.sponsorTypeCode = sponsorTypeCode;
	}

	public String getDunAndBradstreetNumber() {
		return dunAndBradstreetNumber;
	}

	public void setDunAndBradstreetNumber(String dunAndBradstreetNumber) {
		this.dunAndBradstreetNumber = dunAndBradstreetNumber;
	}

	public String getDunsPlusFourNumber() {
		return dunsPlusFourNumber;
	}

	public void setDunsPlusFourNumber(String dunsPlusFourNumber) {
		this.dunsPlusFourNumber = dunsPlusFourNumber;
	}

	public String getDodacNumber() {
		return dodacNumber;
	}

	public void setDodacNumber(String dodacNumber) {
		this.dodacNumber = dodacNumber;
	}

	public String getCageNumber() {
		return cageNumber;
	}

	public void setCageNumber(String cageNumber) {
		this.cageNumber = cageNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}

	public String getAuditReportSentForFY() {
		return auditReportSentForFY;
	}

	public void setAuditReportSentForFY(String auditReportSentForFY) {
		this.auditReportSentForFY = auditReportSentForFY;
	}

	public String getOwnedByUnit() {
		return ownedByUnit;
	}

	public void setOwnedByUnit(String ownedByUnit) {
		this.ownedByUnit = ownedByUnit;
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

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getActvInd() {
		return actvInd;
	}

	public void setActvInd(String actvInd) {
		this.actvInd = actvInd;
	}

	public String getDunningCampaignId() {
		return dunningCampaignId;
	}

	public void setDunningCampaignId(String dunningCampaignId) {
		this.dunningCampaignId = dunningCampaignId;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}	
}
