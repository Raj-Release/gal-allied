package com.shaic.claim.processtranslation.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchProcessTranslationViewR3 extends GMVPView{
	public void list(Page<SearchProcessTranslationTableDTO> tableRows);	
}
