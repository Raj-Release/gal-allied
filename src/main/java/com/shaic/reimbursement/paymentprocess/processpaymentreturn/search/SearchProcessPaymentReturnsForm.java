/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.processpaymentreturn.search;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessPaymentReturnsForm extends SearchComponent<SearchProcessPaymentReturnsFormDTO> {
	
	@Inject
	private SearchProcessPaymentReturnsTable searchAcknowledgementDocumentReceiverTable;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private TextField txtRODNo;
	private TextField txtClaimNo;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process Payment Returns");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("148px");
		 
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		txtRODNo = binder.buildAndBind("ROD No","rodNo",TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtRODNo);
		formLayoutLeft.setMargin(true);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo,txtClaimNo);	
		formLayoutReight.setMargin(true);
		formLayoutReight.setSpacing(true);
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
		absoluteLayout_3.addComponent(fieldLayout);
		absoluteLayout_3
		.addComponent(btnSearch, "top:100.0px;left:250.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:100.0px;left:359.0px;");
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessPaymentReturnsFormDTO>(SearchProcessPaymentReturnsFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessPaymentReturnsFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}	
}