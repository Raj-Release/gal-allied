package com.shaic.paclaim.cashless.flp.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureTableDTO;
import com.shaic.claim.preauth.wizard.pages.ProcedureExclusionCheckTable;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.paclaim.cashless.listenertables.PAPEDValidationListenerTableForPremedical;
import com.shaic.paclaim.cashless.listenertables.PAProcedureExclusionCheckTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;

public class PAPreMedicalPreauthMedicalProcessingPage extends ViewComponent implements WizardStep<PreauthDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4045806801312795730L;
	
	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Instance<PAPEDValidationListenerTableForPremedical> pedValidationTable;
	
	@Inject
	private Instance<PAProcedureExclusionCheckTable> procedureExclusionCheckTable;
	
	@Inject
	private Instance<PAPreMedicalPreauthButtons> preMedicalPreauthButtons;

	private PAPEDValidationListenerTableForPremedical pedValidationTableObj;

	private PAProcedureExclusionCheckTable procedureExclusionCheckTableObj;
	
	private PAPreMedicalPreauthButtons preMedicalPreauthButtonsObj;
	
	private Map<String, Object> referenceData;
	
	private Boolean isPEDAvailable = false;
	
	private OptionGroup preExistingDisablities;
	
	private VerticalLayout preExDiesForm;
	
	private TextArea pedDetailsBox;

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
	}
	
	@Override
	public String getCaption() {
		return "First Level Processing";
	}
	@Override
	public Component getContent() {
		
		PAPEDValidationListenerTableForPremedical pedValidationTableInstance = pedValidationTable.get();
		pedValidationTableInstance.init("premedicalPreauth", bean);
		this.pedValidationTableObj = pedValidationTableInstance;
		
		PAProcedureExclusionCheckTable procedureExclusionCheckTableInstance = procedureExclusionCheckTable.get();
		procedureExclusionCheckTableInstance.init("Procedure Exclusion Check", false);
		this.procedureExclusionCheckTableObj = procedureExclusionCheckTableInstance;
		
		PAPreMedicalPreauthButtons preMedicalPreauthButtonsInstance = preMedicalPreauthButtons.get();
		preMedicalPreauthButtonsInstance.initView(bean);
		this.preMedicalPreauthButtonsObj = preMedicalPreauthButtonsInstance;
		
		Collection<Boolean> preExstingdisValues = new ArrayList<Boolean>(2);
		preExstingdisValues.add(true);
		preExstingdisValues.add(false);
		
		preExistingDisablities = new OptionGroup("Pre-Existing Disabilities");
		preExistingDisablities.addItems(preExstingdisValues);
		
		preExistingDisablities.setItemCaption(true, "Yes");
		preExistingDisablities.setItemCaption(false, "No");
		preExistingDisablities.setStyleName("horizontal");
		
		preExDiesForm = new VerticalLayout(new FormLayout(preExistingDisablities));
		
		preExistingDisablities.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -283060651842304500L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					pedDetailsBox = new TextArea();
					pedDetailsBox.setCaption("Details");
					pedDetailsBox.setNullRepresentation("");
					pedDetailsBox.setValue(bean.getPreauthMedicalProcessingDetails().getPedDisabilityDetails());
					preExDiesForm.addComponent(pedDetailsBox);
				} else {
					if(pedDetailsBox != null) {
						preExDiesForm.removeComponent(pedDetailsBox);
					}
					
				}
				
			}
		});

		preExistingDisablities.setValue(bean.getPreauthMedicalProcessingDetails().getPedDisability());
		preExistingDisablities.select(bean.getPreauthMedicalProcessingDetails().getPedDisability());
		
		VerticalLayout wholeVLayout = new VerticalLayout(preExDiesForm,pedValidationTableInstance, procedureExclusionCheckTableInstance, preMedicalPreauthButtonsInstance);
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
	
	private boolean validatePage() {
		Boolean hasError = false;
		String eMsg = "";
		
		if(this.pedValidationTableObj != null) {
			boolean isValid = this.pedValidationTableObj.isValid();
			if(!isValid) {
				hasError = true;
				List<String> errors = this.pedValidationTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}
		
		if(this.procedureExclusionCheckTableObj != null) {
			boolean isValid = this.procedureExclusionCheckTableObj.isValid();
			if(!isValid) {
				hasError = true;
				List<String> errors = this.procedureExclusionCheckTableObj.getErrors();
				for (String error : errors) {
					eMsg += error+"</br>";
				}
			}
		}
		
		if(this.preMedicalPreauthButtonsObj != null) {
			boolean isValid = this.preMedicalPreauthButtonsObj.isValid();
			if(!isValid) {
				hasError = true;
				List<String> errors = this.preMedicalPreauthButtonsObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}
		
		if(!hasError) {
			if(preExistingDisablities != null && preExistingDisablities.getValue() == null) {
				hasError = true;
				eMsg += "Please choose Pre Existing Disabilities" + "</br>";
			}
		}
		
	   if(hasError) {
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
		    
		    hasError = true;
		    return !hasError;
	   } else {
			try {
				this.bean.getPreMedicalPreauthMedicalDecisionDetails().setProcedureExclusionCheckTableList(this.procedureExclusionCheckTableObj.getValues());
				this.bean.getPreauthMedicalProcessingDetails().setPedDisability((Boolean)preExistingDisablities.getValue());
				this.bean.getPreauthMedicalProcessingDetails().setPedDisabilityDetails(pedDetailsBox != null  ? pedDetailsBox.getValue() : null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		   return true;
	   }
	}
	
	public void generateButtonFields(String buttonSelected){
		if(buttonSelected.equalsIgnoreCase("query")) {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS);
			this.bean.setStageKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
			preMedicalPreauthButtonsObj.generateFieldsForQuery();
		} else if(buttonSelected.equalsIgnoreCase("suggestion")) {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS);
			this.bean.setStageKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
			preMedicalPreauthButtonsObj.generateFieldsForSuggesRejection(false);
		} else {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS);
			this.bean.setStageKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
			preMedicalPreauthButtonsObj.generateFieldsForSendForProcessing(false);
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
				procedureDTO.setCopayPercentage(0d);
				if(procedureDTO.getCopayPercentage() != null) {
					SelectValue selectValue = new SelectValue();
					selectValue.setId(procedureDTO.getCopayPercentage().longValue());
					selectValue.setValue(String.valueOf(procedureDTO.getCopayPercentage().intValue()));
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
				procedureDTO = StarCommonUtils.getDefaultProcedureDTO(procedureDTO);
				this.procedureExclusionCheckTableObj.addBeanToList(procedureDTO);
			}
		}
		
//		if(this.bean.getStatusKey() != null) {
//			if(this.bean.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)) {
//				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_QUERY_EVENT, null);
//			} else if(this.bean.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)) {
//				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_SUGGEST_REJECTION_EVENT, null);
//			} else if(this.bean.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)){
//				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_SEND_FOR_PROCESSING_EVENT, null);
//			}
//		}
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
}
