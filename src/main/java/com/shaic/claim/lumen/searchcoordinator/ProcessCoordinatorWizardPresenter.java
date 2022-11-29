package com.shaic.claim.lumen.searchcoordinator;

import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.components.MISQueryReplyDTO;

@SuppressWarnings("serial")
@ViewInterface(ProcessCoordinatorWizard.class)
public class ProcessCoordinatorWizardPresenter extends AbstractMVPPresenter<ProcessCoordinatorWizard>{
	
	public static final String CANCEL_COORDINATOR_REQUEST = "cancel_coordinator_submit";
	public static final String SUBMIT_COORDINATOR_REQUEST = "coordinator_submit";
	public static final String COORDINATOR_MIS_DETAILS = "coordinator_mis_details";
	
	@Inject
	LumenDbService lumenService;
	
	@Override
	public void viewEntered() {
		System.out.println("ProcessCoordinatorWizardPresenter called....");
		
	}
	public void cancelLumenRequest(@Observes @CDIEvent(CANCEL_COORDINATOR_REQUEST) final ParameterDTO parameters) {
		view.cancelLumenRequest();
	}
	
	public void submitLumenRequest(@Observes @CDIEvent(SUBMIT_COORDINATOR_REQUEST) final ParameterDTO parameters) {
		view.submitLumenRequest();
	}
	
	public void getMISDetailsForRequest(@Observes @CDIEvent(COORDINATOR_MIS_DETAILS) final ParameterDTO parameters) {
		MISQueryReplyDTO searchDTO = (MISQueryReplyDTO) parameters.getPrimaryParameter();
		Map<String, List<?>> dataHolder = lumenService.getMISReplyDetails(searchDTO);
		view.showReplyFromMISSubView(dataHolder,searchDTO.getMisReplyRemarks());
	}	
	
}
