package com.shaic.claim.reports.finapprovednotsettledreport;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.navigator.View;
import com.vaadin.v7.data.util.BeanItemContainer;
/**
 * Part of CR R1201
 * @author Lakshminarayana
 *
 */
@UIScoped
@CDIView(value = MenuItemBean.FA_APPROVED_SETTLEMENT_PENDING_REPORT)

public class FinApprovedPaymentPendingReportViewImpl extends AbstractMVPView  implements FinApprovedPaymentPendingReportView,View{
	
	@Inject
    private FinApprovedPaymentPendingReport searchReportComponent;
	
	private BeanItemContainer<SelectValue> cpuSelectValueContainer;

	public void init(BeanItemContainer<SelectValue> cpuContainer){
		
		setSizeFull();
		setVisible(true);
		this.cpuSelectValueContainer = cpuContainer;
		searchReportComponent.init(cpuContainer);
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
	public void showFinApprovedSettlementPendingReport(List<FinApprovedPaymentPendingReportDto> invAssignDetailsList) {
				
		searchReportComponent.showResultTable(invAssignDetailsList);
	}

}
