package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;




import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.ProcedureExclusionCheckTable;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.premedical.listenerTables.PEDValidationListenerTableForPremedical;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.AddAdditionalFVRPointsPageUI;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class MedicalApprovalPremedicalProcessingUI extends ViewComponent {

	private static final long serialVersionUID = -5139520050760412223L;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Instance<PEDValidationListenerTableForPremedical> pedValidationTable;
	
	@Inject
	private Instance<ProcedureExclusionCheckTable> procedureExclusionCheckTable;
	
	@Inject
	private Instance<ZonalReviewPreMedicalProcessingButtonsUI> zonalReviewButtons;
	
	private PEDValidationListenerTableForPremedical pedValidationTableObj;

	private ProcedureExclusionCheckTable procedureExclusionCheckTableObj;
	
	private ZonalReviewPreMedicalProcessingButtonsUI zonalReviewButtonsObj;
	
	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;
	
	private Map<String, Object> referenceData = new HashMap<String, Object>();
	
	
	//private Button submitButton;
	
	//private Boolean isPEDAvailable = false;

	private OptionGroup verifiedPolicySchedule;
	
	private Button addAdditionalFvrPointsBtn;
	@Inject
	private AddAdditionalFVRPointsPageUI addAdditionalFvrPointsPageUI;
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
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
		
		PEDValidationListenerTableForPremedical pedValidationTableInstance = pedValidationTable.get();
		pedValidationTableInstance.init(SHAConstants.MEDICAL_APPROVAL_MEDICAL_PROCESSING, bean);
		this.pedValidationTableObj = pedValidationTableInstance;
		
		ProcedureExclusionCheckTable procedureExclusionCheckTableInstance = procedureExclusionCheckTable.get();
		procedureExclusionCheckTableInstance.init("Procedure Exclusion Check", false);
		this.procedureExclusionCheckTableObj = procedureExclusionCheckTableInstance;
		this.procedureExclusionCheckTableObj.setReference(referenceData);
		
		zonalReviewButtonsObj = zonalReviewButtons.get();
		zonalReviewButtonsObj.initView(bean);
		
//		PreMedicalPreauthButtons preMedicalPreauthButtonsInstance = preMedicalPreauthButtons.get();
//		preMedicalPreauthButtonsInstance.initView(bean);
//		this.preMedicalPreauthButtonsObj = preMedicalPreauthButtonsInstance;
		addAdditionalFvrPointsBtn = new Button("Add Additional FVR Points");
		addAdditionalFvrPointsBtn.setEnabled(false);
		if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrReplyReceived() && !bean.getPreauthMedicalDecisionDetails().getIsFvrReplyReceived()){
			addAdditionalFvrPointsBtn.setEnabled(true);
		}
		
		FormLayout formLayout = new FormLayout(verifiedPolicySchedule);
		formLayout.setComponentAlignment(verifiedPolicySchedule,Alignment.TOP_RIGHT);
		formLayout.setMargin(true);
		FormLayout formLayout1 = new FormLayout(addAdditionalFvrPointsBtn);
		formLayout1.setComponentAlignment(addAdditionalFvrPointsBtn,Alignment.TOP_RIGHT);
		HorizontalLayout hLayout = new HorizontalLayout(formLayout,formLayout1);
		VerticalLayout wholeVLayout = new VerticalLayout(hLayout, pedValidationTableInstance, procedureExclusionCheckTableInstance, zonalReviewButtonsObj);
		wholeVLayout.setSpacing(true);
		addAdditionalFvrPointsListener();
		return wholeVLayout;
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
		
		if(this.pedValidationTableObj != null) {
			boolean isValid = this.pedValidationTableObj.isValid();
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
		
		if(this.zonalReviewButtonsObj != null) {
			boolean isValid = this.zonalReviewButtonsObj.isValidForReimbursement();
			if(!isValid) {
				hasError = true;
				List<String> errors = this.zonalReviewButtonsObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if(verifiedPolicySchedule != null && (verifiedPolicySchedule.getValue() == null || !(verifiedPolicySchedule.getValue().toString() == "true"))) {
			hasError = true;
			eMsg.append("Please Select Verified Policy Schedule as YES to proceed further. ").append("</br>");
		}
		
		if(!this.bean.getIsScheduleClicked() && (null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy())){
			hasError = true;
			eMsg.append("Please Verify View Policy Schedule Button.");
		}
		
		/*if(this.bean.getAuditList() != null && !this.bean.getAuditList().isEmpty()){
			if(this.bean.getCvcTable() != null){

				if(this.bean.getCvcTable().getValues() != null){
					if(this.bean.getCvcTable().getValues().isEmpty()){
						hasError = true;
						eMsg.append("CVC (Claim Verification Celll) Observation is available and Reply Required.").append("</br>");
					}else{
						for (CVCObservationReplyAckTableDTO component : this.bean.getCvcTable().getValues()) {
							if(!component.getObservationAckFlag()){
								hasError = true;
								eMsg.append("CVC (Claim Verification Celll) Observation is available and Reply Required.").append("</br>");
								break;
							}
						}
					}
				}else{
					hasError = true;
					eMsg.append("CVC (Claim Verification Celll) Observation is available and Reply Required.").append("</br>");
				}
			}else{
				hasError = true;
				eMsg.append("CVC (Claim Verification Celll) Observation is available and Reply Required.").append("</br>");
			}
		}*/
		
		this.bean.getPreauthMedicalDecisionDetails().setReferToBillEntryBillingRemarks(this.bean.getPreauthMedicalProcessingDetails().getRefBillEntyRsn());

	   if(hasError) {
		   /* Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
		    */
		    HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
		    hasError = true;
		    return !hasError;
	   } else {
			try {
				
				if(! ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					this.binder.commit();
					this.bean.getPreMedicalPreauthMedicalDecisionDetails().setProcedureExclusionCheckTableList(this.procedureExclusionCheckTableObj.getValues());
					// New requirement for saving Copay values to Transaction Table......... 
				}
				
				SHAUtils.setCopayAmounts(bean, this.zonalReviewButtonsObj.amountConsideredTable);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		   return true;
	   }
	}
	
	public void generateButtonFields(String buttonSelected,BeanItemContainer<SelectValue> dropDownValues){
		//submitButton = new Button("Submit");
		this.bean.setStageKey(ReferenceTable.ZONAL_REVIEW_STAGE);
		
		if(buttonSelected.equalsIgnoreCase(SHAConstants.INITIATE_INVESTIGATION)) {
			this.bean.setStatusKey(ReferenceTable.ZONAL_REVIEW_INITATE_INV_STATUS);
			zonalReviewButtonsObj.generateFieldsForInitiateInvestigation(bean.getNewIntimationDTO().getIntimationId(), true,ReferenceTable.ZONAL_REVIEW_STAGE,this); 
		}
		else if(buttonSelected.equalsIgnoreCase(SHAConstants.QUERY)) {
			this.bean.setStatusKey(ReferenceTable.ZONAL_REVIEW_QUERY_STATUS);
			zonalReviewButtonsObj.generateFieldsForReimbursementQuery();
		} 
		else if(buttonSelected.equalsIgnoreCase(SHAConstants.REFER_TO_BILL_ENTRY)){
		
			this.bean.setStatusKey(ReferenceTable.ZMR_REFER_TO_BILL_ENTRY);
			zonalReviewButtonsObj.generateFieldsForReferToBillEntry();
		}
		else if(buttonSelected.equalsIgnoreCase(SHAConstants.REJECT)) {
			this.bean.setStatusKey(ReferenceTable.ZONAL_REVIEW_REJECTION_STATUS);
			
		if(bean.getHospAmountAlreadyPaid() != null && bean.getHospAmountAlreadyPaid().intValue() != 0){
			dropDownValues = (BeanItemContainer<SelectValue>) referenceData.get("setlRejCateg");
//			zonalReviewButtonsObj.generateFieldsForSuggesRejectionForReimbursement(false,setlRejCatContainer);
		}
		else{
			dropDownValues = (BeanItemContainer<SelectValue>) referenceData.get("rejCateg");			
//			zonalReviewButtonsObj.generateFieldsForSuggesRejectionForReimbursement(false,rejCatContainer);
		}
		/*if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
			alertForAdditionalFvrTriggerPoints(SHAConstants.REJECT,dropDownValues);
		}
		else
		{*/
			zonalReviewButtonsObj.generateFieldsForSuggesRejectionForReimbursement(false,dropDownValues); //
		//}
		
		}else if(buttonSelected.equalsIgnoreCase(SHAConstants.CANCEL_ROD)) {
				this.bean.setStatusKey(ReferenceTable.ZONAL_REVIEW_CANCEL_ROD);
				zonalReviewButtonsObj.generateFieldsForCancelROD();
		}else if(buttonSelected.equalsIgnoreCase(SHAConstants.INITIATE_FVR)){
			
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.ZMR_INITIATE_FIELD_REQUEST_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 this.bean.getPreauthMedicalDecisionDetails().setAllocationTo(null);
				 this.bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints("");
			 }

			 if(!ReferenceTable.ZMR_INITIATE_FIELD_REQUEST_STATUS.equals(this.bean.getStatusKey())){
				 ViewFVRDTO trgptsDto = null;
				 List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
				 for(int i = 1; i<=5;i++){
					 trgptsDto = new ViewFVRDTO();
					 trgptsDto.setRemarks("");
					 trgptsList.add(trgptsDto);
				 }
				 this.bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(trgptsList);
			 }
			 zonalReviewButtonsObj.generateFieldsForInitiateFVR();
		}
		
		else {
			this.bean.setStatusKey(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS);
			if((bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) >= 5000d) ) {
				alertMessageForPostHosp();
			} else {
				createSuggestApprovalFields();
			}			
		}
		
	}
	
	public void setInitiateInitInvDto(InitiateInvestigationDTO  initInvDto){
		this.bean.setInitInvDto(initInvDto);
		this.bean.setStatusKey(ReferenceTable.ZONAL_REVIEW_INITATE_INV_STATUS);
	}

	private void createSuggestApprovalFields() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		if(this.pedValidationTableObj != null) {
			boolean isValid = this.pedValidationTableObj.isValid();
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
		if(hasError) {
			 	/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
				GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
		} else {
			
			/*if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
				alertForAdditionalFvrTriggerPoints(SHAConstants.APPROVAL,null);
			}
			else
			{*/
				zonalReviewButtonsObj.generateFieldsForSuggestApproval();
			//}
		}
	}

	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		referenceData.put(SHAConstants.IS_DEFAULT_COPAY, this.bean.getIsDefaultCopay() != null ? this.bean.getIsDefaultCopay() : false);
		referenceData.put(SHAConstants.DEFAULT_COPAY_VALUE, this.bean.getDefaultCopayStr());
		this.zonalReviewButtonsObj.setReference(referenceData);
		pedValidationTableObj.setReferenceData(this.referenceData);
		this.procedureExclusionCheckTableObj.setReference(referenceData);
		BeanItemContainer<SelectValue> coapyValues = (BeanItemContainer<SelectValue>) referenceData.get("copay");
		List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		
		if(! ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
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
					
					if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() && 
							!(ReferenceTable.getDefaultCopayNotApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
						
						coapyValues.sort(new Object[] {"value"}, new boolean[] {false});
					}
					List<SelectValue> itemIds = coapyValues.getItemIds();
					
				    if(itemIds != null && !itemIds.isEmpty()) {
				    	
				    	SelectValue selValue = itemIds.get(0);
				    	procedureDTO.setCopay(selValue);
					}
					
					this.procedureExclusionCheckTableObj.addBeanToList(procedureDTO);
				}
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
	}

	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionContainer) {
		this.pedValidationTableObj.setExclusionDetailsValue(exclusionContainer);
	}

	public void setMedicalDecisionTableValues(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		this.zonalReviewButtonsObj.suggestApprovalClick(dto, medicalDecisionTableValues);
			
		
	}

	public void setCategoryValue(
			BeanItemContainer<SelectValue> selectValueContainer) {
		zonalReviewButtonsObj.setCategoryValue(selectValueContainer);
		
	}

	public void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO) {
		zonalReviewButtonsObj.setBillEntryFinalStatus(uploadDTO);
	}

	public void setBillEntryAmountConsideredValue(Double sumValue) {
		zonalReviewButtonsObj.setBillEntryAmountConsideredValue(sumValue);
		
	}

	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		zonalReviewButtonsObj.setUploadDTOForBillEntry(uploadDTO);
		// TODO Auto-generated method stub
		
	}

	public Boolean alertMessageForPostHosp() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.POST_HOSP_ALERT_MESSAGE + "</b>",
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
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.POST_HOSP_ALERT_MESSAGE + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				createSuggestApprovalFields();
			}
		});
		return true;
	}
	
	public void alertMessageForInvestigation() {
		
		String message = "Investigation Request has already been initiated. </br> Do you still want to initiate another Investigation request?</b>";
		
		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		/*Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Yes");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Button cancelButton = new Button("No");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
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
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();		
				generateButtonFields(SHAConstants.INITIATE_INVESTIGATION,null);
				//fireViewEvent(ClaimRequestWizardPresenter.SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST, bean);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				//wizard.getFinishButton().setEnabled(true);
				bean.setIsInvestigation(false);
//				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
	}
	
	public void alertForHealthGainProduct() {	 
		 
		 /*Label successLabel = new Label(
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
					.createAlertBox(SHAConstants.SUM_INSURED_CHANGE_MESSAGE_HEALTH_GAIN + "</b>", buttonsNamewithType);
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

	
	public void clearReferenceData(){
		SHAUtils.setClearReferenceData(referenceData);;
	}
	
	public void setEnableOrDisableButtons(){
		
		if(zonalReviewButtonsObj != null){
			zonalReviewButtonsObj.setEnableOrDisableButtons();
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
	public void alertForAdditionalFvrTriggerPoints(final String action,final BeanItemContainer<SelectValue> dropDownValues) {	 
		 
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
					.createAlertBox("<b style = 'color: red;'>" + SHAConstants.ADDTIONAL_FVR_TRIGGER_POINTS_ALERT + "</b>", buttonsNamewithType);
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
							//dialog.close();
						}
					});

					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
					

					System.out.println("Close listener called");
					if(SHAConstants.APPROVAL.equalsIgnoreCase(action)){
						zonalReviewButtonsObj.generateFieldsForSuggestApproval();
					}
					else if(SHAConstants.QUERY.equalsIgnoreCase(action)){
						
						zonalReviewButtonsObj.generateFieldsForReimbursementQuery();
					}
					else if(SHAConstants.REJECT.equalsIgnoreCase(action)){
						zonalReviewButtonsObj.generateFieldsForSuggesRejectionForReimbursement(false,dropDownValues); 	
					}
				
				}
			});
			
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();

					System.out.println("Close listener called");
					if(SHAConstants.APPROVAL.equalsIgnoreCase(action)){
						zonalReviewButtonsObj.generateFieldsForSuggestApproval();
					}
					else if(SHAConstants.QUERY.equalsIgnoreCase(action)){
						
						zonalReviewButtonsObj.generateFieldsForReimbursementQuery();
					}
					else if(SHAConstants.REJECT.equalsIgnoreCase(action)){
						zonalReviewButtonsObj.generateFieldsForSuggesRejectionForReimbursement(false,dropDownValues); 	
					}
				
				}
			});
			
			/*dialog.addCloseListener(new Window.CloseListener() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
					if(SHAConstants.APPROVAL.equalsIgnoreCase(action)){
						zonalReviewButtonsObj.generateFieldsForSuggestApproval();
					}
					else if(SHAConstants.QUERY.equalsIgnoreCase(action)){
						
						zonalReviewButtonsObj.generateFieldsForReimbursementQuery();
					}
					else if(SHAConstants.REJECT.equalsIgnoreCase(action)){
						zonalReviewButtonsObj.generateFieldsForSuggesRejectionForReimbursement(false,dropDownValues); 	
					}
				}
			});*/
		}
	
	
}
