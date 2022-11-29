package com.shaic.claim.rod.wizard.cancelAcknowledgement;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.common.APIService;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;


@ViewInterface(CancelAcknowledgementWizardView.class)
public class CancelAcknowledgementWizardPresenter extends AbstractMVPPresenter<CancelAcknowledgementWizardView> {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CANCEL_ACKNOWLEDGEMENT = "Cancel acknowledgement";
	
	@EJB
	private CreateRODService createRODService;
	
	@EJB
	private APIService apiService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ClaimService claimService;
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(CANCEL_ACKNOWLEDGEMENT) final ParameterDTO parameters) {
		
		Boolean isPA = false;
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		
		DocAcknowledgement docAcknowledgement = createRODService.submitCancelAcknowledgement(rodDTO);
		
		Claim claim = docAcknowledgement.getClaim();
		
		if(claim != null && claim.getLobId() != null && claim.getLobId().equals(ReferenceTable.PA_LOB_KEY)){
			isPA = true;
		}
		
		Long countOfAckByClaimKey = 0l;
		
		List<DocAcknowledgement> listOfAcknowledgement = createRODService.getNonCancelledAcknowledgement(claim.getKey());
		for (DocAcknowledgement docAcknowledgement2 : listOfAcknowledgement) {
			if(! docAcknowledgement2.getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS)){
				countOfAckByClaimKey = countOfAckByClaimKey+1;
			}
		}
		
		Hospitals hospitalDetailsByKey = hospitalService.getHospitalDetailsByKey(claim.getIntimation().getHospital());
		
		try{
			//createRODService.updateClaimProvisionAmount(countOfAckByClaimKey, docAcknowledgement);
		
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				
				Claim claim1 = claimService.getClaimByClaimKey(docAcknowledgement.getClaim().getKey());
				String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim1, hospitalDetailsByKey.getName(), String.valueOf(claim1.getCurrentProvisionAmount().longValue()));
				apiService.updateProvisionAmountToPremia(provisionAmtInput);
			}
		}catch(Exception e){
			e.printStackTrace();
		}


//		createRODService.submitTaskToBPMForCancelAcknowledgement(rodDTO,docAcknowledgement);
		
		createRODService.submitDBprocedureForCancelAcknowlegdement(rodDTO, docAcknowledgement);
		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(docAcknowledgement.getClaim(), hospitalDetailsByKey);
		
		Object[] inputArray = (Object[])arrayListForDBCall[0];
		
		//preauthService.callReminderTaskForDB(inputArray);

		if(isPA){
			createRODService.submitTaskToBPMForPACancelAcknowledgement(rodDTO,docAcknowledgement);
		}else{
			createRODService.submitTaskToBPMForCancelAcknowledgement(rodDTO,docAcknowledgement);	
		}

		view.buildSuccessLayout();
		
	}
	
	

	
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
