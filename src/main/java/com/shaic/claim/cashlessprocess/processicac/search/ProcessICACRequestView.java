package com.shaic.claim.cashlessprocess.processicac.search;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.claim.scoring.HospitalScoringDTO;

public interface ProcessICACRequestView extends GMVPView{

	public void setReferenceData(Map<String, Object> referenceData);

	void buildSuccessLayout();
	
}
