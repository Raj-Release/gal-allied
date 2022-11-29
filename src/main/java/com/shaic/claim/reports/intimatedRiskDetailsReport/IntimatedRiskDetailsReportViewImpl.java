package com.shaic.claim.reports.intimatedRiskDetailsReport;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

public class IntimatedRiskDetailsReportViewImpl extends AbstractMVPView implements IntimatedRiskDetailsReportView{
	
	
	@Inject
    private Instance<IntimatedRiskDetailsReport> intimatedRiskDetailsReportUI; 

	@Override
	public void resetView() {
		intimatedRiskDetailsReportUI.get().resetAllValues();
	}
	
	@PostConstruct
	public void initView() {
		showIntimatedRiskDetailsReport();
    	resetView();
    }
    
	public void intimatedRiskReportDetailsView(List<IntimatedRiskDetailsReportDto> intimatedRiskDetailsList) {
		
		intimatedRiskDetailsReportUI.get().showTable(intimatedRiskDetailsList);
	}
	
	@Override
	public void showIntimatedRiskDetailsReport() {
		addStyleName("view");
        setSizeFull();
        intimatedRiskDetailsReportUI.get().init();
        setCompositionRoot(intimatedRiskDetailsReportUI.get());
        setVisible(true);
	}	

}
