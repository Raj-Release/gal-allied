package com.shaic.paclaim.addAdditionalDocPaymentInfo.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;





@ViewInterface(SearchAddAdditionalDocumentPaymentInfoView.class)
public class SearchAddAdditionalDocumentPaymentInfoPresenter extends
		AbstractMVPPresenter<SearchAddAdditionalDocumentPaymentInfoView> {
	
	
	public static final String SEARCH_BUTTON_CLICK = "doSearchPAAddAditionaDocumentPaymentInfo";
	
	public static final String SEARCH_ERROR = "doSearchPAAddAditionaDocumentPaymentInfoError";
	
	@EJB
	private SearchPAAddAdditionalDocumentPaymentInfoService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchAddAdditionalDocumentPaymentInfoFormDTO searchFormDTO = (SearchAddAdditionalDocumentPaymentInfoFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
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
