package com.shaic.claim.reimbursement.processDraftRejectionLetterDetail;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.common.APIService;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.ReimbursementRejectionService;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(DecideOnRejectionWizard.class)
public class DecideOnRejectionWizardPresenter extends	AbstractMVPPresenter<DecideOnRejectionWizard> {

	public static final String SUBMIT_PROCESS_DRAFT_REJECTION = "Submit Process Draft Rejection Letter";
		
		@EJB
		private ReimbursementRejectionService reimbursementRejectionService;
		
		@EJB
		private HospitalService hospitalService;
		
		@EJB
		private ReimbursementService reimbursementService;
		
		@EJB
		private DBCalculationService dbCalculationService;
		
		@EJB
		private ClaimService claimService;
		
		@EJB
		private APIService apiService;
		
		@Override
		public void viewEntered() {
			// TODO Auto-generated method stub
			
		}

		public void submitDraftRejection(
				@Observes @CDIEvent(SUBMIT_PROCESS_DRAFT_REJECTION) final ParameterDTO parameters) {
			
			ClaimRejectionDto clmRejectionDto = (ClaimRejectionDto)parameters.getPrimaryParameter();
			ReimbursementRejectionDto reimbursementRejectionDto = clmRejectionDto.getReimbursementRejectionDto();
			
//			if(reimbursementRejectionDto.getRejectionLetterRemarks() != null){
			if(reimbursementRejectionDto.getStatusValue() != null && !reimbursementRejectionDto.getStatusValue().isEmpty() && SHAConstants.REJECTION_APPROVE_OUT_COME.equalsIgnoreCase(reimbursementRejectionDto.getStatusValue())){
				dbCalculationService.reimbursementRollBackProc(reimbursementRejectionDto.getReimbursementDto().getKey(),"R");
			}
			
			ReimbursementRejection reimbursementRej = reimbursementRejectionService.submitProcessedReimbursementRejection(reimbursementRejectionDto);
			Reimbursement reimbursement = reimbursementRej.getReimbursement();
			
			Reimbursement partialHospitalizationROD = reimbursementService.getPartialHospitalizationROD(reimbursement.getClaim().getKey());
			
			Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(reimbursement.getClaim().getKey(), reimbursement.getKey());
			Status status = reimbursement.getStatus();
			if(status != null && (status.getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
					|| status.getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)
					|| status.getKey().equals(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS)
					|| status.getKey().equals(ReferenceTable.PAYMENT_REJECTED))){
				reimbursementRejectionService.updateReimbursementAmount(reimbursement);
				reimbursementRejectionService.setReimbursementOtherApprovedAmt(reimbursement);
				
				if(reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					
					if(partialHospitalizationROD != null){
						dbCalculationService.invokeReimbursementAccumulatorProcedure(partialHospitalizationROD.getKey());
					}else{
						Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursement.getClaim().getKey());
						dbCalculationService.invokeAccumulatorProcedure(latestPreauth.getKey());
					}
				}else{
					if(hospitalizationRod != null){
						dbCalculationService.invokeReimbursementAccumulatorProcedure(hospitalizationRod.getKey());
					}
				}

				dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
				
				try{
				Hospitals hospitalDetailsByKey = hospitalService.getHospitalDetailsByKey(reimbursement.getClaim().getIntimation().getHospital());
				Claim claim = claimService.getClaimByClaimKey(reimbursement.getClaim().getKey());
				String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
				apiService.updateProvisionAmountToPremia(provisionAmtInput);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				try {
					if(reimbursementRej != null && reimbursementRej.getStatus() != null && (reimbursementRej.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS) || reimbursementRej.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS))) {
						if(reimbursementService.isAnyRodActive(reimbursement)){
							PremiaService.getInstance().UnLockPolicy(reimbursementRej.getReimbursement().getClaim().getIntimation().getIntimationId());
						}
					}
				} catch(Exception e) {
					
				}
			}
			
			view.buildSuccessLayout();
		}
		

}
