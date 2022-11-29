package com.shaic.claim.settlementpullback.searchbatchpendingpullback;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ClaimPayment;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;

@SuppressWarnings("serial")
@ViewInterface(LotPullBackPageView.class)
public class LotPullBackPagePresenter extends AbstractMVPPresenter<LotPullBackPageView>{


	private static final long serialVersionUID = -8024160721276240363L;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private LotPullBackPageService lotPullBackService;
	
	public static final String SUBMIT_lOT_PULL_BACK = "submit_lot_pull_back";
	public static final String CANCEL_LOT_PULL_BACK = "cancel_lot_pull_back";

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	protected void submitClearCashless(
			@Observes @CDIEvent(SUBMIT_lOT_PULL_BACK) final ParameterDTO parameters) {
		SearchLotPullBackTableDTO dto = (SearchLotPullBackTableDTO) parameters.getPrimaryParameter();
		
		lotPullBackService.submitLotPullBack(dto);
			
		view.buildSuccessLayout();
	}
	
	protected void cancelClearCashless(
			@Observes @CDIEvent(CANCEL_LOT_PULL_BACK) final ParameterDTO parameters) {
		
	}
	
}
