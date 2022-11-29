package com.shaic.claim.omp.registration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.view.LoaderPresenter;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.MasterService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.PolicyService;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.domain.OMPClaimMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

@ViewScoped
@SuppressWarnings({ "serial", "deprecation" })
public class OMPClaimRegistrationWizardForm extends ViewComponent {

	@Inject
	private ViewDetails viewDetails;

	@Inject
	private OMPPreviousClaimDetailTable previousClaimsRgistration;

	@Inject
	private OMPBalanceSiDetailTable balanceSIComponentInstance;


	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@Inject
//	private Instance<SublimtListTable> sublimitListInstance;

	@EJB
	private PolicyService policyService;
	
	private BeanFieldGroup<ClaimDto> binder;
	
	
	private OMPClaimRegistrationWizardForm form;
//	private OMPSearchClaimRegistrationTableDTO bean;
	
//	private FieldGroup binder;
	@Inject
	private OMPBalanceSiForm ompBalanceSiFormInstance;

	private List<SublimitFunObject> resultSublimitList = new ArrayList<SublimitFunObject>();

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

	private VerticalLayout dynamicFieldsLayout;

	private TextField provisionalAmtTxt;

	private Button registerButton;

	private Button suggestRejectBtn;

	private Button homePageButton;

	private OMPSearchClaimRegistrationTableDTO registerationBean = new OMPSearchClaimRegistrationTableDTO();

	private OMPClaim claim = null;

	private ClaimDto claimDto;

	private NewIntimationDto newIntimationDto = new NewIntimationDto();

	private TmpCPUCode tmpCpuCode = null;

	private Double provisionamount = 0d;
	
	private Double balanceSumInsured = 0d;

	private VerticalLayout registrationDetailsLayout;

	private Button submitButton;

	private Button cancelButton;

	private HorizontalLayout submitButtonLayout;
	
	private HorizontalLayout hLayout;
	
	private FormLayout leftLayout;
	
	private FormLayout rightLayout;
	
	private OptionGroup  claimTypeOption;
	
	private OptionGroup hospitalOption ;
	
	private TextField txtHospitalName;
	
	private TextField txtHospitalCity;
	
	private ComboBox cmbHospitalCountry;
	
	private TextField txtAilmentOrLoss;
	
	private TextField txtINRConversionRate;
	
	private TextField txtINRValue;
	
	private ComboBox cmbEventCode;
	
	private TextField txtInitialProvisionAmt;
	
	private TextField txtAmoutnClaimed;

	private TextField txtnameofInsured;
	
	private TextField txtproductName ;
	
	private TextField amountClaimed;
	
	private TextField claimNumber;
	
	private BeanItemContainer<SelectValue> currencyMasterContainer;

	@Inject
	private Instance<IntimationDetailsCarousel> carouselInstance;

	private IntimationDetailsCarousel intimationDetailCarousel;

	@EJB
	private MasterService masterService;

	private HorizontalLayout registerBtnLayout;

	private OMPSearchClaimRegistrationTableDTO searchDTO;
	
	private PreauthDTO preauthDTO;
	
	private OMPBalanceSiTableDTO balanceDto;
	
	private Double inrTotal = 0d;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private RRCDTO rrcDTO;
	
	private NewIntimationDto bean;
	
	private List<String> errorList;
	
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
	//	initBinder(); my err uc

	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<ClaimDto>(
				ClaimDto.class);
		this.binder
				.setItemDataSource(claimDto);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void initView(OMPSearchClaimRegistrationTableDTO dto) {
		

		if (dto != null) {
			this.searchDTO = dto;
			if(dto.getPopupMap() != null && ! dto.getPopupMap().isEmpty())
			{
				poupMessageForProduct();
			}else if(dto.getSuspiciousPopUp() != null && ! dto.getSuspiciousPopUp().isEmpty()){
				suspiousPopupMessage();
			}
//			newIntimationDto = new NewIntimationDto();//re
			newIntimationDto = dto.getNewIntimationDto();
			registerationBean = dto;
			preauthDTO = new PreauthDTO();
//			claimDto = new ClaimDto();
			claimDto = dto.getClaimDto();
//			preauthDTO.setRrcDTO(dto.getRrcDTO());
//			preauthDTO.setRodHumanTask(dto.getHumanTask());
			initBinder();
			fireViewEvent(OMPClaimRegistrationWizardPresenter.GET_BALANCE_SI,newIntimationDto); // uncomment//
			fireViewEvent(OMPClaimRegistrationWizardPresenter.GET_CPU_OBJECT,
					newIntimationDto.getCpuId());			

			claimDto.setNewIntimationDto(newIntimationDto);
		}
	
		
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(newIntimationDto);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);

		Panel buildMainLayout = buildMainLayout();
		if(!dto.getIsProceedFurther()) {
			registerButton.setEnabled(false);
			String message = "This policy Endorsement in process. Claim cannot be Registered.";
			if(dto.getIsCancelledPolicy()) {
				message = "This policy is Cancelled. Claim cannot be Registered.";
			} 
			showPolicyStatusMessage(message);
		}
//		registerButton.setEnabled(true);
		addListener();
		setCompositionRoot(buildMainLayout);

	}
	
	private Panel buildMainLayout() {
		mainLayout = new VerticalLayout();
//		initBinder();
		homePageButton = new Button();
		VerticalLayout tabsLayout = buildTabsLayout();
		registrationDetailsLayout = new VerticalLayout();
		registerBtnLayout = new HorizontalLayout();
		submitButtonLayout = new HorizontalLayout();
		Panel registrationPanel = buildRegistrationPanel();
		mainLayout.addComponent(registrationPanel);
		mainLayout.addComponent(intimationDetailCarousel);
		viewDetails.initView(newIntimationDto.getIntimationId(), ViewLevels.OMP, false,"Claim Reg");
		mainLayout.addComponent(viewDetails);
		mainLayout.addComponent(commonButtonsLayout());
		//mainLayout.setComponentAlignment(viewDetails, Alignment.TOP_CENTER);

//			FormLayout currencyDetailsForm = buildCurrencyFormLayout();
//			registrationDetailsLayout.addComponent(currencyDetailsForm);

			tabsLayout.setMargin(true);
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
		submitButtonLayout.setWidth("100%");
		submitButtonLayout.setSpacing(true);
		submitButtonLayout.setMargin(true);
		submitButtonLayout.setComponentAlignment(buttonLayout1,
				Alignment.MIDDLE_CENTER);
		registrationDetailsLayout.addComponent(submitButtonLayout);

		mainLayout.addComponent(registrationDetailsLayout);
		mainPanel.setWidth("100%");
		mainPanel.setHeight("620px");
		mainPanel.setContent(mainLayout);

		return mainPanel;
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
	  protected Collection<Boolean> getReadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
			coordinatorValues.add(true);
			coordinatorValues.add(false);
			
			return coordinatorValues;
		}
	  private HorizontalLayout commonButtonsLayout()
		{
			
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
			
			if(this.searchDTO.getClaimedAmount() != null){
				claimedAmtTxt.setValue(String.valueOf(this.searchDTO.getClaimedAmount().longValue()));
				claimedAmtTxt.setEnabled(false);
			}
			claimTypeOption = binder.buildAndBind("","claimTypeBoolean",OptionGroup.class);
//			claimTypeOption = new OptionGroup("");
			claimTypeOption.setNullSelectionAllowed(true);
			claimTypeOption.addItems(getReadioButtonOptions());
			claimTypeOption.setItemCaption(true, "Cashless");
			claimTypeOption.setItemCaption(false, "Reimbursement");
			claimTypeOption.setStyleName("horizontal");
			
			/*if(this.claimDto!=null && this.claimDto.getClaimTypeBoolean()!=null){
				if(this.claimDto.getClaimTypeBoolean()){
					enableDisableNonHospitalisationFields(true);
				}else{
					enableDisableNonHospitalisationFields(false);
				}
			}*/
			//claimTypeOption.select(true);
//			//Vaadin8-setImmediate() claimTypeOption.setImmediate(true);
//			if(claimDto.getClaimType()!= null && claimDto.getClaimType().equals("Cashless")) {
//				claimTypeOption.setValue(true);
////				bean.setClaimId(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
//				
//			} else
//			{
//				claimTypeOption.setValue(false);
////				bean.setClaimId(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
//			}
			
//			if(claimTypeOption.getValue() != null){
//				SelectValue claimTypeSelection = new SelectValue();
//				if(String.valueOf(claimTypeOption.getValue()).equals("Cashless")){
//					claimTypeSelection.setValue("Cashless");
//					claimTypeSelection.setId(401L);
//				}else{
//					claimTypeSelection.setValue("Reimbursement");
//					claimTypeSelection.setId(402L);
//				}
//				this.searchDTO.setCmbclaimType(claimTypeSelection);
//			}
			
			CSValidator intialProamtTxtValidator = new CSValidator();
			txtInitialProvisionAmt = binder.buildAndBind("Initial Provision Amt($)","dollarInitProvisionAmount",TextField.class);
			txtInitialProvisionAmt.setRequired(true);
			//Vaadin8-setImmediate() txtInitialProvisionAmt.setImmediate(false);
			txtInitialProvisionAmt.setWidth("160px");
			txtInitialProvisionAmt.setHeight("-1px");
			intialProamtTxtValidator.extend(txtInitialProvisionAmt);
			intialProamtTxtValidator.setRegExp("^[0-9.]*$");
			intialProamtTxtValidator.setPreventInvalidTyping(true);
			CSValidator inrConvRateTxtValidator = new CSValidator();
			txtINRConversionRate = binder.buildAndBind("INR Conversion Rate","inrConversionRate",TextField.class);
			txtINRConversionRate.setRequired(true);
			//Vaadin8-setImmediate() txtINRConversionRate.setImmediate(false);
			txtINRConversionRate.setWidth("160px");
			txtINRConversionRate.setHeight("-1px");
			inrConvRateTxtValidator.extend(txtINRConversionRate);
			inrConvRateTxtValidator.setRegExp("^[0-9.]*$");
			inrConvRateTxtValidator.setPreventInvalidTyping(true);

			txtINRValue = binder.buildAndBind("INR Value","inrTotalAmount",TextField.class);
			txtINRValue.setReadOnly(true);
			//Vaadin8-setImmediate() txtINRValue.setImmediate(false);
			txtINRValue.setWidth("160px");
			txtINRValue.setHeight("-1px");
	
				
			cmbEventCode = binder.buildAndBind("Event Code","eventCodeValue",ComboBox.class);
//			if(this.searchDTO.getType() == null){
			BeanItemContainer<SelectValue> eventCode = masterService.getListMasterEventByProduct(newIntimationDto.getPolicy().getProduct().getKey());
			cmbEventCode.setContainerDataSource(eventCode);
			cmbEventCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbEventCode.setItemCaptionPropertyId("value");
//			}
			//Vaadin8-setImmediate() cmbEventCode.setImmediate(false);
			cmbEventCode.setWidth("160px");
			cmbEventCode.setHeight("-1px");
			cmbEventCode.setValue(searchDTO.getType());
//			if (this.searchDTO.getType() != null){
//				cmbEventCode.setValue((this.searchDTO.getType()));
//			}
			
			FormLayout firstForm = new FormLayout(claimTypeOption,txtInitialProvisionAmt,txtINRConversionRate,txtINRValue,cmbEventCode);
		
			
//			hospitalOption = new OptionGroup("");
			hospitalOption = binder.buildAndBind("","hospitalTypeBoolean",OptionGroup.class);
			hospitalOption.setStyleName("horizontal");
			//Vaadin8-setImmediate() hospitalOption.setImmediate(false);
			hospitalOption.setNullSelectionAllowed(true);
			hospitalOption.addItems(getReadioButtonOptions());
			hospitalOption.setItemCaption(true, "Hospitalisation");
			hospitalOption.setItemCaption(false, "Non Hospitalisation");
			
//			if((searchDTO.getHospitalisationFlag()!= null && searchDTO.getHospitalisationFlag().equals("Y"))&&(searchDTO.getNonHospitalisationFlag()!= null && searchDTO.getNonHospitalisationFlag().equals("N"))) {
//				hospitalOption.setValue(true);
//			} 
//			else{
//				hospitalOption.setValue(false);
//			}
//			if(String.valueOf(hospitalOption.getValue()).equals("Hospitalisation")){
//				this.searchDTO.setHospitalisationFlag("Y");
//				this.searchDTO.setNonHospitalisationFlag("N");
//			}else{
//				this.searchDTO.setHospitalisationFlag("N");
//				this.searchDTO.setNonHospitalisationFlag("Y");
//			}
			
			txtHospitalName = binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
			//Vaadin8-setImmediate() txtHospitalName.setImmediate(false);
			txtHospitalName.setWidth("160px");
			txtHospitalName.setHeight("-1px");

			txtHospitalCity = binder.buildAndBind("Hospital City","cityName",TextField.class);
			//Vaadin8-setImmediate() txtHospitalCity.setImmediate(false);
			txtHospitalCity.setWidth("160px");
			txtHospitalCity.setHeight("-1px");

			cmbHospitalCountry = binder.buildAndBind("Hospital Country","hospitalCountry",ComboBox.class);
			BeanItemContainer<SelectValue> country = masterService.getCountryValue();
			cmbHospitalCountry.setContainerDataSource(country);
			cmbHospitalCountry.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbHospitalCountry.setItemCaptionPropertyId("value");
			
	
//			if (this.searchDTO.getHospitalCountry() != null){
//				cmbHospitalCountry.setValue((this.searchDTO.getHospitalCountry()));
////				cmbHospitalCountry.setEnabled(false);
//			}
//		
			
			txtAilmentOrLoss = binder.buildAndBind("Ailment/Loss","ailmentLoss",TextField.class);
			//Vaadin8-setImmediate() txtAilmentOrLoss.setImmediate(false);
			txtAilmentOrLoss.setWidth("160px");
			txtAilmentOrLoss.setHeight("-1px");


			 mandatoryFields.add(txtInitialProvisionAmt);
			 mandatoryFields.add(txtINRConversionRate);

			 showOrHideValidation(false);
			FormLayout secondForm = new FormLayout(hospitalOption,txtHospitalName,txtHospitalCity,cmbHospitalCountry,txtAilmentOrLoss);
			HorizontalLayout alignmentHLayout = new HorizontalLayout(firstForm,secondForm);
			if(this.claimDto!=null && this.claimDto.getHospitalTypeBoolean()!=null){
				if(this.claimDto.getHospitalTypeBoolean()){
					enableDisableNonHospitalisationFields(true);
				}else{
					enableDisableNonHospitalisationFields(false);
				}
			}
			
			return alignmentHLayout;
			
		}
	  
	  public void enableDisableNonHospitalisationFields(boolean enable){
			
			if(!enable){
				txtHospitalName.setValue("");
				txtHospitalCity.setValue("");
				cmbHospitalCountry.setValue("");
			}
			txtHospitalName.setEnabled(enable);
			txtHospitalCity.setEnabled(enable);
			cmbHospitalCountry.setEnabled(enable);
		}

	  protected void addListener() {

		  txtInitialProvisionAmt
					.addBlurListener(new BlurListener() {
						
						@Override
						public void blur(BlurEvent event) {
							TextField txtInitialProvisionAmt1 = (TextField) event.getComponent();
							if(txtInitialProvisionAmt1!=null && txtInitialProvisionAmt1.getValue()!=null && txtINRConversionRate!=null && txtINRConversionRate.getValue()!=null){
								String initialProvisionAmt = txtInitialProvisionAmt1.getValue();
								String iNRConversionRate = txtINRConversionRate.getValue();
								double calculateInrValue = calculateInrValue(initialProvisionAmt, iNRConversionRate);
								txtINRValue.setReadOnly(false);
								txtINRValue.setValue(String.valueOf(calculateInrValue));
								txtINRValue.setReadOnly(true);
							}
							
						}
					}
							
							
							
							/*new Property.ValueChangeListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							TextField txtInitialProvisionAmt1 = (TextField) event.getProperty();
							if(txtInitialProvisionAmt1!=null && txtInitialProvisionAmt1.getValue()!=null && txtINRConversionRate!=null && txtINRConversionRate.getValue()!=null){
								String initialProvisionAmt = txtInitialProvisionAmt1.getValue();
								String iNRConversionRate = txtINRConversionRate.getValue();
								double calculateInrValue = calculateInrValue(initialProvisionAmt, iNRConversionRate);
								txtINRValue.setReadOnly(false);
								txtINRValue.setValue(String.valueOf(calculateInrValue));
								txtINRValue.setReadOnly(true);
							}
							
						}

						
					}*/);
		  
		  txtINRConversionRate
			.addBlurListener(new BlurListener() {
				
				@Override
				public void blur(BlurEvent event) {
					TextField txtInitialProvisionAmt1 = (TextField) event.getComponent();
					if(txtInitialProvisionAmt1!=null && txtInitialProvisionAmt1.getValue()!=null && txtInitialProvisionAmt!=null && txtInitialProvisionAmt.getValue()!=null){
						String initialProvisionAmt = txtInitialProvisionAmt1.getValue();
						String iNRConversionRate = txtInitialProvisionAmt.getValue();
						double calculateInrValue = calculateInrValue(initialProvisionAmt, iNRConversionRate);
						txtINRValue.setReadOnly(false);
						txtINRValue.setValue(String.valueOf(calculateInrValue));
						txtINRValue.setReadOnly(true);
					}
					
				}

				
			});
		  
		  if(claimTypeOption!=null){
			  claimTypeOption.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 7455756225751111662L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						OptionGroup totalValue = (OptionGroup) event.getProperty();
						Boolean value = (Boolean) totalValue.getValue();
						if(totalValue.getValue()!=null && totalValue.getValue().equals(true)){
								if(hospitalOption!=null){
									hospitalOption.setValue(true);
								}
						}
						if(totalValue.getValue()!=null && totalValue.getValue().equals(false)){
							if(hospitalOption!=null){
								hospitalOption.setValue(false);
							}
					}
						if(value != null && value.equals(true)){
							enableDisableNonHospitalisationFields(true);
						}else{
							enableDisableNonHospitalisationFields(false);
						}
					/*	if(value != null && value.equals(true) && hospitalOption!= null && hospitalOption.getValue() != null && hospitalOption.getValue().equals(false)){
							enableDisableNonHospitalisationFields(true);
						}else{
							if(value != null && value.equals(false) && hospitalOption!= null && hospitalOption.getValue() != null && hospitalOption.getValue().equals(true)){
								enableDisableNonHospitalisationFields(true);
							}
						}*/
					}
				});
			  }
		  
		  
		  if(hospitalOption!=null){
			  hospitalOption.addBlurListener(new BlurListener() {
					
					@Override
					public void blur(BlurEvent event) {
						OptionGroup totalValue = (OptionGroup) event.getComponent();
						Boolean value = (Boolean) totalValue.getValue();
						if(totalValue.getValue()!=null && totalValue.getValue().equals(true) && claimTypeOption!= null && claimTypeOption.getValue() != null && claimTypeOption.getValue().equals(false) ){
							enableDisableNonHospitalisationFields(true);
						}else{
						if(totalValue.getValue()!=null && totalValue.getValue().equals(false) && claimTypeOption!= null && claimTypeOption.getValue() != null && claimTypeOption.getValue().equals(true))
							enableDisableNonHospitalisationFields(true);
					}
						if(totalValue.getValue()!=null && totalValue.getValue().equals(false) && claimTypeOption!= null && claimTypeOption.getValue() != null && claimTypeOption.getValue().equals(false) ){
							enableDisableNonHospitalisationFields(false);
						}
					}
				});
			  }
	  }
	  
	  private double calculateInrValue(
				String initialProvisionAmt,
				String iNRConversionRate) {
		  
		  NumberFormat format = NumberFormat.getInstance(Locale.US);
		  double iNRValue =0d;

	       Number number;
	       Number number1;
		try {
			number = format.parse(initialProvisionAmt);
			number1 = format.parse(iNRConversionRate);
		  double initialProvisionAmtDouble = number.doubleValue();
		  double iNRConversionRateDouble = number1.doubleValue();
		  iNRValue =(initialProvisionAmtDouble * iNRConversionRateDouble);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 	iNRValue;
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
								OMPClaimRegistrationWizardPresenter.SUBMIT_CLAIM_CLICK,
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
					
					VaadinSession session = getSession();
					SHAUtils.releaseHumanTask(registerationBean.getUsername(), registerationBean.getPassword(), registerationBean.getTaskNumber(),session);

					fireViewEvent(
							OMPClaimRegistrationWizardPresenter.CANCEL_CLAIM_REGISTRATION,
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
				claimDto.setStatusName("Registered");
				fireViewEvent(OMPClaimRegistrationWizardPresenter.CLICK_REGISTER_BUTTON,
						null);
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
				fireViewEvent(OMPClaimRegistrationWizardPresenter.SUGGEST_REJECTION,
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

		panelCaption.addStyleName(ValoTheme.PANEL_WELL);
		panelCaption.setSpacing(true);
		panelCaption.setWidth("100%");
		panelCaption.setMargin(new MarginInfo(false, true, false, true));
		Label captionLbl = new Label(caption);
		panelCaption.addComponent(captionLbl);
		// panelCaption.setHeight("30px"); //for testing purpose

//			vipChk = new CheckBox("Flag as VIP");
//			panelCaption.addComponent(vipChk);
//			panelCaption.setComponentAlignment(vipChk, Alignment.TOP_RIGHT);
		
		//Vaadin8-setImmediate() registrationPanel.setImmediate(false);
		registrationPanel.setWidth("100%");
		// registrationPanel.setHeight("130px");
		registrationPanel.addStyleName("panelHeader");

		intimationDetailCarousel = carouselInstance.get();
		intimationDetailCarousel.initOMPCarousal(newIntimationDto, "");
		intimationDetailCarousel.addOmpTpaIntimation(newIntimationDto);
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.addComponent(panelCaption);
		vlayout.addComponent(intimationDetailCarousel);
		vlayout.setStyleName("policygridinfo");
		registrationPanel.setContent(vlayout);

		return registrationPanel;
	}

	

//	private FormLayout buildCurrencyFormLayout() {
////		// common part: create layout
//		FormLayout currencyDetailsForm = new FormLayout();
//		//Vaadin8-setImmediate() currencyDetailsForm.setImmediate(false);
//		currencyDetailsForm.setMargin(false);
//		currencyDetailsForm.setSpacing(true);
//
//		// nativeSelect_1
//		currencyNameSelect = new ComboBox();
//		currencyNameSelect.setCaption("Currency Name");
//		//Vaadin8-setImmediate() currencyNameSelect.setImmediate(false);
//		currencyNameSelect.setWidth("160px");
//		currencyNameSelect.setHeight("-1px");
//		currencyNameSelect.setNullSelectionAllowed(false);
//		fireViewEvent(ClaimRegistrationWizardPresenter.GET_CURRENCY_MASTER, null);
//		currencyNameSelect.setEnabled(false);
//
//		currencyNameSelect.setContainerDataSource(currencyMasterContainer);
//		currencyNameSelect.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		currencyNameSelect.setItemCaptionPropertyId("value");
//		// Set Default Value to First Option.
//		Collection<?> itemIds = currencyNameSelect.getContainerDataSource()
//				.getItemIds();
////		currencyNameSelect.setValue(itemIds.toArray()[2]);
//
//		currencyNameSelect
//				.addValueChangeListener(new Property.ValueChangeListener() {
//					private static final long serialVersionUID = -4820170898280727113L;
//
//					public void valueChange(ValueChangeEvent valueChangeEvent) {
//						SelectValue masterValue = (SelectValue) valueChangeEvent
//								.getProperty().getValue();
//						if (claimedAmtTxt != null) {
//							claimedAmtTxt.setCaption("Amount Claimed (INR) "
//									+ masterValue.getValue());
//							//Vaadin8-setImmediate() currencyNameSelect.setImmediate(true);
//						}
//					}
//				});
//		//Vaadin8-setImmediate() currencyNameSelect.setImmediate(true);
//		currencyDetailsForm.addComponent(currencyNameSelect);
//
//		CSValidator claimedAmtTxtValidator = new CSValidator();
//		claimedAmtTxt = new TextField();
//		claimedAmtTxt.setCaption("Amount Claimed (INR) ");
//		//Vaadin8-setImmediate() claimedAmtTxt.setImmediate(false);
//		claimedAmtTxt.setWidth("160px");
//		claimedAmtTxt.setHeight("-1px");
//		claimedAmtTxt.setRequired(true);
//		claimedAmtTxt.setRequiredError("Please enter Claimed Amount.");
//		claimedAmtTxt.setValidationVisible(false);
//		claimedAmtTxt.setMaxLength(13);
//		claimedAmtTxtValidator.extend(claimedAmtTxt);
//		claimedAmtTxtValidator.setRegExp("^[0-9.]*$");
//		claimedAmtTxtValidator.setPreventInvalidTyping(true);
//		
//		if(this.searchDTO.getClaimedAmount() != null){
//			claimedAmtTxt.setValue(String.valueOf(this.searchDTO.getClaimedAmount().longValue()));
//			claimedAmtTxt.setEnabled(false);
//		}
//
//		currencyDetailsForm.addComponent(claimedAmtTxt);
//		
////
//		return currencyDetailsForm;
//	}

//	public void setCurrencyMaster(BeanItemContainer<SelectValue> currencyMaster) {
//		currencyMasterContainer = currencyMaster;
//	}

	private VerticalLayout buildTabsLayout() {
		// common part: create layout
		VerticalLayout tabsLayout = new VerticalLayout();
		//Vaadin8-setImmediate() tabsLayout.setImmediate(false);
		 tabsLayout.setWidth("100%");
		 tabsLayout.setHeight("100%");
		tabsLayout.setSizeFull();
		tabsLayout.setMargin(true);

		// tabSheet_1
		TabSheet previousClaimTab = buildClaimTabs();
		tabsLayout.addComponent(previousClaimTab);

		if (registerationBean != null
				&& registerationBean.getNewIntimationDto() != null) {
			NewIntimationDto intimationDto = registerationBean.getNewIntimationDto();
			fireViewEvent(OMPClaimRegistrationWizardPresenter.GET_PREVIOUS_CLAIMS,
					intimationDto);
		}

		return tabsLayout;
	}

	private TabSheet buildClaimTabs() {
		TabSheet previousClaimTab = new TabSheet();
		//Vaadin8-setImmediate() previousClaimTab.setImmediate(true);
		// previousClaimTab.setWidth("100.0%");
		// previousClaimTab.setHeight("100.0%");
		previousClaimTab.setSizeFull();
		previousClaimTab.setStyleName(ValoTheme.TABSHEET_FRAMED);

		TabSheet previousClaimSheet = buildPreviouClaimTable();
		previousClaimTab.setHeight("100.0%");
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

		VerticalLayout previousClaimTableLayout = new VerticalLayout();

		previousClaimTableLayout.setHeight("195px");
		previousClaimTableLayout.setWidth("100%");
		previousClaimTableLayout.setMargin(true);
		previousClaimTableLayout.setSpacing(true);

		previousClaimTableLayout.addComponent(previousClaimsRgistration);

		previousClaimLayout.setHeight("200px");

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


		VerticalLayout balanceSIVerticalLayout = new VerticalLayout();
		
		balanceSIVerticalLayout.setHeight("195px");
		balanceSIVerticalLayout.setWidth("100%");
		balanceSIVerticalLayout.setMargin(true);
		balanceSIVerticalLayout.setSpacing(true);
		
//		balanceSiTable = balanceSiTableInstance.get();
//		balanceSiTable.init("", false, false);
//	//	sublimtListTable.setTableList(resultSublimitList);
//	
		
		balanceSIComponent = ompBalanceSiFormInstance
				.bindFieldGroup(newIntimationDto.getIntimationId(),null,null);

		balanceSIVerticalLayout.addComponent(balanceSIComponent);
		
	//	balanceSIComponentInstance.init("", false, false);
	//	balanceSIVerticalLayout.addComponent(hLayout);	
	//	balanceSIVerticalLayout.addComponent(balanceSIComponentInstance);
		balanceSIVerticalLayout.setHeight("200px");
		balanceSITab.addComponent(balanceSIVerticalLayout);
	
		return balanceSITab;
	}



	private VerticalLayout buildRegisterFields() {

		//	buildProvisionAmountField();
		

			if (registrationRemarksTxta == null) {
				registrationRemarksTxta = new TextArea("Registration Remarks");
				registrationRemarksTxta.setRequired(true);//
				registrationRemarksTxta.setValidationVisible(false);//
				registrationRemarksTxta.setMaxLength(200);
				registrationRemarksTxta.setRequiredError("Please Provide Suggesion Remarks for Registration");
				mandatoryFields.add(registrationRemarksTxta);
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

//	private void buildProvisionAmountField() {
//		CSValidator provisionalAmtValidator = new CSValidator();
//		provisionalAmtTxt = new TextField();
//		provisionalAmtTxt.setCaption("Provision Amount");
//		//Vaadin8-setImmediate() provisionalAmtTxt.setImmediate(false);
//		provisionalAmtTxt.setWidth("-1px");
//		provisionalAmtTxt.setHeight("-1px");
//		provisionalAmtTxt.setMaxLength(13);
//		provisionalAmtTxt.setRequired(true);
//		provisionalAmtTxt.setRequiredError("Please enter Provision Amount");
//		provisionalAmtValidator.extend(provisionalAmtTxt);
//		provisionalAmtValidator.setRegExp("^[0-9]*$");
//		provisionalAmtValidator.setPreventInvalidTyping(true);
//		provisionalAmtTxt.setValidationVisible(false);
//
//
//		if (tmpCpuCode != null) {
//			
//			if (tmpCpuCode.getProvisionAmount() != null) {
//				provisionamount = tmpCpuCode.getProvisionAmount();
//				System.out.println("provision amt====================="+provisionamount);
//				System.out.println("Balance SI amt====================="+balanceSumInsured);
//				
//				
//				Double claimedAmount = claimedAmtTxt.getValue() != null ? Double.valueOf(claimedAmtTxt.getValue()) : 0d;
//				
//				System.out.println("Claimed amt======================"+claimedAmount);
//				
//				if(balanceSumInsured != null && balanceSumInsured>0){
//					if(balanceSumInsured > claimedAmount){
//						provisionamount = claimedAmount;
//						provisionalAmtTxt.setValue(provisionamount.toString());
//				}
//				else {
//					provisionalAmtTxt.setValue(balanceSumInsured.toString());
//				}					
//					
//				} else{
//					provisionalAmtTxt.setValue("0");
//				}
//			}
//			
//			System.out.println("final provision amount --------------------------------"+provisionalAmtTxt.getValue());
//			claimDto.setProvisionHomeAmount(Double.valueOf(provisionalAmtTxt.getValue()));
//			claimDto.setProvisionAmount(Double.valueOf(provisionalAmtTxt.getValue()));
//
//			if (currencyNameSelect.getValue() != null) {
//				claimDto.setCurrencyId((SelectValue) currencyNameSelect
//						.getValue());
//			}
//			
//		}
//		provisionalAmtTxt.addTextChangeListener(new TextChangeListener() {
//
//			@Override
//			public void textChange(TextChangeEvent event) {
//				if (provAmtTxt != null) {
//					provAmtTxt.setReadOnly(false);
//					provAmtTxt.setValue(event.getText());
//					provAmtTxt.setReadOnly(true);
//				}
//			}
//		});
//		dynamicFrmLayout.removeAllComponents();
//		dynamicFrmLayout.addComponent(provisionalAmtTxt);
//	}

	private FormLayout buildSuggestRejectionFields(FormLayout dynamicFrmLayout) {
		claimDto.setSuggestRejection(true);

		if (dynamicFrmLayout == null) {
			dynamicFrmLayout = new FormLayout();
		}
//		if (provisionalAmt == null && dynamicFrmLayout.getComponentCount() < 0) {
//			provisionalAmt = new TextField("Provision Amount INR  ");
//			provisionalAmt.setValue("0");
//			dynamicFrmLayout.addComponent(provisionalAmt);
//		}
		if (dynamicFrmLayout.getComponentCount() > 0) {
			if (registrationRemarksTxta != null) {
				dynamicFrmLayout.removeComponent(registrationRemarksTxta);
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
		mandatoryFields.add(suggestRejectionTxta);
		dynamicFrmLayout.addComponent(suggestRejectionTxta);

		return dynamicFrmLayout;
	}

	public void registerClick() {

//		boolean hasError = validateClaimdeAmount();
//		if(!hasError)
//		{
		if (dynamicFieldsLayout != null) {
			if (dynamicFieldsLayout.getComponentCount() > 0) {
				dynamicFieldsLayout.removeAllComponents();
			}

		}
			dynamicFieldsLayout = buildRegisterFields();
//		}
//		else
//		{
//			Notification.show("ERROR", "Please Fill the Manadatory Fields.",
//					Notification.Type.ERROR_MESSAGE);
//		}
		
	}

	public void suggestRejectionClick() {		
		claimDto.setSuggestRejection(true);
		if (dynamicFieldsLayout != null) {
			if (dynamicFieldsLayout.getComponentCount() > 0) {
				dynamicFieldsLayout.removeAllComponents();
			}
		}
		
//		boolean hasError = validateClaimdeAmount();
//		if(!hasError){
	//		buildProvisionAmountField();
			dynamicFieldsLayout
					.addComponent(buildSuggestRejectionFields(dynamicFrmLayout));
			
//		}
//		else
//		{
//			Notification.show("ERROR", "Please Fill the Manadatory Field.",
//					Notification.Type.ERROR_MESSAGE);
//		}
		
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
							releaseWorkFlowTask();
							registerationBean = null;
							newIntimationDto = null;
//							fireViewEvent(LoaderPresenter.LOAD_URL,
//									MenuItemBean.OMP_CLAIM_REGISTRATION);
//
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
		hasError = validatePage();
	
		FormLayout frmLayout = dynamicFrmLayout;

//		hasError = validateClaimdeAmount();
		if (claimDto.getStatusName() == null && claimDto.getStatusName() == "") {
			hasError = true;
			Notification
					.show("ERROR",
							"Please Click Register Or Suggest Reject Button before Submitting the Claim. ",
							Notification.Type.ERROR_MESSAGE);
			return;
		}

		if (suggestRejectionTxta != null
				&& suggestRejectionTxta.getValue() == null) {
			Notification
					.show("ERROR",
							"Please Enter Manadatory Fields by Clicking Register Or Suggest Reject Button. ",
							Notification.Type.ERROR_MESSAGE);
			return;
		}
		
		if(!claimDto.getSuggestRejection() && !isProceedfurther) {
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

//		if (provisionalAmtTxt != null && !provisionalAmtTxt.isValid()) {
//			provisionalAmtTxt.setValidationVisible(true);
//			hasError = true;
//		}
		if (frmLayout.getComponentCount() == 3 && suggestRejectionTxta != null
				&& suggestRejectionTxta.getValue() == null) {
			suggestRejectionTxta.setValidationVisible(true);
			hasError = true;
		}

		if (suggestRejectionTxta != null) {
			suggestRejectionTxta.setValidationVisible(false);
		}

		if (hasError) {
//			Notification.show("ERROR", "Please Fill the Manadatory Fields.",
//					Notification.Type.ERROR_MESSAGE);
//			hasError = true;
			return;
		}
		if (!hasError) {
			claimDto.setNewIntimationDto(newIntimationDto);
//			Long vip = vipChk.getValue() ? 1l : 0l;
//			claimDto.setIsVipCustomer(vip);
//			claimDto.setConversionLetter(0l);
			claimDto.setStatusDate(new Timestamp(System.currentTimeMillis()));
//			claimDto.setCurrencyId((SelectValue) currencyNameSelect.getValue());

			// TODO set currency conversion and set the provision
			
			claimDto.setClaimedAmount((claimedAmtTxt.getValue() == null || claimedAmtTxt
					.getValue() == "") ? Double.valueOf("0") : Double
					.valueOf(claimedAmtTxt.getValue()));
			claimDto.setRegistrationRemarks((registrationRemarksTxta == null || registrationRemarksTxta
					.getValue() == null) ? null : registrationRemarksTxta
					.getValue());
			claimDto.setSuggestedRejectionRemarks((suggestRejectionTxta == null || suggestRejectionTxta
					.getValue() == null) ? null : suggestRejectionTxta
					.getValue());
	
//			claimDto.setDollarInitProvisionAmount((txtInitialProvisionAmt.getValue() == null || txtInitialProvisionAmt
//					.getValue() == "") ? Double.valueOf("0") : Double.valueOf(txtInitialProvisionAmt.getValue()));
////			claimDto.setNewIntimationDto(searchDTO.getNewIntimationDto());
//			claimDto.setInrConversionRate((txtINRConversionRate.getValue() == null || txtINRConversionRate
//					.getValue() == "") ? Double.valueOf("0") : Double.valueOf(txtINRConversionRate.getValue()));
//			Double amt = (Double.valueOf(txtInitialProvisionAmt.getValue()) * Double.valueOf(txtINRConversionRate.getValue()));
//			claimDto.setInrTotalAmount(amt);
//			if (frmLayout != null && frmLayout.getComponentCount() == 1
//					&& provisionalAmtTxt != null && provisionalAmtTxt.isVisible()) {
//				claimDto.setProvisionAmount((provisionalAmtTxt.getValue() != null && provisionalAmtTxt
//						.getValue() != "") ? Double.valueOf(provisionalAmtTxt
//						.getValue()) : Double.valueOf("0"));
//				claimDto.setProvisionHomeAmount((provisionalAmtTxt.getValue() != null && provisionalAmtTxt
//						.getValue() != "") ? Double.valueOf(provisionalAmtTxt
//						.getValue()) : Double.valueOf("0"));
//			}

			fireViewEvent(OMPClaimRegistrationWizardPresenter.SUBMIT_CLAIM_REGISTRATION,
					registerationBean, claimDto);
		}
	}
	
	

//	private Boolean validateClaimdeAmount() {
//		boolean hasError = false;
//		
//		if (claimedAmtTxt == null || ("").equals(claimedAmtTxt.getValue())){
//			hasError = true;
//		}
//		else if(claimedAmtTxt != null && !("").equalsIgnoreCase(claimedAmtTxt.getValue())){
//			if(!(Integer.valueOf(claimedAmtTxt.getValue()) >= 0)){
//					
//					claimedAmtTxt.setValidationVisible(true);
//					hasError = true;
//				}
//			}	
	
//		if(txtInitialProvisionAmt == null ||("0").equalsIgnoreCase(txtInitialProvisionAmt.getValue())){
//			hasError = true;
//		}else 
//			if(txtInitialProvisionAmt != null && !("0").equalsIgnoreCase(txtInitialProvisionAmt.getValue())){
//			if(!(SHAUtils.getDoubleValueFromString(txtInitialProvisionAmt.getValue()) >=0)){
//			txtInitialProvisionAmt.setValidationVisible(true);
//			hasError = true;
//	   			}
//		}
//		if(txtINRConversionRate == null ||("0").equalsIgnoreCase(txtINRConversionRate.getValue())){
//			hasError = true;
//		}else 
//			if(txtINRConversionRate != null && !("0").equalsIgnoreCase(txtINRConversionRate.getValue())){
//			if(!(SHAUtils.getDoubleValueFromString(txtINRConversionRate.getValue()) >=0)){
//			txtINRConversionRate.setValidationVisible(true);
//			hasError = true;
//		}
//	}		
//		if(!(null != this.txtInitialProvisionAmt && null != this.txtInitialProvisionAmt.getValue() && !("").equalsIgnoreCase(this.txtInitialProvisionAmt.getValue())))
//			hasError = true;
//		if(!(null != this.txtINRConversionRate && null != this.txtINRConversionRate.getValue() && !("").equalsIgnoreCase(this.txtINRConversionRate.getValue())))
//			hasError = true;
//
//		return hasError;
//	}

	private FormLayout buildBasicInfoLayout(OMPClaim claim) {
//	private FormLayout buildBasicInfoLayout() {
		String value = "";
		if (claim != null) {

//			if (claim.getCurrencyId() != null) {
//				value = claim.getCurrencyId().getValue();
//			}

		}
//		claim.setClaimedAmount(this.searchDTO.getInrValue());
		if(claim.getCurrencyId() != null){
		value = claim.getCurrencyId().getValue();
		}
		FormLayout formFieldsLayout = new FormLayout();


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

		return formFieldsLayout;
	}

	private VerticalLayout buildRegisterSuccessLayout(OMPClaim claim) {
//		if (provisionalAmtTxt != null) {
//			dynamicFrmLayout.removeComponent(provisionalAmtTxt);
//		}
		FormLayout formFieldsLayout = buildBasicInfoLayout(claim);
//		FormLayout formFieldsLayout = buildBasicInfoLayout();

		HorizontalLayout buttonLayoutForSuccess = new HorizontalLayout();
					

		if (registrationRemarksTxta == null) {
			registrationRemarksTxta = new TextArea("Registration Remarks *");
		}
		registrationRemarksTxta.setValue(claimDto.getRegistrationRemarks());
		registrationRemarksTxta.setReadOnly(true);
		registrationRemarksTxta
				.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		registrationRemarksTxta.setHeight("-1px");
		TextField rejectionTxt = new TextField(" Rejection Remarks");
		
			rejectionTxt.setValue(claimDto.getSuggestedRejectionRemarks());//
		formFieldsLayout.addComponent(registrationRemarksTxta);
//		formFieldsLayout.addComponent(rejectionTxt);
		
		TextField suggestRejectionTxt = new TextField("Suggest Rejection");
			if (claimDto.getSuggestRejection()) {

				if (registrationRemarksTxta != null) {
					formFieldsLayout.removeComponent(registrationRemarksTxta);
				}
//				suggestRejectionTxt
//						.setValue(claimDto.getSuggestRejection() ? "Yes" : "No");
//				suggestRejectionTxt.setReadOnly(true);
//				formFieldsLayout.addComponent(suggestRejectionTxt);

				if (suggestRejectionTxta == null) {
					suggestRejectionTxta = new TextArea(
							"Suggested Rejection Remarks *");
				}
				suggestRejectionTxta.setValue(claimDto
						.getSuggestedRejectionRemarks());
				suggestRejectionTxta.setReadOnly(true);
				suggestRejectionTxta.setHeight("-1px");
				suggestRejectionTxta
						.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				
//				formFieldsLayout.addComponent(registrationRemarksTxta);
				formFieldsLayout.addComponent(suggestRejectionTxta);	
		}
			
			claimNumber = new TextField("Claim Number");
			claimNumber.setValue(claim.getClaimId());
			claimNumber.setReadOnly(true);
			claimNumber.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			claimNumber.setWidth("200px");
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

//				fireViewEvent(LoaderPresenter.LOAD_URL,
//						MenuItemBean.OMP_CLAIM_REGISTRATION);
				
			}
		});
	
		
		//Vaadin8-setImmediate() homePageButton.setImmediate(true);
		
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

		
		return layout;
	}

	public void openPdfFileInWindow(final String filepath) {
		Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("Claim Form Covering Letter PDF");
		window.setWidth("800");
		window.setHeight("600");
		window.setModal(true);
		window.center();

		Path p = Paths.get(filepath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public InputStream getStream() {
				try {

					File f = new File(filepath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		window.setContent(e);
		UI.getCurrent().addWindow(window);
	}

	public void setPreviousClaimsDtoList(
			List<OMPPreviousClaimTableDTO> previousClaimDtoList) {
		if (null != previousClaimDtoList && !previousClaimDtoList.isEmpty()) {
			previousClaimsRgistration.setTableList(previousClaimDtoList);
		}
	}

	public void setBalanceSumInsured(Double balanceSI){
		balanceSumInsured = balanceSI;
		System.out.println("Balance SI In UI Screen ============================="+balanceSI);
	}	
	
	public void setCpuObject(TmpCPUCode tmpCpu) {

		tmpCpuCode = new TmpCPUCode();

		if (tmpCpu != null) {
			tmpCpuCode.setKey(tmpCpu.getKey());
			tmpCpuCode.setCpuCode(tmpCpu.getCpuCode());
			tmpCpuCode.setDescription(tmpCpu.getDescription());
			tmpCpuCode.setProvisionAmount(tmpCpu.getProvisionAmount());
		}
	}

//	public void setSublimitList(List<SublimitFunObject> sublimitList) {
//
//		resultSublimitList = sublimitList;
//	}

	public void setClaimDetails(ClaimDto newClaimDto) {
		claimDto.setClaimId(newClaimDto.getClaimId());
		claimDto.setKey(newClaimDto.getKey());
		claim =  OMPClaimMapper.getInstance().getClaim(newClaimDto);

		if (claim != null) {
			registrationDetailsLayout.removeComponent(registerBtnLayout);
			registrationDetailsLayout.removeComponent(submitButtonLayout);
			registrationDetailsLayout
					.addComponent(buildRegisterSuccessLayout(claim));
//			registrationDetailsLayout
//			.addComponent(buildRegisterSuccessLayout());
		}
	}
	
	private void releaseWorkFlowTask(){
		VaadinSession session = getSession();
		Long wrkFlowKey=(Long)session.getAttribute(SHAConstants.WK_KEY);
		DBCalculationService dbService = new DBCalculationService();
 		if(wrkFlowKey != null) {
 			dbService.callOMPUnlockProcedure(wrkFlowKey);
 			getSession().setAttribute(SHAConstants.WK_KEY, null);
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
	  
	  public boolean validatePage() {
			Boolean hasError = false;
			showOrHideValidation(true);
			String eMsg = "";
			if (!this.binder.isValid()) {
			    for (Field<?> field : this.binder.getFields()) {
			    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
			    	if (errMsg != null) {
			    		eMsg += errMsg.getFormattedHtmlMessage();
			    	}
			    	hasError = true;
			    }
			 } 
						
			if(!(null != this.txtInitialProvisionAmt && null != this.txtInitialProvisionAmt.getValue() && !("").equalsIgnoreCase(this.txtInitialProvisionAmt.getValue())))
			{
				hasError = true;
				eMsg += "Please enter Provision Amount </br>";
			}
//			else{
//				bean.setDollarInitProvisionAmount(Double.valueOf(txtInitialProvisionAmt.getValue()));
//			}
			
			if(!(null != this.txtINRConversionRate && null != this.txtINRConversionRate.getValue() && !("").equalsIgnoreCase(this.txtINRConversionRate.getValue())))
			{
				hasError = true;
				eMsg += "Please enter INR Conversion Rate </br>";
			}
			
			if(!(null != this.txtInitialProvisionAmt && null != this.txtInitialProvisionAmt.getValue() && !("").equalsIgnoreCase(this.txtInitialProvisionAmt.getValue())))
			{
				hasError = true;
				eMsg += "Please enter Initial Provision Amt</br>";
			}
						
			
//			if(hospitalOption.getValue() != null && (boolean)hospitalOption.getValue() ){
			if(hospitalOption.getValue() != null && hospitalOption.getValue().equals(true) && claimTypeOption.getValue()!= null && claimTypeOption.getValue().equals(true) ){
				
				if(txtHospitalName.getValue() == null || txtHospitalName.getValue().isEmpty()){
					hasError = true;
					eMsg += "Please enter Hospital Name </br>";
				}
				if(txtHospitalCity.getValue() == null || txtHospitalCity.getValue().isEmpty()){
					hasError = true;
					eMsg += "Please enter Hospital City </br>";
				}
				if(cmbHospitalCountry.getValue() == null){
					hasError = true;
					eMsg += "Please Select County Value</br>";
				}
				if(txtAilmentOrLoss.getValue() == null || txtAilmentOrLoss.getValue().isEmpty()){
					hasError = true;
					eMsg += "Please enter Ailment / Loss </br>";
				}
			}			

			if(("Registered").equalsIgnoreCase(claimDto.getStatusName())){
				if(registrationRemarksTxta.getValue() == null ||  registrationRemarksTxta.getValue().isEmpty()){
					hasError = true;
					eMsg += "Please enter Registration Remarks</br>";
				}
			}
			
			if(("SuggestRejection").equalsIgnoreCase(claimDto.getStatusName())){
				if(suggestRejectionTxta.getValue() == null ||  suggestRejectionTxta.getValue().isEmpty()){
					hasError = true;
					eMsg += "Please enter Suggest Rejection Remarks</br>";
				}
			}			
			
//			else{
//				bean.setInrConversionRate(Double.valueOf(txtINRConversionRate.getValue()));
//			}
//				bean.setHospitalName(txtHospitalName.getValue());
//				bean.setCityName(txtHospitalCity.getValue());
//				bean.setAilmentLoss(txtAilmentOrLoss.getValue());
			   if(hasError) {
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
				    
				    return hasError;
			   } else {
					try {
						this.binder.commit();
									
					} catch (CommitException e) {
						e.printStackTrace();
					}
				   showOrHideValidation(false);
				   return false;
			   }
		}
	  
//		public Boolean isValid() {
//			Boolean isValid = true;
//			String eMsg = "";
//			errorList.removeAll(errorList);
//			try {
//				if(this.binder.isValid()) {
//					this.binder.commit();
//					if(this.searchDTO != null)
//					{
//									
////						if(null != patientCareList && !patientCareList.isEmpty())
////						{
////							patientCareList.clear();
////						}
////						
////						this.searchDTO.getUploadDocumentsDTO().setPatientCareDTO(this.patientCareTableObj.getValues());
////						patientCareList.addAll(this.patientCareTableObj.getValues());
//					}
//				} else {
//					isValid = false;
//					
//					for (Field<?> field : this.binder.getFields()) {
//						ErrorMessage errMsg = ((AbstractField<?>) field)
//								.getErrorMessage();
//						if (errMsg != null) {
//							eMsg += errMsg.getFormattedHtmlMessage();
//						}
//						errorList.add(eMsg);
//					}
//				}
//				
//				
//			} catch (CommitException e) {
//				e.printStackTrace();
//			}
//			return isValid; 
//		}
	  
		protected void showOrHideValidation(Boolean isVisible) {
			for (Component component : mandatoryFields) {
				AbstractField<?>  field = (AbstractField<?>)component;
				field.setRequired(!isVisible);
				field.setValidationVisible(isVisible);
			}
		}
	  
	  
}
