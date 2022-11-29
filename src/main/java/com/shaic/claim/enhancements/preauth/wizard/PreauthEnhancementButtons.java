package com.shaic.claim.enhancements.preauth.wizard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.ContributeRRCPopupUI;
import com.shaic.claim.SumInsuredBonusAlertUI;
import com.shaic.claim.common.RevisedMedicalDecisionTable;
import com.shaic.claim.pcc.EscalatePCCRemarkAlertUI;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.preauth.HRMHospitalDetailsTableDTO;
import com.shaic.claim.preauth.HRMListenerTable;
import com.shaic.claim.preauth.HRMTable;
import com.shaic.claim.preauth.HRMTableDTO;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.ViewEarlierHRMListenerTable;
import com.shaic.claim.preauth.ViewEarlierHRMTable;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsService;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsTable;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsTableDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.AddAdditionalFVRPointsPageUI;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.MasterRemarks;
import com.shaic.domain.preauth.NegotiationDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
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
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class PreauthEnhancementButtons extends ViewComponent{
	private static final long serialVersionUID = 3468181378653730908L;

	@Inject
	private PreauthDTO bean;
	
	/*@Inject 
	private SearchPreauthTableDTO searchDTO;*/
	
	@Inject
	private PreAuthPreviousQueryDetailsTable preAuthPreviousQueryDetailsTable;
	
	@Inject
	private Intimation intimation;
	
	private GWizard wizard;
	
	@EJB
	private MasterService masterService;
	
	private File file;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private IntimationService intimationService;
	
	private  Map<String, Object> referenceDataMap;
	
	@Inject
	private HRMListenerTable hrmListenerTable;
	
	@Inject
	private ViewEarlierHRMListenerTable viewEarilerHrmListenerTable;
	
	@Inject
	private HRMTable hrmtable;
	
	@Inject
	private ViewEarlierHRMTable viewEarlierHrmTable;
	
	private HRMTableDTO hrmDto;
	
	HashMap<String, String> enteredValues = new HashMap<String, String>();


	public Button referToHrm;
	private Button btnSubmit;
	private Button btnViewEarlierHrmDetails;
	
	public Button referTo64Complaince;
	
	//Dinesh
	public Button escalatePCC;
	
	
	@Inject
	private PreAuthPreviousQueryDetailsService preAuthPreviousQueryDetailsService;
	
	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	private RevisedMedicalDecisionTable medicalDecisionTableObj;
	
	public Button approveBtn;
	private Button queryBtn;
	private Button rejectBtn;
	private Button escalateClaimBtn;
	private Button referCoordinatorBtn;
	private Button withdrawPreauthBtn;
	private Button withdrawRejectBtn;
	public Button downSizePreauthBtn;
//	private Button btnSubmit;
	private Button btnCancel;
	private Button btnBack;
	public VerticalLayout wholeVlayout;
	public VerticalLayout wholeVertlayout;
	private TextField initialApprovedAmtTxt;
	private TextField selectedCopyTxt;
	private TextField initialTotalApprovedAmtTxt;
	private TextField uniquePremiumAmt;
	private TextField amountToHospAftPremium;
	private TextArea approvalRemarksTxta;
	private TextArea queryRemarksTxta;
	private ComboBox cmbWithdraw;
	private TextArea txtWithdraw;
	private TextArea hospRejectionRemarksTxta;
	private TextArea rejRemarksForInsuredTxta;
	private TextArea withDrawRemarksForInsuredTxta;
	private TextArea rejremarksForInsuredTxta;
	private ComboBox reasonForDenialCmb;
	private TextArea denialRemarksTxta;
	private ComboBox escalateToCmb;
	private ComboBox cmbSpecialistType;
	private Upload uploadFile;
	private TextArea escalationRemarksTxta;
	private ComboBox typeOfCoordinatorRequestCmb;
	private TextArea reasonForReferringTxta;
	//public HorizontalLayout dynamicFrmLayout;
	public FormLayout dynamicFrmLayout;
	private HorizontalLayout remarksLayout;
	private OptionGroup sentToCPU;
	private TextArea remarksForCPU;
	private TextField totalApprvedAmtField;
	private TextField downsizedAmtField;
	private ComboBox cmbDownsizeReason;
	private TextArea downsizeRemarks;
	private TextArea downsizeInsuredRemarks;
	private TextArea vb64RemarksTxta;
	private ArrayList<Component> mandatoryFields; 
	private TextArea icdExclusionReasonTxta;
	//private OptionGroup benefitSheetRatio;
	
	private OptionGroup negotiation;
	
	private OptionGroup verifiedBonus;

	private FormLayout bonusFrmLayout;
	
	VerticalLayout bonusVLayout = new VerticalLayout();
	
	private List<String> errorMessages;

	private ComboBox rejectionCategoryCmb;
	
	private ComboBox rejSubCategCmb;
	
	private TextField rejCondtNoTxt;
	
	private TextField txtQueryCount;
	private ComboBox cmbQueryType;
	
	private Button updateButton;

	private HorizontalLayout updatePanCardLayout;
	private Button referToCPU;
    private TextArea referCPURemarksTxta;
    private ComboBox referCPUCategoryCmb;
    private TextField referCPUamountSuggested;
	
	private TextField benefitAmtTxt;
	
	private Button viewOtherBenefitAmtLink;
	
	private TextField definedLimtAmtTxt;
	
	@Inject
	private AddAdditionalFVRPointsPageUI addAdditionalFvrPointsPageUI;
	private ComboBoxMultiselect cmbUserRoleMulti;
	private ComboBoxMultiselect cmbDoctorNameMulti;
	private TextArea remarksFromDeptHead;
	
	FormLayout userLayout = new FormLayout();
	@EJB
	private DBCalculationService dbCalculationService;
	
	private Button holdBtn;
	
	private TextArea holdRemarksTxta;
	
	private OptionGroup negotiationOpinionTaken;	
	private TextArea txtNegotiationRemarks;	
	private TextField amtToNegotiate;
	private FormLayout negotiationLayout;
	
	// R1207
	private Map<String, String> roleValidationContainer = new HashMap<String, String>();
	private Map<String, String> userValidationContainer = new HashMap<String, String>();
	
	public Map<String, String> getRoleValidationContainer() {
		return roleValidationContainer;
	}

	public void setRoleValidationContainer(
			Map<String, String> roleValidationContainer) {
		this.roleValidationContainer = roleValidationContainer;
	}

	public Map<String, String> getUserValidationContainer() {
		return userValidationContainer;
	}

	public void setUserValidationContainer(
			Map<String, String> userValidationContainer) {
		this.userValidationContainer = userValidationContainer;
	}
	
	//R1256
	private CheckBox chkSubDoc;
	private CheckBox chkFVR;
	private CheckBox chkIR;
	private CheckBox chkOthers;
	private TextArea txtaRemarks;
	
//	CR20181286
	private TextField txtClaimedAmt;
	private TextField txtNegotiationAmt;
	private TextField txtSavedAmt;
	private TextArea txtNegotiationWith;
	private Boolean isNegotiationMade = false;
	private TextField txtTotNegotiationSavedAmt;
	private TextField txthigestCLTransAmt;
	
	
	@Inject
	private SumInsuredBonusAlertUI bonusAlertUI;
	
	@Inject
	private Instance<EscalatePCCRemarkAlertUI> pCCRemarkAlertinstance;
	
	private EscalatePCCRemarkAlertUI pCCRemarkAlertobj;

	//CR2019017
	public Button getApproveBtn(){
		return approveBtn;
	}
	
	//CR2019179
	public Button getQueryBtn(){
		return queryBtn;
	}
	private Button topUpBtn;
	
	private TextField policyInstPremiumAmt;

//	private ComboBox rejSubCategCmb;
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() 
	{
		
	}
	
	public void initView(PreauthDTO bean,GWizard wizard,RevisedMedicalDecisionTable medicalDecisionTableObj) 
	{
		errorMessages = new ArrayList<String>();
		mandatoryFields = new ArrayList<Component>();
		this.bean = bean;
		this.wizard = wizard;
		this.medicalDecisionTableObj = medicalDecisionTableObj;
		this.bean.getPreauthMedicalDecisionDetails().setIsNegotiationDone(Boolean.TRUE);
		wholeVlayout = new VerticalLayout();
		wholeVlayout.setHeight("-1px");
		wholeVlayout.setWidth("-1px");
		wholeVlayout.setSpacing(true);

		wholeVertlayout  = new VerticalLayout();
		wholeVertlayout.setSpacing(true);
		
		HorizontalLayout buttonsHLayout = new HorizontalLayout();
		buttonsHLayout.setSizeFull();
		buttonsHLayout.setWidth("-1px");
		buttonsHLayout.setMargin(false);
		addListener();
		if(null != bean.getIsPreauthAutoAllocationQ() && !bean.getIsPreauthAutoAllocationQ()){
			buttonsHLayout.addComponents(approveBtn,queryBtn,rejectBtn,escalateClaimBtn,referCoordinatorBtn, withdrawPreauthBtn, downSizePreauthBtn, withdrawRejectBtn,referToHrm,referTo64Complaince, topUpBtn);
		}
		else
		{
			buttonsHLayout.addComponents(approveBtn,queryBtn,rejectBtn,escalateClaimBtn,referCoordinatorBtn, withdrawPreauthBtn, downSizePreauthBtn, withdrawRejectBtn,referToHrm,referTo64Complaince,holdBtn, topUpBtn);
		}
		
		//TOP UP POLICY
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			topUpBtn.setVisible(true);
		}else{
			topUpBtn.setVisible(false);
		}
		
//		buttonsHLayout.addComponents(approveBtn,queryBtn,rejectBtn,escalateClaimBtn,referCoordinatorBtn, withdrawPreauthBtn, downSizePreauthBtn);
		buttonsHLayout.setSpacing(true);
		//As per Urgent Request from BA
		//buttonsHLayout.addComponent(referToCPU);
		referToCPU.setVisible(false);
		
		if(bean.getIsAutoAllocationCorpUser() && (bean.getIsAboveLimitCorpAdvise() || bean.getIsAboveLimitCorpProcess())){
			referToCPU.setVisible(true);
		}else{
			referToCPU.setVisible(false);
		}
		
		HorizontalLayout escalatePCCHLayout = new HorizontalLayout(escalatePCC);
		escalatePCCHLayout.setSizeFull();
		escalatePCCHLayout.setWidth("-1px");
		escalatePCCHLayout.setMargin(false);
		
		buttonsHLayout.setSpacing(true);
		wholeVertlayout.addComponents(buttonsHLayout,escalatePCCHLayout,wholeVlayout);
		wholeVertlayout.setComponentAlignment(escalatePCCHLayout, Alignment.TOP_LEFT);
		dynamicFrmLayout = new FormLayout();
	//	dynamicFrmLayout = new HorizontalLayout();
		dynamicFrmLayout.setHeight("100%");
		dynamicFrmLayout.setWidth("100%");
		dynamicFrmLayout.setMargin(true);
		dynamicFrmLayout.setSpacing(true);
		
		remarksLayout = new HorizontalLayout();
		remarksLayout.setWidth("100%");
		remarksLayout.setMargin(true);
		remarksLayout.setSpacing(true);
		
		
		wholeVlayout.addComponents(dynamicFrmLayout,remarksLayout);
		
		enableOrDisable(false);
		
		if (bean.getIsDishonoured()
				|| (bean.getPreauthDataExtractionDetails().getIllness() != null && bean
						.getPreauthDataExtractionDetails().getIllness().getId()
						.equals(ReferenceTable.RELATED_TO_EARLIER_CLAIMS))
				|| (null != bean.getPreauthDataExtractionDetails()
						.getHospitalisationDueTo()
						&& !bean.getMaternityFlag() && bean
						.getPreauthDataExtractionDetails()
						.getHospitalisationDueTo().getId()
						.equals(ReferenceTable.MATERNITY_MASTER_ID))) {
			approveBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			withdrawPreauthBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
			downSizePreauthBtn.setEnabled(false);
		} else {
//			approveBtn.setEnabled(true);
			if(this.bean.isClsProsAllowed()){
				approveBtn.setEnabled(true);
			}
			else{
				approveBtn.setEnabled(false);
			}
			queryBtn.setEnabled(true);
			withdrawPreauthBtn.setEnabled(true);
			downSizePreauthBtn.setEnabled(true);
			escalateClaimBtn.setEnabled(true);
			referCoordinatorBtn.setEnabled(true);
		}
		
		referTo64Complaince.setEnabled(false);
		if(this.bean.getIsReleased()){
//			approveBtn.setEnabled(true);
			if(this.bean.isClsProsAllowed()){
//				approveBtn.setEnabled(true);
				if(this.bean.isClsProsAllowed()){
					approveBtn.setEnabled(true);
				}
				else{
					approveBtn.setEnabled(false);
				}
			}
			else{
				approveBtn.setEnabled(false);
			}
		}
		if(this.bean.getIsPending()){
			approveBtn.setEnabled(false);
			referTo64Complaince.setEnabled(true);
		}
		if(this.bean.getIsDishonoured()){
			approveBtn.setEnabled(false);
		}
		
		if(this.bean.getPreauthMedicalProcessingDetails()!=null){
			String vbApprovalStatus = this.bean.getPreauthMedicalProcessingDetails().getVbApprovalStatus();
			if(vbApprovalStatus!=null && "Approve".equalsIgnoreCase(vbApprovalStatus)){
//				approveBtn.setEnabled(true);
				if(this.bean.isClsProsAllowed()){
					approveBtn.setEnabled(true);
				}
				else{
					approveBtn.setEnabled(false);
				}
			}
		}
		
		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) &&
				this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null && 
				this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT_HOSPITALISATION_KEY)){
			approveBtn.setEnabled(false);
			rejectBtn.setEnabled(true);
		}
		
		if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			//IMSSUPPOR-27615
			if(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null){
			if(ReferenceTable.LUMPSUM_SECTION_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue()) &&
					ReferenceTable.LUMPSUM_COVER_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue())){
				
				approveBtn.setEnabled(false);
				withdrawPreauthBtn.setEnabled(false);
				rejectBtn.setEnabled(false);
				downSizePreauthBtn.setEnabled(false);
				
			}
			}
		}
		//CR  GLX2020076   
		Long prodKey = bean.getNewIntimationDTO().getPolicy().getProduct().getKey();
		String insuredNo=null;
		String commonIP=null;
		 if(bean.getPreauthDataExtractionDetails()!=null && bean.getPreauthDataExtractionDetails().getDiagnosisTableList()!=null
				 && !bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()){
			 for (DiagnosisDetailsTableDTO detailsTableDTO: bean.getPreauthDataExtractionDetails().getDiagnosisTableList()){
				 if(detailsTableDTO.getIcdCode() !=null && detailsTableDTO.getIcdCode().getCommonValue() !=null){
					 Map<String,String> PerExcludedICDCode = dbCalculationService.getPermanentExclusionsIcdMapping(detailsTableDTO.getIcdCode().getCommonValue(),prodKey,null,null,insuredNo,commonIP);
					 String icdCodeFlag= PerExcludedICDCode.get("flag");
						String icdCodeRemarks= PerExcludedICDCode.get("remarks");
						if(icdCodeFlag!=null && icdCodeFlag.equalsIgnoreCase("Y")){
							approveBtn.setEnabled(false);
							}
				 		}
			 		}
				 }
		
		if((ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) ||
				(ReferenceTable.STAR_CANCER_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			
			disableButtonForLumpSum(bean);
		}
		if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
			
			if(bean.getPreauthDataExtractionDetails().getIsStarGrpCorApproveBtn()){
				approveBtn.setEnabled(false);
			}
		}
		
		if(null != bean.getPreauthDataExtractionDetails() && null != bean.getPreauthDataExtractionDetails().getPreAuthType()
				&& ("i").equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag())){
			escalatePCC.setEnabled(false);
		}
		//As per Urgent Request from BA
		/*if(bean.getIsAutoAllocationCorpUser() && bean.getIsAboveLimitCorpAdvise()){
			approveBtn.setEnabled(false);
		}*/
		
		setCompositionRoot(wholeVertlayout);
	
	}

	private void disableButtonForLumpSum(PreauthDTO bean) {
		//IMSSUPPOR-27615
		if(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover() != null && (ReferenceTable.LUMPSUM_SECTION_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue()) &&
				ReferenceTable.LUMPSUM_COVER_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue()))){
			
			approveBtn.setEnabled(false);
			withdrawPreauthBtn.setEnabled(false);
			rejectBtn.setEnabled(false);
			downSizePreauthBtn.setEnabled(false);
		}
		else if(null != bean.getNewIntimationDTO().getPolicy().getInsured())
			{
			for (Insured insured : bean.getNewIntimationDTO().getPolicy().getInsured()) {
				
				if(null != insured && (SHAConstants.N_FLAG.equalsIgnoreCase(insured.getSumInsured3Flag())) && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null && 
						(ReferenceTable.HOSP_NON_SURGICAL_SECTION_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue()))){
					approveBtn.setEnabled(false);
					withdrawPreauthBtn.setEnabled(false);
					downSizePreauthBtn.setEnabled(false);
					escalateClaimBtn.setEnabled(false);
				}
			}
			
			}
		else
		{
//			approveBtn.setEnabled(true);
			if(this.bean.isClsProsAllowed()){
				approveBtn.setEnabled(true);
			}
			else{
				approveBtn.setEnabled(false);
			}
			withdrawPreauthBtn.setEnabled(true);
			rejectBtn.setEnabled(true);
			downSizePreauthBtn.setEnabled(true);
		}
	}
	
	public void setApprovedAmtValue(Integer amount) {
		if(initialApprovedAmtTxt != null) {
			initialApprovedAmtTxt.setValue(amount.toString());
		}
		fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_LOAD_NEGOTIATED_SAVED_AMOUNT,null);
	}
	
	
	
	public String getApprovedAmount() {
		if(initialApprovedAmtTxt != null) {
			return initialApprovedAmtTxt.getValue();
		}
		return "0";
	}
	
	public void enableOrDisable(Boolean isDisableApproval) {
		
			if(approveBtn != null) {
				if(!bean.getIsSDEnabled()){
					if(! bean.getIsDishonoured()){
						if(this.bean.getPreauthMedicalProcessingDetails()!=null){
							String vbApprovalStatus = this.bean.getPreauthMedicalProcessingDetails().getVbApprovalStatus();
							if(vbApprovalStatus!=null){
								if("Approve".equalsIgnoreCase(vbApprovalStatus)){
									approveBtn.setEnabled(!isDisableApproval);

									if(!isDisableApproval){
										if(this.bean.isClsProsAllowed()){
											approveBtn.setEnabled(true);
										}
										else{
											approveBtn.setEnabled(false);
										}	
									}

								}else{
									approveBtn.setEnabled(false);
								}
							}else if(bean.getIsPending()){
								approveBtn.setEnabled(false);
							}else{
								approveBtn.setEnabled(!isDisableApproval);

								if(!isDisableApproval){
									if(this.bean.isClsProsAllowed()){
										approveBtn.setEnabled(true);
									}
									else{
										approveBtn.setEnabled(false);
									}
								}
							}
						}else{
							approveBtn.setEnabled(!isDisableApproval);
							if(!isDisableApproval){
								if(this.bean.isClsProsAllowed()){
									approveBtn.setEnabled(true);
								}
								else{
									approveBtn.setEnabled(false);
								}
							}						

						}
					}
				}
				//As per Urgent Request from BA
				/*if(bean.getIsAutoAllocationCorpUser() && bean.getIsAboveLimitCorpAdvise()){
					approveBtn.setEnabled(false);
				}*/	
				
			}
			if(downSizePreauthBtn != null) {
				if(! bean.getIsDishonoured()){
				downSizePreauthBtn.setEnabled(isDisableApproval);
				}
			}
			
			if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) &&
					this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null && 
					this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT_HOSPITALISATION_KEY)){
				approveBtn.setEnabled(false);
				rejectBtn.setEnabled(true);
			}
			
			
			if((ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) ||
					(ReferenceTable.STAR_CANCER_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
				
				disableButtonForLumpSum(bean);
			}
//			//added for installment payment status check at policy level
//			if(this.bean.getPolicyInstalmentFlag() != null && this.bean.getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
//				approveBtn.setEnabled(false);
//				downSizePreauthBtn.setEnabled(false);
//			}
			
	}
	
	
	private void addListener() {
		approveBtn = new Button("Approve");
//		approveBtn.setHeight("-1px");
		//approveBtn.setWidth("100%");
		//Vaadin8-setImmediate() approveBtn.setImmediate(true);
		approveBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_APPROVE_BSI_ALERT,null);
				
				//changes done for GMC SI restrict CR
				if (ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					if(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null && 
							ReferenceTable.getGMCRelationShipKey().containsKey(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
						showAlertPopForGMCParentSIRestrict();
					}
				}
//				setBalanceSumInsuredAlert(bean);
				//R1261
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					Boolean issubmitApproval = false;

					List<DiagnosisDetailsTableDTO> detailsTableDTOs = bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
					if(detailsTableDTOs !=null && !detailsTableDTOs.isEmpty()){
						for(DiagnosisDetailsTableDTO detailsTableDTO:detailsTableDTOs){
							if(detailsTableDTO != null && detailsTableDTO.getIcdCode() != null 
									&& !(detailsTableDTO.getIcdCode().getId().equals(SHAConstants.COVID_19_ICD_NOT_IDENT_KEY)
											|| detailsTableDTO.getIcdCode().getId().equals(SHAConstants.COVID_19_ICD_IDENT_KEY))){
								SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_ICD_MSG, "INFORMATION");
								approveBtn.setEnabled(false);
								issubmitApproval=true;
								break;
							}
						}
					}
					if(!issubmitApproval){
						SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
						enhancementApproveAction();
					}
				}else if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag().equals("I")){
					enhancementApproveAction();
				} 
				else{
					if(bean.getScoringClicked()){
						enhancementApproveAction();
					}else{
						showErrorMessage("Please Complete the Hospital Scoring before Approving");
					}
				}	
				showRestrictedSIAlert();
			}
		});
		
		queryBtn = new Button("Query");
//		queryBtn.setHeight("-1px");
		//queryBtn.setWidth("100%");
		//Vaadin8-setImmediate() queryBtn.setImmediate(true);
		queryBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 4614951723748846970L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				//changes done for GMC SI restrict CR
				if (ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					if(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null && 
							ReferenceTable.getGMCRelationShipKey().containsKey(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
						showAlertPopForGMCParentSIRestrict();
					}
				}
				
				//R1261
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag().equals("I")){
					enhancementQueryAction();
				}else if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					enhancementQueryAction();
				}
				else{
					//R1192 - Hospital Scoring Implementation
					if(bean.getScoringClicked()){
						enhancementQueryAction();
					}else{
						showErrorMessage("Please Complete the Hospital Scoring before Raising Query");
					}
				}
			}
		});
		
		rejectBtn = new Button("Reject");
//		rejectBtn.setHeight("-1px");
		//rejectBtn.setWidth("100%");
		//Vaadin8-setImmediate() rejectBtn.setImmediate(true);
		rejectBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -4241727763379504532L;

			@Override
			public void buttonClick(ClickEvent event) {
				//R1261
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag().equals("I")){
					enhancementRejectAction();
				}
				else if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					enhancementRejectAction();
				}
				else{
					//R1192 - Hospital Scoring Implementation
					if(bean.getScoringClicked()){
						enhancementRejectAction();
					}else{
						showErrorMessage("Please Complete the Hospital Scoring before Rejecting");
					}
				}
			}
		});
		
		
		escalateClaimBtn = new Button("Escalate Claim");
//		escalateClaimBtn.setHeight("-1px");
		//escalateClaimBtn.setWidth("100%");
		//Vaadin8-setImmediate() escalateClaimBtn.setImmediate(true);
		escalateClaimBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 6641041437396264183L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("Escalate Claim");
				bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ESCALATE_EVENT, null);
			}
		});
		
		referCoordinatorBtn = new Button("Refer to Co-ordinator");		
//		referCoordinatorBtn.setHeight("-1px");
		//Vaadin8-setImmediate() referCoordinatorBtn.setImmediate(true);
		referCoordinatorBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("Refer to Co-ordinator");
				bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_REFERCOORDINATOR_EVENT,null);
			}
		});
		
        referCoordinatorBtn.setVisible(false);
		
//		withdrawPreauthBtn = new Button("Withdraw Pre-auth");
        withdrawPreauthBtn = new Button();
        withdrawPreauthBtn.setCaption("Withdrawal of cashless approval");
//		withdrawPreauthBtn.setHeight("-1px");
		//Vaadin8-setImmediate() withdrawPreauthBtn.setImmediate(true);
		withdrawPreauthBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7255298985095729669L;
			@Override
			public void buttonClick(ClickEvent event) {
				//R1261
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag().equals("I")){
					enhancementWithdrawPreAuthAction();
				}else{
					//R1192 - Hospital Scoring Implementation
					if(bean.getScoringClicked()){
						
						enhancementWithdrawPreAuthAction();
					}else{
						showErrorMessage("Please Complete the Hospital Scoring before Withdrawing Pre-Auth");
					}
				}
			}
		});
		
//		withdrawRejectBtn = new Button("Withdraw & Reject");
		withdrawRejectBtn = new Button("Rejection and withdrawal");
//		withdrawRejectBtn.setHeight("-1px");
		//Vaadin8-setImmediate() withdrawRejectBtn.setImmediate(true);
		withdrawRejectBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7255298985095729669L;
			@Override
			public void buttonClick(ClickEvent event) {
				//R1261
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag().equals("I")){
					enhancementWithdrawRejectAction();
				}else{
					//R1192 - Hospital Scoring Implementation
					if(bean.getScoringClicked()){
						enhancementWithdrawRejectAction();
					}else{
						showErrorMessage("Please Complete the Hospital Scoring before Withdrawing/Rejecting");
					}
				}
			}
		});
		
		
		
		
		downSizePreauthBtn = new Button("Revision of Authorization"/*"Downsize Pre-auth"*/);		
//		downSizePreauthBtn.setHeight("-1px");
		//Vaadin8-setImmediate() downSizePreauthBtn.setImmediate(true);
		downSizePreauthBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7255298985095729669L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				//R1261
				if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag().equals("I")){
					enhancementDownsizePreAuthAction();
				}else{
					//R1192 - Hospital Scoring Implementation
					if(bean.getScoringClicked()){
						enhancementDownsizePreAuthAction();
					}else{
						showErrorMessage("Please Complete the Hospital Scoring before Downsizing Pre-auth");

					}
				}
			}
		});

		btnViewEarlierHrmDetails = new Button("View Earlier HRM");
//		btnViewEarlierHrmDetails.setHeight("-1px");
		//Vaadin8-setImmediate() btnViewEarlierHrmDetails.setImmediate(true);
		
		btnBack = new Button("Back");
		btnBack.setHeight("-1px");
		//Vaadin8-setImmediate() btnBack.setImmediate(true);
		
		btnCancel = new Button("Cancel");
		btnCancel.setHeight("-1px");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		
		btnCancel.addClickListener(new Button.ClickListener() {

    		 @Override
    		 public void buttonClick(ClickEvent event) {
	          
    			 
    			
    		 }
    	 });
		
		btnSubmit = new Button("Submit");
		btnSubmit.setHeight("-1px");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
		
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {				
				List<HRMTableDTO> tableList = hrmListenerTable.getValues();
				String err=preauthService.validate(bean.getNewIntimationDTO().getIntimationId(),tableList);
				if(err == null)
				{
					preauthService.submitHrmRequestDetails(tableList);
					buildSuccessLayout();
				}
				else
				{
					showErrorMessage(err);
				}
				
			} 
		});
		
		referTo64Complaince = new Button("Refer To 64 VB Compliance");
//		referTo64Complaince.setHeight("-1px");
		//Vaadin8-setImmediate() referTo64Complaince.setImmediate(true);
		referTo64Complaince.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 6641041437396264183L;

			@Override
			public void buttonClick(ClickEvent event) {		
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("Refer To 64 VB Compliance");
				bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				fireViewEvent(PreauthEnhancemetWizardPresenter.REFER_VB_64_COMPLAINCE,null);
				
			}
			
		});
		
		
		//Dinesh
				escalatePCC = new Button("Escalate PCC (Post Cashless Cell)");

				escalatePCC.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						
						if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
								bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
							SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
						}
						
						PCCRequest pccRequest = preauthService.findByintimationNo(bean.getNewIntimationDTO().getIntimationId());
						if(pccRequest == null){
							Window popup = new com.vaadin.ui.Window();
							popup.setWidth("35%");
							popup.setHeight("60%");
							popup.setCaption("Escalate PCC (Post Cashless Cell)");
							pCCRemarkAlertobj = pCCRemarkAlertinstance.get();
							pCCRemarkAlertobj.initView(bean,"Process Enhancements");
							pCCRemarkAlertobj.setPopupWindow(popup);
							popup.setContent(pCCRemarkAlertobj);
							popup.setClosable(true);
							popup.center();
							popup.setResizable(false);
							popup.addCloseListener(new Window.CloseListener() {
								@Override
								public void windowClose(CloseEvent e) {
									System.out.println("Close listener called");
								}
							});
							popup.setModal(true);
							UI.getCurrent().addWindow(popup);
						}else{
							SHAUtils.showMessageBox("PCC already initiated", "Information - Escalate PCC");
						}
						
					}

				});
		
		referToHrm = new Button("Refer To HRM");
//		referToHrm.setHeight("-1px");
		//Vaadin8-setImmediate() referToHrm.setImmediate(true);
		referToHrm.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 6641041437396264183L;

			@Override
			public void buttonClick(ClickEvent event) {		
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("Refer To HRM");
				bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				List<HRMHospitalDetailsTableDTO> hospitalDtoList = new ArrayList<HRMHospitalDetailsTableDTO>();	
				List<HRMTableDTO> hrmtableDtoList = new ArrayList<HRMTableDTO>();
				
					HRMHospitalDetailsTableDTO hospitalDto1 = new HRMHospitalDetailsTableDTO();
					hospitalDto1.setHardCodedString("Intimation Number");
					hospitalDto1.setHardCodedStringValue(bean.getNewIntimationDTO().getIntimationId());
					hospitalDto1.setHardCodedString1("Hospital Name");
					hospitalDto1.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getName());
					hospitalDtoList.add(hospitalDto1);
					HRMHospitalDetailsTableDTO hospitalDto2 = new HRMHospitalDetailsTableDTO();
					hospitalDto2.setHardCodedString("Hrm Id");
					hospitalDto2.setHardCodedStringValue(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmCode());
					hospitalDto2.setHardCodedString1("Phone");
					hospitalDto2.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmContactNo());
					hospitalDtoList.add(hospitalDto2);
					HRMHospitalDetailsTableDTO hospitalDto3 = new HRMHospitalDetailsTableDTO();
					hospitalDto3.setHardCodedString("Name");
					hospitalDto3.setHardCodedStringValue(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmUserName());
					hospitalDto3.setHardCodedString1("Email Id");
					hospitalDto3.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmMailId());
					hospitalDtoList.add(hospitalDto3);	
					
					hrmDto  = new HRMTableDTO();
					hrmDto.setAnhOrNanh(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getNetworkHospitalType());
					hrmDto.setDiagnosis(bean.getNewIntimationDTO().getDiagnosis());
					hrmDto.setClaimedAmt(Double.parseDouble(bean.getAmountRequested()));
					//hrmDto.setRequestType(bean.getClaimDTO().getClaimType());
					hrmDto.setDocRemarks(bean.getDoctorNote());
					hrmDto.setIntimationNO(bean.getNewIntimationDTO().getIntimationId());
					hrmDto.setHrmId(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmCode());
					hrmDto.setCashlessKey(bean.getKey());
					hrmDto.setHospitalCode(bean.getNewIntimationDTO().getHospitalDto().getHospitalCode());
				
					List<ProcedureDTO>  procedureList =  bean.getPreauthDataExtractionDetails().getProcedureList();
					ArrayList<Long> packageList = new ArrayList<Long>();
					 
					 StringBuffer procedureName = new StringBuffer();
					 for (ProcedureDTO procedureDTO : procedureList) {
						 
						 procedureName.append(procedureDTO.getProcedureName().getValue()).append(",");
						 
						 packageList.add(procedureDTO.getPackageRate());
					}
					 if(null != packageList && !packageList.isEmpty())
					 {
					hrmDto.setPackageAmt(Collections.max(packageList));
					 }
					 
					 hrmDto.setSurgicalProcedure(procedureName.toString());
					 
					 List<DiagnosisDetailsTableDTO> diagnosisList =  bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
					 
					 StringBuffer diagnosisName = new StringBuffer();
					 for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
						
						 diagnosisName.append(diagnosisDetailsTableDTO.getDiagnosis()).append(",");
					}
					 
					 hrmDto.setDiagnosis(diagnosisName.toString());
					
					 TmpEmployee employeeDetails  = preauthService.getLoginDetails(null != bean.getStrUserName() ? bean.getStrUserName().toLowerCase() : null);
					 if(null != employeeDetails){
						 
						 hrmDto.setDocName(employeeDetails.getEmpFirstName());
						 hrmDto.setDocUserId(employeeDetails.getEmpId());
					 }
					
					hrmDto.setAssigneeDateAndTime(new Timestamp(System.currentTimeMillis()));

					hrmtableDtoList.add(hrmDto);
					
				
				VerticalLayout horLayout = new VerticalLayout();
					
				hrmtable.viewInt(hospitalDtoList);
				hrmtable.init("",false,false);
				hrmtable.initTable();
				
				horLayout.addComponents(btnViewEarlierHrmDetails,hrmtable);				
			
				hrmListenerTable.init();			
				hrmListenerTable.initTable();
				referenceDataMap = new WeakHashMap<String, Object>();
				
				SelectValue selectPreauth= new SelectValue();
				selectPreauth.setId(1l);
				selectPreauth.setValue("Pre-Auth");

				SelectValue selectEnhancement = new SelectValue();

				selectEnhancement.setId(2l);
				selectEnhancement.setValue("Enhancement");
				
				SelectValue selectInformation = new SelectValue();

				selectInformation.setId(3l);
				selectInformation.setValue("Information");
				

				List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
				selectVallueList.add(selectPreauth);
				selectVallueList.add(selectEnhancement);
				selectVallueList.add(selectInformation);				
				
				BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				selectValueContainer.addAll(selectVallueList);
				referenceDataMap.put("requestTypeContainer",selectValueContainer);
				hrmListenerTable.setReferenceData(referenceDataMap);
				setTableValues(hrmtableDtoList);
				//SHAUtils.setClearReferenceData(referenceDataMap);
				Window popup = new com.vaadin.ui.Window();
		    	popup.setWidth("75%");
		    	popup.setHeight("90%");	
		    	HorizontalLayout hLayout = new HorizontalLayout();
		    	hLayout.addComponents(btnSubmit);
		    	VerticalLayout verticalLayout = new VerticalLayout();
		    	verticalLayout.addComponents(horLayout,hrmListenerTable,hLayout);			
				popup.setContent(verticalLayout);
				verticalLayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
//		    	popup.setContent();
		    	popup.setClosable(true);
		    	popup.center();
		    	popup.setResizable(false);
		    	popup.addCloseListener(new Window.CloseListener() {
		    		
		    		@Override
		    		public void windowClose(CloseEvent e) {
		    			System.out.println("Close listener called");
		    		}
		    	});

		    	popup.setModal(true);
		    	UI.getCurrent().addWindow(popup);		    	
		    				
			}
		});
		

		btnViewEarlierHrmDetails.addClickListener(new Button.ClickListener() {
			
		
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {		
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("View Earlier HRM");
				bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				List<HRMHospitalDetailsTableDTO> hospitalDtoList = new ArrayList<HRMHospitalDetailsTableDTO>();	
								
				HRMHospitalDetailsTableDTO hospitalDto1 = new HRMHospitalDetailsTableDTO();
				hospitalDto1.setHardCodedString("Intimation Number");
				hospitalDto1.setHardCodedStringValue(bean.getNewIntimationDTO().getIntimationId());
				hospitalDto1.setHardCodedString1("Hospital Name");
				hospitalDto1.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getName());
				hospitalDtoList.add(hospitalDto1);
				HRMHospitalDetailsTableDTO hospitalDto2 = new HRMHospitalDetailsTableDTO();
				hospitalDto2.setHardCodedString("Hrm Id");
				hospitalDto2.setHardCodedStringValue(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmCode());
				hospitalDto2.setHardCodedString1("Phone");
				hospitalDto2.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmContactNo());
				hospitalDtoList.add(hospitalDto2);
				HRMHospitalDetailsTableDTO hospitalDto3 = new HRMHospitalDetailsTableDTO();
				hospitalDto3.setHardCodedString("Name");
				hospitalDto3.setHardCodedStringValue(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmUserName());
				hospitalDto3.setHardCodedString1("Email Id");
				hospitalDto3.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmMailId());
				hospitalDtoList.add(hospitalDto3);	
				
				viewEarlierHrmTable.viewInt(hospitalDtoList);
				viewEarlierHrmTable.init("",false,false);
				viewEarlierHrmTable.initTable();
				
				List<HRMTableDTO> earilerHrmDetails = preauthService.getEarlierHrmDetails(bean.getNewIntimationDTO().getIntimationId());
				List<DiagnosisDetailsTableDTO> diagnosisList =  bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
				 
				for (HRMTableDTO hrmTableDTO : earilerHrmDetails) {
					
					StringBuffer diagnosisName = new StringBuffer();
					
					 for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
						
						 diagnosisName.append(diagnosisDetailsTableDTO.getDiagnosis()).append(" ");
					}
					 
					 hrmTableDTO.setDiagnosis(diagnosisName.toString());
				}
				 
				
				 
				viewEarilerHrmListenerTable.init();			
				viewEarilerHrmListenerTable.initTable();
			//	setTableValues(hrmtableDtoList);
				
				 
				setTableValuesForEarliarHrmDetails(earilerHrmDetails);
				VerticalLayout verticalLayout = new VerticalLayout();
		    	verticalLayout.addComponents(viewEarlierHrmTable,viewEarilerHrmListenerTable);	
		    	
				Window popup = new com.vaadin.ui.Window();
		    	popup.setWidth("75%");
		    	popup.setHeight("90%");					
				popup.setContent(verticalLayout);
//		    	popup.setContent();
		    	popup.setClosable(true);
		    	popup.center();
		    	popup.setResizable(false);
		    	popup.addCloseListener(new Window.CloseListener() {
		    		
		    		@Override
		    		public void windowClose(CloseEvent e) {
		    			System.out.println("Close listener called");
		    		}
		    	});

		    	popup.setModal(true);
		    	UI.getCurrent().addWindow(popup);
			}
		});
		if(this.bean.getIsCancelPolicy()){
			
            if(this.wizard != null){
            	this.wizard.getCancelButton().setEnabled(false);
            }
			approveBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			downSizePreauthBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);	
			withdrawPreauthBtn.setEnabled(false);
		}
		
		referToCPU = new Button("Opinion");
//		referToCPU.setHeight("-1px");
		//Vaadin8-setImmediate() referToCPU.setImmediate(true);
		referToCPU.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		referToCPU.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_REFER_CPU_USER,null);
			}
		});
		
		holdBtn = new Button("Hold");
//		holdBtn.setHeight("-1px");
		//Vaadin8-setImmediate() holdBtn.setImmediate(true);
		holdBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("Hold");
				fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_HOLD_EVENT,null);
			}
		});
		
		topUpBtn = new Button("Utilise Top-up policy");
		topUpBtn.setEnabled(false);
		topUpBtn.setHeight("-1px");
		//Vaadin8-setImmediate() approveBtn.setImmediate(true);
		topUpBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				//confirmMessageForTopUp();
				fireViewEvent(PreauthEnhancemetWizardPresenter.ENHN_TOPUP_POLICY_EVENT,bean);
			}
		});
	
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

	private void callApproveLayout(Intimation intimationDtls) {
		SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
		if (ReferenceTable.getSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				&& sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null 
				&& sectionDetailsDTO.getCover().getCommonValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE)
				&& null != bean.getSuperSurplusAlertList() && bean.getSuperSurplusAlertList().size() > 0 ) {
					showSuperSurplusAlertList(bean.getSuperSurplusAlertList());
		}else {
			if(validatePolicyStatus(bean.getPolicyDto().getPolicyNumber()) || (intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y"))){
				if (!bean.getIsReverseAllocation() && !medicalDecisionTableObj.getTotalAmountConsidered().equals(
						SHAUtils.getIntegerFromString(bean.getAmountConsidered()))) {
					String msg = "Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table </br>";
					showErrorMessage(msg);
				}else{
					if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
						alertForAdditionalFvrTriggerPoints(SHAConstants.APPROVAL);
					}
					else{
						if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement()){
							reConfirmMessageForBenefitSheet();
						}else{
							bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);
							fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
						}
					}
					
				}
			} else {
				showErrorPageForCancelledPolicy();
			}
		}
	}

	public void reConfirmMessageForBenefitSheet(){/*
	
		
		ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Do you wish to send the Benefit sheet to Hospital?",
		        "No", "Yes", new ConfirmDialog.Listener() {
	
		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                	bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);
		                	 dialog.close();
		                	 fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
		                } else {
		                    bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(false);
		                    dialog.close();
		                    fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
		                }
		            }
		        });
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	*/
		/*final MessageBox getConf = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage("Do you wish to send the Benefit sheet to Hospital?")
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
			Button homeButton=getConf.getButton(ButtonType.YES);
			Button cancelButton=getConf.getButton(ButtonType.NO);	
			homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
*/					bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);
					//getConf.close();
	           	 fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
	          
			/*		
				}
			});
			
			cancelButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					 bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(false);
					 getConf.close();
	                    fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
	                
					
				}
			});*/
	}

	public void manageNegotation(final Intimation intimationDtls,final NegotiationDetails negotiation,final String decision){
		bean.getPreauthMedicalDecisionDetails().setIsNegotiationDone(Boolean.FALSE);
		ConfirmDialog dialog = ConfirmDialog.show(UI.getCurrent(),"Confirmation", "Negotation is under progress. Do You Want to Cancel or Update?",
		        "Update", "Cancel", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                	 dialog.close();
		                	 showNagotiationRemarks(true, intimationDtls,negotiation,decision);
		                } else {
		                    dialog.close();
		                    showNagotiationRemarks(false, intimationDtls,negotiation,decision);
		                }
		            }
		        });
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	}
	
	
	private void setTableValues(List<HRMTableDTO> hrmtableDtoList)
	{
		
		if(null != hrmListenerTable)
	
			{
				if(null != hrmtableDtoList && !hrmtableDtoList.isEmpty())
				{
					for (HRMTableDTO hrmTableDto : hrmtableDtoList) {
						
						hrmListenerTable.addBeanToList(hrmTableDto);
					}
				}
			}
		}
	
	private void setTableValuesForEarliarHrmDetails(List<HRMTableDTO> hrmtableDtoList)
	{
		
		if(null != viewEarilerHrmListenerTable)
	
			{
				if(null != hrmtableDtoList && !hrmtableDtoList.isEmpty())
				{
					for (HRMTableDTO hrmTableDto : hrmtableDtoList) {
						
						viewEarilerHrmListenerTable.addBeanToList(hrmTableDto);
					}
				}
			}
		}
	
	public void setCancelPolicyProcess(PreauthDTO bean){
		this.bean = bean;
		if(this.bean.getIsCancelPolicy()){
				if(this.wizard != null){
	            	this.wizard.getCancelButton().setEnabled(false);
	         }
			approveBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			downSizePreauthBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);	
			withdrawPreauthBtn.setEnabled(false);
		}
		
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	@SuppressWarnings("unchecked")
	public void buildApproveLayout(Integer amt)
	{
		if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(("i").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag())){

				this.bean.getPreauthMedicalDecisionDetails().setApprovalRemarks("\n\nPlease send us indoor case sheets, investigation / diagnosis reports, OT notes, implant invoices if applicable, discharge summary, final bill with break up details and other relevant documents and also documents collected from the insured-patient for our further action.");
				bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);

			}
			else{
				this.bean.getPreauthMedicalDecisionDetails().setApprovalRemarks("");
				bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);
			}
		}
		
		initBinder();
		bean.setStatusKey(ReferenceTable.ENHANCEMENT_APPROVE_STATUS);

		unbindAndRemoveComponents(dynamicFrmLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);
		
		txtClaimedAmt = (TextField) binder.buildAndBind("Claimed Amount","claimedAmt",TextField.class);
		txtClaimedAmt.setNullRepresentation("");
		txtClaimedAmt.setEnabled(false);
		txtClaimedAmt.setVisible(false);
		txtClaimedAmt.setValue(bean.getPreauthDataExtractionDetails().getAmtClaimed());
		
		txtNegotiationAmt = (TextField) binder.buildAndBind("Negotiated Amount/Discount","negotiationAmount",TextField.class);
		txtNegotiationAmt.setNullRepresentation("");
		txtNegotiationAmt.setVisible(false);
		CSValidator txtAmtToNeg = new CSValidator();
		txtAmtToNeg.setRegExp("^[0-9']*$");
		txtAmtToNeg.setPreventInvalidTyping(true);
		txtAmtToNeg.extend(txtNegotiationAmt);
		txtNegotiationAmt.addBlurListener(setNegotiationdiscListener());
		
		txtSavedAmt = (TextField) binder.buildAndBind("Saved Amount", "savedAmt", TextField.class);
		txtSavedAmt.setNullRepresentation("");
		txtSavedAmt.setVisible(false);
		txtSavedAmt.setEnabled(false);
		
		txtNegotiationWith = (TextArea) binder.buildAndBind("Negotiated With","negotiationWith",TextArea.class);
		txtNegotiationWith.setNullRepresentation("");
		txtNegotiationWith.setMaxLength(500);
		txtNegotiationWith.setWidth("180px");
		txtNegotiationWith.setVisible(false);
		txtNegotiationWith.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtNegotiationWith,null,getUI(),SHAConstants.NEGOWITH);
		
		txtTotNegotiationSavedAmt = (TextField) binder.buildAndBind("Total Negotiated Savings","totalNegotiationSaved",TextField.class);
		txtTotNegotiationSavedAmt.setNullRepresentation("");
		txtTotNegotiationSavedAmt.setEnabled(false);
		txtTotNegotiationSavedAmt.setVisible(false);
		
		txthigestCLTransAmt = (TextField) binder.buildAndBind("Highest Cashless transaction","higestCLTrans",TextField.class);
		txthigestCLTransAmt.setNullRepresentation("");
		txthigestCLTransAmt.setEnabled(false);
		txthigestCLTransAmt.setVisible(false);
			
		
		if(bean.getPreauthMedicalDecisionDetails().getNegotiationMade()){
			txtClaimedAmt.setVisible(true);
			txtNegotiationAmt.setVisible(true);
			txtSavedAmt.setVisible(true);
			txtNegotiationWith.setVisible(true);
			txtTotNegotiationSavedAmt.setVisible(true);
			txthigestCLTransAmt.setVisible(true);
		}
		
		/*if(bean.getPreauthMedicalDecisionDetails().getNegotiationAmount() != null){
			String negAmt = txtNegotiationAmt.getValue();
			Integer negotiationAmt = SHAUtils.getIntegerFromStringWithComma(negAmt);
			Integer amountConsidered = SHAUtils.getIntegerFromStringWithComma(bean.getAmountConsidered());
			Integer savedAmt = amountConsidered - negotiationAmt;
				if(negotiationAmt > amountConsidered) {
					showErrorMsgForNegotiation();
					txtNegotiationAmt.setValue("");
				} else {
					txtSavedAmt.setValue(savedAmt.toString());
				}
		}*/

		initialApprovedAmtTxt = (TextField) binder.buildAndBind("Total Approved Amt", "initialTotalApprovedAmt", TextField.class);
		initialApprovedAmtTxt.setNullRepresentation("");
		initialApprovedAmtTxt.setValue(amt.toString());
		initialApprovedAmtTxt.setEnabled(false);
		
		/*benefitSheetRatio = (OptionGroup) binder.buildAndBind(
				"Do you wish to send Benefit sheet to hospital",
				"isBeneifitSheetAvailable", OptionGroup.class);
		
		benefitSheetRatio.addItems(getReadioButtonOptions());
		benefitSheetRatio.setItemCaption(true, "Yes");
		benefitSheetRatio.setItemCaption(false, "No");
		benefitSheetRatio.setStyleName("horizontal");*/
		
		if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(("i").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag())){
				/*benefitSheetRatio.setValue(null);
				benefitSheetRatio.setEnabled(false);*/
				bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);
			}
			else {
				/*benefitSheetRatio.setValue(true);
				benefitSheetRatio.setEnabled(true);*/
				bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);
			}
		}
		
		negotiation = (OptionGroup) binder.buildAndBind("Have you made negotiation on this claim",
				"negotiationMade",OptionGroup.class);
		negotiation.addItems(getReadioButtonOptions());
		negotiation.setItemCaption(true, "Yes");
		negotiation.setItemCaption(false, "No");
		negotiation.setStyleName("horizontal");
		negotiation.select(false);
		if(bean.getPreauthMedicalDecisionDetails().getNegotiationMade()){
			negotiation.setValue(true);
			isNegotiationMade = true;
			fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_LOAD_NEGOTIATED_SAVED_AMOUNT,null);
		}
		negotiation.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				String enhancementType = "I";
				String hospitilizationType = "Y";
				Integer qryTyp;
				if(event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue().toString().equalsIgnoreCase("true")) {
					isChecked = true;
					isNegotiationMade = true;
					txtClaimedAmt.setVisible(true);
					txtNegotiationAmt.setVisible(true);
					txtSavedAmt.setVisible(true);
					txtNegotiationWith.setVisible(true);
					txtTotNegotiationSavedAmt.setVisible(true);
					txthigestCLTransAmt.setVisible(true);
					if (bean.getQueryType() == null){
						qryTyp = 0;
					}else{
						qryTyp = bean.getQueryType().getId().intValue();
					}
					Integer qryCnt;
					if (bean.getQueryCount() == null){
						qryCnt = 0;
					}else{
						qryCnt = bean.getQueryCount() + 1;
					}
					String reconsiderationFlag = null != bean.getIsRejectReconsidered() && bean.getIsRejectReconsidered() ? "Y" : "N";
					String finalClaimAmount = "";
					if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
						finalClaimAmount = bean.getAmountConsidered();
					}else{
						finalClaimAmount = bean.getPreauthDataExtractionDetails().getAmtClaimed();
					}
					finalClaimAmount = (finalClaimAmount == null)?"0":finalClaimAmount;
					System.out.println("Opinion Call For Negotation Y Flag!!!!!");
					Map<String,Object> opinionValues = dbCalculationService.getOpinionValidationDetails(Long.valueOf(finalClaimAmount),bean.getStageKey(),bean.getStatusKey(),
							Long.valueOf(bean.getNewIntimationDTO().getCpuCode()),reconsiderationFlag,bean.getNewIntimationDTO().getPolicy().getKey(),bean.getClaimDTO().getKey(),enhancementType,hospitilizationType,qryTyp,qryCnt,
							bean.getClaimDTO().getClaimType().getId(),
							ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL,SHAConstants.PRE_AUTH,bean.getKey(), bean.getStatusKey(),SHAConstants.YES_FLAG); 

					BeanItemContainer<SpecialSelectValue> userRole = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
					userRole.addAll((List<SpecialSelectValue>)opinionValues.get("role"));

					BeanItemContainer<SpecialSelectValue> empNames = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
					empNames.addAll((List<SpecialSelectValue>) opinionValues.get("emp"));

					if(cmbUserRoleMulti !=null){
						cmbUserRoleMulti.setContainerDataSource(userRole);
						cmbUserRoleMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cmbUserRoleMulti.setItemCaptionPropertyId("value");	
						cmbUserRoleMulti.setData(userRole);
					}
					if(cmbDoctorNameMulti !=null){
						cmbDoctorNameMulti.setContainerDataSource(empNames);
						cmbDoctorNameMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cmbDoctorNameMulti.setItemCaptionPropertyId("commonValue");
						cmbDoctorNameMulti.setData(empNames);
					}
					String mandatoryFlag =  (String) opinionValues.get("mandatoryFlag");
					if(null != mandatoryFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(mandatoryFlag)){
						bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.TRUE);
						mandatoryFields.add(cmbUserRoleMulti);
						mandatoryFields.add(cmbDoctorNameMulti);
						mandatoryFields.add(remarksFromDeptHead);
						showOrHideValidation(false);
					}
					if (bean.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)){
						Long prodKey = bean.getNewIntimationDTO().getPolicy().getProduct().getKey();
						String insuredNo=null;
						String commonIP=null;
						 if(bean.getPreauthDataExtractionDetails()!=null && bean.getPreauthDataExtractionDetails().getDiagnosisTableList()!=null
								 && !bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()){
							 for (DiagnosisDetailsTableDTO detailsTableDTO: bean.getPreauthDataExtractionDetails().getDiagnosisTableList()){
								 if(detailsTableDTO.getIcdCode() !=null && detailsTableDTO.getIcdCode().getCommonValue() !=null){
									 Map<String,String> PerExcludedICDCode = dbCalculationService.getPermanentExclusionsIcdMapping(detailsTableDTO.getIcdCode().getCommonValue(),prodKey,null,null,insuredNo,commonIP);
									 String icdCodeFlag= PerExcludedICDCode.get("flag");
										String icdCodeRemarks= PerExcludedICDCode.get("remarks");
										if(icdCodeFlag!=null && icdCodeFlag.equalsIgnoreCase("A")){
											bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.TRUE);
											mandatoryFields.add(cmbUserRoleMulti);
											mandatoryFields.add(cmbDoctorNameMulti);
											mandatoryFields.add(remarksFromDeptHead);
											showOrHideValidation(false);
											}
								 		}
							 		}
								 }
							}
					fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_LOAD_NEGOTIATED_SAVED_AMOUNT,null);
				} 
				else if(event.getProperty() != null && event.getProperty().getValue() != null) {
					isNegotiationMade = false;
					txtClaimedAmt.setVisible(false);
					txtNegotiationAmt.setVisible(false);
					txtSavedAmt.setVisible(false);
					txtNegotiationWith.setVisible(false);
					txtTotNegotiationSavedAmt.setVisible(false);
					txthigestCLTransAmt.setVisible(false);
					
					if (bean.getQueryType() == null){
						qryTyp = 0;
					}else{
						qryTyp = bean.getQueryType().getId().intValue();
					}
					Integer qryCnt;
					if (bean.getQueryCount() == null){
						qryCnt = 0;
					}else{
						qryCnt = bean.getQueryCount() + 1;
					}
					String reconsiderationFlag = null != bean.getIsRejectReconsidered() && bean.getIsRejectReconsidered() ? "Y" : "N";
					String finalClaimAmount = "";
					if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
						finalClaimAmount = bean.getAmountConsidered();
					}else{
						finalClaimAmount = bean.getPreauthDataExtractionDetails().getAmtClaimed();
					}
					finalClaimAmount = (finalClaimAmount == null)?"0":finalClaimAmount;
					System.out.println("Opinion Call For Negotation N Flag!!!!!");
					Map<String,Object> opinionValues = dbCalculationService.getOpinionValidationDetails(Long.valueOf(finalClaimAmount),bean.getStageKey(),bean.getStatusKey(),
							Long.valueOf(bean.getNewIntimationDTO().getCpuCode()),reconsiderationFlag,bean.getNewIntimationDTO().getPolicy().getKey(),bean.getClaimDTO().getKey(),enhancementType,hospitilizationType,qryTyp,qryCnt,
							bean.getClaimDTO().getClaimType().getId(),
							ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL,SHAConstants.PRE_AUTH,bean.getKey(), bean.getStatusKey(),SHAConstants.N_FLAG); 

					BeanItemContainer<SpecialSelectValue> userRole = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
					userRole.addAll((List<SpecialSelectValue>)opinionValues.get("role"));

					BeanItemContainer<SpecialSelectValue> empNames = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
					empNames.addAll((List<SpecialSelectValue>) opinionValues.get("emp"));

					if(cmbUserRoleMulti !=null){
						cmbUserRoleMulti.setContainerDataSource(userRole);
						cmbUserRoleMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cmbUserRoleMulti.setItemCaptionPropertyId("value");	
						cmbUserRoleMulti.setData(userRole);
					}
					if(cmbDoctorNameMulti !=null){
						cmbDoctorNameMulti.setContainerDataSource(empNames);
						cmbDoctorNameMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cmbDoctorNameMulti.setItemCaptionPropertyId("commonValue");
						cmbDoctorNameMulti.setData(empNames);
					}
					String mandatoryFlag =  (String) opinionValues.get("mandatoryFlag");
					if(null != mandatoryFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(mandatoryFlag)){
						bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.TRUE);
						mandatoryFields.add(cmbUserRoleMulti);
						mandatoryFields.add(cmbDoctorNameMulti);
						mandatoryFields.add(remarksFromDeptHead);
						showOrHideValidation(false);
					}else{
						bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
						mandatoryFields.remove(cmbUserRoleMulti);
						mandatoryFields.remove(cmbDoctorNameMulti);
						mandatoryFields.remove(remarksFromDeptHead);
						cmbUserRoleMulti.setRequired(false);
						cmbDoctorNameMulti.setRequired(false);
						remarksFromDeptHead.setRequired(false);
					}
					if (bean.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)){
						Long prodKey = bean.getNewIntimationDTO().getPolicy().getProduct().getKey();
						String insuredNo=null;
						String commonIP=null;
						 if(bean.getPreauthDataExtractionDetails()!=null && bean.getPreauthDataExtractionDetails().getDiagnosisTableList()!=null
								 && !bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()){
							 for (DiagnosisDetailsTableDTO detailsTableDTO: bean.getPreauthDataExtractionDetails().getDiagnosisTableList()){
								 if(detailsTableDTO.getIcdCode() !=null && detailsTableDTO.getIcdCode().getCommonValue() !=null){
									 Map<String,String> PerExcludedICDCode = dbCalculationService.getPermanentExclusionsIcdMapping(detailsTableDTO.getIcdCode().getCommonValue(),prodKey,null,null,insuredNo,commonIP);
									 String icdCodeFlag= PerExcludedICDCode.get("flag");
										String icdCodeRemarks= PerExcludedICDCode.get("remarks");
										if(icdCodeFlag!=null && icdCodeFlag.equalsIgnoreCase("A")){
											bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.TRUE);
											mandatoryFields.add(cmbUserRoleMulti);
											mandatoryFields.add(cmbDoctorNameMulti);
											mandatoryFields.add(remarksFromDeptHead);
											showOrHideValidation(false);
											}
								 		}
							 		}
								 }
							}
						}
					}
				});
		
		
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			uniquePremiumAmt = (TextField) binder.buildAndBind("Less: II Installment Premium Amt","uniquePremiumAmt",TextField.class);
			Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
			uniquePremiumAmt.setValue(String.valueOf(uniqueInstallmentAmount));
			uniquePremiumAmt.setEnabled(false);
			uniquePremiumAmt.setNullRepresentation("0");
			
			amountToHospAftPremium = (TextField) binder.buildAndBind("Pre-auth Approved Amt - Hospital","amountToHospAftPremium",TextField.class);
			
			Double aftPremAmt = SHAUtils.getDoubleFromStringWithComma(initialApprovedAmtTxt.getValue()) - SHAUtils.getDoubleFromStringWithComma(uniquePremiumAmt.getValue());
			amountToHospAftPremium.setValue(String.valueOf(aftPremAmt.longValue() < 0l ? 0l : aftPremAmt.longValue()) );
			amountToHospAftPremium.setEnabled(false);
			amountToHospAftPremium.setNullRepresentation("0");
			
		}
		
		initialTotalApprovedAmtTxt = (TextField) binder.buildAndBind("Pre-auth Approved Amt (till Date)", "initialApprovedAmt", TextField.class);
		initialTotalApprovedAmtTxt.setNullRepresentation("");
		initialTotalApprovedAmtTxt.setEnabled(false);
		initialTotalApprovedAmtTxt.setValue(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null ? this.bean.getPreauthDataExtractionDetails().getTotalApprAmt().toString() : null);
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			initialTotalApprovedAmtTxt.setValue(this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction() != null ? this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction().toString() : null);
		}
		
		TextField preauthAmtTillDate = new TextField("Enhancement App Amt");
		preauthAmtTillDate.setNullRepresentation("");
		preauthAmtTillDate.setEnabled(false);
		Integer value = this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null ? this.bean.getPreauthDataExtractionDetails().getTotalApprAmt().intValue() : 0;
		preauthAmtTillDate.setValue(String.valueOf(amt - value > 0 ? amt-value : 0));
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			Double doubleFromStringWithComma = SHAUtils.getDoubleFromStringWithComma(amountToHospAftPremium.getValue());
			preauthAmtTillDate.setValue(String.valueOf(doubleFromStringWithComma.intValue() - SHAUtils.getDoubleFromStringWithComma(initialTotalApprovedAmtTxt.getValue()).intValue()));
		}
		
		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
				bean.getPreauthDataExtractionDetails().getOtherBenfitFlag() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getOtherBenfitFlag())){
			benefitAmtTxt = new TextField();
			benefitAmtTxt.setValue(String.valueOf(bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount().longValue()));
			benefitAmtTxt.setEnabled(false);
			benefitAmtTxt.setNullRepresentation("0");
			viewOtherBenefitAmtLink = new Button("View Other Benefit");
			viewOtherBenefitAmtLink.setStyleName(ValoTheme.BUTTON_LINK);
			viewOtherBenefitAmtLink.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(PreauthEnhancemetWizardPresenter.VIEW_PREAUTH_ENH_OTHER_BENEFITS,null);					
				}
			});
		}		
		
		//added for cr2019184 policy instalment handling function by noufel on 14-05-2020
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			policyInstPremiumAmt = (TextField) binder.buildAndBind("Policy Installment Premium","policyInstPremiumAmt",TextField.class);
			policyInstPremiumAmt.setNullRepresentation("0");
			policyInstPremiumAmt.setValue(String.valueOf(bean.getPolicyInstalmentPremiumAmt().longValue()));
			policyInstPremiumAmt.setEnabled(false);
			amountToHospAftPremium = (TextField) binder.buildAndBind("Pre-auth Approved Amt - Hospital","amountToHospAftPremium",TextField.class);

			Double aftPremAmt = SHAUtils.getDoubleFromStringWithComma(initialApprovedAmtTxt.getValue()) - SHAUtils.getDoubleFromStringWithComma(policyInstPremiumAmt.getValue());
			amountToHospAftPremium.setValue(String.valueOf(aftPremAmt.longValue() < 0l ? 0l : aftPremAmt.longValue()));
			amountToHospAftPremium.setEnabled(false);
			amountToHospAftPremium.setNullRepresentation("0");
			initialTotalApprovedAmtTxt.setValue(this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction() != null ? this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction().toString() : null);
			//			TextField preauthAmtTillDate = new TextField("Enhancement App Amt");
			//			preauthAmtTillDate.setNullRepresentation("");
			//			preauthAmtTillDate.setEnabled(false);
			//			Integer value = this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null ? this.bean.getPreauthDataExtractionDetails().getTotalApprAmt().intValue() : 0;
			preauthAmtTillDate.setValue(String.valueOf(amt - value > 0 ? amt-value : 0));
			Double doubleFromStringWithComma = SHAUtils.getDoubleFromStringWithComma(amountToHospAftPremium.getValue());
			preauthAmtTillDate.setValue(String.valueOf(doubleFromStringWithComma.intValue() - SHAUtils.getDoubleFromStringWithComma(initialTotalApprovedAmtTxt.getValue()).intValue()));
		}else{
			if(policyInstPremiumAmt != null){
				policyInstPremiumAmt.setVisible(false);
			}
			if(amountToHospAftPremium != null){
				amountToHospAftPremium.setVisible(false);
			}
		}
		approvalRemarksTxta = (TextArea) binder.buildAndBind("Enhancement App Remarks", "approvalRemarks",TextArea.class);
		
		/*if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null &&
					  !ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){

				List<NoOfDaysCell> claimedDetailsList = bean.getPreauthDataExtractionDetails().getClaimedDetailsList();
				if(claimedDetailsList != null && !claimedDetailsList.isEmpty()) {
						for (NoOfDaysCell noOfDaysCell : claimedDetailsList) {
						
								if(noOfDaysCell.getBenefitId().intValue() == 8 && (noOfDaysCell.getClaimedBillAmount() == null || (noOfDaysCell.getClaimedBillAmount() != null && noOfDaysCell.getClaimedBillAmount().intValue() == 0))){ 
										approvalRemarksTxta.setValue(bean.getNewIntimationDTO().getRoomRentEligibility());
										break;
								}	
						}
				}
		}*/

		approvalRemarksTxta.setMaxLength(4000);
		approvalRemarksTxta.setWidth("260px");

		addingSentToCPUFields();

		updateButton = new Button();
		updateButton = new Button("Update Pan Card");
		updateButton.setHeight("-1px");
		//Vaadin8-setImmediate() updateButton.setImmediate(true);
		updateButton.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
					 fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_UPDATE_PAN_CARD_EVENT,null);
				
			}
		});
		
		TextField dummy = new TextField();
		dummy.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		FormLayout formLayout12 = new FormLayout(dummy,updateButton);
		//negotiationLayout = new FormLayout();
		negotiationOpinionTaken = (OptionGroup) binder.buildAndBind(
				"Do you want to Refer for Settlement Negotiation", "negotiationDecisionTaken",
				OptionGroup.class);
		negotiationOpinionTaken.addItems(getReadioButtonOptions());
		negotiationOpinionTaken.setItemCaption(true, "Yes");
		negotiationOpinionTaken.setItemCaption(false, "No");
		negotiationOpinionTaken.setStyleName("horizontal");
	//	negotiationOpinionTaken.select(false);
		negotiationOpinionTaken.setVisible(false);
		
		bean.getPreauthMedicalDecisionDetails().setNegotiationDecisionTaken(null);
		bean.getPreauthMedicalDecisionDetails().setNegotiatePoints(null);
		
		fireViewEvent(PreauthEnhancemetWizardPresenter.NEGOTIATION_PENDING, bean);
		addNegotiationListener();
		
		//negotiationLayout.addComponent(negotiationOpinionTaken);
	    //	negotiationLayout.setMargin(false);
		//negotiationLayout.setSpacing(false);	
		if(null != bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken() && bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken()){
			genertateFieldsBasedOnNegotiation(true);
		}
		verifiedBonus = (OptionGroup) binder.buildAndBind("Verified Bonus", "verifiedBonus",OptionGroup.class);
		verifiedBonus.addItems(getReadioButtonOptions());
		verifiedBonus.setItemCaption(true, "Yes");
		verifiedBonus.setItemCaption(false, "No");
		verifiedBonus.setStyleName("horizontal");
		verifiedBonus.setVisible(false);
		addListenerForVerifiedBonus();
		
		if (null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			verifiedBonus.setVisible(true);
		}	
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			dynamicFrmLayout.addComponents(verifiedBonus,negotiationOpinionTaken,/*benefitSheetRatio,*/initialApprovedAmtTxt, policyInstPremiumAmt, amountToHospAftPremium, initialTotalApprovedAmtTxt, preauthAmtTillDate, approvalRemarksTxta);

		}
		else if(uniquePremiumAmt != null && amountToHospAftPremium != null) {
			dynamicFrmLayout.addComponents(verifiedBonus,negotiationOpinionTaken,/*benefitSheetRatio,*/initialApprovedAmtTxt, uniquePremiumAmt, amountToHospAftPremium, initialTotalApprovedAmtTxt, preauthAmtTillDate, approvalRemarksTxta);

		} else {
			
				if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) &&
						bean.getPreauthDataExtractionDetails().getOtherBenfitFlag() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getOtherBenfitFlag())){
				
					HorizontalLayout benefitLayout = new HorizontalLayout(benefitAmtTxt,viewOtherBenefitAmtLink);
					benefitLayout.setSpacing(true);
				
					benefitLayout.setCaption("Other Benefits Approved Amt");
					TextField totalApprovedAmtTxt = new TextField("Total Amount");
					totalApprovedAmtTxt.setNullRepresentation("");
					
				
									
					Double totalApprovedAmt = amt.doubleValue();
					totalApprovedAmt += bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount();
					totalApprovedAmtTxt.setValue(String.valueOf(totalApprovedAmt.longValue()));				
					totalApprovedAmtTxt.setEnabled(false);
					
					txtTotNegotiationSavedAmt.setVisible(true);
					txthigestCLTransAmt.setVisible(true);
					dynamicFrmLayout.addComponents(verifiedBonus,negotiationOpinionTaken,/*benefitSheetRatio,*/negotiation,txthigestCLTransAmt,txtClaimedAmt,txtNegotiationWith,txtNegotiationAmt,txtSavedAmt,txtTotNegotiationSavedAmt,initialApprovedAmtTxt, initialTotalApprovedAmtTxt, preauthAmtTillDate, benefitLayout,totalApprovedAmtTxt,approvalRemarksTxta);
				}
				else{
					dynamicFrmLayout.addComponents(verifiedBonus,negotiationOpinionTaken,/*benefitSheetRatio,*/negotiation,txthigestCLTransAmt,txtClaimedAmt,txtNegotiationWith,txtNegotiationAmt,txtSavedAmt,txtTotNegotiationSavedAmt,initialApprovedAmtTxt, initialTotalApprovedAmtTxt, preauthAmtTillDate, approvalRemarksTxta);	
				}
			
		}		
		
		
		if(null != bean.getIsNegotiationApplicable() && bean.getIsNegotiationApplicable()){
			if(bean.getIsNegotiationPending()){
				negotiationOpinionTaken.setEnabled(false);
				negotiationOpinionTaken.setValue(true);
				negotiationOpinionTaken.setVisible(true);
			}
			else
			{
				negotiationOpinionTaken.setVisible(true);
				negotiationOpinionTaken.setEnabled(true);
			}
			if(null != bean.getPreauthMedicalDecisionDetails().getIsNegotiationCancelled() && bean.getPreauthMedicalDecisionDetails().getIsNegotiationCancelled()){
				negotiationOpinionTaken.setVisible(false);
			}
		}
		
		//CR  GLX2020076   
				Long prodKey = bean.getNewIntimationDTO().getPolicy().getProduct().getKey();
				//Long prodKey = null;
				String insuredNo=null;
				String commonIP=null;
				 if(bean.getPreauthDataExtractionDetails()!=null && bean.getPreauthDataExtractionDetails().getDiagnosisTableList()!=null
						 && !bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()){
					 for (DiagnosisDetailsTableDTO detailsTableDTO: bean.getPreauthDataExtractionDetails().getDiagnosisTableList()){
						 if(detailsTableDTO.getIcdCode() !=null && detailsTableDTO.getIcdCode().getCommonValue() !=null){
							 Map<String,String> PerExcludedICDCode = dbCalculationService.getPermanentExclusionsIcdMapping(detailsTableDTO.getIcdCode().getCommonValue(),prodKey,null,null,insuredNo,commonIP);
							 String icdCodeFlag= PerExcludedICDCode.get("flag");
								String icdCodeRemarks= PerExcludedICDCode.get("remarks");
								if(icdCodeFlag!=null && icdCodeFlag.equalsIgnoreCase("A")){
									icdExclusionReasonTxta = (TextArea) binder.buildAndBind("Reason for selecting timely excluded ICD","icdExclusionReason",TextArea.class);
									icdExclusionReasonTxta.setMaxLength(500);
									icdExclusionReasonTxta.setWidth("400px");
									System.out.println(icdExclusionReasonTxta.isReadOnly());
									dynamicFrmLayout.addComponent(icdExclusionReasonTxta);
									icdExclusionReasonTxta.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
									SHAUtils.handleTextAreaPopupDetails(icdExclusionReasonTxta,null,getUI(),SHAConstants.TIMELY_EXCLUDED_ICD_CODE_REMARKS);
									mandatoryFields= new ArrayList<Component>();
									mandatoryFields.add(icdExclusionReasonTxta);
									}
						 		}
					 		}
						 }

		bean.getPreauthMedicalDecisionDetails().setUserClickAction("Approve");
		//R1295
		bean.setQueryType(null);
		bean.setQueryCount(0);
		userLayout = buildUserRoleLayout();		
		userLayout.addComponent(updateButton);
		
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(dynamicFrmLayout,userLayout);
		hLayout.setComponentAlignment(userLayout, Alignment.TOP_LEFT);
		hLayout.setHeight("600px");
		
		bonusVLayout.addComponent(hLayout);

		VerticalLayout verLayout = new VerticalLayout(bonusVLayout);
		wholeVlayout.addComponent(verLayout);
		wholeVlayout.removeComponent(remarksLayout);
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
//		mandatoryFields.add(approvalRemarksTxta);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
		
	}
	
	@SuppressWarnings("unchecked")
	public void buildDownsizeLayout(Integer amt, Object downSizeReasonContainer) {
		
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);
		
		totalApprvedAmtField = (TextField) binder.buildAndBind("Total Approved Amt", "approvedAmount", TextField.class);
		totalApprvedAmtField.setNullRepresentation("");
		totalApprvedAmtField.setValue(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null ? this.bean.getPreauthDataExtractionDetails().getTotalApprAmt().toString() : null);
		totalApprvedAmtField.setEnabled(false);
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			totalApprvedAmtField.setValue(this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction() != null ? this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction().toString() : null);
		}
		
//		downsizedAmtField = (TextField) binder.buildAndBind("Pre-auth Downsized Amount ", "downsizedAmt", TextField.class);
		downsizedAmtField = (TextField) binder.buildAndBind("Revised Amount", "downsizedAmt", TextField.class);
		downsizedAmtField.setNullRepresentation("");
		downsizedAmtField.setEnabled(false);
		downsizedAmtField.setValue(amt.toString());
		
		
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			uniquePremiumAmt = (TextField) binder.buildAndBind("Less: II Installment Premium Amt","uniquePremiumAmt",TextField.class);
			Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
			uniquePremiumAmt.setValue(String.valueOf(uniqueInstallmentAmount));
			uniquePremiumAmt.setEnabled(false);
			uniquePremiumAmt.setNullRepresentation("0");
			
//			amountToHospAftPremium = (TextField) binder.buildAndBind("Pre-auth Downsized Amt - Hospital","amountToHospAftPremium",TextField.class);
			amountToHospAftPremium = (TextField) binder.buildAndBind("Revised Amount - Hospital","amountToHospAftPremium",TextField.class);
			
			Double aftPremAmt = SHAUtils.getDoubleFromStringWithComma(downsizedAmtField.getValue()) - SHAUtils.getDoubleFromStringWithComma(uniquePremiumAmt.getValue());
			amountToHospAftPremium.setValue(String.valueOf(aftPremAmt < 0d ? 0l : aftPremAmt.longValue()) );
			amountToHospAftPremium.setEnabled(false);
			amountToHospAftPremium.setNullRepresentation("0");
			
		}
		
		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
				bean.getPreauthDataExtractionDetails().getOtherBenfitFlag() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getOtherBenfitFlag())){
			benefitAmtTxt = new TextField();
			benefitAmtTxt.setValue(String.valueOf(bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount().longValue()));
			benefitAmtTxt.setEnabled(false);
			benefitAmtTxt.setNullRepresentation("0");
			viewOtherBenefitAmtLink = new Button("View Other Benefit");
			viewOtherBenefitAmtLink.setStyleName(ValoTheme.BUTTON_LINK);
			viewOtherBenefitAmtLink.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(PreauthEnhancemetWizardPresenter.VIEW_PREAUTH_ENH_OTHER_BENEFITS,null);					
				}
			});
		}	
		//added for CR2019184
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			totalApprvedAmtField.setValue(this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction() != null ? this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction().toString() : null);
			policyInstPremiumAmt = (TextField) binder.buildAndBind("Policy Installment Premium","policyInstPremiumAmt",TextField.class);
			policyInstPremiumAmt.setNullRepresentation("0");
			policyInstPremiumAmt.setValue(String.valueOf(bean.getPolicyInstalmentPremiumAmt().longValue()));
			policyInstPremiumAmt.setEnabled(false);
			
			amountToHospAftPremium = (TextField) binder.buildAndBind("Revised Amount - Hospital","amountToHospAftPremium",TextField.class);
			Double aftPremAmt = SHAUtils.getDoubleFromStringWithComma(downsizedAmtField.getValue()) - SHAUtils.getDoubleFromStringWithComma(policyInstPremiumAmt.getValue());
			amountToHospAftPremium.setValue(String.valueOf(aftPremAmt < 0d ? 0l : aftPremAmt.longValue()) );
			amountToHospAftPremium.setEnabled(false);
			amountToHospAftPremium.setNullRepresentation("0");

		}else{
			if(policyInstPremiumAmt != null){
				policyInstPremiumAmt.setVisible(false);
			}
			if(amountToHospAftPremium != null){
				amountToHospAftPremium.setVisible(false);
			}
		}
		
//		cmbDownsizeReason = (ComboBox) binder.buildAndBind("Downsize Reason ", "downSizeReason", ComboBox.class);
		cmbDownsizeReason = (ComboBox) binder.buildAndBind("Reason for Downward Revision", "downSizeReason", ComboBox.class);
		
		cmbDownsizeReason.setContainerDataSource((BeanItemContainer<SelectValue>)downSizeReasonContainer);
		cmbDownsizeReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDownsizeReason.setItemCaptionPropertyId("value");
		
		cmbDownsizeReason.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if (value != null) {
					fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_DOWNSIZE_LAYOUT,value.getId(),SHAConstants.ENHANCEMENT_DOWNSIZE);
				}
				
			}
		});

		if(bean.getPreauthMedicalDecisionDetails().getDownSizeReason() != null) {
			cmbDownsizeReason.setValue(bean.getPreauthMedicalDecisionDetails().getDownSizeReason());
		}
		
//		downsizeRemarks = (TextArea) binder.buildAndBind("Downsize Hospital Remarks ", "downsizeRemarks", TextArea.class);
		downsizeRemarks = (TextArea) binder.buildAndBind("Revision Hospital Remarks", "downsizeRemarks", TextArea.class);
		downsizeRemarks.setMaxLength(4000);
		downsizeRemarks.setId("hospDwnSizeRmrks");
		downsizeRemarks.setWidth("50%");
		downsizeRemarks.setHeight("200px");
		downsizeRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(downsizeRemarks,null);
		
//		downsizeInsuredRemarks = (TextArea) binder.buildAndBind("Downsize Insuerd Remarks ", "downsizeInsuredRemarks", TextArea.class);
		downsizeInsuredRemarks = (TextArea) binder.buildAndBind("Revision Insured Remarks", "downsizeInsuredRemarks", TextArea.class);
		downsizeInsuredRemarks.setMaxLength(4000);
		downsizeInsuredRemarks.setId("insDwnSizeRmrks");
		downsizeInsuredRemarks.setWidth("50%");
		downsizeInsuredRemarks.setHeight("200px");
		downsizeInsuredRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(downsizeInsuredRemarks,null);
		
		
		
		negotiationOpinionTaken = (OptionGroup) binder.buildAndBind(
				"Do you want to Refer for Settlement Negotiation", "negotiationDecisionTaken",
				OptionGroup.class);
		negotiationOpinionTaken.addItems(getReadioButtonOptions());
		negotiationOpinionTaken.setItemCaption(true, "Yes");
		negotiationOpinionTaken.setItemCaption(false, "No");
		negotiationOpinionTaken.setStyleName("horizontal");
	//	negotiationOpinionTaken.select(false);
		negotiationOpinionTaken.setVisible(false);
		
		bean.getPreauthMedicalDecisionDetails().setNegotiationDecisionTaken(null);
		bean.getPreauthMedicalDecisionDetails().setNegotiatePoints(null);
		
		fireViewEvent(PreauthEnhancemetWizardPresenter.NEGOTIATION_PENDING, bean);
		addNegotiationListener();
		
		//negotiationLayout.addComponent(negotiationOpinionTaken);
	    //	negotiationLayout.setMargin(false);
		//negotiationLayout.setSpacing(false);	
		if(null != bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken() && bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken()){
			genertateFieldsBasedOnNegotiation(true);
		}
		
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			dynamicFrmLayout.addComponents(negotiationOpinionTaken, totalApprvedAmtField, downsizedAmtField, policyInstPremiumAmt, amountToHospAftPremium, cmbDownsizeReason);
		}
		else if(uniquePremiumAmt != null && amountToHospAftPremium != null) {
			dynamicFrmLayout.addComponents(negotiationOpinionTaken, totalApprvedAmtField, downsizedAmtField, uniquePremiumAmt, amountToHospAftPremium, cmbDownsizeReason);
		} else {
			
			if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) &&
					bean.getPreauthDataExtractionDetails().getOtherBenfitFlag() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getOtherBenfitFlag())){
			
				HorizontalLayout benefitLayout = new HorizontalLayout(benefitAmtTxt,viewOtherBenefitAmtLink);
				benefitLayout.setSpacing(true);
			
				benefitLayout.setCaption("Other Benefits Approved Amt");
				TextField totalApprovedAmtTxt = new TextField("Total Amount");
				totalApprovedAmtTxt.setNullRepresentation("");
				
				Double totalApprovedAmt = amt.doubleValue();
				totalApprovedAmt += bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount();
				totalApprovedAmtTxt.setValue(String.valueOf(totalApprovedAmt.longValue()));				
				totalApprovedAmtTxt.setEnabled(false);
				
				dynamicFrmLayout.addComponents(negotiationOpinionTaken, totalApprvedAmtField, downsizedAmtField, benefitLayout, totalApprovedAmtTxt, cmbDownsizeReason);
			}else{
				dynamicFrmLayout.addComponents(negotiationOpinionTaken, totalApprvedAmtField, downsizedAmtField, cmbDownsizeReason);
			}
		}
		
		if(null != bean.getIsNegotiationApplicable() && bean.getIsNegotiationApplicable()){
			if(bean.getIsNegotiationPending()){
				negotiationOpinionTaken.setEnabled(false);
				negotiationOpinionTaken.setValue(true);
				negotiationOpinionTaken.setVisible(true);
			}
			else
			{
				negotiationOpinionTaken.setVisible(true);
				negotiationOpinionTaken.setEnabled(true);
			}
			if(null != bean.getPreauthMedicalDecisionDetails().getIsNegotiationCancelled() && bean.getPreauthMedicalDecisionDetails().getIsNegotiationCancelled()){
				negotiationOpinionTaken.setVisible(false);
			}
		}
		//R1295
		bean.setQueryType(null);
		bean.setQueryCount(0);
		userLayout = buildUserRoleLayout();		
		userLayout.setMargin(Boolean.TRUE);
	
		remarksLayout = new HorizontalLayout(new FormLayout(downsizeRemarks));
		remarksLayout.setSpacing(Boolean.TRUE);
		remarksLayout.setMargin(Boolean.TRUE);
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null &&
				  !ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			remarksLayout.addComponent(new FormLayout(downsizeInsuredRemarks));
		}
		
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(dynamicFrmLayout,userLayout);
		hLayout.setHeight("100%");
		
		bonusVLayout.addComponent(hLayout);
		
//		VerticalLayout dummyLayout = new VerticalLayout();
//		dummyLayout.setHeight("100%");
//
//		VerticalLayout verLayout = new VerticalLayout(hLayout,dummyLayout);
		
		wholeVlayout.addComponents(bonusVLayout,remarksLayout);
		//wholeVlayout.setSpacing(true);
		
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(downsizedAmtField);
		mandatoryFields.add(cmbDownsizeReason);
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null &&
				  !ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
		//  Not Mandatory for Retail Products.	
			mandatoryFields.remove(downsizeRemarks);
			mandatoryFields.remove(downsizeInsuredRemarks);
		}
		else {
			mandatoryFields.add(downsizeRemarks);
			mandatoryFields.add(downsizeInsuredRemarks);
		}	
		
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
		
	}
	
	public void buildQueryLayout()
	{
		intimation.setKey(this.bean.getIntimationKey());
		final List<PreAuthPreviousQueryDetailsTableDTO> PreAuthPreviousQueryDetailsTableDTO = preAuthPreviousQueryDetailsService.search(intimation);
		if(!PreAuthPreviousQueryDetailsTableDTO.isEmpty()) {
			this.bean.setLastQueryRaiseDate(PreAuthPreviousQueryDetailsTableDTO.get(PreAuthPreviousQueryDetailsTableDTO.size()-1).getQueryRaisedDate());
			alertMessage(SHAConstants.QUERY_RAISE_MESSAGE, PreAuthPreviousQueryDetailsTableDTO);
		} else {
			generateQueryDetails(PreAuthPreviousQueryDetailsTableDTO);
		}
		
	}
	
	public Boolean alertMessage(String message, final List<PreAuthPreviousQueryDetailsTableDTO> preauthQueryDetailsDto) {/*
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
//   		final Boolean isClicked = false;
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
				generateQueryDetails(preauthQueryDetailsDto);
			}
		});
		return true;
	*/
		
		final MessageBox showAlert = showAlertMessageBox(message);
		Button homeButton = showAlert.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showAlert.close();
				generateQueryDetails(preauthQueryDetailsDto);
			}
		});
		return true;
		
	}

	private void generateQueryDetails(
			final List<PreAuthPreviousQueryDetailsTableDTO> PreAuthPreviousQueryDetailsTableDTO) {
		
		/*if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			if(bean.getQueryType() == null || (bean.getQueryType() != null && ReferenceTable.CLS_QUERY_TYPE_NEW_QUERY_KEY.equals(bean.getQueryType().getId()))){
				bean.getPreauthMedicalDecisionDetails().setQueryRemarks("Your request for enhancement of amount is being examined by us. Meantime, please clarify / send us the following: \n 1. \n 2. \n 3. \n");
			}	
			else if(bean.getQueryType() != null && ReferenceTable.CLS_QUERY_TYPE_REMINDER_KEY.equals(bean.getQueryType().getId())){
				bean.getPreauthMedicalDecisionDetails().setQueryRemarks("We have not yet received the required documents/details from you. \n");
			}
		}*/
		
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		unbindAndRemoveComponents(userLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);
		
		preAuthPreviousQueryDetailsTable.init("Previous Query Details", false, false);
		intimation.setKey(this.bean.getIntimationKey());
		preAuthPreviousQueryDetailsTable.setTableList(PreAuthPreviousQueryDetailsTableDTO);	
		//R1295
		cmbQueryType = (ComboBox) binder.buildAndBind("Query Type","queryType",ComboBox.class);
		cmbQueryType.setContainerDataSource(masterService.getOpinionQueryType());
		cmbQueryType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbQueryType.setItemCaptionPropertyId("value");
		cmbQueryType.setWidth("200px");
		if(bean.getQueryType() != null){
			cmbQueryType.setValue(bean.getQueryType());
		}
		final int queryCount = PreAuthPreviousQueryDetailsTableDTO.size();
		final ArrayList<PreAuthPreviousQueryDetailsTableDTO> newArrayList = new ArrayList<>(PreAuthPreviousQueryDetailsTableDTO);
		cmbQueryType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -486851813151743902L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null){
					bean.setQueryType(value);
					bean.setQueryCount(queryCount);
					
					unbindAndRemoveComponents(dynamicFrmLayout);
					unbindAndRemoveComponents(userLayout);
					
					if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
						fireViewEvent(PreauthEnhancemetWizardPresenter.GET_ENHANCEMENT_QUERY_REMARKS, value.getId());
					}
					
					generateQueryDetails(newArrayList);
				}else{
					bean.setQueryType(null);
					bean.setQueryCount(queryCount);
					
					unbindAndRemoveComponents(dynamicFrmLayout);
					unbindAndRemoveComponents(userLayout);
					generateQueryDetails(newArrayList);
				}
			}
		});
		
		txtQueryCount = new TextField("Query Count");
		txtQueryCount.setValue(queryCount+"");
		txtQueryCount.setReadOnly(true);
		txtQueryCount.setEnabled(false);
		
		if(queryCount>0) {
			bean.setQueryCount(queryCount);
			this.bean.setLastQueryRaiseDate(PreAuthPreviousQueryDetailsTableDTO.get(PreAuthPreviousQueryDetailsTableDTO.size()-1).getQueryRaisedDate());
		}
		
		queryRemarksTxta = (TextArea) binder.buildAndBind("Query Remarks","queryRemarks",TextArea.class);
		queryRemarksTxta.setMaxLength(4000);
		queryRemarksTxta.setWidth("400px");
		addingSentToCPUFields();		
		dynamicFrmLayout.addComponents(cmbQueryType,txtQueryCount,queryRemarksTxta);
		bean.getPreauthMedicalDecisionDetails().setUserClickAction("Query");
		bean.setIsNegotiationApplicable(Boolean.FALSE);
		userLayout = buildUserRoleLayout();
		
//		HorizontalLayout hLayout = new HorizontalLayout();
//		hLayout.addComponents(dynamicFrmLayout,userLayout);

		remarksLayout.addComponents(dynamicFrmLayout,userLayout);
		
		bonusVLayout.addComponent(remarksLayout);

		alignFormComponents();
//		VerticalLayout vTblLayout = new VerticalLayout(preAuthPreviousQueryDetailsTable,hLayout);
		
//		wholeVlayout.addComponent(vTblLayout);
		wholeVlayout.addComponents(preAuthPreviousQueryDetailsTable,bonusVLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(cmbQueryType);
		mandatoryFields.add(queryRemarksTxta);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
	}
	
	public void buildRejectLayout(Object rejectionCategoryDropdownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		unbindAndRemoveComponents(userLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);
		
		addingSentToCPUFields();
		rejectionCategoryCmb = (ComboBox) binder.buildAndBind("Rejection Category","rejectionCategory",ComboBox.class);
		
		BeanItemContainer<SelectValue> dropDownValues = (BeanItemContainer<SelectValue>)rejectionCategoryDropdownValues;
		
		BeanItemContainer<SelectValue> filterValues = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
		
		if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
			List<SelectValue> itemIds = dropDownValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_SIVER_REJECT)){
					filterValues.addBean(selectValue);
				}
			}
		}else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")){
			List<SelectValue> itemIds = dropDownValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_GOLD_REJECT)){
					filterValues.addBean(selectValue);
				}
			}
		}else{
			List<SelectValue> itemIds = dropDownValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_GOLD_REJECT)
						&& ! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_SIVER_REJECT)
						&& ! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_GOLD_REJECT_RTL)){
					filterValues.addBean(selectValue);
				}				
			}
		}
		
		if(product != null && ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(product.getKey())){
			BeanItemContainer<SelectValue> rejcategSelectContainer = masterService.getMasterValueByCode(SHAConstants.STAR_CANCER_REJ_CATGY);
					filterValues.addAll(rejcategSelectContainer.getItemIds());
		}
		
		rejectionCategoryCmb.setContainerDataSource(filterValues);
		rejectionCategoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		rejectionCategoryCmb.setItemCaptionPropertyId("value");
		
		rejectionCategoryCmb.setWidth("370px");

		
		/*rejSubCategCmb = (ComboBox) binder.buildAndBind("Rejection Subcategory","rejSubCategory",ComboBox.class);
		rejSubCategCmb.setWidth("370px");*/
		
		addRejectionListener();
		

		addRejectionListener();
		
		if(bean.getPreauthMedicalDecisionDetails().getRejectionCategory() != null){
			
			rejectionCategoryCmb.setValue(bean.getPreauthMedicalDecisionDetails().getRejectionCategory());
		}	
		
		hospRejectionRemarksTxta = (TextArea) binder.buildAndBind("Rejection Remarks For Hospital","rejectionRemarks",TextArea.class);
		hospRejectionRemarksTxta.setMaxLength(4000);
		hospRejectionRemarksTxta.setId("hospRejRmrks");
		hospRejectionRemarksTxta.setWidth("50%");
		hospRejectionRemarksTxta.setHeight("200px");
		hospRejectionRemarksTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(hospRejectionRemarksTxta,null);
		
		if(bean.getPreauthMedicalDecisionDetails().getRejectionCategory() != null){
			hospRejectionRemarksTxta.setValue(bean.getPreauthMedicalDecisionDetails().getRejectionRemarks());
		}
		
		rejRemarksForInsuredTxta = (TextArea) binder.buildAndBind("Remarks For Insured","remarksForInsured",TextArea.class);
		rejRemarksForInsuredTxta.setMaxLength(4000);
		rejRemarksForInsuredTxta.setWidth("50%");		
		rejRemarksForInsuredTxta.setHeight("200px");
		rejRemarksForInsuredTxta.setId("insuredRejRmrks");
		rejRemarksForInsuredTxta.setData(bean);
		rejRemarksForInsuredTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(rejRemarksForInsuredTxta,null);
		
		if(bean.getPreauthMedicalDecisionDetails().getRemarksForInsured() != null){
			rejRemarksForInsuredTxta.setValue(bean.getPreauthMedicalDecisionDetails().getRemarksForInsured());
		}
		
		definedLimtAmtTxt = (TextField) binder.buildAndBind("Amount to be considered for Defined limt","definedLimitAmnt",TextField.class);
		definedLimtAmtTxt.setMaxLength(4000);
		/*definedLimtAmtTxt.setWidth("900px");		
		definedLimtAmtTxt.setHeight("100px");*/
		CSValidator txtAckValidator = new CSValidator();
		txtAckValidator.setRegExp("^[0-9']*$");
		txtAckValidator.setPreventInvalidTyping(true);
		txtAckValidator.extend(definedLimtAmtTxt);

		dynamicFrmLayout.addComponent(rejectionCategoryCmb);
//		dynamicFrmLayout.addComponent(rejSubCategCmb);
		dynamicFrmLayout.addComponent(definedLimtAmtTxt);
		
//		dynamicFrmLayout.addComponent(rejectionRemarksTxta);
//		dynamicFrmLayout.addComponent(sentToCPU);
		

		//R1256
		VerticalLayout containterLayout = new VerticalLayout();
		HorizontalLayout temp1 = new HorizontalLayout(new FormLayout(hospRejectionRemarksTxta),new FormLayout(rejRemarksForInsuredTxta));
		containterLayout.addComponent(temp1);
		
		HorizontalLayout evidenceHolderlayout = buildEvidenceObtainedFrom();
		evidenceHolderlayout.setMargin(false);
		FormLayout evidenceFL = new FormLayout(evidenceHolderlayout);
		evidenceFL.setCaption("<b style='font-size: 14.5px;'>Evidence Obtained From<b style='color:red; font-size: 12.5px; font-weight:600;'> * </b> </b>"); //b>Search By<b style='color:red; font-size: 12.5px; font-weight:600;'> * </b>
		evidenceFL.setCaptionAsHtml(true);
		containterLayout.addComponent(evidenceFL);
		
		remarksLayout.addComponents(containterLayout);
//		remarksLayout.addComponents(new FormLayout(hospRejectionRemarksTxta),new FormLayout(rejRemarksForInsuredTxta));
		remarksLayout.setMargin(Boolean.TRUE);		
		
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponents(dynamicFrmLayout,remarksLayout);
		vLayout.setMargin(Boolean.FALSE);
		bean.getPreauthMedicalDecisionDetails().setUserClickAction("Reject");
		bean.setIsNegotiationApplicable(Boolean.FALSE);
		//R1295
		bean.setQueryType(null);
		bean.setQueryCount(0);
		userLayout = buildUserRoleLayout();
		userLayout.setMargin(Boolean.TRUE);
		
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(dynamicFrmLayout,userLayout);
		
		bonusVLayout.addComponent(hLayout);
		VerticalLayout verLayout = new VerticalLayout(bonusVLayout,remarksLayout);		
		wholeVlayout.addComponents(bonusVLayout,remarksLayout);
		
		alignFormComponents();
		
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(rejectionCategoryCmb);
		
		if(bean.getPreauthMedicalDecisionDetails().getRejectionCategory() != null){
		
			rejectionCategoryCmb.setValue(bean.getPreauthMedicalDecisionDetails().getRejectionCategory());
		}
		

		mandatoryFields.add(definedLimtAmtTxt);
		mandatoryFields.add(hospRejectionRemarksTxta);
		showOrHideValidation(false);
		
//		approveBtn.setWidth("100%");
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
		
	}
	

	@SuppressWarnings("unchecked")
	public void buildWithdrawLayout(Object withdrawDropdownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);
		
		addingSentToCPUFields();
		cmbWithdraw = (ComboBox) binder.buildAndBind("Reason for Withdrawal","withdrawReason",ComboBox.class);
		cmbWithdraw.setContainerDataSource((BeanItemContainer<SelectValue>)withdrawDropdownValues);
		cmbWithdraw.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbWithdraw.setItemCaptionPropertyId("value");
		
		cmbWithdraw.setWidth("300px");
		
		txtWithdraw = (TextArea) binder.buildAndBind("Withdrawal Remarks For Hospital", "withdrawRemarks", TextArea.class);
		txtWithdraw.setData(bean);
		txtWithdraw.setId("hospitalWithdrawRmrks");
		txtWithdraw.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(txtWithdraw,null);
		txtWithdraw.setMaxLength(4000);
		txtWithdraw.setWidth("50%");
		txtWithdraw.setHeight("200px");
		
		
		if(bean.getPreauthMedicalDecisionDetails().getWithdrawReason() != null){
			cmbWithdraw.setValue(bean.getPreauthMedicalDecisionDetails().getWithdrawReason());
		}
		
		if(bean.getPreauthMedicalDecisionDetails().getWithdrawRemarks() != null){
			txtWithdraw.setValue(bean.getPreauthMedicalDecisionDetails().getWithdrawRemarks());
		}
		
		withDrawRemarksForInsuredTxta = (TextArea) binder.buildAndBind("Remarks For Insured","withDrawRemarksForInsured",TextArea.class);
		withDrawRemarksForInsuredTxta.setMaxLength(4000);
		withDrawRemarksForInsuredTxta.setWidth("50%");		
		withDrawRemarksForInsuredTxta.setHeight("200px");
		withDrawRemarksForInsuredTxta.setId("insuredWithdrawRmrks");
		withDrawRemarksForInsuredTxta.setData(bean);
		withDrawRemarksForInsuredTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(withDrawRemarksForInsuredTxta,null);
		
		if(bean.getPreauthMedicalDecisionDetails().getWithDrawRemarksForInsured() != null){
			withDrawRemarksForInsuredTxta.setValue(bean.getPreauthMedicalDecisionDetails().getWithDrawRemarksForInsured());
		}
		addWithDrawListenerListener();
		
		dynamicFrmLayout.addComponent(cmbWithdraw);
		
//		dynamicFrmLayout.addComponent(txtWithdraw);
//		dynamicFrmLayout.addComponent(sentToCPU);
		
		remarksLayout.addComponents(new FormLayout(txtWithdraw),new FormLayout(withDrawRemarksForInsuredTxta));
		
		remarksLayout.setMargin(Boolean.TRUE);
		
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponents(dynamicFrmLayout,remarksLayout);
		vLayout.setMargin(Boolean.FALSE);
		//R1295
		bean.setQueryType(null);
		bean.setQueryCount(0);
		
		userLayout = buildUserRoleLayout();
		userLayout.setMargin(Boolean.TRUE);
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(dynamicFrmLayout,userLayout);
		
		bonusVLayout.addComponent(hLayout);
		alignFormComponents();
		bean.setIsNegotiationApplicable(Boolean.FALSE);
		VerticalLayout verLayout = new VerticalLayout(bonusVLayout,remarksLayout);
		
		wholeVlayout.addComponents(bonusVLayout,remarksLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(cmbWithdraw);
		mandatoryFields.add(txtWithdraw);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
	}
	
	
	@SuppressWarnings("unchecked")
	public void buildWithdrawAndRejectLayout(Object withdrawDropdownValues, Object rejectionDropDownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);
		
		addingSentToCPUFields();
		
		// WITHDRAW FIELDS
		
		cmbWithdraw = (ComboBox) binder.buildAndBind("Reason for Withdrawal","withdrawReason",ComboBox.class);
		cmbWithdraw.setContainerDataSource((BeanItemContainer<SelectValue>)withdrawDropdownValues);
		cmbWithdraw.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbWithdraw.setItemCaptionPropertyId("value");
		
		//cmbWithdraw.setWidth("300px");
		
		addWithDrawListenerListener();
		
		txtWithdraw = (TextArea) binder.buildAndBind("Withdrawal Remarks For Hospital", "withdrawRemarks", TextArea.class);
		txtWithdraw.setData(bean);
		txtWithdraw.setMaxLength(4000);
		txtWithdraw.setWidth("50%");
		txtWithdraw.setHeight("250px");
		txtWithdraw.setId("hospitalWithdrawRmrks");
		txtWithdraw.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(txtWithdraw,null);
		
		if(bean.getPreauthMedicalDecisionDetails().getWithdrawReason() != null){
			cmbWithdraw.setValue(bean.getPreauthMedicalDecisionDetails().getWithdrawReason());
		}
		if(bean.getPreauthMedicalDecisionDetails().getWithdrawRemarks() != null){
			txtWithdraw.setValue(bean.getPreauthMedicalDecisionDetails().getWithdrawRemarks());
		}
		
		withDrawRemarksForInsuredTxta = (TextArea) binder.buildAndBind("Remarks For Insured","withDrawRemarksForInsured",TextArea.class);
		withDrawRemarksForInsuredTxta.setMaxLength(4000);
		withDrawRemarksForInsuredTxta.setWidth("50%");		
		withDrawRemarksForInsuredTxta.setHeight("200px");
		withDrawRemarksForInsuredTxta.setId("insuredWithdrawRmrks");
		withDrawRemarksForInsuredTxta.setData(bean);
		withDrawRemarksForInsuredTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(withDrawRemarksForInsuredTxta,null);
		
		if(bean.getPreauthMedicalDecisionDetails().getWithDrawRemarksForInsured() != null){
			withDrawRemarksForInsuredTxta.setValue(bean.getPreauthMedicalDecisionDetails().getWithDrawRemarksForInsured());
		}
				
		FormLayout withdrawFieldsLayout = new FormLayout();
		mandatoryFields= new ArrayList<Component>();
		
		withdrawFieldsLayout.addComponent(cmbWithdraw);
		withdrawFieldsLayout.addComponent(txtWithdraw);
		withdrawFieldsLayout.addComponent(withDrawRemarksForInsuredTxta);
		withdrawFieldsLayout.setMargin(Boolean.FALSE);
//		dynamicFrmLayout.addComponent(sentToCPU);
//		alignFormComponents();
//		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(cmbWithdraw);
		mandatoryFields.add(txtWithdraw);
//		showOrHideValidation(false);
		
		// REJECTION FIELDS.................
		rejectionCategoryCmb = (ComboBox) binder.buildAndBind("Rejection Category","rejectionCategory",ComboBox.class);
		
		BeanItemContainer<SelectValue> dropDownValues = (BeanItemContainer<SelectValue>)rejectionDropDownValues;
		BeanItemContainer<SelectValue> filterValues = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
		
		if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
			List<SelectValue> itemIds = dropDownValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_SIVER_REJECT)){
					filterValues.addBean(selectValue);
				}
			}
		}else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")){
			List<SelectValue> itemIds = dropDownValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_GOLD_REJECT)){
					filterValues.addBean(selectValue);
				}
			}
		}else{
			List<SelectValue> itemIds = dropDownValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_GOLD_REJECT) 
						&& ! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_SIVER_REJECT)
						&& ! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_GOLD_REJECT_RTL)){
					filterValues.addBean(selectValue);
				}				
			}
		}
		
		
		rejectionCategoryCmb.setContainerDataSource((BeanItemContainer<SelectValue>)filterValues);
		rejectionCategoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		rejectionCategoryCmb.setItemCaptionPropertyId("value");
		//rejectionCategoryCmb.setWidth("300px");
		
		
		rejSubCategCmb = (ComboBox) binder.buildAndBind("Rejection Subcategory","rejSubCategory",ComboBox.class);
//		rejSubCategCmb.setWidth("370px");
		rejSubCategCmb.setVisible(false);
		
		
		addRejectionListener();
		
		if(bean.getPreauthMedicalDecisionDetails().getRejectionCategory() != null){
			
			if(ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION.equals(bean.getPreauthMedicalDecisionDetails().getRejectionCategory().getId())
					|| ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_ENH.equals(bean.getPreauthMedicalDecisionDetails().getRejectionCategory().getId())) {
				
				rejSubCategCmb.setVisible(true);
			}
			else {
				rejSubCategCmb.setVisible(false);
				mandatoryFields.remove(rejSubCategCmb);
			}
			
			rejectionCategoryCmb.setValue(bean.getPreauthMedicalDecisionDetails().getRejectionCategory());
		}
		
		if(bean.getPreauthMedicalDecisionDetails().getRejSubCategory() != null){
			
			rejSubCategCmb.setValue(bean.getPreauthMedicalDecisionDetails().getRejSubCategory());
		}
		
		hospRejectionRemarksTxta = (TextArea) binder.buildAndBind("Rejection Remarks For Hospital","rejectionRemarks",TextArea.class);
		hospRejectionRemarksTxta.setData(bean);
		hospRejectionRemarksTxta.setMaxLength(4000);
		hospRejectionRemarksTxta.setWidth("50%");
		hospRejectionRemarksTxta.setHeight("250px");
		hospRejectionRemarksTxta.setId("insuredRejRmrks");
		hospRejectionRemarksTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(hospRejectionRemarksTxta,null);
		
		if(bean.getPreauthMedicalDecisionDetails().getRejectionRemarks() != null){
			hospRejectionRemarksTxta.setValue(bean.getPreauthMedicalDecisionDetails().getRejectionRemarks());
		}

		
		rejRemarksForInsuredTxta = (TextArea) binder.buildAndBind("Remarks For Insured","remarksForInsured",TextArea.class);
		rejRemarksForInsuredTxta.setData(bean);
		rejRemarksForInsuredTxta.setMaxLength(4000);
		rejRemarksForInsuredTxta.setWidth("50%");		
		rejRemarksForInsuredTxta.setHeight("200px");
		rejRemarksForInsuredTxta.setId("insuredWithdrawRmrks");
		rejRemarksForInsuredTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(rejRemarksForInsuredTxta,null);
		
		if(bean.getPreauthMedicalDecisionDetails().getRemarksForInsured() != null){

			rejRemarksForInsuredTxta.setValue(bean.getPreauthMedicalDecisionDetails().getRemarksForInsured());
		}

		FormLayout rejectionFieldsLayout = new FormLayout();
		
		rejectionFieldsLayout.addComponent(rejectionCategoryCmb);
		rejectionFieldsLayout.addComponent(rejSubCategCmb);
		rejectionFieldsLayout.addComponent(hospRejectionRemarksTxta);
		rejectionFieldsLayout.addComponent(rejRemarksForInsuredTxta);
		rejectionFieldsLayout.setMargin(Boolean.FALSE);
//		dynamicFrmLayout.addComponent(sentToCPU);
//		alignFormComponents();
//		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields.add(rejectionCategoryCmb);
		mandatoryFields.add(hospRejectionRemarksTxta);
		mandatoryFields.add(cmbWithdraw);
		mandatoryFields.add(txtWithdraw);
		showOrHideValidation(false);
		
		//R1295
		bean.setQueryType(null);
		bean.setQueryCount(0);
		userLayout = buildUserRoleLayout();
		remarksFromDeptHead.setWidth("270px");
		userLayout.setMargin(Boolean.TRUE);
		bean.setIsNegotiationApplicable(Boolean.FALSE);
		
		HorizontalLayout layout = new HorizontalLayout(withdrawFieldsLayout, rejectionFieldsLayout,userLayout);
		layout.setSpacing(true);
		layout.setWidth("100%");
		layout.setMargin(Boolean.FALSE);
		dynamicFrmLayout.addComponent(layout);
		wholeVlayout.addComponent(dynamicFrmLayout);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
	}
	
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	public void buildEscalateLayout(Object escalateToValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);
		
       final BeanItemContainer<SelectValue> masterValueByReference;
		if (null != bean.getPreauthDataExtractionDetails().getNatureOfTreatment()){
			masterValueByReference = masterService.getMasterValueByReferenceForNonAllopathic((ReferenceTable.SPECIALIST_TYPE),this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue());
		}else{
			masterValueByReference = masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE));
		}
		
		
		
//		cmbSpecialistType = (ComboBox) binder.buildAndBind("Specialist Type","specialistValue",ComboBox.class);
//		cmbSpecialistType.setContainerDataSource(masterValueByReference);
//		cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cmbSpecialistType.setItemCaptionPropertyId("value");
	
		escalateToCmb = (ComboBox) binder.buildAndBind("Escalate To","escalateTo",ComboBox.class);
		
		
		BeanItemContainer<SelectValue> escalateValue1 = (BeanItemContainer<SelectValue>) escalateToValues;
		List<SelectValue> escalateValues = escalateValue1.getItemIds();
		
		BeanItemContainer<SelectValue> escalateContainer2 = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		for (SelectValue selectValue : escalateValues) {
			if(selectValue.getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
			escalateContainer2.addBean(selectValue);
			}	
		}
		
		if(bean.getDownSizePreauthDataExtrationDetails().getCMA5()){
			for(SelectValue selectValue : escalateValues){
				if(selectValue.getId() > (ReferenceTable.CMA5)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtrationDetails().getCMA4()){
			
			for(SelectValue selectValue : escalateValues){
				if(selectValue.getId() > (ReferenceTable.CMA4)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtrationDetails().getCMA3()){
			for(SelectValue selectValue : escalateValues){
				if(selectValue.getId() > (ReferenceTable.CMA3)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtractionDetails().getCMA2()){
			for(SelectValue selectValue : escalateValues){
				if(selectValue.getId() > (ReferenceTable.CMA2)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtractionDetails().getCMA1()){
			for(SelectValue selectValue : escalateValues){
				if(selectValue.getId() > (ReferenceTable.CMA1)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}
		
		
		
		escalateToCmb.setContainerDataSource(escalateContainer2);
		escalateToCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		escalateToCmb.setItemCaptionPropertyId("value");
		
		escalateToCmb.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				
				if(value != null && value.getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
					
					unbindField(cmbSpecialistType);
					cmbSpecialistType = (ComboBox) binder.buildAndBind("Specialist Type","specialistValue",ComboBox.class);
					cmbSpecialistType.setContainerDataSource(masterValueByReference);
					cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbSpecialistType.setItemCaptionPropertyId("value");
					cmbSpecialistType.setVisible(true);
					
					dynamicFrmLayout.addComponent(cmbSpecialistType,1);
					mandatoryFields.add(cmbSpecialistType);
					
					showOrHideValidation(false);
					
				}else{
					unbindField(cmbSpecialistType);
					if(cmbSpecialistType != null){
						
						dynamicFrmLayout.removeComponent(cmbSpecialistType);
						mandatoryFields.remove(cmbSpecialistType);
						cmbSpecialistType.setVisible(false);
						
					}
					
				}
			}
		});
		
//		uploadFile = (Upload) binder.buildAndBind("File UpLoad","uploadFile",Upload.class);
		uploadFile  = new Upload("", new Receiver() {
			
			private static final long serialVersionUID = 4775959511314943621L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				// Create upload stream
		        FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
		        	if(filename != null && ! filename.equalsIgnoreCase("")){
				            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
				            fos = new FileOutputStream(file);
		        	}
		        	else{
		        		  
		        
		        	}
//		        	if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
//		        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, this.key,filename);
//		        	}
		        } catch (final java.io.FileNotFoundException e) {
		            new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
		                .show(com.vaadin.server.Page.getCurrent());
		            return null;
		        }
		        return fos; // Return the output stream to write to
			}
		});	
		uploadFile.addSucceededListener(new SucceededListener() {
			
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				System.out.println("File uploaded" + event.getFilename());
				
				try{
					
					byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
					
					if(null != fileAsbyteArray )
					{
						
						Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
//						boolean hasSpecialChar = p.matcher(event.getFilename()).find();
					//	if(hasSpecialChar)
						//{
						WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
							Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
							//TO read file after load
							if (flagUploadSuccess.booleanValue())
							{
								String token = "" + uploadStatus.get("fileKey");
								String fileName = event.getFilename();
							    bean.setTokenName(token);
							    bean.setFileName(fileName);
							    buildSuccessLayout();
							    uploadStatus = null;
//							    thisObj.close();
							}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		uploadFile.setCaption(null);
		uploadFile.setButtonCaption(null);
		
		Button uploadBtn = new Button("Upload");
		HorizontalLayout uploadHor = new HorizontalLayout(uploadFile,uploadBtn);
		uploadHor.setSpacing(true);
		
		uploadBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				uploadFile.submitUpload();
			}
		});
		
		escalationRemarksTxta = (TextArea) binder.buildAndBind("Escalate Remarks","escalationRemarks",TextArea.class);
		escalationRemarksTxta.setMaxLength(100);
		escalationRemarksTxta.setWidth("400px");
		dynamicFrmLayout.addComponent(escalateToCmb);
//		dynamicFrmLayout.addComponent(cmbSpecialistType);
		dynamicFrmLayout.addComponent(uploadHor);
		dynamicFrmLayout.addComponent(escalationRemarksTxta);
//		cmbSpecialistType.setVisible(false);
	
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(escalationRemarksTxta);
		mandatoryFields.add(escalateToCmb);
		showOrHideValidation(false);
		
		
		wizard.getFinishButton().setEnabled(true);
		wizard.getNextButton().setEnabled(false);
		
	}
	public void buildSuccessLayout() {/*
		Label successLabel = new Label(
				"<b style = 'color: green;'> File Uploaded Successfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Ok");
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
		dialog.setCaption("");
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
				//popup.close();
				//if(null != popup)
					
			}
		});
	*/

		final MessageBox showInfo= showInfoMessageBox("File Uploaded Successfully.");
		Button homeButton = showInfo.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfo.close();
					
			}
		});
		
	}

	private void addingSentToCPUFields() {
//		unbindField(sentToCPU);
//		sentToCPU = (OptionGroup) binder.buildAndBind( "Send to CPU", "sentToCPUFlag", OptionGroup.class);
//		sentToCPU.addItems(getReadioButtonOptions());
//		sentToCPU.setItemCaption(true, "Yes");
//		sentToCPU.setItemCaption(false, "No");
//		sentToCPU.setStyleName("horizontal");
//		sentToCPU.select(false);
//		
//		sentToCPU.addValueChangeListener(new Property.ValueChangeListener() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				Boolean isChecked = false;
//				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
//					isChecked = true;
//				} 
//				
//				//fireViewEvent(PreauthWizardPresenter.PREAUTH_SENT_TO_CPU_SELECTED, isChecked);
//				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_SENT_TO_CPU_SELECTED, isChecked);
//			}
//		});
	}
	
	@SuppressWarnings("unchecked")
	public void buildReferCoordinatorLayout(Object dropdownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);
		
		typeOfCoordinatorRequestCmb = (ComboBox) binder.buildAndBind("Type of Coordinator Request","typeOfCoordinatorRequest",ComboBox.class);
		typeOfCoordinatorRequestCmb.setContainerDataSource((BeanItemContainer<SelectValue>)dropdownValues);
		reasonForReferringTxta = (TextArea) binder.buildAndBind("Reason For Refering","reasonForRefering",TextArea.class);
		reasonForReferringTxta.setMaxLength(100);
		reasonForReferringTxta.setWidth("400px");
		dynamicFrmLayout.addComponent(typeOfCoordinatorRequestCmb);
		dynamicFrmLayout.addComponent(reasonForReferringTxta);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(typeOfCoordinatorRequestCmb);
		mandatoryFields.add(reasonForReferringTxta);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(true);
		wizard.getNextButton().setEnabled(false);
		
		
	}
	
	@SuppressWarnings("rawtypes")
	public void unbindAndRemoveComponents(AbstractComponent component)
	{
		for(int i=0;i<((FormLayout)component).getComponentCount();i++)
		{
			if(((FormLayout)component).getComponent(i) instanceof Upload)
			{                
				continue;		
			}
			if(! (((FormLayout)component).getComponent(i) instanceof HorizontalLayout)){
				unbindField((AbstractField)((FormLayout)component).getComponent(i));
			}
		}
		
		wholeVlayout.removeAllComponents();
		dynamicFrmLayout.removeAllComponents();
		
		if(txtWithdraw != null){
			unbindField(txtWithdraw);
		}
		
		if(hospRejectionRemarksTxta != null){
			unbindField(hospRejectionRemarksTxta);
		}
		
		if(withDrawRemarksForInsuredTxta != null){
			unbindField(withDrawRemarksForInsuredTxta);
		}
		
		if(rejremarksForInsuredTxta != null){
			unbindField(rejremarksForInsuredTxta);
		}
		
		if(preAuthPreviousQueryDetailsTable != null) {
			wholeVlayout.removeComponent(preAuthPreviousQueryDetailsTable);
		}
		
		userLayout.removeAllComponents();
		dynamicFrmLayout.removeAllComponents();
		wholeVlayout.removeComponent(userLayout);
		wholeVlayout.removeComponent(dynamicFrmLayout);
		
		if(updatePanCardLayout!=null){
			wholeVlayout.removeComponent(updatePanCardLayout);
		}
		
		/*if( null != wholeVlayout && 0!=wholeVlayout.getComponentCount())
		{
			Iterator<Component> componentIterator = wholeVlayout.iterator();
			wholeVlayout.removeComponent(userLayout);
			wholeVlayout.removeComponent(dynamicFrmLayout);
			while(componentIterator.hasNext()) 
			{
				Component searchScrnComponent = componentIterator.next() ;
				if(searchScrnComponent instanceof  VerticalLayout )
				{
					((VerticalLayout) searchScrnComponent).removeAllComponents();
					
				}
			}
		}*/
	}
	
	private void unbindField(Field<?> field) {
		if (field != null)
		{
			Object propertyId = this.binder.getPropertyId(field);
			if(propertyId != null) {
				this.binder.unbind(field);	
			}
			
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void alignFormComponents()
	{
		if(dynamicFrmLayout != null)
		{
			for(int i=0; i<dynamicFrmLayout.getComponentCount();i++)
			{
				dynamicFrmLayout.setExpandRatio(dynamicFrmLayout.getComponent(i), 0.5f);
			}
		}
	}
	
	
	public boolean isValid()
	{
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		
		if(this.binder == null) {
			hasError = true;
//			errorMessages.add("Please select Approve or Query or Reject or Denial or Escalte or Refer to coordinator or Downsize or Witdraw. </br>");
			errorMessages.add("Please select Approve or Query or Reject or Denial or Escalte or Refer to coordinator or Revision of Authorization or Witdraw. </br>");
			return !hasError;
		}
		
		if(bean.getPreauthMedicalDecisionDetails().getNegotiationMade() == null){
			if(negotiation.getValue() == null) {
				errorMessages.add("Please Select Negotiation on this claim. </br>");
				hasError = true;
			}
		}
		
		if (bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)
				&& null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			if(bean.getPreauthMedicalDecisionDetails().getVerifiedBonus() == null){
				hasError = true;
				errorMessages.add("Verify Bonus Applicability"+"</br>");
			}else if(bean.getPreauthMedicalDecisionDetails().getVerifiedBonus().equals(Boolean.FALSE)){
				hasError = true;
				errorMessages.add("Verified Bonus has to be selected as Yes to procees further"+"</br>");
			}
		}	

		if(null != negotiationOpinionTaken && (null != bean.getIsNegotiationApplicable() && bean.getIsNegotiationApplicable())){
			if(!(bean.getPreauthMedicalDecisionDetails().getIsNegotiationCancelled())){
				if(null == negotiationOpinionTaken.getValue())
				{
					errorMessages.add("Please select Refer for negotiation option. </br>");
					hasError = true;
				}
				else if((Boolean.TRUE.equals(negotiationOpinionTaken.getValue())) && 
						(null != txtNegotiationRemarks && null == txtNegotiationRemarks.getValue() || StringUtils.isBlank(txtNegotiationRemarks.getValue())))
				{
					errorMessages.add("Please Enter Points to Negotiate.</br>");
					hasError = true;
				}
				else if((Boolean.TRUE.equals(negotiationOpinionTaken.getValue())) && 
						(null != amtToNegotiate && null == amtToNegotiate.getValue() || StringUtils.isBlank(amtToNegotiate.getValue()))){
					errorMessages.add("Please Enter Amount to be Negotiated.</br>");
					hasError = true;
				}
			}
		}

		if(bean.getPreauthMedicalDecisionDetails().getIsMandatory()){
			/*if((cmbUserRoleMulti.getValue() == null || cmbUserRoleMulti.isEmpty()) ||(cmbDoctorNameMulti.getValue() == null || cmbDoctorNameMulti.isEmpty()) ||
				(remarksFromDeptHead.getValue() == null || ("").equalsIgnoreCase(remarksFromDeptHead.getValue()) || remarksFromDeptHead.getValue().isEmpty())){
				hasError = true;
				errorMessages.add(SHAConstants.USER_ROLE_VALIDATION + " </br>");
			}*/
			if(remarksFromDeptHead.getValue() == null || ("").equalsIgnoreCase(remarksFromDeptHead.getValue())){
				hasError = true;
				errorMessages.add("Please provide Opinion Given is mandatory"+ "</br>");
			}
			
//			R0001	MEDICAL HEAD
//			R0002	UNIT HEAD
//			R0003	DIVISION HEAD
//			R0004	CLUSTER HEAD
//			R0005	SPECIALIST
//			R0006	ZONAL HEAD
//			R00011	MEDICAL HEAD - GMC
//			R00012	DEPUTY MEDICAL HEAD - GMC
			List<String> roleList = null;
			Map<String, String> selectedRole = getRoleValidationContainer();
			Map<String, String> selectedUser = getUserValidationContainer();
			
			System.out.println("Role Val :"+selectedRole);
			System.out.println("User Val :"+selectedUser);
			if(bean.getPreauthMedicalDecisionDetails().getIsSDMarkedForOpinion()){
				System.out.println("ClaimType : "+bean.getNewIntimationDTO().getClaimType().getValue());
				boolean roleAvailabilityFlag = false;
				if(bean.getNewIntimationDTO().getClaimType() != null && bean.getNewIntimationDTO().getClaimType().getValue().equals("Cashless")){
					roleList =  Arrays.asList("R0002", "R0006");
					hasError = doOpinionSDValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
				}else{
					roleList =  Arrays.asList("R0002");
					hasError = doOpinionSDValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
				}
				
			}else{
				if(bean.getPreauthMedicalDecisionDetails().getIsPortedPolicy()){
					System.out.println("This is a Ported Policy.......");
					//				if((Long.valueOf(bean.getPreauthMedicalDecisionDetails().getFinalClaimAmout()) >= 300000L) && bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
					if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
						roleList =  Arrays.asList("R0004", "R0003", "R0001", "R00011", "R00012");
					}
					System.out.println(bean.getNewIntimationDTO().getIntimationId()+"<------>"+bean.getPreauthMedicalDecisionDetails().getUserClickAction());
					List<String> userPortedActionList = Arrays.asList("Withdraw Pre-auth", "Withdraw & Reject");
					boolean roleAvailabilityFlag = false;
					if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
						hasError = doRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
					}else if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Reject")){		
						roleList =  Arrays.asList("R0002", "R0001");
						String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
						boolean isSeniorDocLogin = masterService.checkSeniorDoctor(loginUserId.toUpperCase());
						if(!isSeniorDocLogin){
							hasError = doPortedPolicyRejectionRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
						}else{
							hasError = doPPRejectionValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
						}
						System.out.println("Enhan Ported Policy Rejection ErrorFlg : "+hasError);
					}else if(userPortedActionList.contains(bean.getPreauthMedicalDecisionDetails().getUserClickAction())){					
						roleList =  Arrays.asList("R0004", "R0003", "R0001");
						hasError = doPortedPolicyRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
						System.out.println("Enhan Ported Policy ErrorFlg : "+hasError);
					}

				}else{

					if(Long.valueOf(bean.getPreauthMedicalDecisionDetails().getFinalClaimAmout()) >= 300000L){
						roleList =  Arrays.asList("R0001", "R0002", "R00011", "R00012");
					}else{
						roleList =  Arrays.asList("R0003", "R0001", "R0002", "R00011", "R00012");
					}

					System.out.println(bean.getNewIntimationDTO().getIntimationId()+"<------>"+bean.getPreauthMedicalDecisionDetails().getUserClickAction());

					boolean roleAvailabilityFlag = false;
					List<String> userActionList = Arrays.asList("Approve", "Downsize Pre-auth", "Reject", "Query", "Withdraw Pre-auth", "Withdraw & Reject");
					if(userActionList.contains(bean.getPreauthMedicalDecisionDetails().getUserClickAction())){
						hasError = doRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
						System.out.println("Enhan ErrorFlg : "+hasError);
					}
				}
			}
		}else if(!bean.getPreauthMedicalDecisionDetails().getIsMandatory()){
			String nonMandateerrorMessage = "Please provide Consulted With, Opinion Given by, and Opinion Given or Make Consulted With, Opinion Given by, and Opinion Given as Empty"+"</br>";
			if(cmbUserRoleMulti != null && cmbDoctorNameMulti != null && remarksFromDeptHead != null){
				if((!cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue()))){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}

				if((cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue()))){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}

				if((cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue()))){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}					

				if(!cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue())){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}

				if(!cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue())){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}

				if(cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue())){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}
			}
		}
		
		//R1256
		if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)){
			if(chkOthers != null){
				boolean isEOF_SD = chkSubDoc.getValue();
				boolean isEOF_FVR = chkFVR.getValue();
				boolean isEOF_IR = chkIR.getValue();
				boolean isEOF_Others = chkOthers.getValue();
				String isEOF_OthersRmrks = txtaRemarks.getValue();
	
				if(!isEOF_SD && !isEOF_FVR && !isEOF_IR && !isEOF_Others){
					hasError = true;
					errorMessages.add("Any one of the Evidence Obtained From has to be selected.");
				}
	
				if(isEOF_Others && StringUtils.isBlank(isEOF_OthersRmrks)){
					hasError = true;
					errorMessages.add("Evidence Obtained From Others Remarks is mandatory.");
				}
			}
		}

		/*if(null != referCPUCategoryCmb && null != referCPUCategoryCmb.getValue() && ((SelectValue)(referCPUCategoryCmb.getValue())).getId().equals(ReferenceTable.CPU_ALLOCATION_OTHERS)){
			if(!(null != referCPURemarksTxta && null != referCPURemarksTxta.getValue() && !("").equals( referCPURemarksTxta.getValue())))
			{
				hasError = true;
				errorMessages.add("Please Enter Opinion Remarks.");
				return !hasError;
			}
		}*/
		
		if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)) {
			
			SelectValue rejCategorySelect = rejectionCategoryCmb != null && rejectionCategoryCmb.getValue() != null ? (SelectValue)rejectionCategoryCmb.getValue() : null;
			SelectValue rejSubCategorySelect =  rejSubCategCmb != null && rejSubCategCmb.getValue() != null ? (SelectValue)rejSubCategCmb.getValue() : null;
			
			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) { 
					if(rejCategorySelect != null
							&& rejCategorySelect.getId() != null 
								&& ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION.equals(rejCategorySelect.getId())) {
				
						if(rejSubCategorySelect == null) {
							hasError = true;
							errorMessages.add("Please Select Rejection Sub category.");
						}
					}
					else{
						if(rejSubCategCmb != null) {
							unbindField(rejSubCategCmb);
						}	
					}
			}			
			
			if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
					
				rejCategorySelect = cmbWithdraw != null && cmbWithdraw.getValue() != null ? (SelectValue)cmbWithdraw.getValue() : null; 

				if(rejCategorySelect != null
						&& rejCategorySelect.getId() != null 
						&& (ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION.equals(rejCategorySelect.getId()))
											|| ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_ENH.equals(rejCategorySelect.getId())) {
						
					if(rejSubCategorySelect == null) {
						hasError = true;
						errorMessages.add("Please Select Rejection Sub category.");
					}
				}
				else{
					if(rejSubCategCmb != null) {
						unbindField(rejSubCategCmb);
					}	
				}
			}	
				
			if((rejCategorySelect != null
					&& rejCategorySelect.getId() != null 
					&& SHAUtils.getPolCondionalExclutionRejKeys().containsKey(rejCategorySelect.getId())
					&& !ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
					|| (rejSubCategorySelect != null
							&& (ReferenceTable.REJ_SUB_CATEG_PED_CANCEL_POLICY.equals(rejSubCategorySelect.getId())
									|| ReferenceTable.REJ_SUB_CATEG_PED_CANCEL_POLICY_ENH.equals(rejSubCategorySelect.getId())))){
			
				if(rejCondtNoTxt != null
						&& (rejCondtNoTxt.getValue() == null || rejCondtNoTxt.getValue().isEmpty())) {
					
					hasError = true;
					errorMessages.add("Please Enter Condition Number.");
				}
		
			}
			else{
				if(rejCondtNoTxt != null) {
					unbindField(rejCondtNoTxt);
				}	
			}
	}
		
		if(bean.getStatusKey() != null && ( bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) || bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) && bean.getPreauthMedicalDecisionDetails().getIsNegotiationDone() != null && !bean.getPreauthMedicalDecisionDetails().getIsNegotiationDone()){
			hasError = true;
			errorMessages.add("Negotation is under progress. Do Cancel or Update.");
		}
		
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)){
			if(this.cmbDownsizeReason.getValue() != null) {
				if(downsizeRemarks.getValue() == null || downsizeRemarks.getValue().isEmpty()) {
					hasError = true;
					errorMessages.add("Please Enter Downsize Remarks.</br>");
				}	
			}
		}

		if(icdExclusionReasonTxta!=null){
			if(icdExclusionReasonTxta.getValue() == null || ("").equalsIgnoreCase(icdExclusionReasonTxta.getValue()) || icdExclusionReasonTxta.getValue().isEmpty()){
				hasError = true;
				errorMessages.add("Please Enter Reason for selecting timely excluded ICD </br>");
			}
			if(icdExclusionReasonTxta.getValue() != null){
			if(icdExclusionReasonTxta.getValue().length()<15){
				hasError = true;
				errorMessages.add("Please enter atleast 15 characters in Reason for selecting timely excluded ICD</br> ");
			}
			}
		}
		
		if (!this.binder.isValid()) {
			 hasError = true;
			 for (Field<?> field : this.binder.getFields()) {
			    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
			    	if (errMsg != null) {
			    		errorMessages.add(errMsg.getFormattedHtmlMessage());
			    	}
			  }
				
			 if(!hasError && errorMessages.isEmpty()){
				 try {
						this.binder.commit();
						
						if(this.bean.getPreauthMedicalDecisionDetails().getWithdrawReason() != null){
							this.bean.setWithdrawReason(this.bean.getPreauthMedicalDecisionDetails().getWithdrawReason());
						}
					} catch (CommitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	 
			 }
		}
		else {
			try {
				this.binder.commit();
				
				if(this.bean.getPreauthMedicalDecisionDetails().getWithdrawReason() != null){
					this.bean.setWithdrawReason(this.bean.getPreauthMedicalDecisionDetails().getWithdrawReason());
				}
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		showOrHideValidation(false);
		return !hasError;
	}
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	public void generateFieldsBasedOnSentToCPU(Boolean isChecked) {
		if(isChecked) {
			unbindField(remarksForCPU);
			remarksForCPU = (TextArea) binder.buildAndBind("Remarks for CPU","remarksForCPU",TextArea.class);
			remarksForCPU.setMaxLength(100);
			mandatoryFields.add(remarksForCPU);
			dynamicFrmLayout.addComponent(remarksForCPU);
			showOrHideValidation(false);
		} else {
			unbindField(remarksForCPU);
			mandatoryFields.remove(remarksForCPU);
			dynamicFrmLayout.removeComponent(remarksForCPU);
		}
	}
	
	private boolean validateSelectedCoPay (Double selectedCoPay)
	{
		if(null != this.bean.getCopay())
		{
			if(selectedCoPay >= this.bean.getCopay())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}
	
	public void clearFields() {
		if(dynamicFrmLayout != null) {
			dynamicFrmLayout.removeAllComponents();
		}
	}
	
	 public void wizardCancelled() {/*
			ConfirmDialog dialog = ConfirmDialog
					.show(getUI(),
							"Confirmation",
							"Are you sure you want to cancel ?",
							"No", "Yes", new ConfirmDialog.Listener() {

								public void onClose(ConfirmDialog dialog) {
									if (!dialog.isConfirmed()) {
										// Confirmed to continue
										
										fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,null);
									} else {
										// User did not confirm
									}
								}
							});

			dialog.setClosable(false);
			dialog.setStyleName(Reindeer.WINDOW_BLACK);

		*/final MessageBox getConf = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage("Are you sure you want to cancel ?")
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
			Button homeButton=getConf.getButton(ButtonType.YES);
			Button cancelButton=getConf.getButton(ButtonType.NO);
			homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,null);
				}
			});

			 
	 }
	 
		public void showErrorMessage(String eMsg) {/*
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
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
		
	public void disableApprove(Boolean isEnabled) {
		approveBtn.setEnabled(isEnabled);
		
		if(isEnabled){
			if(this.bean.isClsProsAllowed()){
				approveBtn.setEnabled(true);
			}
			else{
				approveBtn.setEnabled(false);
			}
		}
	}
	
	public Double getHospitalAmtForUnique() {
		if(amountToHospAftPremium != null) {
			return SHAUtils.getDoubleFromStringWithComma(amountToHospAftPremium.getValue()) ;
		}
		return 0d;
	}
	
	private void addRejectionListener() {
		
			rejectionCategoryCmb.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue value = (SelectValue) event.getProperty().getValue();
					if (value != null) {
						
						if(value != null) { 
						
							if(ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION.equals(value.getId())
									|| ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_ENH.equals(value.getId())) {
								
								rejSubCategCmb.setVisible(true);
								mandatoryFields.add(rejSubCategCmb);
								fireViewEvent(PreauthEnhancemetWizardPresenter.ENH_SUB_REJECT_CATEG_LAYOUT, value.getId(), SHAConstants.ENHANCEMENT_REJECTION);
							}
							else {
									if(rejSubCategCmb != null){
										rejSubCategCmb.setVisible(false);
										mandatoryFields.remove(rejSubCategCmb);
									}	
								
								fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_GENERATE_REMARKS,value.getId(), SHAConstants.ENHANCEMENT_REJECTION);
							}	
						}	
					}					
				}
			});
			
			if(rejSubCategCmb != null) {
				rejSubCategCmb.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						
						SelectValue value = event.getProperty().getValue() != null ? (SelectValue) event.getProperty().getValue() : null;
		
						if(value != null) {

							if(ReferenceTable.REJ_SUB_CATEG_PED_CANCEL_POLICY.equals(value.getId())
									|| ReferenceTable.REJ_SUB_CATEG_PED_CANCEL_POLICY_ENH.equals(value.getId())) {
								rejCondtNoTxt.setVisible(true);
								mandatoryFields.add(rejCondtNoTxt);
								showOrHideValidation(false);
							}
							else {
								if(rejCondtNoTxt != null) {
									rejCondtNoTxt.setVisible(false);
									mandatoryFields.remove(rejCondtNoTxt);
								}	
							}
							fireViewEvent(PreauthEnhancemetWizardPresenter.ENH_GET_REJ_SUBCATEG_REMARKS, value.getId(), SHAConstants.ENHANCEMENT_REJECTION);
						}
					}
				});
			}
		}
	
	private void addWithDrawListenerListener() {
		
		cmbWithdraw.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if (value != null) {
					
					if(ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_ENH.equals(value.getId())) {
						
						rejSubCategCmb.setVisible(true);
						mandatoryFields.add(rejSubCategCmb);
						fireViewEvent(PreauthEnhancemetWizardPresenter.ENH_SUB_REJECT_CATEG_LAYOUT, value.getId(), SHAConstants.ENHANCEMENT_WITHDARW);
					}
					else {
						if(rejSubCategCmb != null) {
							rejSubCategCmb.setVisible(false);
							mandatoryFields.remove(rejSubCategCmb);
						}	
						fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_GENERATE_REMARKS,value.getId(),SHAConstants.ENHANCEMENT_WITHDARW);
					}
						
					if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
						if(SHAUtils.getPolCondionalExclutionRejKeys().containsKey(value.getId())) {
						
							if(rejCondtNoTxt != null){
								mandatoryFields.add(rejCondtNoTxt);
								rejCondtNoTxt.setVisible(true);
								showOrHideValidation(false);
							}
						}
						else {
							if(rejCondtNoTxt != null){
								rejCondtNoTxt.setVisible(false);
								mandatoryFields.remove(rejCondtNoTxt);
							}
						}
					}	
					
				}				
				
			}
		});
		
		if(rejSubCategCmb != null) {
			rejSubCategCmb.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					SelectValue value = event.getProperty().getValue() != null ? (SelectValue) event.getProperty().getValue() : null;
	
					if(value != null) {

						if(ReferenceTable.REJ_SUB_CATEG_PED_CANCEL_POLICY.equals(value.getId())
								|| ReferenceTable.REJ_SUB_CATEG_PED_CANCEL_POLICY_ENH.equals(value.getId())) {
							rejCondtNoTxt.setVisible(true);
							mandatoryFields.add(rejCondtNoTxt);
							showOrHideValidation(false);
						}
						else {
							if(rejCondtNoTxt != null) {
								rejCondtNoTxt.setVisible(false);
								mandatoryFields.remove(rejCondtNoTxt);
							}	
						}
						fireViewEvent(PreauthEnhancemetWizardPresenter.ENH_GET_REJ_SUBCATEG_REMARKS, value.getId(), SHAConstants.ENHANCEMENT_WITHDARW);
					}
				}
			});
		}
	}

	private void addWithDrawRejectListenerListener() {
		
		cmbWithdraw.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if (value != null) {
					fireViewEvent(PreauthEnhancemetWizardPresenter.GENERATE_WITHDRAW_REJECT_REMARKS,value.getId());
				}
				
				
			}
		});
	}
	
	
	public void setQryRemarks(String qryRemarks) {
		
		bean.getPreauthMedicalDecisionDetails().setQueryRemarks(qryRemarks);
		queryRemarksTxta.setValue(bean.getPreauthMedicalDecisionDetails().getQueryRemarks());
		
	}
	
	public void setRemarks(MasterRemarks remarks, String decision) {
		String remarksValue =null;
		String insuredRemarks = null;
		if (remarks != null) {
			remarksValue = remarks.getRemarks() != null ? remarks.getRemarks() : "";
		}
		if (remarks != null) {
			insuredRemarks = remarks.getInsuredRemarks() != null ? remarks.getInsuredRemarks() : "";
			
			SelectValue rejCatgId = rejectionCategoryCmb != null ? (SelectValue)rejectionCategoryCmb.getValue() : null;

			if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				
				rejCatgId = cmbWithdraw != null && cmbWithdraw.getValue() != null ? (SelectValue)cmbWithdraw.getValue() : null;
				
				if(rejCatgId != null
						&& SHAUtils.getPolCondionalExclutionRejKeys().containsKey(rejCatgId.getId())) {
						
					String[] userRemarks = remarks.getInsuredRemarks().split(";");

					if (userRemarks.length == 2) {
						insuredRemarks = userRemarks[0];
						bean.getPreauthMedicalDecisionDetails().setPolicyRejectConditClause(userRemarks[userRemarks.length - 1]);
					}
				}			

			}
			
			SelectValue rejSubCatgId = rejSubCategCmb != null ? (SelectValue)rejSubCategCmb.getValue() : null;
			
			if((rejCatgId != null 
					&& SHAUtils.getPolCondionalExclutionRejKeys().containsKey(rejCatgId.getId()) 
					&& !ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
					|| (rejSubCatgId != null
								&& (ReferenceTable.REJ_SUB_CATEG_PED_CANCEL_POLICY.equals(rejSubCatgId.getId())
										|| ReferenceTable.REJ_SUB_CATEG_PED_CANCEL_POLICY_ENH.equals(rejSubCatgId.getId())))) {
				
				String[] userRemarks = remarks.getInsuredRemarks().split(";");

				if (userRemarks.length == 2) {
					insuredRemarks = userRemarks[0];
					bean.getPreauthMedicalDecisionDetails().setPolicyRejectConditClause(userRemarks[userRemarks.length - 1]);
				}
			}			
			
			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				rejCatgId = rejectionCategoryCmb != null && rejectionCategoryCmb.getValue() != null ? (SelectValue)rejectionCategoryCmb.getValue() : null;
			
				if(rejCatgId != null
						&& SHAUtils.getPolCondionalExclutionRejKeys().containsKey(rejCatgId.getId())) {
					
					insuredRemarks = StringUtils.replace(insuredRemarks, ";", "");
					insuredRemarks = StringUtils.replace(insuredRemarks, "PCNO", "-----");
				}
			}
			
		}
		
		if(decision.equals(SHAConstants.ENHANCEMENT_REJECTION) && hospRejectionRemarksTxta != null){
			hospRejectionRemarksTxta.setValue(remarksValue);
		} else if (decision.equals(SHAConstants.ENHANCEMENT_WITHDARW) && txtWithdraw != null){
			txtWithdraw.setValue(remarksValue);
		}
		
		if(decision.equals(SHAConstants.ENHANCEMENT_REJECTION) && rejRemarksForInsuredTxta != null){
			rejRemarksForInsuredTxta.setValue(insuredRemarks);
		}	
		
		if (decision.equals(SHAConstants.ENHANCEMENT_WITHDARW) && withDrawRemarksForInsuredTxta != null){
			withDrawRemarksForInsuredTxta.setValue(insuredRemarks);
		}
		
		if(decision.equals(SHAConstants.ENHANCEMENT_DOWNSIZE) && downsizeRemarks != null){
			downsizeRemarks.setValue(remarksValue);
		}
		if (decision.equals(SHAConstants.ENHANCEMENT_DOWNSIZE) && downsizeInsuredRemarks != null){
			downsizeInsuredRemarks.setValue(insuredRemarks);
		}
		if (decision.equals(SHAConstants.ENHANCEMENT_WITHDRAW_REJECT)  && txtWithdraw != null){
			txtWithdraw.setValue(remarksValue);
		}
		if (decision.equals(SHAConstants.ENHANCEMENT_WITHDRAW_REJECT)  && withDrawRemarksForInsuredTxta != null){
			withDrawRemarksForInsuredTxta.setValue(insuredRemarks);
		}
		
	}

	public void build64VBCompliance(Object dropDownValues) {


		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);
		
		vb64RemarksTxta = (TextArea) binder.buildAndBind("Remarks","refer64VBRemarks",TextArea.class);
		vb64RemarksTxta.setMaxLength(4000);
		vb64RemarksTxta.setWidth("400px");
		vb64RemarksTxta.setHeight("200px");
		dynamicFrmLayout.addComponent(vb64RemarksTxta);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(vb64RemarksTxta);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(true);
		wizard.getNextButton().setEnabled(false);
		/*final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		if (viewAllDocsLayout != null
				&& viewAllDocsLayout.getComponentCount() > 0) {
			viewAllDocsLayout.removeAllComponents();
		}
		
		viewAllDocsLayout.addComponent(viewClaimsDMSDocument);
		
		VerticalLayout VLayout = new VerticalLayout(viewAllDocsLayout,new FormLayout(rejectionRemarksTxta), btnLayout);
		VLayout.setComponentAlignment(viewAllDocsLayout, Alignment.BOTTOM_RIGHT);

		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		bean.setIsFirstStepRejection(true);*/
}
	
	public void buildCPUUserLayout(Object dropDownValues){
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);
		
		referCPUCategoryCmb = (ComboBox) binder.buildAndBind("Claim Processing Suggestion","cpuSuggestionCategory",ComboBox.class);
		referCPUCategoryCmb.setContainerDataSource((BeanItemContainer<SelectValue>)dropDownValues);
		referCPUCategoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		referCPUCategoryCmb.setItemCaptionPropertyId("value");
		
		referCPUCategoryCmb.setWidth("200px");
		addReferCPUCategoryListener();
		
		referCPUamountSuggested = (TextField) binder.buildAndBind("Amount suggested","cpuAmountSuggested",TextField.class);
		referCPUamountSuggested.setVisible(false);
		
		referCPURemarksTxta = (TextArea) binder.buildAndBind("Opinion Remarks","cpuRemarks",TextArea.class);
		referCPURemarksTxta.setMaxLength(4000);
		referCPURemarksTxta.setWidth("400px");
		referCPURemarksTxta.setHeight("200px");
		dynamicFrmLayout.addComponent(referCPUCategoryCmb);
		dynamicFrmLayout.addComponent(referCPUamountSuggested);
		dynamicFrmLayout.addComponent(referCPURemarksTxta);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(referCPUCategoryCmb);
		
		showOrHideValidation(false);
		wizard.getFinishButton().setEnabled(true);
		wizard.getNextButton().setEnabled(false);
	}
	
	private void addReferCPUCategoryListener() {

		referCPUCategoryCmb.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty()
						.getValue();
				if(value != null){
					referCPUCategoryCmb.setValue(value);
					if(value.getId().equals(ReferenceTable.CPU_ALLOCATION_OTHERS)){
						referCPURemarksTxta.setRequired(true);
					}else{
						referCPURemarksTxta.setRequired(false);
					}
					if(value.getId().equals(ReferenceTable.CPU_ALLOCATION_APPROVE)){
						referCPUamountSuggested.setVisible(true);
					}else{
						referCPUamountSuggested.setVisible(false);
					}
					
					
				}
			}
		});
	}
	
public void showErrorMsg(){/*
		
		String message = SHAConstants.DENINED_LIMIT_NOT_EXCEEDED_ALERT_DOWNSIZE ;
		
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
				approveBtn.setEnabled(false);
			}
		});
		
	*/

	
	String message = SHAConstants.DENINED_LIMIT_NOT_EXCEEDED_ALERT_DOWNSIZE ;
	final MessageBox showAlert = showAlertMessageBox(message);
	Button homeButton = showAlert.getButton(ButtonType.OK);
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			showAlert.close();
			approveBtn.setEnabled(false);
		}
	});
	
	
}

public  void remarksPopupListener(TextArea searchField, final  Listener listener) {
	
    ShortcutListener enterShortCut = new ShortcutListener(
        "ShortcutForPreauthMedRemarks", ShortcutAction.KeyCode.F8, null) {
	
      private static final long serialVersionUID = 1L;
      @Override
      public void handleAction(Object sender, Object target) {
        ((ShortcutListener) listener).handleAction(sender, target);
      }
    };
    handleShortcutForMedicalRemarks(searchField, getShortCutListenerForRemarks(searchField));
    
  }

public  void handleShortcutForMedicalRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
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
			
			
//			if(("hospRmrks").equalsIgnoreCase(txtFld.getId()) || ("insuredRmrks").equalsIgnoreCase(txtFld.getId())){
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				dialog.setHeight("75%");
		    	dialog.setWidth("65%");
				txtArea.setReadOnly(false);
//			}
			
			
			txtArea.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					if(("insDwnSizeRmrks").equalsIgnoreCase(txtFld.getId()) || ("hospDwnSizeRmrks").equalsIgnoreCase(txtFld.getId())){
						txtFld.setValue(((TextArea)event.getProperty()).getValue());					
						
					}else if(("hospitalWithdrawRmrks").equalsIgnoreCase(txtFld.getId()) || ("hospRejRmrks").equalsIgnoreCase(txtFld.getId())){
						txtFld.setValue(((TextArea)event.getProperty()).getValue());					
						
					}else if(("insuredWithdrawRmrks").equalsIgnoreCase(txtFld.getId()) || ("insuredRejRmrks").equalsIgnoreCase(txtFld.getId())){					
						txtFld.setValue(((TextArea)event.getProperty()).getValue());						
					}
				}
			});
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
			
			
			String strCaption = "";
			
			if(("hospitalWithdrawRmrks").equalsIgnoreCase(txtFld.getId())){
				strCaption = "Withdraw Remarks For Hospital";
			}
			else if(("hospRejRmrks").equalsIgnoreCase(txtFld.getId())){
				strCaption = "Rejection Remarks For Hospital";
			}
		    else if(("insuredWithdrawRmrks").equalsIgnoreCase(txtFld.getId())){
		    	strCaption = "Withdraw Remarks For Insured";
		    }
		    else if(("insuredRejRmrks").equalsIgnoreCase(txtFld.getId())){
		    	strCaption = "Rejection Remarks For Insured";
		    }		
		    else if(("insDwnSizeRmrks").equalsIgnoreCase(txtFld.getId())){
		    	strCaption = "Downsize Remarks For Insured";
		    }
		    else if(("hospDwnSizeRmrks").equalsIgnoreCase(txtFld.getId())){
		    	strCaption = "Downsize Remarks For Hospital";
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
//					TextArea txtArea = (TextArea)dialog.getData();
//					txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
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
//					TextArea txtArea = (TextArea)dialog.getData();
//					txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
					dialog.close();
				}
			});	
		}
	};
	
	return listener;
}

public FormLayout buildUserRoleLayout(){
	String enhancementType = (bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement() == true)?"I":"F";
	String hospitilizationType = "Y";
	String negotiationFlag ="N";
	
	Integer qryTyp;
	if (bean.getQueryType() == null){
		qryTyp = 0;
	}else{
		qryTyp = bean.getQueryType().getId().intValue();
	}
	Integer qryCnt;
	if (bean.getQueryCount() == null){
		qryCnt = 0;
	}else{
		qryCnt = bean.getQueryCount() + 1;
	}
	
	String finalClaimAmount = "";
	if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
		finalClaimAmount = bean.getAmountConsidered();
	}else{
		finalClaimAmount = bean.getPreauthDataExtractionDetails().getAmtClaimed();
	}
	finalClaimAmount = (finalClaimAmount == null)?"0":finalClaimAmount;
	bean.getPreauthMedicalDecisionDetails().setFinalClaimAmout(Long.valueOf(finalClaimAmount));
	
	if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")
			&& bean.getPreauthMedicalDecisionDetails().getNegotiationMade()){
		negotiationFlag ="Y";
	}
	
	System.out.println("Enhancement : User Role For Intimation No "+bean.getNewIntimationDTO().getIntimationId());
	Map<String,Object> opinionValues = dbCalculationService.getOpinionValidationDetails(Long.valueOf(finalClaimAmount),bean.getStageKey(),bean.getStatusKey(),
			Long.valueOf(bean.getNewIntimationDTO().getCpuCode()),"N",bean.getNewIntimationDTO().getPolicy().getKey(),bean.getClaimDTO().getKey(),enhancementType, hospitilizationType,qryTyp,qryCnt,
			bean.getClaimDTO().getClaimType().getId(),
			ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL,SHAConstants.ENHANCEMENT,bean.getKey(), bean.getStatusKey(),negotiationFlag); 
	
	BeanItemContainer<SpecialSelectValue> userRole = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
	userRole.addAll((List<SpecialSelectValue>)opinionValues.get("role"));
	BeanItemContainer<SpecialSelectValue> userRoleWithoutSGm = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
	for (SpecialSelectValue component : userRole.getItemIds()) {
		if(!component.getSpecialValue().equalsIgnoreCase("R0007")) {
			userRoleWithoutSGm.addItem(component);
		}
	}
	
	BeanItemContainer<SpecialSelectValue> empNames = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
	empNames.addAll((List<SpecialSelectValue>) opinionValues.get("emp"));
	
	if(null != opinionValues){			
		String mandatoryFlag =  (String) opinionValues.get("mandatoryFlag");
		if(null != mandatoryFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(mandatoryFlag)){
			bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.TRUE);
		}
		else{
			bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
		}
		
		String portedFlag =  (String) opinionValues.get("portedFlag");
		if(null != portedFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(portedFlag)){
			bean.getPreauthMedicalDecisionDetails().setIsPortedPolicy(Boolean.TRUE);
		}else{
			bean.getPreauthMedicalDecisionDetails().setIsPortedPolicy(Boolean.FALSE);
		}

		String seriousDefiencyFlagForOpinion = (String) opinionValues.get("seriousDeficiencyFlag");
		System.out.println("seriousDefiencyFlagForOpinion : "+seriousDefiencyFlagForOpinion);
		if(null != portedFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(seriousDefiencyFlagForOpinion)){
			bean.getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(Boolean.TRUE);
		}else if(null != portedFlag && SHAConstants.N_FLAG.equalsIgnoreCase(seriousDefiencyFlagForOpinion)){
			bean.getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(Boolean.FALSE);
		}else{
			bean.getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(null);
		}
	}

	cmbUserRoleMulti = new ComboBoxMultiselect("Consulted With");
	cmbUserRoleMulti.setShowSelectedOnTop(true);
	cmbUserRoleMulti.setComparator(SHAUtils.getComparator());
	cmbUserRoleMulti.setContainerDataSource(userRoleWithoutSGm);
	cmbUserRoleMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbUserRoleMulti.setItemCaptionPropertyId("value");	
	cmbUserRoleMulti.setData(userRoleWithoutSGm);
	
			
	cmbDoctorNameMulti = new ComboBoxMultiselect("Opinion Given by");
	cmbDoctorNameMulti.setShowSelectedOnTop(true);
	cmbDoctorNameMulti.setContainerDataSource(empNames);
	cmbDoctorNameMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbDoctorNameMulti.setItemCaptionPropertyId("commonValue");
	cmbDoctorNameMulti.setData(empNames);
	
	bean.getPreauthMedicalDecisionDetails().setRemarksFromDeptHead(null);
	remarksFromDeptHead = (TextArea) binder.buildAndBind("Opinion Given", "remarksFromDeptHead",TextArea.class);		
	remarksFromDeptHead.setMaxLength(2000);
	remarksFromDeptHead.setWidth("260px");
	remarksFromDeptHead.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
	//handleTextAreaPopup(remarksFromDeptHead,null);
	SHAUtils.handleTextAreaPopup(remarksFromDeptHead,null,getUI());
	remarksFromDeptHead.setData(bean);
	addUserRoleListener();
	userLayout = new FormLayout();
	userLayout.addComponents(cmbUserRoleMulti,cmbDoctorNameMulti,remarksFromDeptHead);
	userLayout.setMargin(Boolean.TRUE);
	userLayout.setSpacing(Boolean.TRUE);
	if (bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){
		Long prodKey = bean.getNewIntimationDTO().getPolicy().getProduct().getKey();
		String insuredNo=null;
		String commonIP=null;
		 if(bean.getPreauthDataExtractionDetails()!=null && bean.getPreauthDataExtractionDetails().getDiagnosisTableList()!=null
				 && !bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()){
			 for (DiagnosisDetailsTableDTO detailsTableDTO: bean.getPreauthDataExtractionDetails().getDiagnosisTableList()){
				 if(detailsTableDTO.getIcdCode() !=null && detailsTableDTO.getIcdCode().getCommonValue() !=null){
					 Map<String,String> PerExcludedICDCode = dbCalculationService.getPermanentExclusionsIcdMapping(detailsTableDTO.getIcdCode().getCommonValue(),prodKey,null,null,insuredNo,commonIP);
					 String icdCodeFlag= PerExcludedICDCode.get("flag");
						String icdCodeRemarks= PerExcludedICDCode.get("remarks");
						if(icdCodeFlag!=null && icdCodeFlag.equalsIgnoreCase("A")){
							bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.TRUE);
							
							}
				 		}
			 		}
				 }
	}
	if(bean.getPreauthMedicalDecisionDetails().getIsMandatory()){
		mandatoryFields.add(cmbUserRoleMulti);
		mandatoryFields.add(cmbDoctorNameMulti);
		mandatoryFields.add(remarksFromDeptHead);
		showOrHideValidation(false);
	}
	else{
		mandatoryFields.remove(cmbUserRoleMulti);
		mandatoryFields.remove(cmbDoctorNameMulti);
		mandatoryFields.remove(remarksFromDeptHead);
	}
	return userLayout;	
	
}


public void addUserRoleListener(){
	getRoleValidationContainer().clear();
	getUserValidationContainer().clear();
	cmbUserRoleMulti.addValueChangeListener(new ValueChangeListener() {
		private static final long serialVersionUID = 1L;
		@Override
		public void valueChange(Property.ValueChangeEvent event) {
			
			BeanItem<PreauthMedicalDecisionDTO> dtoBeanObject = binder.getItemDataSource();
			PreauthMedicalDecisionDTO dtoObject = dtoBeanObject.getBean();
			dtoObject.setUserRoleMulti(null);
			dtoObject.setUserRoleMulti(event.getProperty().getValue());
			
			if(cmbDoctorNameMulti != null && cmbDoctorNameMulti.getData() != null){
				BeanItemContainer<SpecialSelectValue> listOfDoctors = (BeanItemContainer<SpecialSelectValue>) cmbDoctorNameMulti.getData();
				List<String> docList = preauthService.getListFromMultiSelectComponent(event.getProperty().getValue());
				BeanItemContainer<SpecialSelectValue> listOfRoleContainer = (BeanItemContainer<SpecialSelectValue>)cmbUserRoleMulti.getData();
				getRoleValidationContainer().clear();
				List<String> roles = new ArrayList<String>();
				List<SpecialSelectValue> listOfRoles = listOfRoleContainer.getItemIds();
				for (SpecialSelectValue specialSelectValue : listOfRoles) {
					if(null != docList && !docList.isEmpty() &&docList.contains(specialSelectValue.getValue())){
						roles.add(specialSelectValue.getSpecialValue());
						if(!getRoleValidationContainer().containsKey(specialSelectValue.getSpecialValue())){
							getRoleValidationContainer().put(specialSelectValue.getSpecialValue(), specialSelectValue.getValue());
						}
					}
					
				}
				List<SpecialSelectValue> filtersValue = new ArrayList<SpecialSelectValue>();
				List<SpecialSelectValue> itemIds = listOfDoctors.getItemIds();
				for (SpecialSelectValue specialSelectValue : itemIds) {
					if( specialSelectValue.getSpecialValue() != null && 
							roles.contains(specialSelectValue.getSpecialValue())){
						filtersValue.add(specialSelectValue);
					}
				}
				
				BeanItemContainer<SpecialSelectValue> filterContainer = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
				filterContainer.addAll(filtersValue);
				cmbDoctorNameMulti.setContainerDataSource(filterContainer);
				cmbDoctorNameMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbDoctorNameMulti.setItemCaptionPropertyId("commonValue");
			}
		}
	});
	
	cmbDoctorNameMulti.addValueChangeListener(new ValueChangeListener() {
		private static final long serialVersionUID = 1L;
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void valueChange(Property.ValueChangeEvent event) {
			BeanItem<PreauthMedicalDecisionDTO> dtoBeanObject = binder.getItemDataSource();
			PreauthMedicalDecisionDTO dtoObject = dtoBeanObject.getBean();
			dtoObject.setDoctorName(null);
			dtoObject.setDoctorName(event.getProperty().getValue());
			Set selectedObject = new HashSet<>((Collection) event.getProperty().getValue());
			getUserValidationContainer().clear();
			List<SpecialSelectValue> listOfUserSelected = new ArrayList<SpecialSelectValue>(selectedObject);
			if(listOfUserSelected.size() > 0){
				for(SpecialSelectValue specialSelectValue : listOfUserSelected){
					if(!getUserValidationContainer().containsKey(specialSelectValue.getSpecialValue())){
						getUserValidationContainer().put(specialSelectValue.getSpecialValue(), specialSelectValue.getValue());
					}else{
						String temp = getUserValidationContainer().get(specialSelectValue.getSpecialValue());
						if(temp.contains(specialSelectValue.getValue())){
							getUserValidationContainer().put(specialSelectValue.getSpecialValue(),specialSelectValue.getValue());
						}else{
							getUserValidationContainer().put(specialSelectValue.getSpecialValue(), temp+","+specialSelectValue.getValue());
						}
					}
				}
			}else{
				getUserValidationContainer().clear();
			}
			List<String> docList = preauthService.getListFromMultiSelectComponent(event.getProperty().getValue());
			BeanItemContainer<SpecialSelectValue> listOfDoctors = (BeanItemContainer<SpecialSelectValue>) cmbDoctorNameMulti.getData();
			bean.getPreauthMedicalDecisionDetails().setDoctorContainer(listOfDoctors);
		}
	});
}

public void alertForAdditionalFvrTriggerPoints(final String action) {	 
	 
	 Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.ADDTIONAL_FVR_TRIGGER_POINTS_ALERT + "</b>",
				ContentMode.HTML);
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Button addAdditionalFvrButton = new Button("Add Additional FVR Points");
		addAdditionalFvrButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		HorizontalLayout btnLayout = new HorizontalLayout(addAdditionalFvrButton , cancelButton);
		btnLayout.setSpacing(true);
		
		
		VerticalLayout layout = new VerticalLayout(successLabel, btnLayout);
		layout.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		addAdditionalFvrButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
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
						dialog.close();
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
				
			
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		
		dialog.addCloseListener(new Window.CloseListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
				if(SHAConstants.APPROVAL.equalsIgnoreCase(action)){
					if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement()){
						reConfirmMessageForBenefitSheet();
				}else{
					bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);
					fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
				}
				}
				else if(SHAConstants.QUERY.equalsIgnoreCase(action)){
					
					fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_QUERY_BUTTON_EVENT,null);
				}
				else if(SHAConstants.REJECT.equalsIgnoreCase(action)){
					fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_REJECTION_EVENT,bean);	
				}
			}
		});
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
	
		public void showErrorPageForCancelledPolicy(){/*
				
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
				
			*/
			

			
			String message = SHAConstants.CANCELLED_POLICY_ALERT ;
			final MessageBox showAlert = showAlertMessageBox(message);
			Button homeButton = showAlert.getButton(ButtonType.OK);
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
		
				@Override
				public void buttonClick(ClickEvent event) {
					showAlert.close();
				}
			});
			
		
		}
		
		public void buildHoldLayout()

		{
			initBinder();
			unbindAndRemoveComponents(dynamicFrmLayout);
			remarksLayout.removeAllComponents();
			wholeVlayout.removeComponent(remarksLayout);

			addingSentToCPUFields();		

			holdRemarksTxta = (TextArea) binder.buildAndBind("Hold Remarks","holdRemarks",TextArea.class);

			holdRemarksTxta.setMaxLength(4000);

			holdRemarksTxta.setWidth("400px");		

			holdRemarksTxta.setHeight("200px");
			holdRemarksTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
			handleTextAreaPopup(holdRemarksTxta,null);
			holdRemarksTxta.setData(bean);

			if(bean.getPreauthMedicalDecisionDetails().getHoldRemarks() != null){
				holdRemarksTxta.setValue(bean.getPreauthMedicalDecisionDetails().getHoldRemarks());
			}

			dynamicFrmLayout.addComponent(holdRemarksTxta);

			alignFormComponents();

			wholeVlayout.addComponents(dynamicFrmLayout,remarksLayout);

			mandatoryFields= new ArrayList<Component>();

			showOrHideValidation(false);

			wizard.getFinishButton().setEnabled(Boolean.TRUE);

			wizard.getNextButton().setEnabled(Boolean.FALSE);

		}
		
		public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

			ShortcutListener enterShortCut = new ShortcutListener(
					"ShortcutForHoldRemarks", ShortcutAction.KeyCode.F8, null) {

				private static final long serialVersionUID = 1L;
				@Override
				public void handleAction(Object sender, Object target) {
					((ShortcutListener) listener).handleAction(sender, target);
				}
			};
			handleShortcutForHoldRemarks(searchField, getShortCutListenerForHoldRemarks(searchField));

		}

		public  void handleShortcutForHoldRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
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
		
		private ShortcutListener getShortCutListenerForHoldRemarks(final TextArea txtFld)
		{
			ShortcutListener listener =  new ShortcutListener("Hold Remarks",KeyCodes.KEY_F8,null) {

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
					txtArea.setReadOnly(false);
					txtArea.setRows(25);

					txtArea.addValueChangeListener(new ValueChangeListener() {

						@Override
						public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());
							PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
							if(null != mainDto){
								mainDto.getPreauthMedicalDecisionDetails().setHoldRemarks(txtFld.getValue());
							}
						}
					});
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

					final Window dialog = new Window();
					String strCaption = "Hold Remarks";
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
	 	
	 	public void showSuperSurplusAlertList(List<SelectValue> superSurplusAlertList) {
			//batchCpuCountTable.init("Count For Cpu Wise", false, false);
			//batchCpuCountTable.setTableList(tableDTOList);
			Table table = new Table();
			/*table.setHeight("200px");
			table.setWidth("200px");*/
			//table.addContainerProperty("Sr.No.", String.class, null);
			table.addContainerProperty("Intimation No", String.class, null);
			table.addContainerProperty("Amount  Settled",  String.class, null);
			table.setPageLength(10);
			table.setSizeFull();
			table.setHeight("140%");
			int i = 0;
			for (SelectValue selectValue : superSurplusAlertList) {
				table.addItem(new Object[]{selectValue.getValue(),  selectValue.getCommonValue()}, i++);
			}
			
			Label proceedBtn = new Label();
			proceedBtn.setCaption("<b style = 'color: red; font-size: 150%;'>Proceed --</b>");
			proceedBtn.setCaptionAsHtml(true);
			proceedBtn.setSizeFull();
//			proceedBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
			
			Button yesButton = new Button("Yes");
			yesButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			Button noButton = new Button("No");
			noButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			
			HorizontalLayout buttonLayout = new HorizontalLayout(proceedBtn, yesButton, noButton);
			buttonLayout.setSpacing(true);
			
			VerticalLayout layout = new VerticalLayout(table, buttonLayout);
			layout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setSizeFull();
			//layout.setStyleName("borderLayout");
			
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("<b style = 'color: red; size'>Maternity benefit settled  in earlier policies - </b>");
			popup.setCaptionAsHtml(true);
			popup.setWidth("30%");
			popup.setHeight("35%");
			popup.setContent(layout);
			popup.setClosable(false);
			popup.center();
			popup.setResizable(false);
			yesButton.setData(popup);
			noButton.setData(popup);
			
			yesButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					Window box = (Window)event.getButton().getData();
					box.close();
					Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());
					if(validatePolicyStatus(bean.getPolicyDto().getPolicyNumber()) || (intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y"))){
						if (!bean.getIsReverseAllocation() && !medicalDecisionTableObj.getTotalAmountConsidered().equals(
								SHAUtils.getIntegerFromString(bean.getAmountConsidered()))) {
							String msg = "Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table </br>";
							showErrorMessage(msg);
						}else{
							if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
								alertForAdditionalFvrTriggerPoints(SHAConstants.APPROVAL);
							}
							else{
								if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement()){
									reConfirmMessageForBenefitSheet();
								}else{
									bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);
									fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
								}
							}
							
						}
					} else {
						showErrorPageForCancelledPolicy();
					}
				}
			});
			
			noButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727955L;

				@Override
				public void buttonClick(ClickEvent event) {
					Window box = (Window)event.getButton().getData();
					box.close();
//					if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						approveBtn.setEnabled(false);
//					}	
					
					
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
			// TODO Auto-generated method stub
	 	}
	 	
		private boolean doPPRejectionValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
			if(selectedRole != null && selectedRole.size() > 0){
				for(String key : roleList){
					if(selectedRole.containsKey(key)){
						roleAvailabilityFlag = true;
					}
					if(roleAvailabilityFlag){
						break;
					}
				}
				
				if(!roleAvailabilityFlag){
					System.out.println("Required Department Role is not Selected / not Available.");
					hasError = true;
					errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor"+"</br>"+"(Medical Head / Unit Head)"+"</br>");
				}
				
				if(!hasError){
					if(selectedUser != null){
						roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
						for(String key : roleList){
							if(selectedUser.containsKey(key)){
								roleAvailabilityFlag = true;
							}
						}
						
						if(!roleAvailabilityFlag){
							System.out.println("Required Department User is not Selected / not Available.");
							hasError = true;
							errorMessages.add("User Selection is Mandatory, Please select User for the selected Department Heads"+"</br>");
						}
						
					}else{
						hasError = true;
						errorMessages.add("User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("Selected user Map is empty.......");
					}
				}
				
			}else{
				hasError = true;
				errorMessages.add("Consulted With selection is Mandatory"+"</br>");
				System.out.println("Selected user Map is empty.......");
			}
			
			return hasError;
		}
			
	 	private boolean doPortedPolicyRejectionRoleClarityValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
	 		if(selectedRole.get("R00013") != null){
				for(String key : roleList){
					if(selectedRole.containsKey(key)){
						roleAvailabilityFlag = true;
					}
					if(roleAvailabilityFlag){
						break;
					}
				}

				if(!roleAvailabilityFlag){
					System.out.println("Senior Doctor Alone Selected.");
					hasError = true;
					errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor along with Department Heads like"+"</br>"+"(Medical Head / Unit Head)"+"</br>");
				}

				if(!hasError){					
					if(selectedUser != null){
						if(selectedUser.get("R00013") == null){
							hasError = true;
							errorMessages.add("Senior Doctor User Selection is Mandatory, Please select Senior Doctor User"+"</br>");
						}
						roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
						if(selectedUser.get("R00013") != null){
							System.out.println("Senior Doctor is Selected");
							for(String key : roleList){
								if(selectedUser.containsKey(key)){
									roleAvailabilityFlag = true;
								}
								if(roleAvailabilityFlag){
									break;
								}
							}

							if(!roleAvailabilityFlag){
								System.out.println("Senior Doctor User Alone Selected.");
								hasError = true;
								errorMessages.add("User Selection is Mandatory, Please select User for the selected Senior Doctor and Department Heads"+"</br>");
							}
						}

						for (Map.Entry<String, String> entry : selectedUser.entrySet()){
							if(entry.getKey().equals("R00013") && StringUtils.isBlank(entry.getValue())){
								hasError = true;
								System.out.println("PP 00013: "+entry.getKey()+"<------>"+entry.getValue());
								errorMessages.add("Senior Doctor Opnion is Mandatory, please select Senior Doctor User in Opinion Given By"+"</br>");
								break;
							}
							if(!entry.getKey().equals("R00013") && StringUtils.isBlank(entry.getValue())){
								hasError = true;
								System.out.println("PP !00013: "+entry.getKey()+"<------>"+entry.getValue());
								errorMessages.add("Please select User in Opinion Given By for "+"</br>"+"(Medical Head /  Unit Head )"+"</br>");
								break;
							}
							System.out.println(entry.getKey() + "/" + entry.getValue());
						}
					}else{
						hasError = true;
						errorMessages.add("Senior Doctor and Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("PP : Selected user Map is empty.......");
					}
				}
			}else{
				hasError = true;
				errorMessages.add("Senior Doctor selection is mandatory, Please select the Senior Doctor in Consulted With"+"</br>");
				System.out.println("PP : Senior Doctor Not selected");
			}
	 		return hasError;
	 	}	
		
		private boolean doPortedPolicyRoleClarityValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
			if(selectedRole.get("R0005") != null){
				for(String key : roleList){
					if(selectedRole.containsKey(key)){
						roleAvailabilityFlag = true;
					}
					if(roleAvailabilityFlag){
						break;
					}
				}

				if(!roleAvailabilityFlag){
					System.out.println("Specialist Alone Selected.");
					hasError = true;
					errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor along with Specialist"+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head / SGM - Medical)"+"</br>");
				}

				if(!hasError){					
					if(selectedUser != null){
						if(selectedUser.get("R0005") == null){
							hasError = true;
							errorMessages.add("Specialist User Selection is Mandatory, Please select Specialist User"+"</br>");
						}
						roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
						if(selectedUser.get("R0005") != null){
							System.out.println("Specialist is Selected");
							for(String key : roleList){
								if(selectedUser.containsKey(key)){
									roleAvailabilityFlag = true;
								}
								if(roleAvailabilityFlag){
									break;
								}
							}

							if(!roleAvailabilityFlag){
								System.out.println("Specialist User Alone Selected.");
								hasError = true;
								errorMessages.add("User Selection is Mandatory, Please select User for the selected Specialist and Department Heads"+"</br>");
							}
						}

						for (Map.Entry<String, String> entry : selectedUser.entrySet()){
							if(entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
								hasError = true;
								System.out.println("PP 0005: "+entry.getKey()+"<------>"+entry.getValue());
								errorMessages.add("Specialist Opnion is Mandatory, please select Specialist User in Opinion Given By"+"</br>");
								break;
							}
							if(!entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
								hasError = true;
								System.out.println("PP !0005: "+entry.getKey()+"<------>"+entry.getValue());
								errorMessages.add("Please select User in Opinion Given By for "+"</br>"+"(Medical Head /  Division Head / Cluster Head )"+"</br>");
								break;
							}
							System.out.println(entry.getKey() + "/" + entry.getValue());
						}
					}else{
						hasError = true;
						errorMessages.add("Specialist and Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("PP : Selected user Map is empty.......");
					}
				}
			}else{
				//			R0001	MEDICAL HEAD
				//			R0002	UNIT HEAD
				//			R0003	DIVISION HEAD
				//			R0004	CLUSTER HEAD
				//			R0005	SPECIALIST
				//			R0006	ZONAL HEAD
				//			R00011	MEDICAL HEAD - GMC
				//			R00012	DEPUTY MEDICAL HEAD - GMC

				if((selectedRole.get("R0004") == null && selectedRole.get("R0001") == null) && (selectedRole.get("R0003") == null && selectedRole.get("R0001") == null)){
					hasError = true;
					errorMessages.add("Cluster Head/ Division Head selection is mandatory"+"</br>");
					System.out.println("PP : Cluster Head/ Division Head not selected");
				}

				if(!hasError && (selectedRole.get("R0004") != null && selectedRole.get("R0001") == null) || (selectedRole.get("R0004") == null && selectedRole.get("R0001") != null)){
					hasError = true;
					errorMessages.add("Intimation Decision has to be validated by Medical Head along with Cluster Head or Specialist"+"</br>");
					System.out.println("PP : Cluster Head/ Medical Head not selected");
				}

				if(!hasError && (selectedRole.get("R0004") == null && selectedRole.get("R0001") == null) && (selectedRole.get("R0003") != null && selectedRole.get("R0001") == null || (selectedRole.get("R0003") == null && selectedRole.get("R0001") != null))){
					hasError = true;
					errorMessages.add("Intimation Decision has to be validated by Medical Head along with Divisional Head or Specialist"+"</br>");
					System.out.println("PP : Division Head/ Medical Head not selected");
				}

				if(!hasError && (selectedRole.get("R0004") != null && selectedRole.get("R0001") != null) || (selectedRole.get("R0003") != null && selectedRole.get("R0001") != null)){
					if(selectedUser == null || selectedUser.size() == 0){
						hasError = true;
						errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("PP : Division Head/ Medical Head not selected");
					}else if(selectedUser != null){
						if((selectedUser.get("R0004") != null) && (selectedUser.get("R0001") == null)){
							hasError = true;
							errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
							System.out.println("PP : Division Head/ Medical Head not selected");
						}

						if(!hasError &&  (selectedUser.get("R0004") == null) && (selectedUser.get("R0001") != null)){
							hasError = true;
							errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
							System.out.println("PP : Division Head/ Medical Head not selected");
						}

						if(!hasError &&  (selectedUser.get("R0004") == null && selectedUser.get("R0001") == null) && (selectedUser.get("R0003") != null) && (selectedUser.get("R0001") == null)){
							hasError = true;
							errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
							System.out.println("PP : Division Head/ Medical Head not selected");
						}

						if(!hasError &&  (selectedUser.get("R0004") == null && selectedUser.get("R0001") == null) && (selectedUser.get("R0003") == null) && (selectedUser.get("R0001") != null)){
							hasError = true;
							errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
							System.out.println("PP : Division Head/ Medical Head not selected");
						}

					}
				}
			}
			return hasError;
		}
		
		private boolean doRoleClarityValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
			if(selectedRole.get("R0005") != null){
				System.out.println("Specialist is Selected");
				for(String key : roleList){
					if(selectedRole.containsKey(key)){
						roleAvailabilityFlag = true;
					}
					if(roleAvailabilityFlag){
						break;
					}
				}
				
				if(!roleAvailabilityFlag){
					System.out.println("Specialist Alone Selected.");
					hasError = true;
					errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor along with Specialist"+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head / SGM-Medical)"+"</br>");
				}
				
				if(!hasError){					
					if(selectedUser != null){
						if(selectedUser.get("R0005") == null){
							hasError = true;
							errorMessages.add("Specialist User Selection is Mandatory, Please select Specialist User"+"</br>");
						}
						roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
						if(selectedUser.get("R0005") != null){
							System.out.println("Specialist is Selected");
							for(String key : roleList){
								if(selectedUser.containsKey(key)){
									roleAvailabilityFlag = true;
								}
								if(roleAvailabilityFlag){
									break;
								}
							}
							
							if(!roleAvailabilityFlag){
								System.out.println("Specialist User Alone Selected.");
								hasError = true;
								errorMessages.add("User Selection is Mandatory, Please select User for the selected Specialist and Department Heads"+"</br>");
							}
						}
						
						
						for (Map.Entry<String, String> entry : selectedUser.entrySet()){
							if(entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
								hasError = true;
								System.out.println("0005: "+entry.getKey()+"<------>"+entry.getValue());
								errorMessages.add("Specialist Opnion is Mandatory, please select Specialist User in Opinion Given By"+"</br>");
								break;
							}
							if(!entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
								hasError = true;
								System.out.println("! 0005: "+entry.getKey()+"<------>"+entry.getValue());
								errorMessages.add("Please select User in Opinion Given By for "+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head / SGM- Medical / Medical Head - GMC / Deputy Medical Head - GMC)"+ "</br>");
								break;
							}
							System.out.println(entry.getKey() + "/" + entry.getValue());
						}
					}else{
						hasError = true;
						errorMessages.add("Specialist and Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("Selected user Map is empty.......");
					}
				}
			}else{
				if(selectedRole != null && selectedRole.size() > 0){
					for(String key : roleList){
						if(selectedRole.containsKey(key)){
							roleAvailabilityFlag = true;
						}
						if(roleAvailabilityFlag){
							break;
						}
					}
					
					if(!roleAvailabilityFlag){
						System.out.println("Required Department Role is not Selected / not Available.");
						hasError = true;
						errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor"+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head / SGM- Medical)"+"</br>");
					}
					
					if(!hasError){
						if(selectedUser != null){
							roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
							for(String key : roleList){
								if(selectedUser.containsKey(key)){
									roleAvailabilityFlag = true;
								}
							}
							
							if(!roleAvailabilityFlag){
								System.out.println("Required Department User is not Selected / not Available.");
								hasError = true;
								errorMessages.add("User Selection is Mandatory, Please select User for the selected Department Heads"+"</br>");
							}
							
						}else{
							hasError = true;
							errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
							System.out.println("Selected user Map is empty.......");
						}
					}
					
				}else{
					hasError = true;
					errorMessages.add("Consulted With selection is Mandatory"+"</br>");
					System.out.println("Selected user Map is empty.......");
				}
			}
			
			return hasError;	
		}

		public Boolean alertMessageForPED(String message) {/*
	   		Label successLabel = new Label(
					"<b style = 'color: red;'>" + message + "</b>",
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
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();					
				}
			});
			return true;
		*/

	   		final MessageBox showAlert = showAlertMessageBox(message);
	   		Button homeButton = showAlert.getButton(ButtonType.OK);
	   		
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					showAlert.close();					
				}
			});
			return true;
			
		}
		
		//R1256
		private HorizontalLayout buildEvidenceObtainedFrom(){

			chkSubDoc = binder.buildAndBind("Submitted Documents",	"chkSubmittedDoc", CheckBox.class);
			//Vaadin8-setImmediate() chkSubDoc.setImmediate(true);

			chkFVR = binder.buildAndBind("Field Visit Report",	"chkFieldVisitReport", CheckBox.class);
			//Vaadin8-setImmediate() chkFVR.setImmediate(true);

			chkIR = binder.buildAndBind("Investigation Report",	"chkInvestigationReport", CheckBox.class);
			//Vaadin8-setImmediate() chkIR.setImmediate(true);

			chkOthers = binder.buildAndBind("Others", "chkOthers", CheckBox.class);
			//Vaadin8-setImmediate() chkOthers.setImmediate(true);

			txtaRemarks = binder.buildAndBind("<b style='font-size: 13.5px;'>Remarks for Others<b style='color:red; font-size: 12.5px; font-weight:600;'> * </b> </b>",	"txtaOthersRemarks", TextArea.class);
			txtaRemarks.setCaptionAsHtml(true);
			txtaRemarks.setId("evidenceRemarks");
			txtaRemarks.setMaxLength(4000);
			txtaRemarks.setColumns(17);
			txtaRemarks.setRows(3);
			remarksPopupListener(txtaRemarks,null);
			if(bean.getPreauthMedicalDecisionDetails().getChkOthers()){
				txtaRemarks.setVisible(true);
			}else{
				txtaRemarks.setVisible(false);
			}

			chkOthers.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					boolean value = (Boolean) event.getProperty().getValue();
					if(value){
						txtaRemarks.setVisible(true);
					}else{
						bean.getPreauthMedicalDecisionDetails().setTxtaOthersRemarks(null);
						txtaRemarks.setValue("");
						txtaRemarks.setVisible(false);
					}
				}
			});

			FormLayout fOne = new FormLayout(chkSubDoc, chkIR);
			FormLayout fTwo = new FormLayout(chkFVR, chkOthers);
			FormLayout fThree = new FormLayout(txtaRemarks);

			HorizontalLayout holderL =  new HorizontalLayout(fOne,fTwo,fThree);
			holderL.setSpacing(true);
			return holderL;
		}
		//R1261 - Start
		private void enhancementApproveAction(){
			Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());
			Boolean isStopProcess = SHAUtils.alertMessageForStopProcess(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),getUI());
			if(!isStopProcess){
				
				 NegotiationDetails negotiationPending = preauthService.getNegotiationPending(bean.getClaimDTO().getKey());
				if(negotiationPending != null){
				   manageNegotation(intimationDtls,negotiationPending,SHAConstants.APPROVAL);
				}else{

					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}
	
					SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
					if (ReferenceTable.getSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							&& sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null 
							&& sectionDetailsDTO.getCover().getCommonValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE)
							&& null != bean.getSuperSurplusAlertList() && bean.getSuperSurplusAlertList().size() > 0 ) {
						showSuperSurplusAlertList(bean.getSuperSurplusAlertList());
					}else {
						if(validatePolicyStatus(bean.getPolicyDto().getPolicyNumber()) || (intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y"))){
							if (!bean.getIsReverseAllocation() && !medicalDecisionTableObj.getTotalAmountConsidered().equals(
									SHAUtils.getIntegerFromString(bean.getAmountConsidered()))) {
								String msg = "Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table </br>";
								showErrorMessage(msg);
							}else{
								if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
									alertForAdditionalFvrTriggerPoints(SHAConstants.APPROVAL);
								}
								else{
									if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement() && 
											(bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy())){
										reConfirmMessageForBenefitSheet();
									}else{
										bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);
										fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
									}
								}
	
							}
						} else {
							showErrorPageForCancelledPolicy();
						}
					}
				}
			}
		}
		
		private void enhancementQueryAction(){
			if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
				alertForAdditionalFvrTriggerPoints(SHAConstants.QUERY);
			}
			else{
				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_QUERY_BUTTON_EVENT,null);
			}
		}
		
		private void enhancementRejectAction(){
			if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
				alertForAdditionalFvrTriggerPoints(SHAConstants.REJECT);
			}
			else{
				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_REJECTION_EVENT,bean);	
			}
		}
		
		private void enhancementWithdrawPreAuthAction(){
			bean.getPreauthMedicalDecisionDetails().setUserClickAction("Withdraw Pre-auth");
//			fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_WITHDRAW_EVENT,null);     // CR R1313
			fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_WITHDRAW_EVENT, bean.getNewIntimationDTO().getPolicy().getProduct().getKey());
		}
		
		private void enhancementWithdrawRejectAction(){
			bean.getPreauthMedicalDecisionDetails().setUserClickAction("Withdraw & Reject");
			fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_WITHDRAW_AND_REJECT_EVENT,bean);
		}
		
		private void enhancementDownsizePreAuthAction(){
			if (!bean.getIsReverseAllocation() && !medicalDecisionTableObj.getTotalAmountConsidered().equals(
					SHAUtils.getIntegerFromString(bean.getAmountConsidered()))) {
				String msg = "Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table </br>";
				showErrorMessage(msg);
			}else{
				Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());
				 NegotiationDetails negotiationPending = preauthService.getNegotiationPending(bean.getClaimDTO().getKey());
				    if(negotiationPending != null){
				    	manageNegotation(intimationDtls,negotiationPending,SHAConstants.DOWNSIZE_PRE_AUTH);
				    }
				    else{Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
						if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
								bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
							Long definedLimt = null != bean.getDeductibleAmount() ? (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount().intValue() : 300000l): 0l;
							Integer otherInsurerAdmissibleAmt = bean.getOtherInsurerAdmissibleAmt() != null ? bean.getOtherInsurerAdmissibleAmt() : 0;
							if(null != bean.getAmountConsidered() &&  Double.valueOf(bean.getAmountConsidered() + otherInsurerAdmissibleAmt) > definedLimt){
								bean.getPreauthMedicalDecisionDetails().setUserClickAction("Downsize Pre-auth");
//								fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,null);     // CR R1313
								fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,product.getKey());
							}else{
								showErrorMsg();
							}
						}else{
							bean.getPreauthMedicalDecisionDetails().setUserClickAction("Downsize Pre-auth");
//							fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,null);     // CR R1313
							fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,product.getKey());
						}
			 }
			}
		}
		//R1261 - End
		
		public MessageBox showAlertMessageBox(String message){
			
			
			final MessageBox msgBox = MessageBox
				    .createWarning()
				    .withCaptionCust("Warning")
				    .withMessage(message)
				    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				    .open();
			
			return msgBox;
			
			
		}
		public MessageBox showInfoMessageBox(String message){
			
			
			final MessageBox msgBox = MessageBox
				    .createInfo()
				    .withCaptionCust("Information")
				    .withMessage(message)
				    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				    .open();
			
			return msgBox;
			
			
		}
		
		public void showNagotiationRemarks(final Boolean isCancel,final Intimation intimationDtls
				,final NegotiationDetails negotiation,final String decision){

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
			txtArea.setNullRepresentation("");
			if(!isCancel){
				txtArea.setValue(negotiation.getNegotiationRemarks());
			}
			//txtArea.setSizeFull();
			
			
//			if(("hospRmrks").equalsIgnoreCase(txtFld.getId()) || ("insuredRmrks").equalsIgnoreCase(txtFld.getId())){
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				dialog.setHeight("65%");
		    	dialog.setWidth("65%");
				txtArea.setReadOnly(false);
//			}
			
				final TextField amtToNegotiatedFild = new TextField("Amount to be Negotiated");
				amtToNegotiatedFild.setValue(negotiation.getNegotiationAmt() != null ? String.valueOf(negotiation.getNegotiationAmt()) : "0");
				FormLayout negAmt = new FormLayout(amtToNegotiatedFild);
				negAmt.setVisible(false);
				
			
			
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(negAmt);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
			
			
			String strCaption = "";
			
			if(isCancel){
				strCaption = "Cancel Remarks";
				negAmt.setVisible(false);
			}else{
				strCaption = "Update Remarks";
				negAmt.setVisible(true);
			}
			dialog.setCaption(strCaption);
					
			
			dialog.setClosable(true);
			
			dialog.setContent(vLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.setDraggable(true);
			
			dialog.addCloseListener(new Window.CloseListener() {
				
				@Override
				public void windowClose(CloseEvent e) {
					if(null != decision && SHAConstants.APPROVAL.equalsIgnoreCase(decision)){
						callApproveLayout(intimationDtls);
					}
					else if(null != decision && SHAConstants.DOWNSIZE_PRE_AUTH.equalsIgnoreCase(decision)){

				    	Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
						if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
								bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
							Long definedLimt = null != bean.getDeductibleAmount() ? (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount().intValue() : 300000l): 0l;
							if(null != bean.getAmountConsidered() &&  Double.valueOf(bean.getAmountConsidered()) > definedLimt){
								bean.getPreauthMedicalDecisionDetails().setUserClickAction("Downsize Pre-auth");
//								fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,null);     // CR R1313
								fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,product.getKey());
							}else{
								showErrorMsg();
							}
						}else{
							bean.getPreauthMedicalDecisionDetails().setUserClickAction("Downsize Pre-auth");
//							fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,null);     // CR R1313
							fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,product.getKey());
						}
					}

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
					if(txtArea.getValue() != null && ! txtArea.getValue().isEmpty() && !StringUtils.isBlank(txtArea.getValue())){
						if(isCancel){
							Status status = new Status();
							status.setKey(ReferenceTable.NEGOTIATION_CANCELLED);
							negotiation.setStatusId(status);
							negotiation.setNegotiationCancelRemarks(txtArea.getValue());
							bean.getPreauthMedicalDecisionDetails().setIsNegotiationCancelled(Boolean.TRUE);							
						}else{
							negotiation.setCashlessKey(bean.getKey());
							negotiation.setNegotiationRemarks(txtArea.getValue());
							negotiation.setNegotiationAmt(Double.valueOf(amtToNegotiatedFild.getValue()));
							bean.getPreauthMedicalDecisionDetails().setIsNegotiationCancelled(Boolean.FALSE);
						}
						fireViewEvent(PreauthEnhancemetWizardPresenter.NEGOTIATION_CANCEL_OR_UPDATE, negotiation);
						bean.getPreauthMedicalDecisionDetails().setIsNegotiationDone(Boolean.TRUE);
						SHAUtils.buildNegotiationSuccessLayout(getUI(),isCancel);
						
						if(null != decision && SHAConstants.APPROVAL.equalsIgnoreCase(decision)){
							callApproveLayout(intimationDtls);
						}
						else if(null != decision && SHAConstants.DOWNSIZE_PRE_AUTH.equalsIgnoreCase(decision)){

					    	Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
							if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
									bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
								Long definedLimt = null != bean.getDeductibleAmount() ? (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount().intValue() : 300000l): 0l;
								if(null != bean.getAmountConsidered() &&  Double.valueOf(bean.getAmountConsidered()) > definedLimt){
									bean.getPreauthMedicalDecisionDetails().setUserClickAction("Downsize Pre-auth");
//									fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,null);     // CR R1313
									fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,product.getKey());
								}else{
									showErrorMsg();
								}
							}else{
								bean.getPreauthMedicalDecisionDetails().setUserClickAction("Downsize Pre-auth");
//								fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,null);     // CR R1313
								fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,product.getKey());
							}
						}
	
						dialog.close();
					}else{
						showErrorMessage("Please Enter Remarks");
					}
				}
			});	
		
		}
		protected void addNegotiationListener() {
			negotiationOpinionTaken
			.addValueChangeListener(new Property.ValueChangeListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					Boolean isChecked = false;
					if (event.getProperty() != null && null != event.getProperty().getValue()
							&& event.getProperty().getValue().toString() == "true") {
						isChecked = true;
					}

					genertateFieldsBasedOnNegotiation(isChecked);
				}
			});
		}
		
		@SuppressWarnings("unchecked")
		public void genertateFieldsBasedOnNegotiation(Boolean isChecked) {
			if (isChecked) {
				int componentIndex = dynamicFrmLayout.getComponentIndex(negotiationOpinionTaken);
				txtNegotiationRemarks = (TextArea) binder.buildAndBind("Points to Negotiate", "negotiatePoints", TextArea.class);
				txtNegotiationRemarks.setMaxLength(4000);
				txtNegotiationRemarks.setWidth("45%");	
				
				amtToNegotiate = (TextField) binder.buildAndBind("Amount to be Negotiated", "amtToNegotiated", TextField.class);
				CSValidator txtAmtToNeg = new CSValidator();
				txtAmtToNeg.setRegExp("^[0-9']*$");
				txtAmtToNeg.setPreventInvalidTyping(true);
				txtAmtToNeg.extend(amtToNegotiate);
				
				setRequiredAndValidation(txtNegotiationRemarks);
				setRequiredAndValidation(amtToNegotiate);
				//negotiationLayout.addComponent(txtNegotiationRemarks);
				dynamicFrmLayout.addComponent(amtToNegotiate,++componentIndex);
				dynamicFrmLayout.addComponent(txtNegotiationRemarks, ++componentIndex);
				mandatoryFields.add(txtNegotiationRemarks);	
				mandatoryFields.add(amtToNegotiate);
				if(null != bean.getIsNegotiationApplicable() && bean.getIsNegotiationApplicable()){
					if(bean.getIsNegotiationPending()){
						txtNegotiationRemarks.setReadOnly(false);
						txtNegotiationRemarks.setValue(bean.getPreauthMedicalDecisionDetails().getNegotiatePoints());
						txtNegotiationRemarks.setReadOnly(true);
						amtToNegotiate.setReadOnly(false);
						amtToNegotiate.setValue(bean.getPreauthMedicalDecisionDetails().getAmtToNegotiated());
						amtToNegotiate.setReadOnly(true);
					}
				}
				SHAUtils.handleTextAreaPopUp(txtNegotiationRemarks,null,getUI(),SHAConstants.STP_REMARKS,bean);
			} else {
				if (txtNegotiationRemarks != null) {
					unbindField(txtNegotiationRemarks);
					unbindField(amtToNegotiate);
					dynamicFrmLayout.removeComponent(txtNegotiationRemarks);
					dynamicFrmLayout.removeComponent(amtToNegotiate);
					mandatoryFields.remove(txtNegotiationRemarks);
					mandatoryFields.remove(amtToNegotiate);
				}
			}
		}

		//R1261 - End
		
		public void processNegotation(){
				
				ConfirmDialog dialog = ConfirmDialog.show(UI.getCurrent(),"Confirmation", "Have you made Negotiation on this claim ?",
				        "No", "Yes", new ConfirmDialog.Listener() {
		
				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	 dialog.close();
				                	 bean.setNegotiationMade(true);
				                	 enhancementApproveAction();
				                	 
				                } else {
				                    dialog.close();
				                    bean.setNegotiationMade(false);
				                    enhancementApproveAction();
				                   
				                }
				            }
				        });
				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
				
		}		
		
		public void showErrorMsgForNegotiation(String eMsg){
			
			Label label = new Label("Entered amount is greater than Claimed amount", ContentMode.HTML);
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

		private void setRequiredAndValidation(Component component) {
			AbstractField<Field> field = (AbstractField<Field>) component;
			field.setRequired(true);
			field.setValidationVisible(false);
		}
			
		public void showErrorMsgForNegotiationAmount(){
			
			Label label = new Label("Entered Negotiated Amount is less than the pre-auth approved.", ContentMode.HTML);
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
		
		public void buildWithdrawAndRejectLayout(Object withdrawRejectionDropDownValues)
		{
			initBinder();
			unbindAndRemoveComponents(dynamicFrmLayout);
			remarksLayout.removeAllComponents();
			wholeVlayout.removeComponent(remarksLayout);
			
			addingSentToCPUFields();
			
			// WITHDRAW FIELDS
			
			cmbWithdraw = (ComboBox) binder.buildAndBind("Reason for Reject & Withdraw","withdrawReason",ComboBox.class);
			cmbWithdraw.setContainerDataSource((BeanItemContainer<SelectValue>)withdrawRejectionDropDownValues);
			cmbWithdraw.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbWithdraw.setItemCaptionPropertyId("value");

			
			rejSubCategCmb = (ComboBox) binder.buildAndBind("Rejection Subcategory","rejSubCategory",ComboBox.class);
			rejSubCategCmb.setVisible(false);
			
			
			rejCondtNoTxt = (TextField) binder.buildAndBind("Condition Number","policyConditionNoReject",TextField.class);
			rejCondtNoTxt.setWidth("55px");
			rejCondtNoTxt.setMaxLength(10);
			rejCondtNoTxt.setVisible(false);
			rejCondtNoTxt.setValidationVisible(false);
			
			//cmbWithdraw.setWidth("300px");
			
			addWithDrawListenerListener();
			mandatoryFields= new ArrayList<Component>();
			
			txtWithdraw = (TextArea) binder.buildAndBind("Reject & Withdrawal Remarks<br>For Hospital", "withdrawRemarks", TextArea.class);
			txtWithdraw.setCaptionAsHtml(Boolean.TRUE);
			txtWithdraw.setData(bean);
			txtWithdraw.setMaxLength(4000);
			txtWithdraw.setWidth("50%");
			txtWithdraw.setHeight("250px");
			txtWithdraw.setId("hospitalWithdrawRmrks");
			txtWithdraw.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
			remarksPopupListener(txtWithdraw,null);
			
			if(bean.getPreauthMedicalDecisionDetails().getWithdrawReason() != null){
				cmbWithdraw.setValue(bean.getPreauthMedicalDecisionDetails().getWithdrawReason());
				
				if(ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_ENH.equals(bean.getPreauthMedicalDecisionDetails().getWithdrawReason().getId())) {
					
					rejSubCategCmb.setVisible(true);
				}
				else {
					rejSubCategCmb.setVisible(false);
					mandatoryFields.remove(rejSubCategCmb);
				}
				
				if((!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
						&& SHAUtils.getPolCondionalExclutionRejKeys().containsKey(bean.getPreauthMedicalDecisionDetails().getWithdrawReason().getId()))) {
			
					rejCondtNoTxt.setVisible(true);
				}
				else {
					rejCondtNoTxt.setVisible(false);
					mandatoryFields.remove(rejCondtNoTxt);
				}
			
				if(bean.getPreauthMedicalDecisionDetails().getRejSubCategory() != null){
					
					rejSubCategCmb.setValue(bean.getPreauthMedicalDecisionDetails().getRejSubCategory());
				}
			}
			
			
			if(bean.getPreauthMedicalDecisionDetails().getWithdrawRemarks() != null){
				txtWithdraw.setValue(bean.getPreauthMedicalDecisionDetails().getWithdrawRemarks());
			}
			
			withDrawRemarksForInsuredTxta = (TextArea) binder.buildAndBind("Rejection & Withdraw Remarks<br>For Insured","withDrawRemarksForInsured",TextArea.class);
			withDrawRemarksForInsuredTxta.setCaptionAsHtml(Boolean.TRUE);
			withDrawRemarksForInsuredTxta.setMaxLength(4000);
			withDrawRemarksForInsuredTxta.setWidth("50%");		
			withDrawRemarksForInsuredTxta.setHeight("250px");
			withDrawRemarksForInsuredTxta.setId("insuredWithdrawRmrks");
			withDrawRemarksForInsuredTxta.setData(bean);
			withDrawRemarksForInsuredTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
			remarksPopupListener(withDrawRemarksForInsuredTxta,null);
			
			if(bean.getPreauthMedicalDecisionDetails().getWithDrawRemarksForInsured() != null){
				withDrawRemarksForInsuredTxta.setValue(bean.getPreauthMedicalDecisionDetails().getWithDrawRemarksForInsured());
			}
			
			dynamicFrmLayout.addComponent(cmbWithdraw);
			dynamicFrmLayout.addComponent(rejSubCategCmb);
			dynamicFrmLayout.addComponent(rejCondtNoTxt);
			
//			dynamicFrmLayout.addComponent(txtWithdraw);
//			dynamicFrmLayout.addComponent(sentToCPU);
			
			remarksLayout.addComponents(new FormLayout(txtWithdraw),new FormLayout(withDrawRemarksForInsuredTxta));
			
			remarksLayout.setMargin(Boolean.TRUE);
			
			VerticalLayout vLayout = new VerticalLayout();
			vLayout.addComponents(dynamicFrmLayout,remarksLayout);
			vLayout.setMargin(Boolean.FALSE);
			
			//R1295
			bean.setQueryType(null);
			bean.setQueryCount(0);
			userLayout = buildUserRoleLayout();
			userLayout.setMargin(Boolean.TRUE);
			HorizontalLayout hLayout = new HorizontalLayout();
			hLayout.addComponents(dynamicFrmLayout,userLayout);
			
			bonusVLayout.addComponent(hLayout);
			alignFormComponents();
			bean.setIsNegotiationApplicable(Boolean.FALSE);
			
			wholeVlayout.addComponents(bonusVLayout,remarksLayout);
			mandatoryFields.add(cmbWithdraw);
			mandatoryFields.add(txtWithdraw);
			showOrHideValidation(false);
			
			wizard.getFinishButton().setEnabled(false);
			wizard.getNextButton().setEnabled(true);			
			
		}
		
		/*public void confirmMessageForTopUp(){
			final MessageBox getConf = MessageBox
				    .createQuestion()
				    .withCaptionCust("Confirmation")
				    .withMessage("Do you want to create Intimation under Top-up policy?")
				    .withYesButton(ButtonOption.caption("Yes"))
				    .withNoButton(ButtonOption.caption("No"))
				    .open();
				Button homeButton=getConf.getButton(ButtonType.YES);
				homeButton.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						// TODO Auto-generated method stub
					 getConf.close();
	               	 fireViewEvent(PreauthEnhancemetWizardPresenter.ENHN_TOPUP_POLICY_EVENT,bean);
					}
				});
				Button cancelButton=getConf.getButton(ButtonType.NO);
				cancelButton.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {

	                    getConf.close();
	                
					}
				});
			
		}*/
		
		public void enableTopupPolicy(Boolean isEnabled){
			 ArrayList<String> topUpFlag = dbCalculationService.getTopUpAlertFlag(bean.getPolicyDto().getPolicyNumber());
			 if( !topUpFlag.isEmpty() && topUpFlag.size() > 2 && topUpFlag.get(2) != null &&
					 topUpFlag.get(2).equalsIgnoreCase(SHAConstants.ENABLE)){
				 topUpBtn.setEnabled(isEnabled);
			 }
		}
				
		public void setRejSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
			
			rejSubCategCmb.setVisible(true);
			rejSubCategCmb.setContainerDataSource((BeanItemContainer<SelectValue>)rejSubcategContainer);
			rejSubCategCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			rejSubCategCmb.setItemCaptionPropertyId("value");
			
			showOrHideValidation(false);

		}
		
		 private boolean doOpinionSDValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
			 if(selectedRole != null && selectedRole.size() > 0){
				 for(String key : roleList){
					 if(selectedRole.containsKey(key)){
						 roleAvailabilityFlag = true;
					 }
					 if(roleAvailabilityFlag){
						 break;
					 }
				 }

				 if(!roleAvailabilityFlag){
					 System.out.println("SDValidation Required Department Role is not Selected / not Available.");
					 hasError = true;
					 if(bean.getNewIntimationDTO().getClaimType().getValue().equals("Cashless")){
						 errorMessages.add("Intimation Decision has to be validated by Zonal Head / Unit Head"+"</br>");
					 }else{
						 errorMessages.add("Intimation Decision has to be validated by Unit Head"+"</br>");
					 }
				 }

				 if(!hasError){
					 if(selectedUser != null){
						 roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
						 for(String key : roleList){
							 if(selectedUser.containsKey(key)){
								 roleAvailabilityFlag = true;
							 }
						 }

						 if(!roleAvailabilityFlag){
							 System.out.println("SDValidation Required Department User is not Selected / not Available.");
							 hasError = true;
							 errorMessages.add("User Selection is Mandatory, Please select User for the selected department"+"</br>");
						 }

					 }else{
						 hasError = true;
						 errorMessages.add("User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						 System.out.println("SDValidation Selected user Map is empty.......");
					 }
				 }

			 }else{
				 hasError = true;
				 errorMessages.add("Consulted With selection is Mandatory"+"</br>");
				 System.out.println("SDValidation Selected user Map is empty outer.......");
			 }
			 return hasError;
		 }

		 public void setNegotiationSavedAmt(Long addedDCNAmt,Long currentClaimAmount) {

			 Long highestClaimedamount= preauthService.getHigestClaimamtbyintimationkey(bean.getNewIntimationDTO().getKey());
			 if(highestClaimedamount < currentClaimAmount){
				 highestClaimedamount = currentClaimAmount;
			 }
			 if(txthigestCLTransAmt != null){
				 txthigestCLTransAmt.setValue(highestClaimedamount.toString());
			 }
			 Long preauthApprovalAmt = 0L;
			 Long savedAmount =0L;
			 if(initialApprovedAmtTxt !=null 
					 && initialApprovedAmtTxt.getValue() != null){
				 preauthApprovalAmt = Long.parseLong(initialApprovedAmtTxt.getValue().replaceAll(",",""));
			 }
			 if(addedDCNAmt !=null){
				 preauthApprovalAmt += addedDCNAmt;
			 }
			 if(highestClaimedamount >= preauthApprovalAmt){
				 savedAmount =  highestClaimedamount - preauthApprovalAmt;
			 }
			 if(txtSavedAmt != null){
			 txtSavedAmt.setValue(savedAmount.toString());	
			 }
			 setTotalNegotiationSavedAmt();
		 }
		 
		 private BlurListener setNegotiationdiscListener(){

				BlurListener listener = new BlurListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(BlurEvent event){

						TextField value = (TextField) event.getComponent();
						if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
							Integer negotiationdisct = SHAUtils.getIntegerFromStringWithComma(value.getValue());
							Integer amountConsidered = SHAUtils.getIntegerFromStringWithComma(bean.getPreauthDataExtractionDetails().getAmtClaimed());
							if(negotiationdisct > amountConsidered) {
								String eMsg = "Entered  amount is greater than Claimed Amount";
								showErrorMsgForNegotiation(eMsg);
								txtNegotiationAmt.setValue("");
							} else {
								setTotalNegotiationSavedAmt();
							}
						}
					}
				};
				return listener;
			}

			private void setTotalNegotiationSavedAmt(){

				Long negotiationdisct =0L;
				Long savedAmount = 0L;
				Long totalNegotiationSavedAmt =0L;

				if(txtNegotiationAmt !=null &&
						txtNegotiationAmt.getValue()!=null){
					negotiationdisct = Long.parseLong(txtNegotiationAmt.getValue().replaceAll(",",""));
				}
				if(txtSavedAmt !=null &&
						txtSavedAmt.getValue()!=null){
					savedAmount = Long.parseLong(txtSavedAmt.getValue().replaceAll(",",""));
				}
				totalNegotiationSavedAmt = negotiationdisct + savedAmount;
				if(txtTotNegotiationSavedAmt !=null &&
						txtTotNegotiationSavedAmt.getValue()!=null){
				txtTotNegotiationSavedAmt.setValue(totalNegotiationSavedAmt.toString());
				}
			}
			
			public void setBalanceSumInsuredAlert(Long appAmount) 
			{
				NewIntimationDto intimationDTO = bean.getNewIntimationDTO();

				Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getLopFlag());

				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					bean = dbCalculationService.getBalanceSIForGMCAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(), appAmount.doubleValue(), bean);
				}else{
					if(ReferenceTable.getFHORevisedKeys().containsKey(intimationDTO.getPolicy().getProduct().getKey())
							|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
							(bean.getNewIntimationDTO().getPolicy().getProduct() != null && ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())))
							|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null && 
							(SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))
							|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null && (SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
							&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())))){
						if(bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null && 
								bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.INJURY_MASTER_ID) && bean.getPreauthDataExtractionDetails().getCauseOfInjury() != null && ReferenceTable.CAUSE_OF_INJURY_ACCIDENT_KEY.equals(bean.getPreauthDataExtractionDetails().getCauseOfInjury().getId())){
//							bean = dbCalculationService.getRTABalanceSIAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(), 0l).get(SHAConstants.TOTAL_BALANCE_SI);
						
						}
						else{
							bean = dbCalculationService.getBalanceSIAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(), insuredSumInsured,intimationDTO.getKey(), appAmount.doubleValue(), bean);	
						}

					}
					else if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(intimationDTO.getPolicy().getProduct().getKey())){
						String subCover = "";
						if(null != bean.getPreauthDataExtractionDetails() && null != bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() && null != bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover()){			
							subCover = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue();
						}
						bean = dbCalculationService.getBalanceSIForStarCancerGoldAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(), insuredSumInsured,intimationDTO.getKey(),subCover, appAmount.doubleValue(), bean);	
					}
					else{
						bean = dbCalculationService.getBalanceSIAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(), insuredSumInsured,intimationDTO.getKey(), appAmount.doubleValue(), bean);
					}
				}
				if (null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					showBonusView("Information - "+bean.getSiAlertDesc());
					/*SHAUtils.showMessageBoxWithCaption(bean.getSiAlertDesc(),"Verified Bonus");	*/						
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

			private void showRestrictedSIAlert(){

				if(bean.getPreauthDataExtractionDetails() !=null
						&& bean.getPreauthDataExtractionDetails().getDiagnosisTableList()!=null
						&& !bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()){
					for (DiagnosisDetailsTableDTO detailsTableDTO: bean.getPreauthDataExtractionDetails().getDiagnosisTableList()){
						if(detailsTableDTO.getSumInsuredRestriction() !=null
								&& detailsTableDTO.getSumInsuredRestriction().getValue() !=null){
							SHAUtils.showAlertMessageBox(SHAConstants.RESTRICTED_SIALERT);
							break;
						}
					}
				}
			}
			public void disableApproveAndDownsize(Boolean isEnabled) {
				approveBtn.setEnabled(isEnabled);
				downSizePreauthBtn.setEnabled(isEnabled);

				if(isEnabled){
					if(this.bean.isClsProsAllowed()){
						approveBtn.setEnabled(true);
						downSizePreauthBtn.setEnabled(isEnabled);
					}
					else{
						approveBtn.setEnabled(false);
						downSizePreauthBtn.setEnabled(isEnabled);
					}
				}
			}
			
			public void disableDownsize(Boolean isEnabled) {
				downSizePreauthBtn.setEnabled(isEnabled);
			}

			public void showAlertPopForGMCParentSIRestrict(){

				String message = SHAConstants.GMC_PARENT_RESTRICT_ALERT ;

				final MessageBox showInfo= showInfoMessageBox(message);
				Button homeButton = showInfo.getButton(ButtonType.OK);
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						showInfo.close();
					}
				});


			}
			
			 public void generateFieldsBasedOnSubCatTWO(BeanItemContainer<SelectValue> procedure) {
				 pCCRemarkAlertobj.generateFieldsBasedOnSubCatTWO(procedure);
			 }
			 
			 public void addpccSubCategory(BeanItemContainer<SelectValue> pccSubCatContainer) {
				 pCCRemarkAlertobj.addpccSubCategory(pccSubCatContainer);
			 }
	}

