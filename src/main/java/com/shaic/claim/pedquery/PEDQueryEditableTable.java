package com.shaic.claim.pedquery;

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
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.fields.dto.SelectValue;
//import com.shaic.claim.pedrequest.view.NewInitiatePedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.NewInitiatePedEndorsementDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PEDQueryEditableTable extends ViewComponent {
	
private static final long serialVersionUID = 7802397137014194525L;
	
	/**
	 * Due to vaadin limitation, we are storing the editable abstract field in the haspmap for manipulating the values in the table
	 * <code>String</code> will hold the propery of the column please refer ImmediateTableFactory class
	 */
	private Map<NewInitiatePedEndorsementDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<NewInitiatePedEndorsementDTO, HashMap<String, AbstractField<?>>>();

	/***
	 * Bean object fetch from db
	 */
	BeanItemContainer<NewInitiatePedEndorsementDTO> container = new BeanItemContainer<NewInitiatePedEndorsementDTO>(NewInitiatePedEndorsementDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private BeanItemContainer<SelectValue> icdBlock;
	
	private BeanItemContainer<SelectValue> icdCode;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private String pedCode;
	
	
	public void init() {
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		//layout.addComponent(btnLayout);
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	public void setTableList(final List<NewInitiatePedEndorsementDTO> list) {
		table.removeAllItems();
		for (final NewInitiatePedEndorsementDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				BeanItem<NewInitiatePedEndorsementDTO> addItem = container.addItem(new NewInitiatePedEndorsementDTO());
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "pedCode","description", "ICDChapter", "ICDBlock", "ICDCode", "source","doctorRemarks","othersSpecify" });

		table.setColumnHeader("pedCode", "Description");
		table.setColumnHeader("description", "PED Code");
		table.setColumnHeader("ICDChapter", "ICD Chapter");
		table.setColumnHeader("ICDBlock", "ICD Block");
		table.setColumnHeader("ICDCode", "ICD Code");
		table.setColumnHeader("source", "Source");
		table.setColumnHeader("othersSpecify", "Others Specify");
		table.setColumnHeader("doctorRemarks", "Doctors Remarks");
		table.setEditable(true);
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}
	
	public void manageListeners() {

		for (NewInitiatePedEndorsementDTO newInitiatePedEndorsement : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(newInitiatePedEndorsement);

			final ComboBox icdChapterCombo = (ComboBox) combos.get("ICDChapter");
			final ComboBox icdBlockCombo = (ComboBox) combos.get("ICDBlock");
			final ComboBox ickCodeCombo = (ComboBox) combos.get("ICDCode");
			
			addICDChapterListener(icdChapterCombo, icdBlockCombo);
			if(newInitiatePedEndorsement.getICDChapter() != null) {
				addICDBlock(newInitiatePedEndorsement.getICDChapter().getId(), icdBlockCombo, newInitiatePedEndorsement.getICDBlock() );
			}
			
			addICDBlockListener(icdBlockCombo,ickCodeCombo);
			if(newInitiatePedEndorsement.getICDBlock()!=null){
				addICDCode(newInitiatePedEndorsement.getICDBlock().getId(),ickCodeCombo,newInitiatePedEndorsement.getICDCode());
			}
			

		}
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			NewInitiatePedEndorsementDTO initiateDTO = (NewInitiatePedEndorsementDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(initiateDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(initiateDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(initiateDTO);
			}
			
			if("pedCode".equals(propertyId)) {
				ComboBox box = new ComboBox();
				addPedCodeValues(box);
				tableRow.put("ICDChapter", box);
				final ComboBox pedCodeValue=(ComboBox)tableRow.get("description");
				box.setData(initiateDTO);
				addPEDCodeListener(box, pedCodeValue);
				return box;
			} else if ("ICDChapter".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("150px");
				addICDChapterValues(box);
				tableRow.put("ICDChapter", box);
				final ComboBox icdBlock = (ComboBox) tableRow.get("ICDBlock");
				box.setData(initiateDTO);
				addICDChapterListener(box, icdBlock);
				return box;
			} else if ("ICDBlock".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("150px");
				box.setData(initiateDTO);
				tableRow.put("ICDBlock", box);
				ComboBox icdCodeCmb = (ComboBox) tableRow.get("ICDCode");
				addICDBlockListener(box, icdCodeCmb);
				if(initiateDTO.getICDChapter() != null) {
					addICDBlock(initiateDTO.getICDChapter().getId(), box, initiateDTO.getICDBlock());
				}
				return box;
			} else if("ICDCode".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("150px");
				box.setData(initiateDTO);
				tableRow.put("ICDCode", box);
				if(initiateDTO.getICDBlock() != null) {
					addICDCode(initiateDTO.getICDBlock().getId(), box, initiateDTO.getICDCode());
				}
				return box;
			} else if("source".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("200px");
				tableRow.put("source", box);
				box.setData(initiateDTO);
				addCommonValues(box, "source");
				TextField othersText=(TextField)tableRow.get("othersSpecify");
				addSourceListener(box, othersText);
				return box;
			}
			else if("othersSpecify".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9.]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("othersSpecify", field);
				if(initiateDTO.getSource() != null) {
					if(("Others").equals(initiateDTO.getSource().getValue())){
						if(field!=null){
						field.setEnabled(true);
						}
					}
				}
				return field;
			}
			else if("doctorRemarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setReadOnly(false);
				tableRow.put("doctorRemarks", field);
				return field;
			}
			
			else if("description".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("description", field);
				return field;
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	private void addPedCodeValues(ComboBox diagnosisCombo) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> diagnosis = (BeanItemContainer<SelectValue>) referenceData
				.get("pedCode");
		diagnosisCombo.setContainerDataSource(diagnosis);
		diagnosisCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisCombo.setItemCaptionPropertyId("value");
		diagnosisCombo.setNullSelectionAllowed(false);
	    diagnosisCombo.setValue(referenceData.get("pedCode"));
		
	}
	
	private void addCommonValues(ComboBox diagnosisCombo, String tableColumnName) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData
				.get(tableColumnName);
		diagnosisCombo.setContainerDataSource(commonValues);
		diagnosisCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisCombo.setItemCaptionPropertyId("value");
		
		diagnosisCombo.setValue(referenceData.get(tableColumnName));
		
	}
	
	@SuppressWarnings("unchecked")
	public void addICDChapterValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> icdChapter = (BeanItemContainer<SelectValue>) referenceData
				.get("ICDChapter");
		comboBox.setContainerDataSource(icdChapter);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
		comboBox.setNullSelectionAllowed(false);

	}
	
	@SuppressWarnings("unused")
	private void addPEDCodeListener(final ComboBox icdChpterCombo,
			final ComboBox icdBlockCombo) {
		if (icdChpterCombo != null) {
			icdChpterCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					NewInitiatePedEndorsementDTO newInitiatePedDTO = (NewInitiatePedEndorsementDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(newInitiatePedDTO);
					TextField field = (TextField) hashMap.get("description");
					if (newInitiatePedDTO != null) {
						if(newInitiatePedDTO.getPedCode() != null) {
							if(field != null) {
								addPEDCode(newInitiatePedDTO.getPedCode().getId(), field, null);
							}
						}
					}
				}
			});
		}
	}
	
	public void addPEDCode(Long icdChpterComboKey, TextField field, SelectValue value) {
		fireViewEvent(PEDQueryPresenter.GET_PED_CODE, icdChpterComboKey);
		field.setValue(this.pedCode);
	}
	
	@SuppressWarnings("unused")
	private void addICDChapterListener(final ComboBox icdChpterCombo,
			final ComboBox icdBlockCombo) {
		if (icdChpterCombo != null) {
			icdChpterCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					NewInitiatePedEndorsementDTO newInitiatePedDTO = (NewInitiatePedEndorsementDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(newInitiatePedDTO);
					ComboBox comboBox = (ComboBox) hashMap.get("ICDBlock");
					if (newInitiatePedDTO != null) {
						if(newInitiatePedDTO.getICDChapter() != null) {
							if(comboBox != null) {
								addICDBlock(newInitiatePedDTO.getICDChapter().getId(), comboBox, newInitiatePedDTO.getICDBlock());
							}
						}
						else
						{
							comboBox.setValue(null);
						}
						
					}
				}
			});
		}

	}
	
	@SuppressWarnings("unused")
	private void addICDBlockListener(final ComboBox icdBlockCombo,
			final ComboBox icdCodeCombo) {
		if (icdBlockCombo != null) {
			icdBlockCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					NewInitiatePedEndorsementDTO newInitiatePedEndorsementDTO = (NewInitiatePedEndorsementDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(newInitiatePedEndorsementDTO);
					ComboBox comboBox = (ComboBox) hashMap.get("ICDCode");
					if (newInitiatePedEndorsementDTO != null) {
						if(newInitiatePedEndorsementDTO.getICDBlock() != null) {
							
							if(comboBox != null) {
								addICDCode(newInitiatePedEndorsementDTO.getICDBlock().getId(), comboBox, newInitiatePedEndorsementDTO.getICDCode());
							}
						}
						else
						{
							comboBox.setValue(null);
						}
					}
				}
			});
		}
	}
	
	@SuppressWarnings("unused")
	private void addSourceListener(final ComboBox icdBlockCombo,
			final TextField icdCodeCombo) {
		if (icdBlockCombo != null) {
			icdBlockCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					NewInitiatePedEndorsementDTO newInitiatePedEndorsementDTO = (NewInitiatePedEndorsementDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(newInitiatePedEndorsementDTO);
					TextField text = (TextField) hashMap.get("othersSpecify");
					if (newInitiatePedEndorsementDTO != null) {
						if(newInitiatePedEndorsementDTO.getSource() != null) {
							if(("Others").equals(newInitiatePedEndorsementDTO.getSource().getValue())){
								if(text!=null){
								text.setEnabled(true);
								}
							}
							else
							{
								if(text!=null){
								text.setValue(null);
								text.setEnabled(false);
								}
							}
						}
					}
				}
			});
		}
	}
	
	public void addICDBlock(Long icdChpterComboKey, ComboBox icdBlockCombo, SelectValue value) {
		fireViewEvent(PEDQueryPresenter.GET_ICD_BLOCK, icdChpterComboKey);
		icdBlockCombo.setContainerDataSource(icdBlock);
		icdBlockCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		icdBlockCombo.setItemCaptionPropertyId("value");
		icdBlockCombo.setNullSelectionAllowed(false);
		
		if(value != null) {
			icdBlockCombo.setValue(value);
		}
	}
	
	public void addICDCode(Long icdBlockKey, ComboBox icdCodeCombo, SelectValue value) {
		fireViewEvent(PEDQueryPresenter.GET_ICD_CODE, icdBlockKey);
		//icdCode.addBean(value);
		icdCodeCombo.setContainerDataSource(icdCode);
		icdCodeCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		icdCodeCombo.setItemCaptionPropertyId("value");
		icdCodeCombo.setNullSelectionAllowed(false);
		
		if(value != null) {
			icdCodeCombo.setValue(value);
		}
	}
	
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockSelectValueContainer){
		this.icdBlock =	icdBlockSelectValueContainer;
	}
	
	
	
    public void setIcdCode(BeanItemContainer<SelectValue> icdCodeSelectValueContainer){
    	this.icdCode = icdCodeSelectValueContainer;
    }
    
    public List<NewInitiatePedEndorsementDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<NewInitiatePedEndorsementDTO> itemIds = (List<NewInitiatePedEndorsementDTO>) this.table.getItemIds() ;
    	return itemIds;
    }
    
    public boolean isValid() {
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<NewInitiatePedEndorsementDTO> itemIds = (Collection<NewInitiatePedEndorsementDTO>) table
				.getItemIds();
        
		for (NewInitiatePedEndorsementDTO bean : itemIds) {

			if(bean.getSource() != null && bean.getSource().getId().equals(ReferenceTable.OTHERSPECIFY) && bean.getOthersSpecify() == null) {
   				hasError = true;
   				errorMessages.add("Please Enter Others Specify details.");
   			}
			
//			if(bean.getSource() != null && bean.getSource().getId().equals(ReferenceTable.OTHERSPECIFY) && (bean.getDoctorRemarks() == null || (bean.getDoctorRemarks() != null && bean.getDoctorRemarks().equalsIgnoreCase("")))) {
//   				hasError = true;
//   				errorMessages.add("Please Enter Doctor Remarks.");
//   			}
			
			if(bean.getPedCode() != null && bean.getPedCode().getId().equals(ReferenceTable.PED_OTHER_CATEGORY) && (bean.getDoctorRemarks() == null || (bean.getDoctorRemarks() != null && bean.getDoctorRemarks().equalsIgnoreCase("")))) {
   				hasError = true;
   				errorMessages.add("Please Enter Doctor Remarks.");
   			}
			
			Set<ConstraintViolation<NewInitiatePedEndorsementDTO>> validate = validator
					.validate(bean);

			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<NewInitiatePedEndorsementDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}
		return !hasError;
	}
    
    public void addBeanToList(NewInitiatePedEndorsementDTO pedValidationDTO) {
    	container.addBean(pedValidationDTO);

//    	data.addItem(pedValidationDTO);
    	manageListeners();
    }
    
    
   	public List<String> getErrors()
   	{
   		return this.errorMessages;
   	}
    
    public void setPEDCode(String pedCode){
    	this.pedCode=pedCode;
    }

}
