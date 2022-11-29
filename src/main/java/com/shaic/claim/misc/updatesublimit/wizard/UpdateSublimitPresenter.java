package com.shaic.claim.misc.updatesublimit.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.icdSublimitMapping.IcdSubLimitMappingService;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(UpdateSublimitWizard.class)
public class UpdateSublimitPresenter extends AbstractMVPPresenter<UpdateSublimitWizard>{
	
	public static final String BILLING_PROCESS_SET_UP_REFERENCE_FOR_SUBLIMIT = "setUpreferencedata";
	
	public static final String SUM_INSURED_CALCULATION_FOR_SUBLIMIT = "sublimitValuesformedicaldecision";
	
	public static final String EDIT_SUBLIMIT_VALUES_FOR_UPDATE_SUBLIMIT = "editsublimitvalues";
	
	public static final String SUBMIT_EVENT = "submitsublimit";
	
	@EJB
	private IcdSubLimitMappingService icdSublimitMapService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private PreauthService preauthService;
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	
	public void setUpReference(
			@Observes @CDIEvent(BILLING_PROCESS_SET_UP_REFERENCE_FOR_SUBLIMIT) final ParameterDTO parameters) {
		
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		
		Double insuredAge = dto.getNewIntimationDTO().getInsuredPatient().getInsuredAge();
		Double sumInsured = dbCalculationService.getInsuredSumInsured(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), dto.getPolicyDto().getKey(),dto.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		if (sumInsured == 0) {
			sumInsured = dto.getPolicyDto().getTotalSumInsured();
		}
		
		String policyPlan = dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";
		
		/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())) {*/
		if(dto.getNewIntimationDTO().getPolicy().getProduct() != null 
				&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode())  ||
						SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()) )
						|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()))
				|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					&& dto.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
			policyPlan = dto.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? dto.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
		}
		if(ReferenceTable.getGMCProductList().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(dto.getNewIntimationDTO().getPolicy().getKey(),
					dto.getNewIntimationDTO().getInsuredPatient().getKey(),dto.getNewIntimationDTO().getPolicy().getSectionCode());
			referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSectionForGMC(dto.getPolicyDto().getKey(), sumInsured, insuredAge,0l,"0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode())));
		}else{
			referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSection(dto.getPolicyDto().getProduct().getKey(), sumInsured, 0l, insuredAge,0l,policyPlan, (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode()),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
		}
		referenceData.put("coPayType", masterService.getSelectValueContainer(ReferenceTable.JIO_COPAY_TYPE_VALUE));
		
		view.setupReferences(referenceData);
	}
	
	public void getValuesForMedicalDecisionTable(
			@Observes @CDIEvent(SUM_INSURED_CALCULATION_FOR_SUBLIMIT) final ParameterDTO parameters){
	 	Map<String, Object> values = (Map<String, Object>) parameters.getPrimaryParameter();
	 	DiagnosisProcedureTableDTO dto = (DiagnosisProcedureTableDTO) parameters.getSecondaryParameters()[0];
	 	
	 	PreauthDTO preauthDto = (PreauthDTO) parameters.getSecondaryParameter(1, PreauthDTO.class);
	 	
		String diagnosis = null;
		if(values.containsKey("diagnosisId")) {
			diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
		}
		
		if (dto.getDiagnosisDetailsDTO() != null) {
			dto.getDiagnosisDetailsDTO()
					.setDiagnosis(diagnosis);
		}
		
		//Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimDTO().getKey(), preauthDto.getKey());
		Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRodForMA(preauthDto.getClaimDTO().getKey(), preauthDto.getKey(),preauthDto.getRodNumber());
		Boolean prePostReimbursement = reimbursementService.isPrePostReimbursement(preauthDto.getKey());
//		
//		if(!prePostReimbursement){
//			values.put("preauthKey",preauthDto.getKey());
//		}else{
//			if(hospitalizationRod != null){
//				values.put("preauthKey", hospitalizationRod.getKey());
//			}else{
//				values.put("preauthKey", preauthDto.getKey());
//			}
//		}
		
//		if(preauthDto.getIsHospitalizationRepeat() && !preauthDto.getIsReferToMedicalApprover()) {
//			values.put("preauthKey", 0l);
//		} else {
//			values.put("preauthKey", preauthDto.getKey());
//		}
		values.put("preauthKey", preauthDto.getKey());
		
		if(!prePostReimbursement){
			values.put("preauthKey",preauthDto.getKey());
			}else{
				if(hospitalizationRod != null){
					values.put("preauthKey", hospitalizationRod.getKey());
				}else{
					values.put("preauthKey", preauthDto.getKey());
				}
		}
		
		if(preauthDto.getIsHospitalizationRepeat() && !preauthDto.getIsReferToMedicalApprover()) {
			values.put("preauthKey", preauthDto.getKey());
		}
		
		if(preauthDto.getIsDirectToFinancial()) {
			values.put("preauthKey", preauthDto.getClaimDTO().getLatestPreauthKey() != null ? preauthDto.getClaimDTO().getLatestPreauthKey() :preauthDto.getKey() );
		}
		
		Map<String, Object> medicalDecisionTableValues = null;
		
		values.put("productKey", preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey());
		
		if(ReferenceTable.getGMCProductList().containsKey(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValueForGMC(values,preauthDto.getNewIntimationDTO());
		}else{
			medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValue(values,preauthDto.getNewIntimationDTO());
		}
		
		if(values.containsKey(SHAConstants.IS_NON_ALLOPATHIC) && (Boolean)values.get(SHAConstants.IS_NON_ALLOPATHIC)) {
			
			Map<String, Double> nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
					,(Long)values.get("preauthKey"),"R", (Long)values.get(SHAConstants.CLAIM_KEY));
			
			Preauth latestPreauthByClaim = preauthService.getLatestPreauthByClaim(preauthDto.getClaimDTO().getKey());
			
			if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
					&& ! prePostReimbursement && latestPreauthByClaim != null && hospitalizationRod == null && preauthDto.getIsDirectToFinancial()){
				
				values.put("preauthKey", latestPreauthByClaim.getKey());
				
				nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
						,(Long)values.get("preauthKey"),"C", (Long)values.get(SHAConstants.CLAIM_KEY));
			}
			
			medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
			medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
			
			//added for jira IMSSUPPOR-27044
			if(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY)){
				medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, preauthDto.getBalanceSI());
				medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, 0d);
			}
		}

		view.getValuesForMedicalDecisionTable(dto, medicalDecisionTableValues);
	}
	
	public void editSublimtValuesForMedicalDescionValues(
			@Observes @CDIEvent(EDIT_SUBLIMIT_VALUES_FOR_UPDATE_SUBLIMIT) final ParameterDTO parameters) {

	 	Map<String, Object> values = (Map<String, Object>) parameters.getPrimaryParameter();
	 	DiagnosisProcedureTableDTO dto = (DiagnosisProcedureTableDTO) parameters.getSecondaryParameters()[0];
	 	
	 	PreauthDTO preauthDto = (PreauthDTO) parameters.getSecondaryParameter(1, PreauthDTO.class);
		String diagnosis = null;
		if(values.containsKey("diagnosisId")) {
			diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
		}
		
		if (dto.getDiagnosisDetailsDTO() != null) {
			dto.getDiagnosisDetailsDTO()
					.setDiagnosis(diagnosis);
		}
		
		//Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimDTO().getKey(), preauthDto.getKey());
		Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRodForMA(preauthDto.getClaimDTO().getKey(), preauthDto.getKey(),preauthDto.getRodNumber());
////		if(hospitalizationRod != null){
////			values.put("preauthKey", preauthDto.getKey());
////		}else{
//		
		Boolean prePostReimbursement = reimbursementService.isPrePostReimbursement(preauthDto.getKey());
//		
//		
//		/// PRevious utilizaion codes ... TODO: needs to be removed...
//		if(!prePostReimbursement){
//			values.put("preauthKey",preauthDto.getKey());
//		}else{
//			if(hospitalizationRod != null){
//				values.put("preauthKey", hospitalizationRod.getKey());
//			}else{
//				values.put("preauthKey", preauthDto.getKey());
//			}
//		}

//		}

		if(preauthDto.getIsHospitalizationRepeat() && !preauthDto.getIsReferToMedicalApprover()) {
			values.put("preauthKey",  preauthDto.getKey());
		} else {
			values.put("preauthKey", preauthDto.getKey());
		}
		
		if(!prePostReimbursement){
			values.put("preauthKey",preauthDto.getKey());
			}else{
				if(hospitalizationRod != null){
					values.put("preauthKey", hospitalizationRod.getKey());
				}else{
					values.put("preauthKey", preauthDto.getKey());
				}
		}
		
		if(preauthDto.getIsHospitalizationRepeat() && !preauthDto.getIsReferToMedicalApprover()) {
			values.put("preauthKey", preauthDto.getKey());
			/*if(hospitalizationRod != null){
				values.put("preauthKey", hospitalizationRod.getKey());
			}else{
				values.put("preauthKey", preauthDto.getKey());
			}*/
		} 
		
		
		
		if(preauthDto.getIsDirectToBilling() && hospitalizationRod == null) {
			values.put("preauthKey", preauthDto.getClaimDTO().getLatestPreauthKey() != null ? preauthDto.getClaimDTO().getLatestPreauthKey() :preauthDto.getKey() );
		}else if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && hospitalizationRod == null){
			Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(preauthDto.getKey());
			if(reimbursementByKey.getMedicalCompletedDate() == null){
				values.put("preauthKey", preauthDto.getClaimDTO().getLatestPreauthKey() != null ? preauthDto.getClaimDTO().getLatestPreauthKey() :preauthDto.getKey());
				preauthDto.setIsDirectToBilling(true);
			}

		}
		
		if(dto.getDiagnosisDetailsDTO() != null && dto.getDiagnosisDetailsDTO().getKey() != null){
			PedValidation diagnosisByKey = reimbursementService.getDiagnosisByKey(dto.getDiagnosisDetailsDTO().getKey());
			if(diagnosisByKey.getSublimitId() == null){
				values.put("preauthKey", 0l);
			}
		}else if(dto.getProcedureDTO() != null && dto.getProcedureDTO().getKey() != null){
			Procedure procedure = reimbursementService.getProcedureKey(dto.getProcedureDTO().getKey());
			if(procedure.getSublimitNameId() == null){
				values.put("preauthKey", 0l);
			}
		}
		
		Map<String, Object> medicalDecisionTableValues = null;
		
		values.put("productKey", preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey());
		
		if(dto.getIsDiagnosisSublimitChanged() != null && dto.getIsDiagnosisSublimitChanged()){
			values.put("preauthKey", 0l);
		}
		
		if(ReferenceTable.getGMCProductList().containsKey(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValueForGMC(values,preauthDto.getNewIntimationDTO());
		}else{
			medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValue(values,preauthDto.getNewIntimationDTO());
		}
		
		if(values.containsKey(SHAConstants.IS_NON_ALLOPATHIC) && (Boolean)values.get(SHAConstants.IS_NON_ALLOPATHIC)) {
			
			Map<String, Double> nonAllopathicAmount = null;
			
			Preauth latestPreauthByClaim = preauthService.getLatestPreauthByClaim(preauthDto.getClaimDTO().getKey());
			
			if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
					&& ! prePostReimbursement && latestPreauthByClaim != null && hospitalizationRod == null && preauthDto.getIsDirectToBilling()){
				
				values.put("preauthKey", latestPreauthByClaim.getKey());
				
				nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
						,(Long)values.get("preauthKey"),"C", (Long)values.get(SHAConstants.CLAIM_KEY));
			}else{
				
//				if(hospitalizationRod != null){
//					nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
//							,hospitalizationRod.getKey(),"R", (Long)values.get(SHAConstants.CLAIM_KEY));
//				}else{
				if(ReferenceTable.getFHORevisedKeys().equals(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
							,0l,"0", (Long)values.get(SHAConstants.CLAIM_KEY));
				}else{
					nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
							,(Long)values.get("preauthKey"),"R", (Long)values.get(SHAConstants.CLAIM_KEY));
				}
				
				
					
//				}
				
			}
			
			
			medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
			medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
			
			   //added for jira IMSSUPPOR-27044
            if(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY)){
            	medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, preauthDto.getBalanceSI());
            	medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, 0d);
            }
		}
		
		
		/**
		 *  CR R1136 - start
		 */
		if(dto.getDiagnosisDetailsDTO() != null && dto.getDiagnosisDetailsDTO().getSublimitName() != null){
			SublimitFunObject sublimitObj = icdSublimitMapService.getSublimitDetailsBasedOnIcdCode(dto.getDiagnosisDetailsDTO().getIcdCodeKey());
			if(dto.getDiagnosisDetailsDTO().getSublimitApplicable() != null && 
					dto.getDiagnosisDetailsDTO().getSublimitApplicable().getId().equals(ReferenceTable.COMMONMASTER_YES) && 
					sublimitObj != null && 
					dto.getDiagnosisDetailsDTO().getSublimitName() != null &&
					sublimitObj.getName().equalsIgnoreCase(dto.getDiagnosisDetailsDTO().getSublimitName().getName())){
				
				dto.getDiagnosisDetailsDTO().setSublimitMapAvailable(Boolean.TRUE);
			}
		}
	
		/**
		 *  CR R1136 - end
		 */
		
		view.editSublimitValues(dto, medicalDecisionTableValues);

	}
	
	public void updateSublmit(
			@Observes @CDIEvent(SUBMIT_EVENT) final ParameterDTO parameters) {
		PreauthDTO reimbursementDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String userName = (String) parameters.getSecondaryParameter(0, String.class);
		List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();

		try {
			reimbursementService.updateSublimitValues(reimbursementDTO,userName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		view.buildSuccessLayout();
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
