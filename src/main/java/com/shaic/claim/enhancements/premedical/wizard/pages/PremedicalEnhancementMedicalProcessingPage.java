package com.shaic.claim.enhancements.premedical.wizard.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.enhancementButtons.PremedicalEnhancementButtons;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.ProcedureExclusionCheckTable;
import com.shaic.claim.premedical.listenerTables.PEDValidationListenerTableForPremedical;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.VerticalLayout;

public class PremedicalEnhancementMedicalProcessingPage  extends ViewComponent implements WizardStep<PreauthDTO>{
	private static final long serialVersionUID = 665404374570436408L;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Instance<PEDValidationListenerTableForPremedical> pedValidationTable;
	
	@Inject
	private Instance<ProcedureExclusionCheckTable> procedureExclusionCheckTable;
	
	@Inject
	private Instance<PremedicalEnhancementButtons> preMedicalPreauthButtons;

	private PEDValidationListenerTableForPremedical pedValidationTableObj;

	private ProcedureExclusionCheckTable procedureExclusionCheckTableObj;
	
	private PremedicalEnhancementButtons preMedicalPreauthButtonsObj;
	
	private Map<String, Object> referenceData;

	private GWizard wizard;

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
	}
	
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}
	
	@Override
	public String getCaption() {
		return "First Level Processing";
	}
	@Override
	public Component getContent() {
		
		if(ReferenceTable.getHealthGainProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(bean.getIsChangeInsumInsuredAlert()){
				alertForHealthGainProduct();
			}
		}
		
		PEDValidationListenerTableForPremedical pedValidationTableInstance = pedValidationTable.get();
		pedValidationTableInstance.init("premedicalEnhancement", bean);
		this.pedValidationTableObj = pedValidationTableInstance;
		
		ProcedureExclusionCheckTable procedureExclusionCheckTableInstance = procedureExclusionCheckTable.get();
		procedureExclusionCheckTableInstance.init("Procedure Exclusion Check", false);
		if(bean.getNewIntimationDTO().getIsTataPolicy() || bean.getNewIntimationDTO().getIsPaayasPolicy()) {
			procedureExclusionCheckTableInstance.setEnabled(false);
		}
		
		this.procedureExclusionCheckTableObj = procedureExclusionCheckTableInstance;
		
		PremedicalEnhancementButtons preMedicalPreauthButtonsInstance = preMedicalPreauthButtons.get();
		preMedicalPreauthButtonsInstance.initView(bean);
		this.preMedicalPreauthButtonsObj = preMedicalPreauthButtonsInstance;
		
		
		
		VerticalLayout wholeVLayout = new VerticalLayout(pedValidationTableInstance, procedureExclusionCheckTableInstance,preMedicalPreauthButtonsInstance);
		wholeVLayout.setSpacing(true);
		
		return wholeVLayout;
	}

	
	private void setExclusionTable(ProcedureExclusionCheckTable procedureExclusionCheck) {
//		List<ProcedureTableDTO> procedureDTOList = this.bean.getPreauthDataExtractionDetails().getProcedureList();
//		List<NewProcedureTableDTO> newProcedureDTOList = this.bean.getPreauthDataExtractionDetails().getNewProcedureList();
//		List<ProcedureTableDTO> list = new ArrayList<ProcedureTableDTO>();
//		for (ProcedureTableDTO procedureDto : procedureDTOList) {
//			addProcedureBeantoList(procedureExclusionCheck, procedureDto);
//		}
//		
//		for (NewProcedureTableDTO procedureDto : newProcedureDTOList) {
//			ProcedureDTO dto = new ProcedureDTO();
//			dto.setProcedureName(procedureDto.getProcedureName());
//			dto.setProcedureCode(procedureDto.getProcedureCode());
//			dto.setPackageAmount("2000");
//			procedureExclusionCheck.addBeanToList(dto);
//		}
	}

	
	public void afterConfirmOk(){
		//fireViewEvent(MenuPresenter.showSearchPreAuthView(), null);
	}

	@Override
	public boolean onAdvance() {
		if(validatePage()){
			if(! bean.getAlertMessageForCopay() && bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
				if(bean.getHighestCopay() != null && bean.getHighestCopay().equals(0.0d)){
					warningMessageForCopay();
					this.wizard.getFinishButton().setEnabled(true);
				}else if(bean.getHighestCopay() != null && ! bean.getHighestCopay().equals(0.0d)){
					alertMessageForCopay();
				}else{
					return true;
				}
			}else{
				return true;
			}
		}
		
		return false;
		
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onSave() {
		return validatePage();
	}
	
	private boolean validatePage() {
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
		
		if(this.preMedicalPreauthButtonsObj != null) {
			boolean isValid = this.preMedicalPreauthButtonsObj.isValid();
			if(!isValid) {
				hasError = true;
				List<String> errors = this.preMedicalPreauthButtonsObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if(!this.bean.getIsScheduleClicked() && (null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy()) ){
			hasError = true;
			eMsg.append("Please Verify View Policy Schedule Button.").append("</br>");
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
		    
		    hasError = true;
		    return !hasError;
	   } else {
			try {
//				this.bean.getPreMedicalPreauthMedicalDecisionDetails().setPedValidationTableList(this.pedValidationTableObj.getValues());
				
				/*Below condition removed as per CR2019007 copay editable for GMC product
				if(! this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)){*/

					this.bean.getPreMedicalPreauthMedicalDecisionDetails().setProcedureExclusionCheckTableList(this.procedureExclusionCheckTableObj.getValues());
					
					Double copayAvailableForPEDValidation = SHAUtils.isCopayAvailableForPEDValidation(this.pedValidationTableObj.getValues());
					Double copayAvailableForProcedureValidation = SHAUtils.isCopayAvailableForProcedureValidation(this.procedureExclusionCheckTableObj.getValues());
					
					if(copayAvailableForPEDValidation != null && copayAvailableForProcedureValidation != null){
						copayAvailableForPEDValidation = Math.max(copayAvailableForPEDValidation, copayAvailableForProcedureValidation);
					}
					
					bean.setHighestCopay(copayAvailableForPEDValidation);
//				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		   return true;
	   }
	}
	
	public void generateButtonFields(String buttonSelected){
		this.bean.setStageKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
		if(buttonSelected.equalsIgnoreCase("query")) {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS);
			bean.setPreauthHoldStatusKey(null);
			preMedicalPreauthButtonsObj.generateFieldsForQuery();
		} else if(buttonSelected.equalsIgnoreCase("suggestion")) {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS);
			bean.setPreauthHoldStatusKey(null);
			preMedicalPreauthButtonsObj.generateFieldsForSuggesRejection(true);
			//preMedicalPreauthButtonsObj.generateFieldsForSuggesRejection(false);
		} else if(buttonSelected.equalsIgnoreCase("hold")) {
			bean.setPreauthHoldStatusKey(ReferenceTable.PREAUTH_HOLD_STATUS_KEY);
			this.bean.setStatusKey(null);
			preMedicalPreauthButtonsObj.buildHoldLayout();
		}else {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS);
			//preMedicalPreauthButtonsObj.generateFieldsForSendForProcessing(false);
			bean.setPreauthHoldStatusKey(null);
			preMedicalPreauthButtonsObj.generateFieldsForSendForProcessing(true);
		}
		
	}
	
	public void warningMessageForCopay() {/*
		
		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		Label successLabel = new Label("<b style = 'color: red;'>Selected Co-pay Percentage is Zero !!!!.</br> Do you wish to Proceed.</b>", ContentMode.HTML);
		
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
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				bean.setHighestCopay(null);
				bean.setAlertMessageForCopay(true);
				wizard.finish();
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				
				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
	*/
        final MessageBox getConf = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage("Selected Co-pay Percentage is Zero !!!!.\n Do you wish to Proceed.")
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
			Button homeButton=getConf.getButton(ButtonType.YES);
			Button cancelButton=getConf.getButton(ButtonType.NO);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				getConf.close();
				bean.setHighestCopay(null);
				bean.setAlertMessageForCopay(true);
				wizard.finish();
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		 cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				getConf.close();
				
				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		
	}
	
	public void alertMessageForCopay() {/*
		
		String message = "Selected Co-pay Percentage is "+bean.getHighestCopay() + " %"+"</br> Do you wish to Proceed.</b>";
		
		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);
		
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
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				bean.setHighestCopay(null);
				bean.setAlertMessageForCopay(true);
				wizard.finish();
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				
				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
	*/

		
		String message = "Selected Co-pay Percentage is "+bean.getHighestCopay() + " %"+"\n Do you wish to Proceed.";

		final MessageBox getConf = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage(message)
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
			Button homeButton=getConf.getButton(ButtonType.YES);
			Button cancelButton=getConf.getButton(ButtonType.NO);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				getConf.close();
				bean.setHighestCopay(null);
				bean.setAlertMessageForCopay(true);
				wizard.finish();
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				getConf.close();
				
				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		
		this.preMedicalPreauthButtonsObj.setReference(referenceData);
		referenceData.put(SHAConstants.IS_DEFAULT_COPAY, this.bean.getIsDefaultCopay() != null ? this.bean.getIsDefaultCopay() : false);
		referenceData.put(SHAConstants.DEFAULT_COPAY_VALUE, this.bean.getDefaultCopayStr());
		pedValidationTableObj.setReferenceData(this.referenceData);
		this.procedureExclusionCheckTableObj.setReference(referenceData);
//		this.pedValidationTableObj.setTableList(this.bean.getPreMedicalPreauthMedicalDecisionDetails().getPedValidationTableList());
		List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		/*Below condition removed as per CR2019007 copay editable for GMC product
		if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){*/
		SelectValue selectValue = null;
		for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : pedValidationTableList) {
			List<PedDetailsTableDTO> pedList = diagnosisDetailsTableDTO.getPedList();
			for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
				pedDetailsTableDTO.setRecTypeFlag(diagnosisDetailsTableDTO.getRecTypeFlag());
			}
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
						selectValue.setValue(String.valueOf(diagnosisDetailsTableDTO.getCopayPercentage().doubleValue()));
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
				procedureDTO.setStatusFlag(true);
				if(procedureDTO.getRecTypeFlag() != null && procedureDTO.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c")) {
					procedureDTO.setStatusFlag(false);
				}
				procedureDTO.setStatusFlag(true);
				
				/*Below condition  as per CR2019007 copay editable for GMC product*/
				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
					if(procedureDTO.getCopay() != null) {
						procedureDTO.setCopay(procedureDTO.getCopay());
					} else {
				    	procedureDTO.setCopay(null);
					}
					procedureDTO.setIsGMC(true);
					procedureDTO.setGmcFlag(true);
				}
				if(bean.getNewIntimationDTO().getIsTataPolicy() || bean.getNewIntimationDTO().getIsPaayasPolicy()) {
					procedureDTO.setIsTata(true);
					procedureDTO.setIsPayaas(true);
				}
				
				this.procedureExclusionCheckTableObj.addBeanToList(procedureDTO);
			}
		 }
//		}
	}

	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionContainer) {
		this.pedValidationTableObj.setExclusionDetailsValue(exclusionContainer);
	}	
	
	public void setClearReferenceData(){
		/* pedValidationTable.destroy(pedValidationTableObj);
		 procedureExclusionCheckTable.destroy(procedureExclusionCheckTableObj);
		 preMedicalPreauthButtons.destroy(preMedicalPreauthButtonsObj);*/
		SHAUtils.setClearReferenceData(referenceData);
	}
	
    public void alertForHealthGainProduct() {/*	 
		 
		 Label successLabel = new Label(
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
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});
		*/
    	final MessageBox showAlert = showAlertMessageBox(SHAConstants.SUM_INSURED_CHANGE_MESSAGE_HEALTH_GAIN);
    	Button homeButton = showAlert.getButton(ButtonType.OK);


			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					showAlert.close();
				}
			});
			
    }
    
	/**			  
	 * Part of CR R1136
	 */
	public void showSublimitAlert() {/*
			
			List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			if(diagnosisList != null && !diagnosisList.isEmpty()){
					StringBuffer selectedSublimitNames = new StringBuffer("");
					boolean sublimitMapAvailable = false;
					for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
						if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getSublimitName() != null){
							sublimitMapAvailable = sublimitMapAvailable || diagnosisDetailsTableDTO.isSublimitMapAvailable(); 
							selectedSublimitNames = selectedSublimitNames.toString().isEmpty() ? selectedSublimitNames.append(diagnosisDetailsTableDTO.getSublimitName().getName()) : selectedSublimitNames.append(", ").append(diagnosisDetailsTableDTO.getSublimitName().getName());
						}
					}
				Label successLabel = new Label("<b style = 'color: red;'> Sublimit selected is "+selectedSublimitNames.toString()+"</b>", ContentMode.HTML);
				
				Button homeButton = new Button("OK");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
				horizontalLayout.setMargin(true);
				
				VerticalLayout layout = new VerticalLayout(successLabel, homeButton horizontalLayout);
				layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
				layout.setWidth("100%");
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.setStyleName("borderLayout");
				
				HorizontalLayout hLayout = new HorizontalLayout(layout);
				hLayout.setMargin(true);
				
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setWidth("250px");
				dialog.setClosable(true);
				dialog.setResizable(false);
				dialog.setModal(true);
				
				if(sublimitMapAvailable && !selectedSublimitNames.toString().isEmpty()){
				
					dialog.show(getUI().getCurrent(), null, true);
				}	
				else if(!sublimitMapAvailable){
					fireViewEvent(
							PremedicalEnhancementWizardPresenter.PREMEDICAL_SUBMITTED_EVENT,
							bean);
				}
				
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 1L;
	
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
						fireViewEvent(
								PremedicalEnhancementWizardPresenter.PREMEDICAL_SUBMITTED_EVENT,
								bean);
					}
				});
		  }	
	*/

		
		List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		if(diagnosisList != null && !diagnosisList.isEmpty()){
				StringBuffer selectedSublimitNames = new StringBuffer("");
				boolean sublimitMapAvailable = false;
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
					if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getSublimitName() != null){
						sublimitMapAvailable = sublimitMapAvailable || diagnosisDetailsTableDTO.isSublimitMapAvailable(); 
						selectedSublimitNames = selectedSublimitNames.toString().isEmpty() ? selectedSublimitNames.append(diagnosisDetailsTableDTO.getSublimitName().getName()) : selectedSublimitNames.append(", ").append(diagnosisDetailsTableDTO.getSublimitName().getName());
					}
				}
			
			
			if(sublimitMapAvailable && !selectedSublimitNames.toString().isEmpty()){
			//dialog.show(getUI().getCurrent(), null, true);
				final MessageBox showInfo= showInfoMessageBox(" Sublimit selected is "+selectedSublimitNames.toString());
				Button homeButton = showInfo.getButton(ButtonType.OK);
				//showInfo.open();
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						showInfo.close();
						fireViewEvent(
								PremedicalEnhancementWizardPresenter.PREMEDICAL_SUBMITTED_EVENT,
								bean);
					}
				});
			}	
			else if(!sublimitMapAvailable){
				fireViewEvent(
						PremedicalEnhancementWizardPresenter.PREMEDICAL_SUBMITTED_EVENT,
						bean);
			}
			
			
	  }	
	
	
	}
	
	public MessageBox showAlertMessageBox(String message){
		
		
		final MessageBox msgBox = MessageBox
			    .createWarning()
			    .withCaptionCust("Warning")
			    .withHtmlMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
		return msgBox;
		
		
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
    
}
