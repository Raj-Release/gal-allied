/**
 * 
 */
package com.shaic.paclaim.billing.processclaimbilling.search;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.domain.MasterService;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingFormDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;


public class PASearchProcessClaimBillingForm extends SearchComponent<SearchProcessClaimBillingFormDTO> {
	
	@Inject
	private PASearchProcessClaimBillingTable searchTable;
	
	@EJB
	private MasterService masterService;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxCPUCode;
	private ComboBox cbxType;
	private ComboBox cbxProductNameCode;
	private ComboBox cmbPriority;
	private ComboBox cmbCoverBenefits;
	
	
	
	private ComboBox cmbSource;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process Claim Billing - Non Hospitalisation");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		cbxType = binder.buildAndBind("Type","type",ComboBox.class);
		cbxProductNameCode = binder.buildAndBind("Product Name/Code","productNameCode",ComboBox.class);
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		cmbCoverBenefits= binder.buildAndBind("Covers/Benefits","coverBenefits",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtPolicyNo,cbxCPUCode,cmbSource);
		FormLayout formLayoutReight = new FormLayout(cbxType, cbxProductNameCode,cmbPriority,cmbCoverBenefits);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:190.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:190.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("700px");
		// mainVerticalLayout.setHeight("500px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("243px");
		
		
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessClaimBillingFormDTO>(SearchProcessClaimBillingFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessClaimBillingFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCBXValue(BeanItemContainer<SelectValue> cpucode,
			BeanItemContainer<SelectValue> productNameCode, BeanItemContainer<SelectValue> typeContainer, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> coverContainer) {
		
		cbxCPUCode.setContainerDataSource(cpucode);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SpecialSelectValue> productName = masterService.getContainerForProductByLineOfBusiness(SHAConstants.PA_LOB);
		
		cbxProductNameCode.setContainerDataSource(productName);
		cbxProductNameCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxProductNameCode.setItemCaptionPropertyId("specialValue");
		
//		cbxProductNameCode.setContainerDataSource(typeContainer);
//		cbxProductNameCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cbxProductNameCode.setItemCaptionPropertyId("value");
		
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cbxType.setContainerDataSource(selectValueForType);
		cbxType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxType.setItemCaptionPropertyId("value");
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
		
		cmbCoverBenefits.setContainerDataSource(coverContainer);
		cmbCoverBenefits.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCoverBenefits.setItemCaptionPropertyId("value");
	}	
}