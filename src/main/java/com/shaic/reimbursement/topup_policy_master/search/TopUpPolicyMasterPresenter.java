package com.shaic.reimbursement.topup_policy_master.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.TopUpPolicy;
import com.shaic.domain.Policy;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.VaadinSession;


@SuppressWarnings("serial")
@ViewInterface(TopUpPolicyMasterView.class)
public class TopUpPolicyMasterPresenter extends AbstractMVPPresenter<TopUpPolicyMasterView> {
	
	
	@EJB
	private TopUpPolicyMasterService topUpPolicyService;

	protected static final String SUBMIT_TOP_UP_POLICY = "submit_top_up_policy";
	public static final String SEARCH_TOP_UP_POLICY = "search_top_up_policy";

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	
	protected void searchByPolicyNo(
			@Observes @CDIEvent(SEARCH_TOP_UP_POLICY) final ParameterDTO parameters) {
		if(parameters.getPrimaryParameter() != null) {
			String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);	
			String policyNo = (String) parameters.getPrimaryParameter();
			List<Policy> PolicyNo = topUpPolicyService.getPolicyNo(policyNo);
			List<TopUpPolicyMasterTableDTO> dtoList = new ArrayList<TopUpPolicyMasterTableDTO>();
			if(PolicyNo != null && !PolicyNo.isEmpty()) {
				for (Policy tmpPolicyNo : PolicyNo) {
					TopUpPolicyMasterTableDTO dto = new TopUpPolicyMasterTableDTO();
					
					TopUpPolicy tempPolicy = topUpPolicyService.getTopUpPolicyObj(tmpPolicyNo.getPolicyNumber());
					
					if(tempPolicy != null){
						dto.setPolicyNo(tempPolicy.getPolicyNo());
						if(tempPolicy.getActiveStatus() != null && tempPolicy.getActiveStatus().equals(String.valueOf(1)))
							dto.setEnable(Boolean.TRUE);
							else if(tempPolicy.getActiveStatus() != null && tempPolicy.getActiveStatus().equals(String.valueOf(0)))
							dto.setDisable(Boolean.TRUE);
							dto.setRemarks(tempPolicy.getRemarks()); 
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
			@Observes @CDIEvent(SUBMIT_TOP_UP_POLICY) final ParameterDTO parameters) {
		List<TopUpPolicyMasterTableDTO> dto = (List<TopUpPolicyMasterTableDTO>) parameters.getPrimaryParameter();
		topUpPolicyService.submitTopUpPolicy(dto);
		view.buildSuccessLayout();
	}

}
