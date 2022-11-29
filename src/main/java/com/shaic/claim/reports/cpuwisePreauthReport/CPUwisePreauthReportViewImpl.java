package com.shaic.claim.reports.cpuwisePreauthReport;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.navigator.View;
import com.vaadin.v7.data.util.BeanItemContainer;

@UIScoped
@CDIView(value = MenuItemBean.SEARCH_PREAUTH_CPUWISE_REPORT)

public class CPUwisePreauthReportViewImpl extends AbstractMVPView implements CPUwisePreauthReportView, View{
	
	@Inject
    private Instance<CPUwisePreauthReport> searchClaimPolicyReportComponent;

	@Override
	public void resetView() {
		searchClaimPolicyReportComponent.get().resetAlltheValues();
	}

	@PostConstruct
	public void initView(){
		showSearchPreauthCPUWise();
		
		resetView();
	}
	
	public void init(BeanItemContainer<SelectValue> cpuContainer){
		searchClaimPolicyReportComponent.get().setCPUDropDownValue(cpuContainer);
	}
	
	@Override
	public void showSearchPreauthCPUWise() {

		addStyleName("view");
        setSizeFull();
        searchClaimPolicyReportComponent.get().init();
        setCompositionRoot(searchClaimPolicyReportComponent.get());
	}

	@Override
	public void showCPUWisePreauthDetails(
			CPUWisePreauthResultDto cpuWisePreauthResultDto) {
		
		searchClaimPolicyReportComponent.get().showTableResult(cpuWisePreauthResultDto);
		
	}

}
