package com.shaic.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the IMS_TMP_HOSPITAL_T database table.
 * 
 */
@Entity
@Table(name="IMS_TMP_HOSPITAL")
@NamedQueries({
@NamedQuery(name = "TmpHospital.findByKey", query = "SELECT m FROM TmpHospital m where m.key = :key"),
@NamedQuery(name="TmpHospital.findAll", query="SELECT i FROM TmpHospital i")})
public class TmpHospital implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5664572769353497456L;

	@Id
	@SequenceGenerator(name="SEQ_TMP_HOSPITALS_KEY_GENERATOR", sequenceName = "SEQ_TMP_HOSPITALS_KEY" ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TMP_HOSPITALS_KEY_GENERATOR" ) 
	@Column(name="TMP_HOSPITAL_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private BigDecimal activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	private String address;

	@Column(name="CITY_ID")
	private Long cityId;

	@Column(name="LOCALITY_ID")
	private Long localityId;
	
	@Column(name="CPU_ID")
	private Long cpuId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="FAX_NUMBER")
	private String faxNumber;

	@Column(name="HOSPITAL_NAME")
	private String hospitalName;

//	@Column(name="IS_TEMP")
//	private Long isTemp;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="CONTACT_NUMBER")
	private String contactNumber;

	@Column(name="MOBILE_NUMBER")
	private String mobileNumber;
	
	@Column(name="EMAIL_ID")
	private String emailId;
	
	private Long pincode;

	@Column(name="STATE_ID")
	private Long stateId;
	
	@Transient
	private String state;
	
	@Transient
	private String city;
	
	@Column(name = "REPRESENTATIVE_NAME")
    private String representativeName;
	
	@Column(name = "WS_STATE")
    private String wsState;
	
	@Column(name = "WS_CITY")
    private String wsCity;

//	@Column(name="VERSION")
//	private Long version;
	
	public TmpHospital() {
	}

	public BigDecimal getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(BigDecimal activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}


	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getFaxNumber() {
		return this.faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getHospitalName() {
		return this.hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getContactNumber() {
		return this.contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Long getPincode() {
		return this.pincode;
	}

	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}

	public Long getStateId() {
		return this.stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	
	public Long getCpuId() {
		return cpuId;
	}

	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}

	public Long getLocalityId() {
		return localityId;
	}

	public void setLocalityId(Long localityId) {
		this.localityId = localityId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getWsState() {
		return wsState;
	}

	public void setWsState(String wsState) {
		this.wsState = wsState;
	}

	public String getWsCity() {
		return wsCity;
	}

	public void setWsCity(String wsCity) {
		this.wsCity = wsCity;
	}
	
}