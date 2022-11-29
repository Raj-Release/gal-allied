package com.shaic.claim.omp.reports.statusreport;

import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.omp.reports.outstandingreport.OmpStatusReportDto;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.navigator.View;

@UIScoped
@CDIView(value = MenuItemBean.SEARCH_OMP_STATUS_WISE_REPORT)
public class OmpStatusWiseReportViewImpl extends AbstractMVPView  implements OmpStatusWiseReportView,View{
	
	@Inject
    private OmpStatusWiseReport searchReportComponent;
	
	private BeanItemContainer<SelectValue> classificationContainer;
//	private BeanItemContainer<SelectValue> subClassificationContainer;
	WeakHashMap<Long,BeanItemContainer<SelectValue>> subClassificationMap;
	private BeanItemContainer<SelectValue> statusContainer;
	private BeanItemContainer<SelectValue> lossContainer;
	private BeanItemContainer<SelectValue> yearContainer;

	public void init(BeanItemContainer<SelectValue> classificationContainer,
			WeakHashMap<Long,BeanItemContainer<SelectValue>> subClassificationMap,
			BeanItemContainer<SelectValue> statusContainer,
			BeanItemContainer<SelectValue> lossContainer,
			BeanItemContainer<SelectValue> yearContainer){
		
		setSizeFull();
		setVisible(true);
		this.classificationContainer = classificationContainer;
//		this.subClassificationContainer = subClassificationContainer;
		this.subClassificationMap = subClassificationMap;
		this.statusContainer = statusContainer;
		this.lossContainer = lossContainer;
		this.yearContainer = yearContainer;
		
		searchReportComponent.init(classificationContainer, subClassificationMap,
				statusContainer,lossContainer,yearContainer);
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
	public void showOmpStatusWiseReport(List<OmpStatusReportDto> ompDetailsList) {
				
		searchReportComponent.showResultTable(ompDetailsList);
	}

}
