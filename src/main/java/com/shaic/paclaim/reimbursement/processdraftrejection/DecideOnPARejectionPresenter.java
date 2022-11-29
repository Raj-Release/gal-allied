package com.shaic.paclaim.reimbursement.processdraftrejection;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

/**
 * 
 * @author Lakshminarayana
 *
 */
@ViewInterface(DecideOnPARejectionView.class)
public class DecideOnPARejectionPresenter extends AbstractMVPPresenter<DecideOnPARejectionView> {
	
	public static final String BUILD_PA_RE_DRAFT_REJECTION_LAYOUT = "Redraft PA Rejection Layout";
	public static final String BUILD_PA_DISAPPROVE_REJECTION_LAYOUT = "Disapprove PA Rejection Layout";
	public static final String BUILD_PA_APPROVE_REJECTION_LAYOUT = "Approve PA Rejection Layout";
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void buildRedraftQueryLayout(
			@Observes @CDIEvent(BUILD_PA_RE_DRAFT_REJECTION_LAYOUT) final ParameterDTO parameters) {
		view.buildRedraftRejectionLayout();
	}
	
	public void buildRejectQueryLayout(
			@Observes @CDIEvent(BUILD_PA_DISAPPROVE_REJECTION_LAYOUT) final ParameterDTO parameters) {
		view.buildDisapproveRejectionLayout();
	}
	
	public void buildApproveQueryLayout(
			@Observes @CDIEvent(BUILD_PA_APPROVE_REJECTION_LAYOUT) final ParameterDTO parameters) {
		view.buildApproveRejectionLayout();
	}

}
