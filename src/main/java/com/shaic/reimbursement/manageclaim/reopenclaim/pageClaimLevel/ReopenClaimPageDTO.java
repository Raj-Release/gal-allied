package com.shaic.reimbursement.manageclaim.reopenclaim.pageClaimLevel;

import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;


public class ReopenClaimPageDTO {
	
	 private NewIntimationDto newIntimationDto;
	 
	 private String intimationNumber;
	 
	 private String claimNumber;
	    
	 private ClaimDto claimDto;
	 
	 private String diagnosis;
	 
	 private Long claimKey;
	 
	 private Double provisionAmount;
	 
	 private SelectValue reasonForReopen;
	 
	 private String reOpenRemarks;
	 
	 private String userName;
	 
	 private Double balanceSI;
	 
	 private BeanItemContainer<SelectValue> reopenContainer;
	
	 
	 private List<PreviousPreAuthTableDTO> previousPreauthDetailsList;
	 
	 private List<ViewDocumentDetailsDTO> rodDocumentDetailsList;
	 
	 private List<ReopenClaimTableDTO> reopenClaimList;
	 
	 public ReopenClaimPageDTO(){
		 previousPreauthDetailsList = new ArrayList<PreviousPreAuthTableDTO>();
		 rodDocumentDetailsList = new ArrayList<ViewDocumentDetailsDTO>();
		 reopenClaimList = new ArrayList<ReopenClaimTableDTO>();
	 }
	 
	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}
	
	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public List<PreviousPreAuthTableDTO> getPreviousPreauthDetailsList() {
		return previousPreauthDetailsList;
	}

	public void setPreviousPreauthDetailsList(
			List<PreviousPreAuthTableDTO> previousPreauthDetailsList) {
		this.previousPreauthDetailsList = previousPreauthDetailsList;
	}

	public List<ViewDocumentDetailsDTO> getRodDocumentDetailsList() {
		return rodDocumentDetailsList;
	}

	public void setRodDocumentDetailsList(
			List<ViewDocumentDetailsDTO> rodDocumentDetailsList) {
		this.rodDocumentDetailsList = rodDocumentDetailsList;
	}

	public List<ReopenClaimTableDTO> getReopenClaimList() {
		return reopenClaimList;
	}

	public void setReopenClaimList(List<ReopenClaimTableDTO> reopenClaimList) {
		this.reopenClaimList = reopenClaimList;
	}

	public BeanItemContainer<SelectValue> getReopenContainer() {
		return reopenContainer;
	}

	public void setReopenContainer(BeanItemContainer<SelectValue> reopenContainer) {
		this.reopenContainer = reopenContainer;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public SelectValue getReasonForReopen() {
		return reasonForReopen;
	}

	public void setReasonForReopen(SelectValue reasonForReopen) {
		this.reasonForReopen = reasonForReopen;
	}

	public String getReOpenRemarks() {
		return reOpenRemarks;
	}

	public void setReOpenRemarks(String reOpenRemarks) {
		this.reOpenRemarks = reOpenRemarks;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(Double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	public Double getBalanceSI() {
		return balanceSI;
	}

	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}
	
	


}
