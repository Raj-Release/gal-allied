package com.shaic.paclaim.settlementpullback;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.settlementpullback.dto.PASearchSettlementPullBackDTO;


@SuppressWarnings("serial")
@ViewInterface(PASettlementPullBackView.class)
public class PASettlementPullBackPresenter extends AbstractMVPPresenter<PASettlementPullBackView> {

	private static final long serialVersionUID = -8024160721276240363L;
	
	@EJB
	private PASettlementPullBackService clearCashlessService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private PASettlementPullBackService pASettlementPullBackService;
	
	public static final String SUBMIT_SETTLEMENT_PULL_BACK = "submit_settlement_pull_back_PA";
	public static final String CANCEL_SETTLEMENT_PULL_BACK = "cancel_settlement_pull_back_PA";

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	protected void submitClearCashless(
			@Observes @CDIEvent(SUBMIT_SETTLEMENT_PULL_BACK) final ParameterDTO parameters) {
		PASearchSettlementPullBackDTO dto = (PASearchSettlementPullBackDTO) parameters.getPrimaryParameter();
		Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(dto.getRodKey());
		
		pASettlementPullBackService.submitSettlementPullBack(dto, reimbursementByKey);
			
		view.buildSuccessLayout();
	}
	
	protected void cancelClearCashless(
			@Observes @CDIEvent(CANCEL_SETTLEMENT_PULL_BACK) final ParameterDTO parameters) {
		
	}
	
	

}
