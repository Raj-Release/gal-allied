package com.shaic.newcode.wizard.dto;

import java.text.SimpleDateFormat;

import com.shaic.domain.NomineeDetails;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.ProposerNominee;
import com.shaic.domain.ReferenceTable;

public class NomineeDetailsDto {

	private String sno;
	private String nomineeName;
	private String nomineeAge;
	private String nomineeRelationship;
	private String nomineePercent;
	private String nomineeDob;
	private String appointeeName;
	private String appointeeAge;
	private String appointeeRelationship;
	private boolean selectedNominee;	
	private String selectedNomineeFlag;
	
	private Long proposerNomineeKey;
	private Long claimKey;
	private Long insuredKey;
	private Long transactionKey;
	private String transactionType;
	
	private String modifiedBy;
	
	private Long policyNomineeKey;
	
	private String preference;
	private String accType;
	private String accNumber;
	private String ifscCode;
	private String bankName;
	private Long bankId;
	private String bankBranchName;
	private String bankCity;
	private String payableAt;
	private boolean paymentMode;
	private Long paymentModeId;
	
	private String nameAsPerBankAc;
	private String micrCode;
	private String panNumber;
	private String virtualPayAdd;
	private String effFrmDate;
	private String effToDate;
	private String nomineeCode;
	
	
	public NomineeDetailsDto(){
		
	}
	
	public NomineeDetailsDto(NomineeDetails nomineeDetails) {
		
		this.nomineeName = nomineeDetails.getNomineeName();
		this.nomineeAge = nomineeDetails.getNomineeAge() != null ? String.valueOf(nomineeDetails.getNomineeAge()) : "";
		this.nomineeDob = ""; // No Place Holder in Table
		this.nomineeRelationship = nomineeDetails.getRelationId() != null ? nomineeDetails.getRelationId().getValue() : "";
		this.nomineePercent = nomineeDetails.getNomineeSharePercent() != null ? (nomineeDetails.getNomineeSharePercent() + "%") : "";		
		
	}
	
	public NomineeDetailsDto(ProposerNominee nomineeDetails) {
		
		this.proposerNomineeKey = nomineeDetails.getKey();
		this.policyNomineeKey = nomineeDetails.getPolicyNominee().getKey();
		this.nomineeName = nomineeDetails.getNomineeName();
		this.nomineeAge = nomineeDetails.getNomineeAge() != null ? String.valueOf(nomineeDetails.getNomineeAge()) : "";
		this.nomineeDob = nomineeDetails.getNomineeDob() != null ? new SimpleDateFormat("dd/MM/yyyy").format(nomineeDetails.getNomineeDob()) : ""; 
		this.nomineeRelationship = nomineeDetails.getRelationshipWithProposer() != null ? nomineeDetails.getRelationshipWithProposer() : "";
		this.nomineePercent = nomineeDetails.getSharePercent() != null ? (nomineeDetails.getSharePercent() + "%") : "";
		this.appointeeName = nomineeDetails.getAppointeeName() != null ? nomineeDetails.getAppointeeName().trim() : "";
		this.appointeeAge = nomineeDetails.getAppointeeAge() != null ? String.valueOf(nomineeDetails.getAppointeeAge()) : "";
		this.appointeeRelationship = nomineeDetails.getAppointeeRelationship() != null ? nomineeDetails.getAppointeeRelationship() : "";
		this.selectedNomineeFlag = nomineeDetails.getSelectedFlag() != null ? nomineeDetails.getSelectedFlag() : ReferenceTable.NO_FLAG;

		this.selectedNominee = this.selectedNomineeFlag.equalsIgnoreCase(ReferenceTable.YES_FLAG) ? true : false;
		
		//this.nameAsPerBankAc = nomineeDetails.getPolicyNominee().getNameAsPerBank() != null ? nomineeDetails.getPolicyNominee().getNameAsPerBank() : nomineeDetails.getNameAsperBankAcc() != null ? nomineeDetails.getNameAsperBankAcc() : "";
		this.nameAsPerBankAc = nomineeDetails.getNameAsperBankAcc() != null ? nomineeDetails.getNameAsperBankAcc() : "";
//		this.micrCode = nomineeDetails.getMicrCode();
//		this.panNumber = nomineeDetails.getPanNumber();
//		this.virtualPayAdd = nomineeDetails.getVirtualPaymentAddress();
//		this.effFrmDate = nomineeDetails.get;
//		this.effToDate = nomineeDetails.getE;
		this.nomineeCode = nomineeDetails.getNomineeCode();

		//this.accNumber = nomineeDetails.getPolicyNominee().getAccountNumber() != null ? nomineeDetails.getPolicyNominee().getAccountNumber() : nomineeDetails.getAccNumber() != null ? nomineeDetails.getAccNumber() : "";
		this.accNumber = nomineeDetails.getAccNumber() != null ? nomineeDetails.getAccNumber() : "";
//		this.preference = nomineeDetails.getAccPreference();
//		this.accType = nomineeDetails.getAccType();
		//this.ifscCode = nomineeDetails.getPolicyNominee().getIFSCcode() != null ? nomineeDetails.getPolicyNominee().getIFSCcode() :  nomineeDetails.getIfscCode() != null ? nomineeDetails.getIfscCode() : "";
		this.ifscCode = nomineeDetails.getIfscCode() != null ? nomineeDetails.getIfscCode() : "";
		this.bankName = nomineeDetails.getBankName();
		this.bankBranchName =  nomineeDetails.getBankBranchName();
//		this.bankCity = nomineeDetails.get
		this.paymentModeId = nomineeDetails.getPaymentModeId();
		
		this.paymentMode = nomineeDetails.getPaymentModeId() == null || (nomineeDetails.getPaymentModeId() != null && nomineeDetails.getPaymentModeId().equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)) ? true : false;

	}
	
	public NomineeDetailsDto(PolicyNominee nomineeDetails) {
		
		this.policyNomineeKey = nomineeDetails.getKey();
		this.nomineeName = nomineeDetails.getNomineeName();
		this.nomineeAge = nomineeDetails.getNomineeAge() != null ? String.valueOf(nomineeDetails.getNomineeAge()) : "";
		this.nomineeDob = nomineeDetails.getNomineeDob() != null ? new SimpleDateFormat("dd/MM/yyyy").format(nomineeDetails.getNomineeDob()) : ""; 
		this.nomineeRelationship = nomineeDetails.getRelationshipWithProposer() != null ? nomineeDetails.getRelationshipWithProposer() : "";
		this.nomineePercent = nomineeDetails.getSharePercent() != null ? (nomineeDetails.getSharePercent() + "%") : "";
		this.appointeeName = nomineeDetails.getAppointeeName() != null ? nomineeDetails.getAppointeeName().trim() : "";
		this.appointeeAge = nomineeDetails.getAppointeeAge() != null ? String.valueOf(nomineeDetails.getAppointeeAge()) : "";
		this.appointeeRelationship = nomineeDetails.getAppointeeRelationship() != null ? nomineeDetails.getAppointeeRelationship() : "";
		this.selectedNomineeFlag = ReferenceTable.NO_FLAG;
		this.nomineeCode = nomineeDetails.getNomineeCode();
		
		this.accNumber = nomineeDetails.getAccountNumber() != null ? nomineeDetails.getAccountNumber() : "";
		this.nameAsPerBankAc = nomineeDetails.getNameAsPerBank() != null ? nomineeDetails.getNameAsPerBank() : "";
		this.ifscCode = nomineeDetails.getIFSCcode() != null ? nomineeDetails.getIFSCcode() : "";		

		this.selectedNominee = false;		
	}
	
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getNomineeName() {
		return nomineeName;
	}
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	public String getNomineeAge() {
		return nomineeAge;
	}
	public void setNomineeAge(String nomineeAge) {
		this.nomineeAge = nomineeAge;
	}
	public String getNomineeRelationship() {
		return nomineeRelationship;
	}
	public void setNomineeRelationship(String nomineeRelationship) {
		this.nomineeRelationship = nomineeRelationship;
	}
	public String getNomineePercent() {
		return nomineePercent;
	}
	public void setNomineePercent(String nomineePercent) {
		this.nomineePercent = nomineePercent;
	}
	public String getNomineeDob() {
		return nomineeDob;
	}
	public void setNomineeDob(String nomineeDob) {
		this.nomineeDob = nomineeDob;
	}
	public String getAppointeeName() {
		return appointeeName;
	}
	public void setAppointeeName(String appointeeName) {
		this.appointeeName = appointeeName;
	}
	public String getAppointeeAge() {
		return appointeeAge;
	}
	public void setAppointeeAge(String appointeeAge) {
		this.appointeeAge = appointeeAge;
	}
	public String getAppointeeRelationship() {
		return appointeeRelationship;
	}
	public void setAppointeeRelationship(String appointeeRelationship) {
		this.appointeeRelationship = appointeeRelationship;
	}
	public boolean isSelectedNominee() {
		return selectedNominee;
	}
	public void setSelectedNominee(boolean selectedNominee) {
		this.selectedNominee = selectedNominee;
		
		this.selectedNomineeFlag = this.selectedNominee ? ReferenceTable.YES_FLAG : ReferenceTable.NO_FLAG;  
	}

	public String getSelectedNomineeFlag() {
		return selectedNomineeFlag;
	}

	public void setSelectedNomineeFlag(String selectedNomineeFlag) {
		this.selectedNomineeFlag = selectedNomineeFlag;
		
		this.selectedNominee = this.selectedNomineeFlag != null && ReferenceTable.YES_FLAG.equalsIgnoreCase(this.selectedNomineeFlag) ? true : false; 
		
	}
	
	public Long getProposerNomineeKey() {
		return proposerNomineeKey;
	}

	public void setProposerNomineeKey(Long proposerNomineeKey) {
		this.proposerNomineeKey = proposerNomineeKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getPolicyNomineeKey() {
		return policyNomineeKey;
	}

	public void setPolicyNomineeKey(Long policyNomineeKey) {
		this.policyNomineeKey = policyNomineeKey;
	}	
		
	public String getPreference() {
		return preference;
	}

	public void setPreference(String preference) {
		this.preference = preference;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getPayableAt() {
		return payableAt;
	}

	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}

	public boolean isPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(boolean paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Long getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}
	
	public String getNameAsPerBankAc() {
		return nameAsPerBankAc;
	}

	public void setNameAsPerBankAc(String nameAsPerBankAc) {
		this.nameAsPerBankAc = nameAsPerBankAc;
	}

	public String getMicrCode() {
		return micrCode;
	}

	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getVirtualPayAdd() {
		return virtualPayAdd;
	}

	public void setVirtualPayAdd(String virtualPayAdd) {
		this.virtualPayAdd = virtualPayAdd;
	}

	public String getEffFrmDate() {
		return effFrmDate;
	}

	public void setEffFrmDate(String effFrmDate) {
		this.effFrmDate = effFrmDate;
	}

	public String getEffToDate() {
		return effToDate;
	}

	public void setEffToDate(String effToDate) {
		this.effToDate = effToDate;
	}

	public String getNomineeCode() {
		return nomineeCode;
	}

	public void setNomineeCode(String nomineeCode) {
		this.nomineeCode = nomineeCode;
	}	
	
}