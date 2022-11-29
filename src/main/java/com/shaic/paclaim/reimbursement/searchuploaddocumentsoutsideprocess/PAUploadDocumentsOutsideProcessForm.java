package com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedFormDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PAUploadDocumentsOutsideProcessForm extends SearchComponent<UploadDocumentsForAckNotReceivedFormDTO>{


	
	private ComboBox cmbType;
	private TextField txtAckNo;
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	SelectValue selectUploadedDocuments = new SelectValue();
	
	
	
	private BeanItemContainer<SelectValue> reportType;
	
	private BeanItemContainer<SelectValue> dropDownType;
	
	
	private Button buttonSearch;
	
	private Button buttonReset;

	
	
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Upload Document (Outside Process)");
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
		 absoluteLayout_3.setHeight("206px");
		 
		
		 buttonSearch = new Button();
		 buttonSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		 buttonSearch.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			//btnSearch.setDisableOnClick(true);
		/*btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);*/
		
		
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		cmbType.setEnabled(false);
		txtAckNo = binder.buildAndBind("Acknowledgement No","ackNo",TextField.class);
		txtIntimationNo = binder.buildAndBind("Intimation No","intimationNo",TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy No","policyNo",TextField.class);
		
		FormLayout formLayoutLeft = new FormLayout(cmbType,txtIntimationNo);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(txtPolicyNo);
		formLayoutRight.setSpacing(true);
		
		
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(true);
		absoluteLayout_3.addComponent(fieldLayout);
		
/*		absoluteLayout_3
		.addComponent(btnSearch, "top:150.0px;left:250.0px;");*/
		absoluteLayout_3
		.addComponent(buttonSearch, "top:150.0px;left:250.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:150.0px;left:359.0px;");		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		addSearchListener();	
		
		return mainVerticalLayout;
	}
	
	
	
	/*@Override
	protected void addListener(){*/
	private void addSearchListener(){
	if(null != buttonSearch)
	{
		//btnSearch.removeClickListener(this);
		buttonSearch.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 6100598273628582002L;

			public void buttonClick(ClickEvent event) {
				buttonSearch.setEnabled(false);
				searchable.doSearch();
				buttonSearch.setEnabled(true);
	        } 
	    });
	}
	
	if(null != btnReset)
	{
		//btnReset.removeClickListener(this);
		btnReset.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 6100598273628582002L;

			public void buttonClick(ClickEvent event) {

				SHAUtils.resetComponent(mainVerticalLayout);
				searchable.resetSearchResultTableValues();
				if(null != dropDownType)
					setDropDownValues(dropDownType);
				
	        } 
	    });
	}
		
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<UploadDocumentsForAckNotReceivedFormDTO>(UploadDocumentsForAckNotReceivedFormDTO.class);
		this.binder.setItemDataSource(new UploadDocumentsForAckNotReceivedFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setDropDownValues(BeanItemContainer<SelectValue> type) 
	{
		
		this.dropDownType = type;
	    		
		/*SelectValue selectYes = new SelectValue();
		selectYes.setId(1l);
		selectYes.setValue("Yes");

		SelectValue selectNo = new SelectValue();

		selectNo.setId(2l);
		selectNo.setValue("No");*/
		
		selectUploadedDocuments.setId(1l);
		selectUploadedDocuments.setValue("Post Process");
		
		List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
		selectVallueList.add(selectUploadedDocuments);
		//selectVallueList.add(selectNo);	
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addAll(selectVallueList);
		cmbType.setContainerDataSource(selectValueContainer);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		cmbType.setValue(selectUploadedDocuments);
	     
		
	}	
	
	
	
	
	public String validate()
	{
		String err = "";
		
		if((null != txtPolicyNo && (null == txtPolicyNo.getValue()|| txtPolicyNo.getValue().equals(""))) 
				&& (null != txtIntimationNo && (null == txtIntimationNo.getValue() || txtIntimationNo.getValue().equals("")))){
			
			return err = "Please Select Search by Intimation Or Policy Number";
		}
		
		return null;
		
	}



}
