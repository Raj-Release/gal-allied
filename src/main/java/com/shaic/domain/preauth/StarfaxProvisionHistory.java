package com.shaic.domain.preauth;

import java.sql.Timestamp;

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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Claim;
import com.shaic.domain.Status;

@Entity
@Table(name = "IMS_CLS_SFX_PROVISION_AMT_HIS")
@NamedQueries({
	@NamedQuery(name = "StarfaxProvisionHistory.findAll", query = "SELECT i FROM StarfaxProvisionHistory i where nvl(i.provisionUpdateFlag,'N')='N' order by i.key asc"),
})

public class StarfaxProvisionHistory extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_SFX_PROVISION_AMT_HIS_KEY_GENERATOR", sequenceName = "SEQ_GLX_SFX_PROV_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SFX_PROVISION_AMT_HIS_KEY_GENERATOR" ) 
	@Column(name="GLX_SFX_PROV_KEY", updatable=false)
	private Long key;

	@Column(name = "INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name = "CLAIM_NUMBER")
	private String claimNumber;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private Claim claim;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "CURRENT_PROVISION_AMT")
	private Double currentProvisonAmt;
	
	@Column(name = "PROVSION_UPD_FLAG")
	private String provisionUpdateFlag;
	
	@Column(name = "PROVSION_UPD_DATE")
	private Timestamp provisionUpdateDate;

	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

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

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Double getCurrentProvisonAmt() {
		return currentProvisonAmt;
	}

	public void setCurrentProvisonAmt(Double currentProvisonAmt) {
		this.currentProvisonAmt = currentProvisonAmt;
	}

	public String getProvisionUpdateFlag() {
		return provisionUpdateFlag;
	}

	public void setProvisionUpdateFlag(String provisionUpdateFlag) {
		this.provisionUpdateFlag = provisionUpdateFlag;
	}

	public Timestamp getProvisionUpdateDate() {
		return provisionUpdateDate;
	}

	public void setProvisionUpdateDate(Timestamp provisionUpdateDate) {
		this.provisionUpdateDate = provisionUpdateDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	
	
}
