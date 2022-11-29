package com.shaic.claim.reports.cpuwiseperformancedetail;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.UsertoCPUMappingService;
@ViewInterface(CpuWisePerformanceReportView.class)

public class CpuWisePerformanceReportPresenter extends AbstractMVPPresenter<CpuWisePerformanceReportView >{

	private static final long serialVersionUID = 1L;
	public static final String CPU_WISE_REPORT = "doSearchForCpuReport";
	@EJB
	private CpuWisePerformanceReportService searchService;

	@EJB
	private UsertoCPUMappingService usertoCPUMapService;
	
	public static final String GENERATE_REPORT = "generate_cpu_report";
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(CPU_WISE_REPORT) final ParameterDTO parameters) {
		
		CpuWisePerformanceReportFormDTO searchFormDTO = (CpuWisePerformanceReportFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord,usertoCPUMapService));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void generateReport(@Observes @CDIEvent(GENERATE_REPORT) final ParameterDTO parameters) {
		view.generateReport();
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
}
