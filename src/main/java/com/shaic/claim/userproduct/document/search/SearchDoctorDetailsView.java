package com.shaic.claim.userproduct.document.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


public interface SearchDoctorDetailsView extends Searchable {
	
	public void getResultList(Page<SearchDoctorDetailsTableDTO> tableRows);
	
	public void setDoctorDetails(SearchDoctorDetailsTableDTO viewSearchCriteriaDTO);

}
