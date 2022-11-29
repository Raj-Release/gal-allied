package com.shaic.reimbursement.paymentprocess.initiateRecovery;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.themes.Reindeer;


public class InitiateRecoveryDetailsPage extends ViewComponent{
	
	@EJB
	private MasterService masterService;
	
	private TextField txtIntimationNo;

	private ComboBox cmbDocumentRecvFrom;
	
	private TextField txtAmountSettled;
	
	private TextField txtAmountRecoverable;
	
	private TextField txtRodNumber;
	
	private TextField txtHospitalCode;
	
	private TextField txtTdsDeducted;
	
	private ComboBox cmbPaymentCancelType;
	
	private ComboBox cmbReasonforRecovery;
	
	private ComboBox cmbNatureforRecovery;
	
	private ComboBox paymentType;
	
	private TextField approvedAmt;
	
	private TextField payableAt;
	
	private TextField payableName;
	
	private TextField instrumentNumber;
	
	private TextField remittanceBankName;
	
	private TextField remittanceBankBranch;
	
	private TextField remittanceAccount;
	
	private DateField instrumentDate;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private BeanFieldGroup<PaymentInitiateRecoveryTableDTO> binder;
	
	private PaymentInitiateRecoveryTableDTO bean;
	
	private Window popUp;
	
	
	
	@PostConstruct
	public void initView() {
	}
	
	public void init(PaymentInitiateRecoveryTableDTO searchDTO){
		
		this.bean = searchDTO;
//		this.popup = popup;
		
		this.binder = new BeanFieldGroup<PaymentInitiateRecoveryTableDTO>(PaymentInitiateRecoveryTableDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		txtIntimationNo = (TextField) binder.buildAndBind("Intimation No","intimationNo", TextField.class);
		txtIntimationNo.setEnabled(false);

		cmbDocumentRecvFrom =(ComboBox) binder.buildAndBind("Received From","documentReceivedFrom", ComboBox.class);
		cmbDocumentRecvFrom.setEnabled(false);
		
		txtAmountSettled = (TextField) binder.buildAndBind("Amount Settled","amountSettled", TextField.class);
		txtAmountSettled.setEnabled(false);
		
		txtAmountRecoverable = (TextField) binder.buildAndBind("Amount Recoverable","amountRecoverable", TextField.class);
		
		txtRodNumber = (TextField) binder.buildAndBind("ROD Number","rodNumber", TextField.class);
		txtRodNumber.setEnabled(false);
		
		txtHospitalCode = (TextField) binder.buildAndBind("Hospital Code","hospitalCode", TextField.class);
		txtHospitalCode.setEnabled(false);
		
		txtTdsDeducted = (TextField) binder.buildAndBind("TDS Deducted","tdsDeducted", TextField.class);
		txtTdsDeducted.setEnabled(false);
		
		cmbPaymentCancelType = (ComboBox)binder.buildAndBind("Payment Cancel Type","paymentCancelType",ComboBox.class);
		
		cmbReasonforRecovery = (ComboBox) binder.buildAndBind("Reason for Recovery","reasonForRecovery", ComboBox.class);
		
		cmbNatureforRecovery = (ComboBox) binder.buildAndBind("Nature of Recovery","natureofRecovery", ComboBox.class);
		
		paymentType = (ComboBox) binder.buildAndBind("Payment Type","paymentTypeVal", ComboBox.class);
		
		approvedAmt = (TextField) binder.buildAndBind("Approved Amount","approvedAmount", TextField.class);

		payableAt = (TextField) binder.buildAndBind("Payable At","payableAt", TextField.class);
		
		/**
		 * code changes for BANCS Bugs fixing
		 */
		//payableName = (TextField) binder.buildAndBind("Payable Name","payableName", TextField.class);
		if(bean.getPayeeName() != null && !bean.getPayeeName().isEmpty()){
			payableName = (TextField) binder.buildAndBind("Payable Name","payeeName", TextField.class);
		}else if(bean.getNomineeName() != null && !bean.getNomineeName().isEmpty()){
			payableName = (TextField) binder.buildAndBind("Payable Name","nomineeName", TextField.class);
		}else{
			payableName = (TextField) binder.buildAndBind("Payable Name","legalHeirName", TextField.class);
		}
		payableName.setEnabled(false);
		
		//instrumentNumber =(TextField) binder.buildAndBind("Instrument Number","instrumentNo", TextField.class);
		instrumentNumber =(TextField) binder.buildAndBind("Instrument Number","utrNumber", TextField.class);
		instrumentNumber.setEnabled(false);
		
		//instrumentDate = (DateField) binder.buildAndBind("Instrument Date","instrumentDate", DateField.class);
		instrumentDate = (DateField) binder.buildAndBind("Instrument Date","chequeDDDate", DateField.class);
		instrumentDate.setEnabled(false);
		
		remittanceBankName = (TextField) binder.buildAndBind("Remittance Bank Name","remittanceBankName", TextField.class);
		
		remittanceBankBranch = (TextField) binder.buildAndBind("Remittance Bank Branch","remittanceBankBranch", TextField.class);
		
		remittanceAccount = (TextField) binder.buildAndBind("Remittance Account","remittanceAccount", TextField.class);
		
		
		

				
		FormLayout firstForm = new FormLayout(txtIntimationNo,cmbDocumentRecvFrom,txtAmountSettled,txtAmountRecoverable,cmbPaymentCancelType,instrumentNumber,paymentType,payableName,remittanceBankName);
		firstForm.setSpacing(true);
		
		FormLayout secondForm = new FormLayout(txtRodNumber,txtHospitalCode,txtTdsDeducted,cmbReasonforRecovery,cmbNatureforRecovery,instrumentDate,payableAt,remittanceBankBranch,remittanceAccount);
		firstForm.setSpacing(true);
		
		HorizontalLayout horLayout = new HorizontalLayout(firstForm,secondForm);
		horLayout.setSpacing(true);
		
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		HorizontalLayout buttonHor = new HorizontalLayout(submitBtn,cancelBtn);
		buttonHor.setSpacing(true);
		
		VerticalLayout mainHor = new VerticalLayout(horLayout,buttonHor);
		mainHor.setComponentAlignment(buttonHor, Alignment.BOTTOM_CENTER);
		mainHor.setComponentAlignment(horLayout, Alignment.MIDDLE_CENTER);
		mainHor.setSpacing(true);
		mainHor.setMargin(true);
		
//		showOrHideValidation(false);
		
		addListener();
		
		setUpReference();
		
		setCompositionRoot(mainHor);

		
	}
	
	public void setUpReference(){
		
		BeanItemContainer<SelectValue> reasonForRecovery = masterService.getMasterValueByCode("RESONFRRCVRY");
		cmbReasonforRecovery.setContainerDataSource(reasonForRecovery);
		cmbReasonforRecovery.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonforRecovery.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> natureForRecovery = masterService.getMasterValueByCode("NATUREOFRCVRY");
		cmbNatureforRecovery.setContainerDataSource(natureForRecovery);
		cmbNatureforRecovery.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbNatureforRecovery.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> paymentCancel = masterService.getMasterValueByCode("PAYRPSUBTYPE");
		cmbPaymentCancelType.setContainerDataSource(paymentCancel);
		cmbPaymentCancelType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPaymentCancelType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> paymentTypeList = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue cheq = new SelectValue();
		cheq.setId(01l);
		cheq.setValue("CHEQUE");
		SelectValue neft = new SelectValue();
		neft.setId(02l);
		neft.setValue("NEFT");
		paymentTypeList.addBean(cheq);
		paymentTypeList.addBean(neft);
		paymentType.setContainerDataSource(paymentTypeList);
		paymentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		paymentType.setItemCaptionPropertyId("value");
	}
	
	public void setPopUp(Window popup){
		this.popUp = popup;
	}
	
	public void addListener(){
		
		cmbReasonforRecovery.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{
					BeanItemContainer<SelectValue> natureForRecovery = masterService.getMasterValueByCode("NATUREOFRCVRY");
					if(value.getValue() != null && value.getValue().equalsIgnoreCase("Excess payment")){
						
						BeanItemContainer<SelectValue> finalList = new BeanItemContainer<SelectValue>(SelectValue.class);
						List<SelectValue> itemIds = natureForRecovery.getItemIds();
						
						List<SelectValue> selectVal = new ArrayList<SelectValue>();
						if(itemIds != null && itemIds.size() > 0){

							if(bean.getDocumentReceivedFrom().getId() != null &&
									!bean.getDocumentReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
								selectVal.add(itemIds.get(0));
							}
							selectVal.add(itemIds.get(1));
						}
						natureForRecovery.removeAllItems();
						natureForRecovery.addAll(selectVal);
						cmbNatureforRecovery.setContainerDataSource(natureForRecovery);
						cmbNatureforRecovery.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cmbNatureforRecovery.setItemCaptionPropertyId("value");
					} else if(value.getValue() != null && value.getValue().equalsIgnoreCase("Wrong payment")){

						
						BeanItemContainer<SelectValue> finalList = new BeanItemContainer<SelectValue>(SelectValue.class);
						List<SelectValue> itemIds = natureForRecovery.getItemIds();
						
						List<SelectValue> selectVal = new ArrayList<SelectValue>();
						if(itemIds != null && itemIds.size() > 0){
							
							selectVal.add(itemIds.get(2));
							if(bean.getDocumentReceivedFrom().getId() != null &&
									!bean.getDocumentReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
								selectVal.add(itemIds.get(3));
							}
						}
						natureForRecovery.removeAllItems();
						natureForRecovery.addAll(selectVal);
						cmbNatureforRecovery.setContainerDataSource(natureForRecovery);
						cmbNatureforRecovery.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cmbNatureforRecovery.setItemCaptionPropertyId("value");
					
					}
				}
			}
		});
		

		
		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
//				                	UI.getCurrent().removeWindow(popup);
				                	popUp.close();
				                	fireViewEvent(MenuItemBean.PAYMENT_INITIATE_RECOVERY, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
				dialog.setClosable(false);
			}
		});
		submitBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				setTableValuestoDto();
				if(validatePage()){
				
				fireViewEvent(PaymentInitiateRecoverySearchPresenter.PAYMENT_INITIATE_RECOVERY_SUBMIT,bean);
				popUp.close();
				
				}
				
//				UI.getCurrent().removeWindow(popup);
				
				/*if(validatePage()){
					fireViewEvent(CloseClaimPageRodLevelPresenter.SUBMIT_DATA, searchDTO,bean);
				}*/
				
			}
		});
		
	
	}
	
	public void setTableValuestoDto(){
		bean.setAmountRecoverable(txtAmountRecoverable.getValue() != null ? txtAmountRecoverable.getValue() : null);
//		bean.setApprovedAmount(approvedAmt.getValue() != null ? Double.valueOf(approvedAmt.getValue()) : null);
		bean.setPaymentCancelType((SelectValue)cmbPaymentCancelType.getValue());
		bean.setReasonForRecovery((SelectValue)cmbReasonforRecovery.getValue());
		bean.setNatureofRecovery((SelectValue)cmbNatureforRecovery.getValue());
		bean.setInstrumentNo(instrumentNumber.getValue() != null ?instrumentNumber.getValue() : null);
		bean.setInstrumentDate(instrumentDate.getValue() != null ? instrumentDate.getValue() : null);
		bean.setPaymentTypeVal((SelectValue)paymentType.getValue());
		bean.setPayableAt(payableAt.getValue() != null ? payableAt.getValue() : null);
		bean.setPayableName(payableName.getValue() != null ? payableName.getValue() : null);
		bean.setRemittanceBankName(remittanceBankName.getValue() != null ? remittanceBankName.getValue() : null);
		bean.setRemittanceBankBranch(remittanceBankBranch.getValue() != null ? remittanceBankBranch.getValue() : null);
		bean.setRemittanceAccount(remittanceAccount.getValue() != null ? remittanceAccount.getValue() : null);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		bean.setCreatedBy(userName);
		
		
	}
	public boolean validatePage() {
		Boolean hasError = false;
//		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();		
		
		SelectValue selectValPaymentCan = (SelectValue)cmbPaymentCancelType.getValue();
		SelectValue selectValueRes = (SelectValue)cmbReasonforRecovery.getValue();
		SelectValue selectValueNat = (SelectValue)cmbNatureforRecovery.getValue();
		
		
		if(selectValPaymentCan == null || selectValPaymentCan.getValue() == null){
			hasError =true;
			eMsg.append("Please Select Payment Cancel Type<br>");
			
		}
		
		if(selectValueRes == null || selectValueRes.getValue() == null){
			hasError =true;
			eMsg.append("Please Select Reason for Recovery<br>");
			
		}
		
		if(selectValueNat == null || selectValueNat.getValue() == null){
			hasError =true;
			eMsg.append("Please Select Nature for Recovery");
			
		}

		if (hasError) {
//			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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

			hasError = true;
			return !hasError;
		} else {
			
			
//			showOrHideValidation(false);
			return true;
		}
	}
	
	

}