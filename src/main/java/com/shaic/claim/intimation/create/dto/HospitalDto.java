package com.shaic.claim.intimation.create.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ProviderWiseScoring;
import com.shaic.domain.TmpHospital;
import com.shaic.domain.UnFreezHospitals;

public class HospitalDto implements Serializable{

	private static final long serialVersionUID = -4393948868604854977L;
	
	private Long key;
	@NotNull
	@Pattern(regexp="^[a-zA-Z0-9./']*$", message="Please Enter a valid Hospital Name")	
	private String name;
	private SelectValue hospitalType;
	private String HospitalTypeValue;
	private SelectValue networkHospitalType;
	private Long cpuId;
	private String hospitalCode;
	private String hospitalStatus;
	private String hospitalIrdaCode;
	private String city;
	private String state;
	private String emailId;
	private String phoneNumber;
	private String mobileNumber;
	private String address;
	private String hospAddr1;
	private String hospAddr2;
	private String hospAddr3;
	private String hospAddr4;
	private String pincode;
	private String fax;
	private Long cityId;
	private Long stateId;
	private Long areaId;
	private String hospitalZone;
	private Hospitals registedHospitals;
	private TmpHospital notRegisteredHospitals;
	//private HospitalPackageRatesDto packageRatesDto;
	private String suspiciousRemarks;
	private String suspiciousFlag;
	private String hospitalPayableAt;
	private String nonPreferredRemarks;
	private String nonPreferredFlag;
	private String isPreferredHospital;
	private String discount;
	private String finalGradeName;
	private Double discountPercentage;
	private Boolean isEnable  = true;
	private String hospitalFlag;
	
	private String suspiciousType;
	private String clmPrcsInstruction;
	private Long claimsReported;
	private Long claimsScored;
	private String deficiencyNotIn;
	private Double finalScore;
	private String reInstatedBy;
	private String remarks;
	private String discountRemark;
	private String representativeName;
	private Integer medibuddyFlag;
	private String fspFlag;
	private String exclusionProvideFlag;
	private String finalScoreDeficiency;
	private String exclusionProvideRemarks;
	
	
	public Long getClaimsReported() {
		return claimsReported;
	}

	public void setClaimsReported(Long claimsReported) {
		this.claimsReported = claimsReported;
	}

	public Long getClaimsScored() {
		return claimsScored;
	}

	public void setClaimsScored(Long claimsScored) {
		this.claimsScored = claimsScored;
	}

	

	public String getDeficiencyNotIn() {
		return deficiencyNotIn;
	}

	public void setDeficiencyNotIn(String deficiencyNotIn) {
		this.deficiencyNotIn = deficiencyNotIn;
	}

	public Double getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(Double finalScore) {
		this.finalScore = finalScore;
	}



	private UnFreezHospitals registedUnFreezHospitals;
	
	public String getHospitalPayableAt() {
		return hospitalPayableAt;
	}

	public void setHospitalPayableAt(String hospitalPayableAt) {
		this.hospitalPayableAt = hospitalPayableAt;
	}

	
	public String getReInstatedBy() {
		return reInstatedBy;
	}

	public void setReInstatedBy(String reInstatedBy) {
		this.reInstatedBy = reInstatedBy;
	}


	private Long networkHospitalTypeId;
	
	public HospitalDto() {
		registedHospitals = new Hospitals();
		notRegisteredHospitals = new TmpHospital();
		hospitalType = new SelectValue();
		registedUnFreezHospitals = new UnFreezHospitals();
	}
	
	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	
	public HospitalDto(Hospitals hospital, SelectValue hospitalType)
	{
		this(hospital);
		this.setHospitalType(hospitalType);
	}
	
	public HospitalDto(UnFreezHospitals hospital, SelectValue hospitalType)
	{
		this(hospital);
		this.setHospitalType(hospitalType);
	}
	
	public HospitalDto(Hospitals hospital)
	{
		this.key= hospital.getKey();
		SelectValue hospitalType = new SelectValue();
		if(hospital.getHospitalType() != null){
			hospitalType.setId(hospital.getHospitalType().getKey());
			hospitalType.setValue(hospital.getHospitalType().getValue());
			this.setHospitalTypeValue(hospital.getHospitalType().getValue());
			this.setHospitalTypeValue(hospital.getHospitalType().getValue());
			this.setHospitalType(hospitalType);
		}
		this.setRegistedHospitals(hospital);
		this.setName(hospital.getName());
		this.setKey(hospital.getKey());
		this.setCpuId(hospital.getCpuId());
		this.setPincode(hospital.getPincode());
		
		this.setState(hospital.getState());
		this.setCity(hospital.getCity());
		this.setHospitalCode(hospital.getHospitalCode());
		this.setStateId(hospital.getStateId());
		this.setCityId(hospital.getCityId());
		
		this.setPhoneNumber(hospital.getPhoneNumber());
		this.setFax(hospital.getFax());
		this.setMobileNumber(hospital.getMobileNumber());
		this.setEmailId(hospital.getEmailId());
		
		this.setAddress(hospital.getAddress());
		this.setHospitalCode(hospital.getHospitalCode());
		this.setHospitalZone(hospital.getZone());
		this.setSuspiciousFlag(hospital.getSuspicousFlag());
		this.setSuspiciousRemarks(hospital.getSuspiciousRemarks());
		this.setNetworkHospitalTypeId(hospital.getNetworkHospitalTypeId());
		this.setHospitalIrdaCode(hospital.getHospitalIrdaCode());
		this.setHospitalPayableAt(hospital.getPayableAt());
		this.setNonPreferredFlag(hospital.getNonPreferredFlag());
		this.setNonPreferredRemarks(hospital.getNonPreferredRemarks());
		this.setIsPreferredHospital(hospital.getIsPreferredFlag());
		this.setDiscount(hospital.getDiscount());
		this.setFinalGradeName(hospital.getFinalGradeName());
		this.setDiscountPercentage(hospital.getDiscountPercentage());
		this.setHospitalFlag(hospital.getSuspiciousType());
		this.setSuspiciousType(hospital.getSuspiciousType());
		this.setClmPrcsInstruction(hospital.getClmPrcsInstruction());
		this.setReInstatedBy(hospital.getReinstatedBy() != null ? hospital.getReinstatedBy() : "");
		this.setHospitalStatus(hospital.getHospitalStatus());
		this.setRemarks(hospital.getRemark());
		this.setDiscountRemark(hospital.getDiscountRemark());
		this.setMedibuddyFlag(hospital.getMediBuddy());
		this.setFspFlag(hospital.getFspFlag());
		this.setExclusionProvideFlag(hospital.getExclusionProvide());
		this.setExclusionProvideRemarks(hospital.getExclusionProvideRemarks());
	}
	/*public HospitalDto(ProviderWiseScoring providerScoring) {
		
		this.setClaimsReported(providerScoring.getCliamCount());
		this.setClaimsScored(providerScoring.getScoredClaimCount());
		this.setDeficiencyNotIn(providerScoring.getScorePercentage());
		this.setFinalScore(providerScoring.getFinalScore());
	}*/
	
	public HospitalDto(UnFreezHospitals hospital)
	{
		this.key= hospital.getKey();
		SelectValue hospitalType = new SelectValue();
		if(hospital.getHospitalType() != null){
			hospitalType.setId(hospital.getHospitalType().getKey());
			hospitalType.setValue(hospital.getHospitalType().getValue());
			this.setHospitalTypeValue(hospital.getHospitalType().getValue());
			this.setHospitalTypeValue(hospital.getHospitalType().getValue());
			this.setHospitalType(hospitalType);
		}
		this.setRegistedUnFreezHospitals(hospital);
		this.setName(hospital.getName());
		this.setKey(hospital.getKey());
		this.setCpuId(hospital.getCpuId());
		this.setPincode(hospital.getPincode());
		
		this.setState(hospital.getState());
		this.setCity(hospital.getCity());
		this.setHospitalCode(hospital.getHospitalCode());
		this.setStateId(hospital.getStateId());
		this.setCityId(hospital.getCityId());
		
		this.setPhoneNumber(hospital.getPhoneNumber());
		this.setFax(hospital.getFax());
		this.setMobileNumber(hospital.getMobileNumber());
		this.setEmailId(hospital.getEmailId());
		
		this.setAddress(hospital.getAddress());
		this.setHospitalCode(hospital.getHospitalCode());
		this.setHospitalZone(hospital.getZone());
		this.setSuspiciousFlag(hospital.getSuspicousFlag());
		this.setSuspiciousRemarks(hospital.getSuspiciousRemarks());
		this.setNetworkHospitalTypeId(hospital.getNetworkHospitalTypeId());
		this.setHospitalIrdaCode(hospital.getHospitalIrdaCode());
		this.setHospitalPayableAt(hospital.getPayableAt());
		this.setNonPreferredFlag(hospital.getNonPreferredFlag());
		this.setNonPreferredRemarks(hospital.getNonPreferredRemarks());
		this.setIsPreferredHospital(hospital.getIsPreferredFlag());
		this.setDiscount(hospital.getDiscount());
		this.setFinalGradeName(hospital.getFinalGradeName());
		this.setDiscountPercentage(hospital.getDiscountPercentage());
		this.setRepresentativeName(hospital.getRepresentativeName());
	}
	
	
	public HospitalDto(Hospitals hospital, MastersValue hospitalType)
	{
		this(hospital);
		this.hospitalType= new SelectValue();
		this.hospitalType.setValue(hospitalType.getValue());
		this.hospitalType.setId(hospitalType.getKey());
		
	}
	
	public HospitalDto(TmpHospital hospital, String hospitalType)
	{
		this.setKey(hospital.getKey());
		this.setNotRegisteredHospitals(hospital);
		this.setHospitalTypeValue(hospitalType);
		this.setName(hospital.getHospitalName());
		//this.setPincode(hospital.getPincode());
	}
	
	public HospitalDto(TmpHospital hospital, SelectValue hospitalType)
	{
		this.setKey(hospital.getKey());
		this.setNotRegisteredHospitals(hospital);
		this.setHospitalType(hospitalType);
		this.setName(hospital.getHospitalName());
		this.setPincode(hospital.getPincode() != null ? hospital.getPincode().toString() : null);
		this.setCpuId(null);
	}
	
	public TmpHospital getNotRegisteredHospitals() {
		return notRegisteredHospitals;
	}

	public void setNotRegisteredHospitals(TmpHospital notRegisteredHospitals) {
		this.notRegisteredHospitals = notRegisteredHospitals;
	}

	public SelectValue getHospitalType() {
		return hospitalType;
	}

	public String getName() {
		return name;
	}

	public Long getKey() {
		return key;
	}

	public Hospitals getRegistedHospitals() {
		return registedHospitals;
	}

	public void setHospitalType(SelectValue hospitalType) {
		this.hospitalType = hospitalType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public void setRegistedHospitals(Hospitals registedHospitals) {
		this.registedHospitals = registedHospitals;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}

	public String getHospitalIrdaCode() {
		return hospitalIrdaCode;
	}

	public void setHospitalIrdaCode(String hospitalIrdaCode) {
		this.hospitalIrdaCode = hospitalIrdaCode;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHospAddr1() {
		return hospAddr1;
	}

	public void setHospAddr1(String hospAddr1) {
		this.hospAddr1 = hospAddr1;
	}

	public String getHospAddr2() {
		return hospAddr2;
	}

	public void setHospAddr2(String hospAddr2) {
		this.hospAddr2 = hospAddr2;
	}

	public String getHospAddr3() {
		return hospAddr3;
	}

	public void setHospAddr3(String hospAddr3) {
		this.hospAddr3 = hospAddr3;
	}

	public String getHospAddr4() {
		return hospAddr4;
	}

	public void setHospAddr4(String hospAddr4) {
		this.hospAddr4 = hospAddr4;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getHospitalTypeValue() {
		return HospitalTypeValue;
	}

	public void setHospitalTypeValue(String hospitalTypeValue) {
		HospitalTypeValue = hospitalTypeValue;
	}

	public SelectValue getNetworkHospitalType() {
		return networkHospitalType;
	}

	public void setNetworkHospitalType(SelectValue networkHospitalType) {
		this.networkHospitalType = networkHospitalType;
	}

	public String getHospitalZone() {
		return hospitalZone;
	}

	public void setHospitalZone(String hospitalZone) {
		this.hospitalZone = hospitalZone;
	}

	public String getSuspiciousRemarks() {
		return suspiciousRemarks;
	}

	public void setSuspiciousRemarks(String suspiciousRemarks) {
		this.suspiciousRemarks = suspiciousRemarks;
	}

	public String getSuspiciousFlag() {
		return suspiciousFlag;
	}

	public void setSuspiciousFlag(String suspiciousFlag) {
		this.suspiciousFlag = suspiciousFlag;
	}

	public Long getNetworkHospitalTypeId() {
		return networkHospitalTypeId;
	}

	public void setNetworkHospitalTypeId(Long networkHospitalTypeId) {
		this.networkHospitalTypeId = networkHospitalTypeId;
	}

	public String getNonPreferredRemarks() {
		return nonPreferredRemarks;
	}

	public void setNonPreferredRemarks(String nonPreferredRemarks) {
		this.nonPreferredRemarks = nonPreferredRemarks;
	}

	public String getNonPreferredFlag() {
		return nonPreferredFlag;
	}

	public void setNonPreferredFlag(String nonPreferredFlag) {
		this.nonPreferredFlag = nonPreferredFlag;
	}

	public String getIsPreferredHospital() {
		return isPreferredHospital;
	}

	public void setIsPreferredHospital(String isPreferredHospital) {
		this.isPreferredHospital = isPreferredHospital;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getFinalGradeName() {
		return finalGradeName;
	}

	public void setFinalGradeName(String finalGradeName) {
		this.finalGradeName = finalGradeName;
	}	

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public UnFreezHospitals getRegistedUnFreezHospitals() {
		return registedUnFreezHospitals;
	}

	public void setRegistedUnFreezHospitals(
			UnFreezHospitals registedUnFreezHospitals) {
		this.registedUnFreezHospitals = registedUnFreezHospitals;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public String getHospitalFlag() {
		return hospitalFlag;
	}

	public void setHospitalFlag(String hospitalFlag) {
		this.hospitalFlag = hospitalFlag;
	}

	public String getSuspiciousType() {
		return suspiciousType;
	}

	public void setSuspiciousType(String suspiciousType) {
		this.suspiciousType = suspiciousType;
	}

	public String getClmPrcsInstruction() {
		return clmPrcsInstruction;
	}

	public void setClmPrcsInstruction(String clmPrcsInstruction) {
		this.clmPrcsInstruction = clmPrcsInstruction;
	}

	public String getHospitalStatus() {
		return hospitalStatus;
	}

	public void setHospitalStatus(String hospitalStatus) {
		this.hospitalStatus = hospitalStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDiscountRemark() {
		return discountRemark;
	}

	public void setDiscountRemark(String discountRemark) {
		this.discountRemark = discountRemark;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public Integer getMedibuddyFlag() {
		return medibuddyFlag;
	}

	public void setMedibuddyFlag(Integer medibuddyFlag) {
		this.medibuddyFlag = medibuddyFlag;
	}
	
	public String getFspFlag() {
		return fspFlag;
	}

	public void setFspFlag(String fspFlag) {
		this.fspFlag = fspFlag;
	}

	public String getExclusionProvideFlag() {
		return exclusionProvideFlag;
	}

	public void setExclusionProvideFlag(String exclusionProvideFlag) {
		this.exclusionProvideFlag = exclusionProvideFlag;
	}

	public String getFinalScoreDeficiency() {
		return finalScoreDeficiency;
	}

	public void setFinalScoreDeficiency(String finalScoreDeficiency) {
		this.finalScoreDeficiency = finalScoreDeficiency;
	}

	public String getExclusionProvideRemarks() {
		return exclusionProvideRemarks;
	}

	public void setExclusionProvideRemarks(String exclusionProvideRemarks) {
		this.exclusionProvideRemarks = exclusionProvideRemarks;
	}
	
	
}
