package org.mit.irb.web.committee.pojo;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.mit.irb.web.committee.util.JpaCharBooleanConversion;

@Entity
@Table(name = "IRB_PROTOCOL_REVIEW_TYPE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProtocolReviewType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROTOCOL_REVIEW_TYPE_CODE")
	private String reviewTypeCode;

	@Column(name = "DESCRIPTION")
	private String description;

	@Convert(converter = JpaCharBooleanConversion.class)
	@Column(name = "GLOBAL_FLAG")
	private boolean globalFlag;

	public String getReviewTypeCode() {
		return reviewTypeCode;
	}

	public void setReviewTypeCode(String reviewTypeCode) {
		this.reviewTypeCode = reviewTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isGlobalFlag() {
		return globalFlag;
	}

	public void setGlobalFlag(boolean globalFlag) {
		this.globalFlag = globalFlag;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
