package com.shaic.claim.preauth.wizard.pages;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.Wizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.ReportDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.common.ApprovedTable;
import com.shaic.claim.common.BillEntryWizardStep;
import com.shaic.claim.common.RevisedMedicalDecisionTable;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.fvrdetails.view.FVRTriggerPtsTable;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.premedical.dto.PreauthMedicalProcessingDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.AddAdditionalFVRPointsPageUI;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingUI;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsListenerTable;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
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
import com.vaadin.server.StreamResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
@Alternative
public class PreauthButtonsUI extends ViewComponent implements PreauthButtonListeners {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3354277990534196558L;
	
	@Inject
	private PreAuthPreviousQueryDetailsTable preAuthPreviousQueryDetailsTable;
	
	@Inject
	private Instance<BillEntryWizardStep> billEntrywizardStepInstance;
	
	@Inject
	private Instance<ApprovedTable> approvedTableWizardStepInstance;
	
	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;

	
	@Inject
	private Intimation intimation;
	
	@Inject
	private ViewBillSummaryPage viewBillSummary;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private PreAuthPreviousQueryDetailsService preAuthPreviousQueryDetailsService;
	
	@Inject
	private Instance<RevisedMedicalDecisionTable> medicalDecisionTable;
	
	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;
	
	@Inject
	private Instance<UploadedDocumentsListenerTable> uploadDocumentListenerTable;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ClaimService claimService;
	
	public UploadedDocumentsListenerTable uploadDocumentListenerTableObj;
	
	public AmountConsideredTable amountConsideredTable;
	
	private RevisedMedicalDecisionTable medicalDecisionTableObj;
	
	public BeanFieldGroup<PreauthMedicalProcessingDTO> binder;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Window popup;
	
	public List<String> errorMessages;  
	
	public Button query;
	
	public Button suggestRejection;
	
	public Button cancelRod;
	
	public Button invstionbtn;
	
	public Button sendForProcessing;
	
	public TextArea queryRemarks;
	
	public ComboBox cmbCategory;
	
	public ComboBox cmbCancelRodReason;
	
	public ComboBox changePreauth;
	
	public TextArea rejectionRemarks;
	
	public TextArea reasonRefBillEntryTxta;
	
	public TextArea remarksCancellation;
	
	public TextArea premedicalRemarks;
	
	private TextField txtQueryCount;
	
	public VerticalLayout dynamicFieldsLayout;
	
	private PreauthDTO bean;
	
	Map<String, Object> referenceData = new HashMap<String, Object>();

	private Button showBalanceSumInsured;

	private Button showClaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;
	
	private TextArea approvalRemarks;
	
	private Button submitButton;

	public Label amountConsidered;

	private TextField approvedAmtField;
	
	public TextArea zonalRemarks;
	
	public TextArea corporateRemarks;

	private VerticalLayout optionCLayout;
	
	public Button cancelButton;

	private TextField nonAllopathicTxt;
	
	/*private Button btnBillingWorksheet;
	
	private SelectValue value;
	
	private BrowserWindowOpener claimsDMSWindow;*/
	//private BrowserWindowOpener viewUploadDocWindow;
	
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	/*@Inject
	private Instance<DMSDocumentViewListenerTable> dmsDocumentViewListenerTableObj;
	private DMSDocumentViewListenerTable dmsDocumentViewListenerTable;*/
	
	private Button viewClaimsDMSDocument;
	
	private HorizontalLayout viewAllDocsLayout = null;
	
	public Button initiateFieldVisitBtn;
	
	private ComboBox allocationTo;
	
	private ComboBox fvrPriority;
	
	private Label countFvr;
	
	private Button viewFVRDetails;
	
	@Inject
	private Instance<FVRTriggerPtsTable> triggerPtsTable;
	
	private FVRTriggerPtsTable triggerPtsTableObj;
	
	private OptionGroup select;
	
	private VerticalLayout fvrVertLayout;
	
	private HorizontalLayout submitCancelLayout;
	
	protected Button referToBillEntryBtn;
	
	public Button holdBtn;
	
	private TextArea holdRemarksTxta;
	
	@Inject
	private AddAdditionalFVRPointsPageUI addAdditionalFvrPointsPageUI;
	
	public HorizontalLayout buildButtons(PreauthDTO bean) {
		this.bean = bean;
		//this.referenceData = referenceData;
		query = new Button("Query");
		suggestRejection = new Button("Suggest Rejection");
		sendForProcessing = new Button("Send for Processing");
		holdBtn = new Button("Hold");
		holdBtn.setVisible(false);
		
		if(bean.getIsDishonoured() || bean.getIsWithDrawn() || (bean.getPreauthDataExtractionDetails().getIllness() != null && bean.getPreauthDataExtractionDetails().getIllness().getId().equals(ReferenceTable.RELATED_TO_EARLIER_CLAIMS))) {
			query.setEnabled(false);
			sendForProcessing.setEnabled(false);
		} else {
			query.setEnabled(true);
			sendForProcessing.setEnabled(true);
		}
		if((ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) ||
				(ReferenceTable.STAR_CANCER_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			//IMSSUPPOR-27615
			
			if(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover() != null && (ReferenceTable.LUMPSUM_SECTION_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue()) &&
					ReferenceTable.LUMPSUM_COVER_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue()))){
				
				query.setEnabled(false);
				sendForProcessing.setEnabled(false);
			}
			else if(null != bean.getNewIntimationDTO().getPolicy().getInsured())
				{
				for (Insured insured : bean.getNewIntimationDTO().getPolicy().getInsured()) {
					
					if(null != insured && (SHAConstants.N_FLAG.equalsIgnoreCase(insured.getSumInsured3Flag())) && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null &&
							(ReferenceTable.HOSP_NON_SURGICAL_SECTION_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue()))){
						query.setEnabled(false);
						sendForProcessing.setEnabled(false);
					}
				}
				
				}
			else {
				query.setEnabled(true);
				sendForProcessing.setEnabled(true);
			}
		}
		if(bean.getIsPreauthAutoAllocationQ() != null
				&& bean.getIsPreauthAutoAllocationQ()){
			holdBtn.setVisible(true);
		}
		
		HorizontalLayout buttonHLayout = new HorizontalLayout(query, suggestRejection,sendForProcessing,holdBtn);
		buttonHLayout.setSpacing(true);
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(buttonHLayout);
		alignmentHLayout.setWidth("100%");
		alignmentHLayout.setComponentAlignment(buttonHLayout, Alignment.MIDDLE_RIGHT);
			
		return alignmentHLayout;
	}
	
	
	public HorizontalLayout buildZonalReviewButtons(PreauthDTO bean, Button submitButton) {
		this.bean = bean;
		this.submitButton = submitButton;
		//this.referenceData = referenceData;
		
		initiateFieldVisitBtn = new Button("Initiate Field Visit");
		
		invstionbtn = new Button("Initiate Investigation");
		
		
		query = new Button("Suggest Query");
		suggestRejection = new Button("Suggest Rejection");
		referToBillEntryBtn = new Button("Refer To Bill Entry");
		
		if(null != bean.getIsRejectReconsidered() && !bean.getIsRejectReconsidered()){
			referToBillEntryBtn.setEnabled(Boolean.FALSE);
		}
		
		sendForProcessing = new Button("Suggest Approval");
		cancelRod = new Button("Cancel ROD");
		//HorizontalLayout buttonHLayout = new HorizontalLayout(invstionbtn,query, suggestRejection,sendForProcessing,cancelRod);
		HorizontalLayout buttonHLayout = new HorizontalLayout(query, suggestRejection,referToBillEntryBtn,sendForProcessing,cancelRod);
		buttonHLayout.setSpacing(true);
		
		if(bean.getIsDishonoured() || bean.getIsWithDrawn() || (bean.getPreauthDataExtractionDetails().getIllness() != null && bean.getPreauthDataExtractionDetails().getIllness().getId().equals(ReferenceTable.RELATED_TO_EARLIER_CLAIMS))) {
			query.setEnabled(false);
			sendForProcessing.setEnabled(false);
		} else {
			query.setEnabled(true);	
			sendForProcessing.setEnabled(true);
		}
		
		if(ReferenceTable.STAR_CANCER_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
				(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())))
				{
			//IMSSUPPOR-27615
			if(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null){
			if(ReferenceTable.LUMPSUM_SECTION_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue()) &&
					ReferenceTable.LUMPSUM_COVER_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue())){
					if((null != bean.getIsWaitingDaysLessThan180() && bean.getIsWaitingDaysLessThan180()) ||
							(null != bean.getIsWaitingDaysLessThan30() && bean.getIsWaitingDaysLessThan30())){
			
					sendForProcessing.setEnabled(false);
				}
			}
		}
	}
		
		if(null != this.bean.getPreauthDataExtractionDetails().getIsFvrOrInvsInitiated() &&
				this.bean.getPreauthDataExtractionDetails().getIsFvrOrInvsInitiated()){
			sendForProcessing.setEnabled(false);
			suggestRejection.setEnabled(false);
			cancelRod.setEnabled(false);
			referToBillEntryBtn.setEnabled(false);
		}
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(buttonHLayout);
		alignmentHLayout.setWidth("100%");
		alignmentHLayout.setComponentAlignment(buttonHLayout, Alignment.MIDDLE_RIGHT);
		
		this.viewClaimsDMSDocument = new Button("View All Documents");
		viewClaimsDMSDocument.setStyleName(ValoTheme.BUTTON_LINK);

		/*claimsDMSWindow = new BrowserWindowOpener(DMSDocumentViewUI.class);
		claimsDMSWindow.setFeatures("height=693,width=1300,resizable");
		claimsDMSWindow.extend(viewClaimsDMSDocument);*/
		/*if(this.bean.getIsQueryReceived() || this.bean.getIsReconsiderationRequest()){
			cancelRod.setEnabled(false);
		}*/
		
		
		viewAllDocsLayout = new HorizontalLayout();
		
		addViewAllDocsListener();
		
		return alignmentHLayout;
	}
	
	private void addViewAllDocsListener()
	{
		if(null != viewClaimsDMSDocument)
		{
			if(null != viewClaimsDMSDocument)
			{
				viewClaimsDMSDocument.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					@SuppressWarnings("deprecation")
					public void buttonClick(ClickEvent event) {

						DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
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
						/*VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOCUMENT_DTO,dmsDTO);
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOCUMENT_VIEW_PAGE,dmsDocumentDetailsViewPage);
						
						dmsDocumentViewListenerTable = dmsDocumentViewListenerTableObj.get();
						
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOC_TABLE,dmsDocumentViewListenerTable);*/
						//VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_PRESENTER_STRING,SHACON);
					//	getSession().setAttribute(SHAConstants.DMS_DOCUMENT_DTO,dmsDTO);
						/*
						claimsDMSWindow = new BrowserWindowOpener(DMSDocumentViewUI.class);
						claimsDMSWindow.setWindowName("_blank");*/
						/*
						claimsDMSWindow.setFeatures("height=693,width=1300,resizable");
						claimsDMSWindow.extend(viewClaimsDMSDocument);*/
						//claimsDMSWindow.markAsDirty();
					//	UI.getCurrent().addWindow(popup);
						
					}
				});
			}
		}
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private void removeComponents() {
		if(dynamicFieldsLayout != null) {
			dynamicFieldsLayout.removeAllComponents();
		}
	}
	
	private void showInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("");
		dialog.setClosable(true);
		
		Panel panel = new Panel();
		panel.setHeight("500px");
		panel.setWidth("800px");
		panel.setContent(layout);
		
		dialog.setContent(panel);
		dialog.setResizable(false);
		dialog.setModal(true);
		
//		submitButton.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = -5934419771562851393L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				dialog.close();
//			}
//		});
		
		dialog.show(getUI().getCurrent(), null, true);
		
		
	}
	
	private void showErrorPopup(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
	    
	    HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	    buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	    GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	    
	}
	

	@Override
	public void generateFieldsForQuery() {
		intimation.setKey(bean.getNewIntimationDTO().getKey());	
		final List<PreAuthPreviousQueryDetailsTableDTO> PreAuthPreviousQueryDetailsTableDTO = preAuthPreviousQueryDetailsService.search(intimation);
		if(!PreAuthPreviousQueryDetailsTableDTO.isEmpty()) {
			alertMessage(SHAConstants.QUERY_RAISE_MESSAGE, true, PreAuthPreviousQueryDetailsTableDTO, 0);
		} else {
			generateQueryDetailsForCashless(PreAuthPreviousQueryDetailsTableDTO);
		}
	
		
		
		
//		dynamicFieldsLayout.addComponent(new VerticalLayout(preAuthPreviousQueryDetailsTable, fieldsFLayout));
	}
	
	
	 public Boolean alertMessage(String message, final Boolean isCashless, final List<PreAuthPreviousQueryDetailsTableDTO> preauthQueryDetailsDto, final Integer count) {
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
//			dialog.setCaption("Alert");
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
				   if(isCashless) {
					   generateQueryDetailsForCashless(preauthQueryDetailsDto);
				   } else {
					   generateReimburemenQueryDetails(count);
				   }
				}
			});
			return true;
		}
	
	private void generateQueryDetailsForCashless(
			final List<PreAuthPreviousQueryDetailsTableDTO> PreAuthPreviousQueryDetailsTableDTO) {
		removeComponents();
		
		if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS.equals(this.bean.getStatusKey()) 
				&& !ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			bean.getPreauthMedicalProcessingDetails().setQueryRemarks("We require the following documents / details also : \n 1. \n 2. \n 3. \n");
		}
		else if(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS.equals(this.bean.getStatusKey())) {

			bean.getPreauthMedicalProcessingDetails().setQueryRemarks("Your request for enhancement of amount is being examined by us. Meantime, please clarify / send us the following: \n 1. \n 2. \n 3. \n");

		}

		initBinder();
		//TODO: things need to do
		preAuthPreviousQueryDetailsTable.init("Previous Query Details", false, false);
		intimation.setKey(bean.getNewIntimationDTO().getKey());		
		preAuthPreviousQueryDetailsTable.setTableList(PreAuthPreviousQueryDetailsTableDTO);
		txtQueryCount = new TextField("Query Count");
		txtQueryCount.setValue(PreAuthPreviousQueryDetailsTableDTO.size()+"");
		txtQueryCount.setEnabled(false);
		txtQueryCount.setReadOnly(true);
		queryRemarks = (TextArea) binder.buildAndBind("Query Remarks(First Level)", "queryRemarks", TextArea.class);
		queryRemarks.setMaxLength(4000);
		queryRemarks.setWidth("400px");
		mandatoryFields.removeAll(mandatoryFields);
		mandatoryFields.add(queryRemarks);
		
		
		final ConfirmDialog dialog = new ConfirmDialog();
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(isValid()) {
					dialog.close();
					if(queryRemarks.getValue() != null){
						bean.getPreauthMedicalDecisionDetails().setQueryRemarks(queryRemarks.getValue());
					}
					openPdfFileForQueryInWindow(bean);
				} else {
					StringBuffer eMsg = new StringBuffer();
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		
		showOrHideValidation(false);
		FormLayout fieldsFLayout = new FormLayout(txtQueryCount, queryRemarks);
		fieldsFLayout.setMargin(true);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButton,getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		
		VerticalLayout verticalLayout = new VerticalLayout(preAuthPreviousQueryDetailsTable, fieldsFLayout, btnLayout);
		verticalLayout.setWidth("800px");
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		showInPopup(verticalLayout, dialog);
	}
	
	public void openPdfFileForQueryInWindow(PreauthDTO preauthDTO) {
		
		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());	
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
		}
		
		
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null){
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt());	
			this.bean.getPreauthDataExtractionDetails().setAmountInWords(amtInwords);
		}
		
		this.bean.getPreauthMedicalDecisionDetails().setMedicalRemarks(this.bean.getPreauthMedicalProcessingDetails().getMedicalRemarks());
		
		
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList() != null){
			List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			String diagnosis = "";
			if(!diagnosisList.isEmpty()){
			for(DiagnosisDetailsTableDTO diagnosisDto : diagnosisList){
				
				if(diagnosis.equals("")){
					diagnosis = diagnosisDto.getDiagnosisName().getValue();
				}
				else{
				diagnosis += " / " + ( diagnosisDto.getDiagnosisName() != null ? diagnosisDto.getDiagnosisName().getValue() : " / " ) ;
				}
			}
			}
			if(!diagnosis.equals("")){
				diagnosis = StringUtils.removeEnd(diagnosis, "/");
			this.bean.getPreauthDataExtractionDetails().setDiagnosis(diagnosis);
			}
		}
		
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		preauthDTOList.add(this.bean);		
		reportDto.setClaimId(this.bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDTOList);		
		
//		String filePath = docGen.generatePdfDocument("PreauthQueryLetter", reportDto);
		
		String templateName = "PreauthQueryLetter";
		
		if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS.equals(this.bean.getStatusKey())) {
			
			templateName = "PreauthQueryLetter_NON_GMC";
			if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				templateName = "PreauthQueryLetter";
			}
			
		}		
		else if(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS.equals(this.bean.getStatusKey())) {
			
			templateName = "ClsEnhancementQueryLetter_NON_GMC";
			if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				templateName = "CashlessEnhancementQueryLetter";
			}
		}
		
		String filePath = docGen.generatePdfDocument(templateName, reportDto);
		
		bean.setDocType(SHAConstants.PREAUTH_QUERY_LETTER);
		
		final String finalFilePath = filePath;
		
		final Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("");
		window.setWidth("800");
		window.setHeight("100%");
		window.setClosable(false);
		window.setModal(true);
		window.center();

		Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(finalFilePath);
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
		e.setHeight("700px");
		
		this.bean.setDocFilePath(finalFilePath);
		this.bean.setDocSource(SHAConstants.PRE_MEDICAL_PRE_AUTH);
		
		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS.equals(bean.getStatusKey())) {
					fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_SUBMITTED_EVENT, bean);	
				}
				else if(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS.equals(bean.getStatusKey())) {
					fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_SUBMITTED_EVENT,bean);
				}
				window.close();
//				savedResult();
				
			}
		});
		
		HorizontalLayout hor = new HorizontalLayout(homeButton);
		hor.setWidth("100%");
		hor.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		
		VerticalLayout mainVertical = new VerticalLayout(e,hor);
		
		window.setContent(mainVertical);
		UI.getCurrent().addWindow(window);
	}
	
	@Override
	public void generateFieldsForReimbursementQuery() {
		final Integer setQueryValues = setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());
		if(setQueryValues > 0) {
			alertMessage(SHAConstants.QUERY_RAISE_MESSAGE, false, null, setQueryValues);
		} else {
			if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
				alertForAdditionalFvrTriggerPoints(SHAConstants.QUERY,setQueryValues);
			}
			else{
				generateReimburemenQueryDetails(setQueryValues);
			}
		}
		
//		dynamicFieldsLayout.addComponent(new VerticalLayout(preAuthPreviousQueryDetailsTable, fieldsFLayout));
	}


	private void generateReimburemenQueryDetails(Integer setQueryValues) {
		removeComponents();
		initBinder();
		//TODO: things need to do
		queryDetailsTableObj.init("Previous Query Details", false,
				false);
		intimation.setKey(this.bean.getNewIntimationDTO().getKey());
		
		
//		List<PreAuthPreviousQueryDetailsTableDTO> PreAuthPreviousQueryDetailsTableDTO = preAuthPreviousQueryDetailsService.searchForReimbursementQuery(intimation);
//		preAuthPreviousQueryDetailsTable.setTableList(PreAuthPreviousQueryDetailsTableDTO);
		
		
		queryDetailsTableObj.setViewQueryVisibleColumn();
		setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());
		
		
		
		txtQueryCount = new TextField("Query Count");
		txtQueryCount.setValue(setQueryValues + "");
		txtQueryCount.setEnabled(false);
		txtQueryCount.setReadOnly(true);
		queryRemarks = (TextArea) binder.buildAndBind("Query Remarks(Zonal Medical)", "queryRemarks", TextArea.class);
		queryRemarks.setMaxLength(4000);
		queryRemarks.setWidth("400px");
		mandatoryFields.removeAll(mandatoryFields);
		mandatoryFields.add(queryRemarks);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(isValid()) {
					dialog.close();
				} else {
					StringBuffer eMsg = new StringBuffer();
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		
		showOrHideValidation(false);
		FormLayout fieldsFLayout = new FormLayout(txtQueryCount, queryRemarks);
		fieldsFLayout.setMargin(true);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButton,getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		
		if (viewAllDocsLayout != null
				&& viewAllDocsLayout.getComponentCount() > 0) {
			viewAllDocsLayout.removeAllComponents();
		}
		
		viewAllDocsLayout.addComponent(viewClaimsDMSDocument);
		
		VerticalLayout verticalLayout = new VerticalLayout(queryDetailsTableObj, viewAllDocsLayout ,fieldsFLayout, btnLayout);
		
		verticalLayout.setWidth("800px");
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		verticalLayout.setComponentAlignment(viewAllDocsLayout, Alignment.MIDDLE_RIGHT);
		
		showInPopup(verticalLayout, dialog);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void generateFieldsForSuggesRejection(Boolean showChangeInPreauth) {
		removeComponents();
		initBinder();
		cmbCategory = (ComboBox) binder.buildAndBind("Category", "category", ComboBox.class);
		BeanItemContainer<SelectValue> category = (BeanItemContainer<SelectValue>) referenceData.get("medicalCategory");
		
		cmbCategory.setNewItemsAllowed(false);
	    cmbCategory.setContainerDataSource(category);	
		cmbCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCategory.setItemCaptionPropertyId("value");
		
		
		if(showChangeInPreauth) {
			changePreauth = (ComboBox) binder.buildAndBind("Change in Pre-auth", "changeInPreauth", ComboBox.class);
			BeanItemContainer<SelectValue> changepreauth = (BeanItemContainer<SelectValue>) referenceData.get("commonValues");
			changePreauth.setContainerDataSource(changepreauth);
			changePreauth.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			changePreauth.setItemCaptionPropertyId("value");
			
			addChangePreauthListener();
		}
		
		
		if(this.bean.getPreauthMedicalProcessingDetails().getCategory() != null) {
			cmbCategory.setValue(this.bean.getPreauthMedicalProcessingDetails().getCategory());
		}
		
	//	rejectionRemarks = (TextArea) binder.buildAndBind("Rejection Remarks(Pre-medical)", "rejectionRemarks", TextArea.class);
		
		rejectionRemarks = (TextArea) binder.buildAndBind("Rejection Remarks(First Level)", "rejectionRemarks", TextArea.class);
		rejectionRemarks.setMaxLength(4000);
		rejectionRemarks.setWidth("400px");
		mandatoryFields.removeAll(mandatoryFields);
		mandatoryFields.add(cmbCategory);
		mandatoryFields.add(rejectionRemarks);
		showOrHideValidation(false);
		FormLayout fieldsFLayout;
		if(!showChangeInPreauth) {
			fieldsFLayout = new FormLayout(cmbCategory, rejectionRemarks);
		} else {
			fieldsFLayout = new FormLayout(cmbCategory, changePreauth, rejectionRemarks);
		}
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(isValid()) {
					dialog.close();
				} else {
					StringBuffer eMsg = new StringBuffer();
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButton,getCancelButton(dialog));
		btnLayout.setWidth("500px");
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		
		VerticalLayout verticalLayout = new VerticalLayout(fieldsFLayout, btnLayout);
//		verticalLayout.setWidth("500px");
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		showInPopup(verticalLayout, dialog);
//		dynamicFieldsLayout.addComponent(fieldsFLayout);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void generateFieldsForSuggesRejectionForReimbursement(Boolean showChangeInPreauth, BeanItemContainer<SelectValue> dropDownValues) {
		removeComponents();
		initBinder();
//		cmbCategory = (ComboBox) binder.buildAndBind("Rejection Category", "category", ComboBox.class);
//		cmbCategory.setNewItemsAllowed(false);
//
//		cmbCategory.setContainerDataSource(dropDownValues);
//		cmbCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cmbCategory.setItemCaptionPropertyId("value");
		
//		
//		if(showChangeInPreauth) {
//			changePreauth = (ComboBox) binder.buildAndBind("Change in Pre-auth", "changeInPreauth", ComboBox.class);
//			BeanItemContainer<SelectValue> changepreauth = (BeanItemContainer<SelectValue>) referenceData.get("commonValues");
//			changePreauth.setContainerDataSource(changepreauth);
//			changePreauth.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//			changePreauth.setItemCaptionPropertyId("value");
//			
//			addChangePreauthListener();
//		}
		
//		if(this.bean.getPreauthMedicalProcessingDetails().getCategory() != null) {
//			cmbCategory.setValue(this.bean.getPreauthMedicalProcessingDetails().getCategory());
//		}
		
//	//	rejectionRemarks = (TextArea) binder.buildAndBind("Rejection Remarks(Pre-medical)", "rejectionRemarks", TextArea.class);
		
		rejectionRemarks = (TextArea) binder.buildAndBind("Rejection Remarks(Zonal Medical)", "rejectionRemarks", TextArea.class);
		rejectionRemarks.setMaxLength(4000);
		rejectionRemarks.setWidth("400px");
		rejectionRemarks.setHeight("200px");
		mandatoryFields.removeAll(mandatoryFields);
//		mandatoryFields.add(cmbCategory);
		mandatoryFields.add(rejectionRemarks);
		showOrHideValidation(false);
		FormLayout fieldsFLayout;
//		if(!showChangeInPreauth) {
		
		fieldsFLayout = new FormLayout(/*cmbCategory,*/ rejectionRemarks);
		
//		} else {
//			fieldsFLayout = new FormLayout(cmbCategory, changePreauth, rejectionRemarks);
//		}
		
		final ConfirmDialog dialog = new ConfirmDialog();
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(isValid()) {
					dialog.close();
				} else {
					StringBuffer eMsg = new StringBuffer();
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButton,getCancelButton(dialog));
		btnLayout.setWidth("500px");
		btnLayout.setSpacing(true);
		btnLayout.setMargin(true);
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		
		if (viewAllDocsLayout != null
				&& viewAllDocsLayout.getComponentCount() > 0) {
			viewAllDocsLayout.removeAllComponents();
		}
		
		viewAllDocsLayout.addComponent(viewClaimsDMSDocument);
		
		VerticalLayout verticalLayout = new VerticalLayout(viewAllDocsLayout, fieldsFLayout, btnLayout);
//		verticalLayout.setWidth("500px");
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.setComponentAlignment(viewAllDocsLayout, Alignment.MIDDLE_RIGHT);
		showInPopup(verticalLayout, dialog);
//		dynamicFieldsLayout.addComponent(fieldsFLayout);
	}
	public void generateFieldsForCancelROD(){
		removeComponents();
		initBinder();
		cmbCancelRodReason = (ComboBox) binder.buildAndBind("Reason for Cancelling", "cancellationReason", ComboBox.class);
		cmbCancelRodReason.setNewItemsAllowed(false);
		
		BeanItemContainer<SelectValue> cancellationReason = (BeanItemContainer<SelectValue>) referenceData.get("cancellationReason");
		cmbCancelRodReason.setContainerDataSource(cancellationReason);
		cmbCancelRodReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCancelRodReason.setItemCaptionPropertyId("value");
		
		remarksCancellation =(TextArea) binder.buildAndBind("Remarks (Cancellation)", "cancelRemarks", TextArea.class);
		remarksCancellation.setMaxLength(4000);
		remarksCancellation.setWidth("400px");
		remarksCancellation.setHeight("200px");
		mandatoryFields.removeAll(mandatoryFields);
		mandatoryFields.add(cmbCancelRodReason);
		mandatoryFields.add(remarksCancellation);
		showOrHideValidation(false);
		FormLayout fieldsFLayout;
			fieldsFLayout = new FormLayout(cmbCancelRodReason, remarksCancellation);
		final ConfirmDialog dialog = new ConfirmDialog();
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(isValid()) {
					dialog.close();
				} else {
					StringBuffer eMsg = new StringBuffer();
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButton,getCancelButton(dialog));
		btnLayout.setWidth("500px");
		btnLayout.setSpacing(true);
		btnLayout.setMargin(true);
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		
		VerticalLayout verticalLayout = new VerticalLayout(fieldsFLayout, btnLayout);
//		verticalLayout.setWidth("500px");
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		showInPopup(verticalLayout, dialog);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void generateFieldsForSendForProcessing(Boolean showChangeInPreauth) {
		removeComponents();
		initBinder();
		cmbCategory = (ComboBox) binder.buildAndBind("Category", "category", ComboBox.class);
		BeanItemContainer<SelectValue> category = (BeanItemContainer<SelectValue>) referenceData
				.get("medicalCategory");
		
		cmbCategory.setContainerDataSource(category);
		cmbCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCategory.setItemCaptionPropertyId("value");
		cmbCategory.setValue(category.firstItemId());
		//cmbCategory.setEnabled(false);

//		if(this.bean.getPreauthMedicalProcessingDetails().getCategory() != null) {
//			cmbCategory.setValue(this.bean.getPreauthMedicalProcessingDetails().getCategory());
//		}
		
		if(showChangeInPreauth) {
			changePreauth = (ComboBox) binder.buildAndBind("Change in Pre-auth", "changeInPreauth", ComboBox.class);
			BeanItemContainer<SelectValue> changepreauth = (BeanItemContainer<SelectValue>) referenceData.get("commonValues");
			changePreauth.setContainerDataSource(changepreauth);
			changePreauth.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			changePreauth.setItemCaptionPropertyId("value");
			
			addChangePreauthListener();
			
			if(bean.getPreauthMedicalProcessingDetails().getChangeInPreauth() != null) {
				changePreauth.setValue(bean.getPreauthMedicalProcessingDetails().getChangeInPreauth());
			}
		}
		
		premedicalRemarks = (TextArea) binder.buildAndBind("Remarks (First Level)", "medicalRemarks", TextArea.class);
		premedicalRemarks.setMaxLength(4000);
		premedicalRemarks.setWidth("400px");
		premedicalRemarks.setHeight("200px");
		mandatoryFields.removeAll(mandatoryFields);
		mandatoryFields.add(cmbCategory);
//		mandatoryFields.add(premedicalRemarks);
		showOrHideValidation(false);
		FormLayout fieldsFLayout;
		if(!showChangeInPreauth) {
			fieldsFLayout = new FormLayout(cmbCategory, premedicalRemarks);
		} else {
			fieldsFLayout = new FormLayout(cmbCategory, changePreauth, premedicalRemarks);
		}
		
		final ConfirmDialog dialog = new ConfirmDialog();
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(isValid()) {
					dialog.close();
				} else {
					StringBuffer eMsg = new StringBuffer();
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButton,getCancelButton(dialog));
		btnLayout.setWidth("500px");
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		
		VerticalLayout verticalLayout = new VerticalLayout(fieldsFLayout, btnLayout);
//		verticalLayout.setWidth("500px");
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		showInPopup(verticalLayout, dialog);
		
//		dynamicFieldsLayout.addComponent(fieldsFLayout);
	}
	
	
	private void createNonAllopathicFields(Double originalAmt, Double utilizedAmt) {
		if(optionCLayout == null || (optionCLayout != null && optionCLayout.getComponentCount() == 1)) {
			nonAllopathicTxt = new TextField("Non-Allopathic Avail Amt");
			nonAllopathicTxt.setWidth("80px");
			Double availAmt = originalAmt - utilizedAmt;
			nonAllopathicTxt.setValue(String.valueOf(availAmt.intValue()));
			nonAllopathicTxt.setEnabled(false);
			bean.setNonAllopathicAvailAmt(availAmt.intValue());
			bean.setNonAllopathicOriginalAmt(originalAmt);
			bean.setNonAllopathicUtilizedAmt(utilizedAmt);
			
			Map<String, Object> values = new HashMap<String, Object>();
			values.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, originalAmt);
			values.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, utilizedAmt);
			Button viewBtn = new Button("View");
			viewBtn.setData(values);
			viewBtn.setStyleName("link");
			viewBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 9127517383717464157L;

				@Override
				public void buttonClick(ClickEvent event) {
					Map<String, Object> values = (Map<String, Object>) event.getButton().getData();
					
					TextField originalAmt = new TextField("Total Original Amt");
					originalAmt.setValue(String.valueOf(((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT)).intValue()));
					originalAmt.setReadOnly(true);
					originalAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					TextField utilizedAmt = new TextField("Utilized Amt");
					utilizedAmt.setValue(String.valueOf(((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT)).intValue()));
					utilizedAmt.setReadOnly(true);
					utilizedAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					TextField availableAmt = new TextField("Available Amt");
					Double availAmt = (Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT) - (Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT);
					availableAmt.setValue(String.valueOf(availAmt.intValue()) );
					availableAmt.setReadOnly(true);
					availableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					
					ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Non-Allopathic Details");
					dialog.setClosable(true);
					dialog.setWidth("400px");
					dialog.setResizable(false);
					dialog.setContent(new FormLayout(originalAmt, utilizedAmt, availableAmt));
					dialog.show(getUI().getCurrent(), null, true);
				}
			});
			HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(nonAllopathicTxt), viewBtn);
			horizontalLayout.setSpacing(true);
			optionCLayout.addComponent(horizontalLayout);
			optionCLayout.setSpacing(true);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void generateFieldsForSuggestApproval() {
		removeComponents();
		initBinder();
		Double consideredAmount = 0d;
		if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
			List<UploadDocumentDTO> uploadList = this.bean.getUploadDocumentDTO();
			for (UploadDocumentDTO uploadDocumentDTO : uploadList) {
				consideredAmount += uploadDocumentDTO.getBillValue() != null ? uploadDocumentDTO.getBillValue() : 0d;
			}
			bean.setInitialAmountConsidered(String.valueOf(consideredAmount.longValue()));
		}
		if(SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) > 0) {
			Integer amount = SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) - (SHAUtils.getIntegerFromString(this.bean.getPreHospitalisationValue()) + SHAUtils.getIntegerFromString(this.bean.getPostHospitalisationValue()));
			this.bean.setAmountConsidered(String.valueOf(amount));
			if(!this.bean.getHospitalizaionFlag()) {
				this.bean.setAmountConsidered(String.valueOf(this.bean.getHospitalizationAmount()));
			}
		}
		
		this.bean.setAmbulanceAmountConsidered(this.bean.getAmountConsidered());
		
		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			
			if(this.bean.getAmbulanceLimitAmount() != null){
			 Double totalAmountConsidered = SHAUtils.getDoubleFromStringWithComma(this.bean.getAmountConsidered());
		      totalAmountConsidered -= this.bean.getAmbulanceLimitAmount();
		      
		      this.bean.setAmbulanceAmountConsidered(String.valueOf(totalAmountConsidered.intValue()));
			}
			
		}

		
		Button calculateBtn = new Button("Calculate Net Payable");
		calculateBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		
		
		amountConsidered = new Label("Amount considered");
		amountConsidered.setCaption("Amount considered");
//		amountConsidered.setValue(this.bean.getAmountConsidered());
		
		approvalRemarks = (TextArea) binder.buildAndBind("Approval Remarks (Zonal Medical)", "approvalRemarks", TextArea.class);
		approvalRemarks.setMaxLength(4000);
		approvalRemarks.setWidth("400px");

//		mandatoryFields.removeAll(mandatoryFields);
//		mandatoryFields.add(approvalRemarks);
		showOrHideValidation(false);
		
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
				popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				viewBillSummary.init(bean,bean.getKey(), true);
				
				Panel mainPanel = new Panel(viewBillSummary);
		        mainPanel.setWidth("2000px");
				popup.setContent(mainPanel);
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
				UI.getCurrent().addWindow(popup);
			}
		});
		
		this.medicalDecisionTableObj = medicalDecisionTable.get();
		this.medicalDecisionTableObj.init(this.bean);
		this.medicalDecisionTableObj.setWidth("1300px");
		this.medicalDecisionTableObj.setReferenceData(this.referenceData);

		approvedAmtField = new TextField();
		approvedAmtField.setEnabled(false);
		HorizontalLayout approvedFormLayout = new HorizontalLayout(new Label(
				"C) Sub limits, </br> Package &  </br> SI Restriction </br> Amount",
				ContentMode.HTML), approvedAmtField);
		optionCLayout = new VerticalLayout(approvedFormLayout);
		Label amtClaimedDetailsLbl = new Label("A) Amount Considered");
		HorizontalLayout viewLayout1 = new HorizontalLayout(
				amtClaimedDetailsLbl, showClaimAmtDetailsBtnDuplicate);
		viewLayout1.setSpacing(true);
		
		showClaimAmtDetailsBtnDuplicate.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				viewBillSummary.init(bean,bean.getKey(),true);
				Panel mainPanel = new Panel(viewBillSummary);
		        mainPanel.setWidth("2000px");
				popup.setContent(mainPanel);
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
		});
		
		showBalanceSumInsured.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getViewBalanceSumInsured(bean.getNewIntimationDTO().getIntimationId());
				
			}
		});

		Label balanceSumInsuredLbl = new Label("B) Balance Sum Insured");

		HorizontalLayout viewLayout2 = new HorizontalLayout(
				balanceSumInsuredLbl, showBalanceSumInsured);
		viewLayout2.setSpacing(true);

		this.amountConsideredTable = this.amountConsideredTableInstance.get();
		this.amountConsideredTable.setPresenterString(SHAConstants.ZONAL_REVIEW);
		this.amountConsideredTable.initTable(this.bean, viewLayout1,
				viewLayout2, optionCLayout, false, false);
		this.amountConsideredTable.setAlreadySettledAmount(this.bean.getAlreadySettlementAmt());
		
		uploadDocumentListenerTableObj =   uploadDocumentListenerTable.get();
		uploadDocumentListenerTableObj.initPresenter(SHAConstants.ZONAL_REVIEW);
		uploadDocumentListenerTableObj.init();
		uploadDocumentListenerTableObj.setReferenceData(referenceData);
		Integer i = 1;
		List<UploadDocumentDTO> uploadList = this.bean.getUploadDocumentDTO();
		
	
		uploadDocumentListenerTableObj.setTableInfo(uploadList);
		
		if(null !=  uploadList && !uploadList.isEmpty())
		for (UploadDocumentDTO uploadDocLayout : uploadList) {
			uploadDocLayout.setSeqNo(i);
			if(null != this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation())
				uploadDocLayout.setDomicillaryFlag(this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation());
			if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() && null != bean.getNewIntimationDTO().getPolicy().getProduct())
			{
				uploadDocLayout.setProductKey( bean.getNewIntimationDTO().getPolicy()
				.getProduct().getKey());
			}
			if(null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue())
				uploadDocLayout.setSubCoverCode(this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
			this.uploadDocumentListenerTableObj.addBeanToList(uploadDocLayout);
			i++;
		}
		//}
		
		//uploadDocumentListenerTableObj.setTableList(this.bean.getUploadDocumentDTO());
		
		zonalRemarks = (TextArea) binder.buildAndBind("Dr Remarks (Zonal)", "zonalRemarks", TextArea.class);
		zonalRemarks.setMaxLength(4000);
//		zonalRemarks.setWidth("400px");
		
		corporateRemarks = (TextArea) binder.buildAndBind("Dr Remarks (Corporate)", "corporateRemarks", TextArea.class);
		corporateRemarks.setMaxLength(4000);
		corporateRemarks.setEnabled(false);
//		corporateRemarks.setWidth("400px");
		
		HorizontalLayout amountConsideredLayout = new HorizontalLayout(
				new FormLayout(amountConsidered), new FormLayout(amountConsideredViewButton));
		
		addRowsForProcAndDiag();
		
		final ConfirmDialog dialog = new ConfirmDialog();
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				validatePage(dialog);
			}

		
		});
		
		addMedicalDecisionTableFooterListener();
		
		Integer min = Math
				.min(amountConsideredTable.getMinimumValue(),
						SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
								.getValue()));
		approvedAmtField.setValue(String.valueOf(this.medicalDecisionTableObj.dummyField
				.getValue()));
		if(bean.getNewIntimationDTO() != null) {
			Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
			if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
				min = Math.min(amountConsideredTable.getConsideredAmountValue(), SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
						.getValue()));
				amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
				
				approvedAmtField.setValue(this.medicalDecisionTableObj.dummyField
						.getValue());
			}
		}
		
		addTotalClaimedListener();
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButton,getCancelButton(dialog));
		btnLayout.setWidth("1200px");
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		bean.setIsNonAllopathic(bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
		if(bean.getIsNonAllopathic()) {
			createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
		}
		VerticalLayout layout = null;
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				/*&& bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()*/){
			HorizontalLayout generateFieldBasedOnCorpBuffer = generateFieldBasedOnCorpBuffer();
			layout = new VerticalLayout( /*new HorizontalLayout(new FormLayout(zonalRemarks), new FormLayout(corporateRemarks)),*/  amountConsideredLayout, this.medicalDecisionTableObj,generateFieldBasedOnCorpBuffer,this.amountConsideredTable, new FormLayout(approvalRemarks) );
		}else{
			layout = new VerticalLayout( /*new HorizontalLayout(new FormLayout(zonalRemarks), new FormLayout(corporateRemarks)),*/  amountConsideredLayout, this.medicalDecisionTableObj, this.amountConsideredTable, new FormLayout(approvalRemarks) );
		}
		
		
		layout.setWidth("1200px");
		layout.setMargin(true);
		layout.setSpacing(true);
		
		Wizard wizard = new Wizard();
		BillEntryWizardStep billEntryWizardStep = billEntrywizardStepInstance.get();
		billEntryWizardStep.init(this.bean);
		billEntryWizardStep.setMedicalDecisiontable(this.medicalDecisionTableObj);
		billEntryWizardStep.setAmountConsideredTable(amountConsideredTable);
		billEntryWizardStep.setReferenceData(this.referenceData);
	   
		billEntryWizardStep.setComponent(uploadDocumentListenerTableObj, SHAConstants.ZONAL_REVIEW);
		wizard.addStep(billEntryWizardStep, "Bill View");
//		if(billEntryWizardStep.onAdvance()) {
//			List<DiagnosisProcedureTableDTO> values = this.medicalDecisionTableObj.getValues();
//			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : values) {
//				if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_DELITE)) {
//					Integer subLimitAvaliableAmt = 0;
//					Boolean isResidual = false;
//					if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null && diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName() != null && (diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
//						subLimitAvaliableAmt = diagnosisProcedureTableDTO.getSubLimitAvaliableAmt();
//					} else if (diagnosisProcedureTableDTO.getProcedureDTO() != null && diagnosisProcedureTableDTO.getProcedureDTO().getSublimitName() != null && (diagnosisProcedureTableDTO.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || diagnosisProcedureTableDTO.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
//						subLimitAvaliableAmt = diagnosisProcedureTableDTO.getSubLimitAvaliableAmt();
//					} else {
//						isResidual = true;
//					}
//					
//					if(!isResidual) {
//						Integer entitlementNoOfDays = SHAUtils.getEntitlementNoOfDays(bean.getUploadDocumentDTO());
//						Integer availAmt = entitlementNoOfDays * subLimitAvaliableAmt;
//						int minValue = Math.min(SHAUtils.getIntegerFromString(diagnosisProcedureTableDTO.getSubLimitAmount()) , availAmt);
//						diagnosisProcedureTableDTO.setSubLimitAvaliableAmt(minValue);
//						diagnosisProcedureTableDTO.setSubLimitUtilAmount(0);
//					}
//				}
//			}
//		}
		
		ApprovedTable approvedTablewizard = approvedTableWizardStepInstance.get();
		approvedTablewizard.init(this.bean);
		approvedTablewizard.setUIPage(this, dialog);
		approvedTablewizard.setComponent(layout);
		wizard.addStep(approvedTablewizard, "Approval");
		Button cancelButton2 = wizard.getCancelButton();
		cancelButton2.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog cancelDialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure you want to cancel ?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog confirmDialog) {
										if (!confirmDialog.isConfirmed()) {
//											fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST,null);
											binder = null;
											dialog.close();
										} else {
											// User did not confirm
										}
									}
								});
				
				cancelDialog.setClosable(false);

				cancelDialog.setStyleName(Reindeer.WINDOW_BLACK);
				
			}
		});
		dialog.setCaption("");
		dialog.setClosable(false);
		Panel panel = new Panel();
		panel.setSizeFull();
//		panel.setWidth("400px");
		panel.setContent(wizard);
		
		dialog.setContent(panel);
		dialog.setHeight("60%");
		dialog.setWidth("90%");
		dialog.setResizable(true);
		dialog.setModal(true);
		
		dialog.show(getUI().getCurrent(), null, true);
	}

	
	public void validatePage(final ConfirmDialog dialog) {
		StringBuffer eMsg = new StringBuffer();
		Boolean hasError = false;
		/*if (medicalDecisionTableObj.getTotalAmountConsidered() > 0 && !medicalDecisionTableObj.getTotalAmountConsidered().equals(
				SHAUtils.getIntegerFromString(bean.getAmountConsidered()))) {
			hasError = true;
			eMsg.append("Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table</br>");
		}*/
		
		
		if (medicalDecisionTableObj.getTotalAmountConsidered() > (
				SHAUtils.getIntegerFromString(bean.getAmountConsidered()))) {
			hasError = true;
			eMsg.append("Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table</br>");
		}
		
		if(null != bean.getNewIntimationDTO().getIsJioPolicy() && bean.getNewIntimationDTO().getIsJioPolicy()){
			List<DiagnosisProcedureTableDTO> medicalDecisionTableValues = this.medicalDecisionTableObj.getValues();
			if(null != medicalDecisionTableValues && !medicalDecisionTableValues.isEmpty()){
				for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : medicalDecisionTableValues) {
					if(diagnosisProcedureTableDTO.getCoPayType() == null){
						
						hasError = true;
						eMsg.append("Please Select Co_Pay Type </br>");
						break;
					}
				}
			}
		}
		
		
		/*if(medicalDecisionTableObj.getTotalAmountConsidered() < (
				SHAUtils.getIntegerFromString(bean.getAmountConsidered()))){
			showErrorPopup("Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table</br>");
		}*/
		
		
		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			if(this.bean.getIsAmbulanceApplicable() && this.medicalDecisionTableObj.getAmbulanceLimitAmount() != null
					&& ! this.bean.getAmbulanceLimitAmount().equals(this.medicalDecisionTableObj.getAmbulanceLimitAmount())){
				
				hasError = true;
				eMsg.append("Amount Entered against Ambulance charges should be equal");
				
			}
		}
		
		if(isValid() && !hasError) {
			dialog.close();
			setResidualAmtToDTO();
			bean.getPreauthMedicalDecisionDetails()
			.setMedicalDecisionTableDTO(
					medicalDecisionTableObj.getValues());	
			
			SHAUtils.doFinalEnhancementCalculationForLetter(this.medicalDecisionTableObj.getValues(), SHAUtils.getDoubleFromStringWithComma(this.amountConsideredTable.getCoPayValue()) , bean);
			
			
		} else {
			List<String> errors = getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
			showErrorPopup(eMsg.toString());
		}
	}
	
	private void setResidualAmtToDTO() {
		List<DiagnosisProcedureTableDTO> values = this.medicalDecisionTableObj
				.getValues();
		for (DiagnosisProcedureTableDTO medicalDecisionTableDTO : values) {
			if (medicalDecisionTableDTO.getDiagOrProcedure() != null
					&& medicalDecisionTableDTO.getDiagOrProcedure().contains(
							"Residual")) {
				ResidualAmountDTO residualAmountDTO = this.bean
						.getResidualAmountDTO();
				residualAmountDTO
						.setAmountConsideredAmount(medicalDecisionTableDTO
								.getAmountConsidered() != null ? medicalDecisionTableDTO
								.getAmountConsidered().intValue() : 0d);
				residualAmountDTO.setNetAmount(medicalDecisionTableDTO
						.getNetAmount() != null ? medicalDecisionTableDTO
						.getNetAmount().doubleValue() : 0d);
				residualAmountDTO.setMinimumAmount(medicalDecisionTableDTO
						.getMinimumAmountOfAmtconsideredAndPackAmt() != null ? medicalDecisionTableDTO
						.getMinimumAmountOfAmtconsideredAndPackAmt().doubleValue() : 0d);
				residualAmountDTO.setCopayPercentage(medicalDecisionTableDTO
						.getCoPayPercentage() != null ? Double
								.valueOf(medicalDecisionTableDTO.getCoPayPercentage()
										.getValue()) : 0d);
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
				residualAmountDTO.setCopayPercentage(medicalDecisionTableDTO
						.getCoPayPercentage() != null ? Double
						.valueOf(medicalDecisionTableDTO.getCoPayPercentage()
								.getValue()) : 0d);
				residualAmountDTO.setCoPayTypeId(medicalDecisionTableDTO.getCoPayType());
			}
		}
	}



	private void addRowsForProcAndDiag() {
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				addDiagnosisToMedicalDecision(medicalDecisionDTOList, pedValidationTableDTO, false , false);
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
				addProcedureToDecisionTable(medicalDecisionDTOList, procedureDTO, false ,false);
			}
			
			List<ProcedureDTO> deletedProcedureList = bean.getDeletedProcedure();
			if(!deletedProcedureList.isEmpty()) {
				for (ProcedureDTO procedureDTO : deletedProcedureList) {
					addProcedureToDecisionTable(medicalDecisionDTOList, procedureDTO, true, true);
				}
			}

			Map<String, Object> caluculationInputValues = new HashMap<String, Object>();
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
			
			int medicalDecisionSize = 0;
			
			for (DiagnosisProcedureTableDTO medicalDecisionDto : medicalDecisionDTOList) {
				if(! medicalDecisionDto.getIsDeletedOne()){
					medicalDecisionSize++;
				}
			}

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
								medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName() == null ? 0l : medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName().getId().toString());
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
						caluculationInputValues.put("diagOrProcId", medicalDecisionDto.getProcedureDTO().getProcedureName() == null ? 0l : (medicalDecisionDto.getProcedureDTO().getProcedureName().getId() == null ? 0l : medicalDecisionDto.getProcedureDTO().getProcedureName().getId()));
						caluculationInputValues.put("referenceFlag", "P");
					}
					caluculationInputValues.put("preauthKey", this.bean.getPreauthKey() != null ? this.bean.getPreauthKey() : 0l);
					
					if((this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) || (this.bean.getIsHospitalizationRepeat() != null && this.bean.getIsHospitalizationRepeat())) {
						caluculationInputValues.put("preauthKey", 0l);
					}
					
					caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
					caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
					caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, bean.getClaimKey());
					
					bean.setIsNonAllopathic((Boolean)caluculationInputValues.get(SHAConstants.IS_NON_ALLOPATHIC));
					medicalDecisionDto
					.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount"))
							.toString() : "NA");
					
					
					if(medicalDecisionSize == 1){
						
						if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
							if(this.bean.getAmbulanceLimitAmount() > 0){
								medicalDecisionDto.setAmbulanceCharge(this.bean.getAmbulanceLimitAmount().intValue());
								medicalDecisionDto.setIsAmbulanceEnable(true);
								medicalDecisionDto.setIsAmbChargeApplicable(true);
							}
						}
						
					}
					
					fireViewEvent(
							MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_REVIEW_SUM_INSURED_CALCULATION,
							caluculationInputValues, medicalDecisionDto,bean);
				}
				
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				dto.setIsAmbulanceEnable(false);
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
					fireViewEvent(
							MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_REVIEW_SUM_INSURED_CALCULATION,
							caluculationInputValues, diagnosisProcedureTableDTO,bean);
				}else{
					residualDTO = diagnosisProcedureTableDTO;
				}
			
				
			}
			
			if(residualDTO != null){
				this.medicalDecisionTableObj.addBeanToList(residualDTO);
			}
			
//			this.medicalDecisionTableObj.addList(filledDTO);
			bean.setIsNonAllopathic(bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
			if(bean.getIsNonAllopathic()) {
				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
			}
		}
	}


	private void addDiagnosisToMedicalDecision(
			List<DiagnosisProcedureTableDTO> medicalDecisionDTOList,
			DiagnosisDetailsTableDTO pedValidationTableDTO, Boolean isZeroApprAmt, Boolean isDeletedOne) {
		DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue() != 2904 || 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS) ||
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS)){
			
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
		} else {
			dto.setIsPaymentAvailable(false);
		}
		
		if(isZeroApprAmt) {
			dto.setMinimumAmount(0);
			dto.setReverseAllocatedAmt(0);
			dto.setIsPaymentAvailable(false);
		}
		if (pedValidationTableDTO.getSumInsuredRestriction() == null) {
			dto.setRestrictionSI("NA");
		} else {
			dto.setRestrictionSI(SHAUtils.getIntegerFromString(
					pedValidationTableDTO.getSumInsuredRestriction()
							.getValue()).toString());
		}
		List<PedDetailsTableDTO> pedList = pedValidationTableDTO.getPedList();
		for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
			if(pedDetailsTableDTO.getCopay() != null) {
				dto.setCoPayPercentage(pedDetailsTableDTO.getCopay());
			}
		}

		if (pedValidationTableDTO.getSublimitName() == null) {
			dto.setSubLimitAmount("NA");
		} else {
			dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
					pedValidationTableDTO.getSublimitAmt()).toString());
		}
		dto.setPackageAmt("NA");
		dto.setDiagnosisDetailsDTO(pedValidationTableDTO);

		if(pedValidationTableDTO.getPedList().size() == 1) {
			dto.setCoPayPercentage(pedValidationTableDTO.getPedList().get(0).getCopay());
		}
		
		if(this.bean.getIsAmbulanceApplicable()){
			dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
		}else{
			dto.setIsAmbChargeApplicable(false);
		}
		
		dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
		dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());
		
		if(isDeletedOne) {
			dto.setIsDeletedOne(true);
		}
		medicalDecisionDTOList.add(dto);
	}


	private void addProcedureToDecisionTable(
			List<DiagnosisProcedureTableDTO> medicalDecisionDTOList,
			ProcedureDTO procedureDTO, Boolean isZeroAppAmt, Boolean isDeletedOne) {
		DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
		dto.setProcedureDTO(procedureDTO);
		Boolean isPaymentAvailable = true;
		if (procedureDTO.getConsiderForPaymentFlag() != null) {
			isPaymentAvailable = procedureDTO
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
		} else {
			isPaymentAvailable = false;
			if(procedureDTO.getNewProcedureFlag() != null && procedureDTO.getNewProcedureFlag().equals(1l)) {
				isPaymentAvailable = true;
			}
			dto.setIsPaymentAvailable(isPaymentAvailable);
		}
			
			
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
			
			if(isZeroAppAmt) {
				dto.setIsPaymentAvailable(false);
				dto.setMinimumAmount(0);
				dto.setReverseAllocatedAmt(0);
			}
		dto.setRestrictionSI("NA");
		
		dto.setPackageAmt("NA");
		if(procedureDTO.getPackageRate() != null && procedureDTO.getPackageRate() >= 0) {
			dto.setPackageAmt(procedureDTO.getPackageRate().toString());
		}
		
		if(procedureDTO.getCopay() != null) {
			dto.setCoPayPercentage(procedureDTO.getCopay());
		}

		if (procedureDTO.getSublimitName() == null) {
			dto.setSubLimitAmount("NA");
		} else {
			dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
					procedureDTO.getSublimitAmount()).toString());
		}
		dto.setCoPayPercentage(procedureDTO.getCopay());
		if(isDeletedOne) {
			dto.setIsDeletedOne(true);
		}
		
		if(this.bean.getIsAmbulanceApplicable()){
			dto.setIsAmbChargeFlag(procedureDTO.getIsAmbChargeFlag());
		}else{
			dto.setIsAmbChargeApplicable(false);
		}
		
		dto.setAmbulanceCharge(procedureDTO.getAmbulanceCharge());
		dto.setAmtWithAmbulanceCharge(procedureDTO.getAmtWithAmbulanceCharge());
		
		medicalDecisionDTOList.add(dto);
	}


	public void setAppropriateValuesToDTOFromProcedure(DiagnosisProcedureTableDTO medicalDecisionDto, Map<String, Object> values) {
		if(bean.getIsNonAllopathic()) {
			bean.setNonAllopathicOriginalAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
			bean.setNonAllopathicUtilizedAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
//			createNonAllopathicFields((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT), (Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
		}
		medicalDecisionDto.setAvailableAmout(((Double) values
				.get("restrictedAvailAmt")).intValue());
		medicalDecisionDto.setUtilizedAmt(((Double) values
				.get("restrictedUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAmount(((Double) values
				.get("currentSL")).intValue() > 0 ? (String
				.valueOf(((Double) values.get("currentSL"))
						.intValue())) : "NA");
		medicalDecisionDto.setSubLimitUtilAmount(((Double) values
				.get("SLUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
				.get("SLAvailAmt")).intValue());
		medicalDecisionDto
				.setCoPayPercentageValues((List<String>) values
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
		
		if(this.bean.getIsAmbulanceApplicable()){
			medicalDecisionDto.setIsAmbulanceEnable(true);
		}else{
			medicalDecisionDto.setIsAmbulanceEnable(false);
		}
		
		// need to implement in new medical listener table
		this.medicalDecisionTableObj
				.addBeanToList(medicalDecisionDto);
	}

	private void addChangePreauthListener() {
		changePreauth.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && value.getId().equals(ReferenceTable.COMMONMASTER_YES)) {
					Collection<?> itemIds = cmbCategory.getContainerDataSource().getItemIds();
					cmbCategory.setValue(itemIds.toArray()[0]);
					cmbCategory.setEnabled(false);
					bean.getPreauthMedicalProcessingDetails().setChangeInPreauthFlag("Y");
				} else {
					cmbCategory.setEnabled(true);
					bean.getPreauthMedicalProcessingDetails().setChangeInPreauthFlag("N");
				}
			
			}
		});
	}
	
	public boolean isValid()
	{
		Boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		//String eMsg = "";
		
		if(this.binder == null) {
			hasError = true;
			errorMessages.add("Query or Refer To Bill Entry Reason or Suggest for Rejection or Send For Processing to be entered. </br>");
			return !hasError;
		}
		
		
		if (!this.binder.isValid()) {
		    
		    for (Field<?> field : this.binder.getFields()) {
		    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		    	if (errMsg != null) {
		    		errorMessages.add(errMsg.getFormattedHtmlMessage());
		    	}
		    	hasError = true;
		    }
		 }  else {
			 try {
				 
				/* if(bean.getIsFvrInitiate()){
					 this.bean.setStatusKey(ReferenceTable.ZMR_INITIATE_FIELD_REQUEST_STATUS);
					 this.bean.getPreauthMedicalDecisionDetails().setAllocationTo(this.bean.getPreauthMedicalProcessingDetails().getAllocationTo());
					 this.bean.getPreauthMedicalDecisionDetails().setPriority(this.bean.getPreauthMedicalProcessingDetails().getPriority());
				 }*/
				 
				this.binder.commit();
				this.bean.getPreauthMedicalDecisionDetails().setQueryRemarks(this.bean.getPreauthMedicalProcessingDetails().getQueryRemarks());
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		showOrHideValidation(false);
		return !hasError;
	}
	
	public List<String> getErrors() {
		return this.errorMessages;
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalProcessingDTO>(PreauthMedicalProcessingDTO.class);
		this.binder.setItemDataSource(this.bean.getPreauthMedicalProcessingDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		try {
			this.binder.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
						if(bean.getIsNonAllopathic()) {
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
						bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
						approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
						if ((bean.getStatusKey()
								.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS))) {
							
							approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
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

									
								} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
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
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
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

									
								} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
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
						if(bean.getIsNonAllopathic()) {
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
						
						bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
						approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
						if ((bean.getStatusKey()
								.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS))) {
							
							approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
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

								
								} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
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
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
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

									
								} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
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
	
	protected void addTotalClaimedListener()
	{
		this.uploadDocumentListenerTableObj.claimedAmtField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != uploadDocumentListenerTableObj)
				{
					
					String provisionAmt = (String)event.getProperty().getValue();
					if(null != provisionAmt && !("").equalsIgnoreCase(provisionAmt))
					{
						if(SHAUtils.isValidDouble(provisionAmt)) {
							bean.setAmountConsidered(uploadDocumentListenerTableObj.getHospitalizationAmount() != null ? uploadDocumentListenerTableObj.getHospitalizationAmount(): bean.getAmountConsidered());
							if(null != amountConsidered) {
								amountConsidered.setValue(bean.getAmountConsidered());
							}
							if(null != amountConsidered) {
								amountConsideredTable.setDynamicValues(null, true, false,true ,false);
							}
						}
					}
				}
			}
			
		});
		
		/*//Code to written for saving the total net amount.
		
		this.uploadDocumentListenerTableObj.netAmtField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != uploadDocumentListenerTableObj)
				{
					String provisionAmt = (String)event.getProperty().getValue();
					if(null != provisionAmt && !("").equalsIgnoreCase(provisionAmt))
					{
						if(SHAUtils.isValidDouble(provisionAmt)) {
							bean.setAmountConsidered(uploadDocumentListenerTableObj.getHospitalizationAmount() != null ? uploadDocumentListenerTableObj.getHospitalizationAmount(): bean.getAmountConsidered());
							if(amountConsidered != null) {
								amountConsidered.setValue(bean.getAmountConsidered());
							}
							if(amountConsideredTable != null) {
								amountConsideredTable.setDynamicValues(null, true);
							}
						}
					}
				}
			}
			
			
		});*/
		
		
		
	}
	
	public void setAmountConsideredValue(Double sumValue, PreauthDTO bean) {
		this.amountConsidered.setValue(String.valueOf(sumValue.longValue()));
	}

	private Button getCancelButton(final ConfirmDialog dialog) {
		cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		return cancelButton;
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
				
				if(null != viewQueryDTO.getStatusId() && !ReferenceTable.PARALLEL_QUERY_CANCELLED.equals(viewQueryDTO.getStatusId())){
					
					count++;
				}
			}	
			
			return count;
		}


	/*@Override
	public void generateFieldsForInitiateInvestigation(String IntimationNo,
			Boolean InvRequired,Long stageKey, MedicalApprovalPremedicalProcessingUI parent) {
		removeComponents();
		initBinder();		
		ViewInvestigationDetails viewInvestigationDetails = viewDetails.getRevisedInvestigationDetails(IntimationNo, InvRequired,stageKey,parent);
		UI.getCurrent().addWindow((ViewInvestigationDetails)viewInvestigationDetails);
	}*/
	
	public InitiateInvestigationDTO getInitInvDto(){
		return this.bean.getInitInvDto();
	}
	
	public void setClearReferenceData(){
		SHAUtils.setClearReferenceData(referenceData);
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


@Override
public void generateFieldsForInitiateInvestigation(String IntimationNo,
		Boolean InvRequired, Long stageKey,
		MedicalApprovalPremedicalProcessingUI parent) {
	// TODO Auto-generated method stub
	
}
	public void showAmtConsiderAlert(final ConfirmDialog dialog){
	
	if(medicalDecisionTableObj.getTotalAmountConsidered() < (
			SHAUtils.getIntegerFromString(bean.getAmountConsidered()))){
		buildSuccessLayout("Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table", dialog);
	}else{
		validatePage(dialog);	
	}
	}
	
	
	private void buildSuccessLayout(String message, final ConfirmDialog confirmDialog)
	{/*
	  	Label successLabel = new Label("<b style = 'color: red;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		 HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final Window dialog = new Window();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		
		getUI().getCurrent().addWindow(dialog);
	
//		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {					
				dialog.close();
				validatePage(confirmDialog);
			}
		});
		cancelButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
	*/

		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "OK");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "Cancel");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());
		
		homeButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {					
				
				validatePage(confirmDialog);
			}
		});
		cancelButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
			}
		});
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void buildInitiateFieldVisit() {
		removeComponents();
		initBinder();

		allocationTo = (ComboBox) binder.buildAndBind("Allocation To","allocationTo",ComboBox.class);
		allocationTo.setContainerDataSource((BeanItemContainer<SelectValue>) referenceData.get("allocateTo"));
		allocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		allocationTo.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo() != null){
			allocationTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo());
		}
		
		/*fvrAssignTo = (ComboBox) binder.buildAndBind("Assign To","assignTo",ComboBox.class);
		fvrAssignTo.setContainerDataSource(map.get("fvrAssignTo"));
		fvrAssignTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		fvrAssignTo.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getAssignTo() != null){
			fvrAssignTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAssignTo());
		}*/
		
		fvrPriority = (ComboBox) binder.buildAndBind("Priority","priority",ComboBox.class);
		fvrPriority.setContainerDataSource((BeanItemContainer<SelectValue>) referenceData.get("priority"));
		fvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		fvrPriority.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getPriority()!= null){
			fvrPriority.setValue(this.bean.getPreauthMedicalDecisionDetails().getPriority());
		}

//		fvrTriggerPoints = (TextArea) binder.buildAndBind("FVR Trigger Points","fvrTriggerPoints",TextArea.class);
//		fvrTriggerPoints.setMaxLength(4000);
//		fvrTriggerPoints.setWidth("400px");
		
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
		
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(allocationTo);
//		mandatoryFields.add(fvrTriggerPoints);
		//mandatoryFields.add(fvrAssignTo);
		mandatoryFields.add(fvrPriority);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(horizontalLayout2, triggerPtsTableObj);
		if(fvrVertLayout != null){
			fvrVertLayout.addComponent(VLayout);	
		}
		
	}
	
	@Override
	public void generateFieldsForReferToBillEntry() {
		removeComponents();
		initBinder();
		reasonRefBillEntryTxta = (TextArea) binder.buildAndBind("Reason For Referring To Bill Entry", "refBillEntyRsn", TextArea.class);
		reasonRefBillEntryTxta.setWidth("400px");
		reasonRefBillEntryTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(reasonRefBillEntryTxta,null);
		
		mandatoryFields.add(reasonRefBillEntryTxta);
		showOrHideValidation(false);
		FormLayout fieldsFLayout;
		fieldsFLayout = new FormLayout(reasonRefBillEntryTxta);
				
		
		final ConfirmDialog dialog = new ConfirmDialog();
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(isValid()) {
					dialog.close();
				} else {
					StringBuffer eMsg = new StringBuffer();
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButton,getCancelButton(dialog));
		btnLayout.setWidth("500px");
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		
		VerticalLayout verticalLayout = new VerticalLayout(fieldsFLayout, btnLayout);
//		verticalLayout.setWidth("500px");
		verticalLayout.setWidth("800px");
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
				
		showInPopup(verticalLayout, dialog);
		
	}
	
	private Button getSubmitButtonWithListener(final ConfirmDialog dialog) {
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();
				if (isValid()) {
					dialog.close();
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
	
	public void generateFieldsForInitiateFVR(){
		final VerticalLayout submainlayout = new VerticalLayout();
		select = new OptionGroup();
		select.addStyleName("horizontal");
		select.addItem("yes");
		select.addItem("no");
		select.setItemEnabled("no", false);
		select.setItemCaption("yes", "Yes");
		select.setItemCaption("no", "No");
		
		final ConfirmDialog dialog = new ConfirmDialog();
		HorizontalLayout selectlayout = new HorizontalLayout(new Label(
				"Initiate Field Visit Request:"), select);
		selectlayout.setSpacing(true);
		submainlayout.setSpacing(true);
		submainlayout.setMargin(true);
		submainlayout.addComponent(selectlayout);
		submainlayout.setComponentAlignment(selectlayout,
				Alignment.MIDDLE_CENTER);
		fvrVertLayout = new VerticalLayout();
		select.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (select.getValue().equals("yes")) {
					if(bean.getPreauthMedicalDecisionDetails()!= null && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() != null && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated().equals(Boolean.FALSE)){
						buildInitiateFieldVisit();
						bean.setIsFvrInitiate(Boolean.TRUE);
					}else{
						bean.setIsFvrInitiate(Boolean.FALSE);
						showErrorPopup("FVR request is in process, cannot initiate another request");
					}
				}else{
					bean.setIsFvrInitiate(Boolean.FALSE);
				}
			}
		});
		
		submainlayout.addComponent(fvrVertLayout);
		submainlayout.setComponentAlignment(fvrVertLayout, Alignment.MIDDLE_CENTER);
		
		submitCancelLayout = new HorizontalLayout(getFvrSubmitButtonWithListener(dialog), getCancelButton(dialog));
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

		// submitButton.addClickListener(new ClickListener() {
		// private static final long serialVersionUID = -5934419771562851393L;
		//
		// @Override
		// public void buttonClick(ClickEvent event) {
		// dialog.close();
		// }
		// });

		dialog.show(getUI().getCurrent(), null, true);

	}
	
	private Button getFvrSubmitButtonWithListener(final ConfirmDialog dialog) {
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();
				if (isValidFVR()) {
						fireViewEvent(
								MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_REVIEW_AUTO_SKIP_FVR,
								bean);	
					dialog.close();
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
	
	public boolean isValidFVR()
	{
		Boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		//String eMsg = "";
		
		if(this.binder == null) {
			hasError = true;
			errorMessages.add("Query or Suggest for Rejection or Send For Processing to be entered. </br>");
			return !hasError;
		}
		

		if(triggerPtsTable != null){
			hasError = !triggerPtsTableObj.isValid();
			if(hasError){
				errorMessages.add("Please Provide atleast one FVR Trigger Point.</br>");
				errorMessages.add("FVR Trigger Points size will be Max. of 600.<br>");
			}
			else{
				bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());
				bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
			}
		}
	
		
		if (!this.binder.isValid()) {
		    
		    for (Field<?> field : this.binder.getFields()) {
		    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		    	if (errMsg != null) {
		    		errorMessages.add(errMsg.getFormattedHtmlMessage());
		    	}
		    	hasError = true;
		    }
		 }  else {
			 try {
				 
				 if(bean.getIsFvrInitiate()){
					 this.bean.setStatusKey(ReferenceTable.ZMR_INITIATE_FIELD_REQUEST_STATUS);
				 }
				 
				this.binder.commit();
				this.bean.getPreauthMedicalDecisionDetails().setQueryRemarks(this.bean.getPreauthMedicalProcessingDetails().getQueryRemarks());
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		showOrHideValidation(false);
		return !hasError;
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
				
				
//				if(("hospRmrks").equalsIgnoreCase(txtFld.getId()) || ("insuredRmrks").equalsIgnoreCase(txtFld.getId())){
					txtArea.setRows(25);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					txtArea.setReadOnly(false);
//				}
				
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
					
						txtFld.setValue(((TextArea)event.getProperty()).getValue());						
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				String strCaption = "Reason for Referring to Billentry";

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
	
	public void alertForAdditionalFvrTriggerPoints(final String action,final Integer setQueryValues) {/*	 
		 
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
							
							if(SHAConstants.QUERY.equalsIgnoreCase(action)){
								
								generateReimburemenQueryDetails(setQueryValues);
							}
						}
					});

					
		*/
		 
	
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Add Additional FVR Points");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "Cancel");
			HashMap<String, Object> messageBoxButtons = GalaxyAlertBox
					.createCutomWithCloselBox(SHAConstants.ADDTIONAL_FVR_TRIGGER_POINTS_ALERT, buttonsNamewithType);
			Button addAdditionalFvrButton = (Button) messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
			Button cancelButton = (Button) messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());
			Window window = (Window) messageBoxButtons.get("close");
			addAdditionalFvrButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

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
			
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {

				}
			});
			
					window.addCloseListener(new Window.CloseListener() {
					
						private static final long serialVersionUID = 1L;

						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
							
							if(SHAConstants.QUERY.equalsIgnoreCase(action)){
								
								generateReimburemenQueryDetails(setQueryValues);
							}
						}
					});

					
			
	}
	
	public void buildHoldLayout(){
		
		removeComponents();
		initBinder();
		holdRemarksTxta = (TextArea) binder.buildAndBind("Hold Remarks","holdRemarks",TextArea.class);
		holdRemarksTxta.setMaxLength(4000);
		holdRemarksTxta.setWidth("400px");		
		holdRemarksTxta.setHeight("200px");
		holdRemarksTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		SHAUtils.handleTextAreaPopupDetails(holdRemarksTxta,null,getUI(),SHAConstants.FLP_HOLD_REMARK);
		holdRemarksTxta.setData(bean);
		if(bean.getPreMedicalPreauthMedicalDecisionDetails().getHoldRemarks() != null){
			holdRemarksTxta.setValue(bean.getPreMedicalPreauthMedicalDecisionDetails().getHoldRemarks());
		}
		mandatoryFields.removeAll(mandatoryFields);
		mandatoryFields.add(holdRemarksTxta);

		FormLayout fieldsFLayout = new FormLayout(holdRemarksTxta);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(isValid()) {
					dialog.close();
				} else {
					StringBuffer eMsg = new StringBuffer();
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		
		showOrHideValidation(false);
		HorizontalLayout btnLayout = new HorizontalLayout(submitButton,getCancelButton(dialog));
		btnLayout.setWidth("500px");
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		
		VerticalLayout verticalLayout = new VerticalLayout(fieldsFLayout, btnLayout);
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		showInPopup(verticalLayout, dialog);
	}
}
