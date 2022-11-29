
package com.shaic.claim.negotiation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.NegotiationDetails;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(ProcessNegotiationWizard.class)
public class NegotiationPreauthRequestPresenter extends AbstractMVPPresenter<ProcessNegotiationWizard>{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NEGOTIATION_STEP_CHANGE_EVENT = "negotiation_preauth_request_step_change_event";
	
	public static final String GET_EXCLUSION_DETAILS = "negotiation_preauth_request_get_exclusion_details";
	
	public static final String NEGOTIATION_SUM_INSURED_CALCULATION="Sum insured calculation for negotiation preauth request ";
	
	public static final String SET_DOWNSIZE_AMOUNT="Set negotiation amount for negotiation preauth request";
	
	public static final String NEGOTIATION_SUBMIT_EVENT="submit for negotiation preauth request";
	
	public static final String GET_NEGOTIATION_HOSPITALIZATION_DETAILS = "Negotiation_Preauth_request_get_hospitalization_details";
	
	public static final String BALANCE_SUM_INSURED = "negotiation_preauth_request_balance_sum_insured";
	
	public static final String VALIDATE_NEGOTIATION_REQUEST_USER_RRC_REQUEST = "negotiation_user_rrc_request";
	
	public static final String NEGOTIATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "negotiation_load_rrc_request_drop_down_values";
	
	public static final String NEGOTIATION_PREAUTH_REQUEST_SAVE_RRC_REQUEST_VALUES = "negotiation_request_save_rrc_request_values";
	
	public static final String VIEW_NEG_CLAIMED_AMOUNT_DETAILS = "view_claimed_amount_details for negotiation";
	
	public static final String VIEW_NEG_BALANCE_SUM_INSURED_DETAILS = "view_Negotiation_Balance_Sum_Insured_details for negotiation";
	
	public static final String SETUP_REFERENCE_DATA = "Negotiation Medical Decision Reference Data";
	
	public static final String PREAUTH_APPROVED_DATE = "set preauth approved date in negotiation downsize letter";
	
	public static final String NEGOTIATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "negotiation_load_rrc_request_sub_category_values";

	public static final String NEGOTIATION_LOAD_RRC_REQUEST_SOURCE_VALUES = "negotiation_load_rrc_request_source_values";
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	@EJB
	private PEDValidationService pedValidationService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PreviousPreAuthService previousPreauthService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	public void setDownsizeData(
			@Observes @CDIEvent(SET_DOWNSIZE_AMOUNT) final ParameterDTO parameters) {
		
		Double downSizeAmount=(Double)parameters.getPrimaryParameter();
		
		view.setDownsizeAmount(downSizeAmount);
		
	}
	
	public void submitEventClick(
			@Observes @CDIEvent(NEGOTIATION_SUBMIT_EVENT) final ParameterDTO parameters)  throws Exception{
		
		
        PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
        
        Boolean isValid = true;
        
	        if(preauthDTO.getStatusKey() != null && preauthDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)) {
	        
	        if(preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() != null 
	        		&& preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null
	        		&& preauthDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
	        	Double TotalApprovedAmount = preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt();
	        	Double downsizeAmt = preauthDTO.getPreauthMedicalDecisionDetails().getDownsizedAmt();
	        	
	        	if(TotalApprovedAmount <= downsizeAmt){
	        		isValid = false;
	        	}
	        }
			if(isValid){
				/*String referenceNo = preauthDTO.getReferenceType();
				
				String[] splitString=referenceNo.split("/");
				
				Integer integerValue=new Integer(splitString[splitString.length-1]);
				integerValue=integerValue+1;
				
				splitString=ArrayUtils.remove(splitString, splitString.length-1);
				String newReferenceNo="";
		
				for (String string : splitString) {
					newReferenceNo=newReferenceNo+string+"/";
				}
				
				newReferenceNo=newReferenceNo+"00"+integerValue;
				
				
				preauthDTO.getPreauthDataExtractionDetails().setReferenceNo(newReferenceNo);*/
				
				//IMSSUPPOR-27038
				List<Preauth> preauthByClaimKeyWithClearCashless = preauthService
						.getPreauthByClaimKeyWithClearCashless(preauthDTO.getClaimKey());
				String referenceNo = preauthDTO.getClaimNumber() + "/001";
				if(!preauthByClaimKeyWithClearCashless.isEmpty()) {
					referenceNo = preauthDTO.getClaimNumber() + "/00" + String.valueOf(preauthByClaimKeyWithClearCashless.size() + 1);
				}
				
				preauthDTO.getPreauthDataExtractionDetails().setReferenceNo(
						referenceNo);
				
				preauthDTO.setKey(null);
				
				List<DiagnosisDetailsTableDTO> diagnosisTableList = preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList();
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisTableList) {
					diagnosisDetailsTableDTO.setKey(null);
				}
				
				List<ProcedureDTO> procedureExclusionCheckTableList = preauthDTO.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
				for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
					procedureDTO.setKey(null);
				}
				
				List<NoOfDaysCell> claimedDetailsList = preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsList();
				for (NoOfDaysCell noOfDaysCell : claimedDetailsList) {
					noOfDaysCell.setKey(null);
				}
				
				preauthDTO.setProcessType("D");
				
	//			MastersValue status = masterService.getMaster(preauthDTO.getStatusKey());
	//			
	//			if(status != null){
	//				preauthDTO.setStatusKey(status.getKey());
	//				preauthDTO.setStatusValue(status.getValue());
	//			}
				
				preauthDTO.setStageKey(ReferenceTable.DOWNSIZE_STAGE);
				
				if(preauthDTO.getPreviousPreauthKey() != null){
					dbCalculationService.reimbursementRollBackProc(preauthDTO.getPreviousPreauthKey(),"C");
				}
				
				Preauth preauth = preauthService.submitPreAuth(preauthDTO, true);
				
					dbCalculationService.invokeAccumulatorProcedure(preauth.getKey());
					
					//				R1135    -     13-04-2018
					if(preauthDTO.getPreauthMedicalDecisionDetails().getDownsizedAmt() < SHAConstants.ONE_LAKH){
						dbCalculationService.stopReminderProcessProcedure(preauth.getClaim().getIntimation().getIntimationId(),SHAConstants.PAN_CARD);
					}
				
	//			preauthService.setBPMOutComeForDownsizePreauthRequest(preauthDTO, preauth);
					
//				preauthService.setDBOutComeForDownsizePreauthRequest(preauthDTO, preauth);
				
				NegotiationDetails negDetails = preauthService.getNegotiationPending(preauthDTO.getClaimKey());
	        	if(negDetails != null){
	        		Status sts = new Status();
	        		sts.setKey(ReferenceTable.NEGOTIATION_AGREED);
	        		sts.setProcessValue(ReferenceTable.NEGOTIATION_AGREED_VALUE);
	        		negDetails.setStatusId(sts);
	        		negDetails.setHospitalType(preauthDTO.getNegotiationagreedFlagValue());
	        		negDetails.setNegotiateRemarksByHospital(preauthDTO.getAgreedWith());
	        		negDetails.setModifiedBy(preauthDTO.getStrUserName());
	        		preauthService.updateNegotiationDetails(negDetails);
	        	}
				if(preauth.getStatus().getKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
					view.buildSuccessLayout("Pre-auth has been Negotiated from Rs."+preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt()
							+ " to "+preauthDTO.getPreauthMedicalDecisionDetails().getDownsizedAmt());
					
					}
				}else{
					view.showErrorMessage();
				}
			
	        } else {
	        	
	        	NegotiationDetails negDetails = preauthService.getNegotiationPending(preauthDTO.getClaimKey());
	        	if(negDetails != null){
	        		Status sts = new Status();
	        		sts.setKey(ReferenceTable.NEGOTIATION_NOT_AGREED);
	        		sts.setProcessValue(ReferenceTable.NEGOTIATION_NOT_AGREED_VALUE);
	        		negDetails.setStatusId(sts);
	        		negDetails.setModifiedBy(preauthDTO.getStrUserName());
	        		negDetails.setHospitalType(preauthDTO.getNegotiationagreedFlagValue());
	        		negDetails.setNegotiateRemarksByHospital(preauthDTO.getRegotiateRemarks());
	        		preauthService.updateNegotiationDetails(negDetails);
	        	}
	        	view.buildSuccessLayout("Negotiation Details updated successfully");
	        }
		
		
	}
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_NEGOTIATION_REQUEST_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildNegotiationValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(NEGOTIATION_PREAUTH_REQUEST_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildNegotiationRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(NEGOTIATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadNegotiationRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	public void getNegotiationHospitalizationDetails(
			@Observes @CDIEvent(GET_NEGOTIATION_HOSPITALIZATION_DETAILS) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Hospitals hospitalById = hospitalService.getHospitalById(preauthDTO.getHospitalKey());
		PolicyDto policyDTO = preauthDTO.getPolicyDto();
		//String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
		Double sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		if (sumInsured == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}
		Map<Integer, Object> hospitalizationDetails = null;
		String policyPlan = preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";

		if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
				&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
				|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
				&& preauthDTO.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
        	policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
        }
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
			hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
					sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,"0");
		}else{
			hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
					sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,policyPlan);
		}
		
		referenceData.put("claimDBDetails", hospitalizationDetails);
		
		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null && preauthDTO.getPreauthDataExtractionDetails().getSection().getId() != null){
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
                        sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId() ,"A");
			}else{
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
                        sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId() ,"A");
			}
			
			referenceData.put("claimDBDetails", hospitalizationDetails);
		}
		
		referenceData.put("coPayType", masterService.getSelectValueContainer(ReferenceTable.JIO_COPAY_TYPE_VALUE));
		
		//CR R20181300
		referenceData.put("pedImpactOnDiagnosis", masterService.getSelectValueContainer(ReferenceTable.PED_IMPACT_ON_DIAGNOSI));
		referenceData.put("reasonForNotPaying", masterService.getSelectValueContainer(ReferenceTable.PED_NON_PAYABLE_REASON)); //EXCLUSION_DETAILS
		//CR R20181300
		
		view.setNegotiationHospitalizationDetails(hospitalizationDetails);
		
	}
	
	public void setBalanceSumInsured(@Observes @CDIEvent(BALANCE_SUM_INSURED) final ParameterDTO parameters) 
	{
		/**
		 * Since BalanceSI procedure requires insured key as one parameter,
		 * now intimation dto is passed instead of policy DTO and insured key is
		 * obtained from this new intimation dto.
		 * */
		NewIntimationDto intimationDTO = (NewIntimationDto) parameters.getPrimaryParameter();
	//	PolicyDto policyDTO = (PolicyDto) parameters.getPrimaryParameter();
		//Double balanceSI = dbCalculationService.getBalanceSI(policyDTO.getKey(), policyDTO.getTotalSumInsured());
		
		//Integer insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getPolicy().getKey(), intimationDTO.getInsuredPatient().getInsuredId().toString());
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getLopFlag());

		
		//Double balanceSI = dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getPolicy().getTotalSumInsured());
		//Double balanceSI = dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(), Double.valueOf(insuredSumInsured.toString()));
		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		if(ReferenceTable.getGMCProductList().containsKey(intimationDTO.getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(), 0l);
			copayValue = dbCalculationService.getProductCoPayForGMC(intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getKey());
		}else{
			balanceSI = dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(), 0l, insuredSumInsured,intimationDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			copayValue = dbCalculationService.getProductCoPay(intimationDTO.getPolicy().getProduct().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getInsuredPatient().getInsuredId(),intimationDTO);
		}

		
		//Double balanceSI = new Double("9999");
		view.setBalanceSumInsured(balanceSI, copayValue);
	}
	
	public void getSumInsuredInfoFromDB(@Observes @CDIEvent(NEGOTIATION_SUM_INSURED_CALCULATION) final ParameterDTO parameters) {
		 Map<String, Object> values = (Map<String, Object>) parameters.getPrimaryParameter();
		 PreauthDTO bean = (PreauthDTO) parameters.getSecondaryParameter(0, PreauthDTO.class);
			String diagnosis = null;
			if(values.containsKey("diagnosisId")) {
				diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
			}
			
			Map<String, Object> medicalDecisionTableValue = null;
			
			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValueForGMC(values,bean.getNewIntimationDTO());
			}else{
				medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(values,bean.getNewIntimationDTO());
			}
			
			if(values.containsKey(SHAConstants.IS_NON_ALLOPATHIC) && (Boolean)values.get(SHAConstants.IS_NON_ALLOPATHIC)) {
				Long preauthKey = (Long)values.get("preauthKey");
				Map<String, Double> nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY),preauthKey,"C", (Long)values.get(SHAConstants.CLAIM_KEY));
				medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
				medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
				
				//added for jira IMSSUPPOR-27044
				if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY)){
					medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, bean.getBalanceSI());
					medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, 0d);
				}
			}
			
			
			view.setDiagnosisSumInsuredValuesFromDB(medicalDecisionTableValue, diagnosis);
	}
	
	public void activeStepChanged(@Observes @CDIEvent(NEGOTIATION_STEP_CHANGE_EVENT) final ParameterDTO parameters) {
		view.setWizardPageReferenceData(referenceData);
	}
	
	public void getExclusionDetails(@Observes @CDIEvent(GET_EXCLUSION_DETAILS) final ParameterDTO parameters)
	{
		Long impactDiagnosisKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<ExclusionDetails> icdCodeContainer = masterService.getExclusionDetailsByImpactKey(impactDiagnosisKey);
		
		view.setExclusionDetails(icdCodeContainer);
	}
	
	public void viewClaimAmountDetails(
			@Observes @CDIEvent(VIEW_NEG_CLAIMED_AMOUNT_DETAILS) final ParameterDTO parameters) {
		view.viewClaimAmountDetails();
	}
	
	public void viewBalanceSumInsured(
			@Observes @CDIEvent(VIEW_NEG_BALANCE_SUM_INSURED_DETAILS) final ParameterDTO parameters) {
		String intimationId = (String) parameters.getPrimaryParameter();
		if(intimationId!=null && !intimationId.equals("")){
			view.viewBalanceSumInsured(intimationId);
		}
		
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void getNegotiationMedicalDecisionDetails(
			@Observes @CDIEvent(SETUP_REFERENCE_DATA) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
			
		//CR R20181300
		referenceData.put("pedImpactOnDiagnosis", masterService.getSelectValueContainer(ReferenceTable.PED_IMPACT_ON_DIAGNOSI));
		referenceData.put("reasonForNotPaying", masterService.getSelectValueContainer(ReferenceTable.PED_NON_PAYABLE_REASON)); //EXCLUSION_DETAILS
		//CR R20181300
		
		view.setReferenceDetailsForMedicalDecision(referenceData);
		
	}
	
    public void previousPreauthApporvedDate(@Observes @CDIEvent(PREAUTH_APPROVED_DATE) final ParameterDTO parameters) {
		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		Preauth previousPreauthList = preauthService.getPreviousPreauthList(preauthdto.getClaimDTO().getKey());
	    if(previousPreauthList != null){
	    	preauthdto.getClaimDTO().setPreauthApprovedDate(previousPreauthList.getModifiedDate());
	    }
	}
    
    public void setUpSubCategoryValues(
			@Observes @CDIEvent(NEGOTIATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(NEGOTIATION_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
