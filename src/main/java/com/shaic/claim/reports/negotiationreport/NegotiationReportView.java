package com.shaic.claim.reports.negotiationreport;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.claim.ViewNegotiationDetailsDTO;

public interface NegotiationReportView extends GMVPView{
	
	void negotionReportDtls(List<ViewNegotiationDetailsDTO> reportList);

}
