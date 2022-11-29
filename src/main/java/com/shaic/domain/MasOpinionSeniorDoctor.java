package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author Karthikeyan R
 *
 */
@Entity
@Table(name="MAS_OPINION_UNIT_SRDOC_MAPPING")
@NamedQueries({
@NamedQuery(name="MasOpinionSeniorDoctor.findAll", query="SELECT m FROM MasOpinionSeniorDoctor m "),
@NamedQuery(name ="MasOpinionSeniorDoctor.findByEmpId",query="SELECT o FROM MasOpinionSeniorDoctor o WHERE o.empId = :empId")
})
public class MasOpinionSeniorDoctor extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="OPN_UN_SRDOC_KEY")
	private Long key;

	@Column(name="UNIT_CODE")
	private int unitCode;
	
	@Column(name="EMP_ID")
	private String empId;
	
	@Column(name="DOC_NAME")
	private String doctorName;
	
	@Column(name="CLAIM_TYPE")
	private int claimType;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public int getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(int unitCode) {
		this.unitCode = unitCode;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public int getClaimType() {
		return claimType;
	}

	public void setClaimType(int claimType) {
		this.claimType = claimType;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

}
