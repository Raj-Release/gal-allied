package com.shaic.claim.reports.claimstatusreportnew;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
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
public class SearchClaimsStatusReport extends ViewComponent {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private HorizontalLayout formHorizantal;
	private FormLayout searchfieldLayout;
	private FormLayout searchFieldLayout1;
	private HorizontalLayout paidDateLayout;
	private HorizontalLayout buttonLayout;
	private ComboBox cpuCmb;
	private ComboBox clmStageCmb;
	private ComboBox periodTypeCmb;
	private ComboBox finYearCmb;
	private Button searchBtn;
	private Button resetBtn;
	private Button exportBtn;
	private Label dummyLabel;
	private Label dummyLabel1;
	private Label dummyLabel2;
	private Label dummyLabel3;
	private PopupDateField fromDate; 
	private PopupDateField toDate;
		
	
	private ExcelExport excelExport;
	
	@Inject
	private ClaimsStatusReportTable claimsStatusTable;
	
	
	@PostConstruct
	public void init()
	{
		wholeVerticalLayout = new VerticalLayout();
		buttonLayout = new HorizontalLayout();
		wholeVerticalLayout.setSizeFull();
		wholeVerticalLayout.addComponent(buildSearchPanel());
		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		claimsStatusTable.init("", false, false);
		setSizeFull();
		setCompositionRoot(wholeVerticalLayout);
	}

	private Panel buildSearchPanel(){
		 buildSearchLayout();
		searchPanel = new Panel("Claims Status Report New");
		searchPanel.addStyleName("panelHeader");
		searchPanel.addStyleName("g-search-panel"); 

		searchPanel.setContent(searchVerticalLayout);
		searchPanel.setHeight("100%");
		return searchPanel;
	}
	
	private void buildSearchLayout(){
		searchVerticalLayout = new VerticalLayout();
		cpuCmb = new ComboBox("CPU Office");
		
		clmStageCmb = new ComboBox("Report Type");
		
		periodTypeCmb = new ComboBox("Period Type");
		
		dummyLabel = new Label();
		
		clmStageCmb.addValueChangeListener((new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
//				if(clmStageCmb.getValue() != null){
//					if(((SelectValue)clmStageCmb.getValue()).getValue() != null && ((SelectValue)clmStageCmb.getValue()).getValue().equalsIgnoreCase(SHAConstants.CLAIMS_PAID_STAUS)){
//						paidDateLayout.setCaption("Paid From");
//						toDate.setCaption("Paid To");
//					}
//					else{
//						paidDateLayout.setCaption("From Date");
//						toDate.setCaption("To Date");
//					}
//				}
				String claimStatus = clmStageCmb.getValue() != null ? clmStageCmb.getValue().toString() : null;
				if(claimStatus != null){
				switch(claimStatus){
				case SHAConstants.CLAIMS_PAID_STATUS :
					fromDate.setCaption("Paid From");
						toDate.setCaption("Paid To");
						break;
				case SHAConstants.CLAIMS_PRE_AUTH_STATUS :
					fromDate.setCaption("From Date");
						toDate.setCaption("To Date");
						break;
				case SHAConstants.CLOSED_CLAIMS :
					fromDate.setCaption("Closed From");
					toDate.setCaption("Closed To");
					break;
				case SHAConstants.CLAIM_QUERY :
					fromDate.setCaption("From Date");
					toDate.setCaption("To Date");
					break;
				case SHAConstants.MEDICAL_APPROVAL :
					fromDate.setCaption("From Date");
					toDate.setCaption("To Date");
					break;
				case SHAConstants.BILLING_COMPLETED :
					fromDate.setCaption("From Date");
					toDate.setCaption("To Date");
					break;
				case SHAConstants.CLAIMS_BILL_RECEIVED_STATUS :
					fromDate.setCaption("Received From");
					toDate.setCaption("Received To");
					break;
				case SHAConstants.REJECTED_CLAIMS :
					fromDate.setCaption("Rejected From");
					toDate.setCaption("Rejected To");
					break;
				}
				
				}				
			}
		})
		);	
		
		
		periodTypeCmb.addValueChangeListener((new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if((SelectValue)periodTypeCmb.getValue() != null && StringUtils.equalsIgnoreCase(((SelectValue)periodTypeCmb.getValue()).getValue(),SHAConstants.FINANCIAL_YEAR_WISE)){
						getfinancialYearCmb(); 
				}
				else{
						removeFinancialYearCmb();
				}
				
			}
		})
		);
				
		searchfieldLayout = new FormLayout();
		searchFieldLayout1= new FormLayout();
		searchfieldLayout.addComponents(cpuCmb,clmStageCmb,periodTypeCmb);
		addPaidLayout();
		formHorizantal = new HorizontalLayout(searchfieldLayout,searchFieldLayout1);
		formHorizantal.setSpacing(true);
		formHorizantal.setMargin(true);
		searchVerticalLayout.addComponents(formHorizantal);
		searchVerticalLayout.setMargin(true);
		searchVerticalLayout.setSpacing(true);
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
				((Button)event.getSource()).setEnabled(true);
				Date frmdate = fromDate.getValue();
				Date endDate = toDate.getValue();
				Date fyFrmdate = null;
				Date fyEndDate = null;
				
				Map<String,Object> filters = new HashMap<String, Object>();
				
				if(validateFields())
					{
						if(cpuCmb.getValue() != null){
							String cpuCode = ((SelectValue)cpuCmb.getValue()).getValue();
							if(cpuCode != null && !cpuCode.isEmpty()){
								String[] split = cpuCode.split("-");
								String requestedString = split[0];
								if(requestedString != null){
									requestedString = requestedString.replaceAll("\\s","");
								}
								
								filters.put("cpuKey", requestedString);
							}
							
							
							
						}
						
						if(clmStageCmb.getValue() != null){
						filters.put("claimStageName", ((SelectValue)clmStageCmb.getValue()).getValue());
						}
						
						if(periodTypeCmb.getValue() != null){
						
						if ((SelectValue) periodTypeCmb.getValue() != null
								&& StringUtils.equalsIgnoreCase(
										((SelectValue) periodTypeCmb.getValue())
												.getValue(),
										SHAConstants.FINANCIAL_YEAR_WISE)) {

							String finYears[] = finYearCmb.getValue()
									.toString().split("-");
							if (finYears.length > 0) {
								 Calendar from = Calendar.getInstance();
								
								 from.set(
										Integer.valueOf(finYears[0]),
										Calendar.getInstance().APRIL, 1);

								 fyFrmdate = from.getTime();
								
								Calendar to = Calendar.getInstance();

								to.set(
										Integer.valueOf(finYears[1]),
										Calendar.getInstance().MARCH, 31);

								fyEndDate = to.getTime();
							}
							filters.put("periodType", "F");
						}else{
							filters.put("periodType", "O");
						}
						}
						
						if(fyFrmdate != null){
							filters.put("fyFromDate", fyFrmdate);
						}
						if(fyEndDate != null){
							filters.put("fyEndDate", fyEndDate);
						}
						
						if(frmdate != null){
							filters.put("fromDate", frmdate);
							if(endDate != null){
								filters.put("endDate",endDate);
							}
						}
						
						
						
						fireViewEvent(ClaimsStatusReportPresenter.SEARCH_CLMS_STATUS_NEW, filters);
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
		Label dummyLabel =new Label();
		dummyLabel.setWidth("30px");
		Label dummyLabe2 =new Label();
		dummyLabe2.setWidth("180px");
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		buttonLayout.addComponents(dummyLabe2,searchBtn,dummyLabel,resetBtn);
		absoluteLayout_3.addComponent(buttonLayout, "top:0.0px;left:200.0px;");
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);
//		searchVerticalLayout.setComponentAlignment(searchfieldLayout, Alignment.MIDDLE_CENTER);
		searchVerticalLayout.addComponent(buttonLayout);
//		searchVerticalLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);
		
//		return searchVerticalLayout;
	}

	private void getfinancialYearCmb(){
		
		removePaidLayout();
		finYearCmb = new ComboBox("Financial Year");
		
//		finYearCmb.getContainerDataSource().addItem("2012-2013");
//		finYearCmb.getContainerDataSource().addItem("2013-2014");
//		finYearCmb.getContainerDataSource().addItem("2014-2015");
		
		BeanItemContainer<SelectValue> policyYearValues = getPolicyYearValues();
		
		finYearCmb.setContainerDataSource(policyYearValues);
		finYearCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		finYearCmb.setItemCaptionPropertyId("value");

		searchfieldLayout.addComponent(finYearCmb);
	}
	
	private void removeFinancialYearCmb(){
		if(searchfieldLayout.getComponentIndex(finYearCmb) > 0){
			searchfieldLayout.removeComponent(finYearCmb);
			addPaidLayout();
		}
	}
	
	private void addPaidLayout(){
		
		fromDate = new PopupDateField("From Date");
		fromDate.setDateFormat("dd/MM/yyyy");
		
		toDate = new PopupDateField("To Date");
		toDate.setDateFormat("dd/MM/yyyy");
		 dummyLabel3 =new Label();
		dummyLabel.setSizeFull();
		dummyLabel1 =new Label();
		dummyLabel1.setSizeFull();
		 dummyLabel2 =new Label();
		dummyLabel2.setSizeFull();
		
		FormLayout fromDtFrm = new FormLayout();
		fromDtFrm.addComponent(fromDate);
//		fromDtFrm.setWidth("180px");
		fromDtFrm.setMargin(false);
		FormLayout toDtFrm = new FormLayout();
		toDtFrm.addComponent(toDate);
//		toDtFrm.setWidth("200px");
		toDtFrm.setMargin(false);
		paidDateLayout = new HorizontalLayout(fromDate,toDate);
		paidDateLayout.setCaption("From Date");
//		paidDateLayout.setWidth("500px");
		searchfieldLayout.addComponent(fromDate);
		searchFieldLayout1.addComponent(dummyLabel3);
		searchFieldLayout1.addComponent(dummyLabel1);
		searchFieldLayout1.addComponent(dummyLabel2);
		searchFieldLayout1.addComponent(toDate);
	}
	
	private void removePaidLayout(){
		if(searchfieldLayout.getComponentIndex(fromDate)> 0){
			searchfieldLayout.removeComponent(fromDate);
			searchFieldLayout1.removeComponent(dummyLabel3);
			searchFieldLayout1.removeComponent(dummyLabel1);
			searchFieldLayout1.removeComponent(dummyLabel2);
			searchFieldLayout1.removeComponent(toDate);
			
		}
		
	}
	
	public BeanItemContainer<SelectValue> getPolicyYearValues(){
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 1);
		int intYear = instance.get(Calendar.YEAR);
		Long year = Long.valueOf(intYear);
		for(Long i= year+1;i>=year-3;i--){
			SelectValue selectValue = new SelectValue();
			Long j = i-1;
			selectValue.setId(j);
			selectValue.setValue(""+j.intValue()+"-"+i.intValue());
			selectValueList.add(selectValue);
		}
		container.addAll(selectValueList);
		
		return container;
	}
	
	public boolean validateFields(){
	
		Date frmdate = fromDate.getValue();
		Date endDate = toDate.getValue();
		
		StringBuffer errorMsg = new StringBuffer();
		boolean isValid = true;
		
		
		if(periodTypeCmb.getValue() != null && StringUtils.containsIgnoreCase(((SelectValue)periodTypeCmb.getValue()).getValue(), SHAConstants.OPERATION_PERIOD)){
			if(frmdate == null || endDate == null){
				errorMsg.append("Please Select Paid Date<br>");
				isValid = false;
			}
		}
		
		if(frmdate != null && endDate != null ){
			
			if(SHAUtils.validateDate(frmdate) ){
				errorMsg.append("Please Enter Valid From Date<br>");
				isValid = false;
			}
			if(SHAUtils.validateDate(endDate)){

				errorMsg.append("Please Enter Valid To Date<br>"); 
				isValid = false;
			}
				
			if(fromDate.getValue().compareTo(toDate.getValue()) > 0){
				errorMsg.append("From date should not be greater than To date<br>");
				isValid = false;
			}	
			if(frmdate.before(endDate) || frmdate.compareTo(endDate)<=0)
			{
				isValid = true;
			}
				
		}
		
		/*else{
			
			if(fromDate.getValue() == null)
			{
				errorMsg += "Please Enter From Date<br>";
				isValid = false;
			}
				
			if(toDate.getValue() == null){
				
				errorMsg += "Please Enter To Date<br>";
				isValid = false;
			}
		}*/
		
		if(clmStageCmb.getValue() == null || ("").equalsIgnoreCase(((SelectValue)clmStageCmb.getValue()).getValue())){
			errorMsg.append("Please Select Report Type Value<br>");
			isValid = false;
		}

		if(periodTypeCmb.getValue() == null){
				errorMsg.append("Please Select Period Type<br>");
				isValid = false;
		}
			
		if(periodTypeCmb.getValue() != null && StringUtils.containsIgnoreCase(((SelectValue)periodTypeCmb.getValue()).getValue(), SHAConstants.FINANCIAL_YEAR_WISE)){
			if(finYearCmb.getValue() == null){
				errorMsg.append("Please Select Financial Year<br>");
				isValid = false;
			}
		}
		
		if(!isValid){
			showErrorPopup(errorMsg.toString());
		}		
		
		return isValid;
			
	}
	
	public void showTable(List<ClaimsStatusReportDto> claimList){
		
		if(claimList != null && !claimList.isEmpty()){			
			clearSearchTable();
			
			if(clmStageCmb.getValue() != null && ((SelectValue)clmStageCmb.getValue()).getValue().equalsIgnoreCase(SHAConstants.CLAIMS_PRE_AUTH_STATUS)){
				claimsStatusTable.setCashlessColHeader();
			}
			
//			else{
//				claimsStatusTable.setReimbursementColHeader();
//			}
				
				
			if(clmStageCmb.getValue() != null && ((SelectValue)clmStageCmb.getValue()).getValue().equalsIgnoreCase(SHAConstants.CLAIMS_PAID_STATUS)){
				claimsStatusTable.setReimbPaidColHeader();
			}
			else if(clmStageCmb.getValue() != null && ((SelectValue)clmStageCmb.getValue()).getValue().equalsIgnoreCase(SHAConstants.REJECTED_CLAIMS)){
				claimsStatusTable.setReimbRejectColHeader();
			}
			else if(clmStageCmb.getValue() != null && ((SelectValue)clmStageCmb.getValue()).getValue().equalsIgnoreCase(SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){
				claimsStatusTable.setReimbBillsReceicedColHeader();
			}
			else if(clmStageCmb.getValue() != null && ((SelectValue)clmStageCmb.getValue()).getValue().equalsIgnoreCase(SHAConstants.BILLING_COMPLETED)){
				claimsStatusTable.setReimbBillingCompletedColHeader();
			}
			else if(clmStageCmb.getValue() != null && ((SelectValue)clmStageCmb.getValue()).getValue().equalsIgnoreCase(SHAConstants.MEDICAL_APPROVAL)){
				claimsStatusTable.setReimbMedicalApprovedColHeader();
			}
			else if(clmStageCmb.getValue() != null && ((SelectValue)clmStageCmb.getValue()).getValue().equalsIgnoreCase(SHAConstants.CLAIM_QUERY)){
				claimsStatusTable.setReimbQueryColHeader();
			}
			else if(clmStageCmb.getValue() != null && ((SelectValue)clmStageCmb.getValue()).getValue().equalsIgnoreCase(SHAConstants.CLOSED_CLAIMS)){
				claimsStatusTable.setReimbClosedColHeader();
			}
			else if(clmStageCmb.getValue() != null && ((SelectValue)clmStageCmb.getValue()).getValue().equalsIgnoreCase(SHAConstants.CLAIMS_PRE_AUTH_STATUS)){
				claimsStatusTable.setCashlessColHeader();
			}
			
			claimsStatusTable.setTableList(claimList);
			searchVerticalLayout.addComponent(claimsStatusTable);
			
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
			Button homeButton = new Button("Claims Status Report Home");
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
					fireViewEvent(MenuItemBean.CLAIM_STATUS_CPUWISE_REPORT, null);
					
				}
			});
			
		}
	}

	private void addExportButton(){
		
		exportBtn = new Button("Export to Excel");
		dummyLabel.setWidth("30px");
		buttonLayout.addComponent(dummyLabel);
		buttonLayout.addComponent(exportBtn);
		
		
		
		exportBtn.addClickListener(new Button.ClickListener() {
				
			@Override
			public void buttonClick(ClickEvent event) {
				excelExport = new ExcelExport(claimsStatusTable.getTable());
				excelExport.setReportTitle("Claims Status Report New");
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
		claimsStatusTable.setTableList(new ArrayList<ClaimsStatusReportDto>());
		if(exportBtn != null){
			buttonLayout.removeComponent(exportBtn);
			buttonLayout.removeComponent(dummyLabel);
			exportBtn = null;
		}
		excelExport = null;
	}
	public void clearSearchFields(){
		cpuCmb.setValue(null);
		clmStageCmb.setValue(null);
		periodTypeCmb.setValue(null);
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
	}
	
	public void setDropDownValues(BeanItemContainer<SelectValue> cpuContainer,
			BeanItemContainer<SelectValue> clmStatusContainer ){
		
		cpuCmb.setContainerDataSource(cpuContainer);
		cpuCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cpuCmb.setItemCaptionPropertyId("value");
		
		clmStageCmb.setContainerDataSource(clmStatusContainer);
		clmStageCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		clmStageCmb.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> clmOperationPeriodContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue oprPriod = new SelectValue(null,SHAConstants.OPERATION_PERIOD);
		clmOperationPeriodContainer.addBean(oprPriod);
		SelectValue finPriod = new SelectValue(null,SHAConstants.FINANCIAL_YEAR_WISE);
		clmOperationPeriodContainer.addBean(finPriod);
		
		periodTypeCmb.setContainerDataSource(clmOperationPeriodContainer);
		periodTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		periodTypeCmb.setItemCaptionPropertyId("value");
	}
	
}
