package com.shaic.claim.premedical.wizard.pages;

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

import org.apache.commons.lang.time.DateUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.NewClaimedAmountTable;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.PedRaisedDetailsTable;
import com.shaic.claim.preauth.PreauthCoordinatorView;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.pages.NewProcedureTableList;
import com.shaic.claim.preauth.wizard.pages.SpecialityTable;
import com.shaic.claim.preauth.wizard.pages.ViewGmcCorpBufferDetailsPage;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.dto.RevisedOtherBenifitsTable;
import com.shaic.claim.premedical.listenerTables.DiganosisDetailsListenerForPremedical;
import com.shaic.claim.premedical.listenerTables.ProcedureListenerTableForPremedical;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.tables.SectionDetailsListenerTable;
import com.shaic.claim.intimation.create.ViewBasePolicyDetails;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ZUATopViewQueryTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PreMedicalDataExtractionPage extends ViewComponent implements
		WizardStep<PreauthDTO> {
	private static final long serialVersionUID = -4185722564638053875L;

	@Inject
	private PreauthDTO bean;
	
	private GWizard wizard;
	
	////private static Window popup;

	@EJB
	private MasterService masterService;

	@Inject
	private Instance<PreauthCoordinatorView> preauthCoordinatorView;
	
	@Inject
	private Instance<ProcedureListenerTableForPremedical> procedureListenerTable;

	@Inject
	private Instance<SpecialityTable> specialityTableList;
	
	@Inject
	private Instance<DiganosisDetailsListenerForPremedical> diagnosisListnerTable;
	
	@Inject
	private Instance<SectionDetailsListenerTable> sectionDetailsListenerTable;
	
	@Inject
	private Instance<NewClaimedAmountTable> claimedAmountDetailsTable;

	@Inject
	private Instance<NewProcedureTableList> newProcedureTableList;

	private ProcedureListenerTableForPremedical procedureTableObj;

	private NewProcedureTableList newProcedurdTableObj;

	private PreauthCoordinatorView preauthCoordinatorViewInstance;
	
	private NewClaimedAmountTable claimedDetailsTableObj;

	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;

	private TextField referenceNoTxt;

	private TextField reasonForAdmissionTxt;

	private DateField admissionDate;

	private TextField noOfDaysTxt;

	private ComboBox cmbNatureOfTreatment;

	private PopupDateField firstConsultantDate;

	private CheckBox corpBufferChk;
	
	private TextField txtCorpBufferAllocatedAmt;

	private Label automaticRestorationLbl;
	
	private Label autoRestorationCountLbl;

	private CheckBox criticalIllnessChk;

	private ComboBox cmbSpecifyIllness;

	private ComboBox cmbRoomCategory;
	
	private ComboBox cmbCategory;

	private ComboBox cmbTreatmentType;

	private TextArea treatmentRemarksTxt;

	private ComboBox cmbIllness;

	private ComboBox cmbPatientStatus;

	private DateField deathDate;

	private TextField txtReasonForDeath;

	private ComboBox cmbTerminateCover;
	
	// -------------- Newly added fields --------------//
	
	private ComboBox cmbCauseOfInjury;
	
	private DateField diseaseDetectedDate;
	
	private DateField deliveryDate;
	
	private ComboBox cmbHospitalisationDueTo;
	
	private ComboBox cmbPreAuthType;
	
	private TextField firNumberTxt;

	private OptionGroup policeReportedAttached;
	
	private DateField injuryDate;
	
	private OptionGroup medicalLegalCase;
	
	private OptionGroup reportedToPolice;
	
	private ComboBox cmbTypeOfDelivery;
	
	private ComboBox cmbSection;
	
	//private ComboBox cmbCatastrophicLoss;
	
	private ComboBox cmbNatureOfLoss;
			
	private ComboBox cmbCauseOfLoss;
	
	// -------------- Newly added fields --------------//


	private FormLayout firstFLayout;

	private FormLayout secondFLayout;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private Map<String, Object> referenceData;

	VerticalLayout wholeVLayout;

	VerticalLayout tableVLayout;

	VerticalLayout benefitLayout;
	
	private SpecialityTable specialityTableObj;

	private DiganosisDetailsListenerForPremedical diagnosisListenerTableObj;
	
	private SectionDetailsListenerTable sectionDetailsListenerTableObj;

	//private Map<String, Property.ValueChangeListener> listenerMap = new HashMap<String, Property.ValueChangeListener>();
	
	private HorizontalLayout categoryHLayout;

	private HorizontalLayout dynamicElementsHLayout;
	
	private FormLayout categoryFLayout;

	private FormLayout treatmentFLayout;

	private FormLayout patientStatusFLayout;
	
	//Added for reason for date change.
	private TextField reasonForChangeInDOA;
	
	private BeanItemContainer<SelectValue> roomCategory ;
	
	private List<String> diagnosisList = new ArrayList<String>();
	
	private Boolean isValid = false;
	
	private Button corpBufferDetails;
	
	@Inject
	private ViewGmcCorpBufferDetailsPage gmcBufferDetails;

	private HorizontalLayout corpBufferHLayout;
	
	private Boolean isCorpBufferChecked = false;
	
	private OptionGroup otherBenefitsOption;

	@Inject
	private Instance<RevisedOtherBenifitsTable> befitsTableInstance;
	@Inject
	private ViewBasePolicyDetails viewBasePolicyDetail;
	
	@EJB
		private PolicyService policyService;
	 
	 @EJB
	 private IntimationService intimationService;
	
	private RevisedOtherBenifitsTable benefitsTableObj;
	
	private Button btnRTAView;
	
	private Long assistedTreatment = 0l;
	
	private TextField txtAmtClaimed;
	
	private TextField txtDiscntHospBill;
	
	private TextField txtNetAmt;
	
	/*private BeanItemContainer<State> container = new BeanItemContainer<State>(
			State.class);*/
	@Inject 
	private Instance<PedRaisedDetailsTable> pedRaiseDetailsTable;
	
	private PedRaisedDetailsTable pedRaiseDetailsTableObj;	
	MessageBox msgBox;
	
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	private FormLayout legaHeirLayout;
	
	@Override
	public String getCaption() {
		return "Data Extraction";
	}

	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
	}
	
	public Boolean alertMessageForPED(final String alertMessage) {/*
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + alertMessage + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel);
		
		if(SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(alertMessage)){
			
			pedRaiseDetailsTableObj = pedRaiseDetailsTable.get();
			pedRaiseDetailsTableObj.init("", false, false);
			pedRaiseDetailsTableObj.initView(bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey());
			
			layout.addComponent(pedRaiseDetailsTableObj.getTable());
		}
		
		layout.addComponent(homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				bean.setIsPEDInitiated(false);
				
				if(bean.isMultiplePEDAvailableNotDeleted() && !SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(alertMessage)){
					 alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					 bean.setMultiplePEDAvailableNotDeleted(false);
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageFor30DaysWaitingPeriod();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
	*/
		
		Button homeButton;
		Label successLabel = new Label(
				"<b>" + alertMessage + "</b>",
				ContentMode.HTML);
		
		VerticalLayout layout=new VerticalLayout();
		layout.addComponent(successLabel);
		if(SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(alertMessage)){
			
			pedRaiseDetailsTableObj = pedRaiseDetailsTable.get();
			pedRaiseDetailsTableObj.init("", false, false);
			pedRaiseDetailsTableObj.initView(bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey());
			layout.addComponent(pedRaiseDetailsTableObj.getTable());
		}
		msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Inforamtion")
			    .withTableMessage(layout)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		homeButton=msgBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				msgBox.close();
				bean.setIsPEDInitiated(false);
				
				if(bean.isMultiplePEDAvailableNotDeleted() && !SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(alertMessage)){
					 alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					 bean.setMultiplePEDAvailableNotDeleted(false);
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageFor30DaysWaitingPeriod();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
		
	}
	
	public Boolean alertMessageForAutoRestroation() {/*
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.AUTO_RESTORATION_MESSAGE + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				bean.setIsAutoRestorationDone(true);
				if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}
				}else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageFor30DaysWaitingPeriod();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
	*/

		final MessageBox showInfoMessageBox = showInfoMessageBox(SHAConstants.AUTO_RESTORATION_MESSAGE);
		Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
				bean.setIsAutoRestorationDone(true);
				if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}
				}else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageFor30DaysWaitingPeriod();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
		
	}
	
    public void poupMessageForProduct() {/*
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
			       if(bean.getIs64VBChequeStatusAlert()){
			    	   get64VbChequeStatusAlert();
			       }else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						popupMessageFor30DaysWaitingPeriod();
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setSpacing(true);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout();
			Map<String, String> popupMap = bean.getPopupMap();
			for (Map.Entry<String, String> entry : popupMap.entrySet()) {
				layout.addComponent(new Label(entry.getValue(), ContentMode.HTML));
				 layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
			}
			layout.addComponent(okButton);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.setWidth("35%");
			bean.setIsPopupMessageOpened(true);
			dialog.show(getUI().getCurrent(), null, true);
		*/
    	StringBuffer layout=new StringBuffer();
    	Map<String, String> popupMap = bean.getPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			layout.append(entry.getValue());
			layout.append(entry.getKey());
		}
			
		final MessageBox showInfoMessageBox = showInfoMessageBox(layout.toString());
		Button okButton = showInfoMessageBox.getButton(ButtonType.OK);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {

					showInfoMessageBox.close();
			       if(bean.getIs64VBChequeStatusAlert()){
			    	   get64VbChequeStatusAlert();
			       }else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});	
    }
    
    public void get64VbChequeStatusAlert() {/*
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.VB64STATUSALERT + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				// bean.setIsPopupMessageOpened(true);
				 
//					bean.setIsPopupMessageOpened(true);
					dialog.close();
					 if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						 popupMessageFor30DaysWaitingPeriod();
					 }else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
			}
		});
		
	*/

    	final MessageBox showInfoMessageBox = showInfoMessageBox(SHAConstants.VB64STATUSALERT);
    	Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				// bean.setIsPopupMessageOpened(true);
				 
//					bean.setIsPopupMessageOpened(true);
				showInfoMessageBox.close();
					 if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						 popupMessageFor30DaysWaitingPeriod();
					 }else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
			}
		});
		
		
    }


	@Override
	public Component getContent() {
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
      
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)){
			getAlertForCoronaAlert();
		}
		if(this.bean.getNewIntimationDTO().getInsuredDeceasedFlag() != null 
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getNewIntimationDTO().getInsuredDeceasedFlag())) {
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
//		//GLX2020017 commented by noufel for cr2019184 which should show common for all applicable products
//		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY)
//				|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY)){
//			SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_AROGYA_ALERT,"Information");
//		}
		//GLX2020017 //added for CR2019184
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getPolicyInstalmentFlag() != null 
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_AROGYA_ALERT,"Information");
		}
		//CR2019217
		//changes done for SM agent percentage by noufel on 13-01-2020
				String agentpercentage = masterService.getAgentPercentage(ReferenceTable.ICR_AGENT);
				String smpercentage = masterService.getAgentPercentage(ReferenceTable.SM_AGENT);
				if((bean.getIcrAgentValue() != null && !bean.getIcrAgentValue().isEmpty() 
						&& (Integer.parseInt(bean.getIcrAgentValue()) >= Integer.parseInt(agentpercentage))) 
						|| bean.getSmAgentValue() != null && !bean.getSmAgentValue().isEmpty() 
								&& (Integer.parseInt(bean.getSmAgentValue()) >= Integer.parseInt(smpercentage))){
					SHAUtils.showICRAgentAlert(bean.getIcrAgentValue(), agentpercentage, bean.getSmAgentValue(), smpercentage);
				}
		
		if(bean.getPopupSIRestrication()!=null && bean.getPopupSIRestrication().size() == 2){
			siRestricationAlert(this.bean.getPopupSIRestrication());
		}
		else if(bean.getNewIntimationDTO().getIsDeletedRisk()){
			showDeletedInsuredAlert();
		}
		else if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
			showCMDAlert();
		}
		else if(null != this.bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getTopUpPolicyAlertFlag())){
			showTopUpAlertMessage(this.bean.getTopUpPolicyAlertMessage());
		}
		else if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && this.bean.getDuplicateInsuredList() != null && ! this.bean.getDuplicateInsuredList().isEmpty()){
			showDuplicateInsured(this.bean.getDuplicateInsuredList());
		}
		else if(null != bean.getPolicyDto().getGmcPolicyType() && !bean.getPolicyDto().getGmcPolicyType().isEmpty() && bean.getPolicyDto().getLinkPolicyNumber() != null
				&& (bean.getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
						bean.getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
			showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
		}
		else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
			getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
		}		
		else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
			paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
		}
		else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
			showICRMessage();
		}else if(bean.getIsPolicyValidate()){	
			policyValidationPopupMessage();
		}
		
		else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
			suspiousPopupMessage();
		}else if(!bean.getNonPreferredPopupMap().isEmpty()){
			nonPreferredPopupMessage();
		}
		else if(bean.getIsPEDInitiated()) {
//			alertMessageForPED();
			if(bean.isInsuredDeleted()){
				alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
			}else{
				alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
			}	
		} else if(bean.isInsuredDeleted()){
			alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
		} else if(bean.getIsAutoRestorationDone()) {
			alertMessageForAutoRestroation();
		} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
		} else if(bean.getIs64VBChequeStatusAlert()){
			get64VbChequeStatusAlert();
		}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
				bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
			popupMessageFor30DaysWaitingPeriod();
		}else if(bean.getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
		}
		
		if(bean.getNewIntimationDTO().getHospitalDto()	 != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getDiscount() != null
				&& SHAConstants.CAPS_YES.equalsIgnoreCase(bean.getNewIntimationDTO().getHospitalDto().getDiscount())) {
			String hsptRemark = bean.getNewIntimationDTO().getHospitalDto().getDiscountRemark();
			if(hsptRemark != null){
				SHAUtils.showMessageBoxWithCaption(SHAConstants.HOSPITAL_DISCOUNT_ALERT_MSG + hsptRemark,"Information - Hospital Discount");
			}
		}
		//added for CR young star product by noufel on 08-04-2020
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_YOUNG_PRODUCT_CODE) ||
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_91))
				&& bean.getPreauthDataExtractionDetails() != null && bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null 
				&& bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null
				&& bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.MATERNITY_MASTER_ID)){
			SHAUtils.showMessageBoxWithCaption(SHAConstants.ALERT_FOR_MATERNITY_YOUNG_PRODUCT,"Information");
		}
		// CR2019257 Base Policy
		if(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo() != null){
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "View Base Policy");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(SHAConstants.BASEPOLICY + "</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			Button BaseViewButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
				
		
			@Override
			public void buttonClick(ClickEvent event) {
				}
				});
				BaseViewButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						
						if(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo() != null)
						{
						VerticalLayout verticalLayout = null;
							VerticalLayout vLayout = new VerticalLayout();
							Intimation intimation=intimationService.getIntimationByKey(bean.getNewIntimationDTO().getKey());
							Policy policy = policyService.getByPolicyNumber(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo());
							if(policy !=null){
							viewBasePolicyDetail.setPolicyServiceAndPolicy(policyService, policy,
									masterService, intimationService);	
							viewBasePolicyDetail.initView();
							UI.getCurrent().addWindow(viewBasePolicyDetail);
						}
						else{
							getErrorMessage("Intimation not available for this Base policy");
						}
							
					}
						else
						{
							getErrorMessage("Base Policy is not available");
						}
						
					}
				});	
		}
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		referenceNoTxt = (TextField) binder.buildAndBind("Reference No",
				"referenceNo", TextField.class);
		referenceNoTxt.setWidth("210px");
		referenceNoTxt.setEnabled(false);
		reasonForAdmissionTxt = (TextField) binder.buildAndBind(
				"Reason For Admission", "reasonForAdmission", TextField.class);
		reasonForAdmissionTxt.setMaxLength(101);
		admissionDate = (DateField) binder.buildAndBind(
				"Date of Admission", "admissionDate", DateField.class);
		reasonForChangeInDOA = (TextField)binder.buildAndBind(
				"Reason For Change in DOA", "changeOfDOA", TextField.class);
		/*cmbCatastrophicLoss = (ComboBox) binder.buildAndBind(
				"Catastrophe Loss", "catastrophicLoss", ComboBox.class);*/
		
		cmbNatureOfLoss = (ComboBox) binder.buildAndBind(
				"Nature Of Loss", "natureOfLoss", ComboBox.class);
		
		cmbCauseOfLoss = (ComboBox) binder.buildAndBind(
				"Cause Of Loss", "causeOfLoss", ComboBox.class);
		reasonForChangeInDOA.setVisible(false);
		reasonForChangeInDOA.setMaxLength(100);
		//CSValidator validator = new CSValidator();
//		validator.extend(reasonForChangeInDOA);
//		validator.setRegExp("^[a-zA-Z 0-9/]*$");
//		validator.setPreventInvalidTyping(true);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getChangeOfDOA() && !("").equals(this.bean.getPreauthDataExtractionDetails().getChangeOfDOA()))
		{
			admissionDate.setValue(this.bean.getPreauthDataExtractionDetails().getAdmissionDate());
			reasonForChangeInDOA.setValue(this.bean.getPreauthDataExtractionDetails().getChangeOfDOA());
			reasonForChangeInDOA.setVisible(true);
			reasonForChangeInDOA.setRequired(true);
		}
		
		noOfDaysTxt = (TextField) binder.buildAndBind("No of Days", "noOfDays",
				TextField.class);
		noOfDaysTxt.setMaxLength(4);
		/**
		 * Adding validation for noOfDaysTxt field.
		 * */
		
		CSValidator noOfDaysValidator = new CSValidator();
		
		noOfDaysValidator.extend(noOfDaysTxt);
		noOfDaysValidator.setRegExp("^[0-9]*$");
		noOfDaysValidator.setPreventInvalidTyping(true);
		
		
		cmbNatureOfTreatment = (ComboBox) binder.buildAndBind(
				"Nature of Treatment", "natureOfTreatment", ComboBox.class);
		firstConsultantDate = (PopupDateField) binder.buildAndBind(
				"1st Consultation Date", "firstConsultantDate",
				PopupDateField.class);
		corpBufferChk = (CheckBox) binder.buildAndBind("", "corpBuffer",
				CheckBox.class);
		
		//Added for corp buffer checkbox validation.
		editCorpBufferChk();

		
		criticalIllnessChk = (CheckBox) binder.buildAndBind("Critical Illness",
				"criticalIllness", CheckBox.class);
		cmbRoomCategory = (ComboBox) binder.buildAndBind("Room Category",
				"roomCategory", ComboBox.class);
		/*
		if (null != bean.getNewIntimationDTO().getRoomCategory())
		{
			for(int i = 0 ; i<roomCategory.size() ; i++)
			{
				if ((bean.getNewIntimationDTO().getRoomCategory().getValue()).equalsIgnoreCase(roomCategory.getIdByIndex(i).getValue()))
				{
					this.cmbRoomCategory.setValue(roomCategory.getIdByIndex(i));
				}
				
			}
		}*/
		
		/*if(null != this.bean.getPreauthDataExtractionDetails() && null != this.bean.getPreauthDataExtractionDetails().getRoomCategory() 
				&& null != this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue())
				{
					if(null != roomCategory)
					for(int i =0 ; i<roomCategory.size() ; i++)
					{
						if((this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue()).equalsIgnoreCase(roomCategory.getIdByIndex(i).getValue()))
						{
							this.cmbRoomCategory.setValue(roomCategory.getIdByIndex(i).getValue());
						}
					}
//					/cmbRoomCategory.setValue(this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue());
				}
					*/
		
		cmbSpecifyIllness = (ComboBox) binder.buildAndBind("Specify",
				"specifyIllness", ComboBox.class);
		cmbSpecifyIllness.setReadOnly(true);
		cmbCategory = (ComboBox) binder.buildAndBind("Category",
				"category", ComboBox.class);
		cmbCategory.setEnabled(false);
		cmbTreatmentType = (ComboBox) binder.buildAndBind("Treatment Type",
				"treatmentType", ComboBox.class);
		cmbTreatmentType.setEnabled(false);
		cmbPatientStatus = (ComboBox) binder.buildAndBind("Patient Status",
				"patientStatus", ComboBox.class);
//		automaticRestorationTxt = (TextField) binder.buildAndBind(
//				" ", "autoRestoration", TextField.class);
//		automaticRestorationTxt.setEnabled(false);

		automaticRestorationLbl = new Label(bean.getPreauthDataExtractionDetails().getAutoRestoration(), ContentMode.HTML);
		autoRestorationCountLbl = new Label("&nbsp;&nbsp;&nbsp;&nbsp;No of Times  " + bean.getPreauthDataExtractionDetails().getRestorationCount(), ContentMode.HTML);

		HorizontalLayout restorationLayout = new HorizontalLayout(automaticRestorationLbl,autoRestorationCountLbl);
		restorationLayout.setCaption("Automatic Restoration");
		restorationLayout.setSpacing(true);
				
		cmbIllness = (ComboBox) binder.buildAndBind("Illness", "illness",
				ComboBox.class);
		if((SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE).equals(bean.getPreauthDataExtractionDetails().getAutoRestoration()) || (SHAConstants.AUTO_RESTORATION_NOTDONE).equals(bean.getPreauthDataExtractionDetails().getAutoRestoration()))
		{
			cmbIllness.setEnabled(false);
		}
		
		if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			cmbIllness.setVisible(false);
		}
		
		FormLayout illnessFLayout = new FormLayout(criticalIllnessChk);
		illnessFLayout.setCaption("Critical Illness");
		illnessFLayout.setMargin(false);
		
		txtCorpBufferAllocatedAmt = new TextField("Corporate Buffer Limit");
		
		CSValidator validator = new CSValidator();
		validator.extend(txtCorpBufferAllocatedAmt);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
		
		FormLayout bufferFLayout = new FormLayout(corpBufferChk);
		bufferFLayout.setCaption("Corp Buffer");
		bufferFLayout.setMargin(false);
		
		corpBufferDetails = new Button("View");
		corpBufferDetails.setStyleName("link");
		corpBufferDetails.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
			gmcBufferDetails.bindFieldGroup(bean, isCorpBufferChecked);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Corporate Buffer Details");
			popup.setWidth("30%");
			popup.setHeight("30%");
			popup.setContent(gmcBufferDetails);
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
			UI.getCurrent().addWindow(popup);}
		});
		
		txtCorpBufferAllocatedAmt.addBlurListener(getgmcCorpBufferLimitListener());
		corpBufferHLayout = new HorizontalLayout(txtCorpBufferAllocatedAmt,corpBufferDetails);
		bufferFLayout.addComponent(corpBufferHLayout);
		corpBufferHLayout.setVisible(false);
		
		editCorpBufferChk();
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(this.bean.getClaimDTO() != null && this.bean.getClaimDTO().getGmcCorpBufferLmt() != null){
				txtCorpBufferAllocatedAmt.setValue(this.bean.getClaimDTO().getGmcCorpBufferLmt().toString());
			}	
		}
		
		cmbHospitalisationDueTo = (ComboBox) binder.buildAndBind(
				"Hospitalisation Due to", "hospitalisationDueTo", ComboBox.class);
		
		cmbPreAuthType = (ComboBox) binder.buildAndBind("Pre-Auth Type", "preAuthType", ComboBox.class);
		
		cmbSection = (ComboBox) binder.buildAndBind(
				"Section", "section", ComboBox.class);
		
		if(this.bean.getNewIntimationDTO() != null && !ReferenceTable.getSectionKeys().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
			cmbSection.setEnabled(false);
		}

		firstFLayout = new FormLayout(referenceNoTxt, admissionDate,reasonForChangeInDOA,
				cmbRoomCategory, illnessFLayout, cmbSpecifyIllness, cmbHospitalisationDueTo,cmbPreAuthType);
		firstFLayout.setSpacing(true);
		firstFLayout.setMargin(false);
		secondFLayout = new FormLayout(reasonForAdmissionTxt, noOfDaysTxt,
				cmbNatureOfTreatment, firstConsultantDate, /*bufferFLayout,*/   //R1167
//				automaticRestorationTxt,
				restorationLayout,cmbIllness, cmbSection,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss);
		secondFLayout.setMargin(false);
		
		
		//Fix for issue 692 -- starts
		//List<ProcedureDTO> procList = (null != procedureTableObj ? procedureTableObj.getValues() : null);
		this.diagnosisListenerTableObj =   diagnosisListnerTable.get();
		//this.diagnosisListenerTableObj.init("premedicalPreauth",procList);
		this.diagnosisListenerTableObj.init(this.bean, "premedicalPreauth");
		
		this.sectionDetailsListenerTableObj = sectionDetailsListenerTable.get();
		this.sectionDetailsListenerTableObj.init(this.bean, SHAConstants.PRE_MEDICAL_PRE_AUTH);
		if(this.referenceData != null) {
			this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		}
		this.sectionDetailsListenerTableObj.addBeanToList(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO());

		HorizontalLayout formHLayout = new HorizontalLayout(firstFLayout,
				secondFLayout);
		formHLayout.setWidth("100%");
		PreauthCoordinatorView preauthCoordinatorViewInstance = preauthCoordinatorView
				.get();
		preauthCoordinatorViewInstance.setWizard(wizard,SHAConstants.PRE_MEDICAL_PRE_AUTH);
		this.preauthCoordinatorViewInstance = preauthCoordinatorViewInstance;
		preauthCoordinatorViewInstance.init(this.bean);
		

		tableVLayout = new VerticalLayout();
		tableVLayout.setSpacing(true);
		categoryFLayout = new FormLayout(cmbCategory);
		categoryFLayout.setMargin(false);
		treatmentFLayout = new FormLayout(cmbTreatmentType);
		treatmentFLayout.setMargin(false);
		patientStatusFLayout = new FormLayout(cmbPatientStatus);
		patientStatusFLayout.setMargin(false);
		categoryHLayout = new HorizontalLayout(categoryFLayout);
		categoryHLayout.setWidth("100%");
		dynamicElementsHLayout = new HorizontalLayout(treatmentFLayout,
				patientStatusFLayout);
		dynamicElementsHLayout.setWidth("100%");
//		dynamicElementsHLayout.setMargin(false);

		claimedDetailsTableObj = claimedAmountDetailsTable.get();
		claimedDetailsTableObj.initView(this.bean, SHAConstants.PRE_MEDICAL_PRE_AUTH);
		
		//R1006
		txtAmtClaimed = binder.buildAndBind("Amount Claimed" , "amtClaimed",TextField.class);
		txtAmtClaimed.setNullRepresentation("");
		txtAmtClaimed.setMaxLength(15);
		
		CSValidator ClaimedAmtValidator = new CSValidator();
		ClaimedAmtValidator.extend(txtAmtClaimed);
		ClaimedAmtValidator.setRegExp("^[0-9]*$");
		ClaimedAmtValidator.setPreventInvalidTyping(true);
		
		txtAmtClaimed.addBlurListener(getclaimedAmtListener());
		
		txtDiscntHospBill = binder.buildAndBind("Discount in Hospital Bill" , "disCntHospBill",TextField.class);
		txtDiscntHospBill.setNullRepresentation("");
		txtDiscntHospBill.setMaxLength(15);
		
		CSValidator disCntHospBillAmtValidator = new CSValidator();
		disCntHospBillAmtValidator.extend(txtDiscntHospBill);
		disCntHospBillAmtValidator.setRegExp("^[0-9]*$");
		disCntHospBillAmtValidator.setPreventInvalidTyping(true);
		
		txtNetAmt = binder.buildAndBind("Claimed amount after Discount" , "netAmt",TextField.class);
		txtNetAmt.setNullRepresentation("");
		txtNetAmt.setMaxLength(15);
		txtNetAmt.setEnabled(false);
		
		CSValidator netAmtValidator = new CSValidator();
		netAmtValidator.extend(txtNetAmt);
		netAmtValidator.setRegExp("^[0-9]*$");
		netAmtValidator.setPreventInvalidTyping(true);
		
		txtDiscntHospBill.addBlurListener(getHospDiscountBillListener());
		
		FormLayout claimedAmtFLayout = new FormLayout(txtAmtClaimed);
		claimedAmtFLayout.setMargin(false);
		FormLayout disCntFLayout = new FormLayout(txtDiscntHospBill);
		disCntFLayout.setMargin(false);
		FormLayout netAmtFLayout = new FormLayout(txtNetAmt);
		netAmtFLayout.setMargin(false);
		HorizontalLayout hospDiscountHLayout = new HorizontalLayout(claimedAmtFLayout,
				disCntFLayout, netAmtFLayout);
		hospDiscountHLayout.setWidth("100%");
		
		//Added for test.
		Label amtClaimedDetailsLbl = new Label("Amount Claimed Details");
		/*VerticalLayout lableLayout = new VerticalLayout();
		lableLayout.addComponent(amtClaimedDetailsLbl);
		lableLayout.setHeight("5px");
		lableLayout.setMargin(true);*/

		VerticalLayout lLayout = new VerticalLayout();
		lLayout.addComponent(amtClaimedDetailsLbl);
		lLayout.addComponent(hospDiscountHLayout);
		lLayout.addComponent(claimedDetailsTableObj);
		
		benefitLayout = new VerticalLayout();
		
		if(bean.getPreauthDataExtractionDetails().getOtherBeneitApplicableFlag() == 1){
		
			unbindField(otherBenefitsOption);
			otherBenefitsOption = (OptionGroup) binder.buildAndBind(
					"Other Benifits", "otherBenfitOpt", OptionGroup.class);
			otherBenefitsOption.addItems(getReadioButtonOptions());
			otherBenefitsOption.setItemCaption(true, "Yes");
			otherBenefitsOption.setItemCaption(false, "No");
			otherBenefitsOption.setStyleName("horizontal");
			otherBenefitsOption.setValue(false);
		
		benefitLayout.addComponent(new FormLayout(otherBenefitsOption));	
		otherBenefitsOption.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
		
				boolean selected = (boolean)event.getProperty().getValue();
				bean.getPreauthDataExtractionDetails().setOtherBenfitOpt(selected);
				
				fireViewEvent(PreMedicalPreauthWizardPresenter.PRE_MEDICAL_OTHER_BENEFITS, bean);				
			}
		});
		
		if(bean.getPreauthDataExtractionDetails().getOtherBenfitOpt() != null){
			otherBenefitsOption.setValue(bean.getPreauthDataExtractionDetails().getOtherBenfitOpt());
		}
	 	   
        wholeVLayout = new VerticalLayout(diagnosisListenerTableObj, formHLayout, sectionDetailsListenerTableObj, diagnosisListenerTableObj,categoryHLayout,dynamicElementsHLayout, tableVLayout, lLayout , benefitLayout,preauthCoordinatorViewInstance);
		}	
		else{
			wholeVLayout = new VerticalLayout(diagnosisListenerTableObj, formHLayout, sectionDetailsListenerTableObj, diagnosisListenerTableObj,categoryHLayout,dynamicElementsHLayout, tableVLayout, lLayout, preauthCoordinatorViewInstance);		
		}
		/*Below code not implemented for cashless*/	
		/*if (cmbPatientStatus.getValue() != null 
				&& (this.cmbPatientStatus.getValue().toString().toLowerCase().contains("deceased"))
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
			buildNomineelayout();
		}*/				
		
		wholeVLayout.setSpacing(false);
		wholeVLayout.setMargin(true);
		
		addListener();
		addAdmissionDateChangeListener();
		addDiagnosisNameChangeListener();
		mandatoryFields.add(admissionDate);
				
		mandatoryFields.add(cmbRoomCategory);
//		mandatoryFields.add(cmbTreatmentType);
		mandatoryFields.add(cmbPatientStatus);
		mandatoryFields.add(cmbPreAuthType);
		
		
		if(this.bean.getNewIntimationDTO() != null && ReferenceTable.getSectionKeysOfCombiProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
				mandatoryFields.add(cmbSection);
			}

		showOrHideValidation(false);
		return wholeVLayout;
	}
	



	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private void alertMessageForClaimCount(Long claimCount){/*
		
		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;
		
		
   		Label successLabel = new Label(
				"<b style = 'color: black;'>"+msg+"</b>",
				ContentMode.HTML);
//   		successLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//   		successLabel.addStyleName(ValoTheme.LABEL_BOLD);
   		successLabel.addStyleName(ValoTheme.LABEL_H3);
   		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		HorizontalLayout mainHor = new HorizontalLayout(successLabel);
		TextField dummyField = new TextField();
		dummyField.setEnabled(false);
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
   		VerticalLayout firstForm = new VerticalLayout(dummyField,mainHor,homeButton);
   		
		Panel panel = new Panel(firstForm);
		
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			panel.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			panel.addStyleName("girdBorder2");
		}
		
		panel.setHeight("135px");
//		panel.setSizeFull();
		
		
		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("30%");
		popup.setHeight("20%");
//		popup.setContent( viewDocumentDetailsPage);
		popup.setContent(panel);
		popup.setClosable(true);
		
		popup.center();
		popup.setResizable(false);
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
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				popup.close();
				if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
					Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate.getValue());
		            MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
		            if((diffDays != 0 && diffDays < 30)){
		            	StarCommonUtils.alertMessage(getUI(), SHAConstants.THIRTY_DAYS_WAITING_ALERT);
					}
				}
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	*/

		
		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;
		
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2 && !ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){

			final MessageBox showInfoMessageBox = showInfoMessageBox(msg);
			Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					bean.setIsPopupMessageOpened(true);
					showInfoMessageBox.close();
					if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
						Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate.getValue());
			            MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
			            if((diffDays != 0 && diffDays < 30)){
			            	StarCommonUtils.alertMessage(getUI(), SHAConstants.THIRTY_DAYS_WAITING_ALERT);
						}
					}
					
				}
			});
		}else if(this.bean.getClaimCount() >2 && !ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			final MessageBox msgBox = MessageBox
				    .createCritical()
				    .withCaptionCust("Critical Alert")
				    .withMessage(msg)
				    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				    .open();
		Button homeButton = msgBox.getButton(ButtonType.OK);
		
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				msgBox.close();
				if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
					Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate.getValue());
		            MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
		            if((diffDays != 0 && diffDays < 30)){
		            	StarCommonUtils.alertMessage(getUI(), SHAConstants.THIRTY_DAYS_WAITING_ALERT);
					}
				}
				
			}
		});
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		
		
		
		reasonForAdmissionTxt.setValue(this.bean.getReasonForAdmission());
		//Setting this feild readOnly true, since this needs to be editable as per sathish sir.
		reasonForAdmissionTxt.setReadOnly(true);
		reasonForAdmissionTxt.setEnabled(false);
		
		//reasonForChangeInDOA.setNullRepresentation(reasonForAdmissionTxt.getValue());
		
		BeanItemContainer<SelectValue> treatementType = (BeanItemContainer<SelectValue>) referenceData
				.get("treatmentType");
		roomCategory = (BeanItemContainer<SelectValue>) referenceData
				.get("roomCategory");
		BeanItemContainer<SelectValue> natureOfTreatment = (BeanItemContainer<SelectValue>) referenceData
				.get("natureOfTreatment");
		BeanItemContainer<SelectValue> patientStatus = (BeanItemContainer<SelectValue>) referenceData
				.get("patientStatus");
		BeanItemContainer<SelectValue> illness = (BeanItemContainer<SelectValue>) referenceData
				.get("illness");
		
		BeanItemContainer<SelectValue> criticalIllness = (BeanItemContainer<SelectValue>) referenceData
				.get("criticalIllness");
		
		BeanItemContainer<SelectValue> hospitalizationDueTo = (BeanItemContainer<SelectValue>) referenceData
				.get("hospitalisationDueTo");		
		
		BeanItemContainer<SelectValue> preAuthType = (BeanItemContainer<SelectValue>) referenceData
				.get("preAuthTypeValue");
		
		if(!ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && hospitalizationDueTo != null && hospitalizationDueTo.getItemIds().size()>0){
			SelectValue ARTreatement = hospitalizationDueTo.getItemIds().get(0);
			hospitalizationDueTo.removeItem(ARTreatement);
		}
		
		BeanItemContainer<SelectValue> section = (BeanItemContainer<SelectValue>) referenceData
				.get("section");
		
		BeanItemContainer<SelectValue> catastrophicLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("catastrophicLoss");
		
		BeanItemContainer<SelectValue> natureOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("natureOfLoss");
		
		BeanItemContainer<SelectValue> causeOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("causeOfLoss");
		
		this.bean.getPolicyDto().setAdmissionDate(admissionDate.getValue());
		//fireViewEvent(PreMedicalPreauthWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, this.bean.getPolicyDto());
		/**
		 * In menu presenter , the below observer method uses preauthDTO for further processing. But since 
		 * here policyDto is passed, class cast exception was thrown. Hence to overcome the same, preauthDTO is passed
		 * instead of policyDTO. Moreover, from preauthDTO, policyDTO can be obtained. Hence there will be
		 * no impact due to the below change.
		 * */
		if(cmbSection != null && cmbSection.getValue() != null){
			SelectValue sectionValue = (SelectValue) cmbSection.getValue();
			this.bean.getPreauthDataExtractionDetails().setSection(sectionValue);
		}
		/*if(cmbCatastrophicLoss != null && cmbCatastrophicLoss.getValue() != null){
			SelectValue sectionValue = (SelectValue) cmbCatastrophicLoss.getValue();
			this.bean.getPreauthDataExtractionDetails().setCatastrophicLoss(sectionValue);
		}*/
		if(cmbNatureOfLoss != null && cmbNatureOfLoss.getValue() != null){
			SelectValue sectionValue = (SelectValue) cmbNatureOfLoss.getValue();
			this.bean.getPreauthDataExtractionDetails().setNatureOfLoss(sectionValue);
		}
		if(cmbCauseOfLoss != null && cmbCauseOfLoss.getValue() != null){
			SelectValue sectionValue = (SelectValue) cmbCauseOfLoss.getValue();
			this.bean.getPreauthDataExtractionDetails().setCauseOfLoss(sectionValue);
		}
		
		fireViewEvent(PreMedicalPreauthWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, this.bean);
		
		cmbTreatmentType.setContainerDataSource(treatementType);
		cmbTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbTreatmentType.setItemCaptionPropertyId("value");

		cmbRoomCategory.setContainerDataSource(roomCategory);
		cmbRoomCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRoomCategory.setItemCaptionPropertyId("value");

		if(this.bean.getPolicyDto().getProduct().getNonAllopathicFlag() != null && this.bean.getPolicyDto().getProduct().getNonAllopathicFlag().toLowerCase().equalsIgnoreCase("n")) {
			List<SelectValue> itemIds2 = natureOfTreatment.getItemIds();
			List<SelectValue> allopathicValues = new ArrayList<SelectValue>() ; 
			for (SelectValue selectValue : itemIds2) {
				if(!selectValue.getValue().toString().toLowerCase().contains("non")) {
					allopathicValues.add(selectValue);
				}
			}
			natureOfTreatment.removeAllItems();
			natureOfTreatment.addAll(allopathicValues);
		}
		
		cmbNatureOfTreatment.setContainerDataSource(natureOfTreatment);
		cmbNatureOfTreatment.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbNatureOfTreatment.setItemCaptionPropertyId("value");

		cmbPatientStatus.setContainerDataSource(patientStatus);
		cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPatientStatus.setItemCaptionPropertyId("value");

		cmbIllness.setContainerDataSource(illness);
		cmbIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbIllness.setItemCaptionPropertyId("value");
		cmbIllness.setNullSelectionAllowed(false);
		
		cmbSpecifyIllness.setReadOnly(false);
		cmbSpecifyIllness.setContainerDataSource(criticalIllness);
		cmbSpecifyIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSpecifyIllness.setItemCaptionPropertyId("value");
		
		cmbHospitalisationDueTo.setContainerDataSource(hospitalizationDueTo);
		cmbHospitalisationDueTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospitalisationDueTo.setItemCaptionPropertyId("value");
		
		cmbPreAuthType.setContainerDataSource(preAuthType);
		cmbPreAuthType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPreAuthType.setItemCaptionPropertyId("value");
		
		
		cmbSection.setContainerDataSource(section);
		cmbSection.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSection.setItemCaptionPropertyId("value");
		
		/*cmbCatastrophicLoss.setContainerDataSource(catastrophicLoss);
		cmbCatastrophicLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCatastrophicLoss.setItemCaptionPropertyId("value");*/
		
		cmbNatureOfLoss.setContainerDataSource(natureOfLoss);
		cmbNatureOfLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbNatureOfLoss.setItemCaptionPropertyId("value");
		
		cmbCauseOfLoss.setContainerDataSource(causeOfLoss);
		cmbCauseOfLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCauseOfLoss.setItemCaptionPropertyId("value");
		
		if (this.bean.getPreauthDataExtractionDetails().getSection() != null) {
			this.cmbSection.setValue(this.bean
					.getPreauthDataExtractionDetails().getSection());
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null) {
			this.cmbHospitalisationDueTo.setValue(this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo());
		}
		
		if(this.bean.getPreauthDataExtractionDetails().getPreAuthType() != null) {
			this.cmbPreAuthType.setValue(this.bean.getPreauthDataExtractionDetails().getPreAuthType());
		}
		
		cmbSpecifyIllness.setReadOnly(true);
		
		if (this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null) {
			this.cmbNatureOfTreatment.setValue(this.bean
					.getPreauthDataExtractionDetails().getNatureOfTreatment());
		}
		
		
		
		/**
		 * Room category mentioned at intimation level 
		 * will be set into new intimation DTO . Since preAuthDataExtraction
		 * details().getRoomCategory() was null, this approach was followed.
		 * */

		if (null != bean.getNewIntimationDTO().getRoomCategory())
		{
			for(int i = 0 ; i<roomCategory.size() ; i++)
			{
				if ((bean.getNewIntimationDTO().getRoomCategory().getValue()).equalsIgnoreCase(roomCategory.getIdByIndex(i).getValue()))
				{
					this.cmbRoomCategory.setValue(roomCategory.getIdByIndex(i));
				}
				
			}
		}
		if(null != this.bean.getPreauthDataExtractionDetails() && null != this.bean.getPreauthDataExtractionDetails().getRoomCategory() 
				&& null != this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue())
				{
					if(null != roomCategory)
					for(int i =0 ; i<roomCategory.size() ; i++)
					{
						if((this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue()).equalsIgnoreCase(roomCategory.getIdByIndex(i).getValue()))
						{
							this.cmbRoomCategory.setValue(roomCategory.getIdByIndex(i));
						}
					}
				//cmbRoomCategory.setValue(this.bean.getPreauthDataExtractionDetails().getRoomCategory().getValue());
				}
					
		/*if (this.bean.getPreauthDataExtractionDetails().getRoomCategory() != null) {
		//	System.out.println("---the value----"+);
			this.cmbRoomCategory.setValue(this.bean
					.getPreauthDataExtractionDetails().getRoomCategory());
		}*/
		
		if (this.bean.getPreauthDataExtractionDetails().getSpecifyIllness() != null) {
			this.cmbSpecifyIllness.setReadOnly(false);
			this.cmbSpecifyIllness.setValue(this.bean
					.getPreauthDataExtractionDetails().getSpecifyIllness());
		}

		if (this.bean.getPreauthDataExtractionDetails().getTreatmentType() != null) {
			this.cmbTreatmentType.setValue(this.bean
					.getPreauthDataExtractionDetails().getTreatmentType());
		}

		if (this.bean.getPreauthDataExtractionDetails().getPatientStatus() != null) {
			this.cmbPatientStatus.setValue(this.bean
					.getPreauthDataExtractionDetails().getPatientStatus());
		}

		if (this.bean.getPreauthDataExtractionDetails().getIllness() != null) {
			this.cmbIllness.setValue(this.bean
					.getPreauthDataExtractionDetails().getIllness());
		}

		this.preauthCoordinatorViewInstance.setUpReference(referenceData);
//		this.claimedDetailsTableObj.setDBCalculationValues((Map<String, Double>)referenceData.get("claimDBDetails"));
		
		this.diagnosisListenerTableObj.setReferenceData(referenceData);
		
		this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		
		if(this.procedureTableObj != null) {
			this.procedureTableObj.setReferenceData(referenceData);
			procedureTableObj.setEnabled(false);
		}
		
		
		setTableValues();
	}

	@Override
	public boolean onAdvance() {
		setTableValuesToDTO();
        if(validatePage()) {
			
			if(this.bean.getAlertMessageOpened()){
				this.bean.setAlertMessageOpened(false);
				if(bean.getIsDialysis() && !bean.getDialysisOpened()) {
					alertNoOfSittings();
					return false;
				}
				bean.setDialysisOpened(false);
				setTableValuesToDTO();
				return true;
			}
			alertMessage();
			return false;
			
		}else{
			return false;
		}
		
//		return validatePage();
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

	@SuppressWarnings("static-access")
	public boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		//boolean diagValues = true;
		
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

		try {
			if(!this.preauthCoordinatorViewInstance.isValid()) {
				hasError = true;
				List<String> errors = this.preauthCoordinatorViewInstance.getErrors();
				for (String error : errors) {
					eMsg.append(error);
				 }
			}
		} catch (CommitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*if(null != this.cmbTreatmentType && null == this.cmbTreatmentType.getValue())
		{
			hasError = true;
			eMsg.append("Please Select Treatment Type. </br>");
		}*/
		
		if(this.diagnosisListenerTableObj != null && this.diagnosisListenerTableObj.getValues().isEmpty()) {
			hasError = true;
			eMsg.append("Please Add Atleast one Diagnosis Details to Proceed Further. </br>"); 
		}
		
		if(this.reasonForChangeInDOA.isVisible() && !(null != this.reasonForChangeInDOA && null!= this.reasonForChangeInDOA.getValue()))
			
		{
			hasError = true;
			eMsg.append("Please enter Reason For Change in DOA to Proceed Further. </br>");
					
		}
		if(this.bean.getNewIntimationDTO() != null && ReferenceTable.getSectionKeysOfCombiProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {					
		
			if(null != this.cmbSection && (null == this.cmbSection.getValue() || ("").equalsIgnoreCase(this.cmbSection.getValue().toString())))
			
				{
					hasError = true;
					eMsg.append("Please Select Section. </br>");
					
				}
		}
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			
			String hospitalisationValue = cmbHospitalisationDueTo.getValue() != null ? ((SelectValue)cmbHospitalisationDueTo.getValue()).getValue() : "";
			
			String preAuthTypeValue = cmbPreAuthType.getValue() != null ? ((SelectValue)cmbPreAuthType.getValue()).getValue() : "";
			
			Long sectionTableCoverValue = sectionDetailsListenerTableObj.getValue().getCover() != null ? sectionDetailsListenerTableObj.getValue().getCover().getId() : 0l;
			
			
			/*if(((SHAConstants.ASSISTED_REPRODUCTION_TREATMENT).equalsIgnoreCase(sectionTableCoverValue)
					|| (SHAConstants.ASSISTED_REPRODUCTION_TREATMENT).equalsIgnoreCase(hospitalisationValue))){
								
				if(!hospitalisationValue.equalsIgnoreCase(sectionTableCoverValue)){
					hasError = true;
					eMsg.append("Please Select Cover Value to match with The Hospitalisation Due to. </br>");
				}
			}	*/
			fireViewEvent(PreMedicalPreauthWizardPresenter.GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_FLP, bean);
			
			SelectValue cmbHospitalizationDueTo = (SelectValue) this.cmbHospitalisationDueTo.getValue();
			
			SelectValue cmbPreAuthTypeVal = (SelectValue) this.cmbPreAuthType.getValue();
			
			
			
			if(null != this.cmbHospitalisationDueTo && null == this.cmbHospitalisationDueTo.getValue() &&
					(assistedTreatment).equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getId()))
			{
				hasError = true;
				eMsg.append("Please Select Hospitalization due to</br>");
			}
			
			else if(null != bean && null != this.cmbHospitalisationDueTo && null != this.cmbHospitalisationDueTo.getValue() &&
					(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT_HOSPITALISATION_KEY).equals(cmbHospitalizationDueTo.getId()) && 
					null != sectionTableCoverValue &&
					!(assistedTreatment).equals(sectionTableCoverValue)
					)
			{
				hasError = true;
				eMsg.append("Please Change the cover since Hospitalization due to is Assisted Reproduction Treatment.</br>");
			}		
			else
			{
				if(null != bean && null != this.cmbHospitalisationDueTo && null != this.cmbHospitalisationDueTo.getValue() &&
						!(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT_HOSPITALISATION_KEY).equals(cmbHospitalizationDueTo.getId())&&
						null != sectionTableCoverValue &&
						(assistedTreatment).equals(sectionTableCoverValue))
				{
					hasError = true;
					eMsg.append("Please Change the cover since Hospitalization due to is not Assisted Reproduction Treatment.</br>");
				}
			}
			/*if(null == this.cmbPreAuthType && null == this.cmbPreAuthType.getValue()) {
				hasError = true;
				eMsg.append("Please Change the cover since Hospitalization due to is not Assisted Reproduction Treatment.</br>");
			}*/
			
		 }	
		
		if((this.procedureTableObj != null && this.procedureTableObj.getValues().isEmpty()) && (this.newProcedurdTableObj != null && this.newProcedurdTableObj.getValues().isEmpty())) {
			hasError = true;
			eMsg.append("Please Add Atleast one Procedure List Details to Proceed Further. </br>"); 
		}
		
		/*if(cmbTreatmentType != null && cmbTreatmentType.getValue() != null 
				&& (cmbTreatmentType.getValue().toString().toLowerCase().equalsIgnoreCase("surgical")
				|| cmbTreatmentType.getValue().toString().toLowerCase().equalsIgnoreCase("medical"))){
			if(this.specialityTableObj != null && this.specialityTableObj.getValues().isEmpty()){
				hasError = true;
				eMsg.append("Please add atleast one Speciality to proceed further. </br>");
			}
			
		}*/
		
		/*String strMsg = validateProcedureAndDiagnosisName();
		if(null != strMsg && !("").equalsIgnoreCase(strMsg))
		{
			eMsg += strMsg;
			hasError = true;
		}*/
		
		if(this.claimedDetailsTableObj != null && this.claimedDetailsTableObj.getTotalPayableAmt() <= 0){
			hasError = true;
			eMsg.append("Claimed Payable amount should not be Zero. </br>");
		}
		
		if(this.claimedDetailsTableObj != null && !claimedDetailsTableObj.isValid(false)) {
			hasError = true;
			List<String> errors = this.claimedDetailsTableObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		} else {
			Float enteredDays = SHAUtils.convertFloatToString(noOfDaysTxt.getValue());
			if(this.claimedDetailsTableObj != null  && this.claimedDetailsTableObj.getTotalNoOfDays() < (enteredDays)) {
				hasError = true;
				eMsg.append("The total of number of days entered against Room Rent and ICU should be lesser or equal to no. of days. </br> Claim Table Value is ").append(this.claimedDetailsTableObj.getTotalNoOfDays()).append(" and Data Extraction Value is ").append(enteredDays).append("</br>");
			}
		}
		
		if(this.procedureTableObj != null && !this.procedureTableObj.getValues().isEmpty()) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisListenerTableObj.getValues();
			List<ProcedureDTO> procedureList = this.procedureTableObj.getValues();
			Boolean isError = false;
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
				for (ProcedureDTO procedureDTO : procedureList) {
					if(procedureDTO.getSublimitName() != null && diagnosisDetailsTableDTO.getSublimitName() != null && procedureDTO.getSublimitName().getLimitId().equals(diagnosisDetailsTableDTO.getSublimitName().getLimitId())) {
						if((null != diagnosisDetailsTableDTO.getConsiderForPayment() && diagnosisDetailsTableDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)) && (null != procedureDTO.getConsiderForPayment() && procedureDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES))) {
							hasError = true;
							isError = true;
							eMsg.append("Same Sublimit is Selected for both Diagnosis and Procedure. </br> Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .  </br>");
							break;
						}
					}
				}
				if(isError) {
					break;
				}
			}
		}
		
		/*Boolean diagnosisIsConsiderForPayment = false;
		Boolean procedureIsConsiderForPayment = false;
		if(this.diagnosisListenerTableObj != null && !this.diagnosisListenerTableObj.getValues().isEmpty()) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisListenerTableObj.getValues();
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
						if((null != diagnosisDetailsTableDTO.getConsiderForPayment() && diagnosisDetailsTableDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES))) {
							diagnosisIsConsiderForPayment =false;
							break;
						} else {
							diagnosisIsConsiderForPayment = true;
						}
					
				
			}
		}
		if(this.procedureTableObj != null && !this.procedureTableObj.getValues().isEmpty()) {
			List<ProcedureDTO> procedureList = this.procedureTableObj.getValues();
			for (ProcedureDTO procedureDTO : procedureList) {
				if(null != procedureDTO.getConsiderForPayment() && procedureDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)) {
					procedureIsConsiderForPayment = false;
					break;
				} else {
					procedureIsConsiderForPayment = true;
				}
			}
		} else {
				procedureIsConsiderForPayment =true;
		}*/
		
//		if (diagnosisIsConsiderForPayment && procedureIsConsiderForPayment){
//			hasError = true;
//			eMsg += SHAConstants.DIAGNOSIS_ERROR_MSG +"</br>";
//		}
		
		if (this.diagnosisListenerTableObj != null){
			boolean isValid = this.diagnosisListenerTableObj.isValid();
			if(!isValid){
				hasError = true;
				List<String> errors = this.diagnosisListenerTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		
		if (this.procedureTableObj != null) {
			boolean isValid = this.procedureTableObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.procedureTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}

		if (this.newProcedurdTableObj != null) {
			boolean isValid = this.newProcedurdTableObj.isValid();
			boolean isValidProcedure = this.newProcedurdTableObj.isValidProcedure();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.newProcedurdTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
			
			if (!isValidProcedure) {
				hasError = true;
				List<String> errors = this.newProcedurdTableObj.getProcedureErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		
		

		if (this.specialityTableObj != null) {
			boolean isValid = this.specialityTableObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.specialityTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		// Section details included for Comprehensive product. Remaining products, the Hospitalization will be the default value.........
		if(this.sectionDetailsListenerTableObj != null && !sectionDetailsListenerTableObj.isValid(true)) {
			hasError = true;
			List<String> errors = this.sectionDetailsListenerTableObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		if(this.corpBufferChk != null && this.corpBufferChk.getValue() != null && this.corpBufferChk.getValue()){
			if(this.txtCorpBufferAllocatedAmt != null && this.txtCorpBufferAllocatedAmt.getValue() != null && this.txtCorpBufferAllocatedAmt.getValue().equalsIgnoreCase("") ){
				txtCorpBufferAllocatedAmt.setValidationVisible(true);
				hasError = true;
				eMsg.append("Enter Corporate Buffer Limit Amount. </br>");
			}
			
		}
		
		/*if(cmbTreatmentType != null && cmbTreatmentType.getValue() != null && cmbTreatmentType.getValue().toString().toLowerCase()
		.contains("medical")) {
	
			if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) &&
					(null != bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() &&
					(ReferenceTable.HOSPITALISATION_SURGICAL_SECTION_CODE.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue())))){
				hasError = true;
				eMsg.append("Please Change the Treatement Type as Surgical since Section is Hospitalisation Surgical.</br>");
			}
		}else if(cmbTreatmentType != null && cmbTreatmentType.getValue() != null && cmbTreatmentType.getValue().toString().toLowerCase()
				.contains("surgical")) {
			if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) &&
					(null != bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() &&
					(ReferenceTable.HOSP_NON_SURGICAL_SECTION_CODE.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue())))){
				hasError = true;
				eMsg.append("Please Change the Treatement Type as Medical since Section is Hospitalisation Non-surgical.</br>");
			}
		}*/
		
		if(this.txtAmtClaimed != null && (this.txtAmtClaimed.getValue() == null || this.txtAmtClaimed.getValue().equalsIgnoreCase("")) ){
			txtAmtClaimed.setValidationVisible(true);
			hasError = true;
			eMsg.append("Enter Claimed Amount. </br>");
		}
		
		if(this.txtDiscntHospBill != null && (this.txtDiscntHospBill.getValue() == null || this.txtDiscntHospBill.getValue().equalsIgnoreCase("")) ){
			txtDiscntHospBill.setValidationVisible(true);
			hasError = true;
			eMsg.append("Enter Discount in Hospital Bill. </br>");
		}
		
		if(this.txtNetAmt != null && this.txtNetAmt.getValue() != null && !this.txtNetAmt.getValue().equalsIgnoreCase("") && this.claimedDetailsTableObj != null){
			Integer totalNetAmtforAmtconsd = claimedDetailsTableObj.getTotalClaimedAmt();
			Integer netAmt = Integer.valueOf(txtNetAmt.getValue());
			if(totalNetAmtforAmtconsd != null && !(totalNetAmtforAmtconsd.equals(netAmt))){
				hasError = true;
				eMsg.append("Claimed amount after discount to be equal to Claimed amount. </br>");
			}
		}
		
		
		
		
		if (hasError) {
			/*setRequired(true);
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
			dialog.show(getUI().getCurrent(), null, true);*/
			setRequired(true);
			MessageBox.createError()
	    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
	        .withOkButton(ButtonOption.caption("OK")).open();

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
				this.bean.setAmountConsidered(this.claimedDetailsTableObj.getTotalPayableAmt().toString());
				if(admissionDate != null && admissionDate.getValue() != null){
					bean.setDateOfAdmission(admissionDate.getValue());
				}
				
				if(this.bean.getDeletedDiagnosis().isEmpty()) {
					this.bean.setDeletedDiagnosis(this.diagnosisListenerTableObj.deletedDTO);
				} else {
					List<DiagnosisDetailsTableDTO> deletedDTO = this.diagnosisListenerTableObj.deletedDTO;
					for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDTO) {
						if(!this.bean.getDeletedDiagnosis().contains(diagnosisDetailsTableDTO)) {
							this.bean.getDeletedDiagnosis().add(diagnosisDetailsTableDTO);
						}
						
					}
				}
				
				/**			  
				 * Part of CR R1136
				 */
				
				List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisListenerTableObj.getValues();
				if(diagnosisList != null && !diagnosisList.isEmpty()){
					StringBuffer selectedSublimitNames = new StringBuffer("");
					for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
						if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getSublimitName() != null ){
							selectedSublimitNames = selectedSublimitNames.toString().isEmpty() ? selectedSublimitNames.append(diagnosisDetailsTableDTO.getSublimitName().getName()) : selectedSublimitNames.append(", ").append(diagnosisDetailsTableDTO.getSublimitName().getName());
						}
					}
					if(!selectedSublimitNames.toString().isEmpty()){
						Collection<Window> windows = UI.getCurrent().getWindows();
						for (Window window : windows) {
							if(window.getId() != null && window.getId().equalsIgnoreCase("sublimitAlert")){
								window.close();
								break;
							}
						}
						StarCommonUtils.showPopup(getUI(),selectedSublimitNames.insert(0, "Sublimit selected is ").toString(),null);
					}
				}
				//============= END of CR R1136 - ICD Sublimit Map ==========================================================
				
				Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
				
				MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
//				bean.getPolicyDto().getPolicyType();
				Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate.getValue());
				if((diffDays != 0 && diffDays > 90) || (policyType != null && policyType.getKey().equals(ReferenceTable.RENEWAL_POLICY)) || !bean.getAdmissionDatePopup()){
					this.bean.setAlertMessageOpened(true);
				}
				if(SHAUtils.getDialysisDiagnosisDTO(bean.getPreauthDataExtractionDetails().getDiagnosisTableList()) != null || SHAUtils.getDialysisProcedureDTO(bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList()) != null) {
					this.bean.setIsDialysis(true);
				}
				
				if(this.bean.getPreauthDataExtractionDetails().getNoOfDays() == null || (this.bean.getPreauthDataExtractionDetails().getNoOfDays() != null
						&& this.bean.getPreauthDataExtractionDetails().getNoOfDays().equalsIgnoreCase(""))){
					this.bean.getPreauthDataExtractionDetails().setNoOfDays("0");
					
				}
				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					if(corpBufferChk != null && corpBufferChk.getValue() != null && corpBufferChk.getValue() && txtCorpBufferAllocatedAmt != null){
						if(txtCorpBufferAllocatedAmt.getValue() != null && ! txtCorpBufferAllocatedAmt.getValue().isEmpty()){
							bean.getPreauthDataExtractionDetails().setCorpBufferAllocatedClaim(SHAUtils.getIntegerFromStringWithComma(txtCorpBufferAllocatedAmt.getValue()));
						}
						
					}
				}
				
				/*if(cmbPatientStatus.getValue() != null 
						&& (this.cmbPatientStatus.getValue().toString().toLowerCase().contains("deceased"))
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {

					if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()){
						List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
					
						if(tableList != null && !tableList.isEmpty()){
							bean.getNewIntimationDTO().setNomineeList(tableList);
							StringBuffer nomineeNames = new StringBuffer("");
							int selectCnt = 0;
							for (NomineeDetailsDto nomineeDetailsDto : tableList) {
								nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
								if(nomineeDetailsDto.isSelectedNominee()) {
									nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
								    selectCnt++;	
								}
							}
							bean.getNewIntimationDTO().setNomineeSelectCount(selectCnt);
							if(selectCnt>0){
								bean.getNewIntimationDTO().setNomineeName(nomineeNames.toString());
								bean.getNewIntimationDTO().setNomineeAddr(null);
							}
							else{
								Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
								if((legalHeirMap.get("FNAME") != null && !legalHeirMap.get("FNAME").toString().isEmpty())
										&& (legalHeirMap.get("ADDR") != null && !legalHeirMap.get("ADDR").toString().isEmpty()))
								{
									bean.getNewIntimationDTO().setNomineeName(legalHeirMap.get("FNAME").toString());
									bean.getNewIntimationDTO().setNomineeAddr(legalHeirMap.get("ADDR").toString());
									
								}		
							}							
						}
					}
					else{
						bean.getNewIntimationDTO().setNomineeList(null);
						bean.getNewIntimationDTO().setNomineeName(null);
						Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
						if((legalHeirMap.get("FNAME") != null && !legalHeirMap.get("FNAME").toString().isEmpty())
								&& (legalHeirMap.get("ADDR") != null && !legalHeirMap.get("ADDR").toString().isEmpty()))
						{
							bean.getNewIntimationDTO().setNomineeName(legalHeirMap.get("FNAME").toString());
							bean.getNewIntimationDTO().setNomineeAddr(legalHeirMap.get("ADDR").toString());
							
						}	
					}					
				}*/				
				
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
	}
	
	/*private String validateProcedureAndDiagnosisName()
	{
		String eMsg = "";
		StringBuffer strBuf = new StringBuffer();
		List<DiagnosisDetailsTableDTO> diagList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		List<ProcedureDTO> procList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		if((null != diagList && !diagList.isEmpty()) && (null != procList && !procList.isEmpty()))
		{
			for(DiagnosisDetailsTableDTO diagObj : diagList)
			{
				String strDiagName = diagObj.getDiagnosis();
				
				for(ProcedureDTO procObj : procList)
				{
					String strProcName = procObj.getProcedureNameValue();
					if(strDiagName.equals(strProcName))
					{
						strBuf.append("Diagnosis and Procedure are same. Diagnosis"+" "+ strDiagName).append("\n");
					}
				}
			}
		}
		
		if(null != strBuf && strBuf.length() > 0)
		{
			eMsg = strBuf.toString();
		}
		
		return eMsg;
	}
	*/
	private PedDetailsTableDTO setPEDDetailsToDTO(DiagnosisDetailsTableDTO diagnosisDetailsTableDTO,
			InsuredPedDetails pedList) {
		PedDetailsTableDTO dto = new PedDetailsTableDTO();
		dto.setDiagnosisName(diagnosisDetailsTableDTO.getDiagnosis());
		dto.setEnableOrDisable(diagnosisDetailsTableDTO.getEnableOrDisable());
		dto.setPolicyAgeing(diagnosisDetailsTableDTO.getPolicyAgeing());
		if(pedList == null) {
			dto.setPedCode("");
			dto.setPedName("");
		} else {
			dto.setPedCode(pedList.getPedCode());
			dto.setPedName(pedList.getPedDescription());
		}
		return dto;
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public void setTableValuesToDTO() {
		this.bean.getPreauthDataExtractionDetails().setSpecialityList(
				this.specialityTableObj != null ? this.specialityTableObj
						.getValues() : new ArrayList<SpecialityDTO>());
		this.bean.getPreauthDataExtractionDetails().setNewProcedureList(
				this.newProcedurdTableObj != null ?  getProcedureVariationList(this.newProcedurdTableObj.getValues(), 1l)  : new ArrayList<ProcedureDTO>());
		this.bean.getPreauthDataExtractionDetails().setProcedureList(this.procedureTableObj != null ? getProcedureVariationList(this.procedureTableObj.getValues(), 0l) : new ArrayList<ProcedureDTO>());
		
		List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisListenerTableObj.getValues();
		List<InsuredPedDetails> tmpPEDList = (List<InsuredPedDetails>) referenceData.get("insuredPedList");
		String policyAgeing = (String) referenceData.get("policyAgeing");
		
		for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
			if(diagnosisDetailsTableDTO.getDiagnosisName() != null && diagnosisDetailsTableDTO.getDiagnosisName().getValue() != null) {
				diagnosisDetailsTableDTO.setDiagnosis(diagnosisDetailsTableDTO.getDiagnosisName().getValue());
			}
			if(diagnosisDetailsTableDTO.getDiagnosisName() != null) {
				diagnosisDetailsTableDTO.setDiagnosisId(diagnosisDetailsTableDTO.getDiagnosisName().getId());
			}
			
			
			diagnosisDetailsTableDTO.setPolicyAgeing(policyAgeing);
			List<PedDetailsTableDTO> list = new ArrayList<PedDetailsTableDTO>();
			if(diagnosisDetailsTableDTO.getPedList().isEmpty()) {
				if(!tmpPEDList.isEmpty()) {
					for (InsuredPedDetails insuredPEDDetails : tmpPEDList) {
						PedDetailsTableDTO setPEDDetailsToDTO = setPEDDetailsToDTO(diagnosisDetailsTableDTO, insuredPEDDetails);
						list.add(setPEDDetailsToDTO);
					}
					
				}  else {
					PedDetailsTableDTO setPEDDetailsToDTO = setPEDDetailsToDTO(diagnosisDetailsTableDTO, null);
					list.add(setPEDDetailsToDTO);
				}
				diagnosisDetailsTableDTO.setPedList(list);
			} else {
				List<PedDetailsTableDTO> pedList = diagnosisDetailsTableDTO.getPedList();
				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
					pedDetailsTableDTO.setDiagnosisName(diagnosisDetailsTableDTO.getDiagnosis());
				}
			}
		}
		
		/** 
		 * Part of CR R1136 - ICD Sublimit Map
		 */

		
		this.bean.getPreauthDataExtractionDetails().setDiagnosisTableList(diagnosisList);
		
		List<SectionDetailsTableDTO> sectionDetailsList = this.sectionDetailsListenerTableObj.getValues();
		this.bean.getPreauthDataExtractionDetails().setSectionDetailsDTO(sectionDetailsList.get(0));
		
		List<ProcedureDTO> wholeProcedureList = new ArrayList<ProcedureDTO>();
		wholeProcedureList.addAll(this.bean.getPreauthDataExtractionDetails().getProcedureList());
		wholeProcedureList.addAll(this.bean.getPreauthDataExtractionDetails().getNewProcedureList());
		
		this.bean.getPreauthMedicalProcessingDetails().setProcedureExclusionCheckTableList(wholeProcedureList);
		
		List<NoOfDaysCell> values = this.claimedDetailsTableObj.getValues();
		List<NoOfDaysCell> claimedDetailsList = this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList();
		
		if(!claimedDetailsList.isEmpty()) {
			for (NoOfDaysCell oldNoOfCell : claimedDetailsList) {
				for (NoOfDaysCell newNoOfDaysCell : values) {
					if(oldNoOfCell.getBenefitId() == newNoOfDaysCell.getBenefitId()) {
						newNoOfDaysCell.setKey(oldNoOfCell.getKey());
						break;
					}
				}
			}
		}
		this.bean.setDeletedClaimedAmountIds(this.claimedDetailsTableObj.getDeletedItems());
		this.bean.getPreauthDataExtractionDetails().setClaimedDetailsList(this.claimedDetailsTableObj != null ?  values: new ArrayList<NoOfDaysCell>());
	}

	private List<ProcedureDTO> getProcedureVariationList(List<ProcedureDTO> procedureDTOList, Long isNewProcedure)  {
		if(!procedureDTOList.isEmpty()) {
			SelectValue procedureName = null;
			SelectValue procedureCode = null;
			for (ProcedureDTO procedureDTO : procedureDTOList) {
				if(isNewProcedure == 0) {
					procedureDTO.setProcedureNameValue(procedureDTO.getProcedureName() != null ? procedureDTO.getProcedureName().getValue() : null);
					procedureDTO.setProcedureCodeValue(procedureDTO.getProcedureCode() != null ? procedureDTO.getProcedureCode().getValue() : null);
				} else {
					procedureName = new SelectValue();
					procedureName.setValue(procedureDTO.getProcedureNameValue());
					procedureDTO.setProcedureName(procedureName);
					procedureCode = new SelectValue();
					procedureCode.setValue(procedureDTO.getProcedureCodeValue());
					procedureDTO.setProcedureCode(procedureCode);
				}
				
			//	procedureDTOList
				procedureDTO.setNewProcedureFlag(isNewProcedure);
				procedureDTO.setPolicyAging(referenceData.containsKey("policyAgeing") ? (String)referenceData.get("policyAgeing") : null);
			}
		}
		return procedureDTOList;
	}
	
	private void setTableValues() {
		
		if(this.specialityTableObj != null) {
			List<SpecialityDTO> specialityList = this.bean.getPreauthDataExtractionDetails().getSpecialityList();
			for (SpecialityDTO specialityDTO : specialityList) {
				if(null != specialityDTO && null != specialityDTO.getEnableOrDisable() && !specialityDTO.getEnableOrDisable())
				{
					specialityDTO.setStatusFlag(false);
					specialityDTO.setEnableOrDisable(false);
				}
				else
				{
					specialityDTO.setStatusFlag(true);
					specialityDTO.setEnableOrDisable(true);
				}
				this.specialityTableObj.addBeanToList(specialityDTO);
			}
		}
		
		List<ProcedureDTO> procedureExclusionCheckTableList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		List<ProcedureDTO> oldProcedureDTOList = new ArrayList<ProcedureDTO>();
	
		List<ProcedureDTO> newProcedureDTOList = new ArrayList<ProcedureDTO>();
		if(!procedureExclusionCheckTableList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				if(procedureDTO.getNewProcedureFlag() == 1) {
					newProcedureDTOList.add(procedureDTO);
				} else {
					oldProcedureDTOList.add(procedureDTO);
				}
			}
		}
		this.bean.getPreauthDataExtractionDetails().setProcedureList(oldProcedureDTOList);
		this.bean.getPreauthDataExtractionDetails().setNewProcedureList(newProcedureDTOList);
		if(this.newProcedurdTableObj != null) {
			List<ProcedureDTO> newProcedureList = this.bean.getPreauthDataExtractionDetails().getNewProcedureList();
			for (ProcedureDTO newProcedureTableDTO : newProcedureList) {
				this.newProcedurdTableObj.addBeanToList(newProcedureTableDTO);
			}
		}
		
		if(this.procedureTableObj != null) {
			List<ProcedureDTO> procedureList = this.bean.getPreauthDataExtractionDetails().getProcedureList();
			for (ProcedureDTO procedureTableDTO : procedureList) {
				this.procedureTableObj.addBeanToList(procedureTableDTO);
			}
		}
		
		if(this.diagnosisListenerTableObj != null) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO diagnosisDTO : diagnosisList) {
				this.diagnosisListenerTableObj.addBeanToList(diagnosisDTO);
			}
		}
		
		if(this.claimedDetailsTableObj != null) {
			this.claimedDetailsTableObj.setValues(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList(), false);
		}
		
		if(this.bean.getDeletedClaimedAmountIds() != null && !this.bean.getDeletedClaimedAmountIds().isEmpty()) {
			this.claimedDetailsTableObj.setDeletedItems(this.bean.getDeletedClaimedAmountIds());
		}
		
		if(this.bean.getPreauthDataExtractionDetails().getOtherBenfitFlag() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getOtherBenfitFlag())){
			this.otherBenefitsOption.setValue(true);
			if(this.benefitsTableObj != null){
				if(this.bean.getPreauthDataExtractionDetails().getOtherBenefitsList() != null){
					this.benefitsTableObj.addList(this.bean.getPreauthDataExtractionDetails().getOtherBenefitsList());
					benefitsTableObj.setDefaultValues();
				}
			}
		 }	
	}

	@Override
	public boolean onBack() {
		//isBackClicked = true;
		return true;
	}

	@Override
	public boolean onSave() {
		setTableValuesToDTO();
		return validatePage();
	}

	public void editSpecifyVisible(Boolean checkValue) {
		
		if (checkValue) {
			cmbSpecifyIllness.setReadOnly(false);
		} else {
			
			cmbSpecifyIllness.setValue(null);
			cmbSpecifyIllness.setNullSelectionAllowed(true);
		
			cmbSpecifyIllness.setReadOnly(true);
		}
	}
	
	
	protected void addAdmissionDateChangeListener()
	{
		
			admissionDate.setValue(this.bean.getDateOfAdmission());

		admissionDate.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				admissionDate.setValue(admissionDate.getValue());
				
				if(admissionDate.getValue() != null && (bean.getNewIntimationDTO().getAdmissionDate().compareTo(admissionDate.getValue()))!=0){
				
				reasonForChangeInDOA.setVisible(true);
				reasonForChangeInDOA.setRequired(true);
				/**
				 * If admission Date is changed, then reasonForChangeInDOA 
				 * is mandatory
				 * */
				mandatoryFields.add(reasonForChangeInDOA);
				}
				else{
					reasonForChangeInDOA.setValue(null);
					unbindField(reasonForChangeInDOA);
					reasonForChangeInDOA.setVisible(false);
					reasonForChangeInDOA.setRequired(false);
					mandatoryFields.remove(reasonForChangeInDOA);
				}
				
				// TODO Auto-generated method stub
				
			}
		});
	}

	protected void addListener() {
		
		criticalIllnessChk
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean checkValue = criticalIllnessChk.getValue();
						fireViewEvent(
								PreMedicalPreauthWizardPresenter.CHECK_CRITICAL_ILLNESS,
								checkValue, true);
					}
				});

		cmbTreatmentType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && cmbTreatmentType.getValue().toString().toLowerCase()
						.contains("medical")) {
					
					/*if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) &&
							(null != bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() &&
							(ReferenceTable.HOSPITALISATION_SURGICAL_SECTION_CODE.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue())))){
						
						StarCommonUtils.alertMessage(getUI(), "Please Change the Treatement Type as Surgical since Section is Hospitalisation Surgical.");
						cmbTreatmentType.setValue(null);
					}*/
					
					if(!bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().isEmpty()) {
						alertMessageForChangeOfTreatmentType();
					} else {
						fireViewEvent(
								PreMedicalPreauthWizardPresenter.PREMEDICAL_TREATMENT_TYPE_CHANGED,
								null);
					}
				} else {
					fireViewEvent(
							PreMedicalPreauthWizardPresenter.PREMEDICAL_TREATMENT_TYPE_CHANGED,
							null);
				}
				
				
			}
		});
		
             cmbRoomCategory.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue value = (SelectValue) event.getProperty().getValue();
				bean.getNewIntimationDTO().setRoomCategory(value);
				
//				List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
//				
//				if(procedureTableObj != null){
//					List<ProcedureDTO> values = procedureTableObj.getValues();
//					procedureList.addAll(values);
//					procedureTableObj.removeRow();
//					for (ProcedureDTO procedureDTO : procedureList) {
////						procedureDTO.setProcedureCode(null);
//						procedureTableObj.addBeanToList(procedureDTO);
//					}
//				}
			
			}
		});
             
             cmbSection.addValueChangeListener(new ValueChangeListener() {
     			
     			@Override
     			public void valueChange(ValueChangeEvent event) {
     				
     				if(cmbSection != null && cmbSection.getValue() != null){
     					SelectValue sectionValue = (SelectValue) cmbSection.getValue();
     					bean.getPreauthDataExtractionDetails().setSection(sectionValue);
     				
     				
     				if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
       						(ReferenceTable.STAR_CARDIAC_CARE.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))||
       						(ReferenceTable.STAR_CARDIAC_CARE_NEW.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
     					
       					
       				if(null != bean.getPreauthDataExtractionDetails().getSection().getValue() &&
       						(SHAConstants.SECTION_2.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getSection().getValue()))){ 					
       				
       					Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
       					Long diffDays = 0l;
       					if(admissionDate.getValue() != null){
       						diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate.getValue());
       					}
       					if(null != bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey() &&
       							(ReferenceTable.FRESH_POLICY.equals(bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey()))
       							&& diffDays < 90){
       						cardiaCareSectionAlert();       						
       					}
       					else if(null != bean.getNewIntimationDTO().getPolicy().getPolicyPlan())
       					{        						
       						alertMessageForPlocyPlan();				
       					}
       						
       				}       					
       					
       				}
     			}
       				
     				
     				fireViewEvent(PreMedicalPreauthWizardPresenter.SUBLIMIT_CHANGED_BY_SECTION, bean);
     				

     				List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();
       				
       				diagnosisList.addAll(diagnosisListenerTableObj.getValues());
       				
       				diagnosisListenerTableObj.removeAllItems();
       				diagnosisListenerTableObj.clearTableItems();

     				diagnosisListenerTableObj.init(bean,"premedicalPreauth");
     				diagnosisListenerTableObj.setReferenceData(referenceData);
     				
     				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
     					diagnosisDetailsTableDTO.setSublimitName(null);
     					diagnosisListenerTableObj.addBeanToList(diagnosisDetailsTableDTO);
						
					}
 
     				if(procedureTableObj != null) {
     					
     					List<ProcedureDTO> procedureDTO = new ArrayList<ProcedureDTO>();
     					
     					procedureDTO = procedureTableObj.getValues();
     				
     					procedureTableObj.init(bean.getHospitalCode(), "premedicalPreauth", bean);
     					procedureTableObj.setReferenceData(referenceData);
     					procedureTableObj.setEnabled(false);
     					
                        for (ProcedureDTO procedureDTO2 : procedureDTO) {
							procedureTableObj.addBeanToList(procedureDTO2);
						}
     				}
     				
     				if(claimedDetailsTableObj != null){
     					
     					List<NoOfDaysCell> claimedAmountValues = new ArrayList<NoOfDaysCell>();
     					
     					claimedAmountValues.addAll(claimedDetailsTableObj.getValues());
     					
//     					claimedDetailsTableObj.initView(bean, SHAConstants.PRE_MEDICAL_PRE_AUTH);
     					Map<Integer,Object> values = (Map<Integer,Object>)referenceData.get("claimDBDetails");
     					claimedDetailsTableObj.setDBCalculationValues(values);
     					for (NoOfDaysCell noOfDaysCell : claimedAmountValues) {
							if(noOfDaysCell.getBenefitId() != null && values.containsKey(noOfDaysCell.getBenefitId().intValue())) {
								noOfDaysCell.setPolicyPerDayPayment(((Double)values.get(noOfDaysCell.getBenefitId().intValue())).intValue());
							}
						}
     					claimedDetailsTableObj.setValuesForSectionChange(claimedAmountValues, false);
     					
     				}
     				
//     				List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
//     				
//     				if(procedureTableObj != null){
//     					List<ProcedureDTO> values = procedureTableObj.getValues();
//     					procedureList.addAll(values);
//     					procedureTableObj.removeRow();
//     					for (ProcedureDTO procedureDTO : procedureList) {
     				
////     						procedureDTO.setProcedureCode(null);
//     						procedureTableObj.addBeanToList(procedureDTO);
//     					}
//     				}
     			
     			}
     		});
             
             
		
		cmbNatureOfTreatment.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && value.getValue() != null && value.getValue().toString().toLowerCase().contains("non")) {
					Collection<?> itemIds = cmbTreatmentType.getContainerDataSource().getItemIds();
					cmbTreatmentType.setValue(itemIds.toArray()[0]);
					cmbTreatmentType.setEnabled(false);
				} else {
//					cmbTreatmentType.setEnabled(true);
				}
			}
		});

		cmbPatientStatus.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				fireViewEvent(
						PreMedicalPreauthWizardPresenter.PREMEDICAL_PATIENT_STATUS_CHANGED,
						null);
			}
		});
		
		admissionDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) event.getProperty().getValue();
				if(enteredDate != null) {/*
					Date policyFromDate = bean.getPolicyDto().getPolicyFromDate();
					Date policyToDate = bean.getPolicyDto().getPolicyToDate();
					
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setClosable(false);
					dialog.setResizable(false);
					
					if (!( DateUtils.isSameDay(enteredDate, policyFromDate) || enteredDate.after(policyFromDate)) || !(DateUtils.isSameDay(enteredDate, policyToDate) || enteredDate.before(policyToDate) )) {
						
						 Button okButton = new Button("OK");
						 okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;

							@Override
							public void buttonClick(ClickEvent event) {
								dialog.close();
							}
						});
						HorizontalLayout hLayout = new HorizontalLayout(okButton);
						hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
						hLayout.setMargin(true);
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Admission Date is not in range between Policy From Date and Policy To Date. </b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						dialog.show(getUI().getCurrent(), null, true);
						
						event.getProperty().setValue(null);
					}
				*/

					Date policyFromDate = bean.getPolicyDto().getPolicyFromDate();
					Date policyToDate = bean.getPolicyDto().getPolicyToDate();
					
					
					if (!( DateUtils.isSameDay(enteredDate, policyFromDate) || enteredDate.after(policyFromDate)) || !(DateUtils.isSameDay(enteredDate, policyToDate) || enteredDate.before(policyToDate) )) {
						
						final MessageBox showError = showErrorMessageBox("Admission Date is not in range between Policy From Date and Policy To Date.");
						Button okButton = showError.getButton(ButtonType.OK);
						 okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;

							@Override
							public void buttonClick(ClickEvent event) {
								showError.close();
							}
						});
						
						event.getProperty().setValue(null);
					}
					
				}
				
			}
		});
		
		cmbHospitalisationDueTo.addValueChangeListener(new ValueChangeListener() {/*
			private static final long serialVersionUID = -2577540521492098375L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
										
					
					if(value != null && value.getValue() != null && !value.getValue().isEmpty() && (ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT).equals(value.getId())){
						
						Window window = new Window();
						window.setResizable(true);
						window.setCaption("ALERT");
						window.setModal(true);
						Label alertMsg = new Label("<B>Waiting period of 36 Months and once in block of THREE YEARS are applicable and <BR>Both HUSBAND and WIFE to be covered during the period of 36 Months</B>", ContentMode.HTML);
						alertMsg.setSizeFull();
						VerticalLayout vlayout = new VerticalLayout(alertMsg);
						vlayout.setSizeFull();
						vlayout.setMargin(true);
						vlayout.setComponentAlignment(alertMsg, Alignment.MIDDLE_CENTER);
						window.setContent(alertMsg);
						window.setWidth("530px");
						window.setHeight("100px");
						window.setResizable(false);
						window.center();
						window.setStyleName(Reindeer.WINDOW_BLACK);
						UI.getCurrent().addWindow(window);
					}
				}
				fireViewEvent(
						PreMedicalPreauthWizardPresenter.PREMEDICAL_HOSPITALISATION_DUE_TO,
						bean);
				
				
////				if(value != null) {
//					fireViewEvent(
//							PreMedicalPreauthWizardPresenter.PREMEDICAL_HOSPITALISATION_DUE_TO,
//							value);
////				}
				
			}
		*/

			private static final long serialVersionUID = -2577540521492098375L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
										
					
					if(value != null && value.getValue() != null && !value.getValue().isEmpty() && (ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT).equals(value.getId())){
						final MessageBox showAlert = showAlertMessageBox("Waiting period of 36 Months and once in block of THREE YEARS are applicable and \nBoth HUSBAND and WIFE to be covered during the period of 36 Months");
						Button homeButton = showAlert.getButton(ButtonType.OK);
					}
				}
				fireViewEvent(
						PreMedicalPreauthWizardPresenter.PREMEDICAL_HOSPITALISATION_DUE_TO,
						bean);
				
				
////				if(value != null) {
//					fireViewEvent(
//							PreMedicalPreauthWizardPresenter.PREMEDICAL_HOSPITALISATION_DUE_TO,
//							value);
////				}
				
			}
			
		
		});
		
		this.sectionDetailsListenerTableObj.dummySubCoverField.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -7831804284490287934L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField property = (TextField) event.getProperty();
				String value = property.getValue();
				if(value != null) {
					fireViewEvent(PreMedicalPreauthWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, bean);
					if(diagnosisListenerTableObj != null && !diagnosisListenerTableObj.getValues().isEmpty()) {
						diagnosisListenerTableObj.changeSublimitValues();
					} 
					if(procedureTableObj != null && !procedureTableObj.getValues().isEmpty()) {
						procedureTableObj.changeSublimitValues();
					}
				}
			}
		});
		
		cmbCategory.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue categoryValues = (SelectValue) event.getProperty().getValue();
				
				if (categoryValues != null && categoryValues.getValue() != null 
						&& categoryValues.getValue().equalsIgnoreCase("FEVER")) {
					cmbTreatmentType.setEnabled(false);
				}
				else {
//					cmbTreatmentType.setEnabled(true);
				}
				
			}
		});
	}
	
	public void causeOfInsuryListener(){
		
		if(cmbCauseOfInjury != null){
			cmbCauseOfInjury.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					SelectValue value = (SelectValue) event.getProperty().getValue();
					if(value != null && value.getId() != null && value.getId().equals(1102L)){
						if(btnRTAView != null){
							btnRTAView.setEnabled(true);
						}
					}else{
						if(btnRTAView != null){
							btnRTAView.setEnabled(false);
						}
					}
					

				
				}
			});
		}
	}
	
	public void addCorporateBufferListener(){
		if(corpBufferChk != null){
			corpBufferChk.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
	
					Boolean checkValue = corpBufferChk.getValue();
					if(checkValue){
						if(corpBufferHLayout != null){
							corpBufferHLayout.setVisible(true);
		                }
						txtCorpBufferAllocatedAmt.setRequired(true);
						isCorpBufferChecked = true;
					}else{
						if(txtCorpBufferAllocatedAmt != null && txtCorpBufferAllocatedAmt.getValue() != null){
							txtCorpBufferAllocatedAmt.setValue("");
						}if(corpBufferHLayout != null){
		                	corpBufferHLayout.setVisible(false);
		                }
						if(txtCorpBufferAllocatedAmt != null){
							txtCorpBufferAllocatedAmt.setRequired(false);
						}
		                isCorpBufferChecked = false;
					}
	                
				
				}
			});
		}
	}
	
	/**
	 * Method to validate death date.
	 * 
	 * */
	private void addDeathDateValueChangeListener()
	{
		deathDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {/*
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setClosable(false);
				dialog.setResizable(false);
				Date enteredDate = (Date) event.getProperty().getValue();
				if(null != enteredDate && null != admissionDate && null != admissionDate.getValue())
					if(!SHAUtils.validateDeathDate(enteredDate,admissionDate.getValue()))
					{
						 Button okButton = new Button("OK");
						 okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;

							@Override
							public void buttonClick(ClickEvent event) {
								dialog.close();
							}
						});
						HorizontalLayout hLayout = new HorizontalLayout(okButton);
						hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
						hLayout.setMargin(true);
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Please select valid death date. Death date is not in range between admission date and current date.</b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						dialog.show(getUI().getCurrent(), null, true);
						
						event.getProperty().setValue(null);
					}
			*/

				final MessageBox showError = showErrorMessageBox("Please select valid death date. Death date is not in range between admission date and current date.");
				
				Date enteredDate = (Date) event.getProperty().getValue();
				if(null != enteredDate && null != admissionDate && null != admissionDate.getValue())
					if(!SHAUtils.validateDeathDate(enteredDate,admissionDate.getValue()))
					{
						Button okButton = showError.getButton(ButtonType.OK);
						 okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;

							@Override
							public void buttonClick(ClickEvent event) {
								showError.close();
							}
						});
						
						event.getProperty().setValue(null);
					}
				
			}
		});
	}
	
   public Boolean alertMessage() {/*
		

   		Label successLabel = new Label(
				"<b style = 'color: red;'> Close Proximity Claim</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
			    isValid = true;
			    bean.setAlertMessageOpened(true);
			    if(!bean.getIsDialysis()) {
			    	wizard.next();
			    }
			    dialog.close();

			}
		});

		return isValid;

	*/
	   final MessageBox showInfoMessageBox = showInfoMessageBox("Date of Admission is less than 90 days from the Policy inception Date!!!");
		Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
			    isValid = true;
			    bean.setAlertMessageOpened(true);
			    if(!bean.getIsDialysis()) {
			    	wizard.next();
			    }
			    showInfoMessageBox.close();

			}
		});

		return isValid;   
   }
   
	  public void alertMessageForChangeOfTreatmentType() {/*
		  final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("Yes");
			Button cancelButton = new Button("No");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					List<ProcedureDTO> procedureExclusionCheckTableList = bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
                	for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
                		if(!bean.getDeletedProcedure().contains(procedureDTO) && procedureDTO.getKey() != null) {
	        				bean.getDeletedProcedure().add(procedureDTO);
	        			}
					}
                	
                	bean.getPreauthMedicalProcessingDetails().setProcedureExclusionCheckTableList(new ArrayList<ProcedureDTO>());
                	fireViewEvent(
							PreMedicalPreauthWizardPresenter.PREMEDICAL_TREATMENT_TYPE_CHANGED,
							null);
					dialog.close();
				}
			});
			
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					cmbTreatmentType.setValue(cmbTreatmentType.getItemIds().toArray()[1]);
					dialog.close();
				}
			});
			HorizontalLayout hLayout = new HorizontalLayout(okButton, cancelButton);
			hLayout.setSpacing(true);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("Entered Procedure Will be marked as deleted one. Do you want to proceed further ?"), hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);
		*/
		   final MessageBox showConfirmationMessageBox = showConfirmationMessageBox("Entered Procedure Will be marked as deleted one. Do you want to proceed further ?");
		   Button okButton = showConfirmationMessageBox.getButton(ButtonType.YES);
				
				okButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						List<ProcedureDTO> procedureExclusionCheckTableList = bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
	                	for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
	                		if(!bean.getDeletedProcedure().contains(procedureDTO) && procedureDTO.getKey() != null) {
		        				bean.getDeletedProcedure().add(procedureDTO);
		        			}
						}
	                	
	                	bean.getPreauthMedicalProcessingDetails().setProcedureExclusionCheckTableList(new ArrayList<ProcedureDTO>());
	                	fireViewEvent(
								PreMedicalPreauthWizardPresenter.PREMEDICAL_TREATMENT_TYPE_CHANGED,
								null);
	                	showConfirmationMessageBox.close();
					}
				});
				
	      Button cancelButton = showConfirmationMessageBox.getButton(ButtonType.NO);
				cancelButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						cmbTreatmentType.setValue(cmbTreatmentType.getItemIds().toArray()[1]);
						showConfirmationMessageBox.close();
					}
				});
	  }

	
	/*private void claimDetailsNoOfDaysListener() {
		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = -6214266810135299292L;

			@Override
			public void blur(BlurEvent event) {
//				fireViewEvent(PreMedicalPreauthWizardPresenter, primaryParameter, secondaryParameters);
			}
		};

	}*/

	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && field.isAttached() && propertyId != null) {
				this.binder.unbind(field);
			}
		}
	}

	public void generatedFieldsBasedOnTreatment() {
		if (cmbTreatmentType.getValue() != null) {
			Boolean isMedicalSeclted = true;
			if (cmbTreatmentType.getValue().toString().toLowerCase()
					.contains("medical")) {
				unbindField(treatmentRemarksTxt);
				treatmentRemarksTxt = (TextArea) binder.buildAndBind(
						"Treatment Remarks", "treatmentRemarks",
						TextArea.class);
				treatmentRemarksTxt.setWidth("400px");
				treatmentFLayout.addComponent(treatmentRemarksTxt);
//				treatmentRemarksTxt.setRequired(true);
//				treatmentRemarksTxt.setValidationVisible(false);
				treatmentRemarksTxt.setMaxLength(4000);
//				CSValidator validator = new CSValidator();
//				treatmentRemarksTxt.setMaxLength(100);
//				validator.extend(treatmentRemarksTxt);
//				validator.setRegExp("^[a-zA-Z 0-9]*$");
//				validator.setPreventInvalidTyping(true);
//				mandatoryFields.add(treatmentRemarksTxt);

				if (tableVLayout != null
						&& tableVLayout.getComponentCount() > 0) {
					tableVLayout.removeAllComponents();
				}
				this.procedureTableObj = null;
				this.newProcedurdTableObj = null;

			} else {
				if (treatmentRemarksTxt != null) {
					unbindField(treatmentRemarksTxt);
					treatmentFLayout.removeComponent(treatmentRemarksTxt);
//					mandatoryFields.remove(treatmentRemarksTxt);

				}
				isMedicalSeclted = false;
				ProcedureListenerTableForPremedical procedureTableInstance = procedureListenerTable.get();
				
				/*List<DiagnosisDetailsTableDTO> diagList = null;
				if(null != diagnosisListenerTableObj)
				{
					diagList = diagnosisListenerTableObj.getValues();
				}*/
				
				//procedureTableInstance.init(bean.getHospitalKey(), "premedicalPreauth", diagList);
				procedureTableInstance.init(bean.getHospitalCode(), "premedicalPreauth", bean);
				this.procedureTableObj = procedureTableInstance;
				this.procedureTableObj.diagnosisList = diagnosisList;
				this.procedureTableObj.setReferenceData(referenceData);
				procedureTableObj.setEnabled(false);

				NewProcedureTableList newProcedureTableListInstance = newProcedureTableList
						.get();
				newProcedureTableListInstance.init("Add New Procedure", true);
				newProcedureTableListInstance.setReference(referenceData);
				newProcedureTableListInstance.setEnabled(false);
				this.newProcedurdTableObj = newProcedureTableListInstance;
				this.newProcedurdTableObj.setPreauthDTO(bean);

				if (tableVLayout != null
						&& tableVLayout.getComponentCount() > 0) {
					tableVLayout.removeAllComponents();
				}
				tableVLayout.addComponent(procedureTableObj);
				tableVLayout.addComponent(newProcedurdTableObj);
				
				List<ProcedureDTO> procedureExclusionCheckTableList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
				List<ProcedureDTO> oldProcedureDTOList = new ArrayList<ProcedureDTO>();
			
				List<ProcedureDTO> newProcedureDTOList = new ArrayList<ProcedureDTO>();
				if(!procedureExclusionCheckTableList.isEmpty()) {
					for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
						if(null != procedureDTO && null != procedureDTO.getEnableOrDisable() && !procedureDTO.getEnableOrDisable())
						{
							procedureDTO.setEnableOrDisable(false);
						}
						else
						{
							procedureDTO.setEnableOrDisable(true);
						}
						procedureDTO.setStatusFlag(true);
						if(procedureDTO.getNewProcedureFlag() == 1) {
							newProcedureDTOList.add(procedureDTO);
						} else {
							oldProcedureDTOList.add(procedureDTO);
						}
					}
				}
				this.bean.getPreauthDataExtractionDetails().setProcedureList(oldProcedureDTOList);
				this.bean.getPreauthDataExtractionDetails().setNewProcedureList(newProcedureDTOList);
				if(this.newProcedurdTableObj != null) {
					List<ProcedureDTO> newProcedureList = this.bean.getPreauthDataExtractionDetails().getNewProcedureList();
					for (ProcedureDTO newProcedureTableDTO : newProcedureList) {
						newProcedureTableDTO.setEnableOrDisable(true);
						this.newProcedurdTableObj.addBeanToList(newProcedureTableDTO);
					}
				}
				
				if(this.procedureTableObj != null) {
					List<ProcedureDTO> procedureList = this.bean.getPreauthDataExtractionDetails().getProcedureList();
					for (ProcedureDTO procedureTableDTO : procedureList) {
						if(null != procedureTableDTO && null != procedureTableDTO.getEnableOrDisable() && !procedureTableDTO.getEnableOrDisable())
						{
							procedureTableDTO.setEnableOrDisable(false);
						}
						else
						{
							procedureTableDTO.setEnableOrDisable(true);
						}
						procedureTableDTO.setEnableOrDisable(true);
						this.procedureTableObj.addBeanToList(procedureTableDTO);
					}
				}

			}
			SpecialityTable specialityTableInstance = specialityTableList.get();
			specialityTableInstance.init("Speciality", true);
			referenceData.put("specialityType",
					referenceData.get("medicalSpeciality"));
			if (!isMedicalSeclted) {
				referenceData.put("specialityType",
						referenceData.get("surgicalSpeciality"));
			}
			if(cmbCategory != null && cmbCategory.getValue() != null && cmbCategory.getValue().toString().equalsIgnoreCase("FEVER")){
				referenceData.put("specialityType",
						referenceData.get("nextLOVSpeciality"));
			}
			specialityTableInstance.setReference(referenceData);
			specialityTableInstance.setEnabled(false);
			this.specialityTableObj = specialityTableInstance;
			tableVLayout.addComponent(specialityTableObj);
		} else {
			if (treatmentRemarksTxt != null) {
				unbindField(treatmentRemarksTxt);
				treatmentFLayout.removeComponent(treatmentRemarksTxt);
//				mandatoryFields.remove(treatmentRemarksTxt);
			}

			if (tableVLayout != null && tableVLayout.getComponentCount() > 0) {
				tableVLayout.removeAllComponents();
			}
			this.procedureTableObj = null;
			this.newProcedurdTableObj = null;
		}
	}

	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}

	public void generateFieldsBasedOnPatientStatus() {
		if (cmbPatientStatus.getValue() != null) {
			if (this.cmbPatientStatus.getValue().toString().toLowerCase()
					.contains("deceased")) {
				deathDate = (PopupDateField) binder.buildAndBind(
						"Date Of Death", "deathDate", PopupDateField.class);
				
				//validation added for death date.
				addDeathDateValueChangeListener();
				
				txtReasonForDeath = (TextField) binder.buildAndBind(
						"Reason For Death", "reasonForDeath", TextField.class);
				txtReasonForDeath.setMaxLength(100);
				cmbTerminateCover = (ComboBox) binder.buildAndBind(
						"Terminate Cover", "terminateCover", ComboBox.class);
				
				@SuppressWarnings("unchecked")
				BeanItemContainer<SelectValue> terminateCover = (BeanItemContainer<SelectValue>) referenceData
						.get("terminateCover");

				cmbTerminateCover.setContainerDataSource(terminateCover);
				cmbTerminateCover.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbTerminateCover.setItemCaptionPropertyId("value");
				
				/*if(this.bean.getPreauthDataExtractionDetails().getTerminateCoverFlag() != null && this.bean.getPreauthDataExtractionDetails().getTerminateCoverFlag().toLowerCase().equalsIgnoreCase("y")){
					cmbTerminateCover.setValue(this.bean.getPreauthDataExtractionDetails().getTerminateCover());
				}*/

				List<SelectValue> terminateContainerList = terminateCover.getItemIds();
				
				for (SelectValue terminateSelect : terminateContainerList) {
					if((SHAConstants.YES_FLAG).equalsIgnoreCase(terminateSelect.getValue())) {
						cmbTerminateCover.setValue(terminateSelect);
						break;
					}					
				}

				setRequiredAndValidation(deathDate);
				setRequiredAndValidation(txtReasonForDeath);
				setRequiredAndValidation(cmbTerminateCover);

				mandatoryFields.add(deathDate);
				mandatoryFields.add(txtReasonForDeath);
				mandatoryFields.add(cmbTerminateCover);

				patientStatusFLayout.addComponent(deathDate);
				patientStatusFLayout.addComponent(txtReasonForDeath);
				patientStatusFLayout.addComponent(cmbTerminateCover);
				
				/*if(ReferenceTable.RELATION_SHIP_SELF_KEY.equals(this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
					buildNomineelayout();
				}	*/
				
			} else {
				/*if(nomineeDetailsTable != null) {
					wholeVLayout.removeComponent(nomineeDetailsTable);
				}
				if(legaHeirLayout != null) {
					wholeVLayout.removeComponent(legaHeirLayout);
				}*/
				removePatientStatusGeneratedFields();
			}

		} else {
			removePatientStatusGeneratedFields();
		}

	}
	

	private void removePatientStatusGeneratedFields() {
		if (deathDate != null && txtReasonForDeath != null
				&& cmbTerminateCover != null) {
			unbindField(deathDate);
			unbindField(txtReasonForDeath);
			unbindField(cmbTerminateCover);
			mandatoryFields.remove(deathDate);
			mandatoryFields.remove(txtReasonForDeath);
			mandatoryFields.remove(cmbTerminateCover);
			patientStatusFLayout.removeComponent(deathDate);
			patientStatusFLayout.removeComponent(txtReasonForDeath);
			patientStatusFLayout.removeComponent(cmbTerminateCover);
		}
	}

	@Override
	public void init(PreauthDTO bean) {
		// TODO Auto-generated method stub

	}

	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer){
		this.diagnosisListenerTableObj.setIcdBlock(icdBlockContainer);
		
	}
	
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer){
		this.diagnosisListenerTableObj.setIcdCode(icdCodeContainer);
	}

	public void setPackageRateForProcedure(Map<String, String> mappedValues) {
		this.procedureTableObj.setPackageRate(mappedValues);
	}
	
	/**
	 * If the policy type is "Group Policy" , then the corp buffer check box
	 * will be enabled. Else it will be disabled. The below method
	 * does the validation for the same.
	 * */
	
	public void editCorpBufferChk()
	{
		/**
		 * The string "Group Policy" needs to be replaced, after checkwith
		 * saravana/Sathish the valid values for Group Policy.For time being
		 * the below string is used. 
		 */
		
		
		if (null != this.bean.getNewIntimationDTO() && null != this.bean.getNewIntimationDTO().getPolicy() 
				&& null != this.bean.getNewIntimationDTO().getPolicy().getProductType() && ("Group").equalsIgnoreCase(this.bean.getNewIntimationDTO().getPolicy().getProductType().getValue()))
		{
			/*addCorporateBufferListener();
			corpBufferChk.setReadOnly(false);
			corpBufferChk.setValue(false);
			if(this.bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && this.bean.getPreauthDataExtractionDetails().getCorpBuffer()){
				corpBufferChk.setValue(this.bean.getPreauthDataExtractionDetails().getCorpBuffer());
				isCorpBufferChecked = this.bean.getPreauthDataExtractionDetails().getCorpBuffer();
			}else{
				isCorpBufferChecked = false;
			}*/
			//R1167
			corpBufferChk.setReadOnly(true);
		}
		else
		{
			corpBufferChk.setReadOnly(true);
		}
	}
	
	
	public void setHospitalizationDetails(
			Map<Integer, Object> hospitalizationDetails) {
		this.claimedDetailsTableObj.setDBCalculationValues(hospitalizationDetails);
		
	}
	
	public void setCustomDiagValue(SelectValue selValue,ComboBox cmbBox)
	{
		diagnosisListenerTableObj.setCustomDiagValueToContainer(selValue,cmbBox);
	}
	/*public void setCustomDiagValue(BeanItemContainer<SelectValue> selectValue,ComboBox cmbBox,Long key)
	{
		diagnosisListenerTableObj.setCustomDiagValueToContainer(selectValue,cmbBox , key);
	}*/
	
	protected void addDiagnosisNameChangeListener()
	{
		this.diagnosisListenerTableObj.dummyField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				String diagnosisValue = (String)event.getProperty().getValue();
				if(null != procedureTableObj)
				{
					if(!procedureTableObj.diagnosisList.contains(diagnosisValue))
					{
						procedureTableObj.diagnosisList.add(diagnosisValue);
					}
				System.out.println("---the diagnosisListValue----"+procedureTableObj.diagnosisList);
				}
				else
				{
					diagnosisList = new ArrayList<String>();
					if(!diagnosisList.contains(diagnosisValue))
						diagnosisList.add(diagnosisValue);
				}
				/*if(null != newProcedurdTableObj)
				{
					String diagnosisValue = (String)event.getProperty().getValue();
					if(!newProcedurdTableObj.diagnosisList.contains(diagnosisValue))
					{
						newProcedurdTableObj.diagnosisList.add(diagnosisValue);
					}
				System.out.println("---the diagnosisListValue----"+newProcedurdTableObj.diagnosisList);
				}*/
			}
			
			
		});
		
	}
	
    public void alertNoOfSittings() {
	   	 DiagnosisDetailsTableDTO dialysisDiagnosisDTO = SHAUtils.getDialysisDiagnosisDTO(bean.getPreauthDataExtractionDetails().getDiagnosisTableList());
	   	 ProcedureDTO dialysisProcedureDTO = SHAUtils.getDialysisProcedureDTO(bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList());
	   	 if(dialysisDiagnosisDTO != null || dialysisProcedureDTO != null) { 
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setClosable(false);
					dialog.setResizable(false);
					dialog.setModal(true);
					
					final TextField sittingsField = new TextField("No. Of sittings");
					CSValidator validator = new CSValidator();
					validator.extend(sittingsField);
					validator.setRegExp("^[0-9]*$");
					validator.setPreventInvalidTyping(true);
					
					sittingsField.setData(dialysisDiagnosisDTO != null ? dialysisDiagnosisDTO : (dialysisProcedureDTO != null ? dialysisProcedureDTO : null ));
					if(dialysisDiagnosisDTO != null) {
						if(dialysisDiagnosisDTO.getSittingsInput() != null) {
							sittingsField.setValue(dialysisDiagnosisDTO.getSittingsInput() );
						}
					} else if(dialysisProcedureDTO != null) {
						if(dialysisProcedureDTO.getSittingsInput() != null) {
							sittingsField.setValue(dialysisProcedureDTO.getSittingsInput() );
						}
					}
					
					Button okButton = new Button("OK");
					okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					
					okButton.setData(dialysisDiagnosisDTO != null ? dialysisDiagnosisDTO : (dialysisProcedureDTO != null ? dialysisProcedureDTO : null ));
					okButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;
	
						@Override
						public void buttonClick(ClickEvent event) {
							if(event.getButton().getData() != null) {
								if((event.getButton().getData()) instanceof DiagnosisDetailsTableDTO) {
									DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) event.getButton().getData();
									dto.setSittingsInput(sittingsField.getValue() != null ? sittingsField.getValue() : "0");
								} else if((event.getButton().getData()) instanceof ProcedureDTO) {
									ProcedureDTO dto = (ProcedureDTO) event.getButton().getData();
									dto.setSittingsInput(sittingsField.getValue() != null ? sittingsField.getValue() : "0");
								}
								bean.setDialysisOpened(true);
							}
							wizard.next();
							dialog.close();
						}
					});
					HorizontalLayout hLayout = new HorizontalLayout(okButton);
					hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
					hLayout.setMargin(true);
					VerticalLayout layout = new VerticalLayout(sittingsField, hLayout);
					layout.setMargin(true);
					dialog.setContent(layout);
					dialog.show(getUI().getCurrent(), null, true);
	   	 } else {
	   		 bean.setIsDialysis(false);
	   		 bean.setDialysisOpened(false);
	   	 }
    }
    
    private void unbindAndRemoveFromLayout() {
		unbindField(injuryDate);
		unbindField(cmbCauseOfInjury);
		unbindField(medicalLegalCase);
		unbindField(reportedToPolice);
		unbindField(deliveryDate);
		unbindField(diseaseDetectedDate);
		unbindField(firNumberTxt);
		unbindField(policeReportedAttached);
		unbindField(cmbTypeOfDelivery);
		
		
		if(injuryDate != null && cmbCauseOfInjury != null && medicalLegalCase != null && reportedToPolice != null  ) {
			firstFLayout.removeComponent(medicalLegalCase);
			firstFLayout.removeComponent(reportedToPolice);
			firstFLayout.removeComponent(injuryDate);
			secondFLayout.removeComponent(cmbCauseOfInjury);
			
		}
		
		if(firNumberTxt != null && policeReportedAttached != null) {
			firstFLayout.removeComponent(firNumberTxt);
			secondFLayout.removeComponent(policeReportedAttached);
		}
		
		if(deliveryDate != null ) {
			firstFLayout.removeComponent(deliveryDate);
			mandatoryFields.remove(deliveryDate);
		}
		
		if(cmbTypeOfDelivery != null) {
			firstFLayout.removeComponent(cmbTypeOfDelivery);
		}
		
		if(diseaseDetectedDate != null) {
			firstFLayout.removeComponent(diseaseDetectedDate);
		}
	}
    
    protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
    
    public void generatedFieldsBasedOnOtherBenefits(PreauthDTO bean) {
    	if(benefitsTableObj != null){
    		benefitLayout.removeComponent(benefitsTableObj);
    	}
    	
    	benefitsTableObj = befitsTableInstance.get();					
		benefitsTableObj.init(bean);
		benefitsTableObj.setWidth("100%");
		benefitsTableObj.addList(bean.getPreauthDataExtractionDetails().getOtherBenefitsList());
		
		
	if(otherBenefitsOption != null && (boolean)otherBenefitsOption.getValue()){	
		benefitLayout.addComponent(benefitsTableObj);
		benefitsTableObj.setDefaultValues();
	}
	else{
		benefitLayout.removeComponent(benefitsTableObj);		
	}    	
    }
    
    
    public void generatedFieldsBasedOnHospitalisationDueTo(PreauthDTO bean) {
    	
    	SelectValue value = (SelectValue)cmbHospitalisationDueTo.getValue();
    	
		if (null != value) {
			if(value.getId() != null && ReferenceTable.INJURY_MASTER_ID.equals(value.getId())) {
				unbindAndRemoveFromLayout();
				injuryDate = (DateField) binder.buildAndBind(
						"Date of Injury/Accident", "injuryDate", DateField.class);
				cmbCauseOfInjury = (ComboBox) binder.buildAndBind(
						"Cause of Injury/Accident", "causeOfInjury", ComboBox.class);
				
				@SuppressWarnings("unchecked")
				BeanItemContainer<SelectValue> causeOfInjuryContainer = (BeanItemContainer<SelectValue>) referenceData
						.get("causeOfInjury");

				cmbCauseOfInjury.setContainerDataSource(causeOfInjuryContainer);
				cmbCauseOfInjury.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbCauseOfInjury.setItemCaptionPropertyId("value");
				
				if(this.bean.getPreauthDataExtractionDetails().getCauseOfInjury() != null) {
					cmbCauseOfInjury.setValue(this.bean.getPreauthDataExtractionDetails().getCauseOfInjury());
				}
				else{
					cmbCauseOfInjury.setValue(causeOfInjuryContainer.getItemIds().get(0));
				}
				
				medicalLegalCase = (OptionGroup) binder.buildAndBind(
						"Medical Legal Case", "medicalLegalCase", OptionGroup.class);
				medicalLegalCase.addItems(getReadioButtonOptions());
				medicalLegalCase.setItemCaption(true, "Yes");
				medicalLegalCase.setItemCaption(false, "No");
				medicalLegalCase.setStyleName("horizontal");
				
				reportedToPolice = (OptionGroup) binder.buildAndBind(
						"Reported to Police", "reportedToPolice", OptionGroup.class);
				reportedToPolice.addItems(getReadioButtonOptions());
				reportedToPolice.setItemCaption(true, "Yes");
				reportedToPolice.setItemCaption(false, "No");
				reportedToPolice.setStyleName("horizontal");
				reportedToPolice.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean isChecked = false;
						if(event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue().toString() == "true") {
							isChecked = true;
						} 
						if(event.getProperty() != null && event.getProperty().getValue() != null) {
							fireViewEvent(PreMedicalPreauthWizardPresenter.PRE_MEDICAL_REPORTED_TO_POLICE, isChecked);
						}
					}
				});
				
				
				firstFLayout.addComponent(injuryDate);
				firstFLayout.addComponent(medicalLegalCase);
				firstFLayout.addComponent(reportedToPolice);
				
				causeOfInsuryListener();
				
				secondFLayout.addComponent(cmbCauseOfInjury);
				if(this.bean.getPreauthDataExtractionDetails().getReportedToPolice() != null && this.bean.getPreauthDataExtractionDetails().getReportedToPolice()) {
					generatedFieldsBasedOnReportedToPolice(true);
				}
				
			} else if(value.getId() != null && ReferenceTable.ILLNESS_MASTER_ID.equals(value.getId())) {
				unbindAndRemoveFromLayout();
				diseaseDetectedDate = (DateField) binder.buildAndBind(
						"Date Disease First Detected", "diseaseFirstDetectedDate", DateField.class);
				firstFLayout.addComponent(diseaseDetectedDate);
			} else if(value.getId() != null && ReferenceTable.MATERNITY_MASTER_ID.equals(value.getId())) {
				if(firNumberTxt != null){
					firstFLayout.removeComponent(firNumberTxt);
					}
				if(policeReportedAttached != null){
					secondFLayout.removeComponent(policeReportedAttached);
				}
				unbindAndRemoveFromLayout();
				
				if(this.bean.getNewIntimationDTO().getInsuredPatient() != null && this.bean.getNewIntimationDTO().getInsuredPatient().getInsuredGender() != null && this.bean.getNewIntimationDTO().getInsuredPatient().getInsuredGender().getKey().equals(ReferenceTable.FEMALE_GENDER)
						|| (ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
					deliveryDate = (DateField) binder.buildAndBind(
							"Date of Delivery", "deliveryDate", DateField.class);
					mandatoryFields.add(deliveryDate);
					showOrHideValidation(false);
					cmbTypeOfDelivery = (ComboBox) binder.buildAndBind(
							"Type of Delivery", "typeOfDelivery", ComboBox.class);
					
					@SuppressWarnings("unchecked")
					BeanItemContainer<SelectValue> terminateCover = (BeanItemContainer<SelectValue>) referenceData
							.get("typeOfDelivery");

					cmbTypeOfDelivery.setContainerDataSource(terminateCover);
					cmbTypeOfDelivery.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbTypeOfDelivery.setItemCaptionPropertyId("value");
					
					if(this.bean.getPreauthDataExtractionDetails().getTypeOfDelivery() != null) {
						cmbTypeOfDelivery.setValue(this.bean.getPreauthDataExtractionDetails().getTypeOfDelivery());
					}
					addTypeOfDeliveryChangeListner();
					firstFLayout.addComponent(deliveryDate);
					firstFLayout.addComponent(cmbTypeOfDelivery);
				} else {
					getErrorMessage("Selected insured is Male.Hence Maternity is not applicable for the selected insured");
					cmbHospitalisationDueTo.setValue(null);
					//Vaadin8-setImmediate() cmbHospitalisationDueTo.setImmediate(true);
					
				}
			}
			else {
				unbindAndRemoveFromLayout();
			}
//			if(bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && (SHAConstants.ASSISTED_REPRODUCTION_TREATMENT).equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getValue())){
//				
//				String sectionTableCoverValue = sectionDetailsListenerTableObj.getValue().getCover() != null ? sectionDetailsListenerTableObj.getValue().getCover().getValue() : "";
//				String hospitalisationValue = bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getValue();				
//				if(!hospitalisationValue.equalsIgnoreCase(sectionTableCoverValue)){
//					getErrorMessage("Please Select Cover Value to match with The Hospitalisation Due to");
//				}
//			}			
			
		} else {
			unbindAndRemoveFromLayout();
		}
	}
    
    public void generatedFieldsBasedOnReportedToPolice(Boolean isChecked) {
		if (isChecked) {
				unbindAndRemovePoliceReportFromLayout();
				firNumberTxt = (TextField) binder.buildAndBind(
						"FIR NO", "firNumber", TextField.class);
				
				policeReportedAttached = (OptionGroup) binder.buildAndBind(
						"MLC Report & Police Report Attached", "policeReportAttached", OptionGroup.class);
				policeReportedAttached.addItems(getReadioButtonOptions());
				policeReportedAttached.setItemCaption(true, "Yes");
				policeReportedAttached.setItemCaption(false, "No");
				policeReportedAttached.setStyleName("horizontal");
				
				firstFLayout.addComponent(firNumberTxt);
				
				secondFLayout.addComponent(policeReportedAttached);
		} else {
			unbindAndRemovePoliceReportFromLayout();
		}
	}
    
    private void unbindAndRemovePoliceReportFromLayout() {
		unbindField(firNumberTxt);
		unbindField(policeReportedAttached);
		
		if(firNumberTxt != null && policeReportedAttached != null) {
			firstFLayout.removeComponent(firNumberTxt);
			secondFLayout.removeComponent(policeReportedAttached);
		}
		
	}
    
public void getErrorMessage(String eMsg){/*
		
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
	*/
	MessageBox.createError()
	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
    .withOkButton(ButtonOption.caption("OK")).open();

}

public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
	// TODO Auto-generated method stub
	sectionDetailsListenerTableObj.setCoverList(coverContainer);
	
}

public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
	
	sectionDetailsListenerTableObj.setSubCoverList(subCoverContainer);
	
}


public void setClearReferenceData(){
	SHAUtils.setClearReferenceData(referenceData);
	if(wholeVLayout!=null){
		wholeVLayout.removeAllComponents();
	}
	
}

public void clearTableObj(){
	
//	preauthCoordinatorView.destroy(preauthCoordinatorViewInstance);
//	procedureListenerTable.destroy(procedureTableObj);
//	specialityTableList.destroy(specialityTableObj);
//	diagnosisListnerTable.destroy(diagnosisListenerTableObj);
//	sectionDetailsListenerTable.destroy(sectionDetailsListenerTableObj);
//	claimedAmountDetailsTable.destroy(claimedDetailsTableObj);
//	newProcedureTableList.destroy(newProcedurdTableObj);
//	befitsTableInstance.destroy(benefitsTableObj);
	this.diagnosisListenerTableObj.clearObject();
	this.diagnosisListenerTableObj = null;
	this.sectionDetailsListenerTableObj = null;
	this.specialityTableObj = null;
	this.procedureTableObj = null;
	this.claimedDetailsTableObj = null;
}
	public void suspiousPopupMessage() {/*
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);

		Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		okButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				dialog.close();
				if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageFor30DaysWaitingPeriod();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});

		HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);
		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getSuspiciousPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			Label label = new Label(entry.getValue(), ContentMode.HTML);
			label.setWidth(null);
			layout.addComponent(label);
			layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
			layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
		}
		layout.addComponent(okButton);
		layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setWidth("30%");
		this.bean.setIsPopupMessageOpened(true);
		dialog.show(getUI().getCurrent(), null, true);
	*/
		StringBuffer label=new StringBuffer();
		Map<String, String> popupMap = bean.getSuspiciousPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			//Label label = new Label(entry.getValue(), ContentMode.HTML);
			//label.append(entry.getValue());
			label.append(entry.getKey());
		}
		final MessageBox infoMsg= showInfoMessageBox(label.toString());
		Button okButton = infoMsg.getButton(ButtonType.OK);
        okButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				infoMsg.close();
				if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageFor30DaysWaitingPeriod();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
	}

	public void nonPreferredPopupMessage() {/*
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);

		Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		okButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				dialog.close();
				if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  
				else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}
				else if (bean.getIs64VBChequeStatusAlert()) {
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageFor30DaysWaitingPeriod();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});

		HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);
		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getNonPreferredPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			Label label = new Label(entry.getValue(), ContentMode.HTML);
			label.setWidth(null);
			layout.addComponent(label);
			layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
			layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
		}
		layout.addComponent(okButton);
		layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setWidth("30%");
		this.bean.setIsPopupMessageOpened(true);
		dialog.show(getUI().getCurrent(), null, true);
	*/
		StringBuffer label=new StringBuffer();
		Map<String, String> popupMap = bean.getNonPreferredPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			 //label.append(entry.getValue());
			 label.append(entry.getKey());
		}	
		final MessageBox infoMsg = showInfoMessageBox(label.toString());
		Button okButton = infoMsg.getButton(ButtonType.OK);
		okButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				infoMsg.close();
				if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  
				else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}
				else if (bean.getIs64VBChequeStatusAlert()) {
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageFor30DaysWaitingPeriod();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
	
	}
	
	public void setRTAButton(Button btnRTAView){
		this.btnRTAView = btnRTAView;
		
	}
	
	 private BlurListener getgmcCorpBufferLimitListener() {
			BlurListener listener = new BlurListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField value = (TextField) event.getComponent();
					if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
						
						Double diff = Double.valueOf(value.getValue());
						Double si = bean.getPreauthDataExtractionDetails().getCorpBufferSI()!= null ? bean.getPreauthDataExtractionDetails().getCorpBufferSI() : 0d;
						Double utilisedAmt = bean.getPreauthDataExtractionDetails().getCorpBufferUtilisedAmt()!= null ? bean.getPreauthDataExtractionDetails().getCorpBufferUtilisedAmt() : 0d;
						
						if(diff > (si - utilisedAmt)){
							getGMCAlert();
							bean.getClaimDTO().setGmcCorpBufferLmt(0);
							value.setValue(bean.getClaimDTO().getGmcCorpBufferLmt() != null ? bean.getClaimDTO().getGmcCorpBufferLmt().toString() : "");
						}else{
							bean.getClaimDTO().setGmcCorpBufferLmt(Integer.valueOf(value.getValue()));
						}
						
						
					}else{
						
						bean.getClaimDTO().setGmcCorpBufferLmt(null);
					}
						
				}
			};
			return listener;
		}
	 
	 public void getGMCAlert() {/*
	   		Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.GMC_ALERT_AMT + "</b>",
					ContentMode.HTML);
	   		Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			
			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					 	dialog.close();
				}
			});
			
	

*/
		 final MessageBox showInfoMessageBox = showInfoMessageBox(SHAConstants.GMC_ALERT_AMT);
	     Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
	     homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					showInfoMessageBox.close();
				}
			});
	 
	 }
	 
	 
	 
	 public Boolean alertMessageForPlocyPlan() {/*
		  
		  String msg = "";
		  
		  if(SHAConstants.POLICY_GOLD_PLAN.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicyPlan())){
					
			  				msg = SHAConstants.GOLD_ALERT_MSG;							
				}
			else if(SHAConstants.POLICY_SILVER_PLAN.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicyPlan())){
				
							msg = SHAConstants.SILVER_ALERT_MSG;				
					
			}		  
		  
		  
	   		Label successLabel = new Label(
					"<b style = 'color: red;'>" + msg  + "</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setStyleName("borderLayout");
			layout.setSpacing(true);
			layout.setMargin(true);				

			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setContent(layout);
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
			return true;
		*/
		 String msg = "";
		  
		  if(SHAConstants.POLICY_GOLD_PLAN.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicyPlan())){
					
			  				msg = SHAConstants.GOLD_ALERT_MSG;							
				}
			else if(SHAConstants.POLICY_SILVER_PLAN.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicyPlan())){
				
							msg = SHAConstants.SILVER_ALERT_MSG;				
					
			}
		  
		   final MessageBox showInfoMessageBox = showInfoMessageBox(msg);
	    	Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					showInfoMessageBox.close();						
				}
			});
			return true;
	 }
	 

	 public void getSectionAlert(StringBuffer planAlertMeg) {/*
		 
	   		Label successLabel = new Label(
					"<b style = 'color: red;'>" + planAlertMeg + "</b>",
					ContentMode.HTML);
	   		Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");	
			
			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);	
			
			
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					 	dialog.close();
				}
			});
	 */
		 final MessageBox showInfoMessageBox = showInfoMessageBox(planAlertMeg.toString());
	    	Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

			homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					showInfoMessageBox.close();
				}
			});	 
	 }
	 
	 
 public void policyValidationPopupMessage() {/*	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.POLICY_VALIDATION_ALERT + "</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
						suspiousPopupMessage();
					}else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					} else if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					} else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						popupMessageFor30DaysWaitingPeriod();
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});
		*/
	 final MessageBox showInfoMessageBox = showInfoMessageBox(SHAConstants.POLICY_VALIDATION_ALERT);
	 Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
	 homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
				if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if(bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageFor30DaysWaitingPeriod();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
 }
	 
	 
	 private void cardiaCareSectionAlert(){/*
			
			String msg = SHAConstants.CARDIAC_CARE_SECTION_ALERT;			
			
	   		Label successLabel = new Label(
					"<b style = 'color: red;'>"+msg+"</b>",
					ContentMode.HTML);		   		
	   	
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setStyleName("borderLayout");
			layout.setSpacing(true);
			layout.setMargin(true);				

			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					
				//	bean.setIsPopupMessageOpened(true);
					dialog.close();	
					
					if(null != bean.getNewIntimationDTO().getPolicy().getPolicyPlan())
					{     						
						alertMessageForPlocyPlan();
							
					}
						
				}					
				
			});

			dialog.setModal(true);
		//	dialog.close();	
			//UI.getCurrent().addWindow(popup);
		*/
		 final MessageBox showInfoMessageBox = showInfoMessageBox(SHAConstants.CARDIAC_CARE_SECTION_ALERT);
		 Button button = showInfoMessageBox.getButton(ButtonType.OK);
			
			button.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					
				//	bean.setIsPopupMessageOpened(true);
					showInfoMessageBox.close();	
					
					if(null != bean.getNewIntimationDTO().getPolicy().getPolicyPlan())
					{     						
						alertMessageForPlocyPlan();
							
					}
						
				}					
				
			}); 
	 
	 }
	 
	 public void setAssistedValue(Long value){
			this.assistedTreatment = value;
		}
	 
	 private void showICRMessage(){/*
		 String msg = SHAConstants.ICR_MESSAGE;
		 Label successLabel = new Label("<div style = 'text-align:center;'><b style = 'color: red;'>"+msg+"</b></div>",	ContentMode.HTML);

		 Button homeButton = new Button("OK");
		 homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		 VerticalLayout firstForm = new VerticalLayout(successLabel,homeButton);
		 firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		 firstForm.setSpacing(true);
		 firstForm.setMargin(true);
		 firstForm.setStyleName("borderLayout");

		 final ConfirmDialog dialog = new ConfirmDialog();
		 dialog.setClosable(false);
		 dialog.setContent(firstForm);
		 dialog.setResizable(false);
		 dialog.setModal(true);
		 dialog.setWidth("20%");
		 dialog.show(getUI().getCurrent(), null, true);

		 homeButton.addClickListener(new ClickListener() {
			 private static final long serialVersionUID = 7396240433865727954L;

			 @Override
			 public void buttonClick(ClickEvent event) {
				 bean.setIsPopupMessageOpened(true);
				 dialog.close();

				 if(bean.getIsPolicyValidate()){	
					 policyValidationPopupMessage();
				 } else {
					 if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
						 suspiousPopupMessage();
					 }else if(!bean.getNonPreferredPopupMap().isEmpty()){
						 nonPreferredPopupMessage();
					 }
					 else if(bean.getIsPEDInitiated()) {
//						 alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					 } else if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						} else if(bean.getIsAutoRestorationDone()) {
						 alertMessageForAutoRestroation();
					 } else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						 poupMessageForProduct();
					 } else if(bean.getIs64VBChequeStatusAlert()){
						 get64VbChequeStatusAlert();
					 }else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					 }else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
								bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
								&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						 popupMessageFor30DaysWaitingPeriod();
					 }else if(bean.getIsSuspicious()!=null){
						 StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					 }
				 }

			 }
		 });
	 */
		 
		 final MessageBox showInfoMessageBox = showInfoMessageBox(SHAConstants.ICR_MESSAGE);
		 Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
		 
		 homeButton.addClickListener(new ClickListener() {
			 private static final long serialVersionUID = 7396240433865727954L;

			 @Override
			 public void buttonClick(ClickEvent event) {
				 bean.setIsPopupMessageOpened(true);
				 showInfoMessageBox.close();

				 if(bean.getIsPolicyValidate()){	
					 policyValidationPopupMessage();
				 } else {
					 if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
						 suspiousPopupMessage();
					 }else if(!bean.getNonPreferredPopupMap().isEmpty()){
						 nonPreferredPopupMessage();
					 }
					 else if(bean.getIsPEDInitiated()) {
//						 alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					 } else if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						} else if(bean.getIsAutoRestorationDone()) {
						 alertMessageForAutoRestroation();
					 } else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						 poupMessageForProduct();
					 } else if(bean.getIs64VBChequeStatusAlert()){
						 get64VbChequeStatusAlert();
					 }else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					 }else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
								bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
								&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						 popupMessageFor30DaysWaitingPeriod();
					 }else if(bean.getIsSuspicious()!=null){
						 StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					 }
				 }

			 }
		 });
		 
	 }
	 
	 public void paayasClaimManualyProcessedAlertMessage(String insuredName) {/*	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>" + "Claim processed outside the system for the insured-"+ insuredName + " Verify  and restirict sum insured and other benefits  before proceeding "+ "</b>",
					ContentMode.HTML);
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){	
						policyValidationPopupMessage();
					}
					
					else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
						suspiousPopupMessage();
					}else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					} else if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					} else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						popupMessageFor30DaysWaitingPeriod();
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});

 	*/
		 
		 final MessageBox showInfoMessageBox = showInfoMessageBox("Claim processed outside the system for the insured-"+insuredName+" Verify  and restirict sum insured and other benefits  before proceeding ");
		 Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
		 homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					showInfoMessageBox.close();
					if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){	
						policyValidationPopupMessage();
					}
					
					else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
						suspiousPopupMessage();
					}else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					} else if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					} else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						popupMessageFor30DaysWaitingPeriod();
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});
	 }
 
 	private BlurListener getHospDiscountBillListener() {
			BlurListener listener = new BlurListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField value = (TextField) event.getComponent();
					if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
						
						if(txtAmtClaimed != null && txtAmtClaimed.getValue() != null && !txtAmtClaimed.getValue().isEmpty()){
							Integer discount = Integer.valueOf(value.getValue());
							Integer claimedAmt = Integer.valueOf(txtAmtClaimed.getValue());
							Integer netAmt = claimedAmt - discount;
							
							if(txtNetAmt != null){
								txtNetAmt.setValue(netAmt.toString());
							}
							
						}
						
						Integer discount = Integer.valueOf(value.getValue());
						discount = discount*= -1;
						
						if(claimedDetailsTableObj != null){
	     					
	     					List<NoOfDaysCell> claimedAmountValues = new ArrayList<NoOfDaysCell>();
	     					
	     					claimedAmountValues.addAll(claimedDetailsTableObj.getValues());
	     					
	     					NoOfDaysCell noOfDaysCell1 = null;
	     					for (NoOfDaysCell noOfDaysCell : claimedAmountValues) {
								if(noOfDaysCell.getBenefitId() != null && noOfDaysCell.getBenefitId().equals(21L)) {
									noOfDaysCell.setClaimedBillAmount(discount);
									noOfDaysCell.setNetAmount(discount);
									noOfDaysCell.setPaybleAmount(discount);
									noOfDaysCell1 = noOfDaysCell;
									break;
								}
							}
	     					claimedDetailsTableObj.setValuesForHospDiscount(noOfDaysCell1);
	     					
	     				}
						
						
					}
						
				}
			};
			return listener;
		}
	 
	 private BlurListener getclaimedAmtListener() {
			BlurListener listener = new BlurListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField value = (TextField) event.getComponent();
					if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
						if(txtDiscntHospBill != null && txtDiscntHospBill.getValue() != null && !txtDiscntHospBill.getValue().isEmpty()){
							Integer claimedAmt = Integer.valueOf(value.getValue());
							Integer discount = Integer.valueOf(txtDiscntHospBill.getValue());
							Integer netAmt = claimedAmt - discount;
							
							if(txtNetAmt != null){
								txtNetAmt.setValue(netAmt.toString());
							}
							
						}
						
					}
						
				}
			};
			return listener;
		}
 
public void getHospitalCategory(String hospitalCategory) {/*	 
	 
	    Label successLabel = new Label(
				"<b style = 'color: red;'>" + hospitalCategory + " Category Hospital"+ "</b>",
				ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
					paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
				}
				else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
					showICRMessage();
				}else if(bean.getIsPolicyValidate()){	
					policyValidationPopupMessage();
				}
				
				else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if(bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageFor30DaysWaitingPeriod();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
	*/
	final MessageBox showInfoMessageBox = showInfoMessageBox(hospitalCategory);
    Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
    homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			showInfoMessageBox.close();
			if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
				paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
			}
			else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
				showICRMessage();
			}else if(bean.getIsPolicyValidate()){	
				policyValidationPopupMessage();
			}
			
			else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				suspiousPopupMessage();
			}else if(!bean.getNonPreferredPopupMap().isEmpty()){
				nonPreferredPopupMessage();
			}
			else if(bean.getIsPEDInitiated()) {
//				alertMessageForPED();
				if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				}else{
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				}
			} else if(bean.isInsuredDeleted()){
				alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
			} else if(bean.getIsAutoRestorationDone()) {
				alertMessageForAutoRestroation();
			} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
				poupMessageForProduct();
			} else if(bean.getIs64VBChequeStatusAlert()){
				get64VbChequeStatusAlert();
			}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
					bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
					&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
				popupMessageFor30DaysWaitingPeriod();
			}else if(bean.getIsSuspicious()!=null){
				StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
			}
		}
	});

}

public void showDuplicateInsured(List<SelectValue> duplicateInsured) {/*
	//batchCpuCountTable.init("Count For Cpu Wise", false, false);
	//batchCpuCountTable.setTableList(tableDTOList);
	Table table = new Table();
	table.setHeight("200px");
	table.setWidth("200px");
	table.addContainerProperty("Insured Name", String.class, null);
	table.addContainerProperty("Health Card Number",  String.class, null);
	table.setPageLength(10);
	table.setSizeFull();
	table.setHeight("140%");
	int i = 0;
	for (SelectValue selectValue : duplicateInsured) {
		table.addItem(new Object[]{selectValue.getValue(),  selectValue.getCommonValue()}, i++);
	}
	
	Button homeButton = new Button("OK");
	homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	
	VerticalLayout layout = new VerticalLayout(table, homeButton);
	layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
	layout.setSpacing(true);
	layout.setMargin(true);
	layout.setSizeFull();
	//layout.setStyleName("borderLayout");
	
	Window popup = new com.vaadin.ui.Window();
	popup.setCaption("<b style = 'color: red;'>Duplicate Insured Details !!!</b>");
	popup.setCaptionAsHtml(true);
	popup.setWidth("30%");
	popup.setHeight("35%");
	popup.setContent(layout);
	popup.setClosable(false);
	popup.center();
	popup.setResizable(false);
	homeButton.setData(popup);
	
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			Window box = (Window)event.getButton().getData();
			box.close();
			if(null != bean.getPolicyDto().getGmcPolicyType() && !bean.getPolicyDto().getGmcPolicyType().isEmpty() && bean.getPolicyDto().getLinkPolicyNumber() != null
					&& (bean.getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
							bean.getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
				showAlertForGMCParentLink(bean.getPolicyDto().getPolicyNumber());
			} else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
				getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
			}		
			else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
				paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
			}
			else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
				showICRMessage();
			}else if(bean.getIsPolicyValidate()){	
				policyValidationPopupMessage();
			}
			
			else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				suspiousPopupMessage();
			}else if(!bean.getNonPreferredPopupMap().isEmpty()){
				nonPreferredPopupMessage();
			}
			else if(bean.getIsPEDInitiated()) {
//				alertMessageForPED();
				if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				}else{
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				}	
			} else if(bean.isInsuredDeleted()){
				alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
			} else if(bean.getIsAutoRestorationDone()) {
				alertMessageForAutoRestroation();
			} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
				poupMessageForProduct();
			} else if(bean.getIs64VBChequeStatusAlert()){
				get64VbChequeStatusAlert();
			}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
			}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
					bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
					&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
				popupMessageFor30DaysWaitingPeriod();
			}else if(bean.getIsSuspicious()!=null){
				StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
			}
			
		}
	});

	popup.setModal(true);
	UI.getCurrent().addWindow(popup);
	// TODO Auto-generated method stub
	
*/
	Table table = new Table();
	table.addContainerProperty("Insured Name", String.class, null);
	table.addContainerProperty("Health Card Number",  String.class, null);
	table.setPageLength(10);
	table.setSizeFull();
	table.setHeight("140%");
	int i = 0;
	duplicateInsured=new ArrayList<SelectValue>();
	for (SelectValue selectValue : duplicateInsured) {
		table.addItem(new Object[]{selectValue.getValue(),  selectValue.getCommonValue()}, i++);
	}
	VerticalLayout layout=new VerticalLayout();
	Label successLabel = new Label(
			"<b>" +"Duplicate Insured Details !!!" + "</b>",
			ContentMode.HTML);
	
	layout.addComponent(successLabel);
	layout.addComponent(table);
	
	MessageBox msgBox = MessageBox
		    .createInfo()
		    .withCaptionCust("Information")
		    .withTableMessage(layout)
		    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
		    .open();
	
	Button homeButton = msgBox.getButton(ButtonType.OK);
	homeButton.setData(msgBox);
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			MessageBox box = (MessageBox)event.getButton().getData();
			box.close();
			if(null != bean.getPolicyDto().getGmcPolicyType() && !bean.getPolicyDto().getGmcPolicyType().isEmpty() && bean.getPolicyDto().getLinkPolicyNumber() != null
					&& (bean.getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
							bean.getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
				showAlertForGMCParentLink(bean.getPolicyDto().getPolicyNumber());
			} else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
				getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
			}		
			else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
				paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
			}
			else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
				showICRMessage();
			}else if(bean.getIsPolicyValidate()){	
				policyValidationPopupMessage();
			}
			
			else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				suspiousPopupMessage();
			}else if(!bean.getNonPreferredPopupMap().isEmpty()){
				nonPreferredPopupMessage();
			}
			else if(bean.getIsPEDInitiated()) {
//				alertMessageForPED();
				if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				}else{
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				}	
			} else if(bean.isInsuredDeleted()){
				alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
			} else if(bean.getIsAutoRestorationDone()) {
				alertMessageForAutoRestroation();
			} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
				poupMessageForProduct();
			} else if(bean.getIs64VBChequeStatusAlert()){
				get64VbChequeStatusAlert();
			}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
					bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
					&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
				popupMessageFor30DaysWaitingPeriod();
			}else if(bean.getIsSuspicious()!=null){
				StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
			}
			
		}
	});

}
	public void showAlertForGMCParentLink(String policyNumber){/*	 
	 
	    Label successLabel = new Label(
				"<b style = 'color: red;'>Policy is  Linked to Policy No " + policyNumber + "</b>",
				ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
					getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
				}		
				else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
					paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
				}
				else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
					showICRMessage();
				}else if(bean.getIsPolicyValidate()){	
					policyValidationPopupMessage();
				}
				
				else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if(bean.getIsPEDInitiated()) {
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
	*/
	    final MessageBox showInfoMessageBox = showInfoMessageBox("Policy is  Linked to Policy No " + policyNumber);
	    Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
				if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
					getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
				}		
				else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
					paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
				}
				else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
					showICRMessage();
				}else if(bean.getIsPolicyValidate()){	
					policyValidationPopupMessage();
				}
				
				else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if(bean.getIsPEDInitiated()) {
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		
	}

public void showTopUpAlertMessage(String remarks) {/*	 
	 
	 	Label successLabel = new Label(
				"<b style = 'color: red;'>" + remarks + "</b>",
				ContentMode.HTML);
		TextArea txtArea = new TextArea();
		txtArea.setMaxLength(4000);
		txtArea.setData(bean);
		//txtArea.setStyleName("Boldstyle");
		txtArea.setValue(remarks);
		txtArea.setNullRepresentation("");
		txtArea.setSizeFull();
		txtArea.setWidth("100%");
		txtArea.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		txtArea.setRows(remarks.length()/80 >= 25 ? 25 : ((remarks.length()/80)%25)+1);
		VerticalLayout layout = new VerticalLayout(txtArea, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setHeight(layout.getHeight(), Sizeable.UNITS_PERCENTAGE);
		dialog.setWidth("45%");
		dialog.setResizable(false);
		dialog.setModal(true);		
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && !bean.getDuplicateInsuredList().isEmpty()){
					showDuplicateInsured(bean.getDuplicateInsuredList());
				}

				else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
					getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
				}		
				else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
					paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
				}
				else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
					showICRMessage();
				}else if(bean.getIsPolicyValidate()){	
					policyValidationPopupMessage();
				}
				
				else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if(bean.getIsPEDInitiated()) {
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
	*/
	final MessageBox showMessageBox = showInfoMessageBox(remarks);
	Button homeButton = showMessageBox.getButton(ButtonType.OK);	
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			showMessageBox.close();
			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && !bean.getDuplicateInsuredList().isEmpty()){
				showDuplicateInsured(bean.getDuplicateInsuredList());
			}

			else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
				getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
			}		
			else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
				paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
			}
			else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
				showICRMessage();
			}else if(bean.getIsPolicyValidate()){	
				policyValidationPopupMessage();
			}
			
			else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				suspiousPopupMessage();
			}else if(!bean.getNonPreferredPopupMap().isEmpty()){
				nonPreferredPopupMessage();
			}
			else if(bean.getIsPEDInitiated()) {
				alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
			} else if(bean.getIsAutoRestorationDone()) {
				alertMessageForAutoRestroation();
			} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
				poupMessageForProduct();
			} else if(bean.getIs64VBChequeStatusAlert()){
				get64VbChequeStatusAlert();
			
			}else if(bean.getIsSuspicious()!=null){
				StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
			}
		}
	});

}
public void popupMessageFor30DaysWaitingPeriod() {/*
	Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
	Date admissionDate = bean.getNewIntimationDTO().getAdmissionDate();
	Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
    MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
    if((diffDays != null && diffDays < 30)){
    	 Label successLabel = new Label(
    				"<b style = 'color: red;'>" + SHAConstants.THIRTY_DAYS_WAITING_ALERT + "</b>",
    				ContentMode.HTML);
    			//final Boolean isClicked = false;
    		Button homeButton = new Button("OK");
    		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
    		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
    		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
    		layout.setSpacing(true);
    		layout.setMargin(true);
    		layout.setStyleName("borderLayout");
    		HorizontalLayout hLayout = new HorizontalLayout(layout);
    		hLayout.setMargin(true);
    		hLayout.setStyleName("borderLayout");

    		final ConfirmDialog dialog = new ConfirmDialog();
//    		dialog.setCaption("Alert");
    		dialog.setClosable(false);
    		dialog.setContent(layout);
    		dialog.setResizable(false);
    		dialog.setModal(true);
    		dialog.show(getUI().getCurrent(), null, true);

    		homeButton.addClickListener(new ClickListener() {
    			private static final long serialVersionUID = 7396240433865727954L;

    			@Override
    			public void buttonClick(ClickEvent event) {
    				// bean.setIsPopupMessageOpened(true);
    				 
   					bean.setIsPopupMessageOpened(true);
    					dialog.close();
    					 if(bean.getIsSuspicious()!=null){
    						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
    					}
    	}
	});
    }
*/

	Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
	Date admissionDate = bean.getNewIntimationDTO().getAdmissionDate();
	Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
    MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
    final MessageBox showInfoMessageBox = showInfoMessageBox(SHAConstants.THIRTY_DAYS_WAITING_ALERT);
    	 Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

    		homeButton.addClickListener(new ClickListener() {
    			private static final long serialVersionUID = 7396240433865727954L;

    			@Override
    			public void buttonClick(ClickEvent event) {
    				// bean.setIsPopupMessageOpened(true);
    				 
   					bean.setIsPopupMessageOpened(true);
   					showInfoMessageBox.close();
   					 
    					 if(bean.getIsSuspicious()!=null){
    						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
    					}
    	}
	});
    
	
}

public void showCMDAlert() {/*	 
	 
	 Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.CMD_ALERT + "</b>",
				ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
					showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
				}
				else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && !bean.getDuplicateInsuredList().isEmpty()){
					showDuplicateInsured(bean.getDuplicateInsuredList());
				}

				else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
					getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
				}		
				else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
					paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
				}
				else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
					showICRMessage();
				}else if(bean.getIsPolicyValidate()){	
					policyValidationPopupMessage();
				}
				
				else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if(bean.getIsPEDInitiated()) {
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
	*/
	 final MessageBox showInfoMessageBox = showInfoMessageBox(SHAConstants.CMD_ALERT);
	 Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
				if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
					showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
				}
				else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && !bean.getDuplicateInsuredList().isEmpty()){
					showDuplicateInsured(bean.getDuplicateInsuredList());
				}

				else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
					getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
				}		
				else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
					paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
				}
				else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
					showICRMessage();
				}else if(bean.getIsPolicyValidate()){	
					policyValidationPopupMessage();
				}
				
				else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if(bean.getIsPEDInitiated()) {
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		
}

public void showDeletedInsuredAlert() {

	final MessageBox showInfoMessageBox = showInfoMessageBox("<b style = 'color: red;'> Selected risk is deleted from policy : </b>"+bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
	Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			// bean.setIsPopupMessageOpened(true);
//				bean.setIsPopupMessageOpened(true);
			showInfoMessageBox.close();
			DBCalculationService dbService = new DBCalculationService();
			String memberType = dbService.getCMDMemberType(bean.getPolicyKey());
			if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
				showCMDAlert();
			}
			else if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
				showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
			}
			else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && ! bean.getDuplicateInsuredList().isEmpty()){
				showDuplicateInsured(bean.getDuplicateInsuredList());
			}
			else if(null != bean.getPolicyDto().getGmcPolicyType() && !bean.getPolicyDto().getGmcPolicyType().isEmpty() && bean.getPolicyDto().getLinkPolicyNumber() != null
					&& (bean.getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
							bean.getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
				showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
			}
			else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
				getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
			}		
			else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
				paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
			}
			else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
				showICRMessage();
			}else if(bean.getIsPolicyValidate()){	
				policyValidationPopupMessage();
			}
			
			else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				suspiousPopupMessage();
			}else if(!bean.getNonPreferredPopupMap().isEmpty()){
				nonPreferredPopupMessage();
			}
			else if(bean.getIsPEDInitiated()) {
//				alertMessageForPED();
				if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				}else{
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				}	
			} else if(bean.isInsuredDeleted()){
				alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
			} else if(bean.getIsAutoRestorationDone()) {
				alertMessageForAutoRestroation();
			} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
				poupMessageForProduct();
			} else if(bean.getIs64VBChequeStatusAlert()){
				get64VbChequeStatusAlert();
			}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
					bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
					&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
				popupMessageFor30DaysWaitingPeriod();
			}else if(bean.getIsSuspicious()!=null){
				StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
			}
		}
	});
	
	
}

private void siRestricationAlert(List<String> list) {
	// TODO Auto-generated method stub
	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	if(list.size() == 2){
	HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
			.createInformationBox(list.get(1).toString(), buttonsNamewithType);
	Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
	
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			DBCalculationService dbService = new DBCalculationService();
			String memberType = dbService.getCMDMemberType(bean.getPolicyKey());
			if(bean.getNewIntimationDTO().getIsDeletedRisk()){
				showDeletedInsuredAlert();
			}
			else if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
				showCMDAlert();
			}
			else if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
				showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
			}
			else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && ! bean.getDuplicateInsuredList().isEmpty()){
				showDuplicateInsured(bean.getDuplicateInsuredList());
			}
			else if(null != bean.getPolicyDto().getGmcPolicyType() && !bean.getPolicyDto().getGmcPolicyType().isEmpty() && bean.getPolicyDto().getLinkPolicyNumber() != null
					&& (bean.getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
							bean.getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
				showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
			}
			else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
				getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
			}		
			else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
				paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
			}
			else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
				showICRMessage();
			}else if(bean.getIsPolicyValidate()){	
				policyValidationPopupMessage();
			}
			
			else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
				suspiousPopupMessage();
			}else if(!bean.getNonPreferredPopupMap().isEmpty()){
				nonPreferredPopupMessage();
			}
			else if(bean.getIsPEDInitiated()) {
//				alertMessageForPED();
				if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				}else{
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				}	
			} else if(bean.isInsuredDeleted()){
				alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
			} else if(bean.getIsAutoRestorationDone()) {
				alertMessageForAutoRestroation();
			} else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
				poupMessageForProduct();
			} else if(bean.getIs64VBChequeStatusAlert()){
				get64VbChequeStatusAlert();
			}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
					bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
					&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
				popupMessageFor30DaysWaitingPeriod();
			}else if(bean.getIsSuspicious()!=null){
				StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
			}
		}
	});
}

}



public MessageBox showInfoMessageBox(String message){
	
	
	final MessageBox msgBox = MessageBox
		    .createInfo()
		    .withCaptionCust("Information")
		    .withHtmlMessage(message)
		    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
		    .open();
	
	return msgBox;
	
	
}

public MessageBox showAlertMessageBox(String message){
	
	
	final MessageBox msgBox = MessageBox
		    .createWarning()
		    .withCaptionCust("Warning")
		    .withHtmlMessage(message)
		    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
		    .open();
	
	return msgBox;
	
	
}

public MessageBox showCritcalAlertMessageBox(String message){
	
	
	final MessageBox msgBox = MessageBox
		    .createCritical()
		    .withCaptionCust("Critical Alert")
		    .withHtmlMessage(message)
		    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
		    .open();
	
	return msgBox;
	
	
}
 public MessageBox  showConfirmationMessageBox(String message){
	 final MessageBox msgBox =MessageBox
			 .createQuestion()
			 .withCaptionCust("Confirmation")
			 .withMessage(message)
			 .withYesButton(ButtonOption.caption(ButtonType.YES.name()))
			 .withNoButton(ButtonOption.caption(ButtonType.NO.name()))
			 .open();
	 return msgBox;
 }
 
 final MessageBox showErrorMessageBox(String message){

	 final MessageBox msgBox =MessageBox
			 .createError()
			 .withCaptionCust("Errors")
			 .withMessage(message)
			 .withOkButton(ButtonOption.caption("OK")).open();
	 return msgBox;

 }
 
 public void addCategoryValues(SelectValue categoryValues){
		
		BeanItemContainer<SelectValue> categoryBean = new BeanItemContainer<SelectValue>(SelectValue.class);
		categoryBean.addBean(categoryValues);
		cmbCategory.setContainerDataSource(categoryBean);
		cmbCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCategory.setItemCaptionPropertyId("value");
		cmbCategory.setValue(categoryValues);
		
		BeanItemContainer<SelectValue> treatementType = (BeanItemContainer<SelectValue>) referenceData
				.get("treatmentType");
		
		if (categoryValues != null && categoryValues.getValue() != null 
				&& categoryValues.getValue().equalsIgnoreCase("FEVER")) {
			cmbTreatmentType.setEnabled(false);
			referenceData.put("specialityType",
					referenceData.get("nextLOVSpeciality"));
			cmbTreatmentType.setContainerDataSource(treatementType);
			cmbTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbTreatmentType.setItemCaptionPropertyId("value");
			cmbTreatmentType.setValue(treatementType.getIdByIndex(0));
		}
		else {
//			cmbTreatmentType.setEnabled(true);
			referenceData.put("specialityType",
					referenceData.get("medicalSpeciality"));
			if (cmbTreatmentType != null && cmbTreatmentType.getValue() != null 
					&& !cmbTreatmentType.getValue().toString().toLowerCase().contains("medical")) {
				referenceData.put("specialityType",
						referenceData.get("surgicalSpeciality"));
			}
			
			if(cmbNatureOfTreatment != null && cmbNatureOfTreatment.getValue() != null && cmbNatureOfTreatment.getValue().toString().toLowerCase().contains("non")) {
				cmbTreatmentType.setEnabled(false);
			}
		}
		
	}
 
 public void setQuantumReductionForRRC(PreauthDTO preauthDTO){

	 if(txtAmtClaimed != null
			 && txtAmtClaimed.getValue() != null){
		 preauthDTO.getRrcDTO().getQuantumReductionDetailsDTO().setPreAuthAmount(Long.parseLong(txtAmtClaimed.getValue()));
	 }
	 if(bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()){
		 if(diagnosisListenerTableObj !=null 
				 && this.diagnosisListenerTableObj.getValues() !=null 
				 && !this.diagnosisListenerTableObj.getValues().isEmpty()){
			 List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisListenerTableObj.getValues();
			 String diagnosisDetails =null;
			 for(DiagnosisDetailsTableDTO detailsTableDTO : diagnosisList){
				 if(detailsTableDTO.getDiagnosisName() !=null && detailsTableDTO.getDiagnosisName().getValue() !=null){
					 if(diagnosisDetails !=null){
						 diagnosisDetails = " ,"+detailsTableDTO.getDiagnosisName().getValue();
					 }else{
						 diagnosisDetails = detailsTableDTO.getDiagnosisName().getValue();
					 } 
				 }
			 }
			 preauthDTO.getRrcDTO().getQuantumReductionDetailsDTO().setDiagnosis(diagnosisDetails); 
		 }
	 }
	 if(preauthDTO.getPreauthDataExtractionDetails().getTreatmentType() == null
			 && this.cmbTreatmentType.getValue() !=null){
		 SelectValue selectValue = (SelectValue) cmbTreatmentType.getValue();
		 preauthDTO.getRrcDTO().getQuantumReductionDetailsDTO().setManagement(selectValue.getValue());
	 }
 }
 
 
 
 /*Below code not implemented for cashless*/
 /*private void buildNomineelayout(){
	 if (cmbPatientStatus.getValue() != null 
				&& (this.cmbPatientStatus.getValue().toString().toLowerCase().contains("deceased"))) {
			
			nomineeDetailsTable = nomineeDetailsTableInstance.get();
			
			nomineeDetailsTable.init("", false, false);
			nomineeDetailsTable.setPresenterString(SHAConstants.CASHLESS);
			
			if(bean.getClaimDTO().getNewIntimationDto().getNomineeList() != null) {
				nomineeDetailsTable.setTableList(bean.getClaimDTO().getNewIntimationDto().getNomineeList());
				nomineeDetailsTable.generateSelectColumn();
			}
			
			wholeVLayout.addComponent(nomineeDetailsTable);
		
			boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
					
			legaHeirLayout = nomineeDetailsTable.getLegalHeirLayout(enableLegalHeir);
			nomineeDetailsTable.removeLegalHeirValidation();
			
		
			if(enableLegalHeir) {
				nomineeDetailsTable.setLegalHeirDetails(
				bean.getNewIntimationDTO().getNomineeName(),
				bean.getNewIntimationDTO().getNomineeAddr());
			}	
			
			wholeVLayout.addComponent(legaHeirLayout);
		}
	}*/
	//added for CR - Young star product by noufel on 08-04-2020
 public void addTypeOfDeliveryChangeListner(){
	 cmbTypeOfDelivery.addValueChangeListener(new ValueChangeListener() {
		 private static final long serialVersionUID = -2577540521492098375L;

		 @Override
		 public void valueChange(ValueChangeEvent event) {
			 SelectValue value = (SelectValue) event.getProperty().getValue();
			 if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					 (bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_YOUNG_PRODUCT_CODE) ||
							 bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_91))){


				 if(value != null && value.getValue() != null){
					 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					 buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					 HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							 .createInformationBox("Waiting period of 36 months for Maternity from the date of first commencement of this policy" , buttonsNamewithType);
					 Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
				 }
			 }
		 }
	 });
 } 	
 public void getAlertForCoronaAlert() {
	 if(bean.getNewIntimationDTO().getPolicy().getPolicyFromDate() != null && this.bean.getPreauthDataExtractionDetails().getAdmissionDate() != null){
	 	Long alertDte =null;
		Date policyStartDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
		
		Date admissionDate = this.bean.getPreauthDataExtractionDetails().getAdmissionDate();
		
		alertDte = SHAUtils.getDiffDays(policyStartDate, admissionDate);
	
	    MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
	    SHAUtils.popupMessageForWaitingPeriodForNovelCorona(alertDte);
		
	 }
	 SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_GRP_ALERT_MSG, "INFORMATION");
 }
}
