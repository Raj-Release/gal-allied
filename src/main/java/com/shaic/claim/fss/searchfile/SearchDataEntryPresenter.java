
/**
 * 
 */
package com.shaic.claim.fss.searchfile;

/**
 * 
 *
 */

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@ViewInterface(SearchDataEntryView.class)
public class SearchDataEntryPresenter extends AbstractMVPPresenter<SearchDataEntryView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK_DATA_ENTRY_FORM = "doSearchForDataEntry";
	
	
	@EJB
	private SearchDataEntryService searchService;
	

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_DATA_ENTRY_FORM) final ParameterDTO parameters) {
		
		SearchDataEntryFormDTO searchFormDTO = (SearchDataEntryFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}

