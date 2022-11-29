package com.shaic.claim.reimbursement.paymentprocesscpu;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.LotNumberComboBox;
import com.shaic.arch.test.LotSuggestingContainer;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;

import com.vaadin.v7.ui.VerticalLayout;

public class PaymentProcessCpuForm extends SearchComponent<PaymentProcessCpuFormDTO>{
	
	private TextField txtIntimationNO;
	//private TextField txtintimationSeqNo;
	private TextField txtClaimNo;
	private DateField dateField;
	private DateField toDateField;
	private ComboBox cmbYear;
	private ComboBox cmbCpu;
	private LotNumberComboBox cmbCpuLotNo;
	private ComboBox cmbStatus;
	private ComboBox cmbBranch;
	
	private BeanItemContainer<SelectValue> year;
	private BeanItemContainer<SelectValue> cpuLotNo;
	private BeanItemContainer<SelectValue> status;
	private BeanItemContainer<SelectValue> branch;
	private BeanItemContainer<SelectValue> cpu;
	
	@Inject
	PaymentProcessCpuTable paymentProcessCpuTable;
	
	@EJB
	private MasterService masterService;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Payment Process Cpu");
		mainPanel.setContent(mainVerticalLayout());
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
		 absoluteLayout_3.setHeight("230px");

		
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		
		
		txtIntimationNO = binder.buildAndBind("Intimation No","intimationNo",TextField.class);
	//	txtintimationSeqNo = binder.buildAndBind("Intimation Seq No","intimationSeqNo",TextField.class);
		txtClaimNo = binder.buildAndBind("Claim Number","claimNumber",TextField.class);
		dateField = binder.buildAndBind("From Date","fromDate",DateField.class);
		toDateField = binder.buildAndBind("To Date","toDate",DateField.class);
		cmbYear = binder.buildAndBind("Year","year",ComboBox.class);
		cmbCpu = binder.buildAndBind("CPU","cpu",ComboBox.class);
		cmbCpuLotNo = new LotNumberComboBox();
		cmbCpuLotNo.setCaption("Cpu Lot No");
		cmbStatus = binder.buildAndBind("Status","status",ComboBox.class);
		cmbBranch = binder.buildAndBind("Branch","branch",ComboBox.class);

		FormLayout formLayoutLeft = new FormLayout(txtIntimationNO,dateField,cmbCpu,cmbCpuLotNo);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(txtClaimNo,toDateField,cmbStatus,cmbBranch);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		
		absoluteLayout_3.addComponent(fieldLayout);
		
		absoluteLayout_3.addComponent(btnSearch, "top:190.0px;left:280.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:190.0px;left:380.0px;");
		
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);

		
		addListener();
		
		addCpuCodeListener();
		
	
		return mainVerticalLayout;
		
	}
	
	private void addCpuCodeListener()
	{
	
		cmbCpu.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue selCpuCodeValue = (SelectValue) event.getProperty().getValue();
				if(null != selCpuCodeValue)
				{
					//Long iValue = value.getId();
					String cpuCodeVal[] = selCpuCodeValue.getValue().split("-");
					
					String strcpuCode = cpuCodeVal[0];
					if(null != strcpuCode)
					{
						Long cpuCode = Long.valueOf(strcpuCode.trim());
						fireViewEvent(PaymentProcessCpuPresenter.SEARCH_CPU_BRANCH, cpuCode, null);
					}
				}
			}
		});
		
		/*cmbCpu.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{
					validateFieldsBasedOnModeOfReceipt(value.getValue());
				}
			}
		});*/

	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PaymentProcessCpuFormDTO>(PaymentProcessCpuFormDTO.class);
		this.binder.setItemDataSource(new PaymentProcessCpuFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	
		public void setDropDownValues(BeanItemContainer<SelectValue> cpu,BeanItemContainer<SelectValue> year,
				BeanItemContainer<SelectValue> cpuLotNo, BeanItemContainer<SelectValue> status,BeanItemContainer<SelectValue> branch) 
		{
			cmbCpu.setContainerDataSource(cpu);
			cmbCpu.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbCpu.setItemCaptionPropertyId("value");
			
			
			cmbYear.setContainerDataSource(year);
			cmbYear.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbYear.setItemCaptionPropertyId("value");
			
//			cmbCpuLotNo.setContainerDataSource(cpuLotNo);
//			cmbCpuLotNo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//			cmbCpuLotNo.setItemCaptionPropertyId("value");
			final LotSuggestingContainer cpuLotContainer = new LotSuggestingContainer(masterService);
			cmbCpuLotNo.setContainerDataSource(cpuLotContainer);
			cmbCpuLotNo.setFilteringMode(FilteringMode.STARTSWITH);
			cmbCpuLotNo.setTextInputAllowed(true);
			cmbCpuLotNo.setNullSelectionAllowed(true);
			cmbCpuLotNo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbCpuLotNo.setItemCaptionPropertyId("value");
			cmbCpuLotNo.setNewItemsAllowed(true);
						
					
			cmbBranch.setContainerDataSource(branch);
			cmbBranch.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbBranch.setItemCaptionPropertyId("value");
			
			SelectValue allTypeOfLetters = new SelectValue();
			allTypeOfLetters.setId(1l);
			allTypeOfLetters.setValue(SHAConstants.ALL_TYPE_OF_LETTERS);

			SelectValue lettersPending = new SelectValue();
			lettersPending.setId(2l);
			lettersPending.setValue(SHAConstants.LETTERS_PRINT_PENDING);
			
			SelectValue lettersPrinted = new SelectValue();
			lettersPrinted.setId(3l);
			lettersPrinted.setValue(SHAConstants.LETTERS_PRINTED);
			
			SelectValue emailSent = new SelectValue();
			emailSent.setId(4l);
			emailSent.setValue(SHAConstants.EMAIL_SENT);
			
			SelectValue lettersPrintedAndEmailSent = new SelectValue();
			lettersPrintedAndEmailSent.setId(5l);
			lettersPrintedAndEmailSent.setValue(SHAConstants.LETTERS_PRINTED_AND_EMAIL_SENT);
			
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();			
			selectValueList.add(allTypeOfLetters);
			selectValueList.add(lettersPending);
			selectValueList.add(lettersPrinted); 
			selectValueList.add(emailSent);
			selectValueList.add(lettersPrintedAndEmailSent);
			
			
		   BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		   selectValueContainer.addAll(selectValueList);		  
		   cmbStatus.setContainerDataSource(selectValueContainer);
		   cmbStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		   cmbStatus.setItemCaptionPropertyId("value");
					
		}


		public void loadBranchContainer(
				BeanItemContainer<SelectValue> cpuCodeContainer) {
			
			if(null != cmbBranch)
			{
				cmbBranch.setContainerDataSource(cpuCodeContainer);
				cmbBranch.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbBranch.setItemCaptionPropertyId("value");
			}
		}	
			
	
	public String validate()
	{
		String err = "";
		
		if(dateField.getValue()!=null || toDateField.getValue()!=null)
		{
		 if(toDateField.getValue().before(dateField.getValue()))
		 {
			return err= "Enter Valid To Date";
		}
		}
		if(dateField.getValue()==null && toDateField.getValue()==null &&  null != cmbCpuLotNo.getValue() )
		{
			return err= "Enter From Date and to Date";
		}
		
		return err;
		
	}
	
	public SelectValue getLotCpuCodeValue(){
		if(cmbCpuLotNo != null && cmbCpuLotNo.getValue() != null){
			return (SelectValue)cmbCpuLotNo.getValue();
		}
		return null;
	}
	
	
	

}
