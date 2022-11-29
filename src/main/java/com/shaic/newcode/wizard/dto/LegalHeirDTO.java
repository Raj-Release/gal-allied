package com.shaic.newcode.wizard.dto;

import java.util.List;

import com.shaic.arch.fields.dto.AbstractSearchDTO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.domain.LegalHeir;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class LegalHeirDTO  extends AbstractTableDTO{

	private int slNo = 1;
	private String heirName;
	private String relationshipCode;
	private SelectValue relationship;
	private Double sharePercentage;
	private String address;
	private SelectValue accountType;
	private SelectValue accountPreference;
	private Boolean paymentMode;
	private String paymentModeChangeReason;
	private Long paymentModeId;
	private String payableAt;
	private List<SelectValue> accountTypeContainer;
	private String beneficiaryName;
	private String accountNo;
	private String ifscCode;
	private List<SelectValue> relationshipContainer;
	private List<LegalHeirDTO> legalHeir;
	private SelectValue docType;
	private Long documentToken;
	private String fileName;
	private String deleteLegalHeir;

	private Long legalHeirKey;
	private Long insuredKey;
	private Long policyKey;
	private Long rodKey;
	private String pincode;
	
	private String bankName;
	private String bankBranchName;
	
	private ViewSearchCriteriaTableDTO accoutDetailsDto;
	
	
	public LegalHeirDTO(){
		
	}
	public LegalHeirDTO(LegalHeir legalHeir)
	{
		this.setHeirName(legalHeir.getLegalHeirName());
		SelectValue value = new SelectValue();
		value.setId(legalHeir.getRelationCode());
		value.setValue(legalHeir.getRelationDesc());
		this.setRelationship(value);
		this.setSharePercentage(legalHeir.getSharePercentage());
		this.setAddress(legalHeir.getAddress());
		this.setBeneficiaryName(legalHeir.getBeneficiaryName());
		this.setAccountNo(legalHeir.getAccountNo() != null ? legalHeir.getAccountNo().toString() : "");
		this.setBankName(legalHeir.getBankName());
		this.setBankBranchName(legalHeir.getBankBranchName());
		this.setPaymentModeId(legalHeir.getPaymentModeId());
		this.setIfscCode(legalHeir.getIfscCode());
		this.setLegalHeirKey(legalHeir.getKey());
		this.setRodKey(legalHeir.getRodKey());
		this.setPolicyKey(legalHeir.getPolicyKey());
		this.setInsuredKey(legalHeir.getInsuredKey());
		if(legalHeir.getAccountType() != null && !legalHeir.getAccountType().isEmpty()) {
			this.setAccountType(new SelectValue(1l,legalHeir.getAccountType()));
		}
		// Below Condition for Account Preference mandatory for Bancs
		if(legalHeir.getAccountPreference() != null && !legalHeir.getAccountPreference().isEmpty()) {
            if(legalHeir.getAccountPreference().equalsIgnoreCase("PRIMARY")){
                    this.setAccountPreference(new SelectValue(1l,legalHeir.getAccountPreference()));
            }else{
                    this.setAccountPreference(new SelectValue(2l,legalHeir.getAccountPreference()));
            }
		}
		this.setPayableAt(legalHeir.getPayableAt());
		this.setPincode(legalHeir.getPincode());
		this.setDocumentToken(legalHeir.getDocKey());
		
	}

	public String getHeirName() {
		return heirName;
	}
	public void setHeirName(String heirName) {
		this.heirName = heirName;
	}
	public SelectValue getRelationship() {
		return relationship;
	}
	public void setRelationship(SelectValue relationship) {
		this.relationship = relationship;
	}
	public SelectValue getAccountType() {
		return accountType;
	}
	public void setAccountType(SelectValue accountType) {
		this.accountType = accountType;
	}
	public Double getSharePercentage() {
		return sharePercentage;
	}
	public void setSharePercentage(Double sharePercentage) {
		this.sharePercentage = sharePercentage;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBeneficiaryName() {
		return beneficiaryName;
	}
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public int getSlNo() {
		return slNo;
	}
	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}
	public String getRelationshipCode() {
		return relationshipCode;
	}
	public void setRelationshipCode(String relationshipCode) {
		this.relationshipCode = relationshipCode;
	}
	public List<SelectValue> getRelationshipContainer() {
		return relationshipContainer;
	}
	public void setRelationshipContainer(List<SelectValue> relationshipContainer) {
		this.relationshipContainer = relationshipContainer;
	}
	public List<LegalHeirDTO> getLegalHeir() {
		return legalHeir;
	}
	public void setLegalHeir(List<LegalHeirDTO> legalHeir) {
		this.legalHeir = legalHeir;
	}
	public SelectValue getDocType() {
		return docType;
	}
	public void setDocType(SelectValue docType) {
		this.docType = docType;
	}
	public List<SelectValue> getAccountTypeContainer() {
		return accountTypeContainer;
	}
	public void setAccountTypeContainer(List<SelectValue> accountTypeContainer) {
		this.accountTypeContainer = accountTypeContainer;
	}
	public Long getDocumentToken() {
		return documentToken;
	}
	public void setDocumentToken(Long documentToken) {
		this.documentToken = documentToken;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDeleteLegalHeir() {
		return deleteLegalHeir;
	}
	public void setDeleteLegalHeir(String deleteLegalHeir) {
		this.deleteLegalHeir = deleteLegalHeir;
	}
	public Long getLegalHeirKey() {
		return legalHeirKey;
	}
	public void setLegalHeirKey(Long legalHeirKey) {
		this.legalHeirKey = legalHeirKey;
	}
	public Long getInsuredKey() {
		return insuredKey;
	}
	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}
	public Long getPolicyKey() {
		return policyKey;
	}
	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public ViewSearchCriteriaTableDTO getAccoutDetailsDto() {
		return accoutDetailsDto;
	}
	public void setAccoutDetailsDto(ViewSearchCriteriaTableDTO accoutDetailsDto) {
		this.accoutDetailsDto = accoutDetailsDto;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public Boolean getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(Boolean paymentMode) {
		this.paymentMode = paymentMode;
		
		this.paymentModeId = this.paymentMode != null && this.paymentMode ? ReferenceTable.PAYMENT_MODE_CHEQUE_DD : ReferenceTable.PAYMENT_MODE_BANK_TRANSFER;
	}
	public Long getPaymentModeId() {
		return paymentModeId;
	}
	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
		
		this.paymentMode = this.paymentModeId != null && ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(paymentModeId) ? true  : false;
		
	}
	public String getPayableAt() {
		return payableAt;
	}
	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}
	public String getPaymentModeChangeReason() {
		return paymentModeChangeReason;
	}
	public void setPaymentModeChangeReason(String paymentModeChangeReason) {
		this.paymentModeChangeReason = paymentModeChangeReason;
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
	public SelectValue getAccountPreference() {
		return accountPreference;
	}
	public void setAccountPreference(SelectValue accountPreference) {
		this.accountPreference = accountPreference;
	}	
}