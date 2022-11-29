package com.shaic.claim.registration;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.domain.menu.RegistrationBean;

@SuppressWarnings("serial")
			 
public class SearchClaimRegisterViewImpl extends AbstractMVPView implements SearchClaimRegisterView {
	
	@Inject
	private Instance<SearchClaimRegister> searchClaimRegister;

   /* @Inject
    private Instance<MVPView> views;*/
	
    private SearchClaimRegister searchScreen;
    
    private Map<String, Object> referenceData;
	 
	@PostConstruct
	protected void initView() {
		 	addStyleName("view");
	        setSizeFull();
	        searchScreen = searchClaimRegister.get(); 
	        searchScreen.init();	
	        ClaimRegistrationSearchTable searchTable = searchScreen.getSearchTable();
	        searchTable.addSearchListener(this);
	        searchScreen.addSearchListener(this);
//	        setCompositionRoot(searchScreen);
	}
	public void init(){
		searchScreen.initView(referenceData);
		setCompositionRoot(searchScreen);
	}
		
	public void showSerachClaimRegistrationView() {
		
	}

	@Override
	public void showSearchClaimRegistrationTable(RegistrationBean registrationBean) {
//		searchScreen.showTable();
	}
	
	@Override
	public void doSearch() {
		SearchClaimRegisterationFormDto searchDTO = searchScreen.getSearchDTO();
		 ClaimRegistrationSearchTable searchTable = searchScreen.getSearchTable();
		Pageable pageable = searchTable.getPageable();
		searchDTO.setPageable(pageable);
		//String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		//String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchClaimRegistrationPresenter.SEARCH_CLAIMREGISTER_TABLE_SUBMIT,searchDTO);	
		
	}

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

	@Override
	public void resetView() {
		if(searchScreen != null) {
			searchScreen.init();
		}
		
	}

	@Override
	public void setTableDataSource(
			List<SearchClaimRegistrationTableDto> searchResultList) {
		
		/**
		 * comment by Yosuva.a
		 */

//		searchScreen.setSearchResultList(searchResultList);
	}

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
	public void resetSearchResultTableValues() {
		
		searchScreen.resetValues();
	}
	
}
