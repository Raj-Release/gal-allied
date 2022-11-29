package com.shaic.claim.reports.ExecutiveStatusReport;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.ims.bpm.claim.BPMClientContext;
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
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScoped
public class ExecutiveStatusDetailReport extends ViewComponent  {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private PopupDateField fromDate;
	private PopupDateField toDate;
	private ComboBox employeeNameCmb;
	private ComboBox empCpuCmb;
	private ComboBox typeCmb;
	private EmpSearchDto bean;
	private Button btnSearch;
	private Button btnReset;
	private Button exportBtn;
	private Label dummyLbl;
	private Label dummyLabel2;
	private ExcelExport excelExport;
	private HorizontalLayout buttonLayout;
	private BeanFieldGroup<EmpSearchDto> binder;
	
	private BeanItemContainer<SelectValue> empCodeListContainer;
	
	@Inject
	private ExecutiveStatusReportTable 	searchResultTable;
	
	@PostConstruct
	public void init()
	{
		initBinder();
		wholeVerticalLayout = new VerticalLayout();
		wholeVerticalLayout.addComponent(buildSearchPanel());
		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		searchResultTable.init("", false, false);
		setCompositionRoot(wholeVerticalLayout);
	}

	private void initBinder()
	{
		this.binder = new BeanFieldGroup<EmpSearchDto>(EmpSearchDto.class);
		this.bean = new EmpSearchDto();
		this.binder.setItemDataSource(this.bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setDropDownValues(BeanItemContainer<SelectValue> empCPUContainer,BeanItemContainer<SelectValue> empTypeContainer,BeanItemContainer<SelectValue> empListContainer) 
	{
		
		typeCmb.setContainerDataSource(empTypeContainer);
		typeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		typeCmb.setItemCaptionPropertyId("value");		
		
		empCpuCmb.setContainerDataSource(empCPUContainer);
		empCpuCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		empCpuCmb.setItemCaptionPropertyId("value");		
		
		empCodeListContainer = empListContainer;
		employeeNameCmb.setContainerDataSource(empCodeListContainer);
		employeeNameCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		employeeNameCmb.setItemCaptionPropertyId("value");

	}	
	
	
	
	private Panel buildSearchPanel(){
		searchVerticalLayout = buildSearchLayout();
		searchPanel = new Panel("Executive Status Detail Report");
		searchPanel.addStyleName("panelHeader");
		searchPanel.addStyleName("g-search-panel");
		searchPanel.setContent(searchVerticalLayout);
		
		return searchPanel;
	}
	
	private VerticalLayout buildSearchLayout(){
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 searchVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() searchVerticalLayout.setImmediate(false);
		 searchVerticalLayout.setWidth("100.0%");
		 searchVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("180px");
		 
		 buttonLayout = new HorizontalLayout();

		 dummyLbl =new Label();
		 dummyLbl.setSizeFull();
		 dummyLabel2 = new Label();
		 dummyLabel2.setWidth("30px");
		 
		 
		 fromDate = binder.buildAndBind("From Date", "fromDate", PopupDateField.class);
		 fromDate.setDateFormat("dd/MM/yyyy");
		 fromDate.setRangeEnd(new Date());
		
		toDate = binder.buildAndBind("To Date", "toDate", PopupDateField.class);
		toDate.setDateFormat("dd/MM/yyyy");		
		
		Label dummyLabel3 =new Label();
		dummyLabel3.setWidth("30px");
		typeCmb = new ComboBox("Type");
		typeCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(typeCmb.getValue() != null){
					String empType = String.valueOf(typeCmb.getValue());
					fireViewEvent(ExecutiveStatusDetailReportPresenter.GET_EMP_TYPE_FILTER, empType);
				}				
			}
		}); 
		empCpuCmb = new ComboBox("CPU Code");
		
		empCpuCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(empCpuCmb.getValue() != null){
					String cpuCode = String.valueOf(empCpuCmb.getValue());
					fireViewEvent(ExecutiveStatusDetailReportPresenter.GET_CPU_FILTER_EMP, cpuCode);
				}				
			}
		});
		
		Label dummyLabel =new Label();
		dummyLabel.setSizeFull();
		employeeNameCmb = binder.buildAndBind("Employee", "empName", ComboBox.class);
		FormLayout leftForm = new FormLayout(typeCmb,employeeNameCmb,fromDate);
		FormLayout rightForm = new FormLayout(empCpuCmb,dummyLabel,toDate);
		HorizontalLayout frmLayout = new HorizontalLayout(leftForm,rightForm);
		frmLayout.setMargin(true);
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
		
		buttonLayout.addComponents(btnSearch,dummyLabel3,btnReset);
		buttonLayout.setSpacing(true);
		
		absoluteLayout_3.addComponent(frmLayout);
		absoluteLayout_3.addComponent(buttonLayout, "top:150.0px;left:200.0px;");
				
		addListener();
		
		searchVerticalLayout.addComponent(absoluteLayout_3);
		
		return searchVerticalLayout;
	}
	
	private void addListener(){
		btnSearch.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validateBean()){
					btnSearch.setEnabled(false);
					String userName = (String) getUI().getSession().getAttribute(
							BPMClientContext.USERID);
					fireViewEvent(ExecutiveStatusDetailReportPresenter.SEARCH_EXECUTIVE_STATUS, bean,userName);
					
				}
				
			}
		});
		
		
		btnReset.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(ExecutiveStatusDetailReportPresenter.RESET_EXECUTIVE_STATUS, null);
			}
		});
		
	}
	
	public void setCPUBasedEmpList(BeanItemContainer<SelectValue> empListContainer){
		
		employeeNameCmb.setContainerDataSource(empListContainer);
	}
	
	public void showResultTable(List<ExecutiveStatusDetailReportDto> resultListDto){
		btnSearch.setEnabled(true);
		if(resultListDto != null && !resultListDto.isEmpty()){
		
		searchResultTable.setTableList(resultListDto);
		searchVerticalLayout.addComponent(searchResultTable);
			if(exportBtn == null){
				addExportButton();
			}
		}
		else{
			
			btnSearch.setEnabled(true);
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Search Executive Status Report Home");
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
					resetSearchForm();
					fireViewEvent(MenuItemBean.EXECUTIVE_STATUS_REPORT, null);
					
				}
			});
		}
		
	}
	
	public void resetSearchForm(){
		btnSearch.setEnabled(true);
		this.employeeNameCmb.setValue(null);
		this.typeCmb.setValue(null);
		this.empCpuCmb.setValue(null);
		this.fromDate.setValue(null);
		this.toDate.setValue(null);
		if(exportBtn != null){
			buttonLayout.removeComponent(dummyLabel2);
			buttonLayout.removeComponent(exportBtn);
			exportBtn = null;
		}
		
		searchResultTable.removeRow();
		
	}
	public ExecutiveStatusReportTable getSearchResultTable(){
	
		return searchResultTable;
	}
	
	private boolean validateBean(){
		try{
			if(!binder.isValid()){
				
				if(binder.getItemDataSource().getBean().getFromDate() == null || binder.getItemDataSource().getBean().getFromDate().toString().equalsIgnoreCase("")){
					
					showErrorPopup("Please Enter Valid From Date");
					return false;
				}
				if(binder.getItemDataSource().getBean().getToDate() == null || binder.getItemDataSource().getBean().getToDate().toString().equalsIgnoreCase("")){
					showErrorPopup("Please Enter Valid To Date");
					return false;
				}
				
				if(toDate.getValue() != null && fromDate.getValue() != null){
					if(toDate.getValue().compareTo(fromDate.getValue()) < 0){
						showErrorPopup("From Date Should Not be Less Than To Date");
						return false;
					}
				}
				
				if(binder.getItemDataSource().getBean().getEmpName().getValue() == null || binder.getItemDataSource().getBean().getEmpName().getValue().equalsIgnoreCase("")){
					showErrorPopup("Please Slect Employee Name");
					return false;
				}
				
				btnSearch.setEnabled(true);
			}else{
				binder.commit();
				this.bean = binder.getItemDataSource().getBean();
				
				if(this.bean.getEmpName() == null){
					showErrorPopup("Please Select Executive Name");
					return false;
				}
				
				if(this.bean.getFromDate() == null){
					showErrorPopup("Please Select From Date");
					return false;
				}
				
				if(this.bean.getToDate() == null){
					showErrorPopup("Please Select To Date");
					return false;
				}
				
				if(this.bean.getFromDate() != null && this.bean.getToDate() != null && this.bean.getFromDate().compareTo(this.bean.getToDate()) > 0 ){
					showErrorPopup("From date should not be greater than the To date"); 
					return false;
					
				}
				
				return true;
			}
			
		}catch(Exception e){
			btnSearch.setEnabled(true);
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	
	private void showErrorPopup(String errorMessage)
	{
		Label label = new Label(errorMessage, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
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
				excelExport = new ExcelExport(searchResultTable.getTable());
				excelExport.setReportTitle("Executive Status Detail Report");
				excelExport.setDisplayTotals(false);
				excelExport.export();
			}
		});
		buttonLayout.addComponent(dummyLabel2);
		buttonLayout.addComponent(exportBtn);
		
	}
	
}
