package com.shaic.claim.reimbursement.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shaic.claim.ClaimDto;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthPreviousClaimsDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
//import com.shaic.ims.bpm.claim.modelv2.HumanTask;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class ReimbursementWizardDTO implements Serializable {
	private static final long serialVersionUID = 5396473523361389154L;

	private Long key;
	
	//Set for user name and password issue.
	private String strUserName;
	
	private String strPassword;
	
	private Long policyKey;
	private Long intimationKey;
	private String claimNumber;
	private Long hospitalKey;
	private Long claimKey;
	private Long stageKey;
	private Long statusKey;
	private String statusValue;
	private String processType;
	
	//private HumanTask humanTask;
	
	private ClaimDto claimDTO;
	
	private PolicyDto policyDto;
	
	private Double balanceSI;
	
	private NewIntimationDto newIntimationDTO;
	
	private CoordinatorDTO coordinatorDetails;
	
	private PreauthPreviousClaimsDTO preauthPreviousClaimsDetails;
	
	private List<PreviousClaimsTableDTO> previousClaimsList;
	
	private ReimbursementDataExtractionDTO reimbursementDataExtractionDTO;
	
	public ReimbursementWizardDTO() {
		reimbursementDataExtractionDTO = new ReimbursementDataExtractionDTO();
		coordinatorDetails = new CoordinatorDTO();
		preauthPreviousClaimsDetails = new PreauthPreviousClaimsDTO();
		previousClaimsList = new ArrayList<PreviousClaimsTableDTO>();
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public Long getHospitalKey() {
		return hospitalKey;
	}

	public void setHospitalKey(Long hospitalKey) {
		this.hospitalKey = hospitalKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	/*public HumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/

	public ClaimDto getClaimDTO() {
		return claimDTO;
	}

	public void setClaimDTO(ClaimDto claimDTO) {
		this.claimDTO = claimDTO;
	}

	public PolicyDto getPolicyDto() {
		return policyDto;
	}

	public void setPolicyDto(PolicyDto policyDto) {
		this.policyDto = policyDto;
	}

	public Double getBalanceSI() {
		return balanceSI;
	}

	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}

	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	public CoordinatorDTO getCoordinatorDetails() {
		return coordinatorDetails;
	}

	public void setCoordinatorDetails(CoordinatorDTO coordinatorDetails) {
		this.coordinatorDetails = coordinatorDetails;
	}

	public PreauthPreviousClaimsDTO getPreauthPreviousClaimsDetails() {
		return preauthPreviousClaimsDetails;
	}

	public void setPreauthPreviousClaimsDetails(
			PreauthPreviousClaimsDTO preauthPreviousClaimsDetails) {
		this.preauthPreviousClaimsDetails = preauthPreviousClaimsDetails;
	}

	public List<PreviousClaimsTableDTO> getPreviousClaimsList() {
		return previousClaimsList;
	}

	public void setPreviousClaimsList(
			List<PreviousClaimsTableDTO> previousClaimsList) {
		this.previousClaimsList = previousClaimsList;
	}

	public ReimbursementDataExtractionDTO getReimbursementDataExtractionDTO() {
		return reimbursementDataExtractionDTO;
	}

	public void setReimbursementDataExtractionDTO(
			ReimbursementDataExtractionDTO reimbursementDataExtractionDTO) {
		this.reimbursementDataExtractionDTO = reimbursementDataExtractionDTO;
	}
}
