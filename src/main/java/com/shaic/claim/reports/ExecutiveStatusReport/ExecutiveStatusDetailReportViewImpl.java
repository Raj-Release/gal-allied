package com.shaic.claim.reports.ExecutiveStatusReport;

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
@CDIView(value = MenuItemBean.EXECUTIVE_STATUS_REPORT)
public class ExecutiveStatusDetailReportViewImpl extends AbstractMVPView  implements ExecutiveStatusDetailReportView, View {
	
	@Inject
    private Instance<ExecutiveStatusDetailReport> searchClaimEmpReportComponent;
	
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
	public void resetEmpResultView() {
		resetView();
		if(searchClaimEmpReportComponent.get().getSearchResultTable() != null){
		searchClaimEmpReportComponent.get().getSearchResultTable().removeRow();
		}
	}

	@Override
	public void showSearchEmpwiseReport(BeanItemContainer<SelectValue> empTypeContainer,BeanItemContainer<SelectValue> empCPUContainer,BeanItemContainer<SelectValue> empContainer) {
		
//		BeanItemContainer<SelectValue> empTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
//		SelectValue calcentEmpSelect = new SelectValue(null, "Call Center Employee");
//		SelectValue clmEmpSelect = new SelectValue(null, "Claims Employee");
//		empTypeContainer.addBean(calcentEmpSelect);
//		empTypeContainer.addBean(clmEmpSelect);

		this.empTypeSelectValueContainer = empTypeContainer;
		this.empNameSelectContainer = empContainer;
		this.empCPUSelectValueContainer = empCPUContainer;
		searchClaimEmpReportComponent.get().setDropDownValues(empCPUSelectValueContainer,empTypeSelectValueContainer,empNameSelectContainer);
	}

	@Override
	public void showEmpwiseResultReport(List<ExecutiveStatusDetailReportDto> empDetailsList) {
				
		searchClaimEmpReportComponent.get().showResultTable(empDetailsList);
	}

	@Override
	public void populateFilteredEmpList(
			BeanItemContainer<SelectValue> empListContainer) {
		
		searchClaimEmpReportComponent.get().setCPUBasedEmpList(empListContainer);
	}

}
