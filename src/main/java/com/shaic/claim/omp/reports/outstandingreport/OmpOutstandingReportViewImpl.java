package com.shaic.claim.omp.reports.outstandingreport;

import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.navigator.View;

@UIScoped
@CDIView(value = MenuItemBean.SEARCH_OMP_OUTSTANDING_REPORT)
public class OmpOutstandingReportViewImpl extends AbstractMVPView  implements OmpOutstandingReportView ,View{
	
	@Inject
    private OmpOutstandingReport searchReportComponent;
	
	private BeanItemContainer<SelectValue> classificationContainer;
//	private BeanItemContainer<SelectValue> subClassificationContainer;
	WeakHashMap<Long,BeanItemContainer<SelectValue>> subClassificationMap;

	public void init(BeanItemContainer<SelectValue> classificationContainer,
			 WeakHashMap<Long,BeanItemContainer<SelectValue>> subClassificationMap){
		
		setSizeFull();
		setVisible(true);
		this.classificationContainer = classificationContainer;
		this.subClassificationMap = subClassificationMap;
		searchReportComponent.init(classificationContainer, subClassificationMap);
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
	public void showOmpOutstandingReport(List<OmpStatusReportDto> empDetailsList) {
				
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
