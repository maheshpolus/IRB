package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="IRB_PROTOCOL_LOCATION")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolCollaborator {
	@Id
	@Column(name="PROTOCOL_LOCATION_ID")
	/*@GenericGenerator(name = "ProtocolCollaboratorIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProtocolCollaboratorIdGenerator")*/
	private Integer protocolLocationId;
	
	@Transient
	private String acType;
	
	@Column(name="UPDATE_TIMESTAMP")
	private Date updateTimestamp;
	
	@Column(name="PROTOCOL_ID")
	private Integer protocolId;
	
	@Column(name="SEQUENCE_NUMBER")
	private Integer sequenceNumber;
	
	@Column(name="ROLODEX_ID")
	private Integer rolodexId;
	
	@Column(name="PROTOCOL_NUMBER")
	private String protocolNumber;
	
	@Column(name="PROTOCOL_ORG_TYPE_CODE")
	private String protocolOrgTypeCode;
	
	@Column(name="UPDATE_USER")
	private String updateUser;
	
	@Column(name="ORGANIZATION_ID")
	private String organizationId;

	@Column(name="EXPIRATION_DATE")
	private Date expirationDate;
	
	@Column(name="APPROVAL_DATE")
	private Date approvalDate;
	
	@Column(name="POINT_OF_CONTACT")
	private String pointOfContact;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_ORGANIZATION_FK1"), name = "ORGANIZATION_ID", referencedColumnName = "ORGANIZATION_ID", insertable = false, updatable = false)
	CollaboratorNames collaboratorNames;
	
	@OneToMany(mappedBy = "protocolCollaborator", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProtocolCollaboratorAttachments> protocolCollaboratorAttachments;
	
	@OneToMany(mappedBy = "protocolCollaborator", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProtocolCollaboratorPersons> protocolCollaboratorPersons;
	
	@Transient
	private String stringExpirationDate;
	
	@Transient
	private String stringApprovalDate;
	
	public String getStringExpirationDate() {
		return stringExpirationDate;
	}

	public void setStringExpirationDate(String stringExpirationDate) {
		this.stringExpirationDate = stringExpirationDate;
	}

	public String getStringApprovalDate() {
		return stringApprovalDate;
	}

	public void setStringApprovalDate(String stringApprovalDate) {
		this.stringApprovalDate = stringApprovalDate;
	}

	public Integer getProtocolLocationId() {
		return protocolLocationId;
	}

	public void setProtocolLocationId(Integer protocolLocationId) {
		this.protocolLocationId = protocolLocationId;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getProtocolOrgTypeCode() {
		return protocolOrgTypeCode;
	}

	public void setProtocolOrgTypeCode(String protocolOrgTypeCode) {
		this.protocolOrgTypeCode = protocolOrgTypeCode;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public CollaboratorNames getCollaboratorNames() {
		return collaboratorNames;
	}

	public void setCollaboratorNames(CollaboratorNames collaboratorNames) {
		this.collaboratorNames = collaboratorNames;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getPointOfContact() {
		return pointOfContact;
	}

	public void setPointOfContact(String pointOfContact) {
		this.pointOfContact = pointOfContact;
	}
}
