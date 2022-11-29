package com.shaic.claim.processtranslation.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchProcessTranslationView.class)
public class SearchProcessTranslationPresenter  extends AbstractMVPPresenter<SearchProcessTranslationView> {

//
//@EJB
//PedService pedService;



/**
 * 
 */
private static final long serialVersionUID = 1L;

@EJB
private SearchProcessTranslationService searchProcessTranslationService;

//public static final String SEARCH_BUTTON_CLICK = "convertClaimSearchClick";
public static final String SEARCH_BUTTON_CLICK = "searchBtn";

public void searchClick(
		@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
	SearchProcessTranslationFormDTO searchFormDTO = (SearchProcessTranslationFormDTO) parameters
			.getPrimaryParameter();
	String userName=(String)parameters.getSecondaryParameter(0, String.class);
	String passWord=(String)parameters.getSecondaryParameter(1, String.class);
	
	view.list(searchProcessTranslationService.search(searchFormDTO,userName,passWord));
	
}

@Override
public void viewEntered() {

}

	


}
