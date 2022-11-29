package com.shaic.claim.reports.branchManagerFeedbackReportingPattern;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.claim.reports.branchManagerFeedBack.BranchManagerFeedBackReportDto;
import com.vaadin.v7.data.util.BeanItemContainer;
/**
 * Part of CR R1238
 * @author Lakshminarayana
 *
 */
public interface BranchManagerFeedBackReportingPatternView extends GMVPView {
	
	public void loadBranchDropDown(BeanItemContainer<SelectValue> branchContainer);
	public void showBmFeedbackReportingPattern(List<BranchManagerFeedBackReportingPatternDto> feedbackRptPatternList);
	public void resetSearchView();
}
