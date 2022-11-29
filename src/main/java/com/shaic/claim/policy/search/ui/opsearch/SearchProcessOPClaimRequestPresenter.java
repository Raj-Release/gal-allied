package com.shaic.claim.policy.search.ui.opsearch;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchProcessOPClaimRequestView.class)
public class SearchProcessOPClaimRequestPresenter extends AbstractMVPPresenter<SearchProcessOPClaimRequestView>{


	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK_PROCESS_OP_REQUEST = "doSearchForProcessOPClaim";

	@EJB
	private ProcessOPRequestService searchService;


	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_PROCESS_OP_REQUEST) final ParameterDTO parameters) {

		SearchProcessOPClaimFormDTO searchFormDTO = (SearchProcessOPClaimFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {

	}

}
