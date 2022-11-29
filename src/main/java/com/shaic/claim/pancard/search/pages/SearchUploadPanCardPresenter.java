/**
 * 
 */
package com.shaic.claim.pancard.search.pages;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;



/**
 * @author ntv.narenj
 *
 */

@ViewInterface(SearchUploadPanCardView.class)
public class SearchUploadPanCardPresenter extends AbstractMVPPresenter<SearchUploadPanCardView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "pan_doSearchupTable";
	
	@EJB
	private SearchUploadPanCardService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchUploadPanCardDTO searchFormDTO = (SearchUploadPanCardDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
			view.list(searchService.search(searchFormDTO,userName,passWord));
			/*else{
			String emsg ="Please Enter Intimation no";
			view.showErrorPopUp(emsg);
		}*/
	}
	
	@Override
	public void viewEntered() {
		
		
	}

	
}
