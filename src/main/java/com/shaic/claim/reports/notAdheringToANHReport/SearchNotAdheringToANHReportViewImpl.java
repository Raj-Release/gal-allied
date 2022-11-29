package com.shaic.claim.reports.notAdheringToANHReport;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchNotAdheringToANHReportViewImpl extends AbstractMVPView
		implements SearchNotAdheringToANHReportView {
	
	@Inject
    private Instance<SearchNotAdheringToANHReportUI> searchNotAdheringToANHReportUI;

	
	@PostConstruct
	public void initView() {
		showNotAdheringToANHcReport();
    	resetView();
    }
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		searchNotAdheringToANHReportUI.get().resetAllValues();

	}


	@Override
	public void setCPUContainerToForm(
			BeanItemContainer<SelectValue> cpuContainer) {
		// TODO Auto-generated method stub
		searchNotAdheringToANHReportUI.get().setCPUContainer(cpuContainer);

	}

	
	public void searchNotAdheringToANHReport(List<NewIntimationNotAdheringToANHDto> claimList) {
		// TODO Auto-generated method stub
		searchNotAdheringToANHReportUI.get().showTable(claimList);

	}
	@Override
	public void showNotAdheringToANHcReport() {
		// TODO Auto-generated method stub
		addStyleName("view");
        setSizeFull();
//        searchclaimsDailyReportUI.get().init();
        setCompositionRoot(searchNotAdheringToANHReportUI.get());
        setVisible(true);
		
	}

}
