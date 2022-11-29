package com.shaic.claim.cashlessprocess.processicac.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchProcessICACView.class)
public class SearchProcessICACPresenter extends
		AbstractMVPPresenter<SearchProcessICACView> {

	private static final long serialVersionUID = -5504472929540762973L;

	public static final String SEARCH_BUTTON_CLICK = "Process ICAC Search";

	@EJB
	private ProcessICACService processICACService;

	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchProcessICACReqFormDTO searchFormDTO = (SearchProcessICACReqFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(processICACService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

}
