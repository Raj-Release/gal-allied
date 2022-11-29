package com.shaic.reimbursement.manageclaim.HoldMonitorFLPScreen;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenFormDTO;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;
import com.vaadin.server.VaadinSession;

@ViewInterface(SearchHoldMonitorFLPScreenView.class)
public class SearchMonitorFLPScreenPresenter extends AbstractMVPPresenter<SearchHoldMonitorFLPScreenView> {
	
	private static final long serialVersionUID = 8716581020528848930L;

	public static final String SEARCH_BUTTON = "doFLSearch";
	
	public static final String RELEASE_BUTTON = "releaseholdFLclaim";
	
	@EJB
	private SearchHoldMonitorFLScreenService searchHoldMonitorService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON) final ParameterDTO parameters) {
		SearchHoldMonitorScreenFormDTO formDTO = (SearchHoldMonitorScreenFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		view.list(searchHoldMonitorService.search(formDTO, userName, passWord));
	}
	
	public void submitReleaseHoldClaim(@Observes @CDIEvent(RELEASE_BUTTON) final ParameterDTO parameters){
		SearchHoldMonitorScreenTableDTO tableDto = (SearchHoldMonitorScreenTableDTO) parameters.getPrimaryParameter();
		String message = dbCalculationService.releaseHoldClaim(tableDto.getWkKey());
		String strUserName =  (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		tableDto.setUsername(strUserName);
		Preauth preauthObj = searchHoldMonitorService.getPreauthByKey(tableDto.getPreauthKey());
		searchHoldMonitorService.updateStageInformation(tableDto,preauthObj);
		view.buildSuccessLayout(message);
	}

	@Override
	public void viewEntered() {
		System.out.println("SearchHoldMonitorFLPScreenView Presenter called");
	}
	
}
