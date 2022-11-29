package com.shaic.claim.omp.searchregistration;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.omp.registration.OMPBalanceSiTableDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class OMPSearchClaimRegistrationTableDTO extends AbstractTableDTO  implements Serializable{
	
	
	

	private String intimationNo;
	
	private Long intimationKey;
	
	private String policyno;
	
	private String productcode;
	
	private Date intimationDate;
	
	private NewIntimationDto newIntimationDto;
	
	private SelectValue type;
	
	private String loss;
	
	private Date lossdate;
	
	private Date admissiondate;

	private Map<String, String> popupMap;
	
	private Map<String,String> suspiciousPopUp;
	
	private Boolean isProceedFurther = true;
	
	private Boolean isCancelledPolicy = false;
	
	private Double claimedAmount = 0d;
	
	private String userId;
	
	private String password;
	
	private String hospitalType;
	
	private Double inrConversionRate;
	
	private Double initlProvisionAmt;
	
	private Double inrValue;
	
	private String eventCode;
	
	private String hospitalName;
	
	private String hospitalCity;
	
	private SelectValue hospitalCountry;
	
	private String ailmentLoss;
	
	private String claimType;
	
	private Long claimtypeId;
	
	private String hospType;
	
	private String naemOfInsured;
	
	private String productName;
	
	private SelectValue cmbclaimType;
	
	private String hospitalisationFlag;
	
	private String nonHospitalisationFlag;
	
	private Long countryId;
	
	private OMPBalanceSiTableDTO ompBalanceSiTableDto;
	
	private ClaimDto claimDto;
	
	public OMPSearchClaimRegistrationTableDTO(){
		this.ompBalanceSiTableDto = new OMPBalanceSiTableDTO();
	}
	
	public Double getClaimedAmount() {
		return claimedAmount;
	}
	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getIntimationNo() {
		return intimationNo;
	}


	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}


	public Long getIntimationKey() {
		return intimationKey;
	}


	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	
	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}
	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
		this.newIntimationDto.setPolicy(newIntimationDto.getPolicy());
	}
	
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	
	public String getProductcode() {
		return productcode;
	}
	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	public Date getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
	}
	
	public SelectValue getType() {
		return type;
	}
	public void setType(SelectValue type) {
		this.type = type;
	}
		
	
	public String getLoss() {
		return loss;
	}
	public void setLoss(String loss) {
		this.loss = loss;
	}
	
	public Date getLossdate() {
		return lossdate;
	}
	public void setLossdate(Date lossdate) {
		this.lossdate = lossdate;
	}
	
	
	public Date getAdmissiondate() {
		return admissiondate;
	}
	public void setAdmissiondate(Date admissiondate) {
		this.admissiondate = admissiondate;
	}
	

	public Map<String, String> getPopupMap() {
		return popupMap;
	}
	public void setPopupMap(Map<String, String> popupMap) {
		this.popupMap = popupMap;
	}
	public Map<String, String> getSuspiciousPopUp() {
		return suspiciousPopUp;
	}
	public void setSuspiciousPopUp(Map<String, String> suspiciousPopUp) {
		this.suspiciousPopUp = suspiciousPopUp;
	}
	
	public Boolean getIsProceedFurther() {
		return isProceedFurther;
	}
	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public void setIsProceedFurther(Boolean isProceedFurther) {
		this.isProceedFurther = isProceedFurther;
	}
	public Boolean getIsCancelledPolicy() {
		return isCancelledPolicy;
	}
	public void setIsCancelledPolicy(Boolean isCancelledPolicy) {
		this.isCancelledPolicy = isCancelledPolicy;
	}
/*	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}*/
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public OMPBalanceSiTableDTO getOmpBalanceSiTableDto() {
		return ompBalanceSiTableDto;
	}

	public void setOmpBalanceSiTableDto(OMPBalanceSiTableDTO ompBalanceSiTableDto) {
		this.ompBalanceSiTableDto = ompBalanceSiTableDto;
	}

	public Double getInrConversionRate() {
		return inrConversionRate;
	}

	public void setInrConversionRate(Double inrConversionRate) {
		this.inrConversionRate = inrConversionRate;
	}

	public Double getInitlProvisionAmt() {
		return initlProvisionAmt;
	}

	public void setInitlProvisionAmt(Double initlProvisionAmt) {
		this.initlProvisionAmt = initlProvisionAmt;
	}

	public Double getInrValue() {
		return inrValue;
	}

	public void setInrValue(Double inrValue) {
		this.inrValue = inrValue;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

//	public String getHospitalCountry() {
//		return hospitalCountry;
//	}
//
//	public void setHospitalCountry(String hospitalCountry) {
//		this.hospitalCountry = hospitalCountry;
//	}

	public String getAilmentLoss() {
		return ailmentLoss;
	}

	public void setAilmentLoss(String ailmentLoss) {
		this.ailmentLoss = ailmentLoss;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getHospType() {
		return hospType;
	}

	public void setHospType(String hospType) {
		this.hospType = hospType;
	}

	public String getNaemOfInsured() {
		return naemOfInsured;
	}

	public void setNaemOfInsured(String naemOfInsured) {
		this.naemOfInsured = naemOfInsured;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public SelectValue getHospitalCountry() {
		return hospitalCountry;
	}

	public void setHospitalCountry(SelectValue hospitalCountry) {
		this.hospitalCountry = hospitalCountry;
	}

	public SelectValue getCmbclaimType() {
		return cmbclaimType;
	}

	public void setCmbclaimType(SelectValue cmbclaimType) {
		this.cmbclaimType = cmbclaimType;
	}

	public String getHospitalisationFlag() {
		return hospitalisationFlag;
	}

	public void setHospitalisationFlag(String hospitalisationFlag) {
		this.hospitalisationFlag = hospitalisationFlag;
	}

	public String getNonHospitalisationFlag() {
		return nonHospitalisationFlag;
	}

	public void setNonHospitalisationFlag(String nonHospitalisationFlag) {
		this.nonHospitalisationFlag = nonHospitalisationFlag;
	}

	public Long getClaimtypeId() {
		return claimtypeId;
	}

	public void setClaimtypeId(Long climtypeId) {
		this.claimtypeId = climtypeId;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	
}
