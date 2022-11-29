package com.shaic.claim.reimbursement.createandsearchlot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.leagalbilling.LegalBillingDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.domain.Hospitals;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;

public class CreateAndSearchLotTableDTO extends AbstractTableDTO  implements Serializable{
	
	
	//private String chxBox;
	/*private String editDetails;
	private String viewDetails;*/
	private ClaimDto claimDto;
	private String intimationNo;
	private String claimNo;
	private String policyNo;	
	private String rodNo;
	private String paymentStatus;
	private String product;
	private String cpuCode;
	private SelectValue paymentType;
	private String ifscCode;
	private String beneficiaryAcntNo;
	private String branchName;
	private String typeOfClaim;
	private Double approvedAmt;
	private SelectValue payeeName;
	private String payeeNameStr;
	private String payableAt;
	private String panNo;
	private String providerCode;
	private String emailID;	
	private String accountBatchNo;
	private String lotNo;
	private Double serviceTax;
	private Long totalAmnt;
	private Long tdsAmt;
	private Long netAmnt;
	private Long tdsPercentage;
	private Long refNo;
	private Date paymentReqDt;
	private String paymentReqDateValue;
	private String paymentReqUID;
	
	private String checkBoxStatus;
	
	private Boolean paymentMode;
	
	private String reasonForChange;
	
	private String legalFirstName;
	
	private String bankName;
	
	private String city;
	
	private Long claimPaymentKey;
	
	
	private SelectValue cmbPayeeName;
	
	private String hospitalCode;

	private String hospitalName;
	
	
	private String paymentModeFlag;

	private Boolean chkSelect;
	
	private SelectValue zone;
	
	private String pioCode;
	
	private String zonalMailId;
	
	private String userId;
	
	private Integer claimCount;
	
	private Double sumOfApprovedAndServiceTax;
	
	private Boolean verificationClicked = false;
	
	private List<VerificationAccountDeatilsTableDTO> VerificationAccountDeatilsTableDTO;
	
	private List<VerificationAccountDeatilsTableDTO> paidAccountDeatilsTableDTO;
	
	private String chequeNo;
	//private string bankName;
	private Date chequeDate;
	private Long bankCode;
	private Date faApprovedDate;
	private String faApprovedDateValue;	
	private long paymentStatusKey;		
	private Integer numberofdays;
	private Double intrestRate;	
	private Double interestRateForCalculation;
	private Double intrestAmount;
	private String remarks;
	private String remarksForValidation;
	private Date lastAckDate;
	private String lastAckDateValue;
	private Date nextDayOfFaApprovedDate;
	private String nextDayOfFaApprovedDateValue;
	private Double faApprovedAmnt;	
	private Integer irdaTAT;
	private Double penalTotalAmnt;
	private String reconsiderationFlag;
	private Integer noofdaysexceeding;
	private Integer noOfDiffDays;
	private String docReceivedFrom;
	private Integer noOfDaysExceedingforCalculation;
	private Double penalInterestAmntForCalculation;
	private Double interestAmntForCalculation;
	private Integer serialNo;
	private String  dbSideRemark;
	private String batchProcessFlag;
	private Integer previousPage;
	private Integer nextPage;
	
	private Hospitals hospitals;
	
	private String remarksForHold;
	
	private String changedPaymentModeFlag;
	
	private String changedPaymentMode;
	
	private String payModeChangeReason;
	private Boolean isFvrOrInvesInitiated;
	
	private String gmcEmployeeName;
	
	private String gmcProposerName;
	
	private String productName;
	
	private String cpuName;
	
	private String paymentTypeValue;
	
	private SelectValue paymentCpuCode;
	private String paymentCpuName;
	
	private String docVerifiedValue = "Yes";
	
	private List<PreviousAccountDetailsDTO> previousAccntDetailsList;
	
	private String claimType;
	private String paymentCpuString;
	
	private Long documentReceivedFromId;
	
	private Boolean isInsured = Boolean.FALSE;
	
	private String reasonForChangePayee;
	
	private BeanItemContainer<SelectValue> payeeNameContainer;
	
	private Button searchButton;
	
	private Button payableAtButton;
	
	private String bancsPriorityFlag;
	
	private String batchNumber;
	
	private String hospitalPayableAt;
	
	private String hospitalPayableName;
	
	private SelectValue tempPayeeName;
	
	private SelectValue tempPaymentType;
	
	private Boolean isPaymentCpuCodeListenerEnable = false;
	
	private Boolean isNEFTDetailsAvailable = false;
	 
	private Boolean isNEFTDetailsAvailableinDMS = false;
	
	private String dbEmailId;
	
	private Double sumOfApproveIntAmnt;
	
	private String paymentCpucodeTextValue;
	
	private List<ViewSearchCriteriaTableDTO> payeeNameList;
	private Boolean isRecordExceed = false;
	
	private Long cpuCodeCount;
	
	private Integer totalCpuCodeCount;
	
	private Long cashlessCount;
	
	private Long reimbursementCount;
	
	private Long cashlessTotalCount;
	private Long reimbursementTotalCount;
	
	private String paymentCpuCodeName;
	
	private String patientName;
	
	private String saveFlag;
	
	private String paymentPolicyType;
	
	private Button previousAccntDtlsBtn;
	
	private String recordCount;
	
	private String micrCode;
	
	private String virtualPaymentProcess;
	
	private String sourceRskID;
	
	private ReceiptOfDocumentsDTO receiptOfDocumentsDTO;
	
	private LegalBillingDTO legalBillingDTO;
	
	private boolean isLegalPayment;
	
	private String paymentPartyMode;
	
	public ReceiptOfDocumentsDTO getReceiptOfDocumentsDTO() {
		return receiptOfDocumentsDTO;
	}

	public void setReceiptOfDocumentsDTO(ReceiptOfDocumentsDTO receiptOfDocumentsDTO) {
		this.receiptOfDocumentsDTO = receiptOfDocumentsDTO;
	}

	public String getMicrCode() {
		return micrCode;
	}

	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}

	public String getVirtualPaymentProcess() {
		return virtualPaymentProcess;
	}

	public void setVirtualPaymentProcess(String virtualPaymentProcess) {
		this.virtualPaymentProcess = virtualPaymentProcess;
	}

	public String getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(String recordCount) {
		this.recordCount = recordCount;
	}

	public SelectValue getPriorityFlag() {
		return priorityFlag;
	}

	public void setPriorityFlag(SelectValue priorityFlag) {
		this.priorityFlag = priorityFlag;
		if(this.bancsPriorityFlag != null) {
			this.priorityFlag.setValue(this.bancsPriorityFlag);
		}
	}

	public String getPayeeRelationship() {
		return payeeRelationship;
	}

	public void setPayeeRelationship(String payeeRelationship) {
		this.payeeRelationship = payeeRelationship;
	}

	public String getNominee() {
		return nominee;
	}

	public void setNominee(String nominee) {
		this.nominee = nominee;
	}

	public String getNomineeRelationship() {
		return nomineeRelationship;
	}

	public void setNomineeRelationship(String nomineeRelationship) {
		this.nomineeRelationship = nomineeRelationship;
	}

	public String getLegalHeir() {
		return LegalHeir;
	}

	public void setLegalHeir(String legalHeir) {
		LegalHeir = legalHeir;
	}

	public String getLegalHeirRelationship() {
		return legalHeirRelationship;
	}

	public void setLegalHeirRelationship(String legalHeirRelationship) {
		this.legalHeirRelationship = legalHeirRelationship;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getNameAsPerBankAccount() {
		return nameAsPerBankAccount;
	}

	public void setNameAsPerBankAccount(String nameAsPerBankAccount) {
		this.nameAsPerBankAccount = nameAsPerBankAccount;
	}

	private SelectValue priorityFlag;
	
	private String payeeRelationship;
	
	private String nominee;
	
	private String nomineeRelationship;
	
	private String LegalHeir;
	
	private String legalHeirRelationship;
	
	private String accountPreference;
	
	private String accountType;
	
	private String nameAsPerBankAccount;
	
	private Button verifyAccntDtlsBtn;
	
	private String verifyAccntValue;
	
	private String verifiedStatusFlag;
	
	private String verifiedUserId;
	
	private String verifiedStatus;
	
	private Date verifiedDate;	
	
	public String getVerifiedStatusFlag() {
		return verifiedStatusFlag;
	}

	public void setVerifiedStatusFlag(String verifiedStatusFlag) {
		this.verifiedStatusFlag = verifiedStatusFlag;
	}
	
	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Button getSearchButton() {
		return searchButton;
	}

	public void setSearchButton(Button searchButton) {
		this.searchButton = searchButton;
	}
	
	public Integer getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(Integer previousPage) {
		this.previousPage = previousPage;
	}

	public Integer getNextPage() {
		return nextPage;
	}

	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}

	private String recStatusFlag;

	public String getRecStatusFlag() {
		return recStatusFlag;
	}

	public void setRecStatusFlag(String recStatusFlag) {
		this.recStatusFlag = recStatusFlag;
	}

	public String getBatchProcessFlag() {
		return batchProcessFlag;
	}

	public void setBatchProcessFlag(String batchProcessFlag) {
		this.batchProcessFlag = batchProcessFlag;
	}

	public String getDbSideRemark() {
		return dbSideRemark;
	}

	public void setDbSideRemark(String dbSideRemark) {
		this.dbSideRemark = dbSideRemark;
	}


	public Integer getNumberofdays() {
		return numberofdays;
	}

	public void setNumberofdays(Integer numberofdays) {
		this.numberofdays = numberofdays;
	}
	
	

	public Integer getNoOfDiffDays() {
		return noOfDiffDays;
	}

	public void setNoOfDiffDays(Integer noOfDiffDays) {
		this.noOfDiffDays = noOfDiffDays;
	}

	public Integer getNoofdaysexceeding() {
		return noofdaysexceeding;
	}

	public void setNoofdaysexceeding(Integer noofdaysexceeding) {
		this.noofdaysexceeding = noofdaysexceeding;
	}

	public Double getPenalTotalAmnt() {
		return penalTotalAmnt;
	}

	public void setPenalTotalAmnt(Double penalTotalAmnt) {
		this.penalTotalAmnt = penalTotalAmnt;
	}

	public Date getNextDayOfFaApprovedDate() {
		return nextDayOfFaApprovedDate;
	}

	public void setNextDayOfFaApprovedDate(Date nextDayOfFaApprovedDate) {
		if(nextDayOfFaApprovedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(nextDayOfFaApprovedDate);
			setNextDayOfFaApprovedDateValue(dateformat);
		    this.nextDayOfFaApprovedDate = nextDayOfFaApprovedDate;
		}
	}

	public String getNextDayOfFaApprovedDateValue() {
		return nextDayOfFaApprovedDateValue;
	}

	public void setNextDayOfFaApprovedDateValue(String nextDayOfFaApprovedDateValue) {
		this.nextDayOfFaApprovedDateValue = nextDayOfFaApprovedDateValue;
	}

	public String getReconsiderationFlag() {
		return reconsiderationFlag;
	}

	public void setReconsiderationFlag(String reconsiderationFlag) {
		this.reconsiderationFlag = reconsiderationFlag;
	}

	public Date getFaApprovedDate() {
		return faApprovedDate;
	}

	public void setFaApprovedDate(Date faApprovedDate) {
		if(faApprovedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(faApprovedDate);
			setFaApprovedDateValue(dateformat);
		    this.faApprovedDate = faApprovedDate;
		}
	}

	public String getFaApprovedDateValue() {
		return faApprovedDateValue;
	}

	public void setFaApprovedDateValue(String faApprovedDateValue) {
		this.faApprovedDateValue = faApprovedDateValue;
	}

	

	public Double getFaApprovedAmnt() {
		return faApprovedAmnt;
	}

	public void setFaApprovedAmnt(Double faApprovedAmnt) {
		this.faApprovedAmnt = faApprovedAmnt;
	}

	public Integer getIrdaTAT() {
		return irdaTAT;
	}

	public void setIrdaTAT(Integer irdaTAT) {
		this.irdaTAT = irdaTAT;
	}

		
	public Date getLastAckDate() {
		return lastAckDate;
	}

	public void setLastAckDate(Date lastAckDate) {
		if(lastAckDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(lastAckDate);
			setLastAckDateValue(dateformat);
		    this.lastAckDate = lastAckDate;
		}
	}

	public String getLastAckDateValue() {
		return lastAckDateValue;
	}

	public void setLastAckDateValue(String lastAckDateValue) {
		this.lastAckDateValue = lastAckDateValue;
	}

	private String documentReceivedFrom;
	
	private List<List<PreviousAccountDetailsDTO>> previousAccountDetailsList;

	
	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public Date getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

	public Long getBankCode() {
		return bankCode;
	}

	public void setBankCode(Long bankCode) {
		this.bankCode = bankCode;
	}

	
	
	
//	private String hospitalizationFlag;
	
	

	public Double getSumOfApprovedAndServiceTax() {
		return sumOfApprovedAndServiceTax;
	}

	public void setSumOfApprovedAndServiceTax(Double sumOfApprovedAndServiceTax) {
		this.sumOfApprovedAndServiceTax = sumOfApprovedAndServiceTax;
	}

	public String getUserId() {
		return userId;
	}

	public Integer getClaimCount() {
		return claimCount;
	}

	public void setClaimCount(Integer claimCount) {
		this.claimCount = claimCount;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getZonalMailId() {
		return zonalMailId;
	}

	public void setZonalMailId(String zonalMailId) {
		this.zonalMailId = zonalMailId;
	}

	public String getPioCode() {
		return pioCode;
	}

	public void setPioCode(String pioCode) {
		this.pioCode = pioCode;
	}

	public SelectValue getZone() {
		return zone;
	}

	public void setZone(SelectValue zone) {
		this.zone = zone;
	}

	public Boolean getPaymentMode() {
		return paymentMode;
	}
	/*public void setPaymentMode(Boolean paymentMode) {
		this.paymentMode = paymentMode;
	}*/
	
	/**
	 * @param paymentMode the paymentMode to set
	 */
	public void setPaymentMode(Boolean paymentMode) {
		
		this.paymentMode = paymentMode;
		//this.paymentModSelectFlag = this.paymentMode != null && paymentMode ? "Y" : "N" ;
		this.paymentModeFlag = null != this.paymentMode && paymentMode ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG; 
	}

	
	/*public String getChxBox() {
		return chxBox;
	}
	public void setChxBox(String chxBox) {
		this.chxBox = chxBox;
	}*/
	/*public String getEditDetails() {
		return editDetails;
	}
	public void setEditDetails(String editDetails) {
		this.editDetails = editDetails;
	}
	public String getViewDetails() {
		return viewDetails;
	}
	public void setViewDetails(String viewDetails) {
		this.viewDetails = viewDetails;
	}*/
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	public String getCpuCode() {
		return cpuCode;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public SelectValue getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(SelectValue paymentType) {
		this.paymentType = paymentType;
	}
/*	public Long getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(Long ifscCode) {
		this.ifscCode = ifscCode;
	}
	public Long getBeneficiaryAcntNo() {
		return beneficiaryAcntNo;
	}
	public void setBeneficiaryAcntNo(Long beneficiaryAcntNo) {
		this.beneficiaryAcntNo = beneficiaryAcntNo;
	}*/
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getTypeOfClaim() {
		return typeOfClaim;
	}
	public void setTypeOfClaim(String typeOfClaim) {
		this.typeOfClaim = typeOfClaim;
	}
	public Double getApprovedAmt() {
		return approvedAmt;
	}
	public void setApprovedAmt(Double approvedAmt) {
		this.approvedAmt = approvedAmt;
	}
	public SelectValue getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(SelectValue payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayableAt() {
		return payableAt;
	}
	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getProviderCode() {
		return providerCode;
	}
	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getAccountBatchNo() {
		return accountBatchNo;
	}
	public void setAccountBatchNo(String accountBatchNo) {
		this.accountBatchNo = accountBatchNo;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public Double getServiceTax() {
		return serviceTax;
	}
	public void setServiceTax(Double serviceTax) {
		this.serviceTax = serviceTax;
	}
	public Long getTotalAmnt() {
		return totalAmnt;
	}
	public void setTotalAmnt(Long totalAmnt) {
		this.totalAmnt = totalAmnt;
	}
	public Long getTdsAmt() {
		return tdsAmt;
	}
	public void setTdsAmt(Long tdsAmt) {
		this.tdsAmt = tdsAmt;
	}
	public Long getNetAmnt() {
		return netAmnt;
	}
	public void setNetAmnt(Long netAmnt) {
		this.netAmnt = netAmnt;
	}
	public Long getTdsPercentage() {
		return tdsPercentage;
	}
	public void setTdsPercentage(Long tdsPercentage) {
		this.tdsPercentage = tdsPercentage;
	}
	public Long getRefNo() {
		return refNo;
	}
	public void setRefNo(Long refNo) {
		this.refNo = refNo;
	}
	
	public Date getPaymentReqDt() {
		return paymentReqDt;
	}

	public void setPaymentReqDt(Date paymentReqDt) {
		if(paymentReqDt !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(paymentReqDt);
			setPaymentReqDateValue(dateformat);
		    this.paymentReqDt = paymentReqDt;
		}
	}

	public String getPaymentReqDateValue() {
		return paymentReqDateValue;
	}

	public void setPaymentReqDateValue(String paymentReqDateValue) {
		this.paymentReqDateValue = paymentReqDateValue;
	}

	public String getPaymentReqUID() {
		return paymentReqUID;
	}
	public void setPaymentReqUID(String paymentReqUID) {
		this.paymentReqUID = paymentReqUID;
	}
	
	public String getCheckBoxStatus() {
		return checkBoxStatus;
	}
	public void setCheckBoxStatus(String checkBoxStatus) {
		this.checkBoxStatus = checkBoxStatus;
	}
	public String getReasonForChange() {
		return reasonForChange;
	}
	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}
	public String getLegalFirstName() {
		return legalFirstName;
	}
	public void setLegalFirstName(String legalFirstName) {
		this.legalFirstName = legalFirstName;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getBeneficiaryAcntNo() {
		return beneficiaryAcntNo;
	}
	public void setBeneficiaryAcntNo(String beneficiaryAcntNo) {
		this.beneficiaryAcntNo = beneficiaryAcntNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Long getClaimPaymentKey() {
		return claimPaymentKey;
	}
	public void setClaimPaymentKey(Long claimPaymentKey) {
		this.claimPaymentKey = claimPaymentKey;
	}
	public SelectValue getCmbPayeeName() {
		return cmbPayeeName;
	}
	public void setCmbPayeeName(SelectValue cmbPayeeName) {
		this.cmbPayeeName = cmbPayeeName;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getPaymentModeFlag() {
		return paymentModeFlag;
	}

	public void setPaymentModeFlag(String paymentModeFlag) {
		this.paymentModeFlag = paymentModeFlag;
	}

	public Boolean getChkSelect() {
		return chkSelect;
	}

	public void setChkSelect(Boolean chkSelect) {
		this.chkSelect = chkSelect;
	}

	/*public Integer getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Integer noOfDays) {
		this.noOfDays = noOfDays;
	}*/

	public Double getIntrestRate() {
		return intrestRate;
	}

	public void setIntrestRate(Double intrestRate) {
		this.intrestRate = intrestRate;
	}

	public Double getIntrestAmount() {
		return intrestAmount;
	}

	public void setIntrestAmount(Double intrestAmount) {
		this.intrestAmount = intrestAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public long getPaymentStatusKey() {
		return paymentStatusKey;
	}

	public void setPaymentStatusKey(long paymentStatusKey) {
		this.paymentStatusKey = paymentStatusKey;
	}

	public String getDocReceivedFrom() {
		return docReceivedFrom;
	}

	public void setDocReceivedFrom(String docReceivedFrom) {
		this.docReceivedFrom = docReceivedFrom;
	}

	public Integer getNoOfDaysExceedingforCalculation() {
		return noOfDaysExceedingforCalculation;
	}

	public void setNoOfDaysExceedingforCalculation(
			Integer noOfDaysExceedingforCalculation) {
		this.noOfDaysExceedingforCalculation = noOfDaysExceedingforCalculation;
	}

	public Double getPenalInterestAmntForCalculation() {
		return penalInterestAmntForCalculation;
	}

	public void setPenalInterestAmntForCalculation(
			Double penalInterestAmntForCalculation) {
		this.penalInterestAmntForCalculation = penalInterestAmntForCalculation;
	}



	public String getRemarksForValidation() {
		return remarksForValidation;
	}

	public void setRemarksForValidation(String remarksForValidation) {
		this.remarksForValidation = remarksForValidation;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}


	public List<List<PreviousAccountDetailsDTO>> getPreviousAccountDetailsList() {
		return previousAccountDetailsList;
	}

	public void setPreviousAccountDetailsList(
			List<List<PreviousAccountDetailsDTO>> previousAccountDetailsList) {
		this.previousAccountDetailsList = previousAccountDetailsList;
	}

	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}

	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}

	public Double getInterestAmntForCalculation() {
		return interestAmntForCalculation;
	}

	public void setInterestAmntForCalculation(Double interestAmntForCalculation) {
		this.interestAmntForCalculation = interestAmntForCalculation;
	}

	public Double getInterestRateForCalculation() {
		return interestRateForCalculation;
	}

	public void setInterestRateForCalculation(Double interestRateForCalculation) {
		this.interestRateForCalculation = interestRateForCalculation;
	}

	public String getRemarksForHold() {
		return remarksForHold;
	}

	public void setRemarksForHold(String remarksForHold) {
		this.remarksForHold = remarksForHold;
	}

	public Hospitals getHospitals() {
		return hospitals;
	}

	public void setHospitals(Hospitals hospitals) {
		this.hospitals = hospitals;
	}

	public String getChangedPaymentModeFlag() {
		return changedPaymentModeFlag;
	}

	public void setChangedPaymentModeFlag(String changedPaymentModeFlag) {
		this.changedPaymentModeFlag = changedPaymentModeFlag;
	}

	public String getPayModeChangeReason() {
		return payModeChangeReason;
	}

	public void setPayModeChangeReason(String payModeChangeReason) {
		this.payModeChangeReason = payModeChangeReason;
	}

	public String getChangedPaymentMode() {
		return changedPaymentMode;
	}

	public void setChangedPaymentMode(String changedPaymentMode) {
		this.changedPaymentMode = changedPaymentMode;
	}

	public Boolean getIsFvrOrInvesInitiated() {
		return isFvrOrInvesInitiated;
	}

	public void setIsFvrOrInvesInitiated(Boolean isFvrOrInvesInitiated) {
		this.isFvrOrInvesInitiated = isFvrOrInvesInitiated;
	}
	public String getGmcEmployeeName() {
		return gmcEmployeeName;
	}

	public void setGmcEmployeeName(String gmcEmployeeName) {
		this.gmcEmployeeName = gmcEmployeeName;
	}

	public String getGmcProposerName() {
		return gmcProposerName;
	}

	public void setGmcProposerName(String gmcProposerName) {
		this.gmcProposerName = gmcProposerName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<PreviousAccountDetailsDTO> getPreviousAccntDetailsList() {
		return previousAccntDetailsList;
	}

	public void setPreviousAccntDetailsList(
			List<PreviousAccountDetailsDTO> previousAccntDetailsList) {
		this.previousAccntDetailsList = previousAccntDetailsList;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getCpuName() {
		return cpuName;
	}

	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}

	public String getPaymentTypeValue() {
		return paymentTypeValue;
	}

	public void setPaymentTypeValue(String paymentTypeValue) {
		this.paymentTypeValue = paymentTypeValue;
	}

	public SelectValue getPaymentCpuCode() {
		return paymentCpuCode;
	}

	public void setPaymentCpuCode(SelectValue paymentCpuCode) {
		this.paymentCpuCode = paymentCpuCode;
	}

	public String getPaymentCpuName() {
		return paymentCpuName;
	}

	public void setPaymentCpuName(String paymentCpuName) {
		this.paymentCpuName = paymentCpuName;
	}

	public String getDocVerifiedValue() {
		return docVerifiedValue;
	}

	public void setDocVerifiedValue(String docVerifiedValue) {
		this.docVerifiedValue = docVerifiedValue;
	}

	public String getPaymentCpuString() {
		return paymentCpuString;
	}

	public void setPaymentCpuString(String paymentCpuString) {
		this.paymentCpuString = paymentCpuString;
	}

	public Long getDocumentReceivedFromId() {
		return documentReceivedFromId;
	}

	public void setDocumentReceivedFromId(Long documentReceivedFromId) {
		this.documentReceivedFromId = documentReceivedFromId;
	}

	
	public Boolean getIsInsured() {
		return isInsured;
	}

	public void setIsInsured(Boolean isInsured) {
		this.isInsured = isInsured;
	}

	public String getReasonForChangePayee() {
		return reasonForChangePayee;
	}

	public void setReasonForChangePayee(String reasonForChangePayee) {
		this.reasonForChangePayee = reasonForChangePayee;
	}

	public String getPayeeNameStr() {
		return payeeNameStr;
	}

	public void setPayeeNameStr(String payeeNameStr) {
		this.payeeNameStr = payeeNameStr;
	}

	public BeanItemContainer<SelectValue> getPayeeNameContainer() {
		return payeeNameContainer;
	}

	public void setPayeeNameContainer(BeanItemContainer<SelectValue> payeeNameContainer) {
		this.payeeNameContainer = payeeNameContainer;
	}

	public String getHospitalPayableAt() {
		return hospitalPayableAt;
	}

	public void setHospitalPayableAt(String hospitalPayableAt) {
		this.hospitalPayableAt = hospitalPayableAt;
	}

	public String getHospitalPayableName() {
		return hospitalPayableName;
	}

	public void setHospitalPayableName(String hospitalPayableNae) {
		this.hospitalPayableName = hospitalPayableNae;
	}

	public SelectValue getTempPayeeName() {
		return tempPayeeName;
	}

	public void setTempPayeeName(SelectValue tempPayeeName) {
		this.tempPayeeName = tempPayeeName;
	}

	public SelectValue getTempPaymentType() {
		return tempPaymentType;
	}

	public void setTempPaymentType(SelectValue tempPaymentType) {
		this.tempPaymentType = tempPaymentType;
	}

	public Boolean getIsPaymentCpuCodeListenerEnable() {
		return isPaymentCpuCodeListenerEnable;
	}

	public void setIsPaymentCpuCodeListenerEnable(
			Boolean isPaymentCpuCodeListenerEnable) {
		this.isPaymentCpuCodeListenerEnable = isPaymentCpuCodeListenerEnable;
	}

	public String getDbEmailId() {
		return dbEmailId;
	}

	public void setDbEmailId(String dbEmailId) {
		this.dbEmailId = dbEmailId;
	}

	public Double getSumOfApproveIntAmnt() {
		return sumOfApproveIntAmnt;
	}

	public void setSumOfApproveIntAmnt(Double sumOfApproveIntAmnt) {
		this.sumOfApproveIntAmnt = sumOfApproveIntAmnt;
	}

	public String getPaymentCpucodeTextValue() {
		return paymentCpucodeTextValue;
	}

	public void setPaymentCpucodeTextValue(String paymentCpucodeTextValue) {
		this.paymentCpucodeTextValue = paymentCpucodeTextValue;
	}

	public List<ViewSearchCriteriaTableDTO> getPayeeNameList() {
		return payeeNameList;
	}

	public void setPayeeNameList(List<ViewSearchCriteriaTableDTO> payeeNameList) {
		this.payeeNameList = payeeNameList;
	}

	public Boolean getIsRecordExceed() {
		return isRecordExceed;
	}

	public void setIsRecordExceed(Boolean isRecordExceed) {
		this.isRecordExceed = isRecordExceed;
	}

	public Long getCpuCodeCount() {
		return cpuCodeCount;
	}

	public void setCpuCodeCount(Long cpuCodeCount) {
		this.cpuCodeCount = cpuCodeCount;
	}

	public Integer getTotalCpuCodeCount() {
		return totalCpuCodeCount;
	}

	public void setTotalCpuCodeCount(Integer totalCpuCodeCount) {
		this.totalCpuCodeCount = totalCpuCodeCount;
	}

	public Long getCashlessCount() {
		return cashlessCount;
	}

	public void setCashlessCount(Long cashlessCount) {
		this.cashlessCount = cashlessCount;
	}

	public Long getReimbursementCount() {
		return reimbursementCount;
	}

	public void setReimbursementCount(Long reimbursementCount) {
		this.reimbursementCount = reimbursementCount;
	}

	public Long getCashlessTotalCount() {
		return cashlessTotalCount;
	}

	public void setCashlessTotalCount(Long cashlessTotalCount) {
		this.cashlessTotalCount = cashlessTotalCount;
	}

	public Long getReimbursementTotalCount() {
		return reimbursementTotalCount;
	}

	public void setReimbursementTotalCount(Long reimbursementTotalCount) {
		this.reimbursementTotalCount = reimbursementTotalCount;
	}

	public String getSaveFlag() {
		return saveFlag;
	}

	public void setSaveFlag(String saveFlag) {
		this.saveFlag = saveFlag;
	}

	public Button getPayableAtButton() {
		return payableAtButton;
	}

	public void setPayableAtButton(Button payableAtButton) {
		this.payableAtButton = payableAtButton;
	}

	public String getPaymentCpuCodeName() {
		return paymentCpuCodeName;
	}

	public void setPaymentCpuCodeName(String paymentCpuCodeName) {
		this.paymentCpuCodeName = paymentCpuCodeName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPaymentPolicyType() {
		return paymentPolicyType;
	}

	public void setPaymentPolicyType(String paymentPolicyType) {
		this.paymentPolicyType = paymentPolicyType;
	}

	public Button getPreviousAccntDtlsBtn() {
		return previousAccntDtlsBtn;
	}

	public void setPreviousAccntDtlsBtn(Button previousAccntDtlsBtn) {
		this.previousAccntDtlsBtn = previousAccntDtlsBtn;
	}

	public String getSourceRskID() {
		return sourceRskID;
	}

	public void setSourceRskID(String sourceRskID) {
		this.sourceRskID = sourceRskID;
	}

	public String getBancsPriorityFlag() {
		return bancsPriorityFlag;
	}

	public void setBancsPriorityFlag(String bancsPriorityFlag) {
		this.bancsPriorityFlag = bancsPriorityFlag;
		
	}
	
	public Boolean getVerificationClicked() {
		return verificationClicked;
	}

	public void setVerificationClicked(Boolean verificationClicked) {
		this.verificationClicked = verificationClicked;
	}

	public List<VerificationAccountDeatilsTableDTO> getVerificationAccountDeatilsTableDTO() {
		return VerificationAccountDeatilsTableDTO;
	}

	public void setVerificationAccountDeatilsTableDTO(
			List<VerificationAccountDeatilsTableDTO> verificationAccountDeatilsTableDTO) {
		VerificationAccountDeatilsTableDTO = verificationAccountDeatilsTableDTO;
	}

	public Button getVerifyAccntDtlsBtn() {
		return verifyAccntDtlsBtn;
	}

	public void setVerifyAccntDtlsBtn(Button verifyAccntDtlsBtn) {
		this.verifyAccntDtlsBtn = verifyAccntDtlsBtn;
	}

	public String getVerifiedUserId() {
		return verifiedUserId;
	}

	public void setVerifiedUserId(String verifiedUserId) {
		this.verifiedUserId = verifiedUserId;
	}

	public String getVerifiedStatus() {
		return verifiedStatus;
	}

	public void setVerifiedStatus(String verifiedStatus) {
		this.verifiedStatus = verifiedStatus;
	}

	public Date getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public String getAccountPreference() {
		return accountPreference;
	}

	public void setAccountPreference(String accountPreference) {
		this.accountPreference = accountPreference;
	}

	public List<VerificationAccountDeatilsTableDTO> getPaidAccountDeatilsTableDTO() {
		return paidAccountDeatilsTableDTO;
	}

	public void setPaidAccountDeatilsTableDTO(
			List<VerificationAccountDeatilsTableDTO> paidAccountDeatilsTableDTO) {
		this.paidAccountDeatilsTableDTO = paidAccountDeatilsTableDTO;
	}

	public String getVerifyAccntValue() {
		return verifyAccntValue;
	}

	public void setVerifyAccntValue(String verifyAccntValue) {
		this.verifyAccntValue = verifyAccntValue;
	}

	public LegalBillingDTO getLegalBillingDTO() {
		return legalBillingDTO;
	}

	public void setLegalBillingDTO(LegalBillingDTO legalBillingDTO) {
		this.legalBillingDTO = legalBillingDTO;
	}

	public boolean isLegalPayment() {
		return isLegalPayment;
	}

	public void setLegalPayment(boolean isLegalPayment) {
		this.isLegalPayment = isLegalPayment;
	}

	public String getPaymentPartyMode() {
		return paymentPartyMode;
	}

	public void setPaymentPartyMode(String paymentPartyMode) {
		this.paymentPartyMode = paymentPartyMode;
	}

	public Boolean getIsNEFTDetailsAvailable() {
		return isNEFTDetailsAvailable;
	}

	public void setIsNEFTDetailsAvailable(Boolean isNEFTDetailsAvailable) {
		this.isNEFTDetailsAvailable = isNEFTDetailsAvailable;
	}

	public Boolean getIsNEFTDetailsAvailableinDMS() {
		return isNEFTDetailsAvailableinDMS;
	}

	public void setIsNEFTDetailsAvailableinDMS(Boolean isNEFTDetailsAvailableinDMS) {
		this.isNEFTDetailsAvailableinDMS = isNEFTDetailsAvailableinDMS;
	}
	
	

}
