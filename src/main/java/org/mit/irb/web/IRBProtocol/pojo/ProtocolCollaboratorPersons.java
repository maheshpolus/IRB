package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="IRB_PROTOCOL_LOCATION_PERSON")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolCollaboratorPersons {

	@Id
	@Column(name="PROTOCOL_LOCATION_PERSON_ID")
	/*@GenericGenerator(name = "ProtocolCollaboratorPersonsIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProtocolCollaboratorPersonsIdGenerator")*/
	private Integer collaboratorPersonId;
	
	@Column(name="PROTOCOL_PERSON_ID")
	private Integer personId;
	
	@Column(name="PROTOCOL_LOCATION_ID")
	private Integer collaboratorId;
	
	@Column(name="PROTOCOL_ID")
	private Integer protocolId;
	
	@Column(name="PROTOCOL_NUMBER")
	private String protocolNumber;
	
	@Column(name="SEQUENCE_NUMBER")
	private Integer sequenceNumber; 
	
	@Column(name="UPDATE_TIMESTAMP")
	private Date updateTimestamp; 
	
	@Column(name="UPDATE_USER")
	private String updateUser;

	@Transient
	private String acType;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTO_LOCATION_PERSON_FK1"), name = "PROTOCOL_PERSON_ID", referencedColumnName = "PROTOCOL_PERSON_ID", insertable = false, updatable = false)
	ProtocolPersonnelInfo personnelInfo;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "IRB_PROTO_LOCATION_PERSON_FK2"), name = "PROTOCOL_LOCATION_ID", referencedColumnName = "PROTOCOL_LOCATION_ID", insertable = false, updatable = false)
	ProtocolCollaborator protocolCollaborator;
	
	public Integer getCollaboratorPersonId() {
		return collaboratorPersonId;
	}

	public void setCollaboratorPersonId(Integer collaboratorPersonId) {
		this.collaboratorPersonId = collaboratorPersonId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
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
	public ProtocolPersonnelInfo getPersonnelInfo() {
		return personnelInfo;
	}

	public void setPersonnelInfo(ProtocolPersonnelInfo personnelInfo) {
		this.personnelInfo = personnelInfo;
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
}
