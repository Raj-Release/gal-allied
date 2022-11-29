package com.shaic.reimbursement.manageclaim.HoldMonitorScreen;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;

@ViewInterface(SearchHoldMonitorScreenView.class)
public class SearchMonitorScreenPreseneter extends AbstractMVPPresenter<SearchHoldMonitorScreenView> {
	
	public static final String SEARCH_BUTTON = "doSearch";
	
	public static final String RELEASE_BUTTON = "releaseholdclaim";
	
	@EJB
	private SearchHoldMonitorScreenService searchHoldMonitorService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private CreateRODService createRODService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON) final ParameterDTO parameters) {
		
		SearchHoldMonitorScreenFormDTO formDTO = (SearchHoldMonitorScreenFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		String ScreenName = "BLPR";
		view.list(searchHoldMonitorService.search(formDTO, userName, passWord));
	}
	
	public void submitReleaseHoldClaim(
			@Observes @CDIEvent(RELEASE_BUTTON) final ParameterDTO parameters){
		SearchHoldMonitorScreenTableDTO tableDto = (SearchHoldMonitorScreenTableDTO) parameters.getPrimaryParameter();
		String message = dbCalculationService.releaseHoldClaim(tableDto.getWkKey());
		String strUserName =  (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		tableDto.setUsername(strUserName);
		Reimbursement currentReimbursement = createRODService.getReimbursementObjectByKey(tableDto.getPreauthKey());
		searchHoldMonitorService.updateStageInformation(tableDto,currentReimbursement);
		view.buildSuccessLayout(message);
		
	
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}