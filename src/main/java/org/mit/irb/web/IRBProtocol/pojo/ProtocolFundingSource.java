package org.mit.irb.web.IRBProtocol.pojo;

import java.sql.Date;

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
@Table(name="MITKC_IRB_PROTO_FUNDING_SOURCE")
public class ProtocolFundingSource {	
	@Id
	@GenericGenerator(name = "ProtocolFundingSourceIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProtocolFundingSourceIdGenerator")
	@Column(name="PROTOCOL_FUNDING_SOURCE_ID")
	private Integer protocolFundingSourceId;
	
	@Transient
	private String acType; 
	
	@Column(name="UPDATE_TIMESTAMP")
	private Date updateTimestamp;
	
	@Column(name="PROTOCOL_ID")
	private Integer protocolId;
	
	@Column(name="SEQUENCE_NUMBER")
	private Integer sequenceNumber;
	
	@Column(name="PROTOCOL_NUMBER")
	private String protocolNumber;
	
	@Column(name="FUNDING_SOURCE_NAME")
	private String fundingSourceName;
	
	@Column(name="FUNDING_SOURCE")
	private String fundingSource;
	
	@Column(name="FUNDING_SOURCE_TYPE_CODE")
	private String fundingSourceTypeCode;
	
	@Column(name="UPDATE_USER")
	private String updateUser;

	@Column(name="DESCRIPTION")
	private String description;
	
	@Transient
	private String title;
	
	@Transient
	private String sourceName;
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "MITKC_IRB_FUNDING_SOURCE_FK1"), name = "FUNDING_SOURCE_TYPE_CODE", referencedColumnName = "FUNDING_SOURCE_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolFundingSourceTypes protocolFundingSourceTypes;
	
	public Integer getProtocolFundingSourceId() {
		return protocolFundingSourceId;
	}

	public void setProtocolFundingSourceId(Integer protocolFundingSourceId) {
		this.protocolFundingSourceId = protocolFundingSourceId;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
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

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getFundingSourceName() {
		return fundingSourceName;
	}

	public void setFundingSourceName(String fundingSourceName) {
		this.fundingSourceName = fundingSourceName;
	}

	public String getFundingSource() {
		return fundingSource;
	}

	public void setFundingSource(String fundingSource) {
		this.fundingSource = fundingSource;
	}

	public String getFundingSourceTypeCode() {
		return fundingSourceTypeCode;
	}

	public void setFundingSourceTypeCode(String fundingSourceTypeCode) {
		this.fundingSourceTypeCode = fundingSourceTypeCode;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public ProtocolFundingSourceTypes getProtocolFundingSourceTypes() {
		return protocolFundingSourceTypes;
	}

	public void setProtocolFundingSourceTypes(ProtocolFundingSourceTypes protocolFundingSourceTypes) {
		this.protocolFundingSourceTypes = protocolFundingSourceTypes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
