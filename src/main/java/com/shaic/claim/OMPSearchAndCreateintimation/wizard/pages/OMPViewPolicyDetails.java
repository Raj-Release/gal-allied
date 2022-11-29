package com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.intimation.create.PolicySchedulepopupUI;
import com.shaic.claim.intimation.create.ViewEndorsementDetailsTable;
import com.shaic.claim.intimation.create.ViewPolicyDetails;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyEndorsementDetails;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.cdi.UIScoped;
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

@UIScoped
public class OMPViewPolicyDetails extends Window {

	private static final long serialVersionUID = 5487431603301000217L;

	private VerticalLayout mainLayout;
	private HorizontalLayout totalHLayout;
	private VerticalLayout policyVerticalLayout;
	private Table insuredTable;
	private HorizontalLayout policyGridLayout;
	private TextField totalPremium;
	private Label totalLabel;
	private HorizontalLayout stampdutyHlayout;
	private TextField stampDuty;
	private Label stamDutyLabel;
	private HorizontalLayout grassPremiumHLayout;
	private TextField grossPremium;
	private Label grossPremiumlabel;
	private TextField polProduct;
	private Label productNamelabel;
	private TextField periodofInsurancetextField;
	private Label periodofInsurancelabel;
	private TextField agentName;
	private TextField prePolicyTelephoneText;
	private TextField polTelephoneNumber;
	private Label brokerlabel;
	private TextField smName;
	private Label MktgExecutivelabel;
	private TextField receiptDate;
	private Label depPremRecptDatelabel;
	private TextField receiptNumber;
	private Label depPremRecptNolabel;
	private HorizontalLayout policyOfficeContactHLayout;
	private FormLayout serviceTaxFrmLayout;
	private TextField polOffEmail;
	private TextField polOffTax;
	private TextField polOffPhone;
	private Label policyTelephonelabel;
	private HorizontalLayout policyContactHLayout;
	private TextField polEmailId;
	private TextField polFaxNo;
	private TextField polTelNo;
	private Label insuredTelephonelabel;
	private TextArea polprevAddress;
	private Label policyAddresslabel;
	private TextArea polAddress;
	private Label insuredAddresslabel;
	private TextField proposerFirstName;
	private Label insuredNamelabel;
	private Label issueOfficeCodelabel;
	private TextField issueOfficeCodeName;
	private TextField proposerCode;
	private Label insuredCodelabel;
	private TextField endorsementNumber;
	private Label endorsementNumberlabel;
	private TextField renewalPolicyNumber;
	private Label previousPolicyNolabel;
	private TextField policyNumber;
	private TextField premiumTax;
	private Label policyNolabel;
	
	private Button btnClose;

	private Policy policy;
	

	private PolicyService policyService;
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationService;
	
	@Inject
	private ViewEndorsementDetailsTable viewEndorsementDetailsTable;
	
	@Inject
	private Instance<PolicySchedulepopupUI> policySchedulepopupUI;
	

	public void setPolicyServiceAndPolicy(PolicyService a_policyService, Policy policy,	MasterService masterService) {
		//super("View Policy Details");
		this.policyService = a_policyService;
		this.policy = policy;
		this.masterService = masterService;
		


	}
	public void initView() {
		viewEndorsementDetailsTable.init("Endorsement Details", false, false);
		List<PolicyEndorsementDetails> endorsementList = policyService.getPolicyEndorsementList(policy.getPolicyNumber());
		
		if(endorsementList != null && endorsementList.isEmpty()){
			viewEndorsementDetailsTable.setVisible(false);
		}else{
			viewEndorsementDetailsTable.setVisible(true);
		}
		viewEndorsementDetailsTable.setTableList(endorsementList);
		//viewEndorsementDetailsTable.setTablesize();
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
		
		if(apolicy != null && apolicy.getEndorsementNumber() != null && apolicy.getEndorsementNumber().equals("0")){
			endorsementNumber.setValue(null);
		}

		grossPremium.setWidth(70, Unit.PIXELS);
		premiumTax.setWidth(100, Unit.PIXELS);
		stampDuty.setWidth(60, Unit.PIXELS);

		Product productByProductCode = masterService
				.getProductByProductCode(apolicy.getProduct().getCode());
		if (productByProductCode != null) {
			polProduct.setValue(productByProductCode.getValue());
		}

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

		agentName.setValue(apolicy.getAgentName() != null ? apolicy
				.getAgentName() : "");

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
		setReadOnly(policyContactHLayout);
		setReadOnly(stampdutyHlayout);
		setReadOnly(totalHLayout);
		setReadOnly(policyOfficeContactHLayout);
		setReadOnly(serviceTaxFrmLayout);
	}

//	private String getAddress(Address policyAddress) {
//		return (policyAddress.getDoorApartmentNumber() == null ? ""
//				: policyAddress.getDoorApartmentNumber() + ",  ")
//				+ (policyAddress.getPlotGateNumber() == null ? ""
//						: policyAddress.getPlotGateNumber() + ", ")
//				+ (policyAddress.getStreetName() == null ? "" : policyAddress
//						.getStreetName())
////				+ (policyAddress.getCityTownVillage() == null ? ""
////						: policyAddress.getCityTownVillage().getValue())
////				+ ",  "
////				+ (policyAddress.getDistrict() == null ? "" : policyAddress
////						.getDistrict().getValue())
//				+ (policyAddress.getState() == null ? "" : policyAddress
//						.getState().getValue())
//				+ ",  "
//				+ (policyAddress.getCountry() == null ? "" : policyAddress
//						.getCountry().getValue());
//	}

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

	private void setReadOnly(FormLayout formLayout) {
		@SuppressWarnings("deprecation")
		Iterator<Component> a = formLayout.getComponentIterator();
		while (a.hasNext()) {
			Component c = a.next();
			if (c instanceof AbstractField) {
				@SuppressWarnings("rawtypes")
				AbstractField field = (AbstractField) c;
				field.setReadOnly(true);
			}

		}
	}

	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		policyVerticalLayout = buildPolicyVLayout();
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

		Button viewPolicyScheduleBtn = new Button("View Proposal");
		viewPolicyScheduleBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1034187828701338725L;

			public void buttonClick(ClickEvent event) {
				getViewDocumentByPolicyNo(policy.getPolicyNumber());
			}
		});

		policyVerticalLayout.addComponent(viewPolicyScheduleBtn);
		policyVerticalLayout.setComponentAlignment(viewPolicyScheduleBtn,
				Alignment.BOTTOM_RIGHT);
		policyVerticalLayout.addComponent(buildPolicyPanel);
		insuredTable = new Table();
		insuredTable.setCaption("Insured Details");
		//Vaadin8-setImmediate() insuredTable.setImmediate(false);
		insuredTable.setWidth("100.0%");
//		insuredTable.setHeight("200px");
		insuredTable.setPageLength(5);
		policyVerticalLayout.addComponent(insuredTable);
		
		
		policyVerticalLayout.addComponent(viewEndorsementDetailsTable);
		
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
				if(event.getButton().getData() != null && event.getButton().getData() instanceof ViewPolicyDetails) {
					ViewPolicyDetails viewPolicyDetails = (ViewPolicyDetails) event.getButton().getData();
					viewPolicyDetails.close();
				}
				
			}
		});
	}

	private Panel buildPolicyGridLayout() {

		policyNumber = new TextField("Policy No");
		policyNumber.setNullRepresentation("");
		policyNumber.setWidth("200px");
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		renewalPolicyNumber = new TextField("Previous Policy No.");
		renewalPolicyNumber.setNullRepresentation("");
		renewalPolicyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		endorsementNumber = new TextField("Endorsement No");
		endorsementNumber.setNullRepresentation("");
		endorsementNumber.setWidth(String.valueOf(endorsementNumber.getValue()
				.length()));
		endorsementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		proposerCode = new TextField("Proposer Code");
		proposerCode.setNullRepresentation("");
		proposerCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		issueOfficeCodeName = new TextField("Issue Office Code");
		issueOfficeCodeName.setNullRepresentation("");
		issueOfficeCodeName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		proposerFirstName = new TextField("Proposer Name");
		proposerFirstName.setNullRepresentation("");
		proposerFirstName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		polAddress = new TextArea("Address");
		polAddress.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		polprevAddress = new TextArea("Address");
		polprevAddress.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		polTelephoneNumber = new TextField("Telephone / Email / Fax");
		polTelephoneNumber.setNullRepresentation("");
		polTelephoneNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		policyContactHLayout = buildPolicyContactHLayout();

		policyOfficeContactHLayout = buildPolicyOfficeContactHLayout();

		prePolicyTelephoneText = new TextField();
		prePolicyTelephoneText.setNullRepresentation("TelePhone / Email / Fax");
		prePolicyTelephoneText.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		receiptNumber = new TextField("Dep. Prem. Recpt. No.");
		receiptNumber.setNullRepresentation("");
		receiptNumber.setValidationVisible(false);
		receiptNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		receiptDate = new TextField("Dep. Prem. Recpt. Date");
		receiptDate.setValidationVisible(false);
		receiptDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		smName = new TextField("Jr. Officer / Mktg Executive");
		smName.setNullRepresentation("");
		smName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		agentName = new TextField("Agent / Broker");
		agentName.setNullRepresentation("");
		agentName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		periodofInsurancetextField = new TextField("Period of Insurance");
		periodofInsurancetextField.setNullRepresentation("");
		periodofInsurancetextField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		polProduct = new TextField("Product Name");
		polProduct.setNullRepresentation("");
		polProduct.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		grassPremiumHLayout = buildGrossPremiumHLayout();

		serviceTaxFrmLayout = buildServiceTaxHLayout();
		
		stampdutyHlayout = buildStampDutyHLayout();
		
		totalHLayout = buildTotalHLayout();
		
		Label dummyLabel1 = new Label();
		dummyLabel1.setSizeFull();
		
		Label dummyLabel2 = new Label();
		dummyLabel2.setSizeFull();
		
		
		FormLayout leftForm= new FormLayout(policyNumber,endorsementNumber,proposerCode,proposerFirstName,polAddress,polTelephoneNumber,receiptNumber,smName,periodofInsurancetextField,grossPremium,premiumTax);
		FormLayout rightForm = new FormLayout(renewalPolicyNumber,dummyLabel1,issueOfficeCodeName,dummyLabel2,polprevAddress,prePolicyTelephoneText,receiptDate,agentName,polProduct,stampDuty,totalPremium);
		
		policyGridLayout = new HorizontalLayout(leftForm,rightForm);
		policyGridLayout.setMargin(false);
		policyGridLayout.setSpacing(true);
		policyGridLayout.setSizeFull();
		policyGridLayout.addStyleName("gridBorder");
		policyGridLayout.setCaption("Policy Details");
		Panel communicationPanel=new Panel(policyGridLayout);
		communicationPanel.addStyleName("girdBorder");
		return communicationPanel;
	}

	private HorizontalLayout buildPolicyContactHLayout() {
		policyContactHLayout = new HorizontalLayout();

		polTelNo = new TextField();
		polTelNo.setNullRepresentation("");
		polTelNo.setWidth("80px");
		policyContactHLayout.addComponent(polTelNo);

		polFaxNo = new TextField();
		polFaxNo.setNullRepresentation("");
		polFaxNo.setWidth("80px");
		policyContactHLayout.addComponent(polFaxNo);

		polEmailId = new TextField();
		polEmailId.setNullRepresentation("");
		polEmailId.setWidth("80px");
		policyContactHLayout.addComponent(polEmailId);

		return policyContactHLayout;
	}

	private HorizontalLayout buildPolicyOfficeContactHLayout() {
		policyOfficeContactHLayout = new HorizontalLayout();

		polOffPhone = new TextField();
		polOffPhone.setNullRepresentation("");
		polOffPhone.setWidth("80px");
		policyOfficeContactHLayout.addComponent(polOffPhone);

		polOffTax = new TextField();
		polOffTax.setWidth("80px");
		polOffTax.setNullRepresentation("");
		policyOfficeContactHLayout.addComponent(polOffTax);

		polOffEmail = new TextField();
		polOffEmail.setWidth("80px");
		polOffEmail.setNullRepresentation("");
		policyOfficeContactHLayout.addComponent(polOffEmail);

		return policyOfficeContactHLayout;
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
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents", new ExternalResource(strDmsViewURL+dmsToken));
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
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
						popup.close();
			}
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);*/
	}
	
}
