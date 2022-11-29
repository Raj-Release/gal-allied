package com.shaic.claim.registration.ackhoscomm.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchAcknowledgeHospitalCommunicationView extends GMVPView {
	public void list(
			Page<SearchAcknowledgeHospitalCommunicationTableDTO> tableRows);

	public void setCpuCode(BeanItemContainer<SelectValue> tmpCpuCodes);
	
	
}
