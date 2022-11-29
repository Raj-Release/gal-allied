package com.shaic.claim.reports.intimationAlternateCPUReport;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

public class IntimationAlternateCPUwiseReportViewImpl extends AbstractMVPView implements IntimationAlternateCPUwiseReportView{
	
	
	@Inject
    private Instance<IntimationAlternateCPUwiseReport> intimatedRiskDetailsReportUI; 

	@Override
	public void resetView() {
		intimatedRiskDetailsReportUI.get().resetAllValues();
	}
	
	@PostConstruct
	public void initView() {
		showIntimationAlternateCPUReport();
    	resetView();
    }
    
	public void intimationAlternateCPUReportDetailsView(List<IntimationAlternateCPUwiseReportDto> intimationAlternateCPUDtoList) {
		
		intimatedRiskDetailsReportUI.get().showTable(intimationAlternateCPUDtoList);
	}
	
	@Override
	public void showIntimationAlternateCPUReport() {
		addStyleName("view");
        setSizeFull();
        intimatedRiskDetailsReportUI.get().init();
        setCompositionRoot(intimatedRiskDetailsReportUI.get());
        setVisible(true);
	}	

}
