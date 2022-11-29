package com.shaic.claim.reports.finapprovednotsettledreport;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;
/**
 * Part of CR R1201
 * @author Lakshminarayana
 *
 */
public interface FinApprovedPaymentPendingReportView extends GMVPView {
	
	public void showFinApprovedSettlementPendingReport(List<FinApprovedPaymentPendingReportDto> invAssignStatusList);
	public void resetSearchView();
}
