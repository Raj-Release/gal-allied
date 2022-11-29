package com.shaic.claim.registration;

import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.ClaimDto;
import com.shaic.domain.Claim;

public interface GenerateLetterView extends Searchable{
	
	public void initView();
	public void showClaimSearchTable(Page<GenerateCoveringLetterSearchTableDto> searchresultList);
	public void resetClaimSearchCoveringLetterGeneration();
	public void showGenrateCoveringLetterDetailView(ParameterDTO parameter);
//	public void showGenrateCoveringLetterDetailViewG(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, ParameterDTO parameter);
	public void showSearchCoveringLetterView(String letterType);
	public void cancelCoveringLetter(Claim a_claim);
	public void generateCoveringLetter(ClaimDto claimDto);
}
