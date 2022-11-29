package com.shaic.claim.OMPReopenClaimRODLevel.SearchBased.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@SuppressWarnings("serial")
@ViewInterface(OMPReopenClaimRODLevelSearchBasedView.class)
public class OMPReopenClaimRODLevelSearchBasedPresenter extends AbstractMVPPresenter<OMPReopenClaimRODLevelSearchBasedView>{
	
	public static final String RESET_SEARCH_VIEW = "Re Open Omp Rod Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = "Re Open Omp Rod Search Submit";
	public static final String DISABLE_SEARCH_FIELDS = "Re Open Omp Rod Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "Re Open Omp Rod Edit Intimation Screen";
	
	@EJB
	private OMPReopenClaimRODLevelSearchBasedService searchService;
	
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
//		System.out.println(view.getCurrentParameters());
		}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		OMPReopenClaimRODLevelSearchBasedFormDto searchFormDTO = (OMPReopenClaimRODLevelSearchBasedFormDto) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}

}
