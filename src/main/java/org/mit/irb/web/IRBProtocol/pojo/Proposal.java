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
@Table(name = "PROPOSAL")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Proposal {
	@Id
	@Column(name = "PROPOSAL_ID")
	private Integer ProposalId;

	@Column(name = "NSF_SEQUENCE_NUMBER")
	private Integer nsfSequenceNumber;

	@Column(name = "MAIL_DESCRIPTION")
	private String mailDescription;	

	@Column(name = "CREATE_TIMESTAMP")
	private Date createTimestamp;		

	@Column(name = "PROPOSAL_SEQUENCE_STATUS")
	private String proposalSequenceStatus;

	@Column(name = "FISCAL_MONTH")
	private String fiscal_Month;

	@Column(name = "FISCAL_YEAR")
	private String fiscalYear;

	@Column(name = "LEAD_UNIT_NUMBER")
	private String leadUnitNumber;

	@Column(name = "DEADLINE_TIME")
	private String deadlineTime;

	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;

	@Column(name = "SPONSOR_PROPOSAL_NUMBER")
	private String sponsorProposalNumber;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;		

	@Column(name = "PROPOSAL_TYPE_CODE")
	private String proposalTypeCode;

	@Column(name = "CURRENT_ACCOUNT_NUMBER")
	private String currentAccountNumber;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "SPONSOR_CODE")
	private String sponsorCode;		

	@Column(name = "ROLODEX_ID")
	private Integer rolodexId;				

	@Column(name = "NOTICE_OF_OPPORTUNITY_CODE")
	private String noticeOfOpportunityCode;									

	@Column(name = "GRAD_STUD_HEADCOUNT")
	private Integer gradStudHeadcount;

	@Column(name = "GRAD_STUD_PERSON_MONTHS")
	private Integer gradStudPersonMonths;

	@Column(name = "TYPE_OF_ACCOUNT")
	private String typeOfAccount;

	@Column(name = "ACTIVITY_TYPE_CODE")
	private String activityTypeCode;

	@Column(name = "REQUESTED_START_DATE_INITIAL")
	private Date requestedStartDateInitial;

	@Column(name = "REQUESTED_START_DATE_TOTAL")
	private Date requestedStartDateTotal;

	@Column(name = "REQUESTED_END_DATE_INITIAL")
	private Date requestedEndDateInitial;

	@Column(name = "REQUESTED_END_DATE_TOTAL")
	private Date requestedEndDateTotal;

	@Column(name = "TOTAL_DIRECT_COST_INITIAL")
	private Integer totalDirectCostInitial;

	@Column(name = "TOTAL_DIRECT_COST_TOTAL")
	private Integer totalDirectCostTotal;

	@Column(name = "TOTAL_INDIRECT_COST_INITIAL")
	private Integer totalIndirectCostInitial;

	@Column(name = "TOTAL_INDIRECT_COST_TOTAL")
	private Integer totalIndirectCostTotal;

	@Column(name = "NUMBER_OF_COPIES")
	private String numberOfCopies;		

	@Column(name = "DEADLINE_DATE")
	private Date deadlineDate;

	@Column(name = "DEADLINE_TYPE")
	private String deadlineType;	

	@Column(name = "MAIL_BY")
	private String mailBy;	

	@Column(name = "MAIL_TYPE")
	private String mailType;	

	@Column(name = "MAIL_ACCOUNT_NUMBER")
	private String mailAccountNumber;	

	@Column(name = "SUBCONTRACT_FLAG")
	private String subcontractFlag;	

	@Column(name = "COST_SHARING_INDICATOR")
	private String costSharingIndicator;	

	@Column(name = "IDC_RATE_INDICATOR")
	private String idcRateIndicator;	

	@Column(name = "SPECIAL_REVIEW_INDICATOR")
	private String specialReviewIndicator;			

	@Column(name = "STATUS_CODE")
	private Integer statusCode;		

	@Column(name = "SCIENCE_CODE_INDICATOR")
	private String scienceCodeIndicator;

/*	@Column(name = "NSF_CODE")
	private String nsfCode;*/
	
	@Column(name = "PRIME_SPONSOR_CODE")
	private String primeSponsorCode;

	@Column(name = "INITIAL_CONTRACT_ADMIN")
	private String initialContractAdmin;

	@Column(name = "IP_REVIEW_ACTIVITY_INDICATOR")
	private String ipReviewActivityIndicator;

	@Column(name = "CURRENT_AWARD_NUMBER")
	private String currentAwardNumber;

	@Column(name = "CFDA_NUMBER")
	private String cfdaNumber;

	@Column(name = "OPPORTUNITY")
	private String opportunity;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;		

	@Column(name = "UPDATE_USER")
	private String updateUser;	
	
	@Column(name = "AWARD_TYPE_CODE")
	private Integer awardTypeCode;
	
	@Column(name = "VER_NBR")
	private Integer verNbr;			
	
	@Column(name = "OBJ_ID")
	private String objId;	
	
	@Column(name = "DOCUMENT_NUMBER")
	private String documentNumber;

	public Integer getProposalId() {
		return ProposalId;
	}

	public void setProposalId(Integer proposalId) {
		ProposalId = proposalId;
	}

	public Integer getNsfSequenceNumber() {
		return nsfSequenceNumber;
	}

	public void setNsfSequenceNumber(Integer nsfSequenceNumber) {
		this.nsfSequenceNumber = nsfSequenceNumber;
	}

	public String getMailDescription() {
		return mailDescription;
	}

	public void setMailDescription(String mailDescription) {
		this.mailDescription = mailDescription;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getProposalSequenceStatus() {
		return proposalSequenceStatus;
	}

	public void setProposalSequenceStatus(String proposalSequenceStatus) {
		this.proposalSequenceStatus = proposalSequenceStatus;
	}

	public String getFiscal_Month() {
		return fiscal_Month;
	}

	public void setFiscal_Month(String fiscal_Month) {
		this.fiscal_Month = fiscal_Month;
	}

	public String getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public String getLeadUnitNumber() {
		return leadUnitNumber;
	}

	public void setLeadUnitNumber(String leadUnitNumber) {
		this.leadUnitNumber = leadUnitNumber;
	}

	public String getDeadlineTime() {
		return deadlineTime;
	}

	public void setDeadlineTime(String deadlineTime) {
		this.deadlineTime = deadlineTime;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public String getSponsorProposalNumber() {
		return sponsorProposalNumber;
	}

	public void setSponsorProposalNumber(String sponsorProposalNumber) {
		this.sponsorProposalNumber = sponsorProposalNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getProposalTypeCode() {
		return proposalTypeCode;
	}

	public void setProposalTypeCode(String proposalTypeCode) {
		this.proposalTypeCode = proposalTypeCode;
	}

	public String getCurrentAccountNumber() {
		return currentAccountNumber;
	}

	public void setCurrentAccountNumber(String currentAccountNumber) {
		this.currentAccountNumber = currentAccountNumber;
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

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}

	public String getNoticeOfOpportunityCode() {
		return noticeOfOpportunityCode;
	}

	public void setNoticeOfOpportunityCode(String noticeOfOpportunityCode) {
		this.noticeOfOpportunityCode = noticeOfOpportunityCode;
	}

	public Integer getGradStudHeadcount() {
		return gradStudHeadcount;
	}

	public void setGradStudHeadcount(Integer gradStudHeadcount) {
		this.gradStudHeadcount = gradStudHeadcount;
	}

	public Integer getGradStudPersonMonths() {
		return gradStudPersonMonths;
	}

	public void setGradStudPersonMonths(Integer gradStudPersonMonths) {
		this.gradStudPersonMonths = gradStudPersonMonths;
	}

	public String getTypeOfAccount() {
		return typeOfAccount;
	}

	public void setTypeOfAccount(String typeOfAccount) {
		this.typeOfAccount = typeOfAccount;
	}

	public String getActivityTypeCode() {
		return activityTypeCode;
	}

	public void setActivityTypeCode(String activityTypeCode) {
		this.activityTypeCode = activityTypeCode;
	}

	public Date getRequestedStartDateInitial() {
		return requestedStartDateInitial;
	}

	public void setRequestedStartDateInitial(Date requestedStartDateInitial) {
		this.requestedStartDateInitial = requestedStartDateInitial;
	}

	public Date getRequestedStartDateTotal() {
		return requestedStartDateTotal;
	}

	public void setRequestedStartDateTotal(Date requestedStartDateTotal) {
		this.requestedStartDateTotal = requestedStartDateTotal;
	}

	public Date getRequestedEndDateInitial() {
		return requestedEndDateInitial;
	}

	public void setRequestedEndDateInitial(Date requestedEndDateInitial) {
		this.requestedEndDateInitial = requestedEndDateInitial;
	}

	public Date getRequestedEndDateTotal() {
		return requestedEndDateTotal;
	}

	public void setRequestedEndDateTotal(Date requestedEndDateTotal) {
		this.requestedEndDateTotal = requestedEndDateTotal;
	}

	public Integer getTotalDirectCostInitial() {
		return totalDirectCostInitial;
	}

	public void setTotalDirectCostInitial(Integer totalDirectCostInitial) {
		this.totalDirectCostInitial = totalDirectCostInitial;
	}

	public Integer getTotalDirectCostTotal() {
		return totalDirectCostTotal;
	}

	public void setTotalDirectCostTotal(Integer totalDirectCostTotal) {
		this.totalDirectCostTotal = totalDirectCostTotal;
	}

	public Integer getTotalIndirectCostInitial() {
		return totalIndirectCostInitial;
	}

	public void setTotalIndirectCostInitial(Integer totalIndirectCostInitial) {
		this.totalIndirectCostInitial = totalIndirectCostInitial;
	}

	public Integer getTotalIndirectCostTotal() {
		return totalIndirectCostTotal;
	}

	public void setTotalIndirectCostTotal(Integer totalIndirectCostTotal) {
		this.totalIndirectCostTotal = totalIndirectCostTotal;
	}

	public String getNumberOfCopies() {
		return numberOfCopies;
	}

	public void setNumberOfCopies(String numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}

	public Date getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public String getDeadlineType() {
		return deadlineType;
	}

	public void setDeadlineType(String deadlineType) {
		this.deadlineType = deadlineType;
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

	public String getCostSharingIndicator() {
		return costSharingIndicator;
	}

	public void setCostSharingIndicator(String costSharingIndicator) {
		this.costSharingIndicator = costSharingIndicator;
	}

	public String getIdcRateIndicator() {
		return idcRateIndicator;
	}

	public void setIdcRateIndicator(String idcRateIndicator) {
		this.idcRateIndicator = idcRateIndicator;
	}

	public String getSpecialReviewIndicator() {
		return specialReviewIndicator;
	}

	public void setSpecialReviewIndicator(String specialReviewIndicator) {
		this.specialReviewIndicator = specialReviewIndicator;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getScienceCodeIndicator() {
		return scienceCodeIndicator;
	}

	public void setScienceCodeIndicator(String scienceCodeIndicator) {
		this.scienceCodeIndicator = scienceCodeIndicator;
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

	public String getInitialContractAdmin() {
		return initialContractAdmin;
	}

	public void setInitialContractAdmin(String initialContractAdmin) {
		this.initialContractAdmin = initialContractAdmin;
	}

	public String getIpReviewActivityIndicator() {
		return ipReviewActivityIndicator;
	}

	public void setIpReviewActivityIndicator(String ipReviewActivityIndicator) {
		this.ipReviewActivityIndicator = ipReviewActivityIndicator;
	}

	public String getCurrentAwardNumber() {
		return currentAwardNumber;
	}

	public void setCurrentAwardNumber(String currentAwardNumber) {
		this.currentAwardNumber = currentAwardNumber;
	}

	public String getCfdaNumber() {
		return cfdaNumber;
	}

	public void setCfdaNumber(String cfdaNumber) {
		this.cfdaNumber = cfdaNumber;
	}

	public String getOpportunity() {
		return opportunity;
	}

	public void setOpportunity(String opportunity) {
		this.opportunity = opportunity;
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

	public Integer getAwardTypeCode() {
		return awardTypeCode;
	}

	public void setAwardTypeCode(Integer awardTypeCode) {
		this.awardTypeCode = awardTypeCode;
	}

	public Integer getVerNbr() {
		return verNbr;
	}

	public void setVerNbr(Integer verNbr) {
		this.verNbr = verNbr;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}					
}
