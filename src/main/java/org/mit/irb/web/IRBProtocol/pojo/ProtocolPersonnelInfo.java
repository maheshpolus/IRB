package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Clob;
import java.sql.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/*
 * Class to fetch personnel information of IRB protocol persons 
 * @author anu
*/

@Entity
@Table(name = "IRB_PROTOCOL_PERSONS")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolPersonnelInfo {
	@Id
	@GenericGenerator(name = "ProtocolPersonnelInfoIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProtocolPersonnelInfoIdGenerator")
	@Column(name = "PROTOCOL_PERSON_ID")
	private Integer protocolPersonId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTOCOL_PERSONS_FK1"), name = "PROTOCOL_ID", referencedColumnName = "PROTOCOL_ID")
	private ProtocolGeneralInfo protocolGeneralInfo;

	@Column(name = "AFFILIATION_TYPE_CODE")
	private Integer affiliationTypeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTOCOL_PERSONS_FK3"), name = "AFFILIATION_TYPE_CODE", referencedColumnName = "AFFILIATION_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolAffiliationTypes protocolAffiliationTypes;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_ROLE_FK1"), name = "PROTOCOL_PERSON_ROLE_ID", referencedColumnName = "PROTOCOL_PERSON_ROLE_ID", insertable = false, updatable = false)
	private ProtocolPersonRoleTypes protocolPersonRoleTypes;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "personnelInfo", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProtocolLeadUnits> protocolLeadUnits;

	@Transient
	private String acType;

	@Transient
	@Column(name = "IS_GRADUATE_STUDENT_STAFF")
	private char isGraduateStudentStaff;

	@Transient
	@Column(name = "IS_ON_SABBATICAL")
	private char isOnSabbatical;

	@Transient
	@Column(name = "VACATION_ACCURAL")
	private char vacationAccural;

	@Transient
	@Column(name = "IS_MEDICAL_STAFF")
	private char isMedicalStaff;

	@Transient
	@Column(name = "IS_OTHER_ACCADEMIC_GROUP")
	private char isOtherAccademicGroup;

	@Transient
	@Column(name = "IS_HANDICAPPED")
	private char isHandicapped;

	@Transient
	@Column(name = "IS_VETERAN")
	private char isVeteran;

	@Transient
	@Column(name = "IS_FACULTY")
	private char isFaculty;

	@Transient
	@Column(name = "IS_RESEARCH_STAFF")
	private char isResearchStaff;

	@Transient
	@Column(name = "IS_SERVICE_STAFF")
	private char isServiceStaff;

	@Transient
	@Column(name = "IS_SUPPORT_STAFF")
	private char isSupportStaff;

	@Transient
	@Column(name = "COUNTRY_CODE")
	private char countryCode;

	@Column(name = "COMMENTS")
	private Clob comments;

	@Column(name = "VISA_RENEWAL_DATE")
	private Date visaRenewalDate;

	@Column(name = "DATE_OF_BIRTH")
	private Date dateOfBirth;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "AGE")
	private Integer age;

	@Column(name = "AGE_BY_FISCAL_YEAR")
	private Integer ageByFiscalYear;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "DEGREE")
	private String degree;

	@Column(name = "PROTOCOL_PERSON_ROLE_ID")
	private String protocolPersonRoleId;

	@Column(name = "POSTAL_CODE")
	private String postalCode;

	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Column(name = "VISA_CODE")
	private String visaCode;

	@Column(name = "MOBILE_PHONE_NUMBER")
	private String mobilePhoneNumber;

	@Column(name = "PAGER_NUMBER")
	private String pagerNumber;

	@Column(name = "FAX_NUMBER")
	private String faxNumber;

	@Column(name = "SECONDRY_OFFICE_PHONE")
	private String secondryOfficePhone;

	@Column(name = "OFFICE_PHONE")
	private String officePhone;

	@Column(name = "ERA_COMMONS_USER_NAME")
	private String eraCommonsUserName;

	@Column(name = "NON_EMPLOYEE_FLAG")
	private String nonEmployeeFlag;

	@Column(name = "DIRECTORY_DEPARTMENT")
	private String directoryDepartment;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "PRIOR_NAME")
	private String priorName;

	@Column(name = "CITY")
	private String city;

	@Column(name = "ID_VERIFIED")
	private String idVerified;

	@Column(name = "ID_PROVIDED")
	private String idProvided;

	@Column(name = "COUNTRY_OF_CITIZENSHIP")
	private String countryOfCitizenship;

	@Column(name = "SALUTATION")
	private String salutation;

	@Column(name = "YEAR_GRADUATED")
	private String yearGraduated;

	@Column(name = "SECONDRY_OFFICE_LOCATION")
	private String secondryOfficeLocation;

	@Column(name = "STATE")
	private String state;

	@Column(name = "OFFICE_LOCATION")
	private String officeLocation;

	@Column(name = "VISA_TYPE")
	private String visaType;

	@Column(name = "GENDER")
	private String gender;

	@Column(name = "RACE")
	private String race;

	@Column(name = "EDUCATION_LEVEL")
	private String educationLevel;

	@Column(name = "MAJOR")
	private String major;

	@Column(name = "HANDICAP_TYPE")
	private String handicapType;

	@Column(name = "VETERAN_TYPE")
	private String veteranType;

	@Column(name = "COUNTY")
	private String county;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "SCHOOL")
	private String school;

	@Column(name = "DIRECTORY_TITLE")
	private String directoryTitle;

	@Column(name = "PRIMARY_TITLE")
	private String primaryTitle;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "EMAIL_ADDRESS")
	private String emailAddress;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "HOME_UNIT")
	private String homeUnit;

	@Column(name = "ADDRESS_LINE_1")
	private String addressLine1;

	@Column(name = "ADDRESS_LINE_2")
	private String addressLine2;

	@Column(name = "ADDRESS_LINE_3")
	private String addressLine3;

	@Column(name = "SSN")
	private String ssn;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "PERSON_NAME")
	private String personName;

	public Integer getProtocolPersonId() {
		return protocolPersonId;
	}

	public void setProtocolPersonId(Integer protocolPersonId) {
		this.protocolPersonId = protocolPersonId;
	}

	public char getIsGraduateStudentStaff() {
		return isGraduateStudentStaff;
	}

	public void setIsGraduateStudentStaff(char isGraduateStudentStaff) {
		this.isGraduateStudentStaff = isGraduateStudentStaff;
	}

	public char getIsOnSabbatical() {
		return isOnSabbatical;
	}

	public void setIsOnSabbatical(char isOnSabbatical) {
		this.isOnSabbatical = isOnSabbatical;
	}

	public char getVacationAccural() {
		return vacationAccural;
	}

	public void setVacationAccural(char vacationAccural) {
		this.vacationAccural = vacationAccural;
	}

	public char getIsMedicalStaff() {
		return isMedicalStaff;
	}

	public void setIsMedicalStaff(char isMedicalStaff) {
		this.isMedicalStaff = isMedicalStaff;
	}

	public char getIsOtherAccademicGroup() {
		return isOtherAccademicGroup;
	}

	public void setIsOtherAccademicGroup(char isOtherAccademicGroup) {
		this.isOtherAccademicGroup = isOtherAccademicGroup;
	}

	public char getIsHandicapped() {
		return isHandicapped;
	}

	public void setIsHandicapped(char isHandicapped) {
		this.isHandicapped = isHandicapped;
	}

	public char getIsVeteran() {
		return isVeteran;
	}

	public void setIsVeteran(char isVeteran) {
		this.isVeteran = isVeteran;
	}

	/*
	 * public char getHasVisa() { return hasVisa; }
	 * 
	 * public void setHasVisa(char hasVisa) { this.hasVisa = hasVisa; }
	 */

	public char getIsFaculty() {
		return isFaculty;
	}

	public void setIsFaculty(char isFaculty) {
		this.isFaculty = isFaculty;
	}

	public char getIsResearchStaff() {
		return isResearchStaff;
	}

	public void setIsResearchStaff(char isResearchStaff) {
		this.isResearchStaff = isResearchStaff;
	}

	public char getIsServiceStaff() {
		return isServiceStaff;
	}

	public void setIsServiceStaff(char isServiceStaff) {
		this.isServiceStaff = isServiceStaff;
	}

	public char getIsSupportStaff() {
		return isSupportStaff;
	}

	public void setIsSupportStaff(char isSupportStaff) {
		this.isSupportStaff = isSupportStaff;
	}

	public char getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(char countryCode) {
		this.countryCode = countryCode;
	}

	public Clob getComments() {
		return comments;
	}

	public void setComments(Clob comments) {
		this.comments = comments;
	}

	public Date getVisaRenewalDate() {
		return visaRenewalDate;
	}

	public void setVisaRenewalDate(Date visaRenewalDate) {
		this.visaRenewalDate = visaRenewalDate;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getAffiliationTypeCode() {
		return affiliationTypeCode;
	}

	public void setAffiliationTypeCode(Integer affiliationTypeCode) {
		this.affiliationTypeCode = affiliationTypeCode;
	}

	public Integer getAgeByFiscalYear() {
		return ageByFiscalYear;
	}

	public void setAgeByFiscalYear(Integer ageByFiscalYear) {
		this.ageByFiscalYear = ageByFiscalYear;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getProtocolPersonRoleId() {
		return protocolPersonRoleId;
	}

	public void setProtocolPersonRoleId(String protocolPersonRoleId) {
		this.protocolPersonRoleId = protocolPersonRoleId;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getVisaCode() {
		return visaCode;
	}

	public void setVisaCode(String visaCode) {
		this.visaCode = visaCode;
	}

	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	public String getPagerNumber() {
		return pagerNumber;
	}

	public void setPagerNumber(String pagerNumber) {
		this.pagerNumber = pagerNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getSecondryOfficePhone() {
		return secondryOfficePhone;
	}

	public void setSecondryOfficePhone(String secondryOfficePhone) {
		this.secondryOfficePhone = secondryOfficePhone;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getEraCommonsUserName() {
		return eraCommonsUserName;
	}

	public void setEraCommonsUserName(String eraCommonsUserName) {
		this.eraCommonsUserName = eraCommonsUserName;
	}

	public String getDirectoryDepartment() {
		return directoryDepartment;
	}

	public void setDirectoryDepartment(String directoryDepartment) {
		this.directoryDepartment = directoryDepartment;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getPriorName() {
		return priorName;
	}

	public void setPriorName(String priorName) {
		this.priorName = priorName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIdVerified() {
		return idVerified;
	}

	public void setIdVerified(String idVerified) {
		this.idVerified = idVerified;
	}

	public String getIdProvided() {
		return idProvided;
	}

	public void setIdProvided(String idProvided) {
		this.idProvided = idProvided;
	}

	public String getCountryOfCitizenship() {
		return countryOfCitizenship;
	}

	public void setCountryOfCitizenship(String countryOfCitizenship) {
		this.countryOfCitizenship = countryOfCitizenship;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getYearGraduated() {
		return yearGraduated;
	}

	public void setYearGraduated(String yearGraduated) {
		this.yearGraduated = yearGraduated;
	}

	public String getSecondryOfficeLocation() {
		return secondryOfficeLocation;
	}

	public void setSecondryOfficeLocation(String secondryOfficeLocation) {
		this.secondryOfficeLocation = secondryOfficeLocation;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOfficeLocation() {
		return officeLocation;
	}

	public void setOfficeLocation(String officeLocation) {
		this.officeLocation = officeLocation;
	}

	public String getVisaType() {
		return visaType;
	}

	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getHandicapType() {
		return handicapType;
	}

	public void setHandicapType(String handicapType) {
		this.handicapType = handicapType;
	}

	public String getVeteranType() {
		return veteranType;
	}

	public void setVeteranType(String veteranType) {
		this.veteranType = veteranType;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getDirectoryTitle() {
		return directoryTitle;
	}

	public void setDirectoryTitle(String directoryTitle) {
		this.directoryTitle = directoryTitle;
	}

	public String getPrimaryTitle() {
		return primaryTitle;
	}

	public void setPrimaryTitle(String primaryTitle) {
		this.primaryTitle = primaryTitle;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getHomeUnit() {
		return homeUnit;
	}

	public void setHomeUnit(String homeUnit) {
		this.homeUnit = homeUnit;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public ProtocolAffiliationTypes getProtocolAffiliationTypes() {
		return protocolAffiliationTypes;
	}

	public void setProtocolAffiliationTypes(ProtocolAffiliationTypes protocolAffiliationTypes) {
		this.protocolAffiliationTypes = protocolAffiliationTypes;
	}

	public ProtocolGeneralInfo getProtocolGeneralInfo() {
		return protocolGeneralInfo;
	}

	public void setProtocolGeneralInfo(ProtocolGeneralInfo protocolGeneralInfo) {
		this.protocolGeneralInfo = protocolGeneralInfo;
	}

	public String getNonEmployeeFlag() {
		return nonEmployeeFlag;
	}

	public void setNonEmployeeFlag(String nonEmployeeFlag) {
		this.nonEmployeeFlag = nonEmployeeFlag;
	}

	public List<ProtocolLeadUnits> getProtocolLeadUnits() {
		return protocolLeadUnits;
	}

	public void setProtocolLeadUnits(List<ProtocolLeadUnits> protocolLeadUnits) {
		this.protocolLeadUnits = protocolLeadUnits;
	}

	public ProtocolPersonRoleTypes getProtocolPersonRoleTypes() {
		return protocolPersonRoleTypes;
	}

	public void setProtocolPersonRoleTypes(ProtocolPersonRoleTypes protocolPersonRoleTypes) {
		this.protocolPersonRoleTypes = protocolPersonRoleTypes;
	}
}
