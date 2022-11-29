/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;



import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;


/**
 * @author ntv.vijayar
 *
 */
public class SearchReviewRRCRequestForm extends SearchComponent<SearchReviewRRCRequestFormDTO> {
	
	private static final long serialVersionUID = -4088910426204201267L;
	
	@EJB
	private MasterService masterService;
	
	
	private TextField txtIntimationNo;
	private TextField txtRRCRequestNo;
	private ComboBox cmbCpu;
	private ComboBox cmbRRCRequestType;
	private ComboBox cmbEligiblityType;
	private DateField fromDate;
	private DateField toDate;
	private Label dummyField;
	
	private ComboBox cmbPriority;
	private ComboBox cmbSource;
	private ComboBox cmbType;
	
	
	private BeanItemContainer<SelectValue> cpuCode;
	private BeanItemContainer<SelectValue> rrcRequestType;
	
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Review RRC Request");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.GET_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		
		//txtIntimationNo.setMaxLength(25);
		CSValidator validator = new CSValidator();
		validator.extend(txtIntimationNo);
		validator.setRegExp("^[a-z A-Z 0-9 / .]*$");
		validator.setPreventInvalidTyping(true);
		
		
		txtRRCRequestNo = binder.buildAndBind("RRC Request No","rrcRequestNo",TextField.class);
		//txtRRCRequestNo.setMaxLength(25);
		CSValidator rrcValidator = new CSValidator();
		rrcValidator.extend(txtRRCRequestNo);
		rrcValidator.setRegExp("^[a-z A-Z 0-9 / .]*$");
		rrcValidator.setPreventInvalidTyping(true);
		
		
		cmbCpu = binder.buildAndBind("CPU","cpu",ComboBox.class);
		//Vaadin8-setImmediate() cmbCpu.setImmediate(true);
		
		cmbType =  binder.buildAndBind("Type", "type", ComboBox.class);
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		
		cmbRRCRequestType = binder.buildAndBind("RRC Request Type","rrcRequestType",ComboBox.class);
		cmbEligiblityType = binder.buildAndBind("Eligibility Type","eligibilityType",ComboBox.class);
		fromDate = binder.buildAndBind("From Date","fromDate",DateField.class);
		toDate = binder.buildAndBind("To Date","toDate",DateField.class);
		dummyField = new Label();
		dummyField.setVisible(true);
		dummyField.setReadOnly(true);
		
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cmbCpu,fromDate,cmbPriority,cmbType);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(txtRRCRequestNo,cmbRRCRequestType,cmbEligiblityType,toDate,cmbSource);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:225.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:225.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("275px");
		
		addListener();
		
		setCmbxValues();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchReviewRRCRequestFormDTO>(SearchReviewRRCRequestFormDTO.class);
		this.binder.setItemDataSource(new SearchReviewRRCRequestFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setDropDownValues(BeanItemContainer<SelectValue> cpu,
			BeanItemContainer<SelectValue> rrcRequestType, BeanItemContainer<SelectValue> rrcEligiblity) 
	{
		cmbCpu.setContainerDataSource(cpu);
		cmbCpu.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpu.setItemCaptionPropertyId("value");
		
		
		cmbRRCRequestType.setContainerDataSource(rrcRequestType);
		cmbRRCRequestType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRRCRequestType.setItemCaptionPropertyId("value");
		
		cmbEligiblityType.setContainerDataSource(rrcEligiblity);
		cmbEligiblityType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbEligiblityType.setItemCaptionPropertyId("value");
				
	}	
	
	 public void setCmbxValues(){
			
			BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
			
			cmbType.setContainerDataSource(selectValueForType);
			cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbType.setItemCaptionPropertyId("value");
			
			BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
			
			cmbPriority.setContainerDataSource(selectValueForPriority);
			cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbPriority.setItemCaptionPropertyId("value");
			
//			Status statusByKey = preauthService.getStatusByKey(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS); 
			
//			Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);
			BeanItemContainer<SelectValue> stageList = masterService.getStageList();
			
			cmbSource.setContainerDataSource(stageList);
			cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbSource.setItemCaptionPropertyId("value");
			
			stageList.sort(new Object[] {"value"}, new boolean[] {true});
		}
	
	/*private void cbxhospitalListener(){
		cbxhospitalType.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
			
				
				
				if(cbxhospitalType.getValue() != null){
					System.out.println("ggggggggggggggggggggggg"+cbxhospitalType.getValue());
				if(  ReferenceTable.HOSPITAL_NETWORK.equals(cbxhospitalType.getValue().toString())){
					cbxNetworkHospType.setContainerDataSource(networkHospitalType);
					cbxNetworkHospType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cbxNetworkHospType.setItemCaptionPropertyId("value");
				}else{
					cbxNetworkHospType.setContainerDataSource(null);
				}
			}else{
					cbxNetworkHospType.setContainerDataSource(null);
				}
			}
			});
	}
	*/

}
