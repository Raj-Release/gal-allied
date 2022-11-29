package com.shaic.reimbursement.claims_alert.search;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;

public interface ClaimsAlertMasterView extends GMVPView{
	
	public void initView();
	public void buildSuccessLayout();
	public void buildFailureLayout(String message);
	public void generateTableForClaimsAlerts(List<ClaimsAlertTableDTO> claimsAlertTableDTOs,Map<String, Object> referenceData);
}
