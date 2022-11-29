package com.shaic.claim.process64VB.wizard.pages;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.preauth.wizard.pages.PreauthMedicalProcessingPage;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class Process64VBWizardViewImpl extends AbstractMVPView implements Process64VBview{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6013903728510416209L;

	@Inject
	private Instance<Process64VBDataExtractionPage> processVBDataExtractionPageInstance;


	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;

	@Inject
	private ViewDetails viewDetails;
	

	private Process64VBDataExtractionPage preauthDataExtractionPage;


	private VerticalSplitPanel mainPanel;
	
	private FieldGroup binder;

	private SearchPreauthTableDTO bean;

	private PreauthMedicalProcessingPage preauthMedicalProcessingPage;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	////private static Window popup;

	private void initBinder() {/*

		//wizard.getFinishButton().setCaption("Submit");
		//wizard.getCancelButton().setEnabled(true);
		this.binder = new FieldGroup();
		BeanItem<PreauthDTO> item = new BeanItem<PreauthDTO>(bean);
		item.addNestedProperty("preauthDataExtractionDetails");
		item.addNestedProperty("coordinatorDetails");
		item.addNestedProperty("preauthPreviousClaimsDetails");
		item.addNestedProperty("preauthMedicalDecisionDetails");

		item.addNestedProperty("preauthDataExtractionDetails.referenceNo");
		item.addNestedProperty("preauthDataExtractionDetails.reasonForAdmission");
		item.addNestedProperty("preauthDataExtractionDetails.admissionDate");
		item.addNestedProperty("preauthDataExtractionDetails.noOfDays");
		item.addNestedProperty("preauthDataExtractionDetails.natureOfTreatment");
		item.addNestedProperty("preauthDataExtractionDetails.diagnosis");
		item.addNestedProperty("preauthDataExtractionDetails.firstConsultantDate");
		item.addNestedProperty("preauthDataExtractionDetails.enhancementType");
		item.addNestedProperty("preauthDataExtractionDetails.subLimit");
		item.addNestedProperty("preauthDataExtractionDetails.corpBuffer");
		item.addNestedProperty("preauthDataExtractionDetails.criticalIllness");
		item.addNestedProperty("preauthDataExtractionDetails.specifyIllness");
		item.addNestedProperty("preauthDataExtractionDetails.roomCategory");
		item.addNestedProperty("preauthDataExtractionDetails.treatmentType");
		item.addNestedProperty("preauthDataExtractionDetails.preauthReqAmt");
		item.addNestedProperty("preauthDataExtractionDetails.treatmentRemarks");
		item.addNestedProperty("preauthDataExtractionDetails.sublimitSpecify");
		item.addNestedProperty("preauthDataExtractionDetails.sumInsured");
		item.addNestedProperty("preauthDataExtractionDetails.sublimitSumInsured");
		item.addNestedProperty("preauthDataExtractionDetails.enhancementReqAmt");
		item.addNestedProperty("preauthDataExtractionDetails.preauthApprAmt");

		item.addNestedProperty("preauthPreviousClaimsDetails.relapseOfIllness");
		item.addNestedProperty("preauthPreviousClaimsDetails.relapseRemarks");
		item.addNestedProperty("preauthPreviousClaimsDetails.attachToPreviousClaim");

		item.addNestedProperty("preauthMedicalDecisionDetails.investigationReportReviewed");
		item.addNestedProperty("preauthMedicalDecisionDetails.investigationReviewRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.investigatorName");
		item.addNestedProperty("preauthMedicalDecisionDetails.initiateFieldVisitRequest");
		item.addNestedProperty("preauthMedicalDecisionDetails.fvrNotRequiredRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.specialistOpinionTaken");
		item.addNestedProperty("preauthMedicalDecisionDetails.specialistType");
		item.addNestedProperty("preauthMedicalDecisionDetails.specialistConsulted");
		item.addNestedProperty("preauthMedicalDecisionDetails.remarksBySpecialist");
		item.addNestedProperty("preauthMedicalDecisionDetails.allocationTo");
		item.addNestedProperty("preauthMedicalDecisionDetails.fvrTriggerPoints");
		item.addNestedProperty("preauthMedicalDecisionDetails.specialistOpinionTakenFlag");
		item.addNestedProperty("preauthMedicalDecisionDetails.initiateFieldVisitRequestFlag");

		item.addNestedProperty("preauthMedicalDecisionDetails.approvedAmount");
		item.addNestedProperty("preauthMedicalDecisionDetails.selectedCopay");
		item.addNestedProperty("preauthMedicalDecisionDetails.initialTotalApprovedAmt");
		item.addNestedProperty("preauthMedicalDecisionDetails.approvalRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.queryRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.rejectionCategory");
		item.addNestedProperty("preauthMedicalDecisionDetails.rejectionRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.reasonForDenial");
		item.addNestedProperty("preauthMedicalDecisionDetails.denialRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.escalateTo");
		// item.addNestedProperty("preauthMedicalDecisionDetails.upLoadFile");
		item.addNestedProperty("preauthMedicalDecisionDetails.escalationRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.typeOfCoordinatorRequest");
		item.addNestedProperty("preauthMedicalDecisionDetails.reasonForRefering");

		item.addNestedProperty("preauthMedicalDecisionDetails.sentToCPU");
		item.addNestedProperty("preauthMedicalDecisionDetails.remarksForCPU");

		item.addNestedProperty("coordinatorDetails.refertoCoordinator");
		item.addNestedProperty("coordinatorDetails.typeofCoordinatorRequest");
		item.addNestedProperty("coordinatorDetails.reasonForRefering");
		
		item.addNestedProperty("preauthDataExtractionDetails.hospitalisationDueTo.id");
		item.addNestedProperty("preauthDataExtractionDetails.hospitalisationDueTo.value");
		item.addNestedProperty("preauthDataExtractionDetails.causeOfInjury.id");
		item.addNestedProperty("preauthDataExtractionDetails.injuryDate");
		item.addNestedProperty("preauthDataExtractionDetails.medicalLegalCaseFlag");
		item.addNestedProperty("preauthDataExtractionDetails.diseaseFirstDetectedDate");
		item.addNestedProperty("preauthDataExtractionDetails.deliveryDate");
		item.addNestedProperty("preauthDataExtractionDetails.section.id");
		item.addNestedProperty("preauthDataExtractionDetails.typeOfDelivery.id");
		item.addNestedProperty("preauthDataExtractionDetails.firNumber");
		item.addNestedProperty("preauthDataExtractionDetails.policeReportAttachedFlag");

		this.binder.setItemDataSource(item);
	*/}

	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();
	}

	public void initView(SearchPreauthTableDTO preauthDTO) {
		this.bean = preauthDTO;
		mainPanel = new VerticalSplitPanel();
		setHeight("100%");
		Process64VBDataExtractionPage preauthDataExtractionPage = processVBDataExtractionPageInstance
				.get();
		preauthDataExtractionPage.init(this.bean);
		this.preauthDataExtractionPage = preauthDataExtractionPage;

		// Get Button Instace
		/*ProcessPreAuthButtonLayout processPreAuthButtonLayout2 = processPreauthButtonLayoutInstance
				.get();
		this.processPreAuthButtonLayout = processPreAuthButtonLayout2;*/


		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
//		intimationDetailsCarousel.init(preauthDTO.getNewIntimationDTO(),
//				preauthDTO.getClaimDTO(), "Process Pre-authorization");
		NewIntimationDto newIntimationDTO = preauthDTO.getNewIntimationDTO();
		ClaimDto claimDto = preauthDTO.getClaimDto();
		intimationDetailsCarousel.init(newIntimationDTO,claimDto, "Process 64VB Complaince");
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		// ComboBox viewDetailsSelect = new ComboBox();
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),
				ViewLevels.PREAUTH_MEDICAL, true,"Process Pre-authorization");
		viewDetails.setPreAuthKey(bean.getKey());
		viewDetails.setStageKey(ReferenceTable.PREAUTH_STAGE);
		// viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),
		// ViewLevels.INTIMATION);
		viewDetailsForm.addComponent(viewDetails);
		HorizontalLayout commonButtons2 = commonButtons(preauthDTO);
		
		HorizontalLayout commonButtons = new HorizontalLayout(viewDetails);
		commonButtons.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		commonButtons.setWidth("100%");
//		commonButtons.setHeight("70px");
		
		VerticalLayout mainHor = new VerticalLayout(commonButtons);
		mainHor.addComponent(commonButtons2);
		mainHor.setComponentAlignment(commonButtons2, Alignment.TOP_RIGHT);
		mainHor.setSpacing(true);
	

		
		VerticalLayout wizardLayout1 = new VerticalLayout(mainHor);
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1,preauthDataExtractionPage );
		// mainPanel.addComponent(wizardLayout1);
		mainPanel.setSecondComponent(wizardLayout2);
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
		
		/*if(bean.getIsPedPending()){
			getErrorMessage("Pending PED in Action");
		}*/

		//fireViewEvent(PreauthWizardPresenter.SETUP_REFERENCE_DATA, bean);
		
		/*Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		if (wrkFlowMap != null){
			DBCalculationService db = new DBCalculationService();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String aciquireByUserId = db.getLockUser(wrkFlowKey);
			if((bean.getStrUserName() != null && aciquireByUserId != null && ! bean.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
				//compareWithUserId(aciquireByUserId);
			}
		
		}*/
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		fireViewEvent(PreauthWizardPresenter.PREAUTH_STEP_CHANGE_EVENT, event);

	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {

	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
		fireViewEvent(PreauthWizardPresenter.PREAUTH_SUBMITTED_EVENT, this.bean);

	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Confirmation",
						"Are you sure you want to cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									// Confirmed to continue
									releaseHumanTask();
									fireViewEvent(MenuItemBean.PROCESS_64_VB_COMPLIANCE,
											null);
								} else {
									// User did not confirm
								}
							}
						});

		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);

	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub

	}


	@Override
	public void wizardSave(GWizardEvent event) {
		// TODO Auto-generated method stub

	}


	private HorizontalLayout commonButtons(final SearchPreauthTableDTO preauthDTO) {
		
		Button btnViewPolicyDoc = new Button("Policy Document");
		btnViewPolicyDoc.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String intimationKey = preauthDTO.getNewIntimationDTO().getIntimationId();
				viewDetails.getViewDocument(intimationKey);
			}
			
		});
		

		Button btnViewClaimDoc = new Button("Claim Document");
		btnViewClaimDoc.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String intimationKey = preauthDTO.getNewIntimationDTO().getIntimationId();
				viewDetails.viewUploadedDocumentDetails(intimationKey);
			}
			
		});
		
		
		Button btnViewPreauth = new Button("Pre auth (Detailed)");
		btnViewPreauth.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String intimationKey = preauthDTO.getNewIntimationDTO().getIntimationId();
				viewDetails.getPreAuthUpdateDetail(intimationKey);
			}
			
		});
		
		Button btnView64Comp = new Button("64 VB Complaince");
		btnView64Comp.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String intimationKey = preauthDTO.getNewIntimationDTO().getIntimationId();
				viewDetails.getIrda64VbDetails(intimationKey);
			}
			
		});
		
		Button btnViewpolicyDetail = new Button("Current Policy Details");
		btnViewpolicyDetail.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String intimationKey = preauthDTO.getNewIntimationDTO().getIntimationId();
				viewDetails.getViewPolicy(intimationKey);
			}
			
		});
		
		
		Button btnViewBalanceInsured = new Button("Balance Sum Insured");
		btnViewBalanceInsured.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String intimationKey = preauthDTO.getNewIntimationDTO().getIntimationId();
				viewDetails.getViewBalanceSumInsured(intimationKey);
			}
			
		});
		
		FormLayout formleft = new FormLayout(btnViewPolicyDoc, btnViewClaimDoc, btnViewPreauth);
		formleft.setSpacing(true);
		
		FormLayout formRight = new FormLayout(btnView64Comp, btnViewpolicyDetail, btnViewBalanceInsured);
		formRight.setSpacing(true);
		HorizontalLayout mainLayout = new HorizontalLayout(formleft,formRight);
		mainLayout.setComponentAlignment(formleft, Alignment.TOP_RIGHT);
		return mainLayout;

	   }
	
	
	private void showErrorPopup(String eMsg) {
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
	
	  
	     public void getErrorMessage(String eMsg){

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
       }
	
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(PreauthWizardPresenter.VALIDATE_PREAUTH_USER_RRC_REQUEST, bean);//, secondaryParameters);
	}

	
	

	@SuppressWarnings("static-access")
	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Cashless Claim Record Saved Successfully.</b>",
				ContentMode.HTML);
		/*if(bean.getStatusKey() != null && ReferenceTable.REFER_TO_FLP_KEYS.containsKey(bean.getStatusKey())) {
			successLabel = new Label(
					"<b style = 'color: green;'> Intimation No:" + bean.getNewIntimationDTO().getIntimationId()+ "has referred to First level processor successfully</b>",
					ContentMode.HTML);
		}*/
		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Process 64VB Compliance");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

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
				fireViewEvent(MenuItemBean.PROCESS_64_VB_COMPLIANCE, null);

			}
		});
	}

	@SuppressWarnings("unused")
	private HorizontalLayout showPremedicalStatus() {
		return null;/*
		TextField premedicalSuggestion = new TextField("First Level Suggestion");
		TextArea premedicalRemarks = new TextArea("First Level Remarks");
		premedicalRemarks.setEnabled(false);
		if (this.bean.getStatusKey().equals(
				ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)
				|| this.bean.getStatusKey().equals(
						ReferenceTable.PREAUTH_QUERY_STATUS)
						|| this.bean.getStatusKey().equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS)) {
			premedicalRemarks = new TextArea("Remarks (First Level)");
			premedicalSuggestion.setValue("Preauth Query");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);

		} else if (this.bean.getStatusKey().equals(
				ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)) {
			premedicalSuggestion.setValue("Suggestion Rejection");
			premedicalRemarks = new TextArea("First Level Remarks ");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			
		} else if (this.bean.getStatusKey().equals(
				ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS)) {
			premedicalSuggestion.setValue("Send For Processing");
			premedicalRemarks = new TextArea("First Level Remarks");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);
		} else if (this.bean.getStatusKey().equals(
				ReferenceTable.PREAUTH_ESCALATION_STATUS)) {
			premedicalSuggestion.setValue("Escalation");
			premedicalRemarks = new TextArea("First Level Remarks");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getEscalationRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);
		}  else if (this.bean.getStatusKey().equals(
				ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS)|| this.bean.getStatusKey().equals(ReferenceTable.PREAUTH_REFER_TO_COORDINATOR_STATUS)) {
			premedicalSuggestion.setValue("Coordinator");
			premedicalRemarks = new TextArea("First Level Remarks");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);
		} 

		premedicalSuggestion.setEnabled(false);
		TextField premedicalCategory = new TextField("Category");

		String category = "";
		if (this.bean.getPreauthMedicalProcessingDetails().getCategory() != null) {
			category = this.bean
					.getPreauthMedicalProcessingDetails()
					.getCategory()
					.getId()
					.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY) ? "Non - Medical"
					: "Medical";
		}
		premedicalCategory.setValue(category);
		premedicalCategory.setEnabled(false);
		// TextArea premedicalRemarks = new
		// TextArea("Rejection Remarks (Pre-medical)");
		*//**
		 * In pre auth screen , instead of Rejection Remarks, it should be
		 * Remarks as per sathish sir. Hence commenting the above and adding the
		 * below line code.
		 * *//*
		FormLayout sugessionform =new FormLayout(premedicalSuggestion, premedicalRemarks);
//		sugessionform.setMargin(false);
		FormLayout premedicalCategoryform =new FormLayout(premedicalCategory);
//		premedicalCategoryform.setMargin(false);
		HorizontalLayout premedicalHLayout = new HorizontalLayout(
				sugessionform,premedicalCategoryform);
		premedicalHLayout.setSpacing(true);
		premedicalHLayout.setHeight("40%");

		return premedicalHLayout;
	*/}


	
  private void releaseHumanTask(){
		
		Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

// 		if(existingTaskNumber != null){
// 			BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
// 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
// 		}
 		
 		if(wrkFlowKey != null){
 			DBCalculationService dbService = new DBCalculationService();
 			dbService.callUnlockProcedure(wrkFlowKey);
 			getSession().setAttribute(SHAConstants.WK_KEY, null);
 		}
	}
  
	 public void compareWithUserId(String userId) {
		 
		 if(userId == null){
			 userId = "";
		 }

		 final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH,null);
					dialog.close();
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red'>Intimation is already opened by another user</b>"+userId, ContentMode.HTML), hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);
	
 }

}
