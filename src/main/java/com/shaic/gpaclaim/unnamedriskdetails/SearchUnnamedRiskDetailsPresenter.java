package com.shaic.gpaclaim.unnamedriskdetails;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchUnnamedRiskDetailsView.class)
public class SearchUnnamedRiskDetailsPresenter extends AbstractMVPPresenter<SearchUnnamedRiskDetailsView >{
	

	
	private static final long serialVersionUID = 1L;
	public static final String UNNAMED_RISK_DETAILS = "doSearchUnnamedRiskDetails";
	@EJB
	private SearchUnnamedRiskDetailsService searchService;
	
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(UNNAMED_RISK_DETAILS) final ParameterDTO parameters) {
		
		SearchUnnamedRiskDetailsFormDTO searchFormDTO = (SearchUnnamedRiskDetailsFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}


}
