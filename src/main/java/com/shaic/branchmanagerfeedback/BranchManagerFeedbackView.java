package com.shaic.branchmanagerfeedback;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface BranchManagerFeedbackView extends Searchable{
	public void initView();
	public void setTableList(Page<BranchManagerFeedbackTableDTO> resultList);
	public void showFeedbaclReply();
	public void showPolicyPopup(BranchManagerFeedbackTableDTO tableDTO);
	public void showProposalPopup(BranchManagerFeedbackTableDTO tableDTO);
	public void showClaimPopup(BranchManagerFeedbackTableDTO tableDTO);
	public void showResult();
	public void loadBranchDetails(BeanItemContainer<SelectValue> branchValueContainer);
}
