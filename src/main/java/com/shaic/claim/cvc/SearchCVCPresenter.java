package com.shaic.claim.cvc;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.table.Page;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthFormDTO;

@ViewInterface(SearchCVCView.class)
public class SearchCVCPresenter extends AbstractMVPPresenter<SearchCVCView>{



	private static final long serialVersionUID = -5504472929540762973L;
	
	
	public static final String SEARCH_BUTTON_CLICK = "search_for_CVC";
	
	@EJB
	private SearchCVCService cvcSearchService;
	
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchCVCFormDTO searchFormDTO = (SearchCVCFormDTO) parameters.getPrimaryParameter();		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);

		SearchCVCTableDTO search = cvcSearchService.search(searchFormDTO,userName,passWord);
				
		view.list(search); 
	}
	

	@Override
	public void viewEntered() {

	}

}
