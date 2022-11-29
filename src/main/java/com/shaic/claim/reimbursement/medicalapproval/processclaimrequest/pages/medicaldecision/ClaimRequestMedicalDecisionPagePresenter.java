package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

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
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.FVRGradingDetail;
import com.shaic.domain.FVRGradingMaster;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.FvrTriggerPoint;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PostHospitalisation;
import com.shaic.domain.PreHospitalisation;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.shaic.starfax.simulation.PremiaPullService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(ClaimRequestMedicalDecisionPageInterface.class)
public class ClaimRequestMedicalDecisionPagePresenter extends
		AbstractMVPPresenter<ClaimRequestMedicalDecisionPageInterface> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9106963317300947015L;

	@EJB
	private MasterService masterService;
	
	@EJB
	private PolicyService policyService;

	@EJB
	private PreauthService preauthService;

	@EJB
	private PreMedicalService preMedicalService;

	@EJB
	private ClaimService claimService;

	@EJB
	private DBCalculationService dbCalculationService;

	@EJB
	private HospitalService hospitalService;

	@EJB
	private PreviousPreAuthService previousPreauthService;

	@EJB
	private FieldVisitRequestService fvrService;
	@EJB
	private InvestigationService investigationService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	@EJB
	private CreateRODService createRodService;

	@EJB
	private PEDValidationService pedValidationService;
	
	@javax.inject.Inject
	private ViewBillSummaryPage viewBillSummaryPage;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	@EJB
	private CreateRODService rodService;
	
	@EJB
	private PremiaPullService premiaPullService;

	public static final String MEDICAL_APPROVAL_SETUP_REFERNCE = "medical_approval_claim_request_medical_decision_setup_reference";

	public static final String VIEW_BALANCE_SUM_INSURED_DETAILS = "medical_approval_claim_request_medical_decision_balance_sum_insured_details";

	public static final String VIEW_CLAIMED_AMOUNT_DETAILS = "medical_approval_claim_request_medical_decision_claimed_amount_details";

	public static final String GET_PREAUTH_REQUESTED_AMOUT = "medical_approval_claim_request_medical_decision_preauth_requeseted_amount";

	public static final String CLAIM_REQUEST_APPROVE_EVENT = "claim_request_apporve_event";
	
	public static final String CLAIM_CANCEL_ROD_EVENT      =  "claim_request_cancel_rod";            

	public static final String CLAIM_REQUEST_QUERY_BUTTON_EVENT = "claim_request_query_button_event";

	public static final String CLAIM_REQUEST_REJECTION_EVENT = "claim_request_rejection_button_event";

	public static final String CLAIM_REQUEST_ESCALATE_EVENT = "claim_request_escalte_button_event";

	public static final String CLAIM_REQUEST_ESCALATE_REPLY_EVENT = "claim_request_escalte_reply_event";

	public static final String CLAIM_REQUEST_SENT_TO_CPU_SELECTED = "claim_request_sent_to_cpu_selected";

	public static final String CLAIM_REQUEST_REFERCOORDINATOR_EVENT = "claim_request_refer_to_coordinator_event";

	public static final String CLAIM_REQUEST_FIELD_VISIT_EVENT = "claim_request_field_visit_event";

	public static final String CLAIM_REQUEST_INVESTIGATION_EVENT = "claim_request_investigation_event";
	
	public static final String CLAIM_REQUEST_REF_BILLENTRY_EVENT = "claim_request_ref_billentry_event";

	public static final String CLAIM_REQUEST_SPECIALIST_EVENT = "claim_request_specialist_event";

	public static final String CLAIM_REQUEST_SENT_TO_REPLY_EVENT = "claim_request_sent_to_reply_event";

	public static final String CLAIM_REQUSET_SUM_INSURED_CALCULATION = "claim_request_sum_insured_calculation";

	public static final String SUM_INSURED_CALCULATION = "claim_request_for_getting_sum_insured_calculation";

	public static final String BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE = "claim_request_bill_entry_category_dropdown_values";

	public static final String BILL_ENTRY_COMPLETION_STATUS = "claim_request_bill_entry_completion_status";

	public static final String CLAIM_REQUEST_SAVE_BILL_ENTRY = "claim_request_save_bill_entry_for_amount_considered";

	public static final String CHECK_INVESTIGATION_INITIATED = "claim_request_check_investigation_initiated";
	
	public static final String MA_SAVE_BILL_ENTRY_VALUES = "ma_save_bill_entry_values";
	
	public static final String MA_LOAD_BILL_DETAILS_VALUES = "ma_load_bill_details_values";
	
	public static final String REFERENCE_DATA_CLEAR = "reference data clear in MA";
	
	public static final String CLAIM_REQUEST_AUTO_SKIP_FVR = "claim_request_auto_skip_fvr";
	
	public static final String MA_FIELD_VISIT_GRADING = "ma_field_visit_grading";
	
	public static final String RECHARGE_SI_FOR_PRODUCT = "recharging SI for product in Process claim request";
	
	public static final String INVS_REVIEW_REMARKS_LIST = "Invs_Review_Remarks_List";
	
	public static final String MA_TOPUP_POLICY_EVENT = "ma_topup_policy_event";
	
	public static final String MA_TOPUP_POLICY_INTIMATION_EVENT = "ma_topup_policy_intimation_event";
	
	Map<String, Object> referenceData = new HashMap<String, Object>();

	public static final String MA_SUB_REJECT_CATEG_LAYOUT = "Rejection Subcategory Layout for MA";
	
	public static final String CLAIM_REQUEST_APPROVE_BSI_ALERT = "claim_request_approve_bsi_alert";

	public static final String MA_ICAC_SUBMIT_EVENT = "claim_request_icac_sumbit_event";
	
	public static final String CLAIM_REQUEST_HOLD_EVENT = "claim_request_hold_event";
	
	public static final String MA_HOSPITAL_CASH_TOPUP_POLICY_EVENT = "ma_hospitalcash_topup_policy_event";

	
	public void generateRejSubCategoryLayout(
			@Observes @CDIEvent(MA_SUB_REJECT_CATEG_LAYOUT) final ParameterDTO parameters) {

		Long rejCategId = (Long) parameters.getPrimaryParameter();
		
		BeanItemContainer<SelectValue> rejSubcategContainer = masterService.getRejSubcategContainer(rejCategId);
		
		view.setSubCategContainer(rejSubcategContainer);
	}
	
	public void setUpReference(
			@Observes @CDIEvent(MEDICAL_APPROVAL_SETUP_REFERNCE) final ParameterDTO parameters) {

		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();

		if (dto.getClaimKey() != null) {
			List<Investigation> investigationList = investigationService
					.getByInvestigationByClaimKey(dto.getClaimKey());
			if (investigationList != null && investigationList.size() >= 0) {
				dto.setInvestigationSize(investigationList.size());
			} else {
				dto.setInvestigationSize(0);
			}
		}
		referenceData.put("investigatorName", masterService.getInvestigation());
		referenceData.put("commonValues", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("medicalVerification", masterService
				.getSelectValueContainer(ReferenceTable.MEDICAL_VERIFICATION));
		referenceData
				.put("treatmentQualityVerification",
						masterService
								.getSelectValueContainer(ReferenceTable.TREATMENT_QUALITY_VERIFICATION));
		referenceData.put("verified", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("previousPreauth",
				previousPreauthService.search(dto.getClaimKey(), false));
		referenceData.put("status", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData
				.put("fileType",
						masterService
								.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		referenceData.put("cancellationReason", masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		
		referenceData.put("fvrNotRequiredRemarks", masterService.getSelectValueContainer(ReferenceTable.JIO_COPAY_TYPE_VALUE));
		
		/*referenceData.put("billClassification",
				masterService.getMasBillClassificationValues());*/
		referenceData.put("billClassification", setClassificationValues(dto));
		referenceData.put("coPayType", masterService.getSelectValueContainer(ReferenceTable.JIO_COPAY_TYPE_VALUE));
		
		referenceData.put("reasonForNotPaying", masterService.getSelectValueContainer(ReferenceTable.PED_NON_PAYABLE_REASON));
		
		view.setupReferences(referenceData);
	}
	
	
	public BeanItemContainer<SelectValue> setClassificationValues(PreauthDTO preauthDTO)
	{
		BeanItemContainer<SelectValue> beanContainer = masterService.getMasBillClassificationValues();
		
		List<SelectValue> selectValueList = beanContainer.getItemIds();
		
		List<SelectValue> finalClassificationList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> classificationValueContainer = null;
		if(null != selectValueList && !selectValueList.isEmpty())
		{
			 classificationValueContainer = new BeanItemContainer<SelectValue>(
						SelectValue.class);
			for (SelectValue selectValue : selectValueList) {		
				
				//	if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag()))
					if((ReferenceTable.HOSPITALIZATION).equals(selectValue.getId()) && ((null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag())
							|| (null != preauthDTO.getIsHospitalizationRepeat() && preauthDTO.getIsHospitalizationRepeat()) || (null != preauthDTO.getPartialHospitalizaionFlag() &&  preauthDTO.getPartialHospitalizaionFlag())
							))
					{
						finalClassificationList.add(selectValue);
					}
					else if((ReferenceTable.PRE_HOSPITALIZATION).equals(selectValue.getId()) && (null != preauthDTO.getPreHospitalizaionFlag() && preauthDTO.getPreHospitalizaionFlag()))
					{
						finalClassificationList.add(selectValue);
					}
					else if((ReferenceTable.POST_HOSPITALIZATION).equals(selectValue.getId()) && (null != preauthDTO.getPostHospitalizaionFlag() && preauthDTO.getPostHospitalizaionFlag()))
					{
						finalClassificationList.add(selectValue);
					}
					else if((ReferenceTable.LUMPSUM).equals(selectValue.getId()) && (null != preauthDTO.getLumpSumAmountFlag() && preauthDTO.getLumpSumAmountFlag()))
					{
						finalClassificationList.add(selectValue);
					}
					
					else if((ReferenceTable.OTHER_BENEFIT).equals(selectValue.getId()) && (null != preauthDTO.getOtherBenefitsFlag() && preauthDTO.getOtherBenefitsFlag()))
					{
						finalClassificationList.add(selectValue);
					}
					else if((ReferenceTable.HOSPITAL_CASH_BILL_ID).equals(selectValue.getId()) && (null != preauthDTO.getReceiptOfDocumentsDTO() && preauthDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalCash()))
					{
						finalClassificationList.add(selectValue);
					}
				
			}
			classificationValueContainer.addAll(finalClassificationList);
		}
		return classificationValueContainer;
	}
	
	public void loadBillDetailsValues(
			@Observes @CDIEvent(MA_LOAD_BILL_DETAILS_VALUES) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		List<BillEntryDetailsDTO> dtoList = rodService.getBillEntryDetailsList(uploadDTO);
		uploadDTO.setBillEntryDetailList(dtoList);
		view.setUploadDTOForBillEntry(uploadDTO);
	/*	Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getMasBillCategoryValues(billClassificationKey);*/
		//view.setUpCategoryValues(selectValueContainer);
		//view.enableOrDisableBtn(uploadDTO);
		}
	

	public void viewBalanceSumInsured(
			@Observes @CDIEvent(VIEW_BALANCE_SUM_INSURED_DETAILS) final ParameterDTO parameters) {
		String intimationId = (String) parameters.getPrimaryParameter();
		if (intimationId != null && !intimationId.equals("")) {
			view.viewBalanceSumInsured(intimationId);
		}

	}

	public void viewClaimAmountDetails(
			@Observes @CDIEvent(VIEW_CLAIMED_AMOUNT_DETAILS) final ParameterDTO parameters) {
		view.viewClaimAmountDetails();
	}

	public void setBillEntryStatus(
			@Observes @CDIEvent(BILL_ENTRY_COMPLETION_STATUS) final ParameterDTO parameters) {
		// Boolean status = (Boolean) parameters.getPrimaryParameter();
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO) parameters
				.getPrimaryParameter();
		rodService.saveBillEntryValues(uploadDTO);
		view.setBillEntryFinalStatus(uploadDTO);

	}

	public void setPreAuthRequestedAmount(
			@Observes @CDIEvent(GET_PREAUTH_REQUESTED_AMOUT) final ParameterDTO parameters) {
		PreviousPreAuthTableDTO dto = (PreviousPreAuthTableDTO)parameters.getPrimaryParameter();
		view.setPreAuthRequestedAmt(preauthService.getPreauthReqAmt(dto.getKey(), dto.getClaimKey()));
	}

	public void generateApproveLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_APPROVE_EVENT) final ParameterDTO parameters) {		
		
		view.generateApproveLayout();
	}
	public void generateCancelRODLayout(
			@Observes @CDIEvent(CLAIM_CANCEL_ROD_EVENT) final ParameterDTO parameters) {
		view.generateCancelRodLayout();
	}
	

	public void generateQueryLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_QUERY_BUTTON_EVENT) final ParameterDTO parameters) {
		view.generateQueryLayout();
	}

	public void generateRejectLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_REJECTION_EVENT) final ParameterDTO parameters) {
		
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		 BeanItemContainer<SelectValue> dropDownValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		 
		if(dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId() != null && 
				dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getKey() != null && 
				ReferenceTable.SETTLED_RECONSIDERATION_ID.equals(dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getKey()))
			dropDownValueContainer = masterService.getSettledRejectionCategory();
		else if(dto.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				(dto.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
						|| dto.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))){
			if(dto.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)){
				dropDownValueContainer = masterService.getReimbRejCategoryForCoronaFmlProd(dto.getNewIntimationDTO().getPolicy().getProduct().getKey() ,dto.getNewIntimationDTO().getPolicy().getPolicyPlan());
			}else if(dto.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)){
				dropDownValueContainer= masterService.getReimbRejCategoryForCoronaFmlProd(dto.getNewIntimationDTO().getPolicy().getProduct().getKey(),dto.getNewIntimationDTO().getPolicy().getPolicyPlan()); 
			}
		}
		else	
			dropDownValueContainer = masterService.getReimbRejCategoryByValue();
		
		
		

			
		view.generateRejectionLayout(dropDownValueContainer);
//				masterService.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
	}

	public void generateEscalateLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_ESCALATE_EVENT) final ParameterDTO parameters) {
		view.generateEscalateLayout(masterService
				.getSelectValueContainer(ReferenceTable.ESCALATE_TO_ROD));
	}

	public void generateEscalateReplyLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_ESCALATE_REPLY_EVENT) final ParameterDTO parameters) {
		view.generateEscalateReplyLayout();
	}

	public void sentTOCPUChecked(
			@Observes @CDIEvent(CLAIM_REQUEST_SENT_TO_CPU_SELECTED) final ParameterDTO parameters) {
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnSentTOCPU(isChecked);
	}

	public void generateReferCoOrdinatorLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_REFERCOORDINATOR_EVENT) final ParameterDTO parameters) {
		view.generateReferCoOrdinatorLayout(masterService
				.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
	}

	public void generateFieldVisitLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_FIELD_VISIT_EVENT) final ParameterDTO parameters) {
		
		PreauthDTO bean = (PreauthDTO)parameters.getPrimaryParameter();
		
		FieldVisitRequest fvrobjByRodKey = fvrService.getPendingFieldVisitByClaimKey(bean.getClaimDTO().getKey());
		
		if((fvrobjByRodKey  == null || (fvrobjByRodKey != null && fvrobjByRodKey.getStatus() != null && ReferenceTable.INITITATE_FVR.equals(fvrobjByRodKey.getStatus().getKey())))){
		
			if(fvrobjByRodKey  != null ){
				dbCalculationService.invokeProcedureAutoSkipFVR(fvrobjByRodKey.getFvrId());
				fvrService.autoSkipFirstFVRParallel(fvrobjByRodKey,bean.getNewIntimationDTO().getIntimationId(),bean.getStrUserName());
			}
		}
		
		boolean fvrpending = preauthService.getFVRStatusIdByClaimKey(bean.getClaimDTO().getKey());
		
		boolean isAssigned = false;
		FieldVisitRequest fvrobj = null;
		
		if(fvrpending){
			fvrobj = preauthService.getPendingFVRByClaimKey(bean.getClaimDTO().getKey());
		}
		String repName = "";
		String repContactNo = "";
		if(fvrobj != null){
			isAssigned = true;
			repName = fvrobj.getRepresentativeName();
			repContactNo = fvrobj.getRepresentativeContactNumber();
		}
		
		view.genertateFieldsBasedOnFieldVisit(masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO)
				,/*masterService.getSelectValueContainer(ReferenceTable.ASSIGN_TO)*/null,masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY),
				isAssigned, repName, repContactNo);
		//}
	}
	
	public void generateRefBillEntryLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_REF_BILLENTRY_EVENT) final ParameterDTO parameters) {
		
		PreauthDTO bean = (PreauthDTO)parameters.getPrimaryParameter();
			
		view.genertateFieldsBasedOnRefToBillEntry();
	
	}

	public void generateInvestigationLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_INVESTIGATION_EVENT) final ParameterDTO parameters) {
		view.genertateInvestigationLayout(masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO_INVESTIGATION));
	}

	public void generateSpecialistLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_SPECIALIST_EVENT) final ParameterDTO parameters) {
		view.genertateSpecialistLayout(masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE)));
	}

	public void generateSentToReplyLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_SENT_TO_REPLY_EVENT) final ParameterDTO parameters) {
		view.genertateSentToReplyLayout();
	}
	
	public void saveBillValues(
			@Observes @CDIEvent(MA_SAVE_BILL_ENTRY_VALUES) final ParameterDTO parameters) {
			//Boolean status = (Boolean) parameters.getPrimaryParameter();
			UploadDocumentDTO	uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
			rodService.saveBillEntryValues(uploadDTO);
			//view.setBillEntryFinalStatus(uploadDTO);
		
	}

	public void getValuesForMedicalDecisionTable(
			@Observes @CDIEvent(CLAIM_REQUSET_SUM_INSURED_CALCULATION) final ParameterDTO parameters) {
		Map<String, Object> values = (Map<String, Object>) parameters
				.getPrimaryParameter();
		DiagnosisProcedureTableDTO dto = (DiagnosisProcedureTableDTO) parameters
				.getSecondaryParameters()[0];
		PreauthDTO preauthDto = (PreauthDTO) parameters.getSecondaryParameter(1, PreauthDTO.class);
		String diagnosis = null;
		if (values.containsKey("diagnosisId")) {
			diagnosis = masterService.getDiagnosis(Long.valueOf((String) values
					.get("diagnosisId")));
		}

		if (dto.getDiagnosisDetailsDTO() != null) {
			dto.getDiagnosisDetailsDTO().setDiagnosis(diagnosis);
		}
		
		//Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimKey(), preauthDto.getKey());
		Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRodForMA(preauthDto.getClaimKey(), preauthDto.getKey(),preauthDto.getRodNumber());
		
		Boolean queryRaisedFromMA = reimbursementQueryService.isQueryRaisedFromMA(preauthDto.getKey());
		if(queryRaisedFromMA){
			preauthDto.setIsQueryReceived(false);
		}
		
		if(!preauthDto.getIsQueryReceived()){
		
			if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
				
	//			Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimKey(), preauthDto.getKey());
				
				if(hospitalizationRod != null){
					
					values.put("preauthKey", hospitalizationRod.getKey());
					
				}else{
					if(preauthDto.getClaimDTO().getLatestPreauthKey() != null){
						values.put("preauthKey", preauthDto.getClaimDTO().getLatestPreauthKey());
				    }else{
				    	Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(preauthDto.getClaimDTO().getKey());
				    	if(latestPreauth != null){
				    		values.put("preauthKey", latestPreauth.getKey());
				    	}
				    }
				}
			
			}else{
				
				if(hospitalizationRod != null){
					values.put("preauthKey", hospitalizationRod.getKey());
				}else{
					values.put("preauthKey",0l);
				}
			}
		} else {
			values.put("preauthKey", preauthDto.getKey());
		}


		if((preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) || (preauthDto.getIsHospitalizationRepeat() != null && preauthDto.getIsHospitalizationRepeat())) {
			values.put("preauthKey", 0l);
			//if(! (preauthDto.getIsHospitalizationRepeat() != null && preauthDto.getIsHospitalizationRepeat())){
				if(hospitalizationRod != null){
					values.put("preauthKey", hospitalizationRod.getKey());
				}else{
					values.put("preauthKey",0l);
				}
			//}
			
		}
		
		//IMSSUPPOR-29019 - SETTLED RECONSIDERATION SUBLIMIT UTILISATION
		if(preauthDto.getIsReconsiderationRequest() != null && preauthDto.getIsReconsiderationRequest() && preauthDto.getIsRejectReconsidered() != null && !preauthDto.getIsRejectReconsidered()){
			values.put("preauthKey", preauthDto.getKey());
		}
		
		if(preauthDto.getIsHospitalizationRepeat() != null && preauthDto.getIsHospitalizationRepeat()){
			values.put("preauthKey", 0l);
		}
		
		if(ReferenceTable.POS_MEDICLASSIC_PRODUCT_KEY.equals(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| ReferenceTable.REVISED_POS_MEDICLASSIC_PRODUCT_KEY.equals(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey())
				&& preauthDto.getIsHospitalizationRepeat() != null && preauthDto.getIsHospitalizationRepeat()){
			values.put("preauthKey", 0l);
		}
		
		Map<String, Object> medicalDecisionTableValues = null;
		
		values.put("productKey", preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey());
		
		if(ReferenceTable.getGMCProductList().containsKey(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValueForGMC(values,preauthDto.getNewIntimationDTO());
		}else{
			medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValue(values,preauthDto.getNewIntimationDTO());
		}
		
		
		if(values.containsKey(SHAConstants.IS_NON_ALLOPATHIC) && (Boolean)values.get(SHAConstants.IS_NON_ALLOPATHIC)) {
//			Map<String, Double> nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
//					,(Long)values.get("rodKey"),"R");
			Map<String, Double> nonAllopathicAmount = null;
			if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){

				
				Long preauthKey = (Long)values.get("preauthKey");

				
				if(hospitalizationRod != null){
					
					nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
							,preauthKey,"R", (Long)values.get(SHAConstants.CLAIM_KEY));
				}else{
					nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
							,preauthKey,"C", (Long)values.get(SHAConstants.CLAIM_KEY));
				}
				
				
				}else{
				
				nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
							,0l,"0", (Long)values.get(SHAConstants.CLAIM_KEY));
				
				//IMSSUPPOR-26904
				Reimbursement reimbursementByKey = ackDocReceivedService.getReimbursementByKey(preauthDto.getKey());
				if(reimbursementByKey != null && reimbursementByKey.getDocAcknowLedgement() != null){
					
					Boolean isSettled = reimbursementService.isSettledPaymentAvailable(reimbursementByKey.getRodNumber());
					
					DocAcknowledgement docAcknowLedgement = reimbursementByKey.getDocAcknowLedgement();
					if(isSettled){
						if(reimbursementByKey.getReconsiderationRequest() != null && reimbursementByKey.getReconsiderationRequest().equalsIgnoreCase("Y") 
								&& docAcknowLedgement.getPaymentCancellationFlag() != null && docAcknowLedgement.getPaymentCancellationFlag().equalsIgnoreCase("N")){
							nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
									,preauthDto.getKey(),"R", (Long)values.get(SHAConstants.CLAIM_KEY));
						}
					}
					
				}
				
				}
			medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
			medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
			
			/*Below Code For - IMSSUPPOR-28291*/
			if(preauthDto.getIsQueryReceived()){
                Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(preauthDto.getKey());
			      Double nonallopatUtilAmt = nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT);
			      Double utilAmt = nonallopatUtilAmt - (reimbursementByKey.getNonAllopathicApprAmt() !=null ? reimbursementByKey.getNonAllopathicApprAmt() : 0d);
			      nonAllopathicAmount.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, utilAmt > 0 ? utilAmt : 0d);
			      medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, utilAmt > 0 ? utilAmt : 0d);
			}
			
			/*IMSSUPPOR-28968 - Below Code commented and in procedure will handled 
			if(preauthDto.getIsReconsiderationRequest() != null && preauthDto.getIsReconsiderationRequest()){
				Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(preauthDto.getKey());
				Reimbursement orgreimbursementByKey = reimbursementService.getReimbursementByKey(reimbursementByKey.getParentKey());
				medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, orgreimbursementByKey.getNonAllopathicApprAmt() != null ? orgreimbursementByKey.getNonAllopathicApprAmt() : 0d);
			}*/
			
			//added for jira IMSSUPPOR-27044
			if(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY)){
				medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, preauthDto.getBalanceSI());
				medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, 0d);
			}
		}

		view.getValuesForMedicalDecisionTable(dto, medicalDecisionTableValues);
	}

	public void getSumInsuredInfoFromDB(
			@Observes @CDIEvent(SUM_INSURED_CALCULATION) final ParameterDTO parameters) {
		Map<String, Object> values = (Map<String, Object>) parameters
				.getPrimaryParameter();
		DiagnosisProcedureTableDTO dto = (DiagnosisProcedureTableDTO) parameters
				.getSecondaryParameters()[0];
		PreauthDTO preauthDto = (PreauthDTO) parameters.getSecondaryParameter(1, PreauthDTO.class);
		
		String diagnosis = null;
		if (values.containsKey("diagnosisId")) {
			diagnosis = masterService.getDiagnosis(Long.valueOf((String) values
					.get("diagnosisId")));
		}

		if (dto.getDiagnosisDetailsDTO() != null) {
			dto.getDiagnosisDetailsDTO().setDiagnosis(diagnosis);
		}
		
		//Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimKey(), preauthDto.getKey());
		
		Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRodForMA(preauthDto.getClaimKey(), preauthDto.getKey(),preauthDto.getRodNumber());
		
		Boolean queryRaisedFromMA = reimbursementQueryService.isQueryRaisedFromMA(preauthDto.getKey());
		if(queryRaisedFromMA){
			preauthDto.setIsQueryReceived(false);
		}
		
		if(!preauthDto.getIsQueryReceived()){
		
			if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
				
	//			Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimKey(), preauthDto.getKey());
				
				if(hospitalizationRod != null){
					
					values.put("preauthKey", hospitalizationRod.getKey());
					
				}else{
					if(preauthDto.getClaimDTO().getLatestPreauthKey() != null){
						values.put("preauthKey", preauthDto.getClaimDTO().getLatestPreauthKey());
				    }else{
				    	Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(preauthDto.getClaimDTO().getKey());
				    	if(latestPreauth != null){
				    		values.put("preauthKey", latestPreauth.getKey());
				    	}
				    }
				}
			
			}else{
				
				if(hospitalizationRod != null){
					values.put("preauthKey", hospitalizationRod.getKey());
				}else{
					values.put("preauthKey",0l);
//					values.put("preauthKey", preauthDto.getKey());
				}
			}
		} else {
			values.put("preauthKey", preauthDto.getKey());
		}

	     if(preauthDto.getIsReferToMedicalApprover() || preauthDto.getIsEscalateReplyEnabled()){
	    	 values.put("preauthKey", preauthDto.getKey());
	     }
	     
	     if((preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) || (preauthDto.getIsHospitalizationRepeat() != null && preauthDto.getIsHospitalizationRepeat())) {
				values.put("preauthKey", 0l);
				if(hospitalizationRod != null){
					values.put("preauthKey", hospitalizationRod.getKey());
				}
				Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(preauthDto.getKey());
				if(reimbursementByKey.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)||
						reimbursementByKey.getStatus().getKey().equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)){
					values.put("preauthKey", preauthDto.getKey());
				}
			}
		
	     Map<String, Object> medicalDecisionTableValues = null;
	     
	     values.put("productKey", preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey());
			
			if(ReferenceTable.getGMCProductList().containsKey(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValueForGMC(values,preauthDto.getNewIntimationDTO());
			}else{
				medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValue(values,preauthDto.getNewIntimationDTO());
			}

		view.setDiagnosisSumInsuredValuesFromDB(dto, medicalDecisionTableValues);
	}

	public void setUpCategoryValues(
			@Observes @CDIEvent(BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE) final ParameterDTO parameters) {
		Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		SelectValue claimType = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		Long productKey = (Long) parameters.getSecondaryParameter(2, Long.class);
		String subCoverValue = (String) parameters.getSecondaryParameter(3, String.class);
		Boolean isDomicillary = (Boolean) parameters.getSecondaryParameter(4, Boolean.class);
		String intimationNo = (String) parameters.getSecondaryParameter(5, String.class);
		
		Intimation intimationObj = preauthService.getIntimationByNo(intimationNo);
		
		
		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getMasBillCategoryValuesForZonalAndMedical(billClassificationKey,claimType,isDomicillary);
		
		List<SelectValue> selectValueList = selectValueContainer.getItemIds();
		
		
		
		List<SelectValue> finalCategoryList = new ArrayList<SelectValue>();
		List<SelectValue> protaCategoryList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> categoryValueContainer = null;
		if(null != selectValueList && !selectValueList.isEmpty())
		{
			categoryValueContainer = new BeanItemContainer<SelectValue>(
						SelectValue.class);
			for (SelectValue selectValue : selectValueList) {	
				if(!ReferenceTable.getPrePostNatalMap().containsKey(selectValue.getId()))
				{
					if(ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(productKey)){
						if(billClassificationKey.equals(12L)){
							if(selectValue.getId().equals(ReferenceTable.COMPASSIONATE_TRAVEL) 
									|| selectValue.getId().equals(ReferenceTable.PREFERRED_NETWORK_HOSPITAL)){
								
								finalCategoryList.add(selectValue);
							}
						}
						else
						{
							finalCategoryList.add(selectValue);
						}
					}	
					else if(ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(productKey)){
						if(billClassificationKey.equals(12L)){
							if(selectValue.getId().equals(ReferenceTable.SHARED_ACCOMODATION)){
								finalCategoryList.add(selectValue);
							}
						}else{
							finalCategoryList.add(selectValue);
						}
					}else if(ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(productKey)){
						if(billClassificationKey.equals(12L)){
							if(!selectValue.getId().equals(ReferenceTable.COMPASSIONATE_TRAVEL)){
								finalCategoryList.add(selectValue);
							}
						}else{
							finalCategoryList.add(selectValue);
						}
					}
					else if(intimationObj != null && ((((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()) ||
							SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))
							|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
							|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))
								&& ("G").equalsIgnoreCase(intimationObj.getInsured().getPolicyPlan()))
								|| (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()) ||
										SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())))) {
							if(billClassificationKey.equals(12L)){
								if(selectValue.getId().equals(ReferenceTable.SHARED_ACCOMODATION)){
									finalCategoryList.add(selectValue);
								}
							}else{
								finalCategoryList.add(selectValue);
							}
					}// new added for GLX2020193
					else if(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(productKey)){
						if(billClassificationKey.equals(12L)){
							if(!selectValue.getId().equals(ReferenceTable.PREFERRED_NETWORK_HOSPITAL)){
								finalCategoryList.add(selectValue);
							}
						}else{
							finalCategoryList.add(selectValue);
						}
					}
					else{
						finalCategoryList.add(selectValue);
					}
				}
				//added for GMC prorata Calculation
				if(intimationObj != null && !(ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
						|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))){
					protaCategoryList.add(selectValue);
					if(billClassificationKey.equals(8L)){
						if(selectValue.getId().equals(ReferenceTable.OTHERS_WITH_PRORORTIONATE_DEDUCTION) 
								|| selectValue.getId().equals(ReferenceTable.OTHERS_WITHOUT_PRORORTIONATE_DEDUCTION)){
							finalCategoryList.remove(selectValue);

						}
						if(selectValue.getId().equals(ReferenceTable.OTHERS_WITH_PRORORTIONATE_DEDUCTION) 
								|| selectValue.getId().equals(ReferenceTable.OTHERS_WITHOUT_PRORORTIONATE_DEDUCTION)){
							protaCategoryList.remove(selectValue);

						}
					}
				}
				//added for GMC prorata Calculation
				if(intimationObj != null && (ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))){
					protaCategoryList.add(selectValue);
					if(billClassificationKey.equals(8L)){
						if(selectValue.getValue().equalsIgnoreCase(SHAConstants.OTHERS)) {
							finalCategoryList.remove(selectValue);
						}
					}
				}
				// new added for GLX2020193
				if(intimationObj != null && !(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(intimationObj.getPolicy().getProduct().getKey()))){
					if(billClassificationKey.equals(12L)){
						if(selectValue.getId().equals(ReferenceTable.VALUABLE_SERVICE_PROVIDER)){
							finalCategoryList.remove(selectValue);
							protaCategoryList.remove(selectValue);
						}
					}
				}
			}
			categoryValueContainer.addAll(finalCategoryList);
			
			if(intimationObj != null && !(ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
					|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))){
				selectValueContainer.removeAllItems();
				selectValueContainer.addAll(protaCategoryList);
			}
		}
		/**
		 * For star wedding gift insurance , policy pre and post
		 * natal shouldn't be addded in drop down list. Hence below condition is
		 * added , based on which drop down data with or without 
		 * pre and post natal would be added. 
		 * 
		 * This change could've been done in master service. The reason
		 * for not doing there is, domicillary based check was available and
		 * that was respective to claim type. Instead of tampering that code, 
		 * the condition was handled in presenter level.
		 * 
		 * */
		if((ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey) || (ReferenceTable.getComprehensiveProducts().containsKey(productKey))) && (!ReferenceTable.getMaternityMap().containsKey(subCoverValue)) ||
				ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(productKey) || (ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
						|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
						|| ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(productKey))) 
			view.setUpCategoryValues(categoryValueContainer);
		else	
			view.setUpCategoryValues(selectValueContainer);
		}
	
	public void saveBillEntryValues(
			@Observes @CDIEvent(CLAIM_REQUEST_SAVE_BILL_ENTRY) final ParameterDTO parameters) {
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		reimbursementService.billEntrySave(bean);
		viewBillSummaryPage.init(bean,bean.getKey(), true);
		Map<String, String> payableAmount = viewBillSummaryPage.getPayableAmount();
		Double sumValue = 0d;
		String string = payableAmount.get(SHAConstants.HOSPITALIZATION);
		sumValue = SHAUtils.getDoubleValueFromString(string);
		
		bean =  SHAUtils.roomRentNursingMapping(bean, 8l, 9l, false,false);
		bean = SHAUtils.roomRentNursingMapping(bean, 10l, 11l, true,false);
		bean =  SHAUtils.roomRentNursingMapping(bean, 85l, 84l, false,true);
		reimbursementService.saveRoomRentNursingMapping(bean, bean.getRoomRentMappingDTOList());
		//added by noufel fro GMC prop CR
		if(!(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
			dbCalculationService.getBillDetailsSummary(bean.getKey());
		} 
		else if((bean.getProrataDeductionFlag() != null && bean.getProrataDeductionFlag().equalsIgnoreCase("N")) &&
				(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
			dbCalculationService.getBillDetailsSummary(bean.getKey());
		}
		
		setPreAndPostHopitalizationAmount(bean);
		
		view.setBillEntryAmountConsiderValue(sumValue);
    }
	
	public void ruleForCheckInvestigation(@Observes @CDIEvent(CHECK_INVESTIGATION_INITIATED) final ParameterDTO parameters) {
		Long claimKey = (Long) parameters.getPrimaryParameter();
		if(claimKey != null) {
//			Investigation checkInitiateInvestigation = preauthService.checkInitiateInvestigation(claimKey);
//			view.setInvestigationRule(checkInitiateInvestigation);
			
			Boolean isInvPending = preauthService.checkInvestigationPending(claimKey);
			view.setInvestigationRule(isInvPending);
		}
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void setReferenceDataClear(
			@Observes @CDIEvent(REFERENCE_DATA_CLEAR) final ParameterDTO parameters) {
		SHAUtils.setClearReferenceData(referenceData);
		
	}
	
	private PreauthDTO setPreAndPostHopitalizationAmount(PreauthDTO preauthDTO) {
		List<PreHospitalisation> preHospitalisationValues = createRodService
				.getPreHospitalisationList(preauthDTO.getKey());
		List<PostHospitalisation> postHospitalisationValues = createRodService
				.getPostHospitalisationList(preauthDTO.getKey());
		Integer postHospitalizationAmt = 0;
		Integer preHospitalizationAmt = 0;
		for (PostHospitalisation postHospitalisation : postHospitalisationValues) {
			postHospitalizationAmt += postHospitalisation
					.getClaimedAmountBills() != null ? postHospitalisation
					.getClaimedAmountBills().intValue() : 0;
		}

		for (PreHospitalisation preHospitalisation : preHospitalisationValues) {
			preHospitalizationAmt += preHospitalisation.getClaimedAmountBills() != null ? preHospitalisation
					.getClaimedAmountBills().intValue() : 0;
		}
		preauthDTO.getPreauthMedicalDecisionDetails().setBillingRemarks("");
		preauthDTO.setPreHospitalisationValue(String
				.valueOf(preHospitalizationAmt));
		preauthDTO.setPostHospitalisationValue(String
				.valueOf(postHospitalizationAmt));

		return preauthDTO;
	}
	
	public void submitAutoSkipFVR(
			@Observes @CDIEvent(CLAIM_REQUEST_AUTO_SKIP_FVR) final ParameterDTO parameters) {
		// Boolean status = (Boolean) parameters.getPrimaryParameter();
		PreauthDTO uploadDTO = (PreauthDTO) parameters
				.getPrimaryParameter();
		FieldVisitRequest fvrobjByRodKey = fvrService.getPendingFieldVisitByClaimKey(uploadDTO.getClaimDTO().getKey());
		
		if((fvrobjByRodKey  == null || (fvrobjByRodKey != null && fvrobjByRodKey.getStatus() != null && ReferenceTable.INITITATE_FVR.equals(fvrobjByRodKey.getStatus().getKey())))){
		
			if(fvrobjByRodKey  != null ){
				dbCalculationService.invokeProcedureAutoSkipFVR(fvrobjByRodKey.getFvrId());
				if(uploadDTO.getIsFvrInitiate()){
				fvrService.autoSkipFirstFVR(fvrobjByRodKey);
				}else{
					fvrService.autoSkipNoFirstFVR(fvrobjByRodKey);
				}
			}
			}
		

	}
	
	//IMSSUPPOR-24086
	public void generateGradingBasedOnFieldVisit(@Observes @CDIEvent(MA_FIELD_VISIT_GRADING) final ParameterDTO parameters)
	{
		PreauthDTO reimbursementDTO = (PreauthDTO) parameters
				.getPrimaryParameter();
		
		Reimbursement reimbursementObjectByKey = reimbursementService.getReimbursementByKey(reimbursementDTO.getKey());
		
		List<FieldVisitRequest> fvrByClaimKey = reimbursementService
				.getFVRByClaimKey(reimbursementObjectByKey.getClaim().getKey());

		List<FVRGradingMaster> fvrGradingSegB = reimbursementService
				.getFVRGradingBySegment(SHAConstants.FVR_GRADING_SEGMENT_B);

		List<FVRGradingMaster> fvrGradingSegC = reimbursementService
				.getFVRGradingBySegment(SHAConstants.FVR_GRADING_SEGMENT_C);

		List<FvrGradingDetailsDTO> dto = new ArrayList<FvrGradingDetailsDTO>();
		// Map<Integer, FieldVisitRequest> valueMap = new HashMap<Integer,
		// FieldVisitRequest>();
		FvrGradingDetailsDTO fvrDto = null;
		List<NewFVRGradingDTO> FVRTableDTO = null;
		for (int i = 0; i < fvrByClaimKey.size(); i++) {
			FieldVisitRequest fieldVisitRequest = fvrByClaimKey.get(i);
			if (fieldVisitRequest.getStatus() != null
					&& (fieldVisitRequest.getStatus().getKey()
							.equals(ReferenceTable.FVR_REPLY_RECEIVED)
							|| fieldVisitRequest.getStatus().getKey()
									.equals(ReferenceTable.FVR_GRADING_STATUS) || fieldVisitRequest
							.getStatus().getKey()
							.equals(ReferenceTable.ASSIGNFVR))) {
				fvrDto = new FvrGradingDetailsDTO();
				fvrDto.setKey(fieldVisitRequest.getKey());
				fvrDto.setRepresentativeName(fieldVisitRequest
						.getRepresentativeName());
				fvrDto.setRepresentiveCode(fieldVisitRequest
						.getRepresentativeCode());
				if (fieldVisitRequest.getStatus() != null
						&& (fieldVisitRequest.getStatus().getKey()
								.equals(ReferenceTable.FVR_REPLY_RECEIVED) || fieldVisitRequest
								.getStatus().getKey()
								.equals(ReferenceTable.FVR_GRADING_STATUS))) {
					fvrDto.setIsFVRReceived(Boolean.TRUE);
				}

				FVRTableDTO = new ArrayList<NewFVRGradingDTO>();
				NewFVRGradingDTO eachFVRDTO = null;
				Boolean isSegmentABEdit = true;
				Boolean isSegmentCEdit = true;
				List<FVRGradingDetail> fvrGradingDetails = reimbursementService
						.getFVRGradingDetails(fieldVisitRequest.getKey());

				List<FvrTriggerPoint> fvrTriggerPoints = reimbursementService
						.getFVRTriggerPoints(fieldVisitRequest.getKey());

				for (FvrTriggerPoint masterFVR : fvrTriggerPoints) {
					eachFVRDTO = new NewFVRGradingDTO();
					eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
					eachFVRDTO.setCategory(masterFVR.getRemarks());
					eachFVRDTO.setSegment(SHAConstants.FVR_GRADING_SEGMENT_A);

					if (fvrGradingDetails != null
							&& !fvrGradingDetails.isEmpty()) {
						for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
							if (fvrGradingDetail.getSeqNo() != null) {
								if (fvrGradingDetail.getSeqNo().equals(
										masterFVR.getKey())) {
									eachFVRDTO
											.setKey(fvrGradingDetail.getKey());
									if (fvrGradingDetail.getGrading() != null) {
										eachFVRDTO
												.setStatusFlagSegmentA(fvrGradingDetail
														.getGrading());
										isSegmentCEdit = false;
										isSegmentABEdit = true;
									}
									break;
								}
							}

						}
					}
					FVRTableDTO.add(eachFVRDTO);
				}

				for (FVRGradingMaster masterFVR : fvrGradingSegB) {
					eachFVRDTO = new NewFVRGradingDTO();
					eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
					eachFVRDTO.setCategory(masterFVR.getGradingType());
					eachFVRDTO.setSegment(SHAConstants.FVR_GRADING_SEGMENT_B);

					if (fvrGradingDetails != null
							&& !fvrGradingDetails.isEmpty()) {
						for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
							if (fvrGradingDetail.getSeqNo() != null) {
								if (fvrGradingDetail.getSeqNo().equals(
										masterFVR.getKey())) {
									eachFVRDTO
											.setKey(fvrGradingDetail.getKey());
									if (fvrGradingDetail.getGrading() != null) {
										eachFVRDTO
												.setStatusFlag(fvrGradingDetail
														.getGrading());
										isSegmentCEdit = false;
										isSegmentABEdit = true;
									}
									break;
								}
							}
						}
					}
					FVRTableDTO.add(eachFVRDTO);
				}

				if (fieldVisitRequest.getStatus().getKey()
						.equals(ReferenceTable.FVR_REPLY_RECEIVED)) {

					for (FVRGradingMaster masterFVR : fvrGradingSegC) {
						eachFVRDTO = new NewFVRGradingDTO();
						eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
						if (masterFVR.getKey().intValue() == 22) {
							eachFVRDTO.setIsFVRReceived(Boolean.TRUE);
							// eachFVRDTO.setStatusFlagSegmentC(SHAConstants.YES_FLAG);
						}
						eachFVRDTO.setCategory(masterFVR.getGradingType());
						eachFVRDTO
								.setSegment(SHAConstants.FVR_GRADING_SEGMENT_C);

						if (fvrGradingDetails != null
								&& !fvrGradingDetails.isEmpty()) {
							for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
								if (fvrGradingDetail.getSeqNo() != null) {
									if (fvrGradingDetail.getSeqNo().equals(
											masterFVR.getKey())) {
										eachFVRDTO.setKey(fvrGradingDetail
												.getKey());
										if (masterFVR.getKey().intValue() != 22
												&& fvrGradingDetail
														.getGrading() != null) {
											// Fix for populate any segment C
											// other then "FVR not received" if
											// fvr replied
											eachFVRDTO
													.setStatusFlagSegmentC(fvrGradingDetail
															.getGrading());
											isSegmentCEdit = true;
											isSegmentABEdit = false;
										}
										break;
									}
								}
							}
						}

						FVRTableDTO.add(eachFVRDTO);
					}

				} else if (fieldVisitRequest.getStatus().getKey()
						.equals(ReferenceTable.ASSIGNFVR)) {
					for (FVRGradingMaster masterFVR : fvrGradingSegC) {
						eachFVRDTO = new NewFVRGradingDTO();
						eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
						eachFVRDTO.setCategory(masterFVR.getGradingType());
						eachFVRDTO
								.setSegment(SHAConstants.FVR_GRADING_SEGMENT_C);
						eachFVRDTO.setIsFVRReceived(Boolean.FALSE);

						if (fvrGradingDetails != null
								&& !fvrGradingDetails.isEmpty()) {
							for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
								if (fvrGradingDetail.getSeqNo() != null) {
									if (fvrGradingDetail.getSeqNo().equals(
											masterFVR.getKey())) {
										if (eachFVRDTO
												.getFvrSeqNo()
												.equals(ReferenceTable.GRADING_FVR_NOT_RECEIVED)) {
											eachFVRDTO.setKey(fvrGradingDetail
													.getKey());
										}
									}
								}
							}
						}

						// V1.4
						if (eachFVRDTO.getFvrSeqNo().equals(
								ReferenceTable.GRADING_FVR_NOT_RECEIVED)) {
							isSegmentCEdit = true;
							isSegmentABEdit = false;
							eachFVRDTO
									.setStatusFlagSegmentC(SHAConstants.YES_FLAG);
							eachFVRDTO.setIsAssignFVRNotReceived(Boolean.TRUE);
						}
						FVRTableDTO.add(eachFVRDTO);
					}
				} else {

					for (FVRGradingMaster masterFVR : fvrGradingSegC) {
						eachFVRDTO = new NewFVRGradingDTO();
						eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
						eachFVRDTO.setCategory(masterFVR.getGradingType());
						eachFVRDTO
								.setSegment(SHAConstants.FVR_GRADING_SEGMENT_C);
						if (masterFVR.getKey().intValue() == 22) {
							eachFVRDTO.setIsFVRReceived(Boolean.TRUE);
						} else {
							eachFVRDTO.setIsFVRReceived(Boolean.FALSE);
						}

						if (fvrGradingDetails != null
								&& !fvrGradingDetails.isEmpty()) {
							for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
								if (fvrGradingDetail.getSeqNo() != null) {
									if (fvrGradingDetail.getSeqNo().equals(
											masterFVR.getKey())) {
										eachFVRDTO.setKey(fvrGradingDetail
												.getKey());
										if (fvrGradingDetail.getGrading() != null) {
											eachFVRDTO
													.setStatusFlagSegmentC(fvrGradingDetail
															.getGrading());
											isSegmentCEdit = true;
											isSegmentABEdit = false;
										}
										break;
									}
								}
							}
						}

						FVRTableDTO.add(eachFVRDTO);
					}

				}

				// V1.4
				/*
				 * if(fieldVisitRequest.getStatus().getKey().equals(ReferenceTable
				 * .ASSIGNFVR) ||
				 * fieldVisitRequest.getStatus().getKey().equals(ReferenceTable
				 * .FVR_GRADING_STATUS)){ isSegmentABEdit = Boolean.FALSE;
				 * isSegmentCEdit = Boolean.FALSE; }
				 */
				if (fieldVisitRequest.getStatus().getKey()
						.equals(ReferenceTable.ASSIGNFVR)) {
					fvrDto.setIsClearAllEnabled(Boolean.FALSE);
				}

				if (FVRTableDTO != null && !FVRTableDTO.isEmpty()) {

					for (NewFVRGradingDTO gradingDto : FVRTableDTO) {
						if (isSegmentABEdit && !isSegmentCEdit) {
							gradingDto.setIsEditAB(true);
						} else if (!isSegmentABEdit && isSegmentCEdit) {
							gradingDto.setIsEditAB(false);
						}/*
						 * else if(!isSegmentABEdit && !isSegmentCEdit){
						 * gradingDto.setIsEditABC(Boolean.FALSE); }
						 */
					}
				}

				if (isSegmentABEdit && !isSegmentCEdit) {
					fvrDto.setIsSegmentANotEdit(false);
					fvrDto.setIsSegmentBNotEdit(false);
					fvrDto.setIsSegmentCNotEdit(true);
				} else if (!isSegmentABEdit && isSegmentCEdit) {
					fvrDto.setIsSegmentANotEdit(true);
					fvrDto.setIsSegmentBNotEdit(true);
					fvrDto.setIsSegmentCNotEdit(false);
				}/*
				 * else if(!isSegmentABEdit && !isSegmentCEdit){
				 * fvrDto.setIsSegmentANotEdit(true);
				 * fvrDto.setIsSegmentBNotEdit(true);
				 * fvrDto.setIsSegmentCNotEdit(true); }
				 */

				fvrDto.setNewFvrGradingDTO(FVRTableDTO);
				
				if (fieldVisitRequest.getGradingRmrks() != null) {
					fvrDto.setGradingRemarks(fieldVisitRequest
							.getGradingRmrks());
				}

				if (fieldVisitRequest.getStatus().getKey()
						.equals(ReferenceTable.FVR_REPLY_RECEIVED)) {
					fvrDto.setIsFVRReplied(Boolean.TRUE);
				}
				
				
				dto.add(fvrDto);
			}
		}

		reimbursementDTO.getPreauthMedicalDecisionDetails().setFvrGradingDTO(
				dto);

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
				
				 if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)){
					balanceSI = dbCalculationService.getBalanceSIForGMC(
							policyKey,insuredKey, preauthDTO.getClaimDTO().getKey());
				 }else{
					 /*balanceSI = dbCalculationService.getBalanceSI(
							policyKey,insuredKey, preauthDTO.getClaimDTO().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured(),preauthDTO.getNewIntimationDTO().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);*/
					 //code added for cr modern sublimit SI by noufel
					 if(preauthDTO.getIsBalSIForSublimitCardicSelected()){
						 balanceSI = dbCalculationService.getBalanceSIForCardicCarePlatRemb(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey(),preauthDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
					 }
					 else if(preauthDTO.getIsModernSublimitSelected()){
						 balanceSI = dbCalculationService.getBalanceSIForModernSublimit(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey(),preauthDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
					 }
					 else {
						 balanceSI = dbCalculationService.getBalanceSIForReimbursement(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey(),preauthDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
					 }
				 }
				
				preauthDTO.setBalanceSI(SHAUtils.getHospOrPartialAppAmt(preauthDTO, reimbursementService, balanceSI));
				balanceSI = preauthDTO.getBalanceSI();
				
				Reimbursement reimbursementByKey = ackDocReceivedService.getReimbursementByKey(preauthDTO.getKey());
				
				if(reimbursementByKey != null && reimbursementByKey.getDocAcknowLedgement() != null){
					
					Boolean isSettled = reimbursementService.isSettledPaymentAvailable(reimbursementByKey.getRodNumber());
					
					DocAcknowledgement docAcknowLedgement = reimbursementByKey.getDocAcknowLedgement();
					if(isSettled){
						if(reimbursementByKey.getReconsiderationRequest() != null && reimbursementByKey.getReconsiderationRequest().equalsIgnoreCase("Y") 
								&& docAcknowLedgement.getPaymentCancellationFlag() != null && docAcknowLedgement.getPaymentCancellationFlag().equalsIgnoreCase("N")){
							Double alreadyPaidAmt = 0d;
							List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = ackDocReceivedService.getReimbursementCalculationDetails(preauthDTO.getKey());
							for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
								if(reimbursementCalCulationDetails2.getPayableInsuredAfterPremium() != null){
									alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableInsuredAfterPremium();
								}
							}
							
							preauthDTO.setBalanceSI(preauthDTO.getBalanceSI()+alreadyPaidAmt);
							
						}
					}
					
				}
	
				view.setBalanceSIforRechargedProcess(balanceSI);
				
		}

	public void invsReviewRemarksTableList(
			@Observes @CDIEvent(INVS_REVIEW_REMARKS_LIST) final ParameterDTO parameters) {
		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		List<AssignedInvestigatiorDetails> invsReviewRemarksList = reimbursementService.getInvsReviewRemarksDetails(preauthDTO);
		preauthDTO.getPreauthMedicalDecisionDetails().setInvsReviewRemarksTableList(invsReviewRemarksList);
		
		}
	public void topUpPolicyInsert(@Observes @CDIEvent(MA_TOPUP_POLICY_EVENT) final ParameterDTO parameters) {
		
		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
//		PremPolicyDetails policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUppolicyNo(), String.valueOf(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUpInsuredId()));
//		Boolean isIntegratedPolicy =premiaPullService.populateGMCandGPAPolicy(policyDetails, String.valueOf(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUpInsuredId()),false);
		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("topupPolicy", premiaPullService.getPolicyByPolicyNubember(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUppolicyNo()));
//		map.put("topupInsured", premiaPullService.getInsuredByPolicyAndInsuredNameForDefault(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUppolicyNo(), preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUpInsuredId()));
//		Insured curentPolicy = preauthService.getTopUpPolicy(preauthdto.getPolicyKey());
//		String topUpPolicyNo = curentPolicy.getTopUppolicyNo();
		List<PreauthDTO> map = dbCalculationService.getTopUpPolicyDetailsForRiskName(preauthdto.getPolicyDto().getPolicyNumber(),
				0L,preauthdto.getNewIntimationDTO().getInsuredPatient().getKey(),preauthdto.getIntimationKey());
		String screenName = "GMC_TOPUP";
		view.generateTopUpLayout(map,screenName);
	}
	
	
	public void topUpPolicyIntimation(@Observes @CDIEvent(MA_TOPUP_POLICY_INTIMATION_EVENT) final ParameterDTO parameters) {
		String intimationNo = null;
		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
//		Map<String, Object> topupData = (Map<String, Object>) parameters.getSecondaryParameters()[0];
//		Policy topUpPolicy = (Policy)topupData.get("topupPolicy");
//    	Insured topUpInsured = (Insured)topupData.get("topupInsured");
//		
//    	String intimationNo = dbCalculationService.getTopupPolicyIntimation(preauthdto.getNewIntimationDTO().getPolicy().getPolicyNumber(), preauthdto.getNewIntimationDTO().getIntimationId(), preauthdto.getNewIntimationDTO().getKey(), topUpPolicy.getKey(), topUpInsured.getKey(), topUpInsured.getInsuredId());
		List<PreauthDTO> topupData = (List<PreauthDTO>) parameters.getSecondaryParameters()[0];
		String riskId = (String)parameters.getSecondaryParameters()[1];
		String isHcActionClicked = (String)parameters.getSecondaryParameters()[2];
		Policy topUpPolicyNo = policyService.getByPolicyNumber(topupData.get(0).getTopUPPolicyNumber());
		Insured topUpInsured = policyService.getInsuredByPolicyAndInsuredName(topupData.get(0).getTopUPPolicyNumber(),Long.valueOf(riskId));
    	if(isHcActionClicked != null && isHcActionClicked.equalsIgnoreCase("HC_TOPUP")){
    		 intimationNo = dbCalculationService.getHospitalCashTopupPolicyIntimationByReimbursement(preauthdto.getNewIntimationDTO().getPolicy().getPolicyNumber(), preauthdto.getNewIntimationDTO().getIntimationId(), preauthdto.getNewIntimationDTO().getKey(), 
        			topUpPolicyNo.getKey(), topUpInsured.getKey(), Long.valueOf(riskId));	
    	}else {
		 intimationNo = dbCalculationService.getTopupPolicyIntimationByReimbursement(preauthdto.getNewIntimationDTO().getPolicy().getPolicyNumber(), preauthdto.getNewIntimationDTO().getIntimationId(), preauthdto.getNewIntimationDTO().getKey(), 
    			topUpPolicyNo.getKey(), topUpInsured.getKey(), Long.valueOf(riskId));
    	}
		view.generateTopUpIntimationLayout(intimationNo);
	}
	
	public void setBalanceSumInsuredAlert(
			@Observes @CDIEvent(CLAIM_REQUEST_APPROVE_BSI_ALERT) final ParameterDTO parameters) {
		view.setBalanceSumInsuredAlert();
	}
	
	public void processICACMaSumbit(@Observes @CDIEvent(MA_ICAC_SUBMIT_EVENT) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();

		try {
			if(preauthDTO != null){
				reimbursementService.submitperauthMaICACProcess(preauthDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void generateClaimRequestHoldLayout(@Observes @CDIEvent(CLAIM_REQUEST_HOLD_EVENT) final ParameterDTO parameters)
	{
		view.generateHoldLayout();
	}
	
	public void topUpPolicyInsertForHospitalCash(@Observes @CDIEvent(MA_HOSPITAL_CASH_TOPUP_POLICY_EVENT) final ParameterDTO parameters) {

		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		List<PreauthDTO> map = new ArrayList<PreauthDTO>();
		Policy policy = policyService.getPolicy(preauthdto.getClaimDTO().getNewIntimationDto().getPolicy().getTopupPolicyNo());
		if(null != policy)
		{
			if(!ReferenceTable.getGMCProductList().containsKey(preauthdto.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				String proposerName =  policy.getProposerFirstName();
				List<Insured> insuredList = policy.getInsured();
				for (int i = 0; i < insuredList.size(); i++) {
					PreauthDTO mappedValues = new PreauthDTO();
					Insured insured = insuredList.get(i);
					mappedValues.setTopUpInsuredName(insured.getInsuredName());
					mappedValues.setTopUpInsuredNo(insured.getInsuredId());
					mappedValues.setTopUpProposerName(proposerName);
					mappedValues.setTopUPPolicyNumber(policy.getPolicyNumber());
					mappedValues.setTopUpInsuredage(insured.getInsuredAge());
					mappedValues.setTopUpInsuredDOB(insured.getInsuredDateOfBirth());
					map.add(mappedValues);
				}
			}
//			List<PreauthDTO> map = dbCalculationService.getTopUpPolicyDetailsForRiskName(preauthdto.getPolicyDto().getPolicyNumber(),
//					0L,preauthdto.getNewIntimationDTO().getInsuredPatient().getKey(),preauthdto.getIntimationKey());
			String screenName = "HC_TOPUP";
			view.generateTopUpLayout(map,screenName);
		}
	}
}
