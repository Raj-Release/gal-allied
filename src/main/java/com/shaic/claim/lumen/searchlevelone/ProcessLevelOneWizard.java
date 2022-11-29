package com.shaic.claim.lumen.searchlevelone;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;

public interface ProcessLevelOneWizard extends GMVPView{

	public void cancelLumenRequest();
	public void submitLumenRequest();
	public void showReplyFromMISSubView(Map<String, List<?>> temp, String argRemarks);
}
