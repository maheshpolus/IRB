package org.mit.irb.web.committee.view;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "WHOSP_PERSON_HISTORY")
public class PersonDetailsView implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "PERSON_ID")
	private String personId;
	
	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "USER_NAME")
	private String prncplName;
	
	@Column(name = "EMAIL_ADDRESS")
	private String emailAddress;

/*	@Column(name = "HOME_UNIT")
	private String primaryDeptCode;*/

/*	@Column(name = "UNIT_NAME")
	private String unitName;*/

	@Column(name = "HOME_UNIT")
	private String unitNumber;

	@Column(name = "OFFICE_PHONE")
	private String phoneNumber;

	@Column(name = "ADDRESS_LINE_1")
	private String addressLine1;

	@Column(name = "DIRECTORY_TITLE")
	private String directoryTitle;
	
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPrncplName() {
		return prncplName;
	}

	public void setPrncplName(String prncplName) {
		this.prncplName = prncplName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

/*	public String getPrimaryDeptCode() {
		return primaryDeptCode;
	}

	public void setPrimaryDeptCode(String primaryDeptCode) {
		this.primaryDeptCode = primaryDeptCode;
	}*/

/*	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}*/

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getDirectoryTitle() {
		return directoryTitle;
	}

	public void setDirectoryTitle(String directoryTitle) {
		this.directoryTitle = directoryTitle;
	}

}
