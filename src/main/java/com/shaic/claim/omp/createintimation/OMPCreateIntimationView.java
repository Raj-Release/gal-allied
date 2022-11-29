package com.shaic.claim.omp.createintimation;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface OMPCreateIntimationView extends Searchable {
	
	public void list(Page<OMPCreateIntimationTableDTO> tableRows);
	/*public void init(BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> selectValueForPriority,
		BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> selectValueForUploadedDocument);*/

}
