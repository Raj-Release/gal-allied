package com.shaic.reimbursement.manageclaim.SearchUpdateRodDetails;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;


@ViewInterface(SearchUpdateRodDetailsView.class)
public class SearchUpdateRodDetailsPresenter extends AbstractMVPPresenter<SearchUpdateRodDetailsView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3384010034816825693L;

	public static final String SEARCH_BUTTON_CLICK ="searchUpdaterodDetails";
	
	@EJB
	private SearchUpdateRodDetailsService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchUpdateRodDetailsFormDTO searchFormDTO = (SearchUpdateRodDetailsFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
