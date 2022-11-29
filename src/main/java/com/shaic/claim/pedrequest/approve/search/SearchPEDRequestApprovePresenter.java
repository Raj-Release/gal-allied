package com.shaic.claim.pedrequest.approve.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchPEDRequestApproveView.class)
public class SearchPEDRequestApprovePresenter extends
		AbstractMVPPresenter<SearchPEDRequestApproveView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "pedappSearchClick";
	
	@EJB
	private SearchPEDRequestApproveService searchPEDRequestApproveService;	
	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchPEDRequestApproveFormDTO searchFormDTO = (SearchPEDRequestApproveFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchPEDRequestApproveService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {

	}

}
