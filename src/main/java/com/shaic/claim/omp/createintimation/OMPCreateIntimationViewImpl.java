package com.shaic.claim.omp.createintimation;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class OMPCreateIntimationViewImpl extends AbstractMVPView implements OMPCreateIntimationView{
	
	@Inject
	private OMPCreateIntimationSearchPage searchForm;

	@Inject
    private OMPSearchIntimationDetailTable searchResultTable;
	
	@Inject
    private OMPCreateIntimationDetailTable createResultTable;
	
	private VerticalSplitPanel mainPanel;
	
	String displayType;
	
	
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		
		searchForm.init();
		
	/*	displayType = searchForm.getdisplayType();
		if(displayType.equals(SHAConstants.SEARCH_INTIMATION.toString())){
			searchResultTable.init("", false, true);
			searchResultTable.setSubmitTableHeader();
		}else{
			createResultTable.init("", false, true);
			createResultTable.setSubmitTableHeader();
		}*/
		
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		/*if(displayType.equals(SHAConstants.SEARCH_INTIMATION.toString())){
			mainPanel.setSecondComponent(buildSearchIntimationTableLayout());
		}else{
			mainPanel.setSecondComponent(buildCreateIntimationTableLayout());
		}*/
		mainPanel.setSplitPosition(46);
		setHeight("550px");
		setCompositionRoot(mainPanel);
		
		searchForm.addSearchListener(this);
		/*if(displayType.equals(SHAConstants.SEARCH_INTIMATION.toString())){
			searchResultTable.addSearchListener(this); //TC
		}else{
			createResultTable.addSearchListener(this);
		}*/
		
		resetView();//unwanted code
	}
	
	private VerticalLayout buildSearchIntimationTableLayout() {

		FormLayout searchTableLayout = new FormLayout();
		searchTableLayout.setSpacing(false);
		searchTableLayout.setMargin(false);		
		
		VerticalLayout	secondLayout = new VerticalLayout();
		HorizontalLayout hLayout = new HorizontalLayout(searchTableLayout);
		hLayout.setSpacing(false);
		hLayout.setMargin(false);
		secondLayout.addComponent(hLayout);
		secondLayout.setSpacing(false);
		secondLayout.setMargin(false);
		secondLayout.addComponent(searchResultTable);

		return secondLayout;		
	}
	
	private VerticalLayout buildCreateIntimationTableLayout() {
		
		FormLayout createTableLayout = new FormLayout();
		createTableLayout.setSpacing(false);
		createTableLayout.setMargin(false);		
		
		VerticalLayout	secondLayout = new VerticalLayout();
		HorizontalLayout hLayout = new HorizontalLayout(createTableLayout);
		hLayout.setSpacing(false);
		hLayout.setMargin(false);
		secondLayout.addComponent(hLayout);
		secondLayout.setSpacing(false);
		secondLayout.setMargin(false);
		secondLayout.addComponent(createResultTable);
		
		return secondLayout;
	}
				
	
	@Override
	public void resetSearchResultTableValues() {
//		searchForm.setPassportFieldEnable(true);
//		searchForm.resetAlltheValues();		
//		searchForm.setPassportFieldEnable(false);
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void list(Page<OMPCreateIntimationTableDTO> tableRows) {
		
		displayType = searchForm.getdisplayType();
		
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size()) {
			
			if(displayType.equals(SHAConstants.SEARCH_INTIMATION.toString())){
				mainPanel.setSecondComponent(buildSearchIntimationTableLayout());
				searchResultTable.init("", false, true);
				searchResultTable.setSubmitTableHeader();
				searchResultTable.setTableList(tableRows.getTotalList());
				searchResultTable.tablesize();
				searchResultTable.setHasNextPage(tableRows.isHasNext());
			}else{
				mainPanel.setSecondComponent(buildCreateIntimationTableLayout());
				createResultTable.init("", false, true);
				createResultTable.setSubmitTableHeader();
				createResultTable.setTableList(tableRows.getTotalList());
				createResultTable.tablesize();
				createResultTable.setHasNextPage(tableRows.isHasNext());
			}
		} else {
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Search & Create Intimation Home");
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
					fireViewEvent(MenuItemBean.OMP_SEARCHINTIMATION_CREATE, null);
				}
			});
		}
		searchForm.enableButtons();
	}
		
		
	

	@Override
	public void resetView() {
		//		searchForm.refresh(); 
		searchForm.resetAlltheValues();
		searchForm.setDefaultValue();
		if(mainPanel.getSecondComponent() != null){
			mainPanel.removeComponent(mainPanel.getSecondComponent());
		}
	}



	@Override
	public void doSearch() {
		displayType = searchForm.getdisplayType();
		OMPCreateIntimationFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = null;
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);

		if (displayType.equals(SHAConstants.SEARCH_INTIMATION.toString()) &&!searchForm.doValidation().equals("No value")) {
			searchResultTable.addSearchListener(this);
			pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			fireViewEvent(OMPCreateIntimationPresenter.OMP_SUBMIT_SEARCH, searchDTO,userName,passWord);
		} 
		if(displayType.equals(SHAConstants.SEARCH_INTIMATION.toString()) &&searchForm.doValidation().equals("No value")){
			String eMsg ="Please Enter atleast one input Parameters for Search";
			showErrorPopup(eMsg);
		}
		if(displayType.equals(SHAConstants.CREATE_INTIMATION.toString())&&!searchForm.doPolicyValidation().equals("No PolicyNumber Entered")){
			createResultTable.addSearchListener(this);
			pageable = createResultTable.getPageable();
			searchDTO.setPageable(pageable);
			fireViewEvent(OMPCreateIntimationPresenter.OMP_SUBMIT_CREATE, searchDTO,userName,passWord);
		}
		if(displayType.equals(SHAConstants.CREATE_INTIMATION.toString())&&searchForm.doPolicyValidation().equals("No PolicyNumber Entered")){
			String eMsg ="Please Enter Policy Number";
			showErrorPopup(eMsg);
		}

	}
//	}
	/*@Override
	public void init(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
		searchForm.setType(parameter,selectValueForPriority,statusByStage,selectValueForUploadedDocument);
	}*/

	/*@Override
	public void init(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
		searchForm.setType(parameter,selectValueForPriority,statusByStage,selectValueForUploadedDocument);
	}*/

	private void showErrorPopup(String eMsg) {
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

