package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "IRB_ATTACHMENT_PROTOCOL")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class IRBAttachmentProtocol {

	@Id
	@GenericGenerator(name = "IRBProtocolAttachmentIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "IRBProtocolAttachmentIdGenerator")
	@Column(name = "PA_PROTOCOL_ID")
	private Integer paProtocolId;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_ATT_PROTOCOL_FK2"), name = "PROTOCOL_ID", referencedColumnName = "PROTOCOL_ID")
	private ProtocolGeneralInfo protocolGeneralInfo;

	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "DOCUMENT_ID")
	private Integer documentId;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "TYPE_CD")
	private String typeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_ATT_PROTOCOL_FK3"), name = "TYPE_CD", referencedColumnName = "TYPE_CD", insertable = false, updatable = false)
	private IRBAttachementTypes attachmentType;

	@Column(name = "STATUS_CD")
	private String statusCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_ATT_PROTOCOL_FK4"), name = "STATUS_CD", referencedColumnName = "STATUS_CD", insertable = false, updatable = false)
	private IRBAttachementStatus attachementStatus;

	@ManyToOne(optional = true, cascade = { CascadeType.ALL })
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_ATTACHMENT_PROTO_FK5"), name = "FILE_ID", referencedColumnName = "FILE_ID")
	private ProtocolAttachments protocolAttachment;

	@Column(name = "CONTACT_NAME")
	private String contactName;

	@Column(name = "EMAIL_ADDRESS")
	private String emailAddress;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "DOCUMENT_STATUS_CODE")
	private String documentStatusCode;

	@Column(name = "CREATE_TIMESTAMP")
	private Date createTimestamp;

	@Column(name = "ATTACHMENT_VERSION")
	private Integer attachmentVersion;

	@Transient
	private String acType;
	
	public ProtocolGeneralInfo getProtocolGeneralInfo() {
		return protocolGeneralInfo;
	}

	public void setProtocolGeneralInfo(ProtocolGeneralInfo protocolGeneralInfo) {
		this.protocolGeneralInfo = protocolGeneralInfo;
	}

	public Integer getPaProtocolId() {
		return paProtocolId;
	}

	public void setPaProtocolId(Integer paProtocolId) {
		this.paProtocolId = paProtocolId;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	/*
	 * public Integer getFileId() { return fileId; }
	 * 
	 * public void setFileId(Integer fileId) { this.fileId = fileId; }
	 */

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public String getDocumentStatusCode() {
		return documentStatusCode;
	}

	public void setDocumentStatusCode(String documentStatusCode) {
		this.documentStatusCode = documentStatusCode;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Integer getAttachmentVersion() {
		return attachmentVersion;
	}

	public void setAttachmentVersion(Integer attachmentVersion) {
		this.attachmentVersion = attachmentVersion;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public IRBAttachementTypes getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(IRBAttachementTypes attachmentType) {
		this.attachmentType = attachmentType;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public IRBAttachementStatus getAttachementStatus() {
		return attachementStatus;
	}

	public void setAttachementStatus(IRBAttachementStatus attachementStatus) {
		this.attachementStatus = attachementStatus;
	}

	public ProtocolAttachments getProtocolAttachment() {
		return protocolAttachment;
	}

	public void setProtocolAttachment(ProtocolAttachments protocolAttachment) {
		this.protocolAttachment = protocolAttachment;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}
}
