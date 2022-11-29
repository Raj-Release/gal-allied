package com.shaic.reimbursement.manageclaim.searchClaimwiseApproval;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchClaimWiseAllowApprovalView.class)
public class SearchClaimWiseAllowApprovalPresenter extends AbstractMVPPresenter<SearchClaimWiseAllowApprovalView> {
	
public static final String SEARCH_BUTTON_CLICK = "doSearchAllowApproval";
	
	@EJB
	public SearchClaimWiseApprovalService service;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchClaimWiseAllowApprovalFormDto searchFormDTO = (SearchClaimWiseAllowApprovalFormDto) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(service.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
