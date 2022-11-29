package com.shaic.claim.userproduct.document.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface UserManagementView extends Searchable{
	
	
	
	public void getResultList(Page<SearchDoctorDetailsTableDTO> tableRows);
	
	public void setDoctorDetails(SearchDoctorDetailsTableDTO viewSearchCriteriaDTO);
	
	

}
