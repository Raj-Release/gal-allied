package com.shaic.claim.rod.wizard.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.Validator;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.rod.citySearchCriteria.CitySearchCriteriaViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.shaic.restservices.bancs.triggerPartyEndorsement.TriggerPartyEndorsementResponse;
import com.shaic.restservices.bancs.triggerPartyEndorsement.TriggerPartyEndorsementService;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class AddBankDetailsTable extends ViewComponent{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<AddBanksDetailsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<AddBanksDetailsTableDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<AddBanksDetailsTableDTO> container = new BeanItemContainer<AddBanksDetailsTableDTO>(AddBanksDetailsTableDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Set<String> errorMessages;
	
	private static Validator validator;
	
	private Map<String, Object> referenceData;
	
	//This value will be used for validation.
	public Double totalBillValue;
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	private String presenterString = "";
	
	private ViewDetails objViewDetails;
	
	@Inject
	private Instance<AddBankDetailsTable> addBanksDetailsTableInstance;
	
	private AddBankDetailsTable addBanksDetailsTableObj;
	

	@Inject
	private CitySearchCriteriaViewImpl citySearchCriteriaWindow;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	private AddBankDetailsTable currentObj;
	
	private Panel panelLayout;
	
	private TextField ifscCode;
	
	private TextField nameOfBank;

	private TextField nameAsPerBankAC;

	private TextField branchName;

	private TextField accountNumber;

	private TextField micrCode;

	private TextField panNo;

	private TextField virtualPaymentAddr;

	private Button submit;
	
	private Button cancel;
	
	private ComboBox preferenceCombo;
	
	private TextField accountType;
	
	private VerticalLayout mainLayout;
	
	private PopupDateField effectiveFromDate;
	
	private PopupDateField effectiveToDate;

	private BeanFieldGroup<AddBanksDetailsTableDTO> binder;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	
	private Button btnIFCSSearch;
	
	private BankDetailsTable bankDetail;
	
	private String operationCode;
	
	private List<BankDetailsTableDTO> exitingData;
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void setParentObject(BankDetailsTable bankDetail) {
		this.bankDetail = bankDetail;
	}
	
	
	public BankDetailsTable getBankDetailObj() {
		return bankDetail;
	}

	public void setBankDetailObj(BankDetailsTable bankDetail) {
		this.bankDetail = bankDetail;
	}

	public void init(AddBanksDetailsTableDTO addBanksDetailsTableDTO, Window popup, ReceiptOfDocumentsDTO dto,String operationCode, String seqNo) {
		initBinder(addBanksDetailsTableDTO);
		this.currentObj = this;
		this.operationCode = operationCode;
		mainLayout = new VerticalLayout();
		
		preferenceCombo = binder.buildAndBind("Preference" , "preference" , ComboBox.class);
		BeanItemContainer<SelectValue> cmbAuditFinalStatusContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(addBanksDetailsTableDTO.getPreference() != null){
			SelectValue prefernce = new SelectValue();
			prefernce.setValue(addBanksDetailsTableDTO.getPreference().getValue());
			cmbAuditFinalStatusContainer.addBean(prefernce);
			preferenceCombo.setValue(cmbAuditFinalStatusContainer);
			preferenceCombo.setReadOnly(true);
		}else{
			addPreferenceValue(preferenceCombo);
		}
		
//		preferenceCombo.addValueChangeListener(new Property.ValueChangeListener() {
//			
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				
//				SelectValue prefSelect = event.getProperty().getValue() != null ? (SelectValue)event.getProperty().getValue() : null;
//				
//				if(exitingData != null && !exitingData.isEmpty()) {
//					boolean prefAvailable = false;
//					for (BankDetailsTableDTO bankDetailsTableDTO : exitingData) {
//						if(bankDetailsTableDTO.getAccountType().equalsIgnoreCase(prefSelect.getValue()))
//							prefAvailable = true;						
//					}
//				}
//			}
//		});
		
	
		ifscCode = (TextField) binder.buildAndBind("IFSC Code","ifscCode",TextField.class);
		ifscCode.setMaxLength(11);
		ifscCode.setNullRepresentation("");
		
		accountType = (TextField) binder.buildAndBind("Account Type","accountType",TextField.class);
		accountType.setMaxLength(20);
		accountType.setNullRepresentation("");
		
		nameOfBank = binder.buildAndBind("Name Of Bank","nameOfBank",TextField.class);
		nameOfBank.setNullRepresentation("");
		nameOfBank.setMaxLength(30);

		nameAsPerBankAC = binder.buildAndBind("Name As Per BankAC","nameAsPerBankAC",TextField.class);
		nameAsPerBankAC.setNullRepresentation("");
		nameAsPerBankAC.setMaxLength(200);

		accountNumber = binder.buildAndBind("Account Number","accountNumber",TextField.class);
		accountNumber.setNullRepresentation("");
		accountNumber.setMaxLength(20);

		branchName = binder.buildAndBind("Branch Name","branchName",TextField.class);
		branchName.setNullRepresentation("");
		branchName.setMaxLength(30);

		micrCode = binder.buildAndBind("MICR Code","micrCode",TextField.class);
		micrCode.setNullRepresentation("");
		micrCode.setMaxLength(15);

		/*panNo = binder.buildAndBind("PAN No","panNo",TextField.class);
		panNo.setNullRepresentation("");
		panNo.setMaxLength(10);

		virtualPaymentAddr = binder.buildAndBind("Virtual Payment Address","virtualPaymentAddr",TextField.class);
		virtualPaymentAddr.setNullRepresentation("");
		virtualPaymentAddr.setMaxLength(100);*/

		effectiveFromDate = binder.buildAndBind("Effective From Date", "effectiveFromDate", PopupDateField.class);
		//effectiveFromDate.setValue(new Date());
		effectiveFromDate.setEnabled(true);
		effectiveFromDate.setTextFieldEnabled(false);
		
		btnIFCSSearch = new Button();
		btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
		setupIfscDetails();
		
		effectiveToDate = binder.buildAndBind("Effective To Date", "effectiveToDate", PopupDateField.class);
		//effectiveToDate.setValue(new Date());
		effectiveToDate.setEnabled(true);
		effectiveToDate.setTextFieldEnabled(false);
		
		mandatoryFields.add(preferenceCombo);
		mandatoryFields.add(nameAsPerBankAC);
		mandatoryFields.add(accountType);
		mandatoryFields.add(accountNumber);
		mandatoryFields.add(ifscCode);
		
		showOrHideValidation(false);


		submit = new Button();
		submit.setCaption("Submit");
		submit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submit.setWidth("-1px");
		submit.setHeight("-10px");
		
		cancel = new Button("Cancel");
		cancel.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		cancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
			}
		});
		
		submit = new Button("Submit");
		submit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submit.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(isValid()){
				TriggerPartyEndorsementResponse callTriggerEndorsement = TriggerPartyEndorsementService.getInstance().callTriggerEndorsement(getBankDTO(),dto.getSourceRiskID(),operationCode,seqNo);
					if(callTriggerEndorsement != null && callTriggerEndorsement.getStatus().equalsIgnoreCase(SHAConstants.SUCCESS_STATUS)){
						buildSuccessLayout(dto);
						popup.close();
					}else{
						buildFailureLayout(callTriggerEndorsement != null?callTriggerEndorsement.getErrorDescription():"");
					}
				}
			}
//			buildSuccessLayout();
		});
		
		FormLayout formLayoutLeft = new FormLayout(preferenceCombo,accountType,nameAsPerBankAC,accountNumber/*,panNo*/,effectiveFromDate);
		FormLayout formLayoutRight = new FormLayout(ifscCode,nameOfBank,branchName,micrCode/*,virtualPaymentAddr*/,effectiveToDate);
		FormLayout formLayout = new FormLayout(btnIFCSSearch);

		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight,formLayout);		
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		fieldLayout.setHeight("100%");
		
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
	    absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(submit, "top:250.0px;left:320.0px;");
		absoluteLayout_3.addComponent(cancel, "top:250.0px;left:429.0px;");
		
		mainLayout.addComponent(absoluteLayout_3);
		mainLayout.setWidth("100%");
		mainLayout.setMargin(false);		 
		absoluteLayout_3.setWidth("100.0%");
	    absoluteLayout_3.setHeight("280px");	 
		 
		panelLayout = new Panel();
		panelLayout.setContent(mainLayout);
		panelLayout.setWidth("100%");
		
		setCompositionRoot(panelLayout);
}
	

	private boolean isValid() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();						
		if(preferenceCombo !=null && preferenceCombo.getValue() ==null){
			hasError = true;
			eMsg.append("Account Preference is Mandatory. </br>");	
		}
		if(nameAsPerBankAC !=null && (nameAsPerBankAC.getValue() ==null || nameAsPerBankAC.getValue().isEmpty()) ){
			hasError = true;
			eMsg.append("Name as per Bank Account is Mandatory. </br>");	
		}
		if(accountType !=null && (accountType.getValue() ==null || accountType.getValue().isEmpty())){
			hasError = true;
			eMsg.append("Account Type is Mandatory. </br>");	
		}
		if(accountNumber !=null && (accountNumber.getValue() ==null || accountNumber.getValue().isEmpty())){
			hasError = true;
			eMsg.append("Account Number is Mandatory. </br>");	
		}
		if(ifscCode !=null && (ifscCode.getValue() ==null || ifscCode.getValue().isEmpty())){
			hasError = true;
			eMsg.append("IFSC Code is Mandatory. </br>");	
		}
		
		SelectValue prefSelect = preferenceCombo.getValue() != null ? (SelectValue)preferenceCombo.getValue() : null;
		
		if(exitingData != null && !exitingData.isEmpty()) {
			boolean prefAvailable = false;
			boolean accNoAvailable = false;
			for (BankDetailsTableDTO bankDetailsTableDTO : exitingData) {
				if(SHAConstants.ADD_BANK.equalsIgnoreCase(operationCode) && bankDetailsTableDTO.getPreference().equalsIgnoreCase(prefSelect.getValue()))
					prefAvailable = true;
				
				if(SHAConstants.UPDATE_BANK.equalsIgnoreCase(operationCode)  && ("PRIMARY").equalsIgnoreCase(prefSelect.getValue()))
					prefAvailable = true;
				
				if(accountNumber.getValue() != null 
						&& bankDetailsTableDTO.getAccountNumber().equalsIgnoreCase(accountNumber.getValue()))
				accNoAvailable = true;
			}
		
			if(prefAvailable) {
				hasError = true;
				eMsg.append("Selected Account Preference already Available.</br>Please Select different Account Preference.<br>");
			}
			if(accNoAvailable) {
				hasError = true;
				eMsg.append("Account Number already Available.</br>Please Provide different Account Number.<br>");
			}
		}
		
		if (hasError) {
			showOrHideValidation(true);
			setRequired(true);
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);

			hasError = true;
			return !hasError;
		} 
		showOrHideValidation(false);
		return true;
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
	
	
	public AddBanksDetailsTableDTO getBankDTO()
	{
		try {
			this.binder.commit();
			AddBanksDetailsTableDTO bean = this.binder.getItemDataSource().getBean();
			return  bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	private void setupIfscDetails() {
		btnIFCSSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				viewSearchCriteriaWindow.setWindowObject(popup);
				viewSearchCriteriaWindow.setPresenterString(presenterString);
				viewSearchCriteriaWindow.setbankDetailsObj(currentObj);
				viewSearchCriteriaWindow.initView();
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setContent(viewSearchCriteriaWindow);
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
					}
				});

				popup.setModal(true);
				
				UI.getCurrent().addWindow(popup);
				btnIFCSSearch.setEnabled(true);
			}
		});
	}
	
	public void initBinder(AddBanksDetailsTableDTO addBanksDetailsTableDTO) {
		this.binder = new BeanFieldGroup<AddBanksDetailsTableDTO>(AddBanksDetailsTableDTO.class);
		this.binder.setItemDataSource(addBanksDetailsTableDTO);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
	}
	
	private void addPreferenceValue(ComboBox field) {
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		SelectValue selectValue = new SelectValue();
		selectValue.setId(1l);
		selectValue.setValue("PRIMARY");
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(2l);
		selectValue2.setValue("SECONDARY");
		selectValueList.add(selectValue);
		selectValueList.add(selectValue2);
		selectValueContainer.addAll(selectValueList);
		field.setContainerDataSource(selectValueContainer);
	}

	public void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		ifscCode.setValue(dto.getIfscCode());
		
		nameOfBank.setReadOnly(false);
		nameOfBank.setValue(dto.getBankName());
		nameOfBank.setReadOnly(true);
		
		branchName.setReadOnly(false);
		branchName.setValue(dto.getBranchName());
		branchName.setReadOnly(true);

	}
	
	public void buildSuccessLayout(ReceiptOfDocumentsDTO dto) {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Bank Details Added Sucessfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//fireViewEvent(CreateRODDocumentDetailsPresenter.SETUP_BANK_DETAILS,dto);
				BankDetailsTable bankDetailObj = getBankDetailObj();
				bankDetailObj.getBankDetails();
				dialog.close();
			}
		});
	}
	
	public void buildFailureLayout(String status) {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Failed to Add Bank Details.</b><br>" + status,
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
	}
	
	public void setExistingData(List<BankDetailsTableDTO> beanDto) {
		this.exitingData = beanDto;
	}
}
