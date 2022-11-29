/**
 * 
 */
package com.shaic.reimbursement.investigation.assigninvestigation.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

/**
 * @author ntv.narenj
 *
 */
public class SearchAssignInvestigationTableDTO extends AbstractTableDTO  implements Serializable{

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
	private Long investigationKey;
	private Long investigationAssignedKey;
	private String investigatorName;
	private String strCpuCode;
	private NewIntimationDto newIntimationDto;
	
	private Long rodKey;
	
	private String investigationRole;
	
	private RRCDTO rrcDTO;
	
	private Boolean isCashlessTask;
	
	private PreauthDTO preauthDTO;
	
	private String crmFlagged;
	
	private String crcFlaggedReason;
	
	private String crcFlaggedRemark;
	
	private Long intimationKey;
	
	//CR2019058 New fields for Change in get task
	private Date fromDate;
	
	private Date endDate;
	
	private Long investigationState;
	
	private String invInitiatedDate;

	private String invApprovedDate;

	private String invDraftedDate;

	
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
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
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
/*	public HumanTask getHumanTaskDTO() {
		return humanTaskDTO;
	}
	public void setHumanTaskDTO(HumanTask humanTaskDTO) {
		this.humanTaskDTO = humanTaskDTO;
	}*/
	public Long getInvestigationKey() {
		return investigationKey;
	}
	public void setInvestigationKey(Long investigationKey) {
		this.investigationKey = investigationKey;
	}
	public String getInvestigationRole() {
		return investigationRole;
	}
	public void setInvestigationRole(String investigationRole) {
		this.investigationRole = investigationRole;
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
	public Boolean getIsCashlessTask() {
		return isCashlessTask;
	}
	public void setIsCashlessTask(Boolean isCashlessTask) {
		this.isCashlessTask = isCashlessTask;
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
	public String getInvestigatorName() {
		return investigatorName;
	}
	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}
	public Long getInvestigationAssignedKey() {
		return investigationAssignedKey;
	}
	public void setInvestigationAssignedKey(Long investigationAssignedKey) {
		this.investigationAssignedKey = investigationAssignedKey;
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
	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}
	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
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
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getInvestigationState() {
		return investigationState;
	}
	public void setInvestigationState(Long investigationState) {
		this.investigationState = investigationState;
	}
	public String getInvInitiatedDate() {
		return invInitiatedDate;
	}
	public void setInvInitiatedDate(String invInitiatedDate) {
		this.invInitiatedDate = invInitiatedDate;
	}
	public String getInvApprovedDate() {
		return invApprovedDate;
	}
	public void setInvApprovedDate(String invApprovedDate) {
		this.invApprovedDate = invApprovedDate;
	}
	public String getInvDraftedDate() {
		return invDraftedDate;
	}
	public void setInvDraftedDate(String invDraftedDate) {
		this.invDraftedDate = invDraftedDate;
	}
	
	
}
