package com.shaic.claim.reports.ackwithoutrodreport;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface AckWithoutRodReportView extends GMVPView {
	
	public void showAckWithoutRodReport(List<AckWithoutRodTableDto> ackWithoutRodList);
	public void resetSearchView();
}
