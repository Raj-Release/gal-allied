/**
 * 
 */
package com.shaic.reimbursement.financialapprover.processclaimrequestBenefits.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;



/**
 * @author ntv.narenj
 *
 */

@ViewInterface(SearchProcessClaimRequestBenefitsFinancView.class)
public class SearchProcessClaimRequestBenefitsFinancPresenter extends AbstractMVPPresenter<SearchProcessClaimRequestBenefitsFinancView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchssppafinaceTable";
	public static final String SPECIALITY = "speciality";
	
	@EJB
	private SearchProcessClaimRequestBenefitsFinancService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchProcessClaimRequestBenefitsFinancFormDTO searchFormDTO = (SearchProcessClaimRequestBenefitsFinancFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void getspeciality(@Observes @CDIEvent(SPECIALITY) final ParameterDTO parameters) {
		
		Long key = (Long) parameters.getPrimaryParameter();
		
		
		view.specialityList(searchService.getSpecialityValueByReference(key));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
