package com.shaic.claim.pcc.wizard;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestFormDTO;

@ViewInterface(ProcessPCCCoOrdinatorRequestWizard.class)
public class ProcessPccCoOrdinateRequestPresenter extends AbstractMVPPresenter<ProcessPCCCoOrdinatorRequestWizard>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private SearchProcessPCCRequestService pccRequestService;

	public static final String PCCCOORDINATE_GENERATE_APPROVE_LAYOUT = "pcccoordinate_generate_approve_layout";
	
	public static final String PCCCOORDINATE_GENERATE_QUERY_LAYOUT = "pcccoordinate_generate_query_layout";
	
	public static final String PCCCOORDINATE_GENERATE_USER_DETAILS = "pcccoordinate_generate_user_details";
	
	public static final String SUBMIT_PCCCOORDINATE_DETAILS = "submit_pcccoordinate_details";
	
	public void generateapproveLayout(@Observes @CDIEvent(PCCCOORDINATE_GENERATE_APPROVE_LAYOUT) final ParameterDTO parameters) {	
		
		view.generateapproveLayout();		
	}

	public void generateQuerryLayout(@Observes @CDIEvent(PCCCOORDINATE_GENERATE_QUERY_LAYOUT) final ParameterDTO parameters) {	

		view.generateQuerryLayout();		
	}
	
	public void addUserDetails(@Observes @CDIEvent(PCCCOORDINATE_GENERATE_USER_DETAILS) final ParameterDTO parameters) {	

		String roleCode = (String) parameters.getPrimaryParameter();
		Long cpuCode = (Long)parameters.getSecondaryParameter(0, Long.class);
		view.addUserDetails(pccRequestService.getPCCUserNamesBasedOnCPUCode(roleCode,cpuCode));
		//view.addUserDetails(pccRequestService.getPCCUserNames(roleCode));		
	}
	
	public void submitCOOrdinate(@Observes @CDIEvent(SUBMIT_PCCCOORDINATE_DETAILS) final ParameterDTO parameters) {	

		PccDTO pccDTO = (PccDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		pccRequestService.submitPCCCOOrdinate(pccDTO,userName);
		view.buildSuccessLayout();		
	}
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
