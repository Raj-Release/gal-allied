package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


public class IntimationSearchUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7397444425570391633L;
	
	private TextField txtIntimationNo;
	
	private TextField txtPolicyNo;
	
	private Button btnSearch;
	
	@Inject
	private SearchIntimationNoSearchTable searchIntimationNoSearchTable;

	private BeanFieldGroup<IntimationSearchDTO> binder;

	
	private void initIntimationSearch(){
		
		

		initBinder();
		
		txtIntimationNo = binder.buildAndBind("Intimation no", "intimationNo", TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy no", "policyNo", TextField.class);
		
		btnSearch = new Button("Search");
		btnSearch.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		FormLayout leftLayout = new FormLayout(txtIntimationNo);
		
		FormLayout rightLayout = new FormLayout(txtPolicyNo);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(leftLayout,rightLayout);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setComponentAlignment(btnSearch, Alignment.BOTTOM_RIGHT);
		searchIntimationNoSearchTable.init("", false, false);
		//searchRepresentativeNameSearchTable.setPresenterString(presenterString);
		VerticalLayout mainLayout = new VerticalLayout(horizontalLayout, searchIntimationNoSearchTable);
		mainLayout.setMargin(true);
		Panel mainPanel = new Panel();
		mainPanel.setContent(mainLayout);
		setCompositionRoot(mainPanel);
//		cmbCity.setEnabled(false);
//		cmbAllocationTo.setEnabled(false);
//		cmbBrachOffice.setEnabled(false);
		//addListener();
		
		
		
	
		
		
		
		
		
	}


	public void initBinder() {
		this.binder = new BeanFieldGroup<IntimationSearchDTO>(
				IntimationSearchDTO.class);
		this.binder
				.setItemDataSource(new IntimationSearchDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
}
