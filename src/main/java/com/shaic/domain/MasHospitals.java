package com.shaic.domain;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "MAS_HOSPITALS")
@NamedQueries({
		@NamedQuery(name = "MasHospitals.findAll", query = "SELECT m FROM MasHospitals m"),
		@NamedQuery(name = "MasHospitals.findByKey", query = "SELECT m FROM MasHospitals m where m.key = :key"),
		@NamedQuery(name = "MasHospitals.findByHRMCode", query = "SELECT m FROM MasHospitals m where Lower(m.hrmCode) = :hrmCode"),
		@NamedQuery(name = "MasHospitals.findByCode", query = "SELECT m FROM MasHospitals m where Upper(m.hospitalCode) like :hospitalCode")
})
public class MasHospitals extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "HOSPITAL_KEY")
	private Long key;

	@Column(name = "HOSPITAL_NAME")
	private String name;

	@Column(name = "EMAIL_ID")
	private String emailId;
	

	@Column(name = "HRM_USER_ID")
	private String hrmUserId;
	
	@Column(name = "CPU_ID")
	private Long cpuId;
	
	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;
	
	@Column (name = "HRM_CODE")
	private String hrmCode;
	
	@Column(name = "HRM_MOBILE_NO")
	private String hrmContactNo;
	
	@Column(name = "HRM_USER_NAME")
	private String hrmUserName;
	
	@Column(name = "HRM_REG_HEAD_NAME")
	private String hrmHeadName;
	
	@Column(name = "HRM_REG_HEAD_CONTACT")
	private String hrmHeadContactNo;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getHrmUserId() {
		return hrmUserId;
	}

	public void setHrmUserId(String hrmUserId) {
		this.hrmUserId = hrmUserId;
	}

	public Long getCpuId() {
		return cpuId;
	}

	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getHrmCode() {
		return hrmCode;
	}

	public void setHrmCode(String hrmCode) {
		this.hrmCode = hrmCode;
	}

	public String getHrmContactNo() {
		return hrmContactNo;
	}

	public void setHrmContactNo(String hrmContactNo) {
		this.hrmContactNo = hrmContactNo;
	}

	public String getHrmUserName() {
		return hrmUserName;
	}

	public void setHrmUserName(String hrmUserName) {
		this.hrmUserName = hrmUserName;
	}

	public String getHrmHeadName() {
		return hrmHeadName;
	}

	public void setHrmHeadName(String hrmHeadName) {
		this.hrmHeadName = hrmHeadName;
	}

	public String getHrmHeadContactNo() {
		return hrmHeadContactNo;
	}

	public void setHrmHeadContactNo(String hrmHeadContactNo) {
		this.hrmHeadContactNo = hrmHeadContactNo;
	}
	
}