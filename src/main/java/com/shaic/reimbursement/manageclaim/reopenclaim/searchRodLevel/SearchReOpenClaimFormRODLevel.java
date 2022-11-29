/**
 * 
 */
package com.shaic.reimbursement.manageclaim.reopenclaim.searchRodLevel;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
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
public class SearchReOpenClaimFormRODLevel extends SearchComponent<SearchReOpenClaimFormDTORODLevel> {
	
	@Inject
	private SearchReOpenClaimRODLevelTable searchAcknowledgementDocumentReceiverTable;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private TextField txtClaimNo;
	
	private ComboBox cmbType;
	private ComboBox cmbPriority;
	
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
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtClaimNo/*,cmbPriority*/);
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo,cmbType);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:110.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:110.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("160px");
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchReOpenClaimFormDTORODLevel>(SearchReOpenClaimFormDTORODLevel.class);
		this.binder.setItemDataSource(new SearchReOpenClaimFormDTORODLevel());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}	
}