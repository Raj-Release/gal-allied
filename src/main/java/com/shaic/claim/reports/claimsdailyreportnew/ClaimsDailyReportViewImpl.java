package com.shaic.claim.reports.claimsdailyreportnew;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ClaimsDailyReportViewImpl extends AbstractMVPView implements ClaimsDailyReportView{
	
	
	@Inject
    private Instance<SearchClaimsDailyReport> searchclaimsDailyReportUI; 

	@Override
	public void resetView() {
		searchclaimsDailyReportUI.get().resetAllValues();
	}
	
	@PostConstruct
	public void initView() {
		showSearchClaimsDailyReport();
    	resetView();
    }
    
	public void searchClaimsDailyReport(List<ClaimsDailyReportDto> claimList) {
		
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
