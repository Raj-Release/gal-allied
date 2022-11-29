package com.shaic.paclaim.cashless.fle.wizard.pages;

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
import com.shaic.claim.preauth.wizard.pages.ProcedureExclusionCheckTable;
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

public class PAPremedicalEnhancementMedicalProcessingPage  extends ViewComponent implements WizardStep<PreauthDTO>{
	private static final long serialVersionUID = 3786938368639439195L;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Instance<PAPEDValidationListenerTableForPremedical> pedValidationTable;
	
	@Inject
	private Instance<PAProcedureExclusionCheckTable> procedureExclusionCheckTable;
	
	@Inject
	private Instance<PAPremedicalEnhancementButtons> preMedicalPreauthButtons;

	private PAPEDValidationListenerTableForPremedical pedValidationTableObj;

	private PAProcedureExclusionCheckTable procedureExclusionCheckTableObj;
	
	private PAPremedicalEnhancementButtons preMedicalPreauthButtonsObj;
	
	private Map<String, Object> referenceData;
	
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
		pedValidationTableInstance.init("premedicalEnhancement", bean);
		this.pedValidationTableObj = pedValidationTableInstance;
		
		PAProcedureExclusionCheckTable procedureExclusionCheckTableInstance = procedureExclusionCheckTable.get();
		procedureExclusionCheckTableInstance.init("Procedure Exclusion Check", false);
		this.procedureExclusionCheckTableObj = procedureExclusionCheckTableInstance;
		
		PAPremedicalEnhancementButtons preMedicalPreauthButtonsInstance = preMedicalPreauthButtons.get();
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
		return validatePage();
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
					eMsg += error+ "</br>";
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
//				this.bean.getPreMedicalPreauthMedicalDecisionDetails().setPedValidationTableList(this.pedValidationTableObj.getValues());
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
		this.bean.setStageKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
		if(buttonSelected.equalsIgnoreCase("query")) {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS);
			preMedicalPreauthButtonsObj.generateFieldsForQuery();
		} else if(buttonSelected.equalsIgnoreCase("suggestion")) {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS);
			preMedicalPreauthButtonsObj.generateFieldsForSuggesRejection(true);
			//preMedicalPreauthButtonsObj.generateFieldsForSuggesRejection(false);
		} else {
			this.bean.setStatusKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS);
			//preMedicalPreauthButtonsObj.generateFieldsForSendForProcessing(false);
			preMedicalPreauthButtonsObj.generateFieldsForSendForProcessing(true);
		}
		
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
				procedureDTO.setStatusFlag(true);
				if(procedureDTO.getRecTypeFlag() != null && procedureDTO.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c")) {
					procedureDTO.setStatusFlag(false);
				}
				procedureDTO = StarCommonUtils.getDefaultProcedureDTO(procedureDTO);
				this.procedureExclusionCheckTableObj.addBeanToList(procedureDTO);
			}
		}
	}

	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionContainer) {
		this.pedValidationTableObj.setExclusionDetailsValue(exclusionContainer);
	}	
}
