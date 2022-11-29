package com.shaic.claim.reports.negotiationreport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.ViewNegotiationDetailsDTO;

public interface SearchUpdateNegotiationView extends Searchable{
	public void list(Page<ViewNegotiationDetailsDTO> tableRows);
	public void setNegotiationDetails(ViewNegotiationDetailsDTO negDtls,Boolean isClmProc,Boolean isNegotiated);
}
