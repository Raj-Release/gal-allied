package com.shaic.paclaim.generateCoveringLetter;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;

public interface SearchGenerateCoveringLetterPAView extends Searchable{
	
	public void initView();
	public void showClaimSearchTable(Page<GenerateCoveringLetterSearchTableDto> searchresultList);
	public void resetSearchResultTableValues();
//	public void showGenrateCoveringLetterDetailView(ParameterDTO parameter);
//	public void showGenrateCoveringLetterDetailViewG(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, ParameterDTO parameter);
//	public void showSearchCoveringLetterView(String letterType);
//	public void cancelCoveringLetter(Claim a_claim);
//	public void generateCoveringLetter(ClaimDto claimDto);
}
