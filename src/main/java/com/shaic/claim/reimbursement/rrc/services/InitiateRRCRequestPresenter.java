package com.shaic.claim.reimbursement.rrc.services;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.UsertoCPUMappingService;

@ViewInterface(InitiateRRCRequestView.class)
public class InitiateRRCRequestPresenter extends AbstractMVPPresenter<InitiateRRCRequestView > {

	
public static final String SEARCH_BUTTON_CLICK_INITIATE_RRC_REQUEST = "doSearchForInitiateRRC";
public static final String RRC_INITIATE_PAGE = "RRCInitiatePage";
	
	@EJB
	private UsertoCPUMappingService userCPUMapService;
	
	@EJB
	private InitiateRRCRequestService searchService;
	public static final String SHOW_FVR_REQUEST_VIEW = "show_FVR_request_view";
	
	public static final String GENERATE_REPORT = "generate_fvr_report";

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_INITIATE_RRC_REQUEST) final ParameterDTO parameters) {
		
		InitiateRRCRequestFormDTO searchFormDTO = (InitiateRRCRequestFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void initiateRRCPopUp(@Observes @CDIEvent(RRC_INITIATE_PAGE) final ParameterDTO parameters) {
			
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	

}
