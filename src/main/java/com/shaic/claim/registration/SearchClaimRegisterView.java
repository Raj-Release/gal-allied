package com.shaic.claim.registration;

import java.util.List;
import java.util.Map;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.menu.RegistrationBean;

public interface SearchClaimRegisterView extends Searchable{
	void setReferenceData(Map<String, Object> referenceData);
	void showSearchClaimRegistrationTable(Page<SearchClaimRegistrationTableDto> registrationListBean);
	void showSearchClaimRegistrationTable(RegistrationBean registrationBean);
//	void setView(Class<? extends MVPView> viewClass, boolean selectInNavigationTree, ParameterDTO parameter);
	void setTableDataSource(List<SearchClaimRegistrationTableDto> searchResultList);
//	void setIntimationDetails(NewIntimationDto intimationDto);
	
}
