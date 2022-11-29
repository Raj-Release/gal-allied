package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "IMS_CLS_LINK_POLICY_DTLS")
@NamedQueries({
		
	 })
public class RelatedPolicies implements Serializable{

	@Id
	@SequenceGenerator(name="IMS_CLS_LINK_POLICY_DTLS_KEY_GENERATOR", sequenceName = "SEQ_LINK_POLICY_DTLS_ID", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LINK_POLICY_DTLS_KEY_GENERATOR" )
	@Column(name = "LINK_POLICY_DTLS_KEY")
	private Long key;
	
	@Column(name = "POLICY_KEY")
	private Long policyKey;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNo;
	
	@Column(name = "LINK_POLICY_NUMBER")
	private String linkPolicyNo;
	
	@Column(name = "LINK_TYPE")
	private String linkType;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")  
	private Date createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;
		
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getLinkPolicyNo() {
		return linkPolicyNo;
	}

	public void setLinkPolicyNo(String linkPolicyNo) {
		this.linkPolicyNo = linkPolicyNo;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	
}
