/**
 * 
 */
package com.shaic.claim.paymentprocess.createbatch.search;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narenj
 *
 */
public class SearchCreateOrSearchBatchForm extends SearchComponent<SearchCreateOrSearchBatchFormDTO> {
	
	@Inject
	private SearchCreateOrSearchBatchTable searchAcknowledgementDocumentReceiverTable;
	
	
	private ComboBox cbxType;
	
	private DateField dtfdStartDate;
	private DateField dtfdEndDate;
	private ComboBox cbxCPUCode;
	private ComboBox cbxClaimType;
	private ComboBox cbxClaimant;
	
	private TextField txtIntimationNo;
	private TextField txtLOTNo;
	private TextField txtRODNo;
	private TextField txtClaimNo;
	
	private HorizontalLayout fieldLayout1;
	private HorizontalLayout fieldLayout2;
	private FormLayout formLayoutLeft2;
	private FormLayout formLayoutLeft1;
	private FormLayout formLayoutReight2; 
	private FormLayout formLayoutReight1;
	
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Re-Open Claim");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
		txtLOTNo = binder.buildAndBind("LOT NO","lotNo",TextField.class);
		txtRODNo = binder.buildAndBind("ROD No","rodNO",TextField.class);
		
		dtfdStartDate = binder.buildAndBind("Start Date","startDate",DateField.class);
		dtfdEndDate = binder.buildAndBind("End Date","endDate",DateField.class);
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		cbxClaimType = binder.buildAndBind("Claim Type","cliamType",ComboBox.class);
		cbxClaimant = binder.buildAndBind("Claimant","claimant",ComboBox.class);
		
		cbxType = new ComboBox("Type");
		cbxType.setValue("Create LOT");
		cbxType.addItem("Create LOT");
		cbxType.addItem("Search LOT");
		//Vaadin8-setImmediate() cbxType.setImmediate(true);
		mainVerticalLayout.addComponent(new FormLayout(cbxType));
		formLayoutLeft2 = new FormLayout(dtfdStartDate,cbxCPUCode,cbxClaimant);
		formLayoutReight2 = new FormLayout(dtfdEndDate,cbxClaimType);	
		
		formLayoutLeft1 = new FormLayout(txtLOTNo,txtRODNo);
		formLayoutReight1 = new FormLayout(txtIntimationNo,txtClaimNo);	
		fieldLayout2 = new HorizontalLayout(formLayoutLeft2,formLayoutReight2);
		fieldLayout1 = new HorizontalLayout(formLayoutLeft1,formLayoutReight1);
		fieldLayout1.setVisible(false);
		mainVerticalLayout.addComponent(fieldLayout1);
		mainVerticalLayout.setComponentAlignment(fieldLayout1, Alignment.BOTTOM_CENTER);
		mainVerticalLayout.addComponent(fieldLayout2);
		mainVerticalLayout.setComponentAlignment(fieldLayout2, Alignment.BOTTOM_CENTER);
		setLayoutData();
		HorizontalLayout buttonlayout = new HorizontalLayout(btnSearch,btnReset);
		buttonlayout.setSpacing(true);
		
		
		
		mainVerticalLayout.addComponent(buttonlayout);
		mainVerticalLayout.setComponentAlignment(buttonlayout, Alignment.MIDDLE_CENTER);
		mainVerticalLayout.setWidth("50%");
		mainVerticalLayout.setHeight("250px");
		mainVerticalLayout.setMargin(true);
		mainVerticalLayout.setSpacing(true);
		addListener();
	
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchCreateOrSearchBatchFormDTO>(SearchCreateOrSearchBatchFormDTO.class);
		this.binder.setItemDataSource(new SearchCreateOrSearchBatchFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}	
	private void setLayoutData(){

		cbxType.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
			
				if(cbxType.getValue().equals("Create LOT")){
					fieldLayout1.setVisible(false);
					fieldLayout2.setVisible(true);
					
				}else if(cbxType.getValue().equals("Search LOT")){
					fieldLayout2.setVisible(false);
					fieldLayout1.setVisible(true);
				}	
				
			}
		});
	 
	}
}