package com.shaic.claim.rod.searchCriteria;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;
import com.vaadin.v7.ui.TextField;

public class ViewSearchCriteriaTableDTO extends AbstractTableDTO{

	private String ifscCode;
	
	private String bankName;
	
	private String branchName;
	
	private String address;
	
	private String city; 
	
	private Long bankId;
	
	private String cpuCodeWithDescription;
	
	private String payeeName;
	
	private String relationship;
	
	private String changePayeeName;
	
	private TextField txtPayeeName;
	
	private String accType;
	
	private String accPreference;
	
	private String panNumber;
	
	private String micrCode;
	
	private String effectiveFrmDate;
	
	private String effectiveToDate;
	
	private String accNumber;
	
	public ViewSearchCriteriaTableDTO() {
		
	}

	public ViewSearchCriteriaTableDTO(BankDetailsTableDTO bankDto) {
		this.ifscCode = bankDto.getIfscCode();
		this.bankName = bankDto.getNameOfBank();
		this.branchName = bankDto.getBranchName();
		this.address = bankDto.getVirtualPaymentAddr();
//		this.city = bankDto.get
		this.payeeName = bankDto.getNamePerBankAccnt();
		this.accNumber = bankDto.getAccountNumber();
		this.accType = bankDto.getAccountType();
		this.accPreference = bankDto.getPreference();
		this.effectiveFrmDate = bankDto.getEffectiveFromDate();
		this.effectiveToDate = bankDto.getEffectiveToDate();
		this.micrCode = bankDto.getMicrCode();
//		this.panNumber = bankDto.getPanNumber();
		
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

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getCpuCodeWithDescription() {
		return cpuCodeWithDescription;
	}

	public void setCpuCodeWithDescription(String cpuCodeWithDescription) {
		this.cpuCodeWithDescription = cpuCodeWithDescription;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getChangePayeeName() {
		return changePayeeName;
	}

	public void setChangePayeeName(String changePayeeName) {
		this.changePayeeName = changePayeeName;
	}

	public TextField getTxtPayeeName() {
		return txtPayeeName;
	}

	public void setTxtPayeeName(TextField txtPayeeName) {
		this.txtPayeeName = txtPayeeName;
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

	public String getAccPreference() {
		return accPreference;
	}

	public void setAccPreference(String accPreference) {
		this.accPreference = accPreference;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getMicrCode() {
		return micrCode;
	}

	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}

	public String getEffectiveFrmDate() {
		return effectiveFrmDate;
	}

	public void setEffectiveFrmDate(String effectiveFrmDate) {
		this.effectiveFrmDate = effectiveFrmDate;
	}

	public String getEffectiveToDate() {
		return effectiveToDate;
	}

	public void setEffectiveToDate(String effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
	}	

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
}