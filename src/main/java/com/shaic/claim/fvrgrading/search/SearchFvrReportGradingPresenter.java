package com.shaic.claim.fvrgrading.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchFvrReportGradingView.class)
public class SearchFvrReportGradingPresenter extends
		AbstractMVPPresenter<SearchFvrReportGradingView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SearchFvrReportGradingService searchConvertClaimService;
	
	/*@EJB
	private MasterService masterService;*/

	//public static final String SEARCH_BUTTON_CLICK = "convertClaimSearchClick";
	public static final String SEARCH_FVR_GRADING = "searchfvrgrading";

	public void searchClick(
			@Observes @CDIEvent(SEARCH_FVR_GRADING) final ParameterDTO parameters) {
		SearchFvrReportGradingFormDto searchFormDTO = (SearchFvrReportGradingFormDto) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchConvertClaimService.search(searchFormDTO,userName,passWord));
	}
	
	/*public void loadLOBValue(
			@Observes @CDIEvent(LOAD_LOB) final ParameterDTO parameters) {
		SearchConvertClaimFormDto searchFormDTO = (SearchConvertClaimFormDto) parameters
				.getPrimaryParameter();
		String strLob = (String)parameters.getPrimaryParameter();
		Long lobId = Long.parseLong((strLob));
		searchConvertClaimService.set
	}*/

	@Override
	public void viewEntered() {

	}

}
