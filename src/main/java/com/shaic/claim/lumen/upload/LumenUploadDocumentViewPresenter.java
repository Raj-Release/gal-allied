package com.shaic.claim.lumen.upload;

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
@ViewInterface(LumenUploadDocumentsView.class)
public class LumenUploadDocumentViewPresenter extends AbstractMVPPresenter<LumenUploadDocumentsView>{

	public static final String DELETE_LUMEN_DOCUMENT = "deletelumendocument";

	@Inject
	private LumenDbService lumenService;

	@Override
	public void viewEntered() {
		System.out.println("LumenUploadDocumentView Presenter Called...");
	}
	
	@SuppressWarnings("unchecked")
	public void handleSearch(@Observes @CDIEvent(DELETE_LUMEN_DOCUMENT) final ParameterDTO parameters) {
		DocumentTableDTO docTblDTO = (DocumentTableDTO) parameters.getPrimaryParameter();
		List<DocumentDetails> listOfDocs = (List<DocumentDetails>) parameters.getSecondaryParameters()[0];
		lumenService.removeDocumentRecInCache(listOfDocs,docTblDTO);
		view.reloadDocumentPopup(docTblDTO);
	}
}


