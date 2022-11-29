package com.shaic.reimbursement.manageclaim.closeclaimInProcess.pageRODLevel;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel.SearchCloseClaimServiceRODLevel;
import com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel.SearchCloseClaimTableDTORODLevel;
import com.vaadin.v7.data.util.BeanItemContainer;


@ViewInterface(CloseClaimInProcessView.class)
public class CloseClaimInProcessPresenter extends AbstractMVPPresenter<CloseClaimInProcessView> {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public static final String SET_DATA_FIELD = "Set data for common couresel and close claim in process reason";
	
	public static final String SET_TABLE_DATA = "close claim table for mangaged claim";
	
	public static final String SUBMIT_DATA = "Submit close claim in process screen";
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ReimbursementService rodService;
	
	@EJB
	private SearchCloseClaimServiceRODLevel closeClaimService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	protected void setDataValues(
			@Observes @CDIEvent(SET_DATA_FIELD) final ParameterDTO parameters){
		
		SearchCloseClaimTableDTORODLevel tableDTO = (SearchCloseClaimTableDTORODLevel) parameters.getPrimaryParameter();
		
		
		BeanItemContainer<SelectValue> reasonForClosing = masterService.getMasterValueByReference(ReferenceTable.REASON_FOR_CLOSING);

		view.setUpReference(reasonForClosing);
		
	}
	
	protected void setTableValues(
			@Observes @CDIEvent(SET_TABLE_DATA) final ParameterDTO parameters){
		
		SearchCloseClaimTableDTORODLevel tableDTO = (SearchCloseClaimTableDTORODLevel) parameters.getPrimaryParameter();
		
		List<ViewDocumentDetailsDTO> listDocumentDetails = reimbursementService.getDocumentDetailsForCloseClaimInProcess(tableDTO.getIntimationNo(),tableDTO.getUsername(),tableDTO.getPassword());
	

		view.setTableList(listDocumentDetails);
		
	}
	
	protected void submitEvent(
			@Observes @CDIEvent(SUBMIT_DATA) final ParameterDTO parameters){
		
		SearchCloseClaimTableDTORODLevel tableDTO = (SearchCloseClaimTableDTORODLevel) parameters.getPrimaryParameter();
		
		ViewDocumentDetailsDTO documentDTO = (ViewDocumentDetailsDTO) parameters.getSecondaryParameter(0, ViewDocumentDetailsDTO.class);
		
		List<ViewDocumentDetailsDTO> documentDetailsList = documentDTO.getDocumentDetailsList();
		
		for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : documentDetailsList) {
			dbCalculationService.reimbursementRollBackProc(viewDocumentDetailsDTO.getReimbursementKey(),"R");
		}

		List<Reimbursement> reimbursementList = closeClaimService.submitCloseClaimInProcess(documentDTO, tableDTO);
//		
		for (Reimbursement reimbursement : reimbursementList) {
			
//			if(reimbursement.getStage().getKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE)
//					|| reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)
//					|| reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
//					|| reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
				
//				dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursement.getKey());
//				dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
				
				Reimbursement hospitalizationRod = rodService.getHospitalizationRod(reimbursement.getClaim().getKey(), reimbursement.getKey());
				
				if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
						|| reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
					
					if(reimbursement.getClaim().getClaimType() != null && reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
						if(hospitalizationRod == null){
							if(reimbursement.getClaim().getLatestPreauthKey() != null){
								dbCalculationService.invokeAccumulatorProcedure(reimbursement.getClaim().getLatestPreauthKey());
							}else{
								Preauth latestPreauth = rodService.getLatestPreauthByClaim(reimbursement.getClaim().getKey());
								dbCalculationService.invokeAccumulatorProcedure(latestPreauth.getKey());
							}
						}else{
							dbCalculationService.invokeReimbursementAccumulatorProcedure(hospitalizationRod.getKey());
						}
					}else{
						if(hospitalizationRod != null){
							dbCalculationService.invokeReimbursementAccumulatorProcedure(hospitalizationRod.getKey());
						}
					}
				}
			
			dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
			PremiaService.getInstance().UnLockPolicy(reimbursement.getClaim().getIntimation().getIntimationId());
		}
		
	    view.buildSuccessLayout();
		
	}
	


	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
