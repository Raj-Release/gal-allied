package com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages;

import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.claimhistory.view.ompView.OMPEarlierRodMapper;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationService;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationTableDTO;
import com.shaic.claim.omp.createintimation.OMPIntimationMapper;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.Insured;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
//import com.shaic.claim.OMPcreateintimation.search.OMPCreateIntimationFormDto;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.domain.OMPNewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;



@ViewInterface(OMPCreateIntimationWizardView.class)
public class OMPCreateIntimationWizardViewPresenter extends AbstractMVPPresenter<OMPCreateIntimationWizardView>{
	
	private static final long serialVersionUID = 1641401308501786852L;
	
	public static final String SUBMIT_INTIMATION = "submit_omp_intimation";
	public static final String SAVE_INTIMATION = "save_omp_intimation";
	public static final String CANCEL_INTIMATION = "cancel_omp_intimation";
	public static final String FIND_INSURED = "find_omp_insured_create";
	public static final String CLICK_REGISTER_BUTTON = "OMP intim click_register_button";
	
	public static final String SUGGEST_REJECTION = "OMP intim Rejection_Suggested";
	
	public static final String SUBMIT_CLAIM_CLICK = "OMP intim submit_register_claim";
	
	public static final String SUBMIT_CLAIM_REGISTRATION = "OMP intim submit_registration_of_claim";
	@EJB
	private OMPCreateIntimationService ompService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private OMPClaimService ompclaimService;
	
	@EJB
	private OMPIntimationService ompIntimationService;
	
	ClaimDto claimDto = new ClaimDto();
	
	@Override
	public void viewEntered() {
		System.out.println("OMPCreateIntimationWizardViewPresenter Entered...");
	}
	
	@SuppressWarnings("static-access")
	public void submitWizard(@Observes @CDIEvent(SUBMIT_INTIMATION) final ParameterDTO parameters) {
		OMPCreateIntimationTableDTO intimationDTO = (OMPCreateIntimationTableDTO) parameters.getPrimaryParameter();
		OMPIntimation ompintimation = new OMPIntimationMapper().getInstance().getNewIntimation(intimationDTO);
		ompintimation = updateNewIntimationWithValues(ompintimation, ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
		try{
			ompintimation.setCityName(intimationDTO.getCityName());
			if(intimationDTO.getHospitalId()!=null){
				ompintimation.setHospital(intimationDTO.getHospitalId().getId());
			}
			ompintimation.setCountryId(intimationDTO.getCountryId());
			ompintimation.setLossTime(intimationDTO.getLossTime());
			ompintimation.setLossDetails(intimationDTO.getLossDetails());
			ompintimation.setEventPlace(intimationDTO.getPlaceofAccidentOrEvent());
			ompintimation.setCreatedBy(intimationDTO.getUsername());
			ompintimation.setCreatedDate(new Date());
			if(!StringUtils.isBlank(intimationDTO.getTpaIntimationNumber())){
				ompintimation.setTpaIntimationNumber(intimationDTO.getTpaIntimationNumber());
			}
			//R1276
			ompintimation.setClaimedAmount(intimationDTO.getIntimationClmAmount());
			ompintimation.setRemarks(intimationDTO.getIntimationRemarks());
			
			ompintimation = ompService.submitOMPIntimation(ompintimation);
			intimationDTO.setIntimationKey(ompintimation.getKey());
			ompintimation = ompService.getOMPIntimationByKey(ompintimation.getKey());
			intimationDTO.setIntimationno(ompintimation.getIntimationId());
			ompService.submitDBProcedureForOMPIntimation(ompintimation);			// Re-enable workflow in R1276 CR
			String message = "Claim Intimation No "+ompintimation.getIntimationId()+" has been successfully submitted.";
			System.out.println(message);
//			OMPClaim claimsByIntimationNumber = ompclaimService.getClaimsByIntimationNumber(ompintimation.getIntimationId());
//			view.showSubmitMessage(claimsByIntimationNumber);
//			view.showSubmitMessagePanel(message);
		}catch(Exception exp){
			System.out.println("Exception occurred while submitting OMP Intimation"+exp.getMessage());
			exp.printStackTrace();
			view.showErrorMessagePanel("Error Occurred while submitting intimation, please contact administrator.");
		}
	}
	
	@SuppressWarnings("static-access")
	public void saveWizard(@Observes @CDIEvent(SAVE_INTIMATION) final ParameterDTO parameters) {
		OMPCreateIntimationTableDTO intimationDTO = (OMPCreateIntimationTableDTO) parameters.getPrimaryParameter();
		OMPIntimation ompintimation = new OMPIntimationMapper().getInstance().getNewIntimation(intimationDTO);
		ompintimation = updateNewIntimationWithValues(ompintimation,ReferenceTable.INTIMATION_SAVE_STATUS_KEY);
		try{
			ompService.submitOMPIntimation(ompintimation);
			intimationDTO.setIntimationKey(ompintimation.getKey());
			intimationDTO.setIntimationno(ompintimation.getIntimationId());
			ompService.submitDBProcedureForOMPIntimation(ompintimation);
			String message = "Claim Intimation No has been successfully saved.";
			view.showSubmitMessagePanel(message);
		}catch(Exception exp){
			System.out.println("Exception occurred while saving OMP Intimation"+exp.getMessage());
			exp.printStackTrace();
			view.showErrorMessagePanel("Error Occurred while saving intimation, please contact administrator.");
		}
	}
	
	public void cancelIntimation(@Observes @CDIEvent(CANCEL_INTIMATION) final ParameterDTO parameters) {
		view.cancelIntimation();
	}
	
	public void getInsuredFromPolicy(@Observes @CDIEvent(FIND_INSURED) final ParameterDTO parameters){
		Insured insured = null;
		long insuredKey =  Long.parseLong(parameters.getPrimaryParameter().toString());
		insured = policyService.findByClsInsuredKey(insuredKey);
		view.setInsuredInPage(insured);
	}
	
	private OMPIntimation updateNewIntimationWithValues(OMPIntimation ompintimation, Long statusKey ){
		Stage stage = new Stage();
		stage.setKey(ReferenceTable.INTIMATION_STAGE_KEY);
		ompintimation.setStage(stage);
		
		Status status = new Status();
		status.setKey(statusKey);
		ompintimation.setStatus(status);
		
		return ompintimation;
	}
	
	protected void registerClicked(
			@Observes @CDIEvent(CLICK_REGISTER_BUTTON) final ParameterDTO parameters) {
		view.registerClicked();
	}


	protected void suggestRejectionClicked(
			@Observes @CDIEvent(SUGGEST_REJECTION) final ParameterDTO parameters) {
		view.suggestRejectionClicked();
	}
	
	protected void submitClaimRegister(
			@Observes @CDIEvent(SUBMIT_CLAIM_CLICK) final ParameterDTO parameters) {
		ClaimDto claimDto = (ClaimDto) parameters.getPrimaryParameter();
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		Boolean isProceedFurther = true;
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
//			String get64vbStatus = "";																											 
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(claimDto.getNewIntimationDto().getPolicy().getPolicyNumber(), claimDto.getNewIntimationDto().getIntimationId());   
			if(get64vbStatus != null && (SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
				isProceedFurther = false;
			}
		}
		view.submitClaimRegister(claimDto, isProceedFurther);
	}
	
	
	protected void submitClaimRegistration(
			@Observes @CDIEvent(SUBMIT_CLAIM_REGISTRATION) final ParameterDTO parameters) {
		
		OMPCreateIntimationTableDTO resultTask = (OMPCreateIntimationTableDTO) parameters.getPrimaryParameter();
		OMPSearchClaimRegistrationTableDTO searchDto = OMPEarlierRodMapper.getInstance().getintimationChangeDetails(resultTask);
		OMPIntimation intimation = ompIntimationService.getNotRegisteredIntimation(resultTask.getIntimationno());
		NewIntimationDto intimationToRegister = ompIntimationService.getIntimationDto(intimation);
		ClaimDto claimDto = (ClaimDto) parameters.getSecondaryParameters()[0];
		claimDto.setIntimationKey(resultTask.getIntimationKey());
		//R1276 - Removed Provisional Details Fields.
		if(intimationToRegister!= null && intimationToRegister.getInrConversionRate()!= null){
			claimDto.setInrConversionRate(intimationToRegister.getInrConversionRate());
		}
		claimDto.setInrTotalAmount(intimationToRegister.getInrTotalAmount());
		claimDto.setDollarInitProvisionAmount(intimationToRegister.getDollarInitProvisionAmt());
		
		claimDto.setAdmissionDate(intimationToRegister.getAdmissionDate());
		claimDto.setDischargeDate(intimationToRegister.getDischargeDate());
		if(intimation.getPolicy()!= null && intimation.getPolicy().getProduct()!=null && intimation.getPolicy().getProduct().getCode()!= null){
			claimDto.setProductCode(intimation.getPolicy().getProduct().getCode());
		}
		claimDto.setProductName(intimationToRegister.getProductName());
		claimDto.setNewIntimationDto(intimationToRegister);
		if(intimation.getEvent()!= null && intimation.getEvent().getEventCode()!= null){
			MastersEvents event = intimation.getEvent();
			SelectValue eventCodeValue = new SelectValue();
			eventCodeValue.setId(event.getKey());
			eventCodeValue.setValue(event.getEventCode());
			claimDto.setEventCodeValue(eventCodeValue);
		}
		claimDto.setHospitalName(intimation.getHospitalName());
		claimDto.setCityName(intimation.getCityName());
		claimDto.setCountryId(intimation.getCountryId());
		claimDto.setHospitalisationFlag(intimationToRegister.getHospitalizationFlag());
		claimDto.setNonHospitalisationFlag(intimationToRegister.getNonHospitalizationFlag());
		claimDto.setAilmentLoss(intimation.getAilmentLoss());
		claimDto.setClaimType(intimationToRegister.getClaimType());
		claimDto.setPlaceOfEvent(intimation.getEventPlace());
		claimDto.setPlaceOfvisit(intimation.getPlaceVisit());
		claimDto.setLossDetails(intimation.getLossDetails());
		claimDto.setLossTime(resultTask.getLossTime());
		claimDto.setLossDateTime(intimation.getLossDateTime());
		claimDto.setPlaceLossDelay(intimation.getPlaceLossDelay());
		claimDto.setHospitalId(intimation.getHospital());
		MastersValue master =null;
		if(claimDto.getClaimType()!= null){
			master = masterService.getMaster(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
		}else{
			master = masterService.getMaster(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
		}
		
		if (master != null) {
			claimDto.setConversionFlag(master.getValue().toLowerCase()
					.contains("cashless") ? 1l : 0l);
			SelectValue claimType = new SelectValue();
			claimType.setId(master.getKey());
			claimType.setValue(master.getValue());
			claimDto.setClaimType(claimType);
		}
		 /*Setting Intimation stage and status as Registration is separated in CR1276 and based upon rejection and 
		 registration status will be updated in claim table in registration screen.*/
		claimDto.setStageId(ReferenceTable.INTIMATION_STAGE_KEY); 
		claimDto.setStatusId(ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
/*		if(claimDto.getSuggestRejection().equals(Boolean.TRUE)){				
			claimDto.setStatusId(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS);
//			claimDto.setSuggestedRejectionRemarks(resultTask.getRemarks());
		}
		else{
			claimDto.setStatusId(ReferenceTable.CLAIM_REGISTERED_STATUS);
//			claimDto.setStatusId(ReferenceTable.INTIMATION_REGISTERED_STATUS);
//			claimDto.setRegistrationRemarks(resultTask.getRemarks());
		}*/
		
		if(claimDto.getNewIntimationDto().getPolicy() != null){
			claimDto.setOfficeCode(claimDto.getNewIntimationDto().getPolicy().getHomeOfficeCode());	
		}		
		searchDto.setUsername(resultTask.getUsername());

		claimDto.setClaimedAmount(intimation.getClaimedAmount());
		claimDto.setIntimationRemarks(intimation.getRemarks());
		claimDto = ompclaimService.submitClaim(searchDto,claimDto);
		OMPClaim claim = ompclaimService.getClaimsByIntimationNumber(intimationToRegister.getIntimationId());
//		view.setClaimDetails(claimDto);
//		view.showSubmitMessage(claim, claimDto);
		view.showSubmitMessage(null, null, intimation);
	}
	
	
}
