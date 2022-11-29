package com.shaic.claim.reports.cpuwisePreauthReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.UIScoped;
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
public class CPUwisePreauthReport extends ViewComponent {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private HorizontalLayout buttonLayout;
	private PopupDateField fromDate; 
	private ComboBox CPUCode;
	private PopupDateField toDate;
	private Button searchBtn;
	private Button resetBtn;
	private Button exportBtn;
	private Label dummyLabel;
	private ExcelExport excelExport;
	
	@Inject
	private CPUwisePreauthReportTable cpuPreauthTable;
	
	@PostConstruct
	public void init()
	{
		wholeVerticalLayout = new VerticalLayout();
		wholeVerticalLayout.addComponent(buildSearchPanel());
		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		cpuPreauthTable.init("", false, false);
		
		setCompositionRoot(wholeVerticalLayout);
	}
	
	public void setCPUDropDownValue(BeanItemContainer<SelectValue> cPUCodeContainer){
		CPUCode.setContainerDataSource(cPUCodeContainer);
		CPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		CPUCode.setItemCaptionPropertyId("value");
	}

	private Panel buildSearchPanel(){
		searchVerticalLayout = buildSearchLayout();
		searchPanel = new Panel("Cashless Issuance - CPU Wise Details");
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
		 absoluteLayout_3.setHeight("200px");
		 
		fromDate = new PopupDateField("From Date");
		fromDate.setDateFormat("dd/MM/yyyy");
		fromDate.setTextFieldEnabled(false);
		
		toDate = new PopupDateField("To Date");
		toDate.setDateFormat("dd/MM/yyyy");
		toDate.setTextFieldEnabled(false);
		
		CPUCode = new ComboBox("CPU Office");

		dummyLabel =new Label();
		dummyLabel.setWidth("30px");
		
		Label dummyLabel1 =new Label();
		dummyLabel1.setSizeFull();

		FormLayout leftForm = new FormLayout(CPUCode,fromDate);
		FormLayout rightForm = new FormLayout(dummyLabel1,toDate);
		HorizontalLayout frmLayout = new HorizontalLayout(leftForm,rightForm);
		frmLayout.setMargin(true);
		frmLayout.setSpacing(true);
		
		
	
		
		
		searchBtn = new Button("Search");
		searchBtn.setWidth("-1px");
		searchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		searchBtn.setDisableOnClick(true);
		searchBtn.addStyleName("hover");
		//Vaadin8-setImmediate() searchBtn.setImmediate(true);
		searchBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				searchBtn.setEnabled(false);
				Map<String,Object> filter = getSearchFileterValues(); 
				if(!filter.isEmpty()){
					fireViewEvent(CPUwisePreauthReportPresenter.SEARCH_CPU_WISE_PREAUTH, filter);
				}				
			}
		});
		
		resetBtn = new Button("Reset");
		resetBtn.setWidth("-1px");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		
		resetBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				resetAlltheValues();
			}
		});
		
		buttonLayout = new HorizontalLayout();

		Label dummyLabel2 =new Label();
		dummyLabel2.setWidth("30px");
		buttonLayout.addComponents(searchBtn,dummyLabel2,resetBtn);
			buttonLayout.setSpacing(true);

		absoluteLayout_3.addComponent(frmLayout);
		absoluteLayout_3.addComponent(buttonLayout, "top:130.0px;left:200.0px;");

		searchVerticalLayout.addComponent(absoluteLayout_3);
	
		
		return searchVerticalLayout;
	}
	
	private Map<String, Object>getSearchFileterValues(){
		WeakHashMap<String, Object> searchfilters = new WeakHashMap<String, Object>();
		Boolean hasError = false;
		StringBuffer errorMessage = new StringBuffer();
				
		if(fromDate.getValue() == null){
			errorMessage.append("<br>Please Select From Date");
			hasError = true;
		}
		
		if(toDate.getValue() == null){
			errorMessage.append("<br>Please Select To Date");
			hasError = true;
		}
		
		if(fromDate.getValue() != null && toDate.getValue() != null){
			if(fromDate.getValue().compareTo(toDate.getValue()) > 0){
				errorMessage.append("<br>From date should not be greater than the To date");
				hasError = true;
			}
			else{
				searchfilters.put("fromDate", fromDate.getValue());	
				searchfilters.put("toDate", toDate.getValue());	
			}
		}		
		
		if(CPUCode.getValue() != null){
			SelectValue cpuSelect = (SelectValue)CPUCode.getValue();
			searchfilters.put("cpuCode", cpuSelect);
		}
		if(searchfilters.isEmpty()){
			errorMessage.append("<br>Please Enter Atleast one field value for Search");
			hasError = true;
		}
		
		if(hasError){
			showErrorPopup(errorMessage.toString());
			searchfilters.clear();			
		}
		return searchfilters;
		
	}
	public void showTableResult(CPUWisePreauthResultDto resultDto){
		
		List<CPUwisePreauthReportDto> resultDtoList = ( resultDto != null ? resultDto.getCpuwisePreauthProcessedList() : null);
		searchBtn.setEnabled(true);
		if(resultDtoList != null && !resultDtoList.isEmpty()){
			
			cpuPreauthTable.setTableList(resultDtoList);			
			searchVerticalLayout.addComponent(cpuPreauthTable);
			
			if(exportBtn == null){
				addExportButton();
			}
		}
		else{
			searchBtn.setEnabled(true);
			if(exportBtn != null){
				buttonLayout.removeComponent(exportBtn);
				buttonLayout.removeComponent(dummyLabel);
				exportBtn = null;
			}
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Search CPU Wise Preauth Report Home");
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
					resetAlltheValues();
					fireViewEvent(MenuItemBean.SEARCH_PREAUTH_CPUWISE_REPORT, null);
					
				}
			});	
		}
		
	}
	public void resetAlltheValues(){
		fromDate.setValue(null);
		toDate.setValue(null);
		CPUCode.setValue(null);
		cpuPreauthTable.setTableList(new ArrayList<CPUwisePreauthReportDto>());
		searchBtn.setEnabled(true);
		if(exportBtn != null){
			buttonLayout.removeComponent(exportBtn);
			buttonLayout.removeComponent(dummyLabel);
			exportBtn = null;
		}
	}
	private void addExportButton() {
		exportBtn = new Button("Export to Excel");
		exportBtn.addClickListener(new Button.ClickListener() {
				
			@Override
			public void buttonClick(ClickEvent event) {
				excelExport = new ExcelExport(cpuPreauthTable.getTable());
				excelExport.setReportTitle("Cashless Issuance - CPU Wise Details");
				excelExport.setDisplayTotals(false);
				excelExport.export();
			}
		});
		buttonLayout.addComponent(dummyLabel);
		buttonLayout.addComponent(exportBtn);
	}
	
	public void showErrorPopup(String eMsg)
	{
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
	searchBtn.setEnabled(true);
	}
}
