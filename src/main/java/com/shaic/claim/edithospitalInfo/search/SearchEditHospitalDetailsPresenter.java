package com.shaic.claim.edithospitalInfo.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;



@ViewInterface(SearchEditHospitalDetailsView.class)
public class SearchEditHospitalDetailsPresenter extends
		AbstractMVPPresenter<SearchEditHospitalDetailsView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private SearchEditHospitalDetailsService editHospitalDetailsService;

	public static final String SEARCH_BUTTON_CLICK = "editHospitalInfoSearchBtn";	
	
	public void handleSearch(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchEditHospitalDetailsFormDTO searchFormDTO = (SearchEditHospitalDetailsFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
        String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		
		view.list(editHospitalDetailsService.search(searchFormDTO,userName,passWord));
	}	

	@Override
	public void viewEntered() {

	}
}
