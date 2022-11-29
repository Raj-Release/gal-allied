package com.shaic.claim.corpbuffer.allocation.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.corpbuffer.allocation.wizard.AllocateCorpBufferDetailDTO;

@ViewInterface(AllocateCorpBufferView.class)
public class AllocateCorpBufferPresenter extends AbstractMVPPresenter<AllocateCorpBufferView> {
	
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "doSearchCorpBufferTable";
	
	public static final String SUBMIT_BUTTON_CLICK = "submitCorpBufferDetails";
	
	@EJB
	private AllocateCorpBufferService searchService;

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		AllocateCorpBufferFormDTO searchFormDTO = (AllocateCorpBufferFormDTO) parameters.getPrimaryParameter();
		view.list(searchService.search(searchFormDTO));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void submitDetails(@Observes @CDIEvent(SUBMIT_BUTTON_CLICK) final ParameterDTO parameters) {
		AllocateCorpBufferDetailDTO corpBufferDto = (AllocateCorpBufferDetailDTO) parameters.getPrimaryParameter();
		searchService.updateCorpBufferLimit(corpBufferDto);
	}
	
	@Override
	public void viewEntered() {
		
	}

}
