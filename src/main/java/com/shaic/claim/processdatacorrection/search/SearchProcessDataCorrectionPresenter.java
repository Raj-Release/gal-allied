package com.shaic.claim.processdatacorrection.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.processtranslation.search.SearchProcessTranslationFormDTO;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(SearchProcessDataCorrectionView.class)
public class SearchProcessDataCorrectionPresenter extends AbstractMVPPresenter<SearchProcessDataCorrectionView>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6873439646301818591L;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;

	public static final String SEARCH_INTIMATION_DATA_CORRECTION = "search_intimation_data_correction";
	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_INTIMATION_DATA_CORRECTION) final ParameterDTO parameters) {
		String IntimationNo = (String) parameters.getPrimaryParameter();
		String userName = (String)parameters.getSecondaryParameter(0, String.class);
		
		view.list(dbCalculationService.getDataCoadingGetTask(userName,IntimationNo, null));
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
}
