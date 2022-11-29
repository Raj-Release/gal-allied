package com.shaic.claim.rod.citySearchCriteria;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;

public interface CitySearchCriteriaView  extends GMVPView{

	
	public void setTableValues(List<SearchPayableAtTableDTO> list);
}
