package com.shaic.paclaim.healthsettlementpullback.searchsettlementpullback;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@ViewInterface(PAHospSearchSettlementPullBackView.class)
public class PAHospSearchSettlementPullBackPresenter extends AbstractMVPPresenter<PAHospSearchSettlementPullBackView> {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "search for settlement pull back PA hosp";
	
	@EJB
	private PAHospSearchSettlementPullBackService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PAHospSearchSettlementPullBackFormDTO searchFormDTO = (PAHospSearchSettlementPullBackFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		view.list(searchService.searchForSettlementPullBack(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

} 

