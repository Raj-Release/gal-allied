package com.shaic.claim.reports.autoallocationaancelreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(AutoAllocationCancelReportView.class)
@SuppressWarnings("serial")
public class AutoAllocationCancelPresenter extends AbstractMVPPresenter<AutoAllocationCancelReportView>{

	public static final String SEARCH_CANCEL_STATUS = "Search Cancel Status";
	
	public static final String GENERATE_REPORT = "generate_cancel_status_report";
	
	@EJB
	private CancelService cancelService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	 protected void showCancelStatusSearch(@Observes @CDIEvent(SEARCH_CANCEL_STATUS) final ParameterDTO parameters) {
		 CancelSearchDTO searchDto = (CancelSearchDTO)parameters.getPrimaryParameter();
		 String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
			
		 view.list(cancelService.getCancelStatusDetails(searchDto,userName,passWord));
	 }
	 
	 @SuppressWarnings({ })
		public void generateReport(@Observes @CDIEvent(GENERATE_REPORT) final ParameterDTO parameters) {
			view.generateReport();
			
		}
	
}
