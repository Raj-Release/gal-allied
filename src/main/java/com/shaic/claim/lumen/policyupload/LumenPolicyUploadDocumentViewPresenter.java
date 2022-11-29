package com.shaic.claim.lumen.policyupload;

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
@ViewInterface(LumenPolicyUploadDocumentsView.class)
public class LumenPolicyUploadDocumentViewPresenter extends AbstractMVPPresenter<LumenPolicyUploadDocumentsView>{

	public static final String POLICY_DELETE_LUMEN_DOCUMENT = "policydeletelumendocument";

	@Inject
	private LumenDbService lumenService;

	@Override
	public void viewEntered() {
		System.out.println("LumenPolicyUploadDocumentView Presenter Called...");
	}
	
	@SuppressWarnings("unchecked")
	public void handleSearch(@Observes @CDIEvent(POLICY_DELETE_LUMEN_DOCUMENT) final ParameterDTO parameters) {
		PolicyDocumentTableDTO docTblDTO = (PolicyDocumentTableDTO) parameters.getPrimaryParameter();
		List<DocumentDetails> listOfDocs = (List<DocumentDetails>) parameters.getSecondaryParameters()[0];
		lumenService.removeDocumentRecInCache(listOfDocs,docTblDTO);
		view.reloadDocumentPopup(docTblDTO);
	}
}


