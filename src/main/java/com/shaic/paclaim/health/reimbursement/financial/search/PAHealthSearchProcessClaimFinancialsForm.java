/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.financial.search;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.domain.MasterService;
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

/**
 * @author ntv.narenj
 *
 */
public class PAHealthSearchProcessClaimFinancialsForm extends SearchComponent<PAHealthSearchProcessClaimFinancialsFormDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PAHealthSearchProcessClaimFinancialsTable searchTable;
	
	@EJB
	private MasterService masterService;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxType;
	private ComboBox cmbCpuCode;
	private ComboBox cmbClaimType;
	private ComboBox cmbProductName;
	private ComboBox cmbPriority;
	
	private ComboBox cmbSource;
	
	private TextField txtClaimedAmountFrom;
	private TextField txtClaimedAmountTo;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process Claim Financials");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cbxType = binder.buildAndBind("Type","type",ComboBox.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		cmbCpuCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		cmbProductName = binder.buildAndBind("Product Name/Code","productName",ComboBox.class);
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		txtClaimedAmountFrom = binder.buildAndBind("Claimed Amount From", "claimedAmountFrom", TextField.class);
		txtClaimedAmountFrom.setNullRepresentation("");
		
		CSValidator validator = new CSValidator();
		validator.extend(txtClaimedAmountFrom);
		validator.setRegExp("^[0-9.]*$");
		validator.setPreventInvalidTyping(true);
		
		txtClaimedAmountTo = binder.buildAndBind("Claimed Amount To", "claimedAmountTo", TextField.class);
		txtClaimedAmountTo.setNullRepresentation("");
		
		CSValidator validator1 = new CSValidator();
		validator1.extend(txtClaimedAmountTo);
		validator1.setRegExp("^[0-9.]*$");
		validator1.setPreventInvalidTyping(true);
		
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cbxType,cmbProductName,cmbPriority, txtClaimedAmountFrom);
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo,cmbCpuCode,cmbClaimType,cmbSource, txtClaimedAmountTo);	
		formLayoutReight.setMargin(true);
	
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
		 
		 absoluteLayout_3.setHeight("242px");
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PAHealthSearchProcessClaimFinancialsFormDTO>(PAHealthSearchProcessClaimFinancialsFormDTO.class);
		this.binder.setItemDataSource(new PAHealthSearchProcessClaimFinancialsFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setUpDropDownValues(BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> productName1,
			BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		
		cmbCpuCode.setContainerDataSource(cpuCode);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");
		
		
		cmbClaimType.setContainerDataSource(claimType);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
		
	//	BeanItemContainer<SpecialSelectValue> productName = masterService.getContainerForProduct();
		BeanItemContainer<SpecialSelectValue> productName = masterService.getContainerForProductByLineOfBusiness(SHAConstants.PA_LOB);
		cmbProductName.setContainerDataSource(productName);
		cmbProductName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbProductName.setItemCaptionPropertyId("specialValue");
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cbxType.setContainerDataSource(selectValueForType);
		cbxType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxType.setItemCaptionPropertyId("value");
		
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");

	}	
}