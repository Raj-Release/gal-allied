package com.shaic.claim.omp.searchregistration;

import java.util.List;
import java.util.Map;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.menu.RegistrationBean;

public interface OMPSearchClaimRegisterView extends Searchable{
	void setReferenceData(Map<String, Object> referenceData);
	void showSearchClaimRegistrationTable(Page<OMPSearchClaimRegistrationTableDTO> registrationListBean);
	void showSearchClaimRegistrationTable(RegistrationBean registrationBean);
//	void setView(Class<? extends MVPView> viewClass, boolean selectInNavigationTree, ParameterDTO parameter);
	void setTableDataSource(List<OMPSearchClaimRegistrationTableDTO> searchResultList);
//	void setIntimationDetails(NewIntimationDto intimationDto);
	
}
