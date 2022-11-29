package com.shaic.claim.reports.plannedAdmissionReport;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

public class PlannedAdmissionReportViewImpl extends AbstractMVPView implements PlannedAdmissionReportView{
	
	
	@Inject
    private Instance<PlannedAdmissionReport> plannedAdmissionReportUI; 

	@Override
	public void resetView() {
		plannedAdmissionReportUI.get().resetAllValues();
	}
	
	@PostConstruct
	public void initView() {
		showSearchPlannedAdmissionReport();
    	resetView();
    }
    
	public void plannedAdmissionReportDetailsView(List<PlannedAdmissionReportDto> plannedDetailsList) {
		
		plannedAdmissionReportUI.get().showTable(plannedDetailsList);
	}
	
	@Override
	public void showSearchPlannedAdmissionReport() {
		addStyleName("view");
        setSizeFull();
        plannedAdmissionReportUI.get().init();
        setCompositionRoot(plannedAdmissionReportUI.get());
        setVisible(true);
	}	

}
