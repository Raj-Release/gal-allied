package com.shaic.paclaim.generateCoveringLetter;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;

public interface GenerateLetterPAClaimView extends GMVPView,WizardStep<GenerateCoveringLetterSearchTableDto>{
	
	public void init(GenerateCoveringLetterSearchTableDto bean, GWizard wizard);
	public void initView();
//	public void showClaimSearchTable(Page<GenerateCoveringLetterSearchTableDto> searchresultList);
//	public void resetClaimSearchCoveringLetterGeneration();
//	public void showGenrateCoveringLetterDetailView(ParameterDTO parameter);
//	public void showGenrateCoveringLetterDetailViewG(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, ParameterDTO parameter);
//	public void showSearchCoveringLetterView(String letterType);
//	public void cancelCoveringLetter(Claim a_claim);
//	public void generateCoveringLetter(ClaimDto claimDto);
//	public void returnPreviousPage(GenerateCoveringLetterSearchTableDto updatedBean);
	public void getUpdatedBean();
}
