package com.shaic.claim.preauth.wizard.view;

import java.sql.Timestamp;
import java.util.ArrayList;



import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import oracle.sql.DATE;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.view.PreAuthService;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingUI;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationDetails;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.reimbursement.draftinvesigation.DraftInvestigatorDto;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;
import com.shaic.reimbursement.draftinvesigation.RevisedDraftInvTriggerPointsTable;
import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterTableDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.shaic.arch.fields.dto.SelectValue;


@SuppressWarnings("unused")
public class ViewInvestigationDetails extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ComboBox assigntoCmb;
	private ComboBox reasonforIniInvesCmb;
	private TextArea reasonforRefering;
	private TextArea triggerPointstoFocus;

	private List<Component> mandatoryFields = new ArrayList<Component>();
	private VerticalLayout mainLayout;
	private BeanFieldGroup<InitiateInvestigationDTO> binder;
	
	private DMSDocumentViewDetailsPage dmsDocViwPage;

	@EJB
	private HospitalService hospitalService;

	@EJB
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationService;

	@EJB
	private InvestigationService investigationService;

	@Inject
	private InvestigationDetailsTable investigationDetailsTable;

	@EJB
	private ClaimService claimService;

	@EJB
	private PreauthService preauthService;
	
	@EJB
	private CreateRODService billDetailsService;
	
	@EJB
	private  ReimbursementService reimbursementService;
	
	@EJB
	private DBCalculationService dbCalService;

	private InvestigationDetailsTableDTO detailsTableDTO;
	
	private InitiateInvestigationDTO initateInvDto; 

	private FormLayout InitiateInvestigationLayout;
	private OptionGroup intiateInvestigationOptionGroup;

	// private boolean isDisabled = false;

	/* public boolean isDisabled = false; */

	private Long claimKey;
	
	private Long preauthKey;
	
	private String userName;
	
	private Long stageKey;
	
	private PreauthDTO bean;
	
	private MedicalApprovalPremedicalProcessingUI parent;
	
	//private MedicalApprovalZonalReviewWizardViewImpl parent;
	
	/*@Inject
	private ZonalReviewPreMedicalProcessingButtonsUI zonalButtonsUI;*/

	@Inject
	private Instance<RevisedDraftInvTriggerPointsTable> draftTriggerPointsTableInstance;
	
	private RevisedDraftInvTriggerPointsTable draftTriggerPointsTableInstanceObj;
	
	@Inject
	private Toolbar toolBar;
	
	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	private String password;
	
	private Claim claim;
	
	private Preauth preauth;

	ViewInvestigationDetails() {
		super("Investigation Details");
		this.setHeight("85%");
		this.setWidth("75%");
		setModal(true);
		setClosable(true);
		setResizable(true);
	}

	// @PostConstruct
	public void init(Boolean isDisabled) {
		initBinder();
		mainLayout = new VerticalLayout();
		Panel mainPanel = new Panel();
		mainLayout.addComponent(investigationDetailsTable);
		investigationDetailsTable.init("", false, false);
		investigationDetailsTable.setSeviceObjects(hospitalService,claimService,investigationService,masterService);
		investigationDetailsTable.setViewDMSDocViewPage(dmsDocViwPage);
		mainPanel.setContent(investigationDetailsTable);
		mainLayout.addComponent(mainPanel);
		InitiateInvestigationLayout = new FormLayout();

		FormLayout radioForm = new FormLayout();
		intiateInvestigationOptionGroup = new OptionGroup(
				"Initiate Investigation");
		intiateInvestigationOptionGroup.addItem("Yes");
		intiateInvestigationOptionGroup.addItem("No");
		intiateInvestigationOptionGroup.setValue("No");
		intiateInvestigationOptionGroup.setStyleName("inlineStyle");
		getOptionGroupAction(intiateInvestigationOptionGroup);
		if (isDisabled) {
			radioForm.addComponent(intiateInvestigationOptionGroup);
		}
		mainLayout.addComponent(radioForm);

		setContent(mainLayout);
	}
	
	public void init(Boolean isDisabled,PreauthDTO bean) {
		initBinder();
		mainLayout = new VerticalLayout();
		Panel mainPanel = new Panel();
		this.bean = bean;
		mainLayout.addComponent(investigationDetailsTable);
		investigationDetailsTable.init("", false, false);
		mainPanel.setContent(investigationDetailsTable);
		mainLayout.addComponent(mainPanel);
		InitiateInvestigationLayout = new FormLayout();

		FormLayout radioForm = new FormLayout();
		intiateInvestigationOptionGroup = new OptionGroup(
				"Initiate Investigation");
		intiateInvestigationOptionGroup.addItem("Yes");
		intiateInvestigationOptionGroup.addItem("No");
		intiateInvestigationOptionGroup.setValue("No");
		intiateInvestigationOptionGroup.setStyleName("inlineStyle");
		getOptionGroupAction(intiateInvestigationOptionGroup);
		if (isDisabled) {
			radioForm.addComponent(intiateInvestigationOptionGroup);
		}
		mainLayout.addComponent(radioForm);

		setContent(mainLayout);
	}

	private void getOptionGroupAction(
			OptionGroup intiateInvestigationOptionGroup) {
		intiateInvestigationOptionGroup
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(
							com.vaadin.v7.data.Property.ValueChangeEvent event) {
						if (event.getProperty() != null
								&& event.getProperty().getValue().toString() == "Yes") {
							InitiateInvestigationLayout = buildInitiateInvestigationLayout();
							mainLayout
									.addComponent(InitiateInvestigationLayout);
						} else {
							InitiateInvestigationLayout.removeAllComponents();
							binder.discard();
							mainLayout
									.removeComponent(InitiateInvestigationLayout);
						}

					}
				});
	}

	private FormLayout buildInitiateInvestigationLayout() {
		unbindField(assigntoCmb);
		assigntoCmb = binder.buildAndBind("Allocation to", "allocationTo",
				ComboBox.class);
		assigntoCmb.setRequired(true);
		assigntoCmb.setContainerDataSource(masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO_INVESTIGATION));
		assigntoCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		assigntoCmb.setItemCaptionPropertyId("value");
		
		unbindField(reasonforIniInvesCmb);
		reasonforIniInvesCmb = binder.buildAndBind("Reason for Initiating Investigation", "reasonforInitiatingInvestigation",
				ComboBox.class);
		reasonforIniInvesCmb.setRequired(true);
		reasonforIniInvesCmb.setContainerDataSource(masterService.getSelectValueContainer(ReferenceTable.REASON_FOR_INITIATE_INVESTIGATION));
		reasonforIniInvesCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonforIniInvesCmb.setItemCaptionPropertyId("value");
		
		reasonforIniInvesCmb.addValueChangeListener( new ValueChangeListener() {

				@Override

				public void valueChange(ValueChangeEvent event) {

					SelectValue value = event.getProperty().getValue() != null ? (SelectValue) event.getProperty().getValue() : null;

					if(value != null) { 
						reasonforIniInvesCmb.setValue(value);
						if(ReferenceTable.INITIATE_INVESTIGATION_OTHERS.equals(value.getId())) {
							showOrHideValidation(false);
							reasonforRefering.setRequired(true);
							reasonforRefering.setEnabled(true);
							mandatoryFields.remove(reasonforRefering);
						}	
						
					}
				}
		 });
		
		unbindField(reasonforRefering);
		reasonforRefering = binder.buildAndBind("Reason for Refering",
				"reasonForRefering", TextArea.class);
		//reasonforRefering.setRequired(true);
		reasonforRefering.setMaxLength(4000);
		reasonforRefering.setNullRepresentation("");
		reasonforRefering.setId("reasonforRefering");
		reasonforRefering.setData(bean);
		getF8ForReasonforReferring(reasonforRefering,null);
		
		unbindField(triggerPointstoFocus);
		/*triggerPointstoFocus = binder.buildAndBind("Trigger points to focus",
				"triggerPointsToFocus", TextArea.class);
		triggerPointstoFocus.setRequired(true);
		triggerPointstoFocus.setMaxLength(4000);
		triggerPointstoFocus.setNullRepresentation("");
		triggerPointstoFocus.setId("reasonforRefering");
		triggerPointstoFocus.setData(bean);
		getF8ForReasonforReferring(triggerPointstoFocus,null);*/
		
		draftTriggerPointsTableInstanceObj = draftTriggerPointsTableInstance.get();
		draftTriggerPointsTableInstanceObj.init("Trigger Points to focus");
		

		mandatoryFields.add(assigntoCmb);
		mandatoryFields.add(reasonforIniInvesCmb);
		//mandatoryFields.add(reasonforRefering);
		//mandatoryFields.add(triggerPointstoFocus);
		Button submitBtn = new Button("Submit");
		submitBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submitBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {

				if (validatePage()) {

					InvestigationMapper investigationMapper = new InvestigationMapper();

					Investigation investigation = investigationMapper
							.getInvestigation(detailsTableDTO);
					
					InitiateInvestigationDTO dto = binder.getItemDataSource()
							.getBean();
					
					if (investigation != null) {

						preauth = preauthService.searchByKey(preauthKey);

						dto.setPolicyKey(investigation.getPolicy().getKey());
						dto.setIntimationkey(investigation.getIntimation()
								.getKey());
						dto.setClaimKey(investigation.getClaim().getKey());
						if (preauth != null) {
							dto.setTransactionKey(preauth.getKey());
							dto.setTransactionFlag("C");		
						}
					} else {
						try {
							preauth = preauthService.searchByKey(preauthKey);
							if (preauth != null) {
								claim = claimService.getClaimByClaimKey(preauth
										.getClaim().getKey());
							}
							if(null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()){
								claim = claimService.getClaimByClaimKey(bean.getClaimKey());
							}
							// Claim claim = claimService
							// .getClaimByClaimKey(preauth.getClaim().getKey());

							if (claim != null) {
								dto.setPolicyKey(claim.getIntimation()
										.getPolicy().getKey());
								dto.setIntimationkey(claim.getIntimation()
										.getKey());
								dto.setClaimKey(claim.getKey());
								Stage stage = preauthService
										.getStageByKey(claim.getStage()
												.getKey());
								Status status = null;
								if (preauth != null) {
									dto.setTransactionKey(preauth.getKey());
									dto.setTransactionFlag("C");
														
								}
								
								List<DraftTriggerPointsToFocusDetailsTableDto> list = draftTriggerPointsTableInstanceObj.getValues();
								
								draftTriggerPointsTableInstanceObj.deleteEmptyRows();

									if(list != null && !list.isEmpty()){
										for(DraftTriggerPointsToFocusDetailsTableDto triggerPointsDto: list){
											triggerPointsDto.setSno(list.indexOf(triggerPointsDto)+1);
										}
									}
									
									dto.setTriggerPointsList(list);
									dto.setDeletedTriggerPointsList(draftTriggerPointsTableInstanceObj.getDeletedDraftInvgDescList());
								dto.setStage(claim.getStage());
								dto.setStatus(status);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (claim != null) {
						dto.setClaimKey(claim.getKey());
						dto.setIntimationkey(claim.getIntimation().getKey());
						dto.setPolicyKey(claim.getIntimation().getPolicy()
								.getKey());
						Status status = null;
						if (ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY
								.equals(claim.getStage().getKey()) || ReferenceTable.CREATE_ROD_STAGE_KEY.equals(claim.getStage().getKey())) {
							status = preauthService
									.getStatusByPreauth(ReferenceTable.ZONAL_REVIEW_INITATE_INV_STATUS);
						} else if (ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY
								.equals(claim.getStage().getKey())) {
							status = preauthService
									.getStatusByPreauth(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);

						}
						dto.setStage(claim.getStage());
						dto.setStatus(status);
					}
					if (!ReferenceTable.ZONAL_REVIEW_STAGE.equals(stageKey) && !ReferenceTable.CLAIM_REQUEST_STAGE.equals(stageKey)) {
						userName = String.valueOf(VaadinSession.getCurrent()
								.getAttribute(BPMClientContext.USERID));

						if (claim != null && preauth != null) {

							Hospitals hospital = claimService
									.getHospitalById(claim.getIntimation()
											.getHospital());
							Map<String, Object> workFlowPayload = SHAUtils
									.getRevisedPayloadMap(claim, hospital);
							Stage stage = preauthService.getStageByKey(claim
									.getStage().getKey());
							if (ReferenceTable.PREAUTH_STAGE.equals(preauth
									.getStage().getKey())
									|| ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE
											.equals(preauth.getStage().getKey())) {
								stage = preauthService
										.getStageByKey(ReferenceTable.PREAUTH_STAGE);
								/*workFlowPayload
										.put(SHAConstants.OUTCOME,
												SHAConstants.OUTCOME_PROCESS_PREAUTH_INITIATE_INVS);*/
								workFlowPayload.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.PP_CURRENT_QUEUE);
								if(null != bean && null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy()){
									workFlowPayload
									.put(SHAConstants.OUTCOME,
											SHAConstants.OUTCOME_CASHLESS_INITIATE_INVS_DIRECT_TO_ASSIGN);
								}
								else{
									
									workFlowPayload
									.put(SHAConstants.OUTCOME,
											SHAConstants.OUTCOME_CASHLESS_TATA_TRUST_INVS_DIRECT_TO_UPLOAD);
								}
								
							} else if ((ReferenceTable.ENHANCEMENT_STAGE
									.equals(preauth.getStage().getKey()))
									|| ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE
											.equals(preauth.getStage().getKey())) {
								stage = preauthService
										.getStageByKey(ReferenceTable.ENHANCEMENT_STAGE);
								workFlowPayload.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.PE_CURRENT_QUEUE);
							/*	workFlowPayload
										.put(SHAConstants.OUTCOME,
												SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_INITIATE_INVS);*/
								if(null != bean && null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy()){
									workFlowPayload
									.put(SHAConstants.OUTCOME,
											SHAConstants.OUTCOME_CASHLESS_INITIATE_INVS_DIRECT_TO_ASSIGN);
								}
								else
								{
									workFlowPayload
									.put(SHAConstants.OUTCOME,
											SHAConstants.OUTCOME_CASHLESS_TATA_TRUST_INVS_DIRECT_TO_UPLOAD);
								}
								
							}

							investigation = createInvestigation(investigation,
									dto, workFlowPayload, stage);
						}
						else if(null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy())
						{
							
							Hospitals hospital = claimService
									.getHospitalById(claim.getIntimation()
											.getHospital());
							Map<String, Object> workFlowPayload = SHAUtils
									.getRevisedPayloadMap(claim, hospital);
							
							 Stage stage = preauthService
									.getStageByKey(ReferenceTable.PREAUTH_STAGE);
							
							workFlowPayload.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,SHAConstants.PP_CURRENT_QUEUE);
							
							if(null != bean && null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy()){
								workFlowPayload
								.put(SHAConstants.OUTCOME,
										SHAConstants.OUTCOME_CASHLESS_INITIATE_INVS_DIRECT_TO_ASSIGN);
							}
							else{
								
								workFlowPayload
								.put(SHAConstants.OUTCOME,
										SHAConstants.OUTCOME_CASHLESS_TATA_TRUST_INVS_DIRECT_TO_UPLOAD);
							}
							

							investigation = createInvestigation(investigation,
									dto, workFlowPayload, stage);
						}
						

						showValues(dto.getClaimKey());
						
						if(dto.getTriggerPointsList() != null && !dto.getTriggerPointsList().isEmpty()){
							investigationService.submitInitiateLevelInvestigationTriggerPointsDetails(dto.getTriggerPointsList(), investigation, dto);
						}
						
						InitiateInvestigationLayout.removeAllComponents();
						binder.discard();

						mainLayout.removeComponent(InitiateInvestigationLayout);
						intiateInvestigationOptionGroup.setValue("No");
						initBinder();
					} else {
						initateInvDto = dto;
						parent.setInitiateInitInvDto(initateInvDto);
						//zonalButtonsUI.setEnableOrDisableButtons();
						bean.getPreauthDataExtractionDetails().setIsFvrOrInvsInitiated(Boolean.TRUE);
						buildSuccessLayout();
					}
				}
			}

			private Investigation createInvestigation(
					Investigation investigation, InitiateInvestigationDTO dto,
					Map<String, Object> workFlowPayload, Stage stage) {
				dto.setStage(stage);
				Status status = preauthService
						.getStatusByKey(ReferenceTable.INITIATE_INVESTIGATION);
				dto.setStatus(status);
				String docToken = uploadCashlessInvestigationLetter(bean,dto);                  // CR R1163
				//investigation.setToken(docToken);
				investigation = investigationService.create(dto,
						investigation, userName, password,bean,docToken);
				
				AssignedInvestigatiorDetails assInvDetails = new AssignedInvestigatiorDetails();
				
				if(null != bean && null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()){
					 assInvDetails = investigationService.insertInvsDetailsForTataTrust(investigation,dto,bean);
				}
				
				Map<String, Object> workFlowObj = (Map<String, Object>) bean.getDbOutArray();
				
				Long wkKey = (Long)workFlowObj.get(SHAConstants.WK_KEY);	
				workFlowPayload.put(SHAConstants.RRC_REQUEST_KEY, wkKey);
				workFlowPayload.put(SHAConstants.WK_KEY, 0);
				workFlowPayload.put(SHAConstants.STAGE_SOURCE,dto.getStage().getStageName());
				workFlowPayload.put(SHAConstants.CASHLESS_NO, null != preauth ?preauth.getPreauthId(): null);
				workFlowPayload.put(SHAConstants.CASHLESS_KEY, null != preauth ? preauth.getKey():0L);
//	workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_ZMR_INITIATE_INVESTIGATION_STATUS);
				workFlowPayload.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY,investigation.getKey() != null ? investigation.getKey() : 0);
				
				if(null != bean && null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()){
					workFlowPayload.put(SHAConstants.RRC_REQUEST_KEY, assInvDetails.getKey());
				}
				//CR2019058 new date parameter for submit task 
				workFlowPayload.put(SHAConstants.INV_INITIATED_DATE, new Timestamp(System.currentTimeMillis()));
				workFlowPayload.put(SHAConstants.INV_APPROVED_DATE, null);
				workFlowPayload.put(SHAConstants.INV_REQ_DRAFTED_DATE, null);
				

				if (investigation != null) {
//								DBCalculationService dbServcie = new DBCalculationService();
//					Object[] inputWorkFlowArray = SHAUtils.getRevisedObjArrayForSubmit(workFlowPayload);
					Object[] inputWorkFlowArray = SHAUtils.getRevisedObjArrayForAssignInvestigationSubmit(workFlowPayload);

//					dbCalService.revisedInitiateTaskProcedure(inputWorkFlowArray);
					dbCalService.revisedAssignInvestigationTaskProcedure(inputWorkFlowArray); //CR2019058
					buildSuccessLayout();
				}
				return investigation;
			}
		});


		Button canselBtn = new Button("Cancel");
		canselBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		canselBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {

				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Confirmation", "Are you sure you want to cancel ?",
						"Cancel", "Ok", new ConfirmDialog.Listener() {

							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									close();

								} else {
									// User did not confirm
								}
							}
						});

			}

		});
		HorizontalLayout buttonHLayout = new HorizontalLayout(submitBtn,
				canselBtn);
		buttonHLayout.setSpacing(true);
		buttonHLayout.setComponentAlignment(submitBtn, new Alignment(34));
		buttonHLayout.setComponentAlignment(canselBtn, new Alignment(33));
		// buttonHLayout
		// .addComponent(submitBtn, "top:200.0px;left:354.0px;");
		buttonHLayout.setWidth("100%");	
		draftTriggerPointsTableInstanceObj.setWidth("80%");
		//triggerPointsDetailLayout.addComponent(draftTriggerPointsTableInstanceObj);
		
		return new FormLayout(assigntoCmb, reasonforRefering,
				draftTriggerPointsTableInstanceObj, buttonHLayout);
	}
	
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Claim Record Saved Successfully!!!</b>",
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
							
				if(null != stageKey && (stageKey.equals(ReferenceTable.ZONAL_REVIEW_STAGE) ||
						stageKey.equals(ReferenceTable.PREAUTH_STAGE) || stageKey.equals(ReferenceTable.ENHANCEMENT_STAGE)))
				{	Collection<Window> windows =  (Collection<Window>)getUI().getCurrent().getWindows();
					Object[] winArray = windows.toArray();
					for(int i = 0; i < winArray.length;i++){
						((Window)winArray[i]).close();
					}
				}
				dialog.close();
//				toolBar.countTool(); -- As Per BA Revised Req sub flow process won't considered as count
				
			}
		});
		
	}

	private void initBinder() {

		this.binder = new BeanFieldGroup<InitiateInvestigationDTO>(
				InitiateInvestigationDTO.class);
		this.binder.setItemDataSource(new InitiateInvestigationDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private void unbindField(Field<?> field) {
		if (field != null && field.getValue() == null
				&& binder.getPropertyId(field) != null) {
			this.binder.unbind(field);
		}
	}

	public void showValues(Long claimKey) {
		this.claimKey = claimKey;
		showInvestigationValues(this.claimKey);
		this.stageKey = 0l;
	}
	public void showRevisedValues(Long claimKey,Long stageKey,MedicalApprovalPremedicalProcessingUI parent) {
		this.claimKey = claimKey;
		showInvestigationValues(this.claimKey);
		this.stageKey = stageKey;
		this.parent = parent;
	}
	
	public void showRevisedValues(Long claimKey,Long stageKey,PreauthDTO bean) {
		this.claimKey = claimKey;
		showInvestigationValues(this.claimKey);
		this.stageKey = stageKey;
		//this.parent = parent;
	}
	public void showValuesForPreauth(Long claimKey, Long preauthKey, String userName, String password){
		this.claimKey = claimKey;
		this.preauthKey = preauthKey;
		this.stageKey = 0l;
		this.userName = userName;
	    this.password = password;
	    showInvestigationValues(this.claimKey);
		
	}
	
	public void showRevisedValues(Long claimKey,Long stageKey,MedicalApprovalPremedicalProcessingUI parent,PreauthDTO bean) {
		this.bean = bean;
		this.claimKey = claimKey;
		showInvestigationValues(this.claimKey);
		this.stageKey = stageKey;
		this.parent = parent;
	}

	/**
	 * Added for ticket 756. This method is used only by process enhancement
	 * screen.
	 * 
	 * */

	public void showValues(Long claimKey, boolean isDisabled) {
		this.claimKey = claimKey;
		
		/*
		 * if(isDisabled) { intiateInvestigationOptionGroup.setEnabled(false);
		 * intiateInvestigationOptionGroup.setReadOnly(true); } else {
		 * intiateInvestigationOptionGroup.setEnabled(true);
		 * intiateInvestigationOptionGroup.setReadOnly(false); }
		 */
		showInvestigationValues(this.claimKey);
	}

	/**
	 * show values code is now being used by two method. This is a common code
	 * and hence below method is introduced to reuse the same.Instead of
	 * repeating the same code twice, the below method can be invoked.
	 * */
	private void showInvestigationValues(Long ClaimKey) {
	//	List<Investigation> investigationList = investigationService
//			.getByClaimKey(ClaimKey);
			
	List<Investigation> investigationList = investigationService
			.getLatestListByClaimKey(ClaimKey);
	
	Claim claimByClaimKey = claimService.getClaimByClaimKey(this.claimKey);
	
	this.claim = claimByClaimKey;
	
	

	if (investigationList.size() != 0 || investigationList == null) {

		List<InvestigationDetailsTableDTO> investigationDetailsTableDTOList = new ArrayList<InvestigationDetailsTableDTO>();
		Integer index = 1;

			for (Investigation investigation : investigationList) {
				
				List<AssignedInvestigatiorDetails> assignedObjList = investigationService.getAssignedListByInvestigationKey(investigation.getKey());
				if(assignedObjList != null && !assignedObjList.isEmpty()){
					for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : assignedObjList) {
						InvestigationDetailsTableDTO investigationDetailsTableDTO = new InvestigationDetailsTableDTO();
						InvestigationMapper investigationMapper = new InvestigationMapper();
						investigationDetailsTableDTO = investigationMapper
								.getInvestigationDetailsTableDTO(investigation);
					
						investigationDetailsTableDTO.setInvestigatorName(assignedInvestigatiorDetails.getInvestigatorName());
						investigationDetailsTableDTO.setInvestigatorCode(assignedInvestigatiorDetails.getInvestigatorCode());
						investigationDetailsTableDTO.setInvestigAssignedKey(assignedInvestigatiorDetails.getKey());
						investigationDetailsTableDTO.setInvestigationTriggerPoints(investigation.getTriggerPoints());
						investigationDetailsTableDTO.setStatus(assignedInvestigatiorDetails.getStatus().getProcessValue() != null ? assignedInvestigatiorDetails.getStatus().getProcessValue() : "");
						
						Hospitals hospObj = hospitalService.getHospitalById(investigationDetailsTableDTO.getHospitalkey());
						
							if(hospObj != null){
								
								investigationDetailsTableDTO.setHospitalName(hospObj.getName());
							}
							else{
								investigationDetailsTableDTO.setHospitalName("");
							}
								
														
						if (assignedInvestigatiorDetails.getInvestigatorCode() != null) {
							if(assignedInvestigatiorDetails.getZoneCode() != null &&  !(assignedInvestigatiorDetails.getInvestigatorCode().contains("INV"))){
								Long privateInvKey = Long.valueOf(assignedInvestigatiorDetails.getInvestigatorCode());
								MasPrivateInvestigator privateInvestigation = investigationService.getPrivateInvestigatorByKey(privateInvKey);
								investigationDetailsTableDTO
								.setInvestigatorContactNo(privateInvestigation.getMobileNumberTwo() != null ? privateInvestigation.getMobileNumberTwo().toString() : "");
								investigationDetailsTableDTO.setInvestigatorCode("");
							} else {
								TmpInvestigation tmpInvestigation = investigationService
										.getTmpInvestigationByInactiveInvestigatorCode(assignedInvestigatiorDetails.getInvestigatorCode());
								investigationDetailsTableDTO
								.setInvestigatorContactNo(tmpInvestigation.getPhoneNumber() != null ? tmpInvestigation.getPhoneNumber().toString() : "");
							}
							
						}
						
						if (assignedInvestigatiorDetails.getCreatedDate() != null
								&& !assignedInvestigatiorDetails.getCreatedDate().equals("")) {
							investigationDetailsTableDTO
							.setInvestigationAssignedDate(SHAUtils
									.formatDate(assignedInvestigatiorDetails.getCreatedDate()));
							if(assignedInvestigatiorDetails.getCompletionDate() != null){
								Long noOfDays = SHAUtils.getDaysBetweenDate(assignedInvestigatiorDetails.getCreatedDate(), assignedInvestigatiorDetails.getCompletionDate());
								investigationDetailsTableDTO.setTat(noOfDays.toString());
							}					
					    }
						if (assignedInvestigatiorDetails.getCompletionDate() != null) {
							investigationDetailsTableDTO
							.setInvestigationCompletedDateStr(SHAUtils
									.formatDate(assignedInvestigatiorDetails.getCompletionDate()));
						}			
						
						if(assignedInvestigatiorDetails.getReportReceivedDate() != null){
							investigationDetailsTableDTO.setInvestigationReportReceivedDate(SHAUtils.formatDate(assignedInvestigatiorDetails.getReportReceivedDate()));
						}
						
						investigationDetailsTableDTO.setsNo(index);
						index++;


					if(null != investigation.getTransactionFlag() && SHAConstants.CASHLESS_CHAR.equalsIgnoreCase(investigation.getTransactionFlag())){
						List<InvestigationDetails> cashlessInvsTriggerPoints = investigationService.getCashlessInvsTriggerPoints(investigation.getKey());
						if(cashlessInvsTriggerPoints != null && !cashlessInvsTriggerPoints.isEmpty()){
							StringBuffer triggerPoints = new StringBuffer();
							for (InvestigationDetails investigationDetails : cashlessInvsTriggerPoints) {
								triggerPoints = triggerPoints.append(investigationDetails.getDraftOrReDraftRemarks()).append(",");
							}
							investigationDetailsTableDTO.setInvestigationTriggerPoints(String.valueOf(triggerPoints));
						}	
					}
					investigationDetailsTableDTOList.add(investigationDetailsTableDTO);
			    }
			}
			if(ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey()) || (assignedObjList == null || (assignedObjList != null && assignedObjList.isEmpty()))){
				
				InvestigationDetailsTableDTO investigationDetailsTableDTO = new InvestigationDetailsTableDTO();
				InvestigationMapper investigationMapper = new InvestigationMapper();
				investigationDetailsTableDTO = investigationMapper
						.getInvestigationDetailsTableDTO(investigation);
				investigationDetailsTableDTO.setInvestigationTriggerPoints(investigation.getTriggerPoints());
	
				investigationDetailsTableDTO.setIntimationkey(investigation.getKey());
				investigationDetailsTableDTO.setInvestigAssignedKey(null);
				investigationDetailsTableDTO.setHospitalName(hospitalService
						.getHospitalById(investigationDetailsTableDTO
								.getHospitalkey()) == null ? ""
						: hospitalService.getHospitalById(
								investigationDetailsTableDTO.getHospitalkey())
								.getName());
				if (investigationDetailsTableDTO.getInvestigatorCode() != null) {
					TmpInvestigation tmpInvestigation = investigationService
							.getTmpInvestigationByInactiveInvestigatorCode(investigationDetailsTableDTO
									.getInvestigatorCode());
					investigationDetailsTableDTO
							.setInvestigatorContactNo(tmpInvestigation
									.getPhoneNumber().toString());
				}
				investigationDetailsTableDTO.setsNo(investigationDetailsTableDTOList.size()+1);
				if(null != investigation.getTransactionFlag() && SHAConstants.CASHLESS_CHAR.equalsIgnoreCase(investigation.getTransactionFlag())){
					List<InvestigationDetails> cashlessInvsTriggerPoints = investigationService.getCashlessInvsTriggerPoints(investigation.getKey());
					if(cashlessInvsTriggerPoints != null && !cashlessInvsTriggerPoints.isEmpty()){
						StringBuffer triggerPoints = new StringBuffer();
						for (InvestigationDetails investigationDetails : cashlessInvsTriggerPoints) {
							triggerPoints = triggerPoints.append(investigationDetails.getDraftOrReDraftRemarks()).append(",");
						}
						investigationDetailsTableDTO.setInvestigationTriggerPoints(String.valueOf(triggerPoints));
					}
				}
				investigationDetailsTableDTOList.add(investigationDetailsTableDTO);
			}
			
		}
		if (investigationDetailsTableDTOList.size() > 0) {
			investigationDetailsTable
					.setTableList(investigationDetailsTableDTOList);
		}
	} else {
	}

	}

	private void showOrHideValidation(Boolean isVisible) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				if (field != null) {
					field.setRequired(!isVisible);
					field.setValidationVisible(isVisible);
				}
			}

		}
	}

	private boolean validatePage() {
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
		List<DraftTriggerPointsToFocusDetailsTableDto> list = draftTriggerPointsTableInstanceObj.getValues();
		if(null == list || list.isEmpty()){
			hasError = true;
			eMsg.append("Please Enter Trigger Points");
		}
		
		
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			Window window = new Window();
			window.setCaption("ERRORS");
			window.setModal(true);
			window.setWidth("-1px");
			window.setHeight("-1px");
			window.setContent(layout);
			window.setWindowMode(WindowMode.NORMAL);
			UI.getCurrent().addWindow(window);

			hasError = true;
			return !hasError;
		} else {
			try {

				this.binder.commit();

			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
	}

	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	public InitiateInvestigationDTO getInitateInvDto() {
		return initateInvDto;
	}
	
	public void setViewDMSDocViewPage(DMSDocumentViewDetailsPage dmsDocViwPage){
		this.dmsDocViwPage = dmsDocViwPage;
		investigationDetailsTable.setViewDMSDocViewPage(dmsDocViwPage);		
	}
	
	
	/*
	 * public boolean isDisabled() { return isDisabled; }
	 * 
	 * public void setDisabled(boolean isDisabled) { this.isDisabled =
	 * isDisabled; }
	 */

public  void getF8ForReasonforReferring(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForInvesRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForInvs(searchField, getShortCutListenerForInvs(searchField));
	    
	  }
	
	public  void handleShortcutForInvs(final TextArea textField, final ShortcutListener shortcutListener) {
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
	private ShortcutListener getShortCutListenerForInvs(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Invstigation Details",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				PreauthDTO  maindto = (PreauthDTO) txtFld.getData();
				VerticalLayout vLayout =  new VerticalLayout();
				
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
									
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(("reasonforRefering").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());		
						}
						else if(("triggerPointstoFocus").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();
				dialog.setHeight("75%");
			    dialog.setWidth("65%");		
				String strCaption = "";
				
				if(("reasonforRefering").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Reason for Refering";
				}
			    else if(("triggerPointstoFocus").equalsIgnoreCase(txtFld.getId())){
			    	strCaption = "Trigger points to focus";
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
	
	/**
	 *  CR R1163
	 * @param preauthDto
	 * @param invDto
	 */
	public String uploadCashlessInvestigationLetter(PreauthDTO preauthDto,InitiateInvestigationDTO invDto){
		
		String docToken = null;
		DraftInvestigatorDto draftInvestigatorDto = new DraftInvestigatorDto();
		
		draftInvestigatorDto.setClaimDto(preauthDto.getClaimDTO());
		draftInvestigatorDto.getClaimDto().setNewIntimationDto(preauthDto.getNewIntimationDTO());
		draftInvestigatorDto.setDiagnosisName(preauthDto.getPreauthDataExtractionDetails().getDiagnosis());
		draftInvestigatorDto.setTriggerPointsList(invDto.getTriggerPointsList());
		
		Map<String, Object> portablityStatus = dbCalService.getPortablityStatus(preauthDto.getNewIntimationDTO().getIntimationId());
		if (portablityStatus != null) {
			draftInvestigatorDto.getClaimDto().getNewIntimationDto().setPolicyInceptionDate((Date) (portablityStatus.get(SHAConstants.INCEPTION_DATE)));	
		}		
		
		DocumentGenerator docGen = new DocumentGenerator();
		
		if(draftInvestigatorDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getInsuredSumInsured() != null ){
			String amtWords = SHAUtils.getParsedAmount(draftInvestigatorDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getInsuredSumInsured());
			draftInvestigatorDto.getClaimDto().getNewIntimationDto().setComments(amtWords);
		}
		else{
			draftInvestigatorDto.getClaimDto().getNewIntimationDto().setComments(null);
		}
		
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(draftInvestigatorDto.getClaimDto().getClaimId());
		
		List<DraftInvestigatorDto> dtoList = new ArrayList<DraftInvestigatorDto>();
		
		dtoList.add(draftInvestigatorDto);
		reportDto.setBeanList(dtoList);
		
		String templateName = "InvestigationLetter(C)";

		final String filePath = docGen.generatePdfDocument(templateName, reportDto);
		draftInvestigatorDto.setDocFilePath(filePath);
		draftInvestigatorDto.setDocType(SHAConstants.DOC_TYPE_DRAFT_INVESTIGATION_LETTER);
		
		if(null != preauthDto.getStageKey()){
			if(preauthDto.getStageKey().equals(ReferenceTable.PREAUTH_STAGE)){
				draftInvestigatorDto.setDocSource(SHAConstants.PRE_AUTH);
			}
			if(preauthDto.getStageKey().equals(ReferenceTable.ENHANCEMENT_STAGE)){
				draftInvestigatorDto.setDocSource(SHAConstants.ENHANCEMENT);	
			}
		}
		else
		{
			if(null != preauthDto.getNewIntimationDTO().getIsTataPolicy() && preauthDto.getNewIntimationDTO().getIsTataPolicy()){
				draftInvestigatorDto.setDocSource(SHAConstants.PRE_AUTH);
			}
		}
		
		docToken = investigationService.uploadInvLetterToDms(draftInvestigatorDto);
		return docToken;
		}
}
