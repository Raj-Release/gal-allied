package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MAS_SEC_HOSP_USER_MAPPING")
@NamedQueries({
	//@NamedQuery(name="MasHospitalUserMapping.getHospitalsbyUserId", query="SELECT m FROM MasHospitalUserMapping m WHERE Lower(m.userId) = :userId and m.activeStatus is not null"),
	@NamedQuery(name="MasHospitalUserMapping.getHospitalsbyUserId", query="SELECT m FROM MasHospitalUserMapping m WHERE Lower(m.userId) = :userId and m.activeStatus is not null and m.activeStatus = 1"),
})
public class MasHospitalUserMapping implements Serializable{
	
	@Id
	@Column(name="HOSP_USER_KEY")
	private Long hospUserKey;
	
	@Column(name="HOSPITAL_ID")
	private Long hospitalKey;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	public Long getHospUserKey() {
		return hospUserKey;
	}

	public void setHospUserKey(Long hospUserKey) {
		this.hospUserKey = hospUserKey;
	}

	public Long getHospitalKey() {
		return hospitalKey;
	}

	public void setHospitalKey(Long hospitalKey) {
		this.hospitalKey = hospitalKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
}
