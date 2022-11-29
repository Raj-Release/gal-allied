package com.shaic.paclaim.settlementpullback.searchsettlementpullback;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@ViewInterface(PASearchSettlementPullBackView.class)
public class PASearchSettlementPullBackPresenter extends AbstractMVPPresenter<PASearchSettlementPullBackView> {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "search for settlement pull back PA";
	
	@EJB
	private PASearchSettlementPullBackService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PASearchSettlementPullBackFormDTO searchFormDTO = (PASearchSettlementPullBackFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		view.list(searchService.searchForSettlementPullBack(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

} 

