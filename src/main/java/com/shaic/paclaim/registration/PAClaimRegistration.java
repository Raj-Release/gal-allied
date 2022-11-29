package com.shaic.paclaim.registration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.view.LoaderPresenter;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.claim.registration.SublimtListTable;
import com.shaic.claim.registration.balancesuminsured.view.PABalanceSumInsured;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.PARevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.event.FieldEvents.TextChangeEvent;
import com.vaadin.v7.event.FieldEvents.TextChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
@SuppressWarnings({ "serial", "deprecation" })
public class PAClaimRegistration extends ViewComponent {

	@Inject
	private ViewDetails viewDetails;

	@Inject
	private PreviousPAClaimsTableForClaimRegistration previousClaimsRgistration;
	
	private List<PreviousClaimsTableDTO> previousClaimsRgistrationList;

	@Inject
	private PABalanceSumInsured balanceSumInsuredPA;

	@Inject
	private Instance<SublimtListTable> sublimitListInstance;
	
	@Inject
	private Toolbar toolbar;
	
	@EJB
	private IntimationService intimationService;

	@EJB
	private PolicyService policyService;

	private SublimtListTable sublimtListTable;

	private List<SublimitFunObject> resultSublimitList = new ArrayList<SublimitFunObject>();
	
	HashMap<String, String> enteredValues = new HashMap<String, String>();

	private VerticalLayout balanceSIComponent;

	private Panel mainPanel = new Panel();

	private VerticalLayout mainLayout;

	private FormLayout dynamicFrmLayout;

	private CheckBox vipChk;

	private TextField provAmtTxt;

	private ComboBox currencyNameSelect;

	private TextArea registrationRemarksTxta;

	private TextArea suggestRejectionTxta;

	private TextField claimedAmtTxt;
	
	private TextArea injuryDetailsTxt;
	
	OptionGroup incedentOption;
	
	private PopupDateField dateOfAccidentDeath;
	
	private PopupDateField dateOfAdmission;
	
	private PopupDateField dateOfAccident;
	
	private PopupDateField dateOfDeath;
	
	private PopupDateField dateOfDisablement;
	
	private TextField organisationName;
	
	private TextField sumInsured;
	
	private TextField parentName;
	
	private DateField dateOfBirth;
	
	private TextField riskName;
	
	private TextField age;
	
	private TextField sectionOrclass;
	
	private ComboBox cmbCategory;
	
	private DateField riskDOB;
	
	private TextField riskAge;

	private VerticalLayout dynamicFieldsLayout;

	private TextField provisionalAmtTxt;

	private Button registerButton;

	private Button suggestRejectBtn;

	private Button homePageButton;

	private SearchClaimRegistrationTableDto registerationBean = new SearchClaimRegistrationTableDto();

	private Claim claim = null;

	private ClaimDto claimDto;

	private NewIntimationDto newIntimationDto = new NewIntimationDto();

	private TmpCPUCode tmpCpuCode = null;

	private Double provisionamount = 0d;
	
	private Double balanceSumInsured = 0d;

	private VerticalLayout registrationDetailsLayout;

	private Button submitButton;

	private Button cancelButton;

	private HorizontalLayout submitButtonLayout;

	private BeanItemContainer<SelectValue> currencyMasterContainer;

	@Inject
	private Instance<PARevisedCarousel> paCarouselInstance;

	private PARevisedCarousel paIntimationDetailCarousel;

	@EJB
	private MasterService masterService;

	private HorizontalLayout registerBtnLayout;

	private SearchClaimRegistrationTableDto searchDTO;
	
	private PreauthDTO preauthDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private RRCDTO rrcDTO;
	
	private Window popup;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */

	@PostConstruct
	public void init() {

	}

	public void initView(SearchClaimRegistrationTableDto dto) {

		if (dto != null) {
			this.searchDTO = dto;
			previousClaimsRgistrationList = new ArrayList<PreviousClaimsTableDTO>();
			if(dto.getPopupMap() != null && ! dto.getPopupMap().isEmpty())
			{
				poupMessageForProduct();
			}else if(dto.getSuspiciousPopUp() != null && ! dto.getSuspiciousPopUp().isEmpty()){
				suspiousPopupMessage();
			}
			newIntimationDto = dto.getNewIntimationDto();
			registerationBean = dto;
			preauthDTO = new PreauthDTO();
			preauthDTO.setRrcDTO(dto.getRrcDTO());
		//	preauthDTO.setRodHumanTask(dto.getHumanTask());
//			fireViewEvent(PAClaimRegistrationPresenter.GET_PA_BALANCE_SI,newIntimationDto);
			balanceSumInsured = newIntimationDto.getBalanceSI();
			claimDto = new ClaimDto();
			claimDto.setNewIntimationDto(newIntimationDto);
			claimDto.setIncidenceFlagValue(newIntimationDto.getIncidenceFlag());
			claimDto.setStatusName(null);
			claimDto.setSuggestRejection(null);
			
			if (newIntimationDto != null) {
				fireViewEvent(PAClaimRegistrationPresenter.GET_PREVIOUS_PA_CLAIMS,
						newIntimationDto);
			}
		}

		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				newIntimationDto);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);

		VerticalLayout buildMainLayout = buildMainLayout();
		if(!dto.getIsProceedFurther()) {
			registerButton.setEnabled(false);
			String message = "This policy Endorsement in process. Claim cannot be Registered.";
			if(dto.getIsCancelledPolicy()) {
				message = "Policy is cancelled. Hence claim cannot be approved. Please contact the administrator.";
			} 
			showPolicyStatusMessage(message);
		}
		addListener();
//		registerButton.setEnabled(true);
		setCompositionRoot(buildMainLayout);
//		this.getCompositionRoot().setWidth("80%");

	}
	
	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		homePageButton = new Button();
		registrationDetailsLayout = new VerticalLayout();
		registerBtnLayout = new HorizontalLayout();
		submitButtonLayout = new HorizontalLayout();
		Panel registrationPanel = buildRegistrationPanel();
		mainLayout.addComponent(registrationPanel);
//		mainLayout.addComponent(paIntimationDetailCarousel);
		viewDetails.initView(newIntimationDto.getIntimationId(), ViewLevels.PA_PROCESS, false,"Claim Reg");
		//mainLayout.addComponent(viewDetails);
		mainLayout.addComponent(commonButtonsLayout());
		//mainLayout.setComponentAlignment(viewDetails, Alignment.TOP_CENTER);

			VerticalLayout currencyAccidentDetailsLayout = buildCurrencyFormLayout();
			registrationDetailsLayout.addComponent(currencyAccidentDetailsLayout);

			VerticalLayout tabsLayout = buildTabsLayout();
			tabsLayout.setMargin(true);
			tabsLayout.setWidth("100%");
			registrationDetailsLayout.addComponent(tabsLayout);

			HorizontalLayout suggestRejecionAndRegisterButtonLayout = BuildClaimRegisterAndSuggestRejectionBtnLayout();
			registerBtnLayout
					.addComponent(suggestRejecionAndRegisterButtonLayout);
			registerBtnLayout.setWidth("100%");
			registerBtnLayout.setComponentAlignment(
					suggestRejecionAndRegisterButtonLayout,
					Alignment.BOTTOM_RIGHT);

			registrationDetailsLayout.addComponent(registerBtnLayout);

			dynamicFieldsLayout = new VerticalLayout();
			dynamicFrmLayout = new FormLayout();
			dynamicFieldsLayout.addComponent(dynamicFrmLayout);
			registrationDetailsLayout.addComponent(dynamicFieldsLayout);
		

		HorizontalLayout buttonLayout1 = buildSubmitAndCancelBtnLayout();
		submitButtonLayout.addComponent(buttonLayout1);
		submitButtonLayout.setWidth("80%");
		submitButtonLayout.setSpacing(true);
		submitButtonLayout.setMargin(true);
		submitButtonLayout.setComponentAlignment(buttonLayout1,
				Alignment.MIDDLE_CENTER);
		registrationDetailsLayout.addComponent(submitButtonLayout);

		registrationDetailsLayout.setWidth("100%");
		mainLayout.addComponent(registrationDetailsLayout);
		mainLayout.setWidth("100%");
		mainPanel.setHeight("620px");		
		mainPanel.setContent(mainLayout);
		mainPanel.setWidth("100%");
		
		VerticalLayout wholeLayout = new VerticalLayout();
		wholeLayout.setSizeFull();
		wholeLayout.addComponent(mainLayout);
		return wholeLayout;
		
//		return mainPanel;
	}
	
	  public void poupMessageForProduct() {
		  final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(true);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					if(searchDTO.getSuspiciousPopUp() != null && ! searchDTO.getSuspiciousPopUp().isEmpty()){
						suspiousPopupMessage();
					}
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setSpacing(true);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout();
			Map<String, String> popupMap = searchDTO.getPopupMap();
			for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			   layout.addComponent(new Label(entry.getValue(), ContentMode.HTML));
			   layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
			}
			layout.addComponent(okButton);
			layout.setMargin(true);
			layout.setSpacing(true);
			dialog.setContent(layout);
			dialog.setWidth("30%");
			dialog.show(getUI().getCurrent(), null, true);
		}
	  
	  public void suspiousPopupMessage() {
		  final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(true);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setSpacing(true);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout();
			Map<String, String> popupMap = searchDTO.getSuspiciousPopUp();
			for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			   layout.addComponent(new Label(entry.getValue(), ContentMode.HTML));
			   layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
			}
			layout.addComponent(okButton);
			layout.setMargin(true);
			layout.setSpacing(true);
			dialog.setContent(layout);
			dialog.setWidth("30%");
			dialog.show(getUI().getCurrent(), null, true);
		}
	
	public HorizontalLayout commonButtonsLayout()
	{
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		Label dummyLabel =new Label();
		dummyLabel.setWidth("518px");
		
		Label dummyLabel1 =new Label();
		dummyLabel1.setWidth("518px");
		
		//HorizontalLayout alignmentHLayout = new HorizontalLayout(btnRRC,dummyLabel,viewDetails);
		HorizontalLayout alignmentHLayout = new HorizontalLayout(dummyLabel,dummyLabel1,viewDetails);
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{		
		Label label = new Label("Without Approval you cannot proceed further", ContentMode.HTML);
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
				
		//fireViewEvent(ClaimRegistrationPresenter.VALIDATE_CLAIM_REGISTRATION_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
	}

//	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {

		if (!isValid) {
			Label label = new Label(
					"Same user cannot raise request more than once from same stage",
					ContentMode.HTML);
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
		} else {
			popup = new com.vaadin.ui.Window();
			popup.setCaption("");
			popup.setWidth("85%");
			popup.setHeight("100%");
			rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance
					.get();
			// ViewDocumentDetailsDTO documentDetails = new
			// ViewDocumentDetailsDTO();
			// documentDetails.setClaimDto(bean.getClaimDTO());
			rewardRecognitionRequestViewObj
					.initPresenter(SHAConstants.PROCESS_CLAIM_REGISTRATION);

			rewardRecognitionRequestViewObj.init(preauthDTO, popup);

			// earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
			popup.setCaption("Reward Recognition Request");
			popup.setContent(rewardRecognitionRequestViewObj);
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
	}
		
	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;
		
	}
		
	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	private HorizontalLayout buildSubmitAndCancelBtnLayout() {

		submitButton = new Button();
		String submitCaption = "Submit";
		submitButton.setCaption(submitCaption);
		//Vaadin8-setImmediate() submitButton.setImmediate(true);
		submitButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submitButton.setWidth("-1px");
		submitButton.setHeight("-1px");
		mainLayout.addComponent(submitButton);

		submitButton.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {

					if (claimDto.getStatusName() != null) {
						claimDto.setCreatedBy(registerationBean.getUsername());
						fireViewEvent(
								PAClaimRegistrationPresenter.SUBMIT_PA_CLAIM_CLICK,
								claimDto);
					} else {
						Notification
								.show("ERROR",
										"Please Click Register Or Suggest Reject Button before Submitting the Claim. ",
										Notification.Type.ERROR_MESSAGE);
					}
				}
			});
	
		//Vaadin8-setImmediate() submitButton.setImmediate(true);

		cancelButton = new Button();
		cancelButton.setCaption("Cancel");
		//Vaadin8-setImmediate() cancelButton.setImmediate(true);
		cancelButton.setWidth("-1px");
		cancelButton.setHeight("-1px");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);

		cancelButton.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					
				/*	VaadinSession session = getSession();
					SHAUtils.releaseHumanTask(registerationBean.getUsername(), registerationBean.getPassword(), registerationBean.getTaskNumber(),session);*/
					
					 releaseHumanTask();

					fireViewEvent(
							PAClaimRegistrationPresenter.CANCEL_PA_CLAIM_REGISTRATION,
							null);
				}
			});
		

		HorizontalLayout newBtnLayout = new HorizontalLayout(submitButton,
				cancelButton);
		newBtnLayout.setSpacing(true);
		return newBtnLayout;
	}

	private HorizontalLayout BuildClaimRegisterAndSuggestRejectionBtnLayout() {
		registerButton = new Button();
		registerButton.setCaption("Register");
		//Vaadin8-setImmediate() registerButton.setImmediate(true);
		registerButton.setWidth("-1px");
		registerButton.setHeight("-1px");
		registerButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		registerButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Intimation intimationDtls = intimationService.getIntimationByNo(newIntimationDto.getIntimationId());
				if(validatePolicyStatus(newIntimationDto.getPolicy().getPolicyNumber()) || (intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y"))){
					claimDto.setStatusName("Registered");
					fireViewEvent(PAClaimRegistrationPresenter.CLICK_PA_REGISTER_BUTTON,
							null);
				}else {
					showErrorPageForCancelledPolicy();
					registerButton.setEnabled(false);
				}
			}
		});

		suggestRejectBtn = new Button();
		suggestRejectBtn.setCaption("Suggest Rejection");
		//Vaadin8-setImmediate() suggestRejectBtn.setImmediate(true);
		suggestRejectBtn.setWidth("-1px");
		suggestRejectBtn.setHeight("-1px");
		suggestRejectBtn.setStyleName(ValoTheme.BUTTON_DANGER);
		
//		if(newIntimationDto != null && newIntimationDto.getClaimType() != null && newIntimationDto.getClaimType().getId() != null &&
//				newIntimationDto.getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
//			suggestRejectBtn.setEnabled(false);
//		}
		
		suggestRejectBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				claimDto.setStatusName("SuggestRejection");
				fireViewEvent(PAClaimRegistrationPresenter.SUGGEST_PA_REJECTION,
						null);
			}
		});
		HorizontalLayout submitAndSuggestBtnLayout = new HorizontalLayout(
				registerButton, suggestRejectBtn);
		submitAndSuggestBtnLayout.setSpacing(true);
		return submitAndSuggestBtnLayout;
	}

	private Panel buildRegistrationPanel() {
		// common part: create layout
		Panel registrationPanel = new Panel();
		String caption = "Claim Registration";
		HorizontalLayout panelCaption = new HorizontalLayout();
		panelCaption.setHeight("35%");
		panelCaption.addStyleName(ValoTheme.PANEL_WELL);
		panelCaption.setSpacing(true);
		panelCaption.setMargin(new MarginInfo(false, true, false, true));
		panelCaption.setWidth("100%");
		Label captionLbl = new Label(caption);
		panelCaption.addComponent(captionLbl);
		// panelCaption.setHeight("30px"); //for testing purpose

			vipChk = new CheckBox("Flag as VIP");
			panelCaption.addComponent(vipChk);
			panelCaption.setComponentAlignment(vipChk, Alignment.TOP_RIGHT);
		
		//Vaadin8-setImmediate() registrationPanel.setImmediate(false);
        registrationPanel.setWidth("100%");
		registrationPanel.addStyleName("panelHeader");

		paIntimationDetailCarousel = paCarouselInstance.get();
		paIntimationDetailCarousel.init(newIntimationDto, "");
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.setSpacing(false);
		vlayout.addComponent(panelCaption);
		vlayout.addComponent(paIntimationDetailCarousel);
		vlayout.setStyleName("policygridinfo");
		registrationPanel.setContent(vlayout);

		return registrationPanel;
	}

	private VerticalLayout buildCurrencyFormLayout() {
		
		
		dateOfAdmission = new PopupDateField("Date of Admission");
		dateOfAdmission.setDateFormat("dd/MM/yyyy");
		if(newIntimationDto.getAdmissionDate() != null){
			dateOfAdmission.setValue(newIntimationDto.getAdmissionDate());
		}		
		dateOfAdmission.setData(newIntimationDto.getPolicy());
		dateOfAdmission.setEnabled(false);
		
		dateOfAccident = new PopupDateField("Date of Accident");
		dateOfAccident.setDateFormat("dd/MM/yyyy");		
		dateOfAccident.setData(newIntimationDto.getPolicy());
		if(newIntimationDto.getAdmissionDate() != null){
			dateOfAccident.setValue(newIntimationDto.getAdmissionDate());
		}
		
		dateOfDeath = new PopupDateField("Date of Death");
		dateOfDeath.setDateFormat("dd/MM/yyyy");		
		dateOfDeath.setData(newIntimationDto.getPolicy());
		if(newIntimationDto.getAdmissionDate() != null){
			dateOfDeath.setValue(newIntimationDto.getAdmissionDate());
		}
		
		dateOfDisablement = new PopupDateField("Date of Disablement");
		dateOfDisablement.setDateFormat("dd/MM/yyyy");
		/*if(newIntimationDto.getAccidentDeathDate() != null){
			dateOfDisablement.setValue(newIntimationDto.getAdmissionDate());
		}*/
		dateOfDisablement.setData(newIntimationDto.getPolicy());
		
		// common part: create layout
		FormLayout currencyDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() currencyDetailsForm.setImmediate(false);
		currencyDetailsForm.setMargin(false);
		currencyDetailsForm.setSpacing(true);
		
		
		incedentOption = new OptionGroup("Accident / Death");
		
		Collection<Boolean> paClaimTypeValues = new ArrayList<Boolean>(2);
		paClaimTypeValues.add(true);
		paClaimTypeValues.add(false);

		incedentOption.addItems(paClaimTypeValues);
		incedentOption.setItemCaption(true, "Accident");
		incedentOption.setItemCaption(false, "Death");
		incedentOption.setStyleName("horizontal");
		//Vaadin8-setImmediate() incedentOption.setImmediate(true);
		incedentOption.setValue(claimDto.getIncidenceFlag());
		
		currencyDetailsForm.addComponent(incedentOption);
		// nativeSelect_1
		currencyNameSelect = new ComboBox();
		currencyNameSelect.setCaption("Currency Name");
		//Vaadin8-setImmediate() currencyNameSelect.setImmediate(false);
		currencyNameSelect.setWidth("160px");
		currencyNameSelect.setHeight("-1px");
		currencyNameSelect.setNullSelectionAllowed(false);
		fireViewEvent(PAClaimRegistrationPresenter.GET_PA_CURRENCY_MASTER, null);
		currencyNameSelect.setEnabled(false);

		currencyNameSelect.setContainerDataSource(currencyMasterContainer);
		currencyNameSelect.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		currencyNameSelect.setItemCaptionPropertyId("value");
		// Set Default Value to First Option.
		Collection<?> itemIds = currencyNameSelect.getContainerDataSource()
				.getItemIds();
		currencyNameSelect.setValue(itemIds.toArray()[2]);

		currencyNameSelect
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = -4820170898280727113L;

					public void valueChange(ValueChangeEvent valueChangeEvent) {
						SelectValue masterValue = (SelectValue) valueChangeEvent
								.getProperty().getValue();
						if (claimedAmtTxt != null) {
							claimedAmtTxt.setCaption("Amount Claimed (INR) "
									+ masterValue.getValue());
							//Vaadin8-setImmediate() currencyNameSelect.setImmediate(true);
						}
					}
				});
		//Vaadin8-setImmediate() currencyNameSelect.setImmediate(true);
		currencyDetailsForm.addComponent(currencyNameSelect);

		CSValidator claimedAmtTxtValidator = new CSValidator();
		claimedAmtTxt = new TextField();
		claimedAmtTxt.setCaption("Amount Claimed (INR) ");
		//Vaadin8-setImmediate() claimedAmtTxt.setImmediate(false);
		claimedAmtTxt.setWidth("160px");
		claimedAmtTxt.setHeight("-1px");
		claimedAmtTxt.setRequired(true);
		claimedAmtTxt.setRequiredError("Please enter Claimed Amount.");
		claimedAmtTxt.setValidationVisible(false);
		claimedAmtTxt.setMaxLength(13);
		claimedAmtTxtValidator.extend(claimedAmtTxt);
		claimedAmtTxtValidator.setRegExp("^[0-9.]*$");
		claimedAmtTxtValidator.setPreventInvalidTyping(true);
		claimedAmtTxt.setValue(String.valueOf(this.searchDTO.getClaimedAmount().longValue()));
	
		currencyDetailsForm.addComponent(claimedAmtTxt);
		
	
		
		injuryDetailsTxt = new TextArea();
		injuryDetailsTxt.setCaption("Injury / Loss Details");
		injuryDetailsTxt.setMaxLength(200);
		
		FormLayout accidentFrmLayout = new FormLayout();
		//Vaadin8-setImmediate() accidentFrmLayout.setImmediate(false);
		accidentFrmLayout.setMargin(false);
		accidentFrmLayout.setSpacing(true);
		
		
				
	//	accidentFrmLayout.addComponent(incedentOption);
		
		dateOfAccidentDeath = new PopupDateField("Date of Accident / Death");
		dateOfAccidentDeath.setDateFormat("dd/MM/yyyy");
		if(newIntimationDto.getAccidentDeathDate() != null){
			dateOfAccidentDeath.setValue(newIntimationDto.getAccidentDeathDate());
		}		
		dateOfAccidentDeath.setData(newIntimationDto.getPolicy());
		
		dateOfAccidentDeath.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((PopupDateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((PopupDateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfAccidentDeath.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfAccidentDeath.setValue(null);
						showErrorMessage("Please Enter a valid Accident / Death Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date currentDate = new Date();
				Date policyFrmDate = null;
				Date policyToDate = null;
				if (policy != null) {
					policyFrmDate = policy.getPolicyFromDate();
					policyToDate = policy.getPolicyToDate();
				}
				if (enteredDate != null && policyFrmDate != null
						&& policyToDate != null) {
					if (!enteredDate.after(policyFrmDate)
							|| enteredDate.compareTo(policyToDate) > 0) {
						event.getProperty().setValue(null);
					
						showErrorMessage("Accident / Death Date is not in range between Policy From Date and Policy To Date.");
					}
				}
			}
		});				
		
		
		incedentOption.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = -1774887765294036092L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				//TODO
				Boolean value = (Boolean) event.getProperty().getValue();				
				
				if(value)
				{
					/*if(newIntimationDto.getAdmissionDate() != null){
						dateOfAccident.setValue(newIntimationDto.getAdmissionDate());
					}	
					dateOfDeath.setValue(null);*/
				}	
				
				else
				{
					/*if(newIntimationDto.getAdmissionDate() != null){
						dateOfDeath.setValue(newIntimationDto.getAdmissionDate());
					}
					dateOfAccident.setValue(null);*/
				}
				
				dateOfDisablement.setValue(null);
			}
		});
		//accidentFrmLayout.addComponent(dateOfAccidentDeath);
		accidentFrmLayout.addComponent(dateOfAdmission);
		accidentFrmLayout.addComponent(dateOfAccident);
		accidentFrmLayout.addComponent(dateOfDeath);
		accidentFrmLayout.addComponent(dateOfDisablement);
		
		HorizontalLayout hLayout = new HorizontalLayout();
		
		organisationName = new TextField("Organisation Name");
		organisationName.setEnabled(false);
		if(null != organisationName && null != newIntimationDto.getPolicy().getProposerFirstName()){
			organisationName.setValue(newIntimationDto.getPolicy().getProposerFirstName());			
		}
		
		dateOfBirth = new DateField("Parent(DOB)");
		dateOfBirth.setDateFormat("dd/MM/yyyy");
		if(null != dateOfBirth && newIntimationDto.getParentDOB()!= null){
			dateOfBirth.setValue(newIntimationDto.getParentDOB());
		}	
		
		riskName = new TextField("Risk Name");
		if(null != riskName && null != newIntimationDto.getPaPatientName()){
			riskName.setValue(newIntimationDto.getPaPatientName());
		}
		
		parentName = new TextField("Parent Name");
		if(null != parentName && null != newIntimationDto.getPaParentName()){
			parentName.setValue(newIntimationDto.getPaParentName());
		}
		
		sumInsured = new TextField("Sum Insured");		
		if(null != sumInsured && null != newIntimationDto.getPaSumInsured()){
			sumInsured.setValue(String.valueOf(newIntimationDto.getPaSumInsured()));
			sumInsured.setEnabled(false);;
		}
		
		age = new TextField("Parent(Age)");
		if(null != age && null != newIntimationDto.getParentAge()){
			age.setValue(String.valueOf(newIntimationDto.getParentAge().intValue()));
		}
		
		sectionOrclass = new TextField("Section/Class");
		if(null != sectionOrclass && null != newIntimationDto.getPolicy() && 
				null != newIntimationDto.getPolicy().getSectionCode()){
			sectionOrclass.setValue(String.valueOf( newIntimationDto.getPolicy().getSectionCode()));
		} 
		sectionOrclass.setEnabled(false);
		
		
		cmbCategory = new ComboBox("Category");
		/*if(null != cmbCategory &&  null != newIntimationDto.getInsuredPatient() &&  
				null !=  newIntimationDto.getInsuredPatient().getCategory()){
			cmbCategory.setValue(String.valueOf(newIntimationDto.getInsuredPatient().getCategory()));
		} */
		cmbCategory.setEnabled(false);
		
		
		riskDOB = new DateField("Risk(DOB)");
		riskDOB.setDateFormat("dd/MM/yyyy");
		/*if(null != riskDOB &&  null != newIntimationDto.getInsuredPatient() &&  
				null !=  newIntimationDto.getInsuredPatient().getInsuredDateOfBirth()){
			riskDOB.setValue(newIntimationDto.getInsuredPatient().getInsuredDateOfBirth());
		}*/	
		
		riskAge = new TextField("Risk Age");	
		/*if(null != riskAge &&  null != newIntimationDto.getInsuredPatient() &&  
				null !=  newIntimationDto.getInsuredPatient().getInsuredAge()){
			riskAge.setValue(String.valueOf(newIntimationDto.getInsuredPatient().getInsuredAge()));
		}	*/
			
		
		FormLayout gpaLayout1 = new FormLayout(organisationName,sumInsured,sectionOrclass,cmbCategory,parentName,dateOfBirth,age);
		FormLayout gpaLayout2 = new FormLayout(riskName,riskDOB,riskAge);
		
		HorizontalLayout gpaLayout = new HorizontalLayout(gpaLayout1,gpaLayout2);
		gpaLayout.setCaption("UNNAMED RISK DETAILS");
		gpaLayout.setSpacing(true);
		
		hLayout.addComponents(currencyDetailsForm,accidentFrmLayout,injuryDetailsTxt);
		hLayout.setSpacing(true);
		
		VerticalLayout unNamedLayout = new VerticalLayout(hLayout);
		
		if(null != newIntimationDto && null != newIntimationDto.getPolicy() &&
				null != newIntimationDto.getPolicy().getProduct() && null != newIntimationDto.getPolicy().getProduct().getKey() &&
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDto.getPolicy().getProduct().getKey())) &&
				(SHAConstants.GPA_UN_NAMED_POLICY_TYPE.equalsIgnoreCase(newIntimationDto.getPolicy().getGpaPolicyType()))){
			
			unNamedLayout = new VerticalLayout(hLayout,gpaLayout);
			
			//hLayout.addComponents(unNamedLayout);
		}
		unNamedLayout.setSpacing(true);

		return unNamedLayout;
	}

	public void setCurrencyMaster(BeanItemContainer<SelectValue> currencyMaster) {
		currencyMasterContainer = currencyMaster;
	}

	private VerticalLayout buildTabsLayout() {
		// common part: create layout
		VerticalLayout tabsLayout = new VerticalLayout();
		//Vaadin8-setImmediate() tabsLayout.setImmediate(false);
		// tabsLayout.setWidth("100%");
		// tabsLayout.setHeight("100%");
		tabsLayout.setSizeFull();
		tabsLayout.setMargin(true);

		// tabSheet_1
		TabSheet previousClaimTab = buildClaimTabs();
		tabsLayout.addComponent(previousClaimTab);

		return tabsLayout;
	}

	private TabSheet buildClaimTabs() {
		TabSheet previousClaimTab = new TabSheet();
		//Vaadin8-setImmediate() previousClaimTab.setImmediate(true);
		// previousClaimTab.setWidth("100.0%");
		// previousClaimTab.setHeight("100.0%");
		previousClaimTab.setSizeFull();
		previousClaimTab.setWidth("100%");
		previousClaimTab.setStyleName(ValoTheme.TABSHEET_FRAMED);

		TabSheet previousClaimSheet = buildPreviouClaimTable();
		previousClaimTab.setHeight("100.0%");
//		previousClaimTab.setWidth("75.0%");
		previousClaimTab.addTab(previousClaimSheet, "Previous Claims", null);

		// tabSheet_2
		TabSheet balanceSITab = buildBalanceSITab();
		previousClaimTab.addTab(balanceSITab, "Balance SI", null);
		

		return previousClaimTab;
	}

	private TabSheet buildPreviouClaimTable() {
		TabSheet previousClaimLayout = new TabSheet();
		previousClaimLayout.hideTabs(true);
		//Vaadin8-setImmediate() previousClaimLayout.setImmediate(true);
		previousClaimLayout.setWidth("100%");
		previousClaimLayout.setHeight("100%");
		previousClaimLayout.setSizeFull();
		//Vaadin8-setImmediate() previousClaimLayout.setImmediate(true);
		previousClaimsRgistration.init("", false, false);
		
		previousClaimsRgistration.setTableList(previousClaimsRgistrationList);

		VerticalLayout previousClaimTableLayout = new VerticalLayout();

		previousClaimTableLayout.setHeight("195px");
		previousClaimTableLayout.setWidth("100%");
		previousClaimTableLayout.setMargin(true);
		previousClaimTableLayout.setSpacing(true);

		previousClaimTableLayout.addComponent(previousClaimsRgistration);

		previousClaimLayout.addComponent(previousClaimTableLayout);
		return previousClaimLayout;
	}

	private TabSheet buildBalanceSITab() {
		// common part: create layout
		TabSheet balanceSITab = new TabSheet();
		//Vaadin8-setImmediate() balanceSITab.setImmediate(true);
		balanceSITab.setWidth("100.0%");
		balanceSITab.setHeight("100.0%");
		balanceSITab.setSizeFull();
		balanceSITab.hideTabs(true);

		// verticalLayout_3
		VerticalLayout balanceSIVerticalLayout = buildBalanceSITabLayout();

		balanceSITab.addTab(balanceSIVerticalLayout, "Tab", null);
		return balanceSITab;
	}
	
	private VerticalLayout buildBalanceSITabLayout() {
		// common part: create layout
		VerticalLayout balanceSIVerticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() balanceSIVerticalLayout.setImmediate(false);
		balanceSIVerticalLayout.setWidth("100%");
		balanceSIVerticalLayout.setHeight("106.88%");
		balanceSIVerticalLayout.setMargin(true);
		balanceSIVerticalLayout.setSpacing(true);

		balanceSumInsuredPA.init(newIntimationDto.getIntimationId());

		balanceSIVerticalLayout.addComponent(balanceSumInsuredPA);

		return balanceSIVerticalLayout;
	}

	private VerticalLayout buildRegisterFields() {

			buildProvisionAmountField();

			registrationRemarksTxta = new TextArea("Department Remarks");
			registrationRemarksTxta.setRequired(true);
			registrationRemarksTxta.setValidationVisible(false);
			registrationRemarksTxta.setRequiredError("Please Enter Registration Remarks.");
			//Vaadin8-setImmediate() registrationRemarksTxta.setImmediate(false);
			registrationRemarksTxta.setMaxLength(200);
			
			if (registrationRemarksTxta != null) {
				dynamicFrmLayout.removeComponent(registrationRemarksTxta);
			}

			if (suggestRejectionTxta != null) {
				dynamicFrmLayout.removeComponent(suggestRejectionTxta);
				suggestRejectionTxta = null;

				claimDto.setSuggestRejection(false);
			}

			dynamicFrmLayout.addComponent(registrationRemarksTxta);
			dynamicFieldsLayout.addComponent(dynamicFrmLayout);

		return dynamicFieldsLayout;

	}

	private void buildProvisionAmountField() {
		CSValidator provisionalAmtValidator = new CSValidator();
		provisionalAmtTxt = new TextField();
		provisionalAmtTxt.setCaption("Provision Amount");
		//Vaadin8-setImmediate() provisionalAmtTxt.setImmediate(false);
		provisionalAmtTxt.setWidth("-1px");
		provisionalAmtTxt.setHeight("-1px");
		provisionalAmtTxt.setMaxLength(13);
		provisionalAmtTxt.setRequired(true);
		//provisionalAmtTxt.setValue(String.valueOf(searchDTO.getPaAccProvAmt()));
		provisionalAmtTxt.setValue(searchDTO.getPaAccProvAmt()!=null ?String.valueOf(searchDTO.getPaAccProvAmt()) : "0");
		provisionalAmtTxt.setRequiredError("Please enter Provision Amount");
		provisionalAmtValidator.extend(provisionalAmtTxt);
		provisionalAmtValidator.setRegExp("^[0-9]*$");
		provisionalAmtValidator.setPreventInvalidTyping(true);
		provisionalAmtTxt.setValidationVisible(false);


//			provisionamount = tmpCpuCode.getProvisionAmount();
			
			Double claimedAmount = claimedAmtTxt.getValue() != null ? Double.valueOf(claimedAmtTxt.getValue()) : 0d;
				
			System.out.println("Claimed amt======================"+claimedAmount);
			
			if (currencyNameSelect.getValue() != null) {
				claimDto.setCurrencyId((SelectValue) currencyNameSelect
						.getValue());
			}
			
		provisionalAmtTxt.addTextChangeListener(new TextChangeListener() {

			@Override
			public void textChange(TextChangeEvent event) {
				
				if(event.getText() != null && ! event.getText().isEmpty()){
					Double provAmt = Double.valueOf(event.getText());
					if(balanceSumInsured != null && balanceSumInsured > 0 && balanceSumInsured > provAmt){
							provisionamount = provAmt;
							provisionalAmtTxt.setValue(String.valueOf(provisionamount.longValue()));
					}
					else {
						provisionamount = balanceSumInsured;
						provisionalAmtTxt.setValue(String.valueOf(balanceSumInsured.longValue()));
					}
					claimDto.setProvisionHomeAmount(Double.valueOf(provisionalAmtTxt.getValue()));
					claimDto.setProvisionAmount(Double.valueOf(provisionalAmtTxt.getValue()));

				}
			}
		});
		dynamicFrmLayout.removeAllComponents();
		dynamicFrmLayout.addComponent(provisionalAmtTxt);
	}

	private FormLayout buildSuggestRejectionFields(FormLayout dynamicFrmLayout) {
		claimDto.setSuggestRejection(true);
		provisionalAmtTxt.setValue("");
		
		if (dynamicFrmLayout == null) {
			dynamicFrmLayout = new FormLayout();
		}

		if (dynamicFrmLayout.getComponentCount() > 0) {
			if (registrationRemarksTxta != null) {
				dynamicFrmLayout.removeComponent(registrationRemarksTxta);
				registrationRemarksTxta = null;
			}
		}

		if (suggestRejectionTxta != null) {
			dynamicFrmLayout.removeComponent(suggestRejectionTxta);
		}

		suggestRejectionTxta = new TextArea("Suggested Rejection Remarks");
		suggestRejectionTxta.setRequired(true);
		suggestRejectionTxta.setValidationVisible(false);
		suggestRejectionTxta.setMaxLength(200);
		//Vaadin8-setImmediate() suggestRejectionTxta.setImmediate(false);
		suggestRejectionTxta
				.setRequiredError("Please Provide Suggesion Remarks for Rejection");
		
		dynamicFrmLayout.addComponent(suggestRejectionTxta);

		return dynamicFrmLayout;
	}

	public void registerClick() {
		
		claimDto.setSuggestRejection(false);

		Boolean hasError = validateClaimdeAmount();
		
		if(hasError != null && !hasError)
		{
		if (dynamicFieldsLayout != null) {
			if (dynamicFieldsLayout.getComponentCount() > 0) {
				dynamicFieldsLayout.removeAllComponents();
			}

		}
			dynamicFieldsLayout = buildRegisterFields();
		}
		else
		{
//			Notification.show("ERROR", "Please Fill the Manadatory Fields.",
//					Notification.Type.ERROR_MESSAGE);
		}
		
	}

	public void suggestRejectionClick() {		
		claimDto.setSuggestRejection(true);
		if (dynamicFieldsLayout != null) {
			if (dynamicFieldsLayout.getComponentCount() > 0) {
				dynamicFieldsLayout.removeAllComponents();
			}
		}
		
		Boolean hasError = validateClaimdeAmount();
		if(hasError != null && !hasError){
			buildProvisionAmountField();
			dynamicFieldsLayout
					.addComponent(buildSuggestRejectionFields(dynamicFrmLayout));
			
		}
		else
		{
//			Notification.show("ERROR", "Please Fill the Manadatory Field.",
//					Notification.Type.ERROR_MESSAGE);
		}
		
	}

	public void cancelRegistration() {

		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you really want to cancel the Claim Registration ?",
				"No", "Yes", new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isCanceled() && !dialog.isConfirmed()) {
							// Confirmed to continue
							// fireViewEvent(MenuItemBean.SEARCH_REGISTER_CLAIM,
							// null);

							registerationBean = null;
							newIntimationDto = null;
							fireViewEvent(LoaderPresenter.LOAD_URL,
									MenuItemBean.SEARCH_PA_CLAIM_REGISTER);

						} else {
							// User did not confirm
						}
					}
				});

		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	}
	

	public void submitRegistration(ClaimDto claimDto, Boolean isProceedfurther) {

		// Check Whether all the mandatory fields are entered.
		Boolean hasError = false;

		String errMsg = "";
		FormLayout frmLayout = dynamicFrmLayout;

		hasError = validateClaimdeAmount();
		if(hasError != null && hasError){
			return;
		}
		
		if (hasError != null && (claimDto.getSuggestRejection() ==  null || claimDto.getStatusName() ==  null || (claimDto.getStatusName() != null && claimDto.getStatusName().isEmpty()))) {
			hasError = true;
			Notification
					.show("ERROR",
							"Please Click Register Or Suggest Reject Button before Submitting the Claim ",
							Notification.Type.ERROR_MESSAGE);
			return;
		}

//		if (suggestRejectionTxta != null
//				&& suggestRejectionTxta.getValue() == null) {
//			Notification
//					.show("ERROR",
//							"Please Enter Manadatory Fields by Clicking Register Or Suggest Reject Button. ",
//							Notification.Type.ERROR_MESSAGE);
//			return;
//		}
		
		if(hasError != null && !claimDto.getSuggestRejection() && !isProceedfurther) {
			Notification.show("ERROR",
					"Rejection only applicable for this policy because of Cheque Realisation is Dishonoured. ",
					Notification.Type.ERROR_MESSAGE);
			registerButton.setEnabled(false);
			if (dynamicFieldsLayout.getComponentCount() > 0) {
				dynamicFieldsLayout.removeAllComponents();
			}
			hasError = true;
			return;
		} else {
			registerButton.setEnabled(true);
		}
/*
		if(hasError != null && dateOfAccidentDeath.getValue() == null){
			hasError = true;
			errMsg += "Please Enter Valid Date for Accident / Death <br>";
//			Notification.show("ERROR", "Please Enter Valid Date for Accedent / Death .",
//					Notification.Type.ERROR_MESSAGE);
		}*/
				
		/*if(hasError != null && dateOfAccidentDeath.getValue() != null){
			if(!SHAUtils.validateDate(dateOfAccidentDeath.getValue())){
				hasError = true;
				errMsg += "Please Enter Valid Date for Accident / Death <br>";
//				Notification.show("ERROR", "Please Enter Valid Date for Accedent / Death .",
//						Notification.Type.ERROR_MESSAGE);
			}
		}*/
		if (hasError != null && provisionalAmtTxt != null && !provisionalAmtTxt.isValid()) {
			provisionalAmtTxt.setValidationVisible(true);
			hasError = true;
			errMsg += provisionalAmtTxt.getRequiredError() + "<br>";
		}
		
		if(hasError != null && claimDto.getSuggestRejection() != null && !claimDto.getSuggestRejection() && ((registrationRemarksTxta != null && registrationRemarksTxta.getValue() == null ) || (registrationRemarksTxta != null && registrationRemarksTxta.getValue() != null && registrationRemarksTxta.getValue().isEmpty()))){
			
			if(registrationRemarksTxta.getRequiredError() != null){
				registrationRemarksTxta.setValidationVisible(true);
				errMsg += registrationRemarksTxta.getRequiredError() + "<br>";
				hasError = true;
			}
		}
		if (hasError != null && claimDto.getSuggestRejection() != null && claimDto.getSuggestRejection() && ((suggestRejectionTxta != null
				&& suggestRejectionTxta.getValue() == null ) || (suggestRejectionTxta != null && suggestRejectionTxta.getValue() != null && suggestRejectionTxta.getValue().isEmpty()))) {
			if(suggestRejectionTxta.getRequiredError() != null){
				suggestRejectionTxta.setValidationVisible(true);
				errMsg += suggestRejectionTxta.getRequiredError() + "<br>";
				hasError = true;
			}
		}
		


		if (suggestRejectionTxta != null) {
			suggestRejectionTxta.setValidationVisible(false);
		}

		if (hasError != null && hasError) {
			
			showErrorMessage(errMsg);
//			Notification.show("ERROR", "Please Fill the Manadatory Fields.",
//					Notification.Type.ERROR_MESSAGE);
			hasError = true;
			return;
		}
		if (hasError != null && !hasError) {
			claimDto.setNewIntimationDto(newIntimationDto);
			Long vip = vipChk.getValue() ? 1l : 0l;
			claimDto.setIsVipCustomer(vip);
			claimDto.setConversionLetter(0l);
			claimDto.setStatusDate(new Timestamp(System.currentTimeMillis()));
			claimDto.setCurrencyId((SelectValue) currencyNameSelect.getValue());

			// TODO set currency conversion and set the provision
			
			claimDto.setClaimedAmount((claimedAmtTxt.getValue() == null || claimedAmtTxt
					.getValue() == "") ? Double.valueOf("0") : Double
					.valueOf(claimedAmtTxt.getValue()));
			claimDto.setClaimedHomeAmount((claimedAmtTxt.getValue() == null || claimedAmtTxt
					.getValue() == "") ? Double.valueOf("0") : Double
					.valueOf(claimedAmtTxt.getValue()));
			claimDto.setRegistrationRemarks((registrationRemarksTxta == null || registrationRemarksTxta
					.getValue() == null) ? null : registrationRemarksTxta
					.getValue());
			claimDto.setSuggestedRejectionRemarks((suggestRejectionTxta == null || suggestRejectionTxta
					.getValue() == null) ? null : suggestRejectionTxta
					.getValue());

			claimDto.setIncidenceFlag((Boolean)incedentOption.getValue());
			
			claimDto.setIncidenceFlagValue(claimDto.getIncidenceFlag() ? "A" : "D");
			claimDto.setIncidenceDate(dateOfAccidentDeath.getValue());
			if(null != dateOfAccident && null != dateOfAccident.getValue())
			{
				claimDto.setAccidentDate(dateOfAccident.getValue());
			}
			if(null != dateOfDeath && null != dateOfDeath.getValue())
			{
				claimDto.setDeathDate(dateOfDeath.getValue());
			}
			if(null != dateOfDisablement && null != dateOfDisablement.getValue())
			{
				claimDto.setDisablementDate(dateOfDisablement.getValue());
			}
			claimDto.setInjuryRemarks(injuryDetailsTxt.getValue() != null ? injuryDetailsTxt.getValue() : "");
			
			SelectValue clmType = new SelectValue(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY,SHAConstants.REIMBURSEMENT);
			
			
			if (provisionalAmtTxt != null ) {
				claimDto.setProvisionAmount((provisionalAmtTxt.getValue() != null && provisionalAmtTxt
						.getValue() != "") ? Double.valueOf(provisionalAmtTxt
						.getValue()) : Double.valueOf("0"));
				claimDto.setCurrentProvisionAmount(claimDto.getProvisionAmount());
				claimDto.setProvisionHomeAmount((provisionalAmtTxt.getValue() != null && provisionalAmtTxt
						.getValue() != "") ? Double.valueOf(provisionalAmtTxt
						.getValue()) : Double.valueOf("0"));
				
			}
			claimDto.setLobId(ReferenceTable.PA_LOB_KEY);
			
			SelectValue lobId = claimDto.getNewIntimationDto().getLobId();
			
			if(lobId != null && lobId.getId() != null && lobId.getId().equals(ReferenceTable.PACKAGE_MASTER_VALUE)){
				claimDto.setLobId(ReferenceTable.PA_LOB_KEY);
				claimDto.setProcessClaimType(SHAConstants.PA_LOB_TYPE);	
			}
			else if(("A").equalsIgnoreCase(claimDto.getIncidenceFlagValue()) && claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalType().getKey().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
				claimDto.setProcessClaimType(SHAConstants.HEALTH_LOB_FLAG);
			}
			else{
				claimDto.setProcessClaimType(SHAConstants.PA_LOB_TYPE);	
			}
				
			if(null != claimDto && null != claimDto.getNewIntimationDto() && null != claimDto.getNewIntimationDto().getPolicy() &&
					null != claimDto.getNewIntimationDto().getPolicy().getProduct() && 
					(ReferenceTable.getGPAProducts().containsKey(claimDto.getNewIntimationDto().getPolicy().getProduct().getKey()) &&
							(SHAConstants.GPA_UN_NAMED_POLICY_TYPE.equalsIgnoreCase(claimDto.getNewIntimationDto().getPolicy().getGpaPolicyType())))){
				
			String gpaParentName = null != parentName && null != parentName.getValue() ? parentName.getValue() : null;
			String gpaRiskName = null != riskName && null != riskName.getValue() ? riskName.getValue() : null;
			Double gpaParentAge = null != age && null != age.getValue() && !("").equals(age.getValue())? Double.valueOf(age.getValue()) : 0d;
			Date gpaParentDOB = null != dateOfBirth && null != dateOfBirth.getValue() ? dateOfBirth.getValue() : null;	
			Double gpaRiskAge = null != riskAge && null != riskAge.getValue() && !("").equals(riskAge.getValue())? Double.valueOf(riskAge.getValue()) : 0d;
			Date gpaRiskDOB = null != riskDOB && null != riskDOB.getValue() ? riskDOB.getValue() : null;
			String gpaSection = null != sectionOrclass && null != sectionOrclass.getValue() ? sectionOrclass.getValue() : null;
			String gpaCategory = null != cmbCategory && null != cmbCategory.getValue() && null != cmbCategory.getValue().toString() ? cmbCategory.getValue().toString() : null;
			
			/*claimDto.getNewIntimationDto().setPaParentName(gpaParentName);
			claimDto.getNewIntimationDto().setPaPatientName(gpaRiskName);
			claimDto.getNewIntimationDto().setParentAge(gpaParentAge);
			claimDto.getNewIntimationDto().setParentDOB(gpaParentDOB);*/
			
			claimDto.setGpaParentName(gpaParentName);
			claimDto.setGpaParentDOB(gpaParentDOB);
			claimDto.setGpaParentAge(gpaParentAge);
			claimDto.setGpaRiskName(gpaRiskName);
			claimDto.setGpaRiskDOB(gpaRiskDOB);
			claimDto.setGpaRiskAge(gpaRiskAge);
			claimDto.setGpaSection(gpaSection);			
			
			String gpaCategoryWithDescription = gpaCategory;
			if(null != gpaCategoryWithDescription){
			String[] splitCategory = gpaCategoryWithDescription.split("-");
			String category = splitCategory[0];
			claimDto.setGpaCategory(category);
			}
			
			}

			fireViewEvent(PAClaimRegistrationPresenter.SUBMIT_PA_CLAIM_REGISTRATION,
					registerationBean, claimDto);
		}
	}

	private Boolean validateClaimdeAmount() {
		Boolean hasError = null;
		
		if (claimedAmtTxt == null || ("").equals(claimedAmtTxt.getValue())){
			hasError = true;
		}
		else if(claimedAmtTxt != null && !("").equalsIgnoreCase(claimedAmtTxt.getValue())){
			if(Integer.valueOf(claimedAmtTxt.getValue()) < 0){
					
					claimedAmtTxt.setValidationVisible(true);
					hasError = true;
					showErrorMessage("Please Enter Claimed Amount less than or equal to Available Balance Sum Insured");
			}
			if(Double.valueOf(claimedAmtTxt.getValue()) > balanceSumInsured){
				hasError = true;
				showErrorMessage("Please Enter Claimed Amount less than or equal to Available Balance Sum Insured");
			}
			else{
				hasError = false;
			}
		}
		return hasError;
	}

	private FormLayout buildBasicInfoLayout(Claim claim) {
//	private FormLayout buildBasicInfoLayout() {
		String value = "";
		if (claim != null) {

			if (claim.getCurrencyId() != null) {
				value = claim.getCurrencyId().getValue();
			}

		}

		FormLayout formFieldsLayout = new FormLayout();

		currencyNameSelect = new ComboBox();
		currencyNameSelect.setCaption("Currency Name");
		currencyNameSelect.setWidth("160px");
		currencyNameSelect.setHeight("-1px");
		currencyNameSelect.addItem(value);
		currencyNameSelect.setNullSelectionAllowed(false);
		formFieldsLayout.addComponent(currencyNameSelect);
		currencyNameSelect.setValue(currencyNameSelect.getContainerDataSource()
				.getItemIds().toArray()[0]);
		currencyNameSelect.setReadOnly(true);

		claimedAmtTxt = new TextField();
		claimedAmtTxt.setCaption("Amount Claimed *   " + value);
		claimedAmtTxt.setWidth("-1px");
		claimedAmtTxt.setHeight("-1px");
		if (claim != null) {
			claimedAmtTxt.setValue(claim.getClaimedAmount() != null ? claim
					.getClaimedAmount().toString() : "");
		} else {
			claimedAmtTxt.setValue("0");
		}

		claimedAmtTxt.setReadOnly(true);
		claimedAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		formFieldsLayout.addComponent(claimedAmtTxt);

		provisionalAmtTxt = new TextField();
		provisionalAmtTxt.setCaption("Provision Amount");
		provisionalAmtTxt.setWidth("-1px");
		provisionalAmtTxt.setHeight("-1px");
		if (claim != null) {
			provisionalAmtTxt.setValue(claim.getProvisionAmount() != null ? claim
					.getProvisionAmount().toString() : "");
		}
		provisionalAmtTxt.setReadOnly(true);
		provisionalAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		formFieldsLayout.addComponent(provisionalAmtTxt);

		return formFieldsLayout;
	}

	private VerticalLayout buildRegisterSuccessLayout(Claim claim) {
		if (provisionalAmtTxt != null) {
			dynamicFrmLayout.removeComponent(provisionalAmtTxt);
		}
		FormLayout formFieldsLayout = buildBasicInfoLayout(claim);
//		FormLayout formFieldsLayout = buildBasicInfoLayout();
		HorizontalLayout buttonLayoutForSuccess = new HorizontalLayout();

		TextArea regRemarksTxta = new TextArea("Registration Remarks *");
		TextArea sugRejTxta  = new TextArea("Suggested Rejection Remarks *");
			
		regRemarksTxta.setValue(claimDto.getRegistrationRemarks());
		regRemarksTxta.setReadOnly(true);
		regRemarksTxta.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		regRemarksTxta.setHeight("-1px");
		formFieldsLayout.addComponent(regRemarksTxta);
			
			TextField suggestRejectionTxt = new TextField("Suggest Rejection");

			if (claimDto.getSuggestRejection()) {

				if (registrationRemarksTxta != null) {
					formFieldsLayout.removeComponent(registrationRemarksTxta);
				}
				suggestRejectionTxt
						.setValue(claimDto.getSuggestRejection() ? "Yes" : "No");
				suggestRejectionTxt.setReadOnly(true);
				formFieldsLayout.addComponent(suggestRejectionTxt);

				sugRejTxta.setValue(claimDto
						.getSuggestedRejectionRemarks());
				sugRejTxta.setReadOnly(true);
				sugRejTxta.setHeight("-1px");
				sugRejTxta.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				formFieldsLayout.addComponent(sugRejTxta);	
		}
			
			TextField claimNumber = new TextField("Claim Number");
			claimNumber.setValue(claim.getClaimId());
			claimNumber.setReadOnly(true);
			claimNumber.setWidth("100%");
			claimNumber.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			formFieldsLayout.addComponent(claimNumber);

		String homePageButtonCaption = "Home Page";

		homePageButton.setCaption(homePageButtonCaption);
		//Vaadin8-setImmediate() homePageButton.setImmediate(true);
		homePageButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		homePageButton.setWidth("-1px");
		homePageButton.setHeight("-1px");

		homePageButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				fireViewEvent(LoaderPresenter.LOAD_URL,
						MenuItemBean.SEARCH_PA_CLAIM_REGISTER);
				
			}
		});
	
		
		//Vaadin8-setImmediate() homePageButton.setImmediate(true);


		Button registerAnotherClaimButton = new Button();
		registerAnotherClaimButton.setCaption("Register Another Claim");
		//Vaadin8-setImmediate() registerAnotherClaimButton.setImmediate(true);
		registerAnotherClaimButton.setWidth("-1px");
		registerAnotherClaimButton.setHeight("-1px");
		registerAnotherClaimButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

		registerAnotherClaimButton
				.addClickListener(new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						fireViewEvent(LoaderPresenter.LOAD_URL,
								MenuItemBean.SEARCH_PA_CLAIM_REGISTER);
					}
				});
		buttonLayoutForSuccess.addComponents(homePageButton,
				registerAnotherClaimButton);
		buttonLayoutForSuccess.setSpacing(true);
	
		buttonLayoutForSuccess.addComponent(homePageButton);
	
		HorizontalLayout buttonLayoutForSuccess1 = new HorizontalLayout(
				buttonLayoutForSuccess);
		buttonLayoutForSuccess1.setMargin(true);
		buttonLayoutForSuccess1.setWidth("100%");

		VerticalLayout layout = new VerticalLayout();
		

		Label label = new Label(
				"<b style = 'color:red'>Claim has been registered successfully !!!! </b>",
				Label.CONTENT_XHTML);
		layout.addComponents(formFieldsLayout, label,
				buttonLayoutForSuccess1);
		buttonLayoutForSuccess1.setComponentAlignment(
				buttonLayoutForSuccess, Alignment.MIDDLE_CENTER);
		
		toolbar.countTool();

		
		return layout;
	}

//	public void openPdfFileInWindow(final String filepath) {
//		Window window = new Window();
//		// ((VerticalLayout) window.getContent()).setSizeFull();
//		window.setResizable(true);
//		window.setCaption("Claim Form Covering Letter PDF");
//		window.setWidth("800");
//		window.setHeight("600");
//		window.setModal(true);
//		window.center();
//
//		Path p = Paths.get(filepath);
//		String fileName = p.getFileName().toString();
//
//		StreamResource.StreamSource s = new StreamResource.StreamSource() {
//
//			public InputStream getStream() {
//				try {
//
//					File f = new File(filepath);
//					FileInputStream fis = new FileInputStream(f);
//					return fis;
//
//				} catch (Exception e) {
//					e.printStackTrace();
//					return null;
//				}
//			}
//		};
//
//		StreamResource r = new StreamResource(s, fileName);
//		Embedded e = new Embedded();
//		e.setSizeFull();
//		e.setType(Embedded.TYPE_BROWSER);
//		r.setMIMEType("application/pdf");
//		e.setSource(r);
//		window.setContent(e);
//		UI.getCurrent().addWindow(window);
//	}

	public void setPreviousClaimsDtoList(
			List<PreviousClaimsTableDTO> previousClaimDtoList) {
		if (null != previousClaimDtoList && !previousClaimDtoList.isEmpty()) {
			previousClaimsRgistrationList  = previousClaimDtoList;
		}
	}

//	public void setBalanceSumInsured(Double balanceSI){
//		balanceSumInsured = balanceSI;
//		System.out.println("Balance SI In UI Screen ============================="+balanceSI);
//	}	
	
//	public void setCpuObject(TmpCPUCode tmpCpu) {
//
//		tmpCpuCode = new TmpCPUCode();
//
//		if (tmpCpu != null) {
//			tmpCpuCode.setKey(tmpCpu.getKey());
//			tmpCpuCode.setCpuCode(tmpCpu.getCpuCode());
//			tmpCpuCode.setDescription(tmpCpu.getDescription());
//			tmpCpuCode.setProvisionAmount(tmpCpu.getProvisionAmount());
//		}
//	}

	public void setSublimitList(List<SublimitFunObject> sublimitList) {

		resultSublimitList = sublimitList;
	}

	public void setClaimDetails(ClaimDto newClaimDto) {
		claimDto.setClaimId(newClaimDto.getClaimId());
		claimDto.setKey(newClaimDto.getKey());
		claim =  ClaimMapper.getInstance().getClaim(newClaimDto);

		if (claim != null) {
			registrationDetailsLayout.removeComponent(registerBtnLayout);
			registrationDetailsLayout.removeComponent(submitButtonLayout);
			dynamicFrmLayout.removeAllComponents();
			registrationDetailsLayout
					.addComponent(buildRegisterSuccessLayout(claim));
//			registrationDetailsLayout
//			.addComponent(buildRegisterSuccessLayout());
		}
	}
	
	  public void showPolicyStatusMessage(String message) {
		  final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(true);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setSpacing(true);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout();
			layout.addComponent(new Label(message, ContentMode.HTML));
			layout.addComponent(okButton);
			layout.setMargin(true);
			layout.setSpacing(true);
			dialog.setContent(layout);
			dialog.setWidth("30%");
			dialog.show(getUI().getCurrent(), null, true);
		}
		private void showErrorMessage(String eMsg) {
			Label label = new Label(eMsg, ContentMode.HTML);
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
		}
		
		public void addListener()
		{
			dateOfAccident.addValueChangeListener(new Property.ValueChangeListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void valueChange(ValueChangeEvent event) {
					Policy policy = (Policy) ((PopupDateField) event
							.getProperty()).getData();

					Date enteredDate = (Date) ((PopupDateField) event
							.getProperty()).getValue();
					if (enteredDate != null) {

						try {
							dateOfAccident.validate();
							enteredDate = (Date) event.getProperty()
									.getValue();
						} catch (Exception e) {
							dateOfAccident.setValue(null);
							showErrorMessage("Please Enter a valid Accident Date");
							// Notification.show("Please Enter a valid Date");
							return;
						}
					}

					Date currentDate = new Date();
					Date policyFrmDate = null;
					Date policyToDate = null;
					if (policy != null) {
						policyFrmDate = policy.getPolicyFromDate();
						policyToDate = policy.getPolicyToDate();
					}
					if (enteredDate != null && policyFrmDate != null
							&& policyToDate != null) {
						if (!enteredDate.after(policyFrmDate)
								|| enteredDate.compareTo(policyToDate) > 0) {
							event.getProperty().setValue(null);
						
							showErrorMessage("Accident Date is not in range between Policy From Date and Policy To Date.");
						}
					}
				}
			});		
			
			dateOfDeath.addValueChangeListener(new Property.ValueChangeListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void valueChange(ValueChangeEvent event) {
					Policy policy = (Policy) ((PopupDateField) event
							.getProperty()).getData();

					Date enteredDate = (Date) ((PopupDateField) event
							.getProperty()).getValue();
					if (enteredDate != null) {

						try {
							dateOfDeath.validate();
							enteredDate = (Date) event.getProperty()
									.getValue();
						} catch (Exception e) {
							dateOfDeath.setValue(null);
							showErrorMessage("Please Enter a valid Death Date");
							// Notification.show("Please Enter a valid Date");
							return;
						}
					}

					Date currentDate = new Date();
					Date policyFrmDate = null;
					Date policyToDate = null;
					if (policy != null) {
						policyFrmDate = policy.getPolicyFromDate();
						policyToDate = policy.getPolicyToDate();
					}
					/*if (enteredDate != null && policyFrmDate != null
							&& policyToDate != null) {
						if (!enteredDate.after(policyFrmDate)
								|| enteredDate.compareTo(policyToDate) > 0) {
							event.getProperty().setValue(null);
						
							showErrorMessage("Death Date is not in range between Policy From Date and Policy To Date.");
						}
					}*/
					if(null != enteredDate)
					{
						Date accidentDate = new Date();
						if(null != dateOfAccident.getValue()){
							accidentDate = dateOfAccident.getValue();
						}
						if (accidentDate != null && null != enteredDate) {
							
							Long diffDays = SHAUtils.getDaysBetweenDate(accidentDate,enteredDate);
							
							if(null != diffDays && diffDays>365)
							{
								showErrorMessage("The date of death captured is beyond 12 months from the date of accident");
							}
						}
					}
					
					
					
				}
			});		
			
			dateOfDisablement.addValueChangeListener(new Property.ValueChangeListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void valueChange(ValueChangeEvent event) {
					Policy policy = (Policy) ((PopupDateField) event
							.getProperty()).getData();

					Date enteredDate = (Date) ((PopupDateField) event
							.getProperty()).getValue();
					if (enteredDate != null) {

						try {
							dateOfDisablement.validate();
							enteredDate = (Date) event.getProperty()
									.getValue();
						} catch (Exception e) {
							dateOfDisablement.setValue(null);
							showErrorMessage("Please Enter a valid Disablement Date");
							// Notification.show("Please Enter a valid Date");
							return;
						}
					}

					Date accidentDate = new Date();
					if(null != dateOfAccident.getValue()){
						accidentDate = dateOfAccident.getValue();
					}
					if (accidentDate != null && null != enteredDate) {
						
						Long diffMonths = SHAUtils.getDaysBetweenDate(accidentDate,enteredDate);
						
						if(null != diffMonths && diffMonths>365)
						{
							dateOfDisablement.setValue(null);
							showErrorMessage("The date of disablement captured is beyond 12 months from the date of accident");
						}
					}
					
				}
			});				
		}
		
		private void releaseHumanTask(){
			
			Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
	     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
	 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
	 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

	 		if(existingTaskNumber != null){
	 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
	 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
	 		}
	 		
	 		if(wrkFlowKey != null){
	 			DBCalculationService dbService = new DBCalculationService();
	 			dbService.callUnlockProcedure(wrkFlowKey);
	 			getSession().setAttribute(SHAConstants.WK_KEY, null);
	 		}
		}
	
		public void setDropDownValues(BeanItemContainer<SelectValue> category) 
		{	
			
			cmbCategory.setContainerDataSource(category);
			cmbCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbCategory.setItemCaptionPropertyId("value");		
			   
			if(null != category){
			 SelectValue defaultCategory = category.getIdByIndex(0);
			 cmbCategory.setValue(defaultCategory);
			}
			       
		}
		
		 private Boolean validatePolicyStatus(String policyNumber){
				Boolean hasError = false;
				enteredValues.put("polNo", policyNumber);
				
				BeanItemContainer<PremPolicy> policyContainer =  policyService.filterPolicyDetailsPremia(enteredValues);
				List<PremPolicy> policyList = policyContainer.getItemIds();
				if(policyList !=null && !policyList.isEmpty()){
					for (PremPolicy premPolicy : policyList) {
						if(premPolicy.getPolicyStatus().equalsIgnoreCase(SHAConstants.CANCELLED_POLICY)){
							hasError = true;
						}
						
					}
				}
				return !hasError;
			}
			
			public void showErrorPageForCancelledPolicy(){
					
					String message = SHAConstants.CANCELLED_POLICY_ALERT ;
					
					Label successLabel = new Label(
							"<b style = 'color: red;'>" + message + "</b>",
							ContentMode.HTML);
					Button homeButton = new Button("Ok");
					homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
					layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
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
					dialog.show(getUI().getCurrent(), null, true);
					homeButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;
				
						@Override
						public void buttonClick(ClickEvent event) {
							dialog.close();
						}
					});
					
				}
}
