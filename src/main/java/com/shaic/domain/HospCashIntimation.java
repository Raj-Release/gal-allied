package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_HOSPCASH_INTIMATION")
@NamedQueries({
	@NamedQuery(name="HospCashIntimation.findByTopUpIntimationNumber", query="SELECT o FROM HospCashIntimation o where o is not null and o.tpIntimationNumber = :tpIntimationNumber")
})
public class HospCashIntimation extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="TP_INTM_KEY")
	private Long key;
	
	@Column(name="INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name="INTIMATION_NUMBER", nullable=true)
	private String intimationId;
	

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name="TP_BATCH_READ_FLAG", nullable=true)
	private String batchReadFlag;
	
	@Column(name="TP_INTIMATION_KEY")
	private Long tpIntimationKey;
	
	@Column(name="TP_INTIMATION_NUMBER", nullable=true)
	private String tpIntimationNumber;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getIntimationId() {
		return intimationId;
	}

	public void setIntimationId(String intimationId) {
		this.intimationId = intimationId;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getBatchReadFlag() {
		return batchReadFlag;
	}

	public void setBatchReadFlag(String batchReadFlag) {
		this.batchReadFlag = batchReadFlag;
	}

	public Long getTpIntimationKey() {
		return tpIntimationKey;
	}

	public void setTpIntimationKey(Long tpIntimationKey) {
		this.tpIntimationKey = tpIntimationKey;
	}

	public String getTpIntimationNumber() {
		return tpIntimationNumber;
	}

	public void setTpIntimationNumber(String tpIntimationNumber) {
		this.tpIntimationNumber = tpIntimationNumber;
	}

}
