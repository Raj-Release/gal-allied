package com.shaic.claim.premedical.wizard.pages;

import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureTableDTO;
import com.shaic.claim.preauth.wizard.pages.ProcedureExclusionCheckTable;
import com.shaic.claim.premedical.listenerTables.PEDValidationListenerTableForPremedical;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PreMedicalPreauthMedicalProcessingPage extends ViewComponent implements WizardStep<PreauthDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4045806801312795730L;
	
	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Instance<PEDValidationListenerTableForPremedical> pedValidationTable;
	
	@Inject
	private Instance<ProcedureExclusionCheckTable> procedureExclusionCheckTable;
	
	@Inject
	private Instance<PreMedicalPreauthButtons> preMedicalPreauthButtons;

	private PEDValidationListenerTableForPremedical pedValidationTableObj;

	private ProcedureExclusionCheckTable procedureExclusionCheckTableObj;
	
	private PreMedicalPreauthButtons preMedicalPreauthButtonsObj;
	
	private Map<String, Object> referenceData;
	
	//private Boolean isPEDAvailable = false;
	
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
		pedValidationTableInstance.init("premedicalPreauth", bean);
		this.pedValidationTableObj = pedValidationTableInstance;
		
		ProcedureExclusionCheckTable procedureExclusionCheckTableInstance = procedureExclusionCheckTable.get();
		procedureExclusionCheckTableInstance.init("Procedure Exclusion Check", false);
		this.procedureExclusionCheckTableObj = procedureExclusionCheckTableInstance;
		
		if(bean.getNewIntimationDTO().getIsTataPolicy() || bean.getNewIntimationDTO().getIsPaayasPolicy()) {
			procedureExclusionCheckTableInstance.setEnabled(false);
		}
		
		PreMedicalPreauthButtons preMedicalPreauthButtonsInstance = preMedicalPreauthButtons.get();
		preMedicalPreauthButtonsInstance.initView(bean);
		this.preMedicalPreauthButtonsObj = preMedicalPreauthButtonsInstance;
		
		VerticalLayout wholeVLayout = new VerticalLayout(pedValidationTableInstance, procedureExclusionCheckTableInstance, preMedicalPreauthButtonsInstance);
		wholeVLayout.setSpacing(true);
		
		return wholeVLayout;
	}

	@SuppressWarnings("unused")
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
		
		/*if(this.procedureExclusionCheckTableObj != null) {
			boolean isValid = this.procedureExclusionCheckTableObj.isValid();
			if(!isValid) {
				hasError = true;
				List<String> errors = this.procedureExclusionCheckTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}*/
		
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
		
		if(!this.bean.getIsScheduleClicked() && (null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy())){
			hasError = true;
			eMsg.append("Please Verify View Policy Schedule Button.").append("</br>");
		}
		
	   if(hasError) {/*
		    Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
		    
		    hasError = true;
		    return !hasError;
	   */
		   MessageBox.createError()
	    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
	        .withOkButton(ButtonOption.caption("OK")).open();
		    hasError = true;
		    return !hasError;
	   
	   } else {
			try {
				/*Below condition removed as per CR2019007 copay editable for GMC product
				if(! ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){*/
					
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
		final MessageBox open = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage("Selected Co-pay Percentage is Zero !!!!.\n Do you wish to Proceed.")
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
		
		Button homeButton=open.getButton(ButtonType.YES);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				open.close();
				bean.setHighestCopay(null);
				bean.setAlertMessageForCopay(true);
				wizard.finish();
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		Button cancelButton=open.getButton(ButtonType.NO);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				open.close();
				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
	}
	
	public void alertMessageForCopay() {/*
		
		String message = "Selected Co-pay Percentage is "+bean.getHighestCopay() + "%"+"</br> Do you wish to Proceed.</b>";
		
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
		
		final MessageBox open = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage("Selected Co-pay Percentage is "+bean.getHighestCopay() + "%"+"\n Do you wish to Proceed.")
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
		
		Button homeButton=open.getButton(ButtonType.YES);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				open.close();
				bean.setHighestCopay(null);
				bean.setAlertMessageForCopay(true);
				wizard.finish();
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		Button cancelButton=open.getButton(ButtonType.NO);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				open.close();
				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
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
						if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getSublimitName() != null ){
							sublimitMapAvailable = sublimitMapAvailable || diagnosisDetailsTableDTO.isSublimitMapAvailable(); 
							selectedSublimitNames = selectedSublimitNames.toString().isEmpty() ? selectedSublimitNames.append(diagnosisDetailsTableDTO.getSublimitName().getName()) : selectedSublimitNames.append(", ").append(diagnosisDetailsTableDTO.getSublimitName().getName());
						}
					}
				Label successLabel = new Label("<b style = 'color: red;'> Sublimit selected is "+selectedSublimitNames.toString()+"</b>", ContentMode.HTML);
				
				Button homeButton = new Button("OK");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
				
				VerticalLayout layout = new VerticalLayout(successLabel, homeButton horizontalLayout);
				layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.setWidth("100%");
				layout.setStyleName("borderLayout");
				
				HorizontalLayout hLayout = new HorizontalLayout(layout);
				hLayout.setMargin(true);
				
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setWidth("250px");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				
				if(sublimitMapAvailable && !selectedSublimitNames.toString().isEmpty()){
				
					dialog.show(getUI().getCurrent(), null, true);
				}
				else if(!sublimitMapAvailable){
					fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_SUBMITTED_EVENT, bean);
				}
				
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 1L;
	
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
						fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_SUBMITTED_EVENT, bean);
					}
				});
		  }	
	*/
		

		
		List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		if(diagnosisList != null && !diagnosisList.isEmpty()){
				StringBuffer selectedSublimitNames = new StringBuffer("");
				boolean sublimitMapAvailable = false;
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
					if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getSublimitName() != null ){
						sublimitMapAvailable = sublimitMapAvailable || diagnosisDetailsTableDTO.isSublimitMapAvailable(); 
						selectedSublimitNames = selectedSublimitNames.toString().isEmpty() ? selectedSublimitNames.append(diagnosisDetailsTableDTO.getSublimitName().getName()) : selectedSublimitNames.append(", ").append(diagnosisDetailsTableDTO.getSublimitName().getName());
					}
				}
				
			
			if(sublimitMapAvailable && !selectedSublimitNames.toString().isEmpty()){
				
				final MessageBox showMessageBox=showInfoMessageBox("Sublimit selected is "+selectedSublimitNames.toString());
				Button homeButton=showMessageBox.getButton(ButtonType.OK);
				
				//showMessageBox.open();
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						showMessageBox.close();
						fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_SUBMITTED_EVENT, bean);
					}
				});
			}
			else if(!sublimitMapAvailable){
				fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_SUBMITTED_EVENT, bean);
			}
			
			
	  }	

	}
	public void generateButtonFields(String buttonSelected){
		if(buttonSelected.equalsIgnoreCase("query")) {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS);
			this.bean.setStageKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
			preMedicalPreauthButtonsObj.generateFieldsForQuery();
			bean.setPreauthHoldStatusKey(null);
		} else if(buttonSelected.equalsIgnoreCase("suggestion")) {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS);
			this.bean.setStageKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
			preMedicalPreauthButtonsObj.generateFieldsForSuggesRejection(false);
			bean.setPreauthHoldStatusKey(null);
		}else if(buttonSelected.equalsIgnoreCase("hold")) {
			bean.setPreauthHoldStatusKey(ReferenceTable.PREAUTH_HOLD_STATUS_KEY);
			preMedicalPreauthButtonsObj.buildHoldLayout();
			//added for jira IMSSUPPOR-31343
			this.bean.setStatusKey(null);
			this.bean.setStageKey(null);
		} else {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS);
			this.bean.setStageKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
			preMedicalPreauthButtonsObj.generateFieldsForSendForProcessing(false);
			//added for jira IMSSUPPOR-31343
			bean.setPreauthHoldStatusKey(null);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		referenceData.put(SHAConstants.IS_DEFAULT_COPAY, this.bean.getIsDefaultCopay() != null ? this.bean.getIsDefaultCopay() : false);
		referenceData.put(SHAConstants.DEFAULT_COPAY_VALUE, this.bean.getDefaultCopayStr());
		this.preMedicalPreauthButtonsObj.setReference(referenceData);
		pedValidationTableObj.setReferenceData(this.referenceData);
		this.procedureExclusionCheckTableObj.setReference(referenceData);
		procedureExclusionCheckTableObj.setEnabled(false);
		BeanItemContainer<SelectValue> coapyValues = (BeanItemContainer<SelectValue>) referenceData.get("copay");
		List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		
		/*Below condition removed as per CR2019007 copay editable for GMC product
		if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){*/
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
							selectValue.setValue(String.valueOf(diagnosisDetailsTableDTO.getCopayPercentage().doubleValue()));
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
					
					
					List<SelectValue> itemIds = coapyValues.getItemIds();
					
				    if(itemIds != null && !itemIds.isEmpty()) {
				    	
				    	SelectValue selValue = itemIds.get(0);
				    	procedureDTO.setCopay(selValue);
					}
//				    Below condition  as per CR2019007 copay editable for GMC product
				    if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				    		procedureDTO.setCopay(null);
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

		if(this.bean.getStatusKey() != null) {
			if(this.bean.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)) {
				fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_QUERY_EVENT, null);
			} else if(this.bean.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)) {
				fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_SUGGEST_REJECTION_EVENT, null);
			} else if(this.bean.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)){
				fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_SEND_FOR_PROCESSING_EVENT, null);
			}
		}
	}

	private void setPEDDetailsToDTO(DiagnosisDetailsTableDTO diagnosisDetailsTableDTO,
			InsuredPedDetails pedList) {
		PedDetailsTableDTO dto = new PedDetailsTableDTO();
		dto.setDiagnosisName(diagnosisDetailsTableDTO.getDiagnosis());
		dto.setEnableOrDisable(diagnosisDetailsTableDTO.getEnableOrDisable());
		dto.setPolicyAgeing(diagnosisDetailsTableDTO.getPolicyAgeing());
		if(pedList == null) {
			dto.setPedCode("");
			dto.setPedName("");
		} else {
			dto.setPedCode(pedList.getPedCode());
			dto.setPedName(pedList.getPedDescription());
		}
        
		this.pedValidationTableObj.addBeanToList(dto);
	}

	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionContainer) {
		this.pedValidationTableObj.setExclusionDetailsValue(exclusionContainer);
	}
	
	 public void setClearReferenceData(){
//		 pedValidationTable.destroy(pedValidationTableObj);
//		 procedureExclusionCheckTable.destroy(procedureExclusionCheckTableObj);
//		 preMedicalPreauthButtons.destroy(preMedicalPreauthButtonsObj);
		 SHAUtils.setClearPreauthDTO(bean);
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
	 final MessageBox showMessageBox=showInfoMessageBox(SHAConstants.SUM_INSURED_CHANGE_MESSAGE_HEALTH_GAIN);
	 Button homeButton=showMessageBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showMessageBox.close();
			}
		});
		 
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
}
