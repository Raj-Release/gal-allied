package com.shaic.claim.reimbursement.financialapproval.pages.billsummary;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.domain.PreauthService;

@ViewInterface(FinancialSummaryPageWizard.class)
public class FinancialSummaryPagePresenter extends AbstractMVPPresenter<FinancialSummaryPageWizard>{
	private static final long serialVersionUID = 7488192193097057694L;
	
	public static final String UPDATE_PREVIOUS_CLAIM_DETAILS = "update_previous_claim_details for process claim financial";
	
	@EJB
	private PreauthService preauthService;
	
	public void showUpdateClaimDetailTable(
			@Observes @CDIEvent(UPDATE_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {
		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		
		List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails = new ArrayList<UpdateOtherClaimDetailDTO>();
		
		if(preauthDTO.getUpdateOtherClaimDetailDTO().isEmpty()){

			//updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getNewIntimationDTO().getPolicy().getKey());
			updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetailsDTOForReimbursement(preauthDTO.getKey());
			if(updateOtherClaimDetails != null && updateOtherClaimDetails.isEmpty()){
				updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO);
			}
		}else{
			updateOtherClaimDetails = preauthDTO.getUpdateOtherClaimDetailDTO();
		}
	    
	    view.setUpdateOtherClaimsDetails(updateOtherClaimDetails);
	
		
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
