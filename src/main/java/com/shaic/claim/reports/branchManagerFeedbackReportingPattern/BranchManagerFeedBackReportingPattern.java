package com.shaic.claim.reports.branchManagerFeedbackReportingPattern;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
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
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * AS Part of CR  R1238
 * @author Lakshminarayana
 *
 */

@SuppressWarnings("serial")
@UIScoped
public class BranchManagerFeedBackReportingPattern extends ViewComponent  {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private ComboBox feedBackTypeCmb;
	private ComboBox zonalOfficeCmb;
	private ComboBox yearMonthCmb;
	private ComboBox branchOfficeCmb;	

	private Button btnSearch;
	private Button btnReset;
	private HorizontalLayout buttonLayout;
	private Button exportBtn; 
	private ExcelExport excelExport;
	private Label dummyLabel;
	private BeanFieldGroup<BranchManagerFeedBackReportingPatternDto> binder;
	private BranchManagerFeedBackReportingPatternDto bean;
	
	private BeanItemContainer<SelectValue> feedBackTypeContainer;
	private BeanItemContainer<SelectValue> zonalOfficeContainer;
	private BeanItemContainer<SelectValue> yearMonthContainer;
	private BeanItemContainer<SelectValue> branchOfficeContainer;
	
	@Inject
	private BranchManagerFeedBackReportingPatternTable searchResultTable;
	
	@Inject
	private BMFBRptPatternExcelTable excelResultTable;
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	public void init(BeanItemContainer<SelectValue> feedBackTypeContainer, BeanItemContainer<SelectValue> zonalOfficeContainer, BeanItemContainer<SelectValue> yearMonthContainer)
	{
		initBinder();
		this.feedBackTypeContainer = feedBackTypeContainer; 
		this.zonalOfficeContainer = zonalOfficeContainer;
		this.yearMonthContainer = yearMonthContainer;
		
		searchPanel = new Panel("Branch Manager Feedback - Reporting Pattern"); 
		searchPanel.addStyleName("panelHeader");
		searchPanel.addStyleName("g-search-panel");
		
		wholeVerticalLayout = new VerticalLayout(searchPanel,buildSearchPanel());
		searchResultTable.init("", false, false);
		searchResultTable.getTable().setCaption(bean.getPeriodSelect() != null ? bean.getPeriodSelect().getValue(): "");
		wholeVerticalLayout.addComponent(searchResultTable.getTable());
		wholeVerticalLayout.setComponentAlignment(searchResultTable.getTable(), Alignment.BOTTOM_CENTER);
		setSizeFull();
		setCompositionRoot(wholeVerticalLayout);
	}

	public VerticalLayout getContent(){
		wholeVerticalLayout.addComponents(searchPanel,buildSearchPanel());
		searchResultTable.init("", false, false);
		searchResultTable.getTable().setCaption(bean.getPeriodSelect() != null ? bean.getPeriodSelect().getValue(): "");
		wholeVerticalLayout.addComponent(searchResultTable.getTable());
		wholeVerticalLayout.setComponentAlignment(searchResultTable.getTable(), Alignment.BOTTOM_CENTER);
		return wholeVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<BranchManagerFeedBackReportingPatternDto>(BranchManagerFeedBackReportingPatternDto.class);
		
		this.bean = new BranchManagerFeedBackReportingPatternDto();
		this.binder.setItemDataSource(this.bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	public void setDropDownValues() 
	{
		
		this.feedBackTypeContainer = feedBackTypeContainer; 
		this.zonalOfficeContainer = zonalOfficeContainer;
		this.yearMonthContainer = yearMonthContainer;
		
		feedBackTypeCmb.setContainerDataSource(feedBackTypeContainer);
		feedBackTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		feedBackTypeCmb.setItemCaptionPropertyId("value");
		
		zonalOfficeCmb.setContainerDataSource(zonalOfficeContainer);
		zonalOfficeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		zonalOfficeCmb.setItemCaptionPropertyId("value");

		yearMonthCmb.setContainerDataSource(yearMonthContainer);
		yearMonthCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		yearMonthCmb.setItemCaptionPropertyId("value");
				
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
		 
		 
		 feedBackTypeCmb = binder.buildAndBind("Feedback", "feedbackSelect", ComboBox.class);
			
		 zonalOfficeCmb= binder.buildAndBind("Zone", "zonalOfficeSelect", ComboBox.class);
			
		 zonalOfficeCmb.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					ComboBox zoneCmb = (ComboBox)event.getProperty();
					
					if(zoneCmb.getValue() != null){
						Long zoneCode = ((SelectValue)zoneCmb.getValue()).getId(); 
						fireViewEvent(BranchManagerFeedBackReportingPatternPresenter.GET_BM_FB_PATTERN_BRANCH_DETAILS, zoneCode);
					}	
					
				}
			});
		 
		 yearMonthCmb= binder.buildAndBind("Year & Month", "periodSelect", ComboBox.class);
			
		 branchOfficeCmb= binder.buildAndBind("Branch","branchOfficeSelect", ComboBox.class);
		 
		 setDropDownValues();
		
		Label dummyLabel1 =new Label();
		dummyLabel1.setSizeFull();
		Label dummyLabel2 =new Label();
		dummyLabel2.setSizeFull();
		FormLayout leftForm = new FormLayout(feedBackTypeCmb,zonalOfficeCmb);
		leftForm.setHeight("100px");
		FormLayout rightForm = new FormLayout(yearMonthCmb, branchOfficeCmb);
		rightForm.setHeight("100px");
		HorizontalLayout frmLayout = new HorizontalLayout(leftForm,rightForm);

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
//		absoluteLayout_3.addComponent(frmLayout);
		absoluteLayout_3.addComponent(buttonLayout, "top:0.0px;left:200.0px;");
		
		searchVerticalLayout.addComponent(absoluteLayout_3);
//		searchVerticalLayout.setMargin(true);
		searchVerticalLayout.setSpacing(true);
		
		 mandatoryFields.add(feedBackTypeCmb);
		 mandatoryFields.add(zonalOfficeCmb);
		 mandatoryFields.add(yearMonthCmb);		 
		 showOrHideValidation(false);
		
		return searchVerticalLayout;
	}
	
	private void addListener(){
		btnSearch.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validateSearchBean()){
					btnSearch.setEnabled(false);
					fireViewEvent(BranchManagerFeedBackReportingPatternPresenter.SEARCH_BM_FB_PATTERN,bean);	
				}				
			}
		});
		
		btnReset.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(BranchManagerFeedBackReportingPatternPresenter.RESET_BM_FB_REPORT_PATTERN,null);
			}
		});
		
		
	}
	
	public void showResultTable(List<BranchManagerFeedBackReportingPatternDto> resultListDto){
		
		btnSearch.setEnabled(true);
		if(resultListDto != null && !resultListDto.isEmpty()){
			searchResultTable.getTable().setCaption(bean.getPeriodSelect() != null ? bean.getPeriodSelect().getValue(): "");
			searchResultTable.setReportColHeader();
			searchResultTable.setTableList(resultListDto);
			searchResultTable.loadImageColumns();
			List<String> notAppCol = new ArrayList<String>();
			
			if(resultListDto.get(0) != null && resultListDto.get(0).getD29() != null && resultListDto.get(0).getD29().intValue() == -3) {
				notAppCol.add("d29");
			}
			if(resultListDto.get(0) != null && resultListDto.get(0).getD30() != null && resultListDto.get(0).getD30().intValue() == -3) {
				notAppCol.add("d30");
			}
			if(resultListDto.get(0) != null && resultListDto.get(0).getD31() != null && resultListDto.get(0).getD31().intValue() == -3) {
				notAppCol.add("d31");
			}
			searchResultTable.removeNAColumns(notAppCol);
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
			Button homeButton = new Button("Branch Manager Feedback - Reporting Pattern Home");
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
					fireViewEvent(MenuItemBean.BRANCH_MANAGER_FEEDBACK_REPORTING_PATTERN, null);					
				}
			});
		}
		
	}
	
	public void resetSearchForm(){
		if(btnSearch != null){
			btnSearch.setEnabled(true);
		}
		if(feedBackTypeCmb != null){
			this.feedBackTypeCmb.setValue(null);
		}
		if(zonalOfficeCmb != null){
			this.zonalOfficeCmb.setValue(null);
		}
		if(yearMonthCmb != null){
			this.yearMonthCmb.setValue(null);
		}
		if(branchOfficeCmb != null){
			this.branchOfficeCmb.setValue(null);
		}
		
		if(exportBtn != null){
			buttonLayout.removeComponent(exportBtn);
			buttonLayout.removeComponent(dummyLabel);
			exportBtn = null;
		}
		if(searchResultTable != null){
			searchResultTable.removeRow();
			searchResultTable.getTable().setCaption("");
		}
		
	}
	public BranchManagerFeedBackReportingPatternTable getSearchResultTable(){
	
		return searchResultTable;
	}
	private boolean validateSearchBean(){
		try{
			boolean hasError = false;
			StringBuffer errMsg = new StringBuffer("");
			if(!binder.isValid()){
				
				
				if(feedBackTypeCmb.getValue() == null){
					hasError = true;
					errMsg.append("Please Select a value For Feedback <br>");
				}
				if(zonalOfficeCmb.getValue() == null){
					hasError = true;
					errMsg.append("Please Select a value For Zone <br>");
				}
				if(yearMonthCmb.getValue() == null){
					hasError = true;
					errMsg.append("Please Select a value For Year & Month <br>");
				}
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
				if(feedBackTypeCmb.getValue() == null){
					hasError = true;
					errMsg.append("Please Select a value For Feedback <br>");
				}
				if(zonalOfficeCmb.getValue() == null){
					hasError = true;
					errMsg.append("Please Select a value For Zone <br>");
				}
				if(yearMonthCmb.getValue() == null){
					hasError = true;
					errMsg.append("Please Select a value For Year & Month <br>");
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
				
				 List<BranchManagerFeedBackReportingPatternDto> tableList = searchResultTable.getTableList();
				getExportDetails(tableList);
			}	
		});
		buttonLayout.addComponent(dummyLabel);
		buttonLayout.addComponent(exportBtn);
		
	}	
	
	public void getExportDetails(List<BranchManagerFeedBackReportingPatternDto> tableList){
		if(excelResultTable != null)
		{
			wholeVerticalLayout.removeComponent(excelResultTable.getTable());
		}		
		excelResultTable.init("", false, false);
		wholeVerticalLayout.addComponent(excelResultTable.getTable());
		excelResultTable.getTable().setVisible(false);
		List<BranchManagerFeedBackReportingPatternDto> excelList = new ArrayList<BranchManagerFeedBackReportingPatternDto>();
		List<String> notAppCols = new ArrayList<String>();
		if(tableList != null && !tableList.isEmpty()){
			BranchManagerFeedBackReportingPatternDto excelDto = null;
			for (BranchManagerFeedBackReportingPatternDto branchManagerFeedBackReportingPatternDto : tableList) {
				excelDto = new BranchManagerFeedBackReportingPatternDto();
				excelDto.setBranchCode(branchManagerFeedBackReportingPatternDto.getBranchCode());
				excelDto.setBranchName(branchManagerFeedBackReportingPatternDto.getBranchName());
				excelDto.setD1Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD1()));
				excelDto.setD2Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD2()));
				excelDto.setD3Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD3()));
				excelDto.setD4Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD4()));
				excelDto.setD5Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD5()));
				excelDto.setD6Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD6()));
				excelDto.setD7Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD7()));
				excelDto.setD8Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD8()));
				excelDto.setD9Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD9()));
				excelDto.setD10Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD10()));
				excelDto.setD11Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD11()));
				excelDto.setD12Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD12()));
				excelDto.setD13Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD13()));
				excelDto.setD14Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD14()));
				excelDto.setD15Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD15()));
				excelDto.setD16Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD16()));
				excelDto.setD17Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD17()));
				excelDto.setD18Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD18()));
				excelDto.setD19Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD19()));
				excelDto.setD20Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD20()));
				excelDto.setD21Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD21()));
				excelDto.setD22Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD22()));
				excelDto.setD23Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD23()));
				excelDto.setD24Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD24()));
				excelDto.setD25Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD25()));
				excelDto.setD26Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD26()));
				excelDto.setD27Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD27()));
				excelDto.setD28Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD28()));
				excelDto.setD29Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD29()));
				excelDto.setD30Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD30()));
				excelDto.setD31Value(getDayValue(branchManagerFeedBackReportingPatternDto.getD31()));
				excelList.add(excelDto);
				excelDto = null;
			}
		}
		if(excelList.get(0).getD29() != null && excelList.get(0).getD29().intValue() == -3){
			excelResultTable.setReportColHeader(28);
		}
		else if((excelList.get(0).getD29() != null && excelList.get(0).getD29().intValue() >= -2) 
				&& excelList.get(0).getD30() != null && excelList.get(0).getD30().intValue() == -3){
			excelResultTable.setReportColHeader(29);
		}
		else if(excelList.get(0).getD31() != null && excelList.get(0).getD31().intValue() == -3)
			excelResultTable.setReportColHeader(30);
		else if(excelList.get(0).getD31() != null && excelList.get(0).getD31().intValue() >= -2)
			excelResultTable.setReportColHeader(31);
		
		excelResultTable.setTableList(excelList);
		excelExport = new ExcelExport(excelResultTable.getTable());
		excelExport.setReportTitle(("Branch Manager Feedback - Reporting Pattern" + (this.bean.getPeriodSelect() != null ? (" - " + this.bean.getPeriodSelect().getValue()) : "")));
		excelExport.setDisplayTotals(false);
		excelExport.export();
	
	}
	
	public void loadBranchDropDown(BeanItemContainer<SelectValue> branchContainer){
		branchOfficeContainer = branchContainer;
		branchOfficeCmb.setContainerDataSource(branchOfficeContainer);
		branchOfficeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		branchOfficeCmb.setItemCaptionPropertyId("value");	
		
	}
	
	public String getDayValue(Integer reportedValue){
		if(reportedValue != null && reportedValue.intValue() > 0)
			return "Y";
		else if(reportedValue != null && (reportedValue.intValue() == -1 || reportedValue.intValue() == -2))
			return "";
		else if(reportedValue == null ||(reportedValue != null && reportedValue.intValue() == 0))
			return "N";
		
		return "";
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			//field.setValidationVisible(isVisible);
		}
	}
}
