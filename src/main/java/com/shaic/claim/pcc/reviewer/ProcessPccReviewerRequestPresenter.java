package com.shaic.claim.pcc.reviewer;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;

@ViewInterface(ProcessPCCReviewerRequestWizardView.class)
public class ProcessPccReviewerRequestPresenter extends AbstractMVPPresenter<ProcessPCCReviewerRequestWizardView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2825493969601811363L;

	@EJB
	private PccReviewerRequestService pccReviewerRequestService;
	
	@EJB
	private SearchProcessPCCRequestService processPCCRequestService;
	
	public static final String SUBMIT_PCC_REVIEWER_DETAILS = "submit_pccreviewer_details";

	public static final String PCC_REVIEWER_GENERATE_RESPONSE_LAYOUT = "pccreviewer_generate_response_layout";
	
	public static final String PCC_REVIEWER_GENERATE_QUERY_LAYOUT = "pccreviewer_generate_query_layout";
	
	public static final String PCC_REVIEWER_GENERATE_USER_DETAILS = "pccreviewer_generate_user_details";
	
	
	
	public void submitPccReviewer(@Observes @CDIEvent(SUBMIT_PCC_REVIEWER_DETAILS) final ParameterDTO parameters) {	

		PccDTO pccDTO = (PccDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		PccDetailsTableDTO pccDetailsTableDTO=(PccDetailsTableDTO)parameters.getSecondaryParameter(1, PccDetailsTableDTO.class);
		pccReviewerRequestService.submitPccReviewer(pccDTO,userName,pccDetailsTableDTO);
		view.buildSuccessLayout();		
	}
	
    public void generateapproveLayout(@Observes @CDIEvent(PCC_REVIEWER_GENERATE_RESPONSE_LAYOUT) final ParameterDTO parameters) {	
		
		view.generateResponseLayout();;		
	}

	public void generateQuerryLayout(@Observes @CDIEvent(PCC_REVIEWER_GENERATE_QUERY_LAYOUT) final ParameterDTO parameters) {	

		view.generateQuerryLayout();		
	}
	
	public void addUserDetails(@Observes @CDIEvent(PCC_REVIEWER_GENERATE_USER_DETAILS) final ParameterDTO parameters) {	

		String roleCode = (String) parameters.getPrimaryParameter();
		Long cpuCode = (Long)parameters.getSecondaryParameter(0, Long.class);
		if(roleCode.equalsIgnoreCase(SHAConstants.PCC_REVIEWER_ROLE)){
			view.addUserDetails(processPCCRequestService.getPCCUserNames(roleCode));
		}else{
			view.addUserDetails(processPCCRequestService.getPCCUserNamesBasedOnCPUCode(roleCode,cpuCode));	
		}
		//view.addUserDetails(processPCCRequestService.getPCCUserNames(roleCode));		
	}
	
	
	
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
