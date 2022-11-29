package com.shaic.branchmanagerfeedback;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.userproduct.document.ProductAndDocumentTypeView;
import com.shaic.feedback.managerfeedback.previousFeedback.BranchManagerPreviousFeedbackTableDTO;
import com.shaic.feedback.managerfeedback.previousFeedback.PreviousFeedbackView;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(BranchManagerFeedbackView.class)
public class BranchManagerFeedbackPresenter extends AbstractMVPPresenter<BranchManagerFeedbackView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	DBCalculationService dBCalculationService;

	@EJB
	BranchManagerFeedbackService branchManagerFeedbackService;
	
	public static final String BRANCH_TYPE_SEARCH = "search on branch basis";
	public static final String REPLY_FEEDBACK = "feedback reply";
	public static final String POLICY_REPLY_FEEDBACK = "policy feedback reply";
	public static final String PROPOSAL_REPLY_FEEDBACK = "proposal feedback reply";
	public static final String CLAIM_REPLY_FEEDBACK = "claim feedback reply";
	public static final String SUBMIT_CLICK ="submit";
	public static final String LOAD_PREVIOUS_FEED_BACK_VIEW_VALUE ="load_previous_feedback_view_values feedback";
	public static final String ZONE_CODE_BASED_BRANCH = "zone code based branch";

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	protected void branchBasedSearch(@Observes @CDIEvent(BRANCH_TYPE_SEARCH) final ParameterDTO parameters){
		
		TechnicalDepartmentFeedbackDTO  bean = (TechnicalDepartmentFeedbackDTO) parameters.getPrimaryParameter();
		List<BranchManagerFeedbackTableDTO> resultList = branchManagerFeedbackService.getFeedbackDetails(bean);
		Page<BranchManagerFeedbackTableDTO> pageList = new Page<BranchManagerFeedbackTableDTO>();
		pageList.setPageItems(resultList);
		pageList.setTotalRecords(resultList.size());
		view.setTableList(pageList);
		
	}
	
	protected void feedbackReply(@Observes @CDIEvent(REPLY_FEEDBACK) final ParameterDTO parameters) {
		//BranchManagerFeedbackTableDTO  bean = (BranchManagerFeedbackTableDTO) parameters.getPrimaryParameter();
		//List<BranchManagerFeedbackTableDTO> result = branchManagerFeedbackService.getTechTeamReply(bean);
		
		view.showFeedbaclReply();
	}
	protected void policyFeedbackReply(@Observes @CDIEvent(POLICY_REPLY_FEEDBACK) final ParameterDTO parameters) {
		BranchManagerFeedbackTableDTO  bean = (BranchManagerFeedbackTableDTO) parameters.getPrimaryParameter();
		List<BranchManagerFeedbackTableDTO> policyDetailList = branchManagerFeedbackService.getPolicyDetailsByParentKey(bean.getFeedbackKey());
		bean.setPolicyList(policyDetailList);
		view.showPolicyPopup(bean);
	}
	protected void proposalFeedbackReply(@Observes @CDIEvent(PROPOSAL_REPLY_FEEDBACK) final ParameterDTO parameters) {
		BranchManagerFeedbackTableDTO  bean = (BranchManagerFeedbackTableDTO) parameters.getPrimaryParameter();
		List<BranchManagerFeedbackTableDTO> proposalDetailList = branchManagerFeedbackService.getProposalDetailsByParentKey(bean.getFeedbackKey());
		bean.setProposalList(proposalDetailList);
		view.showProposalPopup(bean);
	}
	protected void claimFeedbackReply(@Observes @CDIEvent(CLAIM_REPLY_FEEDBACK) final ParameterDTO parameters) {
		BranchManagerFeedbackTableDTO  bean = (BranchManagerFeedbackTableDTO) parameters.getPrimaryParameter();
		List<BranchManagerFeedbackTableDTO>  claimDetailList = branchManagerFeedbackService.getClaimDetailsByParentKey(bean.getFeedbackKey());
		bean.setClaimList(claimDetailList);
		view.showClaimPopup(bean);
	}
	protected void submitValues(@Observes @CDIEvent(SUBMIT_CLICK) final ParameterDTO parameters) {
		BranchManagerFeedbackTableDTO  bean = (BranchManagerFeedbackTableDTO) parameters.getPrimaryParameter();
		branchManagerFeedbackService.submitValues(bean);
		view.showResult();
	}
	protected void loadPreviouFeedbackViewValues(@Observes @CDIEvent(LOAD_PREVIOUS_FEED_BACK_VIEW_VALUE) final ParameterDTO parameters) {
		BranchManagerFeedbackTableDTO  bean = (BranchManagerFeedbackTableDTO) parameters.getPrimaryParameter();
		ManagerFeedbackReplyView previousFeedbackView = (ManagerFeedbackReplyView)parameters.getSecondaryParameter(0, ManagerFeedbackReplyView.class);
		List<BranchManagerFeedbackTableDTO> tableList = branchManagerFeedbackService.getViewPolicyDetails(bean.getFeedbackKey());
		bean.setViewFeedbackTableList(tableList);
	}
	protected void zoneBasedBranch(@Observes @CDIEvent(ZONE_CODE_BASED_BRANCH) final ParameterDTO parameters){
		Long  zoneCode = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> branchValueContainer = dBCalculationService.getBranchDetailsContainerForBranchManagerFeedback(zoneCode);
		view.loadBranchDetails(branchValueContainer);
		
	}
}
