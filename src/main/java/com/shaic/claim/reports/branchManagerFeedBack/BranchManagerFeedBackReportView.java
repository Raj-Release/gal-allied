package com.shaic.claim.reports.branchManagerFeedBack;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;
/**
 * Part of CR R1238
 * @author Lakshminarayana
 *
 */
public interface BranchManagerFeedBackReportView extends GMVPView {
	
	public void loadBranchDropDown(BeanItemContainer<SelectValue> branchContainer);
	public void showBmFeedbackReport(List<BranchManagerFeedBackReportDto> invAssignStatusList);
	public void showBranchDetailsResultTable(List<BranchManagerFeedBackReportDto> invAssignStatusList);
	public void resetSearchView();
}
