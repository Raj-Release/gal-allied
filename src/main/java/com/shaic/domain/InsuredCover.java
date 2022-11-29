package com.shaic.domain;

import java.io.Serializable;

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
@Table(name = "IMS_CLS_POLICY_INSURED_COVER")
@NamedQueries({
		@NamedQuery(name = "InsuredCover.findAll", query = "SELECT i FROM InsuredCover i"),
		@NamedQuery(name = "InsuredCover.findByInsured", query = "SELECT i FROM InsuredCover i where i.insuredKey = :insuredKey"),
		@NamedQuery(name = "InsuredCover.findHospitalExpnByInsured", query = "SELECT i FROM InsuredCover i where i.insuredKey = :insuredKey and i.coverCode =:coverCode")
})
public class InsuredCover implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_POLICY_INSURED_COVER_KEY_GENERATOR", sequenceName = "SEQ_INSURED_COVER_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_POLICY_INSURED_COVER_KEY_GENERATOR" ) 
	@Column(name = "INSURED_COVER_KEY")
	private Long key;
	
	@Column(name = "COVER_CODE")
	private String coverCode;
	
	@Column(name = "INSURED_KEY")
	private Long insuredKey;
	
	@Column(name = "COVER_DESCRIPTION")
	private String coverCodeDescription;
	
//	@Column(name = "CREATED_DATE")
//	private Date createdDate;
	
	@Column(name = "SUM_INSURED")
	private Double sumInsured;
	
	/**
	 * Added column for GPA
	 */
	
	@Column(name = "COVER_SI_4")
	private Double coverSI4;
	
	@Column(name = "POLICY_KEY")
	private Long policyKey;
	
	@Column(name = "COVER_LEVEL")
	private Long coverLevel;

	@Column(name = "DELETED_FLAG")
	private String deletedFlag;

//	@Column(nullable = true, columnDefinition = "NUMBER", name="ACTIVE_STATUS", length = 1)
//	private Boolean activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getCoverCode() {
		return coverCode;
	}

	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}

	public String getCoverCodeDescription() {
		return coverCodeDescription;
	}

	public void setCoverCodeDescription(String coverCodeDescription) {
		this.coverCodeDescription = coverCodeDescription;
	}

//	public Date getCreatedDate() {
//		return createdDate;
//	}
//
//	public void setCreatedDate(Date createdDate) {
//		this.createdDate = createdDate;
//	}
//
//	public Boolean getActiveStatus() {
//		return activeStatus;
//	}
//
//	public void setActiveStatus(Boolean activeStatus) {
//		this.activeStatus = activeStatus;
//	}



	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Double getCoverSI4() {
		return coverSI4;
	}

	public void setCoverSI4(Double coverSI4) {
		this.coverSI4 = coverSI4;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Long getCoverLevel() {
		return coverLevel;
	}

	public void setCoverLevel(Long coverLevel) {
		this.coverLevel = coverLevel;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
	
	

}

