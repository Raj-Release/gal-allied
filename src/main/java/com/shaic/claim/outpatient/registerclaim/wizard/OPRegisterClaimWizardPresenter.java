package com.shaic.claim.outpatient.registerclaim.wizard;

import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.outpatient.OutpatientService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(OPRegisterClaimWizard.class)
public class OPRegisterClaimWizardPresenter extends AbstractMVPPresenter<OPRegisterClaimWizard>{

	private static final long serialVersionUID = 8820720086680581847L;
	public static final String OP_SUBMITTED_EVENT = "op_submitted_event";
	public static final String OP_CANCEL_EVENT = "op_cancel_event";
	public static final String OP_BALANCE_SUM_INSURED_EVENT = "op_balance_sumInsured_event";

	@EJB
	private OutpatientService outpatientService;

	@EJB
	private DBCalculationService dbService;

	
	
	public void submitWizard(@Observes @CDIEvent(OP_SUBMITTED_EVENT) final ParameterDTO parameters) {
		OutPatientDTO bean = (OutPatientDTO) parameters.getPrimaryParameter();
		outpatientService.submitOPRegisterClaim(bean);
		dbService.invokeAccumulatorForOP(bean.getPolicy().getPolicyNumber(), bean.getDocumentDetails().getInsuredPatientName().getHealthCardNumber(),bean.getDocumentDetails().getConsultationType().getCommonValue());
		view.buildSuccessLayout();
	}
	
	public void cancelIntimation(@Observes @CDIEvent(OP_CANCEL_EVENT) final ParameterDTO parameters) {
		view.cancelIntimation();
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void balanceSumInsuredWizard(@Observes @CDIEvent(OP_BALANCE_SUM_INSURED_EVENT) final ParameterDTO parameters) {
		OutPatientDTO bean = (OutPatientDTO) parameters.getPrimaryParameter();
		String claimTypeId = "OP";
		Long claimKey = 0l;
		Map<String, Integer> opBalanceSumInsured = dbService.getOPAvailableAmount(bean.getDocumentDetails().getInsuredPatientName().getKey(),claimKey, ReferenceTable.OUT_PATIENT,
				(bean.getDocumentDetails().getConsultationType() != null && bean.getDocumentDetails().getConsultationType().getCommonValue() != null) ? bean.getDocumentDetails().getConsultationType().getCommonValue() : "0");
		Integer opAvailableAmount = 0;
		
		if(opBalanceSumInsured != null && !opBalanceSumInsured.isEmpty()){
			System.out.println(String.format("OP Available amount [%s]", opAvailableAmount));
			opAvailableAmount = opBalanceSumInsured.get(SHAConstants.CURRENT_BALANCE_SI);
			bean.setAvailableSI(opAvailableAmount.toString());
		}
		
		view.setValueToBalanceSumInsured(opAvailableAmount);
	}
}
