package com.shaic.claim.doctorinternalnotes;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.newcode.wizard.dto.NewIntimationDto;

@ViewInterface(SearchInternalNotesView.class)
public class SearchInternalNotesPresenter extends AbstractMVPPresenter<SearchInternalNotesView> {
	
	private static final long serialVersionUID = -5504472929540762973L;
	
	public static final String SEARCH_INTERNAL_NOTES_BUTTON_CLICK = "SearchInternalNotesBtnClick";
	
	@EJB
	private SearchInternalNotesService searchService;
	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_INTERNAL_NOTES_BUTTON_CLICK) final ParameterDTO parameters) {
		NewIntimationDto searchFormDTO = (NewIntimationDto) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		view.list(searchService.search(searchFormDTO,userName));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
	}

}
