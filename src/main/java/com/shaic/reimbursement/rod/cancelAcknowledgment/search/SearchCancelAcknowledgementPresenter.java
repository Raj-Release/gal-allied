package com.shaic.reimbursement.rod.cancelAcknowledgment.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODFormDTO;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODService;


@ViewInterface(SearchCancelAcknowledgementView.class)
public class SearchCancelAcknowledgementPresenter extends AbstractMVPPresenter<SearchCancelAcknowledgementView> {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "search for cancel acknowledgement";
	
	@EJB
	//private SearchCancelAcknowledgementService searchService;
	private SearchCreateRODService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchCreateRODFormDTO searchFormDTO = (SearchCreateRODFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.searchForCancelAcknowledgement(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
