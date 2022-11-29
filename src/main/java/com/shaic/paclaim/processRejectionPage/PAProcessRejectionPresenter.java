package com.shaic.paclaim.processRejectionPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PAProcessRejectionView.class)
public class PAProcessRejectionPresenter extends AbstractMVPPresenter<PAProcessRejectionView> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String CONFIRM_PA_BUTTON="pa_Confirm Rejection Button";
	public static final String WAIVE_PA_BUTTON="pa_Cancel Rejection Button";
	public static final String SET_PA_DATA="pa_Bindind data to field";
	public static final String SUBMIT_PA_DATA="pa_submit data in process Rejection";
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private HospitalService hosptialService;
	
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_PROCESS_REJECTION_USER_RRC_REQUEST = "pa_process_rejection_user_rrc_request";
	
	public static final String PROCESS_REJECTION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "pa_process_rejection_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_REJECTION_SAVE_RRC_REQUEST_VALUES = "pa_process_rejection_save_rrc_request_values";

	public static final String PA_PROCESS_REJECTION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "pa_process_rejection_load_rrc_request_sub_category_values";
	
	public static final String PA_PROCESS_REJECTION_LOAD_RRC_REQUEST_SOURCE_VALUES = "pa_process_rejection_load_rrc_request_source_values";
	 
	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_PROCESS_REJECTION_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_REJECTION_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_REJECTION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	public void generateFieldsBasedOnEnhancementChange(@Observes @CDIEvent(CONFIRM_PA_BUTTON) final ParameterDTO parameters)
	{
		BeanItemContainer<SelectValue> rejectionCategory=masterService.getRejectionCategoryByValue();
		
		view.generateFieldBasedOnConfirmClick(rejectionCategory);
	}
	
	public void setReferenceData(@Observes @CDIEvent(PAProcessRejectionPresenter.SET_PA_DATA)final ParameterDTO parameters){
		
		SearchProcessRejectionTableDTO searchDTO=(SearchProcessRejectionTableDTO)parameters.getPrimaryParameter();
//		bean.setKey(27l);

		Long key=searchDTO.getKey();
		
		searchDTO.setProcessRejectionDTO(claimService.processRejectionByClaimKey(key));		
		
		if(searchDTO.getProcessRejectionDTO().getIsPremedical()){
			
			if(searchDTO.getProcessRejectionDTO().getPremedicalCategoryId() != null){
				String categoryValue=masterService.getCommonByValue(searchDTO.getProcessRejectionDTO().getPremedicalCategoryId());
				searchDTO.getProcessRejectionDTO().setPremedicalCategory(categoryValue);
			}
		}
		
		Intimation intimation= intimationService.getIntimationByKey(searchDTO.getProcessRejectionDTO().getIntimationKey());
		
		NewIntimationDto intimationDto=intimationService.getIntimationDto(intimation);
		searchDTO.setIntimationDTO(intimationDto);

		view.setReferenceData(searchDTO,intimationDto);
	}
	
	public void generateFieldsBasedOnCancel(@Observes @CDIEvent(WAIVE_PA_BUTTON) final ParameterDTO parameters)
	{
		view.generateFieldBasedOnWaiveClick();
	}
	
	public void saveData(@Observes @CDIEvent(PAProcessRejectionPresenter.SUBMIT_PA_DATA)final ParameterDTO parameters){
		
		ProcessRejectionDTO rejectionDto=(ProcessRejectionDTO)parameters.getPrimaryParameter();
		Boolean submitDescion=(Boolean)parameters.getSecondaryParameter(0, Boolean.class);
		String outCome=(String)parameters.getSecondaryParameter(1, String.class);
		SearchProcessRejectionTableDTO searchDTO=(SearchProcessRejectionTableDTO)parameters.getSecondaryParameter(2, SearchProcessRejectionTableDTO.class);
		NewIntimationDto intimationDTO = (NewIntimationDto) parameters.getSecondaryParameter(3, NewIntimationDto.class);
		Claim claimByIntimation = claimService.getClaimsByIntimationNumber(intimationDTO.getIntimationId());
		
		Boolean result=claimService.saveProcessRejection(rejectionDto,submitDescion,outCome,searchDTO);
//		Boolean result = true;
		if(result){

			Map<String,Object> outObj = (Map<String, Object>) searchDTO.getDbOutArray();
			
			//Map<String, String> preAuthValuesFromBPM = BPMClientContext.getMapFromPayload(humanTask.getPayload(), "PreAuthReq");
			Long preAuthkeyValue = null;
//			if(null != humanTask.getPayloadCashless())
//			{
//				PreAuthReqType preauthType = payloadBO.getPreAuthReq();
//				if(null != preauthType)
			if(null != outObj.get(SHAConstants.CASHLESS_KEY))
				{
					//preAuthkeyValue = Long.valueOf(preAuthValuesFromBPM.get("key"));
//					preAuthkeyValue = preauthType.getKey();
					preAuthkeyValue = (Long)outObj.get(SHAConstants.CASHLESS_KEY);
				}
			Preauth preauth = null;
			PreauthDTO preauthDTO = new PreauthDTO();
			
			if(preAuthkeyValue != null){
			
				preauth = preauthService.getPreauthById(preAuthkeyValue);
				if(preauth != null){
					Claim claimObj = preauth.getClaim();
					PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
//					PreMedicalMapper.getAllMapValues();
					
					preauthDTO = premedicalMapper.getPreauthDTO(preauth);
					
					claimObj = claimService.getClaimByKey(preauth.getClaim()
							.getKey());
					ClaimDto claimDto = ClaimMapper.getInstance().getClaimDto(claimObj);
					claimDto.setNewIntimationDto(intimationDTO);
					preauthDTO.setClaimDTO(claimDto);
					preauthDTO.setCreatedBy(rejectionDto.getUsername());
					preauthDTO.setStrUserName(rejectionDto.getUsername());
					setpreauthTOPreauthDTO(premedicalMapper, claimObj, preauth,
							preauthDTO, true);
					
					setDiagnosis(preauthDTO);
				}
			}
			

			if(intimationDTO.getKey() != null && preauth == null){
				Claim claimByKey = claimService.getClaimByKey((Long)outObj.get(SHAConstants.DB_CLAIM_KEY));
				if(claimByKey != null){
					ClaimDto claimDto = (new ClaimMapper()).getClaimDto(claimByKey);
					claimDto.setRejectionRemarks(rejectionDto.getConfirmRemarks());
					preauthDTO.setClaimDTO(claimDto);
				}				
				preauthDTO.setNewIntimationDTO(intimationDTO);
				preauthDTO.getClaimDTO().setNewIntimationDto(intimationDTO);
				preauthDTO.getPreauthDataExtractionDetails().setDiagnosis(preauthDTO.getPreauthDataExtractionDetails().getDiagnosis() == null ? intimationDTO.getReasonForAdmission() : preauthDTO.getPreauthDataExtractionDetails().getDiagnosis());
				preauthDTO.getPreauthDataExtractionDetails().setAdmissionDate(intimationDTO.getAdmissionDate());
				preauthDTO.getPreauthDataExtractionDetails().setRoomCategory(intimationDTO.getRoomCategory());
			}
			preauthDTO.getPreMedicalPreauthMedicalDecisionDetails().setRejectionRemarks(rejectionDto.getConfirmRemarks());
			preauthDTO.setCreatedBy(rejectionDto.getUsername());
			preauthDTO.setStrUserName(rejectionDto.getUsername());
			
//			view.savedResult();	
			if(outCome.equalsIgnoreCase("REJECT")){
				view.openPdfFileInWindow(claimByIntimation,preauthDTO);
			}else{
				view.savedResult();
			}
		}
	}

	private void setDiagnosis(PreauthDTO preauthDTO) {
		if(preauthDTO.getPreauthDataExtractionDetails() != null && preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList() != null){
			List<DiagnosisDetailsTableDTO> diagnosisList = preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList();
			String diagnosis = "";
			if(!diagnosisList.isEmpty()){
			for(DiagnosisDetailsTableDTO diagnosisDto : diagnosisList){
				
				if(diagnosis.equals("")){
					diagnosis = diagnosisDto.getDiagnosisName().getValue();
				}
				else{
				diagnosis += " / " + ( diagnosisDto.getDiagnosisName() != null ? diagnosisDto.getDiagnosisName().getValue() : " / " ) ;
				}
			}
			}
			if(!diagnosis.equals("")){
				diagnosis = StringUtils.removeEnd(diagnosis, "/");
				preauthDTO.getPreauthDataExtractionDetails().setDiagnosis(diagnosis);
			}
		}
	}
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList) {
		try {
			Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
			List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
			if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
				for (ViewTmpClaim viewTmpClaim : previousPolicyPreviousClaims) {
					if(!generatedList.contains(viewTmpClaim)) {
						generatedList.add(viewTmpClaim);
					}
				}
			}
			if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
				getPreviousClaimForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList);
			} else {
				return generatedList;
			}
			
			
		} catch(Exception e) {
			
		}
		return generatedList;
	}
	
	
	
	public void setpreauthTOPreauthDTO(PreMedicalMapper premedicalMapper,
			Claim claimByKey, Preauth previousPreauth, PreauthDTO preauthDTO,
			Boolean isEnabled) {
		if (claimByKey != null) {
			setClaimValuesToDTO(preauthDTO, claimByKey);
			NewIntimationDto newIntimationDto = intimationService
					.getIntimationDto(claimByKey.getIntimation());
			ClaimDto claimDTO = ClaimMapper.getInstance().getClaimDto(claimByKey);
			preauthDTO.setNewIntimationDTO(newIntimationDto);
			preauthDTO.setClaimDTO(claimDTO);
		}
		/*String policyNumber = preauthDTO.getPolicyDto().getPolicyNumber();
		List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
				.getViewTmpClaimsByPolicyNumber(policyNumber);
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		previousclaimsList.addAll(claimsByPolicyNumber);
		
		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);*/


//		List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
//				.getPreviousClaims(claimsByPolicyNumber,
//						claimByKey.getClaimId(), pedValidationService,
//						masterService);
		
		/*List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousclaimsList,
						claimByKey.getClaimId());*/
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(claimByKey.getKey(), claimByKey.getIntimation().getPolicy().getKey(), 
				claimByKey.getIntimation().getInsured().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);

		// List<PreviousClaimsTableDTO> previousClaimDTOList = new
		// PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);

		preauthDTO.setPreviousClaimsList(previousClaimDTOList);

		if (previousPreauth.getCoordinatorFlag().equalsIgnoreCase("y")) {

			CoordinatorDTO coordinatorDTO = premedicalMapper
					.getCoordinatorDTO(preauthService
							.findCoordinatorByClaimKey(previousPreauth
									.getClaim().getKey()));
			coordinatorDTO.setRefertoCoordinator(true);
			preauthDTO.setCoordinatorDetails(coordinatorDTO);
		}

		List<SpecialityDTO> specialityDTOList = premedicalMapper
				.getSpecialityDTOList(preauthService
						.findSpecialityByClaimKey(previousPreauth.getClaim()
								.getKey()));
		for (SpecialityDTO specialityDTO : specialityDTOList) {
			specialityDTO.setEnableOrDisable(isEnabled);
		}
		preauthDTO.getPreauthDataExtractionDetails().setSpecialityList(
				specialityDTOList);

	

		List<PedValidation> findPedValidationByPreauthKey = preauthService
				.findPedValidationByPreauthKey(previousPreauth.getKey());
		List<DiagnosisDetailsTableDTO> newPedValidationTableListDto = premedicalMapper
				.getNewPedValidationTableListDto(findPedValidationByPreauthKey);

		// Fix for issue 732 starts.
		//DBCalculationService dbCalculationService = new DBCalculationService();

		Double insuredSumInsured = 0d;
		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO.getPolicyDto()
						.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					preauthDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), preauthDTO.getPolicyDto()
							.getKey());
		}
		
		Double balanceSI = 0d;
		
		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){

			balanceSI = dbCalculationService.getBalanceSI(
				preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
				preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
				preauthDTO.getClaimKey(), insuredSumInsured,preauthDTO.getNewIntimationDTO().getKey()).get(
				SHAConstants.TOTAL_BALANCE_SI);
		}
		else
		{
			balanceSI = dbCalculationService.getGPABalanceSI(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey(), insuredSumInsured,preauthDTO.getNewIntimationDTO().getKey()).get(
					SHAConstants.TOTAL_BALANCE_SI);
		}
		List<Double> copayValue = dbCalculationService.getProductCoPay(
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey(), preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),preauthDTO.getNewIntimationDTO());

		if (preauthDTO.getNewIntimationDTO() != null) {
			Product product = preauthDTO.getNewIntimationDTO().getPolicy()
					.getProduct();
			if (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey())) {
				// BalanceSumInsuredDTO claimsOutstandingAmt =
				// dbCalculationService.getClaimsOutstandingAmt(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
				// preauthDTO.getNewIntimationDTO().getIntimationId(),
				// insuredSumInsured);
				preauthDTO.setSettledAmount(0d);
				preauthDTO
						.setDeductibleAmount((preauthDTO.getNewIntimationDTO()
								.getInsuredPatient() != null && preauthDTO
								.getNewIntimationDTO().getInsuredPatient()
								.getDeductibleAmount() != null) ? preauthDTO
								.getNewIntimationDTO().getInsuredPatient()
								.getDeductibleAmount() : 0d);
			}
		}

		preauthDTO.setBalanceSI(balanceSI);
		preauthDTO.setProductCopay(copayValue);

		if (preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()
				.equals(ReferenceTable.SUPER_SURPLUS_INDIVIDUAL)
				|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey().equals(ReferenceTable.SUPER_SURPLUS_FLOATER)) {
			// preauthDTO.setSpecificProductDeductibleDTO(getPreviousClaimsSuperSurplusTable(claimsByPolicyNumber
			// ,claimByKey.getClaimId(), preauthDTO));
		}

		Map<Long, SublimitFunObject> sublimitFunObjMap = getSublimitFunObjMap(
				preauthDTO.getPolicyDto().getProduct().getKey(),
				insuredSumInsured, preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredAge(),preauthDTO);
		// Fix for issue 732 ends
		
		
		List<ProcedureDTO> procedureMainDTOList = premedicalMapper
				.getProcedureMainDTOList(preauthService
						.findProcedureByPreauthKey(previousPreauth.getKey()));
		for (ProcedureDTO procedureDTO : procedureMainDTOList) {
			procedureDTO.setEnableOrDisable(isEnabled);
			if (procedureDTO.getSublimitName() != null) {
				SublimitFunObject objSublimitFun = sublimitFunObjMap
						.get(procedureDTO
								.getSublimitName().getLimitId());
				if(objSublimitFun != null) {
					procedureDTO.setSublimitName(objSublimitFun);
					procedureDTO.setSublimitDesc(objSublimitFun.getDescription());
					procedureDTO.setSublimitAmount(String.valueOf(objSublimitFun.getAmount().intValue()));
				}
				
			}
		}

		preauthDTO.getPreauthMedicalProcessingDetails()
				.setProcedureExclusionCheckTableList(procedureMainDTOList);

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : newPedValidationTableListDto) {
			pedValidationTableDTO.setEnableOrDisable(isEnabled);
			if (pedValidationTableDTO.getDiagnosisName() != null) {
				String diagnosis = masterService
						.getDiagnosis(pedValidationTableDTO.getDiagnosisName()
								.getId());
				pedValidationTableDTO.setDiagnosis(diagnosis);
				pedValidationTableDTO.getDiagnosisName().setValue(diagnosis);
			}

			if (pedValidationTableDTO.getSublimitName() != null) {
				// Fix for issue 732 starts.
				SublimitFunObject objSublimitFun = sublimitFunObjMap
						.get(pedValidationTableDTO.getSublimitName()
								.getLimitId());
				// ClaimLimit limit =
				// claimService.getClaimLimitByKey(pedValidationTableDTO.getSublimitName().getLimitId());
				if (objSublimitFun != null) {
					pedValidationTableDTO.setSublimitName(objSublimitFun);
					pedValidationTableDTO.setSublimitAmt(String
							.valueOf(objSublimitFun.getAmount()));
				}
				// Fix for issue 732 ends
			}

			if (pedValidationTableDTO.getSumInsuredRestriction() != null) {
				MastersValue master = masterService
						.getMaster(pedValidationTableDTO
								.getSumInsuredRestriction().getId());
				pedValidationTableDTO.getSumInsuredRestriction().setValue(
						master.getValue());

			}
			List<DiagnosisPED> pedDiagnosisByPEDValidationKey = preauthService
					.getPEDDiagnosisByPEDValidationKey(pedValidationTableDTO
							.getKey());
			List<PedDetailsTableDTO> dtoList = new ArrayList<PedDetailsTableDTO>();
			for (DiagnosisPED diagnosisPED : pedDiagnosisByPEDValidationKey) {
				PedDetailsTableDTO dto = new PedDetailsTableDTO();
				// Added for disabling the procedure that is coming from
				// preauth.
				dto.setEnableOrDisable(isEnabled);
				dto.setDiagnosisName(pedValidationTableDTO.getDiagnosis());
				dto.setPolicyAgeing(pedValidationTableDTO.getPolicyAgeing());
				dto.setKey(diagnosisPED.getKey());
				dto.setPedCode(diagnosisPED.getPedCode());
				dto.setPedName(diagnosisPED.getPedName());

				if (diagnosisPED.getDiagonsisImpact() != null) {
					SelectValue value = new SelectValue();
					value.setId(diagnosisPED.getDiagonsisImpact().getKey());
					value.setValue(diagnosisPED.getDiagonsisImpact().getValue());
					dto.setPedExclusionImpactOnDiagnosis(value);
				}

				if (diagnosisPED.getExclusionDetails() != null) {
					SelectValue exclusionValue = new SelectValue();
					exclusionValue.setId(diagnosisPED.getExclusionDetails()
							.getKey());
					exclusionValue.setValue(diagnosisPED.getExclusionDetails()
							.getExclusion());
					dto.setExclusionDetails(exclusionValue);
				}

				dto.setRemarks(diagnosisPED.getDiagnosisRemarks());
				dtoList.add(dto);
			}
			pedValidationTableDTO.setPedList(dtoList);
		}

		// TODO: Need to change this behaviour..
		preauthDTO.getPreauthDataExtractionDetails().setDiagnosisTableList(
				newPedValidationTableListDto);

		// preauthDTO.getPreauthMedicalProcessingDetails().setPedValidationTableList(newPedValidationTableListDto);

		List<ClaimAmountDetails> findClaimAmountDetailsByPreauthKey = preauthService
				.findClaimAmountDetailsByPreauthKey(previousPreauth.getKey());
		preauthDTO
				.getPreauthDataExtractionDetails()
				.setClaimedDetailsList(
						premedicalMapper
								.getClaimedAmountDetailsDTOList(findClaimAmountDetailsByPreauthKey));
		Integer sumInsured = preauthService.getSumInsured(preauthDTO
				.getPolicyDto().getProduct().getKey(),
				(insuredSumInsured == 0) ? preauthDTO.getPolicyDto()
						.getTotalSumInsured() : insuredSumInsured);
		preauthDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);
		String strAutoRestorationFlg = preauthDTO.getNewIntimationDTO()
				.getPolicy().getProduct().getAutoRestorationFlag();

		if (("Y").equalsIgnoreCase(strAutoRestorationFlg)) {
			if (sumInsured != null && sumInsured.intValue() > 0) {
				preauthDTO.getPreauthDataExtractionDetails()
						.setAutoRestoration(
								SHAConstants.AUTO_RESTORATION_NOTDONE);
			} else if (null != sumInsured && 0 == sumInsured.intValue()) {
				preauthDTO.getPreauthDataExtractionDetails()
						.setAutoRestoration(SHAConstants.AUTO_RESTORATION_DONE);
			}
		} else {
			preauthDTO.getPreauthDataExtractionDetails().setAutoRestoration(
					SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE);
		}
	}
	
	private void setClaimValuesToDTO(PreauthDTO preauthDTO, Claim claimByKey) {
		PolicyDto policyDto = new PolicyMapper().getPolicyDto(claimByKey
				.getIntimation().getPolicy());
		preauthDTO.setHospitalKey(claimByKey.getIntimation().getHospital());
		Long hospital = claimByKey.getIntimation().getHospital();
		Hospitals hospitalById = hosptialService.getHospitalById(hospital);
		preauthDTO.setHospitalCode(hospitalById.getHospitalCode());
		preauthDTO.setClaimNumber(claimByKey.getClaimId());
		preauthDTO.setPolicyDto(policyDto);
		preauthDTO.setDateOfAdmission(claimByKey.getIntimation()
				.getAdmissionDate());
		preauthDTO.setReasonForAdmission(claimByKey.getIntimation()
				.getAdmissionReason());
		preauthDTO.setIntimationKey(claimByKey.getIntimation().getKey());
		preauthDTO
				.setPolicyKey(claimByKey.getIntimation().getPolicy().getKey());
		preauthDTO.setClaimKey(claimByKey.getKey());
	}
	
	private Map<Long, SublimitFunObject> getSublimitFunObjMap(Long productKey,
			Double insuredSumInsured, Double insuredAge,PreauthDTO preauthDTO) {
		
		Preauth preauth = null;
		
		if(preauthDTO.getKey() != null){
			List<Preauth> preauthList = preauthService.getPreauthByKey(preauthDTO.getKey());
			if(preauthList != null && ! preauthList.isEmpty()){
				preauth = preauthList.get(0);
				}
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<Long, SublimitFunObject> sublimitFunMap = new HashMap<Long, SublimitFunObject>();
		
		List<SublimitFunObject> sublimitList = new ArrayList<SublimitFunObject>();
		
		if(preauth != null && preauth.getSectionCategory() != null){
			
			sublimitList = dbCalculationService
					.getClaimedAmountDetailsForSection(productKey, insuredSumInsured, 0l,
							insuredAge,preauth.getSectionCategory(),preauthDTO.getPolicyDto().getPolicyPlan() != null ? preauthDTO.getPolicyDto().getPolicyPlan() : "0",null,preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
		}else{
			sublimitList = dbCalculationService
					.getClaimedAmountDetailsForSection(productKey, insuredSumInsured, 0l,
							insuredAge,0l, "0",null,preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
		}
		
		if (null != sublimitList && !sublimitList.isEmpty()) {
			for (SublimitFunObject sublimitFunObj : sublimitList) {
				sublimitFunMap.put(sublimitFunObj.getLimitId(), sublimitFunObj);
			}
		}
		return sublimitFunMap;

	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PA_PROCESS_REJECTION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PA_PROCESS_REJECTION_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
