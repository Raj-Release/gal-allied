package com.shaic.claim.intimation.unlockintimationaudit;
import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.naming.NamingException;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.IntimationService;


@ViewInterface(SearchUnlockIntimationAuditDBView.class)
public class SearchUnlockIntimationAuditDBPresenter extends AbstractMVPPresenter<SearchUnlockIntimationAuditDBView> {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK_AUDIT_INTIMATION = "search_button_click_audit_intimation";
	
	public static final String UNLOCK_AUDIT_INTIMATION = "unlock_audit_intimation";
	
	@EJB
	private SearchUnlockIntimationAuditDBService searchService;
	
	@EJB
	private IntimationService intimationService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_AUDIT_INTIMATION) final ParameterDTO parameters) throws NamingException {
		
		SearchUnlockIntimationAuditDBFormDTO searchFormDTO = (SearchUnlockIntimationAuditDBFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.searchForUnlockIntimation(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings({ "deprecation" })
	public void unlockauditIntimation(@Observes @CDIEvent(UNLOCK_AUDIT_INTIMATION) final ParameterDTO parameters) throws NamingException {
		
		String intimation = (String) parameters.getPrimaryParameter();	
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		searchService.unlockauditIntimation(intimation,userName);
	}
	

} 


