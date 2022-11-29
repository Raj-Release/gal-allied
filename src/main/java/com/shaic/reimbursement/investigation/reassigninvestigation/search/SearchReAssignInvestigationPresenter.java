/**
 * 
 */
package com.shaic.reimbursement.investigation.reassigninvestigation.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationFormDTO;



/**
 * @author ntv.narenj
 *
 */

@ViewInterface(SearchReAssignInvestigationView.class)
public class SearchReAssignInvestigationPresenter extends AbstractMVPPresenter<SearchReAssignInvestigationView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_RE_ASSIGN_INV_BUTTON_CLICK = "doSearchReAsignInvTable";
	
	@EJB
	private SearchReAssignInvestigationService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_RE_ASSIGN_INV_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchAssignInvestigationFormDTO searchFormDTO = (SearchAssignInvestigationFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
