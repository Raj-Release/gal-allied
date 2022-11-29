package com.shaic.claim.policy.search.ui.opsearch;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpDTO;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class SearchSettlementLetterProcessOPClaimRequestForm extends SearchComponent<CreateBatchOpDTO> {


	private static final long serialVersionUID = -4088910426204201267L;

	@EJB
	private PreauthService preauthService;

	private TextField txtIntimationNo;
	private TextField txtHelathCardNo;
	private TextField txtPolicyNumber;
	private TextField txtClaimNumber;
//	private ComboBox cmbClaimType;
	private ComboBox cmbPIOCode;


//	private BeanItemContainer<SelectValue> claimType;

	@PostConstruct
	public void init() {
		initBinder();

		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process OutPatient Settlement"); //Process Claim (OP Check-up) 
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}

	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption("Search");
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();

		txtIntimationNo = binder.buildAndBind("Intimation Number", "intimationNo", TextField.class);
		txtPolicyNumber = binder.buildAndBind("Policy Number","policyNumber",TextField.class);
		txtHelathCardNo = binder.buildAndBind("Health Card ID No","healthCardNo",TextField.class);
		txtClaimNumber = binder.buildAndBind("Claim No","claimNo",TextField.class);
		cmbPIOCode = binder.buildAndBind("PIO Code","pioCode",ComboBox.class);
//		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtPolicyNumber);
		FormLayout formLayoutMiddle = new FormLayout(txtClaimNumber,txtHelathCardNo);
		FormLayout formLayoutRight = new FormLayout(cmbPIOCode);

		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutMiddle,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:140.0px;left:370.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:140.0px;left:470.0px;");


		mainVerticalLayout.addComponent(absoluteLayout_3);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("1000px");
		 mainVerticalLayout.setMargin(false);			 
		//Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("224px");
		addListener();

//		setPreAuthType();

		return mainVerticalLayout;
	}

	private void initBinder()
	{
		this.binder = new BeanFieldGroup<CreateBatchOpDTO>(CreateBatchOpDTO.class);
		this.binder.setItemDataSource(new CreateBatchOpDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

//	public void setDropDownValues(BeanItemContainer<SelectValue> claimType) 
//	{
//		cmbClaimType.setContainerDataSource(claimType);
//		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cmbClaimType.setItemCaptionPropertyId("value");
//	}	

	public void setPreAuthType(){

		//		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		//		
		//		cmbType.setContainerDataSource(selectValueForType);
		//		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		//		cmbType.setItemCaptionPropertyId("value");
		//		
		//		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
		//		
		//		cmbPriority.setContainerDataSource(selectValueForPriority);
		//		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		//		cmbPriority.setItemCaptionPropertyId("value");

		Status statusByKey = preauthService.getStatusByKey(ReferenceTable.OP_REGISTER_CLAIM_STATUS); 
		//		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(statusByKey.getKey());
		selectValue2.setValue(statusByKey.getProcessValue());



		//		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		//		statusByStage.addBean(selectValue2);
		//		
		//		cmbSource.setContainerDataSource(statusByStage);
		//		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		//		cmbSource.setItemCaptionPropertyId("value");



	}
	
	public void setDropDownValues(BeanItemContainer<SelectValue> pioCode) 
	{
		cmbPIOCode.setContainerDataSource(pioCode);
		cmbPIOCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPIOCode.setItemCaptionPropertyId("value");
	}	


}
