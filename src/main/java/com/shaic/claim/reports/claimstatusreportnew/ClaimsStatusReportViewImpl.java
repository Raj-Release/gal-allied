package com.shaic.claim.reports.claimstatusreportnew;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ClaimsStatusReportViewImpl extends AbstractMVPView implements ClaimsStatusReportView{
	
	
	@Inject
    private Instance<SearchClaimsStatusReport> searchclaimsDailyReportUI; 

	@Override
	public void resetView() {
		searchclaimsDailyReportUI.get().resetAllValues();
	}
	
	@PostConstruct
	public void initView() {
		showSearchClaimsStatusReport();
    	resetView();
    }
    
	public void searchClaimsDailyReport(List<ClaimsStatusReportDto> claimList) {
		
		searchclaimsDailyReportUI.get().showTable(claimList);
	}
	
	@Override
	public void showSearchClaimsStatusReport() {
		addStyleName("view");
        setSizeFull();
        searchclaimsDailyReportUI.get().init();
        setCompositionRoot(searchclaimsDailyReportUI.get());
        setVisible(true);
	}

	@Override
	public void setupDroDownValues(
			BeanItemContainer<SelectValue> cpuCodeContainer,
			BeanItemContainer<SelectValue> clmStatusContainer) {
		searchclaimsDailyReportUI.get().setDropDownValues(cpuCodeContainer,clmStatusContainer);
	}

	@Override
	public void searchClaimsStatusReport(List<ClaimsStatusReportDto> claimList) {
		
		searchclaimsDailyReportUI.get().showTable(claimList);
		
	}
	
	

}
