package com.shaic.claim.lumen.searchleveltwo;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;

public interface ProcessLevelTwoWizard extends GMVPView{

	public void cancelLumenRequest();
	public void submitLumenRequest();
	public void showReplyFromMISSubView(Map<String, List<?>> temp, String argRemarks);
	//public void showGenerateLetterTable(Boolean flag);
}
