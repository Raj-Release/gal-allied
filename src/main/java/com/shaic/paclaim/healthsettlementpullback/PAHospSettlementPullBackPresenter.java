package com.shaic.paclaim.healthsettlementpullback;

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
import com.shaic.paclaim.healthsettlementpullback.dto.PAHospSearchSettlementPullBackDTO;


@SuppressWarnings("serial")
@ViewInterface(PAHospSettlementPullBackView.class)
public class PAHospSettlementPullBackPresenter extends AbstractMVPPresenter<PAHospSettlementPullBackView> {

	private static final long serialVersionUID = -8024160721276240363L;
	
	@EJB
	private PAHospSettlementPullBackService clearCashlessService;
	
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
	private PAHospSettlementPullBackService pAHospSettlementPullBackService;
	
	public static final String SUBMIT_SETTLEMENT_PULL_BACK = "submit_settlement_pull_back_PA_hosp";
	public static final String CANCEL_SETTLEMENT_PULL_BACK = "cancel_settlement_pull_back_PA_hosp";

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	protected void submitClearCashless(
			@Observes @CDIEvent(SUBMIT_SETTLEMENT_PULL_BACK) final ParameterDTO parameters) {
		PAHospSearchSettlementPullBackDTO dto = (PAHospSearchSettlementPullBackDTO) parameters.getPrimaryParameter();
		Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(dto.getRodKey());
		
		pAHospSettlementPullBackService.submitSettlementPullBack(dto, reimbursementByKey);
			
		view.buildSuccessLayout();
	}
	
	protected void cancelClearCashless(
			@Observes @CDIEvent(CANCEL_SETTLEMENT_PULL_BACK) final ParameterDTO parameters) {
		
	}
	
	

}
