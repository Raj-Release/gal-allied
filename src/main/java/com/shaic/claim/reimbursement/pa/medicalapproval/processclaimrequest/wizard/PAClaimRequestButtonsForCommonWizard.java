package com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.common.ApprovedTable;
import com.shaic.claim.common.BillEntryWizardStep;
import com.shaic.claim.common.RevisedMedicalDecisionTable;
import com.shaic.claim.fvrdetails.view.FVRTriggerPtsTable;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPageUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.AddAdditionalFVRPointsPageUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.PendingFvrInvsQueryPageUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsListenerTable;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision.PAHealthClaimRequestMedicalDecisionPagePresenter;
import com.shaic.paclaim.health.reimbursement.medicalapproval.wizard.PAHealthClaimRequestWizardPresenter;
import com.shaic.paclaim.reimbursement.service.PAReimbursementService;
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
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
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
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAClaimRequestButtonsForCommonWizard extends ViewComponent implements
Receiver, SucceededListener{


	private static final long serialVersionUID = 7089191150922046819L;

	@Inject
	private PreauthDTO bean;

	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;
	
	@Inject
	private Instance<BillEntryWizardStep> billEntrywizardStepInstance;
	
	@Inject
	private Instance<ApprovedTable> approvedTableWizardStepInstance;

	@Inject
	private ViewBillSummaryPage viewBillSummary;

	@Inject
	private ViewDetails viewDetails;

	@Inject
	private Intimation intimation;

	@Inject
	private Instance<UploadedDocumentsListenerTable> uploadDocumentListenerTable;

	private UploadedDocumentsListenerTable uploadDocumentListenerTableObj;

	@Inject
	private Instance<RevisedMedicalDecisionTable> medicalDecisionTable;

	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;
	
	@Inject
	private Instance<FVRTriggerPtsTable> triggerPtsTable;
	
	private FVRTriggerPtsTable triggerPtsTableObj;

	public AmountConsideredTable amountConsideredTable;

	private Window popup;

	private File file;

	private RevisedMedicalDecisionTable medicalDecisionTableObj;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ClaimService claimService;
	
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ReimbursementService reimbService;
	
	@Inject
	private PendingFvrInvsQueryPageUI invesFvrQueryPendingPageUI;
	
	@EJB
	private FieldVisitRequestService fvrService;
	
	@EJB
	private IntimationService intimationService;
	
	Map<String, Object> referenceData = new HashMap<String, Object>();

	private Button showBalanceSumInsured;

	private Button showClaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;

	private Button submitButton;

	public Button cancelButton;

	private Label amountConsidered;

	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;

	private Button queryBtn;
	private Button initiateFieldVisitBtn;
	private Button initiateInvestigationBtn;

	private VerticalLayout wholeVlayout;
	private TextField initialApprovedAmtTxt;
	private TextField initialTotalApprovedAmtTxt;
	private TextArea queryRemarksTxta;
	private ComboBox rejectionCategoryCmb;
	private Upload uploadFile;
	private TextField remarks;
	private TextArea medicalApproversReply;

	private ComboBox allocationTo;
	private ComboBox fvrPriority;
	private TextArea fvrTriggerPoints;
	private Label countFvr;
	private Button viewFVRDetails;

	private TextArea triggerPointsToFocus;
	private TextArea reasonForReferringIV;

	private TextArea remarksForCPU;

	private TextField txtQueryCount;

	private ArrayList<Component> mandatoryFields;

	private List<String> errorMessages;

	private GWizard wizard;

	private TextField approvedAmtField;

	private VerticalLayout optionCLayout;

	private TextField nonAllopathicTxt;
	
	@EJB
	private PEDQueryService pedQueryService;
	
	@EJB
	private MasterService masterService;
	
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	private Button viewClaimsDMSDocument;
	
	private HorizontalLayout viewAllDocsLayout = null;
	
	@Inject
	private InvestigationService investigationService;
	
	private TextField txtDefinedLimit;

	@Inject
	private ReimbursementService rodService;
	
	private FormLayout dynamicFrmLayout;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocRecvdService;
	
	private ComboBox cmbFVRNotRequiredRemarks;
	
	private VerticalLayout fvrVertLayout;
	
	private OptionGroup select;
	
	private TextArea fvrOthersRemarks;
	
	private VerticalLayout submainlayout;
	
	@Inject
	private AddAdditionalFVRPointsPageUI addAdditionalFvrPointsPageUI;
	private ComboBoxMultiselect cmbUserRoleMulti;
	private ComboBoxMultiselect cmbDoctorNameMulti;
	private ComboBox userRole;
	private ComboBox doctorName;
	private TextArea remarksFromDeptHead;
	
	FormLayout userLayout = new FormLayout();
	
	@Inject
	private Toolbar toolBar;

	private String screenName = "";
	
	@EJB
	private PAReimbursementService paReimbService;
	

	//CR2019179
	public Button getQueryBtn(){
		return queryBtn;
	}
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() {

	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	public void initView(PreauthDTO bean, GWizard wizard, String screenName) {
		errorMessages = new ArrayList<String>();
		mandatoryFields = new ArrayList<Component>();
		this.bean = bean;
		this.wizard = wizard;
		this.screenName = screenName;
		wholeVlayout = new VerticalLayout();
		wholeVlayout.setHeight("-1px");
		wholeVlayout.setWidth("-1px");
		wholeVlayout.setSpacing(true);
		HorizontalLayout buttonsHLayout = new HorizontalLayout();
		buttonsHLayout.setSizeFull();
		addListener();
		
		VerticalLayout verticalLayout = new VerticalLayout(initiateInvestigationBtn,
				initiateFieldVisitBtn,queryBtn);
		verticalLayout.setSpacing(true);
		buttonsHLayout.addComponents(verticalLayout);
		buttonsHLayout.setComponentAlignment(verticalLayout,
				Alignment.MIDDLE_CENTER);
		buttonsHLayout.setSpacing(true);
		wholeVlayout.addComponent(buttonsHLayout);
		dynamicFrmLayout = new FormLayout();
		dynamicFrmLayout.setHeight("100%");
		dynamicFrmLayout.setWidth("100%");
		dynamicFrmLayout.setMargin(true);
		dynamicFrmLayout.setSpacing(true);
		wholeVlayout.addComponent(dynamicFrmLayout);
		
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
			queryBtn.setEnabled(false);
			initiateFieldVisitBtn.setEnabled(false);
			initiateInvestigationBtn.setEnabled(false);
			bean.setIsFvrButtonDisabled(true);
			bean.setIsFvrClicked(Boolean.TRUE);
		} else {
			queryBtn.setEnabled(true);
			initiateFieldVisitBtn.setEnabled(true);
			initiateInvestigationBtn.setEnabled(true);
			bean.setIsFvrButtonDisabled(false);
			bean.setIsFvrClicked(Boolean.FALSE);
		}
		
		if(null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()){
			initiateFieldVisitBtn.setEnabled(false);
			initiateInvestigationBtn.setEnabled(false);
		}		
		setButtonDisabledForconsiderforPayment(this.bean.getIsConsiderForPaymentNo());
		
		this.viewClaimsDMSDocument = new Button("View All Documents");
		viewClaimsDMSDocument.setStyleName(ValoTheme.BUTTON_LINK);
		
		 viewAllDocsLayout = new HorizontalLayout();
		
		addViewAllDocsListener();

		setCompositionRoot(wholeVlayout);

	}

	private void addListener() {

		queryBtn = new Button("Query");
		queryBtn.setHeight("-1px");
		//Vaadin8-setImmediate() queryBtn.setImmediate(true);
		queryBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 4614951723748846970L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
					alertForAdditionalFvrTriggerPoints(SHAConstants.QUERY);
				}
				else
				{
					submitQueryEvent();
				}
			}
		});


		initiateFieldVisitBtn = new Button("Initiate Field Visit");
		initiateFieldVisitBtn.setHeight("-1px");
		//Vaadin8-setImmediate() initiateFieldVisitBtn.setImmediate(true);
		initiateFieldVisitBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
//				isApproval = false;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
//					Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
					
					bean.getPreauthMedicalDecisionDetails().setIsAllowInitiateFVR(preauthService.getFVRStatusByRodKey(bean.getKey()));
					
					if(screenName.equalsIgnoreCase("PAClaimRequestWizard")){
						
						if(bean.getPreauthMedicalDecisionDetails()!= null && bean.getPreauthMedicalDecisionDetails().getIsAllowInitiateFVR() != null && !bean.getPreauthMedicalDecisionDetails().getIsAllowInitiateFVR()){
							
							fireViewEvent(PAClaimRequestWizardPresenter.PA_CLAIM_REQUEST_FIELD_VISIT_EVENT,bean);
						}
						else{
//						sErrorPopUp("FVR request is in process cannot initiate another request");						
							fireViewEvent(PAClaimRequestWizardPresenter.PA_CLAIM_REQUEST_FIELD_VISIT_EVENT,bean);
						}
					}else if(screenName.equalsIgnoreCase("PAHealthClaimRequestWizard")){
						if(bean.getPreauthMedicalDecisionDetails()!= null && bean.getPreauthMedicalDecisionDetails().getIsAllowInitiateFVR() != null && !bean.getPreauthMedicalDecisionDetails().getIsAllowInitiateFVR()){

							fireViewEvent(PAHealthClaimRequestWizardPresenter.PA_HEALTH_CLAIM_REQUEST_FIELD_VISIT_EVENT,bean);
						}
						else{
							//						sErrorPopUp("FVR request is in process cannot initiate another request");						
							fireViewEvent(PAHealthClaimRequestWizardPresenter.PA_HEALTH_CLAIM_REQUEST_FIELD_VISIT_EVENT,bean);
						}
					}
				}else{
					alertMessageForPEDWatchList();
				}
			}
		});

		initiateInvestigationBtn = new Button("Initiate Investigation");
		initiateInvestigationBtn.setHeight("-1px");
		//Vaadin8-setImmediate() initiateInvestigationBtn.setImmediate(true);
		initiateInvestigationBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				//bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);

				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				Boolean investigationAvailable = false;
				Reimbursement reimbObj = ackDocRecvdService.getReimbursementByRodNo(bean.getRodNumber());
				
				investigationAvailable = investigationService.getInvestigationAvailableForClaim(reimbObj.getClaim().getKey());
				bean.setIsInvestigation(investigationAvailable);
				
				if(! bean.getIsPedWatchList()){
					if(! bean.getIsInvestigation()){

						boolean sendToAssignInv = false;
						
						String invBypassAllowed = dbCalculationService.bypassInvestigationAllowed(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
						sendToAssignInv = (SHAConstants.YES_FLAG).equalsIgnoreCase(invBypassAllowed) ? true : false;
						
						bean.setDirectToAssignInv(sendToAssignInv);
						
						if(!bean.getIsInvsRaised()){
							if(screenName.equalsIgnoreCase("PAClaimRequestWizard")){
								
								fireViewEvent(PAClaimRequestWizardPresenter.PA_CLAIM_REQUEST_INITIATE_INV_EVENT,bean);

							}else if(screenName.equalsIgnoreCase("PAHealthClaimRequestWizard")){
								
								fireViewEvent(PAHealthClaimRequestWizardPresenter.PA_HEALTH_CLAIM_REQUEST_INITIATE_INV_EVENT,bean);
							}
						}
						else{
							confirmationForInvestigation();	
						}
					}else if(bean.getIsInvestigation()){
						alertMessageForInvestigation();
					}	
				}
				else{
					alertMessageForPEDWatchList();
				}
			}
		});
		

		
		if (this.bean.getIsCancelPolicy()) {
			if (this.wizard != null) {
				this.wizard.getCancelButton().setEnabled(false);
			}

			queryBtn.setEnabled(false);
		}
	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(
				PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCancelPolicyProcess(PreauthDTO bean) {
		this.bean = bean;
		if (this.bean.getIsCancelPolicy()) {
			if (this.wizard != null) {
				this.wizard.getCancelButton().setEnabled(false);
			}
			queryBtn.setEnabled(false);
		}

	}


	private void showInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("");
		dialog.setClosable(true);

		Panel panel = new Panel();
		panel.setHeight("500px");
		panel.setWidth("850px");
		panel.setContent(layout);
		dialog.setContent(panel);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

	}

	private void showErrorPopup(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

	public void setApprovedAmtValue(Integer amount) {
		if (initialTotalApprovedAmtTxt != null) {
			initialTotalApprovedAmtTxt.setValue(amount.toString());
		}
	}
	
	private void addViewAllDocsListener()
	{
		if(null != viewClaimsDMSDocument)
		{
			if(null != viewClaimsDMSDocument)
			{
				viewClaimsDMSDocument.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						
						BPMClientContext bpmClientContext = new BPMClientContext();
						Map<String,String> tokenInputs = new HashMap<String, String>();
						 tokenInputs.put("intimationNo", bean.getNewIntimationDTO().getIntimationId());
						 String intimationNoToken = null;
						  try {
							  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
						  } catch (NoSuchAlgorithmException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  } catch (ParseException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						String url = bpmClientContext.getGalaxyDMSUrl() +intimationNoToken;
						/*Below code commented due to security reason
						String url = bpmClientContext.getGalaxyDMSUrl() +bean.getNewIntimationDTO().getIntimationId();*/
						//getUI().getPage().open(url, "_blank");
						getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);


						/*DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
						dmsDTO.setIntimationNo(bean.getNewIntimationDTO().getIntimationId());
						dmsDTO.setClaimNo(bean.getClaimDTO().getClaimId());
						dmsDTO.setDmsDocumentDetailsDTOList(bean.getDmsDocumentDTOList());
						popup = new com.vaadin.ui.Window();

						dmsDocumentDetailsViewPage.init(dmsDTO, popup);
						dmsDocumentDetailsViewPage.getContent();

						popup.setCaption("");
						popup.setWidth("75%");
						popup.setHeight("85%");
						//popup.setSizeFull();
						popup.setContent(dmsDocumentDetailsViewPage);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(true);
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
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOCUMENT_DTO,dmsDTO);
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOCUMENT_VIEW_PAGE,dmsDocumentDetailsViewPage);
						
						dmsDocumentViewListenerTable = dmsDocumentViewListenerTableObj.get();
						
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOC_TABLE,dmsDocumentViewListenerTable);*/
						//VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_PRESENTER_STRING,SHACON);
					//	getSession().setAttribute(SHAConstants.DMS_DOCUMENT_DTO,dmsDTO);
						
					/*	claimsDMSWindow = new BrowserWindowOpener(DMSDocumentViewUI.class);
						claimsDMSWindow.setWindowName("_blank");
						
						claimsDMSWindow.setFeatures("height=693,width=1300,resizable");
						claimsDMSWindow.extend(viewClaimsDMSDocument);*/
						//claimsDMSWindow.markAsDirty();
					//	UI.getCurrent().addWindow(popup);
						
					}
				});
			}
		}
	}


	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}


	@SuppressWarnings("rawtypes")
	private void unbindAndRemoveComponents(AbstractComponent component) {
		for (int i = 0; i < ((FormLayout) component).getComponentCount(); i++) {
			if (((FormLayout) component).getComponent(i) instanceof Upload) {
				continue;
			}
			unbindField((AbstractField) ((FormLayout) component)
					.getComponent(i));
		}
		dynamicFrmLayout.removeAllComponents();
		wholeVlayout.removeComponent(dynamicFrmLayout);

		if (null != wholeVlayout && 0 != wholeVlayout.getComponentCount()) {
			Iterator<Component> componentIterator = wholeVlayout.iterator();
			while (componentIterator.hasNext()) {
				Component searchScrnComponent = componentIterator.next();
				if (searchScrnComponent instanceof VerticalLayout) {
					((VerticalLayout) searchScrnComponent)
							.removeAllComponents();

				}
			}
		}
	}


	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (propertyId != null) {
				this.binder.unbind(field);
			}

		}
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			if (field != null) {
				field.setRequired(!isVisible);
				field.setValidationVisible(isVisible);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void alignFormComponents() {
		if (dynamicFrmLayout != null) {
			for (int i = 0; i < dynamicFrmLayout.getComponentCount(); i++) {
				dynamicFrmLayout.setExpandRatio(
						dynamicFrmLayout.getComponent(i), 0.5f);
			}
		}
	}



	public List<String> getErrors() {
		return this.errorMessages;
	}

	public void generateFieldsBasedOnSentToCPU(Boolean isChecked) {
		if (isChecked) {
			unbindField(remarksForCPU);
			remarksForCPU = (TextArea) binder.buildAndBind("Remarks for CPU",
					"remarksForCPU", TextArea.class);
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


	private Button getSubmitButtonWithListener(final ConfirmDialog dialog) {
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();
				if (isValid()) {
					if(screenName.equalsIgnoreCase("PAClaimRequestWizard")){
						
						fireViewEvent(PAClaimRequestWizardPresenter.PA_SUBMIT_PARALLEL_QUERY,bean);

					}else if(screenName.equalsIgnoreCase("PAHealthClaimRequestWizard")){
						
						fireViewEvent(PAHealthClaimRequestWizardPresenter.PA_HEALTH_SUBMIT_PARALLEL_QUERY,bean);
					}
					dialog.close();
					toolBar.countTool();
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error);
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		return submitButton;
	}

	@SuppressWarnings("unused")
	private Button getSubmitButtonWithDMS(final Button submitButton) {

		/*submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();

				if (isValid()) {
					uploadFile.submitUpload();
					//dialog.close();
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error);
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		return submitButton;
	}

	@SuppressWarnings("unchecked")
	public void buildInitiateFieldVisit(Object fieldVisitValues) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);

	    Map<String,BeanItemContainer<SelectValue>> map = (Map<String,BeanItemContainer<SelectValue>>)fieldVisitValues;
	
		allocationTo = (ComboBox) binder.buildAndBind("Allocation To","allocationTo",ComboBox.class);
		allocationTo.setContainerDataSource(map.get("allocationTo"));
		allocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		allocationTo.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo() != null){
			allocationTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo());
		}
		
		
		fvrPriority = (ComboBox) binder.buildAndBind("Priority","priority",ComboBox.class);
		fvrPriority.setContainerDataSource(map.get("fvrPriority"));
		fvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		fvrPriority.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getPriority()!= null){
			fvrPriority.setValue(this.bean.getPreauthMedicalDecisionDetails().getPriority());
		}

		
		triggerPtsTableObj = triggerPtsTable.get();
		triggerPtsTableObj.init();
		if(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList() != null && !bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList().isEmpty()){
			triggerPtsTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
		}
		viewFVRDetails = new Button("View FVR Details");
		viewFVRDetails.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getFVRDetails(bean.getNewIntimationDTO()
						.getIntimationId(), false);
			}
		});
		countFvr = new Label();
		if(bean.getFvrCount() != null){
			
			countFvr.setValue(bean.getFvrCount().toString() + " FVR Reports Already Exists");
		
		}
		
		FormLayout horizontalLayout = new FormLayout(countFvr, viewFVRDetails);
		
		HorizontalLayout horizontalLayout2 = new HorizontalLayout(new FormLayout(allocationTo,/*fvrAssignTo,*/fvrPriority), horizontalLayout);
		horizontalLayout2.setSpacing(true);
		
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(allocationTo);
		mandatoryFields.add(fvrPriority);
		showOrHideValidation(false);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setSpacing(true);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(horizontalLayout2, triggerPtsTableObj, btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		showInPopup(VLayout, dialog);
		
	}

	@SuppressWarnings("unchecked")
	public void buildInitiateInvestigation(Object fieldVisitValues) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);

		allocationTo = (ComboBox) binder.buildAndBind("Allocation To",
				"allocationTo", ComboBox.class);
		allocationTo
				.setContainerDataSource((BeanItemContainer<SelectValue>) fieldVisitValues);
		allocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		allocationTo.setItemCaptionPropertyId("value");

		if (this.bean.getPreauthMedicalDecisionDetails() != null
				&& this.bean.getPreauthMedicalDecisionDetails()
						.getAllocationTo() != null) {

			allocationTo.setValue(this.bean.getPreauthMedicalDecisionDetails()
					.getAllocationTo());
		}
		reasonForReferringIV = (TextArea) binder.buildAndBind(
				"Reason For Referring", "reasonForRefering", TextArea.class);
		reasonForReferringIV.setMaxLength(4000);
		reasonForReferringIV.setWidth("350px");
		triggerPointsToFocus = (TextArea) binder.buildAndBind(
				"Trigger Points To Focus", "triggerPointsToFocus",
				TextArea.class);
		triggerPointsToFocus.setMaxLength(4000);
		triggerPointsToFocus.setWidth("350px");

		viewFVRDetails = new Button("View Investigation Details");
		viewFVRDetails.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean != null && bean.getNewIntimationDTO() != null
						&& bean.getNewIntimationDTO().getIntimationId() != null) {
					viewDetails.getInvestigationDetails(bean
							.getNewIntimationDTO().getIntimationId(), false);
				}
			}
		});
		countFvr = new Label();
		countFvr.setValue(bean.getInvestigationSize()
				+ " Investigation reports already exists");

		VerticalLayout horizontalLayout = new VerticalLayout(countFvr,
				viewFVRDetails);
		horizontalLayout.setComponentAlignment(countFvr,
				Alignment.MIDDLE_CENTER);
		horizontalLayout.setComponentAlignment(viewFVRDetails,
				Alignment.MIDDLE_CENTER);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);

		HorizontalLayout horizontalLayout2 = new HorizontalLayout(
				new FormLayout(allocationTo, reasonForReferringIV,
						triggerPointsToFocus), horizontalLayout);
		horizontalLayout2.setMargin(true);
		horizontalLayout2.setSpacing(true);

		alignFormComponents();
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(allocationTo);
		mandatoryFields.add(reasonForReferringIV);
		mandatoryFields.add(triggerPointsToFocus);
		showOrHideValidation(false);

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);

		HorizontalLayout btnLayout = new HorizontalLayout(
				submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("400px");
		btnLayout.setComponentAlignment(submitButtonWithListener,
				Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);

		VerticalLayout VLayout = new VerticalLayout(horizontalLayout2,
				btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
//		VLayout.setWidth("800px");
		VLayout.setHeight("350px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);

	}

	public void suggestApprovalClick(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		setAppropriateValuesToDTOFromProcedure(dto, medicalDecisionTableValues);

	}

	@SuppressWarnings("unchecked")
	public void setAppropriateValuesToDTOFromProcedure(
			DiagnosisProcedureTableDTO medicalDecisionDto,
			Map<String, Object> values) {
		if(bean.getIsNonAllopathic()) {
			bean.setNonAllopathicOriginalAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
			bean.setNonAllopathicUtilizedAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
//			createNonAllopathicFields((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT), (Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
		}
		
		medicalDecisionDto.setAvailableAmout(((Double) values
				.get("restrictedAvailAmt")).intValue());
		medicalDecisionDto.setUtilizedAmt(((Double) values
				.get("restrictedUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAmount(((Double) values.get("currentSL"))
				.intValue() > 0 ? (String.valueOf(((Double) values
				.get("currentSL")).intValue())) : "NA");
		medicalDecisionDto.setSubLimitUtilAmount(((Double) values
				.get("SLUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
				.get("SLAvailAmt")).intValue());
		medicalDecisionDto.setCoPayPercentageValues((List<String>) values
				.get("copay"));
		
		

		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_DELITE)) {
			Integer subLimitAvaliableAmt = 0;
			Boolean isResidual = false;
			if(medicalDecisionDto.getDiagnosisDetailsDTO() != null && medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName() != null && (medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
			} else if (medicalDecisionDto.getProcedureDTO() != null && medicalDecisionDto.getProcedureDTO().getSublimitName() != null && (medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
			} else {
				isResidual = true;
			}
			
			if(!isResidual) {
				Integer entitlementNoOfDays = SHAUtils.getEntitlementNoOfDays(bean.getUploadDocumentDTO());
				Integer availAmt = entitlementNoOfDays * subLimitAvaliableAmt;
				int min = Math.min(SHAUtils.getIntegerFromString(medicalDecisionDto.getSubLimitAmount()) , availAmt);
				medicalDecisionDto.setSubLimitAvaliableAmt(min);
				medicalDecisionDto.setSubLimitUtilAmount(0);
			}
		}
		// need to implement in new medical listener table
		this.medicalDecisionTableObj.addBeanToList(medicalDecisionDto);
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
						Integer min = Math.min(
								amountConsideredTable.getMinimumValue(),
								totalApprovedAmt);
						if (bean.getIsNonAllopathic()) {
							min = Math.min(min, bean.getNonAllopathicAvailAmt());
						}
						
						if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							min = Math.min(
									amountConsideredTable.getMinimumValueForGMC(),
									SHAUtils.getIntegerFromString(medicalDecisionTableObj.dummyField
											.getValue()));
							
							if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
								Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
								Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
								if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
									bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
								}
								
							}
							
						}
						
						bean.getPreauthMedicalDecisionDetails()
								.setInitialTotalApprovedAmt(min.doubleValue());
						approvedAmtField.setValue(String
								.valueOf(totalApprovedAmt));
						if ((bean.getStatusKey()
								.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS))) {

							approvedAmtField.setValue(String
									.valueOf(totalApprovedAmt));
							if (bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO()
										.getPolicy().getProduct();
								if (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")) {
									//min = Math.min(amountConsideredTable
									min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}

								
								} else if (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									/*min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);*/
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
									}

								
								}
							}
						} else {
							if (bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO()
										.getPolicy().getProduct();
								if (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")) {
									//min = Math.min(amountConsideredTable
									min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}

									
								} else if (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
//									min = Math.min(amountConsideredTable
//											.getConsideredAmountValue(),
//											totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
									}

									
								}
							}
						}
					}
				});

		this.amountConsideredTable.dummyField
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 9193355451830325446L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmt = SHAUtils
								.getIntegerFromString(medicalDecisionTableObj.dummyField
										.getValue());
						Integer min = Math.min(
								amountConsideredTable.getMinimumValue(),
								totalApprovedAmt);
						if (bean.getIsNonAllopathic()) {
							min = Math.min(min, bean.getNonAllopathicAvailAmt());
						}
						
						if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							min = Math.min(
									amountConsideredTable.getMinimumValueForGMC(),
									SHAUtils.getIntegerFromString(medicalDecisionTableObj.dummyField
											.getValue()));
							
							if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
								Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
								Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
								if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
									bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
								}
								
							}
							
						}
						
						bean.getPreauthMedicalDecisionDetails()
								.setInitialTotalApprovedAmt(min.doubleValue());
						if ((bean.getStatusKey()
								.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS))) {

							if (bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO()
										.getPolicy().getProduct();
								if (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")) {
									//min = Math.min(amountConsideredTable
									min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}

									
								}else if (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									/*min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);*/
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
									}

									
								}
							}
						} else {
							if (bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO()
										.getPolicy().getProduct();
								if (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")) {
									//min = Math.min(amountConsideredTable
								   min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}

									
								} else if (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									/*min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);*/
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
									}

									
								}
							}
						}
					}
				});
		
			this.medicalDecisionTableObj.ambulanceChangeField.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -4052108705772482724L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField ambulanceTotal = (TextField) event.getProperty();
				if(amountConsideredTable != null) {
					amountConsideredTable.ambulanceChargeField.setValue(String.valueOf(SHAUtils.getIntegerFromStringWithComma(ambulanceTotal.getValue())));
				}
			}
		});

	}

//	private boolean validateSelectedCoPay(Double selectedCoPay) {
//		if (null != this.bean.getCopay()) {
//			if (selectedCoPay >= this.bean.getCopay()) {
//				return true;
//			} else {
//				return false;
//			}
//		} else {
//			return true;
//		}
//	}

	public void setCategoryValues(
			BeanItemContainer<SelectValue> selectValueContainer) {
		if (uploadDocumentListenerTableObj != null) {
			uploadDocumentListenerTableObj
					.setupCategoryValues(selectValueContainer);
		}

	}

	public void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO) {
		if (uploadDocumentListenerTableObj != null) {
			List<UploadDocumentDTO> uploadDoc = uploadDocumentListenerTableObj
					.getValues();
			List<UploadDocumentDTO> uploadList = new ArrayList<UploadDocumentDTO>();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDoc) {
				if (null != uploadDocumentDTO.getFileType()
						&& null != uploadDocumentDTO.getFileType().getValue()) {
					if (uploadDocumentDTO.getFileType().getValue()
							.contains("Bill")) {
						/*
						 * if (uploadDocumentDTO.getBillNo().equalsIgnoreCase(
						 * uploadDTO.getBillNo())) { uploadList.add(uploadDTO);
						 * } else { uploadList.add(uploadDocumentDTO); }
						 */

						/**
						 * Sequence number is an internal parameter maintained
						 * for updating the uploadlistener table. This is
						 * because the row for which the bill is entered should
						 * only get updated. Rest of rows should be the same.
						 * Earlier this was done with bill no. But there are
						 * chance that even bill no can be duplicate. Hence
						 * removed this and added validation based on seq no.
						 * */
						if (uploadDocumentDTO.getSeqNo().equals(
								uploadDTO.getSeqNo())) {
							//uploadList.add(uploadDTO);
						} else {
							uploadList.add(uploadDocumentDTO);
						}

					} else {
						uploadList.add(uploadDocumentDTO);
					}
				}
			}
			uploadList.add(uploadDTO);
			uploadDocumentListenerTableObj.updateTable(uploadList);
		}

	}

	protected void addTotalClaimedListener() {
		this.uploadDocumentListenerTableObj.claimedAmtField
				.addValueChangeListener(new ValueChangeListener() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != uploadDocumentListenerTableObj) {
							String provisionAmt = (String) event.getProperty()
									.getValue();
							if (null != provisionAmt
									&& !("").equalsIgnoreCase(provisionAmt)) {
								if (SHAUtils.isValidDouble(provisionAmt)) {
									bean.setAmountConsidered(uploadDocumentListenerTableObj
											.getHospitalizationAmount() != null ? uploadDocumentListenerTableObj
											.getHospitalizationAmount() : bean
											.getAmountConsidered());
									// For lumpsum, amount consider will be setted as bill value from bill entry...
									StarCommonUtils.setAmountconsideredForLumpsum(bean);
									if (amountConsidered != null) {
										amountConsidered.setValue(bean
												.getAmountConsidered());
									}

									if (amountConsideredTable != null) {
										amountConsideredTable.setDynamicValues(
												null, true, false,true, false);
									}
								}
							}
						}
					}

				});

	}

	private Button getCancelButton(final ConfirmDialog dialog) {
		cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				//binder = null;
			}
		});
		return cancelButton;
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		// Create upload stream
		FileOutputStream fos = null; // Stream to write to
		try {
			// Open the file for writing.
			file = new File(System.getProperty("jboss.server.data.dir") + "/"
					+ filename);
			fos = new FileOutputStream(file);
			// if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
			// fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT,
			// this.key,filename);
			// }
		} catch (final java.io.FileNotFoundException e) {
			new Notification("Could not open file<br/>", e.getMessage(),
					Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
			return null;
		}
		return fos; // Return the output stream to write to
	}

	@SuppressWarnings({"rawtypes" })
	@Override
	public void uploadSucceeded(SucceededEvent event) {
		System.out.println("File uploaded" + event.getFilename());

		try {

			byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);

			if (null != fileAsbyteArray) {

//				Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
//				boolean hasSpecialChar = p.matcher(event.getFilename()).find();
				// if(hasSpecialChar)
				// {
				WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(
						event.getFilename(), fileAsbyteArray);
				Boolean flagUploadSuccess = new Boolean(""
						+ uploadStatus.get("status"));
				// TO read file after load
				if (flagUploadSuccess.booleanValue()) {
					String token = "" + uploadStatus.get("fileKey");
					String fileName = event.getFilename();
					this.bean.setTokenName(token);
					this.bean.setFileName(fileName);
					uploadStatus = null;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public Integer setQueryValues(Long key,Long claimKey){
			
			List<ViewQueryDTO> QuerytableValues = reimbursementService.getQueryDetails(key);
			Hospitals hospitalDetails=null;    
			
			Integer count =0;
			
			Integer sno = 1;
			
			String diagnosisName = reimbursementService.getDiagnosisName(key);
			
			Claim claim = claimService.getClaimByClaimKey(claimKey);
			//need to implement
			if(claim != null){
			Long hospitalKey = claim.getIntimation().getHospital();
			hospitalDetails = hospitalService.getHospitalById(hospitalKey);
			}	
			for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
				viewQueryDTO.setDiagnosis(diagnosisName);
				if(hospitalDetails != null){
					viewQueryDTO.setHospitalName(hospitalDetails.getName());
					viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
				}
				
				viewQueryDTO.setQueryRaised("");       //need to implement
				viewQueryDTO.setQueryRaiseRole("");    //need to implement
				viewQueryDTO.setDesignation("");    //need to implement
				viewQueryDTO.setClaim(claim);
				viewQueryDTO.setSno(sno);
				if(viewQueryDTO.getQueryRaisedDate() != null){
					viewQueryDTO.setQueryRaisedDateStr(SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate()));
				}
				queryDetailsTableObj.addBeanToList(viewQueryDTO);
				sno++;
				count++;
			}	
			
			return count;
		}

	public void setBillEntryAmountConsideredValue(Double sumValue, PreauthDTO bean) {
		this.bean = bean;
		bean.setAmountConsidered(String.valueOf(sumValue.intValue()));
		
		
		
		bean.setAmbulanceAmountConsidered(this.bean.getAmountConsidered());
		
		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			
			if(bean.getAmbulanceLimitAmount() != null){
			 Double totalAmountConsidered = SHAUtils.getDoubleFromStringWithComma(this.bean.getAmountConsidered());
		      totalAmountConsidered -= this.bean.getAmbulanceLimitAmount();
		      
		     bean.setAmbulanceAmountConsidered(String.valueOf(totalAmountConsidered.intValue()));
			}
			
		}
		
		// For lumpsum, amount consider will be setted as bill value from bill entry...
		StarCommonUtils.setAmountconsideredForLumpsum(bean);
		
		
		amountConsidered.setValue(String.valueOf(sumValue.intValue()));
		
		if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
			amountConsidered.setValue(bean.getAmountConsidered());
		}
//		Double ambulanceAmtConsidered = SHAUtils.getDoubleFromStringWithComma(bean.getAmbulanceAmountConsidered());
		
		if(null != amountConsideredTable) {
			amountConsideredTable.setDynamicValues(null, true, false,false, false);
		}
		
		
	}

	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		// TODO Auto-generated method stub
		uploadDocumentListenerTableObj.loadBillEntryValues(uploadDTO);
		
	}
	
	public Boolean alertMessageForPEDWatchList() {

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.PED_WATCHLIST, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				bean.setIsPEDInitiated(false);
			}
		});
		return true;
	}
	
	public void setButtonDisabledForconsiderforPayment(Boolean visible){
		
		if (visible) {
			queryBtn.setEnabled(false);
			initiateFieldVisitBtn.setEnabled(false);
			initiateInvestigationBtn.setEnabled(false);
		}
	}
		
	public void alertMessageForInvestigation() {
		
		String message = "Investigation Request has already been initiated.</b>";
		
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();		
	
				//fireViewEvent(ClaimRequestWizardPresenter.CLAIM_REQUEST_INITIATE_INV_EVENT,bean);				
				
			}
		});
		
		/*cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				wizard.getFinishButton().setEnabled(true);
				bean.setIsInvestigation(false);
				
			}
		});*/
		
		
	}
	
	public boolean isValidButton(){
		
		Boolean hasError = false;
//		String msg = null;
		if (this.binder == null) {
			hasError = true;				
		 }
		 return hasError;
	}
	
private HorizontalLayout generateFieldBasedOnCorpBuffer(){
		
		String amountConsidered2 = this.bean.getAmountConsidered();
		Integer corpBufferAllocatedClaim = this.bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
		Integer availableAmt = 0;
		Integer integerFromStringWithComma = 0;
		if(amountConsidered2 != null && ! amountConsidered2.isEmpty()){
			integerFromStringWithComma = SHAUtils.getIntegerFromStringWithComma(amountConsidered2);
			if(integerFromStringWithComma != null){
				availableAmt = corpBufferAllocatedClaim - integerFromStringWithComma > 0 ? corpBufferAllocatedClaim - integerFromStringWithComma : 0;
			}
			
		}
		
		Table corpTable  = new Table();
		corpTable.setWidth("80%");
		corpTable.addContainerProperty("Particulars", String.class, null);
		corpTable.addContainerProperty("Amount",  Integer.class, null);
		//corpTable.setStyleName(ValoTheme.TABLE_NO_HEADER);
		corpTable.addItem(new Object[]{"Corporate Buffer Allocated for this claim", corpBufferAllocatedClaim },1);
		corpTable.addItem(new Object[]{"Amount claimed for this claim", integerFromStringWithComma },2);
		corpTable.addItem(new Object[]{"Corporate Buffer Balance Available for this Claim", availableAmt },3);
		corpTable.setPageLength(3);
		corpTable.setCaption("Corporate Buffer Details");
		
		Integer stopLossAvailableAmt = bean.getPreauthMedicalDecisionDetails().getStopLossAvailableAmt();
		
		Table stopLossTable  = new Table();
		stopLossTable.setWidth("80%");
		stopLossTable.addContainerProperty("Particulars", String.class, null);
		stopLossTable.addContainerProperty("Amount",  Integer.class, null);
		//stopLossTable.setStyleName(ValoTheme.TABLE_NO_HEADER);
		stopLossTable.addItem(new Object[]{"Stop Loss Amount Available(excl this claim)", stopLossAvailableAmt },1);
		stopLossTable.setPageLength(1);
		stopLossTable.setCaption("Stop Loss Details");
		
		HorizontalLayout mainHor = new HorizontalLayout(corpTable,stopLossTable);
		mainHor.setSpacing(true);
		
		return mainHor;

	}
public void showErrorPopUp(String emsg) {
	/*Label label = new Label(emsg, ContentMode.HTML);
    label.setStyleName("errMessage");
    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    layout.addComponent(label);
    
    ConfirmDialog dialog = new ConfirmDialog();
    dialog.setCaption("Warning");
    dialog.setClosable(true);
    dialog.setContent(layout);
    dialog.setResizable(false);
    dialog.setModal(true);
    dialog.show(getUI().getCurrent(), null, true);*/
    HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	GalaxyAlertBox.createWarningBox(emsg, buttonsNamewithType);
	
}
	
/*public void buildInitiateParallelFieldVisit(Object fieldVisitValues) {
	initBinder();
	unbindAndRemoveComponents(dynamicFrmLayout);

    Map<String,BeanItemContainer<SelectValue>> map = (Map<String,BeanItemContainer<SelectValue>>)fieldVisitValues;

	allocationTo = (ComboBox) binder.buildAndBind("Allocation To","allocationTo",ComboBox.class);
	allocationTo.setContainerDataSource(map.get("allocationTo"));
	allocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	allocationTo.setItemCaptionPropertyId("value");
	if(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo() != null){
		allocationTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo());
	}
	
	
	fvrPriority = (ComboBox) binder.buildAndBind("Priority","priority",ComboBox.class);
	fvrPriority.setContainerDataSource(map.get("fvrPriority"));
	fvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	fvrPriority.setItemCaptionPropertyId("value");
	if(this.bean.getPreauthMedicalDecisionDetails().getPriority()!= null){
		fvrPriority.setValue(this.bean.getPreauthMedicalDecisionDetails().getPriority());
	}

	triggerPtsTableObj = triggerPtsTable.get();
	triggerPtsTableObj.init();
	if(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList() != null && !bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList().isEmpty()){
		triggerPtsTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
	}
	viewFVRDetails = new Button("View FVR Details");
	viewFVRDetails.addClickListener(new Button.ClickListener() {

		*//**
		 * 
		 *//*
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			viewDetails.getFVRDetails(bean.getNewIntimationDTO()
					.getIntimationId(), false);
		}
	});
	countFvr = new Label();
	if(bean.getFvrCount() != null){
		
		countFvr.setValue(bean.getFvrCount().toString() + " FVR Reports Already Exists");
	
	}
	
	FormLayout horizontalLayout = new FormLayout(countFvr, viewFVRDetails);
	
	HorizontalLayout horizontalLayout2 = new HorizontalLayout(new FormLayout(allocationTo,fvrAssignTo,fvrPriority), horizontalLayout);
	horizontalLayout2.setSpacing(true);
//	horizontalLayout2.setHeight("px");
	
	alignFormComponents();
	mandatoryFields= new ArrayList<Component>();
	mandatoryFields.add(allocationTo);
//	mandatoryFields.add(fvrTriggerPoints);
	//mandatoryFields.add(fvrAssignTo);
	mandatoryFields.add(fvrPriority);
	showOrHideValidation(false);
	
	final ConfirmDialog dialog = new ConfirmDialog();
	Button submitButtonWithListener = getSubmitFVRButtonWithListener(dialog);
	
	HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getFVRCancelButton(dialog));
//	btnLayout.setWidth("600px");
//	btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
	btnLayout.setSpacing(true);
	showOrHideValidation(false);
	
	VerticalLayout VLayout = new VerticalLayout(horizontalLayout2, triggerPtsTableObj, btnLayout);
//	VLayout.setWidth("850px");
//	VLayout.setMargin(true);
//	VLayout.setSpacing(true);
	VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
	showInPopup(VLayout, dialog);
	
}
*/

private Button getSubmitFVRButtonWithListener(final ConfirmDialog dialog,final OptionGroup select) {
	submitButton = new Button("Submit");
	submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	submitButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = -5934419771562851393L;

		@Override
		public void buttonClick(ClickEvent event) {
			StringBuffer eMsg = new StringBuffer();
			//if (isValid()) {
				if (isValidFvr()) {
					
					if(screenName.equalsIgnoreCase("PAClaimRequestWizard")){
						
						fireViewEvent(PAClaimRequestWizardPresenter.PA_CLAIM_REQUEST_AUTO_SKIP_FVR,bean);

					}else if(screenName.equalsIgnoreCase("PAHealthClaimRequestWizard")){
						
						fireViewEvent(PAHealthClaimRequestMedicalDecisionPagePresenter.PA_HEALTH_CLAIM_REQUEST_AUTO_SKIP_FVR,bean);
					}
					
					if(null != select && null != select && ("yes".equals(select.getValue()))){
						bean.setIsFVRAlertOpened(Boolean.TRUE);
						bean.setIsFvrClicked(Boolean.TRUE);
						
						paReimbService.initiateFVR(bean,SHAConstants.CLAIM_REQUEST);				
						bean.getPreauthDataExtractionDetails().setIsFvrOrInvsInitiated(Boolean.TRUE);
						bean.setIsParallelInvFvrQuery(Boolean.TRUE);
						buildFVRSuccessLayout();
						dialog.close();

						ViewFVRDTO trgptsDto = null;
						List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
						for(int i = 1; i<=5;i++){
							trgptsDto = new ViewFVRDTO();
							trgptsDto.setRemarks("");
							trgptsList.add(trgptsDto);
						}
						bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(trgptsList);
						bean.getPreauthMedicalDecisionDetails().setAllocationTo(null);
						bean.getPreauthMedicalDecisionDetails().setPriority(null);
					}
					else
					{
						bean.setIsFVRAlertOpened(Boolean.TRUE);
						paReimbService.updateFVRNotRequiredDetails(bean);
						dialog.close();
					}
				 
					bean.setIsFvrClicked(Boolean.TRUE);
			} else {
				List<String> errors = getErrors();
				for (String error : errors) {
					eMsg.append(error);
				}
				showErrorPopup(eMsg.toString());
			}
		}
	});
	return submitButton;
}


/*private Button getFVRCancelButton(final ConfirmDialog dialog) {
	cancelButton = new Button("Cancel");
	cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
	cancelButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = -5934419771562851393L;

		@Override
		public void buttonClick(ClickEvent event) {
			dialog.close();
			
			ViewFVRDTO trgptsDto = null;
			 List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
			 for(int i = 1; i<=5;i++){
				 trgptsDto = new ViewFVRDTO();
				 trgptsDto.setRemarks("");
				 trgptsList.add(trgptsDto);
			 }
			 bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(trgptsList);
			//binder = null;
		}
	});
	return cancelButton;
}*/

public void buildFVRSuccessLayout() {
	
	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
			.createInformationBox("<b style = 'color: green;'>FVR has been initiated successfully!!</b>", buttonsNamewithType);
	Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
			.toString());
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
						
			if(null != bean.getStageKey() && bean.getStageKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE))
			{	Collection<Window> windows =  (Collection<Window>)getUI().getCurrent().getWindows();
				Object[] winArray = windows.toArray();
				for(int i = 0; i < winArray.length;i++){
					((Window)winArray[i]).close();
				}
			}
			//dialog.close();

			toolBar.countTool();

		}
	});
	
}

public void alertMessageForQuery() {
	
	String message = "Query Request has already been initiated and it is in process. </br> You can not initiate another query until reply is received for this current Query.</b>";
	
	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
			.createAlertBox(message, buttonsNamewithType);
	Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
			.toString());
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			//dialog.close();		
		}
	});
	
	
	
}


public void fVRVisit(final Object fieldVisitValues) {
	submainlayout = new VerticalLayout();
	fvrVertLayout = new VerticalLayout();
	
	select = new OptionGroup();
	select.addStyleName("horizontal");
	select.addItem("yes");
	select.addItem("no");
	select.setItemCaption("yes", "Yes");
	select.setItemCaption("no", "No");
	
/*	if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
			|| !ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
//	commnet for parallelprocess	in PA	&& SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getMulticlaimAvailFlag())
			&& !ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER.equals(bean.getStatusKey())
			&& !ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER.equals(bean.getStatusKey())) {
//		select.setValue("yes");
//		select.setReadOnly(true);
//		select.setEnabled(false);
		bean.setIsFvrInitiate(true);
		bean.setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
		buildInitiateParallelFieldVisit(fieldVisitValues, Boolean.TRUE);
					
	}*/
	
	
	final ConfirmDialog dialog = new ConfirmDialog();
	HorizontalLayout selectlayout = new HorizontalLayout(new Label(
			"Initiate Field Visit Request:"), select);
	selectlayout.setSpacing(true);
	submainlayout.setSpacing(true);
	submainlayout.setMargin(true);
	submainlayout.addComponent(selectlayout);
	submainlayout.setComponentAlignment(selectlayout,
			Alignment.MIDDLE_CENTER);
	
	select.addValueChangeListener(new ValueChangeListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			
			if (select.getValue().equals("yes")) {
				if(bean.getPreauthMedicalDecisionDetails()!= null && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() != null && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated().equals(Boolean.FALSE)){
					//buildInitiateFieldVisit(fieldVisitValues, Boolean.TRUE);					
					 bean.setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
					buildInitiateParallelFieldVisit(fieldVisitValues, Boolean.TRUE);
				}else{
					showErrorPopUp("FVR request is in process, cannot initiate another request");
					
					Map<String,Object> fieldVisitMap = (Map<String,Object>)fieldVisitValues;
					
					boolean isAssigned = fieldVisitMap.containsKey("isFVRAssigned") &&  (boolean)fieldVisitMap.get("isFVRAssigned") ? true : false;
					
					if(isAssigned){
						String repName = ((Map<String,Object>)fieldVisitValues).get("repName").toString();
					
						String repContactNo = ((Map<String,Object>)fieldVisitValues).get("repContactNo").toString();
						buildAssignedFieldVisitDetailsLayout(repName, repContactNo);
					}
					
				}
				
			} else if (select.getValue().equals("no")) {
				//buildInitiateFieldVisit(fieldVisitValues, Boolean.FALSE);
				buildInitiateParallelFieldVisit(fieldVisitValues, Boolean.FALSE);
				
			}
		}
	});
	
	submainlayout.addComponent(fvrVertLayout);
	submainlayout.setComponentAlignment(fvrVertLayout, Alignment.MIDDLE_CENTER);
	
	HorizontalLayout submitCancelLayout = new HorizontalLayout(getSubmitFVRButtonWithListener(dialog,select), getCancelButton(dialog));
	submainlayout.addComponent(submitCancelLayout);
	submainlayout.setComponentAlignment(submitCancelLayout, Alignment.BOTTOM_CENTER);
	
	
	showFvrInPopup(submainlayout, dialog);
	
}

private void showFvrInPopup(Layout layout, ConfirmDialog dialog) {
	dialog.setCaption("");
	dialog.setClosable(false);

	Panel panel = new Panel();
	panel.setHeight("500px");
	panel.setWidth("850px");
	panel.setContent(layout);
	dialog.setContent(panel);
	dialog.setResizable(false);
	dialog.setModal(true);


	dialog.show(getUI().getCurrent(), null, true);

}



@SuppressWarnings("unchecked")
public void buildInitiateParallelFieldVisit(Object fieldVisitValues, Boolean value) {
	bean.setIsFvrInitiate(value);
	if(value){
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
	    Map<String,BeanItemContainer<SelectValue>> map = (Map<String,BeanItemContainer<SelectValue>>)fieldVisitValues;
	
		allocationTo = (ComboBox) binder.buildAndBind("Allocation To","allocationTo",ComboBox.class);
		allocationTo.setContainerDataSource(map.get("allocationTo"));
		allocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		allocationTo.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo() != null){
			allocationTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo());
		}
		
		fvrPriority = (ComboBox) binder.buildAndBind("Priority","priority",ComboBox.class);
		fvrPriority.setContainerDataSource(map.get("fvrPriority"));
		fvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		fvrPriority.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getPriority()!= null){
			fvrPriority.setValue(this.bean.getPreauthMedicalDecisionDetails().getPriority());
		}

		triggerPtsTableObj = triggerPtsTable.get();
		triggerPtsTableObj.init();
		if(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList() != null && !bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList().isEmpty()){
			triggerPtsTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
		}
		viewFVRDetails = new Button("View FVR Details");
		viewFVRDetails.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getFVRDetails(bean.getNewIntimationDTO()
						.getIntimationId(), false);
			}
		});
		countFvr = new Label();
		if(bean.getFvrCount() != null){
			
			countFvr.setValue(bean.getFvrCount().toString() + " FVR Reports Already Exists");
		
		}
		
		FormLayout horizontalLayout = new FormLayout(countFvr, viewFVRDetails);
		
		HorizontalLayout horizontalLayout2 = new HorizontalLayout(new FormLayout(allocationTo,/*fvrAssignTo,*/fvrPriority), horizontalLayout);
		horizontalLayout2.setSpacing(true);
//		horizontalLayout2.setHeight("px");
		
		mandatoryFields = new ArrayList<Component>();
		
		mandatoryFields.add(allocationTo);
		mandatoryFields.add(fvrPriority);
		showOrHideValidation(false);
		
		if(cmbFVRNotRequiredRemarks != null){
			fvrVertLayout.removeComponent(cmbFVRNotRequiredRemarks);
		}
		unbindField(cmbFVRNotRequiredRemarks);
		
		fvrVertLayout.removeAllComponents();
		
		VerticalLayout VLayout = new VerticalLayout(horizontalLayout2, triggerPtsTableObj);
//		VLayout.setWidth("850px");
//		VLayout.setMargin(true);
//		VLayout.setSpacing(true);
		//showInPopup(VLayout, dialog);
		fvrVertLayout.addComponent(VLayout);	
	}else if(!value){

		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		referenceData.put("fvrNotRequiredRemarks", masterService.getSelectValueContainer(ReferenceTable.FVR_NOT_REQUIRED_REMARKS));
		
		cmbFVRNotRequiredRemarks = (ComboBox) binder.buildAndBind(
				"FVR Not Required Remarks", "fvrNotRequiredRemarks",
				ComboBox.class);
		
		/**
		* Requirement Number:R1118
		* For adding Others as additional LOV and text area in FVR Not Required Remarks
		* Modified By : GokulPrasath.A
		* Modified On : 07th MAR 2018
		*
		*/
		fvrOthersRemarks = (TextArea) binder.buildAndBind(
				"Enter remarks for Others", "fvrNotRequiredOthersRemarks",
				TextArea.class);
		fvrOthersRemarks.setHeight("70%");
		fvrOthersRemarks.setMaxLength(230);
		fvrOthersRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		fvrOthersRemarks.setData(bean);
		handleTextAreaPopup(fvrOthersRemarks,null);
		
		
		BeanItemContainer<SelectValue> fvrNotRequiredRemarks = (BeanItemContainer<SelectValue>) referenceData
				.get("fvrNotRequiredRemarks");

		cmbFVRNotRequiredRemarks
				.setContainerDataSource(fvrNotRequiredRemarks);
		cmbFVRNotRequiredRemarks
				.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbFVRNotRequiredRemarks.setItemCaptionPropertyId("value");
		
		if (this.bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks() != null) {
			cmbFVRNotRequiredRemarks.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks());
		}
		
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(cmbFVRNotRequiredRemarks);
		mandatoryFields.add(fvrOthersRemarks);
		showOrHideValidation(false);
		
		unbindField(allocationTo);
		unbindField(fvrPriority);
		
		if(triggerPtsTableObj != null){
			fvrVertLayout.removeComponent(triggerPtsTableObj);
		}
		
		if(null != allocationTo){
			fvrVertLayout.removeComponent(allocationTo);
		}
		
		if(null != fvrPriority) {
			fvrVertLayout.removeComponent(fvrPriority);
		}
		
		fvrVertLayout.removeAllComponents();
		
		final VerticalLayout VLayout = new VerticalLayout(cmbFVRNotRequiredRemarks);
		
		/**
		* Requirement Number:R1118
		* For adding Others as additional LOV and text area in FVR Not Required Remarks
		* Modified By : GokulPrasath.A
		* Modified On : 07th MAR 2018
		*
		*/
		cmbFVRNotRequiredRemarks.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && ((SelectValue)cmbFVRNotRequiredRemarks.getValue()).getValue().equalsIgnoreCase("others")) {
					if(fvrOthersRemarks != null){
						/*fvrOthersRemarks.setValue(StringUtils.EMPTY);*/
						VLayout.removeComponent(fvrOthersRemarks);
					}
					VLayout.addComponent(fvrOthersRemarks);
					
				} else {
					fvrOthersRemarks.setValue(StringUtils.EMPTY);
					bean.getPreauthMedicalDecisionDetails().setFvrNotRequiredOthersRemarks(null);
					VLayout.removeComponent(fvrOthersRemarks);
				}
				
				
			}
		});
		
		fvrVertLayout.addComponent(VLayout);
		
	
	}
	
	
}

public void buildAssignedFieldVisitDetailsLayout(String repName, String repContactNo) {
	unbindAndRemoveComponents(dynamicFrmLayout);
		
	if(triggerPtsTableObj != null){
		fvrVertLayout.removeComponent(triggerPtsTableObj);
	}
	
	if(null != allocationTo){
		fvrVertLayout.removeComponent(allocationTo);
	}
	
	if(null != fvrPriority) {
		fvrVertLayout.removeComponent(fvrPriority);
	}
	
	fvrVertLayout.removeAllComponents();
	
	FormLayout fvrAssignFLayout = new FormLayout();
	
	Label repNameLbl = new Label("Representative Name :  <b>"+ repName + "</b>",ContentMode.HTML);
	Label repContactNoLbl = new Label("Contact No :  <b>"+ repContactNo + "</b>",ContentMode.HTML);
	
	fvrAssignFLayout.addComponents(repNameLbl, repContactNoLbl);
	
	fvrVertLayout.addComponent(fvrAssignFLayout);

}

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
	ShortcutListener listener =  new ShortcutListener("FVR Not Required Remarks For Others",KeyCodes.KEY_F8,null) {

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
			txtArea.setMaxLength(230);
			txtArea.setReadOnly(false);
			txtArea.setRows(25);

			txtArea.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					txtFld.setValue(((TextArea)event.getProperty()).getValue());
					
					PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
					if(null != mainDto){
						mainDto.getPreauthMedicalDecisionDetails().setFvrNotRequiredOthersRemarks(txtFld.getValue());
					}
				}
			});
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

			final Window dialog = new Window();

			String strCaption = "FVR Not Required Remarks For Others";

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

public boolean isValidFvr() {
	boolean hasError = false;
	showOrHideValidation(true);
	errorMessages.removeAll(getErrors());

	if (this.binder == null) {
		hasError = true;
		errorMessages
				.add("Please select Approve or Query or Reject or Escalate Claim or Refer to coordinator. </br>");
		return !hasError;
	}

	System.out.println("Before Validate FVR "+bean.getNewIntimationDTO().getIntimationId());
	if(bean.getIsFvrInitiate()){
		if(triggerPtsTable != null){
			
			hasError = !triggerPtsTableObj.isValid();
			System.out.println("Before validate FVR trigger points "+bean.getNewIntimationDTO().getIntimationId()+" "+hasError);
			if(hasError){
				errorMessages.add("Please Provide atleast one FVR Trigger Point.</br>");
				errorMessages.add("FVR Trigger Points size will be Max. of 600.<br>");
			}
			else{
				bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());
				bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
			}
		}
	}
	else{
		
		
		if(cmbFVRNotRequiredRemarks.getValue() == null)
		{
			hasError = true;
			errorMessages.add("Please Choose FVR Not Required Remarks.</br>");
		}
		else if(null != cmbFVRNotRequiredRemarks.getValue() && cmbFVRNotRequiredRemarks.getValue().toString().equalsIgnoreCase("Others")) 
		{
			if(fvrOthersRemarks.getValue() == null || ("").equalsIgnoreCase(fvrOthersRemarks.getValue()) || 
					fvrOthersRemarks.getValue().isEmpty())	
			{
				hasError = true;
				errorMessages.add("Please Enter Remarks for Others.</br>");
			}
		}
	}
	if (!this.binder.isValid()) {
		hasError = true;
		for (Field<?> field : this.binder.getFields()) {
			ErrorMessage errMsg = ((AbstractField<?>) field)
					.getErrorMessage();
			if (errMsg != null) {
				errorMessages.add(errMsg.getFormattedHtmlMessage());
			}
		}
	} else {
		try {
			
			if(bean.getIsFvrInitiate()){
				bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
				bean.setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
			}
			
			this.binder.commit();
			if(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(bean.getStatusKey())){
				if(triggerPtsTableObj != null){
					bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());
					bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
					
				 }
			}
//			if (this.bean
//					.getStatusKey()
//					.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
////				this.investigationReportReviewedChk.setEnabled(true);
//			} else {
////				this.investigationReportReviewedChk.setValue(false);
////				this.investigationReportReviewedChk.setEnabled(false);
//			}

		} catch (CommitException e) {
			e.printStackTrace();
		}
	}
	showOrHideValidation(false);
	return !hasError;
}

public boolean isValid() {
	boolean hasError = false;
	showOrHideValidation(true);
	errorMessages.removeAll(getErrors());

	if (this.binder == null) {
		hasError = true;
		errorMessages
				.add("Please select Approve or Query or Reject or Escalate Claim or Refer to coordinator. </br>");
		return !hasError;
	}

	if(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(bean.getStatusKey())){
		if(triggerPtsTable != null){
			hasError = !triggerPtsTableObj.isValid();
			if(hasError){
				errorMessages.add("Please Provide atleast one FVR Trigger Point.</br>");
				errorMessages.add("FVR Trigger Points size will be Max. of 600.<br>");
			}
		}
	}
	if (!this.binder.isValid()) {
		hasError = true;
		for (Field<?> field : this.binder.getFields()) {
			ErrorMessage errMsg = ((AbstractField<?>) field)
					.getErrorMessage();
			if (errMsg != null) {
				errorMessages.add(errMsg.getFormattedHtmlMessage());
			}
		}
	} 	
	else {
		try {
			this.binder.commit();
			if(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(bean.getStatusKey())){
				if(triggerPtsTable != null){
					bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());
					bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
				 }
			}
		} catch (CommitException e) {
			e.printStackTrace();
		}
	}
	showOrHideValidation(false);
	return !hasError;
}

		public void confirmationForInvestigation() {
	
		
		String message = "Investigation already raised has been completed. </br>Do you want to proceed further?";
	
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
	
			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if(screenName.equalsIgnoreCase("PAClaimRequestWizard")){
					
					fireViewEvent(PAClaimRequestWizardPresenter.PA_CLAIM_REQUEST_INITIATE_INV_EVENT,bean);	

				}else if(screenName.equalsIgnoreCase("PAHealthClaimRequestWizard")){
					
					fireViewEvent(PAHealthClaimRequestWizardPresenter.PA_HEALTH_CLAIM_REQUEST_INITIATE_INV_EVENT,bean);
				}
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
	
			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				bean.setIsInvestigation(false);
			}
		});
	
		
}
		
		public void submitQueryEvent(){

			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				//				Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
				Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);										
				bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);

			}else{
				bean.setIsPedWatchList(false);
			}

			if(! bean.getIsPedWatchList()){

				ReimbursementQuery reimbQuery = paReimbService.getReplyNotReceivedQueryDetails(bean.getKey());

				if(null != reimbQuery){

					alertMessageForQuery();
				}
				else
				{	
					if(screenName.equalsIgnoreCase("PAClaimRequestWizard")){

						fireViewEvent(PAClaimRequestWizardPresenter.PA_CLAIM_REQUEST_QUERY_BUTTON_EVENT,bean);	

					}else if(screenName.equalsIgnoreCase("PAHealthClaimRequestWizard")){

						fireViewEvent(PAHealthClaimRequestWizardPresenter.PA_HEALTH_CLAIM_REQUEST_QUERY_BUTTON_EVENT,bean);
					}	
				}
			}else{
				alertMessageForPEDWatchList();
			}

		}
		
		public void alertForAdditionalFvrTriggerPoints(final String action) {	 
			 
			 /*Label successLabel = new Label(
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
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Add Additional FVR Points");
				buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Cancel");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createAlertBox(SHAConstants.ADDTIONAL_FVR_TRIGGER_POINTS_ALERT, buttonsNamewithType);
				Button addAdditionalFvrButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.CANCEL
						.toString());
				addAdditionalFvrButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
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
								//dialog.close();
							}
						});

						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
						
						if(SHAConstants.QUERY.equalsIgnoreCase(action)){
							submitQueryEvent();
						}
					}
				});
				
				cancelButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						if(SHAConstants.QUERY.equalsIgnoreCase(action)){
							submitQueryEvent();
						}
					}
				});
				
				/*dialog.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
						if(SHAConstants.QUERY.equalsIgnoreCase(action)){
							submitQueryEvent();
						}
					}
				});*/
			}
		
		public void enableButton(Boolean isValid){

			if(bean.getIsDishonoured()
			|| (!bean.getMaternityFlag() && isValid)){
				queryBtn.setEnabled(false);
				initiateFieldVisitBtn.setEnabled(false);
				initiateInvestigationBtn.setEnabled(false);
				bean.setIsFvrButtonDisabled(true);
				bean.setIsFvrClicked(Boolean.TRUE);
				
			}else{
				queryBtn.setEnabled(true);
				initiateFieldVisitBtn.setEnabled(true);
				initiateInvestigationBtn.setEnabled(true);
				bean.setIsFvrButtonDisabled(false);
				bean.setIsFvrClicked(Boolean.FALSE);
			}
			
			setButtonDisabledForconsiderforPayment(this.bean.getIsConsiderForPaymentNo());
		
		}
		
		public void enableIllnessButton(Boolean isValid){
			if(bean.getIsDishonoured()
					|| (!bean.getMaternityFlag() && isValid)){
				queryBtn.setEnabled(false);
				initiateFieldVisitBtn.setEnabled(false);
				initiateInvestigationBtn.setEnabled(false);
				bean.setIsFvrButtonDisabled(true);
				bean.setIsFvrClicked(Boolean.TRUE);
				
			}else{
				queryBtn.setEnabled(true);
				initiateFieldVisitBtn.setEnabled(true);
				initiateInvestigationBtn.setEnabled(true);
				bean.setIsFvrButtonDisabled(false);
				bean.setIsFvrClicked(Boolean.FALSE);
			}
			
			setButtonDisabledForconsiderforPayment(this.bean.getIsConsiderForPaymentNo());
		}
}
