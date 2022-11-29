package com.shaic.claim.reimbursement.processDraftRejectionLetterDetail;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * 
 * @author Lakshminarayana
 *
 */
@ViewInterface(DecideOnRejectionView.class)
public class DecideOnRejectionPresenter extends AbstractMVPPresenter<DecideOnRejectionView> {
	
	public static final String BUILD_RE_DRAFT_REJECTION_LAYOUT = "Redraft Rejection Layout";
	public static final String BUILD_DISAPPROVE_REJECTION_LAYOUT = "Disapprove Rejection Layout";
	public static final String BUILD_APPROVE_REJECTION_LAYOUT = "Approve Rejection Layout";
	public static final String REJECT_SUB_CATEG_LAYOUT_PROCESS_DRAFT_REJ = "Rej subcateg Layout for Process Draft Rej";
	
	@EJB
	private MasterService masterService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void generateRejSubCategLayout(
			@Observes @CDIEvent(REJECT_SUB_CATEG_LAYOUT_PROCESS_DRAFT_REJ) final ParameterDTO parameters) {

		Long rejCategId = (Long) parameters.getPrimaryParameter();
		
		BeanItemContainer<SelectValue> rejSubcategContainer = masterService.getRejSubcategContainer(rejCategId);
		
		view.setSubCategContainer(rejSubcategContainer);
	}
	
	public void buildRedraftQueryLayout(
			@Observes @CDIEvent(BUILD_RE_DRAFT_REJECTION_LAYOUT) final ParameterDTO parameters) {
		view.buildRedraftRejectionLayout();
	}
	
	public void buildRejectQueryLayout(
			@Observes @CDIEvent(BUILD_DISAPPROVE_REJECTION_LAYOUT) final ParameterDTO parameters) {
		view.buildDisapproveRejectionLayout();
	}
	
	public void buildApproveQueryLayout(
			@Observes @CDIEvent(BUILD_APPROVE_REJECTION_LAYOUT) final ParameterDTO parameters) {
		view.buildApproveRejectionLayout();
	}

}
