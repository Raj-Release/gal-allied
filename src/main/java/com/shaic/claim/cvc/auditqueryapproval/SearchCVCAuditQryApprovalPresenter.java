package com.shaic.claim.cvc.auditqueryapproval;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionFormDTO;



@SuppressWarnings("serial")
@ViewInterface(SearchCVCAuditQryApprovalView.class)
public class SearchCVCAuditQryApprovalPresenter extends AbstractMVPPresenter<SearchCVCAuditQryApprovalView>{
	
	public static final String RESET_SEARCH_VIEW = "CVC Audit Qry Approval Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = "CVC Audit Qry Approval Search Submit";
	public static final String DISABLE_SEARCH_FIELDS = "CVC Audit Qry Approval Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "CVC Audit Qry Approval Edit Intimation Screen";
	public static final String SUBMIT_ALERT = "CVC Audit Qry Approval validation";
	
	@EJB
	private SearchCVCAuditQryApprovalService searchService;
	
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
		}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		SearchCVCAuditActionFormDTO searchFormDTO = (SearchCVCAuditActionFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	public void handleSearchError(@Observes @CDIEvent(SUBMIT_ALERT) final ParameterDTO parameters) {
		SearchCVCAuditActionFormDTO searchFormDTO = (SearchCVCAuditActionFormDTO) parameters.getPrimaryParameter();
		view.validation(searchFormDTO);
	}

}
