package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import java.util.Date;
import java.util.List;

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPNegotiationDetailsDTO;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.vaadin.ui.Button;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.TextField;

public class OMPClaimCalculationViewTableDTO {
	
	
	public OMPClaimCalculationViewTableDTO(){
		
		receiptOfDocumentsDTO = new ReceiptOfDocumentsDTO();
	}
	private Integer serialNumber; 
	
	private SelectValue category;
	
	private Double billAmt;
	
	private Double amtIn;
	
	private Double deduction;
	
	private Double totalAmt;
	
	private Double totalAmtInr;
	
	private Double approvedAmt;
	
	private Double agreedAmt;
		
	private Double differenceAmt;
	
	private Double expenses;
	
	private Double negotiationClaimed;
	
	private Double negotiationCapping;
	
	private Double negotiationPayable;
	
	private Double handlingCharges;
	
	private Double totalExp;
	
	private Long key;
	
	private Long rodKey;
	
	private String deleted = "N";
	
	private SelectValue classification;
	
	private SelectValue subClassification;
	
	private SelectValue docRecivedFrm;
	
	private SelectValue currencyType;
	
	private Double currencyrate;
	
	private Double conversionValue;
	
	private SelectValue copay;
	
	private Double copayamount;
	
	private Double approvedamountaftecopay;
	
	private Double afternegotiation;
	
	private SelectValue select;
	
	private SelectValue negotiationDone;
	
	private String negotiationDetails;
	
	private String uploadDocuments;
	
	private String recoverable;
	
	private String paymentDetails;
	
	private SelectValue viewforApprover;
	
	private String sendforApprover;
	
	private String reject;
	
	private String notinClaimCount;
	
	private String submit;
	
	private Boolean isSendFrApprover;
	
	private Boolean isreject;
	
	private Boolean isnotInClaimCount;
	private String rodnumber;
	
	private Long claimkey;
	
	private Long negokey;
	
	private List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs;
	
	private BeanItemContainer<SelectValue> selectNegoContainer;
	
	private List<OMPNewRecoverableTableDto> ompRecoverableTableList;
	
	private List<OMPPaymentDetailsTableDTO> ompPaymentDetailsList;
	
	private BeanItemContainer<SelectValue> paymentToContainer;
	
	private BeanItemContainer<SelectValue> paymentModeContainer;
	
	private Boolean isReadOnly = Boolean.FALSE; 
	
	private Double finalApprovedAmtDollor;
	
	private Double finalApprovedAmtInr;
	
	private SelectValue reasonForRejectionRemarks;
	
	private String processorRemarks;
	
	private String reasonForApproval;
	
	private Long count;
	
	private ReceiptOfDocumentsDTO receiptOfDocumentsDTO;
	
	private SelectValue rodClaimType;
	
	private BeanItemContainer<SelectValue> rodClaimTypeContainer;
	
	private BeanItemContainer<SelectValue> currencyValueContainer;
	
	private BeanItemContainer<SelectValue> inrCurrencyValueContainer;
	
	private Double deductiblesOriginal;
	
	private String screenName;
	
	private Boolean isReadOnlyRecoverable = Boolean.FALSE;
	
	private Date lateDocReceivedDate;

	//CR20181332
	private String rejectDetails;
	private String rejectionRemarks;
	private String reconsiderFlag;
	private Object rejectionIds;
	private String disVoucFlag;
	private String dischargeDetails;
	private boolean isProcessorRejectedClaim = false;
	
	private Boolean genDisVoucherFlag;
	private String disVoucherRemarks;
	private Boolean genCoveringLetterFlag;
	private Button disChrgDetailsBut;
	private CheckBox chkBoxDicharge;
	private Date dvReceivedDate;
	
	private OMPClaimProcessorDTO claimProcessorDTO;
	private String amountInWords;
	
	private ClaimDto claimDto;
	
	private Long processorStatus;
	private Long rodPaymentStatus;
	private CheckBox rejectChkBox;
	
	private String nomineeName;
	private String nomineeAddress;
	
	private GComboBox cmbViewForApprover;
	private GComboBox cmbNegotiationDone;
	
	private CheckBox approverApprvChekBox;
	private CheckBox notInClaimCountChkBox;
	private CheckBox reConsiderChkBox;
	
	private GComboBox cmbClassification;
	private GComboBox cmbSubClassification;
	private GComboBox cmbRODClaimType;
	private GComboBox cmbDocReceivFrm;
	private GComboBox cmbCategory;
	private GComboBox cmbCurrencyType;
	private TextField txtConversionVal;
	private TextField txtBillAmt;
	private TextField txtDeduction;
	private Boolean isSelected = Boolean.FALSE;
	private String acknumber;
	private SelectValue modeOfReceipt;
	private String ackContactNumber;
	private String emailId;
	private GComboBox cmbModeOfReceipt;
	private String status;
	
	private Long ackKey;
	
	private Date ackDocReceivedDate;
	
	public Integer getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}
	public SelectValue getCategory() {
		return category;
	}
	public void setCategory(SelectValue category) {
		this.category = category;
	}
	public Double getBillAmt() {
		return billAmt;
	}
	public void setBillAmt(Double billAmt) {
		this.billAmt = billAmt;
	}
	public Double getAmtIn() {
		return amtIn;
	}
	public void setAmtIn(Double amtIn) {
		this.amtIn = amtIn;
	}
	public Double getDeduction() {
		return deduction;
	}
	public void setDeduction(Double deduction) {
		this.deduction = deduction;
	}
	public Double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}
	public Double getTotalAmtInr() {
		return totalAmtInr;
	}
	public void setTotalAmtInr(Double totalAmtInr) {
		this.totalAmtInr = totalAmtInr;
	}
	public Double getApprovedAmt() {
		return approvedAmt;
	}
	public void setApprovedAmt(Double approvedAmt) {
		this.approvedAmt = approvedAmt;
	}
	public Double getAgreedAmt() {
		return agreedAmt;
	}
	public void setAgreedAmt(Double agreedAmt) {
		this.agreedAmt = agreedAmt;
	}
	public Double getDifferenceAmt() {
		return differenceAmt;
	}
	public void setDifferenceAmt(Double differenceAmt) {
		this.differenceAmt = differenceAmt;
	}
	public Double getExpenses() {
		return expenses;
	}
	public void setExpenses(Double expenses) {
		this.expenses = expenses;
	}
	public Double getNegotiationClaimed() {
		return negotiationClaimed;
	}
	public void setNegotiationClaimed(Double negotiationClaimed) {
		this.negotiationClaimed = negotiationClaimed;
	}
	public Double getNegotiationCapping() {
		return negotiationCapping;
	}
	public void setNegotiationCapping(Double negotiationCapping) {
		this.negotiationCapping = negotiationCapping;
	}
	public Double getNegotiationPayable() {
		return negotiationPayable;
	}
	public void setNegotiationPayable(Double negotiationPayable) {
		this.negotiationPayable = negotiationPayable;
	}
	public Double getHandlingCharges() {
		return handlingCharges;
	}
	public void setHandlingCharges(Double handlingCharges) {
		this.handlingCharges = handlingCharges;
	}
	public Double getTotalExp() {
		return totalExp;
	}
	public void setTotalExp(Double totalExp) {
		this.totalExp = totalExp;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public SelectValue getClassification() {
		return classification;
	}
	public void setClassification(SelectValue classification) {
		this.classification = classification;
	}
	public SelectValue getSubClassification() {
		return subClassification;
	}
	public void setSubClassification(SelectValue subClassification) {
		this.subClassification = subClassification;
	}
	public SelectValue getDocRecivedFrm() {
		return docRecivedFrm;
	}
	public void setDocRecivedFrm(SelectValue docRecivedFrm) {
		this.docRecivedFrm = docRecivedFrm;
	}
	public SelectValue getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(SelectValue currencyType) {
		this.currencyType = currencyType;
	}
	public Double getCurrencyrate() {
		return currencyrate;
	}
	public void setCurrencyrate(Double currencyrate) {
		this.currencyrate = currencyrate;
	}
	public Double getConversionValue() {
		return conversionValue;
	}
	public void setConversionValue(Double conversionValue) {
		this.conversionValue = conversionValue;
	}
	public SelectValue getCopay() {
		return copay;
	}
	public void setCopay(SelectValue copay) {
		this.copay = copay;
	}
	public Double getCopayamount() {
		return copayamount;
	}
	public void setCopayamount(Double copayamount) {
		this.copayamount = copayamount;
	}
	public Double getApprovedamountaftecopay() {
		return approvedamountaftecopay;
	}
	public void setApprovedamountaftecopay(Double approvedamountaftecopay) {
		this.approvedamountaftecopay = approvedamountaftecopay;
	}
	
	public Double getAfternegotiation() {
		return afternegotiation;
	}
	public void setAfternegotiation(Double afternegotiation) {
		this.afternegotiation = afternegotiation;
	}
	public SelectValue getSelect() {
		return select;
	}
	public void setSelect(SelectValue select) {
		this.select = select;
	}
	public SelectValue getNegotiationDone() {
		return negotiationDone;
	}
	public void setNegotiationDone(SelectValue negotiationDone) {
		this.negotiationDone = negotiationDone;
	}
	public String getNegotiationDetails() {
		return negotiationDetails;
	}
	public void setNegotiationDetails(String negotiationDetails) {
		this.negotiationDetails = negotiationDetails;
	}
	public String getUploadDocuments() {
		return uploadDocuments;
	}
	public void setUploadDocuments(String uploadDocuments) {
		this.uploadDocuments = uploadDocuments;
	}
	public String getRecoverable() {
		return recoverable;
	}
	public void setRecoverable(String recoverable) {
		this.recoverable = recoverable;
	}
	public String getPaymentDetails() {
		return paymentDetails;
	}
	public void setPaymentDetails(String paymentDetails) {
		this.paymentDetails = paymentDetails;
	}
	public SelectValue getViewforApprover() {
		return viewforApprover;
	}
	public void setViewforApprover(SelectValue viewforApprover) {
		this.viewforApprover = viewforApprover;
	}
	public String getSendforApprover() {
		return sendforApprover;
	}
	public void setSendforApprover(String sendforApprover) {
		this.sendforApprover = sendforApprover;
	}
	public String getReject() {
		return reject;
	}
	public void setReject(String reject) {
		this.reject = reject;
	}
	public String getNotinClaimCount() {
		return notinClaimCount;
	}
	public void setNotinClaimCount(String notinClaimCount) {
		this.notinClaimCount = notinClaimCount;
	}
	public String getSubmit() {
		return submit;
	}
	public void setSubmit(String submit) {
		this.submit = submit;
	}
	public Boolean getIsSendFrApprover() {
		return isSendFrApprover;
	}
	public void setIsSendFrApprover(Boolean isSendFrApprover) {
		this.isSendFrApprover = isSendFrApprover;
	}
	public Boolean getIsreject() {
		return isreject;
	}
	public void setIsreject(Boolean isreject) {
		this.isreject = isreject;
	}
	public Boolean getIsnotInClaimCount() {
		return isnotInClaimCount;
	}
	public void setIsnotInClaimCount(Boolean isnotInClaimCount) {
		this.isnotInClaimCount = isnotInClaimCount;
	}
	public String getRodnumber() {
		return rodnumber;
	}
	public void setRodnumber(String rodnumber) {
		this.rodnumber = rodnumber;
	}
	public Long getClaimkey() {
		return claimkey;
	}
	public void setClaimkey(Long claimkey) {
		this.claimkey = claimkey;
	}
	public BeanItemContainer<SelectValue> getSelectNegoContainer() {
		return selectNegoContainer;
	}
	public void setSelectNegoContainer(BeanItemContainer<SelectValue> selectNegoContainer) {
		this.selectNegoContainer = selectNegoContainer;
	}
	public Long getNegokey() {
		return negokey;
	}
	public void setNegokey(Long negokey) {
		this.negokey = negokey;
	}
	public List<OMPNegotiationDetailsDTO> getNegotiationDetailsDTOs() {
		return negotiationDetailsDTOs;
	}
	public void setNegotiationDetailsDTOs(List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs) {
		this.negotiationDetailsDTOs = negotiationDetailsDTOs;
	}
	public List<OMPNewRecoverableTableDto> getOmpRecoverableTableList() {
		return ompRecoverableTableList;
	}
	public void setOmpRecoverableTableList(List<OMPNewRecoverableTableDto> ompRecoverableTableList) {
		this.ompRecoverableTableList = ompRecoverableTableList;
	}
	public List<OMPPaymentDetailsTableDTO> getOmpPaymentDetailsList() {
		return ompPaymentDetailsList;
	}
	public void setOmpPaymentDetailsList(List<OMPPaymentDetailsTableDTO> ompPaymentDetailsList) {
		this.ompPaymentDetailsList = ompPaymentDetailsList;
	}
	public BeanItemContainer<SelectValue> getPaymentToContainer() {
		return paymentToContainer;
	}
	public void setPaymentToContainer(BeanItemContainer<SelectValue> paymentToContainer) {
		this.paymentToContainer = paymentToContainer;
	}
	public BeanItemContainer<SelectValue> getPaymentModeContainer() {
		return paymentModeContainer;
	}
	public void setPaymentModeContainer(BeanItemContainer<SelectValue> paymentModeContainer) {
		this.paymentModeContainer = paymentModeContainer;
	}
	public Boolean getIsReadOnly() {
		return isReadOnly;
	}
	public void setIsReadOnly(Boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
	public Double getFinalApprovedAmtInr() {
		return finalApprovedAmtInr;
	}
	public void setFinalApprovedAmtInr(Double finalApprovedAmtInr) {
		this.finalApprovedAmtInr = finalApprovedAmtInr != null ? Math.round(finalApprovedAmtInr) : 0d ;
	}
	public Double getFinalApprovedAmtDollor() {
		return finalApprovedAmtDollor;
	}
	public void setFinalApprovedAmtDollor(Double finalApprovedAmtDollor) {
		this.finalApprovedAmtDollor = finalApprovedAmtDollor;
	}
	public SelectValue getReasonForRejectionRemarks() {
		return reasonForRejectionRemarks;
	}
	public void setReasonForRejectionRemarks(SelectValue reasonForRejectionRemarks) {
		this.reasonForRejectionRemarks = reasonForRejectionRemarks;
	}
	public String getProcessorRemarks() {
		return processorRemarks;
	}
	public void setProcessorRemarks(String processorRemarks) {
		this.processorRemarks = processorRemarks;
	}
	public String getReasonForApproval() {
		return reasonForApproval;
	}
	public void setReasonForApproval(String reasonForApproval) {
		this.reasonForApproval = reasonForApproval;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public ReceiptOfDocumentsDTO getReceiptOfDocumentsDTO() {
		return receiptOfDocumentsDTO;
	}
	public void setReceiptOfDocumentsDTO(ReceiptOfDocumentsDTO receiptOfDocumentsDTO) {
		this.receiptOfDocumentsDTO = receiptOfDocumentsDTO;
	}
	public SelectValue getRodClaimType() {
		return rodClaimType;
	}
	public void setRodClaimType(SelectValue rodClaimType) {
		this.rodClaimType = rodClaimType;
	}
	public BeanItemContainer<SelectValue> getRodClaimTypeContainer() {
		return rodClaimTypeContainer;
	}
	public void setRodClaimTypeContainer(BeanItemContainer<SelectValue> rodClaimTypeContainer) {
		this.rodClaimTypeContainer = rodClaimTypeContainer;
	}
	public BeanItemContainer<SelectValue> getCurrencyValueContainer() {
		return currencyValueContainer;
	}
	public void setCurrencyValueContainer(BeanItemContainer<SelectValue> currencyValueContainer) {
		this.currencyValueContainer = currencyValueContainer;
	}
	public BeanItemContainer<SelectValue> getInrCurrencyValueContainer() {
		return inrCurrencyValueContainer;
	}
	public void setInrCurrencyValueContainer(
			BeanItemContainer<SelectValue> inrCurrencyValueContainer) {
		this.inrCurrencyValueContainer = inrCurrencyValueContainer;
	}
	public Double getDeductiblesOriginal() {
		return deductiblesOriginal;
	}
	public void setDeductiblesOriginal(Double deductiblesOriginal) {
		this.deductiblesOriginal = deductiblesOriginal;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public Boolean getIsReadOnlyRecoverable() {
		return isReadOnlyRecoverable;
	}
	public void setIsReadOnlyRecoverable(Boolean isReadOnlyRecoverable) {
		this.isReadOnlyRecoverable = isReadOnlyRecoverable;
	}

	public Date getLateDocReceivedDate() {
		return lateDocReceivedDate;
	}
	public void setLateDocReceivedDate(Date lateDocReceivedDate) {
		this.lateDocReceivedDate = lateDocReceivedDate;
	}
	
	public Boolean getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public String getRejectDetails() {
		return rejectDetails;
	}
	public void setRejectDetails(String rejectDetails) {
		this.rejectDetails = rejectDetails;
	}
	public String getReconsiderFlag() {
		return reconsiderFlag;
	}
	public void setReconsiderFlag(String reconsiderFlag) {
		this.reconsiderFlag = reconsiderFlag;
	}
	public String getRejectionRemarks() {
		return rejectionRemarks;
	}
	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}
	public Object getRejectionIds() {
		return rejectionIds;
	}
	public void setRejectionIds(Object rejectionIds) {
		this.rejectionIds = rejectionIds;
	}
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	public boolean isProcessorRejectedClaim() {
		return isProcessorRejectedClaim;
	}
	public void setProcessorRejectedClaim(boolean isProcessorRejectedClaim) {
		this.isProcessorRejectedClaim = isProcessorRejectedClaim;
	}
	public Boolean getGenDisVoucherFlag() {
		return genDisVoucherFlag;
	}
	public void setGenDisVoucherFlag(Boolean genDisVoucherFlag) {
		this.genDisVoucherFlag = genDisVoucherFlag;
	}
	public String getDisVoucherRemarks() {
		return disVoucherRemarks;
	}
	public void setDisVoucherRemarks(String disVoucherRemarks) {
		this.disVoucherRemarks = disVoucherRemarks;
	}
	public Boolean getGenCoveringLetterFlag() {
		return genCoveringLetterFlag;
	}
	public void setGenCoveringLetterFlag(Boolean genCoveringLetterFlag) {
		this.genCoveringLetterFlag = genCoveringLetterFlag;
	}
	public String getDisVoucFlag() {
		return disVoucFlag;
	}
	public void setDisVoucFlag(String disVoucFlag) {
		this.disVoucFlag = disVoucFlag;
	}
	public String getDischargeDetails() {
		return dischargeDetails;
	}
	public void setDischargeDetails(String dischargeDetails) {
		this.dischargeDetails = dischargeDetails;
	}
	public OMPClaimProcessorDTO getClaimProcessorDTO() {
		return claimProcessorDTO;
	}
	public void setClaimProcessorDTO(OMPClaimProcessorDTO claimProcessorDTO) {
		this.claimProcessorDTO = claimProcessorDTO;
	}
	public String getAmountInWords() {
		return amountInWords;
	}
	public void setAmountInWords(String amountInWords) {
		this.amountInWords = amountInWords;
	}
	public Button getDisChrgDetailsBut() {
		return disChrgDetailsBut;
	}
	public void setDisChrgDetailsBut(Button disChrgDetailsBut) {
		this.disChrgDetailsBut = disChrgDetailsBut;
	}
	public Long getProcessorStatus() {
		return processorStatus;
	}
	public void setProcessorStatus(Long processorStatus) {
		this.processorStatus = processorStatus;
	}
	public CheckBox getRejectChkBox() {
		return rejectChkBox;
	}
	public void setRejectChkBox(CheckBox rejectChkBox) {
		this.rejectChkBox = rejectChkBox;
	}
	public String getNomineeName() {
		return nomineeName;
	}
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	public String getNomineeAddress() {
		return nomineeAddress;
	}
	public void setNomineeAddress(String nomineeAddress) {
		this.nomineeAddress = nomineeAddress;
	}
	public GComboBox getCmbViewForApprover() {
		return cmbViewForApprover;
	}
	public void setCmbViewForApprover(GComboBox cmbViewForApprover) {
		this.cmbViewForApprover = cmbViewForApprover;
	}
	public GComboBox getCmbNegotiationDone() {
		return cmbNegotiationDone;
	}
	public void setCmbNegotiationDone(GComboBox cmbNegotiationDone) {
		this.cmbNegotiationDone = cmbNegotiationDone;
	}
	public CheckBox getChkBoxDicharge() {
		return chkBoxDicharge;
	}
	public void setChkBoxDicharge(CheckBox chkBoxDicharge) {
		this.chkBoxDicharge = chkBoxDicharge;
	}
	public Long getRodPaymentStatus() {
		return rodPaymentStatus;
	}
	public void setRodPaymentStatus(Long rodPaymentStatus) {
		this.rodPaymentStatus = rodPaymentStatus;
	}
	public CheckBox getApproverApprvChekBox() {
		return approverApprvChekBox;
	}
	public void setApproverApprvChekBox(CheckBox approverApprvChekBox) {
		this.approverApprvChekBox = approverApprvChekBox;
	}
	public CheckBox getNotInClaimCountChkBox() {
		return notInClaimCountChkBox;
	}
	public void setNotInClaimCountChkBox(CheckBox notInClaimCountChkBox) {
		this.notInClaimCountChkBox = notInClaimCountChkBox;
	}
	public CheckBox getReConsiderChkBox() {
		return reConsiderChkBox;
	}
	public void setReConsiderChkBox(CheckBox reConsiderChkBox) {
		this.reConsiderChkBox = reConsiderChkBox;
	}
	public GComboBox getCmbClassification() {
		return cmbClassification;
	}
	public void setCmbClassification(GComboBox cmbClassification) {
		this.cmbClassification = cmbClassification;
	}
	public GComboBox getCmbSubClassification() {
		return cmbSubClassification;
	}
	public void setCmbSubClassification(GComboBox cmbSubClassification) {
		this.cmbSubClassification = cmbSubClassification;
	}
	public GComboBox getCmbRODClaimType() {
		return cmbRODClaimType;
	}
	public void setCmbRODClaimType(GComboBox cmbRODClaimType) {
		this.cmbRODClaimType = cmbRODClaimType;
	}
	public GComboBox getCmbDocReceivFrm() {
		return cmbDocReceivFrm;
	}
	public void setCmbDocReceivFrm(GComboBox cmbDocReceivFrm) {
		this.cmbDocReceivFrm = cmbDocReceivFrm;
	}
	public GComboBox getCmbCategory() {
		return cmbCategory;
	}
	public void setCmbCategory(GComboBox cmbCategory) {
		this.cmbCategory = cmbCategory;
	}
	public GComboBox getCmbCurrencyType() {
		return cmbCurrencyType;
	}
	public void setCmbCurrencyType(GComboBox cmbCurrencyType) {
		this.cmbCurrencyType = cmbCurrencyType;
	}
	public TextField getTxtConversionVal() {
		return txtConversionVal;
	}
	public void setTxtConversionVal(TextField txtConversionVal) {
		this.txtConversionVal = txtConversionVal;
	}
	public TextField getTxtBillAmt() {
		return txtBillAmt;
	}
	public void setTxtBillAmt(TextField txtBillAmt) {
		this.txtBillAmt = txtBillAmt;
	}
	public TextField getTxtDeduction() {
		return txtDeduction;
	}
	public void setTxtDeduction(TextField txtDeduction) {
		this.txtDeduction = txtDeduction;
	}
	public Date getDvReceivedDate() {
		return dvReceivedDate;
	}
	public void setDvReceivedDate(Date dvReceivedDate) {
		this.dvReceivedDate = dvReceivedDate;
	}
	public String getAcknumber() {
		return acknumber;
	}
	public void setAcknumber(String acknumber) {
		this.acknumber = acknumber;
	}
	public SelectValue getModeOfReceipt() {
		return modeOfReceipt;
	}
	public void setModeOfReceipt(SelectValue modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}
	public String getAckContactNumber() {
		return ackContactNumber;
	}
	public void setAckContactNumber(String ackContactNumber) {
		this.ackContactNumber = ackContactNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public GComboBox getCmbModeOfReceipt() {
		return cmbModeOfReceipt;
	}
	public void setCmbModeOfReceipt(GComboBox cmbModeOfReceipt) {
		this.cmbModeOfReceipt = cmbModeOfReceipt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getAckKey() {
		return ackKey;
	}
	public void setAckKey(Long ackKey) {
		this.ackKey = ackKey;
	}
	public Date getAckDocReceivedDate() {
		return ackDocReceivedDate;
	}
	public void setAckDocReceivedDate(Date ackDocReceivedDate) {
		this.ackDocReceivedDate = ackDocReceivedDate;
	}
	
}
