package com.shaic.claim.reports.branchManagerFeedBack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.MultiSelectCPU;
import com.shaic.arch.table.MultiSelectStatus;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.domain.ReferenceTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * AS Part of CR  R1238
 * @author Lakshminarayana
 *
 */

@SuppressWarnings("serial")
@UIScoped
public class BranchManagerFeedBackReport extends ViewComponent  {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
//	private ComboBox feedBackTypeCmb;
	private ComboBox zonalOfficeCmb;
	private ComboBox periodCmb;
	private ComboBox branchOfficeCmb;	

	private Button btnSearch;
	private Button btnReset;
	private HorizontalLayout buttonLayout;
	private Button exportBtn; 
	private ExcelExport excelExport;
	private ExcelExport branchExcelExport;
	private Label dummyLabel;
	private BeanFieldGroup<BranchManagerFeedBackReportDto> binder;
	private BranchManagerFeedBackReportDto bean;
	
	private BeanItemContainer<SelectValue> feedBackTypeContainer;
	private BeanItemContainer<SelectValue> zonalOfficeContainer;
	private BeanItemContainer<SelectValue> periodContainer;
	private BeanItemContainer<SelectValue> branchOfficeContainer;
	
//	@Inject
//	private BranchManagerFeedBackReportTable searchResultTable;
	
	@Inject
	private BranchManagerFeedBackReportBranchTable branchDetailsTable;
	
	@Inject
	private RevisedBMFeedBackReportTable exportBranchTable;
			
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	public void init(BeanItemContainer<SelectValue> feedBackTypeContainer, BeanItemContainer<SelectValue> zonalOfficeContainer, BeanItemContainer<SelectValue> periodContainer/*, BeanItemContainer<SelectValue> branchOfficeContainer*/)
	{
		initBinder();
		this.feedBackTypeContainer = feedBackTypeContainer; 
		this.zonalOfficeContainer = zonalOfficeContainer;
		this.periodContainer = periodContainer;
//		this.branchOfficeContainer = branchOfficeContainer;
		
		searchPanel = new Panel("Branch Manager Feedback Report"); 
		searchPanel.addStyleName("panelHeader");
		searchPanel.addStyleName("g-search-panel");
		
		wholeVerticalLayout = new VerticalLayout(searchPanel,buildSearchPanel());
		branchDetailsTable.initView();
		wholeVerticalLayout.addComponent(branchDetailsTable);
		wholeVerticalLayout.setComponentAlignment(branchDetailsTable, Alignment.BOTTOM_CENTER);
		
		setSizeFull();
		setCompositionRoot(wholeVerticalLayout);
	}

	public VerticalLayout getContent(){
		wholeVerticalLayout.addComponents(searchPanel,buildSearchPanel());
		branchDetailsTable.initView();
		wholeVerticalLayout.addComponent(branchDetailsTable);
		wholeVerticalLayout.setComponentAlignment(branchDetailsTable, Alignment.BOTTOM_CENTER);
		return wholeVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<BranchManagerFeedBackReportDto>(BranchManagerFeedBackReportDto.class);
		
		this.bean = new BranchManagerFeedBackReportDto();
		this.binder.setItemDataSource(this.bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	public void setDropDownValues() 
	{
		
		this.feedBackTypeContainer = feedBackTypeContainer; 
		this.zonalOfficeContainer = zonalOfficeContainer;
		this.periodContainer = periodContainer;
		
//		feedBackTypeCmb.setContainerDataSource(feedBackTypeContainer);
//		feedBackTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		feedBackTypeCmb.setItemCaptionPropertyId("value");
		
		zonalOfficeCmb.setContainerDataSource(zonalOfficeContainer);
		zonalOfficeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		zonalOfficeCmb.setItemCaptionPropertyId("value");

		periodCmb.setContainerDataSource(periodContainer);
		periodCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		periodCmb.setItemCaptionPropertyId("value");
		
	}		
	
	private VerticalLayout buildSearchPanel(){
		searchVerticalLayout = buildSearchLayout();
		return searchVerticalLayout;
	}
	
	private VerticalLayout buildSearchLayout(){		
		
		 searchVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() searchVerticalLayout.setImmediate(false);
		 searchVerticalLayout.setWidth("100.0%");

		 searchVerticalLayout.addStyleName("g-search-panel");
		 
		 
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("40px");
		 
		 dummyLabel =new Label();
		 dummyLabel.setWidth("30px");
		 
		 buttonLayout = new HorizontalLayout();
		 
		 
//		 feedBackTypeCmb = binder.buildAndBind("Feedback", "feedbackSelect", ComboBox.class);
			
		 zonalOfficeCmb= binder.buildAndBind("Zone", "zonalOfficeSelect", ComboBox.class);
		 
			
		 periodCmb= binder.buildAndBind("Period", "periodSelect", ComboBox.class);
			
		 branchOfficeCmb= binder.buildAndBind("Branch","branchOfficeSelect", ComboBox.class);
		 
		 setDropDownValues();

		 zonalOfficeCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox zoneCmb = (ComboBox)event.getProperty();
				
				if(zoneCmb.getValue() != null){
					Long zoneCode = ((SelectValue)zoneCmb.getValue()).getId(); 
					fireViewEvent(BranchManagerFeedBackReportPresenter.GET_BRANCH_DETAILS, zoneCode);
				}	
				
			}
		});
		
		Label dummyLabel1 =new Label();
		dummyLabel1.setSizeFull();
		Label dummyLabel2 =new Label();
		dummyLabel2.setSizeFull();
		FormLayout leftForm = new FormLayout(/*feedBackTypeCmb,*/zonalOfficeCmb/*,branchOfficeCmb*/);
		leftForm.setHeight("100px");
		FormLayout middleForm = new FormLayout(branchOfficeCmb);
		middleForm.setHeight("100px");
		FormLayout rightForm = new FormLayout(periodCmb);
		rightForm.setHeight("100px");
		HorizontalLayout frmLayout = new HorizontalLayout(leftForm, middleForm, rightForm);

		frmLayout.setSpacing(true);
		
		this.btnSearch = new Button("Search");
		btnSearch.setWidth("-1px");
		btnSearch.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSearch.setDisableOnClick(true);
		btnSearch.addStyleName("hover");
		//Vaadin8-setImmediate() btnSearch.setImmediate(true);

		this.btnReset = new Button("Reset");
		btnReset.setWidth("-1px");
		btnReset.addStyleName(ValoTheme.BUTTON_DANGER);
		//Vaadin8-setImmediate() btnReset.setImmediate(true);
		
		Label dummyLabel3 =new Label();
		dummyLabel3.setWidth("30px");
		
		buttonLayout.addComponents(btnSearch,dummyLabel3,btnReset);
		buttonLayout.setSpacing(true);

		addListener();
		
		searchVerticalLayout.addComponents(frmLayout);
		absoluteLayout_3.addComponent(buttonLayout, "top:0.0px;left:200.0px;");
		
		searchVerticalLayout.addComponent(absoluteLayout_3);
//		searchVerticalLayout.setMargin(true);
		searchVerticalLayout.setSpacing(true);
		
//		 mandatoryFields.add(feedBackTypeCmb);
		 mandatoryFields.add(zonalOfficeCmb);
		 showOrHideValidation(false);
		
		return searchVerticalLayout;
	}
	
	private void addListener(){
		btnSearch.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validateSearchBean()){
					btnSearch.setEnabled(false);
					bean.setSearchType(SHAConstants.BM_FB_BRANCH_ACTUAL_TYPE);
					fireViewEvent(BranchManagerFeedBackReportPresenter.SEARCH_BM_FB,bean);	
				}				
			}
		});
		
		btnReset.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(BranchManagerFeedBackReportPresenter.RESET_BM_FB_REPORT,null);
			}
		});
		
		
	}
	
	public void showResultTable(List<BranchManagerFeedBackReportDto> resultListDto){
		
		btnSearch.setEnabled(true);
		if(resultListDto != null && !resultListDto.isEmpty()){
			
			if(branchDetailsTable != null){
				wholeVerticalLayout.removeComponent(branchDetailsTable);
			}
			
			branchDetailsTable.initView();
			branchDetailsTable.setTableList(resultListDto);
			wholeVerticalLayout.addComponent(branchDetailsTable);
				
			if(exportBtn == null){
				addExportButton();
			}
		}
		else{
			if(exportBtn != null){
				buttonLayout.removeComponent(exportBtn);
				buttonLayout.removeComponent(dummyLabel);
				exportBtn=null;
			}
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Branch Manager Feedback Report Home");
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
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					resetSearchForm();
					fireViewEvent(MenuItemBean.BRANCH_MANAGER_FEEDBACK_REPORT, null);					
				}
			});
		}
		
	}
	
	public void showBranchDetailsResultTable(List<BranchManagerFeedBackReportDto> branchDetailsListDto){
		
//		if(branchDetailsListDto != null && !branchDetailsListDto.isEmpty()){
//			
//			branchDetailsTable.init("", false, false);
//			branchDetailsTable.setReportColHeader();
//			branchDetailsTable.setTableList(branchDetailsListDto);
//			if(branchDetailsTable != null){
//				wholeVerticalLayout.removeComponent(branchDetailsTable.getTable());
//			}
//			wholeVerticalLayout.addComponent(branchDetailsTable.getTable());
//			wholeVerticalLayout.setComponentAlignment(branchDetailsTable.getTable(), Alignment.BOTTOM_CENTER);
//		}
//		else{
//			
//		}
		
	}

	public void loadBranchDropDown(BeanItemContainer<SelectValue> branchContainer){
		branchOfficeContainer = branchContainer;
		branchOfficeCmb.setContainerDataSource(branchOfficeContainer);
		branchOfficeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		branchOfficeCmb.setItemCaptionPropertyId("value");	
		
	}
	
	public void resetSearchForm(){
		if(btnSearch != null){
			btnSearch.setEnabled(true);
		}
		/*if(feedBackTypeCmb != null){
			this.feedBackTypeCmb.setValue(null);
		}*/
		if(zonalOfficeCmb != null){
			this.zonalOfficeCmb.setValue(null);
		}
		if(periodCmb != null){
			this.periodCmb.setValue(null);
		}
		if(branchOfficeCmb != null){
			this.branchOfficeCmb.setValue(null);
		}
		
		if(exportBtn != null){
			buttonLayout.removeComponent(exportBtn);
			buttonLayout.removeComponent(dummyLabel);
			exportBtn = null;
		}
//		if(searchResultTable != null){
//			searchResultTable.removeRow();
//		}
		
		if(branchDetailsTable != null){
			branchDetailsTable.setTableList(new ArrayList<BranchManagerFeedBackReportDto>());
			wholeVerticalLayout.removeComponent(branchDetailsTable);
		}
		
		
	}
	public BranchManagerFeedBackReportBranchTable getSearchResultTable(){
	
		return branchDetailsTable;
	}
	private boolean validateSearchBean(){
		try{
			boolean hasError = false;
			StringBuffer errMsg = new StringBuffer("");
			if(!binder.isValid()){
				
				
				/*if(feedBackTypeCmb.getValue() == null){
					hasError = true;
					errMsg.append("Please Select a value For Feedback <br>");
				}*/
				if(zonalOfficeCmb.getValue() == null){
					hasError = true;
					errMsg.append("Please Select a value For Zone <br>");
				}
//				if(periodCmb.getValue() == null){
//					hasError = true;
//					errMsg.append("Please Select a value For Period <br>");
//				}
//				if(branchOfficeCmb.getValue() == null){
//					hasError = true;
//					errMsg.append("Please Select a value For Branch <br>");
//				}
								
				if(hasError && errMsg.length()>0){
					showErrorPopup(errMsg.toString());
					return false;
				}
				return false;
			}else{
				
				binder.commit();
				/*if(feedBackTypeCmb.getValue() == null){
					hasError = true;
					errMsg.append("Please Select a value For Feedback <br>");
				}*/
				if(zonalOfficeCmb.getValue() == null){
					hasError = true;
					errMsg.append("Please Select a value For Zone <br>");
				}
				if(hasError && errMsg.length()>0){
					showErrorPopup(errMsg.toString());
					return false;
				}
				return true;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
	return false;
	}
	
	private void showErrorPopup(String eMsg){
		
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
		btnSearch.setEnabled(true);
		
	}
	
	private void addExportButton() {
		exportBtn = new Button("Export to Excel");
		exportBtn.addClickListener(new Button.ClickListener() {
				
			@Override
			public void buttonClick(ClickEvent event) {
				
				exportBranchTable.init("", false, false);
				exportBranchTable.setReportColHeader();
				exportBranchTable.setTableList(branchDetailsTable.getTableList());
				exportBranchTable.setVisible(false);
				wholeVerticalLayout.addComponent(exportBranchTable);
				excelExport = new ExcelExport(exportBranchTable.getTable());
				excelExport.setReportTitle("Branch Manager's Feedback Report");
				excelExport.setDisplayTotals(false);
				excelExport.export();
				
/*				searchResultTable.setExportColHeader();
				excelExport = new ExcelExport(searchResultTable.getTable());
				excelExport.setReportTitle("Branch Manager's Feedback Report");
				excelExport.setDisplayTotals(false);
				excelExport.export();
				searchResultTable.generateDynamicColums();*/
			}
		});
		buttonLayout.addComponent(dummyLabel);
		buttonLayout.addComponent(exportBtn);
		
	}	
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);

		}
	}
}
