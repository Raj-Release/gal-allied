package com.shaic.claim.OMPTpaservicechargesmaker.search;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@SuppressWarnings("serial")
@ViewInterface(OMPTpaServiceChargesMakerView.class)
public class OMPTpaServiceChargesMakerPresenter extends AbstractMVPPresenter<OMPTpaServiceChargesMakerView>{
	
	public static final String RESET_SEARCH_VIEW = "OMPTpa Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = "OMPTpa Search Submit";
	public static final String DISABLE_SEARCH_FIELDS = "OMPTpa Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "OMPTpa Edit Intimation Screen";
	
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
//		System.out.println(view.getCurrentParameters());
		}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		OMPTpaServiceChargesMakerFormDto searchFormDTO = (OMPTpaServiceChargesMakerFormDto) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
//		view.list(searchService.search(searchFormDTO,userName,passWord));
	}

}
