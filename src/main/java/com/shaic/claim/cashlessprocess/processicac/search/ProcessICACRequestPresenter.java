package com.shaic.claim.cashlessprocess.processicac.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.PreauthWizard;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationFormDTO;
import com.shaic.claim.scoring.HospitalScoringDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(ProcessICACRequestView.class)
public class ProcessICACRequestPresenter extends AbstractMVPPresenter<ProcessICACRequestView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -256384569425809859L;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ProcessICACService processICACService;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	
	public static final String SUBMIT_BUTTON_DIRECT_ICAC_REQ = "submit_button_direct_ICAC_req";

	public static final String SUBMIT_BUTTON_ICAC_RESPONSE = "submit_button_ICAC_Response";
	
	
	public void submitDirectIcacRequset(@Observes @CDIEvent(SUBMIT_BUTTON_DIRECT_ICAC_REQ) final ParameterDTO parameters) {
				
		SearchProcessICACTableDTO finalReqDto = (SearchProcessICACTableDTO) parameters.getPrimaryParameter();
		String userName = (String)parameters.getSecondaryParameter(0, String.class);
		processICACService.submitForDirectRequest(finalReqDto,userName);
		view.buildSuccessLayout();
		
	}
	
	public void submitResponse(@Observes @CDIEvent(SUBMIT_BUTTON_ICAC_RESPONSE) final ParameterDTO parameters) {
		
		SearchProcessICACTableDTO finalReqDto = (SearchProcessICACTableDTO) parameters.getPrimaryParameter();
		String userName = (String)parameters.getSecondaryParameter(0, String.class);
		processICACService.submitResponseTeamSubmit(finalReqDto,userName);
		view.buildSuccessLayout();
		
	}
	

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
}
