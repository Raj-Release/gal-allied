package com.shaic.claim.processdatacorrection.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;

public interface SearchProcessDataCorrectionView extends GMVPView{	
	
	public void list(ProcessDataCorrectionDTO tableRows);	
	public void init(boolean reimburementFlag);

}
