package com.shaic.claim.preauth.wizard.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.cmn.login.ImsUser;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class SearchPreauthTableDTO  extends AbstractTableDTO  implements Serializable {
	
	private String intimationNo;
	
	private String intimationSource;
	
	private String cpuName;
	
	private String productName;
	
	private String insuredPatientName;
	
	private String hospitalName;
	
	private String networkHospType;
	
	private String hospitalTypeName;
	
	private String preAuthReqAmt;
	
	private String treatmentType;
	
	private String speciality;
	private String enhancementReqAmt;
	
	private Double balanceSI;
	
	private Date docReceivedTimeForReg;
	
	private Date docReceivedTimeForMatch;
	
	private String strDocReceivedTimeForReg;
	
	private Date docReceivedTimeForRegDate;
	
	private String strDocReceivedTimeForMatch;
	
	private Date docReceivedTimeForMatchDate;
	
	private String type;
	
	private String transactionType;
	
	private Long hospitalTypeId;
	
	private String policyNo;
	
	//private Double sumInsured;
	private Integer sumInsured;
	
	private Long policyKey;
	
	private Long insuredId;
	
	private Long insuredKey;
	
	private Long claimKey;
	
	private Long intimationKey;
	
	private Double claimedAmountAsPerBill;
	
	private Boolean preauthCurrentQ = true;
	
	private Boolean isPreauthAutoAllocationQ = false;
	
	private Integer assignedValue = 0;
	private Integer pendingValue = 0;
	private Integer completedValue = 0;
	
	
	private Boolean CMA6 = false;
	private Boolean CMA5 = false;
	private Boolean CMA4 = false;
	private Boolean CMA3 = false;
	private Boolean CMA2 = false;
	private Boolean CMA1 = false; 
	
	private ImsUser imsUser;
	
	private Boolean isAutoAllocationCorpUser = false;
	private Boolean isAutoAllocationCPUUser = false;
	private String aboveCPULimitCorp;
	private String adviseStatus;
	
	
	private ClaimDto claimDto;
//	private HumanTask humanTask;
	
	//Added for treatment  type and speciality.
	
	private NewIntimationDto newIntimationDTO;
	
	
	private String processVBrequestedBy;
	
	private String processVBtype;
	
	private String processVBRemarksComplaince;
	
	private String processVBpayment;
	
	@NotNull(message = "Please Enter the remarks")
	private String processVBremarks;
	
	//@NotNull(message = "Please Select Approve or Disapprove")
	private String processVBStatus;
	
	private String crmFlagged;
	private Long productKey;
	
	private Long nhpUpdDocumentKey;
	
	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getEnhancementReqAmt() {
		return enhancementReqAmt;
	}

	public void setEnhancementReqAmt(String enhancementReqAmt) {
		this.enhancementReqAmt = enhancementReqAmt;
	}

	
	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
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

	public String getPreAuthReqAmt() {
		return preAuthReqAmt;
	}

	public void setPreAuthReqAmt(String preAuthReqAmt) {
		this.preAuthReqAmt = preAuthReqAmt;
	}

	public Double getBalanceSI() {
		return balanceSI;
	}

	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}

	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIntimationSource() {
		return intimationSource;
	}

	public void setIntimationSource(String intimationSource) {
		this.intimationSource = intimationSource;
	}

	public String getNetworkHospType() {
		return networkHospType;
	}

	public void setNetworkHospType(String networkHospType) {
		this.networkHospType = networkHospType;
	}
	
	public String getHospitalTypeName() {
		return hospitalTypeName;
	}

	public void setHospitalTypeName(String hospitalTypeName) {
		this.hospitalTypeName = hospitalTypeName;
	}

	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public Date getDocReceivedTimeForReg() {
		return docReceivedTimeForReg;
	}

	public void setDocReceivedTimeForReg(Date docReceivedTimeForReg) {
		this.docReceivedTimeForReg = docReceivedTimeForReg;
	}

	public Date getDocReceivedTimeForMatch() {
		return docReceivedTimeForMatch;
	}

	public void setDocReceivedTimeForMatch(Date docReceivedTimeForMatch) {
		this.docReceivedTimeForMatch = docReceivedTimeForMatch;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Integer getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Integer sumInsured) {
		this.sumInsured = sumInsured;
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

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
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

	public Date getDocReceivedTimeForRegDate() {
		return docReceivedTimeForRegDate;
	}

	public void setDocReceivedTimeForRegDate(Date docReceivedTimeForRegDate) {
		if(docReceivedTimeForRegDate !=  null){
			String dateformate =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.ss").format(docReceivedTimeForRegDate);
			//setDateOfAdmission(dateformate);
			setStrDocReceivedTimeForReg(dateformate);
			this.docReceivedTimeForRegDate = docReceivedTimeForRegDate;
			}
		
		
		
	}

	public Date getDocReceivedTimeForMatchDate() {
		return docReceivedTimeForMatchDate;
	}

	public void setDocReceivedTimeForMatchDate(Date docReceivedTimeForMatchDate) {
		
		if(docReceivedTimeForMatchDate !=  null){
			String dateformate =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.ss").format(docReceivedTimeForMatchDate);
			//setDateOfAdmission(dateformate);
			setStrDocReceivedTimeForMatch(dateformate);
			this.docReceivedTimeForMatchDate = docReceivedTimeForMatchDate;
			}
		
	}

	
	public Boolean getCMA4() {
		return CMA4;
	}

	public void setCMA4(Boolean cMA4) {
		CMA4 = cMA4;
	}

	public ImsUser getImsUser() {
		return imsUser;
	}

	public void setImsUser(ImsUser imsUser) {
		this.imsUser = imsUser;
	}

	public Boolean getCMA1() {
		return CMA1;
	}

	public void setCMA1(Boolean cMA1) {
		CMA1 = cMA1;
	}

	public Boolean getCMA2() {
		return CMA2;
	}

	public void setCMA2(Boolean cMA2) {
		CMA2 = cMA2;
	}

	public Boolean getCMA3() {
		return CMA3;
	}

	public void setCMA3(Boolean cMA3) {
		CMA3 = cMA3;
	}

	public Boolean getCMA6() {
		return CMA6;
	}

	public void setCMA6(Boolean cMA6) {
		CMA6 = cMA6;
	}

	public Boolean getCMA5() {
		return CMA5;
	}

	public void setCMA5(Boolean cMA5) {
		CMA5 = cMA5;
	}

	public Double getClaimedAmountAsPerBill() {
		return claimedAmountAsPerBill;
	}

	public void setClaimedAmountAsPerBill(Double claimedAmountAsPerBill) {
		this.claimedAmountAsPerBill = claimedAmountAsPerBill;
	}

	public Boolean getPreauthCurrentQ() {
		return preauthCurrentQ;
	}

	public void setPreauthCurrentQ(Boolean preauthCurrentQ) {
		this.preauthCurrentQ = preauthCurrentQ;
	}

	public Boolean getIsPreauthAutoAllocationQ() {
		return isPreauthAutoAllocationQ;
	}

	public void setIsPreauthAutoAllocationQ(Boolean isPreauthAutoAllocationQ) {
		this.isPreauthAutoAllocationQ = isPreauthAutoAllocationQ;
	}

	public Integer getAssignedValue() {
		return assignedValue;
	}

	public void setAssignedValue(Integer assignedValue) {
		this.assignedValue = assignedValue;
	}

	public Integer getPendingValue() {
		return pendingValue;
	}

	public void setPendingValue(Integer pendingValue) {
		this.pendingValue = pendingValue;
	}

	public Integer getCompletedValue() {
		return completedValue;
	}

	public void setCompletedValue(Integer completedValue) {
		this.completedValue = completedValue;
	}

	public Boolean getIsAutoAllocationCorpUser() {
		return isAutoAllocationCorpUser;
	}

	public void setIsAutoAllocationCorpUser(Boolean isAutoAllocationCorpUser) {
		this.isAutoAllocationCorpUser = isAutoAllocationCorpUser;
	}

	public Boolean getIsAutoAllocationCPUUser() {
		return isAutoAllocationCPUUser;
	}

	public void setIsAutoAllocationCPUUser(Boolean isAutoAllocationCPUUser) {
		this.isAutoAllocationCPUUser = isAutoAllocationCPUUser;
	}

	public String getAboveCPULimitCorp() {
		return aboveCPULimitCorp;
	}

	public void setAboveCPULimitCorp(String aboveCPULimitCorp) {
		this.aboveCPULimitCorp = aboveCPULimitCorp;
	}

	public String getAdviseStatus() {
		return adviseStatus;
	}

	public void setAdviseStatus(String adviseStatus) {
		this.adviseStatus = adviseStatus;
	}

	

	
	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	/*public Date getStrDocReceivedTimeForMatch() {
		return strDocReceivedTimeForMatch;
	}

	public void setStrDocReceivedTimeForMatch(Date strDocReceivedTimeForMatch) {
		this.strDocReceivedTimeForMatch = strDocReceivedTimeForMatch;
	}
*/
/*	public HumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/

	public String getProcessVBrequestedBy() {
		return processVBrequestedBy;
	}

	public void setProcessVBrequestedBy(String processVBrequestedBy) {
		this.processVBrequestedBy = processVBrequestedBy;
	}

	public String getProcessVBtype() {
		return processVBtype;
	}

	public void setProcessVBtype(String processVBtype) {
		this.processVBtype = processVBtype;
	}

	public String getProcessVBRemarksComplaince() {
		return processVBRemarksComplaince;
	}

	public void setProcessVBRemarksComplaince(String processVBRemarksComplaince) {
		this.processVBRemarksComplaince = processVBRemarksComplaince;
	}

	public String getProcessVBpayment() {
		return processVBpayment;
	}

	public void setProcessVBpayment(String processVBpayment) {
		this.processVBpayment = processVBpayment;
	}

	public String getProcessVBremarks() {
		return processVBremarks;
	}

	public void setProcessVBremarks(String processVBremarks) {
		this.processVBremarks = processVBremarks;
	}

	public String getProcessVBStatus() {
		return processVBStatus;
	}

	public void setProcessVBStatus(String processVBStatus) {
		this.processVBStatus = processVBStatus;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	public Long getProductKey() {
		return productKey;
	}

	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}

	public Long getNhpUpdDocumentKey() {
		return nhpUpdDocumentKey;
	}

	public void setNhpUpdDocumentKey(Long nhpUpdDocumentKey) {
		this.nhpUpdDocumentKey = nhpUpdDocumentKey;
	}

}
