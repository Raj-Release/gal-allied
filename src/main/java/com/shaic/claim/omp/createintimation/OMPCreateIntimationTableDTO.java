package com.shaic.claim.omp.createintimation;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.domain.Insured;
import com.shaic.domain.Policy;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class OMPCreateIntimationTableDTO extends AbstractTableDTO  implements Serializable{
	
	
	private static final long serialVersionUID = -3620185077139436028L;
	
	private Long intimationKey;
	private String intimationno;
	private String policyNo;
	private String claimno;
	private String insuredName;
	private Date intimationdate;
	private String intimationdateString;
	private String intimatername;
	private SelectValue intimatedby;
	private SelectValue intimaticmode;
	private String hospitalname;
	private String status;
	
	private Long sno;
	//private String policyno;
	private String proposername;
	private String productCodeOrName;
	private Double sumInsured;
	private String plan;
	private String passportNo;
	private Date policyCoverPeriodFromDate;
	private Date policyCoverPeriodToDate;
	private String policyCoverFromDateStr;
	private String policyCoverToDateStr;
	
	private Date lossDateTime;
	private String tpaIntimationNumber;
	private SelectValue eventCode;
	private String placeOfVisit;
	private String placeOfLossDelay;
	private String sponsorName;
	private String contactNumber;
	private Double initProvisionAmount;
	private Double inrConversionRate;
	private Double inrTotalAmount;
	private String ailmentLoss;
	private Date admissionDate;
	private Date dateOfDischarge;
	private String cityName;
	private String countryName;
	private String remarks;
	private Long countryId;
	private Policy policy;
	private Insured insured;
	
	private String hospitalFlag;
	private String nonHospitalFlag;	
	private SelectValue claimType;
	private Long lobId;
	
	private Double balanceSI;
	
	private ClaimDto claimDto;
	
	private NewIntimationDto intimationDto;
	
	private String placeofAccidentOrEvent;
	
	private Date lossTime;
	
	private String lossDetails;
	
	private SelectValue hospitalId;
	
	//R1276
	private Double intimationClmAmount;
	private String intimationRemarks;
	
	public Long getSno(){
		return sno;
	}
	public void setSno(Long sno){
		this.sno = sno;
	}
	public String getProposername(){
		return proposername;
	}
	public void setProposername(String proposername){
		this.proposername = proposername;
	}
	public String getProductCodeOrName(){
		return productCodeOrName;
	}
	public void setProductCodeOrName(String productCodeOrName){
		this.productCodeOrName = productCodeOrName;
	}
	public Double getSumInsured(){
		return sumInsured;
	}
	public void setSumInsured(Double sumInsured){
		this.sumInsured = sumInsured;
	}
	public String getPlan(){
		return plan;
	}
	public void setPlan(String plan){
		this.plan = plan;
	}
	public String getPassportNo(){
		return passportNo;
	}
	public void setPassportNo(String passportNo){
		this.passportNo = passportNo;
	}
	public Date getPolicyCoverPeriodFromDate(){
		return policyCoverPeriodFromDate;
	}
	public void setPolicyCoverPeriodFromDate(Date policyCoverPeriodFromDate){
		this.policyCoverPeriodFromDate = policyCoverPeriodFromDate;
	}
	public Date getPolicyCoverPeriodToDate(){
		return policyCoverPeriodToDate;
	}
	public void setPolicyCoverPeriodToDate(Date policyCoverPeriodToDate){
		this.policyCoverPeriodToDate = policyCoverPeriodToDate;
	}
	public String getIntimationno() {
		return intimationno;
	}
	public void setIntimationno(String intimationno) {
		this.intimationno = intimationno;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getClaimno() {
		return claimno;
	}
	public void setClaimno(String claimno) {
		this.claimno = claimno;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public Date getIntimationdate() {
		return intimationdate;
	}
	public void setIntimationdate(Date intimationdate) {
		this.intimationdate = intimationdate;
	}
	public String getIntimatername() {
		return intimatername;
	}
	public void setIntimatername(String intimatername) {
		this.intimatername = intimatername;
	}
	public SelectValue getIntimatedby() {
		return intimatedby;
	}
	public void setIntimatedby(SelectValue intimatedby) {
		this.intimatedby = intimatedby;
	}
	public SelectValue getIntimaticmode() {
		return intimaticmode;
	}
	public void setIntimaticmode(SelectValue intimaticmode) {
		this.intimaticmode = intimaticmode;
	}
	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Date getLossDateTime() {
		return lossDateTime;
	}
	public void setLossDateTime(Date lossDateTime) {
		this.lossDateTime = lossDateTime;
	}
	public String getTpaIntimationNumber() {
		return tpaIntimationNumber;
	}
	public void setTpaIntimationNumber(String tpaIntimationNumber) {
		this.tpaIntimationNumber = tpaIntimationNumber;
	}
	public SelectValue getEventCode() {
		return eventCode;
	}
	public void setEventCode(SelectValue eventCode) {
		this.eventCode = eventCode;
	}
	public String getPlaceOfVisit() {
		return placeOfVisit;
	}
	public void setPlaceOfVisit(String placeOfVisit) {
		this.placeOfVisit = placeOfVisit;
	}
	public String getPlaceOfLossDelay() {
		return placeOfLossDelay;
	}
	public void setPlaceOfLossDelay(String placeOfLossDelay) {
		this.placeOfLossDelay = placeOfLossDelay;
	}
	public String getSponsorName() {
		return sponsorName;
	}
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Double getInitProvisionAmount() {
		return initProvisionAmount;
	}
	public void setInitProvisionAmount(Double initProvisionAmount) {
		this.initProvisionAmount = initProvisionAmount;
	}
	public Double getInrConversionRate() {
		return inrConversionRate;
	}
	public void setInrConversionRate(Double inrConversionRate) {
		this.inrConversionRate = inrConversionRate;
	}
	public Double getInrTotalAmount() {
		return inrTotalAmount;
	}
	public void setInrTotalAmount(Double inrTotalAmount) {
		this.inrTotalAmount = inrTotalAmount;
	}
	public String getAilmentLoss() {
		return ailmentLoss;
	}
	public void setAilmentLoss(String ailmentLoss) {
		this.ailmentLoss = ailmentLoss;
	}
	public Date getAdmissionDate() {
		return admissionDate;
	}
	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}
	public Date getDateOfDischarge() {
		return dateOfDischarge;
	}
	public void setDateOfDischarge(Date dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Policy getPolicy() {
		return policy;
	}
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	public Insured getInsured() {
		return insured;
	}
	public void setInsured(Insured insured) {
		this.insured = insured;
	}
	public String getHospitalFlag() {
		return hospitalFlag;
	}
	public void setHospitalFlag(String hospitalFlag) {
		this.hospitalFlag = hospitalFlag;
	}
	public String getNonHospitalFlag() {
		return nonHospitalFlag;
	}
	public void setNonHospitalFlag(String nonHospitalFlag) {
		this.nonHospitalFlag = nonHospitalFlag;
	}
	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	public Long getLobId() {
		return lobId;
	}
	public void setLobId(Long lobId) {
		this.lobId = lobId;
	}
	public NewIntimationDto getIntimationDto() {
		return intimationDto;
	}
	public void setIntimationDto(NewIntimationDto intimationDto) {
		this.intimationDto = intimationDto;
	}
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public String getPlaceofAccidentOrEvent() {
		return placeofAccidentOrEvent;
	}
	public void setPlaceofAccidentOrEvent(String placeofAccidentOrEvent) {
		this.placeofAccidentOrEvent = placeofAccidentOrEvent;
	}
	public Date getLossTime() {
		return lossTime;
	}
	public void setLossTime(Date lossTime) {
		this.lossTime = lossTime;
	}
	public String getLossDetails() {
		return lossDetails;
	}
	public void setLossDetails(String lossDetails) {
		this.lossDetails = lossDetails;
	}
	public SelectValue getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(SelectValue hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getIntimationdateString() {
		return intimationdateString;
	}
	public void setIntimationdateString(String intimationdateString) {
		this.intimationdateString = intimationdateString;
	}
	public String getPolicyCoverToDateStr() {
		return policyCoverToDateStr;
	}
	public void setPolicyCoverToDateStr(String policyCoverToDateStr) {
		this.policyCoverToDateStr = policyCoverToDateStr;
	}
	public String getPolicyCoverFromDateStr() {
		return policyCoverFromDateStr;
	}
	public void setPolicyCoverFromDateStr(String policyCoverFromDateStr) {
		this.policyCoverFromDateStr = policyCoverFromDateStr;
	}
	public Double getBalanceSI() {
		return balanceSI;
	}
	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}
	
	//R1276
	public Double getIntimationClmAmount() {
		return intimationClmAmount;
	}
	public void setIntimationClmAmount(Double intimationClmAmount) {
		this.intimationClmAmount = intimationClmAmount;
	}
	public String getIntimationRemarks() {
		return intimationRemarks;
	}
	public void setIntimationRemarks(String intimationRemarks) {
		this.intimationRemarks = intimationRemarks;
	}
}
