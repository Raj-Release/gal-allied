package com.shaic.claim.processrejection.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;

public class SearchProcessRejectionTableDTO  extends AbstractTableDTO  implements Serializable {

	private static final long serialVersionUID = 4016831167620717224L;
	
	private Long key;

	private String intimationNo;

	private Date intimationDate;

	private String hospitalType;
	
	private Boolean isPremedical=false;

	private String status;

	private String preauthStatus;
	
	private String strIntimationDate;
	
	private Long statusKey;
	
	private String docFilePath;
	
	private String docType;
	
	private String docSource;
	
	private ProcessRejectionDTO processRejectionDTO;
	
	private ClaimDto claimDto;

	private String admissionDate;
	
	private String eventCodeDescription;
	
	private String policyNo;
	
	private String patientName;
	
	private String ailmentLoss;
	
	private String accidentDeath;
	
	private String crmFlagged;
	
	private String crcFlaggedReason;
    
    private String crcFlaggedRemark;
    
    private String IsPreferredHospital;
    
    private Double InsuredAge;
    
    private Long Intimationkey;
    
    private PreauthDTO preAuthDto = new PreauthDTO();
    
    private Long rodKey;
	
	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public SearchProcessRejectionTableDTO(){
		this.processRejectionDTO = new ProcessRejectionDTO();
	}
	
	
//	private HumanTask humanTask;
	
	private RRCDTO rrcDTO;
	
	public PreauthDTO getPreAuthDto() {
		return preAuthDto;
	}

	public void setPreAuthDto(PreauthDTO preAuthDto) {
		this.preAuthDto = preAuthDto;
	}


	private NewIntimationDto intimationDTO;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public Date getIntimationDate() {
		return intimationDate;
	}

	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPreauthStatus() {
		return preauthStatus;
	}

	public void setPreauthStatus(String preauthStatus) {
		this.preauthStatus = preauthStatus;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

/*	public HumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}
*/
	public Boolean getIsPremedical() {
		return isPremedical;
	}

	public void setIsPremedical(Boolean isPremedical) {
		this.isPremedical = isPremedical;
	}

	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}

	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}

	public String getStrIntimationDate() {
		return strIntimationDate;
	}

	public void setStrIntimationDate(String strIntimationDate) {
		this.strIntimationDate = strIntimationDate;
	}

	public NewIntimationDto getIntimationDTO() {
		return intimationDTO;
	}

	public void setIntimationDTO(NewIntimationDto intimationDTO) {
		this.intimationDTO = intimationDTO;
	}

	public ProcessRejectionDTO getProcessRejectionDTO() {
		return processRejectionDTO;
	}

	public void setProcessRejectionDTO(ProcessRejectionDTO processRejectionDTO) {
		this.processRejectionDTO = processRejectionDTO;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public String getDocFilePath() {
		return docFilePath;
	}

	public void setDocFilePath(String docFilePath) {
		this.docFilePath = docFilePath;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocSource() {
		return docSource;
	}

	public void setDocSource(String docSource) {
		this.docSource = docSource;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		
		this.claimDto = claimDto;
		
		if(this.claimDto != null && this.claimDto.getEventIdDescription() != null){
			this.eventCodeDescription = this.claimDto.getEventIdDescription();
		}
	}

	public String getAdmissionDate() {

		if(this.claimDto != null && this.claimDto.getAdmissionDate() != null){
			admissionDate = new SimpleDateFormat("dd-mm-yyyy").format(this.claimDto.getAdmissionDate());
		}
		else if (this.claimDto != null && this.claimDto.getNewIntimationDto() != null && this.claimDto.getNewIntimationDto().getAdmissionDate() != null){
			admissionDate = new SimpleDateFormat("dd-mm-yyyy").format(this.claimDto.getNewIntimationDto().getAdmissionDate());
		}
		else{
			admissionDate = "";
		}
		return admissionDate;
	}

	public String getEventCodeDescription() {
		return eventCodeDescription;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getPatientName() {
		
	 this.patientName = this.claimDto != null && this.claimDto.getNewIntimationDto() != null && this.claimDto.getNewIntimationDto().getInsuredPatient() != null && this.claimDto.getNewIntimationDto().getInsuredPatient().getInsuredName() != null ? this.claimDto.getNewIntimationDto().getInsuredPatient().getInsuredName()  : "" ;  
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getAilmentLoss() {

		if(this.claimDto != null && this.claimDto.getAilmentLoss() != null){
			ailmentLoss = this.claimDto.getAilmentLoss();
		}
		else{
			ailmentLoss = "";
		}		
		return ailmentLoss;
	}
	public String getAccidentDeath() {
		return accidentDeath;
	}

	public void setAccidentDeath(String accidentDeath) {
		this.accidentDeath = accidentDeath;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
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

	public Long getIntimationkey() {
		return Intimationkey;
	}

	public void setIntimationkey(Long intimationkey) {
		Intimationkey = intimationkey;
	}

}
