package com.shaic.feedback.managerfeedback;

import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.branchmanagerfeedback.BranchManagerFeedbackTableDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.vaadin.v7.ui.CheckBox;

@ViewInterface(ManagerFeedBackView.class)
public class ManagerFeedBackUIPresenter extends AbstractMVPPresenter<ManagerFeedBackView> {
	
	public static final String SUBMIT_FEEDBACK = "submit Feedback";
	
	public static final String CANCEL_FEEDBACK="Cancel Feedback";
	
	public static final String SEARCH_FOR_COMPLETED_CASE = "search_for_completed_case_feedback";
	
	public static final String GET_HOME_PAGE_STATS = "Get Home Page Stats for Branch Manager";
	public static final String INSERT_BRANCH_MANAGER_REVIEW_DETAILS = "Insert Branch Manager Review Details";
	
	@EJB
	private ManagerFeedBackService managerFeedbackService;
	
	@EJB
	private DBCalculationService dbService;
	
	public void setUpReference(@Observes @CDIEvent(SUBMIT_FEEDBACK) final ParameterDTO parameters)
	{
		ManagerFeedBackPolicyTableDto bean=(ManagerFeedBackPolicyTableDto)parameters.getPrimaryParameter();

		Boolean result=managerFeedbackService.submitManagerFeedbackForm(bean);
		view.buildSuccessLayout();

	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearchForFeedbackCount(@Observes @CDIEvent(SEARCH_FOR_COMPLETED_CASE) final ParameterDTO parameters) {
		Map<String, Object> feedBackCount = null;
		String userName= (String) parameters.getPrimaryParameter();		
		Long  brancName = (Long) parameters.getSecondaryParameter(0, Long.class);
		String branchBasedCount = (String) parameters.getSecondaryParameter(1, String.class);
		if(null != branchBasedCount && branchBasedCount.equalsIgnoreCase(SHAConstants.BRANCH_BASED)){
			feedBackCount = dbService.getFeedBackCountDetails(userName,brancName.toString());
		}
		else
		{
			feedBackCount = dbService.getFeedBackCountDetails(userName,null);
		}
		
		Long dailycount = 0L;
		Long overallcount = 0L;
		if(feedBackCount != null){
			if(feedBackCount.containsKey("dailyCount")){
				dailycount = (Long) feedBackCount.get("dailyCount");
			}
			if(feedBackCount.containsKey("overAllcount")){
				overallcount = (Long) feedBackCount.get("overAllcount");
			}
		}
		view.setValuesForCompletedCase(dailycount,overallcount);
	}

	@SuppressWarnings({ "deprecation" })
	public void getHomePageStatsForBM(@Observes @CDIEvent(GET_HOME_PAGE_STATS) final ParameterDTO parameters) {
		
		BranchManagerFeedbackhomePageDto homePageDto = (BranchManagerFeedbackhomePageDto) parameters.getPrimaryParameter();
		SelectValue selectedBranch = homePageDto.getHomeBranch();
		String branchCodeValue = selectedBranch != null && selectedBranch.getId() != null ? String.valueOf(selectedBranch.getId()) : null;
		String userName= (String) parameters.getSecondaryParameters()[0];
		
		if(branchCodeValue != null && !branchCodeValue.isEmpty()){
			
			homePageDto = dbService.getBranchManagerHomePageStats(branchCodeValue, userName);
			
			homePageDto.setHomeBranch(selectedBranch);
			view.showRevisedTableValues(homePageDto);
		}
	}
	
	
	public void insertBranchManagerReviewDetails(@Observes @CDIEvent(INSERT_BRANCH_MANAGER_REVIEW_DETAILS) final ParameterDTO parameters)
	{
		BranchManagerFeedbackTableDTO bean=(BranchManagerFeedbackTableDTO)parameters.getPrimaryParameter();

		//managerFeedbackService.submitBranchManagerReviewDetails(bean);
		view.buildReviewReplySuccessLayout();

	}
}
