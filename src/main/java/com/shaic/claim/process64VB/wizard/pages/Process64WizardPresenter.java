package com.shaic.claim.process64VB.wizard.pages;

import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.process64VB.search.ProcessVBSearchService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.VB64ApprovalRequest;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(Process64VBview.class)
public class Process64WizardPresenter extends AbstractMVPPresenter<Process64VBview>{

	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ProcessVBSearchService processVBSearchService;
	
	protected static final String PROCESS64_SUBMITTED_EVENT = "process 64 submitted event";

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	public void submitWizard(
			@Observes @CDIEvent(PROCESS64_SUBMITTED_EVENT) final ParameterDTO parameters) {
		
		SearchPreauthTableDTO bean = (SearchPreauthTableDTO) parameters.getPrimaryParameter();
		Object[] secondaryParameters = parameters.getSecondaryParameters();
		String paymentStatus =null;
		if (secondaryParameters != null && secondaryParameters.length >= 1) {
			paymentStatus = (String) secondaryParameters[0];
		}
		
		Preauth preauth = preauthService.getPreauthById(bean.getKey());
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		
		VB64ApprovalRequest approvalRequest = preauthService.getProcess64ById(bean.getKey());
		if(approvalRequest!=null){
			
			processVBSearchService.updateVB64ApprovalRequest(approvalRequest,bean,paymentStatus,preauth);
		
		}
		
		String outCome = "";
		if(preauth!=null && preauth.getStatus().getKey().equals(ReferenceTable.VB_COMPLIANCE_TO_PREAUTH)){
			outCome = SHAConstants.PROCESS_VB_PREAUTH_OUTCOME;
		}
		
		if(preauth!=null && preauth.getStatus().getKey().equals(ReferenceTable.VB_COMPLIANCE_TO_ENHANCEMENT)){
			outCome = SHAConstants.PROCESS_VB_ENHANCEMENT_OUTCOME;
		}
		wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
		
		//Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
		
		DBCalculationService dbCalService = new DBCalculationService();
		//dbCalService.initiateTaskProcedure(objArrayForSubmit);
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		
		view.buildSuccessLayout();	
	}	
}
