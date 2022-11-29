package com.shaic.claim.OMPpaiddetailreport.search;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@SuppressWarnings("serial")
@ViewInterface(OMPPaidDetailReportView.class)
public class OMPPaidDetailReportPresenter extends AbstractMVPPresenter<OMPPaidDetailReportView>{
	
	public static final String RESET_SEARCH_VIEW = "OMP Paid Report Reset Search Fields"; 
	public static final String OMP_SUBMIT_SEARCH = "Omp Search Submit";
	public static final String DISABLE_SEARCH_FIELDS = "OMP Paid Report Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "OMP Paid Report Edit Intimation Screen";
	
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
//		System.out.println(view.getCurrentParameters());
		}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(OMP_SUBMIT_SEARCH) final ParameterDTO parameters) {
		OMPPaidDetailReportFormDto searchFormDTO = (OMPPaidDetailReportFormDto) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
//		view.list(searchService.search(searchFormDTO,userName,passWord));
	}

}
