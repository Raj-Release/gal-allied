/**
 * 
 */
package com.shaic.paclaim.medicalapproval.processclaimrequest.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.paclaim.reimbursement.service.PASearchProcessClaimRequestService;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestFormDTO;



/**
 * @author ntv.narenj
 *
 */

@ViewInterface(PASearchProcessClaimRequestView.class)
public class PASearchProcessClaimRequestPresenter extends AbstractMVPPresenter<PASearchProcessClaimRequestView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "do_search_pa_claim_request";
	public static final String SPECIALITY = "speciality";
	
	@EJB
	private PASearchProcessClaimRequestService searchService;

	
	//@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchProcessClaimRequestFormDTO searchFormDTO = (SearchProcessClaimRequestFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		String screenName=(String)parameters.getSecondaryParameter(2, String.class);
		

		if(null != screenName && !(screenName.equalsIgnoreCase(SHAConstants.PA_MEDICAL_WAIT_FOR_INPUT_SCREEN))){
			
			view.list(searchService.search(searchFormDTO,userName,passWord,screenName));
		}
		else
		{
			view.list(searchService.searchForWaitForInput(searchFormDTO,userName,passWord,screenName));
		}
		
		
	}
	
	
	//@SuppressWarnings({ "deprecation" })
	public void getspeciality(@Observes @CDIEvent(SPECIALITY) final ParameterDTO parameters) {
		
		Long key = (Long) parameters.getPrimaryParameter();
		
		
		view.specialityList(searchService.getSpecialityValueByReference(key));
	}
	
	
	@Override
	public void viewEntered() {
		
		
	}

}
