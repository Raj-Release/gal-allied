/**
 * 
 */
package com.shaic.reimbursement.queryrejection.draftquery.search;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

/**
 * @author ntv.narenj
 *
 */
public class SearchDraftQueryLetterTableDTO extends AbstractTableDTO  implements Serializable{

	private Long claimKey;
	private Long intimationkey;
	private String insuredPatientName;
	private String cpuCode;
	private Long hospitalNameID;
	private String hospitalType;
	private Long cpuId;
	private String intimationNo;
	private String claimNo;
	private String policyNo;
	private String hospitalName;
	private String hospitalAddress;
	private String hospitalCity;
	private String reasonForAdmission;
	private String claimStatus;
	private Long queryKey;
	private Long rodKey;
	private ClaimDto claimDto;
	private ReimbursementQueryDto reimbursementQueryDto;
	private String redraftRemarks;
	private String queryRemarks;
	private String queryLetterRemarks;
	private List<ViewQueryDTO> queryDetailsList;
	private List<ViewQueryDTO> paymentQueryDetailsList;
	
	private String legalHeirFirstName;
	private String legalHeirMiddleName;
	private String legalHeirLastName;
	private String legalHeirAddress;
	
	private PreauthDTO preAuthDto = new PreauthDTO();
	
	private String strCpuCode;
	
	private String diagnosis;
	
	private boolean hasError;
	
	private String queryType;
	
	private boolean generateDisVoucher;
	
	private Boolean isPolicyValidate;
	
	private String crmFlagged;
	
	private String crcFlaggedReason;
    
    private String crcFlaggedRemark;
    
    private String IsPreferredHospital;
    
    private Double InsuredAge;
    
    private NewIntimationDto newIntimationDTO;
	

	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}
	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}
	public String getIsPreferredHospital() {
		return IsPreferredHospital;
	}
	public void setIsPreferredHospital(String isPreferredHospital) {
		IsPreferredHospital = isPreferredHospital;
	}
	public Double getInsuredAge() {
		return InsuredAge;
	}
	public void setInsuredAge(Double insuredAge) {
		InsuredAge = insuredAge;
	}
	public PreauthDTO getPreAuthDto() {
		return preAuthDto;
	}
	public void setPreAuthDto(PreauthDTO preAuthDto) {
		this.preAuthDto = preAuthDto;
	}
	public String getCrcFlaggedReason() {
		return crcFlaggedReason;
	}
	public void setCrcFlaggedReason(String crcFlaggedReason) {
		this.crcFlaggedReason = crcFlaggedReason;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
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
	
	
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalAddress() {
		return hospitalAddress;
	}
	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}
	public String getRedraftRemarks() {
		return redraftRemarks;
	}
	public void setRedraftRemarks(String redraftRemarks) {
		this.redraftRemarks = redraftRemarks;
	}
	public String getQueryRemarks() {
		return queryRemarks;
	}
	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}
	public String getQueryLetterRemarks() {
		return queryLetterRemarks;
	}
	public void setQueryLetterRemarks(String queryLetterRemarks) {
		this.queryLetterRemarks = queryLetterRemarks;
	}
	public List<ViewQueryDTO> getQueryDetailsList() {
		return queryDetailsList;
	}
	public void setQueryDetailsList(List<ViewQueryDTO> queryDetailsList) {
		this.queryDetailsList = queryDetailsList;
	}
	public ReimbursementQueryDto getReimbursementQueryDto() {
		return reimbursementQueryDto;
	}
	public void setReimbursementQueryDto(ReimbursementQueryDto reimbursementQueryDto) {
		this.reimbursementQueryDto = reimbursementQueryDto;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getIntimationkey() {
		return intimationkey;
	}
	public void setIntimationkey(Long intimationkey) {
		this.intimationkey = intimationkey;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = ""+cpuCode;
	}
	public Long getHospitalNameID() {
		return hospitalNameID;
	}
	public void setHospitalNameID(Long hospitalNameID) {
		this.hospitalNameID = hospitalNameID;
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	public Long getCpuId() {
		return cpuId;
	}
	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}
	/*public HumanTask getHumanTaskDTO() {
		return humanTaskDTO;
	}
	public void setHumanTaskDTO(HumanTask humanTaskDTO) {
		this.humanTaskDTO = humanTaskDTO;
	}*/
	public Long getQueryKey() {
		return queryKey;
	}
	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}	
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getStrCpuCode() {
		return strCpuCode;
	}
	public void setStrCpuCode(String strCpuCode) {
		this.strCpuCode = strCpuCode;
	}
	public boolean getHasError() {
		return hasError;
	}
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public boolean isGenerateDisVoucher() {
		return generateDisVoucher;
	}
	public void setGenerateDisVoucher(boolean generateDisVoucher) {
		this.generateDisVoucher = generateDisVoucher;
	}
	public List<ViewQueryDTO> getPaymentQueryDetailsList() {
		return paymentQueryDetailsList;
	}
	public void setPaymentQueryDetailsList(
			List<ViewQueryDTO> paymentQueryDetailsList) {
		this.paymentQueryDetailsList = paymentQueryDetailsList;
	}
	public Boolean getIsPolicyValidate() {
		return isPolicyValidate;
	}
	public void setIsPolicyValidate(Boolean isPolicyValidate) {
		this.isPolicyValidate = isPolicyValidate;
	}
	/**
	 * @return the crcFlaggedRemark
	 */
	public String getCrcFlaggedRemark() {
		return crcFlaggedRemark;
	}
	/**
	 * @param crcFlaggedRemark the crcFlaggedRemark to set
	 */
	public void setCrcFlaggedRemark(String crcFlaggedRemark) {
		this.crcFlaggedRemark = crcFlaggedRemark;
	}
	/**
	 * @return the crmFlagged
	 */
	public String getCrmFlagged() {
		return crmFlagged;
	}
	/**
	 * @param crmFlagged the crmFlagged to set
	 */
	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}
	public String getLegalHeirFirstName() {
		return legalHeirFirstName;
	}
	public void setLegalHeirFirstName(String legalHeirFirstName) {
		this.legalHeirFirstName = legalHeirFirstName;
	}
	public String getLegalHeirMiddleName() {
		return legalHeirMiddleName;
	}
	public void setLegalHeirMiddleName(String legalHeirMiddleName) {
		this.legalHeirMiddleName = legalHeirMiddleName;
	}
	public String getLegalHeirLastName() {
		return legalHeirLastName;
	}
	public void setLegalHeirLastName(String legalHeirLastName) {
		this.legalHeirLastName = legalHeirLastName;
	}
	public String getLegalHeirAddress() {
		return legalHeirAddress;
	}
	public void setLegalHeirAddress(String legalHeirAddress) {
		this.legalHeirAddress = legalHeirAddress;
	}		
	
}	