package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(PhysicalDocumentReceivedMakerView.class)
public class PhysicalDocumentReceivedMakerPresenter extends AbstractMVPPresenter<PhysicalDocumentReceivedMakerView> {

	
	public static final String SEARCH_BUTTON_CLICK = "doSearch Received Physical document";
	
	public static final String SEARCH_ERROR = "doSearch Received Physical document Error";
	
	@EJB
	private PhysicalDocumentReceivedMakerService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PhysicalDocumentReceivedMakerFormDTO searchFormDTO = (PhysicalDocumentReceivedMakerFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		String screenName=(String)parameters.getSecondaryParameter(2, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord,screenName));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearchError(@Observes @CDIEvent(SEARCH_ERROR) final ParameterDTO parameters) {

		view.validation();
	}
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}



}
