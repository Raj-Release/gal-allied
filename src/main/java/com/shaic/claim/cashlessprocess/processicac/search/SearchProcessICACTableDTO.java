package com.shaic.claim.cashlessprocess.processicac.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

/**
 * @author NEWUSER
 *
 */
public class SearchProcessICACTableDTO extends AbstractTableDTO implements Serializable {
	
    // search table value
	private String crmFlagged;
	
	private String intimationNo;
	
	private String intimationSource;
	
	private String cpuName;
	
	private String productName;
	
	private String insuredPatientName;
	
	private String hospitalName;
	
	private String networkHospType;
	
	private String preAuthReqAmt;
	
	private Double claimedAmountAsPerBill;
	
	private String treatmentType;
	
	private String speciality;
	
	private Double balanceSI;
	
	private String strDocReceivedTimeForReg;
	
	private String  strDocReceivedTimeForMatch;
	
	private String adviseStatus;
	
	private String  type;
	
	private Long claimKey;
	
	private Long transactionKey;
	
	private ClaimDto claimDto;
	
	private PreauthDTO preauthDto;
	
	private String policyNumber;

	private Long hospitalTypeId;

	private String hospitalTypeName;
	
	private Long policyKey;
	
	private Long insuredId;
	
	private Long insuredKey;
	
	private Long productKey;
	
	private Long intimationKey;

	private Date docReceivedTimeForMatch;
	
    private Long claimCount = 1l;
	
	// ICAC VALUES

	private Boolean finailDecValue;
	
	private String claimIcacChkflg;
	
	private List<ProcessingDoctorDetailsDTO> processingDoctorDetailsDTOs;

	private List<ProcessingICACTeamResponseDTO> processingICACTeamResponseDTO;
	

	private List<ProcessingICACTeamResponseDTO> processingICACFinalTeamResDTO;
	
	private String viewTransactoinType;
	
	private String directIcacRemarks;
	
	private String responseIcacRemark;
	
	private String finalResponseIcacRemark;

	private String finalResDecFlag;
	
	private String directIcacReq;

	private Long icacKey;
	
	
	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getIntimationSource() {
		return intimationSource;
	}

	public void setIntimationSource(String intimationSource) {
		this.intimationSource = intimationSource;
	}

	public String getCpuName() {
		return cpuName;
	}

	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getNetworkHospType() {
		return networkHospType;
	}

	public void setNetworkHospType(String networkHospType) {
		this.networkHospType = networkHospType;
	}

	public String getPreAuthReqAmt() {
		return preAuthReqAmt;
	}

	public void setPreAuthReqAmt(String preAuthReqAmt) {
		this.preAuthReqAmt = preAuthReqAmt;
	}

	public Double getClaimedAmountAsPerBill() {
		return claimedAmountAsPerBill;
	}

	public void setClaimedAmountAsPerBill(Double claimedAmountAsPerBill) {
		this.claimedAmountAsPerBill = claimedAmountAsPerBill;
	}

	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public Double getBalanceSI() {
		return balanceSI;
	}

	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}

	public String getStrDocReceivedTimeForReg() {
		return strDocReceivedTimeForReg;
	}

	public void setStrDocReceivedTimeForReg(String strDocReceivedTimeForReg) {
		this.strDocReceivedTimeForReg = strDocReceivedTimeForReg;
	}

	public String getStrDocReceivedTimeForMatch() {
		return strDocReceivedTimeForMatch;
	}

	public void setStrDocReceivedTimeForMatch(String strDocReceivedTimeForMatch) {
		this.strDocReceivedTimeForMatch = strDocReceivedTimeForMatch;
	}

	public String getAdviseStatus() {
		return adviseStatus;
	}

	public void setAdviseStatus(String adviseStatus) {
		this.adviseStatus = adviseStatus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public String getHospitalTypeName() {
		return hospitalTypeName;
	}

	public void setHospitalTypeName(String hospitalTypeName) {
		this.hospitalTypeName = hospitalTypeName;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Long getInsuredId() {
		return insuredId;
	}

	public void setInsuredId(Long insuredId) {
		this.insuredId = insuredId;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public Long getProductKey() {
		return productKey;
	}

	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Boolean getFinailDecValue() {
		return finailDecValue;
	}

	public void setFinailDecValue(Boolean finailDecValue) {
		this.finailDecValue = finailDecValue;
	}

	public List<ProcessingDoctorDetailsDTO> getProcessingDoctorDetailsDTOs() {
		return processingDoctorDetailsDTOs;
	}

	public void setProcessingDoctorDetailsDTOs(
			List<ProcessingDoctorDetailsDTO> processingDoctorDetailsDTOs) {
		this.processingDoctorDetailsDTOs = processingDoctorDetailsDTOs;
	}

	public List<ProcessingICACTeamResponseDTO> getProcessingICACTeamResponseDTO() {
		return processingICACTeamResponseDTO;
	}

	public void setProcessingICACTeamResponseDTO(
			List<ProcessingICACTeamResponseDTO> processingICACTeamResponseDTO) {
		this.processingICACTeamResponseDTO = processingICACTeamResponseDTO;
	}

	public String getClaimIcacChkflg() {
		return claimIcacChkflg;
	}

	public void setClaimIcacChkflg(String claimIcacChkflg) {
		this.claimIcacChkflg = claimIcacChkflg;
	}

	public String getViewTransactoinType() {
		return viewTransactoinType;
	}

	public void setViewTransactoinType(String viewTransactoinType) {
		this.viewTransactoinType = viewTransactoinType;
	}

	public String getDirectIcacRemarks() {
		return directIcacRemarks;
	}

	public void setDirectIcacRemarks(String directIcacRemarks) {
		this.directIcacRemarks = directIcacRemarks;
	}

	public String getResponseIcacRemark() {
		return responseIcacRemark;
	}

	public void setResponseIcacRemark(String responseIcacRemark) {
		this.responseIcacRemark = responseIcacRemark;
	}

	public String getFinalResponseIcacRemark() {
		return finalResponseIcacRemark;
	}

	public void setFinalResponseIcacRemark(String finalResponseIcacRemark) {
		this.finalResponseIcacRemark = finalResponseIcacRemark;
	}

	public String getFinalResDecFlag() {
		return finalResDecFlag;
	}

	public void setFinalResDecFlag(String finalResDecFlag) {
		this.finalResDecFlag = finalResDecFlag;
	}

	public String getDirectIcacReq() {
		return directIcacReq;
	}

	public void setDirectIcacReq(String directIcacReq) {
		this.directIcacReq = directIcacReq;
	}

	public Long getIcacKey() {
		return icacKey;
	}

	public void setIcacKey(Long icacKey) {
		this.icacKey = icacKey;
	}

	public List<ProcessingICACTeamResponseDTO> getProcessingICACFinalTeamResDTO() {
		return processingICACFinalTeamResDTO;
	}

	public void setProcessingICACFinalTeamResDTO(
			List<ProcessingICACTeamResponseDTO> processingICACFinalTeamResDTO) {
		this.processingICACFinalTeamResDTO = processingICACFinalTeamResDTO;
	}

	public Date getDocReceivedTimeForMatch() {
		return docReceivedTimeForMatch;
	}

	public void setDocReceivedTimeForMatch(Date docReceivedTimeForMatch) {
		this.docReceivedTimeForMatch = docReceivedTimeForMatch;
	}

	public void setDocReceivedTimeForMatchDate(Date docReceivedTimeForMatchDate) {

		if(docReceivedTimeForMatchDate !=  null){
			String dateformate =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.ss").format(docReceivedTimeForMatchDate);
			setStrDocReceivedTimeForMatch(dateformate);
		}

	}
	
	public void setDocReceivedTimeForRegDate(Date docReceivedTimeForRegDate) {
		if(docReceivedTimeForRegDate !=  null){
			String dateformate =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.ss").format(docReceivedTimeForRegDate);
			setStrDocReceivedTimeForReg(dateformate);
		}
	}

	public Long getClaimCount() {
		return claimCount;
	}

	public void setClaimCount(Long claimCount) {
		this.claimCount = claimCount;
	}

	public PreauthDTO getPreauthDto() {
		return preauthDto;
	}

	public void setPreauthDto(PreauthDTO preauthDto) {
		this.preauthDto = preauthDto;
	}

	
}
