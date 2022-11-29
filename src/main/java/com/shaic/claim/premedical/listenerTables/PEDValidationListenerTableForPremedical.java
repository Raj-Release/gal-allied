package com.shaic.claim.premedical.listenerTables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.premedicalprocessing.ClaimRequestPremedicalProcessingPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingPagePresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PEDValidationListenerTableForPremedical extends ViewComponent{
	private static final long serialVersionUID = 1006202333817442348L;
	
	private Map<PedDetailsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<PedDetailsTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<PedDetailsTableDTO> data = new BeanItemContainer<PedDetailsTableDTO>(PedDetailsTableDTO.class);

	private Table table;	
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private BeanItemContainer<ExclusionDetails> exclusionDetails;
	
	private PreauthDTO bean;
	
	/*private ConfirmDialog dialog;*/
	
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
		
		table.setVisibleColumns(new Object[] { "diagnosisName", "pedCode", "pedName",/* "policyAgeing", */"pedExclusionImpactOnDiagnosis", "exclusionDetails", "copay", "remarks" });

		table.setColumnHeader("diagnosisName", "Diagnosis");
		table.setColumnHeader("pedCode", "PED Code");
		table.setColumnHeader("pedName", "PED Name");
		table.setColumnHeader("policyAgeing", "Policy Ageing");
		table.setColumnHeader("pedExclusionImpactOnDiagnosis", "PED / Exclusion Impact on Diagnosis");
		table.setColumnHeader("exclusionDetails", "Exclusion Details");
		table.setColumnHeader("copay", "Co-Pay");
		table.setColumnHeader("remarks", "Remarks");
		if(bean.getNewIntimationDTO().getIsPaayasPolicy()) {
		table.setEditable(false);
		}else if(bean.getNewIntimationDTO().getIsTataPolicy()) {
		table.setEditable(false);	
		}else{
		table.setEditable(true);
		}
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
				addDiagnosisImpactValues(box);
//				final ComboBox exclusionCombo = (ComboBox) tableRow.get("exclusionDetails");
				box.setData(diagnosisPEDDTO);
				/*if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getCode() && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)) {
					box.setEnabled(true);	
				}else{
					box.setEnabled(false);
				}*/
				box.setEnabled(false);
//				addDiagnosisImpactListener(box, exclusionCombo);
//				box.setEnabled(true);
//				if("premedicalEnhancement".equalsIgnoreCase(presenterString)) {
//					box.setEnabled(Boolean.TRUE);
//				}
				
				return box;
			} else if("exclusionDetails".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("100px");
				tableRow.put("exclusionDetails", box);
				/*if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getCode() && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)) {
					box.setEnabled(true);	
				}else{
					box.setEnabled(false);
				}*/
				box.setEnabled(false);
//				box.setEnabled(true);
//				if("premedicalEnhancement".equalsIgnoreCase(presenterString)) {
//					box.setEnabled(Boolean.TRUE);
//				}
//				if(diagnosisPEDDTO.getPedExclusionImpactOnDiagnosis() != null) {
//					addExclusionDetails(diagnosisPEDDTO.getPedExclusionImpactOnDiagnosis().getId(), box, diagnosisPEDDTO.getExclusionDetails(), diagnosisPEDDTO);
//				}
				return box;
			} else if("copay".equals(propertyId)) {
				if(diagnosisPEDDTO.getIsShowingCopay()) {
					ComboBox box = new ComboBox();
					box.setWidth("100px");
					tableRow.put("copay", box);
					box.setEnabled(true);
					if("premedicalEnhancement".equalsIgnoreCase(presenterString)) {
						box.setEnabled(Boolean.TRUE);
					}
					
					addCopayValues(box, diagnosisPEDDTO);
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
					field.setEnabled(Boolean.TRUE);
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
					ComboBox copayComboBox = (ComboBox) hashMap.get("copay");
					BeanItemContainer<SelectValue> coapyValues = (BeanItemContainer<SelectValue>) referenceData
							.get("copay");
					
					if (pedDetailsDTO != null) {
						
						if(pedDetailsDTO.getPedExclusionImpactOnDiagnosis() != null && comboBox != null) {
							addExclusionDetails(pedDetailsDTO.getPedExclusionImpactOnDiagnosis().getId(), comboBox, pedDetailsDTO.getExclusionDetails(), pedDetailsDTO);
							
							if(ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
								if(pedDetailsDTO.getPedExclusionImpactOnDiagnosis().getId().equals(ReferenceTable.RELATED_TO_PED)){
								
								/*copayCombo.setContainerDataSource(coapyValues);
								copayCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
								copayCombo.setItemCaptionPropertyId("value");	*/
								if(copayComboBox != null && copayComboBox.getValue() == null){
									List<SelectValue> itemIds = coapyValues.getItemIds();
									for (SelectValue selectValue : itemIds) {
										if(selectValue.getValue().equalsIgnoreCase("50")){
											pedDetailsDTO.setCopay(selectValue);
											copayComboBox.setValue(selectValue);
										}
									}
								}
							  }
								alertMessageForPed();
							}
							
						}
						
						
					}
				}
			});
		}
	}
	
	private void addDiagnosisImpactValues(ComboBox diagnosisImpact) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> diagnosis = (BeanItemContainer<SelectValue>) referenceData
				.get("pedExclusionImpactOnDiagnosis");
		diagnosisImpact.setContainerDataSource(diagnosis);
		diagnosisImpact.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisImpact.setItemCaptionPropertyId("value");
		
	}
	
	private void addCopayValues(ComboBox copayCombo, PedDetailsTableDTO pedDetailsDTO) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> coapyValues = (BeanItemContainer<SelectValue>) referenceData
				.get("copay");
		
		if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			//SHAUtils.setDefaultCopayValue(bean);
		}
		
		if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
    			(ReferenceTable.setMaxCopayProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
    		if((bean.getPreauthDataExtractionDetails().getSection() != null &&
    				null != bean.getPreauthDataExtractionDetails().getSection().getId() &&
    				(ReferenceTable.POL_SECTION_2.equals(bean.getPreauthDataExtractionDetails().getSection().getId()))) ||
    				(ReferenceTable.LUMPSUM_SECTION_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue()) &&
    						ReferenceTable.LUMPSUM_COVER_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue()))){
    			List<SelectValue> itemIds = coapyValues.getItemIds();
    			if(! itemIds.isEmpty()){
    				SelectValue selectValue = itemIds.get(0);
    				coapyValues.removeAllItems();
    				coapyValues.addBean(selectValue);
    				pedDetailsDTO.setCopay(selectValue);
    			}
    		}
		}
		
		/*if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() && 
				!(ReferenceTable.getDefaultCopayNotApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			
			coapyValues.sort(new Object[] {"value"}, new boolean[] {false});
		}*/
//		Below Code for SCRC REVISED - CR20181302
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null &&
				(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER))){
			List<SelectValue> itemIds = coapyValues.getItemIds();
			if(itemIds != null) {
				if(bean.getNewIntimationDTO().getPolicy().getTotalSumInsured() != null && bean.getNewIntimationDTO().getPolicy().getTotalSumInsured() <= 1000000){
					List<SelectValue> selectVal = new ArrayList<SelectValue>();
					if(itemIds.size() > 2) {
					selectVal.add(itemIds.get(0));
					selectVal.add(itemIds.get(1));
					selectVal.add(itemIds.get(2));
					} else {
						selectVal.add(itemIds.get(0));
						selectVal.add(itemIds.get(1));
					}
					coapyValues.removeAllItems();
					coapyValues.addAll(selectVal);
				} else {
					List<SelectValue> selectVal = new ArrayList<SelectValue>();
					if(itemIds.size() > 1) {
						selectVal.add(itemIds.get(0));
						selectVal.add(itemIds.get(1)); 
					} else {
						selectVal.add(itemIds.get(0));
					}
					coapyValues.removeAllItems();
					coapyValues.addAll(selectVal);
				}
			}
		}
		
		copayCombo.setContainerDataSource(coapyValues);
		copayCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		copayCombo.setItemCaptionPropertyId("value");
		
		List<SelectValue> itemIds = coapyValues.getItemIds();
		
		if (ReferenceTable.getGMCProductList().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			if (pedDetailsDTO.getCopay() != null) {
				copayCombo.setValue(pedDetailsDTO.getCopay());
				copayCombo.setNullSelectionAllowed(false);
			} else if (itemIds != null && !itemIds.isEmpty()) {
				SelectValue selectValue = itemIds.get(itemIds.size() - 1);
				copayCombo.setValue(selectValue);
				copayCombo.setNullSelectionAllowed(false);
				pedDetailsDTO.setCopay(selectValue);
			}
		} else {
			if (pedDetailsDTO.getCopay() != null) {
				copayCombo.setValue(pedDetailsDTO.getCopay());
				copayCombo.setNullSelectionAllowed(false);
			} else if (itemIds != null && !itemIds.isEmpty()) {
				SelectValue selectValue = itemIds.get(0);
				copayCombo.setValue(selectValue);
				copayCombo.setNullSelectionAllowed(false);
				pedDetailsDTO.setCopay(selectValue);
			}
		}
		/*if(pedDetailsDTO.getCopay()!=null){
	    	copayCombo.setValue(pedDetailsDTO.getCopay());
	    	copayCombo.setNullSelectionAllowed(false);
	    }else if(itemIds != null && !itemIds.isEmpty()){
	    	SelectValue selectValue = itemIds.get(0);
	    	copayCombo.setValue(selectValue);
	    	copayCombo.setNullSelectionAllowed(false);
	    	pedDetailsDTO.setCopay(selectValue);
	    }*/

		Integer index = 100;
		/*if (bean.getIsDefaultCopay() && pedDetailsDTO.getCopay() == null) {
			String string = bean.getDefaultCopayStr();
			for (SelectValue values : coapyValues.getItemIds()) {
					if(values.getValue() != null && string != null && values.getValue().toString().equalsIgnoreCase(string)) {
						pedDetailsDTO.setCopay(values);
						break;
				}
			}
		}*/
		
		if(("Y").equalsIgnoreCase(bean.getNewIntimationDTO().getCopayFlag())) {
			SelectValue selectValue = itemIds.get(0);
			selectValue= itemIds.get(itemIds.size()-1);
			 copayCombo.setValue(selectValue);
			 copayCombo.setNullSelectionAllowed(false);
				pedDetailsDTO.setCopay(selectValue);
		}
//		Below condition  as per CR2019007 copay editable for GMC product
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			if(pedDetailsDTO.getCopay()!=null && pedDetailsDTO.getCopay().getId() > 0){
		    	copayCombo.setValue(pedDetailsDTO.getCopay());
		    	copayCombo.setNullSelectionAllowed(false);
			} else {
				pedDetailsDTO.setCopay(null);
			}
		}
		
	}
	
	private void addExclusionDetails(Long impactId, ComboBox comboBox,
			SelectValue exclusionDetailsValue, PedDetailsTableDTO dto) {
		if("premedicalPreauth".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PreMedicalPreauthWizardPresenter.GET_EXCLUSION_DETAILS, impactId);
		} else if("premedicalEnhancement".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PremedicalEnhancementWizardPresenter.GET_EXCLUSION_DETAILS, impactId);
		} else if("preauthEnhancement".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PreauthEnhancemetWizardPresenter.GET_EXCLUSION_DETAILS, impactId);
		} else if(SHAConstants.MEDICAL_APPROVAL_MEDICAL_PROCESSING.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.GET_EXCLUSION_DETAILS, impactId);
		} else if(SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_MEDICAL_PROCESSING.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(ClaimRequestPremedicalProcessingPagePresenter.GET_EXCLUSION_DETAILS, impactId);
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
			/*if( null == bean.getPedExclusionImpactOnDiagnosis()) {
				hasError = true;
				errorMessages.add("Please Select PED Exclusion Impact On Diagnosis.");
			}
			
			if(null == bean.getExclusionDetails()) {
				hasError = true;
				errorMessages.add("Please Select Exclusion Details.");
			}*/
			if(!this.bean.getNewIntimationDTO().getIsPaayasPolicy() && !this.bean.getNewIntimationDTO().getIsTataPolicy()){
			if(bean.getIsShowingCopay() && bean.getCopay() == null) {
				hasError = true;
			
//				errorMessages.add("Please Select PED Validation Copay.");
				errorMessages.add("Please Select PED copay if applicable.");
			}
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
	
	public void alertMessageForPed() {
		
		/*Label successLabel = new Label("<b style = 'color: red;'> Select PED copay if applicable </b>", ContentMode.HTML);
		
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);*/
		
		/*if(dialog != null){
			dialog.close();
		}
		
		dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("Select PED copay if applicable </b>", buttonsNamewithType);
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
	
	public boolean validateCopay()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<PedDetailsTableDTO> itemIds = (Collection<PedDetailsTableDTO>) table.getItemIds();
		for (PedDetailsTableDTO bean : itemIds) {
			
			if(bean.getIsShowingCopay() != null &&  bean.getIsShowingCopay() && bean.getCopay() == null) {
				hasError = true;
				errorMessages.add("Please Select PED copay if applicable.");
			}
			
			if (errorMessages.size() > 0) {
				hasError = true;
			}
		}
		return !hasError;
	}

}
