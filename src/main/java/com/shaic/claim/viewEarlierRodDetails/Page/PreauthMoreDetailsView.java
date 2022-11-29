package com.shaic.claim.viewEarlierRodDetails.Page;

import java.util.Map;

import com.shaic.arch.GMVPView;


public interface PreauthMoreDetailsView extends GMVPView {

	void setHospitalizationDetails(Map<Integer, Object> hospitalizationDetails);

	void setApprovedAmountField(Integer approvedAmount);
	
	

}
