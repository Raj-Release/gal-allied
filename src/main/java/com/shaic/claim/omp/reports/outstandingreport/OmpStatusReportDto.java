package com.shaic.claim.omp.reports.outstandingreport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.dto.SearchTableDTO;

public class OmpStatusReportDto extends SearchTableDTO {
	
	private Date frmDate;
	private Date toDate;
	private SelectValue classificationSelect;
	private SelectValue subClassificationSelect;
	private SelectValue statusSelect;
	private SelectValue lossPeriodSelect;
	private SelectValue yearSelect;
	
	private List<OmpStatusReportDto> searchResultList;
	
	private String sno;
	private String intimationNo;
	private String tpaIntimationNumber;
	private String policyNo;
	private Date policyFromDate;
	private String policyFromDateValue;
	private Date policyToDate;
	private String policyToDateValue;
	private String branchOfficeCode;
	private String policyIssuingState;
	private String channelType;  // (Corp, Broker, Agent, ect)	
	private String productType;
	private Double policySumInsured;
	private Double eventCodeSumInsured;
	private String insuredName;
	private int age;
	private String eventCode;
	private String typeofClaim;
	private String natureOfClaim;
	private String ailmentLoss;
	private Date intimationDate;
	private String intimationDateValue;
	private Date admissionLossDate;
	private String admissionLossDateValue;
	private String country; // (Place Of Visit)	
	private Double initialProvisionAmount;
	private Double inrConversionRate;
	private Double inrValue;
	private Double converstionRate;
	private String claimStatus;
	private String pendingStage;

	private String rodNumber;
	private String classification;
	private String category;
	private String rodClaimType;
	private String documentReceivedFrom;
	private String currencyType;
	private Double currencyRate;
	private String conversionValue;
	private Double billAmountinFC;
	private Double amountInDoll;
	private Double deductionInDoll;
	private Double totalAmountInDoll;
	private String negotiationDone; 
	private Double agreedAmountInDoll;
	private Double totalExpenseInDoll;
	private Double finalApprovedAmountINR;
	private Double finalApprovedAmountinDoll;
	private String nameOfNegotiator;
	private Double negotiationFee;
	private String payeeName;
	private Date dateOfPayment;
	private String dateOfPaymentValue;
	private Double bankCharges;
	private String payeeCurrency;
	private String paymentType;
	private String chequeNo;
	private Date chequeDt;
	private String chequeDtValue;
	private String bankCode;
	private String bankName;
	private Date accountsApprovalDt;
	private String accountsApprovalDtValue;
	private String regDate;
	private String rejectionDate; 
	private String closureDateValue;
	private String plan;
	private String remarks;
		
	
	public Date getFrmDate() {
		return frmDate;
	}
	public void setFrmDate(Date frmDate) {
		this.frmDate = frmDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public SelectValue getStatusSelect() {
		return statusSelect;
	}
	public void setStatusSelect(SelectValue statusSelect) {
		this.statusSelect = statusSelect;
	}	
	
	public List<OmpStatusReportDto> getSearchResultList() {
		return searchResultList;
	}
	public void setSearchResultList(List<OmpStatusReportDto> searchResultList) {
		this.searchResultList = searchResultList;
	}
	public SelectValue getClassificationSelect() {
		return classificationSelect;
	}
	public void setClassificationSelect(SelectValue classificationSelect) {
		this.classificationSelect = classificationSelect;
	}
	public SelectValue getSubClassificationSelect() {
		return subClassificationSelect;
	}
	public void setSubClassificationSelect(SelectValue subClassificationSelect) {
		this.subClassificationSelect = subClassificationSelect;
	}
	public SelectValue getLossPeriodSelect() {
		return lossPeriodSelect;
	}
	public void setLossPeriodSelect(SelectValue lossPeriodSelect) {
		this.lossPeriodSelect = lossPeriodSelect;
	}
	public SelectValue getYearSelect() {
		return yearSelect;
	}
	public void setYearSelect(SelectValue yearSelect) {
		this.yearSelect = yearSelect;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getTpaIntimationNumber() {
		return tpaIntimationNumber;
	}
	public void setTpaIntimationNumber(String tpaIntimationNumber) {
		this.tpaIntimationNumber = tpaIntimationNumber;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public Date getPolicyFromDate() {
		return policyFromDate;
	}
	public void setPolicyFromDate(Date policyFromDate) {
		this.policyFromDate = policyFromDate;
	}
	public Date getPolicyToDate() {
		return policyToDate;
	}
	public void setPolicyToDate(Date policyToDate) {
		this.policyToDate = policyToDate;
	}
	public String getBranchOfficeCode() {
		return branchOfficeCode;
	}
	public void setBranchOfficeCode(String branchOfficeCode) {
		this.branchOfficeCode = branchOfficeCode;
	}
	public String getPolicyIssuingState() {
		return policyIssuingState;
	}
	public void setPolicyIssuingState(String policyIssuingState) {
		this.policyIssuingState = policyIssuingState;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public Double getPolicySumInsured() {
		return policySumInsured;
	}
	public void setPolicySumInsured(Double policySumInsured) {
		this.policySumInsured = policySumInsured;
	}
	public Double getEventCodeSumInsured() {
		return eventCodeSumInsured;
	}
	public void setEventCodeSumInsured(Double eventCodeSumInsured) {
		this.eventCodeSumInsured = eventCodeSumInsured;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public String getTypeofClaim() {
		return typeofClaim;
	}
	public void setTypeofClaim(String typeofClaim) {
		this.typeofClaim = typeofClaim;
	}
	public String getNatureOfClaim() {
		return natureOfClaim;
	}
	public void setNatureOfClaim(String natureOfClaim) {
		this.natureOfClaim = natureOfClaim;
	}
	public String getAilmentLoss() {
		return ailmentLoss;
	}
	public void setAilmentLoss(String ailmentLoss) {
		this.ailmentLoss = ailmentLoss;
	}
	public Date getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
	}
	public Date getAdmissionLossDate() {
		return admissionLossDate;
	}
	public void setAdmissionLossDate(Date admissionLossDate) {
		this.admissionLossDate = admissionLossDate;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Double getInitialProvisionAmount() {
		return initialProvisionAmount;
	}
	public void setInitialProvisionAmount(Double initialProvisionAmount) {
		this.initialProvisionAmount = initialProvisionAmount;
	}
	public Double getInrConversionRate() {
		return inrConversionRate;
	}
	public void setInrConversionRate(Double inrConversionRate) {
		this.inrConversionRate = inrConversionRate;
	}
	public Double getInrValue() {
		return inrValue;
	}
	public void setInrValue(Double inrValue) {
		this.inrValue = inrValue;
	}
	public Double getConverstionRate() {
		return converstionRate;
	}
	public void setConverstionRate(Double converstionRate) {
		this.converstionRate = converstionRate;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getPendingStage() {
		return pendingStage;
	}
	public void setPendingStage(String pendingStage) {
		this.pendingStage = pendingStage;
	}
	public String getPolicyFromDateValue() {
		policyFromDateValue = policyFromDate != null ? new SimpleDateFormat("dd-MM-yyyy").format(policyFromDate) : "";
		return policyFromDateValue;
	}
	public String getPolicyToDateValue() {
		policyToDateValue = policyToDate != null ? new SimpleDateFormat("dd-MM-yyyy").format(policyToDate) : "";
		return policyToDateValue;
	}
	public String getIntimationDateValue() {
		intimationDateValue = intimationDate != null ? new SimpleDateFormat("dd-MM-yyyy").format(intimationDate) : "";
		return intimationDateValue;
	}
	public String getAdmissionLossDateValue() {
		admissionLossDateValue = admissionLossDate != null ? new SimpleDateFormat("dd-MM-yyyy").format(admissionLossDate) : "";
		return admissionLossDateValue;
	}
	public void setPolicyFromDateValue(String policyFromDateValue) {
		this.policyFromDateValue = policyFromDateValue;
	}
	public void setPolicyToDateValue(String policyToDateValue) {
		this.policyToDateValue = policyToDateValue;
	}
	public void setIntimationDateValue(String intimationDateValue) {
		this.intimationDateValue = intimationDateValue;
	}
	public void setAdmissionLossDateValue(String admissionLossDateValue) {
		this.admissionLossDateValue = admissionLossDateValue;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getRodNumber() {
		return rodNumber;
	}
	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getRodClaimType() {
		return rodClaimType;
	}
	public void setRodClaimType(String rodClaimType) {
		this.rodClaimType = rodClaimType;
	}
	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public Double getCurrencyRate() {
		return currencyRate;
	}
	public void setCurrencyRate(Double currencyRate) {
		this.currencyRate = currencyRate;
	}
	public String getConversionValue() {
		return conversionValue;
	}
	public void setConversionValue(String conversionValue) {
		this.conversionValue = conversionValue;
	}
	public Double getBillAmountinFC() {
		return billAmountinFC;
	}
	public void setBillAmountinFC(Double billAmountinFC) {
		this.billAmountinFC = billAmountinFC;
	}
	public Double getAmountInDoll() {
		return amountInDoll;
	}
	public void setAmountInDoll(Double amountInDoll) {
		this.amountInDoll = amountInDoll;
	}
	public Double getDeductionInDoll() {
		return deductionInDoll;
	}
	public void setDeductionInDoll(Double deductionInDoll) {
		this.deductionInDoll = deductionInDoll;
	}
	public Double getTotalAmountInDoll() {
		return totalAmountInDoll;
	}
	public void setTotalAmountInDoll(Double totalAmountInDoll) {
		this.totalAmountInDoll = totalAmountInDoll;
	}
	public String getNegotiationDone() {
		return negotiationDone;
	}
	public void setNegotiationDone(String negotiationDone) {
		this.negotiationDone = negotiationDone;
	}
	public Double getAgreedAmountInDoll() {
		return agreedAmountInDoll;
	}
	public void setAgreedAmountInDoll(Double agreedAmountInDoll) {
		this.agreedAmountInDoll = agreedAmountInDoll;
	}
	public Double getTotalExpenseInDoll() {
		return totalExpenseInDoll;
	}
	public void setTotalExpenseInDoll(Double totalExpenseInDoll) {
		this.totalExpenseInDoll = totalExpenseInDoll;
	}
	public Double getFinalApprovedAmountINR() {
		return finalApprovedAmountINR;
	}
	public void setFinalApprovedAmountINR(Double finalApprovedAmountINR) {
		this.finalApprovedAmountINR = finalApprovedAmountINR;
	}
	public Double getFinalApprovedAmountinDoll() {
		return finalApprovedAmountinDoll;
	}
	public void setFinalApprovedAmountinDoll(Double finalApprovedAmountinDoll) {
		this.finalApprovedAmountinDoll = finalApprovedAmountinDoll;
	}
	public String getNameOfNegotiator() {
		return nameOfNegotiator;
	}
	public void setNameOfNegotiator(String nameOfNegotiator) {
		this.nameOfNegotiator = nameOfNegotiator;
	}
	public Double getNegotiationFee() {
		return negotiationFee;
	}
	public void setNegotiationFee(Double negotiationFee) {
		this.negotiationFee = negotiationFee;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public Date getDateOfPayment() {
		return dateOfPayment;
	}
	public void setDateOfPayment(Date dateOfPayment) {
		this.dateOfPayment = dateOfPayment;
	}
	public String getDateOfPaymentValue() {
		dateOfPaymentValue = dateOfPayment != null ? new SimpleDateFormat("dd-MM-yyyy").format(dateOfPayment): "";
		return dateOfPaymentValue;
	}
	public void setDateOfPaymentValue(String dateOfPaymentValue) {
		this.dateOfPaymentValue = dateOfPaymentValue;
	}
	public Double getBankCharges() {
		return bankCharges;
	}
	public void setBankCharges(Double bankCharges) {
		this.bankCharges = bankCharges;
	}
	public String getPayeeCurrency() {
		return payeeCurrency;
	}
	public void setPayeeCurrency(String payeeCurrency) {
		this.payeeCurrency = payeeCurrency;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public Date getChequeDt() {
		return chequeDt;
	}
	public void setChequeDt(Date chequeDt) {
		this.chequeDt = chequeDt;
	}
	public String getChequeDtValue() {
		chequeDtValue = chequeDt != null ? new SimpleDateFormat("dd-MM-yyyy").format(chequeDt): "";
		return chequeDtValue;
	}
	public void setChequeDtValue(String chequeDtValue) {
		this.chequeDtValue = chequeDtValue;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Date getAccountsApprovalDt() {
		return accountsApprovalDt;
	}
	public void setAccountsApprovalDt(Date accountsApprovalDt) {
		this.accountsApprovalDt = accountsApprovalDt;
	}
	public String getAccountsApprovalDtValue() {
		accountsApprovalDtValue = accountsApprovalDt != null ? new SimpleDateFormat("dd-MM-yyyy").format(accountsApprovalDt): "";
		return accountsApprovalDtValue;
	}
	public void setAccountsApprovalDtValue(String accountsApprovalDtValue) {
		this.accountsApprovalDtValue = accountsApprovalDtValue;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getRejectionDate() {
		return rejectionDate;
	}
	public void setRejectionDate(String rejectionDate) {
		this.rejectionDate = rejectionDate;
	}
	public String getClosureDateValue() {
		return closureDateValue;
	}
	public void setClosureDateValue(String closureDateValue) {
		this.closureDateValue = closureDateValue;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}			
}
