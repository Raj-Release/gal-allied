package com.shaic.claim.pcc.wizard;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestFormDTO;

@ViewInterface(SearchPccView.class)
public class SearchProcessPCCRequestPresenter extends AbstractMVPPresenter<SearchPccView> {
	
    public static final String SEARCH_PCC_RQUEST = "doSearchForPCC";
	
	@EJB
	private SearchProcessPCCRequestService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_PCC_RQUEST) final ParameterDTO parameters) {
		
		SearchProcessPCCRequestFormDTO searchFormDTO = (SearchProcessPCCRequestFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
