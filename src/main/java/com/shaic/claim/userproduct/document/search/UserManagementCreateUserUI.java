package com.shaic.claim.userproduct.document.search;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.claim.userproduct.document.ApplicableCpuDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
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
import com.vaadin.ui.themes.ValoTheme;

public class UserManagementCreateUserUI  extends SearchComponent<SearchDoctorNameDTO> {
	
	private Window popup;
	
	private TextField userId;
	
	private Panel mainPanel;
	
	private Button submit;
	
	private Button reset;
	
	@PostConstruct
	public void init(){
		
	}
	
	public void initView(Window popup) {
		initBinder();
		mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("User Mangement");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	public VerticalLayout mainVerticalLayout(){
		
		mainVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("10%");
		 mainVerticalLayout.setHeight("30%");
		 mainVerticalLayout.setMargin(false);		
		userId = binder.buildAndBind("User ID", "empId", TextField.class);
		userId.setMaxLength(9);
		CSValidator userIdValidator = new CSValidator();
		userIdValidator.extend(userId);
		userIdValidator.setRegExp("^[A-Z0-9]*$");
		userIdValidator.setPreventInvalidTyping(true);
		submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submit.setDisableOnClick(false);
		reset = new Button("Reset");
		reset.setDisableOnClick(true);
		reset.addStyleName(ValoTheme.BUTTON_DANGER);
		reset.setDisableOnClick(false);
		
		reset.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				userId.setValue(null);
				
			}
		});
		submit.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
			if(userId != null && userId.getValue() != null && !userId.isEmpty()) {
				
				char validateUserId[] = userId.getValue().toCharArray();
				if (validateUserId.length == 8 || validateUserId.length == 9) {
					
					 if(validateUserId.length == 8) {
						for(int i=0;i<validateUserId.length;i++) {
						int count=0;
						if(validateUserId[i] == 'G' && validateUserId[i+1] == 'E') {
						for(int j=2;j<validateUserId.length;j++) {
							if(!Character.isDigit(validateUserId[j])) {
								showErrorMessage("Please enter a valid User ID");
								count++;
								break;
							}
						}
						if(count==0) {
							fireViewEvent(UserManagementPresenter.CREATE_USER_CLICK,
									userId.getValue());
							userId.setValue(null);
						}
						}else if(validateUserId[i] == 'S' && validateUserId[i+1] == 'H') {
							for(int j=2;j<validateUserId.length;j++) {
								if(!Character.isDigit(validateUserId[j])) {
									showErrorMessage("Please enter a valid User ID");
									count++;
									break;
								}
							}
							if(count==0) {
								fireViewEvent(UserManagementPresenter.CREATE_USER_CLICK,
										userId.getValue());
								userId.setValue(null);
							}
							}else if(validateUserId[i] == 'C' && validateUserId[i+1] == 'S') {
								for(int j=2;j<validateUserId.length;j++) {
									if(!Character.isDigit(validateUserId[j])) {
										showErrorMessage("Please enter a valid User ID");
										count++;
										break;
									}
								}
								if(count==0) {
									fireViewEvent(UserManagementPresenter.CREATE_USER_CLICK,
											userId.getValue());
									userId.setValue(null);
								}
								}else if(validateUserId[i] == 'G' && validateUserId[i+1] == 'C') {
									for(int j=2;j<validateUserId.length;j++) {
										if(!Character.isDigit(validateUserId[j])) {
											showErrorMessage("Please enter a valid User ID");
											count++;
											break;
										}
									}
									if(count==0) {
										fireViewEvent(UserManagementPresenter.CREATE_USER_CLICK,
												userId.getValue());
										userId.setValue(null);
									}
									}else{
								showErrorMessage("Please enter a valid User ID");
							}
						break;
						}
					}else if(validateUserId.length == 9) {
						int count=0;
						for(int i=0;i<validateUserId.length;i++) {
							if(validateUserId[i] != 'G' && validateUserId[i+1] != 'E' && validateUserId[i+2] != 'G') {
								showErrorMessage("Please enter a valid User ID");
								break;
							}
							for(int j=3;j<validateUserId.length;j++) {
								if(!Character.isDigit(validateUserId[j])) {
									showErrorMessage("Please enter a valid User ID");
									count++;
									break;
								}
							}
							if(count==0) {
									fireViewEvent(UserManagementPresenter.CREATE_USER_CLICK,
											userId.getValue());
									userId.setValue(null);
							}else{
								showErrorMessage("Please enter a valid User ID");
							}
							break;
						}
						
					}
				}else{
					showErrorMessage("Please enter a valid User ID");
				}
				}else{
					showErrorMessage("Please enter a User ID");
				}
				
			}
		});
		
		FormLayout formLayoutLeft = new FormLayout(userId);
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(submit, "top:80.0px;left:100.0px;");
		absoluteLayout_3.addComponent(reset, "top:80.0px;left:209.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("100%");
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
	public void setUserValue(SearchDoctorDetailsTableDTO viewSearchCriteriaDTO) {
		userId.setValue(viewSearchCriteriaDTO.getEmpId());
	}
	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

	
	
}
