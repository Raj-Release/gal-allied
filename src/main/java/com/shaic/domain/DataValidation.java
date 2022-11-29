package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_CODING_DTLS")
@NamedQueries({
@NamedQuery(name="DataValidation.findbyIntimationNO", query="SELECT c FROM DataValidation c WHERE c.intimationNO = :intimationNO ORDER BY c.key DESC")
})
public class DataValidation extends AbstractEntity {

	@Id
	@Column(name = "CODING_KEY")
	private Long key;
	
	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "ROD_KEY")
	private Long rodKey;
	
	@Column(name = "PAYMENT_KEY")
	private Long paymentKey;
	
	@Column(name = "CLAIM_NUMBER")
	private String claimNo;
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNO;
	
	@Column(name = "ROD_NUMBER")
	private String rodNo;

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

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Long getPaymentKey() {
		return paymentKey;
	}

	public void setPaymentKey(Long paymentKey) {
		this.paymentKey = paymentKey;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getIntimationNO() {
		return intimationNO;
	}

	public void setIntimationNO(String intimationNO) {
		this.intimationNO = intimationNO;
	}

	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	
	
}
