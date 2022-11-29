package com.shaic.claim.cpuskipzmr;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SkipZMRView extends GMVPView {
	public void initView(BeanItemContainer<SelectValue> cpuCodeContainer);

	public void buildSuccessLayout();
	public void buildFailureLayout(String message);

	public void generateTableForCpuCode(List<SkipZMRListenerTableDTO> masCpuCode);
}
