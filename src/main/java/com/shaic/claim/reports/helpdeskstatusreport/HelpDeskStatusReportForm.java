package com.shaic.claim.reports.helpdeskstatusreport;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
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
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;

public class HelpDeskStatusReportForm extends SearchComponent<HelpDeskStatusReportFormDTO> {
	private DateField dateField;
	private DateField toDateField;
	private DateField billRecFrom;
	private DateField billRecTo;
	private ComboBox cmbhospitalType;
	private Button xmlReport;
    private CheckBox chkHealth;	
	private CheckBox chkPa;	
	private CheckBox chkTmpPolicy;	
	private CheckBox chkDaysClaims;	
	private CheckBox chkSeniorCitizenClaim;
	private ComboBox cmbCPUCode;
	private ComboBox cmbClaimType;
	
	private BeanItemContainer<SelectValue> hospitalType;
	private Object bean;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Help Desk Status Report");
		mainPanel.setContent(mainVerticalLayout());
		mainPanel.setSizeFull();
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 mainVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("100.0%");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("293px");
		 
	
		 
		xmlReport = new Button("Export To Excel");
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		
		
		
		cmbhospitalType = binder.buildAndBind("Hospital Type","hospitalType",ComboBox.class);
		//Vaadin8-setImmediate() cmbhospitalType.setImmediate(true);	
		
		//setDropDownValues();
		
		dateField = binder.buildAndBind("From Date","fromDate",DateField.class);
		toDateField = binder.buildAndBind("To Date","toDate",DateField.class);
		billRecFrom = binder.buildAndBind("Bill Rec From","billRecFrom",DateField.class);
		billRecTo = binder.buildAndBind("Bill Rec To","billRecTo",DateField.class);		
		chkHealth = binder.buildAndBind("Health","health",CheckBox.class);
		chkPa = binder.buildAndBind("PA","pa",CheckBox.class);
		chkTmpPolicy = binder.buildAndBind("Only TMP Policy YN","tmpPolicy",CheckBox.class);
		chkDaysClaims = binder.buildAndBind("Only 21 Days Claims YN","claimDays",CheckBox.class);
		chkSeniorCitizenClaim = binder.buildAndBind("Senior Citizen Claims","seniorCitizenClaim",CheckBox.class);
		cmbCPUCode =  binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		chkSeniorCitizenClaim.setValue(false);
		chkHealth.setValue(false);
		FormLayout formLayoutLeft = new FormLayout(dateField,billRecFrom,cmbhospitalType,cmbClaimType);
		formLayoutLeft.setSpacing(true);
//		formLayoutLeft.setWidth("45%");
		FormLayout formLayoutRight = new FormLayout(toDateField,billRecTo,cmbCPUCode);
		formLayoutRight.setSpacing(true);
		
		HorizontalLayout checkBox1 = new HorizontalLayout(chkHealth,chkPa);
		checkBox1.setSpacing(true);
		HorizontalLayout checkBox2 = new HorizontalLayout(chkTmpPolicy,chkDaysClaims,chkSeniorCitizenClaim);
		checkBox2.setSpacing(true);
	
		FormLayout checkBoxLayout = new FormLayout(checkBox1,checkBox2);
		//checkBoxLayout.setMargin(true);

		checkBoxLayout.setSpacing(true);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		absoluteLayout_3.addComponent(fieldLayout);
		absoluteLayout_3.addComponent(checkBoxLayout,"top:160.0px;left:0.0px;");
		absoluteLayout_3
		.addComponent(btnSearch, "top:250.0px;left:190.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:250.0px;left:299.0px;");
		absoluteLayout_3.addComponent(xmlReport, "top:250.0px;left:408.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		addListener();
		addReportListener();
		
		return mainVerticalLayout;
	}
	
	private void addReportListener()
	{
		xmlReport.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;


				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(HelpDeskStatusReportPresenter.GENERATE_REPORT, null,null);
					//getTableDataForReport();
				
			}
		});
	}
	
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<HelpDeskStatusReportFormDTO>(HelpDeskStatusReportFormDTO.class);
		this.binder.setItemDataSource(new HelpDeskStatusReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	 public void setDropDownValues(BeanItemContainer<SelectValue> cpu ,BeanItemContainer<SelectValue> claimType) {
		  
		 	cmbCPUCode.setContainerDataSource(cpu);
		 	cmbCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 	cmbCPUCode.setItemCaptionPropertyId("value");	
		 	
		 	cmbClaimType.setContainerDataSource(claimType);
			cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbClaimType.setItemCaptionPropertyId("value");
		 
		     
		    SelectValue billReceived = new SelectValue();
		    billReceived.setId(1l);
		    billReceived.setValue(SHAConstants.BILL_RECEIVED);

			SelectValue billNotReceived = new SelectValue();
			billNotReceived.setId(2l);
			billNotReceived.setValue(SHAConstants.BILL_NOT_RECEIVED);
			
			SelectValue billReceivedCompleted = new SelectValue();
			billReceivedCompleted.setId(3l);
			billReceivedCompleted.setValue(SHAConstants.BILL_RECEIVED_COMPLETED);	
		

			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			selectValueList.add(billReceived);
			selectValueList.add(billNotReceived);
			selectValueList.add(billReceivedCompleted);
			
		  BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		  selectValueContainer.addAll(selectValueList);		  
		  
		  cmbhospitalType.setContainerDataSource(selectValueContainer);
		  cmbhospitalType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		  cmbhospitalType.setItemCaptionPropertyId("value");
		  
		  	
		 // claimTypeDropDown();
		  
		  
	  }
	 
	/* public void claimTypeDropDown()
	 {
		 	SelectValue cashlessClaimType = new SelectValue();
		 	cashlessClaimType.setId(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
		 	cashlessClaimType.setValue(SHAConstants.CASHLESS_CLAIM_TYPE);

			SelectValue reimbursementClaimType = new SelectValue();
			reimbursementClaimType.setId(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
			reimbursementClaimType.setValue(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
			
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			selectValueList.add(cashlessClaimType);
			selectValueList.add(reimbursementClaimType);			
			
		  BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		  selectValueContainer.addAll(selectValueList);		  
		  
		  cmbClaimType.setContainerDataSource(selectValueContainer);
		  cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		  cmbClaimType.setItemCaptionPropertyId("value");
	 }*/
	
	
	public String validate()
	{
		StringBuffer err = new StringBuffer();
		
		if((dateField.getValue() == null && toDateField.getValue() ==null) && (billRecFrom.getValue() == null && billRecTo.getValue() ==null) 
				&& cmbhospitalType.getValue() == null && cmbCPUCode == null)
		{
			return "Any one of the field is Mandatory";
		}
		else
		{	
			if(dateField.getValue() != null && toDateField.getValue() !=null)
			{
		      if(toDateField.getValue().before(dateField.getValue())) 
						 {
			      err.append("Enter Valid To Date </br>");
		           }
		      
		    }	
		if(billRecFrom.getValue() != null && billRecTo.getValue() !=null)	{
			
			if(billRecTo.getValue().before(billRecFrom.getValue())) 
			 {
				err.append("Enter Valid  Bill Rec To Date </br>");
			 }
	   
		}	
		 if(cmbhospitalType.getValue() == null)
		    {
		    	err.append(" Please select Hospital Type </br>");
		    }
		 
		 if(cmbClaimType.getValue() == null)
		    {
		    	err.append(" Please select Claim Type </br>");
		    }
		}
		if(dateField.getValue() == null && toDateField.getValue() ==null && billRecFrom.getValue() == null && billRecTo.getValue() ==null)
		{
			err.append(" Please select Any Date Value </br>");
		}
		
		 		 
		 return err.toString();
		
	}
	
		
	@Override
	public void buttonClick(ClickEvent event) {
		
		
		if (event.getButton() == btnReset)
		{
			
			fireViewEvent(HelpDeskStatusReportPresenter.RESET_FUNCTION,null,null);
		}
		super.buttonClick(event);
	}
	
      public void clearFields()
      {
    	  chkSeniorCitizenClaim.setValue(null);
			chkDaysClaims.setValue(null);
			chkTmpPolicy.setValue(null);
			chkPa.setValue(null);
			chkHealth.setValue(null);
      }
}
