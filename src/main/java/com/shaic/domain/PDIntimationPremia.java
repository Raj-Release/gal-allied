package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="CLMV_GLX_INTM_LIST")
@NamedQueries({
	@NamedQuery(name="PDIntimationPremia.findByIntmNo", query="SELECT m FROM PDIntimationPremia m WHERE m.intimationSeqNo = :claimNo and m.intimationYear = :year")
})
public class PDIntimationPremia implements Serializable {

	@Id
	@Column(name="INTIMATION_ID")
	private Long key;
	
	@Column(name="INTIMATION_NUMBER", nullable=true)
	private String intimationNumber;
	
	@Column(name="INTIMATION_SEQUENCE_NUMBER", nullable=true)
	private Long intimationSeqNo;
	
	@Column(name="INTIMATION_YEAR", nullable=true)
	private Long intimationYear;
	
	@Transient
	private Boolean hasRecords;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public Boolean getHasRecords() {
		return hasRecords;
	}

	public void setHasRecords(Boolean hasRecords) {
		this.hasRecords = hasRecords;
	}

	public Long getIntimationSeqNo() {
		return intimationSeqNo;
	}

	public void setIntimationSeqNo(Long intimationSeqNo) {
		this.intimationSeqNo = intimationSeqNo;
	}

	public Long getIntimationYear() {
		return intimationYear;
	}

	public void setIntimationYear(Long intimationYear) {
		this.intimationYear = intimationYear;
	}
	
}
