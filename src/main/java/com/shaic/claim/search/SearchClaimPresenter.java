package com.shaic.claim.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.ViewDetails;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;

@ViewInterface(SearchClaimView.class)
public class SearchClaimPresenter  extends AbstractMVPPresenter<SearchClaimView> {
	
	public static final String SEARCH_BUTTON_CLICK = "claimSearchClick";
	public static final String SHOW_CLAIM_STATUS = "viewClaimStatus";
	
	@EJB
	private SearchClaimService seachClaimService;	
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@Inject
	private ViewDetails viewDetailsService;// = new ViewDetails(); 
	
	
	@SuppressWarnings({ "deprecation"})
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchClaimFormDTO searchFormDTO = (SearchClaimFormDTO) parameters
				.getPrimaryParameter();
		view.list(seachClaimService.search(searchFormDTO));			
	}
	
	@SuppressWarnings({ "deprecation"})
	public void viewClaim(
			@Observes @CDIEvent(SHOW_CLAIM_STATUS) final ParameterDTO parameters) {
		SearchClaimTableDTO searchClaimTableDTO = (SearchClaimTableDTO) parameters
			.getPrimaryParameter();
		
		if(searchClaimTableDTO.getClaimTypeId().equals(ReferenceTable.CLAIM_TYPE_CASHLESS_ID) || searchClaimTableDTO.getClaimTypeId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
			
		
			Reimbursement reimbursement = reimbursementService.getLatestReimbursementDetails(searchClaimTableDTO.getKey());
			
			if(reimbursement != null){
			viewDetailsService.viewSearchClaimStatus(searchClaimTableDTO.getIntimationNo(),reimbursement.getKey());
			}
			else{
				viewDetailsService.viewSearchClaimStatus(searchClaimTableDTO.getIntimationNo(), null);
			}
		}else{
			viewDetailsService.getClaimStatus(searchClaimTableDTO.getIntimationNo());
		}
	
	}
	
	
	 @Override
	public void viewEntered() {
		
	}

}
