package com.shaic.claim.OMPProcessOmpClaimApprover.pages;

import java.util.ArrayList;
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
import com.shaic.claim.OMPClaimPayment;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPNegotiationDetailsDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.Policy;
import com.shaic.domain.Product;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.omp.OMPNegotiation;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.HorizontalLayout;

@ViewInterface(OMPProcessOmpClaimApproverWizardView.class)
public class OMPProcessClaimApproverWizardPresenter extends AbstractMVPPresenter<OMPProcessOmpClaimApproverWizardView>{
	
	protected static final String OMP_NEGOTIATE = "omp approver negotiate";
	protected static final String OMP_REJECTION = "omp approver rejection";
	protected static final String OMP_APPROVAL = "omp approver approval";
	protected static final String OMP_CLAIM_APPROVER_SUBMIT = "omp claim approver submit";
	protected static final String OMP_CLAIM_APPROVER_SAVE = "omp claim approver save";
	protected static final String OMP_APPROVER_PARTICULARS = "omp approver particulars";
	protected static final String OMP_Approver_BSI = "OMP_Approver_BSI";
	protected static final String OMP_PROCESS_NEGOTIATION_SAVE = "omp_approver_nego_save";
	protected static final String OMP_CLAIM_PROCESSOR_FETCH_NEGO = "omp_approver_nego_select";
	protected static final String OMP_CLAIM_PROCESSOR_FETCH_NEGO_FROM_LIST = "omp_approver_nego_select_from_list";
	protected static final String OMP_CANCEL_APPROVER = "omp_cancel_approver";
	protected static final String OMP_PROCESS_SHOW_REJECTION_REMARKS = "omp_approve_rejection_remark";
	protected static final String OMP_PROCESS_SHOW_PROCESSOR_REMARKS = "omp_approve_processor_remark";
	protected static final String OMP_FA_SUBMIT = "omp_approve_submit_for_approve";
	
	
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
			@Observes @CDIEvent(OMP_CLAIM_APPROVER_SUBMIT) final ParameterDTO parameters) {
		OMPClaimProcessorDTO ompClaimProcessorDTO = (OMPClaimProcessorDTO) parameters.getPrimaryParameter();
		OMPClaim claim = ompClaimService.getClaimByKey(ompClaimProcessorDTO.getClaimDto().getKey());
		
		if(ompClaimProcessorDTO.getPatientStatus() != null){
			SelectValue sv = ompClaimProcessorDTO.getPatientStatus();
			claim.setPatientStatusId(sv.getId());
		}
		
		if(ompClaimProcessorDTO.getDeathDate() != null){
			claim.setDateOfDeath(ompClaimProcessorDTO.getDeathDate());
		}
		
		if(claim!=null && claim.getIntimation()!=null && claim.getIntimation().getKey()!=null){
			OMPIntimation intimation = ompIntimationService.getIntimationByKey(claim.getIntimation().getKey());
			if(intimation!= null){
				intimation.setModifiedBy(ompClaimProcessorDTO.getUserName());
				rodBillentryService.saveOrUpdateIntimation(ompClaimProcessorDTO,intimation);
				rodBillentryService.insertOrUpdate(intimation);
			}
		}
		
		if(claim!= null){
			claim.setModifiedBy(ompClaimProcessorDTO.getUserName());
			rodBillentryService.saveOrUpdateClaim(ompClaimProcessorDTO ,claim);
			rodBillentryService.insertOrUpdate(claim);
		}
		
		Long rodKey = ompClaimProcessorDTO.getRodKey();
		OMPClaimPayment claimpayment = null;
		OMPReimbursement reimbursement =null;
		 
		 List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable = ompClaimProcessorDTO.getClaimCalculationViewTable();
		 if(claimCalculationViewTable!= null){
			 rodBillentryService.saveReimbursementBilldetails(ompClaimProcessorDTO, claim,claimCalculationViewTable,SHAConstants.OMP_APPROVER);
			 rodBillentryService.updateNegotaion(claimCalculationViewTable);
		 }else{
			 reimbursement = rodBillentryService.saveReimbursement(ompClaimProcessorDTO, claim, null);
		 }
		view.buildSuccessLayout();
	}
	
	@SuppressWarnings("static-access")
	public void saveWizard(
			@Observes @CDIEvent(OMP_CLAIM_APPROVER_SAVE) final ParameterDTO parameters) {
		OMPClaimProcessorDTO ompClaimProcessorDTO = (OMPClaimProcessorDTO) parameters.getPrimaryParameter();
		OMPClaim claim = ompClaimService.getClaimByKey(ompClaimProcessorDTO.getClaimDto().getKey());
		if(claim!= null){
			claim.setModifiedBy(ompClaimProcessorDTO.getUserName());
			rodBillentryService.saveOrUpdateClaim(ompClaimProcessorDTO ,claim);
			rodBillentryService.insertOrUpdate(claim);
		}
		Long rodKey = ompClaimProcessorDTO.getRodKey();
		OMPReimbursement ompReimbursement =null;
		if(rodKey!=null){/*
			 ompReimbursement = rodBillentryService.getReimbursementByKey(rodKey);
			 ompReimbursement = rodBillentryService.saveReimbursement(ompClaimProcessorDTO, claim, ompReimbursement);
			 
				//my 
			 List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable = ompClaimProcessorDTO.getClaimCalculationViewTable();
			 for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : claimCalculationViewTable) {
				 OMPBenefitCover ompBenefitCoverByKey = rodBillentryService.getOMPBenefitCoverByBenefitKey(ompClaimCalculationViewTableDTO.getKey());
				 if(ompClaimCalculationViewTableDTO.getKey()!=null){
					 if(ompBenefitCoverByKey!=null){
						 rodBillentryService.saveBenefitCover(ompClaimCalculationViewTableDTO ,ompBenefitCoverByKey);
						 ompBenefitCoverByKey.setRodKey(ompReimbursement);
						 ompBenefitCoverByKey.setInsuredKey(ompReimbursement.getInsuredKey());
						 MastersEvents eventCodeId = ompReimbursement.getEventCodeId();
						 ompBenefitCoverByKey.setCovedId(eventCodeId.getKey());
						 ompBenefitCoverByKey.setClaimKey(claim);
						 rodBillentryService.insertOrUpdateBenefit(ompBenefitCoverByKey);
					 }
					 
				 }else{
					 ompBenefitCoverByKey = new OMPBenefitCover();
					 rodBillentryService.saveBenefitCover(ompClaimCalculationViewTableDTO , ompBenefitCoverByKey);
					 ompBenefitCoverByKey.setRodKey(ompReimbursement);
					 ompBenefitCoverByKey.setInsuredKey(ompReimbursement.getInsuredKey());
					 MastersEvents eventCodeId = ompReimbursement.getEventCodeId();
					 ompBenefitCoverByKey.setCovedId(eventCodeId.getKey());
					 ompBenefitCoverByKey.setClaimKey(claim);
					 rodBillentryService.insertOrUpdateBenefit(ompBenefitCoverByKey);
				 }
			}
			 //my 
			 
			 
		*/}else{
			 ompReimbursement = rodBillentryService.saveReimbursement(ompClaimProcessorDTO, claim, null);
		}
//		view.buildSaveLayout();
	}
	
	public void setReferenceData(@Observes @CDIEvent(OMP_APPROVER_PARTICULARS) final ParameterDTO parameters)
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

	
	public void setBalanceSumInsured(@Observes @CDIEvent(OMP_Approver_BSI) final ParameterDTO parameters)
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
					if(ompNegotiationDetailsDTO.getExpenseAmt()!=null){
						if(calculationViewTableDTO!= null){
							calculationViewTableDTO.setAfternegotiation(ompNegotiationDetailsDTO.getExpenseAmt() + ompNegotiationDetailsDTO.getHandlingCharges());
						}
					}
					
					rodBillentryService.setSaveNegotiation(negotiation);
			}
		}
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
		calculationViewTableDTO.setSelectNegoContainer(mastersValueContainer);
	}
	
	@SuppressWarnings("static-access")
	public void fetchOneNegotiationSelect(
			@Observes @CDIEvent(OMP_CLAIM_PROCESSOR_FETCH_NEGO_FROM_LIST) final ParameterDTO parameters) {
		OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		OMPNegotiation ompNegotiation = rodBillentryService.getOMPNegotiationbyKey(calculationViewTableDTO.getNegokey());
		if(ompNegotiation!=null){
		calculationViewTableDTO.setApprovedAmt(ompNegotiation.getApprovedAmount());
		calculationViewTableDTO.setAgreedAmt(ompNegotiation.getAggredAmount());
		calculationViewTableDTO.setDifferenceAmt(ompNegotiation.getDiffAmountusd());
		calculationViewTableDTO.setHandlingCharges(ompNegotiation.getHandlingChargsUsd());
		calculationViewTableDTO.setExpenses(ompNegotiation.getExpenseAmountusd());
		calculationViewTableDTO.setTotalExp(ompNegotiation.getExpenseAmountusd() + ompNegotiation.getHandlingChargsUsd());
		}
	}
	
	public void cancelProcessor(@Observes @CDIEvent(OMP_CANCEL_APPROVER) final ParameterDTO parameters) {
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
	
	
	@SuppressWarnings("static-access")
	public void submitFA(
			@Observes @CDIEvent(OMP_FA_SUBMIT) final ParameterDTO parameters) {
		
		OMPClaimProcessorDTO ompClaimProcessorDTO = (OMPClaimProcessorDTO) parameters.getPrimaryParameter();
		OMPClaim claim = ompClaimService.getClaimByKey(ompClaimProcessorDTO.getClaimDto().getKey());
				
		Long rodKey = ompClaimProcessorDTO.getRodKey();
		OMPClaimPayment claimpayment = null;
		OMPReimbursement reimbursement =null;
		if(ompClaimProcessorDTO!= null){
			List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable = ompClaimProcessorDTO.getClaimCalculationViewTable();
			if(claimCalculationViewTable!= null){
				if(ompClaimProcessorDTO.getRodNumber()!= null){
					reimbursement = rodBillentryService.getReimbursementByRodNo(ompClaimProcessorDTO.getRodNumber());
					//rodBillentryService.saveClaimpayment( ompClaimProcessorDTO , claimpayment, claim, reimbursement);
				}
			}
		}
		/* else{
			 reimbursement = rodBillentryService.saveReimbursement(ompClaimProcessorDTO, claim, null);
		 }*/
//		view.buildSuccessLayout();
	}
	
	
	
}
