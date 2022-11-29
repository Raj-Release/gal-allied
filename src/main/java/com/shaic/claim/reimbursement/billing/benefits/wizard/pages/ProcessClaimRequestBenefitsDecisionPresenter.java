/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.pages;

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
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 */
@ViewInterface(ProcessClaimRequestBenefitsDecisionView.class)
public class ProcessClaimRequestBenefitsDecisionPresenter extends AbstractMVPPresenter<ProcessClaimRequestBenefitsDecisionView>  

{
	public static final String SETUP_COPAY_VALUES = "setup_copay_values";
	
	public static final String CLAIM_REQUEST_BENEFITS_DECISION_SETUP_DROPDOWN_VALUES = "claim_request_benefits_decision_setup_dropdown_values";
	
	public static final String BENEFITS_BILLING_REFERCOORDINATOR_EVENT = "benefits_billing_refer_to_coordinator_event";
	
	public static final String BENEFITS_BILLING_SEND_TO_FINANCIAL_EVENT = "benefits_billing_send_to_financial_event";
	
	public static final String BENEFITS_BILLING_CANCEL_ROD_EVENT="benefits_billing_cancel_rod_event";
	
	public static final String 	BENEFITS_FINANCIAL_REFERCOORDINATOR_EVENT = "benefits_financial_refer_to_coordinator_event";
	
	public static final String BENEFITS_FIANANCIAL_APPROVE_EVENT = "benefits_financial_approve_event";
	public static final String BENEFITS_FIANANCIAL_QUERY_EVENT = "benefits_financial_query_event";
	public static final String BENEFITS_FINANCIAL_CANCEL_ROD="benefits_financial_cancel_rod";
	public static final String BENEFITS_FIANANCIAL_REJECT_EVENT = "benefits_financial_reject_event";
	public static final String BENEFITS_FIANANCIAL_REFER_TO_BILLING_EVENT = "benefits_financial_refer_to_billing_event";
	public static final String BENEFITS_FINANCIAL_REFER_TO_MEDICAL_APPROVER_EVENT = "benefits_financial_refer_to_medical_event";

	//added for new product076
	public static final String MEDICAL_REQUEST_FVR_EVENT = "medical_request_fvr_event";
	public static final String MEDICAL_REQUEST_INVESTIGATION_EVENT = "medical_request_initiate_investigation_event";
	public static final String BENEFITS_MEDICAL_REQUEST_QUERY_EVENT = "medical_request_initiate_query_event";
	public static final String BENEFITS_MEDICAL_APPROVE_EVENT = "benefits_medical_approve_event";
	
	public static final String MEDICAL_REQUEST_AUTO_SKIP_FVR = "medical_request_auto_skip_fvr";
	public static final String SUBMIT_PARALLEL_BENEFIT_QUERY = "submit_parallel_query_benefit";
	public static final String BENEFITS_MEDICAL_CANCEL_ROD="benefits_medical_cancel_rod";
	public static final String BENEFITS_MEDICAL_REQUEST_REJECT_EVENT = "benefits_medical_request_reject_event";
	public static final String MA_BENEFIT_SUB_REJECT_CATEG_LAYOUT =  "Benefit Rejection Subcategory Layout for MA";
	
	public static final String 	BENEFITS_MEDICAL_REQUEST_REFERCOORDINATOR_EVENT = "benefits_medical_request_refer_to_coordinator_event";
	public static final String BENEFITS_MEDICAL_REQUEST_SENT_TO_REPLY_EVENT = "benefits_medical_request_sent_reply_event";
	
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private FieldVisitRequestService fvrService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	
	
	public void generateReferCoOrdinatorLayout(@Observes @CDIEvent(BENEFITS_BILLING_REFERCOORDINATOR_EVENT) final ParameterDTO parameters)
	{
		view.generateReferCoOrdinatorLayout(masterService.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
	}
	
	public void generateSendToFinancialLayout(@Observes @CDIEvent(BENEFITS_BILLING_SEND_TO_FINANCIAL_EVENT) final ParameterDTO parameters)
	{
		view.generateSendToFinancialLayout();
	}
	public void generateCancelRODLayout(@Observes @CDIEvent(BENEFITS_BILLING_CANCEL_ROD_EVENT) final ParameterDTO parameters)
	{
		view.generateCancelRODLayout(masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
	}
	
	
	public void generateReferCoOrdinatorLayoutForFA(@Observes @CDIEvent(BENEFITS_FINANCIAL_REFERCOORDINATOR_EVENT) final ParameterDTO parameters)
	{
		view.generateReferCoOrdinatorLayoutForFA(masterService.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
	}
	
	public void generateApproverLayoutForFA(@Observes @CDIEvent(BENEFITS_FIANANCIAL_APPROVE_EVENT) final ParameterDTO parameters)
	{
		view.generateApproveLayoutForFA();
	}
	
	public void generateQueryLayoutForFA(@Observes @CDIEvent(BENEFITS_FIANANCIAL_QUERY_EVENT) final ParameterDTO parameters)
	{
		view.generateQueryLayoutForFA();
	}
	public void generateCancelRODLayoutForFA(@Observes @CDIEvent(BENEFITS_FINANCIAL_CANCEL_ROD) final ParameterDTO parameters)
	{
		view.generateCancelRODLayoutForFA(masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
	}
	
	public void generateRejectLayoutForFA(@Observes @CDIEvent(BENEFITS_FIANANCIAL_REJECT_EVENT) final ParameterDTO parameters)
	{
		view.generateRejectLayoutForFA();
	}
	
	public void generateReferToBillingLayout(@Observes @CDIEvent(BENEFITS_FIANANCIAL_REFER_TO_BILLING_EVENT) final ParameterDTO parameters)
	{
		view.genertateReferToBillingLayoutForFA();
	}
	
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(SETUP_COPAY_VALUES) final ParameterDTO parameters) {
		
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		
		List<Double> copayValue = dbCalculationService.getProductCoPay(
				rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct()
						.getKey(), rodDTO.getClaimDTO().getNewIntimationDto()
						.getInsuredPatient().getKey(), rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId(),
						rodDTO.getNewIntimationDTO());
		
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		referenceDataMap.put("copayValue", copayValue);
		//view.setUpCoPayValues(referenceDataMap);
	
		}
	
	public void setUpDropDownValuess(
			@Observes @CDIEvent(CLAIM_REQUEST_BENEFITS_DECISION_SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
		
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		referenceDataMap.put("docReceivedFrom", masterService
				.getSelectValueContainer(ReferenceTable.ACK_DOC_RECEIVED_FROM));	
		referenceDataMap.put("billClaissificationDetails",validateBillClassification(rodDTO.getClaimDTO().getKey()));
		view.setUpDropDownValues(referenceDataMap);
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public DocumentDetailsDTO validateBillClassification(Long claimKey)
	{
		Reimbursement reimbursement = ackDocReceivedService.getLatestReimbursementDetails(claimKey);
		DocumentDetailsDTO docDTO = null;
		if(null != reimbursement)
		{
			DocAcknowledgement docAck= reimbursement.getDocAcknowLedgement();
			docDTO = new DocumentDetailsDTO();
			docDTO.setHospitalizationFlag(docAck.getHospitalisationFlag());
			docDTO.setPartialHospitalizationFlag(docAck.getPartialHospitalisationFlag());
			docDTO.setPreHospitalizationFlag(docAck.getPreHospitalisationFlag());
			docDTO.setPostHospitalizationFlag(docAck.getPostHospitalisationFlag());
			docDTO.setLumpSumAmountFlag(docAck.getLumpsumAmountFlag());
			docDTO.setAddOnBenefitsPatientCareFlag(docAck.getPatientCareFlag());
			docDTO.setAddOnBenefitsHospitalCashFlag(docAck.getHospitalCashFlag());
			docDTO.setHospitalCashFlag(docAck.getProdHospBenefitFlag());
		}
		
		return docDTO;
		
	}
	
	
	public void generateFieldVisitLayout(
			@Observes @CDIEvent(MEDICAL_REQUEST_FVR_EVENT) final ParameterDTO parameters) {

		PreauthDTO bean = (PreauthDTO)parameters.getPrimaryParameter();
		
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
		
		if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				||!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
						&& bean.getClaimDTO().getClaimType() != null
						&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(bean.getClaimDTO().getClaimType().getId())) {
			String icdCodeValue = null;
			if(bean.getPreauthDataExtractionDetails().getDiagnosisTableList() != null 
					&& !bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty() && (bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getDiagnosisName() != null || bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getIcdCode() != null)) {
				String icdCode = bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getIcdCode() != null ? bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getIcdCode().getValue() : "";
			
				String split[] = icdCode.split("-");
			
				if(split.length>0){
					icdCodeValue = split[split.length-1];  // bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getIcdCode().getCommonValue();
				}
				bean.setMulticlaimAvailFlag(dbCalculationService.getClaimRestrictionAvailable(bean.getNewIntimationDTO().getPolicy().getKey(),
						bean.getNewIntimationDTO().getInsuredPatient().getKey(),bean.getNewIntimationDTO().getKey(),
						icdCodeValue,
						bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getDiagnosisName().getValue()));
	
			}
		}	
		
		view.genrateFieldVisitLayoutForMA(masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO)
				,null,masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY),
				isAssigned, repName, repContactNo);
		
	}
	
	
	public void generateQueryLayoutForMA(@Observes @CDIEvent(BENEFITS_MEDICAL_REQUEST_QUERY_EVENT) final ParameterDTO parameters)
	{
		view.generateQueryLayoutForMA();
	}
	
	public void generateInvestigationLayoutForMA(
			@Observes @CDIEvent(MEDICAL_REQUEST_INVESTIGATION_EVENT) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		view.generateInvestigationLayoutForMA(preauthDTO.isDirectToAssignInv());
	}
	
	public void generateCancelRODLayoutForMA(
			@Observes @CDIEvent(BENEFITS_MEDICAL_CANCEL_ROD) final ParameterDTO parameters) {
		view.generateCancelRODLayoutForMA();
	}
	
	public void generateRejectLayoutForMA(
			@Observes @CDIEvent(BENEFITS_MEDICAL_REQUEST_REJECT_EVENT) final ParameterDTO parameters) {
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		 BeanItemContainer<SelectValue> dropDownValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		 
		if(dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId() != null && 
				dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getKey() != null && 
				ReferenceTable.SETTLED_RECONSIDERATION_ID.equals(dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getKey()))
			dropDownValueContainer = masterService.getSettledRejectionCategory();
		else	
			dropDownValueContainer = masterService.getReimbRejCategoryByValue();
		
		if(dto.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				(dto.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
						|| dto.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))){
			if(dto.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)){
				dropDownValueContainer = masterService.getReimbRejCategoryForCoronaFmlProd(dto.getNewIntimationDTO().getPolicy().getProduct().getKey() ,dto.getNewIntimationDTO().getPolicy().getPolicyPlan()); 
			}else if(dto.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)){
				dropDownValueContainer =  masterService.getReimbRejCategoryForCoronaFmlProd(dto.getNewIntimationDTO().getPolicy().getProduct().getKey(),dto.getNewIntimationDTO().getPolicy().getPolicyPlan()); 
			}
		}
		view.generateRejectLayoutForMA(dropDownValueContainer);
}
	
	public void generateApproverLayoutForMA(@Observes @CDIEvent(BENEFITS_MEDICAL_APPROVE_EVENT) final ParameterDTO parameters)
	{
		view.generateApproveLayoutForMA();
	}
	
	
	public void submitAutoSkipFVR(
			@Observes @CDIEvent(MEDICAL_REQUEST_AUTO_SKIP_FVR) final ParameterDTO parameters) {
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
	
	public void submitParallelQuery(
			@Observes @CDIEvent(SUBMIT_PARALLEL_BENEFIT_QUERY) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		reimbursementService.submitParallelQuery(preauthDTO);
	}
	
	public void generateRejSubCategoryLayout(
			@Observes @CDIEvent(MA_BENEFIT_SUB_REJECT_CATEG_LAYOUT) final ParameterDTO parameters) {

		Long rejCategId = (Long) parameters.getPrimaryParameter();
		
		BeanItemContainer<SelectValue> rejSubcategContainer = masterService.getRejSubcategContainer(rejCategId);
		
		view.setSubCategContainer(rejSubcategContainer);
	}
	
	public void generateReferToCoordinatorLayoutForMA(
			@Observes @CDIEvent(BENEFITS_MEDICAL_REQUEST_REFERCOORDINATOR_EVENT) final ParameterDTO parameters) {

		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		
		view.generateReferToCoordinatorLayoutForMA(masterService
				.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
	}
	
	public void generateReferToMedicalLayoutForFA(@Observes @CDIEvent(BENEFITS_FINANCIAL_REFER_TO_MEDICAL_APPROVER_EVENT) final ParameterDTO parameters)
	{
		view.generateReferToMedicalLayoutForFA();
	}
	
	public void generateSendReplyToFinancialLayout(@Observes @CDIEvent(BENEFITS_MEDICAL_REQUEST_SENT_TO_REPLY_EVENT) final ParameterDTO parameters)
	{
		view.generateSendReplyToFinancial();
	}
}
