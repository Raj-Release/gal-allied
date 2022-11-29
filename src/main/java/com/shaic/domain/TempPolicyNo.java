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
@Entity
@Table(name="MAS_INVESTIGATION_BYPASS")
@NamedQueries({
@NamedQuery(name="TempPolicyNo.findByCode",query = "SELECT o FROM TempPolicyNo o where o.policyNo = :policyNo"),

})
public class TempPolicyNo implements Serializable {
	
	@Id
	@SequenceGenerator(name="MAS_INVESTIGATION_BYPASS_KEY_GENERATOR", sequenceName = "SEQ_INV_BYPASS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_INVESTIGATION_BYPASS_KEY_GENERATOR" )
	@Column(name="INV_BYPASS_KEY")
	private Long key;
	
	@Column(name="POLICY_NUMBER")
	private String policyNo;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date CreatedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifyBy;
	
	@Column(name="MODIFIED_DATE")
	private Date ModifiedDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifiedDate() {
		return ModifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		ModifiedDate = modifiedDate;
	}
	

	
	


}
