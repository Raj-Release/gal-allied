package com.shaic.claim.reports.executivesummaryreqort;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
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
public class ExecutiveStatusSummaryReport extends ViewComponent  {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private PopupDateField fromDate;
	private PopupDateField toDate;
	private ComboBox employeeNameCmb;
	private ComboBox typeCmb;
	private ComboBox empCpuCmb;
	private Button btnSearch;
	private Button btnReset;
	private HorizontalLayout buttonLayout;
	private Button exportBtn; 
	private ExcelExport excelExport;
	private Label dummyLabel;
	private BeanFieldGroup<ExecutiveStatusSummarySearchDto> binder;
	private ExecutiveStatusSummarySearchDto bean;
	@Inject
	private ExecutiveStatusSummaryReportTable searchResultTable;
	
	@PostConstruct
	public void init()
	{
		initBinder();
		wholeVerticalLayout = new VerticalLayout();
		searchPanel = buildSearchPanel();
		wholeVerticalLayout.addComponent(searchPanel);
		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		searchResultTable.init("", false, false);
		setCompositionRoot(wholeVerticalLayout);
	}

	private void initBinder()
	{
		this.binder = new BeanFieldGroup<ExecutiveStatusSummarySearchDto>(ExecutiveStatusSummarySearchDto.class);
		
		this.bean = new ExecutiveStatusSummarySearchDto();
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
		
		employeeNameCmb.setContainerDataSource(empListContainer);
		employeeNameCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		employeeNameCmb.setItemCaptionPropertyId("value");

	}	
	
	
	
	private Panel buildSearchPanel(){
		searchVerticalLayout = buildSearchLayout();
		searchPanel = new Panel("Executive Status Report (Summary)");
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
		 absoluteLayout_3.setHeight("170px");
		 
		 dummyLabel =new Label();
		 dummyLabel.setWidth("30px");
		 
		 buttonLayout = new HorizontalLayout();

		fromDate = binder.buildAndBind("From Date", "fromDate", PopupDateField.class);
		fromDate.setDateFormat("dd/MM/yyyy");
		fromDate.setRangeEnd(new Date());
		
		toDate = binder.buildAndBind("To Date", "toDate", PopupDateField.class);
		toDate.setDateFormat("dd/MM/yyyy");
		
		typeCmb = new ComboBox("Type");
		typeCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(typeCmb.getValue() != null){
					String empType = String.valueOf(typeCmb.getValue());
					fireViewEvent(ExecutiveStatusSummaryReportPresenter.GET_EMP_TYPE_FILTER_SUMMARY, empType);
				}				
			}
		});
		
		empCpuCmb = new ComboBox("CPU Code");
		
		empCpuCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(empCpuCmb.getValue() != null){
					String cpuCode = String.valueOf(empCpuCmb.getValue());
					fireViewEvent(ExecutiveStatusSummaryReportPresenter.GET_CPU_FILTER_EMP_SUMMARY, cpuCode);
				}				
			}
		});
		
		Label dummyLabel1 =new Label();
		dummyLabel1.setSizeFull();
		Label dummyLabel2 =new Label();
		dummyLabel2.setSizeFull();
		employeeNameCmb = binder.buildAndBind("Employee", "empName", ComboBox.class);
		FormLayout leftForm = new FormLayout(typeCmb,employeeNameCmb,fromDate);
		FormLayout rightForm = new FormLayout(empCpuCmb,dummyLabel2,toDate);
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
		
		Label dummyLabel3 =new Label();
		dummyLabel3.setWidth("30px");
		
		buttonLayout.addComponents(btnSearch,dummyLabel3,btnReset);
		buttonLayout.setSpacing(true);

		addListener();
		
		absoluteLayout_3.addComponent(frmLayout);
		absoluteLayout_3.addComponent(buttonLayout, "top:140.0px;left:200.0px;");
		
		searchVerticalLayout.addComponent(absoluteLayout_3);
		
		return searchVerticalLayout;
	}
	
	private void addListener(){
		btnSearch.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validateSearchBean()){
					btnSearch.setEnabled(false);
					if(typeCmb.getValue() != null){
						SelectValue empType = (SelectValue)typeCmb.getValue();
						bean.setEmpType(empType);						
					}
					String userName = (String) getUI().getSession().getAttribute(
							BPMClientContext.USERID);
					fireViewEvent(ExecutiveStatusSummaryReportPresenter.SEARCH_EXECUTIVE_STATUS_SUMMARY,bean,userName);	
				}				
			}
		});
		
		btnReset.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(ExecutiveStatusSummaryReportPresenter.RESET_EXECUTIVE_STATUS_SUMMARY,null);
			}
		});
		
		
	}
	
	public void showResultTable(List<ExecutiveStatusSummaryReportDto> resultListDto){
		
		btnSearch.setEnabled(true);
		if(resultListDto != null && !resultListDto.isEmpty()){
		
		if(bean.getEmpType() != null && bean.getEmpType().getValue() != null && bean.getEmpType().getValue().equalsIgnoreCase(SHAConstants.CALLCENTRE_EMPLOYEE)){
			searchResultTable.setCallcenterTableHeader();

		}
		
		searchResultTable.setTableList(resultListDto);
		
		searchVerticalLayout.addComponent(searchResultTable);
		
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
					fireViewEvent(MenuItemBean.EXECUTIVE_STATUS_SUMMARY_REPORT, null);
					
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
			buttonLayout.removeComponent(exportBtn);
			buttonLayout.removeComponent(dummyLabel);
			exportBtn = null;
		}
		searchResultTable.removeRow();
		
	}
	public ExecutiveStatusSummaryReportTable getSearchResultTable(){
	
		return searchResultTable;
	}
	private boolean validateSearchBean(){
		try{
			if(!binder.isValid()){
				
				if(fromDate.getValue() == null){
					showErrorPopup("Pleae Enter Valid From Date");
				}
				if(toDate.getValue() == null){
					showErrorPopup("Pleae Enter Valid To Date");
				}
				
				if(toDate.getValue() != null && fromDate.getValue() != null){
					if(toDate.getValue().compareTo(fromDate.getValue()) < 0){
						showErrorPopup("From Date Should Not be Less Than To Date");
						return false;
					}
				}
				
				if(employeeNameCmb.getValue() == null){
					showErrorPopup("Please Select Employee Name");
				}
				
				return false;	
			}else{
				binder.commit();
				this.bean = binder.getItemDataSource().getBean();
				if(null != bean.getFromDate() && null != bean.getToDate() ){
					
						if(bean.getToDate().compareTo(bean.getFromDate())<0){
							showErrorPopup("From Date should not be greater than To Date");
						return false;
						}
						return true;
					}	
				
				else{
				
				if(null == bean.getFromDate()){
					showErrorPopup("Please Select From Date");
					return false;
				}
				if(null == bean.getToDate()){
					showErrorPopup("Please Select To Date");
					return false;
				}
				
				}
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
				excelExport = new ExcelExport(searchResultTable.getTable());
				excelExport.setReportTitle("Executive Status Summary Report");
				excelExport.setDisplayTotals(false);
				excelExport.export();
			}
		});
		buttonLayout.addComponent(dummyLabel);
		buttonLayout.addComponent(exportBtn);
		
	}
	
	public void setCPUBasedEmpList(BeanItemContainer<SelectValue> empListContainer){
		
		employeeNameCmb.setContainerDataSource(empListContainer);
	}
	
}
