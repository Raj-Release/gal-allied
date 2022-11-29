package com.shaic.claim.cashlessprocess.flagreconsiderationrequest.showwizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.google.gwt.dev.util.collect.HashMap;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search.SearchFlagReconsiderationReqTableDTO;
import com.shaic.claim.clearcashless.dto.SearchClearCashlessDTO;
import com.shaic.claim.intimation.rule.IntimationRule;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;


@SuppressWarnings("serial")
@ViewInterface(ReconsiderationFlagRequestView.class)
public class ReconsiderationFlagRequestPresenter extends AbstractMVPPresenter<ReconsiderationFlagRequestView> {

	private static final long serialVersionUID = -8024160721276240363L;
	
	@EJB
	private ReconsiderationFlagRequestService reconsiderationFlagRequestService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationService;
	
	public static final String SUBMIT_RECONSIDERATION_FLAG_REQUEST = "submit_reconsideration_flag_request";

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	protected void submitReconsiderationFlag(
			@Observes @CDIEvent(SUBMIT_RECONSIDERATION_FLAG_REQUEST) final ParameterDTO parameters) {
		SearchFlagReconsiderationReqTableDTO dto = (SearchFlagReconsiderationReqTableDTO) parameters.getPrimaryParameter();
	
		if(dto.getRodKey() != null){
			try {
				
				reconsiderationFlagRequestService.submitRejectionReconsiderationFlag(dto);
				
				
			} catch(Exception e) {
				System.out.println(" ERROR----------");
				e.printStackTrace();
			}
			view.buildSuccessLayout();
			
		}else{
			view.buildFailureLayout();

		}
		
	}
	
	
	


}
