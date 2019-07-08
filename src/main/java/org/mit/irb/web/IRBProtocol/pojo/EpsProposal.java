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
@Table(name = "EPS_PROPOSAL")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class EpsProposal {
	@Id
	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;
	
	@Column(name = "SUBMIT_FLAG")
	private String submitFlag;
	
	@Column(name = "IS_HIERARCHY")
	private String isHierarchy;
	
	@Column(name = "HIERARCHY_PROPOSAL_NUMBER")
	private String hierarchyProposalNumber;
	
	@Column(name = "HIERARCHY_HASH_CODE")
	private String hierarchyHashCode;
	
	@Column(name = "HIERARCHY_BUDGET_TYPE")
	private String hierarchyBudgetType;
	
	@Column(name = "PROGRAM_ANNOUNCEMENT_NUMBER")
	private String programAnnouncementNumber;
	
	@Column(name = "PROGRAM_ANNOUNCEMENT_TITLE")
	private String programAnnouncementTitle;
	
	@Column(name = "ACTIVITY_TYPE_CODE")
	private String activityTypeCode;
	
	@Column(name = "REQUESTED_START_DATE_INITIAL")
	private Date requestStartDateInitial;
	
	@Column(name = "REQUESTED_START_DATE_TOTAL")
	private Date requestStartDateTotal;
	
	@Column(name = "REQUESTED_END_DATE_INITIAL")
	private Date requestEndDateInitial;
	
	@Column(name = "REQUESTED_END_DATE_TOTAL")
	private Date requestEndDateTotal;
	
	@Column(name = "DURATION_MONTHS")
	private Integer durationMonths;
	
	@Column(name = "NUMBER_OF_COPIES")
	private String numberOfCopies;
	
	@Column(name = "DEADLINE_DATE")
	private Date deadLineDate;
	
	@Column(name = "DEADLINE_TYPE")
	private String deadlineType;
	
	@Column(name = "MAILING_ADDRESS_ID")
	private Integer mailingAddressId;
	
	@Column(name = "MAIL_BY")
	private String mailBy;
	
	@Column(name = "MAIL_TYPE")
	private String mailType;
	
	@Column(name = "CARRIER_CODE_TYPE")
	private String carrierCodeType;
	
	@Column(name = "CARRIER_CODE")
	private String carrierCode;
	
	@Column(name = "MAIL_DESCRIPTION")
	private String mailDescription;
	
	@Column(name = "MAIL_ACCOUNT_NUMBER")
	private String mailAccountNumber;
	
	@Column(name = "SUBCONTRACT_FLAG")
	private String subcontractFlag;
	
	@Column(name = "NARRATIVE_STATUS")
	private String narrativeStatus;
	
	@Column(name = "BUDGET_STATUS")
	private String budgetStatus;
	
	@Column(name = "OWNED_BY_UNIT")
	private String ownedByUnit;
	
	@Column(name = "CREATE_TIMESTAMP")
	private Date createTimestamp;
	
	@Column(name = "CREATE_USER")
	private String createUser;
	
	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;
	
/*	@Column(name = "NSF_CODE")
	private String nsfCode;*/
	
	@Column(name = "PRIME_SPONSOR_CODE")
	private String primeSponsorCode;
	
	@Column(name = "CFDA_NUMBER")
	private String cfdaNumber;
	
	@Column(name = "AGENCY_PROGRAM_CODE")
	private String agencyProgramCode;
	
	@Column(name = "AGENCY_DIVISION_CODE")
	private String agencyDivisionCode;

	@Column(name = "VER_NBR")
	private Integer versionNumber;		
	
	@Column(name = "DOCUMENT_NUMBER")
	private String documentNumber;
	
	@Column(name = "PROPOSAL_TYPE_CODE")
	private String proposalTypeCode;

	@Column(name = "STATUS_CODE")
	private Integer statusCode;

	@Column(name = "CREATION_STATUS_CODE")
	private Integer creationStatusCode;
	
	@Column(name = "BASE_PROPOSAL_NUMBER")
	private String baseProposalNumber;
	
	@Column(name = "CONTINUED_FROM")
	private String continuedFrom;
	
	@Column(name = "TEMPLATE_FLAG")
	private String templateFlag;
	
	@Column(name = "ORGANIZATION_ID")
	private String organizationId;
	
	@Column(name = "PERFORMING_ORGANIZATION_ID")
	private String performingOrganizationId;
	
	@Column(name = "CURRENT_ACCOUNT_NUMBER")
	private String currentAccountNumber;
	
	@Column(name = "CURRENT_AWARD_NUMBER")
	private String currentAwardNumber;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "SPONSOR_CODE")
	private String sponsorCode;
	
	@Column(name = "SPONSOR_PROPOSAL_NUMBER")
	private String sponsorProposalNumber;
	
	@Column(name = "INTR_COOP_ACTIVITIES_FLAG")
	private String intrCoopActivitiesFlag;
	
	@Column(name = "INTR_COUNTRY_LIST")
	private String intrCountryList;
	
	@Column(name = "OTHER_AGENCY_FLAG")
	private String otherAgencyFlag;
	
	@Column(name = "NOTICE_OF_OPPORTUNITY_CODE")
	private Integer noticeofOpportunityCode;		

	@Column(name = "HIERARCHY_ORIG_CHILD_PROP_NBR")
	private String hierarchyOrigChildPropNbr;

	@Column(name = "OBJ_ID")
	private String objId;

	@Column(name = "ANTICIPATED_AWARD_TYPE_CODE")
	private Integer anticipatedAwardTypeCode;	
	
	@Column(name = "PROPOSALNUMBER_GG")
	private String proposalNumberGG;
	
	@Column(name = "OPPORTUNITYID_GG")
	private String opportunityidGG;
	
	@Column(name = "DEADLINE_TIME")
	private String deadlineTime;
	
	@Column(name = "AGENCY_ROUTING_IDENTIFIER")
	private String agencyRoutingIdentifier;
	
	@Column(name = "PREV_GG_TRACKID")
	private String prevGGTrackid;
	
	@Column(name = "FINAL_BUDGET_ID")
	private Integer finalBudgetId;
	
	@Column(name = "HIERARCHY_LAST_BUDGET_ID")
	private Integer hierarchyLastBudgetId;

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public String getSubmitFlag() {
		return submitFlag;
	}

	public void setSubmitFlag(String submitFlag) {
		this.submitFlag = submitFlag;
	}

	public String getIsHierarchy() {
		return isHierarchy;
	}

	public void setIsHierarchy(String isHierarchy) {
		this.isHierarchy = isHierarchy;
	}

	public String getHierarchyProposalNumber() {
		return hierarchyProposalNumber;
	}

	public void setHierarchyProposalNumber(String hierarchyProposalNumber) {
		this.hierarchyProposalNumber = hierarchyProposalNumber;
	}

	public String getHierarchyHashCode() {
		return hierarchyHashCode;
	}

	public void setHierarchyHashCode(String hierarchyHashCode) {
		this.hierarchyHashCode = hierarchyHashCode;
	}

	public String getHierarchyBudgetType() {
		return hierarchyBudgetType;
	}

	public void setHierarchyBudgetType(String hierarchyBudgetType) {
		this.hierarchyBudgetType = hierarchyBudgetType;
	}

	public String getProgramAnnouncementNumber() {
		return programAnnouncementNumber;
	}

	public void setProgramAnnouncementNumber(String programAnnouncementNumber) {
		this.programAnnouncementNumber = programAnnouncementNumber;
	}

	public String getProgramAnnouncementTitle() {
		return programAnnouncementTitle;
	}

	public void setProgramAnnouncementTitle(String programAnnouncementTitle) {
		this.programAnnouncementTitle = programAnnouncementTitle;
	}

	public String getActivityTypeCode() {
		return activityTypeCode;
	}

	public void setActivityTypeCode(String activityTypeCode) {
		this.activityTypeCode = activityTypeCode;
	}

	public Date getRequestStartDateInitial() {
		return requestStartDateInitial;
	}

	public void setRequestStartDateInitial(Date requestStartDateInitial) {
		this.requestStartDateInitial = requestStartDateInitial;
	}

	public Date getRequestStartDateTotal() {
		return requestStartDateTotal;
	}

	public void setRequestStartDateTotal(Date requestStartDateTotal) {
		this.requestStartDateTotal = requestStartDateTotal;
	}

	public Date getRequestEndDateInitial() {
		return requestEndDateInitial;
	}

	public void setRequestEndDateInitial(Date requestEndDateInitial) {
		this.requestEndDateInitial = requestEndDateInitial;
	}

	public Date getRequestEndDateTotal() {
		return requestEndDateTotal;
	}

	public void setRequestEndDateTotal(Date requestEndDateTotal) {
		this.requestEndDateTotal = requestEndDateTotal;
	}

	public Integer getDurationMonths() {
		return durationMonths;
	}

	public void setDurationMonths(Integer durationMonths) {
		this.durationMonths = durationMonths;
	}

	public String getNumberOfCopies() {
		return numberOfCopies;
	}

	public void setNumberOfCopies(String numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}

	public Date getDeadLineDate() {
		return deadLineDate;
	}

	public void setDeadLineDate(Date deadLineDate) {
		this.deadLineDate = deadLineDate;
	}

	public String getDeadlineType() {
		return deadlineType;
	}

	public void setDeadlineType(String deadlineType) {
		this.deadlineType = deadlineType;
	}

	public Integer getMailingAddressId() {
		return mailingAddressId;
	}

	public void setMailingAddressId(Integer mailingAddressId) {
		this.mailingAddressId = mailingAddressId;
	}

	public String getMailBy() {
		return mailBy;
	}

	public void setMailBy(String mailBy) {
		this.mailBy = mailBy;
	}

	public String getMailType() {
		return mailType;
	}

	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	public String getCarrierCodeType() {
		return carrierCodeType;
	}

	public void setCarrierCodeType(String carrierCodeType) {
		this.carrierCodeType = carrierCodeType;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getMailDescription() {
		return mailDescription;
	}

	public void setMailDescription(String mailDescription) {
		this.mailDescription = mailDescription;
	}

	public String getMailAccountNumber() {
		return mailAccountNumber;
	}

	public void setMailAccountNumber(String mailAccountNumber) {
		this.mailAccountNumber = mailAccountNumber;
	}

	public String getSubcontractFlag() {
		return subcontractFlag;
	}

	public void setSubcontractFlag(String subcontractFlag) {
		this.subcontractFlag = subcontractFlag;
	}

	public String getNarrativeStatus() {
		return narrativeStatus;
	}

	public void setNarrativeStatus(String narrativeStatus) {
		this.narrativeStatus = narrativeStatus;
	}

	public String getBudgetStatus() {
		return budgetStatus;
	}

	public void setBudgetStatus(String budgetStatus) {
		this.budgetStatus = budgetStatus;
	}

	public String getOwnedByUnit() {
		return ownedByUnit;
	}

	public void setOwnedByUnit(String ownedByUnit) {
		this.ownedByUnit = ownedByUnit;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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

/*	public String getNsfCode() {
		return nsfCode;
	}

	public void setNsfCode(String nsfCode) {
		this.nsfCode = nsfCode;
	}*/

	public String getPrimeSponsorCode() {
		return primeSponsorCode;
	}

	public void setPrimeSponsorCode(String primeSponsorCode) {
		this.primeSponsorCode = primeSponsorCode;
	}

	public String getCfdaNumber() {
		return cfdaNumber;
	}

	public void setCfdaNumber(String cfdaNumber) {
		this.cfdaNumber = cfdaNumber;
	}

	public String getAgencyProgramCode() {
		return agencyProgramCode;
	}

	public void setAgencyProgramCode(String agencyProgramCode) {
		this.agencyProgramCode = agencyProgramCode;
	}

	public String getAgencyDivisionCode() {
		return agencyDivisionCode;
	}

	public void setAgencyDivisionCode(String agencyDivisionCode) {
		this.agencyDivisionCode = agencyDivisionCode;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getProposalTypeCode() {
		return proposalTypeCode;
	}

	public void setProposalTypeCode(String proposalTypeCode) {
		this.proposalTypeCode = proposalTypeCode;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Integer getCreationStatusCode() {
		return creationStatusCode;
	}

	public void setCreationStatusCode(Integer creationStatusCode) {
		this.creationStatusCode = creationStatusCode;
	}

	public String getBaseProposalNumber() {
		return baseProposalNumber;
	}

	public void setBaseProposalNumber(String baseProposalNumber) {
		this.baseProposalNumber = baseProposalNumber;
	}

	public String getContinuedFrom() {
		return continuedFrom;
	}

	public void setContinuedFrom(String continuedFrom) {
		this.continuedFrom = continuedFrom;
	}

	public String getTemplateFlag() {
		return templateFlag;
	}

	public void setTemplateFlag(String templateFlag) {
		this.templateFlag = templateFlag;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getPerformingOrganizationId() {
		return performingOrganizationId;
	}

	public void setPerformingOrganizationId(String performingOrganizationId) {
		this.performingOrganizationId = performingOrganizationId;
	}

	public String getCurrentAccountNumber() {
		return currentAccountNumber;
	}

	public void setCurrentAccountNumber(String currentAccountNumber) {
		this.currentAccountNumber = currentAccountNumber;
	}

	public String getCurrentAwardNumber() {
		return currentAwardNumber;
	}

	public void setCurrentAwardNumber(String currentAwardNumber) {
		this.currentAwardNumber = currentAwardNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSponsorCode() {
		return sponsorCode;
	}

	public void setSponsorCode(String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}

	public String getSponsorProposalNumber() {
		return sponsorProposalNumber;
	}

	public void setSponsorProposalNumber(String sponsorProposalNumber) {
		this.sponsorProposalNumber = sponsorProposalNumber;
	}

	public String getIntrCoopActivitiesFlag() {
		return intrCoopActivitiesFlag;
	}

	public void setIntrCoopActivitiesFlag(String intrCoopActivitiesFlag) {
		this.intrCoopActivitiesFlag = intrCoopActivitiesFlag;
	}

	public String getIntrCountryList() {
		return intrCountryList;
	}

	public void setIntrCountryList(String intrCountryList) {
		this.intrCountryList = intrCountryList;
	}

	public String getOtherAgencyFlag() {
		return otherAgencyFlag;
	}

	public void setOtherAgencyFlag(String otherAgencyFlag) {
		this.otherAgencyFlag = otherAgencyFlag;
	}

	public Integer getNoticeofOpportunityCode() {
		return noticeofOpportunityCode;
	}

	public void setNoticeofOpportunityCode(Integer noticeofOpportunityCode) {
		this.noticeofOpportunityCode = noticeofOpportunityCode;
	}

	public String getHierarchyOrigChildPropNbr() {
		return hierarchyOrigChildPropNbr;
	}

	public void setHierarchyOrigChildPropNbr(String hierarchyOrigChildPropNbr) {
		this.hierarchyOrigChildPropNbr = hierarchyOrigChildPropNbr;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public Integer getAnticipatedAwardTypeCode() {
		return anticipatedAwardTypeCode;
	}

	public void setAnticipatedAwardTypeCode(Integer anticipatedAwardTypeCode) {
		this.anticipatedAwardTypeCode = anticipatedAwardTypeCode;
	}

	public String getProposalNumberGG() {
		return proposalNumberGG;
	}

	public void setProposalNumberGG(String proposalNumberGG) {
		this.proposalNumberGG = proposalNumberGG;
	}

	public String getOpportunityidGG() {
		return opportunityidGG;
	}

	public void setOpportunityidGG(String opportunityidGG) {
		this.opportunityidGG = opportunityidGG;
	}

	public String getDeadlineTime() {
		return deadlineTime;
	}

	public void setDeadlineTime(String deadlineTime) {
		this.deadlineTime = deadlineTime;
	}

	public String getAgencyRoutingIdentifier() {
		return agencyRoutingIdentifier;
	}

	public void setAgencyRoutingIdentifier(String agencyRoutingIdentifier) {
		this.agencyRoutingIdentifier = agencyRoutingIdentifier;
	}

	public String getPrevGGTrackid() {
		return prevGGTrackid;
	}

	public void setPrevGGTrackid(String prevGGTrackid) {
		this.prevGGTrackid = prevGGTrackid;
	}

	public Integer getFinalBudgetId() {
		return finalBudgetId;
	}

	public void setFinalBudgetId(Integer finalBudgetId) {
		this.finalBudgetId = finalBudgetId;
	}

	public Integer getHierarchyLastBudgetId() {
		return hierarchyLastBudgetId;
	}

	public void setHierarchyLastBudgetId(Integer hierarchyLastBudgetId) {
		this.hierarchyLastBudgetId = hierarchyLastBudgetId;
	}					
}
