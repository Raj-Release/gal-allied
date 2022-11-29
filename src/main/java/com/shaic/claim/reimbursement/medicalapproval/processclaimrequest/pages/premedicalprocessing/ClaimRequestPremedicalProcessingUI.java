package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.premedicalprocessing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.ProcedureExclusionCheckTable;
import com.shaic.claim.premedical.listenerTables.PEDValidationListenerTableForPremedical;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestButtonsForMedicalProcessingPage;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestFileUploadUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimsSubmitHandler;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;
@Alternative
public class ClaimRequestPremedicalProcessingUI extends ViewComponent implements ClaimsSubmitHandler{

	private static final long serialVersionUID = -5139520050760412223L;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Instance<PEDValidationListenerTableForPremedical> pedValidationTable;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	@Inject
	private Instance<ProcedureExclusionCheckTable> procedureExclusionCheckTable;
	
	
	private PEDValidationListenerTableForPremedical pedValidationTableObj;

	private ProcedureExclusionCheckTable procedureExclusionCheckTableObj;
	
	private Map<String, Object> referenceData;
	
	//private Button submitButton;
	
	//private Boolean isPEDAvailable = false;

	private Button initiatePEDButton;

	private OptionGroup verifiedPolicySchedule;
	
	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;
	
    @EJB
    private PEDQueryService pedQueryService;
    
    private GWizard wizard;
    
    @Inject
	private Instance<ClaimRequestButtonsForMedicalProcessingPage> claimRequestMedicalProcessingButtonInstance;
	
	private ClaimRequestButtonsForMedicalProcessingPage claimRequestMedicalProcessingButtonObj;
	
	private ClaimRequestFileUploadUI specialistWindow = new ClaimRequestFileUploadUI();
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private ClaimsSubmitHandler submitHandler; 
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	@EJB
	private MasterService masterService;
	
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
	}
	
	public void init(PreauthDTO bean,GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
	}
	
	public Component getContent() {
		initBinder();
		
		if(ReferenceTable.getHealthGainProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(bean.getIsChangeInsumInsuredAlert()){
				alertForHealthGainProduct();
			}
		}
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		verifiedPolicySchedule = (OptionGroup) binder.buildAndBind(
				"Verified Policy Schedule", "verifiedPolicySchedule", OptionGroup.class);
		
//		verifiedPolicySchedule = new OptionGroup("Verified Policy Schedule");
		verifiedPolicySchedule.addItems(getReadioButtonOptions());
		verifiedPolicySchedule.setItemCaption(true, "Yes");
		verifiedPolicySchedule.setItemCaption(false, "No");
		verifiedPolicySchedule.setStyleName("horizontal");
		
		initiatePEDButton = new Button("Initiate PED Endorsement");
		
		if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() 
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType()
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType().getKey()
				&& 2904 == bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue()
				&& null != bean.getNewIntimationDTO().getPolicy().getProduct().getCode()
				&& !bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)
				&& !bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS)){
			initiatePEDButton.setEnabled(false);
		}
		else{
			initiatePEDButton.setEnabled(true);
		}
		
		VerticalLayout buttonHLayout = new VerticalLayout(initiatePEDButton);
		//buttonHLayout.setMargin(true);
		buttonHLayout.setHeight("1px");
		buttonHLayout.setComponentAlignment(initiatePEDButton, Alignment.MIDDLE_RIGHT);
		
		PEDValidationListenerTableForPremedical pedValidationTableInstance = pedValidationTable.get();
		pedValidationTableInstance.init(SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_MEDICAL_PROCESSING, bean);
		this.pedValidationTableObj = pedValidationTableInstance;
		
		ProcedureExclusionCheckTable procedureExclusionCheckTableInstance = procedureExclusionCheckTable.get();
		procedureExclusionCheckTableInstance.init("Procedure Exclusion Check", false);
		this.procedureExclusionCheckTableObj = procedureExclusionCheckTableInstance;
		
//		PreMedicalPreauthButtons preMedicalPreauthButtonsInstance = preMedicalPreauthButtons.get();
//		preMedicalPreauthButtonsInstance.initView(bean);
//		this.preMedicalPreauthButtonsObj = preMedicalPreauthButtonsInstance;
		
		FormLayout formLayout = new FormLayout(verifiedPolicySchedule);
		formLayout.setMargin(true);
		
		VerticalLayout wholeVLayout = new VerticalLayout(buttonHLayout, formLayout, pedValidationTableInstance, procedureExclusionCheckTableInstance);
		wholeVLayout.setSpacing(true);
		
		claimRequestMedicalProcessingButtonObj =  claimRequestMedicalProcessingButtonInstance.get();
		claimRequestMedicalProcessingButtonObj.initView(this.bean, this.wizard);
		wholeVLayout.addComponent(claimRequestMedicalProcessingButtonObj);
		 
		addListener();
		return wholeVLayout;
	}
	
	public void addListener() {
		initiatePEDButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8159939563947706329L;
			//private Window popup;

			@Override
			public void buttonClick(ClickEvent event) {
				Long preauthKey=bean.getKey();
				Long intimationKey=bean.getIntimationKey();
				Long policyKey=bean.getPolicyKey();
				Long claimKey=bean.getClaimKey();
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
	/*@SuppressWarnings("unused")
	private void addProcedureBeantoList(
			ProcedureExclusionCheckTable procedureExclusionCheck,
			ProcedureTableDTO procedureDto) {
		ProcedureDTO dto = new ProcedureDTO();
		dto.setProcedureNameValue(procedureDto.getProcedureName().getValue());
		dto.setProcedureCodeValue(procedureDto.getProcedureCode().getValue());
		dto.setPackageAmount("2000");
//			dto.setPackageAmount(procedureDto.get);
//			dto.setPolicyAging(procedureDto.getP);
		procedureExclusionCheck.addBeanToList(dto);
	}*/
	
	public void afterConfirmOk(){
		//fireViewEvent(MenuPresenter.showSearchPreAuthView(), null);
	}
	
	
	public boolean validatePage() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		
		/*if(this.pedValidationTableObj != null) {
			boolean isValid = this.pedValidationTableObj.isValid();
			if(!isValid) {
				hasError = true;
				List<String> errors = this.pedValidationTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}*/
		
		if(this.pedValidationTableObj != null) {
			boolean isValid = this.pedValidationTableObj.validateCopay();
			if(!isValid) {
				hasError = true;
				List<String> errors = this.pedValidationTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if(this.procedureExclusionCheckTableObj != null) {
			boolean isValid = this.procedureExclusionCheckTableObj.isValid();
			if(!isValid) {
				hasError = true;
				List<String> errors = this.procedureExclusionCheckTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if(verifiedPolicySchedule != null && (verifiedPolicySchedule.getValue() == null || !(verifiedPolicySchedule.getValue().toString() == "true"))) {
			hasError = true;
			eMsg.append("Please Select Verified Policy Schedule as YES to proceed further. </br>");
		}
		
//		if(this.preMedicalPreauthButtonsObj != null) {
//			boolean isValid = this.preMedicalPreauthButtonsObj.isValid();
//			if(!isValid) {
//				hasError = true;
//				List<String> errors = this.preMedicalPreauthButtonsObj.getErrors();
//				for (String error : errors) {
//					eMsg += error + "</br>";
//				}
//			}
//		}
		
	   if(hasError) {
		   /* Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
		    
		    hasError = true;
		    return !hasError;
	   } else {
			try {
				this.binder.commit();
				this.bean.getPreMedicalPreauthMedicalDecisionDetails().setProcedureExclusionCheckTableList(this.procedureExclusionCheckTableObj.getValues());
				
				//R1152
				if(!this.bean.getIsGeoSame()){
					getGeoBasedOnCPU();
				}
				
//				Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
//				this.bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
			} catch (Exception e) {
				e.printStackTrace();
			}
		   return true;
	   }
	}
	

	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		referenceData.put(SHAConstants.IS_DEFAULT_COPAY, this.bean.getIsDefaultCopay() != null ? this.bean.getIsDefaultCopay() : false);
		referenceData.put(SHAConstants.DEFAULT_COPAY_VALUE, this.bean.getDefaultCopayStr());
		pedValidationTableObj.setReferenceData(this.referenceData);
		this.procedureExclusionCheckTableObj.setReference(referenceData);
		List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		
		SelectValue selectValue = null;
		for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : pedValidationTableList) {
			List<PedDetailsTableDTO> pedList = diagnosisDetailsTableDTO.getPedList();
			
			int size = pedList.size();
			if(size != 0) {
				Float value = (float) (size/2);
				int round = Math.round(value);
				try {
					PedDetailsTableDTO pedDetailsTableDTO = pedList.get(round);
					pedDetailsTableDTO.setIsShowingCopay(true);
					if(diagnosisDetailsTableDTO.getCopayPercentage() != null) {
						selectValue = new SelectValue();
						selectValue.setId(diagnosisDetailsTableDTO.getCopayPercentage().longValue());
						selectValue.setValue(String.valueOf(diagnosisDetailsTableDTO.getCopayPercentage().intValue()));
						pedDetailsTableDTO.setCopay(selectValue);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
				this.pedValidationTableObj.addBeanToList(pedDetailsTableDTO);

			}
		}
		
		List<ProcedureDTO> procedureExclusionCheckTableList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		if(!procedureExclusionCheckTableList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				if(procedureDTO.getCopayPercentage() != null) {
					//SelectValue selectValue = new SelectValue();
					selectValue = new SelectValue();
					selectValue.setId(procedureDTO.getCopayPercentage().longValue());
					selectValue.setValue(String.valueOf(procedureDTO.getCopayPercentage().doubleValue()));
					procedureDTO.setCopay(selectValue);
				}
				if(procedureDTO.getEnableOrDisable())
				{
					procedureDTO.setStatusFlag(true);
				}
				else
				{
					procedureDTO.setStatusFlag(false);
				}
				this.procedureExclusionCheckTableObj.addBeanToList(procedureDTO);
			}
		}
		
		if(this.bean.getStatusKey() != null) {
			if(this.bean.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)) {
//				fireViewEvent(PreauthWizardPresenter.PREAUTH_QUERY_EVENT, null);
			} else if(this.bean.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)) {
//				fireViewEvent(PreauthWizardPresenter.PREAUTH_SUGGEST_REJECTION_EVENT, null);
			} else if(this.bean.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)){
//				fireViewEvent(PreauthWizardPresenter.PREAUTH_SEND_FOR_PROCESSING_EVENT, null);
			}
		}
		
		referenceData.put("cancellationReason", masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		claimRequestMedicalProcessingButtonObj.setReferenceData(referenceData);
	}

	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionContainer) {
		this.pedValidationTableObj.setExclusionDetailsValue(exclusionContainer);
	}
	
	public void setClearReferenceData(){
    	SHAUtils.setClearReferenceData(referenceData);
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
				.createAlertBox(SHAConstants.SUM_INSURED_CHANGE_MESSAGE_HEALTH_GAIN , buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
				}
			});
		}

	public void generateButton(Integer clickedButton, Object dropDownValues) {
		this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		switch (clickedButton) {
		case 1: 
			
		 this.claimRequestMedicalProcessingButtonObj.buildSendReplyLayout();
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
		 
		 break;
		 
		case 2: 
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
			 this.claimRequestMedicalProcessingButtonObj.buildInitiateFieldVisit(dropDownValues);
			 
			 
			 break;
			 
		case 3: 
			
			 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 this.bean.getPreauthMedicalDecisionDetails().setAllocationTo(null);
				 this.bean.getPreauthMedicalDecisionDetails().setTriggerPointsToFocus("");
				
			 }
			 this.claimRequestMedicalProcessingButtonObj.buildInitiateInvestigation(dropDownValues);
			
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
			 break;
		case 4:
			
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 
			 }
			this.claimRequestMedicalProcessingButtonObj
					.buildReferCoordinatorLayout(dropDownValues);
			 
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
			break;
		case 5:
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
				break;
			
		case 6:
			
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
			 
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS);
			}
			break;
			
		case 7:
			 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			 }
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
			 
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS);
			}
			break;
		case 8:
			
			this.claimRequestMedicalProcessingButtonObj.buildQueryLayout();
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
			break;
		case 9:
			
			this.claimRequestMedicalProcessingButtonObj.buildRejectLayout(dropDownValues);
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
			break;
		case 10:

			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
			this.claimRequestMedicalProcessingButtonObj.generateFieldsForSuggestApproval();
			
			break;
		case 11:
			this.claimRequestMedicalProcessingButtonObj.builtCancelRODLayout();
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
			break;
		default:
			break;
		}
	}
	
	public boolean fileUploadValidatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		/*fireViewEvent(
				ClaimRequestMedicalDecisionPagePresenter.CHECK_INVESTIGATION_INITIATED,
				this.bean.getClaimKey());
*/
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

				return true;
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return false;
		}
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
	
	public void addSubmitHandler(ClaimsSubmitHandler handler)
	{
		this.submitHandler = handler;
	}

	@Override
	public void submit(PreauthDTO preauthDTO) {
		specialistWindow.close();
		wizard.finish();
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	public void getGeoBasedOnCPU() {	 
		 
		 /*Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.GEO_PANT_HOSP_ALERT_MESSAGE + "</b>",
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
			dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(SHAConstants.GEO_PANT_HOSP_ALERT_MESSAGE, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					 
				}
			});
		}
}
