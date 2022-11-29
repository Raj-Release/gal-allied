package com.shaic.claim.pcc.divisionHead;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.wizard.ProcessPCCCoOrdinatorRequestWizard;

@ViewInterface(ProcessPCCDivisionHeadRequestWizardView.class)
public class ProcessPccDivisionHeadRequestPresenter extends AbstractMVPPresenter<ProcessPCCDivisionHeadRequestWizardView> {
	
	@EJB
	private DivisionHeadRequestService divisionHeadRequestService;
	
	public static final String SUBMIT_DIVISION_HEAD_DETAILS = "submit_division_head_details";
	
	public void submitCOOrdinate(@Observes @CDIEvent(SUBMIT_DIVISION_HEAD_DETAILS) final ParameterDTO parameters) {	

		PccDTO pccDTO = (PccDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		PccDetailsTableDTO pccDetailsTableDTO=(PccDetailsTableDTO)parameters.getSecondaryParameter(1, PccDetailsTableDTO.class);
		divisionHeadRequestService.submitPCCDivisionHead(pccDTO,userName,pccDetailsTableDTO);
		view.buildSuccessLayout();		
	}
	
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
