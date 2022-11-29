package com.shaic.paclaim.manageclaim.closeclaim.pageRodLevel;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
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
import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimServiceRODLevel;
import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimTableDTORODLevel;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PACloseClaimRodLevelView.class)
public class PACloseClaimPageRodLevelPresenter extends AbstractMVPPresenter<PACloseClaimRodLevelView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SET_DATA_FIELD = "Set data for common couresel and close claim reason PA";
	
	public static final String SET_TABLE_DATA = "Set List for reimburesment table PA";
	
	public static final String SUBMIT_DATA = "Submit close claim(search based) PA";
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@EJB
	private ReimbursementService rodService;
	
	@EJB
	private PASearchCloseClaimServiceRODLevel closeClaimService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	protected void setDataValues(
			@Observes @CDIEvent(SET_DATA_FIELD) final ParameterDTO parameters){
		
		PASearchCloseClaimTableDTORODLevel tableDTO = (PASearchCloseClaimTableDTORODLevel) parameters.getPrimaryParameter();
		
		
		BeanItemContainer<SelectValue> reasonForClosing = masterService.getMasterValueByReference(ReferenceTable.REASON_FOR_CLOSING);

		view.setUpReference(reasonForClosing);
		
	}
	
	
	protected void submitEvent(
			@Observes @CDIEvent(SUBMIT_DATA) final ParameterDTO parameters){
		
		PASearchCloseClaimTableDTORODLevel tableDTO = (PASearchCloseClaimTableDTORODLevel) parameters.getPrimaryParameter();
		
		ViewDocumentDetailsDTO documentDTO = (ViewDocumentDetailsDTO) parameters.getSecondaryParameter(0, ViewDocumentDetailsDTO.class);
		
		List<ViewDocumentDetailsDTO> documentDetailsList = documentDTO.getDocumentDetailsList();
		for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : documentDetailsList) {
			if(viewDocumentDetailsDTO.getCloseClaimStatus() != null && viewDocumentDetailsDTO.getCloseClaimStatus()){
			dbCalculationService.reimbursementRollBackProc(viewDocumentDetailsDTO.getReimbursementKey(),"R");
			}
		}
		
		List<Reimbursement> reimbursementList = closeClaimService.submitSearchBasedCloseClaim(documentDTO, tableDTO);
		
		dbCalculationService.stopReminderProcessProcedure(tableDTO.getIntimationNo(),SHAConstants.OTHERS);
		
		for (Reimbursement reimbursement : reimbursementList) {
			
			//IMSSUPPOR-37576 rejection rod need stop close
	    	if(!ReferenceTable.getRejectedRODKeys().containsKey(reimbursement.getStatus().getKey())){
//			if(reimbursement.getStage().getKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE)
//					|| reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)
//					|| reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
//					|| reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
//				
//				dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursement.getKey());
//			}
//			dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
			Reimbursement hospitalizationRod = rodService.getHospitalizationRod(reimbursement.getClaim().getKey(), reimbursement.getKey());
			
			
			if((reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
					|| reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)) && ((reimbursement.getDocAcknowLedgement() != null && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null 
							&& reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")) ||(reimbursement.getDocAcknowLedgement() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null 
							&& reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")))){
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
			dbCalculationService.updateProvisionAmountForPANonHealth(reimbursement.getKey(), reimbursement.getClaim().getKey());
			PremiaService.getInstance().UnLockPolicy(reimbursement.getClaim().getIntimation().getIntimationId());
		}
		
	}
		
		
	    view.buildSuccessLayout();
		
	}
	
	protected void setTableValues(
			@Observes @CDIEvent(SET_TABLE_DATA) final ParameterDTO parameters){
		
		PASearchCloseClaimTableDTORODLevel tableDTO = (PASearchCloseClaimTableDTORODLevel) parameters.getPrimaryParameter();
		
		List<ViewDocumentDetailsDTO> listDocumentDetails = reimbursementService.getDocumentDetailsForPACloseClaim(tableDTO.getIntimationNo(),tableDTO.getUsername(),tableDTO.getPassword());
		view.setTableList(listDocumentDetails);
		
	}

	@Override
	public void viewEntered() {
		
		
	}

}
