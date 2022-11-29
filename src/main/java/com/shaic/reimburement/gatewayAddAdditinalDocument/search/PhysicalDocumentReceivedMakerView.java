package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface PhysicalDocumentReceivedMakerView extends Searchable {


	public void list(Page<PhysicalDocumentReceivedMakerTableDTO> tableRows);
	public void validation();


}
