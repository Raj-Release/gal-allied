package com.shaic.reimbursement.queryrejection.draftquery.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ReimbursementQueryService;

/**
 * 
 * @author Lakshminarayana
 *
 */
@ViewInterface(DraftQueryLetterDetailView.class)
public class DecideOnDraftQueryPresenter extends AbstractMVPPresenter<DraftQueryLetterDetailView> {
	
//	public static final String BUILD_RE_DRAFT_QUERY_LAYOUT = "Redraft Query Layout";
//	public static final String BUILD_REJECT_QUERY_LAYOUT = "Reject Query Layout";
//	public static final String BUILD_APPROVE_QUERY_LAYOUT = "Approve Query Layout";
	
	public static final String SAVE_DRAFT_QUERY_LETTER = "Save Draft Query Letter";
	
	@EJB
	private ReimbursementQueryService submitService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
//	public void buildRedraftQueryLayout(
//			@Observes @CDIEvent(BUILD_RE_DRAFT_QUERY_LAYOUT) final ParameterDTO parameters) {
//		view.buildRedraftQueryLayout();
//	}
//	
//	public void buildRejectQueryLayout(
//			@Observes @CDIEvent(BUILD_REJECT_QUERY_LAYOUT) final ParameterDTO parameters) {
//		view.buildRejectQueryLayout();
//	}
//	
//	public void buildApproveQueryLayout(
//			@Observes @CDIEvent(BUILD_APPROVE_QUERY_LAYOUT) final ParameterDTO parameters) {
//		
//		ClaimQueryDto updatedBean = (ClaimQueryDto)parameters.getPrimaryParameter();
//		
//		if(updatedBean != null){
//			view.setUpdatedBean(updatedBean);
//		}
//		
//		view.buildApproveQueryLayout();
//	}
	
	public void buildApproveQueryLayout(
			@Observes @CDIEvent(SAVE_DRAFT_QUERY_LETTER) final ParameterDTO parameters) {
		
		SearchDraftQueryLetterTableDTO updatedBean = (SearchDraftQueryLetterTableDTO)parameters.getPrimaryParameter();
		
		if(updatedBean != null){
//			submitService.saveReimbursementDraftQuery(updatedBean.getReimbursementQueryDto());
			
		}
		
	}

}
