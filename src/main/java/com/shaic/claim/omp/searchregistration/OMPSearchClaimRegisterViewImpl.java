package com.shaic.claim.omp.searchregistration;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.MVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.menu.RegistrationBean;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
			 
public class OMPSearchClaimRegisterViewImpl extends AbstractMVPView implements OMPSearchClaimRegisterView {
	
	@Inject
	private Instance<OMPSearchClaimRegister> searchClaimRegister;

    @Inject
    private Instance<MVPView> views;
	
    private OMPSearchClaimRegister searchScreen;
    
    private Map<String, Object> referenceData;
	 
	@PostConstruct
	protected void initView() {
		 	addStyleName("view");
	        setSizeFull();
	        searchScreen = searchClaimRegister.get(); 
	        searchScreen.init();	
	        OMPSearchClaimRegistrationDetailTable searchTable = searchScreen.getSearchTable();
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
//		if(searchScreen .validatePage()){
		
		OMPSearchClaimRegistrationFormDto searchDTO = searchScreen.getSearchDTO();
		
		OMPSearchClaimRegistrationDetailTable searchTable = searchScreen.getSearchTable();
		Pageable pageable = searchTable.getPageable();
		searchDTO.setPageable(pageable);
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(OMPSearchClaimRegistrationPresenter.SEARCH_CLAIMREGISTER_TABLE_SUBMIT,searchDTO);	
//		}
//		else{
//			showErrorMessage("Please Enter Intimation Number Or Policy Number");
//			resetView();
//		//	resetSearchResultTableValues();
//		}
	}

	@Override
	public void showSearchClaimRegistrationTable(Page<OMPSearchClaimRegistrationTableDTO> registrationListBean) {
//		searchScreen.showTable();
		searchScreen.setSearchResultList(registrationListBean);
	}

	
	


	@Override
	public void resetView() {
		if(searchScreen != null) {
			searchScreen.init();
		}
		
	}

	@Override
	public void setTableDataSource(
			List<OMPSearchClaimRegistrationTableDTO> searchResultList) {
		
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
	
	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
}
