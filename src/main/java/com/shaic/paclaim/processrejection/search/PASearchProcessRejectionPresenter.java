package com.shaic.paclaim.processrejection.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.processrejection.search.SearchProcessRejectionFormDTO;



@ViewInterface(PASearchProcessRejectionView.class)
public class PASearchProcessRejectionPresenter extends
		AbstractMVPPresenter<PASearchProcessRejectionView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private PASearchProcessRejectionService processRejectionService;

	public static final String SEARCH_BUTTON_CLICK = "pa_prSearchBtn";	
	
	public void handleSearch(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchProcessRejectionFormDTO searchFormDTO = (SearchProcessRejectionFormDTO) parameters
				.getPrimaryParameter();
	
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		
		view.list(processRejectionService.search(searchFormDTO,userName,passWord));
	}	

	@Override
	public void viewEntered() {

	}
}
