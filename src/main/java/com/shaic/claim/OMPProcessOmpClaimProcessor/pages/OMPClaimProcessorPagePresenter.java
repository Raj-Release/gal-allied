package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import com.shaic.claim.OMPClaimPayment;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPNegotiationDetailsDTO;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.Country;
import com.shaic.domain.Currency;
import com.shaic.domain.MasOmbudsman;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPCloseReopenClaim;
import com.shaic.domain.OMPDocumentDetails;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.omp.OMPBenefitCover;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.omp.OMPNegotiation;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Window;

@ViewInterface(OMPProcessOmpClaimProcessorPageWizard.class)
public class OMPClaimProcessorPagePresenter extends AbstractMVPPresenter<OMPProcessOmpClaimProcessorPageWizard>{


	protected static final String OMP_NEGOTIATE = "omp negotiate";
	protected static final String OMP_REJECTION = "omp rejection";
	protected static final String OMP_APPROVAL = "omp approval";
	protected static final String OMP_CLAIM_PROCESSOR_SUBMIT = "omp claim processor submit";
	protected static final String OMP_CLAIM_PROCESSOR_SAVE = "omp claim processor save";
	public static final String OMP_PROCESSOR_PARTICULARS ="omp processor particulars";
	protected static final String OMP_Processor_BSI = "OMP_Processor_BSI";
	protected static final String OMP_PROCESSOR_CREATE_ROD = "OMP_Create_ROD";
	public static final String OMP_PROCESS_NEGOTIATION_SAVE = "omp_processor_nego_save";
	public static final String OMP_PROSESS_RECOVERABLE_SAVE = "omp_processor_recover_save";
	public static final String OMP_PROCESS_PAYMENT_SAVE = "omp_payment_details_save";
	public static final String OMP_CLAIM_PROCESSOR_FETCH_NEGO = "omp_processor_nego_select";
	protected static final String OMP_CLAIM_PROCESSOR_FETCH_NEGO_FROM_LIST = "omp_processor_nego_select_from_list";
	public static final String OMP_CANCEL_PROCESSOR = "omp_cancel_processor";
	protected static final String OMP_PROCESS_SHOW_REJECTION_REMARKS = "omp_processor_rejection_remark";
	protected static final String OMP_PROCESS_SHOW_PROCESSOR_REMARKS = "omp_processor_processor_remark";
	public static final String OMP_PROCESSOR_FETCH_UPLOAD_DIC = "omp_processor_fetch_upload_doc";
	public static final String OMP_PROCESS_NEGOTIATION_FETCH = "omp_processor_nego_fetch";
	public static final String OMP_PROCESS_PAYMENT_FETCH = "omp_processor_payment_fetch";
	public static final String OMP_PROCESS_RECOVERY_FETCH = "omp_processor_payment_fetch";
	public static final String OMP_CLAIM_PROCESSOR_PAYMENT_CURRENCY = "omp_processor_payment_currency";
	public static final String OMP_GET_OMBUDSMAN_DETAILS = "Get Ombudsman Details for OMP claim processor";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private OMPClaimService ompClaimService;
	
	@EJB
	private OMPProcessRODBillEntryService rodBillentryService;
	
	@EJB
	private OMPIntimationService ompIntimationService;
	
	@EJB
	private ReimbursementService  reimbursementService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void generateFieldsForNegotiate(@Observes @CDIEvent(OMP_NEGOTIATE) final ParameterDTO parameters)
	{
		HorizontalLayout horizontalLayout = (HorizontalLayout) parameters.getPrimaryParameter();
		Object[] seconObjects = parameters.getSecondaryParameters();
		BeanItemContainer<SelectValue> negotiatorName = null;
		if(seconObjects.length >0 && seconObjects!=null){
			 negotiatorName = (BeanItemContainer<SelectValue>) parameters.getSecondaryParameters()[0];
		}
		view.generateFieldsOnNegotiate(horizontalLayout , negotiatorName);
	}

	public void generateFieldsForRejection(@Observes @CDIEvent(OMP_REJECTION) final ParameterDTO parameters)
	{
		HorizontalLayout horizontalLayout = (HorizontalLayout) parameters.getPrimaryParameter();
		view.generateFieldsForRejection(horizontalLayout);
	}
	
	public void generateFieldsForApproval(@Observes @CDIEvent(OMP_APPROVAL) final ParameterDTO parameters)
	{
		HorizontalLayout horizontalLayout = (HorizontalLayout) parameters.getPrimaryParameter();
		view.generateFieldsOnApproval(horizontalLayout);
	}
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(OMP_CLAIM_PROCESSOR_SUBMIT) final ParameterDTO parameters) {
			OMPClaimProcessorDTO ompClaimProcessorDTO = (OMPClaimProcessorDTO) parameters.getPrimaryParameter();
			OMPClaim claim = ompClaimService.getClaimByKey(ompClaimProcessorDTO.getClaimDto().getKey());
			
			OMPReimbursement ompReimbursement =null;
			ompReimbursement = rodBillentryService.saveReimbursement(ompClaimProcessorDTO, claim, null);
			ompClaimProcessorDTO.setRodKey(ompReimbursement.getKey());
		String outCome = ompClaimProcessorDTO.getOutCome();
		if(outCome!=null){
			Object[] paramter = rodBillentryService.getParamter(ompClaimProcessorDTO,claim,outCome, ompReimbursement);
			Object[] inputArray = (Object[])paramter[0];
			inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.SOURCE_OMP_CLAIM_PROCESSOR;
			DBCalculationService dbCalculationService = new DBCalculationService();
			dbCalculationService.initiateOMPTaskProcedure(paramter);
			
			Stage stgObj = masterService.getStageBykey(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
			Status statusObj = null;
			if(SHAConstants.OUTCOME_FOR_OMP_PROCESSOR_TO_NEGO.equalsIgnoreCase(outCome) && ompReimbursement.getKey() != null){
				
				statusObj = masterService.getStatusByKey(ReferenceTable.ZONAL_REVIEW_REFER_TO_COORDINATOR_STATUS);
			}

			if(SHAConstants.OUTCOME_FOR_OMP_PROCESSOR.equalsIgnoreCase(outCome) && ompReimbursement.getKey() != null){
				statusObj = masterService.getStatusByKey(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS);
			}
			
			if(SHAConstants.OUTCOME_FOR_OMP_APPROVER.equalsIgnoreCase(outCome) && ompReimbursement.getKey() != null){  // Needs to be clarified for out come of rejection
				
//				statusObj = masterService.getStatusByKey(ReferenceTable.);   // Needs to be clarified for rejection status
			}
			rodBillentryService.updateReimbursement(ompReimbursement.getKey(), stgObj, statusObj,null);		
			
		}
		//view.buildSuccessLayout();
	}
	
	@SuppressWarnings({ "static-access", "unused" })
	public void saveWizard(
			@Observes @CDIEvent(OMP_CLAIM_PROCESSOR_SAVE) final ParameterDTO parameters) {
		OMPClaimProcessorDTO ompClaimProcessorDTO = (OMPClaimProcessorDTO) parameters.getPrimaryParameter();
		OMPClaim claim = ompClaimService.getClaimByKey(ompClaimProcessorDTO.getClaimDto().getKey());
		
		if(claim!=null && claim.getIntimation()!=null && claim.getIntimation().getKey()!=null){
			OMPIntimation intimation = ompIntimationService.getIntimationByKey(claim.getIntimation().getKey());
			if(intimation!= null){
				intimation.setModifiedBy(ompClaimProcessorDTO.getUserName());
				rodBillentryService.saveOrUpdateIntimation(ompClaimProcessorDTO,intimation);
				rodBillentryService.insertOrUpdate(intimation);
			}
		}
		
		if(ompClaimProcessorDTO.getCgOption() != null){
			claim.setCgOption(ompClaimProcessorDTO.getCgOption());
			claim.setDateOfCashGuarantee(ompClaimProcessorDTO.getCgDate());
			claim.setCgAmount(ompClaimProcessorDTO.getCgApprovedAmt());
			claim.setCgRemarks(ompClaimProcessorDTO.getCgRemarks());
		}
		
		OMPCloseReopenClaim closeReopenClaim = rodBillentryService.getCloseClaim(claim.getKey());
		if(claim!= null){

			
			if(ompClaimProcessorDTO.getUserName() != null){
				claim.setModifiedBy(ompClaimProcessorDTO.getUserName());
			}
			claim.setModifiedDate(new Date());			

			rodBillentryService.saveOrUpdateClaim(ompClaimProcessorDTO ,claim);
			rodBillentryService.insertOrUpdate(claim);
		}
		
		if(ompClaimProcessorDTO.getStatusKey()!= null && ompClaimProcessorDTO.getStatusKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)){
			claim.setInrConversionRate(0d);
			claim.setInrTotalAmount(0d);
			claim.setDollarInitProvisionAmount(0d);
			claim.setCurrentProvisionAmount(0d);
			claim.setProvisionAmount(0d);
			Status statusObj = null;
			statusObj = masterService.getStatusByKey(ompClaimProcessorDTO.getStatusKey());
			claim.setStatus(statusObj);
			rodBillentryService.insertOrUpdate(claim);
			
			rodBillentryService.saveClosedClaim(closeReopenClaim,claim,ompClaimProcessorDTO);
			if(closeReopenClaim!= null){/*
				closeReopenClaim.setClosedDate(new Timestamp(System.currentTimeMillis()));
//				closeReopenClaim.setStage(stage);
				closeReopenClaim.setStatus(statusObj);//we have to confirm
				rodBillentryService.insertOrUpdate(closeReopenClaim,ompClaimProcessorDTO);
			*/}
		}else{
			if(ompClaimProcessorDTO.getStatusKey()!= null && ompClaimProcessorDTO.getStatusKey().equals(ReferenceTable.CLAIM_REOPENED_STATUS)){
				Status statusObj = null;
				statusObj = masterService.getStatusByKey(ompClaimProcessorDTO.getStatusKey());
				claim.setStatus(statusObj);
				claim.setInrConversionRate(ompClaimProcessorDTO.getInrConversionRate());
				claim.setInrTotalAmount(ompClaimProcessorDTO.getInrtotal());
				claim.setDollarInitProvisionAmount(ompClaimProcessorDTO.getProvisionAmt());
				claim.setCurrentProvisionAmount(ompClaimProcessorDTO.getProvisionAmt());
				claim.setProvisionAmount(ompClaimProcessorDTO.getProvisionAmt());
				rodBillentryService.insertOrUpdate(claim);
				rodBillentryService.saveClosedClaim(closeReopenClaim,claim,ompClaimProcessorDTO);
				if(closeReopenClaim!= null){/*
					closeReopenClaim.setReOpenDate(new Timestamp(System.currentTimeMillis()));
					closeReopenClaim.setStatus(statusObj);//we have to confirm
//					closeReopenClaim.setStage(stage);
					rodBillentryService.insertOrUpdate(closeReopenClaim,ompClaimProcessorDTO);
				*/}
			}
		}
			OMPReimbursement reimbursement =null;
			 
			 List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable = ompClaimProcessorDTO.getClaimCalculationViewTable();
			 if(claimCalculationViewTable!= null){
				 rodBillentryService.saveReimbursementBilldetails(ompClaimProcessorDTO, claim,claimCalculationViewTable,SHAConstants.OMP_PROCESSOR);
				 rodBillentryService.updateNegotaion(claimCalculationViewTable);
		}else{
			reimbursement = rodBillentryService.saveReimbursement(ompClaimProcessorDTO, claim, null);
		}
		view.buildSuccessLayout();
	}

	
	
	public void setReferenceData(@Observes @CDIEvent(OMP_PROCESSOR_PARTICULARS) final ParameterDTO parameters)
	{/*
		Map<String, Object> referenceDataMap =  (Map<String, Object>) parameters.getPrimaryParameter();
		referenceDataMap.put("particulars", masterService.getOMPDocumentCheckListValuesContainer(SHAConstants.OMP_OMPDOC));
		referenceDataMap.put("receivedStatus", masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOCRECSTS));
		referenceDataMap.put("category", masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_CATEGORY));
		view.setReferenceDate(referenceDataMap);
	*/

		Map<String, Object> referenceDataMap =  (Map<String, Object>) parameters.getPrimaryParameter();
		if(parameters.getSecondaryParameters()!=null && parameters.getSecondaryParameters().length >0){
			String typeCode = (String) parameters.getSecondaryParameters()[0];
			if(typeCode!=null){
				referenceDataMap.put("category", masterService.getListMasterValuebyTypeCode(typeCode));
			}
		}
		referenceDataMap.put("particulars", masterService.getOMPDocumentCheckListValuesContainer(SHAConstants.OMP_OMPDOC));
		referenceDataMap.put("receivedStatus", masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOCRECSTS));
		view.setReferenceDate(referenceDataMap);
	
	}
	
	public void setBalanceSumInsured(@Observes @CDIEvent(OMP_Processor_BSI) final ParameterDTO parameters)
	{
		OMPClaimProcessorDTO claimProcessorDTO = (OMPClaimProcessorDTO) parameters.getPrimaryParameter();
		OMPReimbursement reimbursement =null;
		List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable = claimProcessorDTO.getClaimCalculationViewTable();
		if(claimCalculationViewTable!=null){
			for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : claimCalculationViewTable) {
				 reimbursement = ompIntimationService.getReimbursementByKey(ompClaimCalculationViewTableDTO.getRodKey());
			}
		}
		//OMPClaim claimsByIntimationNumber = ompClaimService.getClaimsByIntimationNumber(claimProcessorDTO.getIntimationId());
		Object[] seconObjects = parameters.getSecondaryParameters();
		SelectValue negotiatorName = null;
		if(seconObjects.length >0 && seconObjects!=null){
			 negotiatorName = (SelectValue) parameters.getSecondaryParameters()[0];
		}
		MastersEvents listMasterEventBykey = masterService.getListMasterEventBykey(negotiatorName.getId());
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		//OMPIntimation intimation = reimbursement.getClaim().getIntimation();
		//Policy policy = reimbursement.getClaim().getIntimation().getPolicy();
		//Product product = policy.getProduct();
		//String plan = intimation.getInsured().getPlan();
		Policy policy = claimProcessorDTO.getPolicy();
		Product product = policy.getProduct();
		String plan = claimProcessorDTO.getNewIntimationDto().getPlan();
		//String plan =policy.getPolicyPlan();
		
		Double sumInsured = dbCalculationService.getOmpInsuredSumInsured(policy.getKey(), listMasterEventBykey.getEventCode());
		Double orginalSI = policy.getTotalSumInsured();
		Map<String, Double> ompDeductible = dbCalculationService.getOmpDeductible(product.getKey(), plan, sumInsured, listMasterEventBykey.getEventCode(),orginalSI);
		Double deductibles = (Double)ompDeductible.get(SHAConstants.DEDUCTIBLES);
		claimProcessorDTO.setDeductibles(deductibles);
		if(reimbursement==null){
			Map balanceSIMap = dbCalculationService.getOmpBalanceSI(policy.getKey() , claimProcessorDTO.getNewIntimationDto().getInsuredKey(),
					claimProcessorDTO.getClaimDto().getKey(),null, sumInsured,claimProcessorDTO.getNewIntimationDto().getKey(), listMasterEventBykey.getEventCode());
			Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
			claimProcessorDTO.setBalanceSI(balanceSI);
			claimProcessorDTO.setSumInsured(balanceSI);
			claimProcessorDTO.setDeductiblesOriginal(deductibles);
		}else{
			Double balanceSI = reimbursementService.getBalanceSI(reimbursement, reimbursement.getClaim(), listMasterEventBykey.getEventCode());
			claimProcessorDTO.setBalanceSI(balanceSI);
			claimProcessorDTO.setSumInsured(balanceSI);
		}
		
		
		//claimProcessorDTO.getCalculationSheetDTO().setBalanceSumInured(sumInsured);
		//claimProcessorDTO.getCalculationSheetDTO().setDeductiblePerPolicy(deductibles);
	}
	
	@SuppressWarnings("static-access")
	public void saveNegotiation(
			@Observes @CDIEvent(OMP_PROCESS_NEGOTIATION_SAVE) final ParameterDTO parameters) {
		OMPClaimProcessorDTO ompClaimProcessorDTO = (OMPClaimProcessorDTO) parameters.getPrimaryParameter();
		OMPClaim claim = ompClaimService.getClaimByKey(ompClaimProcessorDTO.getClaimDto().getKey());
//		Window popUp = (Window)parameters.getSecondaryParameters()[0];
		Object[] seconObjects = parameters.getSecondaryParameters();
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = null;
		if(seconObjects.length >0 && seconObjects!=null){
			calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getSecondaryParameters()[0];
		}
		Long rodKey = calculationViewTableDTO.getRodKey();
		OMPReimbursement ompReimbursement = rodBillentryService.getReimbursement(rodKey);
//		 benefitCover = rodBillentryService.saveBenefitCover(ompClaimProcessorDTO, benefitCover);
		List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = calculationViewTableDTO.getNegotiationDetailsDTOs();
		if(negotiationDetailsDTOs!= null){
			for (OMPNegotiationDetailsDTO ompNegotiationDetailsDTO : negotiationDetailsDTOs) {
				OMPNegotiation negotiation =null;
				if(ompNegotiationDetailsDTO.getKey()==null){
					 negotiation = new OMPNegotiation();
				}else{
					 negotiation = rodBillentryService.getOMPNegotiationbyKey(ompNegotiationDetailsDTO.getKey());
				}	
					negotiation.setRodKey(ompReimbursement);
					negotiation.setNegotiatorName(ompNegotiationDetailsDTO.getNameOfNegotiatior());
					negotiation.setClaim(claim);
					negotiation.setApprovedAmount(ompNegotiationDetailsDTO.getApprovedAmt());
					negotiation.setAggredAmount(ompNegotiationDetailsDTO.getAgreedAmount());
					negotiation.setNegotiationCompletedDate(ompNegotiationDetailsDTO.getNegotiationCompletDate());
					negotiation.setNegotiationRemarks(ompNegotiationDetailsDTO.getNegotiationRemarks());
					negotiation.setNegotiationRequestedDate(ompNegotiationDetailsDTO.getNegotiationReqstDate());
					negotiation.setIntimation(claim.getIntimation());
					negotiation.setExpenseAmountusd(ompNegotiationDetailsDTO.getExpenseAmt());
					negotiation.setDiffAmountusd(ompNegotiationDetailsDTO.getDiffAmt());
					negotiation.setHandlingChargsUsd(ompNegotiationDetailsDTO.getHandlingCharges());
					if(ompNegotiationDetailsDTO.getAgreedAmount()!=null){
						if(calculationViewTableDTO!= null){
							calculationViewTableDTO.setAfternegotiation(ompNegotiationDetailsDTO.getAgreedAmount());
						}
					}
					rodBillentryService.setSaveNegotiation(negotiation);
					ompNegotiationDetailsDTO.setKey(negotiation.getKey());
			}
		}
//		view.buildSaveLayout();
		}
	
	@SuppressWarnings("static-access")
	public void saveRecoverableSave(
			@Observes @CDIEvent(OMP_PROSESS_RECOVERABLE_SAVE) final ParameterDTO parameters) {
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		Long rodKey = calculationViewTableDTO.getRodKey();
		OMPReimbursement ompReimbursement = rodBillentryService.getReimbursement(rodKey);
		
		rodBillentryService.saveRecovarables(calculationViewTableDTO, ompReimbursement);
	}

	
	
	@SuppressWarnings("static-access")
	public void savePaymentDetails(
			@Observes @CDIEvent(OMP_PROCESS_PAYMENT_SAVE) final ParameterDTO parameters) {
		OMPClaimCalculationViewTableDTO ompClaimProcessorDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		Long rodKey = ompClaimProcessorDTO.getRodKey();
		OMPReimbursement ompReimbursement = rodBillentryService.getReimbursement(rodKey);
		rodBillentryService.savePayment(ompClaimProcessorDTO, ompReimbursement);
//		view.buildSaveLayout();
	}

		
	
	@SuppressWarnings("static-access")
	public void fetchNegotiationSelect(
			@Observes @CDIEvent(OMP_CLAIM_PROCESSOR_FETCH_NEGO) final ParameterDTO parameters) {
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		Long claimKey = calculationViewTableDTO.getClaimkey();
		List<OMPNegotiation> ompNegotiationsList = rodBillentryService.getOMPNegotiationbyClaimKey(claimKey);
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(ompNegotiationsList!= null){
			for (OMPNegotiation ompNegotiation : ompNegotiationsList) {
				SelectValue select = new SelectValue();
				OMPReimbursement rodKey = ompNegotiation.getRodKey();
				if(rodKey!=null){
					select.setId(ompNegotiation.getKey());
					Double total =0d;
					if(ompNegotiation.getExpenseAmountusd()!=null && ompNegotiation.getHandlingChargsUsd()!=null){
						total = ompNegotiation.getExpenseAmountusd() + ompNegotiation.getHandlingChargsUsd();
					}
					select.setValue(rodKey.getRodNumber() + "-"+ total);
					selectValuesList.add(select);
				}
			}mastersValueContainer.addAll(selectValuesList);
		}
		calculationViewTableDTO.setSelectNegoContainer(mastersValueContainer);
	}
	
	
	@SuppressWarnings("static-access")
	public void fetchOneNegotiationSelect(
			@Observes @CDIEvent(OMP_CLAIM_PROCESSOR_FETCH_NEGO_FROM_LIST) final ParameterDTO parameters) {
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		OMPNegotiation ompNegotiation = rodBillentryService.getOMPNegotiationbyKey(calculationViewTableDTO.getNegokey());
		calculationViewTableDTO.setApprovedAmt(ompNegotiation.getApprovedAmount());
		calculationViewTableDTO.setAgreedAmt(ompNegotiation.getAggredAmount());
		calculationViewTableDTO.setDifferenceAmt(ompNegotiation.getDiffAmountusd());
		calculationViewTableDTO.setHandlingCharges(ompNegotiation.getHandlingChargsUsd());
		calculationViewTableDTO.setExpenses(ompNegotiation.getExpenseAmountusd());
		calculationViewTableDTO.setTotalExp(ompNegotiation.getExpenseAmountusd() + ompNegotiation.getHandlingChargsUsd());
	}
	
	public void cancelProcessor(@Observes @CDIEvent(OMP_CANCEL_PROCESSOR) final ParameterDTO parameters) {
		view.cancelIntimation();
	}
	
	public void generateFieldsForApproveRemark(@Observes @CDIEvent(OMP_PROCESS_SHOW_PROCESSOR_REMARKS) final ParameterDTO parameters)
	{
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		
		view.generateFieldsOnApprove(calculationViewTableDTO);
	}
			
	public void generateFieldsForRejectionRemark(@Observes @CDIEvent(OMP_PROCESS_SHOW_REJECTION_REMARKS) final ParameterDTO parameters)
	{
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		
		view.generateFieldsOnRejectionRemark(calculationViewTableDTO);
	}
	
	public void fecthUploadDoc(@Observes @CDIEvent(OMP_PROCESSOR_FETCH_UPLOAD_DIC) final ParameterDTO parameters)
	{
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		
		if(calculationViewTableDTO.getRodnumber()!=null){
			ReceiptOfDocumentsDTO receiptOfDocumentsDTO = new ReceiptOfDocumentsDTO();
			List<UploadDocumentDTO> uploadDocsList = new ArrayList<UploadDocumentDTO>();
			List<OMPDocumentDetails> documentDetailsByRodNumber = rodBillentryService.getDocumentDetailsByRodNumber(calculationViewTableDTO.getRodnumber());
			if(documentDetailsByRodNumber!=null){
			for (OMPDocumentDetails ompDocumentDetails : documentDetailsByRodNumber) {
				UploadDocumentDTO documentDTO = new UploadDocumentDTO();
				documentDTO.setRodNo(ompDocumentDetails.getReimbursementNumber());
				documentDTO.setRodKey(calculationViewTableDTO.getRodKey());
				documentDTO.setFileName(ompDocumentDetails.getFileName());
				documentDTO.setFileTypeValue(ompDocumentDetails.getFileType());
				if(ompDocumentDetails.getDocumentToken()!=null){
					documentDTO.setDmsDocToken(ompDocumentDetails.getDocumentToken().toString());
				}
				documentDTO.setDmsToken(ompDocumentDetails.getDocumentToken());
				documentDTO.setIntimationNo(ompDocumentDetails.getIntimationNumber());
				documentDTO.setRodKey(calculationViewTableDTO.getRodKey());
				documentDTO.setRodNo(calculationViewTableDTO.getRodnumber());
				documentDTO.setDocumentTypeValue(ompDocumentDetails.getDocumentType());
				documentDTO.setDocReceivedDate(ompDocumentDetails.getDocRecievedDate());
				documentDTO.setReceivStatusValue(ompDocumentDetails.getRecievedStatus());
				documentDTO.setNoOfItems(ompDocumentDetails.getNoOfDoc());
				documentDTO.setRemarks(ompDocumentDetails.getRemarks());
				if(ompDocumentDetails.getCreatedDate()!=null){
					String dateWithoutTime = SHAUtils.getDateWithoutTime(ompDocumentDetails.getCreatedDate());
					documentDTO.setUpdatedOn(dateWithoutTime);
				}
				uploadDocsList.add(documentDTO);
			}}
			receiptOfDocumentsDTO.setUploadDocsList(uploadDocsList);
			calculationViewTableDTO.setReceiptOfDocumentsDTO(receiptOfDocumentsDTO);
		}
	}
	
	@SuppressWarnings("static-access")
	public void getNegotiation(
			@Observes @CDIEvent(OMP_PROCESS_NEGOTIATION_FETCH) final ParameterDTO parameters) {
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		if(calculationViewTableDTO.getRodKey()!=null){
			List<OMPNegotiation> ompNegotiation = rodBillentryService.getOMPNegotiation(calculationViewTableDTO.getRodKey());
			List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = new ArrayList<OMPNegotiationDetailsDTO>();
			if(ompNegotiation!=null){
				for (OMPNegotiation ompNegotiation2 : ompNegotiation) {
					OMPNegotiationDetailsDTO negotiationDetailsDTO = new OMPNegotiationDetailsDTO();
					negotiationDetailsDTO.setKey(ompNegotiation2.getKey());
					negotiationDetailsDTO.setAgreedAmount(ompNegotiation2.getAggredAmount());
					negotiationDetailsDTO.setApprovedAmt(ompNegotiation2.getApprovedAmount());
					negotiationDetailsDTO.setNameOfNegotiatior(ompNegotiation2.getNegotiatorName());
					negotiationDetailsDTO.setNegotiationCompletDate(ompNegotiation2.getNegotiationCompletedDate());
					negotiationDetailsDTO.setNegotiationRemarks(ompNegotiation2.getNegotiationRemarks());
					negotiationDetailsDTO.setNegotiationReqstDate(ompNegotiation2.getNegotiationRequestedDate());
					negotiationDetailsDTO.setDiffAmt(ompNegotiation2.getDiffAmountusd());
					negotiationDetailsDTO.setExpenseAmt(ompNegotiation2.getExpenseAmountusd());
					negotiationDetailsDTO.setHandlingCharges(ompNegotiation2.getHandlingChargsUsd());
					if(ompNegotiation2.getRodKey()!=null){
						negotiationDetailsDTO.setRodKey(ompNegotiation2.getRodKey().getKey());
					}
					if(ompNegotiation2.getClaim()!=null){
						negotiationDetailsDTO.setClaimKey(ompNegotiation2.getClaim().getKey());
					}
					negotiationDetailsDTOs.add(negotiationDetailsDTO);
				}
			}
			calculationViewTableDTO.setNegotiationDetailsDTOs(negotiationDetailsDTOs);
	}
	}
	
	
	@SuppressWarnings("static-access")
	public void getRecoveryDetails(
			@Observes @CDIEvent(OMP_PROCESS_RECOVERY_FETCH) final ParameterDTO parameters) {
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		if(calculationViewTableDTO.getRodKey()!=null){
			OMPReimbursement ompreimbursement = rodBillentryService.getReimbursement(calculationViewTableDTO.getRodKey());
			List<OMPNewRecoverableTableDto> recovList = new ArrayList<OMPNewRecoverableTableDto>();
			OMPNewRecoverableTableDto  recoverableDto = new OMPNewRecoverableTableDto();
			recoverableDto.setDateofRecovery(ompreimbursement.getRecoveredDate());
			if(ompreimbursement.getRecoveredAmountInr()!= null){
				double recoinr = ompreimbursement.getRecoveredAmountInr().doubleValue();
				recoverableDto.setAmountRecoveredInr(recoinr);
			}
			if(ompreimbursement.getRecoveredAmountUsd()!= null){
				double recoUsd = ompreimbursement.getRecoveredAmountUsd().doubleValue();
				recoverableDto.setAmountRecoveredUsd(recoUsd);
			}
			recoverableDto.setSendToAccounts(ompreimbursement.getSendToAccounts());
			recoverableDto.setRodKey(ompreimbursement.getKey());
			recoverableDto.setRemarks(ompreimbursement.getRemarks());
			recovList.add(recoverableDto);
			calculationViewTableDTO.setOmpRecoverableTableList(recovList);
		}
	}
	
	
	
	@SuppressWarnings("static-access")
	public void getpaymentDetails(
			@Observes @CDIEvent(OMP_PROCESS_PAYMENT_FETCH) final ParameterDTO parameters) {
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		if(calculationViewTableDTO.getRodKey()!=null){
			OMPReimbursement ompreimbursement = rodBillentryService.getReimbursement(calculationViewTableDTO.getRodKey());
			List<OMPPaymentDetailsTableDTO> paymentListDto = new ArrayList<OMPPaymentDetailsTableDTO>();
			
			OMPPaymentDetailsTableDTO paymentDto = new OMPPaymentDetailsTableDTO();
			BeanItemContainer<SelectValue> paymentTo = masterService.getMastersValuebyTypeCodeOnStaatus(SHAConstants.OMP_OMP_PAYTO);
			BeanItemContainer<SelectValue> paymentMode = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_PAY_MODE);
			calculationViewTableDTO.setPaymentToContainer(paymentTo);
			calculationViewTableDTO.setPaymentModeContainer(paymentMode);
			paymentDto.setPanNo(ompreimbursement.getPanNumber());
			paymentDto.setPayableAt(ompreimbursement.getPayableAt());
			paymentDto.setPayeeNameStr(ompreimbursement.getPayeeName());
			paymentDto.setEmailId(ompreimbursement.getEmailId());
			Currency payeeCurrencyTypeId = ompreimbursement.getPayeeCurrencyTypeId();
			if(payeeCurrencyTypeId != null && payeeCurrencyTypeId.getKey() != null){
				SelectValue currencyValue = new SelectValue();
				currencyValue.setId(payeeCurrencyTypeId.getKey());
				currencyValue.setValue(payeeCurrencyTypeId.getCurrencyCode());
				paymentDto.setCurrency(currencyValue);
			}
			SelectValue paymentValue = null; 
			MastersValue paymentToMast = ompreimbursement.getPaymentTo();
			if(paymentToMast != null && paymentToMast.getKey() != null){
				paymentValue = new SelectValue();
				paymentValue.setId(paymentToMast.getKey());
				paymentValue.setValue(paymentToMast.getValue());
				paymentDto.setPaymentTo(paymentValue);
			}
			if(ompreimbursement.getPaymentModeId()!= null){
				
				MastersValue master = rodBillentryService.getMaster(ompreimbursement.getPaymentModeId());
				SelectValue value = null;
				if(master!= null && master.getKey()!= null){
					value = new SelectValue();
					value.setId(master.getKey());
					value.setValue(master.getValue());
					paymentDto.setPayMode(value);
					
				}
			}
//					paymentDto.setPaymentStatus(ompreimbursement.getPatientStatus().getValue());
			//IMSSUPPOR-27192
			OMPClaimPayment claimPayment = ompIntimationService.getClaimPaymentByRODNo(ompreimbursement.getRodNumber());
            if(claimPayment!=null){
                    paymentDto.setTransectionChequeNo(claimPayment.getChequeDdNumber());
                    paymentDto.setPaymentDate(claimPayment.getPaymentdate());
                    if(claimPayment.getStatusId()!=null && claimPayment.getStatusId().getKey()!=null){
                            paymentDto.setPaymentStatus(claimPayment.getStatusId().getProcessValue());
                    }
                    //CR20181327 New fields added
                    paymentDto.setChequeDate(claimPayment.getChequeDdDate());
                    paymentDto.setConvRateInInr(claimPayment.getConversionRate() != null ? claimPayment.getConversionRate().toString() : "");
                    Double settledAmountDouble = null;
                    if(claimPayment.getClmSecCode() != null && claimPayment.getClmSecCode().equalsIgnoreCase("OMP Claim Related")) {
                    	settledAmountDouble = claimPayment.getTotApprovedAmt();
                    } else if(claimPayment.getClmSecCode() != null && claimPayment.getClmSecCode().equalsIgnoreCase("OMP Other Expenses")) {
                    	settledAmountDouble = claimPayment.getTotAmtINr();
                    } else if(claimPayment.getClmSecCode() != null && claimPayment.getClmSecCode().equalsIgnoreCase("Negotiator Fee")) {
                    	settledAmountDouble = claimPayment.getApprovedAmt();
                    }
                    paymentDto.setConvAmtInInr(settledAmountDouble != null ? settledAmountDouble.toString() : "");
                    paymentDto.setSettleAmt(settledAmountDouble != null ? settledAmountDouble.toString() : "");
                   //new columns in DB
                    paymentDto.setPostedDate(claimPayment.getPostedDate());
                    paymentDto.setBankChargeInInr(claimPayment.getBankCharges() != null ? claimPayment.getBankCharges().toString() : "");

            }

			
			paymentListDto.add(paymentDto);
			calculationViewTableDTO.setOmpPaymentDetailsList(paymentListDto);
		}
	}
	
	@SuppressWarnings("static-access")
	public void getPaymentCurrency(
			@Observes @CDIEvent(OMP_CLAIM_PROCESSOR_PAYMENT_CURRENCY) final ParameterDTO parameters) {
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
			if(calculationViewTableDTO.getRodClaimType()!=null){
				SelectValue rodClaimType = calculationViewTableDTO.getRodClaimType();
				if(rodClaimType.getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					BeanItemContainer<SelectValue> currencyValue = new BeanItemContainer<SelectValue>(SelectValue.class);
					Currency ompCurrency = rodBillentryService.getOMPCurrency(107L);
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(ompCurrency.getKey());
					selectValue1.setValue(ompCurrency.getCurrencyCode());
					currencyValue.addBean(selectValue1);
					
					if(calculationViewTableDTO.getRodKey()!=null){
					OMPReimbursement ompreimbursement = rodBillentryService.getReimbursement(calculationViewTableDTO.getRodKey());
					
					Currency currencyTypeId =null;
					if(ompreimbursement!=null){
					if(ompreimbursement.getPayeeCurrencyTypeId()!=null){
						currencyTypeId = ompreimbursement.getPayeeCurrencyTypeId();	
					}else{
						currencyTypeId = ompreimbursement.getCurrencyTypeId();	
					}}
					
					if(currencyTypeId!=null){
						SelectValue selectValue = new SelectValue();
						selectValue.setId(currencyTypeId.getKey());
						selectValue.setValue(currencyTypeId.getCurrencyCode());
						currencyValue.addBean(selectValue);
						if(calculationViewTableDTO.getOmpPaymentDetailsList()!=null && calculationViewTableDTO.getOmpPaymentDetailsList().size()>0){
							OMPPaymentDetailsTableDTO ompPaymentDetailsTableDTO = calculationViewTableDTO.getOmpPaymentDetailsList().get(0);
							ompPaymentDetailsTableDTO.setCurrency(selectValue);
						}
					}
					}else{
						if(calculationViewTableDTO.getOmpPaymentDetailsList()!=null && calculationViewTableDTO.getOmpPaymentDetailsList().size()>0){
							OMPPaymentDetailsTableDTO ompPaymentDetailsTableDTO = calculationViewTableDTO.getOmpPaymentDetailsList().get(0);
							currencyValue.addBean(calculationViewTableDTO.getCurrencyType());
							ompPaymentDetailsTableDTO.setCurrency(calculationViewTableDTO.getCurrencyType());
						}
					}
					calculationViewTableDTO.setCurrencyValueContainer(currencyValue);
				}else{
					BeanItemContainer<SelectValue> currencyValue = new BeanItemContainer<SelectValue>(SelectValue.class);
					Currency ompCurrency = rodBillentryService.getOMPCurrency(177L);
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(ompCurrency.getKey());
					selectValue1.setValue(ompCurrency.getCurrencyCode());
					currencyValue.addBean(selectValue1);
					calculationViewTableDTO.setInrCurrencyValueContainer(currencyValue);
					if(calculationViewTableDTO.getOmpPaymentDetailsList()!=null && calculationViewTableDTO.getOmpPaymentDetailsList().size()>0){
					OMPPaymentDetailsTableDTO ompPaymentDetailsTableDTO = calculationViewTableDTO.getOmpPaymentDetailsList().get(0);
					ompPaymentDetailsTableDTO.setCurrency(selectValue1);
					}
				}
			}
	}
	public void getOmbudsManDetails(
			@Observes @CDIEvent(OMP_GET_OMBUDSMAN_DETAILS) final ParameterDTO parameters) {
		
		OMPClaimProcessorDTO ompClaimProcessorDTO = (OMPClaimProcessorDTO) parameters.getPrimaryParameter();
		if (ompClaimProcessorDTO.getClaimDto().getNewIntimationDto().getPolicy().getHomeOfficeCode() != null) {
			 List<MasOmbudsman> ombudsmanOfficeList = getOmbudsmanOffiAddrByPIOCode(ompClaimProcessorDTO.getClaimDto().getNewIntimationDto().getPolicy().getHomeOfficeCode());
			 if(ombudsmanOfficeList !=null && !ombudsmanOfficeList.isEmpty()){
				 ompClaimProcessorDTO.getClaimDto().setOmbudsManAddressList(ombudsmanOfficeList);
			 }
		 }		
	}
	
	private List<MasOmbudsman> getOmbudsmanOffiAddrByPIOCode(String pioCode) {
		
		List<MasOmbudsman> ombudsmanDetailsByCpuCode = new ArrayList<MasOmbudsman>();
		if(pioCode != null){
			OrganaizationUnit branchOffice = ompClaimService
					.getInsuredOfficeNameByDivisionCode(pioCode);
			if (branchOffice != null) {
				String ombudsManCode = branchOffice.getOmbudsmanCode();
				if (ombudsManCode != null) {
					ombudsmanDetailsByCpuCode = masterService
							.getOmbudsmanDetailsByCpuCode(ombudsManCode);
				}
			}
		}
		return ombudsmanDetailsByCpuCode;
	}
}
