package com.shaic.claim.allowghiregistration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.intimation.CashLessTableDetails;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.intimation.ViewIntimationStatus;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.domain.CashlessDetailsService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MASClaimAdvancedProvision;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.domain.PreviousClaimMapper;
import com.shaic.newcode.wizard.dto.ClaimStatusDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
@ViewInterface(GhiAllowClaimRegistrationView.class)
public class GhiAllowClaimRegistrationPresenter extends
		AbstractMVPPresenter<GhiAllowClaimRegistrationView> {

	public static final String SUBMIT_GHI_ALLOW_CLAIM_CLICK = "submit_ghi_allow_register_claim";
	public static final String CLICK_GHI_ALLOW_REGISTER_BUTTON = "click_register_ghi_allow_button";
	public static final String SUGGEST_GHI_ALLOW_REJECTION = "Rejection_ghi_allow_Suggested";
	public static final String SUGGEST_REJECTION_ALLOW_GHI_VALUE_CHANGE = "Suggest Rejection Allow Ghi Value Changed";
	public static final String CANCEL_CLAIM_REGISTRATION_GHI_ALLOW = "cancel_claim_registration_allow_ghi";
	public static final String CLICK_GENERATE_COVERING_LETTER_GHI_ALLOW_BUTTON = "Click_Allow_GHI_Generate_Covering_Letter";
//	public static final String CLICK_HOMEPAGE_OR_REG_ANOTHER_CLAIM = "click_homepage_or_reg_another_claim";
	public static final String GET_GHI_ALLOW_CLAIM = "get_ghi_allow_claim_details";
	public static final String SUBMIT_GHI_ALLOW_CLAIM_REGISTRATION = "submit_ghi_allow_registration_of_claim";
	public static final String GET_CURRENCY_MASTER_GHI_ALLOW = "get_GHI_ALLOW_currency_master";
	public static final String GET_GHI_ALLOW_PREVIOUS_CLAIMS = "get_ghi_allow_previous_claims";
	public static final String GET_BALANCE_SI_GHI_ALLOW = "get_ghi_allow_balance_sumInsured_GHI_Allow";
	public static final String GET_GHI_ALLOW_CPU_OBJECT = "get_ghi_allow_cpu_object";
	public static final String GET_GHI_ALLOW_SUB_LIMIT_LIST = "get_ghi_allow_sub_limit_list";
	public static final String CLICK_GHI_ALLOW_VIEW_CLAIM_STATUS = "click_ghi_allow_view_claim_status";

	@EJB
	private PolicyService policyService;

	@EJB
	private ClaimService claimService;
	
	@EJB
	private PreauthService preauthService;

	@EJB
	private IntimationService intimationService;
	
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
	
	public static final String VALIDATE_GHI_ALLOW_CLAIM_REGISTRATION_USER_RRC_REQUEST = "ghi_allow_validate_claim_registration_user_rrc_request";
	
	public static final String PROCESS_GHI_ALLOW_CLAIM_REGISTRATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "ghi_allow_claim_registration_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_GHI_ALLOW_CLAIM_REGISTRATION_SAVE_RRC_REQUEST_VALUES = "ghi_allow_claim_registration_save_rrc_request_values";
	
	public static final String GET_GHI_ALLOW_GMC_CORP_BUFFER = "get_ghi_allow_gmc_corp_buffer";
	
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	protected void viewPreviousClaimStatus(
			@Observes @CDIEvent(CLICK_GHI_ALLOW_VIEW_CLAIM_STATUS) final ParameterDTO parameters) {
		
		PreviousClaimsTableDTO previousClaimDato = (PreviousClaimsTableDTO) parameters.getPrimaryParameter(); 
		
		Intimation intimation = intimationService.getIntimationByNo(previousClaimDato.getIntimationNumber());
		
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
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_GHI_ALLOW_CLAIM_REGISTRATION_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_GHI_ALLOW_CLAIM_REGISTRATION_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_GHI_ALLOW_CLAIM_REGISTRATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */

	protected void getSublimitOfClaim(
			@Observes @CDIEvent(GET_GHI_ALLOW_SUB_LIMIT_LIST) final ParameterDTO parameters) {

		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters
				.getPrimaryParameter();
		List<SublimitFunObject> sublimitList =  new ArrayList<SublimitFunObject>();
		
		if(null != newIntimationDto.getPolicy().getProduct().getKey() )
		{
			/*sublimitList = dBCalculationService
				.getClaimedAmountDetails(newIntimationDto.getPolicy().getProduct().getKey(),
						newIntimationDto.getPolicy().getInsuredSumInsured(),
						Integer.parseInt(newIntimationDto.getInsuredAge()));*/
			Double insuredSumInsured = dBCalculationService.getInsuredSumInsured(newIntimationDto.getInsuredPatient().getInsuredId().toString(),newIntimationDto.getPolicy().getKey(),newIntimationDto.getInsuredPatient().getLopFlag());
			if(ReferenceTable.getSuperSurplusKeys().containsKey(newIntimationDto.getPolicy().getProduct().getKey())){
				String plan1 = newIntimationDto.getPolicy().getPolicyPlan() != null ? newIntimationDto.getPolicy().getPolicyPlan() : "0";
				sublimitList = dBCalculationService
						.getClaimedAmountDetailsForSection(newIntimationDto.getPolicy().getProduct().getKey(),
								insuredSumInsured, 0l,
								Double.parseDouble(newIntimationDto.getInsuredAge() != null && ! newIntimationDto.getInsuredAge().equals("") ? newIntimationDto.getInsuredAge() : "0"),0l,plan1, (ReferenceTable.HOSP_SUB_COVER_CODE),newIntimationDto.getInsuredPatient().getInsuredId());
			}
			else {
			sublimitList = dBCalculationService
					.getClaimedAmountDetailsForSection(newIntimationDto.getPolicy().getProduct().getKey(),
							insuredSumInsured, 0l,
							Double.parseDouble(newIntimationDto.getInsuredAge() != null && ! newIntimationDto.getInsuredAge().equals("") ? newIntimationDto.getInsuredAge() : "0"),0l,"0", (ReferenceTable.HOSP_SUB_COVER_CODE),newIntimationDto.getInsuredPatient().getInsuredId());
			}
		}
		System.out.println("================list size =======================:"+sublimitList.size());
		view.setSublimt(sublimitList);
	}

	protected void getCurrencyMaster(
			@Observes @CDIEvent(GET_CURRENCY_MASTER_GHI_ALLOW) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> currencyMaster = masterService
				.getSelectValueContainer(ReferenceTable.CURRENCY);

		view.setCurrenceyMaster(currencyMaster);
	}

	protected void getBalanceSumInsured(
			@Observes @CDIEvent(GET_BALANCE_SI_GHI_ALLOW) final ParameterDTO parameters) {
		
		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters.getPrimaryParameter();
		
		Double insuredSumInsured = dBCalculationService.getInsuredSumInsured(newIntimationDto.getInsuredPatient().getKey().toString(),newIntimationDto.getPolicy().getKey(),newIntimationDto.getInsuredPatient().getLopFlag());
		Double balanceSI = 0d;
		if(ReferenceTable.getGMCProductList().containsKey(newIntimationDto.getPolicy().getProduct().getKey())){
			balanceSI = dBCalculationService.getBalanceSI(newIntimationDto.getPolicy().getKey(),newIntimationDto.getInsuredPatient().getKey(), 0l, insuredSumInsured,newIntimationDto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
		}else{
			balanceSI = dBCalculationService.getBalanceSI(newIntimationDto.getPolicy().getKey(),newIntimationDto.getInsuredPatient().getKey(), 0l, insuredSumInsured,newIntimationDto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
		}
		
		
		System.out.println("balanceSI ======================================"+balanceSI);
		view.setBalanceSumInsued(balanceSI);
	}
		
	protected void getCpuObject(
			@Observes @CDIEvent(GET_GHI_ALLOW_CPU_OBJECT) final ParameterDTO parameters) {
		Long cpuKey = (Long) parameters.getPrimaryParameter();
		TmpCPUCode tmpCpuCode = policyService.getTmpCpuCode(cpuKey);
		view.setCpuObject(tmpCpuCode);
	}

	
	
	
	protected void getPreviousClaims(
			@Observes @CDIEvent(GET_GHI_ALLOW_PREVIOUS_CLAIMS) final ParameterDTO parameters) {
		List<PreviousClaimsTableDTO> previousClaimList = null;
		if(null != parameters.getPrimaryParameter())
		{	
			NewIntimationDto newIntimationDto = (NewIntimationDto)parameters.getPrimaryParameter();
//			previousClaimList = claimService.getPreviousClaimForPolicy(newIntimationDto);
			previousClaimList = preauthService.getPreviousClaimByForRegistration(newIntimationDto);
			view.setPreviousClaims(previousClaimList);
		}
	}

	protected void submitClaimRegister(
			@Observes @CDIEvent(SUBMIT_GHI_ALLOW_CLAIM_CLICK) final ParameterDTO parameters) {
		ClaimDto claimDto = (ClaimDto) parameters.getPrimaryParameter();
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		Boolean isProceedFurther = true;
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(claimDto.getNewIntimationDto().getPolicy().getPolicyNumber(), claimDto.getNewIntimationDto().getIntimationId());
			if(get64vbStatus != null && (SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
				isProceedFurther = false;
			}
		}
		view.submitClaimRegister(claimDto, isProceedFurther);
	}

	protected void submitClaimRegistration(
			@Observes @CDIEvent(SUBMIT_GHI_ALLOW_CLAIM_REGISTRATION) final ParameterDTO parameters) {
		
		SearchClaimRegistrationTableDto resultTask = (SearchClaimRegistrationTableDto) parameters.getPrimaryParameter();
		ClaimDto claimDto = (ClaimDto) parameters.getSecondaryParameters()[0];
		
		MastersValue master = masterService.getMaster(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
		if (!StringUtils.equalsIgnoreCase("Network", claimDto
				.getNewIntimationDto().getHospitalType()
				.getValue())) {
			master = masterService.getMaster(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
		}

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
			//claimDto.setStatusId(ReferenceTable.CLAIM_REGISTERED_STATUS);
			claimDto.setStatusId(ReferenceTable.INTIMATION_REGISTERED_STATUS);
		}
		
		if(claimDto.getNewIntimationDto().getPolicy() != null){
			claimDto.setOfficeCode(claimDto.getNewIntimationDto().getPolicy().getHomeOfficeCode());	
		}	
		
		/**
		 * Home Office based Advance provisioning done only for Health Claims Hence below code is removed from submit service
		 * 
		 */

		Policy policyByKey = claimService.getPolicyByKey(claimDto.getNewIntimationDto().getPolicy().getKey());
		
		MASClaimAdvancedProvision claimAdvProvision = claimService.getClaimAdvProvision(Long.valueOf(policyByKey.getHomeOfficeCode()));
		
		
//		Double amt =  claimAdvProvision.getAvgAmt();
		
		NewIntimationDto newIntimationDto = claimDto.getNewIntimationDto();
		
		Double amt = claimService.calculateAmtBasedOnBalanceSI(newIntimationDto.getPolicy().getKey(),newIntimationDto.getInsuredPatient().getInsuredId(),newIntimationDto.getInsuredPatient().getKey()
				, claimAdvProvision != null && claimAdvProvision.getAvgAmt() != null ? claimAdvProvision.getAvgAmt() : 0d, claimDto.getNewIntimationDto().getKey(),newIntimationDto.getInsuredPatient().getLopFlag());
		
		claimDto.setGhiAllowFlag(SHAConstants.YES_FLAG);
		claimDto.setGhiAllowUser(resultTask.getUsername());
		claimDto.setProvisionAmount(amt);
		claimDto.setClaimedAmount(amt);
		claimDto.setCurrentProvisionAmount(amt);
		claimDto.setProcessClaimType(SHAConstants.HEALTH_TYPE);
		claimDto.setRegistrationRemarks(SHAConstants.CLAIM_MANUAL_REGISTRATION + ", " + (claimDto.getRegistrationRemarks() != null ? claimDto.getRegistrationRemarks() :"" ));
		claimDto = claimService.submitClaim(resultTask,claimDto);
		
		view.setClaimDetails(claimDto);
	}

	protected void registerClicked(
			@Observes @CDIEvent(CLICK_GHI_ALLOW_REGISTER_BUTTON) final ParameterDTO parameters) {
		view.registerClicked();
	}

	protected void suggestRejectionClicked(
			@Observes @CDIEvent(SUGGEST_REJECTION_ALLOW_GHI_VALUE_CHANGE) final ParameterDTO parameters) {
		view.suggestRejectionClicked();
	}

	protected void cancelClaimRegistration(
			@Observes @CDIEvent(CANCEL_CLAIM_REGISTRATION_GHI_ALLOW) final ParameterDTO parameters) {
		view.cancelClaimRegistration();
	}
	
	protected void setClaimDetails(
			@Observes @CDIEvent(GET_GHI_ALLOW_CLAIM) final ParameterDTO parameters) {
		ClaimDto claimDto = (ClaimDto) parameters.getPrimaryParameter();
		Claim claim = claimService.getClaimByKey(claimDto.getKey());
		claimDto =ClaimMapper.getInstance().getClaimDto(claim);
		NewIntimationDto newIntimationDto = NewIntimationMapper.getInstance()
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
	
	
	protected void getGmcCorpBuffer(
			@Observes @CDIEvent(GET_GHI_ALLOW_GMC_CORP_BUFFER) final ParameterDTO parameters) {
		
		Double bufferSI = 0d;
		Double utilisedAmt = 0d;
		
		ClaimDto claimDto = (ClaimDto) parameters.getPrimaryParameter();
		Intimation intimation = intimationService.getIntimationByKey(claimDto.getNewIntimationDto().getKey());
		Long mainNo = 0L;
		if(intimation.getInsured().getDependentRiskId() !=null){
			mainNo = intimation.getInsured().getDependentRiskId();
		}else{
			mainNo = intimation.getInsured().getInsuredId();
		}

		Map<String, Double> values = dBCalculationService.getGmcCorpBufferASIForRegister(SHAConstants.PRC_BUFFERTYPE_CB,claimDto.getNewIntimationDto().getPolicy().getPolicyNumber(),intimation.getInsured().getKey(),mainNo,0l);

		if(values != null && !values.isEmpty()){
			if(values.get(SHAConstants.LN_POLICY_BUFFER_SI) != null){
				bufferSI = values.get(SHAConstants.LN_POLICY_BUFFER_SI);
			}
			if(values.get(SHAConstants.LN_BUFFER_UTILISED_AMT) != null){
				utilisedAmt = values.get(SHAConstants.LN_BUFFER_UTILISED_AMT);
			}
		}
		view.setCorpBufferTab(bufferSI, utilisedAmt);
		
	}

}
