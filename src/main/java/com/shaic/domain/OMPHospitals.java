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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "MAS_OMP_HOSPITALS")
@NamedQueries({
		@NamedQuery(name = "OMPHospitals.findAll", query = "SELECT m FROM OMPHospitals m"),
		@NamedQuery(name = "OMPHospitals.findbyKey", query = "SELECT m FROM OMPHospitals m where m.key = :key"),
		@NamedQuery(name = "OMPHospitals.findByHospitalName", query = "SELECT m FROM OMPHospitals m where m.name = :hospitalName"),
		@NamedQuery(name = "OMPHospitals.findMaxHospitalCode", query = "Select max(m.hospitalCode) FROM OMPHospitals m")
		})
		
public class OMPHospitals extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 7012299293228530573L;

	@Id
	@SequenceGenerator(name="OMP_HOSPITALS_KEY_GENERATOR", sequenceName = "SEQ_OMP_HOSPITALS_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMP_HOSPITALS_KEY_GENERATOR" )
	@Column(name = "HOSPITAL_KEY")
	private Long key;
	
	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;

	@Column(name = "HOSPITAL_NAME")
	private String name;
	
	@Column(name = "REMARKS")
	private String remark;
	
	@Column(name = "ADDRESS")
	private String address;
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "DISTRICT")
	private String district;

	@Column(name = "CITY")
	private String city;
	
	@Column(name = "COUNTRY")
	private String country;
	
	@Column(name="CPU_CODE")
	private String cpuCode;
	
	@Column(name = "COUNTRY_ID")
	private Long countryId;

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	
}