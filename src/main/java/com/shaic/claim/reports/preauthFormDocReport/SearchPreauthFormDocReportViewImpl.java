package com.shaic.claim.reports.preauthFormDocReport;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchPreauthFormDocReportViewImpl extends AbstractMVPView implements SearchPreauthFormDocReportView{
	
	
	@Inject
    private Instance<SearchPreauthFormDocReport> searchclaimsDailyReportUI; 

	@Override
	public void resetView() {
		searchclaimsDailyReportUI.get().resetAllValues();
	}
	
	@PostConstruct
	public void initView() {
		showSearchPreauthFormDocReport();
    	resetView();
    }
    
	public void searchPreauthFormDocReport(List<NewIntimationDto> claimList) {
		
		searchclaimsDailyReportUI.get().showTable(claimList);
	}
	
	@Override
	public void showSearchPreauthFormDocReport() {
		addStyleName("view");
        setSizeFull();
//        searchclaimsDailyReportUI.get().init();
        setCompositionRoot(searchclaimsDailyReportUI.get());
        setVisible(true);
	}

	@Override
	public void setCPUContainerToForm(
			BeanItemContainer<SelectValue> cpuContainer) {
		searchclaimsDailyReportUI.get().setCPUContainer(cpuContainer);
		
	}
}
