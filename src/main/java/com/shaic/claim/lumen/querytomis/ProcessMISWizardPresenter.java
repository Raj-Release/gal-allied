package com.shaic.claim.lumen.querytomis;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.lumen.LumenDbService;
import com.shaic.domain.DocumentDetails;

@SuppressWarnings("serial")
@ViewInterface(ProcessMISWizard.class)
public class ProcessMISWizardPresenter extends AbstractMVPPresenter<ProcessMISWizard>{
	
	public static final String CANCEL_QUERY_MIS_REQUEST = "cancel_query_mis_submit";
	public static final String SUBMIT_QUERY_MIS_REQUEST = "query_mis_submit";
	
	public static final String DELETE_MIS_LUMEN_DOCUMENT = "deletemislumendocument";
	
	@Inject
	LumenDbService lumenService;
	
	@Override
	public void viewEntered() {
		System.out.println("ProcessMISWizardPresenter called....");
		
	}
	public void cancelLumenRequest(@Observes @CDIEvent(CANCEL_QUERY_MIS_REQUEST) final ParameterDTO parameters) {
		view.cancelLumenRequest();
	}
	
	public void submitLumenRequest(@Observes @CDIEvent(SUBMIT_QUERY_MIS_REQUEST) final ParameterDTO parameters) {
		view.submitLumenRequest();
	}
	
	@SuppressWarnings("unchecked")
	public void handleSearch(@Observes @CDIEvent(DELETE_MIS_LUMEN_DOCUMENT) final ParameterDTO parameters) {
		DocumentAckTableDTO docTblDTO = (DocumentAckTableDTO) parameters.getPrimaryParameter();
		List<DocumentDetails> listOfDocs = (List<DocumentDetails>) parameters.getSecondaryParameters()[0];
		List<DocumentAckTableDTO> listOfAckDocs = (List<DocumentAckTableDTO>) parameters.getSecondaryParameters()[1];
		lumenService.removeDocumentRecInCache(listOfDocs,docTblDTO);
		lumenService.removeMISDocumentRecInCache(listOfAckDocs,docTblDTO);
		view.reloadDocumentPopup(docTblDTO);
	}
	
}
