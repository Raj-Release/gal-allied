/**
 * 
 */
package com.shaic.claim.reimbursement.createandsearchlot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.forms.DocumentDetailsPage;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class EditPaymentDetailsView extends ViewComponent implements EditPaymentDetails {
	
	@Inject
	private DocumentDetailsPage documentDetailPage;
	
	
	private CreateAndSearchLotTableDTO bean;
	
	
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	private Window popup;
	
	private OptionGroup optPaymentMode;
	
	private BeanFieldGroup<CreateAndSearchLotTableDTO> binder;
	
	private VerticalLayout paymentDetailsLayout;
	
	private VerticalLayout documentDetailsPageLayout;
	
	private ComboBox cmbPayeeName;
	
	private TextField txtEmailId;
	
	private TextField zonalMailId;
	
	private TextField txtReasonForChange;
	
	private TextField txtPanNo;
	
	private TextField txtLegalHeirName;
	/*private TextField txtLegalHeirFirstNmme;
	private TextField txtLegalHeirMiddleName;
	private TextField txtLegalHeirLastName;*/
	
	private TextField txtPayableAt;
	
	private TextField txtAccntNo;
	
	private TextField txtIfscCode;
	
	private Button btnIFCSSearch;
	
	private TextField txtBranch;
	
	private TextField txtBankName;
	
	private TextField txtCity;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private BeanItemContainer<SelectValue> payeeNameList;
	
	private Button btnSubmit;
	
	private Button btnCancel;
	private HorizontalLayout btnLayout;
	
	private String presenterString;
	
	private Window paymentPopup ;
	
	private EditPaymentDetailsView view  = null;
	
	private SelectValue value;
	
	private EditPaymentDetailsView currentInstance;
	
	 
	 private PreviousAccountDetailsTable previousAccountDetailsTable ;
	 
	 private Button btnPopulatePreviousAccntDetails;
	 
	 
	 private Window populatePreviousWindowPopup;
	 
	 private Button btnOk;
	 
	 private Button btnPaymentCancel;
	 
	 private VerticalLayout previousPaymentVerticalLayout;
	 
	 private HorizontalLayout previousAccountDetailsLayout;
	 
	 private Boolean isSaveToDB ;
	 
	 private TextField txtPayModeChangeReason;
	 
	 private VerticalLayout vLayout ;
	 
	 private TextField gmcProposerName;
	 
	 private TextField gmcEmployeeName;

	
	public void init(CreateAndSearchLotTableDTO bean, Window popup,ViewSearchCriteriaViewImpl viewIFSC) {
		vLayout = new VerticalLayout();
		this.bean = bean;
		isSaveToDB = false;
		//this.popup = popup;
		this.paymentPopup = popup;
		this.viewSearchCriteriaWindow = viewIFSC;
	}
	
	
	public Component getContent() {
		view = this;
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		paymentDetailsLayout = new VerticalLayout();
		paymentDetailsLayout.setCaption("Payment Details");
		paymentDetailsLayout.setSpacing(true);
		paymentDetailsLayout.setMargin(true);
		
		btnPopulatePreviousAccntDetails = new Button("Use account details from previous claim");
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_LINK);
		
		getPaymentDetailsLayout();
		
		btnOk = new Button("OK");
		//Vaadin8-setImmediate() btnOk.setImmediate(true);
		btnOk.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnOk.setWidth("-1px");
		btnOk.setHeight("-10px");
		//btnOk.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnOk.setImmediate(true);
		
		currentInstance= this;
		
		btnPaymentCancel = new Button("CANCEL");
		//Vaadin8-setImmediate() btnPaymentCancel.setImmediate(true);
		btnPaymentCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnPaymentCancel.setWidth("-1px");
		btnPaymentCancel.setHeight("-10px");
	//	btnCancel.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnPaymentCancel.setImmediate(true);
		
		 previousAccountDetailsLayout = new HorizontalLayout(btnOk,btnPaymentCancel);
		
		
		
		previousAccountDetailsTable.init("Previous Account Details", false, false);
		//previousAccountDetailsTable.setPresenterStringForLotAndBatch(this.presenterString,this);
		previousPaymentVerticalLayout = new VerticalLayout();
		previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
		previousPaymentVerticalLayout.addComponent(previousAccountDetailsLayout);
		previousPaymentVerticalLayout.setComponentAlignment(previousAccountDetailsLayout, Alignment.TOP_CENTER);
		
		
		
		buildButtonsLayout();
		documentDetailsPageLayout = new VerticalLayout();
		documentDetailsPageLayout.addComponent(paymentDetailsLayout);
		if(null != btnLayout)
		{
			documentDetailsPageLayout.addComponent(btnLayout);
			documentDetailsPageLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
		}
		
		//txtIfscCode.setEnabled(false);
		
		addListener();
		setCompositionRoot(documentDetailsPageLayout);
		setTableValues();
		return documentDetailsPageLayout;
		
	}
	
	
	public void initPresenter(String presenterString)
	{
		this.presenterString = presenterString;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<CreateAndSearchLotTableDTO>(
				CreateAndSearchLotTableDTO.class);
		this.binder.setItemDataSource(this.bean);
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
	}
	
	
		private HorizontalLayout buildButtonsLayout()
		{
			
			btnSubmit = new Button();
			btnSubmit.setCaption("Submit");
			//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
			btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			btnSubmit.setWidth("-1px");
			btnSubmit.setHeight("-10px");
			btnSubmit.setDisableOnClick(true);

			//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
			
			
			btnCancel = new Button();
			btnCancel.setCaption("Cancel");
			//Vaadin8-setImmediate() btnCancel.setImmediate(true);
			btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
			btnCancel.setWidth("-1px");
			btnCancel.setHeight("-10px");
			//Vaadin8-setImmediate() btnCancel.setImmediate(true);
			
			/*FormLayout btnFormLayout = new FormLayout(btnSubmit);
			FormLayout btnFormLayout1 = new FormLayout(btnCancel);
			btnLayout.addComponent(btnFormLayout);
			btnLayout.addComponent(btnCancel);*/
			
			btnLayout = new HorizontalLayout();
			btnLayout.setSpacing(true);
			btnLayout.addComponent(btnSubmit);
			btnLayout.addComponent(btnCancel);
			return btnLayout; 
		}
	
	private void getPaymentDetailsLayout()
	{
		optPaymentMode = (OptionGroup) binder.buildAndBind("Payment Mode" , "paymentMode" , OptionGroup.class);
	//	optPaymentMode = new OptionGroup("Payment Mode");
		optPaymentMode.setRequired(true);
		optPaymentMode.addItems(getReadioButtonOptions());
		optPaymentMode.setItemCaption(true, "Cheque/DD");
		optPaymentMode.setItemCaption(false, "Bank Transfer");
		optPaymentMode.setStyleName("horizontal");
		//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
		optPaymentMode.setReadOnly(false);
		cmbPayeeName = (ComboBox) binder.buildAndBind("Payee Name", "cmbPayeeName" , ComboBox.class);
		cmbPayeeName.setEnabled(true);
		paymentModeListener();
		loadContainerForPayeeName();
		

		        if((bean.getTypeOfClaim().equalsIgnoreCase("Reimbursement")) && ((ReferenceTable.PAYMENT_TYPE_CHEQUE).equals(this.bean.getPaymentType())))
		        		
		        {
		        	 optPaymentMode.setEnabled(true);	
		        	 optPaymentMode.setValue(true);
		        	 
		        }
				if((bean.getTypeOfClaim().equalsIgnoreCase("Reimbursement")) && (ReferenceTable.BANK_TRANSFER.equalsIgnoreCase(this.bean.getPaymentTypeValue())))
				{
					 optPaymentMode.setEnabled(true);
					optPaymentMode.setValue(false);
										
					
					//this.bean.getDocumentDetails().setPaymentModSelectFlag("Cheque");
				}
		        if((bean.getTypeOfClaim().equalsIgnoreCase("Cashless")) && (ReferenceTable.BANK_TRANSFER.equalsIgnoreCase(this.bean.getPaymentTypeValue())))
				{
					optPaymentMode.setValue(false);
					//this.bean.getDocumentDetails().setPaymentModSelectFlag("BankTransfer");
					// optPaymentMode.setReadOnly(true);
					optPaymentMode.setReadOnly(false);
						optPaymentMode.setEnabled(true);
						 /*btnIFCSSearch.setReadOnly(true);
						 btnIFCSSearch.setEnabled(false);*/
						if(null != btnIFCSSearch)
						{

//						btnIFCSSearch.setReadOnly(false);
						 btnIFCSSearch.setEnabled(true);
						}
						// btnIFCSSearch.setVisible(false);
						
				        
				}
		        
		        if((bean.getTypeOfClaim().equalsIgnoreCase("Cashless")) && (ReferenceTable.CHEQUE_DD).equalsIgnoreCase(this.bean.getPaymentTypeValue()))
		        {
		        	optPaymentMode.setValue(true);
					// optPaymentMode.setReadOnly(true);
		        	 optPaymentMode.setReadOnly(false);
					 optPaymentMode.setEnabled(true);
					 if(null != btnIFCSSearch)
					 { 
						 btnIFCSSearch.setEnabled(true);
					 }
					 
		        }
		        
			  /* optPaymentMode.setReadOnly(true);
				optPaymentMode.setEnabled(false);*/
		        
		
		if(null != this.bean.getPaymentMode())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getPaymentMode();
			/**
			 * The below if block is added to enabling the value change listener
			 * for option group. When the screen is painte d for first time, the 
			 * payment mode will be null. When we proceed to next step a value
			 * is assigned in bean. When again traversing back, the same value
			 * is set to the option group, there by ,value change listener is not
			 * invoked. Hence to invoke value change listner, twice the value
			 * is set to optiongroup. This is like, selecting and unselecting the
			 * group. If the value is true, we first set false and again we set to true
			 * and vice versa.
			 * */
			optPaymentMode.setReadOnly(false);
			if(val)
			{
				
				optPaymentMode.setValue(false);
				
			}
			else 
			{
				optPaymentMode.setValue(true);
			}
			//unbindField(optPaymentMode);
			optPaymentMode.setValue(val);
			//optPaymentMode.setReadOnly(true);
			//getContent();
		}
				
	}
	
	
	@SuppressWarnings({ "serial", "deprecation" })
	private void paymentModeListener()
	{
		optPaymentMode.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = -1774887765294036092L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				//TODO
				Boolean value = (Boolean) event.getProperty().getValue();
				
				if (null != paymentDetailsLayout && paymentDetailsLayout.getComponentCount() > 0) 
				{
					paymentDetailsLayout.removeAllComponents();
				}
				if(value)
				{
					//bean.setPaymentModeFlag(SHAConstants.YES_FLAG);
					
					bean.setChangedPaymentModeFlag(SHAConstants.PAYMENT_MODE_CHEQUE);
					if(null != txtAccntNo)
					{
						bean.setBeneficiaryAcntNo(txtAccntNo.getValue());
					}
					if(null != txtIfscCode)
					{
						bean.setIfscCode(txtIfscCode.getValue());
					}
					if(null != txtBranch)
					{
						bean.setBranchName(txtBranch.getValue());
					}
					if(null != txtBankName)
					{
						bean.setBankName(txtBankName.getValue());
					}
					if(null != txtCity)
					{
						bean.setCity(txtCity.getValue());
					}
					
					
					unbindField(getListOfPaymentFields());
					
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					if(null != txtPayableAt)
					{
						mandatoryFields.add(txtPayableAt);
						setRequiredAndValidation(txtPayableAt);
						txtPayableAt.setRequired(true);
						showOrHideValidation(false);
					}
					if(null != txtAccntNo)
						{
							mandatoryFields.remove(txtAccntNo);
						}
						if(null != txtIfscCode)
						{
							mandatoryFields.remove(txtIfscCode);
						}
				//	bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					
				}
				else 
				{
					//bean.setPaymentModeFlag(SHAConstants.N_FLAG);
					if(null != txtPayableAt)
						bean.setPayableAt(txtPayableAt.getValue());
					if(null != txtEmailId)
						bean.setEmailID(txtEmailId.getValue());
				/*	if(null != zonalMailId)
					{
						bean.setZonalMailId(zonalMailId.getValue());
					}*/
					if(null != txtReasonForChange)
						bean.setReasonForChange(txtReasonForChange.getValue());
					if(null != txtLegalHeirName)
						bean.setLegalFirstName(txtLegalHeirName.getValue());
					if(null != txtPanNo)
						bean.setPanNo(txtPanNo.getValue());
					unbindField(getListOfPaymentFields());
					//buildBankTransferLayout();
					paymentDetailsLayout.addComponent(btnPopulatePreviousAccntDetails);
					paymentDetailsLayout.setComponentAlignment(btnPopulatePreviousAccntDetails, Alignment.TOP_RIGHT);
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					paymentDetailsLayout.addComponent(buildBankTransferLayout());
					//txtAccntNo.setEnabled(false); //------need to change 
					//txtIfscCode.setEnabled(true);
					txtBankName.setEnabled(false);
					txtCity.setEnabled(false);
					btnIFCSSearch.setVisible(true);
					txtBranch.setEnabled(false);
					unbindField(txtPayableAt);
					mandatoryFields.remove(txtPayableAt);
					bean.setChangedPaymentModeFlag(SHAConstants.NEFT_TYPE);
					//bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				}	
				
				if(null != bean.getChangedPaymentModeFlag() && !(bean.getChangedPaymentModeFlag().equals(bean.getChangedPaymentMode()))){
					
					mandatoryFields.add(txtPayModeChangeReason);
					txtPayModeChangeReason.setValue("");
					showOrHideValidation(false);
				}
				else
				{
					mandatoryFields.remove(txtPayModeChangeReason);
				}
				
				
			}
		});
	}
	
	private void addListener()
	{
		
	
		btnSubmit.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePage())
				{
					//();
					//view.popup.close();
					paymentPopup.close();
				}
				
			}
		});
		
	btnCancel.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
					//view.popup.close();
				isSaveToDB = false;
				paymentPopup.close();
			}
		});
	
	
	btnPopulatePreviousAccntDetails.addClickListener(new ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			
			
			
			populatePreviousWindowPopup = new com.vaadin.ui.Window();
			populatePreviousWindowPopup.setWidth("75%");
			populatePreviousWindowPopup.setHeight("90%");
			
			previousAccountDetailsTable.init("Previous Account Details", false, false);
		//	previousAccountDetailsTable.setPresenterStringForLotAndBatch(presenterString,currentInstance);
			previousPaymentVerticalLayout = new VerticalLayout();
			previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
			previousPaymentVerticalLayout.addComponent(previousAccountDetailsLayout);
			previousPaymentVerticalLayout.setComponentAlignment(previousAccountDetailsLayout, Alignment.TOP_CENTER);			
			populatePreviousWindowPopup.setContent(previousPaymentVerticalLayout);
			
			setTableValues();
			populatePreviousWindowPopup.setClosable(true);
			populatePreviousWindowPopup.center();
			populatePreviousWindowPopup.setResizable(true);
			
			populatePreviousWindowPopup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			populatePreviousWindowPopup.setModal(true);
			populatePreviousWindowPopup.setClosable(false);
			
			UI.getCurrent().addWindow(populatePreviousWindowPopup);
			btnPopulatePreviousAccntDetails.setEnabled(true);
		}
	});
	
btnOk.addClickListener(new ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			String err = previousAccountDetailsTable.isValidate();
			if("" == err)
			{
			buildDialogBox("Selected Data will be populated in payment details section. Please click OK to proceeed",populatePreviousWindowPopup,SHAConstants.BTN_OK);
		//	populatePreviousWindowPopup.close();
		//	previousAccountDetailsTable.clearCheckBoxValue();
			}
		}
	});

btnPaymentCancel.addClickListener(new ClickListener() {
	
	@Override
	public void buttonClick(ClickEvent event) {
		//buildDialogBox("Selected Data will be cancelled and will not be populated in payment details section. Please click OK to proceed",populatePreviousWindowPopup);
		buildDialogBox("Are you sure you want to cancel",populatePreviousWindowPopup,SHAConstants.BTN_CANCEL);	
		//resetBankPaymentFeidls();
		//previousAccountDetailsTable.clearCheckBoxValue();
		
	}
});
	}
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
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

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	
/*	private void savePaymentData()
	{
		if(SHAConstants.CREATE_LOT_PAYMENT.equalsIgnoreCase(presenterString))
			fireViewEvent(MenuPresenter.CREATE_LOT_SAVE_PAYMENT_INFO, bean);
	}*/
	
	private void unbindField(List<Field<?>> field) {
		if(null != field && !field.isEmpty())
		{
			for (Field<?> field2 : field) {
				if (field2 != null ) {
					Object propertyId = this.binder.getPropertyId(field2);
					//if (field2!= null && field2.isAttached() && propertyId != null) {
					if (field2!= null  && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && propertyId != null) {
				this.binder.unbind(field);
			}
		}
	}
	
	private List<Field<?>> getListOfPaymentFields()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		//fieldList.add(cmbPayeeName);
		fieldList.add(txtEmailId);
		fieldList.add(zonalMailId);
		fieldList.add(txtReasonForChange);
		fieldList.add(txtPanNo);
		fieldList.add(txtLegalHeirName);
		/*fieldList.add(txtLegalHeirMiddleName);
		fieldList.add(txtLegalHeirLastName);*/
		fieldList.add(txtAccntNo);
		fieldList.add(txtIfscCode);
		fieldList.add(txtBranch);
		fieldList.add(txtBankName);
		fieldList.add(txtCity);
		fieldList.add(txtPayableAt);
		fieldList.add(txtPayModeChangeReason);
		fieldList.add(gmcProposerName);
		fieldList.add(gmcEmployeeName);
		return fieldList;
	}
	
	public void loadContainerForPayeeName(BeanItemContainer<SelectValue> payeeNameList)
	{
		this.payeeNameList = payeeNameList;
	}
	
	private void loadContainerForPayeeName()
	{
		 if(null != cmbPayeeName && null != this.payeeNameList)
		 {
			//payeeNameList = (BeanItemContainer<SelectValue>) referenceDataMap.get("payeeNameList");
			 cmbPayeeName.setContainerDataSource(payeeNameList);
			 cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			 cmbPayeeName.setItemCaptionPropertyId("value");
			 for(int i = 0 ; i<payeeNameList.size() ; i++)
			 	{
					if (null != this.bean.getPayeeName() && (this.bean.getPayeeName().getValue()).equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
					{
						this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
						//this.txtReasonForChange.setEnabled(false);
					}
					
					if(null != this.bean.getClaimType() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimType()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimType())) && 
							("Insured").equalsIgnoreCase(this.bean.getDocReceivedFrom()))
				 {
					if(null !=this.bean.getHospitalName())
					{
					 if(this.bean.getHospitalName().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
						{
						 	cmbPayeeName.removeItem(payeeNameList.getIdByIndex(i));
							//this.cmbPayeeName.setEnabled(false);
						}
					}
				 }

					/*if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
							("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
					{
						if(null !=this.bean.getDocumentDetails().getHospitalName()){
							if(this.bean.getDocumentDetails().getHospitalName().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
							{
								this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
								//this.cmbPayeeName.setEnabled(false);
							}
						}
					}
					else if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
								("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
					 {
						if(null !=this.bean.getDocumentDetails().getHospitalName())
						{
						 if(this.bean.getDocumentDetails().getHospitalName().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
							{
							 	cmbPayeeName.removeItem(payeeNameList.getIdByIndex(i));
								//this.cmbPayeeName.setEnabled(false);
							}
						}
					 }*/
				// cmbPayeeName.removeItem(this.bean.getDocumentDetails().getHospitalName());
			 }
		 }
	}
	
	
	private HorizontalLayout buildChequePaymentLayout()
	{
		txtEmailId = (TextField) binder.buildAndBind("Email ID", "emailID" , TextField.class);
		txtEmailId.setNullRepresentation("");
		//txtEmailId.setValue(this.chqEmailId);
		if(null != this.txtEmailId.getValue())
		{
		//isValidEmail(this.txtEmailId.getValue());
		
		}
		txtEmailId.setMaxLength(100);
		txtEmailId.setEnabled(true);
		
		txtPayModeChangeReason = (TextField) binder.buildAndBind("Reason for change (Pay Mode)", "payModeChangeReason" , TextField.class);
		CSValidator payModeChangeValidator = new CSValidator();
		payModeChangeValidator.extend(txtPayModeChangeReason);
		payModeChangeValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		payModeChangeValidator.setPreventInvalidTyping(true);
		txtPayModeChangeReason.setMaxLength(4000);
		payModeShortcutListener(txtPayModeChangeReason, null);
		paymentModeValuechangeListener();
		/*if(null != this.bean.getPayModeChangeReason())
		{
			txtPayModeChangeReason.setValue(this.bean.getPayModeChangeReason());
		}*/
		
		//txtEmailId.setRequired(true);
		
		zonalMailId = (TextField) binder.buildAndBind("Zonal Email ID", "zonalMailId" , TextField.class);
		zonalMailId.setNullRepresentation("");
		zonalMailId.setVisible(false);
		if(null != this.zonalMailId.getValue())
		{
//		isValidEmail(this.zonalMailId.getValue());
		
		}
		zonalMailId.setMaxLength(100);
		zonalMailId.setEnabled(true);
		
		
		txtReasonForChange = (TextField) binder.buildAndBind("Reason For Change(Payee Name)", "reasonForChange", TextField.class);
		txtReasonForChange.setNullRepresentation("");
		
	//	txtReasonForChange.setValue(this.chqResonForChange);
		
		CSValidator reasonForChangeValidator = new CSValidator();
		reasonForChangeValidator.extend(txtReasonForChange);
		reasonForChangeValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		reasonForChangeValidator.setPreventInvalidTyping(true);
		txtReasonForChange.setMaxLength(100);
		txtReasonForChange.setEnabled(false);
		
		
		
		txtPanNo = (TextField) binder.buildAndBind("PAN No","panNo",TextField.class);
		txtPanNo.setNullRepresentation("");
	//	txtPanNo.setValue(this.chqPanNo);
	
		
		CSValidator panValidator = new CSValidator();
		panValidator.extend(txtPanNo);
		panValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		panValidator.setPreventInvalidTyping(true);
		txtPanNo.setMaxLength(10);
		txtPanNo.setEnabled(true);
		
		txtLegalHeirName = (TextField) binder.buildAndBind("Legal Heir Name","legalFirstName",TextField.class);
		txtLegalHeirName.setNullRepresentation("");
		//txtLegalHeirName.setValue(this.chqLegalHeirName);
		txtLegalHeirName.setEnabled(false);
		txtLegalHeirName.setMaxLength(30);
		
		CSValidator legalHairvalidator = new CSValidator();
		legalHairvalidator.extend(txtLegalHeirName);
		legalHairvalidator.setRegExp("^[a-z A-Z /.]*$");
		legalHairvalidator.setPreventInvalidTyping(true);
		
		/*txtLegalHeirMiddleName = (TextField) binder.buildAndBind("", "legalMiddleName" , TextField.class);
		txtLegalHeirLastName = (TextField) binder.buildAndBind("", "legalLastName" , TextField.class);
		*/
		
		txtPayableAt = (TextField) binder.buildAndBind("Payable at", "payableAt", TextField.class);
		txtPayableAt.setNullRepresentation("");
		//txtPayableAt.setValue(this.chqPayableAt);
		txtPayableAt.setMaxLength(50);
/*		txtPayableAt.setRequired(true);
*/		/*mandatoryFields.add(txtPayableAt);
		setRequiredAndValidation(txtPayableAt);
		txtPayableAt.setRequired(true);
		showOrHideValidation(false);*/
		
		CSValidator payableAtValidator = new CSValidator();
		payableAtValidator.extend(txtPayableAt);
		payableAtValidator.setRegExp("^[a-zA-Z /]*$");
		payableAtValidator.setPreventInvalidTyping(true);;
		txtPayableAt.setEnabled(true);
		
		gmcProposerName = (TextField) binder.buildAndBind("Proposer Name", "gmcProposerName", TextField.class);
		gmcProposerName.setNullRepresentation("");		
		gmcProposerName.setMaxLength(50);		
		CSValidator proposerNameValidator = new CSValidator();
		proposerNameValidator.extend(gmcProposerName);
		proposerNameValidator.setRegExp("^[a-zA-Z /]*$");
		proposerNameValidator.setPreventInvalidTyping(true);;
		gmcProposerName.setEnabled(false);
		
		gmcEmployeeName = (TextField) binder.buildAndBind("Employee Name", "gmcEmployeeName", TextField.class);
		gmcEmployeeName.setNullRepresentation("");		
		gmcEmployeeName.setMaxLength(50);
		
		CSValidator employeeNameValidator = new CSValidator();
		employeeNameValidator.extend(gmcEmployeeName);
		employeeNameValidator.setRegExp("^[a-zA-Z /]*$");
		employeeNameValidator.setPreventInvalidTyping(true);;
		gmcEmployeeName.setEnabled(false);
		
		/**
		 * Payment option made editable for issue #375.
		 * */
		
		if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getTypeOfClaim()))
		{	
			
			if(null != bean.getBeneficiaryAcntNo() && !("").equals(bean.getEmailID()) && !("").equals(bean.getZonalMailId()))
			{
				//txtEmailId.setReadOnly(true);
				txtEmailId.setReadOnly(false);
				//txtEmailId.setEnabled(false);
				txtEmailId.setEnabled(true);
				txtEmailId.setDescription(bean.getEmailID());
				zonalMailId.setEnabled(false);
				zonalMailId.setDescription(bean.getZonalMailId());
			}
			else
			{
				txtEmailId.setReadOnly(false);
				txtEmailId.setEnabled(true);
				zonalMailId.setEnabled(false);
				zonalMailId.setDescription(bean.getZonalMailId());
				txtEmailId.setDescription(bean.getEmailID());
			}
			
			
			
		/*	txtReasonForChange.setReadOnly(true);
			txtReasonForChange.setEnabled(false);*/
			txtReasonForChange.setReadOnly(false);
			txtReasonForChange.setEnabled(true);
			
			
			if(null != bean.getBeneficiaryAcntNo() && !("").equals(bean.getPanNo()))
			{
				/*txtPanNo.setReadOnly(true);
				txtPanNo.setEnabled(false);*/
				
				txtPanNo.setReadOnly(false);
				txtPanNo.setEnabled(true);
			}
			else
			{
				txtPanNo.setReadOnly(false);
				txtPanNo.setEnabled(true);

			}
			/*txtLegalHeirName.setReadOnly(true);
			txtLegalHeirName.setEnabled(false);*/
			
			txtLegalHeirName.setReadOnly(false);
			txtLegalHeirName.setEnabled(true);
			
			if(null != bean.getBeneficiaryAcntNo() && !("").equals(bean.getPayableAt()))
			{
				/*txtPayableAt.setReadOnly(true);
				txtPayableAt.setEnabled(false);*/
				txtPayableAt.setReadOnly(false);
				txtPayableAt.setEnabled(true);
			}
			else
			{
				txtPayableAt.setReadOnly(false);
				txtPayableAt.setEnabled(true);
			}
			
			cmbPayeeName.setEnabled(true);
			//cmbPayeeName.setEnabled(false);
			//cmbPayeeName.setReadOnly(true);
			
			/*txtReasonForChange.setEnabled(false);
			txtReasonForChange.setReadOnly(true);*/
			
		     /*btnIFCSSearch.setEnabled(false);
			btnIFCSSearch.setReadOnly(true);*/
			
			 
		      
			
		}
		else if(null != this.bean.getTypeOfClaim() &&  ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getTypeOfClaim()))) 
		{
			txtEmailId.setReadOnly(false);
			txtEmailId.setEnabled(true);
			
			zonalMailId.setEnabled(false);			
			
			cmbPayeeName.setReadOnly(false);
			cmbPayeeName.setEnabled(true);
			
			optPaymentMode.setReadOnly(false);
			optPaymentMode.setEnabled(true);
			
			txtReasonForChange.setReadOnly(false);   
		//	txtReasonForChange.setEnabled(true);
			
			txtPanNo.setReadOnly(false);
			txtPanNo.setEnabled(true);
			
			txtLegalHeirName.setReadOnly(false);
			txtLegalHeirName.setEnabled(true);
			
			txtPayableAt.setReadOnly(false);
			txtPayableAt.setEnabled(true);
		}
		
		
		if(null != this.bean.getEmailID())
		{
			txtEmailId.setValue(this.bean.getEmailID());
			txtEmailId.setDescription(this.bean.getEmailID());
			
		}
		
		if(null != this.bean.getZonalMailId())
		{
			zonalMailId.setValue(this.bean.getZonalMailId());
			zonalMailId.setDescription(this.bean.getZonalMailId());
		}
		
		
		if(null != this.bean.getPanNo())
		{
			txtPanNo.setValue(this.bean.getPanNo());
		}
		
		if(null != this.bean.getLegalFirstName())
		{
			txtLegalHeirName.setValue(this.bean.getLegalFirstName());
		
		}
		
		if(null != this.bean.getPayableAt())
		{
			txtPayableAt.setValue(this.bean.getPayableAt());
		}
		
		if(null != this.bean.getReasonForChange())
		{
			txtReasonForChange.setValue(this.bean.getReasonForChange());
		}
		FormLayout formLayout1 = null;
		FormLayout formLayout2 = null;
		HorizontalLayout hLayout = null;
		/*if(null != this.bean.getDocumentDetails().getPaymentModeFlag() && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) 
				&& (ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getDocumentDetails().getPaymentModeFlag()))*/
		
		//if(("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) && (null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue()))
		if ((null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue()))
		{
			formLayout1 = new FormLayout(optPaymentMode,txtPayModeChangeReason,txtEmailId,zonalMailId,txtPanNo,txtPayableAt);
			 formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,txtLegalHeirName,gmcProposerName,gmcEmployeeName);
		}
		else
		{
			formLayout1 = new FormLayout(optPaymentMode,txtPayModeChangeReason,txtEmailId,zonalMailId,txtPanNo);
			 formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,txtLegalHeirName,gmcProposerName,gmcEmployeeName);
		}
		 hLayout= new HorizontalLayout(formLayout1 ,formLayout2);
		/*else if(("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) && 
				//(null != this.bean.getDocumentDetails().getPaymentMode() && this.bean.getDocumentDetails().getPaymentMode())
				(null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue())
				)
		{
			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
			 formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,txtLegalHeirName);
			 hLayout = new HorizontalLayout(formLayout1 ,formLayout2);
		}
		else
		{
			TextField dField1 = new TextField();
			dField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField1.setReadOnly(true);
			TextField dField2 = new TextField();
			dField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField2.setReadOnly(true);
			TextField dField3 = new TextField();
			dField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField3.setReadOnly(true);
			TextField dField4 = new TextField();
			dField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField4.setReadOnly(true);
			TextField dField5 = new TextField();
			dField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField5.setReadOnly(true);
			dField5.setWidth(2,Unit.CM);
			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtAccntNo,txtIfscCode,txtBankName,txtCity);
			formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,txtLegalHeirName,dField1,txtBranch);
			HorizontalLayout btnHLayout = new HorizontalLayout(dField5,btnIFCSSearch);
			VerticalLayout btnLayout = new VerticalLayout(btnHLayout,dField2,dField3,dField4);
			hLayout = new HorizontalLayout(formLayout1 ,btnLayout,formLayout2);
			hLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		}*/
		
		hLayout.setWidth("100%");
		addComboPayeeNameListener();
		
		return hLayout;
		
		
	}
	
	
	private Boolean isValidEmail(String strEmail)
	{
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern validEmailPattern = Pattern.compile(emailPattern);
		Matcher validMatcher = validEmailPattern.matcher(strEmail);
		return validMatcher.matches();
	}
	private HorizontalLayout buildBankTransferLayout()
	{
		txtAccntNo = (TextField)binder.buildAndBind("Account No" , "beneficiaryAcntNo", TextField.class);
		txtAccntNo.setRequired(true);
		txtAccntNo.setNullRepresentation("");
		txtAccntNo.setEnabled(true); 
		txtAccntNo.setMaxLength(30);
		
		CSValidator accntNoValidator = new CSValidator();
		accntNoValidator.extend(txtAccntNo);
		accntNoValidator.setRegExp("^[a-zA-Z 0-9 ]*$");
		accntNoValidator.setPreventInvalidTyping(true);
		
		
		
		txtIfscCode = (TextField) binder.buildAndBind("IFSC Code", "ifscCode", TextField.class);
		txtIfscCode.setRequired(true);
		txtIfscCode.setNullRepresentation("");
		txtIfscCode.setEnabled(false);
		txtIfscCode.setMaxLength(15);
		CSValidator ifscCodeValidator = new CSValidator();
		ifscCodeValidator.extend(txtIfscCode);
		ifscCodeValidator.setRegExp("^[a-zA-Z 0-9]*$");
		ifscCodeValidator.setPreventInvalidTyping(true);
		
		
		
		
		txtBranch = (TextField) binder.buildAndBind("Branch", "branchName", TextField.class);
		txtBranch.setNullRepresentation("");
		txtBranch.setEnabled(false);
		txtBranch.setMaxLength(100);
		
		
		
		txtBankName = (TextField) binder.buildAndBind("Bank Name", "bankName", TextField.class);
		txtBankName.setNullRepresentation("");
		txtBankName.setEnabled(false);
		txtBankName.setMaxLength(100);

		
		
		txtCity = (TextField) binder.buildAndBind("City", "city", TextField.class);
		txtCity.setNullRepresentation("");
		txtCity.setEnabled(false);
		
		
		
		
		if(null != this.bean.getTypeOfClaim() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getTypeOfClaim()))
		
		/** 
		 * The above code is commented and below code was added for ticket 1584.
		 * Need to check with naren, whether he has done any changes in setting read only parameter to true or false.
		 * */
		/*if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
				("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))*/
		{
			/*txtAccntNo.setReadOnly(false);
			txtAccntNo.setEnabled(false);
			
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setEnabled(false);
			
			txtBranch.setReadOnly(false);
			txtBranch.setEnabled(false);
			
			txtBankName.setReadOnly(false);
			txtBankName.setEnabled(false);
			
			
			txtCity.setReadOnly(false);
			txtCity.setEnabled(false);*/

			/*txtAccntNo.setReadOnly(true);
			txtAccntNo.setEnabled(true);
			
			txtIfscCode.setReadOnly(true);
			txtIfscCode.setEnabled(true);*/
			

			if(null != bean.getBeneficiaryAcntNo() && !("").equals(bean.getBeneficiaryAcntNo()))
			{
				/*txtAccntNo.setReadOnly(true);
				txtAccntNo.setEnabled(false); */
				
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setEnabled(true); 
			}
			else
			{
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setEnabled(true);
			}
			if(null != bean.getIfscCode() && !("").equals(bean.getIfscCode()))
			{
				txtIfscCode.setReadOnly(false);
				//txtIfscCode.setEnabled(true);
			}
			else
			{
				txtIfscCode.setReadOnly(false);
				txtIfscCode.setEnabled(false);
			}
			
			
			
			
			/*txtBranch.setReadOnly(true);
			txtBranch.setEnabled(false);
			
			txtBankName.setReadOnly(true);
			txtBankName.setEnabled(false);
			
			
			txtCity.setReadOnly(true);
			txtCity.setEnabled(false);
			*/
			
			txtBranch.setReadOnly(false);
			txtBranch.setEnabled(true);
			
			txtBankName.setReadOnly(false);
			txtBankName.setEnabled(true);
			
			
			txtCity.setReadOnly(false);
			txtCity.setEnabled(true);
			
			
			
		}
		else if(null != this.bean.getTypeOfClaim() && (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getTypeOfClaim()))

		{
			txtAccntNo.setReadOnly(false);   //need to change
			//txtAccntNo.setEnabled(false);
			txtAccntNo.setValue(null);
			txtAccntNo.setNullRepresentation("");
			
			txtIfscCode.setReadOnly(false);
		//	txtIfscCode.setEnabled(true);
			txtIfscCode.setValue(null);
			txtIfscCode.setNullRepresentation("");
			
			txtBranch.setReadOnly(false);
			txtBranch.setEnabled(false);
			txtBranch.setValue(null);;
			
			txtBankName.setReadOnly(false);
			txtBankName.setEnabled(false);
			txtBankName.setValue(null);
			txtBankName.setNullRepresentation("");
			 
			txtCity.setReadOnly(false);
			txtCity.setEnabled(false);
			txtCity.setValue(null);
			txtCity.setNullRepresentation("");
			
		}
		
		if(null != this.bean.getBeneficiaryAcntNo())
		{
			txtAccntNo.setValue(this.bean.getBeneficiaryAcntNo());
		}
		if(null != this.bean.getIfscCode())
		{
			txtIfscCode.setValue(this.bean.getIfscCode());
		}
		if(null != this.bean.getBranchName())
		{
			txtBranch.setValue(this.bean.getBranchName());
		}
		if(null != this.bean.getBankName())
		{
			txtBankName.setValue(this.bean.getBankName());
		}
		if(null != this.bean.getCity())
		{
			txtCity.setValue(this.bean.getCity());
		}
		btnIFCSSearch = new Button();
		
		
		addIFSCSearchListener();
		
		FormLayout fLayout1 = new FormLayout(txtIfscCode);
		HorizontalLayout lyutIFCS = new HorizontalLayout(fLayout1,btnIFCSSearch);
		//HorizontalLayout lyutIFCS = new HorizontalLayout(txtIfscCode,btnIFCSSearch);
		btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
		//lyutIFCS.setComponentAlignment(fLayout1, Alignment.BOTTOM_LEFT);
		lyutIFCS.setComponentAlignment(btnIFCSSearch, Alignment.BOTTOM_RIGHT);
		//lyutIFCS.setSpacing(true);
		//lyutIFCS.setWidth("88%");
		lyutIFCS.setSpacing(true);
		lyutIFCS.setWidth("100%");
		//lyutIFCS.setCaption("IFSCd Code");
		
		
		FormLayout bankTransferLayout1 = new FormLayout(txtAccntNo,txtIfscCode,txtBankName,txtCity);
		bankTransferLayout1.setComponentAlignment(txtAccntNo, Alignment.MIDDLE_RIGHT);
		//FormLayout bankTransferLayout1 = new FormLayout(txtAccntNo,lyutIFCS,txtBankName,txtCity);
		//bankTransferLayout1.setComponentAlignment(txtAccntNo, Alignment.MIDDLE_RIGHT);
	//	bankTransferLayout1.setComponentAlignment(lyutIFCS, Alignment.MIDDLE_LEFT);
		/*formLayout1.addComponent(txtAccntNo);
		formLayout1.addComponent(txtIfscCode);
		formLayout1.addComponent(txtBankName);
		formLayout1.addComponent(txtCity);
		formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
		*/
		TextField dField = new TextField();
		dField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField.setReadOnly(true);
		TextField dField1 = new TextField();
		dField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField1.setReadOnly(true);
		FormLayout bankTransferLayout2 = new FormLayout(dField,dField1,txtBranch);
		
		TextField dField2 = new TextField();
		dField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField2.setReadOnly(true);
		TextField dField3 = new TextField();
		dField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField3.setReadOnly(true);
		TextField dField4 = new TextField();
		dField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField4.setReadOnly(true);
		TextField dField5 = new TextField();
		dField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField5.setReadOnly(true);
		dField5.setWidth(2,Unit.CM);
		
		TextField dField7 = new TextField();
		dField7.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField7.setReadOnly(true);
		dField7.setWidth(2,Unit.CM);
		
		TextField dField6 = new TextField();
		dField6.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField6.setReadOnly(true);
		dField6.setWidth(2,Unit.CM);
		TextField dField8 = new TextField();
		dField8.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField8.setReadOnly(true);
		dField8.setWidth(2,Unit.CM);
		
		//HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 , bankTransferLayout2);
	/*	HorizontalLayout btnHLayout = new HorizontalLayout(btnIFCSSearch);
		btnHLayout.setWidth("70%");
		btnHLayout.setComponentAlignment(btnIFCSSearch,  Alignment.BOTTOM_LEFT);*/
		
		VerticalLayout btnHLayout = new VerticalLayout(dField7,dField6,btnIFCSSearch,new FormLayout(txtBranch));
		btnHLayout.setWidth("70%");
		btnHLayout.setComponentAlignment(btnIFCSSearch,  Alignment.BOTTOM_LEFT);
		
		/*HorizontalLayout branchLayout = new HorizontalLayout(dField,dField1,txtBranch);
		branchLayout.setWidth("70%");
		branchLayout.setComponentAlignment(txtBranch,  Alignment.BOTTOM_LEFT);*/
		
		txtIfscCode.setEnabled(false);
		//txtIfscCode.setEnabled(true);
		//txtAccntNo.setEnabled(false);
		
		VerticalLayout btnLayout = new VerticalLayout(btnHLayout,dField2,dField3,dField4);
		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 ,btnLayout,bankTransferLayout2);
		hLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		
		
		
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , bankTransferLayout2);
		hLayout.setWidth("80%");
		hLayout.addStyleName("gridBorder");
		if(null != txtAccntNo)
		{
			//mandatoryFields.add(txtAccntNo);
			//setRequiredAndValidation(txtAccntNo);
		}
		if(null != txtIfscCode)
		{
			//mandatoryFields.add(txtIfscCode);
			//setRequiredAndValidation(txtIfscCode);
		}
		return hLayout;
		
		/*if(null != txtPayableAt)
		{*/
		/*unbindField(txtPayableAt);
		mandatoryFields.remove(txtPayableAt);*/
			//txtPayableAt.setRequired(false);
		//}
		//setRequired(false);
		
			/*txtAccntNo.setVisible(false);
			txtIfscCode.setVisible(false);
			txtBankName.setVisible(false);
			txtCity.setVisible(false);
			btnIFCSSearch.setVisible(false);
			txtBranch.setVisible(false);*/
		
	}
	
	private void addIFSCSearchListener()
	{
	btnIFCSSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {					
				
				popup = new com.vaadin.ui.Window();
				viewSearchCriteriaWindow.setWindowObject(popup);
				viewSearchCriteriaWindow.setPresenterString(SHAConstants.EDIT_PAYMENT_DETAILS,view);
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
			}
		});
		
	}
	
	private void addComboPayeeNameListener()
	{
		
		
		cmbPayeeName
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				 value = (SelectValue) event.getProperty().getValue();
				if(null == value)
				{	
						txtLegalHeirName.setEnabled(true);
						txtReasonForChange.setEnabled(true);
				}
				else if(null != bean && bean.getPayeeName()!=null &&  (value.getValue()).equalsIgnoreCase(bean.getPayeeName().getValue()))
				{
					txtReasonForChange.setEnabled(false);
					txtReasonForChange.setValue(bean.getReasonForChange());
					txtReasonForChange.setReadOnly(false);
					
				}
				else
				{
					txtLegalHeirName.setValue(null);
					txtLegalHeirName.setNullRepresentation("");
					txtLegalHeirName.setEnabled(false);
					txtReasonForChange.setEnabled(true);
					txtReasonForChange.setValue(null);
				}
				
			}
				
			
		});
	}
	
public boolean validatePage() {
	
	
		
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		
		if(optPaymentMode.getValue().equals(false))
		{
		
		 if(txtIfscCode.getValue() == null || txtIfscCode.getValue().isEmpty()) 
		 {
			 eMsg.append("Please enter the IFSC Code</br>");
			 hasError = true;
		 }
		 
		  if(txtAccntNo.getValue() == null || txtAccntNo.getValue().isEmpty())
		 {
			 eMsg.append("Please enter Account No</br>");
			 hasError = true; 
		 }
		  
		  if(txtPanNo != null && txtPanNo.getValue() != null  && ! txtPanNo.getValue().equalsIgnoreCase("")){
				String value = txtPanNo.getValue();
				if(value.length() != 10){
//					hasError = true;
//					eMsg += "PAN number should be 10 digit value</br>";
				}
			}
		  
		  
		}
		
		if(optPaymentMode.getValue().equals(true))
		{
			
			 if(txtPayableAt.getValue() == null || txtPayableAt.getValue().isEmpty())
			 {
				 eMsg.append(" </br>Please enter Payable At ");
				 hasError = true;
			 }
			 
			
		}
		if(null != value)
		{
		if(bean.getPayeeName()!=null && !(value.getValue()).equalsIgnoreCase(bean.getPayeeName().getValue()) &&  txtReasonForChange.getValue() == null     )
		{
			eMsg.append(" </br>Please enter Reason For change ");
			 hasError = true;
			
		}
		}
		
		if(null != txtPayModeChangeReason && ((txtPayModeChangeReason.getValue() == null) || (txtPayModeChangeReason.getValue().equals("")))){
			
			if(null != bean.getChangedPaymentModeFlag() && !(bean.getChangedPaymentModeFlag().equals(bean.getChangedPaymentMode()))){				
			
				hasError = true;
				eMsg.append("Please Enter Reason for Changing the Payment Mode</br>");
			}
			
		}
		
		if(null != this.txtEmailId && null != this.txtEmailId.getValue() && !("").equalsIgnoreCase(this.txtEmailId.getValue()))
		{
			/*if(!isValidEmail(this.txtEmailId.getValue()))
			{
				hasError = true;
				eMsg.append("</br>Please enter a valid email ");
			}*/
		}
		
		if(null != this.zonalMailId && null != this.zonalMailId.getValue() && !("").equalsIgnoreCase(this.zonalMailId.getValue()))
		{
//			if(!isValidEmail(this.zonalMailId.getValue()))
//			{
//				hasError = true;
//				eMsg += "</br>Please enter a valid email ";
//			}
		}
		
		
		if(null == this.cmbPayeeName.getValue())
		{
			hasError = true;
			eMsg.append("</br>Please select Payee Name ");
		}
		
		
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}
		
		if (hasError) {
			btnSubmit.setDisableOnClick(false);
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			hasError = true;
			btnSubmit.setEnabled(true);
			
			return !hasError;
		} 
		else 
		{
			try {
				this.binder.commit();		
				isSaveToDB = true;
				if(null != optPaymentMode && null != optPaymentMode.getValue())
				{
					Boolean value = (Boolean) optPaymentMode.getValue();
					if(null != value && value)
					{
						bean.setPaymentModeFlag(SHAConstants.YES_FLAG);
					}
					else
					{
						bean.setPaymentModeFlag(SHAConstants.N_FLAG);
					}
				}
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}		
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		
		
		
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpIFSCDetails(
			ViewSearchCriteriaTableDTO dto,
			final EditPaymentDetailsView view) {
		view.txtIfscCode.setReadOnly(false);
		view.txtIfscCode.setValue(dto.getIfscCode());
		view.txtIfscCode.setReadOnly(true);
		
		view.txtBankName.setReadOnly(false);
		view.txtBankName.setValue(dto.getBankName());
		view.txtBankName.setReadOnly(true);
		
		view.txtBranch.setReadOnly(false);
		view.txtBranch.setValue(dto.getBranchName());
		view.txtBranch.setReadOnly(true);
		
		view.txtCity.setReadOnly(false);
		view.txtCity.setValue(dto.getCity());
		view.txtCity.setReadOnly(true);
		
		/*btnSubmit.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePage())
				{
					view.popup.close();
				}
				
			}
		});
		
		btnCancel.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
					view.popup.close();
			}
		});*/
	}
	
	
	public void buildPaymentDetailsSuccessLayout(Boolean value) {
		String strMessage = "";
		if(value)
		{
			strMessage = "Payment details saved successfully.";
		}
		else
		{
			strMessage = "Error occurred while saving payment data. Please contact administrator";
		}
		
		Label successLabel = new Label("<b style = 'color: green;'>" + strMessage + "</b>",
							ContentMode.HTML);
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Create/Search LOT Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
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
				{
					paymentPopup.close();
				}
				
				
				
			}
		});
	}

	
	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO) {
		if(null != txtEmailId)
		{
			txtEmailId.setReadOnly(false);
			txtEmailId.setValue(tableDTO.getEmailId());
			txtEmailId.setEnabled(true);
		}
		if(null != txtPanNo)
		{
			txtPanNo.setReadOnly(false);
			txtPanNo.setValue(tableDTO.getPanNo());
			txtPanNo.setEnabled(true);
		}
		if(null != txtAccntNo)
		{
			txtAccntNo.setReadOnly(false);
			txtAccntNo.setValue(tableDTO.getBankAccountNo());
			txtAccntNo.setEnabled(true);
		}
		if(null != txtIfscCode)
		{
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(tableDTO.getIfsccode());
			txtIfscCode.setEnabled(true);
		}
		if(null != txtBankName)
		{
			txtBankName.setReadOnly(false);
			txtBankName.setValue(tableDTO.getBankName());
			txtBankName.setEnabled(true);
		}
		if(null != txtCity)
		{
			txtCity.setReadOnly(false);
			txtCity.setValue(tableDTO.getBankCity());
			txtCity.setEnabled(true);
		}
		if(null != txtBranch)
		{
			txtBranch.setReadOnly(false);
			txtBranch.setValue(tableDTO.getBankBranch());
			txtBranch.setEnabled(true);
		}
		
		
		
	}

	public void resetBankPaymentFeidls() {
		if(null != txtEmailId)
		{	
			txtEmailId.setReadOnly(false);
			txtEmailId.setValue(null);
		}	
		if(null != txtPanNo)
		{
			txtPanNo.setReadOnly(false);
			txtPanNo.setValue(null);
		}
		if(null != txtAccntNo)
		{
			txtAccntNo.setReadOnly(false);
			txtAccntNo.setValue(null);
		}
		if(null != txtIfscCode)
		{
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(null);
		}
		if(null != txtBankName)
		{
			txtBankName.setReadOnly(false);
			txtBankName.setValue(null);
		}
		if(null != txtCity)
		{
			txtCity.setReadOnly(false);
			txtCity.setValue(null);
		}
		if(null != txtBranch)
		{
			txtBranch.setReadOnly(false);
			txtBranch.setValue(null);
		}
		
		
		
	}
	
	
//	private void buildDialogBox(String message,final Window populatePreviousWindowPopup)
//	{
//		Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
//		Button homeButton = new Button("OK");
//		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//		/*Button cancelButton = new Button("Cancel");
//		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);*/
//		 HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
//		horizontalLayout.setMargin(true);
//		horizontalLayout.setSpacing(true);
//		horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
//		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
//		//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
//		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
//		
//		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
//		layout.setSpacing(true);
//		layout.setMargin(true);
//		HorizontalLayout hLayout = new HorizontalLayout(layout);
//		hLayout.setMargin(true);
//		
//		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("");
//		dialog.setClosable(false);
//		dialog.setContent(hLayout);
//		dialog.setResizable(false);
//		dialog.setModal(true);
//		/*if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
//			dialog.setPositionX(450);
//			dialog.setPositionY(500);
//			//dialog.setDraggable(true);
//			
//			
//		}*/
//		getUI().getCurrent().addWindow(dialog);
//		homeButton.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = 7396240433865727954L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				dialog.close();
//				if(null != populatePreviousWindowPopup)
//					populatePreviousWindowPopup.close();
//				//fireViewEvent(MenuItemBean.CREATE_ROD, null);
//				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
//				
//			}
//		});
//	}
	
	private void buildDialogBox(String message,final Window populatePreviousWindowPopup,String btnName)
	{
		Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		/*Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);*/
		
		Button cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		
		
		if(SHAConstants.BTN_CANCEL.equalsIgnoreCase(btnName))
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.addComponent(cancelBtn);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			horizontalLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
		}
		else
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		}
		 
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		/*if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
			dialog.setPositionX(450);
			dialog.setPositionY(500);
			//dialog.setDraggable(true);
			
			
		}*/
		getUI().getCurrent().addWindow(dialog);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(null != populatePreviousWindowPopup)
					populatePreviousWindowPopup.close();
				//fireViewEvent(MenuItemBean.CREATE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		if(null != cancelBtn)
		{
			cancelBtn.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					/*if(null != populatePreviousWindowPopup)
						populatePreviousWindowPopup.close();*/
					//fireViewEvent(MenuItemBean.CREATE_ROD, null);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
		}
	}

	public void initAccntTable(PreviousAccountDetailsTable table)
	{
		previousAccountDetailsTable = table;
	}
	
	private void setTableValues()
	{
		if(null != previousAccountDetailsTable)
		{
			int rowCount = 1;
		//	List<List<PreviousAccountDetailsDTO>> previousListTable = this.bean.getPreviousAccountDetailsList();
			List<PreviousAccountDetailsDTO> previousListTable = this.bean.getPreviousAccntDetailsList();
			if(null != previousListTable && !previousListTable.isEmpty())
			{
				//for (List<PreviousAccountDetailsDTO> list : previousListTable) {
					for (PreviousAccountDetailsDTO previousAccountDetailsDTO : previousListTable) {
						
						previousAccountDetailsDTO.setChkSelect(false);
						previousAccountDetailsDTO.setChkSelect(null);						
						previousAccountDetailsDTO.setSerialNo(rowCount);
						previousAccountDetailsTable.addBeanToList(previousAccountDetailsDTO);
						rowCount ++ ;
					}
				//}
				
			}
		}
	}
	
	
	public Boolean getIsSaveToDB()
	{
		return isSaveToDB ;
	}

	public  void payModeShortcutListener(TextField searchField, final  Listener listener) {
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "EnterShortcut", ShortcutAction.KeyCode.F8, null) {
	      private static final long serialVersionUID = -2267576464623389044L;
	      @Override
	      public void handleAction( Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };	  
	    handleShortcut(searchField, getShortCutListener(searchField));
	  }
	
	
	public  void handleShortcut(final TextField textField, final ShortcutListener shortcutListener) {	
		textField.addFocusListener(new FocusListener() {			
			@Override
			public void focus(FocusEvent event) {				
				textField.addShortcutListener(shortcutListener);
				
			}
		});
		
	   textField.addBlurListener(new BlurListener() {
		
		@Override
		public void blur(BlurEvent event) {			
			
			textField.removeShortcutListener(shortcutListener);		
		}
	});
	  }
	
	private ShortcutListener getShortCutListener(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("EnterShortcut",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {							
				 
				if (null != vLayout
						&& vLayout.getComponentCount() > 0) {
					vLayout.removeAllComponents();
				}
				
				TextArea txtArea = new TextArea();
				txtArea.setNullRepresentation("");
				txtArea.setMaxLength(4000);
				
				txtArea.setValue(bean.getPayModeChangeReason());			
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea)event.getProperty();
						txtFld.setValue(txt.getValue());
						txtFld.setDescription(txt.getValue());
						// TODO Auto-generated method stub
						
					}
				});
				
				bean.setPayModeChangeReason(txtArea.getValue());	
				txtFld.setDescription(txtArea.getValue());
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);	
				//vLayout.setWidth("70%");
				//vLayout.setHeight("50%");
				
				
				final Window dialog = new Window();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				//dialog.show(getUI().getCurrent(), null, true);
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				
				okBtn.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}
	
	
	@SuppressWarnings({ "serial", "deprecation" })
	private void paymentModeValuechangeListener()
	{
		txtPayModeChangeReason.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = -1774887765294036092L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				String value = (String) event.getProperty().getValue();				
				if(null != value){
					//bean.setPayModeChangeReason(value);
					txtPayModeChangeReason.setValue(value);
					txtPayModeChangeReason.setDescription(value);
				}
				
			}
		});		
		
	}
	
}
