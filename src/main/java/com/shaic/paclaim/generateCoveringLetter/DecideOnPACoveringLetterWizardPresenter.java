package com.shaic.paclaim.generateCoveringLetter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.domain.ClaimService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.ui.UI;

@ViewInterface(DecideOnPACoveringLetterWizard.class)
public class DecideOnPACoveringLetterWizardPresenter extends
AbstractMVPPresenter<DecideOnPACoveringLetterWizard> {

	public static final String SUBMIT_PA_COVERING_LETTER = "Submit PA Covering Letter";
	
	@EJB
	private ClaimService coveringLetterService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	public void submitDraftQuery(
			@Observes @CDIEvent(SUBMIT_PA_COVERING_LETTER) final ParameterDTO parameters) {
		
		GenerateCoveringLetterSearchTableDto paCoveringLetterDto = (GenerateCoveringLetterSearchTableDto)parameters.getPrimaryParameter();
		
		Map<String,Object> outcome = new HashMap<String,Object>();
		String userId = UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString();
		String password = UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD).toString();
		paCoveringLetterDto.setUserId(userId);
		paCoveringLetterDto.setUsername(userId);
		paCoveringLetterDto.getClaimDto().setModifiedBy(userId);
		paCoveringLetterDto.getClaimDto().setModifiedDate(new Date());
		outcome.put(BPMClientContext.USERID, userId);
		outcome.put(BPMClientContext.PASSWORD,password);
		outcome.put("OUTCOME",SHAConstants.OUTCOME_GENERATE_COVERING_LETTER_END);
		outcome.put("Bean",paCoveringLetterDto);
		//Added for FVR insert.
		outcome.put("fvrDetailsBean",paCoveringLetterDto.getClaimDto().getNewIntimationDto());
		
		
		coveringLetterService.setOutComeForCoveringLetterTask(outcome);
//		coveringLetterService.uploadCoveringLetterToDMs(paCoveringLetterDto.getClaimDto());
		view.buildSuccessLayout();
	}
	
	
}
