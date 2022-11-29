package com.shaic.claim.reports.tatreport;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;

public class TATReportForm extends SearchComponent<TATReportFormDTO> {
	
	private CheckBox chxPendingClaims;
	private CheckBox chxCompletedClaims;
	private ComboBox cmbClaimsQueueType;
	private ComboBox cmbDateType;
	private ComboBox cmbCpuCode;
	private ComboBox cmbOfficeCodeName;
	private DateField dateField;
	private DateField toDateField;
	private ComboBox cmbTatDate;
	private HorizontalLayout fieldLayout;
	private Button excelReport;
	private ComboBox cmbClaimType;
	
	private BeanItemContainer<SelectValue> cpuCode;
	private BeanItemContainer<SelectValue> selectValueContainerCompletedClaims;
	private BeanItemContainer<SelectValue> selectValueContainerPendingClaims;

	@Inject
	private TATReportTable tatReportTable;
	
	
	@Inject
	private TATReportFormDTO tatFormDto;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption(" TAT Report");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	
	public VerticalLayout mainVerticalLayout(){
		
		fieldLayout = new HorizontalLayout();
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 mainVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("100.0%");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("230px");
		 		
		 excelReport = new Button("Export To Excel");
		 
	
		
		
		chxPendingClaims = binder.buildAndBind("Pending Claims","pendingClaims",CheckBox.class);
		chxCompletedClaims = binder.buildAndBind("Completed Claims","completedClaims",CheckBox.class);
		cmbClaimsQueueType = binder.buildAndBind("Claims Queue Type","claimsQueueType",ComboBox.class);
		cmbClaimsQueueType.setEnabled(false);
		cmbDateType = binder.buildAndBind("Date Type","dateType",ComboBox.class);
		cmbCpuCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		cmbOfficeCodeName = binder.buildAndBind("Office Code Name","officeCode",ComboBox.class);
		dateField = binder.buildAndBind("From Date","fromDate",DateField.class);
		dateField.setRequired(true);
		dateField.setRequiredError("From Date is Mandatory");
		dateField.setValidationVisible(false);
		
		toDateField = binder.buildAndBind("To Date","toDate",DateField.class);
		toDateField.setRequired(true);
		toDateField.setRequiredError("To Date is Mandatory");
		toDateField.setValidationVisible(false);
		cmbTatDate = binder.buildAndBind("TAT Date","tatDate",ComboBox.class);
		
		
		FormLayout formLayoutLeft = new FormLayout(chxPendingClaims,cmbDateType,cmbCpuCode,dateField,cmbTatDate);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(chxCompletedClaims,cmbClaimsQueueType,cmbOfficeCodeName,toDateField);
		formLayoutRight.setSpacing(true);
		
		Label dummyLbl = new Label();
		dummyLbl.setHeight("-1px");
		FormLayout clmTypeFrmLayout = new FormLayout(dummyLbl,cmbClaimType);
//		clmTypeFrmLayout.setSpacing(true);
		
		fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight,clmTypeFrmLayout);
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(true);
		absoluteLayout_3.addComponent(fieldLayout);
		
		absoluteLayout_3
		.addComponent(btnSearch, "top:190.0px;left:250.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:190.0px;left:359.0px;");
		absoluteLayout_3.addComponent(excelReport, "top:190.0px;left:468.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		addListener();
		addListenerForCheckBox();
		addReportListener();
		return mainVerticalLayout;
	}
	
	private void addReportListener()
	{
		excelReport.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;


				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(TATReportPresenter.GENERATE_REPORT, null,null);
					//getTableDataForReport();
				
			}
		});
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<TATReportFormDTO>(TATReportFormDTO.class);
		this.binder.setItemDataSource(new TATReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	
	 public void addListenerForCheckBox()
	  {
		 chxPendingClaims.addValueChangeListener(new ValueChangeListener() {
				
				
				@Override
				public void valueChange(Property.ValueChangeEvent event) {
					
					
					if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
					{
					 boolean value = (Boolean) event.getProperty().getValue();
					 
					 
					if(value)
					{
						//unbindFields();
						resetFlds();
						//searchable.resetSearchResultTableValues();
						cmbDateType.setContainerDataSource(selectValueContainerPendingClaims);
						cmbDateType.setValidationVisible(false);
						
						chxCompletedClaims.setValue(false);
						tatReportTable.initTable();
						tatReportTable.setVisibleColumnsForPendingClaims();
					}
					}
				}
			});
		 
		 chxCompletedClaims.addValueChangeListener(new ValueChangeListener() {				
				
				@Override
				public void valueChange(Property.ValueChangeEvent event) {
					
					
					if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
					{
					 boolean value = (Boolean) event.getProperty().getValue();
					 
					if(value)
					{
						//unbindFields();
						resetFlds();
						//searchable.resetSearchResultTableValues();
						cmbDateType.setContainerDataSource(selectValueContainerCompletedClaims);												
						chxPendingClaims.setValue(false);
						tatReportTable.initTable();
						tatReportTable.setVisibleColumnsForCompletedClaims();
					}
					}
				}
			});
		 
		cmbCpuCode.addValueChangeListener(new ValueChangeListener() {
				
				
				@Override
				public void valueChange(Property.ValueChangeEvent event) {

					if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
					{
					 SelectValue value = (SelectValue) event.getProperty().getValue();
					
						fireViewEvent(TATReportPresenter.SEARCH_OFFICE_CODE, value);
					
					}
				}
			});
		 		 
		 
	  }
	
	 private void unbindFields() {
			
			List<Field<?>> field = getListOfFeilds();
			if(null != field && !field.isEmpty())
			{
				for (Field<?> field2 : field) {
					if (field2 != null ) {
						Object propertyId = this.binder.getPropertyId(field2);
						//if (field2!= null && field2.isAttached() && propertyId != null) {
						if (field2!= null  && propertyId != null) {
							this.binder.unbind(field2);
						}
					}
				}
			}
		}
		
		public void resetFlds()
		{
			List<Field<?>>  fieldList = getListOfFeilds();
			if(null != fieldList && !fieldList.isEmpty())
			{
				for (Field<?> field : fieldList) {
					if (field != null ) {
						field.setValue(null);					
						}
				}
			}
			tatFormDto.setDateType(null);
			tatFormDto.setClaimsQueueType(null);
			tatFormDto.setCpuCode(null);
			tatFormDto.setOfficeCode(null);
			tatFormDto.setFromDate(null);
			tatFormDto.setToDate(null);
			tatFormDto.setTatDate(null);
			setValidationVisible(false);			
		}
	 
		private List<Field<?>> getListOfFeilds()
		{
				List<Field<?>>  fieldList = new ArrayList<Field<?>>();
				//fieldList.add(chxPendingClaims);
				//fieldList.add(chxCompletedClaims);
				fieldList.add(cmbCpuCode);
				fieldList.add(cmbClaimsQueueType);
				fieldList.add(cmbDateType);	
				fieldList.add(cmbOfficeCodeName);
				fieldList.add(dateField);
				fieldList.add(toDateField);
				fieldList.add(cmbTatDate);
				fieldList.add(cmbClaimType);
				
				if(cmbClaimType != null)
					fieldList.add(cmbClaimType);
				if(null != cmbCpuCode)
					fieldList.add(cmbCpuCode);
				if(null != cmbClaimsQueueType)
					fieldList.add(cmbClaimsQueueType);
				if(null != cmbDateType )
					fieldList.add(cmbDateType);
				if(null != cmbOfficeCodeName )
					fieldList.add(cmbOfficeCodeName);
				if(null != dateField)
					fieldList.add(dateField);				
				if(null != toDateField)
					fieldList.add(toDateField);
				if(null != cmbTatDate)
					fieldList.add(cmbTatDate);
				return fieldList;
		}
		
		
	public void setDropDownValues(BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> officeCode)
	{
		BeanItemContainer<SelectValue> clmTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		SelectValue cashlessClmSelect = new SelectValue(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY, SHAConstants.CASHLESS_CLAIM_TYPE);
		SelectValue reimbClmSelect = new SelectValue(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY, SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
		clmTypeContainer.addBean(cashlessClmSelect);
		clmTypeContainer.addBean(reimbClmSelect);
		
		cmbClaimType.setContainerDataSource(clmTypeContainer);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
		cmbCpuCode.setContainerDataSource(cpuCode);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");
//		cmbCpuCode.setRequiredError("CPU Code is Mandatory, Please Select CPU Code");
//	    cmbCpuCode.setRequired(true);
//	    cmbCpuCode.setValidationVisible(false); 		
				
		SelectValue selectAckDate = new SelectValue();
		selectAckDate.setId(1l);
		selectAckDate.setValue("ACK DATE");
		
	
		List<SelectValue> selectVallueListForPendingClaims = new ArrayList<SelectValue>();
		selectVallueListForPendingClaims.add(selectAckDate);		
		
		selectValueContainerPendingClaims = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainerPendingClaims.addAll(selectVallueListForPendingClaims);
		cmbDateType.setContainerDataSource(selectValueContainerPendingClaims);
		cmbDateType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDateType.setItemCaptionPropertyId("value");
		cmbDateType.setRequired(true);
		cmbDateType.setValidationVisible(false);
		cmbDateType.setRequiredError("Please Select Date Type");
		
		SelectValue selectFinancialApproved = new SelectValue();
		selectFinancialApproved.setId(2l);
		selectFinancialApproved.setValue(SHAConstants.TAT_FINANCIAL_APPROVED);
		
		SelectValue selectPaidDate = new SelectValue();
		selectPaidDate.setId(3l);
		selectPaidDate.setValue(SHAConstants.TAT_PAID_DATE);
		

		List<SelectValue> selectVallueListForCompletedClaims = new ArrayList<SelectValue>();
		selectVallueListForCompletedClaims.add(selectFinancialApproved);	
		selectVallueListForCompletedClaims.add(selectPaidDate);
		
		selectValueContainerCompletedClaims = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainerCompletedClaims.addAll(selectVallueListForCompletedClaims);
		cmbDateType.setContainerDataSource(selectValueContainerCompletedClaims);
		cmbDateType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDateType.setItemCaptionPropertyId("value");
		
		
		cmbOfficeCodeName.setContainerDataSource(officeCode);
		cmbOfficeCodeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbOfficeCodeName.setItemCaptionPropertyId("value");
		cmbOfficeCodeName.setEnabled(false);
		
		
		SelectValue selectBetween0to8 = new SelectValue();
		selectBetween0to8.setId(1l);
		selectBetween0to8.setValue("BETWEEN 0 TO 8 DAYS");
		
		SelectValue selectBetween9to15 = new SelectValue();
		selectBetween9to15.setId(2l);
		selectBetween9to15.setValue("BETWEEN 9 TO 15 DAYS");
		
		SelectValue selectBetween6to21 = new SelectValue();
		selectBetween6to21.setId(3l);
		selectBetween6to21.setValue("BETWEEN 16 TO 21 DAYS");
		
		SelectValue selectBetween21to30 = new SelectValue();
		selectBetween21to30.setId(4l);
		selectBetween21to30.setValue("BETWEEN 21 TO 30 DAYS");
		
		SelectValue selectAbove7Days = new SelectValue();
		selectAbove7Days.setId(5l);
		selectAbove7Days.setValue(SHAConstants.ABOVE_7_DAYS);
		
		SelectValue selectAbove20Days = new SelectValue();
		selectAbove20Days.setId(6l);
		selectAbove20Days.setValue(SHAConstants.ABOVE_20_DAYS);
				
		SelectValue selectBetween30 = new SelectValue();
		selectBetween30.setId(7l);
		selectBetween30.setValue(SHAConstants.ABOVE_30__DAYS);
		
		
		
		List<SelectValue> selectVallueListForTatDate = new ArrayList<SelectValue>();
		selectVallueListForTatDate.add(selectBetween0to8);	
		selectVallueListForTatDate.add(selectBetween9to15);
		selectVallueListForTatDate.add(selectBetween6to21);
		selectVallueListForTatDate.add(selectBetween21to30);
		selectVallueListForTatDate.add(selectAbove7Days);
		selectVallueListForTatDate.add(selectAbove20Days);
		selectVallueListForTatDate.add(selectBetween30);
		
		BeanItemContainer<SelectValue> selectValueContainerForTatDate = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainerForTatDate.addAll(selectVallueListForTatDate);
		cmbTatDate.setContainerDataSource(selectValueContainerForTatDate);
		cmbTatDate.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbTatDate.setItemCaptionPropertyId("value");
		
	}
	
	
	public String validate()
	{
		boolean error = false;
		StringBuffer err = new StringBuffer();
		
		if(((null == chxPendingClaims.getValue()) || chxPendingClaims.getValue()== false) && ((null == chxCompletedClaims.getValue()) || (chxCompletedClaims.getValue() == false)))
		{
			err.append("Please Select either Pending Claims or Completed Claims").append("<br>");
			error = true;
		}
		
		if(null == cmbDateType.getValue())
		{
			cmbDateType.setValidationVisible(true);
			err.append( cmbDateType.getRequiredError()).append("<br>");   
			error = true;
		}
		
//		if(null == cmbCpuCode.getValue() )
//		{
//			cmbCpuCode.setValidationVisible(true);
//			err += cmbCpuCode.getRequiredError()+"<br>";
//			error = true;
//		}
		
		if(dateField.getValue()!= null && toDateField.getValue() != null)
		{
			if(toDateField.getValue().before(dateField.getValue()) || !dateField.isValid() || !toDateField.isValid())
			 {  
				err.append("Enter Valid From Date / To Date").append("<br>");
				error = true;
			}
		}
		else
		{
			if(dateField.getValue() == null || ("").equals(dateField.getValue()))
			{
				err.append(dateField.getRequiredError()).append("<br>");	
				dateField.setValidationVisible(true);
			}
			if(toDateField.getValue() == null || ("").equals(toDateField.getValue()))
			{
				err.append(toDateField.getRequiredError()).append("<br>");
				toDateField.setValidationVisible(true);
			}			
			error = true;
		}
		
		if(!error && ("").equals(err)){
			error = false;
			setValidationVisible(error);
			return null;
		}
		
		error = true;
		return err.toString();
		/*if(null == cmbCpuCode.getValue() && err.equals(""))
		{
			
			try {
				List<SelectValue> cpuSelectList = (List<SelectValue>)cmbCpuCode.getItemIds();
				List<Long> cpuList = new ArrayList<Long>();
				for(SelectValue cpuSelect : cpuSelectList){
					cpuList.add(Long.valueOf(cpuSelect.getValue()));
				}
				TATReportFormDTO bean = this.binder.getItemDataSource().getBean();
				bean.setCpuCodeList(cpuList);
				this.binder.setItemDataSource(bean);
				this.binder.commit();
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/	
		
	}

	public void setValidationVisible(Boolean valVisible){
		cmbDateType.setValidationVisible(valVisible);
//		cmbCpuCode.setValidationVisible(valVisible);
		dateField.setValidationVisible(valVisible);
		toDateField.setValidationVisible(valVisible);
	}
	
	public void setOfficeCodeDropDown(BeanItemContainer<SelectValue> officeCode)
	{
		cmbOfficeCodeName.setContainerDataSource(officeCode);
		cmbOfficeCodeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbOfficeCodeName.setItemCaptionPropertyId("value");
		cmbOfficeCodeName.setEnabled(false);
		//cmbOfficeCodeName.setValue("selectACpu");
	}
	
	public void validateExportExcel(Page<TATReportTableDTO> tableRows)
	{
		if(null != tableRows && tableRows.getPageItems().isEmpty())
		{
			excelReport.setEnabled(false);
		}
		else
		{
			excelReport.setEnabled(true);
		}
		
	}
}
