package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

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
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.omp.OMPBenefitCover;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@ViewInterface(OMPProcessRODBillEntryPageWizard.class)
public class OMPRODBillEntryPagePresenter extends AbstractMVPPresenter<OMPProcessRODBillEntryPageWizard>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7142042678727556331L;
	protected static final String OMP_NEGOTIATE = "omp rod bill entry negotiate";
	protected static final String OMP_REJECTION = "omp rod bill entry rejection";
	protected static final String OMP_APPROVAL = "omp rod bill entry approval";
	protected static final String OMP_PARTICULARS ="omp particulars";
	protected static final String OMP_ROD_CLAIM_SUBMIT = "omp rod claim submit";
	protected static final String OMP_BSI = "omp bsi";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private OMPProcessRODBillEntryService rodBillentryService;

	@EJB
	private OMPClaimService ompClaimService;
	
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
	
	public void setReferenceData(@Observes @CDIEvent(OMP_PARTICULARS) final ParameterDTO parameters)
	{
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
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(OMP_ROD_CLAIM_SUBMIT) final ParameterDTO parameters) {
		OMPClaimProcessorDTO ompClaimProcessorDTO = (OMPClaimProcessorDTO) parameters.getPrimaryParameter();
		OMPClaim claim = ompClaimService.getClaimByKey(ompClaimProcessorDTO.getClaimDto().getKey());
//		OMPBenefitCover benefitCover = new OMPBenefitCover();
//		OMPClaimPayment claimpayment = new OMPClaimPayment();
		
		Stage stgObj = masterService.getStageBykey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
		Status statusObj = masterService.getStatusByKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
		claim.setStage(stgObj);
		claim.setStatus(statusObj);
		ompClaimProcessorDTO.setUserName(String.valueOf(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID)));
		
		OMPReimbursement ompReimbursement = rodBillentryService.saveReimbursement(ompClaimProcessorDTO, claim, null);
		 List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable = ompClaimProcessorDTO.getClaimCalculationViewTable();
		 if(claimCalculationViewTable!=null){
		 for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : claimCalculationViewTable) {
			 OMPBenefitCover ompBenefitCoverByKey = new OMPBenefitCover();
				 rodBillentryService.saveBenefitCover(ompClaimCalculationViewTableDTO , ompBenefitCoverByKey);
				 ompBenefitCoverByKey.setRodKey(ompReimbursement);
				 rodBillentryService.insertOrUpdateBenefit(ompBenefitCoverByKey);
		}
		 }
//		rodBillentryService.updateReimbursement(ompReimbursement.getKey(), stgObj, statusObj);
		
//		 benefitCover = rodBillentryService.saveBenefitCover(ompClaimProcessorDTO, benefitCover);
//		 claimpayment = rodBillentryService.saveClaimpayment(ompClaimProcessorDTO, claimpayment);
		String outCome = SHAConstants.OUTCOME_FOR_OMP_ROD_BILLENTRY;				
		
		Object[] paramter = rodBillentryService.getParamter(ompClaimProcessorDTO,claim,outCome, ompReimbursement);
		Object[] inputArray = (Object[])paramter[0];
		inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.SOURCE_OMP_ROD;
		DBCalculationService dbCalculationService = new DBCalculationService();
		dbCalculationService.initiateOMPTaskProcedure(paramter);
		view.buildSuccessLayout();
	}
	
	
}
