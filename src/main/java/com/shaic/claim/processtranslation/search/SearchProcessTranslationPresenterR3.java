package com.shaic.claim.processtranslation.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchProcessTranslationViewR3.class)
public class SearchProcessTranslationPresenterR3 extends AbstractMVPPresenter<SearchProcessTranslationViewR3>{

	@EJB
	private SearchProcessTranslationService searchProcessTranslationService;

	//public static final String SEARCH_BUTTON_CLICK = "convertClaimSearchClick";
	public static final String SEARCH_BUTTON_CLICKR3 = "searchR3Btn";

	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICKR3) final ParameterDTO parameters) {
		SearchProcessTranslationFormDTO searchFormDTO = (SearchProcessTranslationFormDTO) parameters
				.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
	//	view.list(searchProcessTranslationService.searchR3(searchFormDTO,userName,passWord));
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
