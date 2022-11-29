package com.shaic.claim.reports;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

public class SearchClaimPolicyReportViewImpl extends AbstractMVPView implements SearchClaimPolicyReportView{
	
	@Inject
    private Instance<SearchClaimPolicyReport> searchClaimPolicyReportComponent;
	
	@Inject
    private Instance<SearchClaimPolicyReport> searchclaimPolicyReportUI; 

	@Override
	public void resetView() {
		searchclaimPolicyReportUI.get().resetAlltheValues();
	}
	
	@PostConstruct
	public void initView() {
    	showSearchClaimPolicyReport();
    	resetView();
    }
    
	public void searchClaiPolicymwise(List<PolicywiseClaimReportDto> claimList) {
		
		searchclaimPolicyReportUI.get().showTable(claimList);
	}
	
	@Override
	public void showSearchClaimPolicyReport() {
		addStyleName("view");
        setSizeFull();
        searchclaimPolicyReportUI.get().init();
        setCompositionRoot(searchclaimPolicyReportUI.get());
        setVisible(true);
	}
	

}
