package com.shaic.claim.omp.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.intimation.CashLessTableDetails;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.intimation.ViewIntimationStatus;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.CashlessDetailsService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.OMPClaimMapper;
import com.shaic.newcode.wizard.domain.OMPNewIntimationMapper;
import com.shaic.newcode.wizard.domain.PreviousClaimMapper;
import com.shaic.newcode.wizard.dto.ClaimStatusDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
@ViewInterface(OMPClaimRegistrationWizardView.class)
public class OMPClaimRegistrationWizardPresenter extends
		AbstractMVPPresenter<OMPClaimRegistrationWizardView> {

	public static final String SUBMIT_CLAIM_CLICK = "OMP submit_register_claim";
	public static final String CLICK_REGISTER_BUTTON = "OMP click_register_button";
	public static final String SUGGEST_REJECTION = "OMP Rejection_Suggested";
	public static final String SUGGEST_REJECTION_VALUE_CHANGE = "OMP Suggest Rejection Value Changed";
	public static final String CANCEL_CLAIM_REGISTRATION = "OMP cancel_claim_registration";
	public static final String CLICK_GENERATE_COVERING_LETTER_BUTTON = "OMP Click_Generate_Covering_Letter";
//	public static final String CLICK_HOMEPAGE_OR_REG_ANOTHER_CLAIM = "click_homepage_or_reg_another_claim";
	public static final String GET_CLAIM = "OMP get_claim_details";
	public static final String SUBMIT_CLAIM_REGISTRATION = "OMP submit_registration_of_claim";
	public static final String GET_CURRENCY_MASTER = "OMP get_currency_master";
	public static final String GET_PREVIOUS_CLAIMS = "OMP get_previous_claims";
	public static final String GET_BALANCE_SI = "OMP get_balance_sumInsured";
	public static final String GET_CPU_OBJECT = "OMP get_cpu_object";
	public static final String GET_SUB_LIMIT_LIST = "OMP get_sub_limit_list";
	public static final String CLICK_VIEW_CLAIM_STATUS = "OMP click_view_claim_status";
	public static final String GET_DATA_TO_FIELDS = "omp get data for fields";

	@EJB
	private PolicyService policyService;

	@EJB
	private ClaimService claimService;
	
	@EJB
	private OMPClaimService ompclaimService;
	
	@EJB
	private PreauthService preauthService;
	
	private NewIntimationDto newIntimationDto;

	@EJB
	private OMPIntimationService intimationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dBCalculationService;
	
	@Inject
	private CashLessTableDetails cashLessTableDetails;

	@Inject
	private CashlessTable cashlessTable;

	@Inject
	private NewIntimationService newIntimationService;

	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private HospitalService hospitalService;
	
//	@Inject
//	private	DBCalculationService dbCalculationService;
	
	@Inject
	private ClaimStatusDto claimStatusDto;
	
	@Inject
	private CashlessDetailsService cashlessDetailsService;
	
	/*@EJB
	private MasterService masterService;*/
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_CLAIM_REGISTRATION_USER_RRC_REQUEST = "OMP validate_claim_registration_user_rrc_request";
	
	public static final String PROCESS_CLAIM_REGISTRATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "OMP claim_registration_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_CLAIM_REGISTRATION_SAVE_RRC_REQUEST_VALUES = "OMP claim_registration_save_rrc_request_values";
	
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	protected void viewPreviousClaimStatus(
			@Observes @CDIEvent(CLICK_VIEW_CLAIM_STATUS) final ParameterDTO parameters) {
		
		PreviousClaimsTableDTO previousClaimDato = (PreviousClaimsTableDTO) parameters.getPrimaryParameter(); 
		
		OMPIntimation intimation = intimationService.getIntimationByNo(previousClaimDato.getIntimationNumber());
		
//		NewIntimationDto newIntimationDto = intimationService.getIntimationDto(intimation);
//		
//		//NewIntimationDto newIntimationDto = policyService.newIntimationToIntimationDTO(intimation);
//		
//		Claim claim = claimService.getClaimsByIntimationNumber(intimation.getIntimationId()); 
//		
//		Hospitals hospital = hospitalService.getHospitalById(intimation.getHospital());
//		
//		CashlessDetailsDto cashlessDetailsDto = cashlessDetailsService
//				.getCashlessDetails(intimation.getKey());
//		
//		/*ClaimDtoOld claimDto = policyService.claimToClaimDTO(claim,
//				hospital);*/
//		ClaimDto claimDto = claimService.claimToClaimDTO(claim);
//		if(claimDto!=null){
//			claimStatusDto.setClaimDto(claimDto);
//			claimStatusDto.setNewIntimationDto(claimDto.getNewIntimationDto());
//			claimStatusDto.setCashlessDetailsDto(cashlessDetailsDto);
//			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(claimStatusDto, intimation.getPolicy()
//							.getActiveStatus() == null, cashLessTableDetails,
//					cashlessTable, cashLessTableMapper, newIntimationService, intimation);
//			
//			UI.getCurrent().addWindow(intimationStatus);
//		}
		
		if( intimation != null){
			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(intimation.getIntimationId(),viewDetails);			
			UI.getCurrent().addWindow(intimationStatus);
		}
		
		
	}
	
	/**
	 * Added for RRC Starts
	 * */
//	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_CLAIM_REGISTRATION_USER_RRC_REQUEST) final ParameterDTO parameters){
//		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
//		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
//		view.buildValidationUserRRCRequestLayout(isValid);
//		
//	}
//	
//
//	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_CLAIM_REGISTRATION_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
//		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
//		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
//		view.buildRRCRequestSuccessLayout(rrcRequestNo);
//		
//	}
//	
//	public void loadRRCRequestDropDownValues(
//			@Observes @CDIEvent(PROCESS_CLAIM_REGISTRATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
//		{
//		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
//		view.loadRRCRequestDropDownValues(mastersValueContainer);
//		}
	/**
	 * Added for RRC Ends
	 * */

//	protected void getSublimitOfClaim(
//			@Observes @CDIEvent(GET_SUB_LIMIT_LIST) final ParameterDTO parameters) {
//
//		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters
//				.getPrimaryParameter();
//		List<SublimitFunObject> sublimitList =  new ArrayList<SublimitFunObject>();
//		
//		if(null != newIntimationDto.getPolicy().getProduct().getKey() )
//		{
//			/*sublimitList = dBCalculationService
//				.getClaimedAmountDetails(newIntimationDto.getPolicy().getProduct().getKey(),
//						newIntimationDto.getPolicy().getInsuredSumInsured(),
//						Integer.parseInt(newIntimationDto.getInsuredAge()));*/
//			Double insuredSumInsured = dBCalculationService.getInsuredSumInsured(newIntimationDto.getInsuredPatient().getInsuredId().toString(),newIntimationDto.getPolicy().getKey());
//			sublimitList = dBCalculationService
//					.getClaimedAmountDetailsForSection(newIntimationDto.getPolicy().getProduct().getKey(),
//							insuredSumInsured,
//							Double.parseDouble(newIntimationDto.getInsuredAge() != null && ! newIntimationDto.getInsuredAge().equals("") ? newIntimationDto.getInsuredAge() : "0"),0l,"0", (ReferenceTable.HOSP_SUB_COVER_CODE));
//			
//		}
//		System.out.println("================list size =======================:"+sublimitList.size());
//		view.setSublimt(sublimitList);
//	}

//	protected void getCurrencyMaster(
//			@Observes @CDIEvent(GET_CURRENCY_MASTER) final ParameterDTO parameters) {
//		BeanItemContainer<SelectValue> currencyMaster = masterService
//				.getSelectValueContainer(ReferenceTable.CURRENCY);
//
//		view.setCurrenceyMaster(currencyMaster);
//	}

	protected void getBalanceSumInsured(
			@Observes @CDIEvent(GET_BALANCE_SI) final ParameterDTO parameters) {
		
		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters.getPrimaryParameter();
		SelectValue eventCodeValue = newIntimationDto.getEventCodeValue();
		if(eventCodeValue!=null){
			String eventCode = eventCodeValue.getValue();
			//Double insuredSumInsured = dBCalculationService.getOMPInsuredSumInsured(newIntimationDto.getInsuredPatient().getKey().toString(),newIntimationDto.getPolicy().getKey());
			Map balanceSIMap = dBCalculationService.getOmpBalanceSI(
					newIntimationDto.getPolicy().getKey(),
					newIntimationDto.getInsuredPatient().getKey(), 0l, null,
					newIntimationDto.getInsuredPatient().getInsuredSumInsured(),  	//uncomment after testing all not rrc
					newIntimationDto.getKey(),eventCode);
			 Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
			/*dbCalculationService.getOmpBalanceSI(policyKey , reimbursement.getClaim().getIntimation().getInsured().getKey() , 
					reimbursement.getClaim().getKey(),sumInsured,reimbursement.getClaim().getIntimation().getKey(), eventCode);*/
			view.setBalanceSumInsued(balanceSI);
			System.out.println("balanceSI ======================================"+balanceSI);
			
		}
		
	}
		
	protected void getCpuObject(
			@Observes @CDIEvent(GET_CPU_OBJECT) final ParameterDTO parameters) {
		Long cpuKey = (Long) parameters.getPrimaryParameter();
		TmpCPUCode tmpCpuCode = policyService.getTmpCpuCode(cpuKey);
		view.setCpuObject(tmpCpuCode);
	}

	
	
	
	protected void getPreviousClaims(
			@Observes @CDIEvent(GET_PREVIOUS_CLAIMS) final ParameterDTO parameters) {
	//	List<OMPPreviousClaimTableDTO> previousClaimList = null;
		List<OMPPreviousClaimTableDTO> previousClaimList = null;
		if(null != parameters.getPrimaryParameter())
		{	
			NewIntimationDto newIntimationDto = (NewIntimationDto)parameters.getPrimaryParameter();
//			previousClaimList = claimService.getPreviousClaimForPolicy(newIntimationDto);
//			previousClaimList = preauthService.getPreviousClaimByForRegistration(newIntimationDto);
			previousClaimList = intimationService.getPreviousClaimByForRegistration(newIntimationDto);
			view.setPreviousClaims(previousClaimList);
		}
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
		
		OMPSearchClaimRegistrationTableDTO resultTask = (OMPSearchClaimRegistrationTableDTO) parameters.getPrimaryParameter();
		ClaimDto claimDto = (ClaimDto) parameters.getSecondaryParameters()[0];
		MastersValue master =null;
		if(claimDto.getClaimTypeBoolean()){
			master = masterService.getMaster(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
		}else{
			master = masterService.getMaster(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
		}
		
//		if (!StringUtils.equalsIgnoreCase("Network", claimDto
//				.getNewIntimationDto().getHospitalType()
//				.getValue())) {																		
//			master = masterService.getMaster(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
//		}

		// TODO Clarify for which type of claim this value to be set
		if (master != null) {
			claimDto.setConversionFlag(master.getValue().toLowerCase()
					.contains("cashless") ? 1l : 0l);
			SelectValue claimType = new SelectValue();
			claimType.setId(master.getKey());
			claimType.setValue(master.getValue());
			claimDto.setClaimType(claimType);
		}
		claimDto.setStageId(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		if(claimDto.getSuggestRejection()){				
			claimDto.setStatusId(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS);
		}
		else{
			//claimDto.setStatusId(ReferenceTable.CLAIM_REGISTERED_STATUS);
//			claimDto.setStatusId(ReferenceTable.CLAIM_REGISTERED_STATUS);
			claimDto.setStatusId(ReferenceTable.INTIMATION_REGISTERED_STATUS);
		}
		
		if(claimDto.getNewIntimationDto().getPolicy() != null){
			claimDto.setOfficeCode(claimDto.getNewIntimationDto().getPolicy().getHomeOfficeCode());	
		}		

		claimDto = ompclaimService.submitClaim(resultTask,claimDto);
		
		view.setClaimDetails(claimDto);
	}

	protected void registerClicked(
			@Observes @CDIEvent(CLICK_REGISTER_BUTTON) final ParameterDTO parameters) {
		view.registerClicked();
	}

	protected void suggestRejectionClicked(
			@Observes @CDIEvent(SUGGEST_REJECTION) final ParameterDTO parameters) {
		view.suggestRejectionClicked();
	}

	protected void cancelClaimRegistration(
			@Observes @CDIEvent(CANCEL_CLAIM_REGISTRATION) final ParameterDTO parameters) {
		view.cancelClaimRegistration();
	}
	
	protected void setClaimDetails(
			@Observes @CDIEvent(GET_CLAIM) final ParameterDTO parameters) {
		ClaimDto claimDto = (ClaimDto) parameters.getPrimaryParameter();
		OMPClaim claim = ompclaimService.getClaimByKey(claimDto.getKey());
		claimDto =OMPClaimMapper.getInstance().getClaimDto(claim);
		NewIntimationDto newIntimationDto = OMPNewIntimationMapper.getInstance()
				.getNewIntimationDto(claim.getIntimation());

		//Long tempPolicyKey = claim.getIntimation().getPolicy().getPolicySysId();
		Long policyKey = claim.getIntimation().getPolicy().getKey();
		//TmpPolicy tmpPolicy = policyService.getPolicyById(tempPolicyKey);
		Policy policy = policyService.getPolicyByKey(policyKey);

		//claimDto.getNewIntimationDto().setTmpPolicy(tmpPolicy);
		claimDto.getNewIntimationDto().setPolicy(policy);

		claimDto.setNewIntimationDto(newIntimationDto);

		List<Claim> previousClaims = claimService
				.getClaimsByPolicyNumber(newIntimationDto.getPolicy()
						.getPolicyNumber());
		List<PreviousClaimsTableDTO> previousClaimsDtoList = new ArrayList<PreviousClaimsTableDTO>();
		if (!previousClaims.isEmpty()) {
			for (Claim prevClaim : previousClaims) {
				if (prevClaim.getClaimId() == claimDto.getClaimId()) {
					previousClaims.remove(prevClaim);
				} else {
					PreviousClaimsTableDTO previousClaimsDto =  PreviousClaimMapper.getInstance()
							.getPreviousClaimsTableDTO(prevClaim);
				}
			}
		}

		view.setClaimDetails(claimDto);
	}
	


}
