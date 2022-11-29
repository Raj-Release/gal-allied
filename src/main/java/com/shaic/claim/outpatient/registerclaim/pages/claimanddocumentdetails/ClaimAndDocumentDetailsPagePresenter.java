package com.shaic.claim.outpatient.registerclaim.pages.claimanddocumentdetails;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.domain.InsuredService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(ClaimAndDocumentDetailsPageInterface.class)
public class ClaimAndDocumentDetailsPagePresenter extends AbstractMVPPresenter<ClaimAndDocumentDetailsPageInterface>{

	private static final long serialVersionUID = 5811691782410656962L;
	public static final String SET_UP_REFERENCE = "claim_and_document_details_page_set_up_reference";
	public static final String CHECK_HEALTH_CHECK_UP_SUM_INSURED_VALIDATION = "check_health_check_up_sum_insured_validation";
	Map<String, Object> referenceData = new HashMap<String, Object>();

	@EJB
	private InsuredService insuredService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setUpReference(
			@Observes @CDIEvent(SET_UP_REFERENCE) final ParameterDTO parameters) {
		
		OutPatientDTO dto = (OutPatientDTO) parameters.getPrimaryParameter();
		referenceData.put("insuredPatientName", insuredService.getCLSInsuredList(dto.getPolicyDto().getPolicyNumber()));
		referenceData.put("insuredList", insuredService.getCLSInsuredList(dto.getPolicyDto().getPolicyNumber()));
		referenceData.put("claimType", masterService.getSelectValueContainer(ReferenceTable.CLAIM_TYPE));
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("modeOfReceipt", masterService.getSelectValueContainer(ReferenceTable.ACK_DOC_MODE_OF_RECEIPT));
		referenceData.put("docReceivedFrom", masterService.getSelectValueContainer(ReferenceTable.ACK_DOC_RECEIVED_FROM));
		referenceData.put("receivedStatus", masterService.getSelectValueContainer(ReferenceTable.ACK_DOC_RECEIVED_STATUS));
		view.setupReferences(referenceData);
	}
	
	public void setSumInsuredValidation(
			@Observes @CDIEvent(CHECK_HEALTH_CHECK_UP_SUM_INSURED_VALIDATION) final ParameterDTO parameters) {
		Long insuredKey = (Long) parameters.getPrimaryParameter();
		Integer healthCheckupSumInsured = dbCalculationService.getHealthCheckupSumInsured(insuredKey);
		view.setSumInsuredValidation(healthCheckupSumInsured);
		
	}

}
