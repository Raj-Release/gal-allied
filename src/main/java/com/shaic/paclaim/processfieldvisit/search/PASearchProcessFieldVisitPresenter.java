/**
 * 
 */
package com.shaic.paclaim.processfieldvisit.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.processfieldvisit.search.SearchProcessFieldVisitFormDTO;



/**
 * @author ntv.narenj
 *
 */

@ViewInterface(PASearchProcessFieldVisitView.class)
public class PASearchProcessFieldVisitPresenter extends AbstractMVPPresenter<PASearchProcessFieldVisitView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "padoSearchFVTable";
	
	@EJB
	private PASearchProcessFieldVisitService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchProcessFieldVisitFormDTO searchFormDTO = (SearchProcessFieldVisitFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
