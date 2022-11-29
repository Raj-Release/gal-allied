/**
 * 
 */
package com.shaic.reimbursement.investigation.draftinvestigation.search;

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
public class SearchDraftInvestigationTableDTO extends AbstractTableDTO  implements Serializable{

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
	private String strCpuCode;
	private String reimbReqBy;
	private String crmFlagged;

	private Long rodKey;
	
	private String investigationRole;
	
	private RRCDTO rrcDTO;
	
	private Boolean isCashlessTask;
	
	private String investigationApprovedDate;
	
	
	
	
	
	
	public String getInvestigationApprovedDate() {
		return investigationApprovedDate;
	}
	public void setInvestigationApprovedDate(String investigationApprovedDate) {
		this.investigationApprovedDate = investigationApprovedDate;
	}
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
	public String getReimbReqBy() {
		return reimbReqBy;
	}
	public void setReimbReqBy(String reimbReqBy) {
		this.reimbReqBy = reimbReqBy;
	}
	
	public String getCrmFlagged() {
		return crmFlagged;
	}
	
	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}
	
}
