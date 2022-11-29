package com.shaic.reimbursement.processi_investigationi_initiated.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessInvestigationInitiatedTableDTO extends AbstractTableDTO {
	
	/**
	 * 
	 */
	private String intimationNo;
	private String policyNo;
	private String insuredPatientName;
	private String hospitalName;
	private String hospitalCity;
	private Long lOBId;
	private String lOB;
	private Long cpuCode;
	private String productName;
	private String reasonForAdmission;
	private Long cpuId;
	private Long hospitalNameID;
	private String investigationRequestedRole;
	private Long investigationKey;
	private String requestedBy;
	private String strCpuCode;
	private String claimType;
	private String dtofInvInitiated;
	private List<SelectValue> invAllocationToList;
	private Boolean isInvestigation;
	private Long claimTypeId;
	
	private Long rodKey;
	
	private RRCDTO rrcDTO;	
	
	private String hospitalType;
	private String networkHospType;
	private String crmFlagged;
	private NewIntimationDto newIntimationDto;
	private PreauthDTO preauthDTO;
	private String crcFlaggedReason;
	private String crcFlaggedRemark;
	
	private Boolean isAllowInitiateFVR;
	
	 private Long claimCount = 1l;
	 
	 private Boolean isPEDInitiated = false;
	 
	 private Map<String, String> suspiciousPopupMap = new HashMap<String, String>();
	 
	  private Boolean isPopupMessageOpened = false;
	  
	  private String isSuspicious;
	
	  private String clmPrcsInstruction;
	  
	  private Map<String, String> nonPreferredPopupMap = new HashMap<String, String>();
	  
	  private Boolean is64VBChequeStatusAlert = false; 
	  
	  private Map<String, String> popupMap = new HashMap<String, String>();
	 
	  
	public Map<String, String> getPopupMap() {
		return popupMap;
	}
	public void setPopupMap(Map<String, String> popupMap) {
		this.popupMap = popupMap;
	}
	public Boolean getIs64VBChequeStatusAlert() {
		return is64VBChequeStatusAlert;
	}
	public void setIs64VBChequeStatusAlert(Boolean is64vbChequeStatusAlert) {
		is64VBChequeStatusAlert = is64vbChequeStatusAlert;
	}
	private Boolean isGeoSame = Boolean.TRUE;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
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
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}
	public Long getlOBId() {
		return lOBId;
	}
	public void setlOBId(Long lOBId) {
		this.lOBId = lOBId;
	}
	public String getlOB() {
		return lOB;
	}
	public void setlOB(String lOB) {
		this.lOB = lOB;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}
	public Long getCpuId() {
		return cpuId;
	}
	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}
	public Long getHospitalNameID() {
		return hospitalNameID;
	}
	public void setHospitalNameID(Long hospitalNameID) {
		this.hospitalNameID = hospitalNameID;
	}
	public String getInvestigationRequestedRole() {
		return investigationRequestedRole;
	}
	public String getRequestedBy() {
		return requestedBy;
	}
	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}
	public void setInvestigationRequestedRole(String investigationRequestedRole) {
		this.investigationRequestedRole = investigationRequestedRole;
	}
	
	
	public Long getInvestigationKey() {
		return investigationKey;
	}
	public void setInvestigationKey(Long investigationKey) {
		this.investigationKey = investigationKey;
	}
	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}
	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getStrCpuCode() {
		return strCpuCode;
	}
	public void setStrCpuCode(String strCpuCode) {
		this.strCpuCode = strCpuCode;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getDtofInvInitiated() {
		return dtofInvInitiated;
	}
	public void setDtofInvInitiated(String dtofInvInitiated) {
		this.dtofInvInitiated = dtofInvInitiated;
	}
	public List<SelectValue> getInvAllocationToList() {
		return invAllocationToList;
	}
	public void setInvAllocationToList(List<SelectValue> invAllocationToList) {
		this.invAllocationToList = invAllocationToList;
	}
	public Boolean getIsInvestigation() {
		return isInvestigation;
	}
	public void setIsInvestigation(Boolean isInvestigation) {
		this.isInvestigation = isInvestigation;
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	public String getNetworkHospType() {
		return networkHospType;
	}
	public void setNetworkHospType(String networkHospType) {
		this.networkHospType = networkHospType;
	}
	public Boolean getIsAllowInitiateFVR() {
		return isAllowInitiateFVR;
	}
	public void setIsAllowInitiateFVR(Boolean isAllowInitiateFVR) {
		this.isAllowInitiateFVR = isAllowInitiateFVR;
	}
	public String getCrmFlagged() {
		return crmFlagged;
	}
	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}
	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}
	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}
	public String getCrcFlaggedReason() {
		return crcFlaggedReason;
	}
	public void setCrcFlaggedReason(String crcFlaggedReason) {
		this.crcFlaggedReason = crcFlaggedReason;
	}
	public String getCrcFlaggedRemark() {
		return crcFlaggedRemark;
	}
	public void setCrcFlaggedRemark(String crcFlaggedRemark) {
		this.crcFlaggedRemark = crcFlaggedRemark;
	}
	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}
	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
	}
	public Long getClaimCount() {
		return claimCount;
	}
	public void setClaimCount(Long claimCount) {
		this.claimCount = claimCount;
	}
	public Boolean getIsPEDInitiated() {
		return isPEDInitiated;
	}
	public void setIsPEDInitiated(Boolean isPEDInitiated) {
		this.isPEDInitiated = isPEDInitiated;
	}
	public Map<String, String> getSuspiciousPopupMap() {
		return suspiciousPopupMap;
	}
	public void setSuspiciousPopupMap(Map<String, String> suspiciousPopupMap) {
		this.suspiciousPopupMap = suspiciousPopupMap;
	}
	public Boolean getIsPopupMessageOpened() {
		return isPopupMessageOpened;
	}
	public void setIsPopupMessageOpened(Boolean isPopupMessageOpened) {
		this.isPopupMessageOpened = isPopupMessageOpened;
	}
	public String getIsSuspicious() {
		return isSuspicious;
	}
	public void setIsSuspicious(String isSuspicious) {
		this.isSuspicious = isSuspicious;
	}
	public String getClmPrcsInstruction() {
		return clmPrcsInstruction;
	}
	public void setClmPrcsInstruction(String clmPrcsInstruction) {
		this.clmPrcsInstruction = clmPrcsInstruction;
	}
	public Map<String, String> getNonPreferredPopupMap() {
		return nonPreferredPopupMap;
	}
	public void setNonPreferredPopupMap(Map<String, String> nonPreferredPopupMap) {
		this.nonPreferredPopupMap = nonPreferredPopupMap;
	}
	public Boolean getIsGeoSame() {
		return isGeoSame;
	}
	public void setIsGeoSame(Boolean isGeoSame) {
		this.isGeoSame = isGeoSame;
	}
	public Long getClaimTypeId() {
		return claimTypeId;
	}
	public void setClaimTypeId(Long claimTypeId) {
		this.claimTypeId = claimTypeId;
	}
	
	
	
}
