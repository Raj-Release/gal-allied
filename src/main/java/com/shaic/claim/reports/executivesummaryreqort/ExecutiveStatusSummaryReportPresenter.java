package com.shaic.claim.reports.executivesummaryreqort;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.ClaimService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;
@ViewInterface(ExecutiveStatusSummaryReportView.class)
public class ExecutiveStatusSummaryReportPresenter extends
AbstractMVPPresenter<ExecutiveStatusSummaryReportView> {

	public static final String SEARCH_EXECUTIVE_STATUS_SUMMARY = "Search Executive Status Summary";
	public static final String RESET_EXECUTIVE_STATUS_SUMMARY = "Reset Executive Status Summary";
	public static final String GET_CPU_FILTER_EMP_SUMMARY = "Get Cpu Filter Emp Summary";
	public static final String GET_EMP_TYPE_FILTER_SUMMARY = "Get Emp Type Filter Summary";
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationservice;
	
	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private MasterService masterService;
		
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showExecutiveStatusSummarySearch(@Observes @CDIEvent(SEARCH_EXECUTIVE_STATUS_SUMMARY) final ParameterDTO parameters)
	    {
		 
		 ExecutiveStatusSummarySearchDto searchDto = (ExecutiveStatusSummarySearchDto)parameters.getPrimaryParameter();
//		   Map<String,Object> searchFilter = (Map<String,Object>) 
		 	String userName=(String)parameters.getSecondaryParameter(0, String.class);
		   
		   List<ExecutiveStatusSummaryReportDto> claimReportDtoList = (List<ExecutiveStatusSummaryReportDto>) reimbursementService.getExecutiveStatusSummary(searchDto,userName);
		    
	    	
		   view.showEmpwiseResultReport(claimReportDtoList);
	    }
	 
	 protected void showCPUBasedEmpForSearch(@Observes @CDIEvent(GET_CPU_FILTER_EMP_SUMMARY) final ParameterDTO parameters)
	 {
		 String cpuCodeValue = parameters.getPrimaryParameter().toString();
		 
		 if(cpuCodeValue != null){
			 String[] cpuCodeArray = cpuCodeValue.split("-");
			 if(cpuCodeArray.length >0){
				 String cpuCode = cpuCodeArray[0]; 
				 
				 BeanItemContainer<SelectValue> empListContainer = masterService.getEmpListByCPUCode(cpuCode);
				 
				 view.populateFilteredEmpList(empListContainer);
			 }
		 }		 
	 
	 }
	 
	 protected void showTypeBasedEmpForSearch(@Observes @CDIEvent(GET_EMP_TYPE_FILTER_SUMMARY) final ParameterDTO parameters)
	 {
		 String typeValue = parameters.getPrimaryParameter().toString();
		 
		 if(typeValue != null){
			 BeanItemContainer<SelectValue> empListContainer = masterService.getEmpContainerByType(typeValue);
			 view.populateFilteredEmpList(empListContainer);
		 }
	 }
	
	 protected void resetExecutiveStatusSummary(@Observes @CDIEvent(RESET_EXECUTIVE_STATUS_SUMMARY) final ParameterDTO parameters)
	    {	
		   view.resetSearchView();
	    }

}
