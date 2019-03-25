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
@Table(name = "AWARD")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Award {
	
	@Id
	@Column(name = "AWARD_ID")
	private Integer awardId;
	
	@Column(name = "CLOSEOUT_DATE")
	private Date closeoutDate;
	
	@Column(name = "TRANSACTION_TYPE_CODE")
	private String transactionTypeCode;

	@Column(name = "NOTICE_DATE")
	private Date noticeDate;

	@Column(name = "LEAD_UNIT_NUMBER")
	private String leadUnitNumber;	

	@Column(name = "ACTIVITY_TYPE_CODE")
	private Integer activityTypeCode;

	@Column(name = "AWARD_TYPE_CODE")
	private Integer awardTypeCode;

	@Column(name = "PRIME_SPONSOR_CODE")
	private String primeSponsorCode;

	@Column(name = "CFDA_NUMBER")
	private String cfdaNumber;

	@Column(name = "METHOD_OF_PAYMENT_CODE")
	private String methodOfPaymentCode;

	@Column(name = "DFAFS_NUMBER")
	private String dfafsNumber;

	@Column(name = "PRE_AWARD_AUTHORIZED_AMOUNT")
	private Integer preAwardAuthorizedAmount;
	
	@Column(name = "PRE_AWARD_EFFECTIVE_DATE")
	private Date preAwardEffectiveDate;

	@Column(name = "PROCUREMENT_PRIORITY_CODE")
	private String procurmentPriorityCode;

	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;

	@Column(name = "SPECIAL_EB_RATE_OFF_CAMPUS")
	private Integer specialEBRateOffCampus;

	@Column(name = "SPECIAL_EB_RATE_ON_CAMPUS")
	private Integer specialEBRateOnCampus;

	@Column(name = "SUB_PLAN_FLAG")
	private String subPlanFlag;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "VER_NBR")
	private Integer versionNumber;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "PRE_AWARD_IN_AUTHORIZED_AMOUNT")
	private Integer preAwardInAuthorizedAmount;

	@Column(name = "ARCHIVE_LOCATION")
	private String archiveLocation;

	@Column(name = "PRE_AWARD_INST_EFFECTIVE_DATE")
	private Date preAwardInstEffectiveDate;

	@Column(name = "BASIS_OF_PAYMENT_CODE")
	private String basicofPaymentCode;

	@Column(name = "DOCUMENT_NUMBER")
	private String documentNumber;

	@Column(name = "AWARD_NUMBER")
	private String awardNumber;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;	

	@Column(name = "SPONSOR_CODE")
	private String sponsorCode;

	@Column(name = "STATUS_CODE")
	private Integer statusCode;

	@Column(name = "TEMPLATE_CODE")
	private Integer templateCode;

	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name = "APPRVD_EQUIPMENT_INDICATOR")
	private String approvedEquipmentIndicator;

	@Column(name = "APPRVD_FOREIGN_TRIP_INDICATOR")
	private String approvedForeignTripIndicator;

	@Column(name = "APPRVD_SUBCONTRACT_INDICATOR")
	private String approvedSubContractIndicator;
	
	@Column(name = "AWARD_EFFECTIVE_DATE")
	private Date awardEffectiveDate;
	
	@Column(name = "AWARD_EXECUTION_DATE")
	private Date awardExecutionDate;
	
	@Column(name = "BEGIN_DATE")
	private Date beginDate;

	@Column(name = "COST_SHARING_INDICATOR")
	private String costSharingIndicator;
	
	@Column(name = "IDC_INDICATOR")
	private String idcIndication;
	
	@Column(name = "MODIFICATION_NUMBER")
	private String modificationNumber;

	@Column(name = "NSF_CODE")
	private String nsfCode;

	@Column(name = "PAYMENT_SCHEDULE_INDICATOR")
	private String paymentScheduleIndicator;

	@Column(name = "SCIENCE_CODE_INDICATOR")
	private String scienceCodeIndicator;

	@Column(name = "SPECIAL_REVIEW_INDICATOR")
	private String specialReviewIndicator;	

	@Column(name = "SPONSOR_AWARD_NUMBER")
	private String sponsorAwardNumber;

	@Column(name = "TRANSFER_SPONSOR_INDICATOR")
	private String transferSponsorIndicator;

	@Column(name = "ACCOUNT_TYPE_CODE")
	private Integer accountTypeCode;	

	@Column(name = "OBJ_ID")
	private String objectId;

	@Column(name = "FIN_ACCOUNT_DOC_NBR")
	private String finAccountDocNbr;
	
	@Column(name = "FIN_ACCOUNT_CREATION_DATE")
	private Date finAccountCreationDate;

	@Column(name = "HIERARCHY_SYNC_CHILD")
	private String hierarchySyncChild;	

	@Column(name = "FIN_CHART_OF_ACCOUNTS_CODE")
	private String finChartofAccountCode;

	@Column(name = "AWARD_SEQUENCE_STATUS")
	private String awardSequenceStatus;

	@Column(name = "FAIN_ID")
	private String fainId;	

	@Column(name = "FED_AWARD_YEAR")
	private Integer fedAwardYear;
	
	@Column(name = "FED_AWARD_DATE")
	private Date fedAwardDate;

	public Date getCloseoutDate() {
		return closeoutDate;
	}

	public void setCloseoutDate(Date closeoutDate) {
		this.closeoutDate = closeoutDate;
	}

	public String getTransactionTypeCode() {
		return transactionTypeCode;
	}

	public void setTransactionTypeCode(String transactionTypeCode) {
		this.transactionTypeCode = transactionTypeCode;
	}

	public Date getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}

	public String getLeadUnitNumber() {
		return leadUnitNumber;
	}

	public void setLeadUnitNumber(String leadUnitNumber) {
		this.leadUnitNumber = leadUnitNumber;
	}

	public Integer getActivityTypeCode() {
		return activityTypeCode;
	}

	public void setActivityTypeCode(Integer activityTypeCode) {
		this.activityTypeCode = activityTypeCode;
	}

	public Integer getAwardTypeCode() {
		return awardTypeCode;
	}

	public void setAwardTypeCode(Integer awardTypeCode) {
		this.awardTypeCode = awardTypeCode;
	}

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

	public String getMethodOfPaymentCode() {
		return methodOfPaymentCode;
	}

	public void setMethodOfPaymentCode(String methodOfPaymentCode) {
		this.methodOfPaymentCode = methodOfPaymentCode;
	}

	public String getDfafsNumber() {
		return dfafsNumber;
	}

	public void setDfafsNumber(String dfafsNumber) {
		this.dfafsNumber = dfafsNumber;
	}

	public Integer getPreAwardAuthorizedAmount() {
		return preAwardAuthorizedAmount;
	}

	public void setPreAwardAuthorizedAmount(Integer preAwardAuthorizedAmount) {
		this.preAwardAuthorizedAmount = preAwardAuthorizedAmount;
	}

	public Date getPreAwardEffectiveDate() {
		return preAwardEffectiveDate;
	}

	public void setPreAwardEffectiveDate(Date preAwardEffectiveDate) {
		this.preAwardEffectiveDate = preAwardEffectiveDate;
	}

	public String getProcurmentPriorityCode() {
		return procurmentPriorityCode;
	}

	public void setProcurmentPriorityCode(String procurmentPriorityCode) {
		this.procurmentPriorityCode = procurmentPriorityCode;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public Integer getSpecialEBRateOffCampus() {
		return specialEBRateOffCampus;
	}

	public void setSpecialEBRateOffCampus(Integer specialEBRateOffCampus) {
		this.specialEBRateOffCampus = specialEBRateOffCampus;
	}

	public Integer getSpecialEBRateOnCampus() {
		return specialEBRateOnCampus;
	}

	public void setSpecialEBRateOnCampus(Integer specialEBRateOnCampus) {
		this.specialEBRateOnCampus = specialEBRateOnCampus;
	}

	public String getSubPlanFlag() {
		return subPlanFlag;
	}

	public void setSubPlanFlag(String subPlanFlag) {
		this.subPlanFlag = subPlanFlag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
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

	public Integer getPreAwardInAuthorizedAmount() {
		return preAwardInAuthorizedAmount;
	}

	public void setPreAwardInAuthorizedAmount(Integer preAwardInAuthorizedAmount) {
		this.preAwardInAuthorizedAmount = preAwardInAuthorizedAmount;
	}

	public String getArchiveLocation() {
		return archiveLocation;
	}

	public void setArchiveLocation(String archiveLocation) {
		this.archiveLocation = archiveLocation;
	}

	public Date getPreAwardInstEffectiveDate() {
		return preAwardInstEffectiveDate;
	}

	public void setPreAwardInstEffectiveDate(Date preAwardInstEffectiveDate) {
		this.preAwardInstEffectiveDate = preAwardInstEffectiveDate;
	}

	public String getBasicofPaymentCode() {
		return basicofPaymentCode;
	}

	public void setBasicofPaymentCode(String basicofPaymentCode) {
		this.basicofPaymentCode = basicofPaymentCode;
	}

	public Integer getAwardId() {
		return awardId;
	}

	public void setAwardId(Integer awardId) {
		this.awardId = awardId;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getAwardNumber() {
		return awardNumber;
	}

	public void setAwardNumber(String awardNumber) {
		this.awardNumber = awardNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getSponsorCode() {
		return sponsorCode;
	}

	public void setSponsorCode(String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Integer getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(Integer templateCode) {
		this.templateCode = templateCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getApprovedEquipmentIndicator() {
		return approvedEquipmentIndicator;
	}

	public void setApprovedEquipmentIndicator(String approvedEquipmentIndicator) {
		this.approvedEquipmentIndicator = approvedEquipmentIndicator;
	}

	public String getApprovedForeignTripIndicator() {
		return approvedForeignTripIndicator;
	}

	public void setApprovedForeignTripIndicator(String approvedForeignTripIndicator) {
		this.approvedForeignTripIndicator = approvedForeignTripIndicator;
	}

	public String getApprovedSubContractIndicator() {
		return approvedSubContractIndicator;
	}

	public void setApprovedSubContractIndicator(String approvedSubContractIndicator) {
		this.approvedSubContractIndicator = approvedSubContractIndicator;
	}

	public Date getAwardEffectiveDate() {
		return awardEffectiveDate;
	}

	public void setAwardEffectiveDate(Date awardEffectiveDate) {
		this.awardEffectiveDate = awardEffectiveDate;
	}

	public Date getAwardExecutionDate() {
		return awardExecutionDate;
	}

	public void setAwardExecutionDate(Date awardExecutionDate) {
		this.awardExecutionDate = awardExecutionDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public String getCostSharingIndicator() {
		return costSharingIndicator;
	}

	public void setCostSharingIndicator(String costSharingIndicator) {
		this.costSharingIndicator = costSharingIndicator;
	}

	public String getIdcIndication() {
		return idcIndication;
	}

	public void setIdcIndication(String idcIndication) {
		this.idcIndication = idcIndication;
	}

	public String getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(String modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public String getNsfCode() {
		return nsfCode;
	}

	public void setNsfCode(String nsfCode) {
		this.nsfCode = nsfCode;
	}

	public String getPaymentScheduleIndicator() {
		return paymentScheduleIndicator;
	}

	public void setPaymentScheduleIndicator(String paymentScheduleIndicator) {
		this.paymentScheduleIndicator = paymentScheduleIndicator;
	}

	public String getScienceCodeIndicator() {
		return scienceCodeIndicator;
	}

	public void setScienceCodeIndicator(String scienceCodeIndicator) {
		this.scienceCodeIndicator = scienceCodeIndicator;
	}

	public String getSpecialReviewIndicator() {
		return specialReviewIndicator;
	}

	public void setSpecialReviewIndicator(String specialReviewIndicator) {
		this.specialReviewIndicator = specialReviewIndicator;
	}

	public String getSponsorAwardNumber() {
		return sponsorAwardNumber;
	}

	public void setSponsorAwardNumber(String sponsorAwardNumber) {
		this.sponsorAwardNumber = sponsorAwardNumber;
	}

	public String getTransferSponsorIndicator() {
		return transferSponsorIndicator;
	}

	public void setTransferSponsorIndicator(String transferSponsorIndicator) {
		this.transferSponsorIndicator = transferSponsorIndicator;
	}

	public Integer getAccountTypeCode() {
		return accountTypeCode;
	}

	public void setAccountTypeCode(Integer accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getFinAccountDocNbr() {
		return finAccountDocNbr;
	}

	public void setFinAccountDocNbr(String finAccountDocNbr) {
		this.finAccountDocNbr = finAccountDocNbr;
	}

	public Date getFinAccountCreationDate() {
		return finAccountCreationDate;
	}

	public void setFinAccountCreationDate(Date finAccountCreationDate) {
		this.finAccountCreationDate = finAccountCreationDate;
	}

	public String getHierarchySyncChild() {
		return hierarchySyncChild;
	}

	public void setHierarchySyncChild(String hierarchySyncChild) {
		this.hierarchySyncChild = hierarchySyncChild;
	}

	public String getFinChartofAccountCode() {
		return finChartofAccountCode;
	}

	public void setFinChartofAccountCode(String finChartofAccountCode) {
		this.finChartofAccountCode = finChartofAccountCode;
	}

	public String getAwardSequenceStatus() {
		return awardSequenceStatus;
	}

	public void setAwardSequenceStatus(String awardSequenceStatus) {
		this.awardSequenceStatus = awardSequenceStatus;
	}

	public String getFainId() {
		return fainId;
	}

	public void setFainId(String fainId) {
		this.fainId = fainId;
	}

	public Integer getFedAwardYear() {
		return fedAwardYear;
	}

	public void setFedAwardYear(Integer fedAwardYear) {
		this.fedAwardYear = fedAwardYear;
	}

	public Date getFedAwardDate() {
		return fedAwardDate;
	}

	public void setFedAwardDate(Date fedAwardDate) {
		this.fedAwardDate = fedAwardDate;
	}
}
