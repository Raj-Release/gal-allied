package com.shaic.claim.cvc.auditaction;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.MasClmAuditUserMapping;
import com.shaic.domain.MasterService;


/**
 * @author GokulPrasath.A
 *
 */
@SuppressWarnings("serial")
@ViewInterface(SearchCVCAuditActionView.class)
public class SearchCVCAuditActionPresenter extends AbstractMVPPresenter<SearchCVCAuditActionView>{
	
	public static final String RESET_SEARCH_VIEW = "CVC Audit Action Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = "CVC Audit Action Search Submit";
	public static final String DISABLE_SEARCH_FIELDS = "CVC Audit Action Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "CVC Audit Action Edit Intimation Screen";
	
	public static final String GET_AUDIT_USER_CLM_TYPE = "Get Claim Type For Audit User";
	public static final String VALIDATE_SUBMIT = "CVC Audit Action Validate";
	
	@EJB
	private SearchCVCAuditActionService searchService;
	
	@EJB
	private MasterService  masterService;
	
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

	public void getClmTypeForAuditUser(@Observes @CDIEvent(GET_AUDIT_USER_CLM_TYPE) final ParameterDTO parameters) {
			
		String userName=(String)parameters.getPrimaryParameter();
		
		MasClmAuditUserMapping auditUserMap = masterService.getAuditUserByEmpId(userName);
		
		view.setAuditUser(auditUserMap);
	}
	
	public void handleSearchError(@Observes @CDIEvent(VALIDATE_SUBMIT) final ParameterDTO parameters) {
		SearchCVCAuditActionFormDTO searchFormDTO = (SearchCVCAuditActionFormDTO) parameters.getPrimaryParameter();
		view.validation(searchFormDTO);
	}

}
