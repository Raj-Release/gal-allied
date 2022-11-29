package com.shaic.claim.lumen.searchlevelone;

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
@ViewInterface(ProcessLevelOneWizard.class)
public class ProcessLevelOneWizardPresenter extends AbstractMVPPresenter<ProcessLevelOneWizard>{
	
	public static final String CANCEL_LEVEL_I_REQUEST = "cancel_level_1_submit";
	public static final String SUBMIT_LEVEL_I_REQUEST = "level_1_submit";
	public static final String LEVEL_I_MIS_DETAILS = "level_1_mis_details";
	
	@Inject
	LumenDbService lumenService;
	
	@Override
	public void viewEntered() {
		System.out.println("ProcessLevelOneWizardPresenter called....");
		
	}
	public void cancelLumenRequest(@Observes @CDIEvent(CANCEL_LEVEL_I_REQUEST) final ParameterDTO parameters) {
		view.cancelLumenRequest();
	}
	
	public void submitLumenRequest(@Observes @CDIEvent(SUBMIT_LEVEL_I_REQUEST) final ParameterDTO parameters) {
		view.submitLumenRequest();
	}
	
	public void getMISDetailsForRequest(@Observes @CDIEvent(LEVEL_I_MIS_DETAILS) final ParameterDTO parameters) {
		MISQueryReplyDTO searchDTO = (MISQueryReplyDTO) parameters.getPrimaryParameter();
		Map<String, List<?>> dataHolder = lumenService.getMISReplyDetails(searchDTO);
		view.showReplyFromMISSubView(dataHolder, searchDTO.getMisReplyRemarks());
	}	
	
}
