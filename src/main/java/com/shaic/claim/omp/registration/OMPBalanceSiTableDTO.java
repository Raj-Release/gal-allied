package com.shaic.claim.omp.registration;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class OMPBalanceSiTableDTO  extends AbstractTableDTO  implements Serializable{
	
	
	private String coverCode;
	private String coverCodeDescription;
	private Long sumInsured;
	private Double claimPaid;
	private Double claimOustanding;
	private Double balance;
	private Double provisionforcurrentclaim;
	private Double bSIafterProvision;
	private Double inrConversionRate;
	private Double initlProvisionAmt;
	private Double inrValue;
	private String eventCode;
	private String hospitalName;
	private String hospitalCity;
	private String hospitalCountry;
	private String ailmentLoss;
	private String claimType;
	private String hospType;
	private String naemOfInsured;
	private String productName;
	private Long intimationKey;
	private  NewIntimationDto newIntimationDto;
	
	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}
	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
		this.newIntimationDto.setPolicy(newIntimationDto.getPolicy());
	}
	public String getCoverCode(){
		return coverCode;
	}
	public void setCoverCode(String coverCode){
		this.coverCode = coverCode;
	}
	public String getCoverCodeDescription(){
		return coverCodeDescription;
	}
	public void setCoverCodeDescription(String coverCodeDescription){
		this.coverCodeDescription = coverCodeDescription;
	}
	public Long getSumInsured(){
		return sumInsured;
	}
	public void setSumInsured(Long sumInsured){
		this.sumInsured = sumInsured;
	}

	public Double getClaimPaid() {
		return claimPaid;
	}
	public void setClaimPaid(Double claimPaid) {
		this.claimPaid = claimPaid;
	}
	public Double getClaimOustanding() {
		return claimOustanding;
	}
	public void setClaimOustanding(Double claimOustanding) {
		this.claimOustanding = claimOustanding;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getProvisionforcurrentclaim() {
		return provisionforcurrentclaim;
	}
	public void setProvisionforcurrentclaim(Double provisionforcurrentclaim) {
		this.provisionforcurrentclaim = provisionforcurrentclaim;
	}
	public Double getbSIafterProvision() {
		return bSIafterProvision;
	}
	public void setbSIafterProvision(Double bSIafterProvision) {
		this.bSIafterProvision = bSIafterProvision;
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
	public String getHospitalCountry() {
		return hospitalCountry;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
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
	public void setHospitalCountry(String hospitalCountry) {
		this.hospitalCountry = hospitalCountry;
	}
	public String getAilmentLoss() {
		return ailmentLoss;
	}
	public void setAilmentLoss(String ailmentLoss) {
		this.ailmentLoss = ailmentLoss;
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
}
