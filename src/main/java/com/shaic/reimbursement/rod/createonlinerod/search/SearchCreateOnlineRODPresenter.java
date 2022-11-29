/**
 * 
 */
package com.shaic.reimbursement.rod.createonlinerod.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODService;



/**
 * @author ntv.narenj
 *
 */

@ViewInterface(SearchCreateOnlineRODView.class)
public class SearchCreateOnlineRODPresenter extends AbstractMVPPresenter<SearchCreateOnlineRODView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CREATE_ONLINE_ROD_SEARCH_BUTTON_CLICK = "create_online_rod_search_button_click";
	
	@EJB
	private SearchCreateOnlineRODService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(CREATE_ONLINE_ROD_SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchCreateOnlineRODFormDTO searchFormDTO = (SearchCreateOnlineRODFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
