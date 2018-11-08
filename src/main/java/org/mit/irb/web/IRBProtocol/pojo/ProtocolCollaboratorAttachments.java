package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="IRB_PROTOCOL_LOCATION_ATTMNT")
public class ProtocolCollaboratorAttachments {
	
	@Id
	@Column(name="PROTOCOL_LOCATION_ATTMNT_ID")
	@GenericGenerator(name = "ProtocolCollaboratorAttachmentsIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProtocolCollaboratorAttachmentsIdGenerator")
	private Integer collaboratorAttachmentId;
	
	@Column(name="PROTOCOL_LOCATION_ID")
	private Integer collaboratorId;
	
	@Column(name="PROTOCOL_ID")
	private Integer protocolId;
	
	@Column(name="PROTOCOL_NUMBER")
	private String protocolNumber;
	
	@Column(name="SEQUENCE_NUMBER")
	private Integer sequenceNumber; 
	
/*	@Column(name="FILE_NAME")
	private String fileName;  we need file type instaed of file name*/
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="UPDATE_TIMESTAMP")
	private Date updateTimestamp; 
	
	@Column(name="CREATE_DATE")
	private Date createDate; 
	
	@Column(name="UPDATE_USER")
	private String updateUser;

	@Transient
	private String acType;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTO_LOCATION_ATTMNT_FK3"), name = "TYPE_CD", referencedColumnName = "TYPE_CD", insertable = false, updatable = false)
	private IRBAttachementTypes attachmentType;
	
	@Column(name = "TYPE_CD")
	private String typeCode;
	
	@ManyToOne(optional = true, cascade = { CascadeType.ALL })
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTO_LOCATION_ATTMNT_FK2"), name = "FILE_ID", referencedColumnName = "FILE_ID")
	ProtocolAttachments protocolAttachments;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTO_LOCATION_ATTMNT_FK1"), name = "PROTOCOL_LOCATION_ID", referencedColumnName = "PROTOCOL_LOCATION_ID", insertable = false, updatable = false)
	ProtocolCollaborator protocolCollaborator;
	
	public Integer getCollaboratorAttachmentId() {
		return collaboratorAttachmentId;
	}

	public void setCollaboratorAttachmentId(Integer collaboratorAttachmentId) {
		this.collaboratorAttachmentId = collaboratorAttachmentId;
	}

	public Integer getCollaboratorId() {
		return collaboratorId;
	}

	public void setCollaboratorId(Integer collaboratorId) {
		this.collaboratorId = collaboratorId;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
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

/*	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}*/

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public ProtocolAttachments getProtocolAttachments() {
		return protocolAttachments;
	}

	public void setProtocolAttachments(ProtocolAttachments protocolAttachments) {
		this.protocolAttachments = protocolAttachments;
	}

	public ProtocolCollaborator getProtocolCollaborator() {
		return protocolCollaborator;
	}

	public void setProtocolCollaborator(ProtocolCollaborator protocolCollaborator) {
		this.protocolCollaborator = protocolCollaborator;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public IRBAttachementTypes getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(IRBAttachementTypes attachmentType) {
		this.attachmentType = attachmentType;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
}
