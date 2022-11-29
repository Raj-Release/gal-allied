package com.shaic.claim.intimation.unlock;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.naming.NamingException;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.IntimationService;


@ViewInterface(SearchUnlockIntimationView.class)
public class SearchUnlockIntimationPresenter extends AbstractMVPPresenter<SearchUnlockIntimationView> {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "search for unlock intimation";
	
	@EJB
	private SearchUnlockIntimationService searchService;
	
	@EJB
	private IntimationService intimationService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) throws NamingException {
		
		SearchUnlockIntimationFormDTO searchFormDTO = (SearchUnlockIntimationFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.searchForUnlockIntimation(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

} 

