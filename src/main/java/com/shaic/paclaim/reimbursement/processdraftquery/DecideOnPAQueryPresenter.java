package com.shaic.paclaim.reimbursement.processdraftquery;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.reimbursement.processdraftquery.ClaimQueryDto;

/**
 * 
 * @author Lakshminarayana
 *
 */
@ViewInterface(DecideOnPAQueryView.class)
public class DecideOnPAQueryPresenter extends AbstractMVPPresenter<DecideOnPAQueryView> {
	
	public static final String BUILD_RE_DRAFT_PA_QUERY_LAYOUT = "Redraft PA Query Layout";
	public static final String BUILD_REJECT_PA_QUERY_LAYOUT = "Reject PA Query Layout";
	public static final String BUILD_APPROVE_PA_QUERY_LAYOUT = "Approve PA Query Layout";
//	public static final String DELET_DRAFT_LETTER_REMARKS = "Delete Draft letter Remarks";
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void buildRedraftQueryLayout(
			@Observes @CDIEvent(BUILD_RE_DRAFT_PA_QUERY_LAYOUT) final ParameterDTO parameters) {
		view.buildRedraftQueryLayout();
	}
	
//	public void deleteDraftQueryLetterRemarks(
//			@Observes @CDIEvent(DELET_DRAFT_LETTER_REMARKS) final ParameterDTO parameters) {
//		DraftQueryLetterDetailTableDto deltedObj = (DraftQueryLetterDetailTableDto)parameters.getPrimaryParameter();
//		view.deleteDraftQueryLetterRemarks(deltedObj);
//	}
	
	public void buildRejectQueryLayout(
			@Observes @CDIEvent(BUILD_REJECT_PA_QUERY_LAYOUT) final ParameterDTO parameters) {
		view.buildRejectQueryLayout();
	}
	
	public void buildApproveQueryLayout(
			@Observes @CDIEvent(BUILD_APPROVE_PA_QUERY_LAYOUT) final ParameterDTO parameters) {
		
		ClaimQueryDto updatedBean = (ClaimQueryDto)parameters.getPrimaryParameter();
		
		if(updatedBean != null){
			view.setUpdatedBean(updatedBean);
		}
		
		view.buildApproveQueryLayout();
	}

}
