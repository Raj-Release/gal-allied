package com.shaic.claim.search.specialist.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SubmitSpecialistView.class)
public class SubmitSpecialistPresenter extends
		AbstractMVPPresenter<SubmitSpecialistView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "Search Clicked";
	
	@EJB
	private SubmitSpecialistService submitSpecialistService;	
	
	@EJB
	private SubmitSpecialistAdviseService submitSpecialistServiceR3;
	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SubmitSpecialistFormDTO searchFormDTO = (SubmitSpecialistFormDTO) parameters
				.getPrimaryParameter();
		boolean reimburementFlag = (boolean) parameters.getSecondaryParameters()[0];
		
		if(reimburementFlag){
			view.list(submitSpecialistServiceR3.search(searchFormDTO, reimburementFlag));
		}else{
			view.list(submitSpecialistService.search(searchFormDTO, reimburementFlag));
		}
		
		
	
	}

	@Override
	public void viewEntered() {

	}

}
