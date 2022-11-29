package com.shaic.claim.misc.updatesublimit;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;


@ViewInterface(SearchUpdateSublimitView.class)
public class SearchUpdateSublimitPresenter extends AbstractMVPPresenter<SearchUpdateSublimitView>{
	
	public static final String SEARCH_BUTTON = "doSearchForSublimit";
	
	@EJB
	private SearchUpdateSublimitService searchSublimitService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON) final ParameterDTO parameters) {
		
		SearchUpdateSublimitFormDTO formDTO = (SearchUpdateSublimitFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchSublimitService.doSearch(formDTO, userName, passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
