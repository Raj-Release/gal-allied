package com.shaic.feedback.managerfeedback.previousFeedback;

import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.Status;
import com.shaic.feedback.managerfeedback.FeedbackStatsDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Window;

public interface BranchManagerPreviousFeedbackView extends Searchable{
	public void initView();
	public void setTableList(Page<BranchManagerPreviousFeedbackTableDTO> resultList);	
	public void setDropDownValues(BeanItemContainer<SelectValue> feedBack,
			BeanItemContainer<SelectValue> feedbackContainer,
			BeanItemContainer<SelectValue> feedbackTypeValue);
	void loadPreviousDropDownValues();
	void buildReviewReplySuccessLayout(Window popup);
	void loadPreviousDropDownValuesForHomePage(FeedbackStatsDto fbStatusDTO, Long fbStatus);
	public void setDropDownValuesHomePage(
			BeanItemContainer<SelectValue> feedBack,
			BeanItemContainer<SelectValue> feedbackContainer,
			BeanItemContainer<SelectValue> feedbackTypeValue,
			FeedbackStatsDto fbStatusDTO, Long fbStatus);
	
}

