package com.shaic.claim.processdatacorrectionhistorical.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.processtranslation.search.SearchProcessTranslationFormDTO;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(SearchProcessDataCorrectionHistoricalView.class)
public class SearchProcessDataCorrectionHistoricalPresenter extends AbstractMVPPresenter<SearchProcessDataCorrectionHistoricalView>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6873439646301818591L;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;

	public static final String SEARCH_INTIMATION_DATA_CORRECTION_HISTORICAL = "search_intimation_data_correction_historical";
	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_INTIMATION_DATA_CORRECTION_HISTORICAL) final ParameterDTO parameters) {
		String IntimationNo = (String) parameters.getPrimaryParameter();
		String userName = (String)parameters.getSecondaryParameter(0, String.class);
		
		view.list(dbCalculationService.getDataHistoricalGetTask(userName,IntimationNo, null));
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
}
