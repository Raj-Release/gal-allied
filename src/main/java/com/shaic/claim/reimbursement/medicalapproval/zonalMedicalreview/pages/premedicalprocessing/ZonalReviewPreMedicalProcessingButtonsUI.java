package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Alternative;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.pages.PreauthButtonListeners;
import com.shaic.claim.preauth.wizard.pages.PreauthButtonsUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.Field;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
@Alternative
public class ZonalReviewPreMedicalProcessingButtonsUI extends PreauthButtonsUI implements PreauthButtonListeners {
	private static final long serialVersionUID = 4814413087097394190L;

	
	private HorizontalLayout buildButtonsHLayout;
	private PreauthDTO bean;
	
	private Button submitButton;
	
	Map<String, Object> referenceData;
	
	HashMap<String, String> enteredValues = new HashMap<String, String>();
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private PreauthService preauthService;
	
	//private Boolean isApproval = false;
	
	@PostConstruct
	public void init() {
		
	}
	
	public void initView(PreauthDTO bean) {
		this.bean = bean;
		errorMessages = new ArrayList<String>();
		submitButton = new Button("Submit");
		buildButtonsHLayout = buildZonalReviewButtons(bean, submitButton);
		dynamicFieldsLayout = new VerticalLayout();		
		addListener();
		if(null != bean && null != this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() && 
				null != this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() &&
				(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT).equals(this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId()))
		{
			sendForProcessing.setEnabled(false);
		}
		
		
		setCompositionRoot(new VerticalLayout(buildButtonsHLayout, dynamicFieldsLayout));
	}
	
	protected void addListener(){
		/*invstionbtn.addClickListener(new ClickListener() {
			
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 2679764179795985945L;

			@Override
			public void buttonClick(ClickEvent event) {
				//isApproval = false;	
				
				fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_REVIEW_INITIATE_INV_EVENT, bean);
			}
		});
*/
		query.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 2679764179795985945L;

			@Override
			public void buttonClick(ClickEvent event) {
				//isApproval = false;
				fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_REVIEW_SUGGEST_QUERY_EVENT, null);
			}
		});
		
		suggestRejection.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -1545640032342015257L;

			@Override
			public void buttonClick(ClickEvent event) {
				//isApproval = false;  referenceData  to be paassed
				fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_REVIEW_SUGGEST_REJECTION_EVENT, null);
			}
		});
		
		referToBillEntryBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
//				isApproval = false;
				if(!bean.getIsReferToBillEntry()){
				fireViewEvent(
						MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_REVIEW_REF_BILLENTRY_EVENT,
						null);
				}		
			}
		});
		
		
		sendForProcessing.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1274221814969702338L;

			@Override
			public void buttonClick(ClickEvent event) {
				//isApproval = true;
				Boolean isStopProcess = SHAUtils.alertMessageForStopProcess(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),getUI());
				if(!isStopProcess){
					
					if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
							&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
						Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
						bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
					}else{
						bean.setIsPedWatchList(false);
					}
					
					if(bean.getIsPedWatchList()){
					    alertMessageForPEDWatchList();
					}
					else if(validatePolicyStatus(bean.getPolicyDto().getPolicyNumber())){
						showErrorPageForCancelledPolicy();
					}
					else {
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}
						
						fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_REVIEW_SUGGEST_APPROVAL_EVENT, null);
					}
				}
			}
		});
		
		cancelRod.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -1545640032342015256L;

			@Override
			public void buttonClick(ClickEvent event) {
				//isApproval = false;
				fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_REVIEW_CANCEL_ROD_EVENT, null);
			}
		});
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1076409627764028279L;

			@Override
			public void buttonClick(ClickEvent event) {
				Boolean hasError = false;
				StringBuffer eMsg = new StringBuffer();
				if(!isValid()) {
					hasError = true;
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
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
				}
			}
		});
		
		initiateFieldVisitBtn.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_REVIEW_INITIATE_FVR_EVENT, null);
			}
		});
	}
	
	public boolean isValid()
	{
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		//String eMsg = "";
		
		
//		if(uploadDocumentListenerTableObj != null){
//			Boolean isValid = uploadDocumentListenerTableObj.getBillEntryZonalRemarks();
//			if(isApproval){
//				if (!isValid) {
//					hasError = true;
//					errorMessages.add("Please Enter Zonal Remarks in Bill Entry. </br>");
//				}
//			}
//			
//		}
		
		if(this.binder == null) {
			hasError = true;
			errorMessages.add("Query or Suggest for Rejection or Send For Processing to be entered. </br>");
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
				this.binder.commit();
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		showOrHideValidation(false);
		return !hasError;
	}
	
	public boolean isValidForReimbursement()
	{
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		//String eMsg = "";
		
		
//		if(uploadDocumentListenerTableObj != null){
//			Boolean isValid = uploadDocumentListenerTableObj.getBillEntryZonalRemarks();
//			if(isApproval){
//				if (!isValid) {
//					hasError = true;
//					errorMessages.add("Please Enter Zonal Remarks in Bill Entry.");
//				}
//			}
//			
//		}
		
		
		if(null != this.bean.getStatusKey() &&
				this.bean.getStatusKey().equals(ReferenceTable.ZONAL_REVIEW_INITATE_INV_STATUS)
				|| this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)){
			hasError = true;
			errorMessages.add("Please select Suggest Query. </br>");
			return !hasError;
		}
		else if(this.binder == null) {
			hasError = true;
			if(this.bean.getPreauthDataExtractionDetails().getIsFvrOrInvsInitiated() != null &&
					this.bean.getPreauthDataExtractionDetails().getIsFvrOrInvsInitiated()){
				errorMessages.add("Please select Suggest Query. </br>");
			}else{
				errorMessages.add("Query or Suggest for Rejection or Suggest Approval to be entered. </br>");
			}
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
				this.binder.commit();
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		showOrHideValidation(false);
		return !hasError;
	}
	
	public void setReference(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		setReferenceData(referenceData);
	}
	
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	
	public void suggestApprovalClick(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		setAppropriateValuesToDTOFromProcedure(dto, medicalDecisionTableValues);
		
	}

	public void setCategoryValue(
			BeanItemContainer<SelectValue> selectValueContainer) {
		if(this.uploadDocumentListenerTableObj != null) {
			this.uploadDocumentListenerTableObj.setupCategoryValues(selectValueContainer);
		}
		
	}

	public void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO) {
		List<UploadDocumentDTO> uploadDoc = uploadDocumentListenerTableObj.getValues();
		List<UploadDocumentDTO> uploadList = new ArrayList<UploadDocumentDTO>();
		for (UploadDocumentDTO uploadDocumentDTO : uploadDoc) {
			if(null != uploadDocumentDTO.getFileType() && null != uploadDocumentDTO.getFileType().getValue())
			{
				if(uploadDocumentDTO.getFileType().getValue().contains("Bill"))
				{
					/**
					 * Sequence number is an internal parameter maintained for updating the 
					 * uploadlistener table. This is because the row for which the bill is entered
					 * should only get updated. Rest of rows should  be the same. Earlier this
					 * was done with bill no. But there are chance that even bill no can be duplicate.
					 * Hence removed this and added validation based on seq no.
					 * */
					
					
					
					
					if(uploadDocumentDTO.getSeqNo().equals(uploadDTO.getSeqNo()))
					{
						//uploadList.add(uploadDTO);
					}
					else
					{
						uploadList.add(uploadDocumentDTO);
					}
				}
				else
				{
					uploadList.add(uploadDocumentDTO);
				}
			}
			
		}
		uploadList.add(uploadDTO);
		uploadDocumentListenerTableObj.updateTable(uploadList);
		
	}

	public void setBillEntryAmountConsideredValue(Double sumValue) {
		
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
		//setAmountConsideredValue(sumValue, bean);
	}

	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		// TODO Auto-generated method stub
		uploadDocumentListenerTableObj.loadBillEntryValues(uploadDTO);
	}

	public void setEnableOrDisableButtons(){
		
		sendForProcessing.setEnabled(false);
		suggestRejection.setEnabled(false);
		cancelRod.setEnabled(false);
	}
	
	public Boolean alertMessageForPED(String message) {
   	/*	Label successLabel = new Label(
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("<b style = 'color: red;'>" + message + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();					
			}
		});
		return true;
	}
	
	public Boolean alertMessageForPEDWatchList() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_WATCHLIST + "</b>",
				ContentMode.HTML);
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
				.createInformationBox(SHAConstants.PED_WATCHLIST +"</b>", buttonsNamewithType);
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
		return hasError;
	}
	
	public void showErrorPageForCancelledPolicy(){
		
		String message = SHAConstants.CANCELLED_POLICY_ALERT ;
		
		/*Label successLabel = new Label(
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox( message + "</b>", buttonsNamewithType);
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
