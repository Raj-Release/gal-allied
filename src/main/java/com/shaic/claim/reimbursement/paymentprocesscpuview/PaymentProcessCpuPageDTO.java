package com.shaic.claim.reimbursement.paymentprocesscpuview;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.intimation.uprSearch.ClmPaymentCancelDto;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.domain.LegalHeir;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Reimbursement;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;

public class PaymentProcessCpuPageDTO  extends AbstractTableDTO  implements Serializable{
		
	private ClaimDto claimDto;
	private Reimbursement reimbursementObj;
	private String intimationNo;
	private String claimNumber;
	private String mainMemberName;
	private String nameOfTheInsured;
	private Double settledAmount;
	private Date admissionDate;
	private String admissionDateValue;
	private String ddNo;
	private String bankName;
	private Date billReceivedDate;
	private String billReceivedDateValue;
	private String policyNo;
	private String insuredPatientName;
	private String hospitalName;
	private String address;
	private Date dischargeDate;
	private String dischargeDateValue;
	private Date ddDate;
	private String ddDateValue;
	private String billNumber;
	private Date billDate;
	private String billDateValue;
	private String ccZonalOfc;
	private String ccAreaOfc;
	private String ccBranchOfc;
	private String dischargeVoucher;
	private String dvCoveringLetter;
	private String nriAndTeleSales;
	private String paymentAndDischarge;
	private String hospitalPayment;
	private String billSummary;
	private String mailId;
	private String sendMail;
	private String confirm;
	private String close;
	private Boolean letterPrintingMode;
	private String letterPrintingModeFlag;
	private Long claimPaymentKey;
	private String amountInwords;
	private HashMap filePathAndTypeMap;
	private String createdBy;
	private String dischargeVoucherUrl;
	private String paymentDischargeUrl;
	private String hospitalDischargeVoucher;
	
	private String dvCoveringUrl;
	private String claimType;
	private String docReceivedFrom;
	private String billClassification;
	private String rodNumber;
	private Double tdsAmount;
	private Date paymentDate;
	private String paymentType;
	private Double netAmount;
	private Double intrestAmount;
	private String intrestAmountInWords;
	private String totalAmntInWords;
	private Date modifiedDate;	
	private List<LegalHeirDTO> legalHeirDTOList;
	private Long modeOfReceipt;
	
	private String batchNo;
	private String accNumber;
	private String ifscCode;
	private String bankBranchName;
	private String payeeName;
	private String payableAt;
	private Double approvedAmount;
	private String paymentCpu;
	private String status;
	private String rodType;
	private PaymentProcessCpuTableDTO paymentProcessDto;
	private LegalHeir legalHeir;
	
	private List<ClmPaymentCancelDto> paymentCancelListDto;
	
	private String cpuCodeWithValue;
	private String insuredGender;
	private String tdsAmountInWords;
	
	private OrganaizationUnit branchOffice;
	private OrganaizationUnit parentOffice;
	private String branchOfficeState;
	private String parentOfficeState;
	private Date todayDate;	
	private String paymentPartyMode;
	
	public PaymentProcessCpuPageDTO()
	{
		
	}
	
	public PaymentProcessCpuPageDTO(PaymentProcessCpuTableDTO cpuTableDto,ClaimDto claimDto,Reimbursement reimbursementObj)
	{
		this.paymentProcessDto = cpuTableDto;
		this.intimationNo = cpuTableDto.getIntimationNo();
		this.claimNumber = cpuTableDto.getClaimNumber();
		this.mainMemberName = claimDto.getNewIntimationDto().getPolicy().getProposerFirstName();
		this.insuredPatientName = claimDto.getNewIntimationDto().getInsuredPatient().getInsuredName();
		this.settledAmount = cpuTableDto.getAmount();
		this.admissionDate =claimDto.getAdmissionDate();
		this.dischargeDate = claimDto.getDischargeDate(); 
		this.policyNo = claimDto.getNewIntimationDto().getPolicy().getPolicyNumber();
		this.hospitalName = claimDto.getNewIntimationDto().getHospitalDto().getName(); 
		this.address = claimDto.getNewIntimationDto().getHospitalDto().getAddress();
		this.nameOfTheInsured = claimDto.getNewIntimationDto().getInsuredPatient().getInsuredName();
	    this.bankName = cpuTableDto.getBankName();
	    this.ddDate = cpuTableDto.getChequeDate();
	    this.ddNo = cpuTableDto.getChequeNo();
	    this.claimPaymentKey = cpuTableDto.getClaimPaymentKey();
	    this.billReceivedDate = reimbursementObj.getDocAcknowLedgement().getDocumentReceivedDate();
	    this.claimType = claimDto.getClaimTypeValue() ;
	    this.docReceivedFrom = cpuTableDto.getDocReceivedFrom();
	    this.billClassification = reimbursementObj.getDocAcknowLedgement().getHospitalisationFlag();
	    this.rodNumber = cpuTableDto.getRodNo();
	    this.tdsAmount = cpuTableDto.getTdsAmount();
	    this.paymentDate = cpuTableDto.getPaymentDate();
	    this.paymentType = cpuTableDto.getPaymentType();
	    this.netAmount = cpuTableDto.getNetAmount();
	    this.claimDto = claimDto;
	    this.modifiedDate = cpuTableDto.getModifiedDate();
	    this.reimbursementObj = reimbursementObj;
	    this.accNumber = cpuTableDto.getAccountNumber();
	    this.ifscCode = cpuTableDto.getIfscCode();
	    this.bankBranchName = cpuTableDto.getBranchName();
	    this.payeeName = cpuTableDto.getPayeeName();
	    this.approvedAmount = cpuTableDto.getAmount();
	    this.cpuCodeWithValue = cpuTableDto.getCpuCodeWithValue();
	    
	    this.paymentCancelListDto = new ArrayList<ClmPaymentCancelDto>();
	    	    		
	}
	
	public String getHospitalDischargeVoucher() {
		return hospitalDischargeVoucher;
	}

	public void setHospitalDischargeVoucher(String hospitalDischargeVoucher) {
		this.hospitalDischargeVoucher = hospitalDischargeVoucher;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Double getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(Double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Reimbursement getReimbursementObj() {
		return reimbursementObj;
	}

	public void setReimbursementObj(Reimbursement reimbursementObj) {
		this.reimbursementObj = reimbursementObj;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getDocReceivedFrom() {
		return docReceivedFrom;
	}

	public void setDocReceivedFrom(String docReceivedFrom) {
		this.docReceivedFrom = docReceivedFrom;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}

	public String getSendMail() {
		return sendMail;
	}
	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	public String getClose() {
		return close;
	}
	public void setClose(String close) {
		this.close = close;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getDischargeVoucher() {
		return dischargeVoucher;
	}
	public void setDischargeVoucher(String dischargeVoucher) {
		this.dischargeVoucher = dischargeVoucher;
	}
	public String getDvCoveringLetter() {
		return dvCoveringLetter;
	}
	public void setDvCoveringLetter(String dvCoveringLetter) {
		this.dvCoveringLetter = dvCoveringLetter;
	}
	public String getNriAndTeleSales() {
		return nriAndTeleSales;
	}
	public void setNriAndTeleSales(String nriAndTeleSales) {
		this.nriAndTeleSales = nriAndTeleSales;
	}
	public String getPaymentAndDischarge() {
		return paymentAndDischarge;
	}
	public void setPaymentAndDischarge(String paymentAndDischarge) {
		this.paymentAndDischarge = paymentAndDischarge;
	}
	public String getHospitalPayment() {
		return hospitalPayment;
	}
	public void setHospitalPayment(String hospitalPayment) {
		this.hospitalPayment = hospitalPayment;
	}
	public String getBillSummary() {
		return billSummary;
	}
	public void setBillSummary(String billSummary) {
		this.billSummary = billSummary;
	}
	public String getCcZonalOfc() {
		return ccZonalOfc;
	}
	public void setCcZonalOfc(String ccZonalOfc) {
		this.ccZonalOfc = ccZonalOfc;
	}
	public String getCcAreaOfc() {
		return ccAreaOfc;
	}
	public void setCcAreaOfc(String ccAreaOfc) {
		this.ccAreaOfc = ccAreaOfc;
	}
	public String getCcBranchOfc() {
		return ccBranchOfc;
	}
	public void setCcBranchOfc(String ccBranchOfc) {
		this.ccBranchOfc = ccBranchOfc;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getMainMemberName() {
		return mainMemberName;
	}
	public void setMainMemberName(String mainMemberName) {
		this.mainMemberName = mainMemberName;
	}
	public String getNameOfTheInsured() {
		return nameOfTheInsured;
	}
	public void setNameOfTheInsured(String nameOfTheInsured) {
		this.nameOfTheInsured = nameOfTheInsured;
	}
	public Double getSettledAmount() {
		return settledAmount;
	}
	public void setSettledAmount(Double settledAmount) {
		this.settledAmount = settledAmount;
	}
	public Date getAdmissionDate() {
		return admissionDate;
	}
	public void setAdmissionDate(Date admissionDate) {
		if(admissionDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(admissionDate);
			setAdmissionDateValue(dateformat);
		    this.admissionDateValue = dateformat;
		}
	}
	public String getAdmissionDateValue() {
		return admissionDateValue;
	}
	public void setAdmissionDateValue(String admissionDateValue) {
		this.admissionDateValue = admissionDateValue;
	}
	public String getDdNo() {
		return ddNo;
	}
	public void setDdNo(String ddNo) {
		this.ddNo = ddNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Date getBillReceivedDate() {
		return billReceivedDate;
	}
	public void setBillReceivedDate(Date billReceivedDate) {
		
		if(billReceivedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(billReceivedDate);
			setBillReceivedDateValue(dateformat);
		    this.billReceivedDateValue = dateformat;
		}
	}
	public String getBillReceivedDateValue() {
		return billReceivedDateValue;
	}
	public void setBillReceivedDateValue(String billReceivedDateValue) {
		this.billReceivedDateValue = billReceivedDateValue;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getDischargeDate() {
		return dischargeDate;
	}
	public void setDischargeDate(Date dischargeDate) {		
		if(dischargeDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(dischargeDate);
			setDischargeDateValue(dateformat);
			this.dischargeDateValue = dateformat;
		}
	}
	public String getDischargeDateValue() {
		return dischargeDateValue;
	}
	public void setDischargeDateValue(String dischargeDateValue) {
		this.dischargeDateValue = dischargeDateValue;
	}
	public Date getDdDate() {
		return ddDate;
	}
	public void setDdDate(Date ddDate) {
		if(dischargeDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(ddDate);
			setDdDateValue(dateformat);
		this.ddDateValue = dateformat;
		}
	}
	public String getDdDateValue() {
		return ddDateValue;
	}
	public void setDdDateValue(String ddDateValue) {
		this.ddDateValue = ddDateValue;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		
		if(billDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(billDate);
			setBillDateValue(dateformat);
		    this.billDateValue = dateformat;
		}
		
	}
	public String getBillDateValue() {
		return billDateValue;
	}
	public void setBillDateValue(String billDateValue) {
		this.billDateValue = billDateValue;
		
	}
	
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public Reimbursement getReimbursementDto() {
		return reimbursementObj;
	}

	public void setReimbursementDto(Reimbursement reimbursementDto) {
		this.reimbursementObj = reimbursementDto;
	}

	public Boolean getLetterPrintingMode() {
		return letterPrintingMode;
	}

	public void setLetterPrintingMode(Boolean letterPrintingMode) {
				this.letterPrintingMode = letterPrintingMode;
		//	this.letterPrintingModeFlag = letterPrintingMode?"Y":"N";
	}

	public String getLetterPrintingModeFlag() {
		return letterPrintingModeFlag;
	}

	public void setLetterPrintingModeFlag(String letterPrintingModeFlag) {
		this.letterPrintingModeFlag = letterPrintingModeFlag;
	}

	public Long getClaimPaymentKey() {
		return claimPaymentKey;
	}

	public void setClaimPaymentKey(Long claimPaymentKey) {
		this.claimPaymentKey = claimPaymentKey;
	}

	public String getAmountInwords() {
		return amountInwords;
	}

	public void setAmountInwords(String amountInwords) {
		this.amountInwords = amountInwords;
	}

	public HashMap getFilePathAndTypeMap() {
		return filePathAndTypeMap;
	}

	public void setFilePathAndTypeMap(HashMap filePathAndTypeMap) {
		this.filePathAndTypeMap = filePathAndTypeMap;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDischargeVoucherUrl() {
		return dischargeVoucherUrl;
	}

	public void setDischargeVoucherUrl(String dischargeVoucherUrl) {
		this.dischargeVoucherUrl = dischargeVoucherUrl;
	}

	public String getPaymentDischargeUrl() {
		return paymentDischargeUrl;
	}

	public void setPaymentDischargeUrl(String paymentDischargeUrl) {
		this.paymentDischargeUrl = paymentDischargeUrl;
	}

	public String getDvCoveringUrl() {
		return dvCoveringUrl;
	}

	public void setDvCoveringUrl(String dvCoveringUrl) {
		this.dvCoveringUrl = dvCoveringUrl;
	}

	public Double getIntrestAmount() {
		return intrestAmount;
	}

	public void setIntrestAmount(Double intrestAmount) {
		this.intrestAmount = intrestAmount;
	}

	public String getIntrestAmountInWords() {
		return intrestAmountInWords;
	}

	public void setIntrestAmountInWords(String intrestAmountInWords) {
		this.intrestAmountInWords = intrestAmountInWords;
	}

	public String getTotalAmntInWords() {
		return totalAmntInWords;
	}

	public void setTotalAmntInWords(String totalAmntInWords) {
		this.totalAmntInWords = totalAmntInWords;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public List<LegalHeirDTO> getLegalHeirDTOList() {
		return legalHeirDTOList;
	}

	public void setLegalHeirDTOList(List<LegalHeirDTO> legalHeirDTOList) {
		this.legalHeirDTOList = legalHeirDTOList;
	}

	public Long getModeOfReceipt() {
		return modeOfReceipt;
	}

	public void setModeOfReceipt(Long modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}
	
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getAccNumber() {
		return accNumber;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayableAt() {
		return payableAt;
	}

	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getPaymentCpu() {
		return paymentCpu;
	}

	public void setPaymentCpu(String paymentCpu) {
		this.paymentCpu = paymentCpu;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRodType() {
		return rodType;
	}

	public void setRodType(String rodType) {
		this.rodType = rodType;
	}

	public PaymentProcessCpuTableDTO getPaymentProcessDto() {
		return paymentProcessDto;
	}

	public void setPaymentProcessDto(PaymentProcessCpuTableDTO paymentProcessDto) {
		this.paymentProcessDto = paymentProcessDto;
	}

	public String getCpuCodeWithValue() {
		return cpuCodeWithValue;
	}

	public void setCpuCodeWithValue(String cpuCodeWithValue) {
		this.cpuCodeWithValue = cpuCodeWithValue;
	}	
	
	public String getInsuredGender() {
		return insuredGender;
	}

	public void setInsuredGender(String insuredGender) {
		this.insuredGender = insuredGender;
	}

	public LegalHeir getLegalHeir() {
		return legalHeir;
	}

	public void setLegalHeir(LegalHeir legalHeir) {
		this.legalHeir = legalHeir;
	}

	public String getTdsAmountInWords() {
		return tdsAmountInWords;
	}

	public void setTdsAmountInWords(String tdsAmountInWords) {
		this.tdsAmountInWords = tdsAmountInWords;
	}

	public OrganaizationUnit getBranchOffice() {
		return branchOffice;
	}

	public void setBranchOffice(OrganaizationUnit branchOffice) {
		this.branchOffice = branchOffice;
	}

	public OrganaizationUnit getParentOffice() {
		return parentOffice;
	}

	public void setParentOffice(OrganaizationUnit parentOffice) {
		this.parentOffice = parentOffice;
	}

	public String getBranchOfficeState() {
		return branchOfficeState;
	}

	public void setBranchOfficeState(String branchOfficeState) {
		this.branchOfficeState = branchOfficeState;
	}

	public String getParentOfficeState() {
		return parentOfficeState;
	}

	public void setParentOfficeState(String parentOfficeState) {
		this.parentOfficeState = parentOfficeState;
	}

	public Date getTodayDate() {
		return todayDate;
	}

	public void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}

	public String getPaymentPartyMode() {
		return paymentPartyMode;
	}

	public void setPaymentPartyMode(String paymentPaymentMode) {
		this.paymentPartyMode = paymentPaymentMode;
	}	
	
}