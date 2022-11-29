package com.shaic.claim.preauth.wizard.listenerTables;

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
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button;
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

public class PEDValidationListenerTableForPreauth extends ViewComponent{
	private static final long serialVersionUID = 1006202333817442348L;

	private Map<PedDetailsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<PedDetailsTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<PedDetailsTableDTO> data = new BeanItemContainer<PedDetailsTableDTO>(PedDetailsTableDTO.class);

	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private BeanItemContainer<ExclusionDetails> exclusionDetails;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private PreauthDTO bean;
	
	private ConfirmDialog dialog;
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		VerticalLayout layout = new VerticalLayout();
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);
		layout.setMargin(true);

		setCompositionRoot(layout);
	}
	
	protected void manageListeners() {

		for (PedDetailsTableDTO pedValidationTableDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(pedValidationTableDTO);

			final ComboBox diagnosisImpactCombo = (ComboBox) combos.get("pedExclusionImpactOnDiagnosis");
			final ComboBox exclusionCombo = (ComboBox) combos.get("exclusionDetails");
			addDiagnosisImpactListener(diagnosisImpactCombo, exclusionCombo);
			
			if(pedValidationTableDTO.getPedExclusionImpactOnDiagnosis() != null) {
				addExclusionDetails(pedValidationTableDTO.getPedExclusionImpactOnDiagnosis().getId(), exclusionCombo, pedValidationTableDTO.getExclusionDetails(), pedValidationTableDTO);
			}
		}
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("PED Validation", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "diagnosisName", "pedCode", "pedName",/* "policyAgeing",*/ "pedExclusionImpactOnDiagnosis", "exclusionDetails", "copay",  "remarks"});

		table.setColumnHeader("diagnosisName", "Diagnosis");
		table.setColumnHeader("pedCode", "PED Code");
		table.setColumnHeader("pedName", "PED Name");
		table.setColumnHeader("policyAgeing", "Policy Ageing");
		table.setColumnHeader("pedExclusionImpactOnDiagnosis", "PED Impact On Diagnosis");
		table.setColumnHeader("exclusionDetails", "Exclusion Details");
		table.setColumnHeader("copay", "Co-Pay");
		table.setColumnHeader("remarks", "Remarks");
		if(bean.getNewIntimationDTO().getIsPaayasPolicy()) {
			table.setEnabled(false);
		}else if(bean.getNewIntimationDTO().getIsTataPolicy()) {
			table.setEnabled(false);
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
			PedDetailsTableDTO pedValidation = (PedDetailsTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			Boolean isEnabled = (null != pedValidation && null != pedValidation.getEnableOrDisable() ) ? pedValidation.getEnableOrDisable(): true;

			if (tableItem.get(pedValidation) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(pedValidation, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(pedValidation);
			}
			
			if("diagnosisName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("diagnosisName", field);
				return field;
			} else if ("pedName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("pedName", field);
				return field;
			} else if ("pedCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("pedCode", field);
				return field;
			}  else if("copay".equals(propertyId)) {
				if(pedValidation.getIsShowingCopay()) {
					ComboBox box = new ComboBox();
					box.setWidth("100px");
					tableRow.put("copay", box);
					box.setEnabled(isEnabled);
					addCopayValues(box,pedValidation);
					return box;
				} 
				return null;
			}
			else if ("policyAgeing".equals(propertyId)) {
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
				addDiagnosisImpactValues(box);
				final ComboBox exclusionCombo = (ComboBox) tableRow.get("exclusionDetails");
				box.setData(pedValidation);

				box.setEnabled(false);
				
				/*if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getCode() && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)) {
					box.setEnabled(true);	
				}else{
					box.setEnabled(false);
				}*/
				
				//				addDiagnosisImpactListener(box, exclusionCombo);	// CR R20181300
				return box;
			} else if("exclusionDetails".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("100px");
				box.setEnabled(false);
				/*if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getCode() && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)) {
					box.setEnabled(true);	
				}else{
					box.setEnabled(false);
				}*/
				tableRow.put("exclusionDetails", box);
				return box;
			} else if("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setMaxLength(100);
				field.setNullRepresentation("");
				tableRow.put("remarks", field);
				return field;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	private void addCopayValues(ComboBox copayCombo ,PedDetailsTableDTO pedDetailsDTO ) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> coapyValues = (BeanItemContainer<SelectValue>) referenceData
				.get("copay");
		
		if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			//SHAUtils.setDefaultCopayValue(bean);
		}
		
		if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
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
    				//pedDetailsDTO.setCopay(selectValue);
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
					if(itemIds.size() > 2){
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
			} 
			if (itemIds != null && !itemIds.isEmpty()) {
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
		
		if(pedDetailsDTO.getCopay()!=null){
	    	copayCombo.setValue(pedDetailsDTO.getCopay());
	    	copayCombo.setNullSelectionAllowed(false);
		} else if (itemIds != null && !itemIds.isEmpty()) {
			SelectValue selectValue = itemIds.get(0);
			if(("Y").equalsIgnoreCase(bean.getNewIntimationDTO().getCopayFlag())) {
				 selectValue = itemIds.get(itemIds.size()-1);
			}
			copayCombo.setValue(selectValue);
			copayCombo.setNullSelectionAllowed(false);
			pedDetailsDTO.setCopay(selectValue);
		}
		if(("Y").equalsIgnoreCase(bean.getNewIntimationDTO().getCopayFlag())) {
			SelectValue selectValue = itemIds.get(0);
			 selectValue = itemIds.get(itemIds.size()-1);
			 copayCombo.setValue(selectValue);
				copayCombo.setNullSelectionAllowed(false);
				pedDetailsDTO.setCopay(selectValue);
		}
//		Below condition  as per CR2019007 copay editable for GMC product
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			if(pedDetailsDTO.getCopay()!=null){
		    	copayCombo.setValue(pedDetailsDTO.getCopay());
		    	copayCombo.setNullSelectionAllowed(false);
			} else {
				pedDetailsDTO.setCopay(null);
			}
		}
		
		if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&    			
    			ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			//IMSSUPPOR-27615
			if(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null){
			if(!(ReferenceTable.LUMPSUM_SECTION_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue()) &&
					ReferenceTable.LUMPSUM_COVER_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue()))){
				
	/*	if (bean.getIsDefaultCopay()) {
			String string = bean.getDefaultCopayStr();
			for (SelectValue values : coapyValues.getItemIds()) {
					if(values.getValue() != null && string != null && values.getValue().toString().equalsIgnoreCase(string)) {
						pedDetailsDTO.setCopay(values);
						break;
					}
				}
			}*/
		}
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
					PedDetailsTableDTO pedValidationTableDTO = (PedDetailsTableDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(pedValidationTableDTO);
					ComboBox comboBox = (ComboBox) hashMap.get("exclusionDetails");
					ComboBox copayComboBox = (ComboBox) hashMap.get("copay");
					BeanItemContainer<SelectValue> coapyValues = (BeanItemContainer<SelectValue>) referenceData
							.get("copay");
					if (pedValidationTableDTO != null) {
						if(pedValidationTableDTO.getPedExclusionImpactOnDiagnosis() != null && comboBox != null) {
							addExclusionDetails(pedValidationTableDTO.getPedExclusionImpactOnDiagnosis().getId(), comboBox, pedValidationTableDTO.getExclusionDetails(), pedValidationTableDTO);
							if(ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
								if(pedValidationTableDTO.getPedExclusionImpactOnDiagnosis().getId().equals(ReferenceTable.RELATED_TO_PED)){
								if(copayComboBox != null && copayComboBox.getValue() == null){
									List<SelectValue> itemIds = coapyValues.getItemIds();
									for (SelectValue selectValue : itemIds) {
										if(selectValue.getValue().equalsIgnoreCase("50")){
											pedValidationTableDTO.setCopay(selectValue);
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
	
	@SuppressWarnings("unused")
	private void addDiagnosisImpactValues(ComboBox diagnosisImpact) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> diagnosis = (BeanItemContainer<SelectValue>) referenceData
				.get("pedExclusionImpactOnDiagnosis");
		diagnosisImpact.setContainerDataSource(diagnosis);
		diagnosisImpact.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisImpact.setItemCaptionPropertyId("value");
		
	}
	
	private void addExclusionDetails(Long impactId, ComboBox comboBox,
			SelectValue exclusionDetailsValue, PedDetailsTableDTO dto) {
		fireViewEvent(PreauthWizardPresenter.GET_EXCLUSION_DETAILS, impactId);
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
    
    public void addBeanToList(PedDetailsTableDTO pedValidationDTO) {
    	data.addItem(pedValidationDTO);
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

//			if(null == bean.getPedExclusionImpactOnDiagnosis()) {
//				hasError = true;
//				errorMessages.add("Please Select PED Exclusion Impact On Diagnosis.");
//			}
//			
//			if(null == bean.getExclusionDetails()) {
//				hasError = true;
//				errorMessages.add("Please Select Exclusion Details.");
//			}
			//IMSSUPPOR-31469
			if(!this.bean.getNewIntimationDTO().getIsPaayasPolicy() && !this.bean.getNewIntimationDTO().getIsTataPolicy()){
				if(null == bean.getCopay()) {
					hasError = true;
//				errorMessages.add("Please Select Copay value.");
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
		
		Label successLabel = new Label("<b style = 'color: red;'> Select PED copay if applicable </b>", ContentMode.HTML);
		
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		if(dialog != null){
			dialog.close();
		}
		
		dialog = new ConfirmDialog();
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
			//IMSSUPPOR-31469
			if(!this.bean.getNewIntimationDTO().getIsPaayasPolicy() && !this.bean.getNewIntimationDTO().getIsTataPolicy()){
				if(bean.getIsShowingCopay() != null &&  bean.getIsShowingCopay() && bean.getCopay() == null) {
					hasError = true;
					errorMessages.add("Please Select PED copay if applicable.");
				}
			}
			
			if (errorMessages.size() > 0) {
				hasError = true;
			}
		}
		return !hasError;
	}


}
