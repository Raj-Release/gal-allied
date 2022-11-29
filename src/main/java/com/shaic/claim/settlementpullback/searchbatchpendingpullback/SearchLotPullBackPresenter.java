package com.shaic.claim.settlementpullback.searchbatchpendingpullback;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.settlementpullback.searchsettlementpullback.SearchSettlementPullBackFormDTO;
import com.shaic.claim.settlementpullback.searchsettlementpullback.SearchSettlementPullBackService;
import com.shaic.claim.settlementpullback.searchsettlementpullback.SearchSettlementPullBackView;

@ViewInterface(SearchLotPullBackView.class)
public class SearchLotPullBackPresenter extends AbstractMVPPresenter<SearchLotPullBackView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "search for lot pull back";
	
	@EJB
	private SearchLotPullBackService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchLotPullBackFormDTO searchFormDTO = (SearchLotPullBackFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		view.list(searchService.searchForSettlementPullBack(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}


}
