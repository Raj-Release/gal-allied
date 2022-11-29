package com.shaic.domain.preauth;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_PED_VALIDATION_T database table.
 * 
 */
@Entity
@Table(name = "S_CPU_REVERSE_FEED")
@NamedQueries({
	@NamedQuery(name = "StarFaxReverseFeed.findAll", query = "SELECT i FROM StarFaxReverseFeed i")
})

public class StarFaxReverseFeed extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "S_CPU_REVERSE_FEED_KEY_GENERATOR", sequenceName = "SEQ_SNO_REV", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CPU_REVERSE_FEED_KEY_GENERATOR")
	@Column(name = "SNO")
	private Long key;
	
	@Column(name = "APPLICATION_ID")
	private String applicationId;

	@Column(name = "CLAIM_NUMBER")
	private String claimNumber;

	@Column(name = "FROMCPU")
	private String fromCpu;
	
	@Column(name = "TOCPU")
	private String toCpu;
	
	@Column(name = "AMOUNT")
	private String amount;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "OPNION_GIVEN_BY")
	private String userId;
	
	@Column(name="IS_GLX_PROCESSED")
	private String isGlxProcessed;

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (this.key == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
			return false;
		}

		AbstractEntity that = (AbstractEntity) obj;

		return this.key.equals(that.getKey());
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return key == null ? 0 : key.hashCode();
	}

	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getFromCpu() {
		return fromCpu;
	}

	public void setFromCpu(String fromCpu) {
		this.fromCpu = fromCpu;
	}

	public String getToCpu() {
		return toCpu;
	}

	public void setToCpu(String toCpu) {
		this.toCpu = toCpu;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsGlxProcessed() {
		return isGlxProcessed;
	}

	public void setIsGlxProcessed(String isGlxProcessed) {
		this.isGlxProcessed = isGlxProcessed;
	}

}