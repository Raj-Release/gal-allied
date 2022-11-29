package com.shaic.claim.allowghiregistration;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.table.Page;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.navigator.View;

@SuppressWarnings("serial")
@UIScoped
@CDIView(value = MenuItemBean.SEARCH_GHI_ALLOW_REGISTER_CLAIM)			 
public class SearchGhiAllowClaimRegisterViewImpl extends AbstractMVPView implements SearchGhiAllowClaimRegisterView,View {
	
//	@Inject
//	private Instance<SearchGhiAllowClaimRegister> searchClaimRegister;

   /* @Inject
    private Instance<MVPView> views;*/
	
	@Inject
    private SearchGhiAllowClaimRegister searchScreen;
    
    private Map<String, Object> referenceData;
	 
	public void init(Map<String, Object> referenceMaster){
		
        setSizeFull();
        referenceData = referenceMaster;
//      searchScreen = searchClaimRegister.get();
        
        searchScreen.init(referenceMaster);
        
//		searchScreen.initView(referenceData);
		setCompositionRoot(searchScreen);
	}
		
	public void showSerachClaimRegistrationView() {
		
	}

	
//	@Override
//	public void doSearch() {
//		SearchClaimRegisterationFormDto searchDTO = searchScreen.getSearchDTO();
//		 SearchGhiAllowClaimRegistrationTable searchTable = searchScreen.getSearchTable();
//		Pageable pageable = searchTable.getPageable();
//		searchDTO.setPageable(pageable);
		//String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		//String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
//		fireViewEvent(SearchGhiAllowClaimRegistrationPresenter.SEARCH_GHI_ALLOW_CLAIMREGISTER_TABLE_SUBMIT,searchDTO);	
//		
//	}

	@Override
	public void showSearchClaimRegistrationTable(Page<SearchClaimRegistrationTableDto> registrationListBean) {
//		searchScreen.showTable();
		searchScreen.setSearchResultList(registrationListBean);
		
		
	}

//	@Override
//	public void setView(Class<? extends MVPView> viewClass,
//			boolean selectInNavigationTree, ParameterDTO parameter) {
//		 RegistrationBean registrationBean = (RegistrationBean) parameter.getPrimaryParameter();
//		 MVPView view = views.select(viewClass).get();
//		 setCompositionRoot((Component) view);
//	      ((AbstractMVPView) view).enter();
//	}

//	@Override
//	public void setIntimationDetails(NewIntimationDto intimationDto) {
//		searchScreen.setIntimationDto(intimationDto);
//	}

	@Override
	public void setReferenceData(Map<String, Object> referenceMaster) {
		
//		searchScreen.setupReferences(referenceMaster);
		
		referenceData = referenceMaster;
		
	}
	@Override
	public void resetView() {
		if(searchScreen !=  null){
			searchScreen.resetValues();
		}	
		
	}

	
}
