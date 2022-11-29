package com.shaic.domain.gmcautomailer;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(GmcAutomailerView.class)
public class GmcAutomailerPresenter extends AbstractMVPPresenter<GmcAutomailerView>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "automailerSearchButton";
	public static final String SUBMIT_GMC_AUTOMAILER = "submitAutomailer";
	public static final String SUBMIT_NEW_ENTRY = "submitNewEntry";
	
	@EJB
	private GmcAutomailerService gmcAutomailerService;
	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		GmcAutomailerFormDTO searchFormDTO = (GmcAutomailerFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		//String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(gmcAutomailerService.search(searchFormDTO));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub		
	}
	
	public void submitUncheckNegotiation(@Observes @CDIEvent(SUBMIT_GMC_AUTOMAILER) final ParameterDTO parameters) {
		List<GmcAutomailerTableDTO> emailDetails = (List<GmcAutomailerTableDTO>) parameters.getPrimaryParameter();		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		gmcAutomailerService.submitEmailId(emailDetails,userName);
		
	}
	
	public void submitNewEntry(@Observes @CDIEvent(SUBMIT_NEW_ENTRY) final ParameterDTO parameters) {
		GmcAutomailerFormDTO emailDetails = (GmcAutomailerFormDTO) parameters.getPrimaryParameter();		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		gmcAutomailerService.submitNewEntry(emailDetails,userName);
		
	}
	
}
