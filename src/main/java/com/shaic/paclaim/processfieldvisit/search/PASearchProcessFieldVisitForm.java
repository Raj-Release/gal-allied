/**
 * 
 */
package com.shaic.paclaim.processfieldvisit.search;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Stage;
import com.shaic.reimbursement.investigation.ackinvestivationcompleted.search.SearchAckInvestigationCompletedTable;
import com.shaic.reimbursement.processfieldvisit.search.SearchProcessFieldVisitFormDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narenj
 *
 */
public class PASearchProcessFieldVisitForm extends SearchComponent<SearchProcessFieldVisitFormDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SearchAckInvestigationCompletedTable searchTable;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxCPUCode;
	
	private ComboBox cmbType;
	//private ComboBox cmbPriority;
	
	private ComboBox cmbAccident;
	
	private ComboBox cmbHospitalType;
	
	private ComboBox cmbProductCode;
	
	private ComboBox cmbSource;
	
	private ComboBox cmbfvrCode;
	
	private PopupDateField intimationDateField;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process Field Visit");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSearch.setImmediate(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		
		//cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		cmbAccident = binder.buildAndBind("Accident/Death","priority",ComboBox.class);
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		cbxCPUCode.setNullSelectionAllowed(false);
		
		cmbHospitalType = binder.buildAndBind("Hospital Type","hospitalType",ComboBox.class);
		
		cmbProductCode = binder.buildAndBind("Product Code","productCode",ComboBox.class);
		
		cmbfvrCode = binder.buildAndBind("Fvr CPU Code","fvrCpuCode",ComboBox.class);
		
		intimationDateField = (PopupDateField) binder.buildAndBind("Date of Admission", "intimatedDate",
				PopupDateField.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cbxCPUCode,cmbHospitalType,cmbfvrCode,cmbAccident);
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo,cmbType,cmbProductCode,cmbSource,intimationDateField);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:190.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:190.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("750px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("250px");
		addListener();
		
		setDropDownValues();
		
		return mainVerticalLayout;
	}
	
	 public void setDropDownValues(){
			
			BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
			
			cmbType.setContainerDataSource(selectValueForType);
			cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbType.setItemCaptionPropertyId("value");
			
			BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
			
			//cmbPriority.setContainerDataSource(selectValueForPriority);
			//cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			//cmbPriority.setItemCaptionPropertyId("value");
			
			Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);
			Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);
//			Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

			SelectValue selectValue1 = new SelectValue();
			selectValue1.setId(stageByKey2.getKey());
			selectValue1.setValue(stageByKey2.getStageName());
			
			SelectValue selectValue2 = new SelectValue();
			selectValue2.setId(stageByKey3.getKey());
			selectValue2.setValue(stageByKey3.getStageName());
			
//			SelectValue selectValue2 = new SelectValue();
//			selectValue2.setId(stageByKey3.getKey());
//			selectValue2.setValue(stageByKey3.getStageName());
			
			BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
			statusByStage.addBean(selectValue1);
			statusByStage.addBean(selectValue2);
			
			cmbSource.setContainerDataSource(statusByStage);
			cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbSource.setItemCaptionPropertyId("value");
			
			BeanItemContainer<SelectValue> hospitalTypeValue = masterService
					.getMasterValueByReference(ReferenceTable.HOSPITAL_TYPE);
			
			cmbHospitalType.setContainerDataSource(hospitalTypeValue);
			cmbHospitalType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbHospitalType.setItemCaptionPropertyId("value");
			
			BeanItemContainer<SpecialSelectValue> productNameValue = masterService.getContainerForProduct();
			cmbProductCode.setContainerDataSource(productNameValue);
			cmbProductCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbProductCode.setItemCaptionPropertyId("specialValue");
			
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
					.getTmpCpuCodes();
			
			cmbfvrCode.setContainerDataSource(selectValueContainerForCPUCode);
			cmbfvrCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbfvrCode.setItemCaptionPropertyId("value"); 
		}

	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessFieldVisitFormDTO>(SearchProcessFieldVisitFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessFieldVisitFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}	
	
	public void setCPUCode(BeanItemContainer<SelectValue> parameter){
		cbxCPUCode.setContainerDataSource(parameter);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
	}
}