package com.shaic.claim.OMPViewDetails.view;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.intimation.create.ViewEndorsementDetailsTable;
import com.shaic.claim.omp.ratechange.OMPViewEndorsementDetailTable;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.InsuredService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyEndorsementDetails;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.Property.ReadOnlyException;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class OMPViewCurrentPolicyDetailsUI extends Window {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerticalLayout mainLayout;
	private HorizontalLayout totalHLayout;
	private VerticalLayout policyVerticalLayout;
	private VerticalLayout insuredVerticalLayout;
	private Table insuredTable;
	private HorizontalLayout policyGridLayout;
	private TextField totalPremium;
	private HorizontalLayout stampdutyHlayout;
	private TextField stampDuty;
	private HorizontalLayout grassPremiumHLayout;
	private TextField grossPremium;
	private TextField polProduct;
	private TextField periodofInsurancetextField;
	private TextField agentName;
	private TextField prePolicyTelephoneText;
	private TextField polTelephoneNumber;
	private TextField receiptDate;
	private TextField receiptNumber;
	private HorizontalLayout policyOfficeContactHLayout;
	private FormLayout serviceTaxFrmLayout;
	private HorizontalLayout policyContactHLayout;
	private TextArea polprevAddress;
	private TextArea polAddress;
	private TextField proposerFirstName;
	private Label insuredNamelabel;
	private Label issueOfficeCodelabel;
	private TextField issueOfficeCodeName;
	private TextField proposerCode;
	private Label insuredCodelabel;
	private TextField renewalPolicyNumber;
	private Label previousPolicyNolabel;
	private TextField policyNumber;
	private TextField premiumTax;
	private Label policyNolabel;
	private TextField emailId;
	private TextField fulfillerCode;
	private TextField intermediaryCode;
	private TextField intermediaryMobile;
	private TextField intermediaryEmail;
	private TextField serviceTax;
	private TextField total;
	
	
	private TextField insuredName;
	private TextField plan;
	private TextField sex;
	private TextField noOfDays;
	private TextField dateOfBirth;
	private TextField dateOfdepaturefromIndia;
	private TextField age;
	private TextField dateOfreturntoIndia;
	private TextField passPortno;
	private TextField purposeofVisit;
	private TextField dateofExpiry;
	private TextField assigneeName;
	private TextField visaType;
	private TextField coPay;
	private TextField placeOfVisit;
	private TextField compulsoryExclusion;
	
	private Button btnClose;

	private Policy policy;
	

	private PolicyService policyService;
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private InsuredService insuredService;
	
	@Inject
	private ViewEndorsementDetailsTable viewEndorsementDetailsTable;
	@Inject
	private Instance<OMPViewEndorsementDetailTable> ompviewEndorsementDetailsTableInstance;
	
	@Inject
    
    private OMPViewEndorsementDetailTable ompviewEndorsementDetailsObj;
	
//	private Panel communicationPanel = null;
	private HorizontalLayout insuredGridLayout;
	
	//R1276
	private boolean isTPAUserLogin;
	
	public boolean isTPAUserLogin() {
		return isTPAUserLogin;
	}

	public void setTPAUserLogin(boolean isTPAUserLogin) {
		this.isTPAUserLogin = isTPAUserLogin;
	}

	public void setPolicyServiceAndPolicy(PolicyService a_policyService, Policy policy,
			MasterService masterService) {
		//super("View Policy Details");
		this.policyService = a_policyService;
		this.policy = policy;
		this.masterService = masterService;
		


	}
	
	public void initView() {
		viewEndorsementDetailsTable.init("Endorsement Details", false, false);
		List<PolicyEndorsementDetails> endorsementList = policyService.getPolicyEndorsementList(policy.getPolicyNumber());
//		
		if(endorsementList != null && endorsementList.isEmpty()){
			viewEndorsementDetailsTable.setVisible(false);
		}else{
			viewEndorsementDetailsTable.setVisible(true);
		}
		viewEndorsementDetailsTable.setTableList(endorsementList);
//		viewEndorsementDetailsTable.setTablesize();
		//R1276
		ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
		isTPAUserLogin = user.getFilteredRoles().contains("CLM_OMP_TPA_INTIMATION");
		
		buildMainLayout();
		setCaption("View Policy Details");
		setModal(true);
		setClosable(true);
		setResizable(true);
		setContent(mainLayout);
		this.setWidth("90%");
		this.setHeight(640, Unit.PIXELS);
		
		
		
		Policy apolicy = policyService.getPolicy(policy.getPolicyNumber());
		FieldGroup binder = new FieldGroup();
		BeanItem<Policy> item = new BeanItem<Policy>(apolicy);
		binder.setItemDataSource(item);
		binder.bindMemberFields(this);

		if (apolicy.getReceiptDate() != null) {
			try {
				Date tempDate = SHAUtils.formatTimestamp(apolicy
						.getReceiptDate().toString());
				receiptDate.setValue(SHAUtils.formatDate(tempDate));
			} catch (ReadOnlyException e) {

				e.printStackTrace();
			}
		}
		List<OrganaizationUnit> insuredOfficeNameByDivisionCode = policyService
				.getInsuredOfficeNameByDivisionCode(apolicy.getHomeOfficeCode());
		if (insuredOfficeNameByDivisionCode != null && !insuredOfficeNameByDivisionCode.isEmpty()) {
			issueOfficeCodeName.setValue(insuredOfficeNameByDivisionCode.get(0)
					.getOrganizationUnitName());
		}
		
		grossPremium.setWidth(70, Unit.PIXELS);
		premiumTax.setWidth(100, Unit.PIXELS);
		stampDuty.setWidth(60, Unit.PIXELS);

		polTelephoneNumber
				.setValue(apolicy.getOfficeTelephone() != null ? apolicy
						.getOfficeTelephone() : " " + "/ "
						+ apolicy.getOfficeEmailId() != null ? apolicy
						.getOfficeEmailId() : " " + " / "
						+ apolicy.getOfficeFax() != null ? apolicy
						.getOfficeFax() : " ");
		prePolicyTelephoneText
				.setValue(apolicy.getOfficeTelephone() != null ? apolicy
						.getOfficeTelephone() : " " + "/ "
						+ apolicy.getOfficeEmailId() != null ? apolicy
						.getOfficeEmailId() : " " + " / "
						+ apolicy.getOfficeFax() != null ? apolicy
						.getOfficeFax() : " ");
//		Address policyAddress = null;
		if (insuredOfficeNameByDivisionCode != null && !insuredOfficeNameByDivisionCode.isEmpty()) {
//			policyAddress	 = policyService
//				.getPolicyAddress(BigDecimal.valueOf(insuredOfficeNameByDivisionCode.get(0)
//						.getKey()));
			OrganaizationUnit organaizationUnit = insuredOfficeNameByDivisionCode.get(0);
			polprevAddress.setValue(organaizationUnit.getAddress());
		}
//		if (policyAddress != null) {
//
//			
//
//		}

		polAddress.setValue(getPolicyAddress(apolicy));
		polAddress.setEnabled(false);

		periodofInsurancetextField.setValue("FROM "
				+ SHAUtils.formatDate(apolicy.getPolicyFromDate()) + " TO "
				+ SHAUtils.formatDate(apolicy.getPolicyToDate()));
		BeanItemContainer<Insured> insuredContainer = new BeanItemContainer<Insured>(
				Insured.class);
		insuredContainer.addAll(apolicy.getInsured());
		insuredTable.setContainerDataSource(insuredContainer);

		buildDateOfBirth();
		buildPreExistingDisease();
		Object[] columns = new Object[] { "insuredName", "insuredGender",
				"DATE OF BIRTH", "insuredAge", "relationshipwithInsuredId",
				"insuredSumInsured", "PRE-EXISTING DISEASE" };

		insuredTable.setVisibleColumns(columns);

		insuredTable.setColumnHeader("insuredName", "INSURED");
		insuredTable.setColumnHeader("insuredGender", "SEX");
		insuredTable.setColumnHeader("relationshipwithInsuredId", "RELATION");
		insuredTable.setColumnHeader("insuredAge", "AGE IN YRS");
		insuredTable.setColumnHeader("insuredSumInsured", "SUM INSURED");
		insuredTable.setColumnHeader("PRE-EXISTING DISEASE", "Risk PED/Portal PED");
		
		
		Iterator<Component> gridComponent = policyGridLayout.iterator();

		while (gridComponent.hasNext()) {
			Component eachComponent = gridComponent.next();
			if (eachComponent != null) {
				if (eachComponent instanceof TextField) {
					((TextField)eachComponent).setReadOnly(true);
					eachComponent.setWidth(350, Unit.PIXELS);
					eachComponent.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					((TextField) eachComponent).setNullRepresentation("");
				} else if (eachComponent instanceof TextArea) {
					TextArea field = (TextArea) eachComponent;
					field.setReadOnly(true);
					eachComponent.setWidth(350, Unit.PIXELS);
					eachComponent.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
			}
		}
		periodofInsurancetextField.setWidth(220, Unit.PIXELS);
		
		setReadOnly(grassPremiumHLayout);
//		setReadOnly(policyContactHLayout);
		setReadOnly(stampdutyHlayout);
		setReadOnly(totalHLayout);
//		setReadOnly(policyOfficeContactHLayout);
//		setReadOnly(serviceTaxFrmLayout);

}
	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		policyVerticalLayout = buildPolicyVLayout();
//		insuredVerticalLayout = buildInsuredVLayout();
		mainLayout.addComponent(policyVerticalLayout);

		return mainLayout;
	}
	
	private VerticalLayout buildPolicyVLayout() {
		policyVerticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() policyVerticalLayout.setImmediate(false);
		policyVerticalLayout.setMargin(true);
		policyVerticalLayout.setSpacing(true);

		Panel buildPolicyPanel = buildPolicyGridLayout();
		policyVerticalLayout.setSpacing(true);

		Panel buildInsuredPanel = buildInsuredGridLayout();
		Button viewPolicyScheduleBtn = new Button("View Policy Schedule");
		viewPolicyScheduleBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1034187828701338725L;

			public void buttonClick(ClickEvent event) {
				
//				PolicySchedulepopupUI policySchedulepopupUIWindow = policySchedulepopupUI.get();
//				UI.getCurrent().addWindow(policySchedulepopupUIWindow);
				
				getViewDocumentByPolicyNo(policy.getPolicyNumber());

			}
		});

		policyVerticalLayout.addComponent(viewPolicyScheduleBtn);
		policyVerticalLayout.setComponentAlignment(viewPolicyScheduleBtn, Alignment.BOTTOM_RIGHT);
		policyVerticalLayout.addComponent(buildPolicyPanel);
		if(!isTPAUserLogin()){
			policyVerticalLayout.addComponent(buildInsuredPanel);
		}
		insuredTable = new Table();
		insuredTable.setCaption("Insured Details");
		//Vaadin8-setImmediate() insuredTable.setImmediate(false);
		insuredTable.setWidth("100.0%");
//		insuredTable.setHeight("200px");
		insuredTable.setPageLength(5);
	/*	ompviewEndorsementDetailsObj = ompviewEndorsementDetailsTableInstance.get();
		ompviewEndorsementDetailsObj.initTable();
		ompviewEndorsementDetailsObj.viewPolicyEndose();
		ompviewEndorsementDetailsObj.setCaption("Endorsement Details");
		policyVerticalLayout.addComponent(ompviewEndorsementDetailsObj);*/
		
		if(!isTPAUserLogin()){
			policyVerticalLayout.addComponent(viewEndorsementDetailsTable);
		}
		
		btnClose = new Button("Close");
		btnClose.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		policyVerticalLayout.addComponent(btnClose);
		policyVerticalLayout.setComponentAlignment(btnClose, Alignment.BOTTOM_CENTER);
		
		addListener();
		
		return policyVerticalLayout;
	}
	
	
	private void addListener(){
		btnClose.setHeight("-1px");
		
		btnClose.setData(this); 
		
		btnClose.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			

			@Override
			public void buttonClick(ClickEvent event) {
				if(event.getButton().getData() != null && event.getButton().getData() instanceof OMPViewCurrentPolicyDetailsUI) {
					OMPViewCurrentPolicyDetailsUI viewPolicyDetails = (OMPViewCurrentPolicyDetailsUI) event.getButton().getData();
					viewPolicyDetails.close();
				}
				
			}
		});
	}
	
	private Panel buildPolicyGridLayout() {

		Policy apolicy = policyService.getPolicy(policy.getPolicyNumber());
//		if(apolicy!= null){
		policyNumber = new TextField("Policy No");
		policyNumber.setNullRepresentation("");
		policyNumber.setWidth("200px");
		policyNumber.setValue(apolicy.getPolicyNumber());
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		renewalPolicyNumber = new TextField("Previous Policy No.");
		renewalPolicyNumber.setNullRepresentation("");
		renewalPolicyNumber.setValue(apolicy.getRenewalPolicyNumber());
		renewalPolicyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

			
		proposerCode = new TextField("Proposer Code/Name");
		proposerCode.setNullRepresentation("");
		proposerCode.setValue(apolicy.getProposerCode());
		proposerCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		issueOfficeCodeName = new TextField("Issue Office Code");
		issueOfficeCodeName.setNullRepresentation("");
//		issueOfficeCodeName.setValue(apolicy.get);
		issueOfficeCodeName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		polAddress = new TextArea("Address");
		polAddress.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		polprevAddress = new TextArea("Address");
		polprevAddress.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		polTelephoneNumber = new TextField("Telephone / Mobile");
		polTelephoneNumber.setNullRepresentation("");
		polTelephoneNumber.setValue(apolicy.getPolTelephoneNumber());
		polTelephoneNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		prePolicyTelephoneText = new TextField();
		prePolicyTelephoneText.setNullRepresentation("TelePhone / Mobile /Email");
		prePolicyTelephoneText.setValue(apolicy.getPolTelephoneNumber());
		prePolicyTelephoneText.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		emailId = new TextField();
		emailId.setNullRepresentation("Email Id");
		emailId.setValue(apolicy.getPolEmailId());
		emailId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		fulfillerCode = new TextField();
		fulfillerCode.setNullRepresentation("Email Id");
//		fulfillerCode.setValue(apolicy.ge);
		fulfillerCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		receiptNumber = new TextField("Dep. Prem. Recpt. No.");
		receiptNumber.setNullRepresentation("");
		receiptNumber.setValidationVisible(false);
		receiptNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		receiptDate = new TextField("Dep. Prem. Recpt. Date");
		receiptDate.setValidationVisible(false);
		receiptDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		intermediaryCode = new TextField("Intermediary Code/Name");
		intermediaryCode.setValidationVisible(false);
//		intermediaryCode.setValue(apolicy.getpo);
		intermediaryCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		intermediaryMobile = new TextField("Intermediary Tel/Mobile");
		intermediaryMobile.setValidationVisible(false);
		intermediaryMobile.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		intermediaryEmail = new TextField("Intermediary Email");
		intermediaryEmail.setValidationVisible(false);
		intermediaryEmail.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		periodofInsurancetextField = new TextField("Period of Insurance");
		periodofInsurancetextField.setNullRepresentation("");
		periodofInsurancetextField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		grossPremium  = new TextField("Gross Premium ");
		grossPremium.setNullRepresentation("");
		grossPremium.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		stampDuty = new TextField("Stamp  Duty");
		stampDuty.setNullRepresentation("");
		stampDuty.setValue(String.valueOf(apolicy.getStampDuty()));
		stampDuty.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		serviceTax = new TextField("Service Tax");
		serviceTax.setNullRepresentation("");
//		serviceTax.setValue(apolicy.get);
		serviceTax.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		
		total = new TextField("Total");
		total.setNullRepresentation("");
		total.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

			

//		policyContactHLayout = buildPolicyContactHLayout();

//		policyOfficeContactHLayout = buildPolicyOfficeContactHLayout();

	

			grassPremiumHLayout = buildGrossPremiumHLayout();

		serviceTaxFrmLayout = buildServiceTaxHLayout();
		
		stampdutyHlayout = buildStampDutyHLayout();
		
		totalHLayout = buildTotalHLayout();
		
		Label dummyLabel1 = new Label();
		dummyLabel1.setSizeFull();
		
		Label dummyLabel2 = new Label();
		dummyLabel2.setSizeFull();
		
		Label dummyLabel3 = new Label();
		dummyLabel3.setSizeFull();
		
		Label dummyLabel4 = new Label();
		dummyLabel4.setSizeFull();
		
		FormLayout leftForm= new FormLayout(policyNumber,proposerCode,dummyLabel3,polAddress,polTelephoneNumber,dummyLabel4,emailId,receiptNumber,intermediaryCode,intermediaryEmail,grossPremium,serviceTax);
		FormLayout rightForm = new FormLayout(renewalPolicyNumber,dummyLabel1,issueOfficeCodeName,dummyLabel2,polprevAddress,prePolicyTelephoneText,fulfillerCode,receiptDate,intermediaryMobile,periodofInsurancetextField,stampDuty,total);
		
		policyGridLayout = new HorizontalLayout(leftForm,rightForm);
		policyGridLayout.setMargin(false);
		policyGridLayout.setSpacing(true);
		policyGridLayout.setSizeFull();
		policyGridLayout.addStyleName("gridBorder");
		policyGridLayout.setCaption("Policy Details");
		
		Panel communicationPanel=new Panel(policyGridLayout);
		communicationPanel.setCaption("Policy Details");
		communicationPanel.addStyleName("girdBorder");
		return communicationPanel;
	}
	
	private Panel buildInsuredGridLayout() {
		Policy apolicy = policyService.getPolicy(policy.getPolicyNumber());
//		if(apolicy!= null){
			Insured insured = insuredService.getInsuredByPolicyNo(apolicy.getKey().toString());
		insuredName = new TextField("Name of the Insured");
		insuredName.setNullRepresentation("");
		insuredName.setValue(insured.getInsuredName());
		insuredName.setWidth("200px");
		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		plan = new TextField("Plan");
		plan.setNullRepresentation("");
		plan.setValue(insured.getPlan());
		plan.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		sex = new TextField("Sex");
		sex.setNullRepresentation("");
		sex.setValue(insured != null && insured.getInsuredGender().getValue() !=null ? insured.getInsuredGender().getValue() : "");
		sex.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		noOfDays = new TextField("No of Days");
		noOfDays.setNullRepresentation("");
		noOfDays.setValue(String.valueOf(insured.getNoOfDays()));
		noOfDays.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		dateOfBirth = new TextField("Date of Birth");
		dateOfBirth.setNullRepresentation("");
		String insureddob = SHAUtils.formatDate(insured.getInsuredDateOfBirth());
		dateOfBirth.setValue(insureddob);
		dateOfBirth.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		dateOfdepaturefromIndia = new TextField("Date of departure from India");
		String diparturDate = SHAUtils.formatDate(insured.getDepartureDate());
		dateOfdepaturefromIndia.setValue(diparturDate);
		dateOfdepaturefromIndia.setNullRepresentation("");
		
		dateOfdepaturefromIndia.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		age = new TextField("Age - Yrs");
		age.setNullRepresentation("");
		age.setValue(insured.getInsuredAge() != null ? insured.getInsuredAge().toString() : "");
		age.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		dateOfreturntoIndia = new TextField("Date of return to India");
		dateOfreturntoIndia.setNullRepresentation("");
		String returnDate = SHAUtils.formatDate(insured.getReturnDate());
		dateOfreturntoIndia.setValue(returnDate);
		dateOfreturntoIndia.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		passPortno = new TextField("Passport Number");
		passPortno.setNullRepresentation("");
		passPortno.setValue(insured.getPassportNo());
		passPortno.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		purposeofVisit = new TextField("Purpose of Visit");
		purposeofVisit.setNullRepresentation("");
		purposeofVisit.setValue(insured.getPurposeOfvisit());
		purposeofVisit.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		dateofExpiry = new TextField("Date of Expiry");
		dateofExpiry.setNullRepresentation("");
		String expDate = SHAUtils.formatDate(insured.getPassPortExpiryDate());
		dateofExpiry.setValue(expDate);
		dateofExpiry.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		assigneeName = new TextField("Assignee Name");
		assigneeName.setNullRepresentation("");
		assigneeName.setValue(insured.getAssigneeName());
		assigneeName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		visaType = new TextField("Visa Type");
		visaType.setNullRepresentation("");
		visaType.setValue(insured.getVisaType());
		visaType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		coPay = new TextField("Copay");
		coPay.setNullRepresentation("");
		coPay.setValue(String.valueOf(insured.getCopay()));
		coPay.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		placeOfVisit = new TextField("Place of Visit");
		placeOfVisit.setNullRepresentation("");
		placeOfVisit.setValue(insured.getPlaceOfvisit());
		placeOfVisit.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		compulsoryExclusion = new TextField("Compulsory Exclusion");
		compulsoryExclusion.setNullRepresentation("");
		compulsoryExclusion.setValue(insured.getCompulsoryExclusions());
		compulsoryExclusion.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		FormLayout leftForm = new FormLayout(insuredName,sex,dateOfBirth,age,passPortno,dateofExpiry,visaType,placeOfVisit);
		FormLayout rightForm = new FormLayout(plan,noOfDays,dateOfdepaturefromIndia,dateOfreturntoIndia,purposeofVisit,assigneeName,coPay,compulsoryExclusion);
		
		
		insuredGridLayout = new HorizontalLayout(leftForm,rightForm);
		insuredGridLayout.setMargin(false);
		insuredGridLayout.setSpacing(true);
		insuredGridLayout.setSizeFull();
		insuredGridLayout.addStyleName("gridBorder");
		insuredGridLayout.setCaption("Insured Details");
//		}
		Panel communicationPanel= new Panel(insuredGridLayout);
		communicationPanel.addStyleName("girdBorder");
		communicationPanel.setCaption("Insured Details");
		
		return communicationPanel;
		
	}
	private HorizontalLayout buildGrossPremiumHLayout() {
		grassPremiumHLayout = new HorizontalLayout();
		grossPremium = new TextField("Gross Premium: ");
		grossPremium.setNullRepresentation("");
		grossPremium.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		grassPremiumHLayout.addComponent(grossPremium);

		return grassPremiumHLayout;
	}
	
	private HorizontalLayout buildStampDutyHLayout() {
		stampdutyHlayout = new HorizontalLayout();
		stampDuty = new TextField("Stamp Duty :  ");
		stampDuty.setNullRepresentation("");
		stampDuty.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		stampdutyHlayout.addComponent(stampDuty);

		return stampdutyHlayout;
	}
	
	private HorizontalLayout buildTotalHLayout() {
		totalHLayout = new HorizontalLayout();
		totalPremium = new TextField("Total :  ");
		totalPremium.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		totalPremium.setNullRepresentation("");
		totalHLayout.addComponent(totalPremium);

		return totalHLayout;
	}
	
	private FormLayout buildServiceTaxHLayout() {
		serviceTaxFrmLayout = new FormLayout();
		premiumTax = new TextField("Service Tax :  ");
		premiumTax.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		premiumTax.setNullRepresentation("");
		serviceTaxFrmLayout.addComponent(premiumTax);

		return serviceTaxFrmLayout;
	}
	
	public void getViewDocumentByPolicyNo(String strPolicyNo) {
		VerticalLayout vLayout = new VerticalLayout();
		String strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
		String dmsToken = intimationService.createDMSToken(strPolicyNo);
		getUI().getPage().open(strDmsViewURL+dmsToken, "_blank",1200,650,BorderStyle.NONE);
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+dmsToken));
		
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		browserFrame.setSizeFull();
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setSizeFull();
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("View Uploaded Documents");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);*/
	}
	
	private void buildDateOfBirth() {
		insuredTable.addGeneratedColumn("DATE OF BIRTH",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = -1827025991172604071L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						Insured insured = (Insured) itemId;

						try {
							return SHAUtils.formatDate(insured
									.getInsuredDateOfBirth());

						} catch (ReadOnlyException e) {
							e.printStackTrace();
						}
						return null;
					}
				});
	}
	
	private String getPolicyAddress(Policy apolicy) {
		return (apolicy.getProposerAddress() != null ? apolicy.getProposerAddress() : "")
				+ "   ";
	}
	
	private void buildPreExistingDisease() {
		insuredTable.addGeneratedColumn("PRE-EXISTING DISEASE",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = -1827025991172604071L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						Insured insured = (Insured) itemId;
						if (insured != null) {
							List<InsuredPedDetails> insuredPedDetailsList = policyService
									.getPEDByInsured(insured.getInsuredId());

							if (insuredPedDetailsList != null && !insuredPedDetailsList.isEmpty()) {
								insured.setInsuredPedList(insuredPedDetailsList);
							}

						}
						return insured.getInsuredPedNames();
					}
				});
	}
	
	private void setReadOnly(HorizontalLayout horizontalLayout) {
		@SuppressWarnings("deprecation")
		Iterator<Component> a = horizontalLayout.getComponentIterator();
		while (a.hasNext()) {
			Component c = a.next();
			if (c instanceof AbstractField) {
				@SuppressWarnings("rawtypes")
				AbstractField field = (AbstractField) c;
				field.setReadOnly(true);
			}

		}
	}
	
}
