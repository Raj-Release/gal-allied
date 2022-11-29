package com.shaic.claim.outpatient.processOP.pages.settlement;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;

@ViewInterface(OPClaimSettlementPageInterface.class)
public class OPClaimSettlementPagePresenter extends AbstractMVPPresenter<OPClaimSettlementPageInterface>{

	private static final long serialVersionUID = 5811691782410656962L;
	public static final String SETTLEMENT_SET_UP_REFERENCE = "process_claim_and_document_settlement_page_set_up_reference";
	public static final String OP_IFSC_DETAILS  = "op_ifsc_details";
	public static final String OP_POPULATE_PREVIOUS_ACCT_DETAILS = "op_populate_previous_acct_details";
	Map<String, Object> referenceData = new HashMap<String, Object>();

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
	}


	public void setUpReference(@Observes @CDIEvent(SETTLEMENT_SET_UP_REFERENCE) final ParameterDTO parameters) {
		//OutPatientDTO dto = (OutPatientDTO) parameters.getPrimaryParameter();		
		view.setupReferences(referenceData);
	}

	public void setupIFSCDetails(@Observes @CDIEvent(OP_IFSC_DETAILS) final ParameterDTO parameters) {
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
		view.setUpIFSCDetails(viewSearchCriteriaDTO);
	}

	public void populatePreviousPaymentDetails(@Observes @CDIEvent(OP_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters){
		PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
		view.populatePreviousPaymentDetails(tableDTO);
	}

}
