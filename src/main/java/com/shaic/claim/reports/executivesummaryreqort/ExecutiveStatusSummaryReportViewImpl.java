package com.shaic.claim.reports.executivesummaryreqort;

import java.util.List;

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
@CDIView(value = MenuItemBean.EXECUTIVE_STATUS_SUMMARY_REPORT)
public class ExecutiveStatusSummaryReportViewImpl extends AbstractMVPView  implements ExecutiveStatusSummaryReportView,View{
	
	@Inject
    private Instance<ExecutiveStatusSummaryReport> searchClaimEmpReportComponent;
	
	private BeanItemContainer<SelectValue> empTypeSelectValueContainer;
	private BeanItemContainer<SelectValue> empNameSelectContainer;
	private BeanItemContainer<SelectValue> empCPUSelectValueContainer;

	@PostConstruct
	public void init(){
		
		setSizeFull();
		setVisible(true);
		searchClaimEmpReportComponent.get().init();
		setCompositionRoot(searchClaimEmpReportComponent.get());
		resetView();
	}	
	
	@Override
	public void resetView() {

		searchClaimEmpReportComponent.get().resetSearchForm();
	}
	
	@Override
	public void resetSearchView() {
		
		resetView();
		if(searchClaimEmpReportComponent.get().getSearchResultTable() != null){
		searchClaimEmpReportComponent.get().getSearchResultTable().removeRow();
		}
	}
		

	@Override
	public void showEmpwiseResultReport(List<ExecutiveStatusSummaryReportDto> empDetailsList) {
				
		searchClaimEmpReportComponent.get().showResultTable(empDetailsList);
	}

	@Override
	public void setupDroDownValues(
			BeanItemContainer<SelectValue> empCPUContainer,
			BeanItemContainer<SelectValue> empTypeContainer,
			BeanItemContainer<SelectValue> empContainer) {
		
//		BeanItemContainer<SelectValue> empTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
//		
//		SelectValue calcentEmpSelect = new SelectValue(null, "Call Center Employee");
//		SelectValue clmEmpSelect = new SelectValue(null, "Claims Employee");
//		empTypeContainer.addBean(calcentEmpSelect);
//		empTypeContainer.addBean(clmEmpSelect);
		
		this.empCPUSelectValueContainer = empCPUContainer;
		this.empTypeSelectValueContainer = empTypeContainer;
		this.empNameSelectContainer = empContainer;
		
		searchClaimEmpReportComponent.get().setDropDownValues(empCPUContainer,empTypeContainer,empContainer);
	}
	
	@Override
	public void populateFilteredEmpList(
			BeanItemContainer<SelectValue> empListContainer) {
		
		searchClaimEmpReportComponent.get().setCPUBasedEmpList(empListContainer);
	}
	
}
