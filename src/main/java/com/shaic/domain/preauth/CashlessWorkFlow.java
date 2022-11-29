package com.shaic.domain.preauth;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "IMS_CLS_SEC_WORK_FLOW")
@NamedQueries({
	@NamedQuery(name = "CashlessWorkFlow.findAll", query = "SELECT i FROM CashlessWorkFlow i"),
	@NamedQuery(name = "CashlessWorkFlow.findByKey", query = "SELECT i FROM CashlessWorkFlow i where i.key = :workFlowKey"),
	@NamedQuery(name = "CashlessWorkFlow.findPendingPAReminderLetter", query = "SELECT i FROM CashlessWorkFlow i where i.currentQ like :curQ and  Lower(i.lob) like :lobStr and (i.reminderLetterFlag is null or i.reminderLetterFlag <> 'Y') and i.claimType like :clmType and i.cpuCode = :cpuCode and Upper(i.remCategory) like :remCateg and i.activeFlag = 'Y'"),
	@NamedQuery(name = "CashlessWorkFlow.findPendingReminderLetter", query = "SELECT i FROM CashlessWorkFlow i where Lower(i.lob) like :lobStr and i.currentQ like :curQ and (i.reminderLetterFlag is null or i.reminderLetterFlag <> 'Y') and i.claimType like :clmType and i.cpuCode = :cpuCode and Lower(i.remCategory) like :remCateg and i.activeFlag = 'Y' order by i.hospitalKey asc"),
	@NamedQuery(name = "CashlessWorkFlow.findPendingPANReminderLetter", query = "SELECT i FROM CashlessWorkFlow i where i.currentQ like :curQ and (i.reminderLetterFlag is null or i.reminderLetterFlag = 'N') and Lower(i.claimType) like :clmType and Lower(i.remCategory) like :remCateg and i.cpuCode = :cpuCode and i.activeFlag = 'Y'"),
	@NamedQuery(name = "CashlessWorkFlow.findPreauthApproved", query = "SELECT i FROM CashlessWorkFlow i where i.outCome = :outcome and i.previousQ = :previous and i.currentQ = :current and i.activeFlag = 'Y' and i.remarks is null"),
	@NamedQuery(name = "CashlessWorkFlow.findWithdrawApproved", query = "SELECT i FROM CashlessWorkFlow i where i.outCome in(:outcome) and i.previousQ in(:previous) and i.currentQ in(:current) and i.activeFlag = 'Y' and i.remarks is null"),
	@NamedQuery(name = "CashlessWorkFlow.findIntimationReallocate", query = "SELECT i FROM CashlessWorkFlow i where i.intimation_no = :intimationNo and i.currentQ in(:current) and i.activeFlag = 'Y' "),
	@NamedQuery(name = "CashlessWorkFlow.findByInvsKey", query = "SELECT i FROM CashlessWorkFlow i where i.investigationKey = :investigationKey"),
})

public class CashlessWorkFlow extends AbstractEntity {
	
	@Id
	@Column(name="WK_KEY", updatable=false)
	private Long key;
	
	@Column(name = "INTIMATION_NO")
	private String intimation_no;
	
	@Column(name = "INTIMATION_KEY")
	private Long intimation_key;
	
	@Column(name = "INT_SOURCE")
	private String intimationSource;
	
	@Column(name = "CPU_CODE")
	private Long cpuCode;
	
	@Column(name = "CLAIM_NUMBER")
	private String claimNumber;
	
	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "CLAIM_TYPE")
	private String claimType;
	
	@Column(name = "BAL_SI_FLAG")
	private String balanceSIflag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ADMISSION_DATE")
	private Date admissionDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INTIMATION_DATE")
	private Date intimationDate;
	
	@Column(name = "POLICY_KEY")
	private Long policyKey;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "PRODUCT_KEY")
	private Long productKey;
	
	@Column(name = "PRODUCT_NAME")
	private String productName;
	
	@Column(name = "LOB")
	private String lob;
	
	
	@Column(name = "LOB_TYPE")
	private String lobType;
	
	@Column(name = "HOSPITAL_TYPE")
	private String hospitalType;
	
	@Column(name = "NETWORK_TYPE")
	private String networkType;
	
	@Column(name = "HOSPITAL_KEY")
	private Long hospitalKey;
	
	@Column(name = "TREATEMENT_TYPE")
	private String treatmentType;
	
	@Column(name = "SPECIALITY_NAME")
	private String specialityName;
	
	@Column(name = "PRIORITY")
	private String priority;
	
	@Column(name = "RECORD_TYPE")
	private String recordType;
	
	@Column(name = "STAGE_SOURCE")
	private String stageSource;
	
	@Column(name = "CASHLESS_NO")
	private String cashlessNumber;
	
	@Column(name = "CASHLESS_KEY")
	private Long cashlessKey;
	
	@Column(name = "CLAIMED_AMOUNT")
	private Integer claimedAmount;
	
	@Column(name = "ROLE_ID")
	private String roleId;
	
	@Column(name = "PREVIOUS_Q")
	private String previousQ;
	
	@Column(name = "CURRENT_Q")
	private String currentQ;
	
	@Column(name = "OUTCOME")
	private String outCome;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "ACTIVE_FLAG")
	private String activeFlag;
	
	@Column(name = "REMINDER_LATTER_FLAG")
	private String reminderLetterFlag;	
	
	@Column(name = "QUERY_KEY")
	private Long queryKey;
	
	@Column(name = "REMINDER_CATEGORY")
	private String remCategory;
	
	@Column(name = "REMINDER_TYPE")
	private String remType;
	
	@Column(name = "ALLOCATED_USER")
	private String allocateUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ALLOCATED_DATE")
	private Date allocateDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESSED_DATE")
	private Date processedDate;
	
	@Column(name = "INVESTIGATION_KEY")
	private Long investigationKey;
	
	@Column(name = "FVR_KEY")
	private Long fvrKey;
	
	@Column(name = "REIMB_REQ_BY")
	private String reimbReqBy;
	
	@Column(name = "PED_REQUESTOR_ROLE")
	private String pedRequesterRole;
	
	public String getIntimation_no() {
		return intimation_no;
	}

	public void setIntimation_no(String intimation_no) {
		this.intimation_no = intimation_no;
	}

	public Long getIntimation_key() {
		return intimation_key;
	}

	public void setIntimation_key(Long intimation_key) {
		this.intimation_key = intimation_key;
	}

	public String getIntimationSource() {
		return intimationSource;
	}

	public void setIntimationSource(String intimationSource) {
		this.intimationSource = intimationSource;
	}

	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getBalanceSIflag() {
		return balanceSIflag;
	}

	public void setBalanceSIflag(String balanceSIflag) {
		this.balanceSIflag = balanceSIflag;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Date getIntimationDate() {
		return intimationDate;
	}

	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Long getProductKey() {
		return productKey;
	}

	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getLobType() {
		return lobType;
	}

	public void setLobType(String lobType) {
		this.lobType = lobType;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public Long getHospitalKey() {
		return hospitalKey;
	}

	public void setHospitalKey(Long hospitalKey) {
		this.hospitalKey = hospitalKey;
	}

	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getStageSource() {
		return stageSource;
	}

	public void setStageSource(String stageSource) {
		this.stageSource = stageSource;
	}

	public String getCashlessNumber() {
		return cashlessNumber;
	}

	public void setCashlessNumber(String cashlessNumber) {
		this.cashlessNumber = cashlessNumber;
	}

	public Long getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public Integer getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Integer claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPreviousQ() {
		return previousQ;
	}

	public void setPreviousQ(String previousQ) {
		this.previousQ = previousQ;
	}

	public String getCurrentQ() {
		return currentQ;
	}

	public void setCurrentQ(String currentQ) {
		this.currentQ = currentQ;
	}

	public String getOutCome() {
		return outCome;
	}

	public void setOutCome(String outCome) {
		this.outCome = outCome;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getReminderLetterFlag() {
		return reminderLetterFlag;
	}

	public void setReminderLetterFlag(String reminderLetterFlag) {
		this.reminderLetterFlag = reminderLetterFlag;
	}

	public Long getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}

	public String getRemCategory() {
		return remCategory;
	}

	public void setRemCategory(String remCategory) {
		this.remCategory = remCategory;
	}

	public String getRemType() {
		return remType;
	}

	public void setRemType(String remType) {
		this.remType = remType;
	}	
	public String getAllocateUser() {
		return allocateUser;
	}

	public void setAllocateUser(String allocateUser) {
		this.allocateUser = allocateUser;
	}

	public Date getAllocateDate() {
		return allocateDate;
	}

	public void setAllocateDate(Date allocateDate) {
		this.allocateDate = allocateDate;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public Long getInvestigationKey() {
		return investigationKey;
	}

	public void setInvestigationKey(Long investigationKey) {
		this.investigationKey = investigationKey;
	}

	public Long getFvrKey() {
		return fvrKey;
	}

	public void setFvrKey(Long fvrKey) {
		this.fvrKey = fvrKey;
	}

	public String getReimbReqBy() {
		return reimbReqBy;
	}

	public void setReimbReqBy(String reimbReqBy) {
		this.reimbReqBy = reimbReqBy;
	}
	
	public String getPedRequesterRole() {
		return pedRequesterRole;
	}

	public void setPedRequesterRole(String pedRequesterRole) {
		this.pedRequesterRole = pedRequesterRole;
	}
}
