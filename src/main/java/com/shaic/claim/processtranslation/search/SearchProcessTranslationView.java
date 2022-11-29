package com.shaic.claim.processtranslation.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchProcessTranslationView extends GMVPView {
	public void list(Page<SearchProcessTranslationTableDTO> tableRows);	
	public void init(boolean reimburementFlag);

}
