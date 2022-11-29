package com.shaic.claim.medical.opinion;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchRecordMarkEscView.class)
public class SearchRecordMarkEscPresenter extends
AbstractMVPPresenter<SearchRecordMarkEscView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "recMarkEscSearchBtn";

	@EJB
	private SearchRecordMarkEscService searchRecordMarkEscProcessService;


	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchRecordMarkEscFormDTO searchFormDTO = (SearchRecordMarkEscFormDTO) parameters
				.getPrimaryParameter();

		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);

		view.list(searchRecordMarkEscProcessService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

}
