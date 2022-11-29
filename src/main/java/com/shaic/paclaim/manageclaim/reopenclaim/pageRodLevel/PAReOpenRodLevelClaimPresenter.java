package com.shaic.paclaim.manageclaim.reopenclaim.pageRodLevel;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.manageclaim.reopenclaim.searchRodLevel.PASearchReOpenClaimRODLevelService;
import com.shaic.paclaim.manageclaim.reopenclaim.searchRodLevel.PASearchReOpenClaimRodLevelTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;


@ViewInterface(PAReOpenRodLevelClaimView.class)
public class PAReOpenRodLevelClaimPresenter extends AbstractMVPPresenter<PAReOpenRodLevelClaimView> {
	
	public static final String SET_DATA_FIELD = "Set data for common couresel and Reopen field PA";
	
	public static final String SUBMIT_DATA = "Submit reopen claim PA";
	
	public static final String SET_TABLE_DATA = "Set List for reimburesment table for reopen claim PA";
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ReimbursementService rodService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@EJB
	private PASearchReOpenClaimRODLevelService reOpenClaimService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	protected void setDataValues(
			@Observes @CDIEvent(SET_DATA_FIELD) final ParameterDTO parameters){
		
		PASearchReOpenClaimRodLevelTableDTO tableDTO = (PASearchReOpenClaimRodLevelTableDTO) parameters.getPrimaryParameter();
		
		
		PAReOpenRodLevelClaimDTO reopenClaimDTO = reOpenClaimService.getReopenPAClaimDTO(tableDTO.getReimbursmentKey(),tableDTO);
		
		BeanItemContainer<SelectValue> reasonForReOpen = masterService.getMasterValueByReference(ReferenceTable.REASON_FOR_REOPEN);
		

		view.setUpReference(reopenClaimDTO,reasonForReOpen);
		
	}
	
	protected void submitEvent(
			@Observes @CDIEvent(SUBMIT_DATA) final ParameterDTO parameters){
		
		PASearchReOpenClaimRodLevelTableDTO tableDTO = (PASearchReOpenClaimRodLevelTableDTO) parameters.getPrimaryParameter();
		
		PAReOpenRodLevelClaimDTO bean = (PAReOpenRodLevelClaimDTO) parameters.getSecondaryParameter(0, PAReOpenRodLevelClaimDTO.class);
		
		
		List<ViewDocumentDetailsDTO> documentDetailsList = bean.getDocumentDetails();
		for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : documentDetailsList) {
			
			if(viewDocumentDetailsDTO.getCloseClaimStatus() != null && viewDocumentDetailsDTO.getCloseClaimStatus()){
				Reimbursement reimbursmentKey = rodService.getReimbursementByKey(viewDocumentDetailsDTO.getReimbursementKey());
				
				if((reimbursmentKey.getDocAcknowLedgement() != null && reimbursmentKey.getDocAcknowLedgement().getPartialHospitalisationFlag() != null 
						&& reimbursmentKey.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")) ||(reimbursmentKey.getDocAcknowLedgement() != null && reimbursmentKey.getDocAcknowLedgement().getHospitalisationFlag() != null 
								&& reimbursmentKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y"))){
				
					Reimbursement hospitalizationRod = rodService.getHospitalizationRod(reimbursmentKey.getClaim().getKey(), reimbursmentKey.getKey());
					
					if(reimbursmentKey.getClaim() != null && reimbursmentKey.getClaim().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
						
						if(hospitalizationRod != null){
							dbCalculationService.reimbursementRollBackProc(hospitalizationRod.getKey(),"R");
						}else{
							Preauth latestPreauth = rodService.getLatestPreauthByClaim(reimbursmentKey.getClaim().getKey());
							dbCalculationService.reimbursementRollBackProc(latestPreauth.getKey(), "C");
						}
					}else{
						if(hospitalizationRod != null){
							dbCalculationService.reimbursementRollBackProc(hospitalizationRod.getKey(),"R");
						}
					}
				}
			}
			
			
			
			
		}
		List<Reimbursement> reimbursementList = reOpenClaimService.submitReopenPAClaim(tableDTO, bean);

		for (Reimbursement reimbursement : reimbursementList) {
			
			//IMSSUPPOR-37576 rejection rod need stop close
	    	if(!ReferenceTable.getRejectedRODKeys().containsKey(reimbursement.getStatus().getKey())){
			 if(reimbursement.getStage().getKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE)
					 ||reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)
					 ||reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)){
		    		
				 dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursement.getKey());
		    	}
			
			dbCalculationService.updateProvisionAmountForPANonHealth(reimbursement.getKey(), reimbursement.getClaim().getKey());
			
		}
	}
		view.buildSuccessLayout();
		
	}
	
	protected void setTableValues(
			@Observes @CDIEvent(SET_TABLE_DATA) final ParameterDTO parameters){
		
		PASearchReOpenClaimRodLevelTableDTO tableDTO = (PASearchReOpenClaimRodLevelTableDTO) parameters.getPrimaryParameter();
		
		List<ViewDocumentDetailsDTO> listDocumentDetails = reimbursementService.getDocumentDetailsForPAReOpenClaim(tableDTO.getIntimationNo(),tableDTO.getUsername(),tableDTO.getPassword());
		view.setTableList(listDocumentDetails);
		
	}


	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
