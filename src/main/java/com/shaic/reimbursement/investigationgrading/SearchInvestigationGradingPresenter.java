package com.shaic.reimbursement.investigationgrading;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationFormDTO;

@ViewInterface(SearchInvestigationGradingView.class)
public class SearchInvestigationGradingPresenter extends AbstractMVPPresenter<SearchInvestigationGradingView> {


	
	@EJB
	SearchInvestigationGradingService searchService;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SEARCH_BUTTON_CLICK = "doSearchInvestigationGrading";
	
	public final static String PROCESS_INVESTIGATION_INITIATED = "ProcessInvestigationInitiated";
	
	
	@EJB 
	private SearchInvestigationGradingService service;

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchAssignInvestigationFormDTO searchFormDTO = (SearchAssignInvestigationFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		

		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}


}
