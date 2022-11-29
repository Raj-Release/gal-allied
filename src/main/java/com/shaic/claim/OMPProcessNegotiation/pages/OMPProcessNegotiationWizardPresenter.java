package com.shaic.claim.OMPProcessNegotiation.pages;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPNegotiation;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.DBCalculationService;


@ViewInterface(OMPProcessNegotiationWizardView.class)
public class OMPProcessNegotiationWizardPresenter extends AbstractMVPPresenter<OMPProcessNegotiationWizardView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final String OMP_PROCESS_NEGOTIATION_SUBMIT = "omp claim negotiation submit";
	protected static final String OMP_PROCESS_NEGOTIATION_SAVE = "omp process negotiation save";
	protected static final String OMP_NEGOTATION_PARTICULARS = "omp negotation particulars";
	@EJB
	private MasterService masterService;
	
	@EJB
	private OMPClaimService ompClaimService;
	
	@EJB
	private OMPProcessRODBillEntryService rodBillentryService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(OMP_PROCESS_NEGOTIATION_SUBMIT) final ParameterDTO parameters) {
		OMPClaimProcessorDTO ompClaimProcessorDTO = (OMPClaimProcessorDTO) parameters.getPrimaryParameter();
		OMPClaim claim = ompClaimService.getClaimByKey(ompClaimProcessorDTO.getClaimDto().getKey());
		
		Long rodKey = ompClaimProcessorDTO.getRodKey();
		OMPReimbursement ompReimbursement = rodBillentryService.getReimbursement(rodKey);
//		 benefitCover = rodBillentryService.saveBenefitCover(ompClaimProcessorDTO, benefitCover);
		List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = ompClaimProcessorDTO.getNegotiationDetailsDTOs();
		for (OMPNegotiationDetailsDTO ompNegotiationDetailsDTO : negotiationDetailsDTOs) {
				if(ompNegotiationDetailsDTO.getKey()==null){
				OMPNegotiation negotiation = new OMPNegotiation();
				negotiation.setRodKey(ompReimbursement);
				negotiation.setNegotiatorName(ompNegotiationDetailsDTO.getNameOfNegotiatior());
				negotiation.setClaim(claim);
				negotiation.setApprovedAmount(ompNegotiationDetailsDTO.getApprovedAmt());
				negotiation.setAggredAmount(ompNegotiationDetailsDTO.getAgreedAmount());
				negotiation.setNegotiationCompletedDate(ompNegotiationDetailsDTO.getNegotiationCompletDate());
				negotiation.setNegotiationRemarks(ompNegotiationDetailsDTO.getNegotiationRemarks());
				negotiation.setNegotiationRequestedDate(ompNegotiationDetailsDTO.getNegotiationReqstDate());
				negotiation.setIntimation(claim.getIntimation());
				rodBillentryService.setSaveNegotiation(negotiation);
				}
				
		}
		
		System.out.println(negotiationDetailsDTOs);
		if(rodKey!=null){
		String outCome = SHAConstants.OUTCOME_FOR_OMP_NEGOTATION;				
		if(outCome!=null){
			Object[] paramter = rodBillentryService.getParamter(ompClaimProcessorDTO,claim,outCome, ompReimbursement);
			Object[] inputArray = (Object[])paramter[0];
			inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.SOURCE_OMP_CLAIM_NEGOTIATION;
			DBCalculationService dbCalculationService = new DBCalculationService();
			dbCalculationService.initiateOMPTaskProcedure(paramter);
			ompReimbursement = rodBillentryService.getReimbursementByKey(rodKey);
			Status statusObj = masterService.getStatusByKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS);
			Stage stgObj =  masterService.getStageBykey(ReferenceTable.CLAIM_REQUEST_STAGE);
			rodBillentryService.updateReimbursement(rodKey,stgObj,statusObj,null);
		}
		
		
//		rodBillentryService.getParamter(ompClaimProcessorDTO,claim,outCome, ompReimbursement);
		
		view.buildSuccessLayout();
		}
	}

	@SuppressWarnings("static-access")
	public void saveWizard(
			@Observes @CDIEvent(OMP_PROCESS_NEGOTIATION_SAVE) final ParameterDTO parameters) {
		OMPClaimProcessorDTO ompClaimProcessorDTO = (OMPClaimProcessorDTO) parameters.getPrimaryParameter();
		OMPClaim claim = ompClaimService.getClaimByKey(ompClaimProcessorDTO.getClaimDto().getKey());
		
		Long rodKey = ompClaimProcessorDTO.getRodKey();
		OMPReimbursement ompReimbursement = rodBillentryService.getReimbursement(rodKey);
//		 benefitCover = rodBillentryService.saveBenefitCover(ompClaimProcessorDTO, benefitCover);
		List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = ompClaimProcessorDTO.getNegotiationDetailsDTOs();
		for (OMPNegotiationDetailsDTO ompNegotiationDetailsDTO : negotiationDetailsDTOs) {
				if(ompNegotiationDetailsDTO.getKey()==null){
				OMPNegotiation negotiation = new OMPNegotiation();
				negotiation.setRodKey(ompReimbursement);
				negotiation.setNegotiatorName(ompNegotiationDetailsDTO.getNameOfNegotiatior());
				//negotiation.setClaim(claim);
				negotiation.setApprovedAmount(ompNegotiationDetailsDTO.getApprovedAmt());
				negotiation.setAggredAmount(ompNegotiationDetailsDTO.getAgreedAmount());
				negotiation.setNegotiationCompletedDate(ompNegotiationDetailsDTO.getNegotiationCompletDate());
				negotiation.setNegotiationRemarks(ompNegotiationDetailsDTO.getNegotiationRemarks());
				negotiation.setNegotiationRequestedDate(ompNegotiationDetailsDTO.getNegotiationReqstDate());
				negotiation.setIntimation(claim.getIntimation());
				rodBillentryService.setSaveNegotiation(negotiation);
				}
				
		}
		view.buildSaveLayout();
	}
	
	public void setReferenceData(@Observes @CDIEvent(OMP_NEGOTATION_PARTICULARS) final ParameterDTO parameters)
	{
		/*Map<String, Object> referenceDataMap =  (Map<String, Object>) parameters.getPrimaryParameter();
		referenceDataMap.put("particulars", masterService.getOMPDocumentCheckListValuesContainer(SHAConstants.OMP_OMPDOC));
		referenceDataMap.put("receivedStatus", masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOCRECSTS));
		referenceDataMap.put("category", masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_CATEGORY));
		view.setReferenceDate(referenceDataMap);*/
		
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
}
