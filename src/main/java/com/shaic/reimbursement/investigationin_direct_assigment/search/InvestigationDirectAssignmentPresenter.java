package com.shaic.reimbursement.investigationin_direct_assigment.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.cpuskipzmr.SkipZMRListenerTableDTO;
import com.shaic.claim.cpuskipzmr.SkipZMRService;
import com.shaic.domain.Policy;
import com.shaic.domain.TempPolicyNo;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.skipZMR.CPUStageMapping;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.VaadinSession;


@SuppressWarnings("serial")
@ViewInterface(InvestigationDirectAssignmentView.class)
public class InvestigationDirectAssignmentPresenter extends
		AbstractMVPPresenter<InvestigationDirectAssignmentView> {
	
	
	@EJB
	private InvestigationDirectAssignmentService idaService;

	protected static final String SUBMIT_INVE_DIRE_ASS = "submit_investigation_direct_assign";
	public static final String SEARCH_INVE_DIRE_ASS = "search_investigation_direct_assign";

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	
	protected void searchByPolicyNo(
			@Observes @CDIEvent(SEARCH_INVE_DIRE_ASS) final ParameterDTO parameters) {
		if(parameters.getPrimaryParameter() != null) {
			String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);	
			String policyNo = (String) parameters.getPrimaryParameter();
			List<Policy> PolicyNo = idaService.getPolicyNo(policyNo);
			List<InvestigationDirectAssignmentTableDTO> dtoList = new ArrayList<InvestigationDirectAssignmentTableDTO>();
			if(PolicyNo != null && !PolicyNo.isEmpty()) {
				for (Policy tmpPolicyNo : PolicyNo) {
					InvestigationDirectAssignmentTableDTO dto = new InvestigationDirectAssignmentTableDTO();
					
					TempPolicyNo tempPolicy = idaService.getInvestigationByPass(tmpPolicyNo.getPolicyNumber());
					
					if(tempPolicy != null){
						dto.setPolicyNo(tempPolicy.getPolicyNo());
						if(tempPolicy.getActiveStatus() != null && tempPolicy.getActiveStatus().equals(String.valueOf(1)))
							dto.setEnable(Boolean.TRUE);
							else if(tempPolicy.getActiveStatus() != null && tempPolicy.getActiveStatus().equals(String.valueOf(0)))
							dto.setDisable(Boolean.TRUE);
					}else{
						dto.setPolicyNo(tmpPolicyNo.getPolicyNumber());
						
					}
					dto.setUserName(userId);
					dtoList.add(dto);
				}
				
				view.generateTableForPolicyStatus(dtoList);
			} else {
				view.buildFailureLayout("No Records found");
			}
		} else {
			view.buildFailureLayout("Please enter policy No. ");
		}
		
		
		
	}
	
	
	protected void submitIDA(
			@Observes @CDIEvent(SUBMIT_INVE_DIRE_ASS) final ParameterDTO parameters) {
		List<InvestigationDirectAssignmentTableDTO> dto = (List<InvestigationDirectAssignmentTableDTO>) parameters.getPrimaryParameter();
		idaService.submitIDA(dto);
		view.buildSuccessLayout();
	}

}
