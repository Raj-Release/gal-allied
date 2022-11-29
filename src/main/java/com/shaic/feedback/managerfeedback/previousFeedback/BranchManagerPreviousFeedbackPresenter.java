package com.shaic.feedback.managerfeedback.previousFeedback;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;	
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.branchmanagerfeedback.BranchManagerFeedbackTableDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.feedback.managerfeedback.FeedbackStatsDto;
import com.shaic.feedback.managerfeedback.ManagerFeedBackService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Window;
@ViewInterface(BranchManagerPreviousFeedbackView.class)
public class BranchManagerPreviousFeedbackPresenter extends AbstractMVPPresenter<BranchManagerPreviousFeedbackView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	BranchManagerPreviousFeedbackService branchManagerPreviousFeedbackService;
	
	public static final String BRANCH_TYPE_SEARCH = "search on branch basis previous";
	public static final String REPLY_FEEDBACK = "feedback reply previous";
	public static final String POLICY_REPLY_FEEDBACK = "policy feedback reply previous";
	public static final String PROPOSAL_REPLY_FEEDBACK = "proposal feedback reply previous";
	public static final String CLAIM_REPLY_FEEDBACK = "claim feedback reply previous";
	public static final String SUBMIT_CLICK ="submit previous";
	public static final String LOAD_PREVIOUS_FEED_BACK_SEARCH_COMPONENTS_VALUE ="load_previous_feedback_values previous";
	public static final String LOAD_PREVIOUS_FEED_BACK_VIEW_VALUE ="load_previous_feedback_view_values previous";
	public static final String INSERT_BRANCH_MANAGER_REVIEW_DETAILS = "Insert Previous Branch Manager Review Details";
	public static final String LOAD_PREVIOUS_FEED_BACK_SEARCH_COMPONENTS_VALUE_HOME_PAGE ="load_previous_feedback_values previous home page";
	
	@EJB
	MasterService masterService;
	

	@EJB
	private ManagerFeedBackService managerFeedbackService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	protected void branchBasedSearch(@Observes @CDIEvent(BRANCH_TYPE_SEARCH) final ParameterDTO parameters){
		BranchManagerPreviousFeedbackSearchDTO  bean = (BranchManagerPreviousFeedbackSearchDTO) parameters.getPrimaryParameter();
		List<BranchManagerPreviousFeedbackTableDTO> resultList = branchManagerPreviousFeedbackService.getFeedbackDetails(bean);
		Page<BranchManagerPreviousFeedbackTableDTO> page = new Page<BranchManagerPreviousFeedbackTableDTO>();
		page.setPageItems(resultList);
		page.setTotalRecords(resultList.size());
		view.setTableList(page);
		
	}
	
	protected void feedbackReply(@Observes @CDIEvent(REPLY_FEEDBACK) final ParameterDTO parameters) {
		//BranchManagerFeedbackTableDTO  bean = (BranchManagerFeedbackTableDTO) parameters.getPrimaryParameter();
		//List<BranchManagerFeedbackTableDTO> result = branchManagerFeedbackService.getTechTeamReply(bean);
		
		//view.showFeedbaclReply();
	}
	
	protected void submitValues(@Observes @CDIEvent(SUBMIT_CLICK) final ParameterDTO parameters) {
		BranchManagerPreviousFeedbackTableDTO  bean = (BranchManagerPreviousFeedbackTableDTO) parameters.getPrimaryParameter();
		branchManagerPreviousFeedbackService.submitValues(bean);
	}
	
	protected void loadPreviouFeedbackSearchValues(@Observes @CDIEvent(LOAD_PREVIOUS_FEED_BACK_SEARCH_COMPONENTS_VALUE) final ParameterDTO parameters) {
		BranchManagerPreviousFeedbackSearchDTO  bean = (BranchManagerPreviousFeedbackSearchDTO) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> feedBack = masterService.getMasterValueByCode(ReferenceTable.FEEDBACK);
		BeanItemContainer<SelectValue> feedbackContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		Status reportedStatus = masterService.getStatusByKey(ReferenceTable.FEEDBACK_REPORTED_KEY);
		Status respondedStatus = masterService.getStatusByKey(ReferenceTable.FEEDBACK_RESPONDED_KEY);
		SelectValue reportedStatusSelectValue = new SelectValue(reportedStatus.getKey(),reportedStatus.getProcessValue());
		SelectValue respondedStatusSelectValue = new SelectValue(respondedStatus.getKey(),respondedStatus.getProcessValue());
		SelectValue completedStatusSelectValue = new SelectValue(9999L,"Feedback Completed");
		SelectValue allStatusSelectValue = new SelectValue(0L,SHAConstants.ALL);
		feedbackContainer.addBean(respondedStatusSelectValue);
		feedbackContainer.addBean(reportedStatusSelectValue);
		feedbackContainer.addBean(completedStatusSelectValue);
		feedbackContainer.addBean(allStatusSelectValue);		
		BeanItemContainer<SelectValue> feedbackTypeValue = masterService.getMasterValueByCode(ReferenceTable.FEEDBACK_TYPE);
		view.setDropDownValues(feedBack,feedbackContainer,feedbackTypeValue);
	}
	protected void loadPreviouFeedbackViewValues(@Observes @CDIEvent(LOAD_PREVIOUS_FEED_BACK_VIEW_VALUE) final ParameterDTO parameters) {
		BranchManagerPreviousFeedbackTableDTO  bean = (BranchManagerPreviousFeedbackTableDTO) parameters.getPrimaryParameter();
		PreviousFeedbackView previousFeedbackView = (PreviousFeedbackView)parameters.getSecondaryParameter(0, PreviousFeedbackView.class);
		List<BranchManagerPreviousFeedbackTableDTO> tableList = branchManagerPreviousFeedbackService.getPolicyDetailsByParentKey(bean.getFeedbackKey());
		bean.setViewFeedbackTableList(tableList);
	}
	
	public void insertBranchManagerReviewDetails(@Observes @CDIEvent(INSERT_BRANCH_MANAGER_REVIEW_DETAILS) final ParameterDTO parameters)
	{
		BranchManagerPreviousFeedbackTableDTO bean=(BranchManagerPreviousFeedbackTableDTO)parameters.getPrimaryParameter();
		Window popup = (Window)parameters.getSecondaryParameter(0, Window.class);
		managerFeedbackService.submitBranchManagerReviewDetails(bean);
		view.buildReviewReplySuccessLayout(popup);

	}
	
	protected void loadPreviouFeedbackSearchHomePageValues(@Observes @CDIEvent(LOAD_PREVIOUS_FEED_BACK_SEARCH_COMPONENTS_VALUE_HOME_PAGE) final ParameterDTO parameters) {
		FeedbackStatsDto  fbStatusDTO = (FeedbackStatsDto) parameters.getPrimaryParameter();
		Long fbStatus = (Long) parameters.getSecondaryParameter(0, Long.class);
		BeanItemContainer<SelectValue> feedBack = masterService.getMasterValueByCode(ReferenceTable.FEEDBACK);
		BeanItemContainer<SelectValue> feedbackContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		Status reportedStatus = masterService.getStatusByKey(ReferenceTable.FEEDBACK_REPORTED_KEY);
		Status respondedStatus = masterService.getStatusByKey(ReferenceTable.FEEDBACK_RESPONDED_KEY);
		SelectValue reportedStatusSelectValue = new SelectValue(reportedStatus.getKey(),reportedStatus.getProcessValue());
		SelectValue respondedStatusSelectValue = new SelectValue(respondedStatus.getKey(),respondedStatus.getProcessValue());
		SelectValue completedStatusSelectValue = new SelectValue(9999L,"Feedback Completed");
		SelectValue allStatusSelectValue = new SelectValue(0L,SHAConstants.ALL);
		feedbackContainer.addBean(respondedStatusSelectValue);
		feedbackContainer.addBean(reportedStatusSelectValue);
		feedbackContainer.addBean(completedStatusSelectValue);
		feedbackContainer.addBean(allStatusSelectValue);		
		BeanItemContainer<SelectValue> feedbackTypeValue = masterService.getMasterValueByCode(ReferenceTable.FEEDBACK_TYPE);
		view.setDropDownValuesHomePage(feedBack,feedbackContainer,feedbackTypeValue,fbStatusDTO,fbStatus);
	}
}
