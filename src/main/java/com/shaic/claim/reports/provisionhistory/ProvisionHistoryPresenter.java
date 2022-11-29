package com.shaic.claim.reports.provisionhistory;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.settlementpullback.searchsettlementpullback.SearchSettlementPullBackFormDTO;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(ProvisionHistoryView.class)
public class ProvisionHistoryPresenter extends AbstractMVPPresenter<ProvisionHistoryView> {

	public static final String SEARCH_BUTTON_CLICK = "search for provision history";
	
	@EJB
	DBCalculationService  dbCalcService;
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		ProvisionHistoryFormDTO searchFormDTO = (ProvisionHistoryFormDTO) parameters.getPrimaryParameter();
		
		//String userName=(String)parameters.getSecondaryParameter(0, String.class);
		//String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		view.list(dbCalcService.getProvisionHistoryList(searchFormDTO.getIntimationNo()));
	}
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
