package com.shaic.claim.preauth.wizard.pages;

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
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureTableDTO;
import com.shaic.claim.preauth.wizard.listenerTables.PEDValidationListenerTableForPreauth;
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
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PreauthMedicalProcessingPage extends ViewComponent implements WizardStep<PreauthDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1439983595499156592L;
	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Instance<PEDValidationListenerTableForPreauth> pedValidationTable;
	
	@Inject
	private Instance<ProcedureExclusionCheckTable> procedureExclusionCheckTable;
	
//	@Inject
//	private Instance<PreauthButtons> preauthButtonInstance;

	private PEDValidationListenerTableForPreauth pedValidationTableObj;

	private ProcedureExclusionCheckTable procedureExclusionCheckTableObj;
	
//	private PreauthButtons preauthButtonObj;
	
	////private static Window popup;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	private Map<String, Object> referenceData;
	
	private String diagnosisName;
	
	Button initiatePEDButton;
	
	//private GWizard wizard;

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
	}
	
	public void init(PreauthDTO bean,GWizard wizard) {
		this.bean = bean;
		//this.wizard = wizard;
	}
	
	@Override
	public String getCaption() {
		return "Medical Processing";
	}

	@Override
	public Component getContent() {
		
		if(ReferenceTable.getHealthGainProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(bean.getIsChangeInsumInsuredAlert()){
				alertForHealthGainProduct();
			}
		}
		
		initiatePEDButton = new Button("Initiate PED Endorsement");
		HorizontalLayout buttonHLayout = new HorizontalLayout(initiatePEDButton);
		buttonHLayout.setMargin(true);
		buttonHLayout.setComponentAlignment(initiatePEDButton, Alignment.MIDDLE_RIGHT);
		
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
		
		PEDValidationListenerTableForPreauth pedValidationTableInstance = pedValidationTable.get();
		pedValidationTableInstance.init(bean);
		this.pedValidationTableObj = pedValidationTableInstance;
		
		ProcedureExclusionCheckTable procedureExclusionCheckTableInstance = procedureExclusionCheckTable.get();
		procedureExclusionCheckTableInstance.init("Procedure Exclusion Check", false);
		this.procedureExclusionCheckTableObj = procedureExclusionCheckTableInstance;
		if(bean.getNewIntimationDTO().getIsTataPolicy() || bean.getNewIntimationDTO().getIsPaayasPolicy()) {
			procedureExclusionCheckTableInstance.setEnabled(false);
		}
		
		
//		PreauthButtons preauthButtonsInstance = preauthButtonInstance.get();
//		preauthButtonsInstance.initView(bean);
//		this.preauthButtonObj = preauthButtonsInstance;
		
		VerticalLayout wholeVLayout = new VerticalLayout(buttonHLayout, pedValidationTableInstance, procedureExclusionCheckTableInstance);
		wholeVLayout.setComponentAlignment(buttonHLayout, Alignment.MIDDLE_RIGHT);
		wholeVLayout.setSpacing(true);
		addListener();
		
		return wholeVLayout;
	}
	
	public void addListener() {
		initiatePEDButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8159939563947706329L;

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
		viewPEDRequest.initView(bean,preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.PREAUTH_STAGE,false);	
		viewPEDRequest.setPresenterString(SHAConstants.CASHLESS_STRING);
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
	
	

	
	
public Boolean alertMessageForPEDInitiate(String message) {/*
   		Label successLabel = new Label(
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
//		dialog.setCaption("Alert");
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
				Long preauthKey=bean.getKey();
				Long intimationKey=bean.getIntimationKey();
				Long policyKey=bean.getPolicyKey();
				Long claimKey=bean.getClaimKey();
				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
			}
		});
		return true;
	*/
	
	final MessageBox showAlert = showAlertMessageBox(message);
	Button homeButton = showAlert.getButton(ButtonType.OK);
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			showAlert.close();
			Long preauthKey=bean.getKey();
			Long intimationKey=bean.getIntimationKey();
			Long policyKey=bean.getPolicyKey();
			Long claimKey=bean.getClaimKey();
			createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
		}
	});
	return true;
	
}
	
	/**
	 * The below method is not in use. Hence commented this during
	 * TmpPolicy to Policy refracting activity.
	 * */
	
	/*private void setPEDTable(PEDValidationTable pedValidationTableInstance) {
//		List<DiagnosisTableDTO> diagnosisTableList2 = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		List<DiagnosisDetailsTableDTO> list = new ArrayList<DiagnosisDetailsTableDTO>();
		List<InsuredPedDetails> insuredPEDList = (List<InsuredPedDetails>) referenceData.get("pedList");
		for(int i = 8; i<10; i++) {
			if(!insuredPEDList.isEmpty()) {
				int size = insuredPEDList.size();
				Float center = new Float(size)/ 2;
				int round = Math.round(center);
				for (int j = 0; j<insuredPEDList.size(); j++) {
					 InsuredPedDetails ped =  insuredPEDList.get(j);
					 DiagnosisDetailsTableDTO dto = new DiagnosisDetailsTableDTO();
					 dto.setDiagnosisId(new Long(i));
					 if(j == round) {
						 
						 dto.setDiagnosis("Diagnosis" + i);
					 }
					 
					 *//**
					  * PED Name should be retrieved by sending ped code to peddetails table
					  *//*
					 
					 dto.setPedName(ped.getPedName());
//					 dto.setPolicyAgeing(new Long(2));
//					 this.pedValidationTableObj.addBeanToList(dto);
				}
				
			}
			
		}*/
		
		
//		for (DiagnosisTableDTO diagnosisTableDTO : diagnosisTableList2) {
//			PEDValidationTableDTO dto = new PEDValidationTableDTO();
//			dto.setDiagnosis(diagnosisTableDTO.getDiagnosis() != null ? diagnosisTableDTO.getDiagnosis().getValue() : null);
//			dto.setPedName("Dummy PED");
//			dto.setPolicyAgeing(new Long(2));
//			pedValidationTableInstance.addBeanToList(dto);
//		}
//	}
	
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
////			dto.setProcedureName(procedureDto.getProcedureName());
////			dto.setProcedureCode(procedureDto.getProcedureCode());
//			dto.setPackageAmount("2000");
//			procedureExclusionCheck.addBeanToList(dto);
//		}
	}

	private void addProcedureBeantoList(
			ProcedureExclusionCheckTable procedureExclusionCheck,
			ProcedureTableDTO procedureDto) {
		ProcedureDTO dto = new ProcedureDTO();
//		dto.setProcedureName(procedureDto.getProcedureName().getValue());
//		dto.setProcedureCode(procedureDto.getProcedureCode().getValue());
		dto.setPackageAmount("2000");
//			dto.setPackageAmount(procedureDto.get);
//			dto.setPolicyAging(procedureDto.getP);
		procedureExclusionCheck.addBeanToList(dto);
	}

	@Override
	public boolean onAdvance() {
		return validatePage();
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		return validatePage();
	}
	
	@SuppressWarnings("static-access")
	private boolean validatePage() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		
		if (this.bean.getIsReferTOFLP()) {
    		return true;
    	}
		
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
//				this.bean.getPreMedicalPreauthMedicalDecisionDetails().setPedValidationTableList(this.pedValidationTableObj.getValues());
				this.bean.getPreMedicalPreauthMedicalDecisionDetails().setProcedureExclusionCheckTableList(this.procedureExclusionCheckTableObj.getValues());
			} catch (Exception e) {
				e.printStackTrace();
			}
		   return true;
	   }
	}
	
	public void generateButtonFields(String buttonSelected){
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		referenceData.put(SHAConstants.IS_DEFAULT_COPAY, this.bean.getIsDefaultCopay() != null ? this.bean.getIsDefaultCopay() : false);
		referenceData.put(SHAConstants.DEFAULT_COPAY_VALUE, this.bean.getDefaultCopayStr());
		pedValidationTableObj.setReferenceData(this.referenceData);
		this.procedureExclusionCheckTableObj.setReference(referenceData);
//		this.pedValidationTableObj.setTableList(this.bean.getPreMedicalPreauthMedicalDecisionDetails().getPedValidationTableList());
		List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		
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
						SelectValue selectValue = new SelectValue();
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
					SelectValue selectValue = new SelectValue();
					selectValue.setId(procedureDTO.getCopayPercentage().longValue());
					selectValue.setValue(String.valueOf(procedureDTO.getCopayPercentage().doubleValue()));
					procedureDTO.setCopay(selectValue);
				}
				procedureDTO.setStatusFlag(true);
				
				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
					if(procedureDTO.getCopay()!= null) {
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
		if(this.bean.getStatusValue() != null) {
			if(this.bean.getStatusKey() == ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) {
				fireViewEvent(PreauthWizardPresenter.PREAUTH_QUERY_EVENT, null);
			} else if(this.bean.getStatusKey() == ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS) {
				fireViewEvent(PreauthWizardPresenter.PREAUTH_SUGGEST_REJECTION_EVENT, null);
			} else {
				fireViewEvent(PreauthWizardPresenter.PREAUTH_SEND_FOR_PROCESSING_EVENT, null);
			}
		}
	}

	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionContainer) {
		this.pedValidationTableObj.setExclusionDetailsValue(exclusionContainer);
		
	}

	public void setDiagnosisName(String diagnosis) {
		this.diagnosisName = diagnosis;
	}
	
	public void setClearReferenceData(){
//		pedValidationTable.destroy(pedValidationTableObj);
//		procedureExclusionCheckTable.destroy(procedureExclusionCheckTableObj);
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
	 
	 final MessageBox showInfoMessageBox = showInfoMessageBox(SHAConstants.SUM_INSURED_CHANGE_MESSAGE_HEALTH_GAIN);
	 Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
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
 
 public MessageBox showAlertMessageBox(String message){
		
		
		final MessageBox msgBox = MessageBox
			    .createWarning()
			    .withCaptionCust("Warning")
			    .withMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
		return msgBox;
		
		
	}
}
