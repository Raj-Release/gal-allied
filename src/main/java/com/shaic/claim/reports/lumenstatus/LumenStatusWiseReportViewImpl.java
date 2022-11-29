package com.shaic.claim.reports.lumenstatus;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.navigator.View;

@UIScoped
@CDIView(value = MenuItemBean.LUMEN_STATUS_WISE_REPORT)
public class LumenStatusWiseReportViewImpl extends AbstractMVPView  implements LumenStatusWiseReportView,View{
	
	@Inject
    private LumenStatusWiseReport searchReportComponent;
	
	private BeanItemContainer<SelectValue> clmTypeSelectValueContainer;
	private BeanItemContainer<SelectValue> lumenStatusContainer;
	private BeanItemContainer<SelectValue> cpuSelectValueContainer;

	public void init(BeanItemContainer<SelectValue> cpuContainer,
			BeanItemContainer<SelectValue> lumenStatusContainer,
			BeanItemContainer<SelectValue> clmTypeContainer){
		
		setSizeFull();
		setVisible(true);
		this.cpuSelectValueContainer = cpuContainer;
		this.clmTypeSelectValueContainer = clmTypeContainer;
		this.lumenStatusContainer = lumenStatusContainer;
		searchReportComponent.init(cpuContainer, lumenStatusContainer, clmTypeContainer);
		setSizeFull();
		setCompositionRoot(searchReportComponent);
		resetView();
	}	
	
	@Override
	public void resetView() {

		searchReportComponent.resetSearchForm();
	}
	
	@Override
	public void resetSearchView() {
		
		resetView();
		if(searchReportComponent.getSearchResultTable() != null){
			searchReportComponent.getSearchResultTable().removeRow();
		}
	}
		

	@Override
	public void showLumenStatusWiseReport(List<SearchLumenStatusWiseDto> empDetailsList) {
				
		searchReportComponent.showResultTable(empDetailsList);
	}

//	@Override
//	public void setupDroDownValues(
//			BeanItemContainer<SelectValue> cpuContainer,
//			BeanItemContainer<SelectValue> lumenStatusContainer,
//			BeanItemContainer<SelectValue> clmTypeContainer) {
//		
//		this.cpuSelectValueContainer = cpuContainer;
//		this.clmTypeSelectValueContainer = clmTypeContainer;
//		this.lumenStatusContainer = lumenStatusContainer;
//		
//		searchReportComponent.setDropDownValues(cpuContainer,clmTypeContainer,lumenStatusContainer);
//		searchReportComponent.getContent();
//	}	
	
}
