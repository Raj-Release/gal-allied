package com.shaic.claim.reports.plannedAdmissionReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScoped
public class PlannedAdmissionReport extends ViewComponent {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private HorizontalLayout searchfieldLayout;
	private HorizontalLayout buttonLayout;
	private Button searchBtn;
	private Button resetBtn;
	private Button exportBtn;
	private PopupDateField fromDate; 
	private PopupDateField toDate;
	private Map<String,Object> filters;
	private Label dummyLabel;
	private ExcelExport excelExport;
	
	@Inject
	private PlannedAdmissionReportTable plannedIntimationDetailsTable;
	
	
	@PostConstruct
	public void init()
	{
		wholeVerticalLayout = new VerticalLayout();
		wholeVerticalLayout.setSizeFull();
		wholeVerticalLayout.addComponent(buildSearchPanel());
		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		plannedIntimationDetailsTable.init("", false, false);
		setSizeFull();
		setCompositionRoot(wholeVerticalLayout);
	}

	private Panel buildSearchPanel(){
		searchVerticalLayout = buildSearchLayout();
		searchPanel = new Panel("Planned Admission Report");
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
		 absoluteLayout_3.setHeight("160px");
		
		fromDate = new PopupDateField("From Date");
		fromDate.setDateFormat("dd/MM/yyyy");
		fromDate.setTextFieldEnabled(false);
		
		toDate = new PopupDateField("To Date");
		toDate.setDateFormat("dd/MM/yyyy");
		toDate.setTextFieldEnabled(false);
		
		FormLayout fromDtFrm = new FormLayout();
		fromDtFrm.addComponent(fromDate);
		FormLayout toDtFrm = new FormLayout();
		toDtFrm.addComponent(toDate);
		searchfieldLayout = new HorizontalLayout(fromDtFrm,toDtFrm);
		searchfieldLayout.setSpacing(true);
		searchfieldLayout.setMargin(true);
		
		dummyLabel = new Label();
		dummyLabel.setWidth("30px");
		searchBtn = new Button("Search");
		searchBtn.setWidth("-1px");
		searchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		searchBtn.setDisableOnClick(true);
		searchBtn.addStyleName("hover");
		//Vaadin8-setImmediate() searchBtn.setImmediate(true);
		searchBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				((Button)event.getSource()).setEnabled(true);
				
				if(validateFields())
					{
						fireViewEvent(PlannedAdmissionReportPresenter.SEARCH_PLANNED_ADMISSION_REPORT, filters);
					}
			}
		});
		
		
		resetBtn = new Button("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		
		resetBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				resetAllValues();

				if(exportBtn != null){
					buttonLayout.removeComponent(exportBtn);
					buttonLayout.removeComponent(dummyLabel);
					exportBtn = null;
				}
				
			}
		});
		buttonLayout = new HorizontalLayout();
		

		Label dummyLabel =new Label();
		dummyLabel.setWidth("30px");

		buttonLayout.addComponents(searchBtn,dummyLabel,resetBtn);
		buttonLayout.setSpacing(true);

		absoluteLayout_3.addComponent(searchfieldLayout);
		
		absoluteLayout_3.addComponent(buttonLayout, "top:100.0px;left:190.0px;");
		
		
		
		
		searchVerticalLayout.addComponent(absoluteLayout_3);
		
		return searchVerticalLayout;
	}

	public boolean validateFields(){
	
		Date frmdate = fromDate.getValue();
		Date endDate = toDate.getValue();
		filters = new HashMap<String, Object>();		
		
		if(frmdate != null && endDate != null ){
			if(!SHAUtils.validateDate(frmdate) ){
				showErrorPopup("Please Enter Valid From Date");
				return false;
			}
			if(!SHAUtils.validateDate(endDate)){

				showErrorPopup("Please Enter Valid To Date");
				return false;
			}
				
			if(frmdate.before(endDate) || frmdate.compareTo(endDate)<=0)
			{
				filters.put("fromDate", frmdate);
				filters.put("endDate",endDate);
				  return true;
			}
			else
			{
				showErrorPopup("From date should not be greater than the To date");
				return false;
			}
		}
//			if(endDate.before(frmdate)){
//				showErrorPopup("Please Enter Valid To Date");
//				return false;
//			}
				
		
		else if(frmdate != null && endDate == null && !SHAUtils.validateDate(frmdate)){
			Date currDate = new Date();
			if(frmdate.before(currDate) || frmdate.compareTo(currDate)<=0)
			{
				endDate = currDate;
				filters.put("fromDate", frmdate);
				filters.put("endDate",endDate);
				return true;
			}
			else{
				filters.put("fromDate", frmdate);
				filters.put("endDate",endDate);
				return true;
			}			
		}
		else if(frmdate == null && endDate != null && !SHAUtils.validateDate(frmdate)){
			Date currDate = new Date();
			if(endDate.before(currDate) || endDate.compareTo(currDate)<=0)
			{
				filters.put("fromDate", frmdate);
				filters.put("endDate",endDate);
				return true;
			}
			else{
				endDate = frmdate;
				filters.put("fromDate", frmdate);
				filters.put("endDate",endDate);
				return true;
			}			
		}
		else if(frmdate == null && endDate == null){
			filters.put("fromDate", frmdate);
			filters.put("endDate",endDate);
		}
		return true;			
	}
	
	public void showTable(List<PlannedAdmissionReportDto> claimList){
		
		if(claimList != null && !claimList.isEmpty()){			
			clearSearchTable();
			plannedIntimationDetailsTable.setTableList(claimList);
			searchVerticalLayout.addComponent(plannedIntimationDetailsTable);
			
			if(exportBtn == null){
				addExportButton();
			}
		}
		
		else{
			
			if(exportBtn != null){
				buttonLayout.removeComponent(exportBtn);
				buttonLayout.removeComponent(dummyLabel);
				exportBtn = null;
			}
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Planned Admission Report Home");
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
					resetAllValues();
					fireViewEvent(MenuItemBean.PLANNED_ADMISSION_REPORT, null);
					
				}
			});
			
		}
	}

	private void addExportButton(){
		
		exportBtn = new Button("Export to Excel");
		buttonLayout.addComponent(exportBtn);
		buttonLayout.addComponent(dummyLabel);
		
		
		exportBtn.addClickListener(new Button.ClickListener() {
				
			@Override
			public void buttonClick(ClickEvent event) {
				excelExport = new ExcelExport(plannedIntimationDetailsTable.getTable());
				excelExport.setReportTitle("Planned Admission Report");
				excelExport.setDisplayTotals(false);
				excelExport.export();
				
			}
		});
		
		buttonLayout.addComponent(exportBtn);
	}

	public void resetAllValues(){
		clearSearchFields();
		clearSearchTable();
		
	}
	public void clearSearchTable(){
		plannedIntimationDetailsTable.setTableList(new ArrayList<PlannedAdmissionReportDto>());
		if(exportBtn != null){
			buttonLayout.removeComponent(exportBtn);
			buttonLayout.removeComponent(dummyLabel);
			exportBtn = null;
		}
		excelExport = null;
	}
	public void clearSearchFields(){
		fromDate.setValue(null);
		toDate.setValue(null);
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
