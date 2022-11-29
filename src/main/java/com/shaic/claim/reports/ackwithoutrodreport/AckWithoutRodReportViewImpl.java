package com.shaic.claim.reports.ackwithoutrodreport;

import java.util.List;

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
@CDIView(value = MenuItemBean.ACK_WITHOUT_ROD_REPORT)
public class AckWithoutRodReportViewImpl extends AbstractMVPView  implements AckWithoutRodReportView,View{
	
	@Inject
    private AckWithoutRodReport searchReportComponent;
	
	private BeanItemContainer<SelectValue> docRcvdFromContainer;
	private BeanItemContainer<SelectValue> cpuSelectContainer;

	public void init(BeanItemContainer<SelectValue> cpuContainer,
			BeanItemContainer<SelectValue> docFrmContainer){
		
		setSizeFull();
		setVisible(true);
		this.cpuSelectContainer = cpuContainer;
		this.docRcvdFromContainer = docFrmContainer;
		searchReportComponent.init(cpuContainer, docFrmContainer);
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
	public void showAckWithoutRodReport(List<AckWithoutRodTableDto> ackWithoutRodList) {
				
		searchReportComponent.showResultTable(ackWithoutRodList);
	}	
	
}
