package com.shaic.claim.reports.branchManagerFeedbackReportingPattern;

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
@CDIView(value = MenuItemBean.BRANCH_MANAGER_FEEDBACK_REPORTING_PATTERN)
public class BranchManagerFeedBackReportingPatternViewImpl extends AbstractMVPView  implements BranchManagerFeedBackReportingPatternView, View{

	
	@Inject
    private BranchManagerFeedBackReportingPattern searchReportComponent;
	
	public void init(BeanItemContainer<SelectValue> feedbackContainer, BeanItemContainer<SelectValue> zoneContainer, BeanItemContainer<SelectValue> periodContainer){
		
		setSizeFull();
		setVisible(true);
		searchReportComponent.init(feedbackContainer,zoneContainer,periodContainer);
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
	public void showBmFeedbackReportingPattern(List<BranchManagerFeedBackReportingPatternDto> feedbackRptPatternList) {
				
		searchReportComponent.showResultTable(feedbackRptPatternList);
	}

	@Override
	public void loadBranchDropDown(BeanItemContainer<SelectValue> branchContainer){
		searchReportComponent.loadBranchDropDown(branchContainer);
	}
}
