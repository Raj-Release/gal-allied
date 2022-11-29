package com.shaic.domain;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "VW_INT_HOSPITALS")
@NamedQueries({
		@NamedQuery(name = "UnFreezHospitals.findByKey", query = "SELECT m FROM UnFreezHospitals m where m.key = :key"),
		@NamedQuery(name = "UnFreezHospitals.findByHospitalKey", query = "SELECT o FROM UnFreezHospitals o where o.key = :key"),
		@NamedQuery(name = "UnFreezHospitals.findANHByName", query = "SELECT o FROM UnFreezHospitals o where Upper(o.name) like :name and o.hospitalType.value not like :hospitalType"),
		@NamedQuery(name = "UnFreezHospitals.findANHByCode", query = "SELECT o FROM UnFreezHospitals o where o.hospitalCode like :hospitalCode and o.hospitalType.value not like :hospitalType"),
		@NamedQuery(name = "UnFreezHospitals.findANHHospitalCount", query = "SELECT Count(o)  FROM UnFreezHospitals o where o.stateId = :stateId and o.cityId = :cityId and o.networkHospitalTypeId in ( :networkHospitalTypeId, :greenChannelHospitalTypeId)"),
		@NamedQuery(name = "UnFreezHospitals.findANHHospitalCountStateWise", query = "SELECT Count(o)  FROM UnFreezHospitals o where o.stateId = :stateId and o.networkHospitalTypeId in ( :networkHospitalTypeId, :greenChannelHospitalTypeId)")}
		)
		
public class UnFreezHospitals extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "HOSPITAL_KEY")
	private Long key;

	@Column(name = "HOSPITAL_NAME")
	private String name;
	
	@Column(name="CPU_ID")
	private Long cpuId;
	
//	@Column(name = "DOOR_APARTMENT_NUMBER")
//	private String doorApartmentNumber;

//	@Column(name = "PLOT_GATE_NUMBER")
//	private String plotGateNumber;
//	
//	@Column(name = "BUILDING_NAME")
//	private String buildingName;

	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;

	@Column(name = "HOSPITAL_IRDA_CODE")
	private String hospitalIrdaCode;
	
	@Column(name = "CITY")
	private String city;

	@Column(name = "STATE_ID")
	private Long stateId;

//	@Column(name = "STREET_NAME")
//	private String streetName;

	@Column(name = "HOSPITAL_CATEGORY")
	private String hospitalCategory;
	
	@Column(name = "NETWORK_HOSPITAL_TYPE")
	private String networkHospitalType;
	
	@Column(name = "NETWORK_HOSPITAL_TYPE_ID")
	private Long networkHospitalTypeId;

	@Column(name = "CITY_ID")
	private Long cityId;
	
	@Column(name = "REGISTRATION_NUMBER")
	private String registrationNumber;

//	@Column(name = "DISTRICT")
//	private String district;

	@Column(name = "STATE")
	private String state;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "PINCODE")
	private String pincode;

	@OneToOne
	@JoinColumn(name="HOSPITAL_TYPE_ID", nullable=false)
	private MastersValue hospitalType;
	
	@Column(name = "HOSPITAL_TYPE")
	private String hospitalTypeName;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;
	
	@Column(name = "MOBILE_NUMBER")
	private String mobileNumber;
	
	@Column(name = "FAX")
	private String fax;
	
	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "AUTHORIZED_REPRESENTATIVE")
	private String authorizedRepresentative;

	@Column(name = "REPRESENTATIVE_NAME")
	private String representativeName;

	@Column(name = "REMARKS")
	private String remark;
	
//	@Column(name = "LOCALITY_ID")
//	private Long localityId;
//	
//	@Column(name = "LOCALITY")
//	private String locality;
	
	@Column(name = "ADDRESS")
	private String address;
	
	@Column(name = "CITY_CLASS")
	private String cityClass;
	
	@Column(name = "PAN_NUMBER")
	private String panNumber;
	
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNo;
	
	@Column(name = "IFSC_CODE")
	private String ifscCode;
	
	@Column(name = "PAYABLE_AT")
	private String payableAt;
	
	@Column(name = "PAYMENT_TYPE")
	private String paymentType;
	
	@Column(name = "ZONE")
	private String zone;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="OT_FACILITIES_AVAILABLE_FLAG", length=1)
	private String otFacilityFlag;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="ICU_FACILITIES_AVAILABLE_FLAG", length=1)
	private String icuFacilityFlag;
	
	@Column(name = "NUMBER_OF_INPATIENT_BEDS")
	private Integer inpatientBeds;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="SUSPICIOUS_FLAG", length=1)
	private String suspicousFlag;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="DISCOUNT_FLAG", length=1)
//	@Transient
	private String hospitalDiscountFlag;
	
	@Column(name = "SUSPICIOUS_REMARKS")
	private String suspiciousRemarks;
	
	@Column(name = "HRM_USER_ID")
	private String hrmCode;

	@Column(name = "HRM_MOBILE_NO")
	private String hrmContactNo;
	
	@Column(name = "HRM_MAIL_ID")
	private String hrmMailId;
	
	@Column(name = "HRM_USER_NAME")
	private String hrmUserName;
	
//	private String hrmCode;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="NON_PREFERRED", length=1)
	private String nonPreferredFlag;
	
	@Column(name = "NON_PREFERRED_REMARKS")
	private String nonPreferredRemarks;
	
	@Column(name="IS_PREFERRED_ANH")
	private String isPreferredFlag;
	
	@Column(name="CLM_PRCS_INSTRUCTION")
	private String clmPrcsInstruction;
	
	@Column(name="SUSPICIOUS_TYPE")
	private String suspiciousType;
	
	@Column(name = "DISCOUNTS")
	private String discount;
	
	@Column(name = "DISCOUNT_PERCENT")
	private Double discountPercentage;
	
	//@Column(name = "FINAL_GRADE_NAME")
	@Transient
	private String finalGradeName;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public String getDoorApartmentNumber() {
//		return doorApartmentNumber;
//	}
//
//	public String getBuildingName() {
//		return buildingName;
//	}
	
	public String getHospitalCategory() {
		return hospitalCategory;
	}

	public void setHospitalCategory(String hospitalCategory) {
		this.hospitalCategory = hospitalCategory;
	}

//	public void setDoorApartmentNumber(String doorApartmentNumber) {
//		this.doorApartmentNumber = doorApartmentNumber;
//	}
//
//	public void setBuildingName(String buildingName) {
//		this.buildingName = buildingName;
//	}

	public Long getCityId() {
		return cityId;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

//	public String getStreetName() {
//		return streetName;
//	}
//
//	public void setStreetName(String streetName) {
//		this.streetName = streetName;
//	}

	public String getAuthorizedRepresentative() {
		return authorizedRepresentative;
	}

	public void setAuthorizedRepresentative(String authorizedRepresentative) {
		this.authorizedRepresentative = authorizedRepresentative;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getHospitalTypeName() {
		return hospitalTypeName;
	}
	
	public void setHospitalTypeName(String hospitalTypeName) {
		this.hospitalTypeName = hospitalTypeName;
	}

	public String getFax() {
		return fax;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public MastersValue getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(MastersValue hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getCity() {
		return city;
	}

//	public String getDistrict() {
//		return district;
//	}

//	public Long getLocalityId() {
//		return localityId;
//	}
//
//	public void setLocalityId(Long localityId) {
//		this.localityId = localityId;
//	}
//
//	public String getLocality() {
//		return locality;
//	}
//
//	public void setLocality(String locality) {
//		this.locality = locality;
//	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	public String getPincode() {
		return pincode;
	}

	public void setCity(String city) {
		this.city = city;
	}

//	public void setDistrict(String district) {
//		this.district = district;
//	}

	public void setState(String state) {
		this.state = state;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public UnFreezHospitals() {
	}

	public String getHospitalCode() {
		return this.hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
	
	public String getNetworkHospitalType() {
		return networkHospitalType;
	}

	public void setNetworkHospitalType(String networkHospitalType) {
		this.networkHospitalType = networkHospitalType;
	}

	public Long getNetworkHospitalTypeId() {
		return networkHospitalTypeId;
	}

	public void setNetworkHospitalTypeId(Long networkHospitalTypeId) {
		this.networkHospitalTypeId = networkHospitalTypeId;
	}

	public String getAddress() {
//		address = getStringWithDefault(this.getDoorApartmentNumber())
//				+ ", " + getStringWithDefault(this.getBuildingName()) + ", "
//				+ getStringWithDefault(this.getStreetName()) + ", "
//				+ getStringWithDefault(this.getCity()) + ", "
//				+ getStringWithDefault(this.getDistrict()) + ", "
//				+ getStringWithDefault(this.getState()) + ", "
//				+ getStringWithDefault(this.getCountry());
		return address;
	}

	public static String getStringWithDefault(String obj) {
		return (obj == null) ? "" : obj;
	}

    public String getHospitalIrdaCode() {
		return hospitalIrdaCode;
	}

	public void setHospitalIrdaCode(String hospitalIrdaCode) {
		this.hospitalIrdaCode = hospitalIrdaCode;
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

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((Person) obj).getId());
        }
    }

    @Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        } else {
            return super.hashCode();
        }
    }

	public Long getCpuId() {
		return cpuId;
	}

	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}

//	public String getPlotGateNumber() {
//		return plotGateNumber;
//	}
//
//	public void setPlotGateNumber(String plotGateNumber) {
//		this.plotGateNumber = plotGateNumber;
//	}

	public String getCityClass() {
		return cityClass;
	}

	public void setCityClass(String cityClass) {
		this.cityClass = cityClass;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the panNumber
	 */
	public String getPanNumber() {
		return panNumber;
	}

	/**
	 * @param panNumber the panNumber to set
	 */
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}

	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	/**
	 * @return the ifscCode
	 */
	public String getIfscCode() {
		return ifscCode;
	}

	/**
	 * @param ifscCode the ifscCode to set
	 */
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	/**
	 * @return the payableAt
	 */
	public String getPayableAt() {
		return payableAt;
	}

	/**
	 * @param payableAt the payableAt to set
	 */
	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}

	/**
	 * @return the paymentMode
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentMode the paymentMode to set
	 */
	public void setPaymentMode(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getOtFacilityFlag() {
		return otFacilityFlag;
	}

	public void setOtFacilityFlag(String otFacilityFlag) {
		this.otFacilityFlag = otFacilityFlag;
	}

	public String getIcuFacilityFlag() {
		return icuFacilityFlag;
	}

	public void setIcuFacilityFlag(String icuFacilityFlag) {
		this.icuFacilityFlag = icuFacilityFlag;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getInpatientBeds() {
		return inpatientBeds;
	}

	public void setInpatientBeds(Integer inpatientBeds) {
		this.inpatientBeds = inpatientBeds;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getSuspicousFlag() {
		return suspicousFlag;
	}

	public void setSuspicousFlag(String suspicousFlag) {
		this.suspicousFlag = suspicousFlag;
	}

	public String getSuspiciousRemarks() {
		return suspiciousRemarks;
	}

	public void setSuspiciousRemarks(String suspiciousRemarks) {
		this.suspiciousRemarks = suspiciousRemarks;
	}

	public String getHospitalDiscountFlag() {
		return hospitalDiscountFlag;
	}

	public void setHospitalDiscountFlag(String hospitalDiscountFlag) {
		this.hospitalDiscountFlag = hospitalDiscountFlag;
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

	public String getHrmMailId() {
		return hrmMailId;
	}

	public void setHrmMailId(String hrmMailId) {
		this.hrmMailId = hrmMailId;
	}

	public String getHrmUserName() {
		return hrmUserName;
	}

	public void setHrmUserName(String hrmUserName) {
		this.hrmUserName = hrmUserName;
	}

	public String getNonPreferredFlag() {
		return nonPreferredFlag;
	}

	public void setNonPreferredFlag(String nonPreferredFlag) {
		this.nonPreferredFlag = nonPreferredFlag;
	}

	public String getNonPreferredRemarks() {
		return nonPreferredRemarks;
	}

	public void setNonPreferredRemarks(String nonPreferredRemarks) {
		this.nonPreferredRemarks = nonPreferredRemarks;
	}

	public String getIsPreferredFlag() {
		return isPreferredFlag;
	}

	public void setIsPreferredFlag(String isPreferredFlag) {
		this.isPreferredFlag = isPreferredFlag;
	}

	public String getClmPrcsInstruction() {
		return clmPrcsInstruction;
	}

	public void setClmPrcsInstruction(String clmPrcsInstruction) {
		this.clmPrcsInstruction = clmPrcsInstruction;
	}

	public String getSuspiciousType() {
		return suspiciousType;
	}

	public void setSuspiciousType(String suspiciousType) {
		this.suspiciousType = suspiciousType;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public String getFinalGradeName() {
		return finalGradeName;
	}

	public void setFinalGradeName(String finalGradeName) {
		this.finalGradeName = finalGradeName;
	}
}