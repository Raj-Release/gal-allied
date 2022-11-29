package com.shaic.claim.corpbuffer.allocation.wizard;

import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageClaimLevel.ReopenClaimTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class AllocateCorpBufferDetailDTO {
	
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
	 
	 private Double disBufferSI;
	 
	 private Double disBufferUtilizedAmt;
	 
	 private Double policywisedisBufferAvlBlnc;
	 
	 private String disBufferApplTo;
	 
	 private String disBufferIndviSI;
	 
	 private String insureName;
	 
	 private Long insureAge;
	 
	 private String insurerelationShip;
	 
	 private Double maxdisBufferInsuredLimit ;
	 
	 private Double disBufferInsuredLimit = 0d;
	 
	 private Double disBufferAvailBalnc = 0d;
	 
	 private Boolean disupdateCorpBuffer;

	 private Double maxwintageBufferLimit;

	 private Double wintageBufferLimit = 0d;
	 
	 private Double wintageBufferAvlBalnc = 0d;

	 private Boolean wintageupdateCorpBuffer;
	 
	 private Double maxnacBufferLimit;

	 private Double nacBufferLimit = 0d;
	 
	 private Double nacBufferAvlBalnc = 0d;

	 private Boolean nacupdateCorpBuffer;
	 
	 private String policyNo;
	 
	 private Long insuredKey;

	 private Long insuredmainNo;
	 
	 private Boolean isEmployee;
	 
	 private Boolean isDependent;
	 
	 private String stageInformation;
	 
	 private Long insuredNo;
	 
	 private Double disAllocatedLimit = 0d;
	 
	 private Double wintageAllocatedLimit = 0d;
	 
	 private Double nacAllocatedLimit = 0d;
	 
	 private Double disAvlBalnc = 0d;

	 private Double wintageAvlBalnc = 0d;

	 private Double nacAvlBalnc = 0d;
	 
	 private Double noOfTimes = 0d;
	 
	 private Double discretionaryUtilizedInsured = 0d;
	 
	 private String winBufferIndviSI;
	 
	 private SelectValue winBufferApplTo;
	 
	 private String wintageNoOfTimes ="NA";
	 
	 private Double wintageUtilizedInsured = 0d;
	 
     private String nacbBufferIndviSI;
	 
	 private SelectValue nacbBufferApplTo;
	 
	 private String nacbNoOfTimes ="NA";
	 
	 private Double nacbUtilizedInsured = 0d;
	 
	 private String maxWinBufferInsuredLimit = "NA";
	 
	 private String maxNacbBufferInsuredLimit = "NA";
	 
	 private Boolean isDisBufferApplicable = false;
	 
	 private Boolean isWintageBufferApplicable = false;
	 
	 private Boolean isNacBufferApplicable = false;
	 
	 private Double policyWise;
	 
	 private Boolean isDisbufferClicked = false;
	 
	 private Boolean isWinbufferClicked = false;
	 
	 private Boolean isNacbufferClicked = false;
	 
	 public AllocateCorpBufferDetailDTO() {
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

	public Double getDisBufferSI() {
		return disBufferSI;
	}

	public void setDisBufferSI(Double disBufferSI) {
		this.disBufferSI = disBufferSI;
	}

	public Double getDisBufferUtilizedAmt() {
		return disBufferUtilizedAmt;
	}

	public void setDisBufferUtilizedAmt(Double disBufferUtilizedAmt) {
		this.disBufferUtilizedAmt = disBufferUtilizedAmt;
	}

	public Double getPolicywisedisBufferAvlBlnc() {
		return policywisedisBufferAvlBlnc;
	}

	public void setPolicywisedisBufferAvlBlnc(Double policywisedisBufferAvlBlnc) {
		this.policywisedisBufferAvlBlnc = policywisedisBufferAvlBlnc;
	}

	public String getDisBufferApplTo() {
		return disBufferApplTo;
	}

	public void setDisBufferApplTo(String disBufferApplTo) {
		this.disBufferApplTo = disBufferApplTo;
	}

	public String getDisBufferIndviSI() {
		return disBufferIndviSI;
	}

	public void setDisBufferIndviSI(String disBufferIndviSI) {
		this.disBufferIndviSI = disBufferIndviSI;
	}

	public String getInsureName() {
		return insureName;
	}

	public void setInsureName(String insureName) {
		this.insureName = insureName;
	}

	public Long getInsureAge() {
		return insureAge;
	}

	public void setInsureAge(Long insureAge) {
		this.insureAge = insureAge;
	}

	public String getInsurerelationShip() {
		return insurerelationShip;
	}

	public void setInsurerelationShip(String insurerelationShip) {
		this.insurerelationShip = insurerelationShip;
	}

	public Double getMaxdisBufferInsuredLimit() {
		return maxdisBufferInsuredLimit;
	}

	public void setMaxdisBufferInsuredLimit(Double maxdisBufferInsuredLimit) {
		this.maxdisBufferInsuredLimit = maxdisBufferInsuredLimit;
	}

	public Double getDisBufferInsuredLimit() {
		return disBufferInsuredLimit;
	}

	public void setDisBufferInsuredLimit(Double disBufferInsuredLimit) {
		this.disBufferInsuredLimit = disBufferInsuredLimit;
	}

	public Double getDisBufferAvailBalnc() {
		return disBufferAvailBalnc;
	}

	public void setDisBufferAvailBalnc(Double disBufferAvailBalnc) {
		this.disBufferAvailBalnc = disBufferAvailBalnc;
	}

	public Boolean getDisupdateCorpBuffer() {
		return disupdateCorpBuffer;
	}

	public void setDisupdateCorpBuffer(Boolean disupdateCorpBuffer) {
		this.disupdateCorpBuffer = disupdateCorpBuffer;
	}

	public Double getMaxwintageBufferLimit() {
		return maxwintageBufferLimit;
	}

	public void setMaxwintageBufferLimit(Double maxwintageBufferLimit) {
		this.maxwintageBufferLimit = maxwintageBufferLimit;
	}

	public Double getWintageBufferLimit() {
		return wintageBufferLimit;
	}

	public void setWintageBufferLimit(Double wintageBufferLimit) {
		this.wintageBufferLimit = wintageBufferLimit;
	}

	public Double getWintageBufferAvlBalnc() {
		return wintageBufferAvlBalnc;
	}

	public void setWintageBufferAvlBalnc(Double wintageBufferAvlBalnc) {
		this.wintageBufferAvlBalnc = wintageBufferAvlBalnc;
	}

	public Boolean getWintageupdateCorpBuffer() {
		return wintageupdateCorpBuffer;
	}

	public void setWintageupdateCorpBuffer(Boolean wintageupdateCorpBuffer) {
		this.wintageupdateCorpBuffer = wintageupdateCorpBuffer;
	}

	public Double getMaxnacBufferLimit() {
		return maxnacBufferLimit;
	}

	public void setMaxnacBufferLimit(Double maxnacBufferLimit) {
		this.maxnacBufferLimit = maxnacBufferLimit;
	}

	public Double getNacBufferLimit() {
		return nacBufferLimit;
	}

	public void setNacBufferLimit(Double nacBufferLimit) {
		this.nacBufferLimit = nacBufferLimit;
	}

	public Double getNacBufferAvlBalnc() {
		return nacBufferAvlBalnc;
	}

	public void setNacBufferAvlBalnc(Double nacBufferAvlBalnc) {
		this.nacBufferAvlBalnc = nacBufferAvlBalnc;
	}

	public Boolean getNacupdateCorpBuffer() {
		return nacupdateCorpBuffer;
	}

	public void setNacupdateCorpBuffer(Boolean nacupdateCorpBuffer) {
		this.nacupdateCorpBuffer = nacupdateCorpBuffer;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public Long getInsuredmainNo() {
		return insuredmainNo;
	}

	public void setInsuredmainNo(Long insuredmainNo) {
		this.insuredmainNo = insuredmainNo;
	}

	public Boolean getIsEmployee() {
		return isEmployee;
	}

	public void setIsEmployee(Boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	public Boolean getIsDependent() {
		return isDependent;
	}

	public void setIsDependent(Boolean isDependent) {
		this.isDependent = isDependent;
	}

	public String getStageInformation() {
		return stageInformation;
	}

	public void setStageInformation(String stageInformation) {
		this.stageInformation = stageInformation;
	}

	public Long getInsuredNo() {
		return insuredNo;
	}

	public void setInsuredNo(Long insuredNo) {
		this.insuredNo = insuredNo;
	}

	public Double getDisAllocatedLimit() {
		return disAllocatedLimit;
	}

	public void setDisAllocatedLimit(Double disAllocatedLimit) {
		this.disAllocatedLimit = disAllocatedLimit;
	}

	public Double getWintageAllocatedLimit() {
		return wintageAllocatedLimit;
	}

	public void setWintageAllocatedLimit(Double wintageAllocatedLimit) {
		this.wintageAllocatedLimit = wintageAllocatedLimit;
	}

	public Double getNacAllocatedLimit() {
		return nacAllocatedLimit;
	}

	public void setNacAllocatedLimit(Double nacAllocatedLimit) {
		this.nacAllocatedLimit = nacAllocatedLimit;
	}

	public Double getWintageAvlBalnc() {
		return wintageAvlBalnc;
	}

	public void setWintageAvlBalnc(Double wintageAvlBalnc) {
		this.wintageAvlBalnc = wintageAvlBalnc;
	}

	public Double getNacAvlBalnc() {
		return nacAvlBalnc;
	}

	public void setNacAvlBalnc(Double nacAvlBalnc) {
		this.nacAvlBalnc = nacAvlBalnc;
	}

	public Double getDisAvlBalnc() {
		return disAvlBalnc;
	}

	public void setDisAvlBalnc(Double disAvlBalnc) {
		this.disAvlBalnc = disAvlBalnc;
	}

	public Double getNoOfTimes() {
		return noOfTimes;
	}

	public void setNoOfTimes(Double noOfTimes) {
		this.noOfTimes = noOfTimes;
	}

	public Double getDiscretionaryUtilizedInsured() {
		return discretionaryUtilizedInsured;
	}

	public void setDiscretionaryUtilizedInsured(Double discretionaryUtilizedInsured) {
		this.discretionaryUtilizedInsured = discretionaryUtilizedInsured;
	}

	public String getWinBufferIndviSI() {
		return winBufferIndviSI;
	}

	public void setWinBufferIndviSI(String winBufferIndviSI) {
		this.winBufferIndviSI = winBufferIndviSI;
	}


	public String getWintageNoOfTimes() {
		return wintageNoOfTimes;
	}

	public void setWintageNoOfTimes(String wintageNoOfTimes) {
		this.wintageNoOfTimes = wintageNoOfTimes;
	}

	public Double getWintageUtilizedInsured() {
		return wintageUtilizedInsured;
	}

	public void setWintageUtilizedInsured(Double wintageUtilizedInsured) {
		this.wintageUtilizedInsured = wintageUtilizedInsured;
	}

	public String getNacbBufferIndviSI() {
		return nacbBufferIndviSI;
	}

	public void setNacbBufferIndviSI(String nacbBufferIndviSI) {
		this.nacbBufferIndviSI = nacbBufferIndviSI;
	}

	public String getNacbNoOfTimes() {
		return nacbNoOfTimes;
	}

	public void setNacbNoOfTimes(String nacbNoOfTimes) {
		this.nacbNoOfTimes = nacbNoOfTimes;
	}

	public Double getNacbUtilizedInsured() {
		return nacbUtilizedInsured;
	}

	public void setNacbUtilizedInsured(Double nacbUtilizedInsured) {
		this.nacbUtilizedInsured = nacbUtilizedInsured;
	}

	public String getMaxWinBufferInsuredLimit() {
		return maxWinBufferInsuredLimit;
	}

	public void setMaxWinBufferInsuredLimit(String maxWinBufferInsuredLimit) {
		this.maxWinBufferInsuredLimit = maxWinBufferInsuredLimit;
	}

	public String getMaxNacbBufferInsuredLimit() {
		return maxNacbBufferInsuredLimit;
	}

	public void setMaxNacbBufferInsuredLimit(String maxNacbBufferInsuredLimit) {
		this.maxNacbBufferInsuredLimit = maxNacbBufferInsuredLimit;
	}

	public Boolean getIsDisBufferApplicable() {
		return isDisBufferApplicable;
	}

	public void setIsDisBufferApplicable(Boolean isDisBufferApplicable) {
		this.isDisBufferApplicable = isDisBufferApplicable;
	}

	public Boolean getIsWintageBufferApplicable() {
		return isWintageBufferApplicable;
	}

	public void setIsWintageBufferApplicable(Boolean isWintageBufferApplicable) {
		this.isWintageBufferApplicable = isWintageBufferApplicable;
	}

	public Boolean getIsNacBufferApplicable() {
		return isNacBufferApplicable;
	}

	public void setIsNacBufferApplicable(Boolean isNacBufferApplicable) {
		this.isNacBufferApplicable = isNacBufferApplicable;
	}

	public void setWinBufferApplTo(SelectValue winBufferApplTo) {
		this.winBufferApplTo = winBufferApplTo;
	}

	public void setNacbBufferApplTo(SelectValue nacbBufferApplTo) {
		this.nacbBufferApplTo = nacbBufferApplTo;
	}

	public SelectValue getWinBufferApplTo() {
		return winBufferApplTo;
	}

	public SelectValue getNacbBufferApplTo() {
		return nacbBufferApplTo;
	}

	public Double getPolicyWise() {
		return policyWise;
	}

	public void setPolicyWise(Double policyWise) {
		this.policyWise = policyWise;
	}

	public Boolean getIsDisbufferClicked() {
		return isDisbufferClicked;
	}

	public void setIsDisbufferClicked(Boolean isDisbufferClicked) {
		this.isDisbufferClicked = isDisbufferClicked;
	}

	public Boolean getIsWinbufferClicked() {
		return isWinbufferClicked;
	}

	public void setIsWinbufferClicked(Boolean isWinbufferClicked) {
		this.isWinbufferClicked = isWinbufferClicked;
	}

	public Boolean getIsNacbufferClicked() {
		return isNacbufferClicked;
	}

	public void setIsNacbufferClicked(Boolean isNacbufferClicked) {
		this.isNacbufferClicked = isNacbufferClicked;
	}

}
