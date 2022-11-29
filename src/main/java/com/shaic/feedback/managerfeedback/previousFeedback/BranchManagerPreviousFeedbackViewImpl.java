package com.shaic.feedback.managerfeedback.previousFeedback;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.cmn.login.ImsUser;
import com.shaic.feedback.managerfeedback.FeedbackStatsDto;
import com.shaic.paclaim.cashless.preauth.search.PASearchPreAuthList;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;

public class BranchManagerPreviousFeedbackViewImpl extends AbstractMVPView implements BranchManagerPreviousFeedbackView {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private BranchManagerPreviousFeedbackUI branchManagerPreviousFeedbackUI;
	
	@Inject
	private BranchManagerPreviousFeedbackTable previousFeedbackTable;
	
	private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();
		branchManagerPreviousFeedbackUI.init();
		previousFeedbackTable.init("", false, false);
		previousFeedbackTable.setVisibleColumnsForPolicy();
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(branchManagerPreviousFeedbackUI);
		mainPanel.setSecondComponent(previousFeedbackTable);
		mainPanel.setSplitPosition(22);
		mainPanel.setCaption("Previous Feedback");
		setHeight("570px");
		previousFeedbackTable.addSearchListener(this);
		branchManagerPreviousFeedbackUI.addSearchListener(this);
		setCompositionRoot(mainPanel);
		
	}
	

	@Override
	public void resetView() {
		branchManagerPreviousFeedbackUI.refresh();
	}
	
	@Override
	public void doSearch() {
		
		String err = branchManagerPreviousFeedbackUI.validate();
		if(err == null || err.isEmpty())
		{
		BranchManagerPreviousFeedbackSearchDTO searchDTO = branchManagerPreviousFeedbackUI.getSearchDTO();
		if(null != searchDTO && null != searchDTO.getFeedbackValue() && null != searchDTO.getFeedbackValue().getValue() &&
				SHAConstants.FEEDBACK_POLICY.equalsIgnoreCase(searchDTO.getFeedbackValue().getValue())){
			previousFeedbackTable.init("", false, false);
			previousFeedbackTable.setVisibleColumnsForPolicy();
			mainPanel.setSecondComponent(previousFeedbackTable);
		}
		else if(null != searchDTO && null != searchDTO.getFeedbackValue() && null != searchDTO.getFeedbackValue().getValue() &&
				SHAConstants.FEEDBACK_PROPOSAL.equalsIgnoreCase(searchDTO.getFeedbackValue().getValue())){
			previousFeedbackTable.init("", false, false);
			previousFeedbackTable.setVisibleColumnsForProposal();
			mainPanel.setSecondComponent(previousFeedbackTable);
		}
		else if(null != searchDTO && null != searchDTO.getFeedbackValue() && null != searchDTO.getFeedbackValue().getValue() &&
				SHAConstants.FEEDBACK_CLAIM.equalsIgnoreCase(searchDTO.getFeedbackValue().getValue())){
					previousFeedbackTable.init("", false, false);
					previousFeedbackTable.setVisibleColumnsForClaim();
					mainPanel.setSecondComponent(previousFeedbackTable);
		}
		
		ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
		searchDTO.setUserName(user.getUserName());
			fireViewEvent(BranchManagerPreviousFeedbackPresenter.BRANCH_TYPE_SEARCH, searchDTO, user);
		}
		else
		{
			showErrorMessage(err);
		}
		
		
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub

		
		previousFeedbackTable.getPageable().setPageNumber(1);
		previousFeedbackTable.resetTable();
		
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof BranchManagerPreviousFeedbackTable)
			{
				((BranchManagerPreviousFeedbackTable) comp).removeRow();
			}
		}
	
		
	}
	
/*	public void setDropDownValues(BeanItemContainer<SelectValue> feedbackValue,
			BeanItemContainer<SelectValue> zoneValue, BeanItemContainer<SelectValue> branchValue,
			BeanItemContainer<SelectValue> feedbackStatusValue, BeanItemContainer<SelectValue> feedbackTypeValue) {
		branchManagerPreviousFeedbackUI.setDropDownValues(feedbackValue,zoneValue,branchValue,feedbackStatusValue,feedbackTypeValue);
	}*/


	@Override
	public void setTableList(
			Page<BranchManagerPreviousFeedbackTableDTO> resultList) {
		// TODO Auto-generated method stub
		previousFeedbackTable.setTableList(resultList);
	}


	@Override
	public void setDropDownValues(BeanItemContainer<SelectValue> feedBack,
			BeanItemContainer<SelectValue> feedbackContainer,
			BeanItemContainer<SelectValue> feedbackTypeValue) {
		// TODO Auto-generated method stub
		branchManagerPreviousFeedbackUI.setDropDownValues(feedBack,feedbackContainer,feedbackTypeValue);
	}

	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	@Override
	public void loadPreviousDropDownValues(){ 
		BranchManagerPreviousFeedbackSearchDTO previousFeedbackDTO = new BranchManagerPreviousFeedbackSearchDTO();
		fireViewEvent(BranchManagerPreviousFeedbackPresenter.LOAD_PREVIOUS_FEED_BACK_SEARCH_COMPONENTS_VALUE,previousFeedbackDTO);
	}
	
	public void buildReviewReplySuccessLayout(Window popup) {
		// TODO Auto-generated method stub
		branchManagerPreviousFeedbackUI.buildReviewReplySuccessLayout(popup);
	}

	@Override
	public void loadPreviousDropDownValuesForHomePage(FeedbackStatsDto fbStatusDTO, Long fbStatus){ 
		fireViewEvent(BranchManagerPreviousFeedbackPresenter.LOAD_PREVIOUS_FEED_BACK_SEARCH_COMPONENTS_VALUE_HOME_PAGE,fbStatusDTO,fbStatus);
	}


	@Override
	public void setDropDownValuesHomePage(
			BeanItemContainer<SelectValue> feedBack,
			BeanItemContainer<SelectValue> feedbackContainer,
			BeanItemContainer<SelectValue> feedbackTypeValue,
			FeedbackStatsDto fbStatusDTO, Long fbStatus) {
		// TODO Auto-generated method stub
		branchManagerPreviousFeedbackUI.setDropDownValuesHomePage(feedBack,feedbackContainer,feedbackTypeValue,fbStatusDTO,fbStatus);
		doSearch();
	}
	
	
}
