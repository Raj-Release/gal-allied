package com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchFlagReconsiderationReqView.class)
public class SearchFlagReconsiderationReqPresenter extends
		AbstractMVPPresenter<SearchFlagReconsiderationReqView> {

	private static final long serialVersionUID = -5504472929540762973L;

	public static final String SEARCH_BUTTON_CLICK = "reconsideration_flag_request_form";

	@EJB
	private SearchFlagReconsiderationReqService searchFlagReconsiderationReqService;

	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchFlagReconsiderationReqFormDTO searchFormDTO = (SearchFlagReconsiderationReqFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchFlagReconsiderationReqService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

}
