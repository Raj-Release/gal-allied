package com.shaic.claim.pcc.processor;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;

@ViewInterface(ProcessPCCProcessorRequestWizardView.class)
public class ProcessPccProcessorRequestPresenter extends AbstractMVPPresenter<ProcessPCCProcessorRequestWizardView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SearchProcessPCCProcessorRequestService pccRequestService;
	
	@EJB
	private SearchProcessPCCRequestService processPCCRequestService;
	
	public static final String PCCCPROCESSOR_GENERATE_APPROVE_LAYOUT = "pccprocessor_generate_approve_layout";	
	
    public static final String PCCCPROCESSOR_GENERATE_QUERY_LAYOUT = "pccprocessor_generate_query_layout";	
    
	public static final String PCCCPROCESSOR_GENERATE_RESPONSE_LAYOUT = "pccprocessor_generate_response_layout";
	
	public static final String PCCCPROCESSOR_GENERATE_DISAPPROVE_LAYOUT = "pccprocessor_generate_disapprove_layout";
	
	public static final String PCCCPROCESSOR_GENERATE_USER_DETAILS = "pccprocessor_generate_user_details";	
	
	public static final String SUBMIT_PCCCPROCESSOR_DETAILS = "submit_pccprocessor_details";
	
	
    public void generateapproveLayout(@Observes @CDIEvent(PCCCPROCESSOR_GENERATE_APPROVE_LAYOUT) final ParameterDTO parameters) {	
		
		view.generateapproveLayout();		
	}

	public void generateQuerryLayout(@Observes @CDIEvent(PCCCPROCESSOR_GENERATE_QUERY_LAYOUT) final ParameterDTO parameters) {	

		view.generateQuerryLayout();		
	}
	
	public void generateResponseLayout(@Observes @CDIEvent(PCCCPROCESSOR_GENERATE_RESPONSE_LAYOUT) final ParameterDTO parameters) {	

		view.generateResponseLayout();		
	}
	
	public void generateDisapproveLayout(@Observes @CDIEvent(PCCCPROCESSOR_GENERATE_DISAPPROVE_LAYOUT) final ParameterDTO parameters) {	

		view.generateDisapproveLayout();		
	}
	
	public void addUserDetails(@Observes @CDIEvent(PCCCPROCESSOR_GENERATE_USER_DETAILS) final ParameterDTO parameters) {	

		String roleCode = (String) parameters.getPrimaryParameter();
		Long cpuCode = (Long)parameters.getSecondaryParameter(0, Long.class);
		
		if(roleCode.equalsIgnoreCase(SHAConstants.PCC_REVIEWER_ROLE)){
			view.addUserDetails(processPCCRequestService.getPCCUserNames(roleCode));
		}else{
			view.addUserDetails(processPCCRequestService.getPCCUserNamesBasedOnCPUCode(roleCode,cpuCode));	
		}
		
	}
	
	public void submitCOOrdinate(@Observes @CDIEvent(SUBMIT_PCCCPROCESSOR_DETAILS) final ParameterDTO parameters) {	

		PccDTO pccDTO = (PccDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		PccDetailsTableDTO pccDetailsTableDTO=(PccDetailsTableDTO)parameters.getSecondaryParameter(1, PccDetailsTableDTO.class);
		pccRequestService.submitPCCProcessor(pccDTO,userName,pccDetailsTableDTO);
		view.buildSuccessLayout();		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
