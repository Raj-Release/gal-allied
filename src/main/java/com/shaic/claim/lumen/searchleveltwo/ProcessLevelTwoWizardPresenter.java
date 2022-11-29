package com.shaic.claim.lumen.searchleveltwo;

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
@ViewInterface(ProcessLevelTwoWizard.class)
public class ProcessLevelTwoWizardPresenter extends AbstractMVPPresenter<ProcessLevelTwoWizard>{
	
	public static final String CANCEL_LEVEL_II_REQUEST = "cancel_level_2_submit";
	public static final String SUBMIT_LEVEL_II_REQUEST = "level_2_submit";
	public static final String LEVEL_II_MIS_DETAILS = "level_2_mis_details";
//	public static final String SHOW_GENERATE_LETTER_TABLE = "showgeneratelettertable";
	
	@Inject
	LumenDbService lumenService;
	
	@Override
	public void viewEntered() {
		System.out.println("ProcessLevelTwoWizardPresenter called....");
		
	}
	public void cancelLumenRequest(@Observes @CDIEvent(CANCEL_LEVEL_II_REQUEST) final ParameterDTO parameters) {
		view.cancelLumenRequest();
	}
	
	public void submitLumenRequest(@Observes @CDIEvent(SUBMIT_LEVEL_II_REQUEST) final ParameterDTO parameters) {
		view.submitLumenRequest();
	}
	
	public void getMISDetailsForRequest(@Observes @CDIEvent(LEVEL_II_MIS_DETAILS) final ParameterDTO parameters) {
		MISQueryReplyDTO searchDTO = (MISQueryReplyDTO) parameters.getPrimaryParameter();
		Map<String, List<?>> dataHolder = lumenService.getMISReplyDetails(searchDTO);
		view.showReplyFromMISSubView(dataHolder, searchDTO.getMisReplyRemarks());
	}	
	
	/*public void showLetterTable(@Observes @CDIEvent(SHOW_GENERATE_LETTER_TABLE) final ParameterDTO parameters) {
		Boolean generateLetterFlag = (Boolean) parameters.getPrimaryParameter();
		view.showGenerateLetterTable(generateLetterFlag);	
	}*/
}
