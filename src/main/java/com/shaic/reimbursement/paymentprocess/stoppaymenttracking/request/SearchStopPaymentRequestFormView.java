package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.navigator.View;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchStopPaymentRequestFormView extends SearchComponent<StopPaymentRequestFormDTO> {
	

	
	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();
	
	private Button searchButton;

	private TextField uprNumber;

	private TextField intimationNumber;

	private static final long serialVersionUID = 1L;
	

	@Inject
	protected StopPaymentRequestDetailTable stopPaymentRequestDetailTable;

	@PostConstruct
	public void init() {

		initBinder();
		setSizeFull();
		//setHeight("450px");
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Stop Payment Request");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	

	}

	
	@SuppressWarnings("deprecation")
	private void initBinder() {
		this.binder = new BeanFieldGroup<StopPaymentRequestFormDTO>(StopPaymentRequestFormDTO.class);
		this.binder.setItemDataSource(new StopPaymentRequestFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	@SuppressWarnings("deprecation")
	 private VerticalLayout mainVerticalLayout() {

		
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setWidth("100.0%");
		mainVerticalLayout.setMargin(false);		 
//		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("150px");

		
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		
		
		intimationNumber = binder.buildAndBind("Intimation Number","intimationNo",TextField.class);
		
		intimationNumber.setWidth("160px");
		
		intimationNumber.setTabIndex(1);
		intimationNumber.setHeight("-1px");
		intimationNumber.setMaxLength(30);
		CSValidator intimationNumValidator = new CSValidator();
		intimationNumValidator.extend(intimationNumber);
		intimationNumValidator.setPreventInvalidTyping(true);
		intimationNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");

		uprNumber = binder.buildAndBind("Cheque / DD Number","utrNumber",TextField.class);
		uprNumber.setWidth("160px");
		uprNumber.setTabIndex(9);
		uprNumber.setHeight("-1px");
		uprNumber.setMaxLength(25);

		CSValidator claimNumValidator = new CSValidator();
		claimNumValidator.extend(uprNumber);
		claimNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		claimNumValidator.setPreventInvalidTyping(true);

		FormLayout formLayoutLeft = new FormLayout(intimationNumber);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(uprNumber);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		
		absoluteLayout_3.addComponent(fieldLayout);
		
		absoluteLayout_3.addComponent(btnSearch, "top:90.0px;left:280.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:90.0px;left:380.0px;");
		
		mainVerticalLayout.addComponent(absoluteLayout_3);

		
		addListener();
		return mainVerticalLayout;
		
	
	}

	


	public void showErrorPopup(String errorMessage) {
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(errorMessage, buttonsNamewithType);
		
	}
	

	public void searchButtonDisable() {
		searchButton.setEnabled(false);
	}

	@SuppressWarnings("deprecation")
	public String validate(StopPaymentRequestFormDTO searchDTO) {
		String err = null;
		if(searchDTO !=null){
			if((intimationNumber != null && intimationNumber.getValue() == null ||(intimationNumber.getValue() !=null && intimationNumber.getValue().toString().length() ==0 )) && 
					(uprNumber != null && uprNumber.getValue() == null ||( uprNumber.getValue() != null &&  uprNumber.getValue().toString().length() == 0)))
			{
				 err= "Intimation or UTR/Cheque/DD Number is mandatory for search";
			}
			
			
		}
		return err;
		
	}





}
