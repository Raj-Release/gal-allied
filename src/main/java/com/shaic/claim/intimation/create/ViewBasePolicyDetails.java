package com.shaic.claim.intimation.create;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.coinsurance.view.CoInsuranceDetailView;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.claim.intimation.viewdetails.search.SearchClaimDocumentsTable;
import com.shaic.claim.policy.search.ui.PremPolicySchedule;
import com.shaic.claim.preauth.view.ViewPreviousClaimsTable;
import com.shaic.claim.registration.BasePolicyPreviousClaimWindowUI;
import com.shaic.claim.registration.ViewBasePolicyClaimsWindowOpen;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyEndorsementDetails;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.vaadin.cdi.UIScoped;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ReadOnlyException;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@UIScoped
public class ViewBasePolicyDetails extends Window{
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
	private FormLayout viewBasePolicyDetailsForm ;
	private HorizontalLayout coInsuranHLayout;
	private NativeSelect viewBasePolicyDetailsSelect;
	private Button btnGo;
	private BrowserWindowOpener opener;
	
	private Button btnClose;

	private Policy policy;
	
	private PolicyService policyService;
	private MasterService masterService;
	
	private IntimationService intimationService;
	
	@Inject
	private ViewEndorsementDetailsTable viewEndorsementDetailsTable;
	
	@Inject
	private SearchClaimDocumentsTable searchResultTable;
	private SearchIntimationFormDto searchDto;
	
	private Insured insuredGMC;
	
	@Inject
	private ViewPreviousClaimsTable preauthPreviousClaimsTable;
	@Inject
	private Instance<ViewBasePolicyClaimsWindowOpen> ViewBasePolicyClaimsWindowOpen;
	
	@Inject
	private CoInsuranceDetailView coInsuranceDetailsView;
	
	
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	private static final String VIEW_BASE_POLICY_CLAIMS = "Base Policy Claims";
	
	private EnhancedBrowserWindowOpener sopener;
	
	Button claimsofPolicyBtn;
	
	public EnhancedBrowserWindowOpener getSopener() {
		return sopener;
	}

	public void setSopener(EnhancedBrowserWindowOpener sopener) {
		this.sopener = sopener;
	}
	
	public void setPolicyServiceAndPolicy(PolicyService a_policyService, Policy policy,
			MasterService masterService, IntimationService intimationService) {
		this.policyService = a_policyService;
		this.policy = policy;
		this.masterService = masterService;
		this.intimationService = intimationService;

	}
	public void initView() {
		opener = new BrowserWindowOpener(BasePolicyPreviousClaimWindowUI.class);
		claimsofPolicyBtn = new Button("Claims of this Policy");
		addVaadinSessionAttribute(policy.getKey());
		viewEndorsementDetailsTable.init("Endorsement Details", false, false);
		List<PolicyEndorsementDetails> endorsementList = policyService.getPolicyEndorsementList(policy.getPolicyNumber());
		
		if(endorsementList != null && endorsementList.isEmpty()){
			viewEndorsementDetailsTable.setVisible(false);
		}else{
			viewEndorsementDetailsTable.setVisible(true);
		}
		viewEndorsementDetailsTable.setTableList(endorsementList);
		viewEndorsementDetailsTable.setTablesize();
		buildMainLayout();
		setCaption("Base Policy Details");
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

		grossPremium.setWidth(100, Unit.PIXELS);
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
		if (insuredOfficeNameByDivisionCode != null && !insuredOfficeNameByDivisionCode.isEmpty()) {
			OrganaizationUnit organaizationUnit = insuredOfficeNameByDivisionCode.get(0);
			polprevAddress.setValue(organaizationUnit.getAddress());
		}

		agentName.setValue(apolicy.getAgentName() != null ? apolicy
				.getAgentName() : "");

		polAddress.setValue(getPolicyAddress(apolicy));
		polAddress.setEnabled(false);

		periodofInsurancetextField.setValue("FROM "
				+ SHAUtils.formatDate(apolicy.getPolicyFromDate()) + " TO "
				+ SHAUtils.formatDate(apolicy.getPolicyToDate()));

		buildDateOfBirth();
		buildPreExistingDisease();
		
		BeanItemContainer<Insured> insuredContainer = new BeanItemContainer<Insured>(
				Insured.class);
		if(apolicy != null && apolicy.getProduct() != null && ReferenceTable.getGMCProductCodeList().containsKey(apolicy.getProduct().getCode())){
			if(insuredGMC != null){
				List<Insured> insuredList = new ArrayList<Insured>();
				if(insuredGMC.getDependentRiskId() != null){
					insuredList = policyService.getInsuredListForGMC(insuredGMC.getDependentRiskId());	
				}else{
					insuredList = policyService.getInsuredListForGMC(insuredGMC.getInsuredId());	
				}
				insuredContainer.addAll(insuredList);
			}else{
				insuredContainer.addAll(apolicy.getInsured());
			}
		}else{
			insuredContainer.addAll(apolicy.getInsured());	
		}
		
		insuredTable.setContainerDataSource(insuredContainer);
		
		Object[] columnsForGMC = new Object[] { "insuredName", "insuredGender",
				"DATE OF BIRTH", "insuredAge", "relationshipwithInsuredId",
				"insuredSumInsured", "PRE-EXISTING DISEASE", "effectiveFromDate", "effectiveToDate" };
		
		Object[] columns = new Object[] { "insuredName", "insuredGender",
				"DATE OF BIRTH", "insuredAge", "relationshipwithInsuredId",
				"insuredSumInsured", "PRE-EXISTING DISEASE" };
		
		if(apolicy != null && apolicy.getProduct() != null && ReferenceTable.getGMCProductCodeList().containsKey(apolicy.getProduct().getCode())){
			insuredTable.setVisibleColumns(columnsForGMC);
		}else{
			insuredTable.setVisibleColumns(columns);	
		}
		
		insuredTable.setColumnHeader("insuredName", "INSURED");
		insuredTable.setColumnHeader("insuredGender", "SEX");
		insuredTable.setColumnHeader("relationshipwithInsuredId", "RELATION");
		insuredTable.setColumnHeader("insuredAge", "AGE IN YRS");
		insuredTable.setColumnHeader("insuredSumInsured", "SUM INSURED");
		insuredTable.setColumnHeader("PRE-EXISTING DISEASE", "Risk PED/Portal PED");
		insuredTable.setColumnHeader("effectiveFromDate", "Effective Start Date");
		insuredTable.setColumnHeader("effectiveToDate", "Effective End Date");
		
		
		
		
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
		policyVerticalLayout.setMargin(true);
		policyVerticalLayout.setSpacing(true);

		Panel buildPolicyPanel = buildPolicyGridLayout();
		policyVerticalLayout.setSpacing(true);
		
		//CR2019199 newly added common HLayout 
		coInsuranHLayout = new HorizontalLayout();
		DBCalculationService dbService = new DBCalculationService();
		String coInsuranceIcon = dbService.getCoInsuranceCheckValue(policy.getPolicyNumber());
		if(coInsuranceIcon.equalsIgnoreCase("Y")){

			HorizontalLayout coInsurance = SHAUtils.coInsuranceFlag(policy.getPolicyNumber(),coInsuranceDetailsView);
			coInsuranHLayout.addComponents(coInsurance);
			coInsuranHLayout.setSpacing(true);
			policyVerticalLayout.addComponent(coInsuranHLayout);
			policyVerticalLayout.setComponentAlignment(coInsuranHLayout, Alignment.BOTTOM_RIGHT);
			
			
		}
		
		if(policy.getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)) {
			Button viewMasterPolicy = new Button("View Master Policy");
			viewMasterPolicy.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					
				}
			});
		}

		Button viewPolicyScheduleBtn = new Button("View Policy Schedule");
		viewPolicyScheduleBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1034187828701338725L;

			public void buttonClick(ClickEvent event) {
				
				getViewDocumentByPolicyNo(policy.getPolicyNumber());

			}
		});
		Button endorsedScheduleBtn = new Button("Endorsed Schedule");
		endorsedScheduleBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1034187828701338725L;
			public void buttonClick(ClickEvent event) {
				getViewPolicySchedule(policy.getPolicyNumber(), -1);
			}
		});
		
		Button claimsDocumentBtn = new Button("Claim Documents");
		claimsDocumentBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1034187828701338725L;
			public void buttonClick(ClickEvent event) {
			doSearch(policy);				
			}
		});
		BPMClientContext bpmnClientContextObj = new BPMClientContext();
		coInsuranHLayout.addComponents(viewPolicyScheduleBtn,claimsDocumentBtn,claimsofPolicyBtn);
		coInsuranHLayout.setSpacing(true);
		policyVerticalLayout.addComponent(coInsuranHLayout);
		policyVerticalLayout.setComponentAlignment(coInsuranHLayout, Alignment.BOTTOM_RIGHT);
		
		policyVerticalLayout.addComponent(buildPolicyPanel);
		insuredTable = new Table();
		insuredTable.setCaption("Insured Details");
		insuredTable.setWidth("100.0%");
		insuredTable.setPageLength(5);
		policyVerticalLayout.addComponent(insuredTable);
		
		
		policyVerticalLayout.addComponent(viewEndorsementDetailsTable);
		
		nomineeDetailsTable = nomineeDetailsTableInstance.get();
		
		nomineeDetailsTable.init("", false, false);
		nomineeDetailsTable.setViewColumnDetails();
		
		List<NomineeDetailsDto> nomineeDetailsListDto = new ArrayList<NomineeDetailsDto>();
			nomineeDetailsListDto = intimationService.getNomineeForPolicyKey(policy.getKey());
		
		if(nomineeDetailsListDto != null) {
			nomineeDetailsTable.setTableList(nomineeDetailsListDto);
		}
		
		policyVerticalLayout.addComponent(nomineeDetailsTable);
		
		btnClose = new Button("Close");
		btnClose.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		policyVerticalLayout.addComponent(btnClose);
		policyVerticalLayout.setComponentAlignment(btnClose, Alignment.BOTTOM_CENTER);
		
		addListener();
		
		return policyVerticalLayout;
	}
	
	public void getViewPolicySchedule(String policyNo, int endIdx) {
		if(policyNo != null) {
			PremPolicySchedule fetchPolicyScheduleFromPremia = policyService.fetchPolicyScheduleFromPremia(policyNo, endIdx);
			if(fetchPolicyScheduleFromPremia != null && fetchPolicyScheduleFromPremia.getResultUrl() != null) {
				String url = fetchPolicyScheduleFromPremia.getResultUrl();
				getUI().getPage().open(url, "_blank", 1550, 650, BorderStyle.NONE);
			} else {
				getErrorMessage("Policy Schedule Not Available");
			}
		} else {
			getErrorMessage("Policy Not Available");
		}
	}
	
	public void doSearch(Policy policy) {
		List<Intimation> newIntimationDtoContainer = policyService.getIntimationListByPolicy(policy.getPolicyNumber());
		List<NewIntimationDto> dotList = new ArrayList<NewIntimationDto>();
		for (Intimation intimation : newIntimationDtoContainer) {
			NewIntimationDto newIntimationDto = new NewIntimationDto();
			newIntimationDto.setIntimationId(intimation.getIntimationId());
			newIntimationDto.setKey(intimation.getKey());
			dotList.add(newIntimationDto);
		}
			searchResultTable.init("", false, false);
			searchResultTable.setSubmitTableHeader();
			 searchResultTable.setTableList(dotList);
			 searchResultTable.tablesize();
			 searchResultTable.setWidth("100%");
			 
			 final Window popup = new com.vaadin.ui.Window();
				
				popup.setCaption("View Claim Documents");
				popup.setWidth("100%");
				popup.setHeight("100%");
				//popup.setSizeFull();
				popup.setContent(searchResultTable);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				
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
	
	@SuppressWarnings("serial")
	public ShortcutListener callPreviousClaimsDetailsViewSListener(){
		ShortcutListener shortcutListener = new ShortcutListener("PreviousClaimDetails", KeyCode.NUM2, new int[]{ModifierKey.CTRL, ModifierKey.ALT}) {
			@Override
			public void handleAction(Object sender, Object target) {
				viewBasePolicyDetailsSelect.setValue(VIEW_BASE_POLICY_CLAIMS);
				if(viewBasePolicyDetailsSelect.getValue() != null){
					viewBasePolicyDetailsSelect.select(viewBasePolicyDetailsSelect.getValue());
					//btnGo.click();
				}
			}
		};
		getActionManager().addAction(shortcutListener);
		return shortcutListener;
	}
public void addVaadinSessionAttribute(Long policyKey){
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,policyKey);					
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,ViewBasePolicyClaimsWindowOpen);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,preauthPreviousClaimsTable);
	
				EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener(BasePolicyPreviousClaimWindowUI.class);
				opener.popupBlockerWorkaround(true);
				opener.withShortcut(callPreviousClaimsDetailsViewSListener());
				opener.setFeatures("height=700,width=1300,resizable");
				opener.doExtend(claimsofPolicyBtn);
				setSopener(opener);
			
				claimsofPolicyBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					getSopener().open();	
				}
			});
		
	}
	public void getErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
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
				if(event.getButton().getData() != null && event.getButton().getData() instanceof ViewBasePolicyDetails) {
					ViewBasePolicyDetails viewBasePolicyDetails = (ViewBasePolicyDetails) event.getButton().getData();
					viewBasePolicyDetails.close();
				}
				
			}
		});
	}

	private Panel buildPolicyGridLayout() {

		policyNumber = new TextField("Policy No");
		policyNumber.setNullRepresentation("text");
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
		
		String strDmsViewURL = null;
		Policy policyObj = null;
		BrowserFrame browserFrame = null;
		
		if (strPolicyNo != null) {
			policyObj = policyService.getByPolicyNumber(strPolicyNo);
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					strDmsViewURL = BPMClientContext.BANCS_POLICY_DOCUMENT_URL;
					strDmsViewURL = strDmsViewURL.replace("POLICY", strPolicyNo);
					if(ReferenceTable.getGMCProductList().containsKey(policyObj.getProduct().getKey())){
						strDmsViewURL = strDmsViewURL.replace("MEMBER", insuredGMC!=null?String.valueOf(insuredGMC.getSourceRiskId()!=null?insuredGMC.getSourceRiskId():""):"");		
					}else{
						strDmsViewURL = strDmsViewURL.replace("MEMBER", "");
					}
					getUI().getPage().open(strDmsViewURL, "_blank",1200,650,BorderStyle.NONE);
//					browserFrame = new BrowserFrame("View Documents",new ExternalResource(strDmsViewURL));
				}else{
					strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
					String dmsToken = intimationService.createDMSToken(strPolicyNo);
					getUI().getPage().open(strDmsViewURL+dmsToken, "_blank",1200,650,BorderStyle.NONE);
//					browserFrame = new BrowserFrame("View Documents",new ExternalResource(strDmsViewURL+dmsToken));
				}
			}
		}
		/*browserFrame.setSizeFull();
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
	
}
