package com.shaic.claim.pedrequest.teamlead.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveFormDTO;

@ViewInterface(SearchPEDRequestTeamLeadView.class)
public class SearchPEDRequestTeamLeadPresenter extends
		AbstractMVPPresenter<SearchPEDRequestTeamLeadView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String PED_TL_SEARCH_BUTTON_CLICK = "pedTLSearchClick";
	
	@EJB
	private SearchPEDRequestTeamLeadService searchPEDRequestApproveService;	
	
	public void searchClick(
			@Observes @CDIEvent(PED_TL_SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
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
