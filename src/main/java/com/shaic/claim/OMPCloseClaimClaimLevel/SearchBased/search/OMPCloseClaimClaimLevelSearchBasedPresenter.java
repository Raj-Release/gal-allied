package com.shaic.claim.OMPCloseClaimClaimLevel.SearchBased.search;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@SuppressWarnings("serial")
@ViewInterface(OMPCloseClaimClaimLevelSearchBasedView.class)
public class OMPCloseClaimClaimLevelSearchBasedPresenter extends AbstractMVPPresenter<OMPCloseClaimClaimLevelSearchBasedView>{
	
	public static final String RESET_SEARCH_VIEW = "OMP CloseClaimSearch Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = "Close Claim Search Submit";
	public static final String DISABLE_SEARCH_FIELDS = "OMP CloseClaimSearch Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "OMP CloseClaimSearch Edit Intimation Screen";
	
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
//		System.out.println(view.getCurrentParameters());
		}
	
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		OMPCloseClaimClaimLevelSearchBasedFormDto searchFormDTO = (OMPCloseClaimClaimLevelSearchBasedFormDto) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
//		view.list(searchService.search(searchFormDTO,userName,passWord));
	}

}
