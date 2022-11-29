package com.shaic.claim.reimbursement.talktalktalk;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestFormDTO;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestService;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestView;
import com.shaic.domain.UsertoCPUMappingService;

@ViewInterface(InitiateTalkTalkTalkView.class)
public class InitiateTalkTalkTalkPresenter  extends AbstractMVPPresenter<InitiateTalkTalkTalkView> {


public static final String SEARCH_BUTTON_CLICK_INITIATE_TALK_TALK = "doSearchForInitiateTALKTALKTALK";
public static final String TALK_TALK_TALK_INITIATE_PAGE = "tALKTALKTALKInitiatePage";
	
	@EJB
	private UsertoCPUMappingService userCPUMapService;
	
	@EJB
	private InitiateTalkTalkTalkService searchService;
	public static final String SHOW_FVR_REQUEST_VIEW = "show_FVR_request_view";
	
	public static final String GENERATE_REPORT = "generate_fvr_report";

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_INITIATE_TALK_TALK) final ParameterDTO parameters) {
		
		InitiateTalkTalkTalkFormDTO searchFormDTO = (InitiateTalkTalkTalkFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void initiateRRCPopUp(@Observes @CDIEvent(TALK_TALK_TALK_INITIATE_PAGE) final ParameterDTO parameters) {
			
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	

}

