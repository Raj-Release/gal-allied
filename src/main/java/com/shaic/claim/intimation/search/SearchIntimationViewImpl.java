package com.shaic.claim.intimation.search;

import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.IntimationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SearchIntimationViewImpl extends AbstractMVPView implements SearchIntimationView {
	
	@EJB
	IntimationService intimationservice;
	
	@Inject
	private Instance<SearchIntimationUI> searchIntimation;

	@Inject
	private SearchIntimationDetailTable searchResultTable;
	
	private SearchIntimationFormDto searchDto;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		
		setSizeFull();
		searchIntimation.get().init();
		setCompositionRoot(searchIntimation.get());
		searchResultTable.setSizeFull();
		searchResultTable=searchIntimation.get().searchResultTable;	
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.addSearchListener(this);
		
		
		resetView();
	}

	
	
	@Override
	public void showIntimationDetailsView(ClickEvent event) {
//	IntimationBean a_intimation =  (IntimationBean) event.getButton().getData();
//	ViewIntimation intimationDetailsView = new ViewIntimation(a_intimation);
//	UI.getCurrent().addWindow(intimationDetailsView);
	}

	
	
//	@Override
//	public void showSearchIntimationTable(Map<String,Object> params) {
	@Override
	public void showSearchIntimationTable(SearchIntimationFormDto searchDto) {
		Map<String,Object> params = searchDto.getFilters();
		this.searchDto = searchDto;
		if(!params.containsKey("intimationStatus") || params.get("intimationStatus") == null)	
		{
			searchIntimation.get().showErrorPopup("<b>Intimation Status Mandatory ,Please Select Intimation Status for Search");
//			Notification.show("Warning","Intimation Status Mandatory ,Please Select Intimation Status for Search",Notification.TYPE_WARNING_MESSAGE);
			searchIntimation.get().clearSearchResultTable();
			return;
		}
		if(!params.isEmpty())
		{
		 	if((params.containsKey("fromDate") && !params.containsKey("toDate")) || (params.containsKey("toDate") && !params.containsKey("fromDate")))
			{
//				Notification.show("Error","Please Select both Intimation from date and to date for Search",Notification.TYPE_WARNING_MESSAGE);
				
				searchIntimation.get().showErrorPopup("<b>Please Select both Intimation from date and to date for Search");
				searchIntimation.get().clearSearchResultTable();
				return;
			 }
		     if( (params.containsKey("fromDate") && params.containsKey("toDate")) && ((Date)params.get("toDate")).before((Date)params.get("fromDate")) )
			 {
//				 Notification.show("Error","Invalid Date Range, Please Select Intimation End date greater than Intimation From date",Notification.TYPE_WARNING_MESSAGE);
				 
		    	 searchIntimation.get().showErrorPopup("<b>Invalid Date Range, <br>Please Select Intimation End date greater than Intimation From date");
		    	 searchIntimation.get().clearSearchResultTable();
				 return;
			 }
		  	 
//			 BeanItemContainer<IntimationsDto> intimationsDtoContainer = intimationservice.getSearchIntimationDetails(params);
				
//			 BeanItemContainer<NewIntimationDto> newIntimationDtoContainer = intimationservice.searchCombination(params);
			
//		     Page<NewIntimationDto> newIntimationDtoPagedContainer = intimationservice.searchCombinationforViewAndEdit(params);
		     
		     doSearch();
			 
		 }
		else
		{
			Notification.show("Error","Please Enter altlest one input Parameters for Search",Notification.TYPE_ERROR_MESSAGE);
			return;
		}
	}

	@Override
	public void resetSearchIntimationView() {
		searchIntimation.get().resetSearchIntimationFields();
	}


	@Override
	public void hideSearchFields(String valChanged) {
		// TODO Auto-generated method stub
		 searchIntimation.get().hideSearchFields(valChanged);
		
	}

	private void noRecordFound(Boolean isCrmDummyHospIntimation){
		Label successLabel = null;
		if(isCrmDummyHospIntimation){
			successLabel = new Label("<b style = 'color: black;'>Intimation is in Hospital Creation Workflow.</b>", ContentMode.HTML);
		}else {
		 successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);
		}		
		Button homeButton = new Button("Search Intimation Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.SEARCH_INTIMATION, null);
				
			}
		});
	

	}

	@Override
	public void resetView() {
		if(searchIntimation.get() != null) {
			//searchIntimation.get().init();
			searchIntimation.get().refresh();
			
		}
	}



	@Override
	public void doSearch() {
		
		Pageable pageable = searchResultTable.getPageable();
		 searchDto.setPageable(pageable);
		Page<NewIntimationDto> newIntimationDtoPagedContainer = intimationservice.searchCombinationforViewAndEdit(searchDto);
	     
		 Boolean isCrmDummyHospIntimation = false; 
		 if(newIntimationDtoPagedContainer == null) {
			 Map<String, Object> params = searchDto.getFilters();
			 String intimationNumber = null;
			 String intimationYear = null;
			 intimationNumber = params.containsKey("intimationNumber") ? StringUtils.trim(params.get("intimationNumber").toString()) : null;
			 intimationYear = params.containsKey("intimationYear") ?  StringUtils.trim(params.get("intimationYear").toString()) : null;
			 if(StringUtils.isNumeric(intimationNumber)){
				 if(intimationYear != null){
					 intimationNumber = params.containsKey("intimationNumber") ?  "C%/"+intimationYear+"/%"
							 + StringUtils.trim(params.get("intimationNumber")
									 .toString())  : null;
				 }
			 }
					 if(intimationNumber != null && !intimationNumber.isEmpty()){
						 isCrmDummyHospIntimation = intimationservice.getCRMDummyHospitalIntimationAvaialble(intimationNumber);
					 }
		 }
		 if(isCrmDummyHospIntimation && newIntimationDtoPagedContainer == null){
              noRecordFound(isCrmDummyHospIntimation); 
              searchIntimation.get().buildSearchIntimationTable(null,null);
		 }
		 else if(newIntimationDtoPagedContainer == null) 
		 {
//			 Notification.show("Information","No results found for Search",Notification.TYPE_HUMANIZED_MESSAGE);
			 noRecordFound(false);
			 
			 searchIntimation.get().buildSearchIntimationTable(null,null);						 
		 }
		 else{			 
			 searchResultTable.setTableList(newIntimationDtoPagedContainer.getPageItems());
				searchResultTable.tablesize();
				searchResultTable.setHasNextPage(newIntimationDtoPagedContainer.isHasNext());
				searchIntimation.get().buildSearchIntimationTable(newIntimationDtoPagedContainer,searchResultTable);			 
		 }
	}



	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

}
