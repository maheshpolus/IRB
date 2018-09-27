package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ORGANIZATION")
public class CollaboratorNames {
	@Id
	@Column(name = "ORGANIZATION_ID")
	private String organizationId;
	
	@Column(name = "ORGANIZATION_NAME")
	private String organizationName;
	
	@Column(name = "CONTACT_ADDRESS_ID")
	private Integer contactAddressId;
	
	@Column(name = "ADDRESS")
	private String address;
	
	@Column(name = "CABLE_ADDRESS")
	private String cableAddress;
	
	@Column(name = "TELEX_NUMBER")
	private String telexNumber;
	
	@Column(name = "COUNTY")
	private String county;
	
	@Column(name = "CONGRESSIONAL_DISTRICT")
	private String congressionalDistrict;
	
	@Column(name = "INCORPORATED_IN")
	private String incorporatedIn;
	
	@Column(name = "INCORPORATED_DATE")
	private Date incorporatedDate;
	
	@Column(name = "NUMBER_OF_EMPLOYEES")
	private Integer numberOfEmployees;
	
	@Column(name = "IRS_TAX_EXCEMPTION")
	private String irsTaxExcemption;
	
	@Column(name = "FEDRAL_EMPLOYER_ID")
	private String fedralEmployerId;
	
	@Column(name = "MASS_TAX_EXCEMPT_NUM")
	private String massTaxExcemptNum;

	@Column(name = "AGENCY_SYMBOL")
	private String agencySymbol;
	
	@Column(name = "VENDOR_CODE")
	private String vendorCode;
	
	@Column(name = "COM_GOV_ENTITY_CODE")
	private String comGovEntityCode;
	
	@Column(name = "MASS_EMPLOYEE_CLAIM")
	private String massEmployeeClaim;
	
	@Column(name = "DUNS_NUMBER")
	private String dunsNumber;
	
	@Column(name = "DUNS_PLUS_FOUR_NUMBER")
	private String dunsPlusFourNumber;
	
	@Column(name = "DODAC_NUMBER")
	private String dodacNumber;
	
	@Column(name = "CAGE_NUMBER")
	private String cageNumber;
	
	@Column(name = "HUMAN_SUB_ASSURANCE")
	private String humanSubAssurance;
	
	@Column(name = "ANIMAL_WELFARE_ASSURANCE")
	private String animalWelfareAssurance;

	@Column(name = "SCIENCE_MISCONDUCT_COMPL_DATE")
	private Date scienceMisconductComplDate;
	
	@Column(name = "PHS_ACOUNT")
	private String phsAcount;
	
	@Column(name = "NSF_INSTITUTIONAL_CODE")
	private String nsfInstitutionalCode;
	
	@Column(name = "INDIRECT_COST_RATE_AGREEMENT")
	private String indirectCostRateAgreement;

	@Column(name = "COGNIZANT_AUDITOR")
	private Integer cognizantAuditor;
	
	@Column(name = "ONR_RESIDENT_REP")
	private Integer onrResidentRep;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;
	
	@Column(name = "VER_NBR")
	private Integer verNumber;
	
	@Column(name = "OBJ_ID")
	private String objId;

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Integer getContactAddressId() {
		return contactAddressId;
	}

	public void setContactAddressId(Integer contactAddressId) {
		this.contactAddressId = contactAddressId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCableAddress() {
		return cableAddress;
	}

	public void setCableAddress(String cableAddress) {
		this.cableAddress = cableAddress;
	}

	public String getTelexNumber() {
		return telexNumber;
	}

	public void setTelexNumber(String telexNumber) {
		this.telexNumber = telexNumber;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCongressionalDistrict() {
		return congressionalDistrict;
	}

	public void setCongressionalDistrict(String congressionalDistrict) {
		this.congressionalDistrict = congressionalDistrict;
	}

	public String getIncorporatedIn() {
		return incorporatedIn;
	}

	public void setIncorporatedIn(String incorporatedIn) {
		this.incorporatedIn = incorporatedIn;
	}

	public Date getIncorporatedDate() {
		return incorporatedDate;
	}

	public void setIncorporatedDate(Date incorporatedDate) {
		this.incorporatedDate = incorporatedDate;
	}

	public Integer getNumberOfEmployees() {
		return numberOfEmployees;
	}

	public void setNumberOfEmployees(Integer numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}

	public String getIrsTaxExcemption() {
		return irsTaxExcemption;
	}

	public void setIrsTaxExcemption(String irsTaxExcemption) {
		this.irsTaxExcemption = irsTaxExcemption;
	}

	public String getFedralEmployerId() {
		return fedralEmployerId;
	}

	public void setFedralEmployerId(String fedralEmployerId) {
		this.fedralEmployerId = fedralEmployerId;
	}

	public String getMassTaxExcemptNum() {
		return massTaxExcemptNum;
	}

	public void setMassTaxExcemptNum(String massTaxExcemptNum) {
		this.massTaxExcemptNum = massTaxExcemptNum;
	}

	public String getAgencySymbol() {
		return agencySymbol;
	}

	public void setAgencySymbol(String agencySymbol) {
		this.agencySymbol = agencySymbol;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getComGovEntityCode() {
		return comGovEntityCode;
	}

	public void setComGovEntityCode(String comGovEntityCode) {
		this.comGovEntityCode = comGovEntityCode;
	}

	public String getMassEmployeeClaim() {
		return massEmployeeClaim;
	}

	public void setMassEmployeeClaim(String massEmployeeClaim) {
		this.massEmployeeClaim = massEmployeeClaim;
	}

	public String getDunsNumber() {
		return dunsNumber;
	}

	public void setDunsNumber(String dunsNumber) {
		this.dunsNumber = dunsNumber;
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

	public String getHumanSubAssurance() {
		return humanSubAssurance;
	}

	public void setHumanSubAssurance(String humanSubAssurance) {
		this.humanSubAssurance = humanSubAssurance;
	}

	public String getAnimalWelfareAssurance() {
		return animalWelfareAssurance;
	}

	public void setAnimalWelfareAssurance(String animalWelfareAssurance) {
		this.animalWelfareAssurance = animalWelfareAssurance;
	}

	public Date getScienceMisconductComplDate() {
		return scienceMisconductComplDate;
	}

	public void setScienceMisconductComplDate(Date scienceMisconductComplDate) {
		this.scienceMisconductComplDate = scienceMisconductComplDate;
	}

	public String getPhsAcount() {
		return phsAcount;
	}

	public void setPhsAcount(String phsAcount) {
		this.phsAcount = phsAcount;
	}

	public String getNsfInstitutionalCode() {
		return nsfInstitutionalCode;
	}

	public void setNsfInstitutionalCode(String nsfInstitutionalCode) {
		this.nsfInstitutionalCode = nsfInstitutionalCode;
	}

	public String getIndirectCostRateAgreement() {
		return indirectCostRateAgreement;
	}

	public void setIndirectCostRateAgreement(String indirectCostRateAgreement) {
		this.indirectCostRateAgreement = indirectCostRateAgreement;
	}

	public Integer getCognizantAuditor() {
		return cognizantAuditor;
	}

	public void setCognizantAuditor(Integer cognizantAuditor) {
		this.cognizantAuditor = cognizantAuditor;
	}

	public Integer getOnrResidentRep() {
		return onrResidentRep;
	}

	public void setOnrResidentRep(Integer onrResidentRep) {
		this.onrResidentRep = onrResidentRep;
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

	public Integer getVerNumber() {
		return verNumber;
	}

	public void setVerNumber(Integer verNumber) {
		this.verNumber = verNumber;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}
}
