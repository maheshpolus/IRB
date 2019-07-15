package org.mit.irb.web.committee.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import org.mit.irb.web.committee.schedule.DateUtils;
import org.mit.irb.web.committee.pojo.Rolodex;
import org.mit.irb.web.committee.util.JpaCharBooleanConversion;
import org.mit.irb.web.committee.view.PersonDetailsView;

@Entity
@Table(name = "IRB_COMM_MEMBERSHIPS")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class CommitteeMemberships implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "membershipIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "membershipIdGererator")
	@Column(name = "COMM_MEMBERSHIP_ID", updatable = false, nullable = false)
	private Integer commMembershipId;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_IRB_COMM_MEMBERSHIPS"), name = "COMMITTEE_ID", referencedColumnName = "COMMITTEE_ID")
	private Committee committee;
	
	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "ROLODEX_ID")
	private Integer rolodexId;

	@Column(name = "PERSON_NAME")
	private String personName;

	@Column(name = "NON_EMPLOYEE_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean nonEmployeeFlag;

	@Column(name = "PAID_MEMBER_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean paidMemberFlag;

	@Column(name = "TERM_START_DATE")
	private java.util.Date termStartDate;

	@Column(name = "TERM_END_DATE")
	private java.util.Date termEndDate;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_IRB_COMM_MEMBERSHIPS_2"), name = "MEMBERSHIP_TYPE_CODE", referencedColumnName = "MEMBERSHIP_TYPE_CODE", insertable = false, updatable = false)
	private CommitteeMembershipType committeeMembershipType;

	@Column(name = "MEMBERSHIP_TYPE_CODE")
	private String membershipTypeCode;

	@Column(name = "UPDATE_TIMESTAMP")
	private java.util.Date updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "CONTACT_NOTES")
	private char[] contactNotes;

	@Column(name = "TRAINING_NOTES")
	private char[] trainingNotes;

	@JsonManagedReference
	@OneToMany(mappedBy = "committeeMemberships", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<CommitteeMemberRoles> committeeMemberRoles;

	@JsonManagedReference
	@OneToMany(mappedBy = "committeeMemberships", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<CommitteeMemberExpertise> committeeMemberExpertises;

	@ManyToOne(optional= true)
	@JoinColumn(foreignKey=@ForeignKey(name="IRB_COMM_MEMBERSHIPS_FK3"),name="PERSON_ID",referencedColumnName="PERSON_ID", insertable = false, updatable = false)
	private PersonDetailsView personDetails;

	@Column(name = "COMMENTS")
	private String comments;

	@Transient
	private boolean active;

	@Transient
	private Rolodex rolodex;
	
	@Transient
	private java.util.Date perviousTermStartDate;
	
	@Transient
	private java.util.Date perviousTermEndDate;
	
	@Transient
	private String acType;

	public CommitteeMemberships() {
		setCommitteeMemberRoles(new ArrayList<CommitteeMemberRoles>());
		setCommitteeMemberExpertises(new ArrayList<CommitteeMemberExpertise>());
	}

	public Integer getCommMembershipId() {
		return commMembershipId;
	}

	public void setCommMembershipId(Integer commMembershipId) {
		this.commMembershipId = commMembershipId;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public CommitteeMembershipType getCommitteeMembershipType() {
		return committeeMembershipType;
	}

	public void setCommitteeMembershipType(CommitteeMembershipType committeeMembershipType) {
		this.committeeMembershipType = committeeMembershipType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public char[] getContactNotes() {
		return contactNotes;
	}

	public void setContactNotes(char[] contactNotes) {
		this.contactNotes = contactNotes;
	}

	public char[] getTrainingNotes() {
		return trainingNotes;
	}

	public void setTrainingNotes(char[] trainingNotes) {
		this.trainingNotes = trainingNotes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<CommitteeMemberExpertise> getCommitteeMemberExpertises() {
		return committeeMemberExpertises;
	}

	public void setCommitteeMemberExpertises(List<CommitteeMemberExpertise> committeeMemberExpertises) {
		this.committeeMemberExpertises = committeeMemberExpertises;
	}

	public PersonDetailsView getPersonDetails() {
		return personDetails;
	}

	public void setPersonDetails(PersonDetailsView personDetails) {
		this.personDetails = personDetails;
	}

	/**
     * This method determines if the current committee member is active as of the current date.
     * @return true if member is active, false otherwise
     */
    public boolean isActive() {
        Date currentDate = DateUtils.clearTimeFields(new Date(System.currentTimeMillis()));
        return isActive(currentDate);
    }

    /**
     * 
     * This method determines if the current committee member is active for the given date
     * @param date
     * @return true if member is active, false otherwise
     */
    public boolean isActive(Date date) {
        boolean isActive = false;
            if (termStartDate != null && termEndDate != null && (date.before(termEndDate) || date.equals(termEndDate))) {              
                    isActive = true;
                } else {
                    isActive = false;
                }                    
        this.active = isActive;
        return this.active;
    }

	public String getMembershipTypeCode() {
		return membershipTypeCode;
	}

	public void setMembershipTypeCode(String membershipTypeCode) {
		this.membershipTypeCode = membershipTypeCode;
	}

	public Rolodex getRolodex() {
		return rolodex;
	}

	public void setRolodex(Rolodex rolodex) {
		this.rolodex = rolodex;
	}

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}

	public Boolean getNonEmployeeFlag() {
		return nonEmployeeFlag;
	}

	public void setNonEmployeeFlag(Boolean nonEmployeeFlag) {
		this.nonEmployeeFlag = nonEmployeeFlag;
	}

	public Boolean getPaidMemberFlag() {
		return paidMemberFlag;
	}

	public void setPaidMemberFlag(Boolean paidMemberFlag) {
		this.paidMemberFlag = paidMemberFlag;
	}

	public java.util.Date getTermStartDate() {
		return termStartDate;
	}

	public void setTermStartDate(java.util.Date termStartDate) {
		this.termStartDate = termStartDate;
	}

	public java.util.Date getTermEndDate() {
		return termEndDate;
	}

	public void setTermEndDate(java.util.Date termEndDate) {
		this.termEndDate = termEndDate;
	}

	public java.util.Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(java.util.Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public CommitteeMemberships(Integer commMembershipId, Committee committee, String personId, Integer rolodexId,
			String personName, Boolean nonEmployeeFlag, java.util.Date termStartDate, java.util.Date termEndDate,
			java.util.Date updateTimestamp, String updateUser,String membershipTypeCode , CommitteeMembershipType committeeMembershipType) {
		super();
		this.commMembershipId = commMembershipId;
		this.committee = committee;
		this.personId = personId;
		this.rolodexId = rolodexId;
		this.personName = personName;
		this.nonEmployeeFlag = nonEmployeeFlag;
		this.termStartDate = termStartDate;
		this.termEndDate = termEndDate;
		this.updateTimestamp = updateTimestamp;
		this.updateUser = updateUser;
		this.membershipTypeCode = membershipTypeCode;
		this.committeeMembershipType = committeeMembershipType;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<CommitteeMemberRoles> getCommitteeMemberRoles() {
		return committeeMemberRoles;
	}

	public void setCommitteeMemberRoles(List<CommitteeMemberRoles> committeeMemberRoles) {
		this.committeeMemberRoles = committeeMemberRoles;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public java.util.Date getPerviousTermStartDate() {
		return perviousTermStartDate;
	}

	public void setPerviousTermStartDate(java.util.Date perviousTermStartDate) {
		this.perviousTermStartDate = perviousTermStartDate;
	}

	public java.util.Date getPerviousTermEndDate() {
		return perviousTermEndDate;
	}

	public void setPerviousTermEndDate(java.util.Date perviousTermEndDate) {
		this.perviousTermEndDate = perviousTermEndDate;
	}
}
