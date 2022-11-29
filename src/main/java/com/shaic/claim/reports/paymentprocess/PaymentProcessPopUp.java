package com.shaic.claim.reports.paymentprocess;

import java.util.Iterator;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.components.GTextField;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PaymentProcessPopUp extends ViewComponent{
	
	private PaymentProcessPopUpDTO bean;
	private BeanFieldGroup<PaymentProcessPopUpDTO> binder;
	
	private TextField txtIntimationNo;
	private TextField txtClaimNo;
	private TextField txtMainMemberName;
	private TextField txtNameOfTheInsured;
	private TextField txtsettledAmount;
	private TextField txtadmissionDate;
	private TextField txtddNo;
	private TextField txtbankName;
	private TextField txtbillReceivedDate;
	private TextField txtpolicyNo;
	private TextField txtinsuredPatientName;
	private TextField txthospitalName;
	private TextArea  txtaddress;
	private TextField txtdischargeDate;
	private TextField txtddDate;
	private TextField txtbillNumber;
	private TextField txtbillDate;
	private TextField txtCcZonalOfc;
	private TextField txtCcAreaOfc;
	private TextField txtBranchOfc;
	private TextField txtMailId;
	private Button btnDischargeVoucher;
	private Button btnDvCoveringLetter;
	private Button btnNriAndTeleSales;
	private Button btnPaymentAndDischarge;
	private Button btnHospitalPayment;
	private Button btnBillSummary;
	private Button btnSndMail;
	private Button btnConfirm;
	private Button btnClose;
	private Button btnAdd;
	
	
	
	public void init(PaymentProcessPopUpDTO bean) {
		this.bean = bean;
		Panel mainPanel = new Panel("Claim Details");
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setContent(getContent());			
		setCompositionRoot(mainPanel);
			
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<PaymentProcessPopUpDTO>(
				PaymentProcessPopUpDTO.class);
		this.binder.setItemDataSource(new PaymentProcessPopUpDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public VerticalLayout getContent() {
		initBinder();
		txtIntimationNo = binder.buildAndBind("Intimation No","intimationNo",TextField.class);
		txtClaimNo = binder.buildAndBind("Claim No","claimNumber",TextField.class);
		txtMainMemberName = binder.buildAndBind("Main Member Name","mainMemberName",TextField.class);	
		txtNameOfTheInsured = binder.buildAndBind("Name of the Insured","nameOfTheInsured",TextField.class);
		txtsettledAmount = binder.buildAndBind("Settled Amount","settledAmount",TextField.class);		
		txtadmissionDate = binder.buildAndBind("Admission Date","admissionDate",TextField.class);
		txtddNo = binder.buildAndBind("DD No.","ddNo",TextField.class);		
		txtbankName = binder.buildAndBind("Bank Name","bankName",TextField.class);		
		txtbillReceivedDate = binder.buildAndBind("Bill Received Date","billReceivedDate",TextField.class);		
		txtpolicyNo = binder.buildAndBind("Policy No","policyNo",TextField.class);		
		txtinsuredPatientName = binder.buildAndBind("Insured Patient Name","insuredPatientName",TextField.class);		
		txthospitalName = binder.buildAndBind("Hospital Name","hospitalName",TextField.class);		
		txtaddress = binder.buildAndBind("Address","address",TextArea.class);		
		txtdischargeDate = binder.buildAndBind("Discharge Date","dischargeDate",TextField.class);		
		txtddDate = binder.buildAndBind("DD Date","ddDate",TextField.class);			
		txtbillNumber = binder.buildAndBind("Bill Number","billNumber",TextField.class);		
		txtbillDate = binder.buildAndBind("Bill Date","billDate",TextField.class);				
		btnSndMail = new Button("Send Mail");
		btnConfirm = new Button("Confirm");
		btnClose = new Button("Close");
		HorizontalLayout buttonLayout1 = new HorizontalLayout(btnSndMail,btnConfirm,btnClose);
		buttonLayout1.setMargin(true);
		//buttonLayout1.setSpacing(true);
		//buttonLayout1.setWidth("100%");
		
			
		
		Panel ccDetailsPanel = new Panel("CC To Office");
		txtCcZonalOfc = binder.buildAndBind(" CC: Zonal Office","ccZonalOfc",TextField.class);
		txtCcZonalOfc.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtCcAreaOfc = binder.buildAndBind(" CC: Area Office","ccAreaOfc",TextField.class);
		txtCcAreaOfc.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtBranchOfc = binder.buildAndBind(" CC: Branch Office ","ccBranchOfc",TextField.class);
		txtBranchOfc.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		FormLayout ccDetailLayout = new FormLayout(txtCcZonalOfc,txtCcAreaOfc,txtBranchOfc);
		ccDetailsPanel.setContent(ccDetailLayout);
		
		Panel exportAndPrintPanel = new Panel("Export & Print");
		
		btnDischargeVoucher =  new Button("Discharge Voucher");
		btnDvCoveringLetter =  new Button("DV Covering Letter");
		btnNriAndTeleSales =  new Button("NRI And Tele Sales");
		btnPaymentAndDischarge =  new Button("Payment And Discharge");
		btnHospitalPayment =  new Button("Hospital Payment");
		btnBillSummary =  new Button("Bill Summary");
		HorizontalLayout exportDetailLayout = new HorizontalLayout(btnDischargeVoucher,btnDvCoveringLetter,btnNriAndTeleSales,btnPaymentAndDischarge);
		HorizontalLayout exportDetailLayout1 = new HorizontalLayout(btnHospitalPayment,btnBillSummary);
	    VerticalLayout buttonLayout = new VerticalLayout(exportDetailLayout,exportDetailLayout1);
	    buttonLayout.setMargin(true);
	    buttonLayout.setSpacing(true);
	    //buttonLayout.setWidth("20%");
	    buttonLayout.setComponentAlignment(exportDetailLayout, Alignment.MIDDLE_CENTER);
	    buttonLayout.setComponentAlignment(exportDetailLayout1, Alignment.MIDDLE_CENTER);
		exportAndPrintPanel.setContent(buttonLayout);
		
		
		Panel mailIdPanel = new Panel("Mail id's");
		txtMailId = binder.buildAndBind("Mail ID","mailId",TextField.class);
		btnAdd = new Button("Add");
		FormLayout mailIdLayout = new FormLayout(txtMailId);
		HorizontalLayout mailidLayout = new HorizontalLayout(mailIdLayout,btnAdd);
		mailidLayout.setMargin(true);
		mailidLayout.setSpacing(true);
		mailIdPanel.setContent(mailidLayout);
		
		
		initFieldValue();
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtClaimNo,txtMainMemberName,txtNameOfTheInsured,txtsettledAmount,txtadmissionDate,
				txtddNo,txtbankName,txtbillReceivedDate); 
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(txtpolicyNo,txtinsuredPatientName,txthospitalName,txtaddress,txtdischargeDate,txtddDate,
				txtbillNumber,txtbillDate);
		formLayoutRight.setSpacing(true);		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		setReadOnly(formLayoutLeft,true);
		setReadOnly(formLayoutRight,true);
		fieldLayout.setMargin(true);
		VerticalLayout mainLayout = new VerticalLayout(fieldLayout,ccDetailsPanel,exportAndPrintPanel,mailIdPanel,buttonLayout1);
		
		
		return mainLayout;
	}
	
	 @SuppressWarnings({ "deprecation" })
		private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
			Iterator<Component> formLayoutLeftComponent = a_formLayout
					.getComponentIterator();
			while (formLayoutLeftComponent.hasNext()) {
				Component c = formLayoutLeftComponent.next();
				if (c instanceof TextField) {
					TextField field = (TextField) c;
					field.setWidth("300px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				} else if(c instanceof GTextField){
					GTextField field = (GTextField) c;
					field.setWidth("300px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					
				}else if (c instanceof TextArea) {
					TextArea field = (TextArea) c;
					field.setWidth("350px");
			        field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
			}
		}
	
	    public void initFieldValue(){
//		binder.setItemDataSource(bean);
	    }	
	
	
	

}
