package com.shaic.claim.reimbursement.rawanalysis;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.reimbursement.rrc.services.SearchProcessRRCRequestFormDTO;


@SuppressWarnings("serial")
@ViewInterface(SearchProcessRawRequestView.class)
public class SearchProcessRawRequestPresenter extends AbstractMVPPresenter<SearchProcessRawRequestView>{
	
	public static final String SEARCH_RAW_REQUEST = "doSearchForRaw";
	
	@EJB
	private SearchProcessRawRequestService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_RAW_REQUEST) final ParameterDTO parameters) {
		
		SearchProcessRawRequestFormDto searchFormDTO = (SearchProcessRawRequestFormDto) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}


	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
