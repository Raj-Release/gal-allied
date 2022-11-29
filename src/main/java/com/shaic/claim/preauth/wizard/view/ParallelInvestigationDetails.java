package com.shaic.claim.preauth.wizard.view;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
/*import org.vaadin.dialogs.ConfirmDialog;*/






import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.galaxyalert.utils.GalaxyTypeofMessage;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.view.PreAuthService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.MedicalApprovalDataExtractionPageViewImpl;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingUI;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.ZonalReviewPreMedicalProcessingButtonsUI;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.wizard.MedicalApprovalZonalReviewWizardViewImpl;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.reimbursement.draftinvesigation.DraftInvestigatorDto;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;
import com.shaic.reimbursement.draftinvesigation.RevisedDraftInvTriggerPointsTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
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
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("unused")
public class ParallelInvestigationDetails extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ComboBox assigntoCmb;
	private TextArea reasonforRefering;
	private TextArea triggerPointstoFocus;

	private List<Component> mandatoryFields = new ArrayList<Component>();
	private VerticalLayout mainLayout;
	private BeanFieldGroup<InitiateInvestigationDTO> binder;

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
	private PreAuthService preauthhService;
	@EJB
	private ClaimService claimService;

	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ReimbursementService reimbService;

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
	
	public TextField dummyField;
	
	private MedicalApprovalDataExtractionPageViewImpl dataExtractionViewImplObj;
	
	/*@Inject
	private ZonalReviewPreMedicalProcessingButtonsUI zonalButtonsUI;*/
	
	@Inject
	private Instance<RevisedDraftInvTriggerPointsTable> draftTriggerPointsTableInstance;
	
	private RevisedDraftInvTriggerPointsTable draftTriggerPointsTableInstanceObj;
	
	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	private String password;
	
	private Claim claim;
	
	private Preauth preauth;
	
	private Window popup;
	
	@Inject
	private Toolbar toolBar;



	// @PostConstruct
	public void init(Boolean isDisabled) {
		initBinder();
		mainLayout = new VerticalLayout();
		Panel mainPanel = new Panel();
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

		setCompositionRoot(mainLayout);
	}
	
	public void init(Boolean isDisabled,PreauthDTO bean,Window popup,MedicalApprovalDataExtractionPageViewImpl dataExtractionPageViewImpl) {
		initBinder();
		mainLayout = new VerticalLayout();
		this.popup = popup;
		dataExtractionViewImplObj = dataExtractionPageViewImpl;
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
		
		this.dummyField = new TextField();
		
		mainLayout.addComponent(radioForm);

		setCompositionRoot(mainLayout);
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

		unbindField(reasonforRefering);
		reasonforRefering = binder.buildAndBind("Reason for Refering",
				"reasonForRefering", TextArea.class);
		reasonforRefering.setRequired(true);
		reasonforRefering.setMaxLength(4000);
		reasonforRefering.setNullRepresentation("");
		
		mandatoryFields.add(assigntoCmb);
		mandatoryFields.add(reasonforRefering);

		
		if(bean.isDirectToAssignInv()){

			draftTriggerPointsTableInstanceObj = draftTriggerPointsTableInstance.get();
			draftTriggerPointsTableInstanceObj.init("");
			
		}else{
			
			unbindField(triggerPointstoFocus);
			triggerPointstoFocus = binder.buildAndBind("Trigger points to focus",
					"triggerPointsToFocus", TextArea.class);
			triggerPointstoFocus.setRequired(true);
			triggerPointstoFocus.setMaxLength(4000);
			triggerPointstoFocus.setNullRepresentation("");
			mandatoryFields.add(triggerPointstoFocus);
		}

		Button submitBtn = new Button("Submit");
		submitBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submitBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {

				if (validatePage()) {
               //wrongly merged to production build
					bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
//					bean.setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
//					bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
					
					InvestigationMapper investigationMapper = new InvestigationMapper();
					
					InitiateInvestigationDTO dto = binder.getItemDataSource()
							.getBean();
					dto.setPolicyKey(bean.getNewIntimationDTO().getPolicy().getKey());
					dto.setIntimationkey(bean.getNewIntimationDTO()
							.getKey());
					dto.setClaimKey(bean.getClaimDTO().getKey());
					
					dto.setTransactionKey(bean.getKey());
					dto.setTransactionFlag("R");
										
					dto.setStageKey(bean.getStageKey());
					dto.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
					
					Stage stage = new Stage();
					stage.setKey(bean.getStageKey());
					dto.setStage(stage);
					
					Status status = new Status();
					status.setKey(bean.getStatusKey());
					dto.setStatus(status);

					if(bean.isDirectToAssignInv()){
						
						List<DraftTriggerPointsToFocusDetailsTableDto> list = draftTriggerPointsTableInstanceObj.getValues();
					
						draftTriggerPointsTableInstanceObj.deleteEmptyRows();

						if(list != null && !list.isEmpty()){
							for(DraftTriggerPointsToFocusDetailsTableDto triggerPointsDto: list){
								triggerPointsDto.setSno(list.indexOf(triggerPointsDto)+1);
							}
						}
						
						dto.setTriggerPointsList(list);
					}
					
					Investigation investigation = investigationService.submitInvestigation(dto, bean);
					
					if(bean.isDirectToAssignInv()){

						if(dto.getTriggerPointsList() != null && !dto.getTriggerPointsList().isEmpty()){
							investigationService.submitInitiateLevelInvestigationTriggerPointsDetails(dto.getTriggerPointsList(), investigation, dto);
						}
						
						DocumentGenerator docGen = new DocumentGenerator();
						ReportDto reportDto = new ReportDto();
						reportDto.setClaimId(bean.getClaimDTO().getClaimId());
						
						List<DraftInvestigatorDto> dtoList = new ArrayList<DraftInvestigatorDto>();
						DraftInvestigatorDto draftInvestigation = new DraftInvestigatorDto();					

						draftInvestigation.setClaimDto(bean.getClaimDTO());
						draftInvestigation.getClaimDto().setNewIntimationDto(bean.getNewIntimationDTO());
						draftInvestigation.setDischargeDate(bean.getPreauthDataExtractionDetails().getDischargeDate());
						
						if(investigation != null && investigation.getTransactionFlag() != null && investigation.getTransactionKey() != null) {
						
							String diagnosisForTransacByKey = reimbService.getDiganosisByTransacKey(investigation.getTransactionKey());
							draftInvestigation.setDiagnosisName(diagnosisForTransacByKey); 
						}
						
						Double claimedAmt = 0d;
						if(investigation.getTransactionKey() != null){
							Reimbursement reimbObj = reimbService.getReimbursementByKey(investigation.getTransactionKey());
							if(reimbObj != null){
								claimedAmt += reimbObj.getDocAcknowLedgement().getPreHospitalizationClaimedAmount() != null ? reimbObj.getDocAcknowLedgement().getPreHospitalizationClaimedAmount() :0d;
								claimedAmt += reimbObj.getDocAcknowLedgement().getHospitalizationClaimedAmount() != null ? reimbObj.getDocAcknowLedgement().getHospitalizationClaimedAmount() : 0d;
								claimedAmt += reimbObj.getDocAcknowLedgement().getPostHospitalizationClaimedAmount() != null ? reimbObj.getDocAcknowLedgement().getPostHospitalizationClaimedAmount() : 0d;
							}
						}						
						draftInvestigation.getClaimDto().setPreauthClaimedAmountAsPerBill(claimedAmt);
						
						draftInvestigation.setTriggerPointsList(dto.getTriggerPointsList());
						
						if(draftInvestigation.getClaimDto().getNewIntimationDto().getOrginalSI() != null ){
							String amtWords = SHAUtils.getParsedAmount(draftInvestigation.getClaimDto().getNewIntimationDto().getOrginalSI());
							draftInvestigation.getClaimDto().getNewIntimationDto().setComments(amtWords);
						}
						else{
							draftInvestigation.getClaimDto().getNewIntimationDto().setComments(null);
						}
						
						dtoList.add(draftInvestigation);
						reportDto.setBeanList(dtoList);
						
						String templateName = "InvestigationLetter(R)";
						
						final String filePath = docGen.generatePdfDocument(templateName, reportDto);
						
						draftInvestigation.setDocFilePath(filePath);
						draftInvestigation.setDocType(SHAConstants.DOC_TYPE_DRAFT_INVESTIGATION_LETTER);	
						
						if(bean.getStageKey() != null && ReferenceTable.ZONAL_REVIEW_STAGE.equals(bean.getStageKey())){
							draftInvestigation.setDocSource(SHAConstants.DOC_SOURCE_ZMR_DIRECT_TO_ASSIGN_INV_LETTER);
						}
						else if(bean.getStageKey() != null && ReferenceTable.CLAIM_REQUEST_STAGE.equals(bean.getStageKey())){
							draftInvestigation.setDocSource(SHAConstants.DOC_SOURCE_MA_DIRECT_TO_ASSIGN_INV_LETTER);
						}
						
						investigationService.uploadInvLetterToDms(draftInvestigation);
					}
					
					
					Map<String, Object> workFlowPayload = new WeakHashMap<String, Object>();
							
							Map<String, Object> workFlowObj = (Map<String, Object>) bean.getDbOutArray();
							
							workFlowPayload.putAll(workFlowObj);
							
							Long wkKey = (Long)workFlowObj.get(SHAConstants.WK_KEY);	
							workFlowPayload.put(SHAConstants.RRC_REQUEST_KEY, wkKey);
						//	workFlowPayload.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER, SHAConstants.PARALLEL_WAITING_FOR_INPUT);
							workFlowPayload.put(SHAConstants.WK_KEY, 0);
							
							//CR2019058 Change in submit task 
							
							if(null != bean && null != bean.getStageKey() && (ReferenceTable.ZONAL_REVIEW_STAGE).equals(bean.getStageKey())){
								
								workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_ZMR_INITIATE_INVESTIGATION_STATUS);
					
								if(bean.isDirectToAssignInv()){
									workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DRAFT_INVESTIGATION);
								}
								
								workFlowPayload.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.ZMR_CURRENT_QUEUE);
							}
							else
							{
								workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
								
								if(bean.isDirectToAssignInv()){
									workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DRAFT_INVESTIGATION);
								}
								
								workFlowPayload.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, SHAConstants.MA_CURRENT_QUEUE);
							}
							
							workFlowPayload
									.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY,
											investigation.getKey() != null ? investigation
													.getKey() : 0);

							//CR2019058
							workFlowPayload.put(SHAConstants.INV_INITIATED_DATE, new Timestamp(System.currentTimeMillis()));
							workFlowPayload.put(SHAConstants.INV_APPROVED_DATE, null);
							workFlowPayload.put(SHAConstants.INV_REQ_DRAFTED_DATE, null);

							if (investigation != null) {
								DBCalculationService dbServcie = new DBCalculationService();
//								Object[] inputWorkFlowArray = SHAUtils
//										.getRevisedObjArrayForSubmit(workFlowPayload);
//								dbServcie
//										.revisedInitiateTaskProcedure(inputWorkFlowArray);
								Object[] inputWorkFlowArray = SHAUtils
										.getRevisedObjArrayForAssignInvestigationSubmit(workFlowPayload);
								dbServcie
										.revisedAssignInvestigationTaskProcedure(inputWorkFlowArray);

								//buildSuccessLayout();
							}

						showValues(dto.getClaimKey());
						InitiateInvestigationLayout.removeAllComponents();
						binder.discard();

						mainLayout.removeComponent(InitiateInvestigationLayout);
						intiateInvestigationOptionGroup.setValue("No");
						initBinder();
						bean.getPreauthDataExtractionDetails().setIsFvrOrInvsInitiated(Boolean.TRUE);
						if(null != dataExtractionViewImplObj && ReferenceTable.ZONAL_REVIEW_STAGE.equals(bean.getStageKey())){
							dataExtractionViewImplObj.setBillEntryDisable(bean.getPreauthDataExtractionDetails().getIsFvrOrInvsInitiated());
						}
						bean.setIsParallelInvFvrQuery(Boolean.TRUE);
						initateInvDto = dto;
						bean.setInitInvDto(dto);
						dummyField.setValue(investigation.getKey().toString());
						popup.close();
						buildInvsSuccessLayout();
						//parent.setInitiateInitInvDto(initateInvDto);
				}
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

				/*ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Confirmation", "Are you sure you want to cancel ?",
						"Cancel", "Ok", new ConfirmDialog.Listener() {

							*//**
							 * 
							 *//*
							private static final long serialVersionUID = 1L;

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									popup.close();

								} else {
									// User did not confirm
								}
							}
						});*/
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Cancel");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createConfirmationbox("Are you sure you want to cancel ?", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						popup.close();
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
		
		
		if(bean.isDirectToAssignInv()){

			HorizontalLayout hlayout = new HorizontalLayout(draftTriggerPointsTableInstanceObj);
			hlayout.setCaption("Trigger points to focus *");
			return new FormLayout(assigntoCmb, reasonforRefering, hlayout,
					buttonHLayout);
				
		}
		
		return new FormLayout(assigntoCmb, reasonforRefering,
				triggerPointstoFocus, buttonHLayout);
	}
	
	public void buildSuccessLayout() {
		/*Label successLabel = new Label(
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("<b style = 'color: green;'> Claim Record Saved Successfully!!!</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
							
				if(null != stageKey && stageKey.equals(ReferenceTable.ZONAL_REVIEW_STAGE))
				{	Collection<Window> windows =  (Collection<Window>)getUI().getCurrent().getWindows();
					Object[] winArray = windows.toArray();
					for(int i = 0; i < winArray.length;i++){
						((Window)winArray[i]).close();
					}
				}
				//dialog.close();
				
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
		
//		List<Investigation> investigationList = investigationService
//				.getByClaimKey(ClaimKey);
		
		List<Investigation> investigationList = investigationService
				.getLatestListByClaimKey(ClaimKey);
		
		Claim claimByClaimKey = claimService.getClaimByClaimKey(this.claimKey);
		
		this.claim = claimByClaimKey;
		
		

		if (investigationList.size() != 0 || investigationList == null) {

			List<InvestigationDetailsTableDTO> investigationDetailsTableDTOList = new ArrayList<InvestigationDetailsTableDTO>();
			Integer index = 1;

			for (Investigation investigation : investigationList) {
				InvestigationDetailsTableDTO investigationDetailsTableDTO = new InvestigationDetailsTableDTO();
				InvestigationMapper investigationMapper = new InvestigationMapper();
				investigationDetailsTableDTO = investigationMapper
						.getInvestigationDetailsTableDTO(investigation);
				investigationDetailsTableDTO.setInvestigationTriggerPoints(investigation.getTriggerPoints());
	
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
				
				if (investigationDetailsTableDTO.getInvestigationAssignedDate() != null
						&& !investigationDetailsTableDTO
								.getInvestigationAssignedDate().equals("")) {
					Date tempDate = SHAUtils
							.formatTimestamp(investigationDetailsTableDTO
									.getInvestigationAssignedDate());
					investigationDetailsTableDTO
							.setInvestigationAssignedDate(SHAUtils
									.formatDate(tempDate));
					if(investigationDetailsTableDTO
						.getInvestigationCompletedDate() != null){
						Long noOfDays = SHAUtils.getDaysBetweenDate(tempDate, investigationDetailsTableDTO
						.getInvestigationCompletedDate());
						investigationDetailsTableDTO.setTat(noOfDays.toString());
					}					
				}
				

				if (investigationDetailsTableDTO
						.getInvestigationCompletedDate() != null) {
					investigationDetailsTableDTO
							.setInvestigationCompletedDateStr(SHAUtils
									.formatDate(investigationDetailsTableDTO
											.getInvestigationCompletedDate()));
				}			
				
				if(investigation.getReportReceivedDate() != null){
					investigationDetailsTableDTO.setInvestigationReportReceivedDate(SHAUtils.formatDate(investigation.getReportReceivedDate()));
				}

				investigationDetailsTableDTO.setsNo(index);
				index++;
				investigationDetailsTableDTOList
						.add(investigationDetailsTableDTO);
			}
			if (investigationDetailsTableDTOList.size() > 0) {
				investigationDetailsTable
						.setTableList(investigationDetailsTableDTOList);
				this.detailsTableDTO = investigationDetailsTableDTOList.get(0);
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

		if(bean.isDirectToAssignInv()){
			List<DraftTriggerPointsToFocusDetailsTableDto> list = draftTriggerPointsTableInstanceObj.getValues();
			if(null == list || list.isEmpty()){
				hasError = true;
				eMsg.append("Please Enter Trigger Points");
			}
		}
		
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			/*Window window = new Window();
			window.setCaption("ERRORS");
			window.setModal(true);
			window.setWidth("-1px");
			window.setHeight("-1px");
			window.setContent(layout);
			window.setWindowMode(WindowMode.NORMAL);
			UI.getCurrent().addWindow(window);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createCustomBox("ERRORS", layout, buttonsNamewithType, GalaxyTypeofMessage.ERROR.toString());
			
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
	
	public void buildInvsSuccessLayout() {
		/*Label successLabel = new Label(
				"<b style = 'color: green;'> Investigation has been initiated successfully!!!</b>",
				ContentMode.HTML);

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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("<b style = 'color: green;'> Investigation has been initiated successfully!!!</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
							
				if(null != stageKey && stageKey.equals(ReferenceTable.ZONAL_REVIEW_STAGE))
				{	Collection<Window> windows =  (Collection<Window>)getUI().getCurrent().getWindows();
					Object[] winArray = windows.toArray();
					for(int i = 0; i < winArray.length;i++){
						((Window)winArray[i]).close();
					}
				}

				//dialog.close();

//				toolBar.countTool(); - As Per BA Revised Req sub flow process won't considered as count

			}
		});
		
	}
	
	/*
	 * public boolean isDisabled() { return isDisabled; }
	 * 
	 * public void setDisabled(boolean isDisabled) { this.isDisabled =
	 * isDisabled; }
	 */

}
