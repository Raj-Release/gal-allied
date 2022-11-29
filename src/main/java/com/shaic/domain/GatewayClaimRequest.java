package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the IMS_CLS_CLAIMS_GATEWAY_REQUEST database table.
 * @author karthikeyan.r
 */

@Entity
@Table(name = "IMS_CLS_CLAIMS_GATEWAY_REQUEST")
@NamedQueries({
	@NamedQuery(name = "GatewayClaimRequest.findByPolicyNumber", query = "SELECT o FROM GatewayClaimRequest o where o.policyNumber = :policyNumber and o.readFlag = :readFlag and o.respondedFlag = :respondedFlag "),
})
public class GatewayClaimRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_CLAIMS_GATEWAY_REQUEST_KEY_GENERATOR", sequenceName = "SEQ_GATEWAY_REQUEST_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_CLAIMS_GATEWAY_REQUEST_KEY_GENERATOR" ) 
	@Column(name = "GATEWAY_REQUEST_KEY")
	private Long key;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "INTIMATION_FROM_DATE")
	private Date intimationFromDate;
	
	@Column(name = "INTIMATION_TO_DATE")
	private Date intimationToDate;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "READ_FLAG")
	private String readFlag;
	
	@Column(name = "REQUESTED_APPLICATION")
	private String requestedApplication;
	
	@Column(name = "RESPONDED_FLAG")
	private String respondedFlag;
	
	@Column(name = "RESPONDED_DATE")
	private Date respondedDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Date getIntimationFromDate() {
		return intimationFromDate;
	}

	public void setIntimationFromDate(Date intimationFromDate) {
		this.intimationFromDate = intimationFromDate;
	}

	public Date getIntimationToDate() {
		return intimationToDate;
	}

	public void setIntimationToDate(Date intimationToDate) {
		this.intimationToDate = intimationToDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	public String getRequestedApplication() {
		return requestedApplication;
	}

	public void setRequestedApplication(String requestedApplication) {
		this.requestedApplication = requestedApplication;
	}

	public String getRespondedFlag() {
		return respondedFlag;
	}

	public void setRespondedFlag(String respondedFlag) {
		this.respondedFlag = respondedFlag;
	}

	public Date getRespondedDate() {
		return respondedDate;
	}

	public void setRespondedDate(Date respondedDate) {
		this.respondedDate = respondedDate;
	}
	
}