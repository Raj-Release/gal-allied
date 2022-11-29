package com.shaic.reimbursement.processi_investigationi_initiated.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


/**
 * @author ntv.narenj
 *
 */
@ViewInterface(SearchProcessInvestigationInitiatedView.class)
public class SearchProcessInvestigationInitiatedPresenter extends AbstractMVPPresenter<SearchProcessInvestigationInitiatedView> {
	
	@EJB
	SearchProcessInvestigationInitiatedService searchService;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SEARCH_BUTTON_CLICK = "doSearchpiviTable";
	
	public final static String PROCESS_INVESTIGATION_INITIATED = "ProcessInvestigationInitiated";
	
	
	@EJB 
	private SearchProcessInvestigationInitiatedService service;

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchProcessInvestigationInitiatedFormDTO searchFormDTO = (SearchProcessInvestigationInitiatedFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.revisedSearchInvestigation(searchFormDTO,userName,passWord));

//		view.list(searchService.search(searchFormDTO,userName,passWord));
		
	}
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
