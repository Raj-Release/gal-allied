package com.shaic.claim.allowghiregistration;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.domain.menu.RegistrationBean;

public interface SearchGhiAllowClaimRegisterView  extends GMVPView{
	void setReferenceData(Map<String, Object> referenceData);
	void showSearchClaimRegistrationTable(Page<SearchClaimRegistrationTableDto> registrationListBean);
//	void showSearchClaimRegistrationTable(RegistrationBean registrationBean);
//	void setView(Class<? extends MVPView> viewClass, boolean selectInNavigationTree, ParameterDTO parameter);
//	void setIntimationDetails(NewIntimationDto intimationDto);
	
}
