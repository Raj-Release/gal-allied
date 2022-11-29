package com.shaic.claim.rod.searchCriteria;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ViewSearchCriteriaForm extends SearchComponent<ViewSearchCriteriaFormDTO> {

	private TextField txtIFSCCode;
	
	private TextField txtBankName;
	
	private TextField txtBranchName;
	
	
	private Button btnClose;
	Window popup;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel(formLayout());
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("");
		setCompositionRoot(mainPanel);
		
	}
	private VerticalLayout formLayout(){
		btnClose = new Button("Close");
		closeWindow();
		HorizontalLayout fieldLayout = new HorizontalLayout();
		txtIFSCCode = binder.buildAndBind("IFSC Code","ifcsCode",TextField.class); 
		txtBankName = binder.buildAndBind("Bank Name","bankName",TextField.class); 
		txtBranchName = binder.buildAndBind("Branch Name","branchName",TextField.class); 
		fieldLayout.addComponents(txtIFSCCode, txtBankName, txtBranchName);
		fieldLayout.setSpacing(true);
		mainVerticalLayout = new VerticalLayout();
		HorizontalLayout btnLayout = new HorizontalLayout();
		btnLayout.setSpacing(true);
		btnLayout.addComponents(btnSearch, btnReset, btnClose);
		mainVerticalLayout.setWidth("100%");
		mainVerticalLayout.setHeight("200px");
		mainVerticalLayout.addComponents(fieldLayout,btnLayout);
		mainVerticalLayout.setComponentAlignment(fieldLayout, Alignment.MIDDLE_CENTER);
		mainVerticalLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
		mainVerticalLayout.setMargin(true);
		addListener();
		return mainVerticalLayout;

		}
	
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<ViewSearchCriteriaFormDTO>(ViewSearchCriteriaFormDTO.class);
		this.binder.setItemDataSource(new ViewSearchCriteriaFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}	
	public void setWindowObject(Window popup){
		this.popup = popup;
	}
	public void closeWindow(){
		btnClose.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
				
			}
		});;
	}
	
}
