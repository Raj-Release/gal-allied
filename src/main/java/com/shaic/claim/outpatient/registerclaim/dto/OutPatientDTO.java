package com.shaic.claim.outpatient.registerclaim.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import com.shaic.claim.ClaimDto;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpTableDTO;
import com.shaic.claim.outpatient.processOPpages.BenefitsAvailedDTO;
import com.shaic.claim.outpatient.processOPpages.OPSpecialityDTO;
import com.shaic.claim.outpatient.registerclaim.wizard.DiagnosisDetailsOPTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredDto;
import com.shaic.domain.Policy;
import com.shaic.domain.State;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.oppayment.dto.OPPaymentDTO;

public class OutPatientDTO implements Serializable{
	
	private static final long serialVersionUID = 4774275215300895714L;

	private Long key;
	private Long statusKey;
	private Long stageKey;
	private String claimId;
	private String intimationId;
	private Long intimationKey;
	private PolicyDto policyDto;
	private InsuredDto insuredDto;
	private  NewIntimationDto newIntimationDTO;
	private ClaimDto claimDTO;
	private OPPaymentDTO opPaymentDTO;	
	private Policy policy;
	private Long policyKey;
	private Boolean outpatientFlag = false;
	private Boolean healthCheckupFlag = false;
	//Set for user name and password issue.
	private String strUserName;
	private String pioName;
	private Long claimKey;
			
	private String strPassword;
	
	private String createdBy;
	
	private Date createDate;
		
	//private HumanTask humanTask;
	
	private OPDocumentDetailsDTO documentDetails;
	
	private OPRODAndBillEntryDTO opBillEntryDetails;
	
	private HospitalDto selectedHospital; 
	
	private List<DiagnosisDetailsOPTableDTO> diagnosisListenerTableList;
	
	private List<DiagnosisDetailsOPTableDTO> deletedDiagnosisListenerTableList;
	
	private List<PreviousAccountDetailsDTO> previousAccntDetailsList;
	
	// Billing Page
	
	private List<UploadDocumentDTO> uploadedDocsTableList;
	private List<UploadDocumentDTO> uploadedDeletedDocsTableList;
	private List<com.shaic.claim.outpatient.processOPpages.OPBillDetailsDTO> opBillDetailsList;
	
	//Settlement Page
	
	private String rejectRemarks; 
	private Double approvedAmt;
	private String approvalRemarks;
	
	private String chqModeChngReason;
	private String chqEmailId;
	private String chqPanno;
	private String chqPayableAt;
	private Insured chqPayeeName;
	private String chqNameChngReason;
	private String chqHeirName;
	
	private String bnkmodeChngReason;
	private String bnkEmailId;
	private String bnkPanno;
	private String bnkAccNo;
	private String bnkIfsc;
	private String bnkName;
	private String bnkCity;
	private Insured bnkPayeeName;
	private String bnkNameChngReason;
	private String bnkHeirName;
	private String bnkBranch;
	
	private Boolean isChqPayment;
	
	private boolean isApprove = false;
	private boolean isReject = false;
	private Integer amountEligible;
	
	private Integer reportOPAvailableSI;
	private Integer reportOPAmountClaimed;
	private Integer reportConsolidatedBillAmount;
	private Integer reportConsolidatedDedAmount;
	
	private BenefitsAvailedDTO benefitsAvailedDto;
	
	private State state;
	private CityTownVillage city;
	
	private Boolean consulation = true;
	private Boolean diagnosis = false;
	private Boolean physiotherapy = false;
	private Boolean medicine = false;
	
	private List<OPSpecialityDTO> specialityDTOList;
	
	private String availableSI;
	private String eligible;
	private Double payble;
	
	private CreateBatchOpTableDTO viewPaymentDto;
	
	private WeakHashMap filePathAndTypeMap;
	
	private Boolean availableSiFlag = false;
	private String policyPed;
	
	private Double perClaimLimit;
	private Double perPolicyLimit;
	private Double availablevaccinationlimit;
	
	
	public CityTownVillage getCity() {
		return city;
	}

	public void setCity(CityTownVillage city) {
		this.city = city;
	}

	public OutPatientDTO() {
		this.documentDetails = new OPDocumentDetailsDTO();
		
		   
		this.opBillEntryDetails = new OPRODAndBillEntryDTO();
		
		this.benefitsAvailedDto = new BenefitsAvailedDTO();
		
		this.viewPaymentDto = new CreateBatchOpTableDTO();
		
		
		
	}

	public OPDocumentDetailsDTO getDocumentDetails() {
		return documentDetails;
	}

	public void setDocumentDetails(OPDocumentDetailsDTO documentDetails) {
		this.documentDetails = documentDetails;
	}

	public OPRODAndBillEntryDTO getOpBillEntryDetails() {
		return opBillEntryDetails;
	}

	public void setOpBillEntryDetails(OPRODAndBillEntryDTO opBillEntryDetails) {
		this.opBillEntryDetails = opBillEntryDetails;
	}

	public PolicyDto getPolicyDto() {
		return policyDto;
	}

	public void setPolicyDto(PolicyDto policyDto) {
		this.policyDto = policyDto;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Boolean getOutpatientFlag() {
		return outpatientFlag;
	}

	public void setOutpatientFlag(Boolean outpatientFlag) {
		this.outpatientFlag = outpatientFlag;
	}

	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	public ClaimDto getClaimDTO() {
		return claimDTO;
	}

	public String getPioName() {
		return pioName;
	}

	public void setPioName(String pioName) {
		this.pioName = pioName;
	}

	public void setClaimDTO(ClaimDto claimDTO) {
		this.claimDTO = claimDTO;
	}

	public Boolean getHealthCheckupFlag() {
		return healthCheckupFlag;
	}

	public void setHealthCheckupFlag(Boolean healthCheckupFlag) {
		this.healthCheckupFlag = healthCheckupFlag;
	}

	public String getStrUserName() {
		return strUserName;
	}

	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	public String getStrPassword() {
		return strPassword;
	}

	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	/*public HumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/

	public String getIntimationId() {
		return intimationId;
	}

	public void setIntimationId(String intimationId) {
		this.intimationId = intimationId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public OPPaymentDTO getOpPaymentDTO() {
		return opPaymentDTO;
	}

	public void setOpPaymentDTO(OPPaymentDTO opPaymentDTO) {
		this.opPaymentDTO = opPaymentDTO;
	}

	public HospitalDto getSelectedHospital() {
		return selectedHospital;
	}

	public void setSelectedHospital(HospitalDto selectedHospital) {
		this.selectedHospital = selectedHospital;
	}

	public List<DiagnosisDetailsOPTableDTO> getDiagnosisListenerTableList() {
		return diagnosisListenerTableList;
	}

	public void setDiagnosisListenerTableList(List<DiagnosisDetailsOPTableDTO> diagnosisListenerTableList) {
		this.diagnosisListenerTableList = diagnosisListenerTableList;
	}

	public List<DiagnosisDetailsOPTableDTO> getDeletedDiagnosisListenerTableList() {
		return deletedDiagnosisListenerTableList;
	}

	public void setDeletedDiagnosisListenerTableList(List<DiagnosisDetailsOPTableDTO> deletedDiagnosisListenerTableList) {
		this.deletedDiagnosisListenerTableList = deletedDiagnosisListenerTableList;
	}
	
	public List<PreviousAccountDetailsDTO> getPreviousAccntDetailsList() {
		return previousAccntDetailsList;
	}

	public void setPreviousAccntDetailsList(List<PreviousAccountDetailsDTO> previousAccntDetailsList) {
		this.previousAccntDetailsList = previousAccntDetailsList;
	}

	public List<UploadDocumentDTO> getUploadedDocsTableList() {
		return uploadedDocsTableList;
	}

	public void setUploadedDocsTableList(
			List<UploadDocumentDTO> uploadedDocsTableList) {
		this.uploadedDocsTableList = uploadedDocsTableList;
	}

	public List<UploadDocumentDTO> getUploadedDeletedDocsTableList() {
		return uploadedDeletedDocsTableList;
	}

	public void setUploadedDeletedDocsTableList(List<UploadDocumentDTO> uploadedDeletedDocsTableList) {
		this.uploadedDeletedDocsTableList = uploadedDeletedDocsTableList;
	}

	public List<com.shaic.claim.outpatient.processOPpages.OPBillDetailsDTO> getOpBillDetailsList() {
		return opBillDetailsList;
	}

	public void setOpBillDetailsList(List<com.shaic.claim.outpatient.processOPpages.OPBillDetailsDTO> opBillDetailsList) {
		this.opBillDetailsList = opBillDetailsList;
	}

	public String getRejectRemarks() {
		return rejectRemarks;
	}

	public void setRejectRemarks(String rejectRemarks) {
		this.rejectRemarks = rejectRemarks;
	}

	public Double getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(Double approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

	public String getChqModeChngReason() {
		return chqModeChngReason;
	}

	public void setChqModeChngReason(String chqModeChngReason) {
		this.chqModeChngReason = chqModeChngReason;
	}

	public String getChqEmailId() {
		return chqEmailId;
	}

	public void setChqEmailId(String chqEmailId) {
		this.chqEmailId = chqEmailId;
	}

	public String getChqPanno() {
		return chqPanno;
	}

	public void setChqPanno(String chqPanno) {
		this.chqPanno = chqPanno;
	}

	public String getChqPayableAt() {
		return chqPayableAt;
	}

	public void setChqPayableAt(String chqPayableAt) {
		this.chqPayableAt = chqPayableAt;
	}

	public Insured getChqPayeeName() {
		return chqPayeeName;
	}

	public void setChqPayeeName(Insured chqPayeeName) {
		this.chqPayeeName = chqPayeeName;
	}

	public String getChqNameChngReason() {
		return chqNameChngReason;
	}

	public void setChqNameChngReason(String chqNameChngReason) {
		this.chqNameChngReason = chqNameChngReason;
	}

	public String getChqHeirName() {
		return chqHeirName;
	}

	public void setChqHeirName(String chqHeirName) {
		this.chqHeirName = chqHeirName;
	}

	public String getBnkmodeChngReason() {
		return bnkmodeChngReason;
	}

	public void setBnkmodeChngReason(String bnkmodeChngReason) {
		this.bnkmodeChngReason = bnkmodeChngReason;
	}

	public String getBnkEmailId() {
		return bnkEmailId;
	}

	public void setBnkEmailId(String bnkEmailId) {
		this.bnkEmailId = bnkEmailId;
	}

	public String getBnkPanno() {
		return bnkPanno;
	}

	public void setBnkPanno(String bnkPanno) {
		this.bnkPanno = bnkPanno;
	}

	public String getBnkAccNo() {
		return bnkAccNo;
	}

	public void setBnkAccNo(String bnkAccNo) {
		this.bnkAccNo = bnkAccNo;
	}

	public String getBnkIfsc() {
		return bnkIfsc;
	}

	public void setBnkIfsc(String bnkIfsc) {
		this.bnkIfsc = bnkIfsc;
	}

	public String getBnkName() {
		return bnkName;
	}

	public void setBnkName(String bnkName) {
		this.bnkName = bnkName;
	}

	public String getBnkCity() {
		return bnkCity;
	}

	public void setBnkCity(String bnkCity) {
		this.bnkCity = bnkCity;
	}

	public Insured getBnkPayeeName() {
		return bnkPayeeName;
	}

	public void setBnkPayeeName(Insured bnkPayeeName) {
		this.bnkPayeeName = bnkPayeeName;
	}

	public String getBnkNameChngReason() {
		return bnkNameChngReason;
	}

	public void setBnkNameChngReason(String bnkNameChngReason) {
		this.bnkNameChngReason = bnkNameChngReason;
	}

	public String getBnkHeirName() {
		return bnkHeirName;
	}

	public void setBnkHeirName(String bnkHeirName) {
		this.bnkHeirName = bnkHeirName;
	}

	public String getBnkBranch() {
		return bnkBranch;
	}

	public void setBnkBranch(String bnkBranch) {
		this.bnkBranch = bnkBranch;
	}

	public Boolean isChqPayment() {
		return isChqPayment;
	}

	public void setChqPayment(Boolean isChqPayment) {
		this.isChqPayment = isChqPayment;
	}

	public boolean isApprove() {
		return isApprove;
	}

	public void setApprove(boolean isApprove) {
		this.isApprove = isApprove;
	}

	public boolean isReject() {
		return isReject;
	}

	public void setReject(boolean isReject) {
		this.isReject = isReject;
	}

	public Integer getReportOPAvailableSI() {
		return reportOPAvailableSI;
	}

	public void setReportOPAvailableSI(Integer reportOPAvailableSI) {
		this.reportOPAvailableSI = reportOPAvailableSI;
	}

	public Integer getReportOPAmountClaimed() {
		return reportOPAmountClaimed;
	}

	public void setReportOPAmountClaimed(Integer reportOPAmountClaimed) {
		this.reportOPAmountClaimed = reportOPAmountClaimed;
	}

	public Integer getReportConsolidatedBillAmount() {
		return reportConsolidatedBillAmount;
	}

	public void setReportConsolidatedBillAmount(Integer reportConsolidatedBillAmount) {
		this.reportConsolidatedBillAmount = reportConsolidatedBillAmount;
	}

	public Integer getReportConsolidatedDedAmount() {
		return reportConsolidatedDedAmount;
	}

	public void setReportConsolidatedDedAmount(Integer reportConsolidatedDedAmount) {
		this.reportConsolidatedDedAmount = reportConsolidatedDedAmount;
	}

	public Integer getAmountEligible() {
		return amountEligible;
	}

	public void setAmountEligible(Integer amountEligible) {
		this.amountEligible = amountEligible;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public BenefitsAvailedDTO getBenefitsAvailedDto() {
		return benefitsAvailedDto;
	}

	public void setBenefitsAvailedDto(BenefitsAvailedDTO benefitsAvailedDto) {
		this.benefitsAvailedDto = benefitsAvailedDto;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Boolean getConsulation() {
		return consulation;
	}

	public void setConsulation(Boolean consulation) {
		this.consulation = consulation;
	}

	public Boolean getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(Boolean diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Boolean getPhysiotherapy() {
		return physiotherapy;
	}

	public void setPhysiotherapy(Boolean physiotherapy) {
		this.physiotherapy = physiotherapy;
	}

	public Boolean getMedicine() {
		return medicine;
	}

	public void setMedicine(Boolean medicine) {
		this.medicine = medicine;
	}

	public List<OPSpecialityDTO> getSpecialityDTOList() {
		return specialityDTOList;
	}

	public void setSpecialityDTOList(List<OPSpecialityDTO> specialityDTOList) {
		this.specialityDTOList = specialityDTOList;
	}

	public String getAvailableSI() {
		return availableSI;
	}

	public void setAvailableSI(String availableSI) {
		this.availableSI = availableSI;
	}

	public String getEligible() {
		return eligible;
	}

	public void setEligible(String eligible) {
		this.eligible = eligible;
	}

	public Double getPayble() {
		return payble;
	}

	public void setPayble(Double payble) {
		this.payble = payble;
	}

	public CreateBatchOpTableDTO getViewPaymentDto() {
		return viewPaymentDto;
	}

	public void setViewPaymentDto(CreateBatchOpTableDTO viewPaymentDto) {
		this.viewPaymentDto = viewPaymentDto;
	}

	public WeakHashMap getFilePathAndTypeMap() {
		return filePathAndTypeMap;
	}

	public void setFilePathAndTypeMap(WeakHashMap filePathAndTypeMap) {
		this.filePathAndTypeMap = filePathAndTypeMap;
	}

	public Boolean getAvailableSiFlag() {
		return availableSiFlag;
	}

	public void setAvailableSiFlag(Boolean availableSiFlag) {
		this.availableSiFlag = availableSiFlag;
	}

	public String getPolicyPed() {
		return policyPed;
	}

	public void setPolicyPed(String policyPed) {
		this.policyPed = policyPed;
	}

	public InsuredDto getInsuredDto() {
		return insuredDto;
	}

	public void setInsuredDto(InsuredDto insuredDto) {
		this.insuredDto = insuredDto;
	}

	public Double getPerClaimLimit() {
		return perClaimLimit;
	}

	public void setPerClaimLimit(Double perClaimLimit) {
		this.perClaimLimit = perClaimLimit;
	}

	public Double getPerPolicyLimit() {
		return perPolicyLimit;
	}

	public void setPerPolicyLimit(Double perPolicyLimit) {
		this.perPolicyLimit = perPolicyLimit;
	}

	public Double getAvailablevaccinationlimit() {
		return availablevaccinationlimit;
	}

	public void setAvailablevaccinationlimit(Double availablevaccinationlimit) {
		this.availablevaccinationlimit = availablevaccinationlimit;
	}
	
}