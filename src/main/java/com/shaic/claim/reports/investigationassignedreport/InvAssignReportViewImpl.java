package com.shaic.claim.reports.investigationassignedreport;

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
/**
 * Part of CR R0768
 * @author Lakshminarayana
 *
 */
@UIScoped
@CDIView(value = MenuItemBean.INV_ASSIGN_STATUS_REPORT)
public class InvAssignReportViewImpl extends AbstractMVPView  implements InvAssignReportView,View{
	
	@Inject
    private InvAssignReport searchReportComponent;
	
	private BeanItemContainer<SelectValue> clmTypeSelectValueContainer;
	private BeanItemContainer<SelectValue> invStatusContainer;
	private BeanItemContainer<SelectValue> cpuSelectValueContainer;

	public void init(BeanItemContainer<SelectValue> cpuContainer,
			BeanItemContainer<SelectValue> invStatusContainer,
			BeanItemContainer<SelectValue> clmTypeContainer){
		
		setSizeFull();
		setVisible(true);
		this.cpuSelectValueContainer = cpuContainer;
		this.clmTypeSelectValueContainer = clmTypeContainer;
		this.invStatusContainer = invStatusContainer;
		searchReportComponent.init(cpuContainer, invStatusContainer, clmTypeContainer);
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
	public void showInvAssignStatusReport(List<InvAssignStatusReportDto> invAssignDetailsList,Long statusId) {
				
		searchReportComponent.showResultTable(invAssignDetailsList,statusId);
	}

}
