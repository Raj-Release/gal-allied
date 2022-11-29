package com.shaic.claim.reports.investigationassignedreport;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;
/**
 * Part of CR R0768
 * @author Lakshminarayana
 *
 */
public interface InvAssignReportView extends GMVPView {
	
	public void showInvAssignStatusReport(List<InvAssignStatusReportDto> invAssignStatusList,Long statusId);
	public void resetSearchView();
}
