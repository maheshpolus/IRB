package org.mit.irb.web.roles.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.mit.irb.web.committee.view.PersonDetailsView;

//import com.polus.fibicomp.person.pojo.Person;

@Entity
@Table(name = "IRB_PERSON_ROLES_ASSIGN")
public class PersonRoles implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "personRoleIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "10000001"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "personRoleIdGenerator")	
	@Column(name = "PERSON_ROLES_ID")
	private Integer personRoleId;
		
	@Column(name = "PERSON_ID")
	private String personId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PERSON_ROLES_ASSIGN_FK3"), name = "PERSON_ID", referencedColumnName = "PERSON_ID", insertable = false, updatable = false)
	private PersonDetailsView person;
	
	@Column(name = "ROLE_ID")
	private Integer roleId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PERSON_ROLES_ASSIGN_FK1"), name = "ROLE_ID", referencedColumnName = "ROLE_ID", insertable = false, updatable = false)
	private Role role;

	@Column(name = "UNIT_NUMBER")
	private String unitNumber;

	@Column(name = "DESCEND_FLAG")
	private String descentFlag;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;
	
	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Integer getPersonRoleId() {
		return personRoleId;
	}

	public void setPersonRoleId(Integer personRoleId) {
		this.personRoleId = personRoleId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getDescentFlag() {
		return descentFlag;
	}

	public void setDescentFlag(String descentFlag) {
		this.descentFlag = descentFlag;
	}
	
	public Date getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Date updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public PersonDetailsView getPerson() {
		return person;
	}

	public void setPerson(PersonDetailsView person) {
		this.person = person;
	}
	
}
