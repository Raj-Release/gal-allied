package com.shaic.claim.reports.gmcdailyreport;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public class GmcDailyReportViewImpl extends AbstractMVPView implements GmcDailyReportView{
	
	
	@Inject
    private Instance<SearchGmcDailyReport> searchclaimsDailyReportUI; 

	@Override
	public void resetView() {
		searchclaimsDailyReportUI.get().resetAllValues();
	}
	
	@PostConstruct
	public void initView() {
		showSearchClaimsDailyReport();
    	resetView();
    }
    
	public void searchClaimsDailyReport(List<GmcDailyReportDto> claimList) {
		
		searchclaimsDailyReportUI.get().showTable(claimList);
	}
	
	@Override
	public void showSearchClaimsDailyReport() {
		addStyleName("view");
        setSizeFull();
        searchclaimsDailyReportUI.get().init();
        setCompositionRoot(searchclaimsDailyReportUI.get());
        setVisible(true);
	}

	@Override
	public void setupDroDownValues(
			BeanItemContainer<SelectValue> cpuContainer,
			BeanItemContainer<SelectValue> clmTypeContainer) {		
		 searchclaimsDailyReportUI.get().setDropDownValues(cpuContainer, clmTypeContainer);
	}
	

}
