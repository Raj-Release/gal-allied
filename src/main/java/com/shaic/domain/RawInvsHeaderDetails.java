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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name="IMS_CLS_RAW_INV_HDR")
@NamedQueries({
	@NamedQuery(name="RawInvsHeaderDetails.findAll", query="SELECT r FROM RawInvsHeaderDetails r where r.key = :key"),
	@NamedQuery(name="RawInvsHeaderDetails.findByIntimationNo", query="SELECT r FROM RawInvsHeaderDetails r where r.intimationNo = :intimationNo")
})
public class RawInvsHeaderDetails implements Serializable{
	@Id
	@SequenceGenerator(name="SEQ_RAW_INV_KEY_GENERATOR", sequenceName = "SEQ_RAW_INV_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RAW_INV_KEY_GENERATOR" )
	@Column(name = "RAW_INV_KEY")
	private Long key;
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNo;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "CLAIM_TYPE")
	private String claimType;
	
	@Column(name = "CPU_CODE")
	private String cpuCode;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifyby;
	
	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifyby() {
		return modifyby;
	}

	public void setModifyby(String modifyby) {
		this.modifyby = modifyby;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	@Override
	public String toString() {
		return "RawInvsHeaderDetails [key=" + key + ", intimationNo="
				+ intimationNo + ", policyNumber=" + policyNumber
				+ ", claimType=" + claimType + ", cpuCode=" + cpuCode
				+ ", createdDate=" + createdDate + ", createdBy=" + createdBy
				+ ", modifiedDate=" + modifiedDate + ", modifyby=" + modifyby
				+ ", hospitalCode=" + hospitalCode + "]";
	}
	
	
}
