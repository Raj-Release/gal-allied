package com.shaic.claim.userreallocation;

import java.util.Map;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.AutoAllocationDetails;


public interface SearchReallocationDoctorDetailsView extends Searchable {
	
	void init(SearchReallocationDoctorDetailsTableDTO tableDTO);
	
	void buildSuccessLayout(Map<String, Object> mapper);
	
	public void getResultList(Page<SearchReallocationDoctorDetailsTableDTO> tableRows);
	
	public void setDoctorDetails(SearchReallocationDoctorDetailsTableDTO viewSearchCriteriaDTO);
	
	public void buildSubmitLayout();
	
	void buildIntimationTableLayout(AutoAllocationDetails detail);

}
