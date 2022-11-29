package com.shaic.claim.OMPclaimprocessmaker.search;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@SuppressWarnings("serial")
@ViewInterface(OMPClaimProcessMakerView.class)
public class OMPClaimProcessMakerPresenter extends AbstractMVPPresenter<OMPClaimProcessMakerView>{
		
	public static final String RESET_SEARCH_VIEW = "OMP Maker Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = "OMP Maker Search Submit";
	public static final String DISABLE_SEARCH_FIELDS = "OMP Maker Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "OMP Maker Edit Intimation Screen";
	
@Override
public void viewEntered() {
	System.out.println("view Entered called");
//	System.out.println(view.getCurrentParameters());
	}
@SuppressWarnings({ "deprecation" })
public void handleSearch(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
	
	OMPClaimProcessMakerFormDto searchFormDTO = (OMPClaimProcessMakerFormDto) parameters.getPrimaryParameter();
	
	String userName=(String)parameters.getSecondaryParameter(0, String.class);
	String passWord=(String)parameters.getSecondaryParameter(1, String.class);
	
//	view.list(searchService.search(searchFormDTO,userName,passWord));
}


}
