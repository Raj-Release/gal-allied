package com.shaic.claim.cvc.postprocess;


import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.claim.cvc.SearchCVCFormDTO;
import com.shaic.claim.cvc.SearchCVCTableDTO;

public interface SearchPostProcessCVCView extends GMVPView{

	public void list(SearchCVCTableDTO bean);
}
