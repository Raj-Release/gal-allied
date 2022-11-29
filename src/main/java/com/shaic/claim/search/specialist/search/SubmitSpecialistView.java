package com.shaic.claim.search.specialist.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SubmitSpecialistView extends GMVPView {
	public void list(Page<SubmitSpecialistTableDTO> tableRows);	
	public void init(BeanItemContainer<SelectValue> parameter, boolean isReimburement, BeanItemContainer<SelectValue> cpuCodeContainer);
	public void setDropDownValues(BeanItemContainer<SelectValue> cpuCodeContainer);

}
