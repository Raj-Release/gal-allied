package com.shaic.claim.OMPProcessOmpClaimApprover.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@SuppressWarnings("serial")
@ViewInterface(OMPProcessOmpClaimApproverView.class)
public class OMPProcessOmpClaimApproverPresenter extends AbstractMVPPresenter<OMPProcessOmpClaimApproverView>{
	
	public static final String RESET_SEARCH_VIEW = "OMP Approver Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = "Approver Search Submit";
	public static final String DISABLE_SEARCH_FIELDS = "OMP Approver Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "OMP Approver Edit Intimation Screen";
	
	@EJB
	private OMPProcessOmpClaimApproverService searchService;
	
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
//		System.out.println(view.getCurrentParameters());
		}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		OMPProcessOmpClaimApproverFormDto searchFormDTO = (OMPProcessOmpClaimApproverFormDto) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}

}
