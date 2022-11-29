package com.shaic.claim.ompviewroddetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.claim.claimhistory.view.ompView.OMPAckDocReceivedMapper;
import com.shaic.domain.OMPIntimation;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OMPViewIntimationDetailsUI extends ViewComponent{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OptionGroup optionGroup;
	
	private OptionGroup optionGroup1;
	// Intimation Details
	
    private TextField txtIntimationNo;
    
    private TextField txtIntimationDate;
    
    private TextField txtLossDate;
    
    private TextField txtTPAIntimationNo;
    
    private TextField txtInsuredName;
    
    private TextField txtAilment;
    
    private TextField txtEventCode;
    
    private TextField txtPlaceofVisit;
    
    private TextField txtModeofIntimation;
    
    private TextField txtIntimatedBy;
    
    private TextField txtIntimatorName;
    
    private TextField txtCallerContactNo;
    
    private TextField txtInitialProvisionAmt;
    
    private TextField txtINRConversionRate;
    
    private TextField txtINRValue;
    
    private TextField txtPolicyNo;
    
    private TextField txtPolicyIssuingOffice;
    
    private TextField txtProductName;
    
    private TextField txtState;
    
    private TextField txtCity;
    
    private TextField txtAdmissionDate;
    
    private TextField txtHospitalCode;
    
    private TextField txtHospitalCity;
    
    private TextField txtCountry;
    
    private TextArea txtIntimationRemarks;
    
    private TextField txtSMCode;
    
    private TextField txtSMName;
    
    private TextField txtAgentOrBrokerCode;
    
    private TextField txtAgentOrBrokerName;
    
    private VerticalLayout mainVerticalLayout;
    
    // Registration Details
    
    private TextField txtClaimNo;
    
    private TextField txtRegistrationStarus;
    
    private TextField txtProvisionAmount;
    
    private TextField txtClaimType;
    
    private TextField txtRegistrationRemarks;
    
    //ROD and Bill Entry
    
    private TextField txtAilmentLoss;
    
    private OMPViewClaimStatusDto bean;
    
    private BeanFieldGroup<OMPViewClaimStatusDto> binder;
    
  public void init(/*OMPViewClaimStatusDto bean,Long rodkey*/ OMPIntimation intimation ){
    	

	  OMPViewClaimStatusDto intimationDetails = OMPAckDocReceivedMapper.getInstance().getViewClaimStatusDto(intimation);
////		Hospitals hospitals = hospitalService.getHospitalById(intimation.getHospital());
////		getHospitalDetails(intimationDetails, hospitals);
		this.bean = intimationDetails;
		
		optionGroup = new OptionGroup("");
    	optionGroup.setNullSelectionAllowed(true);
    	optionGroup.addItems(getReadioButtonOptions());
    	optionGroup.setItemCaption(true, "Cashless");
    	optionGroup.setItemCaption(false, "Reimbursement");
    	optionGroup.setStyleName("horizontal");
    	
    	if(bean.getClaimTypeValue()!= null && bean.getClaimTypeValue().getValue() != null && bean.getClaimTypeValue().getValue().equalsIgnoreCase("Cashless")) {
    		optionGroup.setValue(true);
			
		} else
		{
			optionGroup.setValue(false);
		}
    	optionGroup.setReadOnly(true);
    	
    	optionGroup1 = new OptionGroup("");
    	optionGroup1.setStyleName("horizontal");
    	//Vaadin8-setImmediate() optionGroup1.setImmediate(false);
    	optionGroup1.setNullSelectionAllowed(true);
    	optionGroup1.addItems(getReadioButtonOptions());
    	optionGroup1.setItemCaption(true, "Hospitalisation");
    	optionGroup1.setItemCaption(false, "Non Hospitalisation");
    	
    	if((bean.getHospitalisationFlag()!= null && bean.getHospitalisationFlag().equalsIgnoreCase("Y"))&&(bean.getNonHospitalisationFlag()!= null && bean.getNonHospitalisationFlag().equalsIgnoreCase("N"))) {
			optionGroup1.setValue(true);
		} 
		else{
			optionGroup1.setValue(false);
		}
    	optionGroup1.setReadOnly(true);
    	
    	this.binder = new BeanFieldGroup<OMPViewClaimStatusDto>(OMPViewClaimStatusDto.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
//    	
//    	FormLayout optionForm = new FormLayout(optionGroup);
//    	FormLayout optionForm2 = new FormLayout(optionGroup1);
//    	
    	HorizontalLayout optionHLayout = new HorizontalLayout(/*optionForm,optionForm2*/);
    	
    	txtIntimationNo = (TextField) binder.buildAndBind("Intimation No","intimationId",TextField.class);
    	txtIntimationDate = (TextField)binder.buildAndBind("Intimation Date *","intimationDate",TextField.class);
    	txtIntimationDate.setRequired(true);
    	txtLossDate = (TextField)binder.buildAndBind("Loss Date *","lossDate",TextField.class);
    	txtLossDate.setRequired(true);
    	txtTPAIntimationNo = (TextField)binder.buildAndBind("TPA Intimation No","tpaIntimationNo",TextField.class);
    	txtInsuredName = (TextField)binder.buildAndBind("Insured Name*","insuredName",TextField.class);    
    	txtInsuredName.setRequired(true);
    	txtAilment = (TextField)binder.buildAndBind("Ailment/Loss","ailmentOrLoss",TextField.class);
    	txtEventCode = (TextField)binder.buildAndBind("Event Code","eventCode",TextField.class);
    	txtEventCode.setRequired(true);
    	txtPlaceofVisit = (TextField)binder.buildAndBind("Place of Visit","placeofVisit",TextField.class);
    	txtModeofIntimation = (TextField)binder.buildAndBind("Mode of Intimation","modeOfIntimation",TextField.class);
    	txtIntimatedBy = (TextField)binder.buildAndBind("Intimated By","intimatedBy",TextField.class);
    	txtIntimatorName = (TextField)binder.buildAndBind("Caller/Intimator Name","intimatorName",TextField.class);
    	txtCallerContactNo = (TextField)binder.buildAndBind("Caller Contact No ","callerContactNo",TextField.class);
    	txtInitialProvisionAmt = (TextField)binder.buildAndBind("Initial Provision Amt($)","intialProvisionAmt",TextField.class);
    	txtInitialProvisionAmt.setRequired(true);
    	txtINRConversionRate = (TextField)binder.buildAndBind("INR Conversion Rate","inrConversionRate",TextField.class);
    	txtINRConversionRate.setRequired(true);
    	txtINRValue = (TextField)binder.buildAndBind("INR Value","inrValue",TextField.class);
    	
    	FormLayout firstForm = new FormLayout(optionGroup,txtIntimationNo,txtIntimationDate,txtLossDate,txtTPAIntimationNo,txtInsuredName,txtAilment,txtEventCode,txtPlaceofVisit,txtModeofIntimation,txtIntimatedBy,txtIntimatorName,txtCallerContactNo,txtInitialProvisionAmt,txtINRConversionRate,txtINRValue);
    	
    	firstForm.setSpacing(true);
    	setReadOnly(firstForm,true);
    	
    	txtPolicyNo = (TextField)binder.buildAndBind("Policy No","policyNo",TextField.class);
    	txtPolicyIssuingOffice = (TextField)binder.buildAndBind("Policy Issuing Office","policyIssuingOffice",TextField.class);
    	txtProductName = (TextField)binder.buildAndBind("Product Name","productName",TextField.class);
    	txtState = (TextField)binder.buildAndBind("State","state",TextField.class);
    	txtCity = (TextField)binder.buildAndBind("City","city",TextField.class);
    	txtAdmissionDate = (TextField)binder.buildAndBind("Admission Date","admissionDate",TextField.class);
    	txtHospitalCode = (TextField)binder.buildAndBind("Hospital Code/Name","hospitalCode",TextField.class);
    	txtHospitalCity = (TextField)binder.buildAndBind("City","hospitalCity",TextField.class);
    	txtCountry = (TextField)binder.buildAndBind("Country","country",TextField.class);
    	txtIntimationRemarks = (TextArea)binder.buildAndBind("Remarks","remarks",TextArea.class);
    	txtIntimationRemarks.setRows(4);
    	txtIntimationRemarks.setColumns(25);
    	handleTextAreaPopup(txtIntimationRemarks,null);
    	txtSMCode = (TextField)binder.buildAndBind("SM Code","smCode",TextField.class);
    	txtSMName = (TextField)binder.buildAndBind("SM Name","smName",TextField.class);
    	txtAgentOrBrokerCode = (TextField)binder.buildAndBind("Agent / Broker Code","agentOrBrokerCode",TextField.class);
    	txtAgentOrBrokerName = (TextField)binder.buildAndBind("Agent/Broker Name","agentOrBrokerName",TextField.class);
    	
    	FormLayout secondForm = new FormLayout(optionGroup1,txtPolicyNo,txtPolicyIssuingOffice,txtProductName,txtState,txtCity,txtAdmissionDate,txtHospitalCode,txtHospitalCity,txtCountry,txtIntimationRemarks,txtSMCode,txtSMName,txtAgentOrBrokerCode,txtAgentOrBrokerName);
    	secondForm.setSpacing(true);
    	setReadOnly(secondForm,true);
    	
    	HorizontalLayout intimationLayout = new HorizontalLayout(firstForm,secondForm);
    	intimationLayout.setComponentAlignment(firstForm, Alignment.TOP_CENTER);
    	intimationLayout.setComponentAlignment(secondForm, Alignment.MIDDLE_RIGHT);
    	intimationLayout.setWidth("110%");
    	
	    Panel mainPanel = new Panel(intimationLayout);
	    mainPanel.addStyleName("girdBorder");
	    mainPanel.setCaption("Intimation Details");
	    
//	    Panel registrationPanel = registrationDetailsPanel();
//	    
//	    Panel receiptOfDocumentAndBillEntryPanel = receiptOfDocumentAndBillEntry(); 
//
//	    
//	    ompProcessingDetailsObj = ompProcessingDetailsInstance.get();
//	    ompProcessingDetailsObj.init("", false, false);
//	    ompProcessingDetailsObj.setCaption("OMP Processing Details");
//	    
//	    setProcessingDetailsTable();
//	    
//	    ompPaymentDetailsObj = ompPaymentDetailsInstance.get();
//	    ompPaymentDetailsObj.init("", false, false);
//	    ompPaymentDetailsObj.setCaption("Payment Details");
	    
	    	    	        	    
	    mainVerticalLayout = new VerticalLayout(optionHLayout,mainPanel);
	    mainVerticalLayout.setSpacing(true);
	    mainVerticalLayout.setComponentAlignment(mainPanel, Alignment.TOP_CENTER);
	    mainVerticalLayout.setComponentAlignment(optionHLayout, Alignment.TOP_RIGHT);
	    mainVerticalLayout.setMargin(true);
	    
	    
	//    addListener();
	    
	    setCompositionRoot(mainVerticalLayout);
    	
    }

  protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
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
 				field.setNullRepresentation("-");
 				field.setReadOnly(readOnly);
 				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
 			} else if (c instanceof TextArea) {
 				TextArea field = (TextArea) c;
 				field.setWidth("350px");
 		        field.setNullRepresentation("-");
 				field.setReadOnly(readOnly);
 				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
 			}
 		}
 	}
  
//R1276 - Introduced Textarea and enable F8 option in it.
@SuppressWarnings("unused")
public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

	ShortcutListener enterShortCut = new ShortcutListener(
			"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

		private static final long serialVersionUID = 1L;
		@Override
		public void handleAction(Object sender, Object target) {
			((ShortcutListener) listener).handleAction(sender, target);
		}
	};
	handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

}

public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
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

private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
{
	ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {
		private static final long serialVersionUID = 1L;
		@SuppressWarnings({ "static-access", "deprecation" })
		@Override
		public void handleAction(Object sender, Object target) {
			VerticalLayout vLayout =  new VerticalLayout();

			vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
			vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
			vLayout.setMargin(true);
			vLayout.setSpacing(true);
			final TextArea txtArea = new TextArea();
			txtArea.setStyleName("Boldstyle"); 
			txtArea.setValue(txtFld.getValue());
			txtArea.setNullRepresentation("");
			txtArea.setSizeFull();
			txtArea.setWidth("100%");
			txtArea.setMaxLength(4000);
			txtArea.setReadOnly(true);
			txtArea.setRows(25);

			txtArea.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(ValueChangeEvent event) {
					txtFld.setValue(((TextArea)event.getProperty()).getValue());
				}
			});
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

			final Window dialog = new Window();

			String strCaption = "Intimation Remarks";

			dialog.setCaption(strCaption);

			dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
			dialog.setWidth("45%");
			dialog.setClosable(true);

			dialog.setContent(vLayout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.setDraggable(true);
			dialog.setData(txtFld);

			dialog.addCloseListener(new Window.CloseListener() {
				@Override
				public void windowClose(CloseEvent e) {
					dialog.close();
				}
			});

			if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
				dialog.setPositionX(450);
				dialog.setPositionY(500);
			}
			getUI().getCurrent().addWindow(dialog);
			okBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});	
		}
	};

	return listener;
}
}
