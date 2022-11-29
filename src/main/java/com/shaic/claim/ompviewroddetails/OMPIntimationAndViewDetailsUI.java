package com.shaic.claim.ompviewroddetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.OMPClaimPayment;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.claim.claimhistory.view.ompView.OMPEarlierRodMapper;
import com.shaic.domain.MasterService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
public class OMPIntimationAndViewDetailsUI extends ViewComponent{
	
		
private static final long serialVersionUID = 1L;

	private OptionGroup optionGroup;
	
	private OptionGroup optionGroup1;
	// Intimation Details
	
    private TextField txtIntimationNo;
    
    private TextField txtIntimationDate;
    
    private TextField txtLossOfDate;
    
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
    
    private TextField txtIntimationRemarks;
    
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
    
    private Long rodKey;
    // create intimation DTO and add beanfield
    private BeanFieldGroup<OMPViewClaimStatusDto> binder;
    
    private BeanFieldGroup<OMPClaimStatusRegistrationDto> registrationBinder;
    
    @Inject
    private Instance<OMPProcessingDetailsTable> ompProcessingDetailsInstance;
    
    private OMPProcessingDetailsTable ompProcessingDetailsObj;
    
    @Inject
    private Instance<OMPPaymentDetailsTable> ompPaymentDetailsInstance;
    
    private OMPPaymentDetailsTable ompPaymentDetailsObj;
    
    @Inject
    private Instance<OMPRodAndBillEntryDetailTable> ompRodAndBillEntryDetailInstance;
    
    private OMPRodAndBillEntryDetailTable ompRodAndBillEntryDetailObj;
    
    private OMPViewClaimStatusDto bean;
    
	@EJB
	private OMPIntimationService ompIntimationService;
	
	@EJB
	private OMPClaimService ompclaimService;
    
	@EJB
	private MasterService masterService;
	
	@Inject
    private OMPProcessRODBillEntryService rodBillentryService;
	
    public void init(OMPViewClaimStatusDto bean, Map<String, Object> map){
    	
    	this.bean = bean;
    	this.rodKey = bean.getRodKey();
    	optionGroup = new OptionGroup("");
    	optionGroup.setNullSelectionAllowed(true);
    	optionGroup.addItems(getReadioButtonOptions());
    	optionGroup.setItemCaption(true, "Cashless");
    	optionGroup.setItemCaption(false, "Reimbursement");
    	optionGroup.setStyleName("horizontal");
    	OMPClaim claim = ompclaimService.getClaimByKey(bean.getClaimKey());

    	if(claim!= null&& claim.getClaimType()!= null && claim.getClaimType().getValue() != null && claim.getClaimType().getValue().equalsIgnoreCase("Cashless")) {
    		optionGroup.setValue(true);
		} 
    	if(claim!= null&& claim.getClaimType()!= null && claim.getClaimType().getValue() != null && claim.getClaimType().getValue().equalsIgnoreCase("Reimbursement")) {
			optionGroup.setValue(false);
		}
    	optionGroup.setReadOnly(Boolean.TRUE);
    	optionGroup1 = new OptionGroup();
//		hospitalOption = binder.buildAndBind("","hospTypeBooleanval",OptionGroup.class);
    	optionGroup1.addItem("Non Medical");
		if(claim.getNonHospitalisationFlag()!= null && claim.getNonHospitalisationFlag().equalsIgnoreCase("Y")){
			optionGroup1.setValue("Non Medical");
		}   	
		optionGroup1.setReadOnly(Boolean.TRUE);
    	this.binder = new BeanFieldGroup<OMPViewClaimStatusDto>(OMPViewClaimStatusDto.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
    	
    	txtIntimationNo = (TextField) binder.buildAndBind("Intimation No","intimationId",TextField.class);
    	txtIntimationDate = (TextField)binder.buildAndBind("Intimation Date *","intimationDate",TextField.class);
    	txtIntimationDate.setValue(new SimpleDateFormat("dd-MM-yyyy").format(claim.getIntimation().getIntimationDate()));
    	txtIntimationDate.setRequired(true);
//    	txtLossOfDate = (TextField)binder.buildAndBind("Loss Date *","lossDate",TextField.class);
    	txtLossOfDate = new TextField("Loss Date");
    	if(claim.getIntimation()!= null && claim.getIntimation().getLossDateTime()!= null ){
    		txtLossOfDate.setValue(new SimpleDateFormat("dd-MM-yyyy").format(claim.getIntimation().getLossDateTime()));
    	}
    	txtLossOfDate.setRequired(true);
    	txtTPAIntimationNo = (TextField)binder.buildAndBind("TPA Intimation No","tpaIntimationNo",TextField.class);
    	txtTPAIntimationNo.setValue(claim.getIntimation().getTpaIntimationNumber());
    	txtInsuredName = (TextField)binder.buildAndBind("Insured Name*","insuredName",TextField.class);    
    	txtInsuredName.setRequired(true);
    	txtAilment = (TextField)binder.buildAndBind("Ailment/Loss","ailmentOrLoss",TextField.class);
    	if(claim!= null&&claim.getIntimation()!= null && claim.getIntimation().getAilmentLoss()!= null){
    		txtAilment.setValue(claim.getIntimation().getAilmentLoss());
    	}else{
    		if(claim!= null&&claim.getIntimation()!= null &&claim.getIntimation().getLossDetails()!= null)
    			txtAilment.setValue(claim.getIntimation().getLossDetails());
    	}
    	txtEventCode = (TextField)binder.buildAndBind("Event Code","eventCode",TextField.class);
    	txtEventCode.setValue(claim.getIntimation().getEvent().getEventCode());
    	txtEventCode.setRequired(true);
    	txtPlaceofVisit = (TextField)binder.buildAndBind("Place of Visit","placeofVisit",TextField.class);
    	txtPlaceofVisit.setValue(claim.getIntimation().getPlaceVisit());
    	txtModeofIntimation = (TextField)binder.buildAndBind("Mode of Intimation","modeOfIntimation",TextField.class);
    	txtIntimatedBy = (TextField)binder.buildAndBind("Intimated By","intimatedBy",TextField.class);
    	txtIntimatorName = (TextField)binder.buildAndBind("Caller/Intimator Name","intimatorName",TextField.class);
    	txtIntimatorName.setValue(claim.getIntimation().getIntimaterName());
    	txtCallerContactNo = (TextField)binder.buildAndBind("Caller Contact No ","callerContactNo",TextField.class);
    	txtInitialProvisionAmt = (TextField)binder.buildAndBind("Initial Provision Amt($)","intialProvisionAmt",TextField.class);
    	txtInitialProvisionAmt.setValue(String.valueOf(claim.getIntimation().getDollarInitProvisionAmt()));
    	txtInitialProvisionAmt.setRequired(true);
    	txtINRConversionRate = (TextField)binder.buildAndBind("INR Conversion Rate","inrConversionRate",TextField.class);
    	txtINRConversionRate.setValue(String.valueOf(claim.getIntimation().getInrConversionRate()));
    	txtINRConversionRate.setRequired(true);
    	txtINRValue = (TextField)binder.buildAndBind("INR Value","inrValue",TextField.class);
    	txtINRValue.setValue(String.valueOf(claim.getIntimation().getInrTotalAmount()));
    	
    	FormLayout firstForm = new FormLayout(optionGroup,txtIntimationNo,txtIntimationDate,txtLossOfDate,txtTPAIntimationNo,txtInsuredName,txtAilment,txtEventCode,txtPlaceofVisit,txtModeofIntimation,txtIntimatedBy,txtIntimatorName,txtCallerContactNo,txtInitialProvisionAmt,txtINRConversionRate,txtINRValue);
    	
    	firstForm.setSpacing(true);
    	setReadOnly(firstForm,true);
    	
    	txtPolicyNo = (TextField)binder.buildAndBind("Policy No","policyNo",TextField.class);
    	txtPolicyIssuingOffice = (TextField)binder.buildAndBind("Policy Issuing Office","policyIssuingOffice",TextField.class);
    	txtProductName = (TextField)binder.buildAndBind("Product Name","productName",TextField.class);
    	txtState = (TextField)binder.buildAndBind("State","state",TextField.class);
    	txtCity = (TextField)binder.buildAndBind("City","city",TextField.class);
    	txtAdmissionDate = (TextField)binder.buildAndBind("Admission Date","admissionDate",TextField.class);
    	txtHospitalCode = (TextField)binder.buildAndBind("Hospital Code/Name","hospitalCode",TextField.class);
    	txtHospitalCode.setValue(claim.getIntimation().getHospitalName());
    	txtHospitalCity = (TextField)binder.buildAndBind("City","hospitalCity",TextField.class);
    	txtHospitalCity.setValue(claim.getIntimation().getCityName());
    	txtCountry = (TextField)binder.buildAndBind("Country","country",TextField.class);
    	if(claim.getCountryId()!=null){
			SelectValue countryValueByKey = masterService.getCountryValueByKey(claim.getCountryId());
			txtCountry.setValue(countryValueByKey != null && countryValueByKey.getValue() != null ? countryValueByKey.getValue() : "" );
			
		}
    	txtIntimationRemarks = (TextField)binder.buildAndBind("Remarks","remarks",TextField.class);
//    	txtIntimationRemarks.setValue(claim.getSuggestedRejectionRemarks());
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
	    
	    Panel registrationPanel = registrationDetailsPanel(claim);
	 	    
	    if(map.containsKey(SHAConstants.RECEIPT_OF_DOCUMENTTABLE) && map.get(SHAConstants.RECEIPT_OF_DOCUMENTTABLE) != null){
	    	ompRodAndBillEntryDetailObj = (OMPRodAndBillEntryDetailTable) map.get(SHAConstants.RECEIPT_OF_DOCUMENTTABLE);
	    
	    }
	    else{
	    	ompRodAndBillEntryDetailObj = ompRodAndBillEntryDetailInstance.get();
	    	ompRodAndBillEntryDetailObj.init("", false, false);	
	    }
	    
	    Component tableComponent = ompRodAndBillEntryDetailObj;
	    Panel receiptOfDocumentAndBillEntryPanel = receiptOfDocumentAndBillEntry(tableComponent,claim);
	    
	    if(map.containsKey(SHAConstants.BILLING_PROCESSING_TABLE) && map.get(SHAConstants.BILLING_PROCESSING_TABLE) != null){
	    	ompProcessingDetailsObj = (OMPProcessingDetailsTable) map.get(SHAConstants.BILLING_PROCESSING_TABLE);
	    }
	    else{
	    	ompProcessingDetailsObj = ompProcessingDetailsInstance.get();
		    ompProcessingDetailsObj.init("", false, false);
		}
	    
	    if(map.containsKey(SHAConstants.VIEW_PAYMENT_TABLE) && map.get(SHAConstants.VIEW_PAYMENT_TABLE) != null){
	    	ompPaymentDetailsObj = (OMPPaymentDetailsTable) map.get(SHAConstants.VIEW_PAYMENT_TABLE);	
	    	ompPaymentDetailsObj.init("", false, false);
	    	 List<OMPPaymentDetailsTableDTO> paymentList = new ArrayList<OMPPaymentDetailsTableDTO>();
	 	    if(bean.getClaimKey() != null){
	 	    	OMPReimbursement reimbursement = rodBillentryService.getLatestReimbursementDetailsByclaimKey(bean.getClaimKey());
	 	    	if(reimbursement!= null){
	 	    		if(bean!= null){
	 	    			bean.setAilment(reimbursement.getAilmentLoss());
	 	    		}
	 	    		if(bean!= null && bean.getIntimationId()!= null){
	 	    			List<OMPClaimPayment> paymemt = rodBillentryService.getOMPClaimPaymentByIntimationNo(bean.getIntimationId());
	 	    			if(paymemt!= null){
	 		    			paymentList =OMPEarlierRodMapper.getInstance().getOMPClaimPaymentDetails(paymemt);
	 		    			if(paymentList!= null){
	 		    				ompPaymentDetailsObj.setTableList(paymentList);
	 		    			}
	 		    		}
	 	    		}
//	 	    		List<OMPClaimPayment> paymemt = rodBillentryService.getOMPClaimPayment(reimbursement.getRodNumber());
//	 	    		if(paymemt!= null){
//	 	    			paymentList =OMPEarlierRodMapper.getInstance().getOMPClaimPaymentDetails(paymemt);
//	 	    			if(paymentList!= null){
//	 	    				ompPaymentDetailsObj.setTableList(paymentList);
//	 	    			}
//	 	    		}
	 	    	}
	 	    }
	    }
	    else{
	    	ompPaymentDetailsObj = ompPaymentDetailsInstance.get();
	    }
	    
	    ompProcessingDetailsObj.setCaption("OMP Processing Details");
	    ompPaymentDetailsObj.setCaption("Payment Details");
	    	    	        	    
	    mainVerticalLayout = new VerticalLayout(mainPanel,registrationPanel,receiptOfDocumentAndBillEntryPanel,ompProcessingDetailsObj,ompPaymentDetailsObj);
	    mainVerticalLayout.setSpacing(true);
	    mainVerticalLayout.setComponentAlignment(mainPanel, Alignment.TOP_CENTER);
	    mainVerticalLayout.setMargin(true);

	    setCompositionRoot(mainVerticalLayout);    
    }
        
    public void init(OMPViewClaimStatusDto bean,Long rodkey ){
     	
    	this.bean = bean;
    	this.rodKey = rodkey;    
    	    	
//    	optionGroup = binder.buildAndBind("","claimTypeBooleanval",OptionGroup.class);
    	optionGroup = new OptionGroup("");
    	optionGroup.setNullSelectionAllowed(true);
    	optionGroup.addItems(getReadioButtonOptions());
    	optionGroup.setItemCaption(true, "Cashless");
    	optionGroup.setItemCaption(false, "Reimbursement");
    	optionGroup.setStyleName("horizontal");
    	
    	OMPClaim claim = ompclaimService.getClaimByKey(bean.getClaimKey());
    	
    	if(bean.getClaimTypeValue()!= null && bean.getClaimTypeValue().getValue() != null && bean.getClaimTypeValue().getValue().equalsIgnoreCase("Cashless")) {
    		optionGroup.setValue(true);
			
		} if(claim!= null&& claim.getClaimType()!= null && claim.getClaimType().getValue() != null && claim.getClaimType().getValue().equalsIgnoreCase("Reimbursement")) {
			optionGroup.setValue(false);
		}

    	optionGroup.setReadOnly(Boolean.TRUE);
    	optionGroup1 = new OptionGroup();
//		hospitalOption = binder.buildAndBind("","hospTypeBooleanval",OptionGroup.class);
    	optionGroup1.addItem("Non Medical");
		if(claim.getNonHospitalisationFlag()!= null && claim.getNonHospitalisationFlag().equalsIgnoreCase("Y")){
			optionGroup1.setValue("Non Medical");
		}   	
		optionGroup1.setReadOnly(Boolean.TRUE);
    	this.binder = new BeanFieldGroup<OMPViewClaimStatusDto>(OMPViewClaimStatusDto.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
    	
    	txtIntimationNo = (TextField) binder.buildAndBind("Intimation No","intimationId",TextField.class);
    	txtIntimationDate = (TextField)binder.buildAndBind("Intimation Date *","intimationDate",TextField.class);
    	txtIntimationDate.setValue(new SimpleDateFormat("dd-MM-yyyy").format(claim.getIntimation().getIntimationDate()));
    	txtIntimationDate.setRequired(true);
    	txtLossOfDate = new TextField("Loss Date");
//    	txtLossOfDate = (TextField)binder.buildAndBind("Loss Date *","lossDate",TextField.class);
    	if(claim.getIntimation()!= null && claim.getIntimation().getLossDateTime()!= null ){
    		txtLossOfDate.setValue(new SimpleDateFormat("dd-MM-yyyy").format(claim.getIntimation().getLossDateTime()));
    	}
    	txtLossOfDate.setRequired(true);
    	txtTPAIntimationNo = (TextField)binder.buildAndBind("TPA Intimation No","tpaIntimationNo",TextField.class);
    	txtTPAIntimationNo.setValue(claim.getIntimation().getTpaIntimationNumber());
    	txtInsuredName = (TextField)binder.buildAndBind("Insured Name*","insuredName",TextField.class);    
    	txtInsuredName.setRequired(true);
    	txtAilment = (TextField)binder.buildAndBind("Ailment/Loss","ailmentOrLoss",TextField.class);
    	if(claim!= null&&claim.getIntimation()!= null && claim.getIntimation().getAilmentLoss()!= null){
    		txtAilment.setValue(claim.getIntimation().getAilmentLoss());
    	}else{
    		if(claim!= null&&claim.getIntimation()!= null &&claim.getIntimation().getLossDetails()!= null)
    			txtAilment.setValue(claim.getIntimation().getLossDetails());
    	}
    	txtEventCode = (TextField)binder.buildAndBind("Event Code","eventCode",TextField.class);
    	txtEventCode.setValue(claim.getIntimation().getEvent().getEventCode());
    	txtEventCode.setRequired(true);
    	txtPlaceofVisit = (TextField)binder.buildAndBind("Place of Visit","placeofVisit",TextField.class);
    	txtPlaceofVisit.setValue(claim.getIntimation().getPlaceVisit());
    	txtModeofIntimation = (TextField)binder.buildAndBind("Mode of Intimation","modeOfIntimation",TextField.class);
    	txtIntimatedBy = (TextField)binder.buildAndBind("Intimated By","intimatedBy",TextField.class);
    	txtIntimatorName = (TextField)binder.buildAndBind("Caller/Intimator Name","intimatorName",TextField.class);
    	txtIntimatorName.setValue(claim.getIntimation().getIntimaterName());
    	txtCallerContactNo = (TextField)binder.buildAndBind("Caller Contact No ","callerContactNo",TextField.class);
    	txtInitialProvisionAmt = (TextField)binder.buildAndBind("Initial Provision Amt($)","intialProvisionAmt",TextField.class);
    	txtInitialProvisionAmt.setValue(String.valueOf(claim.getIntimation().getDollarInitProvisionAmt()));
    	txtInitialProvisionAmt.setRequired(true);
    	txtINRConversionRate = (TextField)binder.buildAndBind("INR Conversion Rate","inrConversionRate",TextField.class);
    	txtINRConversionRate.setValue(String.valueOf(claim.getIntimation().getInrConversionRate()));
    	txtINRConversionRate.setRequired(true);
    	txtINRValue = (TextField)binder.buildAndBind("INR Value","inrValue",TextField.class);
    	txtINRValue.setValue(String.valueOf(claim.getIntimation().getInrTotalAmount()));
    	
    	FormLayout firstForm = new FormLayout(optionGroup,txtIntimationNo,txtIntimationDate,txtLossOfDate,txtTPAIntimationNo,txtInsuredName,txtAilment,txtEventCode,txtPlaceofVisit,txtModeofIntimation,txtIntimatedBy,txtIntimatorName,txtCallerContactNo,txtInitialProvisionAmt,txtINRConversionRate,txtINRValue);
    	
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
    	txtIntimationRemarks = (TextField)binder.buildAndBind("Remarks","remarks",TextField.class);
//    	txtIntimationRemarks.setValue(claim.getSuggestedRejectionRemarks());
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
	    
	    Panel registrationPanel = registrationDetailsPanel(claim);
	    
	    mainVerticalLayout = new VerticalLayout(mainPanel,registrationPanel);
	    
	    if(this.rodKey != null && this.rodKey != 0l){
	    	
	    ompRodAndBillEntryDetailObj = ompRodAndBillEntryDetailInstance.get();
    	ompRodAndBillEntryDetailObj.init("", false, false);
    	ompRodAndBillEntryDetailObj.setCaption("Receipt of Document & Bill Entry");
    	List<OMPRodAndBillEntryDetailTableDTO>  rodList = new ArrayList<OMPRodAndBillEntryDetailTableDTO>();
    	if(rodkey != null){
	    	List<OMPReimbursement> reimbursement = rodBillentryService.getOMPReimbursementList(rodkey);
	    	if(reimbursement!= null){
	    		if(bean!= null){
	    			bean.setAilmentOrLoss(reimbursement.get(0).getAilmentLoss());
	    		}
    	rodList = OMPEarlierRodMapper.getInstance().getOMPRodAndBillEntryDetailTableDTOList(reimbursement);
    	if(rodList!= null){
    		for(OMPRodAndBillEntryDetailTableDTO rodDto : rodList){
    			if(rodDto!= null){
    				rodDto.setEventCode(reimbursement.get(0).getEventCodeId().getEventCode() + " - " +reimbursement.get(0).getEventCodeId().getEventDescription());
    				if(reimbursement.get(0).getDocAcknowLedgement().getReconsiderationRequest() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursement.get(0).getDocAcknowLedgement().getReconsiderationRequest())){
    					rodDto.setRodType(SHAConstants.RECONSIDERATION);
					}
					else{
						rodDto.setRodType(SHAConstants.ORIGINAL);
					}
    			}
    		}
    		ompRodAndBillEntryDetailObj.setTableList(rodList);
    		ompRodAndBillEntryDetailObj.setCaption("Receipt of Document & Bill Entry");
    	}
  	}
}
    	
    	Component tableComponent = ompRodAndBillEntryDetailObj; 
	    Panel receiptOfDocumentAndBillEntryPanel = receiptOfDocumentAndBillEntry(tableComponent,claim); 
	    
	    ompProcessingDetailsObj = ompProcessingDetailsInstance.get();
	    ompProcessingDetailsObj.init("", false, false);
	    ompProcessingDetailsObj.setCaption("OMP Processing Details");
	    
	    ompPaymentDetailsObj = ompPaymentDetailsInstance.get();
	    ompPaymentDetailsObj.init("", false, false);
	    List<OMPPaymentDetailsTableDTO> paymentList = new ArrayList<OMPPaymentDetailsTableDTO>();
	    if(rodkey != null){
	    	OMPReimbursement reimbursement = rodBillentryService.getReimbursement(rodkey);
	    	if(reimbursement!= null){
	    		if(bean!= null){
	    			bean.setAilment(reimbursement.getAilmentLoss());
	    		}
	    		if(bean!= null && bean.getIntimationId()!= null){
	    			List<OMPClaimPayment> paymemt = rodBillentryService.getOMPClaimPaymentByIntimationNo(bean.getIntimationId());
	    			if(paymemt!= null){
		    			paymentList =OMPEarlierRodMapper.getInstance().getOMPClaimPaymentDetails(paymemt);
		    			if(paymentList!= null){
		    				ompPaymentDetailsObj.setTableList(paymentList);
		    			}
		    		}
	    		}
//	    		List<OMPClaimPayment> paymemt = rodBillentryService.getOMPClaimPayment(reimbursement.getRodNumber());
//	    		if(paymemt!= null){
//	    			paymentList =OMPEarlierRodMapper.getInstance().getOMPClaimPaymentDetails(paymemt);
//	    			if(paymentList!= null){
//	    				ompPaymentDetailsObj.setTableList(paymentList);
//	    			}
//	    		}
	    	}
	    }
	    ompPaymentDetailsObj.setCaption("Payment Details");
	    
	    	    	        	    
	    mainVerticalLayout.addComponents(receiptOfDocumentAndBillEntryPanel,ompProcessingDetailsObj,ompPaymentDetailsObj);
	    }
	    mainVerticalLayout.setSpacing(true);
	    mainVerticalLayout.setComponentAlignment(mainPanel, Alignment.TOP_CENTER);
	    mainVerticalLayout.setMargin(true);
	    
	    setCompositionRoot(mainVerticalLayout);
    	
    }
    
    private Panel receiptOfDocumentAndBillEntry(Component table,OMPClaim claim){
    	
//    	this.binder = new BeanFieldGroup<OMPViewClaimStatusDto>(OMPViewClaimStatusDto.class);
//		this.binder.setItemDataSource(this.bean);
//		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());    //  This binder was intialised  already in  init()  Method Please Check.
    	
    	txtAilmentLoss = (TextField)binder.buildAndBind("Ailment/Loss","ailment",TextField.class);
    	if(claim!= null&&claim.getAilmentLoss()!= null){
    		txtAilmentLoss.setValue(claim.getAilmentLoss());
    	}else{
    		txtAilmentLoss.setValue(claim.getLossDetails());
    	}
    	txtAilmentLoss.setReadOnly(Boolean.TRUE);
    	FormLayout mainForm = new FormLayout(txtAilmentLoss);
    	
    	HorizontalLayout mainHor = new HorizontalLayout(mainForm);

    	ompRodAndBillEntryDetailObj = (OMPRodAndBillEntryDetailTable)table;
    	
    	VerticalLayout vLayout = new VerticalLayout(mainHor,ompRodAndBillEntryDetailObj);
    	Panel mainPanel = new Panel(vLayout);
    	mainPanel.addStyleName("girdBorder");
    	mainPanel.setCaption("Receipt of Document & Bill Entry");
    	
    	return mainPanel;
    }
    
    private Panel registrationDetailsPanel(OMPClaim claim){
    	
    	this.registrationBinder = new BeanFieldGroup<OMPClaimStatusRegistrationDto>(OMPClaimStatusRegistrationDto.class);
		this.registrationBinder.setItemDataSource(this.bean.getOmpclaimStatusRegistrionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
    	
    	txtClaimNo = (TextField)registrationBinder.buildAndBind("Claim No","claimNo",TextField.class);
    	txtClaimNo.setValue(claim.getClaimId());
    	txtRegistrationStarus = (TextField)registrationBinder.buildAndBind("Registration Status","registrationStatus",TextField.class);
    	if(claim.getStatus()!= null &&claim.getStatus().getProcessValue().equalsIgnoreCase("Rejection suggested")){
    		txtRegistrationStarus.setValue("Rejected");
    	}else{
    		if(claim.getStatus()!= null && !claim.getStatus().getProcessValue().equalsIgnoreCase("Rejection suggested")){
    			txtRegistrationStarus.setValue(claim.getStatus().getProcessValue());
    		}
    	}
    	txtProvisionAmount = (TextField)registrationBinder.buildAndBind("Provision Amount","provisionAmount",TextField.class);
    	txtProvisionAmount.setValue(String.valueOf(claim.getDollarInitProvisionAmount()));
    	txtClaimType = (TextField)registrationBinder.buildAndBind("Claim Type","claimType",TextField.class);
    	if(claim.getClaimType()!= null && claim.getClaimType().getValue()!= null){
    	txtClaimType.setValue(claim.getClaimType().getValue());
    	}else{
    		txtClaimType.setValue("Non Medical");
    	}
    	txtRegistrationRemarks = (TextField)registrationBinder.buildAndBind("Remarks","remarks",TextField.class);
    	if(claim.getRegistrationRemarks()!= null){
    		txtRegistrationRemarks.setValue(claim.getRegistrationRemarks());
    	}else{
    		if(claim.getSuggestedRejectionRemarks()!= null){
    			txtRegistrationRemarks.setValue(claim.getSuggestedRejectionRemarks());
    		}
    	}
    	FormLayout mainForm = new FormLayout(txtClaimNo,txtRegistrationStarus,txtProvisionAmount,txtClaimType,txtRegistrationRemarks);
    	setReadOnly(mainForm, true);
    	
    	HorizontalLayout mainHor = new HorizontalLayout(mainForm);
    	
    	Panel mainPanel = new Panel(mainHor);
    	mainPanel.addStyleName("girdBorder");
    	mainPanel.setCaption("Registration Details");
    	return mainPanel;
    }
    
    public void setProcessingDetailsTable(){
    	
    	
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

    
}
