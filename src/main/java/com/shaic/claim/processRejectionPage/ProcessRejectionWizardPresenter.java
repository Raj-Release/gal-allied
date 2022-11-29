package com.shaic.claim.processRejectionPage;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;

@ViewInterface(ProcessRejectionWizard.class)
public class ProcessRejectionWizardPresenter extends AbstractMVPPresenter<ProcessRejectionWizard> {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_DATA="submit the data for process rejection  wizard page";
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private HospitalService hosptialService;
	
	
	public void saveData(@Observes @CDIEvent(SUBMIT_DATA)final ParameterDTO parameters){
		
		ProcessRejectionDTO rejectionDto=(ProcessRejectionDTO)parameters.getPrimaryParameter();
		Boolean submitDescion=(Boolean)parameters.getSecondaryParameter(0, Boolean.class);
		String outCome=(String)parameters.getSecondaryParameter(1, String.class);
		SearchProcessRejectionTableDTO searchDTO=(SearchProcessRejectionTableDTO)parameters.getSecondaryParameter(2, SearchProcessRejectionTableDTO.class);
		
		Boolean result=claimService.saveProcessRejection(rejectionDto,submitDescion,outCome,searchDTO);
//		Boolean result = true;
		
	    view.savedResult();
		
	}
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
