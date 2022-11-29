package com.shaic.claim.OMPProcessNegotiation.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@SuppressWarnings("serial")
@ViewInterface(OMPProcessNegotiationView.class)
public class OMPProcessNegotiationPresenter extends AbstractMVPPresenter<OMPProcessNegotiationView>{
	
	public static final String RESET_SEARCH_VIEW = "OMP Negotiation Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = " Negotiation Search Submit";
	public static final String DISABLE_SEARCH_FIELDS = "OMP Negotiation Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "OMP Negotiation Edit Intimation Screen";
	
	
	@EJB
	private OMPProcessNegotiationService searchService;
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
//		System.out.println(view.getCurrentParameters());
		}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		OMPProcessNegotiationFormDto searchFormDTO = (OMPProcessNegotiationFormDto) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	

}
