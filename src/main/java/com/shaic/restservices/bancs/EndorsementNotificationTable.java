package com.shaic.restservices.bancs;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name="IMS_CLSB_END_APPR_DTLS")
@NamedQueries({
	@NamedQuery(name="EndorsementNotificationTable.findAll", query="SELECT o FROM EndorsementNotificationTable o where o.readFlag is null order by o.giCreatedOn asc"),
	@NamedQuery(name="EndorsementNotificationTable.findByPolicyNo", query="SELECT o FROM EndorsementNotificationTable o where o.policyNumber = :policyNumber order by o.giCreatedOn asc"),
})

public class EndorsementNotificationTable extends AbstractEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AED_POL_SYS_ID")
	private Long policySysId;
	
	@Id
	@Column(name = "AED_POL_END_NO_IDX")
	private String endorsementIndex;
	
	@Id
	@Column(name = "AED_POL_NO")
	private String policyNumber;
	
	@Column(name = "AED_RISK_ID")
	private String riskId;
	
	@Column(name = "AED_PROD_CODE")
	private String productCode;
	
	@Column(name = "AED_READ_FLG")
	private String readFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AED_CR_DT")
	private Date giCreatedOn;

	public Long getPolicySysId() {
		return policySysId;
	}

	public void setPolicySysId(Long policySysId) {
		this.policySysId = policySysId;
	}

	public String getEndorsementIndex() {
		return endorsementIndex;
	}

	public void setEndorsementIndex(String endorsementIndex) {
		this.endorsementIndex = endorsementIndex;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getRiskId() {
		return riskId;
	}

	public void setRiskId(String riskId) {
		this.riskId = riskId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	public Date getGiCreatedOn() {
		return giCreatedOn;
	}

	public void setGiCreatedOn(Date giCreatedOn) {
		this.giCreatedOn = giCreatedOn;
	}

	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
	}

}
