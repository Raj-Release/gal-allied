package com.shaic.claim.fieldvisit.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchFieldVisitView.class)
public class SearchFieldVisitPresenter extends AbstractMVPPresenter<SearchFieldVisitView> {
	
	private static final long serialVersionUID = -5504472929540762973L;
	
	public static final String SEARCH_BUTTON_CLICK = "fieldVisitSearchClick";
	
	@EJB
	private SearchFieldVisitService searchFieldVisitService;
	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchFieldVisitFormDTO searchFormDTO = (SearchFieldVisitFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchFieldVisitService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
	}

}
