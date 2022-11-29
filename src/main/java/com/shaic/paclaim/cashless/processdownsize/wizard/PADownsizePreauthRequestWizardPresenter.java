package com.shaic.paclaim.cashless.processdownsize.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.apache.commons.lang3.ArrayUtils;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.NegotiationDetails;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;


@ViewInterface(PADownsizePreauthRequestWizard.class)
public class PADownsizePreauthRequestWizardPresenter extends AbstractMVPPresenter<PADownsizePreauthRequestWizard> {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public static final String DOWNSIZE_STEP_CHANGE_EVENT = "pa_downsize_preauth_request_step_change_event";
	
	public static final String GET_EXCLUSION_DETAILS = "pa_downsize_preauth_request_get_exclusion_details";
	
	public static final String SUM_INSURED_CALCULATION="pa_Sum insured calculation for downsize preauth request ";
	
	public static final String SET_DOWNSIZE_AMOUNT="pa_Set downsize amount for downsize preauth request";
	
	public static final String DOWNSIZE_SUBMIT_EVENT="pa_submit for downsize preauth request";
	
	public static final String GET_HOSPITALIZATION_DETAILS = "pa_Downsize_preauth_request_get_hospitalization_details";
	
	public static final String BALANCE_SUM_INSURED = "pa_downsize_preauth_request_balance_sum_insured";
	
	public static final String VALIDATE_DOWNSIZE_PREAUTH_REQUEST_USER_RRC_REQUEST = "pa_downsize_preauth_request_user_rrc_request";
	
	public static final String DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "pa_downsize_preauth_request_load_rrc_request_drop_down_values";
	
	public static final String DOWNSIZE_PREAUTH_REQUEST_SAVE_RRC_REQUEST_VALUES = "pa_downsize_preauth_request_save_rrc_request_values";
	
	public static final String NEGOTIATION_CANCEL_OR_UPDATE = "pa_downsize_request_negotiation cancel";
	
	public static final String PA_DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "pa_downszie_preauth_request_load_rrc_request_sub_category_values";
	
	public static final String PA_DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_SOURCE_VALUES = "pa_downszie_preauth_request_load_rrc_request_source_values";
	 
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
			@Observes @CDIEvent(DOWNSIZE_SUBMIT_EVENT) final ParameterDTO parameters) throws Exception{
		
		
        PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
        
        Boolean isValid = true;
        
        if(preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() != null 
        		&& preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null
        		&& preauthDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
        	Double TotalApprovedAmount = preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt();
        	Double downsizeAmt = preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();
        	
        	if(TotalApprovedAmount <= downsizeAmt){
        		isValid = false;
        	}
        }
		if(isValid){
			String referenceNo = preauthDTO.getReferenceType();
			
			String[] splitString=referenceNo.split("/");
			
			Integer integerValue=new Integer(splitString[splitString.length-1]);
			integerValue=integerValue+1;
			
			splitString=ArrayUtils.remove(splitString, splitString.length-1);
			String newReferenceNo="";
	
			for (String string : splitString) {
				newReferenceNo=newReferenceNo+string+"/";
			}
			
			newReferenceNo=newReferenceNo+"00"+integerValue;
			
			
//			preauthDTO.getPreauthDataExtractionDetails().setReferenceNo(newReferenceNo);
//			preauthDTO.setKey(null);
	//		preauthDto.getPreauthDataExtractionDetails().setApprovedAmount(withdrawDTO.getMedicalDecisionDto().get(0).getApprovedAmount());
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
			
//			if(preauth.getStatus().getKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
				dbCalculationService.invokeAccumulatorProcedure(preauth.getKey());
//			}
			
	//		preauthService.setBPMOutcome(preauthDto, preauth, true);
		//	preauthService.setBPMOutComeForDownsizePreauthRequest(preauthDTO, preauth);
			preauthService.setDBOutComeForDownsizePreauthRequest(preauthDTO, preauth);
			if(preauth.getStatus().getKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
				view.buildSuccessLayout("Pre-auth has been downsized from Rs."+preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt()
						+ " to "+preauthDTO.getPreauthMedicalDecisionDetails().getDownsizedAmt());
			}else{
				view.buildSuccessLayout("Pre-auth has been escalated successfully !!!");
			}
		}else{
			view.showErrorMessage();
		}
		
		
		
	}
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_DOWNSIZE_PREAUTH_REQUEST_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(DOWNSIZE_PREAUTH_REQUEST_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	public void getHospitalizationDetails(
			@Observes @CDIEvent(GET_HOSPITALIZATION_DETAILS) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Hospitals hospitalById = hospitalService.getHospitalById(preauthDTO.getHospitalKey());
		PolicyDto policyDTO = preauthDTO.getPolicyDto();
		//String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
		Double sumInsured = 0d;
		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
		
		 sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			sumInsured = dbCalculationService.getGPAInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey());
		}
		 
		 if (sumInsured == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}
		
		Map<Integer, Object> hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
				sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,"0");
		
		referenceData.put("claimDBDetails", hospitalizationDetails);
		
		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null && preauthDTO.getPreauthDataExtractionDetails().getSection().getId() != null){
			
			hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
					sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getPreauthDataExtractionDetails().getSection().getId() ,0l,"A");
			
			referenceData.put("claimDBDetails", hospitalizationDetails);
		}
		referenceData.put("coPayType", masterService.getSelectValueContainer(ReferenceTable.JIO_COPAY_TYPE_VALUE));
		
		view.setHospitalizationDetails(hospitalizationDetails);
		
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
		Double balanceSI = dbCalculationService.getBalanceSIForPAHealth(intimationDTO.getInsuredPatient().getKey(), 0l,intimationDTO.getPolicy().getProduct().getKey(),ReferenceTable.HOSPITALIZATION_BENEFITS).get(SHAConstants.TOTAL_BALANCE_SI);
		List<Double> copayValue = dbCalculationService.getProductCoPay(intimationDTO.getPolicy().getProduct().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getInsuredPatient().getInsuredId(),intimationDTO);
		//Double balanceSI = new Double("9999");
		view.setBalanceSumInsured(balanceSI, copayValue);
	}
	
	public void getSumInsuredInfoFromDB(@Observes @CDIEvent(SUM_INSURED_CALCULATION) final ParameterDTO parameters) {
		 Map<String, Object> values = (Map<String, Object>) parameters.getPrimaryParameter();
			String diagnosis = null;
			PreauthDTO preauthDTO = (PreauthDTO) parameters.getSecondaryParameter(0, PreauthDTO.class);
			if(values.containsKey("diagnosisId")) {
				diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
			}
			
			Map<String, Object> medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(values,preauthDTO.getNewIntimationDTO());
			if(values.containsKey(SHAConstants.IS_NON_ALLOPATHIC) && (Boolean)values.get(SHAConstants.IS_NON_ALLOPATHIC)) {
				Long preauthKey = (Long)values.get("preauthKey");
				Map<String, Double> nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY),preauthKey,"C", (Long)values.get(SHAConstants.CLAIM_KEY));
				medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
				medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
			}
			
			view.setDiagnosisSumInsuredValuesFromDB(medicalDecisionTableValue, diagnosis);
	}
	
	public void activeStepChanged(@Observes @CDIEvent(DOWNSIZE_STEP_CHANGE_EVENT) final ParameterDTO parameters) {
		view.setWizardPageReferenceData(referenceData);
	}
	
	public void getExclusionDetails(@Observes @CDIEvent(GET_EXCLUSION_DETAILS) final ParameterDTO parameters)
	{
		Long impactDiagnosisKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<ExclusionDetails> icdCodeContainer = masterService.getExclusionDetailsByImpactKey(impactDiagnosisKey);
		
		view.setExclusionDetails(icdCodeContainer);
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	public void updateNegotiation(@Observes @CDIEvent(NEGOTIATION_CANCEL_OR_UPDATE) final ParameterDTO parameters)
	{		
		NegotiationDetails negotiation = (NegotiationDetails) parameters.getPrimaryParameter();
		preauthService.updateNegotiationDetails(negotiation);
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PA_DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PA_DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
}
