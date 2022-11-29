package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.SumInsuredBonusAlertUI;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewTopUpPolicyDetailsForGMC;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.enhacement.table.PreviousPreAuthDetailsTable;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.ViewClaimAmountDetils;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.preauth.wizard.pages.DiagnosisProcedureListenerTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.MedicalVerificationDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.TreatmentQualityVerificationDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
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
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.Calendar;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;
@Alternative
public class ClaimRequestMedicalDecisionPageUI  extends ViewComponent implements ClaimsSubmitHandler{
	private static final long serialVersionUID = -3466733459218208627L;

	//private static final SpecificProductDeductibleTableDTO preauthDTO = null;

	@Inject
	private PreauthDTO bean;

	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	@Inject
	private ViewBillSummaryPage viewBillSummary;

	@Inject
	private Instance<DiagnosisProcedureListenerTable> medicalDecisionTable;
	
	@Inject
	private Instance<NewMedicalDecisionFVRGrading> fvrGradingTableInstance;
	
	private NewMedicalDecisionFVRGrading fvrGradingTableObj;
	
	@Inject
	private Instance<MedicalVerificationTable> medicalVerificationInstance;
	
	@EJB
	private MasterService masterService;
	
	private MedicalVerificationTable medicalVerificationTableObj;
	
//	@Inject
//	private ClaimRequestFileUploadUI claimRequestFileUploadUI;
	
	@Inject
	private Instance<TreatmentQualityVerificationTable> treatmentVerificationTableInstance;
	
	private TreatmentQualityVerificationTable treatmentVerificationTableObj;

	@Inject
	private Instance<ClaimRequestMedicalDecisionButtons> claimRequestButtonInstance;

	@Inject
	private Instance<ViewClaimAmountDetils> viewClaimAmountDetails;
	
	@Inject
	private Instance<PreviousPreAuthDetailsTable> previousPreauthDetailsTableInstance;

	/*@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;*/

	private AmountConsideredTable amountConsideredTable;
	
	private PreviousPreAuthDetailsTable previousPreAuthDetailsTableObj;
	
	/*@Inject
	private Instance<AmountConsideredTable> balanceSumInsuredTableInstance;

	@Inject
	private Instance<SpecificProductPreviousClaims> specifictProductDeductibleTable;

	private SpecificProductPreviousClaims specifictProductDeductibleTableObj;

	private AmountConsideredTable balanceSumInsuredTableObj;*/

	private ClaimRequestMedicalDecisionButtons claimRequestButtonObj;

	private DiagnosisProcedureListenerTable medicalDecisionTableObj;

	private CheckBox investigationReportReviewedChk;

	private ComboBox investigatorName;

	private TextArea investigationReviewRemarks;
	
	private TextField txtMedicalRemarks;

	private TextField txtDoctorNote;

	private VerticalLayout wholeVLayout;

	private FormLayout investigationFLayout;


	private Button showBalanceSumInsured;

	private Button showClaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;

	private Label amountConsidered;

	private String preAuthRequestedAmt;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	Map<String, Object> referenceData = new HashMap<String, Object>();

	Map<String, Object> sublimitCalculatedValues;

	/*private Double balanceSumInsuredValue;

	private String diagnosisName;*/

	private Button initiatePEDButton;

	// Added for intiate field visit.

	private Button initiateFieldVisitButton;

	//private Double copayValue = 0d;

	@Inject
	private ViewDetails viewDetails;

	public Double value;

	private TextField approvedAmtField;

	//private TextField consideredAmtField;
	
	private GWizard wizard;
	
	@Inject
	private UploadedFileViewUI fileViewUI;

	private ClaimRequestFileUploadUI specialistWindow = new ClaimRequestFileUploadUI();
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	private List<String> errorMessages;
	
	@EJB
	private ReimbursementService reimbService;
	
	private Button addAdditionalFvrPointsBtn;
	@Inject
	private AddAdditionalFVRPointsPageUI addAdditionalFvrPointsPageUI;

	@EJB
	private PreauthService preauthService;
	
	@Inject
	private Instance<InvestigationReviewRemarksTable> invsReviewRemarksTableInstance;
	
	private InvestigationReviewRemarksTable invsReviewRemarksTableObj;
	
	private OptionGroup negotiation;
	private TextField txtClaimedAmt;
	private TextField txtNegotiationAmt;
	private TextField txtSavedAmt;
	private VerticalLayout negotiationLayout;
	
	@Inject
	private ViewTopUpPolicyDetailsForGMC viewTopUpGmcPage;
	
	private OptionGroup verifiedBonus;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@Inject
	private SumInsuredBonusAlertUI bonusAlertUI;
	
	private ComboBox rvoFindings;
	private ComboBox rvoReason;
	
	public void init(PreauthDTO bean) {
		this.bean = bean;

	}
	
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		errorMessages = new ArrayList<String>();
		
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}

	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(
				PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthMedicalDecisionDetails());
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	@Override
	public String getCaption() {
		return "Medical Decision";
	}

	public Component getContent() {
		initBinder();
		Long claimkeytemp = bean.getClaimKey();
		Long policyKeytemp = bean.getNewIntimationDTO().getPolicy().getKey();
		Long insuredKeytemp = bean.getNewIntimationDTO().getInsuredKey();
		List<DiagnosisDetailsTableDTO> diagnosisDetailsTableDTOList = bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		List<ProcedureDTO> procedureDetailsTableDTOList = bean.getPreauthDataExtractionDetails().getProcedureList();
		Object icdCodeList[] = new Object[diagnosisDetailsTableDTOList.size()];
		Object modernSublimitDiagSelected[] =  new Object[diagnosisDetailsTableDTOList.size()];;
		Object modernSublimitProcSelected[] =  new Object[procedureDetailsTableDTOList.size()];;
		for (int i= 0; i < diagnosisDetailsTableDTOList.size() ; i++ ) {
			DiagnosisDetailsTableDTO diagnosisDetailsTableDTO = diagnosisDetailsTableDTOList.get(i);
			SelectValue selectValue = diagnosisDetailsTableDTO.getIcdCode();
			String icdCodeValuetemp = selectValue.getCommonValue();
			icdCodeList[i] = icdCodeValuetemp;
			bean.setIsSublimitIdAvail(false);
			if( diagnosisDetailsTableDTO.getSublimitName() != null &&  diagnosisDetailsTableDTO.getSublimitName().getLimitId() != null){
			modernSublimitDiagSelected[i] = diagnosisDetailsTableDTO.getSublimitName().getLimitId();
			bean.setIsSublimitIdAvail(true);
			
			}
			if(ReferenceTable.STAR_CARDIAC_CARE_PLATIANUM.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){	
				if(null != bean.getPreauthDataExtractionDetails().getSection() && null != bean.getPreauthDataExtractionDetails().getSection().getId()	
						&& (ReferenceTable.POL_SECTION_2.equals(bean.getPreauthDataExtractionDetails().getSection().getId()))){	
					if( diagnosisDetailsTableDTO.getSublimitName() != null &&  diagnosisDetailsTableDTO.getSublimitName().getLimitId() != null	
							&& ReferenceTable.getMasClaimLimitSublimitKeysProd().containsKey(diagnosisDetailsTableDTO.getSublimitName().getLimitId())){	
						bean.setIsBalSIForSublimitCardicSelected(true);	
					}else {	
						bean.setIsBalSIForSublimitCardicSelected(false);	
					}	
				}	
			}
		}
		if(procedureDetailsTableDTOList != null && !procedureDetailsTableDTOList.isEmpty()){
		for (int i= 0; i < procedureDetailsTableDTOList.size() ; i++ ) {
			ProcedureDTO procedureDetailsTableDTO = procedureDetailsTableDTOList.get(i);
			if( procedureDetailsTableDTO != null && procedureDetailsTableDTO.getSublimitName() != null &&  procedureDetailsTableDTO.getSublimitName().getLimitId() != null){
			modernSublimitProcSelected[i] = procedureDetailsTableDTO.getSublimitName().getLimitId();
			bean.setIsSublimitIdAvail(true);
			}
		}	
		}
		Object modernSublimitSelected[] =concatenate(modernSublimitDiagSelected,modernSublimitProcSelected); 
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		System.out.println("SIBasedICDAlertMsg ::: policyKey : "+policyKeytemp+" insuredKey : "+insuredKeytemp+" claimkey : "+claimkeytemp+" icdCodeList : "+icdCodeList.toString());
		String alertMsg = dbCalculationService.getSIBasedICDAlertMsg(claimkeytemp, policyKeytemp, insuredKeytemp, icdCodeList);
		if(alertMsg != "null")
		{
			SHAUtils.showAlertMessageBox(alertMsg);
		}
		
		//CR2019056
		if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				||!ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
						&& bean.getClaimDTO().getClaimType() != null
						&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(this.bean.getClaimDTO().getClaimType().getId())) {
			
			if(SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getInvPendingFlag())) {
				SHAUtils.showAlertMessageBox(SHAConstants.MULTIPLE_CLAIMS_INV_PENDING_ALERT_MSG);
			}
		}	
		
		if(bean != null && bean.getPreauthDataExtractionDetails() != null && bean.getPreauthDataExtractionDetails().getIsHomeIsolationExist() !=null 
				&&bean.getPreauthDataExtractionDetails().getIsHomeIsolationExist()){
			SHAUtils.showAlertMessageBox(SHAConstants.HOME_ISOLATION_ALERT_MSG);
		}
		
		errorMessages = new ArrayList<String>();
		Button submitButton = this.wizard.getFinishButton();
		submitButton.setEnabled(false);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		initiatePEDButton = new Button("Initiate PED Endorsement");
		addAdditionalFvrPointsBtn = new Button("Add Additional FVR Points");
		addAdditionalFvrPointsBtn.setEnabled(false);
		if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrReplyReceived() && !bean.getPreauthMedicalDecisionDetails().getIsFvrReplyReceived() && 
				!(null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy())){
			addAdditionalFvrPointsBtn.setEnabled(true);
		}
		
		if((null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() 
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType()
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType().getKey()
				&& 2904 == bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue()) 
				&& null != bean.getNewIntimationDTO().getPolicy().getProduct().getCode()
				&& !bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)
				|| (ReferenceTable.STAR_CRITICARE_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
				|| (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
				|| (ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
				|| (ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) 
						&&bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))
				|| (ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) 
						&&bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))){
			initiatePEDButton.setEnabled(false);
		}
		else{
			initiatePEDButton.setEnabled(true);
		}
		//added for modern sublimit CR
		String modernSublimitFlag = dbCalculationService.getModernSublimitFlag(policyKeytemp, bean.getNewIntimationDTO().getPolicy().getProduct().getKey(), modernSublimitSelected);
		Double balanceSI =0d ;
		if(bean.getIsBalSIForSublimitCardicSelected()){	
			balanceSI = dbCalculationService.getBalanceSIForCardicCarePlatRemb(policyKeytemp,insuredKeytemp,claimkeytemp,bean.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);;	
			bean.setBalanceSI(balanceSI);
		}else{
			if(modernSublimitFlag != null && modernSublimitFlag.equalsIgnoreCase(SHAConstants.YES_FLAG))
			{
				bean.setIsModernSublimitSelected(true);
				balanceSI = dbCalculationService.getBalanceSIForModernSublimit(policyKeytemp,insuredKeytemp,claimkeytemp,bean.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);;
				bean.setBalanceSI(balanceSI);
				if(balanceSI != null && balanceSI == 0d){
					SHAUtils.showAlertMessageBox(SHAConstants.MODERN_SUBLIMIT_ALERT);
				}
			}else{
				bean.setIsModernSublimitSelected(false);
				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					balanceSI = dbCalculationService.getBalanceSIForGMC(bean.getNewIntimationDTO().getPolicy().getKey() ,bean.getNewIntimationDTO().getInsuredPatient().getKey(),bean.getClaimKey());
					bean.setBalanceSI(balanceSI);
				}else{
					balanceSI = dbCalculationService.getBalanceSIForReimbursement(bean.getNewIntimationDTO().getPolicy().getKey() ,bean.getNewIntimationDTO().getInsuredPatient().getKey(),bean.getClaimKey(),bean.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
					bean.setBalanceSI(balanceSI);
				}
			}
		}
		HorizontalLayout buttonHLayout = new HorizontalLayout(addAdditionalFvrPointsBtn,initiatePEDButton);
		buttonHLayout.setComponentAlignment(addAdditionalFvrPointsBtn,
				Alignment.MIDDLE_RIGHT);

		initiateFieldVisitButton = new Button("View Earlier ROD Details");
		// FormLayout
		HorizontalLayout buttonLayout = new HorizontalLayout(
				initiateFieldVisitButton);
		buttonLayout.setComponentAlignment(initiateFieldVisitButton,
				Alignment.MIDDLE_RIGHT);


		txtMedicalRemarks = (TextField) binder.buildAndBind("Medical Remarks",
				"medicalRemarks", TextField.class);
		txtMedicalRemarks.setMaxLength(4000);
		
		txtMedicalRemarks.setId("medicalRmrks");
		medicalRemarksListener(txtMedicalRemarks,null);
		txtMedicalRemarks.setData(bean);
		txtMedicalRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		
		//CSValidator validator = new CSValidator();
		// validator.extend(txtMedicalRemarks);
		// validator.setRegExp("^[a-zA-Z 0-9.]*$");
		// validator.setPreventInvalidTyping(true);
		txtDoctorNote = (TextField) binder.buildAndBind(
				"Doctor Note(Internal Remarks)", "doctorNote", TextField.class);
		txtDoctorNote.setMaxLength(4000);
		
		txtDoctorNote.setId("internalRmrks");
		medicalRemarksListener(txtDoctorNote,null);
		txtDoctorNote.setData(bean);
		txtDoctorNote.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		
		// validator = new CSValidator();
		// validator.extend(txtDoctorNote);
		// validator.setRegExp("^[a-zA-Z 0-9.]*$");
		// validator.setPreventInvalidTyping(true);

		investigationReportReviewedChk = (CheckBox) binder.buildAndBind(
				"Investigation Report Reviewed", "investigationReportReviewed",
				CheckBox.class);
		investigationReportReviewedChk.setEnabled(true);
		investigatorName = (ComboBox) binder.buildAndBind("Investigator Name",
				"investigatorName", ComboBox.class);
		investigatorName.setEnabled(true);
		investigationReviewRemarks = (TextArea) binder.buildAndBind(
				"Investigation Review Remarks", "investigationReviewRemarks",
				TextArea.class);
		investigationReviewRemarks.setEnabled(true);
		investigationReviewRemarks.setMaxLength(100);	
			
		/*rvoFindings = (ComboBox) binder.buildAndBind("RVO findings- Claim decision",
				"rvoFindings", ComboBox.class);
		rvoFindings.setEnabled(true);
		
		rvoReason = (ComboBox) binder.buildAndBind("Reasons for not accepting RVO findings",
				"rvoReason", ComboBox.class);
		rvoReason.setEnabled(true);	
		
		BeanItemContainer<SelectValue> rvoFindingsList = masterService.getTypeContainer(ReferenceTable.MASTER_TYPE_CODE_RVO_FINFINGS);
		rvoFindings.setContainerDataSource(rvoFindingsList);
		rvoFindings.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		rvoFindings.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> rvoReasonList = masterService.getTypeContainer(ReferenceTable.MASTER_TYPE_CODE_RVO_REASON);
		rvoReason.setContainerDataSource(rvoReasonList);
		rvoReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		rvoReason.setItemCaptionPropertyId("value");*/
		
		investigationFLayout = new FormLayout(investigationReportReviewedChk,
				investigatorName, investigationReviewRemarks/*,rvoFindings,rvoReason*/);
		
		invsReviewRemarksTableObj = invsReviewRemarksTableInstance.get();
		invsReviewRemarksTableObj.init();
		fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.INVS_REVIEW_REMARKS_LIST, this.bean);
		invsReviewRemarksTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList());
	//	HorizontalLayout specialistHLayout = new HorizontalLayout(investigationFLayout);
		
		verifiedBonus = (OptionGroup) binder.buildAndBind("Verified Bonus", "verifiedBonus",OptionGroup.class);
		verifiedBonus.addItems(getReadioButtonOptions());
		verifiedBonus.setItemCaption(true, "Yes");
		verifiedBonus.setItemCaption(false, "No");
		verifiedBonus.setStyleName("horizontal");
		verifiedBonus.setVisible(false);
		addListenerForVerifiedBonus();
		
		HorizontalLayout specialistHLayout = new HorizontalLayout(invsReviewRemarksTableObj);
		specialistHLayout.setWidth("100%");
		specialistHLayout.setSpacing(true);

		HorizontalLayout remarksHLayout = new HorizontalLayout(new FormLayout(verifiedBonus,txtMedicalRemarks),
				 new FormLayout(txtDoctorNote));
		remarksHLayout.setWidth("100%");

		claimRequestButtonObj = claimRequestButtonInstance.get();
		claimRequestButtonObj.initView(this.bean, wizard,investigationReportReviewedChk);
		
		//added for modern sublimit SI CR
		if(bean != null && bean.getIsModernSublimitSelected() && (bean.getBalanceSI() == 0d)){
			claimRequestButtonObj.getApproveBtn().setEnabled(false);
		}
		
		if(bean != null && bean.getIsBalSIForSublimitCardicSelected() && (bean.getBalanceSI() == 0d)){
			claimRequestButtonObj.getApproveBtn().setEnabled(false);
		}
				
		//CR2019017 - Start
		if(bean.getIsSDEnabled() && claimRequestButtonObj.getApproveBtn().isEnabled()){
			claimRequestButtonObj.getApproveBtn().setEnabled(false);
		}/*else{
			claimRequestButtonObj.getApproveBtn().setEnabled(true);
		}*/
		
		if(bean != null && bean.getPreauthDataExtractionDetails() != null && bean.getPreauthDataExtractionDetails().getIsHomeIsolationExist() !=null 
				&&bean.getPreauthDataExtractionDetails().getIsHomeIsolationExist()){
			claimRequestButtonObj.getApproveBtn().setEnabled(false);
		}
		
		
		bean.setClaimReqButtonObj(claimRequestButtonObj);
		//CR2019017 - End
		HorizontalLayout buttonsHLayout = new HorizontalLayout(
				claimRequestButtonObj);
		HorizontalLayout buttonWholeLayout = new HorizontalLayout(
				buttonsHLayout);
		buttonWholeLayout.setComponentAlignment(buttonsHLayout,
				Alignment.MIDDLE_CENTER);
		buttonWholeLayout.setWidth("100%");

		this.previousPreAuthDetailsTableObj = previousPreauthDetailsTableInstance
				.get();
		this.previousPreAuthDetailsTableObj.init("Cashless Approval Details", false, false);
		
		if(SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) > 0) {
			Integer amount = SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) - (SHAUtils.getIntegerFromString(this.bean.getPreHospitalisationValue()) + SHAUtils.getIntegerFromString(this.bean.getPostHospitalisationValue()));
			this.bean.setAmountConsidered(String.valueOf(amount));
			if(!this.bean.getHospitalizaionFlag()) {
				this.bean.setAmountConsidered(String.valueOf(this.bean.getHospitalizationAmount()));
			}
			
		}
		viewBillSummary.init(bean,bean.getKey(),true);
		Map<String, String> payableAmount = viewBillSummary.getPayableAmount();
		String string = payableAmount.get(SHAConstants.HOSPITALIZATION);
		this.bean.setAmountConsidered(String.valueOf(SHAUtils.getDoubleValueFromString(string).intValue()));
		
		// For lumpsum, amount consider will be setted as bill value from bill entry...
		StarCommonUtils.setAmountconsideredForLumpsum(bean);
		
		amountConsidered = new Label("Amount considered");
		amountConsidered.setCaption("Amount considered");
		amountConsidered.setValue(this.bean.getAmountConsidered());
		//
		// balanceSumInsured = new Label("Balance Sum Insured");
		// balanceSumInsured.setCaption("Balance Sum Insured");

		showBalanceSumInsured = new Button("View");
		showClaimAmtDetailsBtnDuplicate = new Button("View");
		amountConsideredViewButton = new Button("View");
		showBalanceSumInsured.setStyleName("link");
		showClaimAmtDetailsBtnDuplicate.setStyleName("link");

		amountConsideredViewButton.setStyleName("link");
		amountConsideredViewButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;
			

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("90%");
				popup.setHeight("85%");
				
				viewBillSummary.init(bean.getKey());
				
				Panel mainPanel = new Panel(viewBillSummary);
		        mainPanel.setWidth("90%");
		        mainPanel.setHeight("90%");
				popup.setContent(mainPanel);
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
		});

		// HorizontalLayout amountRequestedLayout = new HorizontalLayout(new
		// FormLayout(preauthAmtRequested), showclaimAmtDetailsBtnDuplicate );
		HorizontalLayout amountConsideredLayout = new HorizontalLayout(
				new FormLayout(amountConsidered),new FormLayout( amountConsideredViewButton));
		amountConsideredLayout.setHeight("55px");
		// HorizontalLayout wholeAmtLayout = new
		// HorizontalLayout(amountRequestedLayout, new
		// VerticalLayout(amountConsideredLayout, new
		// FormLayout(balanceSumInsured)));
		// wholeAmtLayout.setSpacing(true);
		// wholeAmtLayout.setWidth("100%");
		// wholeAmtLayout.setComponentAlignment(amountRequestedLayout,
		// Alignment.MIDDLE_RIGHT);

		// This is used to set the balance SumInsured from DB.
		// fireViewEvent(PreauthWizardPresenter.BALANCE_SUM_INSURED,
		// this.bean.getPolicyDto());
		/**
		 * Balance SI procedure requires insured key as one of the parameter for
		 * calculation. Insured key will be available in new intimation dto and
		 * not in policy dto. Hence commented above code and added below one.
		 * */
//		fireViewEvent(PreauthWizardPresenter.BALANCE_SUM_INSURED, this.bean);

		this.medicalDecisionTableObj = medicalDecisionTable.get();
		this.medicalDecisionTableObj.init(this.bean);
		this.medicalDecisionTableObj.setVisibleColumns();

		medicalVerificationTableObj = medicalVerificationInstance.get();
		medicalVerificationTableObj.init("Medical Verification", false);
		
		treatmentVerificationTableObj = treatmentVerificationTableInstance.get();
		treatmentVerificationTableObj.init("Treatment Quality Verification", false);
		//treatmentVerificationTableObj.setWidth("1500px");
		
		//IMSSUPPOR-24086
		if(this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO() == null || this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO().isEmpty()){
			fireViewEvent(
					ClaimRequestMedicalDecisionPagePresenter.MA_FIELD_VISIT_GRADING,
					this.bean);
		}
		
		fvrGradingTableObj = null;
		if(this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO() != null && !this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO().isEmpty()) {
			fvrGradingTableObj = fvrGradingTableInstance.get();
			fvrGradingTableObj.initView(bean, false);
			
		}
		VerticalLayout verticalLayout = new VerticalLayout();
		if(fvrGradingTableObj != null) {
			verticalLayout.addComponent(fvrGradingTableObj);
		}
		
//		negotiationLayout = buildNegotiationLayout(true); //Remove this layout as per revised document version for R20181286
		
		

		wholeVLayout = new VerticalLayout(buttonHLayout, this.previousPreAuthDetailsTableObj,
				amountConsideredLayout, this.medicalDecisionTableObj,
				 specialistHLayout, verticalLayout, medicalVerificationTableObj, treatmentVerificationTableObj, 
				 remarksHLayout,buttonsHLayout);
		wholeVLayout.setComponentAlignment(buttonHLayout,
				Alignment.MIDDLE_RIGHT);
//		wholeVLayout
//				.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
		wholeVLayout.setSpacing(true);

		// mandatoryFields.add(investigatorName);
		mandatoryFields.add(investigationReviewRemarks);
//		mandatoryFields.add(txtMedicalRemarks);
		showOrHideValidation(false);
//		unbindField(investigationReviewRemarks);
		addListener();
		addPEDListener();
		addAdditionalFvrPointsListener();
		addMedicalDecisionTableFooterListener();
		
		if(SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getInvPendingFlag())) {
			claimRequestButtonObj.getApproveBtn().setEnabled(false);
		}	
		
		//GLx2020132
		Date intiDate=bean.getNewIntimationDTO().getCreatedDate();
		Long DiffDays=SHAUtils.getDaysBetweenDate(intiDate,SHAUtils.getFromDate(BPMClientContext.EXCUDED_PROVIDER_DATE));
		
		if((bean.getNewIntimationDTO().getHospitalDto().getExclusionProvideFlag() != null && bean.getNewIntimationDTO().getHospitalDto().getExclusionProvideFlag().equals(SHAConstants.YES_FLAG) && 
 				bean.getPreauthDataExtractionDetails().getEmergency().equals(Boolean.FALSE)) && DiffDays<=0) {
			claimRequestButtonObj.getApproveBtn().setEnabled(false);
		}

		return wholeVLayout;
	}

	
	
	/*private void getSpecificTableDTO(Integer approvedAmt) {
		SpecificProductDeductibleTableDTO dto = new SpecificProductDeductibleTableDTO();
		dto.setClaims("Claims ");
		Integer floatFromString = SHAUtils.getIntegerFromString(this.bean
				.getAmountConsidered());
		dto.setAmountConsidered(floatFromString);
		dto.setOriginalSI(this.bean.getNewIntimationDTO().getPolicy()
				.getTotalSumInsured() != null ? this.bean.getNewIntimationDTO()
				.getPolicy().getTotalSumInsured().intValue() : 0);
		dto.setAmountPayable(approvedAmt);
		dto.setDeductible(300000);
		Integer max = Math.max(dto.getAmountConsidered(), dto.getOriginalSI());
		dto.setAmountToBeConsidered(max);
		Integer value = max - dto.getDeductible();
		dto.setEligibleAmountPayable(value);
		dto.setBalanceSI(preauthDTO.getBalanceSI() != null ? preauthDTO
				.getBalanceSI().intValue() : 0);
		Integer payableAmt = Math.min(value, dto.getBalanceSI());
		dto.setPayableAmount(payableAmt);
		// previousClaimList.add(dto);
	}*/

	public void addPEDListener() {
		initiatePEDButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8159939563947706329L;

			@Override
			public void buttonClick(ClickEvent event) {
				Long preauthKey = bean.getKey();
				Long intimationKey = bean.getIntimationKey();
				Long policyKey = bean.getPolicyKey();
				Long claimKey = bean.getClaimKey();
				if(bean.getIsPEDInitiatedForBtn()) {
					alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
				} else {
					createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
				}
			}
		});
	}

	
	private void createPEDScreen(Long preauthKey, Long intimationKey,
			Long policyKey, Long claimKey) {
		ViewPEDRequestWindow viewPEDRequest = viewPedRequest.get();
		viewPEDRequest.initView(bean,preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.CLAIM_REQUEST_STAGE,false);
		viewPEDRequest.setPresenterString(SHAConstants.REIMBURSEMENT_SCREEN);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View PED Request Details");
		popup.setWidth("85%");
		popup.setHeight("100%");
		popup.setContent(viewPEDRequest);
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
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	

	
	
public Boolean alertMessageForPEDInitiate(String message) {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
	
	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
			.createInformationBox(message, buttonsNamewithType);
	Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				Long preauthKey=bean.getKey();
				Long intimationKey=bean.getIntimationKey();
				Long policyKey=bean.getPolicyKey();
				Long claimKey=bean.getClaimKey();
				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
			}
		});
		return true;
	}
	protected void addListener() {

		//R0953
		/*txtDoctorNote.addValueChangeListener(new Property.ValueChangeListener() {
		    public void valueChange(ValueChangeEvent event) {
		        // Assuming that the value type is a String
		        String value = (String) event.getProperty().getValue();

		        txtDoctorNote.setDescription(value);
		    }
		});*/
		
		
		// initiateFieldVisitButton = new Button("Intiate Investigation");


		showBalanceSumInsured.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4478247898237407113L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO() != null
						&& bean.getNewIntimationDTO().getIntimationId() != null
						&& !bean.getNewIntimationDTO().getIntimationId()
								.equals("")) {
					fireViewEvent(
							ClaimRequestMedicalDecisionPagePresenter.VIEW_BALANCE_SUM_INSURED_DETAILS,
							bean.getNewIntimationDTO().getIntimationId());
				}
			}
		});

		showClaimAmtDetailsBtnDuplicate.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1159870471084252041L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						ClaimRequestMedicalDecisionPagePresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
						true);
			}
		});
		
		investigationReportReviewedChk.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
//				Boolean value =(Boolean)event.getProperty().getValue();
				Boolean checkValue = investigationReportReviewedChk.getValue();
				
				if(checkValue != null && checkValue){
					investigatorName.setEnabled(true);
					investigationReviewRemarks.setEnabled(true);
					mandatoryFields.remove(investigationReviewRemarks);
					mandatoryFields.add(investigationReviewRemarks);
				}
				else{
					investigatorName.setEnabled(false);
					investigationReviewRemarks.setEnabled(false);
					
				}
				
			}
		});


//		amountConsideredViewButton.addClickListener(new ClickListener() {
//
//			private static final long serialVersionUID = -2259148886587320228L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				fireViewEvent(
//						ClaimRequestMedicalDecisionPagePresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
//						true);
//			}
//		});

	}

	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		this.claimRequestButtonObj.setReferenceData(referenceData);
		@SuppressWarnings("unchecked")
		BeanItemContainer<TmpInvestigation> investigatorNameContainer = (BeanItemContainer<TmpInvestigation>) referenceData
				.get("investigatorName");
		BeanItemContainer<SelectValue> medicalVerificationContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("medicalVerification");
		BeanItemContainer<SelectValue> treatmentVerificationContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("treatmentQualityVerification");

		investigatorName.setContainerDataSource(investigatorNameContainer);
		investigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		investigatorName.setItemCaptionPropertyId("investigatorName");
		
		if (this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName() != null && this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName().getKey() != null) {
			for(int i=0; i<investigatorNameContainer.size() ; i++){
				if(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName().getKey().equals(investigatorNameContainer.getIdByIndex(i).getKey())){
					
					this.investigatorName.setValue(investigatorNameContainer.getIdByIndex(i));
				}
			}
			
		}
		
		
		List<MedicalVerificationDTO> medicalVerificationTableDTO = this.bean.getPreauthMedicalDecisionDetails().getMedicalVerificationTableDTO();
		this.medicalVerificationTableObj.setReference(referenceData);
		if(!medicalVerificationTableDTO.isEmpty()) {
			int i = 1;
			for (MedicalVerificationDTO medicalVerificationDTO : medicalVerificationTableDTO) {
				medicalVerificationDTO.setSlNo(i);
				this.medicalVerificationTableObj.addBeanToList(medicalVerificationDTO);
				i++;
			}
			
			
		} else {
			int i = 1;
			List<SelectValue> itemIds = medicalVerificationContainer.getItemIds();
			for (SelectValue selectValue : itemIds) {
				MedicalVerificationDTO verificationDTO = new MedicalVerificationDTO();
				verificationDTO.setDescription(selectValue.getValue());
				verificationDTO.setDescriptionId(selectValue.getId());
				verificationDTO.setSlNo(i);
				this.medicalVerificationTableObj.addBeanToList(verificationDTO);
				i++;
			}
		}
		
		List<TreatmentQualityVerificationDTO> treatmentVerificationTableDTO = this.bean.getPreauthMedicalDecisionDetails().getTreatmentVerificationDTO();
		this.treatmentVerificationTableObj.setReference(referenceData);
		if(!treatmentVerificationTableDTO.isEmpty()) {
			int i = 1;
			for (TreatmentQualityVerificationDTO treatmentQualityVerificationDTO : treatmentVerificationTableDTO) {
				treatmentQualityVerificationDTO.setSlNo(i);
				this.treatmentVerificationTableObj.addBeanToList(treatmentQualityVerificationDTO);
				i++;
			}
			
		} else {
			int i = 1;
			List<SelectValue> itemIds = treatmentVerificationContainer.getItemIds();
			for (SelectValue selectValue : itemIds) {
				TreatmentQualityVerificationDTO verificationDTO = new TreatmentQualityVerificationDTO();
				verificationDTO.setDescription(selectValue.getValue());
				verificationDTO.setDescriptionId(selectValue.getId());
				verificationDTO.setSlNo(i);
				this.treatmentVerificationTableObj.addBeanToList(verificationDTO);
				i++;
			}
		}
		
		
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				addDiagnosisToMedicalDecision(medicalDecisionDTOList, pedValidationTableDTO, false, false);
			}
			
			if(!this.bean.getDeletedDiagnosis().isEmpty()) {
				List<DiagnosisDetailsTableDTO> deletedDiagnosis = this.bean.getDeletedDiagnosis();
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDiagnosis) {
					addDiagnosisToMedicalDecision(medicalDecisionDTOList, diagnosisDetailsTableDTO, true, true);
				}
			}
			
			
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				addProcedureToMedicalDecision(medicalDecisionDTOList, procedureDTO, false, false);
			}
			
			List<ProcedureDTO> deletedProcedureList = bean.getDeletedProcedure();
			if(!deletedProcedureList.isEmpty()) {
				for (ProcedureDTO procedureDTO : deletedProcedureList) {
					addProcedureToMedicalDecision(medicalDecisionDTOList, procedureDTO, true, true);
				}
			}

			Map<String, Object> caluculationInputValues = new WeakHashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			Double insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));

			if (null != medicalDecisionDTOList
					&& medicalDecisionDTOList.size() > 0) {
				int diag = 1;
				int proc = 1;
				for (int i = 0; i < medicalDecisionDTOList.size(); i++) {
					DiagnosisProcedureTableDTO medicalDecisionDto = medicalDecisionDTOList
							.get(i);
					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Diag "
								+ (diag++));
						caluculationInputValues
						.put("restrictedSI",
								medicalDecisionDto
								.getDiagnosisDetailsDTO()
								.getSumInsuredRestriction() != null ? medicalDecisionDto
								.getDiagnosisDetailsDTO()
								.getSumInsuredRestriction().getId()
								: null);
				caluculationInputValues
				.put("restrictedSIAmount",
						medicalDecisionDto
								.getDiagnosisDetailsDTO()
								.getSumInsuredRestriction() != null ? medicalDecisionDto
								.getDiagnosisDetailsDTO()
								.getSumInsuredRestriction().getValue()
								: null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? 0l
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
								.getDiagnosisName() != null ? medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName().getId().toString() : "");
						caluculationInputValues.put("referenceFlag", "D");
					} else if (medicalDecisionDto.getProcedureDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Proc "
								+ (proc++));
						caluculationInputValues.put("restrictedSI", null);
						caluculationInputValues.put("restrictedSIAmount", null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues
								.put("diagOrProcId",
										(medicalDecisionDto.getProcedureDTO().getProcedureName() == null  || medicalDecisionDto.getProcedureDTO().getNewProcedureFlag().equals(1l)) ? 0l
												: medicalDecisionDto
														.getProcedureDTO()
														.getProcedureName().getId());
						caluculationInputValues.put("referenceFlag", "P");
					}
					caluculationInputValues.put("preauthKey", !this.bean.getPreviousPreauthKey().equals(0l) ? this.bean.getPreviousPreauthKey():this.bean.getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, this.bean.getClaimKey());
					
					if(this.bean.getClaimDTO().getLatestPreauthKey() != null){
				    	caluculationInputValues.put("preauthKey", this.bean.getClaimDTO().getLatestPreauthKey());
				    }
					
					if((this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) || (this.bean.getIsHospitalizationRepeat() != null && this.bean.getIsHospitalizationRepeat())) {
						caluculationInputValues.put("preauthKey", 0l);
					}
					
					medicalDecisionDto.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount")).toString()
							: "NA");
					fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.SUM_INSURED_CALCULATION,caluculationInputValues, medicalDecisionDto,this.bean);
				}
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				this.medicalDecisionTableObj.addBeanToList(dto);
			}
		} else {

			Map<String, Object> caluculationInputValues = new WeakHashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			Double insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));
			caluculationInputValues.put("preauthKey", this.bean.getPreauthKey() != null ? this.bean.getPreauthKey() : 0l);
			
			caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
			caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
			caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
			caluculationInputValues.put(SHAConstants.CLAIM_KEY, bean.getClaimKey());
			
			bean.setIsNonAllopathic((Boolean)caluculationInputValues.get(SHAConstants.IS_NON_ALLOPATHIC));
			
			DiagnosisProcedureTableDTO residualDTO = null;
			
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : filledDTO) {
				if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					Boolean procedureStatus = SHAUtils.getProcedureStatus(diagnosisProcedureTableDTO.getProcedureDTO());
					if(!procedureStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(procedureStatus);
					SHAUtils.fillDetailsForUtilForProcedure(caluculationInputValues, diagnosisProcedureTableDTO);
				} else if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
					Boolean diagnosisStatus = SHAUtils.getDiagnosisStatus(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO());
					if(!diagnosisStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(diagnosisStatus);
					SHAUtils.fillDetailsForUtilizationForDiag(caluculationInputValues, diagnosisProcedureTableDTO);
				}
				if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null || diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.SUM_INSURED_CALCULATION,caluculationInputValues, diagnosisProcedureTableDTO,this.bean);
				}else{
					residualDTO = diagnosisProcedureTableDTO;
				}
			
				
			}
			
			if(residualDTO != null){
				this.medicalDecisionTableObj.addBeanToList(residualDTO);
			}
//			this.medicalDecisionTableObj.addList(filledDTO);
		
		}
		
		
		List<PreviousPreAuthTableDTO> previousPreauthList = (List<PreviousPreAuthTableDTO>) referenceData
				.get("previousPreauth");
		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		if(previousPreauthList != null) {
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
				if (!(this.bean.getKey() != null && this.bean.getKey().equals(
						previousPreAuthTableDTO.getKey()))) {
					fireViewEvent(
							ClaimRequestMedicalDecisionPagePresenter.GET_PREAUTH_REQUESTED_AMOUT,
							previousPreAuthTableDTO);
					previousPreAuthTableDTO.setRequestedAmt(preAuthRequestedAmt);
					newList.add(previousPreAuthTableDTO);
				}
			}
		}
		
		this.previousPreAuthDetailsTableObj.setTableList(newList);
		if(fvrGradingTableObj != null) {
			fvrGradingTableObj.setupReferences(referenceData);
		}
		
		
	}

	private void addProcedureToMedicalDecision(
			List<DiagnosisProcedureTableDTO> medicalDecisionDTOList,
			ProcedureDTO procedureDTO, Boolean isZeroApprAmt, Boolean isDeletedOne) {
		DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
		dto.setProcedureDTO(procedureDTO);
		if (procedureDTO.getConsiderForPaymentFlag() != null) {
			Boolean isPaymentAvailable = procedureDTO
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
			
			if(isPaymentAvailable) {
				if(procedureDTO.getExclusionDetails() != null && procedureDTO.getExclusionDetails().getValue() != null && !procedureDTO.getExclusionDetails().getValue().toLowerCase().equalsIgnoreCase("not applicable")) {
					isPaymentAvailable = false;
				}
			}
			
			if (!isPaymentAvailable) {
				dto.setMinimumAmount(0);
				dto.setReverseAllocatedAmt(0);
			}
			dto.setIsPaymentAvailable(isPaymentAvailable);
			if(isZeroApprAmt) {
				dto.setMinimumAmount(0);
				dto.setReverseAllocatedAmt(0);
				dto.setIsPaymentAvailable(false);
			}
		}
		dto.setRestrictionSI("NA");
		
		dto.setPackageAmt("NA");
		if(procedureDTO.getPackageRate() != null && procedureDTO.getPackageRate() >= 0) {
			dto.setPackageAmt(procedureDTO.getPackageRate().toString());
		}
		

		if (procedureDTO.getSublimitName() == null) {
			dto.setSubLimitAmount("NA");
		} else {
			dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
					procedureDTO.getSublimitAmount()).toString());
		}
		dto.setIsDeletedOne(isDeletedOne);
		

		dto.setIsAmbChargeFlag(procedureDTO.getIsAmbChargeFlag());
		dto.setAmbulanceCharge(procedureDTO.getAmbulanceCharge());
		dto.setAmtWithAmbulanceCharge(procedureDTO.getAmtWithAmbulanceCharge());
		
		
		medicalDecisionDTOList.add(dto);
	}

	private void addDiagnosisToMedicalDecision(
			List<DiagnosisProcedureTableDTO> medicalDecisionDTOList,
			DiagnosisDetailsTableDTO pedValidationTableDTO, Boolean isZeroApprAmt, Boolean isDeletedOne) {
		DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue() != 2904 ||
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS) ||
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS) ||
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_CONTINUITY_PRODUCT_CODE) &&
				bean.getNewIntimationDTO().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_WITH_CONTINUITY_SECTION_CODE)){
			
			dto.setPedImpactOnDiagnosis(pedValidationTableDTO.getPedImpactOnDiagnosis() != null && pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() != null ? pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() : "");
			dto.setNotPayingReason(pedValidationTableDTO.getReasonForNotPaying() != null && pedValidationTableDTO.getReasonForNotPaying().getValue() != null ? pedValidationTableDTO.getReasonForNotPaying().getValue() : "");
			dto.setReasonForNotPaying(pedValidationTableDTO.getReasonForNotPaying() != null ? pedValidationTableDTO.getReasonForNotPaying() : null);
		}
		
		if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
			Boolean isPaymentAvailable = pedValidationTableDTO
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
			if (isPaymentAvailable) {
				List<PedDetailsTableDTO> pedList = pedValidationTableDTO
						.getPedList();
				if (!pedList.isEmpty()) {
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {

						List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
								.getExclusionAllDetails();
						String paymentFlag = "y";
						if(exclusionAllDetails != null){
						for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
								if (null != pedDetailsTableDTO
										.getExclusionDetails()
										&& exclusionDetails
												.getKey()
												.equals(pedDetailsTableDTO
														.getExclusionDetails()
														.getId())) {
									paymentFlag = exclusionDetails
											.getPaymentFlag();
								}
							}
						}

						if (paymentFlag.toLowerCase().equalsIgnoreCase(
								"n")) {
							isPaymentAvailable = false;
							break;
						}
					}
				}
			}

			if (!isPaymentAvailable) {
				dto.setMinimumAmount(0);
				dto.setReverseAllocatedAmt(0);
			}
			dto.setIsPaymentAvailable(isPaymentAvailable);
			
			if(isZeroApprAmt) {
				dto.setIsPaymentAvailable(false);
				dto.setMinimumAmount(0);
				dto.setReverseAllocatedAmt(0);
			}
		}
		if (pedValidationTableDTO.getSumInsuredRestriction() == null) {
			dto.setRestrictionSI("NA");
		} else {
			dto.setRestrictionSI(SHAUtils.getIntegerFromString(
					pedValidationTableDTO.getSumInsuredRestriction()
							.getValue()).toString());
		}

		if (pedValidationTableDTO.getSublimitName() == null) {
			dto.setSubLimitAmount("NA");
		} else {
			dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
					pedValidationTableDTO.getSublimitAmt()).toString());
		}
		dto.setPackageAmt("NA");
		dto.setDiagnosisDetailsDTO(pedValidationTableDTO);
		dto.setIsDeletedOne(true);
		
		dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
		dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
		dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());
		
		medicalDecisionDTOList.add(dto);
	}

	/*private void setResidualAmtToDTO() {
		List<DiagnosisProcedureTableDTO> values = this.medicalDecisionTableObj
				.getValues();
		for (DiagnosisProcedureTableDTO medicalDecisionTableDTO : values) {
			if (medicalDecisionTableDTO.getDiagOrProcedure() != null
					&& medicalDecisionTableDTO.getDiagOrProcedure().contains(
							"Residual")) {
				ResidualAmountDTO residualAmountDTO = this.bean
						.getResidualAmountDTO();
				residualAmountDTO.setNetAmount(medicalDecisionTableDTO
						.getNetAmount() != null ? medicalDecisionTableDTO
						.getNetAmount().doubleValue() : 0d);
				residualAmountDTO.setMinimumAmount(medicalDecisionTableDTO
						.getMinimumAmountOfAmtconsideredAndPackAmt() != null ? medicalDecisionTableDTO
						.getMinimumAmountOfAmtconsideredAndPackAmt().doubleValue() : 0d);
				residualAmountDTO.setCopayPercentage(Double
						.valueOf(medicalDecisionTableDTO.getCoPayPercentage()
								.getValue()));
				residualAmountDTO.setCopayAmount(medicalDecisionTableDTO
						.getCoPayAmount() != null ? medicalDecisionTableDTO
						.getCoPayAmount().doubleValue() : 0d);
				residualAmountDTO.setApprovedAmount(medicalDecisionTableDTO
						.getMinimumAmount() != null ? medicalDecisionTableDTO
						.getMinimumAmount().doubleValue() : 0);
				residualAmountDTO.setNetApprovedAmount(medicalDecisionTableDTO
						.getReverseAllocatedAmt() != null ? medicalDecisionTableDTO
						.getReverseAllocatedAmt().doubleValue() : 0);
				residualAmountDTO.setRemarks(medicalDecisionTableDTO
						.getRemarks());
			}
		}
	}*/

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

	public boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();

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
		
		this.bean.getPreauthMedicalDecisionDetails().setReferToBillEntryBillingRemarks("");
		
		bean.setIsFVRAlertOpened(preauthService.getFVRStatusByClaimKey(bean.getClaimKey(),bean.getRodNumber()));
		
		
			if(bean != null && bean.getIsFVRAlertOpened() != null && !bean.getIsFVRAlertOpened() && !(SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getFvrAlertFlag()))){
				if(bean.getStatusKey() != null && !(SHAUtils.getInitiateFVRStatusMap().containsKey(bean.getStatusKey()))){
					if(!bean.getIsFvrClicked() && !SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getFvrAlertFlag())){
						if(!SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getReconsiderationFlag()) && !(bean.getIsDishonoured()
								|| (bean.getPreauthDataExtractionDetails().getIllness() != null && bean
								.getPreauthDataExtractionDetails().getIllness().getId()
								.equals(ReferenceTable.RELATED_TO_EARLIER_CLAIMS))
						|| (null != bean.getPreauthDataExtractionDetails()
								.getHospitalisationDueTo()
								&& !bean.getMaternityFlag() && bean
								.getPreauthDataExtractionDetails()
								.getHospitalisationDueTo().getId()
								.equals(ReferenceTable.MATERNITY_MASTER_ID)))){
							hasError = true;
							eMsg.append("FVR has not been initiated. Please Initiate FVR</br>");
						}
						else if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
								||!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
								&& bean.getClaimDTO().getClaimType() != null
								&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(bean.getClaimDTO().getClaimType().getId())
								&& (SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getMulticlaimAvailFlag()) && bean.getIsFvrInitiate() != null && !bean.getIsFvrInitiate())) {
							hasError = true;
							eMsg.append("Please Initiate FVR</br>");
						}
					}
					else if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							||!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
							&& bean.getClaimDTO().getClaimType() != null
							&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(bean.getClaimDTO().getClaimType().getId())
							&& (SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getMulticlaimAvailFlag()) && bean.getIsFvrInitiate() != null && !bean.getIsFvrInitiate())) {
						hasError = true;
						eMsg.append("Please Initiate FVR</br>");
					}
				}
				else if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
						||!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
						&& bean.getClaimDTO().getClaimType() != null
						&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(bean.getClaimDTO().getClaimType().getId())
						&& (SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getMulticlaimAvailFlag()) && bean.getIsFvrInitiate() != null && !bean.getIsFvrInitiate())) {
					hasError = true;
					eMsg.append("Please Initiate FVR</br>");
				}							
			}
			else if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					||!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
					&& bean.getClaimDTO().getClaimType() != null
					&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(bean.getClaimDTO().getClaimType().getId())
					&& (SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getMulticlaimAvailFlag()) && bean.getIsFvrInitiate() != null && !bean.getIsFvrInitiate())) {
				hasError = true;
				eMsg.append("Please Initiate FVR</br>");
			}
			
		/*if(bean.getPreauthMedicalDecisionDetails()!= null && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiatedMA() != null && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiatedMA().equals(Boolean.FALSE)){
			if(bean.getStatusKey() != null && !(SHAUtils.getInitiateFVRStatusMap().containsKey(bean.getStatusKey()))){
				if(!bean.getIsFvrClicked() && !SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getFvrAlertFlag())){
					hasError = true;
					eMsg.append("FVR has not been initiated. Please Initiate FVR</br>");
				}
			}
			else{
				if(!bean.getIsFvrInitiate() && !bean.getIsFvrNotRequiredAndNotSelected()){
					hasError = true;
					eMsg.append("Please select Approve or Query or Reject or Escalate Claim or Refer to coordinator. </br>");
				}
			}
		}*/
		
		if(this.bean.getStatusKey() != null && ! this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)){
			
			if(this.medicalVerificationTableObj != null && !this.medicalVerificationTableObj.isValid()) {
				hasError = true;
				List<String> errors = this.medicalVerificationTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		
		
		if(this.treatmentVerificationTableObj != null && !this.treatmentVerificationTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		if(this.treatmentVerificationTableObj != null && !this.treatmentVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj.getErrorsForRemarks();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		if(this.medicalVerificationTableObj != null && !this.medicalVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.medicalVerificationTableObj.getErrorsforRemarks();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		
		/*List<FvrGradingDetailsDTO> fvrGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
		if(!fvrGradingDTO.isEmpty()) {
			for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrGradingDTO) {
				List<FVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getFvrGradingDTO();
				for (FVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
					if(fvrGradingDTO3.getStatus() == null) {
						hasError = true;
						eMsg.append("Please Select All the FVR Grading and set the Status. </br>");
						break;
					}
				}
			}
		}*/
		Boolean isShown = Boolean.FALSE;
		if (!this.claimRequestButtonObj.isValid()) {
			hasError = true;
			List<String> errors = this.claimRequestButtonObj.getErrors();
			for (String error : errors) {
				if(error.equalsIgnoreCase("Please select Approve or Reject or Escalate Claim or Refer to coordinator. </br>")){
					isShown = Boolean.TRUE;
				}
				eMsg.append(error);
			}
			
			if(!isShown){
				if(!bean.getIsFvrInitiate() && !bean.getIsFvrNotRequiredAndSelected()){
					hasError = true;
					eMsg.append("Please select Approve or Query or Reject or Escalate Claim or Refer to coordinator. </br>");
				}	
			}
		}
     
		/*if(bean.getIsInvestigation() && (investigationReviewRemarks.getValue() == null || investigatorName.getValue() == null || (investigationReportReviewedChk.getValue() == null || !investigationReportReviewedChk.getValue()) )) {
			hasError = true;
			eMsg.append("Please Enter Investigation Remarks and Name");
		}*/
		/*int invCount = 0;
		if(this.bean.getStatusKey() != null && (ReferenceTable.invsAlertRequiredStatus()).containsKey(this.bean.getStatusKey())){
			if(null != bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList() && !bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().isEmpty())
			{
				for (AssignedInvestigatiorDetails invsObj : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {			
				
					if(null == invsObj.getReportReviewed() || !(invsObj.getReportReviewed().equalsIgnoreCase(SHAConstants.YES_FLAG))){
						hasError = true;
						eMsg.append("Investigation Review required").append("</br>");
						break;
					}
					invCount++;
				}
				
				if(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().size() == invCount){
					for (AssignedInvestigatiorDetails invsObj : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) 
					{		
						if((null != invsObj.getReportReviewed() && invsObj.getReportReviewed().equalsIgnoreCase(SHAConstants.YES_FLAG)) &&
								(null == invsObj.getReviewRemarks() || ("").equalsIgnoreCase(invsObj.getReviewRemarks()))){
							hasError = true;
							if(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().size()== 1){
								eMsg.append("Investigation Review Remarks required for Selected Item").append("</br>");
								break;
							}
							else
							{
								eMsg.append("Investigation Review Remarks required for Selected Items").append("</br>");
								break;
							}
						}
					}
				}
			}
		}*/
		/*if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS) && bean.getModeOfReceipt() != null 
				&& !bean.getModeOfReceipt().equals(ReferenceTable.MODE_OF_RECEIPT_ONLINE)
				&& !bean.getModeOfReceipt().equals(ReferenceTable.MODE_OF_RECEIPT_EMAIL)){
			BPMClientContext clientContext = new BPMClientContext();
			String documentGateWayUrl = clientContext.getDocumentGateWayUrl();
			if(null != documentGateWayUrl &&  documentGateWayUrl.equalsIgnoreCase("M")){
				if(bean.getCheckerVerified() == null){
					hasError = true;
					eMsg.append("Physical document is not vierified</br>");
				}
			}
		}*/

//		if (!this.medicalDecisionTableObj.getTotalAmountConsidered().equals(
//				SHAUtils.getIntegerFromString(this.bean.getAmountConsidered()))) {
//			hasError = true;
//			eMsg += "Total Amount Considered Should be equal to Data Extraction Page Payable Amount. </br>";
//		}

		if(!this.bean.getIsScheduleClicked() && (null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy())){
			hasError = true;
			eMsg.append("Please Verify View Policy Schedule Button.</br>");
		}
		
		if(!this.bean.getIsBusinessProfileClicked()){
			hasError = true;
			eMsg.append("Please Verify View Mini Business Profile.</br>");
		}
		if(!this.bean.getIsInsuredChannedNameClicked()){
			hasError = true;
			eMsg.append("Please Verify Insured/Channel Name Button.</br>");
		}
				

		//New FVR GRADING SEG A,B&C
		List<FvrGradingDetailsDTO> fvrBGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
		if(fvrBGradingDTO !=null && !fvrBGradingDTO.isEmpty()) {
			int i=0;
			for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrBGradingDTO) {
				i++;
				if(fvrGradingDetailsDTO.getIsSegmentBNotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentBNotEdit() && fvrGradingDetailsDTO.getIsSegmentANotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentANotEdit()){
					List<NewFVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getNewFvrGradingDTO();
					for (NewFVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
						if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_B) && fvrGradingDTO3.getSelectFlag() == null) {
							hasError = true;
							eMsg.append("Please Select All SEGMENT A and B in FVR Grading "+i+". </br>");
							break;
						}else if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_A) && fvrGradingDTO3.getCheckFlagA() == null){
							hasError = true;
							eMsg.append("Please Select All SEGMENT A and B in FVR Grading "+i+". </br>");
							break;
						}
					}
				}else if(fvrGradingDetailsDTO.getIsSegmentCNotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentCNotEdit()){

					List<NewFVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getNewFvrGradingDTO();
					Boolean isAnySegmentCSelected = false;
					for (NewFVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
						if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_C) && (fvrGradingDTO3.getCheckFlag() != null && fvrGradingDTO3.getCheckFlag().equals(Boolean.TRUE))) {
							isAnySegmentCSelected = true;
							break;
						}
					}

					if(!isAnySegmentCSelected){
						hasError = true;
						eMsg.append("Please Select Any SEGMENT C in FVR Grading "+i+". </br>");
					}
					//IMSSUPPOR-23522
					else if(fvrGradingTableObj.getSegmentCListenerTableSelectCount() > 1 ){
						hasError = true;
						eMsg.append("Please Select only one Check box value in SEGMENT C. </br>");
						break;
					}
				}
				else{
					hasError = true;
					eMsg.append("Please Select Any SEGMENT in FVR Grading "+i+". </br>");
				}


				if(fvrGradingTableObj != null){
					Map<Long, AbstractField<?>> tableItem = fvrGradingTableObj.getGradingRemarks();
					if(tableItem != null && !tableItem.isEmpty()){
						if(fvrGradingDetailsDTO.getIsFVRReplied() != null && fvrGradingDetailsDTO.getIsFVRReplied() && fvrGradingDetailsDTO.getKey() != null){
							if(tableItem.get(fvrGradingDetailsDTO.getKey()) == null && fvrGradingDetailsDTO.getGradingRemarks() == null){
								hasError = true;
								eMsg.append("Please Enter Grading Suggestion in FVR Grading "+i+". </br>");
							}else{
								TextArea gradeRmrks = (TextArea)tableItem.get(fvrGradingDetailsDTO.getKey());
								if(gradeRmrks != null && (gradeRmrks.getValue() == null || gradeRmrks.getValue().isEmpty())){
									hasError = true;
									eMsg.append("Please Enter Grading Suggestion in FVR Grading "+i+". </br>");
								}
							}
						}
					}
				}

			}
		}

		if (bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS) &&
				null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			if(bean.getPreauthMedicalDecisionDetails().getVerifiedBonus() == null){
				hasError = true;
				eMsg.append("Verify Bonus Applicability"+"</br>");
			}else if(bean.getPreauthMedicalDecisionDetails().getVerifiedBonus().equals(Boolean.FALSE)){
				hasError = true;
				eMsg.append("Verified Bonus has to be selected as Yes to procees further"+"</br>");
			}
		}	
		
		
		if(bean != null && bean.getPreauthMedicalDecisionDetails() != null && bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList() != null && !bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().isEmpty()){
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
				if (assignedInvestigatiorDetails != null && assignedInvestigatiorDetails.getReviewRemarkskey() == null 
						&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey())) {
					hasError = true;
					eMsg.append("Please Select Investigator/ RVO grade </br>");
					break;
				}
			}
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
				if (assignedInvestigatiorDetails != null && assignedInvestigatiorDetails.getRvoFindingsKey() == null
						&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey())) {
					eMsg.append("Please Select RVO findings- Claim decision </br>");
					hasError = true;
					break;
				}
			}
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
				if (assignedInvestigatiorDetails != null && assignedInvestigatiorDetails.getRvoFindingsKey() != null 
						&& ( (assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_NOT_ACCEPTED_CLAIM_APPROVED_KEY) || 
								assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_NOT_ACCEPTED_CLAIM_REJECTED_KEY))
								&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey()))
								&& assignedInvestigatiorDetails.getRvoReasonKey() == null) {
					eMsg.append("Please Select Reasons for not Accepting RVO Findings </br>");
					hasError = true;
					break;
				}
			}
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
				if (assignedInvestigatiorDetails != null && (assignedInvestigatiorDetails.getReportReviewed() == null 
						|| (assignedInvestigatiorDetails.getReportReviewed() != null && assignedInvestigatiorDetails.getReportReviewed().equalsIgnoreCase(SHAConstants.N_FLAG)))
						&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey())) {
					hasError = true;
					eMsg.append("Please Select Investigation Remarks checkbox </br>");
					break;
				}
			}
		}
				
				/*Below code removed as per Ver Doc R20181286
				if(!hasError) {
					if(bean.getPreauthMedicalDecisionDetails().getNegotiationMade() != null &&
							bean.getPreauthMedicalDecisionDetails().getNegotiationMade() && bean.getPreauthMedicalDecisionDetails().getNegotiationAmount() == null) {
						hasError = true;
						eMsg.append("Please Enter Negotiation Amount");
					}
				}*/
		
		if(bean.getPreauthHoldStatusKey() != null && bean.getPreauthHoldStatusKey().equals(ReferenceTable.PREAUTH_HOLD_STATUS_KEY)) {
       	 if(this.claimRequestButtonObj != null && this.claimRequestButtonObj.validateHoldRemarks()) {
       	 hasError = true;
			eMsg.append("Please enter the HOLD Remarks."); 
       	 }
        }
		
		if (hasError) {
			setRequired(true);
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(label);
			dialog.setResizable(false);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
//				this.bean.getPreauthMedicalDecisionDetails()
//						.setMedicalDecisionTableDTO(
//								this.medicalDecisionTableObj.getValues());
				this.bean.getPreauthMedicalDecisionDetails().setMedicalVerificationTableDTO(this.medicalVerificationTableObj.getValues());
				this.bean.getPreauthMedicalDecisionDetails().setTreatmentVerificationDTO(this.treatmentVerificationTableObj.getValues());
				if(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName() != null){
					this.bean.getPreauthMedicalDecisionDetails().setInvestigatorCode(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName().getInvestigatorCode());
					
				}
				// New requirement for saving Copay values to Transaction Table......... 
				SHAUtils.setCopayAmounts(bean, this.claimRequestButtonObj.amountConsideredTable);
				
				if(fvrGradingTableObj != null){
					Map<Long, AbstractField<?>> tableItem = fvrGradingTableObj.getGradingRemarks();
					List<FvrGradingDetailsDTO> fvrBGradingDTO1 = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
					if(tableItem != null && !tableItem.isEmpty()){
						for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrBGradingDTO1) {
							TextArea gradingRmrk = (TextArea) tableItem.get(fvrGradingDetailsDTO.getKey());
							if(gradingRmrk != null && gradingRmrk.getValue() != null){
								fvrGradingDetailsDTO.setGradingRemarks(gradingRmrk.getValue());
							}
						}
					}
				}
				
				
				
				return true;
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return false;
		}
	}

	/*private void setCordinatorDetails() {
		SelectValue referToCordinatorSelValue = bean
				.getPreauthMedicalDecisionDetails()
				.getTypeOfCoordinatorRequest();
		if (null != referToCordinatorSelValue) {
			this.bean.getCoordinatorDetails().setTypeofCoordinatorRequest(
					referToCordinatorSelValue);
			this.bean.getCoordinatorDetails().getTypeofCoordinatorRequest()
					.setId(referToCordinatorSelValue.getId());
			this.bean.getCoordinatorDetails().getTypeofCoordinatorRequest()
					.setValue(referToCordinatorSelValue.getValue());

			if (null != bean.getPreauthMedicalDecisionDetails()
					.getReasonForRefering()) {
				this.bean.getCoordinatorDetails().setReasonForRefering(
						bean.getPreauthMedicalDecisionDetails()
								.getReasonForRefering());
			}
		}
	}

	private void setRequiredAndValidation(Component component) {
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}

	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field != null && propertyId != null) {
				this.binder.unbind(field);
			}

		}
	}*/
	
	public void generateFieldBasedOnSentToCPU(Boolean isChecked) {
		claimRequestButtonObj.generateFieldsBasedOnSentToCPU(isChecked);
	}

	public void showClaimAmountDetails() {
		ViewClaimAmountDetils claimDetails = viewClaimAmountDetails.get();
		claimDetails.showDetails(this.bean, this.referenceData);
		UI.getCurrent().addWindow(claimDetails);
	}

	public void showBalanceSumInsured(String intimationId) {
		if (intimationId != null && !intimationId.equals("")) {
			viewDetails.getViewBalanceSumInsured(intimationId);
		}
	}

	public void generateButton(Integer clickedButton, Object dropDownValues) {
		this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		Long holddefaultStatus = null;
		this.bean.setPreauthHoldStatusKey(holddefaultStatus);
		switch (clickedButton) {
		case 1: 
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
		 this.claimRequestButtonObj.buildSendReplyLayout();
		 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
		 
		 if(this.bean.getIsReplyToFA() != null && this.bean.getIsReplyToFA()){
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS);
		 }
		 
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		String sendReplyFrom = (String) wrkFlowMap.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
		if(null != sendReplyFrom && SHAConstants.SEND_REPLY_BILLING.equalsIgnoreCase(sendReplyFrom)){
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
		}
		else if(null != sendReplyFrom && SHAConstants.SEND_REPLY_FA.equalsIgnoreCase(sendReplyFrom))
		{
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS);
		}
		 
		bean.setIsFvrInitiate(Boolean.FALSE);
		 
		 if(!bean.getIsFvrInitiate()){
			 bean.setIsFvrNotRequiredAndSelected(Boolean.TRUE);
		 }
		 
		 break;
		 
		case 2: 
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
			 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 this.bean.getPreauthMedicalDecisionDetails().setAllocationTo(null);
				 this.bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints("");
			 }

			 if(!ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(this.bean.getStatusKey())){
				 ViewFVRDTO trgptsDto = null;
				 List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
				 for(int i = 1; i<=5;i++){
					 trgptsDto = new ViewFVRDTO();
					 trgptsDto.setRemarks("");
					 trgptsList.add(trgptsDto);
				 }
				 this.bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(trgptsList);
			 }
			 this.claimRequestButtonObj.fVRVisit(dropDownValues);
			 
				if(bean.getIsFvrInitiate()){
					this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
				}else{
					bean.setIsFvrNotRequiredAndSelected(Boolean.FALSE);
				}
			 
			 break;
			 
		case 3: 
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
			 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 this.bean.getPreauthMedicalDecisionDetails().setAllocationTo(null);
				 this.bean.getPreauthMedicalDecisionDetails().setTriggerPointsToFocus("");
				
			 }
			 this.claimRequestButtonObj.buildInitiateInvestigation(dropDownValues);
			
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
			 
			 if(!bean.getIsFvrInitiate()){
				 bean.setIsFvrNotRequiredAndSelected(Boolean.TRUE);
			 }
			 
			 break;
		case 4:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 
			 }
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
			
			this.claimRequestButtonObj
					.buildReferCoordinatorLayout(dropDownValues);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			 
			 if(!bean.getIsFvrInitiate()){
				 bean.setIsFvrNotRequiredAndSelected(Boolean.TRUE);
			 }
			 
			break;
		case 5:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
			if(fileUploadValidatePage()){
				specialistWindow.init(bean);
				specialistWindow.buildEscalateReplyLayout();
				specialistWindow.center();
				specialistWindow.setHeight("400px");
				specialistWindow.setResizable(false);
				specialistWindow.setModal(true);
				specialistWindow.addSubmitHandler(this);
				UI.getCurrent().addWindow(specialistWindow);
				
				specialistWindow.addCloseListener(new CloseListener() {
			            private static final long serialVersionUID = -4381415904461841881L;

			            public void windowClose(CloseEvent e) {
//			                System.out.println("close called");
			            }
			        });
				 
				 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS); 
				}
			
			bean.setIsFvrInitiate(Boolean.FALSE);
			
			if(!bean.getIsFvrInitiate()){
				 bean.setIsFvrNotRequiredAndSelected(Boolean.TRUE);
			 }
			
				break;
			
		case 6:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
//			this.claimRequestButtonObj.buildEscalateLayout(dropDownValues);
//			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
//			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS);
//			break;
			if(fileUploadValidatePage()){
			specialistWindow.init(bean);
			BeanItemContainer<SelectValue> masterValueByReference = masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE));
			specialistWindow.buildEscalateLayout(dropDownValues,fileViewUI,masterValueByReference);
			specialistWindow.center();
			specialistWindow.setHeight("400px");
			specialistWindow.setResizable(false);
			specialistWindow.setModal(true);
			specialistWindow.addSubmitHandler(this);
			UI.getCurrent().addWindow(specialistWindow);
			
			specialistWindow.addCloseListener(new CloseListener() {
		            private static final long serialVersionUID = -4381415904461841881L;

		            public void windowClose(CloseEvent e) {
//		                System.out.println("close called");
		            }
		        });
			 
//			this.claimRequestFileUploadUI.init(bean, wizard);
//			this.claimRequestFileUploadUI.buildSpecialityLayout(dropDownValues);
			
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
//			 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
//				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
//			 }
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS);
			}
			bean.setIsFvrInitiate(Boolean.FALSE);
			if(!bean.getIsFvrInitiate()){
				 bean.setIsFvrNotRequiredAndSelected(Boolean.TRUE);
			 }
			
			break;
			
		case 7:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
//			this.claimRequestButtonObj.buildSpecialistLayout(dropDownValues);
			 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			 }
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS);
			 
			if(fileUploadValidatePage()){
			specialistWindow.init(bean);
			final BeanItemContainer<SelectValue> masterValueByReference;
			if (null != bean.getPreauthDataExtractionDetails().getNatureOfTreatment()){
				masterValueByReference = masterService.getMasterValueByReferenceForNonAllopathic((ReferenceTable.SPECIALIST_TYPE),this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue());
			}else{
				masterValueByReference = masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE));
			}
			specialistWindow.buildSpecialityLayout(dropDownValues,fileViewUI, masterValueByReference);
			specialistWindow.center();
			specialistWindow.setHeight("400px");
			specialistWindow.setResizable(false);
			specialistWindow.setModal(true);
			specialistWindow.setClosable(false);
			specialistWindow.addSubmitHandler(this);
			UI.getCurrent().addWindow(specialistWindow);
			
			specialistWindow.addCloseListener(new CloseListener() {
		            private static final long serialVersionUID = -4381415904461841881L;

		            public void windowClose(CloseEvent e) {
//		                System.out.println("close called");
		            }
		        });
			 
//			this.claimRequestFileUploadUI.init(bean, wizard);
//			this.claimRequestFileUploadUI.buildSpecialityLayout(dropDownValues);
			
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			
			
			}
			
			if(!bean.getIsFvrInitiate()){
				 bean.setIsFvrNotRequiredAndSelected(Boolean.TRUE);
			 }
			
			break;
		case 8:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
			this.claimRequestButtonObj.buildQueryLayout();
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			if(!bean.getIsFvrInitiate()){
				 bean.setIsFvrNotRequiredAndSelected(Boolean.TRUE);
			 }
			
			break;
		case 9:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
			Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
			if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
			bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
				this.claimRequestButtonObj.buildRejectLayout(dropDownValues,true);
				
			}else{
				if(bean.getReconsiderRodRequestList() != null && !bean.getReconsiderRodRequestList().isEmpty() && 
						bean.getReconsiderRodRequestList().get(0).getIsSettledReconsideration() != null 
							&& bean.getReconsiderRodRequestList().get(0).getIsSettledReconsideration()){      //getHospAmountAlreadyPaid() != null && bean.getHospAmountAlreadyPaid().intValue() != 0){
					BeanItemContainer<SelectValue> setlRejCateg = masterService.getSettledRejectionCategory();
					this.claimRequestButtonObj.buildRejectLayout(setlRejCateg,false);
				}
				else{
					this.claimRequestButtonObj.buildRejectLayout(dropDownValues,false);
				}	
			}

			
			if(!bean.getIsFvrInitiate()){
				 bean.setIsFvrNotRequiredAndSelected(Boolean.TRUE);
			 }
			
			break;
		case 10:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
//			Integer min = Math
//					.min(amountConsideredTable.getMinimumValue(),
//							SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
//									.getValue()));
//			negotiation.setVisible(true);
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
			this.claimRequestButtonObj.generateFieldsForSuggestApproval();
			
			if(!bean.getIsFvrInitiate()){
				 bean.setIsFvrNotRequiredAndSelected(Boolean.TRUE);
			 }
			
			break;
		case 11:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
			this.claimRequestButtonObj.builtCancelRODLayout();
			bean.setIsFvrInitiate(Boolean.FALSE);
			if(!bean.getIsFvrInitiate()){
				 bean.setIsFvrNotRequiredAndSelected(Boolean.TRUE);
			 }
			
			break;
		case 12:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);
			this.claimRequestButtonObj.buildRefToBillEntryLayout();
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_BILL_ENTRY);
			
			bean.setIsFvrInitiate(Boolean.FALSE);
			if(!bean.getIsFvrInitiate()){
				 bean.setIsFvrNotRequiredAndSelected(Boolean.TRUE);
			 }
			
			break;
			
		case 13:	
			this.bean.setPreauthHoldStatusKey(ReferenceTable.PREAUTH_HOLD_STATUS_KEY);
			this.claimRequestButtonObj.buildHoldLayout();
			this.bean.setStatusKey(bean.getOldStatusKey());
			break;
			
		default:
			break;
		}
	}

	public void setSumInsuredCaculationsForSublimit(
			Map<String, Object> diagnosisSumInsuredLimit, String diagnosisName) {
		this.sublimitCalculatedValues = diagnosisSumInsuredLimit;
		//this.diagnosisName = diagnosisName;
	}

	public void setInvestigationCheck(Boolean checkInitiateInvestigation) {
		bean.setIsInvestigation(checkInitiateInvestigation);
		/*if (!checkInitiateInvestigation) {
<<<<<<< HEAD
		unbindField(investigationReportReviewedChk);
		unbindField(investigationReviewRemarks);
		unbindField(investigatorName);
		mandatoryFields.remove(investigationReviewRemarks);
	} else*/ if (checkInitiateInvestigation && bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
		investigationReportReviewedChk.setEnabled(true);
		investigationReviewRemarks.setEnabled(true);
		investigatorName.setEnabled(true);
//		mandatoryFields.remove(investigationReviewRemarks);
	}else{
//		unbindField(investigationReportReviewedChk);
//		unbindField(investigationReviewRemarks);
//		unbindField(investigatorName);
		bean.setIsInvestigation(false);
		investigationReportReviewedChk.setEnabled(false);
		investigationReviewRemarks.setEnabled(false);
		investigatorName.setEnabled(false);
	}

	}


	public void setBalanceSI(Double balanceSI, List<Double> productCopay) {
		if (balanceSI == null) {
			balanceSI = new Double("0");
		}
		this.bean.setBalanceSI(balanceSI);
		this.bean.setProductCopay(productCopay);
	}

	private void addMedicalDecisionTableFooterListener() {

		this.medicalDecisionTableObj.dummyField
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 4843316375590220412L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmt = SHAUtils
								.getIntegerFromString((String) event
										.getProperty().getValue());
						approvedAmtField.setValue(String
								.valueOf(totalApprovedAmt));
						if ((bean.getStatusKey()
								.equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
							Integer min = Math.min(
									amountConsideredTable.getMinimumValue(),
									totalApprovedAmt);
							claimRequestButtonObj.setApprovedAmtValue(min);
						}
					}
				});

//		this.amountConsideredTable.dummyField
//				.addValueChangeListener(new ValueChangeListener() {
//					private static final long serialVersionUID = 9193355451830325446L;
//
//					@Override
//					public void valueChange(ValueChangeEvent event) {
//						Integer totalApprovedAmt = SHAUtils
//								.getIntegerFromString(medicalDecisionTableObj.dummyField
//										.getValue());
//						approvedAmtField.setValue(String
//								.valueOf(totalApprovedAmt));
//						if ((bean.getStatusKey()
//								.equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
//							Integer min = Math.min(
//									amountConsideredTable.getMinimumValue(),
//									totalApprovedAmt);
//							claimRequestButtonObj.setApprovedAmtValue(min);
//						}
//					}
//				});

	}

	public void setPreAuthRequestAmt(String strPreAuthAmt) {
		this.preAuthRequestedAmt = strPreAuthAmt;
	}
	
	public void setMedicalDecisionValues(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		this.claimRequestButtonObj.suggestApprovalClick(dto, medicalDecisionTableValues);
	}
	
	@SuppressWarnings("unchecked")
	public void setMedicalDecisionValuesToTable(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		
		dto.setAvailableAmout(((Double) medicalDecisionTableValues
				.get("restrictedAvailAmt")).intValue());
		dto.setUtilizedAmt(((Double) medicalDecisionTableValues
				.get("restrictedUtilAmt")).intValue());
		dto.setSubLimitAmount(((Double) medicalDecisionTableValues
				.get("currentSL")).intValue() > 0 ? (String
				.valueOf(((Double) medicalDecisionTableValues.get("currentSL"))
						.intValue())) : "NA");
		dto.setSubLimitUtilAmount(((Double) medicalDecisionTableValues
				.get("SLUtilAmt")).intValue());
		dto.setSubLimitAvaliableAmt(((Double) medicalDecisionTableValues
				.get("SLAvailAmt")).intValue());
		dto
				.setCoPayPercentageValues((List<String>) medicalDecisionTableValues
						.get("copay"));

		if (dto.getDiagnosisDetailsDTO() != null) {
//			dto.getDiagnosisDetailsDTO()
//					.setDiagnosis(this.diagnosisName);
		}

		// need to implement in new medical listener table
		
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_DELITE)) {
			Integer subLimitAvaliableAmt = 0;
			Boolean isResidual = false;
			if(dto.getDiagnosisDetailsDTO() != null && dto.getDiagnosisDetailsDTO().getSublimitName() != null && (dto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || dto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = dto.getSubLimitAvaliableAmt();
			} else if (dto.getProcedureDTO() != null && dto.getProcedureDTO().getSublimitName() != null && (dto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || dto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = dto.getSubLimitAvaliableAmt();
			} else {
				isResidual = true;
			}
			
			if(!isResidual && bean.getPreauthDataExtractionDetails().getNoOfDays() != null) {
				Integer availAmt = SHAUtils.getIntegerFromString(bean.getPreauthDataExtractionDetails().getNoOfDays()) * subLimitAvaliableAmt;
				int min = Math.min(SHAUtils.getIntegerFromString(dto.getSubLimitAmount()) , availAmt);
				dto.setSubLimitAvaliableAmt(min);
				dto.setSubLimitUtilAmount(0);
			}
		}
		this.medicalDecisionTableObj
				.addBeanToList(dto);
	}

	public void setCategoryValues(
			BeanItemContainer<SelectValue> selectValueContainer) {
		if(claimRequestButtonObj != null) {
			claimRequestButtonObj.setCategoryValues(selectValueContainer);
		}
		
	}

	public void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO) {
		claimRequestButtonObj.setBillEntryFinalStatus(uploadDTO);
		
	}

	@Override
	public void submit(PreauthDTO preauthDTO) {
		specialistWindow.close();
		wizard.finish();
	}

	public void setBillEntryAmountConsideredValue(Double sumValue) {
		claimRequestButtonObj.setBillEntryAmountConsideredValue(sumValue, bean);
	}
	
	public boolean fileUploadValidatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		fireViewEvent(
				ClaimRequestMedicalDecisionPagePresenter.CHECK_INVESTIGATION_INITIATED,
				this.bean.getClaimKey());

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
		
		if(this.medicalVerificationTableObj != null && !this.medicalVerificationTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.medicalVerificationTableObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		if(this.treatmentVerificationTableObj != null && !this.treatmentVerificationTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		if(this.treatmentVerificationTableObj != null && !this.treatmentVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj.getErrorsForRemarks();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		if(this.medicalVerificationTableObj != null && !this.medicalVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.medicalVerificationTableObj.getErrorsforRemarks();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		
		List<FvrGradingDetailsDTO> fvrGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
		if(fvrGradingDTO != null && !fvrGradingDTO.isEmpty()) {
			for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrGradingDTO) {
				List<FVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getFvrGradingDTO();
				for (FVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
					if(fvrGradingDTO3.getStatus() == null) {
						hasError = true;
						eMsg.append("Please Select All the FVR Grading and set the Status. </br>");
						break;
					}
				}
			}
		}

//		if (!this.medicalDecisionTableObj.getTotalAmountConsidered().equals(
//				SHAUtils.getIntegerFromString(this.bean.getAmountConsidered()))) {
//			hasError = true;
//			eMsg += "Total Amount Considered Should be equal to Data Extraction Page Payable Amount. </br>";
//		}

		if (hasError) {
			setRequired(true);
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(label);
			dialog.setResizable(false);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
//				this.bean.getPreauthMedicalDecisionDetails()
//						.setMedicalDecisionTableDTO(
//								this.medicalDecisionTableObj.getValues());
				this.bean.getPreauthMedicalDecisionDetails().setMedicalVerificationTableDTO(this.medicalVerificationTableObj.getValues());
				this.bean.getPreauthMedicalDecisionDetails().setTreatmentVerificationDTO(this.treatmentVerificationTableObj.getValues());
				return true;
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return false;
		}
	}

	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		claimRequestButtonObj.setUploadDTOForBillEntry(uploadDTO);
		// TODO Auto-generated method stub
		
	}
	
	public Boolean validate()
	{
		Boolean hasError = false;
		String msg = null;
		
		if (this.claimRequestButtonObj.isValidButton()) {
		     hasError = true;
			msg = "Please select Approve or Reject or Escalate Claim or Refer to coordinator. </br>";
		}
		
		
		if (hasError) {
			setRequired(true);
			/*Label label = new Label(msg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(label);
			dialog.setResizable(false);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(msg, buttonsNamewithType);

			hasError = true;
			return !hasError;
		} 
		
		return !hasError;
	}	
	
	public void setClearReferenceData(){
    	SHAUtils.setClearReferenceData(referenceData);
    	if(wholeVLayout != null){
    		wholeVLayout.removeAllComponents();
    	}
    }
	
	public void confirmRejectionLayout(Object rejectionCategoryDropdownValues,Boolean isDefinedLimitReject){
		claimRequestButtonObj.buildRejectLayout(rejectionCategoryDropdownValues, isDefinedLimitReject);
    }
	public  void medicalRemarksListener(TextField searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForMAMedRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForMedicalRemarks(searchField, getShortCutListenerForMedicalRemarks(searchField));
	    
	  }
	
	public  void handleShortcutForMedicalRemarks(final TextField textField, final ShortcutListener shortcutListener) {
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
	
	private ShortcutListener getShortCutListenerForMedicalRemarks(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				//txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				//txtArea.setSizeFull();
				
				
				if(("medicalRmrks").equalsIgnoreCase(txtFld.getId()) || ("internalRmrks").equalsIgnoreCase(txtFld.getId())){
					txtArea.setRows(25);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					txtArea.setReadOnly(false);
				}
				
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(("medicalRmrks").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
							PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
							mainDto.getPreauthMedicalDecisionDetails().setMedicalRemarks(txtFld.getValue());
						}else if(("internalRmrks").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
							PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
							mainDto.getPreauthMedicalDecisionDetails().setDoctorNote(txtFld.getValue());
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				
				String strCaption = "";
				
				if(("medicalRmrks").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Medical Remarks";
				}
			    else if(("internalRmrks").equalsIgnoreCase(txtFld.getId())){
			    	strCaption = "Doctor Note(Internal Remarks)";
			    }
			   				    	
				dialog.setCaption(strCaption);
						
				
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);
				
				dialog.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});	
			}
		};
		return listener;
	}

	public void generateFvrLayout(Object dropDownValues){
	
	if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
		 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
		 this.bean.getPreauthMedicalDecisionDetails().setAllocationTo(null);
		 this.bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints("");
	 }

	 if(!ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(this.bean.getStatusKey())){
		 ViewFVRDTO trgptsDto = null;
		 List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
		 for(int i = 1; i<=5;i++){
			 trgptsDto = new ViewFVRDTO();
			 trgptsDto.setRemarks("");
			 trgptsList.add(trgptsDto);
		 }
		 this.bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(trgptsList);
	 }
	 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
	 this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
	 this.claimRequestButtonObj.buildInitiateParallelFieldVisit(dropDownValues);
	}
	
	
	public void alertForHealthGainProduct() {	 
		 
		/* Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.SUM_INSURED_CHANGE_MESSAGE_HEALTH_GAIN + "</b>",
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
			dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(SHAConstants.SUM_INSURED_CHANGE_MESSAGE_HEALTH_GAIN, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
				}
			});
		}

	public boolean validateSpecialistAndEsclate(){
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		if(specialistWindow != null && !specialistWindow.isValidSpecialist(reimbService)){
			List<String> errors = this.specialistWindow.getErrors();
			for (String error : errors) {
				eMsg.append(error);
			}
			hasError = true;
		}
		
		if (hasError) {
			setRequired(true);
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(label);
			dialog.setResizable(false);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);

			hasError = true;
			return !hasError;
		} else {
			try {
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return false;
		}
	}

	public void addAdditionalFvrPointsListener() {
		addAdditionalFvrPointsBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8159939563947706329L;

			@Override
			public void buttonClick(ClickEvent event) {
				final Window popup = new com.vaadin.ui.Window();
				
				addAdditionalFvrPointsPageUI.init(bean,popup);
			
				popup.setWidth("85%");
				popup.setHeight("60%");
				popup.setContent(addAdditionalFvrPointsPageUI);
				popup.setCaption("Add Additional FVR Trigger Points");
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
		});
	}

	public void setBalanceSIforRechargedProcessing(Double balanceSI) {
		// TODO Auto-generated method stub
		claimRequestButtonObj.setBalanceSIforRechargedProcessing(balanceSI);
		
	}
	
	
	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field != null && propertyId != null) {
				this.binder.unbind(field);
			}

		}
	}
	
	public void generateTopUpLayout(List<PreauthDTO> topupData,String screenName){
			Window popup = new com.vaadin.ui.Window();
			if(screenName != null && screenName.equalsIgnoreCase("HC_TOPUP")){
				popup.setCaption("Input Details for Hospital Cash policy claim");	
			}else {
			popup.setCaption("Top-up policy");
			}
			popup.setWidth("35%");
			popup.setHeight("35%");
			viewTopUpGmcPage.init(topupData, bean, SHAConstants.MEDICAL_APPROVAL, popup,screenName);
			popup.setContent(viewTopUpGmcPage);
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
	
	public MessageBox showInfoMessageBox(String message){
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withHtmlMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
		return msgBox;
      }
	
	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
		claimRequestButtonObj.setSubCategContainer(rejSubcategContainer);
		
	}
	public Long getTotalApprovedAmnt(){
		if(claimRequestButtonObj !=null){
			return claimRequestButtonObj.getTotalApprovedAmnt();
		}
		return 0L;
	}

	public void addListenerForVerifiedBonus(){

		verifiedBonus.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -486851813151643902L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				if(event.getProperty() != null && event.getProperty().getValue() != null 
						&& event.getProperty().getValue().toString().equalsIgnoreCase("true")) {
					bean.getPreauthMedicalDecisionDetails().setVerifiedBonus(Boolean.TRUE);
				}else{
					bean.getPreauthMedicalDecisionDetails().setVerifiedBonus(Boolean.FALSE);
				}
			}
		});

	}
	
	public void setBalanceSumInsuredAlert(){
		
		NewIntimationDto intimationDTO = bean.getNewIntimationDTO();

		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getLopFlag());
		Long appAmount = 0L;
		if(claimRequestButtonObj !=null){
			appAmount = claimRequestButtonObj.getTotalApprovedAmnt();
		}
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			bean = dbCalculationService.getBalanceSIForGMCAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(), appAmount.doubleValue(), bean);
		}else{
			if(ReferenceTable.getFHORevisedKeys().containsKey(intimationDTO.getPolicy().getProduct().getKey())
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
					(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
					&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
							SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())))
					|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
				if(bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null && 
						bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.INJURY_MASTER_ID) && bean.getPreauthDataExtractionDetails().getCauseOfInjury() != null && ReferenceTable.CAUSE_OF_INJURY_ACCIDENT_KEY.equals(bean.getPreauthDataExtractionDetails().getCauseOfInjury().getId())){
//					bean = dbCalculationService.getRTABalanceSIAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(), 0l).get(SHAConstants.TOTAL_BALANCE_SI);
				}
				else{
					bean = dbCalculationService.getBalanceSIForReimbursementAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(),bean.getKey(),appAmount.doubleValue(), bean);	
				}

			}
			else if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(intimationDTO.getPolicy().getProduct().getKey())
					|| ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND.equals(intimationDTO.getPolicy().getProduct().getKey())){
				String subCover = "";
				if(null != bean.getPreauthDataExtractionDetails() && null != bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() && null != bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover()){			
					subCover = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue();
				}
				bean = dbCalculationService.getBalanceSIForReimbursementStarCancerGoldAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(),bean.getKey(),subCover, appAmount.doubleValue(), bean);	
			}
			else{
				bean = dbCalculationService.getBalanceSIForReimbursementAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(),bean.getKey(),appAmount.doubleValue(), bean);	
			}
		}
		if (null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			/*SHAUtils.showMessageBoxWithCaption(bean.getSiAlertDesc(),"Verified Bonus");*/
			verifiedBonus.setVisible(true);
			showBonusView("Information - "+bean.getSiAlertDesc());
		}
		
	}
	
	private void showBonusView(String tableCaption){
		 
		 bonusAlertUI.setbonusAlertDTO(bean.getBonusAlertDTO());
		 bonusAlertUI.init(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),bean.getNewIntimationDTO());

		 VerticalLayout bonusLayout = new VerticalLayout(bonusAlertUI);
		 Window popup = new com.vaadin.ui.Window();
		 popup.setCaption(tableCaption);
		 popup.setWidth("80%");
		 popup.setHeight("80%");
		 popup.setContent(bonusLayout);
		 popup.setClosable(false);
		 popup.center();
		 popup.setResizable(true);
		 bonusAlertUI.setPopupWindow(popup);
		 popup.addCloseListener(new Window.CloseListener() {
			 private static final long serialVersionUID = 1L;
			 @Override
			 public void windowClose(CloseEvent e) {
				 System.out.println("Close listener called");
			 }
		 });
		 popup.setModal(true);
		 UI.getCurrent().addWindow(popup);
	 }
	
	// Function to merge two arrays of same type 
    public static <T> Object[] concatenate(T[] a, T[] b) 
    { 
        Object[] n=new Object[a.length + b.length]; 
          
        System.arraycopy(a, 0, n, 0, a.length); 
          
        System.arraycopy(b, 0, n, a.length, b.length); 
          
        return n; 
    } 
}
