package com.shaic.paclaim.cashless.listenertables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingPagePresenter;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancemetWizardPresenter;
import com.shaic.paclaim.cashless.fle.wizard.wizardfiles.PAPremedicalEnhancementWizardPresenter;
import com.shaic.paclaim.cashless.flp.wizard.wizardFiles.PAPreMedicalPreauthWizardPresenter;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.premedicalprocessing.PAHealthClaimRequestPremedicalProcessingPagePresenter;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PAPEDValidationListenerTableForPremedical extends ViewComponent{
	private static final long serialVersionUID = 1006202333817442348L;
	
	private Map<PedDetailsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<PedDetailsTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<PedDetailsTableDTO> data = new BeanItemContainer<PedDetailsTableDTO>(PedDetailsTableDTO.class);

	private Table table;	
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private BeanItemContainer<ExclusionDetails> exclusionDetails;
	
	private PreauthDTO bean;
	
	private String presenterString;
	
	public void init(String presenterString, PreauthDTO bean) {
		this.presenterString = presenterString;
		this.bean = bean;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		VerticalLayout layout = new VerticalLayout();
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		layout.setMargin(true);
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	protected void manageListeners() {

		for (PedDetailsTableDTO diagnosisPEDDetailsDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(diagnosisPEDDetailsDTO);

			final ComboBox diagnosisImpactCombo = (ComboBox) combos.get("pedExclusionImpactOnDiagnosis");
			final ComboBox exclusionCombo = (ComboBox) combos.get("exclusionDetails");
			addDiagnosisImpactListener(diagnosisImpactCombo, exclusionCombo);
			
			if(diagnosisPEDDetailsDTO.getPedExclusionImpactOnDiagnosis() != null) {
				addExclusionDetails(diagnosisPEDDetailsDTO.getPedExclusionImpactOnDiagnosis().getId(), exclusionCombo, diagnosisPEDDetailsDTO.getExclusionDetails(), diagnosisPEDDetailsDTO);
			}
		}
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("PED Validation", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "diagnosisName", "pedCode", "pedName",/* "policyAgeing",*/ "pedExclusionImpactOnDiagnosis", "exclusionDetails", "copay", "remarks" });

		table.setColumnHeader("diagnosisName", "Diagnosis");
		table.setColumnHeader("pedCode", "PED Code");
		table.setColumnHeader("pedName", "PED Name");
		table.setColumnHeader("policyAgeing", "Policy Ageing");
		table.setColumnHeader("pedExclusionImpactOnDiagnosis", "PED / Exclusion Impact on Diagnosis");
		table.setColumnHeader("exclusionDetails", "Exclusion Details");
		table.setColumnHeader("copay", "Co-Pay");
		table.setColumnHeader("remarks", "Remarks");
		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			PedDetailsTableDTO diagnosisPEDDTO = (PedDetailsTableDTO) itemId;
			Boolean isEnabled = (null != diagnosisPEDDTO && null != diagnosisPEDDTO.getRecTypeFlag() && diagnosisPEDDTO.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c") ) ? false: true;
						
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(diagnosisPEDDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(diagnosisPEDDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(diagnosisPEDDTO);
			}
			
			if("diagnosisName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("diagnosisName", field);
				return field;
			} else if ("pedCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("pedCode", field);
				return field;
			}  
			else if ("pedName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("pedName", field);
				return field;
			} else if ("policyAgeing".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("pedName", field);
				return field;
			} else if("pedExclusionImpactOnDiagnosis".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("150px");
				tableRow.put("pedExclusionImpactOnDiagnosis", box);
				box.setNullSelectionAllowed(false);
				addDiagnosisImpactValues(box, diagnosisPEDDTO);
				final ComboBox exclusionCombo = (ComboBox) tableRow.get("exclusionDetails");
				box.setData(diagnosisPEDDTO);
				addDiagnosisImpactListener(box, exclusionCombo);
				box.setEnabled(true);
				if("premedicalEnhancement".equalsIgnoreCase(presenterString)) {
					box.setEnabled(isEnabled);
				}
				box.setEnabled(false);
				return box;
			} else if("exclusionDetails".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("100px");
				tableRow.put("exclusionDetails", box);
				box.setEnabled(true);
				if("premedicalEnhancement".equalsIgnoreCase(presenterString)) {
					box.setEnabled(isEnabled);
				}
				if(diagnosisPEDDTO.getPedExclusionImpactOnDiagnosis() != null) {
					addExclusionDetails(diagnosisPEDDTO.getPedExclusionImpactOnDiagnosis().getId(), box, diagnosisPEDDTO.getExclusionDetails(), diagnosisPEDDTO);
				}
				box.setEnabled(false);
				return box;
			} else if("copay".equals(propertyId)) {
				if(diagnosisPEDDTO.getIsShowingCopay()) {
					ComboBox box = new ComboBox();
					box.setWidth("100px");
					tableRow.put("copay", box);
					box.setEnabled(true);
					if("premedicalEnhancement".equalsIgnoreCase(presenterString)) {
						box.setEnabled(isEnabled);
					}
					
					addCopayValues(box, diagnosisPEDDTO);
					box.setEnabled(false);
					return box;
				} 
				return null;
			} else if("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setMaxLength(100);
				field.setNullRepresentation("");
				tableRow.put("remarks", field);
				field.setEnabled(true);
				if("premedicalEnhancement".equalsIgnoreCase(presenterString)) {
					field.setEnabled(isEnabled);
				}
				return field;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				field.setEnabled(true);
				if("premedicalEnhancement".equalsIgnoreCase(presenterString)) {
					field.setEnabled(isEnabled);
				}
				return field;
			}
		}

		
	}
	
	private void addDiagnosisImpactListener(ComboBox box,
			ComboBox exclusionCombo) {
		if (box != null) {
			box.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					PedDetailsTableDTO pedDetailsDTO = (PedDetailsTableDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(pedDetailsDTO);
					ComboBox comboBox = (ComboBox) hashMap.get("exclusionDetails");
					if (pedDetailsDTO != null) {
						if(pedDetailsDTO.getPedExclusionImpactOnDiagnosis() != null && comboBox != null) {
							addExclusionDetails(pedDetailsDTO.getPedExclusionImpactOnDiagnosis().getId(), comboBox, pedDetailsDTO.getExclusionDetails(), pedDetailsDTO);
						}
						
					}
				}
			});
		}
	}
	
	private void addDiagnosisImpactValues(ComboBox diagnosisImpact, PedDetailsTableDTO pedDetailsDTO) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> diagnosis = (BeanItemContainer<SelectValue>) referenceData
				.get("pedExclusionImpactOnDiagnosis");
		diagnosisImpact.setContainerDataSource(diagnosis);
		diagnosisImpact.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisImpact.setItemCaptionPropertyId("value");
		
		List<SelectValue> itemIds = diagnosis.getItemIds();
		for (SelectValue selectValue : itemIds) {
			if(selectValue.getValue() != null && selectValue.getValue().equalsIgnoreCase(SHAConstants.IMPACT_ON_DIAGNOSIS_DEFAULT_STRING)) {
				pedDetailsDTO.setPedExclusionImpactOnDiagnosis(selectValue);
				diagnosisImpact.setValue(selectValue);
				break;
			}
		}
	}
	
	private void addCopayValues(ComboBox copayCombo, PedDetailsTableDTO pedDetailsDTO) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> coapyValues = (BeanItemContainer<SelectValue>) referenceData
				.get("copay");
		copayCombo.setContainerDataSource(coapyValues);
		copayCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		copayCombo.setItemCaptionPropertyId("value");
		
		Integer index = 100;
		if (true) {
			String string = "0";
			for (SelectValue values : coapyValues.getItemIds()) {
					if(values.getValue() != null && string != null && values.getValue().toString().equalsIgnoreCase(string)) {
						pedDetailsDTO.setCopay(values);
						copayCombo.setValue(values);
						break;
				}
			}
		}
	
	}
	
	private void addExclusionDetails(Long impactId, ComboBox comboBox,
			SelectValue exclusionDetailsValue, PedDetailsTableDTO dto) {
		if("premedicalPreauth".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPreMedicalPreauthWizardPresenter.GET_EXCLUSION_DETAILS, impactId);
		} else if("premedicalEnhancement".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPremedicalEnhancementWizardPresenter.GET_EXCLUSION_DETAILS, impactId);
		} else if("preauthEnhancement".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPreauthEnhancemetWizardPresenter.GET_EXCLUSION_DETAILS, impactId);
		} else if(SHAConstants.MEDICAL_APPROVAL_MEDICAL_PROCESSING.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.GET_EXCLUSION_DETAILS, impactId);
		} else if(SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_MEDICAL_PROCESSING.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAHealthClaimRequestPremedicalProcessingPagePresenter.GET_EXCLUSION_DETAILS, impactId);
		}
		dto.setExclusionAllDetails(exclusionDetails.getItemIds());
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(exclusionDetails != null) {
			List<ExclusionDetails> itemIds = exclusionDetails.getItemIds();
			List<SelectValue> selectValues = new ArrayList<SelectValue>();
			for (ExclusionDetails exclusionDetails : itemIds) {
				SelectValue value = new SelectValue();
				value.setId(exclusionDetails.getKey());
				value.setValue(exclusionDetails.getExclusion());
				if(value.getValue().equalsIgnoreCase(SHAConstants.EXCLUSION_DETAILS_DEFAULT_STRING)) {
					dto.setExclusionDetails(value);
					exclusionDetailsValue = value;
				}
				
				selectValues.add(value);
			}
			container.addAll(selectValues);
		}
		comboBox.setContainerDataSource(container);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
		if(exclusionDetailsValue != null) {
			comboBox.setValue(exclusionDetailsValue);
		}
	}
	
	public void setExclusionDetailsValue(BeanItemContainer<ExclusionDetails> values) {
		this.exclusionDetails = values;
	}
	
	public List<PedDetailsTableDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<PedDetailsTableDTO> itemIds = (List<PedDetailsTableDTO>) this.table.getItemIds() ;
    	return itemIds;
    }
    
    public void addBeanToList(PedDetailsTableDTO diagnosisPEDDTO) {
    	data.addItem(diagnosisPEDDTO);
    	manageListeners();
    }
    
    public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
    
    public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<PedDetailsTableDTO> itemIds = (Collection<PedDetailsTableDTO>) table.getItemIds();
		for (PedDetailsTableDTO bean : itemIds) {
			Set<ConstraintViolation<PedDetailsTableDTO>> validate = validator.validate(bean);

			//if( !("premedicalEnhancement".equalsIgnoreCase(presenterString) || "preauthEnhancement".equalsIgnoreCase(presenterString)) &&  null == bean.getPedExclusionImpactOnDiagnosis()) {
			if( null == bean.getPedExclusionImpactOnDiagnosis()) {
				hasError = true;
				errorMessages.add("Please Select PED Exclusion Impact On Diagnosis.");
			}
			
			if(null == bean.getExclusionDetails()) {
				hasError = true;
				errorMessages.add("Please Select Exclusion Details.");
			}
			
			if(bean.getIsShowingCopay() && bean.getCopay() == null) {
				hasError = true;
				errorMessages.add("Please Select PED Validation Copay.");
			}
			
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<PedDetailsTableDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}
		return !hasError;
	}
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	

}
