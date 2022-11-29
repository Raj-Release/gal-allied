package com.shaic.claim.reports.branchManagerFeedBack;

import java.util.ArrayList;
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
 * Part of CR R1238
 * @author Lakshminarayana
 *
 */
//GALAXYMAIN-13427
@UIScoped
@CDIView(value = MenuItemBean.BRANCH_MANAGER_FEEDBACK_REPORT)
public class BranchManagerFeedBackReportViewImpl extends AbstractMVPView  implements BranchManagerFeedBackReportView, View{

	@Inject
    private BranchManagerFeedBackReport searchReportComponent;
	
	public void init(BeanItemContainer<SelectValue> feedbackContainer, BeanItemContainer<SelectValue> zoneContainer, BeanItemContainer<SelectValue> periodContainer/*, BeanItemContainer<SelectValue> branchOfficeContainer*/){
		
		setSizeFull();
		setVisible(true);
		searchReportComponent.init(feedbackContainer, zoneContainer, periodContainer/*, branchOfficeContainer*/);
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
			searchReportComponent.getSearchResultTable().setTableList(new ArrayList<BranchManagerFeedBackReportDto>());
		}
	}
		

	@Override
	public void showBmFeedbackReport(List<BranchManagerFeedBackReportDto> branchManagerFeedbackResultList) {
				
		searchReportComponent.showResultTable(branchManagerFeedbackResultList);
	}

	@Override
	public void showBranchDetailsResultTable(List<BranchManagerFeedBackReportDto> invAssignStatusList){
		
		searchReportComponent.showBranchDetailsResultTable(invAssignStatusList);
	}
	
	@Override
	public void loadBranchDropDown(BeanItemContainer<SelectValue> branchContainer){
		searchReportComponent.loadBranchDropDown(branchContainer);
	}
}
