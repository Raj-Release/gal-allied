package com.shaic.claim.reports.opinionvalidationreport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

/**
 * @author GokulPrasath.A
 *
 */
public interface OpinionValidationReportView extends Searchable {
	public void list(Page<OpinionValidationReportTableDTO> tableRows);
	public void generateReport();
}
