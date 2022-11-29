package com.shaic.claim.userproduct.document.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@ViewInterface(SearchDoctorDetailsView.class)
public class SearchDoctorDetailsPresenter extends AbstractMVPPresenter<SearchDoctorDetailsView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doDoctorSearchTable";
	
	public static final String DOCTOR_SEARCH_CRITERIA = "searchdoctorcriteria";
	
	public static final String DOCTOR_SEARCH_BUTTON_CLICK = "doSearchTableDoctor";
	
	@EJB
	private SearchDoctorDetailsService searchDoctorDetailsService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchDoctorNameDTO searchFormDTO = (SearchDoctorNameDTO) parameters.getPrimaryParameter();

		
		view.getResultList(searchDoctorDetailsService.search(searchFormDTO));
	}
	
	public void setupDoctorDetails(
			@Observes @CDIEvent(DOCTOR_SEARCH_CRITERIA) final ParameterDTO parameters) {
		SearchDoctorDetailsTableDTO viewSearchCriteriaDTO = (SearchDoctorDetailsTableDTO) parameters.getPrimaryParameter();
		view.setDoctorDetails(viewSearchCriteriaDTO);
	}
	
	@SuppressWarnings({ "deprecation" })
	public void handleDoctorSearch(@Observes @CDIEvent(DOCTOR_SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchDoctorDetailsTableDTO searchFormDTO = (SearchDoctorDetailsTableDTO) parameters.getPrimaryParameter();

		
		view.getResultList(searchDoctorDetailsService.searchDoctor(searchFormDTO));
	}
	
	@Override
	public void viewEntered() {
		
		
	}
}
