package com.shaic.claim.ompviewroddetails;

import java.util.Date;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.intimation.CashLessTableDTO;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.claim.viewEarlierRodDetails.ClaimStatusRegistrationDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;

public class OMPViewClaimStatusDto extends AbstractSearchDTO{
	
	
	private static final long serialVersionUID = 1L;
	
	// intimation Details
	private Long key;
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}

	private Long claimKey;
	
	private Long intimationKey;
	
	private Long rodKey;
	
	private String status;
	
	private String patientName;
	
	private String hospitalName;
	
	private String intimationId;
	
	private String intimationDate;
	
	private Date lossDate;
	
	private String tpaIntimationNo;
	
	private String insuredName;
	
	private String ailmentOrLoss;
	
	private String eventCode;
	
	private String placeofVisit;
	
	private String modeOfIntimation;
	
	private String intimatedBy;
	
	private String intimatorName;
	
	private String callerContactNo;
	
	private String intialProvisionAmt;
	
	private String inrConversionRate;
	
	private String inrValue;
	
	private String policyNo;
	
	private String policyIssuingOffice;
	
	private String productName;
	
	private String state;
	
	private String city;
	
	private Date admissionDate;
	
	private String hospitalCode;
	
	private String hospitalCity;
	
	private String country;
	
	private String remarks;
	
	private String smCode;
	
	private String smName;
	
	private String agentOrBrokerCode;
	
	private String agentOrBrokerName;
	
	private String ailment;
	
	private Boolean claimTypeBooleanval;
	
	private Boolean hospTypeBooleanval;
	
	private String hospitalisationFlag;
	
	private String nonHospitalisationFlag;
	
	private SelectValue claimTypeValue;
	
	// Registration Details
	private OMPClaimStatusRegistrationDto ompclaimStatusRegistrionDetails;
	
	private CashLessTableDTO cashlessTableDetails;
	
	private List<ViewDocumentDetailsDTO> receiptOfDocumentValues;
	
	private ClaimStatusRegistrationDTO claimStatusRegistrionDetails;
	
	private List<PreviousPreAuthTableDTO> previousPreAuthTableDTO;
	
	public String getAilment(){
		return ailment;
	}
	public void setAilment(String ailment){
		this.ailment = ailment;
	}
	public String getIntimationId(){
		return intimationId;
	}
	public void setIntimationId(String intimationId){
		this.intimationId = intimationId;
	}
	
	public String getIntimationDate(){
		return intimationDate;
	}
	public void setIntimationDate(String intimationDate){
		this.intimationDate = intimationDate;
	}
	public Date getLossDate(){
		return lossDate;
	}
	public void setLossDate(Date lossDate){
		this.lossDate = lossDate;
	}
	public String getTpaIntimationNo(){
		return tpaIntimationNo;
	}
	public void setTpaIntimationNo(String tpaIntimationNo){
		this.tpaIntimationNo = tpaIntimationNo;
	}
	public String getInsuredName(){
		return insuredName;
	}
	public void setInsuredName(String insuredName){
		this.insuredName = insuredName;
	}
	public String getAilmentOrLoss(){
		return ailmentOrLoss;
	}
	public void setAilmentOrLoss(String ailmentOrLoss){
		this.ailmentOrLoss = ailmentOrLoss;
	}
	public String getEventCode(){
		return eventCode;
	}
	public void setEventCode(String eventCode){
		this.eventCode = eventCode;
	}
	public String getPlaceofVisit(){
		return placeofVisit;
	}
	public void setPlaceofVisit(String placeofVisit){
		this.placeofVisit = placeofVisit;
	}
	public String getModeOfIntimation(){
		return modeOfIntimation;
	}
	public void setModeOfIntimation(String modeOfIntimation){
		this.modeOfIntimation = modeOfIntimation;
	}
	public String getIntimatedBy(){
		return intimatedBy;
	}
	public void setIntimatedBy(String intimatedBy){
		this.intimatedBy = intimatedBy;
	}
	public String getIntimatorName(){
		return intimatorName;
	}
	public void setIntimatorName(String intimatorName){
		this.intimatorName = intimatorName;
	}
	public String getCallerContactNo(){
		return callerContactNo;
	}
	public void setCallerContactNo(String callerContactNo){
		this.callerContactNo = callerContactNo;
	}
	public String getIntialProvisionAmt(){
		return intialProvisionAmt;
	}
	public void setIntialProvisionAmt(String intialProvisionAmt){
		this.intialProvisionAmt = intialProvisionAmt ;
	}
	public String getInrConversionRate(){
		return inrConversionRate;
	}
	public void setInrConversionRate(String inrConversionRate){
		this.inrConversionRate = inrConversionRate;
	}
	public String getInrValue(){
		return inrValue;
	}
	public void setInrValue(String inrValue){
		this.inrValue = inrValue ;
	}
	public String getPolicyNo(){
		return policyNo;
	}
	public void setPolicyNo(String policyNo){
		this.policyNo = policyNo ;
	}
	public String getPolicyIssuingOffice(){
		return policyIssuingOffice;
	}
	public void setPolicyIssuingOffice(String policyIssuingOffice){
		this.policyIssuingOffice = policyIssuingOffice ;
	}
	public String getProductName(){
		return productName;
	}
	public void setProductName(String productName){
		this.productName = productName ;
	}
	public String getState(){
		return state;
	}
	public void setState(String state){
		this.state = state;
	}
	public String getCity(){
		return city;
	}
	public void setCity(String city){
		this.city = city;
	}
	public Date getAdmissionDate(){
		return admissionDate;
	}
	public void setAdmissionDate(Date admissionDate){
		this.admissionDate = admissionDate;
	}
	public String getHospitalCode(){
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode){
		this.hospitalCode = hospitalCode ;
	}
	public String getHospitalCity(){
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity){
		this.hospitalCity = hospitalCity;
	}
	public String getCountry(){
		return country;
	}
	public void setCountry(String Country){
		this.country = country;
	}
	public String getRemarks(){
		return remarks;
	}
	public void setRemarks(String remarks){
		this.remarks = remarks;
	}
	public String getSmCode(){
		return smCode;
	}
	public void setSmCode(String smCode){
		this.smCode = smCode;
	}
	public String getSmName(){
		return smName;
	}
	public void setSmName(String smName){
		this.smName = smName ;
	}
	public String getAgentOrBrokerCode(){
		return agentOrBrokerCode;
	}
	public void setAgentOrBrokerCode(String agentOrBrokerCode){
		this.agentOrBrokerCode = agentOrBrokerCode;
	}
	public String getAgentOrBrokerName(){
		return agentOrBrokerName;
	}
	public void setAgentOrBrokerName(String agentOrBrokerName){
		this.agentOrBrokerName = agentOrBrokerName ;
	}
	
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public OMPClaimStatusRegistrationDto getOmpclaimStatusRegistrionDetails() {
		return ompclaimStatusRegistrionDetails;
	}
	public void setOmpclaimStatusRegistrionDetails(
			OMPClaimStatusRegistrationDto ompclaimStatusRegistrionDetails) {
		this.ompclaimStatusRegistrionDetails = ompclaimStatusRegistrionDetails;
	}
	public CashLessTableDTO getCashlessTableDetails() {
		return cashlessTableDetails;
	}
	public void setCashlessTableDetails(CashLessTableDTO cashlessTableDetails) {
		this.cashlessTableDetails = cashlessTableDetails;
	}
	public List<ViewDocumentDetailsDTO> getReceiptOfDocumentValues() {
		return receiptOfDocumentValues;
	}
	public void setReceiptOfDocumentValues(
			List<ViewDocumentDetailsDTO> receiptOfDocumentValues) {
		this.receiptOfDocumentValues = receiptOfDocumentValues;
	}
	public ClaimStatusRegistrationDTO getClaimStatusRegistrionDetails() {
		return claimStatusRegistrionDetails;
	}
	public void setClaimStatusRegistrionDetails(
			ClaimStatusRegistrationDTO claimStatusRegistrionDetails) {
		this.claimStatusRegistrionDetails = claimStatusRegistrionDetails;
	}
	public List<PreviousPreAuthTableDTO> getPreviousPreAuthTableDTO() {
		return previousPreAuthTableDTO;
	}
	public void setPreviousPreAuthTableDTO(
			List<PreviousPreAuthTableDTO> previousPreAuthTableDTO) {
		this.previousPreAuthTableDTO = previousPreAuthTableDTO;
	}
	
	
	public Boolean getClaimTypeBooleanval() {
		return claimTypeBooleanval;
	}
	public void setClaimTypeBooleanval(Boolean claimTypeBooleanval) {
		this.claimTypeBooleanval = claimTypeBooleanval;
	}
	public Boolean getHospTypeBooleanval() {
		return hospTypeBooleanval;
	}
	public void setHospTypeBooleanval(Boolean hospTypeBooleanval) {
		this.hospTypeBooleanval = hospTypeBooleanval;
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
	public SelectValue getClaimTypeValue() {
		return claimTypeValue;
	}
	public void setClaimTypeValue(SelectValue claimTypeValue) {
		this.claimTypeValue = claimTypeValue;
	}
	
}
