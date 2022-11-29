package com.shaic.claim.settlementpullback;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.settlementpullback.dto.SearchSettlementPullBackDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;


@SuppressWarnings("serial")
@ViewInterface(SettlementPullBackView.class)
public class SettlementPullBackPresenter extends AbstractMVPPresenter<SettlementPullBackView> {

	private static final long serialVersionUID = -8024160721276240363L;
	
	@EJB
	private SettlementPullBackService clearCashlessService;
	
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
	private SettlementPullBackService settlementPullBackService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	public static final String SUBMIT_SETTLEMENT_PULL_BACK = "submit_settlement_pull_back";
	public static final String CANCEL_SETTLEMENT_PULL_BACK = "cancel_settlement_pull_back";

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	protected void submitClearCashless(
			@Observes @CDIEvent(SUBMIT_SETTLEMENT_PULL_BACK) final ParameterDTO parameters) {
		SearchSettlementPullBackDTO dto = (SearchSettlementPullBackDTO) parameters.getPrimaryParameter();
		Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(dto.getRodKey());
		
		settlementPullBackService.submitSettlementPullBack(dto, reimbursementByKey);
		DocAcknowledgement docAcknowlegement = ackDocReceivedService
				.findAcknowledgment(reimbursementByKey.getKey());
		
		reimbursementByKey.setDocAcknowLedgement(docAcknowlegement);
			
		view.buildSuccessLayout();
	}
	
	protected void cancelClearCashless(
			@Observes @CDIEvent(CANCEL_SETTLEMENT_PULL_BACK) final ParameterDTO parameters) {
		
	}
	
	

}
