package com.shaic.claim.reimbursement.talktalktalk;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.shaic.arch.SHAConstants;
import com.shaic.domain.IntimationService;
import com.vaadin.server.VaadinSession;

public class DialerTask extends TimerTask{
	
	IntimationService intimationService;
	InitiateTalkTalkTalkDTO initiateTalkDto;
	VaadinSession session;
	
	DialerTask(IntimationService intimationService,InitiateTalkTalkTalkDTO initiateTalkDto,VaadinSession session){
		this.intimationService = intimationService;
		this.initiateTalkDto = initiateTalkDto;
		this.session = session;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		Date date = new Date();
    	System.out.println("Place Call Date and Time "+date);
    	DialerPlaceCallResponse placeCallResponse = intimationService.dialerPlaceCall(initiateTalkDto);
		if(placeCallResponse != null){
			if(placeCallResponse.getSTATUS() != null && placeCallResponse.getSTATUS().equalsIgnoreCase("CL000")){
//				endCallBtn.setEnabled(true);
				if(placeCallResponse.getLEAD_ID() != null){
					initiateTalkDto.setConvoxid(placeCallResponse.getLEAD_ID());
//					fireViewEvent(InitiateTalkTalkTalkWizardPresenter.ENABLE_DIALER_BUTTONS,initiateTalkDto);
//					session.setAttribute(SHAConstants.LEAD_ID, placeCallResponse.getLEAD_ID());
//					session.setAttribute(SHAConstants.REF_ID, placeCallResponse.getRefno());

				}
				if(placeCallResponse.getCALL_HIT_REFERENCE_NUMBER() != null){
					initiateTalkDto.setCallHitRefNo(placeCallResponse.getCALL_HIT_REFERENCE_NUMBER());
				}
			}else {
//				showErrorMessage(placeCallResponse.getMESSAGE());
			}
		}
    
		
	}
	
	

}
