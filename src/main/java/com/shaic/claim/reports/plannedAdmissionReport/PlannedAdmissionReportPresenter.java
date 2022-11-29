package com.shaic.claim.reports.plannedAdmissionReport;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.IntimationService;
import com.shaic.domain.reimbursement.ReimbursementService;
@ViewInterface(PlannedAdmissionReportView.class)
public class PlannedAdmissionReportPresenter extends
AbstractMVPPresenter<PlannedAdmissionReportView> {

	public static final String SEARCH_PLANNED_ADMISSION_REPORT = "Search Planned Admission Report";
	
	@EJB
	private IntimationService intimationservice;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showClaimPolicySearch(@Observes @CDIEvent(SEARCH_PLANNED_ADMISSION_REPORT) final ParameterDTO parameters)
	    {
		   Map<String,Object> searchFilter = (Map<String,Object>) parameters.getPrimaryParameter();
		   
		   List<PlannedAdmissionReportDto> plannedIntimationListDto = (List<PlannedAdmissionReportDto>) intimationservice.getPlannedIntimationDetails(searchFilter);
		    
		   view.plannedAdmissionReportDetailsView(plannedIntimationListDto);
	    }
	

}
