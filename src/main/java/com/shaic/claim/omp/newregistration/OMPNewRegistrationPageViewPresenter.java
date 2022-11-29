package com.shaic.claim.omp.newregistration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationService;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationTableDTO;
import com.shaic.claim.omp.createintimation.OMPIntimationMapper;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Insured;
import com.shaic.domain.MasterOMPEventProcedure;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.VaadinSession;

@ViewInterface(OMPNewRegistrationPageView.class)
public class OMPNewRegistrationPageViewPresenter extends AbstractMVPPresenter<OMPNewRegistrationPageView>{

	private static final long serialVersionUID = 451825898685072621L;

	@Override
	public void viewEntered() {
		System.out.println("OMPRegistrationPageViewPresenter Called.....");
	}
	
	public static final String SUBMIT_INTIMATION = "submit_omp_intimation_Reg";
	public static final String SAVE_INTIMATION = "save_omp_intimation_Reg";
	public static final String CANCEL_INTIMATION = "cancel_omp_intimation_Reg";
	public static final String FIND_INSURED = "find_omp_insured_Reg";
	public static final String CLICK_REGISTER_BUTTON = "OMP intim click_register_button_Reg";
	
	public static final String SUGGEST_REJECTION = "OMP intim Rejection_Suggested_Reg";
	
	public static final String SUBMIT_CLAIM_CLICK = "OMP intim submit_register_claim_Reg";
	
	public static final String SUBMIT_CLAIM_REGISTRATION = "OMP intim submit_registration_of_claim_Reg";
	
	public static final String GET_BALANCE_SI = "OMP get_Balance_SumInsured_Reg";
	
	@EJB
	private OMPCreateIntimationService ompService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private OMPClaimService ompclaimService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private OMPIntimationService ompIntimationService;
	
	ClaimDto claimDto = new ClaimDto();
	
	@SuppressWarnings("static-access")
	public void submitWizard(@Observes @CDIEvent(SUBMIT_INTIMATION) final ParameterDTO parameters) {
		OMPNewRegistrationSearchDTO intimationDTO = (OMPNewRegistrationSearchDTO) parameters.getPrimaryParameter();
		OMPIntimation ompintimation = new OMPNewRegIntimationMapper().getInstance().getNewIntimation(intimationDTO);
		ompintimation = updateNewIntimationWithValues(ompintimation, ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
		try{
			ompintimation.setCityName(intimationDTO.getCityName());
			if(intimationDTO.getHospitalId()!=null){
				ompintimation.setHospital(intimationDTO.getHospitalId().getId());
			}
			
			OMPIntimation ompIntimationObj= ompIntimationService.getIntimationByNo(ompintimation.getIntimationId());
			if(ompIntimationObj!=null){
				ompintimation.setCreatedBy(ompIntimationObj.getCreatedBy());
				ompintimation.setCreatedDate(ompIntimationObj.getCreatedDate());
			}
			ompintimation.setCountryId(intimationDTO.getCountryId());
			ompintimation.setLossTime(intimationDTO.getLossTime());
			ompintimation.setLossDetails(intimationDTO.getLossDetails());
			ompintimation.setEventPlace(intimationDTO.getPlaceofAccidentOrEvent());
			ompintimation.setModifiedBy(intimationDTO.getUsername());
			ompintimation.setModifiedDate(new Date());
			ompintimation.setInrConversionRate(intimationDTO.getInrConversionRate());
			ompintimation.setInrTotalAmount(intimationDTO.getInrTotalAmount());
			ompintimation.setDollarInitProvisionAmt(intimationDTO.getInitProvisionAmount());
			ompintimation.setClaimedAmount(intimationDTO.getIntimationClmAmount());
			ompintimation.setRemarks(intimationDTO.getIntimationRemarks());
			if(!StringUtils.isBlank(intimationDTO.getTpaIntimationNumber())){
				ompintimation.setTpaIntimationNumber(intimationDTO.getTpaIntimationNumber());
			}
			
			ompintimation = ompService.registerOMPIntimation(ompintimation);
			intimationDTO.setIntimationKey(ompintimation.getKey());
			ompintimation = ompService.getOMPIntimationByKey(ompintimation.getKey());
			intimationDTO.setIntimationno(ompintimation.getIntimationId());
			String message = "Claim Intimation No "+ompintimation.getIntimationId()+" has been successfully submitted.";
			System.out.println(message);
			/*OMPClaim claimsByIntimationNumber = ompclaimService.getClaimsByIntimationNumber(ompintimation.getIntimationId());
			claimsByIntimationNumber.setInrConversionRate(intimationDTO.getInrConversionRate());
			claimsByIntimationNumber.setInrTotalAmount(intimationDTO.getInrTotalAmount());
			claimsByIntimationNumber.setDollarInitProvisionAmount(intimationDTO.getInitProvisionAmount());
			ompService.submitOMPClaim(claimsByIntimationNumber);*/
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
		String get64vbStatus = "";																											 
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			get64vbStatus = PremiaService.getInstance().get64VBStatus(claimDto.getNewIntimationDto().getPolicy().getPolicyNumber(), claimDto.getNewIntimationDto().getIntimationId());   
			if(get64vbStatus != null && (SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus) || SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) )) {
				//|| SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus)
				isProceedFurther = false;
			}
		}
		view.submitClaimRegister(claimDto, isProceedFurther, get64vbStatus);
	}
	
	protected void submitClaimRegistration(
			@Observes @CDIEvent(SUBMIT_CLAIM_REGISTRATION) final ParameterDTO parameters) {
		
		OMPNewRegistrationSearchDTO resultTask = (OMPNewRegistrationSearchDTO) parameters.getPrimaryParameter();
		OMPSearchClaimRegistrationTableDTO searchDto = new OMPSearchClaimRegistrationTableDTO();//OMPEarlierRodMapper.getInstance().getintimationChangeDetails(resultTask);
		searchDto.setIntimationKey(resultTask.getIntimationKey());
		searchDto.setIntimationNo(resultTask.getIntimationno());
		searchDto.setPolicyno(resultTask.getPolicyNo());
		searchDto.setProductcode(resultTask.getProductCodeOrName());
		searchDto.setIntimationDate(resultTask.getIntimationdate());
		searchDto.setAdmissiondate(resultTask.getAdmissionDate());
		searchDto.setWorkFlowKey(resultTask.getWfKey());
		
		OMPIntimation intimation = ompIntimationService.getNewNotRegisteredIntimation(resultTask.getIntimationno());
		NewIntimationDto intimationToRegister = ompIntimationService.getIntimationDto(intimation);
		ClaimDto claimDto = (ClaimDto) parameters.getSecondaryParameters()[0];
		claimDto.setIntimationKey(resultTask.getIntimationKey());
		
		resultTask.setIntimationDto(intimationToRegister);
		resultTask.setClaimDto(claimDto);

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
	/*	MastersValue master =null;
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
			
		}*/
		claimDto.setStageId(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		if(claimDto.getSuggestRejection().equals(Boolean.TRUE)){				
//			claimDto.setStageId(ReferenceTable.PROCESS_REJECTION_STAGE);
            claimDto.setStatusId(ReferenceTable.OMP_REGISTRATION_REJECTED);
			claimDto.setSuggestedRejectionRemarks(resultTask.getRemarks());
		}
		else{
//			claimDto.setStatusId(ReferenceTable.CLAIM_REGISTERED_STATUS);
			claimDto.setStatusId(ReferenceTable.CLAIM_REGISTERED_STATUS);
//			claimDto.setStatusId(ReferenceTable.INTIMATION_REGISTERED_STATUS);
			claimDto.setRegistrationRemarks(resultTask.getRemarks());
		}
		
		if(claimDto.getNewIntimationDto().getPolicy() != null){
			claimDto.setOfficeCode(claimDto.getNewIntimationDto().getPolicy().getHomeOfficeCode());	
		}		
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		searchDto.setUserId(loginUserId);
		resultTask.getClaimDto().setModifiedBy(loginUserId);
		claimDto.setClaimedAmount(resultTask.getIntimationClmAmount());
		
		claimDto = ompclaimService.submitClaimRegistration(searchDto,claimDto);
		
		OMPClaim claim = ompclaimService.getClaimsByIntimationNumber(intimationToRegister.getIntimationId());
		
		
		if(ReferenceTable.CLAIM_REGISTERED_STATUS.equals(claimDto.getStatusId())
				&& claim.getClaimId() != null
				&& resultTask.getEventCode() != null
				&& resultTask.getEventCode().getId() != null) {
		
			MasterOMPEventProcedure ompEventProcedure = ompclaimService.getOmpClaimProcedureDetailsyByEventKey(resultTask.getEventCode().getId());
			
			if(ompEventProcedure != null) {
				resultTask.setTypeOfClaim(ompEventProcedure.getTypeOfClaim());
				resultTask.setRiskCovered(ompEventProcedure.getRiskCovered());
				resultTask.setExpensesPayable(ompEventProcedure.getExpensesPayable());
				resultTask.setProcedureToFollow(ompEventProcedure.getProcedureToFollow());
				resultTask.setDocumentsRequired(ompEventProcedure.getDocumentsRequired());
			}
			
			
			DocumentGenerator docgen = new DocumentGenerator();
			
			ReportDto reportDto = new ReportDto();
			
			List<OMPNewRegistrationSearchDTO> claimListDto = new ArrayList<OMPNewRegistrationSearchDTO>();
			
			claimListDto.add(resultTask);
			
			reportDto.setClaimId(claim.getClaimId());
			reportDto.setBeanList(claimListDto);
			
			String fileUrl = docgen.generatePdfDocument(SHAConstants.OMP_CLAIM_COVERING_LETTER, reportDto);
			
			if(!ValidatorUtils.isNull(fileUrl))
			{	
				resultTask.getClaimDto().setDocFilePath(fileUrl);
				resultTask.getClaimDto().setDocType(SHAConstants.CLAIM_COVERING_LETTER);
				ompclaimService.uploadCoveringLetterToDMs(resultTask.getClaimDto());
			 
			}
		}	
		
		view.setClaimDetails(claimDto);
		view.showSubmitMessage(claim);
	}
	
}
