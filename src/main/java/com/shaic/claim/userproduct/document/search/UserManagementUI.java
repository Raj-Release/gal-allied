package com.shaic.claim.userproduct.document.search;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.claim.userproduct.document.search.SearchDoctorDetailsTableDTO;
import com.shaic.claim.userproduct.document.search.SearchDoctorNameDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class UserManagementUI extends SearchComponent<SearchDoctorNameDTO>{
	
	@Inject
	private UserManagementCreateUserUI viewSearchCriteria;
	

	private TextField doctorName;
	
	private Button createUser;
	
	public static Window popup;
	
	
	private Panel mainPanel;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("User Mangement");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		//btnSearch.setDisableOnClick(true);
		btnReset.setCaption("Reset");
		createUser = new Button("Create User");
		createUser.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		/*btnSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				doctorSearchCriteriaServiceObj.search(doctorName.getValue());
			}
		});*/
		createUser.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				
				popup = new com.vaadin.ui.Window();
				
				viewSearchCriteria.initView(popup);
				
				popup.setWidth("20%");
				popup.setHeight("30%");
				popup.setContent(viewSearchCriteria);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(true);
				popup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
						popup.close();
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			
				
			}
		});
		mainVerticalLayout = new VerticalLayout();
		
		doctorName = binder.buildAndBind("User ID / Name", "doctorName", TextField.class);
		doctorName.setNullSettingAllowed(false);
		doctorName.setMaxLength(15);
		CSValidator doctorNameValidator = new CSValidator();
		doctorNameValidator.extend(doctorName);
		doctorNameValidator.setRegExp("^[a-zA-Z 0-9]*$");
		doctorNameValidator.setPreventInvalidTyping(true);
		
		btnSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				
			}
		});
		
		
		
		FormLayout formLayoutLeft = new FormLayout(doctorName);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:80.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:80.0px;left:329.0px;");
		absoluteLayout_3.addComponent(createUser, "top:80.0px;left:429.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("150px");
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchDoctorNameDTO>(SearchDoctorNameDTO.class);
		this.binder.setItemDataSource(new SearchDoctorNameDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}	
	
	public void setPanelCaption(){
		if(mainPanel != null){
			mainPanel.setCaption("User Management");
		}
	}
	

	public void setDoctorNameValue(SearchDoctorDetailsTableDTO viewSearchCriteriaDTO) {
		doctorName.setValue(viewSearchCriteriaDTO.getDoctorName());
	}
	public void setUserId(SearchDoctorDetailsTableDTO viewSearchCriteriaDTO){
		doctorName.setValue(viewSearchCriteriaDTO.getEmpId());
	}
	private void showErrorMessage(String eMsg) {
	
			MessageBox.createError()
	    	.withCaptionCust("Error").withHtmlMessage(eMsg.toString())
	        .withOkButton(ButtonOption.caption("OK")).open();
		
	}
}
