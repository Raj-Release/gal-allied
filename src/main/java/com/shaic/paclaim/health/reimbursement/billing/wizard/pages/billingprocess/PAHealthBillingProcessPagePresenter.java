package com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billingprocess;

import java.util.Date;
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
import com.shaic.arch.SHAUtils;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Hospitalisation;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(PAHealthBillingProcessPageWizard.class)
public class PAHealthBillingProcessPagePresenter extends AbstractMVPPresenter<PAHealthBillingProcessPageWizard>{
	private static final long serialVersionUID = 7488192193097057694L;
	

	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private CreateRODService createRodService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	public static final String SUM_INSURED_CALCULATION = "pa_health_billing_process_row_by_row_calculation";

	public static final String GET_PREAUTH_REQUESTED_AMOUT = "pa_health_billing_process_preauth_requested_amount";


	public static final String BILLING_PROCESS_SET_UP_REFERENCE = "pa_health_billing_process_set_up_reference";


	public static final String POST_HOSPITALIZATION_AMOUNT = "pa_health_billing_post_hospitalisation_calculation";


	public static final String GET_CLAIM_RESTRICTION = "pa_health_billing_get_health_Care_claim_restriction";
	
	public static final String RECHARGE_SI_FOR_PRODUCT = "pa_health_recharging SI for product in Process claim billing";

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	
	@SuppressWarnings("unchecked")
	public void getValuesForMedicalDecisionTable(
			@Observes @CDIEvent(SUM_INSURED_CALCULATION) final ParameterDTO parameters) {
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
			
			Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimDTO().getKey(), preauthDto.getKey());
////			if(hospitalizationRod != null){
////				values.put("preauthKey", preauthDto.getKey());
////			}else{
//			
			Boolean prePostReimbursement = reimbursementService.isPrePostReimbursement(preauthDto.getKey());
//			
//			
//			/// PRevious utilizaion codes ... TODO: needs to be removed...
//			if(!prePostReimbursement){
//				values.put("preauthKey",preauthDto.getKey());
//			}else{
//				if(hospitalizationRod != null){
//					values.put("preauthKey", hospitalizationRod.getKey());
//				}else{
//					values.put("preauthKey", preauthDto.getKey());
//				}
//			}

//			}
			if(preauthDto.getIsHospitalizationRepeat() && !preauthDto.getIsReferToMedicalApprover()) {
				values.put("preauthKey",  preauthDto.getKey());
			} else {
				values.put("preauthKey", preauthDto.getKey());
			}
			
			if(preauthDto.getIsDirectToBilling() && hospitalizationRod == null) {
				values.put("preauthKey", preauthDto.getClaimDTO().getLatestPreauthKey() != null ? preauthDto.getClaimDTO().getLatestPreauthKey() :preauthDto.getKey() );
			}
			Map<String, Object> medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValue(values,preauthDto.getNewIntimationDTO());
			if(values.containsKey(SHAConstants.IS_NON_ALLOPATHIC) && (Boolean)values.get(SHAConstants.IS_NON_ALLOPATHIC)) {
				
				Map<String, Double> nonAllopathicAmount = null;
				
				Preauth latestPreauthByClaim = preauthService.getLatestPreauthByClaim(preauthDto.getClaimDTO().getKey());
				
				if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
						&& ! prePostReimbursement && latestPreauthByClaim != null && hospitalizationRod == null && preauthDto.getIsDirectToBilling()){
					
					values.put("preauthKey", latestPreauthByClaim.getKey());
					
					nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
							,(Long)values.get("preauthKey"),"C", (Long)values.get(SHAConstants.CLAIM_KEY));
				}else{
					
//					if(hospitalizationRod != null){
//						nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
//								,hospitalizationRod.getKey(),"R", (Long)values.get(SHAConstants.CLAIM_KEY));
//					}else{
						nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
								,(Long)values.get("preauthKey"),"R", (Long)values.get(SHAConstants.CLAIM_KEY));
//					}
					
				}
				
				
				medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
				medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
			}
			
			view.getValuesForMedicalDecisionTable(dto, medicalDecisionTableValues);
	}
	
	public void setPreAuthRequestedAmount (@Observes @CDIEvent(GET_PREAUTH_REQUESTED_AMOUT) final ParameterDTO parameters)
	{
		PreviousPreAuthTableDTO dto = (PreviousPreAuthTableDTO)parameters.getPrimaryParameter();
		view.setPreAuthRequestedAmt(preauthService.getPreauthReqAmt(dto.getKey(), dto.getClaimKey()));
	}
	
	protected Reimbursement getHospitalizationROD(Long claimKey) {
		Reimbursement previousROD = null;
		List<Reimbursement> previousRODByClaimKey = reimbursementService
				.getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()
				&& previousRODByClaimKey.size() > 1) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey()) && reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")) {
					previousROD = reimbursement;
					break;
				}
			}
		}

		return previousROD;
	}
	
	protected Reimbursement getPartialHospitalizationROD(Long claimKey) {
		Reimbursement previousROD = null;
		Integer previousNumber = 1;
		List<Reimbursement> previousRODByClaimKey = reimbursementService
				.getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()
				&& previousRODByClaimKey.size() > 1) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey()) && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")) {
					previousROD = reimbursement;
					break;
				}
			}
		}
		return previousROD;
	}
	
	protected Double getPostHospitalizationRODValues(Long claimKey, Long rodKey) {
		Reimbursement previousROD = null;
		Integer previousNumber = 1;
		Double postHospAmt = 0d;
		
		List<Reimbursement> previousRODByClaimKey = reimbursementService
				.getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				
				if(reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag().toLowerCase().equalsIgnoreCase("y") && !reimbursement.getKey().equals(rodKey) && !SHAUtils.getRejectStatusMap().containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())  && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())) {
					ReimbursementCalCulationDetails reimbursementCalcByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(reimbursement.getKey(), ReferenceTable.POST_HOSPITALIZATION);
					if(reimbursementCalcByRodAndClassificationKey != null && ReferenceTable.getFinancialApprovalStatus().containsKey(reimbursement.getStatus().getKey())) {
						if(reimbursement.getOtherInsurerApplicableFlag() != null && reimbursement.getOtherInsurerApplicableFlag().equalsIgnoreCase("Y")) {
							postHospAmt += reimbursementCalcByRodAndClassificationKey.getTpaPayableAmt();
						} else {
							postHospAmt += reimbursementCalcByRodAndClassificationKey.getPayableInsuredAfterPremium();
						}
						
					}
				}
			}
		}
		return postHospAmt;
	}
	
	protected Double getPreHospitalizationRODValues(Long claimKey, Long rodKey) {
		Reimbursement previousROD = null;
		Integer previousNumber = 1;
		Double postHospAmt = 0d;
		
		List<Reimbursement> previousRODByClaimKey = reimbursementService
				.getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				
				if(reimbursement.getDocAcknowLedgement().getPreHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPreHospitalisationFlag().toLowerCase().equalsIgnoreCase("y") && !reimbursement.getKey().equals(rodKey) && !SHAUtils.getRejectStatusMap().containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())  && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())) {
					ReimbursementCalCulationDetails reimbursementCalcByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(reimbursement.getKey(), ReferenceTable.PRE_HOSPITALIZATION);
					if(reimbursementCalcByRodAndClassificationKey != null) {
						if(reimbursement.getOtherInsurerApplicableFlag() != null && reimbursement.getOtherInsurerApplicableFlag().equalsIgnoreCase("Y")) {
							postHospAmt += reimbursementCalcByRodAndClassificationKey.getTpaPayableAmt();
						} else {
							postHospAmt += reimbursementCalcByRodAndClassificationKey.getPayableToInsured();
						}
						
					}
				}
			}
		}
		return postHospAmt;
	}
	
	protected Reimbursement getFirstROD(Long claimKey) {
		Reimbursement previousROD = null;
		Reimbursement firstROD = null;
		List<Reimbursement> previousRODByClaimKey = reimbursementService
				.getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()
				&& previousRODByClaimKey.size() > 1) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				String[] split = reimbursement.getRodNumber().split("/");
				String splitNo = split[split.length - 1];
				Integer rodNo = Integer.valueOf(splitNo);
				if(reimbursement.getStatus() != null  && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())  && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())) {
					previousROD = reimbursement;
					break;
				} 
			}
		}

		return previousROD;
	}
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	public void setUpReference(
			@Observes @CDIEvent(BILLING_PROCESS_SET_UP_REFERENCE) final ParameterDTO parameters) {
		referenceData.put("fileType", masterService
				.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		referenceData.put("billClassification", masterService.getMasBillClassificationValues());
		view.setupReferences(referenceData);
	}
	
	
	public void getClaimRestrictionAmount(
			@Observes @CDIEvent(GET_CLAIM_RESTRICTION) final ParameterDTO parameters) {
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
//		Double originalSiValue = dbCalculationService.getInsuredSumInsured(
//				String.valueOf(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()), dto.getNewIntimationDTO().getPolicy().getKey());
//		Double halfAppAmt = dto.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()/2;
		Double availableNonNetworkSI = dbCalculationService.getAvailableNonNetworkSI(dto.getNewIntimationDTO().getKey(), dto.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? dto.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0, dto.getKey());
//		Double min = Math.min(halfAppAmt, availableNonNetworkSI);
//		Long claimRestriction = dto.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().longValue() - availableNonNetworkSI.longValue();
		dto.setClaimRestrictionAmount(availableNonNetworkSI.longValue());
		view.setClaimRestrictionAmount(availableNonNetworkSI.longValue());
	}
	
	public void setPostHospAmt(
			@Observes @CDIEvent(POST_HOSPITALIZATION_AMOUNT) final ParameterDTO parameters) {
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		Long reduceAmt = 0l;
		Long rodKey = 0l;
		Date doa = null;
		Date dod = null;
		Integer diagnosisList = 1;
		Integer procedureList = 0;
		Boolean isRestrictionSIAvail = false;
		String[] split = dto.getRodNumber().split("/");
		String splitNo = split[split.length - 1];
		Integer rodNo = Integer.valueOf(splitNo);
		Boolean isZeroPostHospAmt = false;
		Integer siRestrictedAmount = 0;
		Integer balanceSIAftHosp = 0;
		Integer amount = 0;
		Double previousPostHospAmt = 0d;
		Boolean isPartialHosp = false;
		Reimbursement partialHospitalization = null;
		Boolean isTwoRod = false;
		Double previousPreHospitalizationRODValues = 0d;
		if(dto.getPreHospitalizaionFlag()) {
			previousPreHospitalizationRODValues = getPreHospitalizationRODValues(dto.getClaimKey(), dto.getKey());
		}
		
		Map<String, Double> postHospitalizationValues = new HashMap<String, Double>();
		if(rodNo == 1 && (dto.getHospitalizaionFlag() || dto.getPartialHospitalizaionFlag() || dto.getPostHospitalizaionFlag() || dto.getPreHospitalizaionFlag())) {
			if(dto.getClaimDTO().getClaimType() != null
					&& dto.getClaimDTO().getClaimType().getId()
							.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && !dto.getPartialHospitalizaionFlag()) {
				isZeroPostHospAmt = true;
			}
			
			if(dto.getClaimDTO().getClaimType() != null
					&& dto.getClaimDTO().getClaimType().getId()
							.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && !dto.getPartialHospitalizaionFlag() && !dto.getHospitalizaionFlag() && (dto.getPreHospitalizaionFlag() || dto.getPostHospitalizaionFlag())) {
				isZeroPostHospAmt = false;
				Preauth latestPreauthByClaim = preauthService.getLatestPreauthByClaim(dto.getClaimKey());
				if(latestPreauthByClaim != null) {
					Double postHospAmt = 0d;
					Double roomRentAndICUAmt = 0d;
					Boolean isRoomRentApplied = false;
					Boolean isAmbulanceApplied = false;
					List<PedValidation> findPedValidationByPreauthKey = preauthService.findPedValidationByPreauthKey(latestPreauthByClaim.getKey());
					List<Procedure> findProcedureByPreauthKey = preauthService.findProcedureByPreauthKey(latestPreauthByClaim.getKey());
					List<ClaimAmountDetails> claimAmountDetailsByPreauth = preauthService.getClaimAmountDetailsByPreauth(latestPreauthByClaim.getKey());
					for (ClaimAmountDetails claimAmountDetails : claimAmountDetailsByPreauth) {
						if((claimAmountDetails.getBenefitId().equals(9l) || claimAmountDetails.getBenefitId().equals(8l)) && claimAmountDetails.getPaybleAmount() != null && claimAmountDetails.getPaybleAmount() > 0f ) {
							isRoomRentApplied = true;
							roomRentAndICUAmt += claimAmountDetails.getPaybleAmount() != null ? claimAmountDetails.getPaybleAmount().doubleValue() : 0d;
						} else if((claimAmountDetails.getBenefitId().equals(15l) && claimAmountDetails.getPaybleAmount() != null && claimAmountDetails.getPaybleAmount() > 0f )) {
							isAmbulanceApplied = true;
							roomRentAndICUAmt += claimAmountDetails.getPaybleAmount() != null ? claimAmountDetails.getPaybleAmount().doubleValue() : 0d;
						}
					}
					if(!isRoomRentApplied) {
						Long daysBetweenDate = SHAUtils.getDaysBetweenDate(latestPreauthByClaim.getDataOfAdmission(), latestPreauthByClaim.getDateOfDischarge());
						if(daysBetweenDate.equals(0l)) {
							daysBetweenDate = 1l;
						}
						
						Double insuredSumInsured = 0d;
						if(null != dto.getNewIntimationDTO() && null != dto.getNewIntimationDTO().getPolicy() && 
								null != dto.getNewIntimationDTO().getPolicy().getProduct() && 
								null != dto.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
								!(ReferenceTable.getGPAProducts().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
						
							insuredSumInsured = dbCalculationService.getInsuredSumInsured(
								dto.getNewIntimationDTO().getInsuredPatient()
										.getInsuredId().toString(), dto
										.getPolicyDto().getKey(),dto.getNewIntimationDTO().getInsuredPatient().getLopFlag());
						}
						else
						{
							insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
									dto.getNewIntimationDTO().getInsuredPatient()
											.getInsuredId().toString(), dto
											.getPolicyDto().getKey());
						}
					
						
						Map<Integer, Object> detailsMap = dbCalculationService
								.getHospitalizationDetails(dto.getPolicyDto()
										.getProduct().getKey(), insuredSumInsured,
										dto.getNewIntimationDTO().getHospitalDto()
												.getRegistedHospitals().getCityClass(),
												dto.getNewIntimationDTO()
												.getInsuredPatient().getInsuredId(), dto.getNewIntimationDTO().getKey(),0l,"0");
						Long reducedAmt = daysBetweenDate * (detailsMap.get(8) != null ? ((Double)detailsMap.get(8)).longValue() : 0l);
						roomRentAndICUAmt += reducedAmt.doubleValue();
					}
					ResidualAmount residualAmtByPreauthKey = preauthService.getResidualAmtByPreauthKey(latestPreauthByClaim.getKey());
					
					Map<String, Object> objMap = new HashMap<String, Object>();
					objMap.put("insuredId", latestPreauthByClaim.getClaim().getIntimation().getInsured().getInsuredId());
					objMap.put("preauthKey", dto.getKey());
					for (PedValidation pedValidation : findPedValidationByPreauthKey) {
						objMap.put("restrictedSI", pedValidation.getSumInsuredRestrictionId() != null ? pedValidation.getSumInsuredRestrictionId() : 0l);
						objMap.put("sublimitId", pedValidation.getSublimitId() != null ? pedValidation.getSublimitId() : 0l);
						objMap.put("diagOrProcId", pedValidation.getDiagnosisId() != null ? pedValidation.getDiagnosisId() : 0l);
						objMap.put("referenceFlag", "D");
						objMap.put(SHAConstants.CLAIM_KEY, latestPreauthByClaim.getClaim().getKey());
						Map<String, Object> medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(objMap,dto.getNewIntimationDTO());
						Double doubleValue = (Double) medicalDecisionTableValue.get("SLAvailAmt");
						Double amt = pedValidation.getAmountConsideredAmount()  + (pedValidation.getAmbulanceChargeFlag() != null && pedValidation.getAmbulanceChargeFlag().equalsIgnoreCase("Y") ? pedValidation.getAmbulanceCharges() : 0d );
						if(pedValidation.getSublimitId() != null) {
							amt = Math.min(doubleValue, amt);
						}
						
						postHospAmt += amt;
					}
					for (Procedure procedure : findProcedureByPreauthKey) {
						objMap.put("restrictedSI",  0l);
						objMap.put("sublimitId", procedure.getSublimitNameId() != null ? procedure.getSublimitNameId() : 0l);
						objMap.put("diagOrProcId", procedure.getProcedureID() != null ? procedure.getProcedureID() : 0l);
						objMap.put("referenceFlag", "D");
						objMap.put(SHAConstants.CLAIM_KEY, latestPreauthByClaim.getClaim().getKey());
						Map<String, Object> medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(objMap,dto.getNewIntimationDTO());
						Double doubleValue = (Double) medicalDecisionTableValue.get("SLAvailAmt");
						Double amt = procedure.getAmountConsideredAmount() + (procedure.getAmbulanceChargeFlag() != null && procedure.getAmbulanceChargeFlag().equalsIgnoreCase("Y") ? procedure.getAmbulanceCharges() : 0d );
						if(procedure.getSublimitNameId() != null) {
							amt = Math.min(doubleValue, amt);
						}
						
						postHospAmt += amt;
					}
					postHospAmt += residualAmtByPreauthKey != null ? residualAmtByPreauthKey.getAmountConsideredAmount() != null ? residualAmtByPreauthKey.getAmountConsideredAmount() : 0d : 0d;
					postHospAmt = postHospAmt - roomRentAndICUAmt;
					dto.setPostHospAmt(postHospAmt.intValue());
				}
			}
			rodKey = dto.getKey();
			doa = dto.getPreauthDataExtractionDetails().getAdmissionDate();
			dod = dto.getPreauthDataExtractionDetails().getDischargeDate();
			diagnosisList = dto.getPreauthDataExtractionDetails().getDiagnosisTableList().size();
			if(diagnosisList == 1 && dto.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getSumInsuredRestriction() != null) {
				isRestrictionSIAvail = true;
			}
			if(isRestrictionSIAvail) {
				List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO = dto.getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO();
				for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : medicalDecisionTableDTO) {
					if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
						siRestrictedAmount = diagnosisProcedureTableDTO.getAvailableAmout() - (dto.getHospitalizationCalculationDTO() != null ? (dto.getHospitalizaionFlag() ? dto.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt() : dto.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt()) : 0);
					}
				}
			}
			procedureList = dto.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().size();
			balanceSIAftHosp = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0) - (dto.getHospitalizationCalculationDTO() != null ? ((dto.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) ? dto.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt() : dto.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt()) : 0);
			
			if(dto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && dto.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && dto.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
				balanceSIAftHosp = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0) - (dto.getOtherInsHospSettlementCalcDTO().getPayableAmt());
			}
		} else {
			Reimbursement hospitalizationROD = getHospitalizationROD(dto.getClaimDTO().getKey());
			
			
			if(dto.getClaimDTO().getClaimType() != null
					&& dto.getClaimDTO().getClaimType().getId()
							.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				 Reimbursement partialHospitalizationROD = getPartialHospitalizationROD(dto.getClaimDTO().getKey());
				 
				 if(partialHospitalizationROD != null && hospitalizationROD != null) {
					 	String[] eachSplit = partialHospitalizationROD.getRodNumber().split("/");
						String eachSplitNo = eachSplit[eachSplit.length - 1];
						Integer eachRODNo = Integer.valueOf(eachSplitNo);
						
						String[] hospSplit = hospitalizationROD.getRodNumber().split("/");
						String hsopSplitNo = hospSplit[hospSplit.length - 1];
						Integer hospNo = Integer.valueOf(hsopSplitNo);
						
						if((partialHospitalizationROD.getStatus() != null && !SHAUtils.getRejectStatusMap().containsKey(partialHospitalizationROD.getStatus().getKey())) && (hospitalizationROD.getStatus() != null && !SHAUtils.getRejectStatusMap().containsKey(hospitalizationROD.getStatus().getKey()))) {
							isTwoRod = true;
						}
						
						if(hospNo < eachRODNo) {
							if(partialHospitalizationROD.getStatus() != null && !SHAUtils.getRejectStatusMap().containsKey(partialHospitalizationROD.getStatus().getKey()) ) {
								isPartialHosp = true;
								hospitalizationROD = partialHospitalizationROD;
								partialHospitalization = partialHospitalizationROD;
							} 
						} else if(eachRODNo.equals(1)) {
							if( hospitalizationROD.getStatus() != null && SHAUtils.getRejectStatusMap().containsKey(hospitalizationROD.getStatus().getKey()) ) {
								isPartialHosp = true;
								hospitalizationROD = partialHospitalizationROD;
								partialHospitalization = partialHospitalizationROD;
							} 
						}
				 }  else if(partialHospitalizationROD != null) {
					 if(partialHospitalizationROD.getStatus() != null && !SHAUtils.getRejectStatusMap().containsKey(partialHospitalizationROD.getStatus().getKey()) ) {
						 	isPartialHosp = true;
							hospitalizationROD = partialHospitalizationROD;
							partialHospitalization = partialHospitalizationROD;
						}
				 } else {
					 if(hospitalizationROD == null) {
						 hospitalizationROD = getFirstROD(dto.getClaimKey());
					 }
					 
				 }
			}
			if(hospitalizationROD != null) {
				rodKey = hospitalizationROD.getKey();
				doa = hospitalizationROD.getDateOfAdmission();
				dod = hospitalizationROD.getDateOfDischarge();
				List<Procedure> findProcedureByPreauthKey = preauthService.findProcedureByPreauthKey(hospitalizationROD.getKey());
				List<PedValidation> diagnosist = preauthService.findPedValidationByPreauthKey(hospitalizationROD.getKey());
				diagnosisList = diagnosist.size();
				if(diagnosisList == 1 && diagnosist.get(0).getSumInsuredRestrictionId() != null) {
					isRestrictionSIAvail = true;
				}
				ResidualAmount residualAmtByPreauthKey = preauthService.getResidualAmtByPreauthKey(hospitalizationROD.getKey());
				Double postHospAmt = 0d;
				Map<String, Object> objMap = new HashMap<String, Object>();
				objMap.put("insuredId", hospitalizationROD.getClaim().getIntimation().getInsured().getInsuredId());
				objMap.put("preauthKey", hospitalizationROD.getKey());
				
				for (PedValidation pedValidation : diagnosist) {
					objMap.put("restrictedSI", isRestrictionSIAvail ? pedValidation.getSumInsuredRestrictionId() : 0l);
					objMap.put("sublimitId", pedValidation.getSublimitId() != null ? pedValidation.getSublimitId() : 0l);
					objMap.put("diagOrProcId", pedValidation.getDiagnosisId() != null ? pedValidation.getDiagnosisId() : 0l);
					objMap.put("referenceFlag", "D");
					objMap.put(SHAConstants.CLAIM_KEY, hospitalizationROD.getClaim().getKey());
					Map<String, Object> medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(objMap,dto.getNewIntimationDTO());
					if(isRestrictionSIAvail) {
						ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(hospitalizationROD.getKey(), 8l);
						siRestrictedAmount = (((Double)medicalDecisionTableValue.get("restrictedAvailAmt")).intValue()) -(reimbursementCalcObjByRodAndClassificationKey != null ? (reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital() + reimbursementCalcObjByRodAndClassificationKey.getPayableToInsured()) : (hospitalizationROD.getFinancialApprovedAmount() != null ? hospitalizationROD.getFinancialApprovedAmount().intValue() : (hospitalizationROD.getBillingApprovedAmount() != null ? hospitalizationROD.getBillingApprovedAmount().intValue() : (hospitalizationROD.getApprovedAmount() != null ? hospitalizationROD.getApprovedAmount().intValue() : 0))));
						if(isTwoRod) {
							Reimbursement rod = null;
							if(isPartialHosp) {
								rod = getHospitalizationROD(dto.getClaimDTO().getKey());
							} else {
								rod = getPartialHospitalizationROD(dto.getClaimDTO().getKey());
							}
							if(rod != null) {
								ReimbursementCalCulationDetails rodCalc = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(rod.getKey(), 8l);
								if(rodCalc != null) {
									if(dto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && rod.getOtherInsurerApplicableFlag() != null && rod.getOtherInsurerApplicableFlag().equalsIgnoreCase("Y")) {
										siRestrictedAmount = (siRestrictedAmount) - (rodCalc.getTpaPayableAmt());
									} else {
										if(rod.getDocAcknowLedgement() != null && rod.getDocAcknowLedgement().getDocumentReceivedFromId() != null && rod.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
											siRestrictedAmount = siRestrictedAmount - (rodCalc != null ? ((rodCalc.getPayableInsuredAfterPremium() != null ? rodCalc.getPayableInsuredAfterPremium() : 0)) : 0);
										} else {
											siRestrictedAmount = siRestrictedAmount - (rodCalc != null ? ((rodCalc.getPayableToHospAftTDS() != null ? rodCalc.getPayableToHospAftTDS() : 0)) : 0);
										}
									}
									
									
//									siRestrictedAmount = siRestrictedAmount - ( ((rod.getDocAcknowLedgement() != null && rod.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && rod.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y"))) ? rodCalc.getPayableInsuredAfterPremium() :  rodCalc.getPayableToHospAftTDS());
								}
								
							}
						}
					}
					Double approvedAmtWithoutCopayAndSiRestriction = pedValidation.getAmountConsideredAmount() != null ? pedValidation.getAmountConsideredAmount() : 0d;
					approvedAmtWithoutCopayAndSiRestriction += pedValidation.getAmbulanceCharges() != null ? pedValidation.getAmbulanceCharges() : 0d;
					if(pedValidation.getSublimitId() != null) {
						approvedAmtWithoutCopayAndSiRestriction = Math.min(approvedAmtWithoutCopayAndSiRestriction, (Double)medicalDecisionTableValue.get("SLAvailAmt"));
					}
					postHospAmt += approvedAmtWithoutCopayAndSiRestriction;
				}
				for (Procedure procedure : findProcedureByPreauthKey) {
					objMap.put("restrictedSI", 0l);
					objMap.put("sublimitId", procedure.getSublimitNameId() != null ? procedure.getSublimitNameId() : 0l);
					objMap.put("diagOrProcId", procedure.getKey());
					objMap.put("referenceFlag", "P");
					objMap.put(SHAConstants.CLAIM_KEY, hospitalizationROD.getClaim().getKey());
					Map<String, Object> medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(objMap,dto.getNewIntimationDTO());
					Double approvedAmtWithoutCopayAndSiRestriction = procedure.getAmountConsideredAmount() != null ? procedure.getAmountConsideredAmount() : 0d;
					approvedAmtWithoutCopayAndSiRestriction += procedure.getAmbulanceCharges() != null ? procedure.getAmbulanceCharges() : 0d;
					if(procedure.getSublimitNameId() != null) {
						approvedAmtWithoutCopayAndSiRestriction = Math.min(approvedAmtWithoutCopayAndSiRestriction, (Double)medicalDecisionTableValue.get("SLAvailAmt"));
					}
					postHospAmt += approvedAmtWithoutCopayAndSiRestriction;
				}
				
				postHospAmt += residualAmtByPreauthKey.getAmountConsideredAmount() != null ? residualAmtByPreauthKey.getAmountConsideredAmount() : 0d;
				dto.setPostHospAmt(postHospAmt.intValue());
				if(findProcedureByPreauthKey != null && findProcedureByPreauthKey.size() > 0) {
					procedureList = findProcedureByPreauthKey.size();
				}
				if(rodKey.equals(dto.getKey())) {
					postHospAmt = 0d;
					List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO = dto.getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO();
					for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : medicalDecisionTableDTO) {
						if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
							Double approvedAmtWithoutCopayAndSiRestriction = diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered().doubleValue() : 0d;
							approvedAmtWithoutCopayAndSiRestriction += diagnosisProcedureTableDTO.getAmbulanceCharge() != null ? diagnosisProcedureTableDTO.getAmbulanceCharge() : 0;
							if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName() != null) {
								approvedAmtWithoutCopayAndSiRestriction = Math.min(approvedAmtWithoutCopayAndSiRestriction, diagnosisProcedureTableDTO.getSubLimitAvaliableAmt());
							}
							postHospAmt += approvedAmtWithoutCopayAndSiRestriction;
						} else if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
							if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
								Double approvedAmtWithoutCopayAndSiRestriction = diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered().doubleValue() : 0d;
								approvedAmtWithoutCopayAndSiRestriction += diagnosisProcedureTableDTO.getAmbulanceCharge() != null ? diagnosisProcedureTableDTO.getAmbulanceCharge() : 0;
								if(diagnosisProcedureTableDTO.getProcedureDTO().getSublimitName() != null) {
									approvedAmtWithoutCopayAndSiRestriction = Math.min(approvedAmtWithoutCopayAndSiRestriction, diagnosisProcedureTableDTO.getSubLimitAvaliableAmt());
								}
								postHospAmt += approvedAmtWithoutCopayAndSiRestriction;
							}
						} else {
							postHospAmt += diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered().doubleValue() : 0d;
						}
					}
					dto.setPostHospAmt(postHospAmt.intValue());
				}
				
			}
			if(hospitalizationROD == null) {
				isZeroPostHospAmt = true;
			}
			ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(hospitalizationROD.getKey(), 8l);
			if(hospitalizationROD.getStatus() != null &&  !hospitalizationROD.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED)) {
				if(hospitalizationROD.getDocAcknowLedgement() != null && hospitalizationROD.getDocAcknowLedgement().getDocumentReceivedFromId() != null && hospitalizationROD.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
					balanceSIAftHosp = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0) - (reimbursementCalcObjByRodAndClassificationKey != null ? ((reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium() : 0)) : 0);
				} else {
					balanceSIAftHosp = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0) - (reimbursementCalcObjByRodAndClassificationKey != null ? ((reimbursementCalcObjByRodAndClassificationKey.getPayableToHospAftTDS() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableToHospAftTDS() : 0)) : 0);
				}
				
				if(dto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && hospitalizationROD.getOtherInsurerApplicableFlag() != null && hospitalizationROD.getOtherInsurerApplicableFlag().equalsIgnoreCase("Y")) {
					balanceSIAftHosp = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0) - (reimbursementCalcObjByRodAndClassificationKey.getTpaPayableAmt());
				}
//				balanceSIAftHosp = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0) - (reimbursementCalcObjByRodAndClassificationKey != null ? ((reimbursementCalcObjByRodAndClassificationKey.getPayableToInsured() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableToInsured() : 0) + (reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital() != null ?  reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital() : 0)) : 0);
			} else {
				balanceSIAftHosp = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0);
			}
			
			if(isTwoRod) {
				Reimbursement rod = null;
				if(isPartialHosp) {
					rod = getHospitalizationROD(dto.getClaimDTO().getKey());
				} else {
					rod = getPartialHospitalizationROD(dto.getClaimDTO().getKey());
				}
				if(rod != null) {
					ReimbursementCalCulationDetails rodCalc = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(rod.getKey(), 8l);
					if(rod.getStatus() != null && !rod.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED)) {
						
						if(rod.getDocAcknowLedgement() != null && rod.getDocAcknowLedgement().getDocumentReceivedFromId() != null && rod.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
							balanceSIAftHosp = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0) - (rodCalc != null ? ((rodCalc.getPayableInsuredAfterPremium() != null ? rodCalc.getPayableInsuredAfterPremium() : 0)) : 0);
						} else {
							balanceSIAftHosp = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0) - (rodCalc != null ? ((rodCalc.getPayableToHospAftTDS() != null ? rodCalc.getPayableToHospAftTDS() : 0)) : 0);
						}
						if(dto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && rod.getOtherInsurerApplicableFlag() != null && rod.getOtherInsurerApplicableFlag().equalsIgnoreCase("Y")) {
							balanceSIAftHosp = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0) - (rodCalc.getTpaPayableAmt());
						}
						
						if(dto.getPartialHospitalizaionFlag() != null && dto.getPartialHospitalizaionFlag()) {
							if(dto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && dto.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && dto.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
								balanceSIAftHosp = (balanceSIAftHosp) - (rodCalc.getTpaPayableAmt());
							} else {
								balanceSIAftHosp = balanceSIAftHosp - (dto.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null ? dto.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : 0);
							}
							
						} else if(dto.getHospitalizaionFlag() != null && dto.getHospitalizaionFlag()) {
							if(dto.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
								if(dto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && dto.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && dto.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
									balanceSIAftHosp = (balanceSIAftHosp) - (rodCalc.getTpaPayableAmt());
								} else {
									balanceSIAftHosp = balanceSIAftHosp - (dto.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null ? dto.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : 0);
								}
								
							} else {
								if(dto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && dto.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && dto.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
									balanceSIAftHosp = (balanceSIAftHosp) - (rodCalc.getTpaPayableAmt());
								} else {
									balanceSIAftHosp = balanceSIAftHosp - (dto.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt() != null ? dto.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt() : 0);
								}
								
							}
							
						}
						
					} else {
						balanceSIAftHosp = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0);
					}
					
				}
			}
		}
		
		if(dto.getPostHospitalizaionFlag()) {
			 previousPostHospAmt = getPostHospitalizationRODValues(dto.getClaimDTO().getKey(), dto.getKey());
			if(isZeroPostHospAmt) {
				amount = 0;
			} else {
				
				List<Hospitalisation> hospitalisationList = createRodService.getHospitalisationList(rodKey);
				Boolean isPackageApplied = false;
				Boolean isRoomRentApplied = false;
				Boolean isAmbulanceApplied = false;
				for (Hospitalisation hospitalisation : hospitalisationList) {
					if(ReferenceTable.PACKAGE_BILL_TYPE_NUMBER.containsKey(hospitalisation.getBillTypeNumber()) ) {
						isPackageApplied = true;
					}
					if(hospitalisation.getBillTypeNumber().equals(8l)) {
						isRoomRentApplied = true;
					}
					
					if(hospitalisation.getBillTypeNumber().equals(ReferenceTable.AMBULANCE_BILL_TYPE_NUMBER)) {
						isAmbulanceApplied = true;
					}
				}
			
				if(isPackageApplied) {
					Double totalDeductable = 0d;
					Double claimedAmt = 0d;
					for (Hospitalisation hospitalisation : hospitalisationList) {
						if(isRoomRentApplied) {
							if(hospitalisation.getBillTypeNumber().equals(8l) || hospitalisation.getBillTypeNumber().equals(10l) || hospitalisation.getBillTypeNumber().equals(ReferenceTable.AMBULANCE_BILL_TYPE_NUMBER)) {
								totalDeductable += SHAUtils.getCorrectAmt(hospitalisation.getNonPayableAmountProduct())  +   SHAUtils.getCorrectLongAmt(hospitalisation.getNonPayableAmt()) +  SHAUtils.getCorrectAmt(hospitalisation.getProrateDeductionAmount()) +  SHAUtils.getCorrectLongAmt(hospitalisation.getDeductibleAmount());
							}
							
							if(hospitalisation.getBillTypeNumber().equals(8l) || hospitalisation.getBillTypeNumber().equals(9l) || hospitalisation.getBillTypeNumber().equals(10l) || hospitalisation.getBillTypeNumber().equals(11l) || hospitalisation.getBillTypeNumber().equals(ReferenceTable.AMBULANCE_BILL_TYPE_NUMBER)) {
								claimedAmt += hospitalisation.getClaimedAmount();
							}
							reduceAmt = claimedAmt.longValue() - totalDeductable.longValue();
						}  else {
							if(isAmbulanceApplied) {
								if(hospitalisation.getBillTypeNumber().equals(8l) || hospitalisation.getBillTypeNumber().equals(10l) || hospitalisation.getBillTypeNumber().equals(ReferenceTable.AMBULANCE_BILL_TYPE_NUMBER)) {
									totalDeductable += SHAUtils.getCorrectAmt(hospitalisation.getNonPayableAmountProduct())  +   SHAUtils.getCorrectLongAmt(hospitalisation.getNonPayableAmt()) +  SHAUtils.getCorrectAmt(hospitalisation.getProrateDeductionAmount()) +  SHAUtils.getCorrectLongAmt(hospitalisation.getDeductibleAmount());
								}
								
								if(hospitalisation.getBillTypeNumber().equals(8l) || hospitalisation.getBillTypeNumber().equals(9l) || hospitalisation.getBillTypeNumber().equals(10l) || hospitalisation.getBillTypeNumber().equals(11l) || hospitalisation.getBillTypeNumber().equals(ReferenceTable.AMBULANCE_BILL_TYPE_NUMBER)) {
									claimedAmt += hospitalisation.getClaimedAmount();
								}
								reduceAmt += claimedAmt.longValue() - totalDeductable.longValue();
							}
							
						}
					}
					if(!isRoomRentApplied) {
						Long daysBetweenDate = SHAUtils.getDaysBetweenDate(doa, dod);
						if(daysBetweenDate.equals(0l)) {
							daysBetweenDate = 1l;
						}
						Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
								dto.getNewIntimationDTO().getInsuredPatient()
										.getInsuredId().toString(), dto
										.getPolicyDto().getKey(),dto.getNewIntimationDTO().getInsuredPatient().getLopFlag());
						
						Map<Integer, Object> detailsMap  = new HashMap<Integer, Object>();
						
						if(dto.getPreauthDataExtractionDetails().getSection() != null && dto.getPreauthDataExtractionDetails().getSection().getId() != null){
							detailsMap = dbCalculationService
									.getHospitalizationDetails(dto.getPolicyDto()
											.getProduct().getKey(), insuredSumInsured,
											dto.getNewIntimationDTO().getHospitalDto()
													.getRegistedHospitals().getCityClass(),
													dto.getNewIntimationDTO()
													.getInsuredPatient().getInsuredId(), dto.getNewIntimationDTO().getKey(),dto.getPreauthDataExtractionDetails().getSection().getId(),"A");
						} else {
						
							detailsMap = dbCalculationService
									.getHospitalizationDetails(dto.getPolicyDto()
											.getProduct().getKey(), insuredSumInsured,
											dto.getNewIntimationDTO().getHospitalDto()
													.getRegistedHospitals().getCityClass(),
													dto.getNewIntimationDTO()
													.getInsuredPatient().getInsuredId(), dto.getNewIntimationDTO().getKey(),0l,"0");
						}
						reduceAmt += daysBetweenDate * (detailsMap.get(8) != null ? ((Double)detailsMap.get(8)).longValue() : 0l);
					}
					
				} else {
					Double totalDeductable = 0d;
					Double claimedAmt = 0d;
					for (Hospitalisation hospitalisation : hospitalisationList) {
						if(hospitalisation.getBillTypeNumber().equals(8l) || hospitalisation.getBillTypeNumber().equals(10l) || hospitalisation.getBillTypeNumber().equals(ReferenceTable.AMBULANCE_BILL_TYPE_NUMBER)) {
							totalDeductable += SHAUtils.getCorrectAmt(hospitalisation.getNonPayableAmountProduct())  +   SHAUtils.getCorrectLongAmt(hospitalisation.getNonPayableAmt()) +  SHAUtils.getCorrectAmt(hospitalisation.getProrateDeductionAmount()) +  SHAUtils.getCorrectLongAmt(hospitalisation.getDeductibleAmount());
						}
						
						if(hospitalisation.getBillTypeNumber().equals(8l) || hospitalisation.getBillTypeNumber().equals(9l) || hospitalisation.getBillTypeNumber().equals(10l) || hospitalisation.getBillTypeNumber().equals(11l) || hospitalisation.getBillTypeNumber().equals(ReferenceTable.AMBULANCE_BILL_TYPE_NUMBER)) {
							claimedAmt += hospitalisation.getClaimedAmount();
						}
					}
					reduceAmt += (claimedAmt.longValue() - totalDeductable.longValue());
				}
					
				amount  = dto.getPostHospAmt() - reduceAmt.intValue();
				
				dto.setPostHospAmt(amount);
			}
			postHospitalizationValues = dbCalculationService.getPostHospitalizationValues(rodKey);
		}
		Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(dto.getKey());
		List<ReimbursementCalCulationDetails> reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodKey(dto.getKey());
		ReimbursementCalCulationDetails hospCalc = SHAUtils.getCalcObjByClassification(reimbursementCalcObjByRodAndClassificationKey, 8l);
		ReimbursementCalCulationDetails preHospCalc = SHAUtils.getCalcObjByClassification(reimbursementCalcObjByRodAndClassificationKey, 9l);
		ReimbursementCalCulationDetails postHospCalc = SHAUtils.getCalcObjByClassification(reimbursementCalcObjByRodAndClassificationKey, 10l);
		if(reimbursementByKey.getOtherInsurerApplicableFlag() != null && reimbursementByKey.getOtherInsurerApplicableFlag().equalsIgnoreCase("Y")) {
			
			if(hospCalc != null) {
				dto.setHospAmountInBiling(hospCalc.getTpaPayableAmt() != null ? hospCalc.getTpaPayableAmt() : 0 );
			}  else {
				dto.setHospAmountInBiling(dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) ? dto.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : dto.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt() );
			}
			if(preHospCalc != null) {
				dto.setPreHospAmtInBilling(preHospCalc.getTpaPayableAmt() != null ? preHospCalc.getTpaPayableAmt() : 0 );
			} else {
				dto.setPreHospAmtInBilling(dto.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt());
			}
			if(postHospCalc != null) {
				dto.setPostHospAmtInBilling(postHospCalc.getTpaPayableAmt() != null ? postHospCalc.getTpaPayableAmt() : 0 );
			} else {
				dto.setPostHospAmtInBilling(dto.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt());
			}
		} else {
			if(hospCalc != null) {
				dto.setHospAmountInBiling(hospCalc.getPayableInsuredAfterPremium() != null ? hospCalc.getPayableInsuredAfterPremium() : 0 );
			} else {
				dto.setHospAmountInBiling(dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) ? dto.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : dto.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt() );
			}
			if(preHospCalc != null) {
				dto.setPreHospAmtInBilling(preHospCalc.getPayableInsuredAfterPremium() != null ? preHospCalc.getPayableInsuredAfterPremium() : 0 );
			} else {
				dto.setPreHospAmtInBilling(dto.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt());
			}
			if(postHospCalc != null) {
				dto.setPostHospAmtInBilling(postHospCalc.getPayableInsuredAfterPremium() != null ? postHospCalc.getPayableInsuredAfterPremium() : 0 );
			} else {
				dto.setPostHospAmtInBilling(dto.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt());
			}
		}
		Integer amt = (dto.getBalanceSI() != null ? dto.getBalanceSI().intValue() : 0) - balanceSIAftHosp;
		dto.setHospAmountInBiling(amt != null ? amt > 0 ? amt : 0 : 0);
		dto.setPreHospAmtInBilling(amt != null ? amt > 0 ? amt : 0 : 0);
		dto.setPostHospAmtInBilling(amt != null ? amt > 0 ? amt : 0 : 0);
		
		List<DiagnosisDetailsTableDTO> diagnosisTableList = dto.getPreauthDataExtractionDetails().getDiagnosisTableList();
		List<ProcedureDTO> procedureList2 = dto.getPreauthDataExtractionDetails().getProcedureList();
		
		for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisTableList) {
			if(dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null && dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue().equalsIgnoreCase(ReferenceTable.BARIATRIC_SUB_COVER_CODE) && diagnosisDetailsTableDTO.getSublimitName() != null && diagnosisDetailsTableDTO.getSublimitName().getLimitId() != null) {
				Map<String, Object> objMap = new HashMap<String, Object>();
				objMap.put("insuredId", dto.getNewIntimationDTO().getInsuredPatient().getInsuredId());
				objMap.put("preauthKey",dto.getKey());
				objMap.put("restrictedSI",  0l);
				objMap.put("insuredId", dto.getNewIntimationDTO().getInsuredPatient().getInsuredId());
				objMap.put("preauthKey", dto.getKey());
				objMap.put("sublimitId", diagnosisDetailsTableDTO.getSublimitName().getLimitId());
				objMap.put("diagOrProcId", diagnosisDetailsTableDTO.getDiagnosisId() != null ?diagnosisDetailsTableDTO.getDiagnosisId() : 0l);
				objMap.put("referenceFlag", "D");
				objMap.put(SHAConstants.CLAIM_KEY, dto.getClaimDTO().getKey());
				Map<String, Object> medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(objMap,dto.getNewIntimationDTO());
				Double availableSubLimit = (Double) medicalDecisionTableValue.get("SLAvailAmt");
				dto.setBariatricAvailableAmount(availableSubLimit);
			}
		}
		
		for (ProcedureDTO procedureDTO : procedureList2) {
			if(dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null && dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue().equalsIgnoreCase(ReferenceTable.BARIATRIC_SUB_COVER_CODE) && procedureDTO.getSublimitName() != null && procedureDTO.getSublimitName().getLimitId() != null) {
				Map<String, Object> objMap = new HashMap<String, Object>();
				objMap.put("insuredId", dto.getNewIntimationDTO().getInsuredPatient().getInsuredId());
				objMap.put("preauthKey",dto.getKey());
				objMap.put("restrictedSI",  0l);
				objMap.put("insuredId", dto.getNewIntimationDTO().getInsuredPatient().getInsuredId());
				objMap.put("preauthKey", dto.getKey());
				objMap.put("sublimitId", procedureDTO.getSublimitName().getLimitId());
				objMap.put("diagOrProcId", procedureDTO.getProcedureName() != null ? procedureDTO.getProcedureName().getId() : 0l);
				objMap.put("referenceFlag", "P");
				objMap.put(SHAConstants.CLAIM_KEY, dto.getClaimDTO().getKey());
				Map<String, Object> medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(objMap,dto.getNewIntimationDTO());
				Double availableSubLimit = (Double) medicalDecisionTableValue.get("SLAvailAmt");
				dto.setBariatricAvailableAmount(availableSubLimit);
			}
			
		}
		if((dto.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.BARIATRIC_SECTION_CODE)) || ( dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null && dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue() != null && dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue().equalsIgnoreCase(ReferenceTable.BARIATRIC_SECTION_CODE))) {
			dto.setBariatricAvailableAmount(dbCalculationService.getBariatricSublimitAmount(dto.getKey(),dto.getNewIntimationDTO().getPolicy().getProduct().getKey()));
			
		}
		view.setPosthospAmt(amount > 0 ? amount : 0, postHospitalizationValues, balanceSIAftHosp > 0 ? balanceSIAftHosp : 0, isRestrictionSIAvail, siRestrictedAmount > 0 ? siRestrictedAmount : 0, previousPostHospAmt.intValue() > 0 ? previousPostHospAmt.intValue() : 0, previousPreHospitalizationRODValues.intValue() > 0 ? previousPreHospitalizationRODValues.intValue() : 0 );
	}
	
	public void rechargingSIEvent(
			@Observes @CDIEvent(RECHARGE_SI_FOR_PRODUCT) final ParameterDTO parameters) {
				PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
				
				Long policyKey = preauthDTO.getNewIntimationDTO().getPolicy().getKey();
				Long insuredKey = preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey();
				
				dbCalculationService.rechargingSIbasedOnProduct(policyKey, insuredKey);
				
//				Double balanceSI = dbCalculationService.getBalanceSI(
//						policyKey,insuredKey, preauthDTO.getClaimDTO().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured(),preauthDTO.getNewIntimationDTO().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
				Double balanceSI = 0d;
				
				if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
						null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
						null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
						!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
				
					balanceSI = dbCalculationService.getBalanceSI(
								policyKey,insuredKey, preauthDTO.getClaimDTO().getKey(),
								preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured(),
								preauthDTO.getNewIntimationDTO().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
				}
				else
				{
					balanceSI = dbCalculationService.getGPABalanceSI(
							policyKey,insuredKey, preauthDTO.getClaimDTO().getKey(),
							preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured(),
							preauthDTO.getNewIntimationDTO().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
				}

//				Double balanceSI = 500000d;
				
				balanceSI = SHAUtils.getHospOrPartialAppAmt(preauthDTO, reimbursementService, balanceSI);
				
				
				view.setBalanceSIforRechargedProcess(balanceSI);
				
		}
	

}
