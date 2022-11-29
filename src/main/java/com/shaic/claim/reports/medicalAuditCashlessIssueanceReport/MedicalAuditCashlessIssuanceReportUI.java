package com.shaic.claim.reports.medicalAuditCashlessIssueanceReport;

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
public class MedicalAuditCashlessIssuanceReportUI extends ViewComponent {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private HorizontalLayout buttonLayout;
	private PopupDateField fromDate; 
	private ComboBox claimStatusCmb;
	private PopupDateField toDate;
	private Button searchBtn;
	private Button resetBtn;
	private Button exportBtn;
	private Label dummyLabel;
	private ExcelExport excelExport;
	
	@Inject
	private MedicalAuditCashlessIssuanceReportTable medicalAuditCashlessIssuanceTable;
	
	@PostConstruct
	public void init()
	{
		wholeVerticalLayout = new VerticalLayout();
		wholeVerticalLayout.addComponent(buildSearchPanel());
		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		medicalAuditCashlessIssuanceTable.init("", false, false);
		
		setCompositionRoot(wholeVerticalLayout);
	}
	
	public void setStatusDropDownValue(BeanItemContainer<SelectValue> statusContainer){
		claimStatusCmb.setContainerDataSource(statusContainer);
		claimStatusCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		claimStatusCmb.setItemCaptionPropertyId("value");
	}

	private Panel buildSearchPanel(){
		searchVerticalLayout = buildSearchLayout();
		searchPanel = new Panel("Medical Audit Cashless Issuance Details");
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
		
		claimStatusCmb = new ComboBox("Claim Status");
		
		dummyLabel=new Label();
		dummyLabel.setWidth("30px");

		Label dummyLabel2 =new Label();
		dummyLabel2.setSizeFull();
		FormLayout leftForm = new FormLayout(claimStatusCmb,fromDate);
		FormLayout rightForm = new FormLayout(dummyLabel2,toDate);
		HorizontalLayout frmLayout = new HorizontalLayout(leftForm,rightForm);
		frmLayout.setMargin(true);
		frmLayout.setSpacing(true);
		
		buttonLayout = new HorizontalLayout();
		
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
				fireViewEvent(MedicalAuditCashlessIssuanceReportPresenter.SEARCH_MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT, filter);
				}				
			}
		});
		
		exportBtn = new Button("Export to Excel");
		searchBtn.setWidth("-1px");
		searchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		//Vaadin8-setImmediate() searchBtn.setImmediate(true);
		
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
		Label dummyLabel =new Label();
		dummyLabel.setWidth("30px");
		buttonLayout.addComponents(searchBtn,dummyLabel,resetBtn);
		buttonLayout.setSpacing(true);
		
		absoluteLayout_3.addComponent(frmLayout);
		
		absoluteLayout_3.addComponent(buttonLayout, "top:100.0px;left:190.0px;");
		
		searchVerticalLayout.addComponent(absoluteLayout_3);
		
		
		return searchVerticalLayout;
	}
	
	private Map<String, Object>getSearchFileterValues(){
		Map<String, Object> searchfilters = new HashMap<String, Object>();
		Boolean hasError = false;
		StringBuffer errorMessage = new StringBuffer();
				
		Date frmdate = fromDate.getValue();
		Date endDate = toDate.getValue();
						
//		if(fromDate.getValue() == null){
//			errorMessage +="<br>Please Select From Date";
//			hasError = true;
//		}
//		
//		if(toDate.getValue() == null){
//			errorMessage +="<br>Please Select To Date";
//			hasError = true;
//		}
				
		if(frmdate != null && endDate != null){
			if(!SHAUtils.validateDate(frmdate) && !SHAUtils.validateDate(endDate))
			if(frmdate.compareTo(endDate) > 0){
				errorMessage.append("<br>From date should not be greater than the To date");
				hasError = true;
			}
			else{
				searchfilters.put("fromDate", frmdate);	
				searchfilters.put("toDate", endDate);
				hasError = false;
			}
		}
		
		else if(frmdate != null && endDate == null && !SHAUtils.validateDate(frmdate)){
			Date currDate = new Date();
			if(frmdate.before(currDate) || frmdate.compareTo(currDate)<=0)
			{
				endDate = currDate;
				searchfilters.put("fromDate", frmdate);	
				searchfilters.put("toDate", endDate);
				hasError = false;
			}
			else{
				endDate = frmdate;
				searchfilters.put("fromDate", frmdate);	
				searchfilters.put("toDate", endDate);
				hasError = false;
			}			
		}
		else if(frmdate == null && endDate == null){
			hasError = false;
		}
		
		if(claimStatusCmb.getValue() == null){
			errorMessage.append("<br>Please Select a value for Claim Status");
			searchfilters.clear();
			hasError = true;
		}
		
		if(claimStatusCmb.getValue() != null){
			String status = ((SelectValue)claimStatusCmb.getValue()).getValue();
			searchfilters.put("status", status);
			hasError = false;
		}			
		
		
		if(hasError){
			showErrorPopup(errorMessage.toString());
			searchfilters.clear();			
		}
		return searchfilters;
		
	}
	public void showTableResult(List<MedicalAuditCashlessIssuanceReportDto> resultDtoList){
		
		searchBtn.setEnabled(true);
		if(resultDtoList != null && !resultDtoList.isEmpty()){
			
			medicalAuditCashlessIssuanceTable.setTableList(resultDtoList);			
			searchVerticalLayout.addComponent(medicalAuditCashlessIssuanceTable);
			
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
			Button homeButton = new Button("Medical Audit Cashless Issuance Report Home");
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
					fireViewEvent(MenuItemBean.MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT, null);
					
				}
			});	
		}
		
	}
	public void resetAlltheValues(){
		fromDate.setValue(null);
		toDate.setValue(null);
		claimStatusCmb.setValue(null);
		medicalAuditCashlessIssuanceTable.setTableList(new ArrayList<MedicalAuditCashlessIssuanceReportDto>());
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
				excelExport = new ExcelExport(medicalAuditCashlessIssuanceTable.getTable());
				excelExport.setReportTitle("Medical Audit Cashless Issueance Report");
				excelExport.setDisplayTotals(false);
				excelExport.export();
			}
		});
		buttonLayout.addComponent(exportBtn);
		buttonLayout.addComponent(dummyLabel);
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
