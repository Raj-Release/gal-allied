package com.shaic.paclaim.generateCoveringLetter;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ClaimService;
import com.shaic.domain.GenerateCoveringLetterService;
import com.shaic.domain.IntimationService;
import com.shaic.main.navigator.domain.MenuItemBean;

@SuppressWarnings("serial")
@ViewInterface(GenerateLetterPAClaimView.class)
public class GenerateCoveringLetterPAClaimPresenter extends AbstractMVPPresenter<GenerateLetterPAClaimView> {
		
	public static final String RESET_PA_CLAIM_SEARCH_VIEW = "Reset PA Claim Search Fields";
	
	public static final String CANCEL_PA_CLAIM_LETTER_GENERATION = "Cancel PA Claim Covering Letter";
	
	public static final String VIEW_PA_CLAIM_COVERINGLETTER = "View PA Claim Covering Letter";
	public static final String GENERATE_PA_LETTER = "Generate PA Covering Letter";
//	public static final String SET_PA_BPM_OUT_COME = "set PA BPM Out Come";
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private GenerateCoveringLetterService generateCoverinLetterSrevice;
	

	@Override
	public void viewEntered() {
	
		
	} 	
	
//	protected void setBpmOutCome(@Observes @CDIEvent(SET_PA_BPM_OUT_COME) final ParameterDTO parameters) {
//		claimService.setOutComeForCoveringLetterTask(parameters.getPrimaryParameter());
//	}
	
	protected void showClaimSearchViewCoveringLetter(@Observes @CDIEvent(MenuItemBean.GENERATE_COVERINGLETTER) final ParameterDTO parameters) {
//		view.showSearchCoveringLetterView("CoveringLetter");
		view.initView();
	}
	
//	protected void cancelClaimCoveringLetter(@Observes @CDIEvent(CANCEL_PA_CLAIM_LETTER_GENERATION) final ParameterDTO parameters) {
//		Claim a_claim = (Claim) parameters.getPrimaryParameter();
//		view.cancelCoveringLetter(a_claim);
//	}
//	
//	protected void resetSerchClaimCoveringLetter(@Observes @CDIEvent(RESET_PA_CLAIM_SEARCH_VIEW) final ParameterDTO parameters) {
//		view.resetClaimSearchCoveringLetterGeneration();
//	}
	
//	protected void showGenrateCoveringLetterDetailView(@Observes @CDIEvent(VIEW_PA_CLAIM_COVERINGLETTER) final ParameterDTO parameters) {
//		
//		GenerateCoveringLetterSearchTableDto tableDTO = (GenerateCoveringLetterSearchTableDto) parameters.getPrimaryParameter();
//		 view.showGenrateCoveringLetterDetailView(parameters);
//	}
	

//	protected void genrateCoveringLetter(@Observes @CDIEvent(GENERATE_PA_LETTER) final ParameterDTO parameters) {
//		 ClaimDto a_claimDto = (ClaimDto) parameters.getPrimaryParameter();
//		 
//		 view.generateCoveringLetter(a_claimDto);
//	}
	
}
