/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.medicalapproval.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;


@ViewInterface(PAHealthSearchProcessClaimRequestView.class)
public class PAHealthSearchProcessClaimRequestPresenter extends AbstractMVPPresenter<PAHealthSearchProcessClaimRequestView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "do_search_pa_health";
	public static final String SPECIALITY = "speciality_pa_health";
	
	@EJB
	private PAHealthSearchProcessClaimRequestService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PAHealthSearchProcessClaimRequestFormDTO searchFormDTO = (PAHealthSearchProcessClaimRequestFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		String screenName=(String)parameters.getSecondaryParameter(2, String.class);


		if(null != screenName && !(screenName.equalsIgnoreCase(SHAConstants.PA_HEALTH_MEDICAL_WAIT_FOR_INPUT_SCREEN))){

			view.list(searchService.search(searchFormDTO,userName,passWord,screenName));
		}
		else
		{
			view.list(searchService.searchForWaitForInput(searchFormDTO,userName,passWord,screenName));
		}
		
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
