package com.shaic.claim.cvc.auditqueryreplyprocessing.fa;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryFormDTO;




@SuppressWarnings("serial")
@ViewInterface(SearchCVCAuditFaQryView.class)
public class SearchCVCAuditFaQryPresenter extends AbstractMVPPresenter<SearchCVCAuditFaQryView>{
	
	public static final String RESET_SEARCH_VIEW = "CVC Audit Fa Qry Rly Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = "CVC Audit Fa Qry Rly Search Submit";
	public static final String DISABLE_SEARCH_FIELDS = "CVC Audit Fa Qry Rly Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "CVC Audit Fa Qry Rly Edit Intimation Screen";
	
	@EJB
	private SearchCVCAuditFaQryService searchService;
	
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
		}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		SearchCVCAuditClsQryFormDTO searchFormDTO = (SearchCVCAuditClsQryFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}

}
