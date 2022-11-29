package com.shaic.claim.doctorinternalnotes;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public interface SearchInternalNotesView extends GMVPView {
	
	public void list(Page<NewIntimationDto> tableRows);
	
	/*void setView(Class<? extends MVPView> viewClass, boolean selectInNavigationTree,ParameterDTO parameter);*/
	
	

}
