package com.shaic.claim.pcc.reassignHRM;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.hrmCoordinator.HRMCoordinatorRequestService;
import com.shaic.claim.pcc.hrmCoordinator.ProcessPCCHrmCoordinatorRequestWizardView;

@ViewInterface(ProcessPCCReassginHrmCoordinatorRequestWizardView.class)
public class ProcessPCCReassignHrmCoordinatorRequestPresenter extends AbstractMVPPresenter<ProcessPCCReassginHrmCoordinatorRequestWizardView>{


	
	@EJB
	private ReassignHRMCoordinatorRequestService requestService;
	
	public static final String SUBMIT_REASSIGN_HRM_COORDINATOR_DETAILS = "submit_reassign_hrm_coorinator_details";
	
	//public static final String REASSIGN_HRM_COORDINATOR_GENERATE_NEGOTIATION_APPLICABLE = "reassign_hrm_coordinator_generate_negotiation_applicable";
	
	//public static final String REASSIGN_HRM_COORDINATOR_GENERATE_USER_DETAILS = "reassign_hrm_coordinator_generate_user_details";
	
	public void submitZonalMedicalHead(@Observes @CDIEvent(SUBMIT_REASSIGN_HRM_COORDINATOR_DETAILS) final ParameterDTO parameters) {	

		PccDTO pccDTO = (PccDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		PccDetailsTableDTO pccDetailsTableDTO=(PccDetailsTableDTO)parameters.getSecondaryParameter(1, PccDetailsTableDTO.class);
		requestService.submitreassignHRMCoordinator(pccDTO,userName,pccDetailsTableDTO);
		view.buildSuccessLayout();		
	}

	/*public void generateFieldsBasedOnNegotiationApplicable(@Observes @CDIEvent(REASSIGN_HRM_COORDINATOR_GENERATE_NEGOTIATION_APPLICABLE) final ParameterDTO parameters)
	{
		Boolean isCked = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnNegotiation(isCked);
	}*/
	
	/*public void addUserDetails(@Observes @CDIEvent(REASSIGN_HRM_COORDINATOR_GENERATE_USER_DETAILS) final ParameterDTO parameters) {	

		String roleCode = (String) parameters.getPrimaryParameter();
		System.out.println(String.format("Role Code [%s]", roleCode));
		view.addUserDetails(requestService.getPCCUserNames(roleCode));		
	}*/
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}


}
