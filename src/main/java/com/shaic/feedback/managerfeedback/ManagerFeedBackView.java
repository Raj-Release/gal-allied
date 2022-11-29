package com.shaic.feedback.managerfeedback;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ManagerFeedBackView extends GMVPView {
	
	public void initView();
	void buildSuccessLayout();
	void setValuesForCompletedCase(Long dailycount,Long overallcount);
	
	public void initHomePage(BranchManagerFeedbackhomePageDto homePageDto);
	public void setHomePageDropDownValues(BeanItemContainer<SelectValue> branchNameContainer);
	public void showRevisedTableValues(BranchManagerFeedbackhomePageDto homePageDto);
	void buildReviewReplySuccessLayout();
	
}
