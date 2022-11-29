package com.shaic.paclaim.addAdditinalDocument.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimburement.addAdditinalDocument.search.SearchAddAdditionalDocumentFormDTO;

@ViewInterface(PAAddAdditionalDocumentsView.class)
public class PAAddAdditionalDocumentsPresenter extends AbstractMVPPresenter<PAAddAdditionalDocumentsView> {

	
public static final String SEARCH_BUTTON_CLICK = "doSearchPAAddAditionaDoc";
	
	public static final String SEARCH_ERROR = "doSearchPAAddAditionaDocError";
	
	@EJB
	private PAAddAdditionalDocumentsService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchAddAdditionalDocumentFormDTO searchFormDTO = (SearchAddAdditionalDocumentFormDTO) parameters.getPrimaryParameter();
		
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
