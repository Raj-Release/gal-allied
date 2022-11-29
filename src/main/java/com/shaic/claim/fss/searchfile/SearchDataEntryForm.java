/**
 * 
 */
package com.shaic.claim.fss.searchfile;

import javax.annotation.PostConstruct;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * 
 *
 */
public class SearchDataEntryForm  extends SearchComponent<SearchDataEntryFormDTO> {
	
	private TextField txtClaimNo;
	private TextField txtChequeNo;
	private TextField txtPatientName;
	private Button addButton;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Search Data Entry Form");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtClaimNo = binder.buildAndBind("Claim No", "claimNo", TextField.class);
		txtClaimNo.setMaxLength(25);
		CSValidator validator = new CSValidator();
		validator.extend(txtClaimNo);
		validator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		validator.setPreventInvalidTyping(true);
		
		txtChequeNo = binder.buildAndBind("Cheque No", "chequeNo", TextField.class);
		txtChequeNo.setMaxLength(25);
		CSValidator validator1 = new CSValidator();
		validator1.extend(txtChequeNo);
		validator1.setRegExp("^[a-z A-Z 0-9 /.]*$");
		validator1.setPreventInvalidTyping(true);
		
		txtPatientName = binder.buildAndBind("Patient Name", "patientName", TextField.class);
		CSValidator validator2 = new CSValidator();
		validator2.extend(txtPatientName);
		validator2.setRegExp("^[a-z A-Z 0-9 /.]*$");
		validator2.setPreventInvalidTyping(true);
		
		addButton = new Button("Add");
		
		FormLayout formLayoutLeft = new FormLayout(txtClaimNo,txtChequeNo);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(txtPatientName);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");	
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:110.0px;left:190.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:110.0px;left:299.0px;");
		absoluteLayout_3.addComponent(addButton, "top:110.0px;left:408.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("600px");
		 mainVerticalLayout.setMargin(false); 
		 mainVerticalLayout.setHeight("140px");
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("270px");
		
		addListener();
		addButtonListener();
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchDataEntryFormDTO>(SearchDataEntryFormDTO.class);
		this.binder.setItemDataSource(new SearchDataEntryFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	private void addButtonListener() {
		addButton.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				SearchDataEntryTableDTO tableDto = new SearchDataEntryTableDTO();
				String userName = (String) getUI().getSession().getAttribute(
						BPMClientContext.USERID);
				if (userName != null) {
					tableDto.setUsername(userName);
				}
				fireViewEvent(MenuPresenter.SHOW_DATA_ENTRY_DETAILS, tableDto,
						null, SHAConstants.ADD_DATA_ENTRY);
			}
		});
	}
	
	public String validate() {
		String err = null;

		if ((txtClaimNo.getValue() == null || txtClaimNo.getValue().equals(""))
				&& (txtPatientName.getValue() == null || txtPatientName
						.getValue().equals(""))
				&& (txtChequeNo.getValue() == null || txtChequeNo.getValue()
						.equals(""))) {

			return err = "Any one of the field is Mandatory";
		}

		return err;

	}

}

