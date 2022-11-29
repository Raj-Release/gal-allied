package com.shaic.claim.pedrequest.approve.bancspedrequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pedquery.PEDQueryPresenter;
import com.shaic.claim.preauth.wizard.dto.NewInitiatePedEndorsementDTO;
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

public class BancsPEDRequestApproveEditableTable extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<NewInitiatePedEndorsementDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<NewInitiatePedEndorsementDTO, HashMap<String, AbstractField<?>>>();

	/***
	 * Bean object fetch from db
	 */
	BeanItemContainer<NewInitiatePedEndorsementDTO> data = new BeanItemContainer<NewInitiatePedEndorsementDTO>(NewInitiatePedEndorsementDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private BeanItemContainer<SelectValue> icdBlock;
	
	private BeanItemContainer<SelectValue> icdCode;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	
	public void init() {
		
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
		layout.addComponent(btnLayout);
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				BeanItem<NewInitiatePedEndorsementDTO> addItem = data.addItem(new NewInitiatePedEndorsementDTO());
				manageListeners();
			}
		});
	}
	
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "pedCode","description", "ICDChapter", "ICDBlock", "ICDCode", "source", "othersSpecify", "doctorRemarks" });

		table.setColumnHeader("pedCode", "Ped Code");
		table.setColumnHeader("description", "Description");
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
				box.setReadOnly(true);
				addPedCodeValues(box);
				tableRow.put("ICDChapter", box);
				return box;
			} else if ("ICDChapter".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("150px");
				box.setReadOnly(true);
				addICDChapterValues(box);
				tableRow.put("ICDChapter", box);
				final ComboBox icdBlock = (ComboBox) tableRow.get("ICDBlock");
				box.setData(initiateDTO);
				addICDChapterListener(box, icdBlock);
				return box;
			} else if ("ICDBlock".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("150px");
				box.setReadOnly(true);
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
				box.setReadOnly(true);
				tableRow.put("ICDCode", box);
				if(initiateDTO.getICDBlock() != null) {
					addICDCode(initiateDTO.getICDBlock().getId(), box, initiateDTO.getICDCode());
				}
				return box;
			} else if("source".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("100px");
				box.setReadOnly(true);
				tableRow.put("source", box);
				addCommonValues(box, "source");
				return box;
			}
			else if("othersSpecify".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("othersSpecify", field);
				return field;
			}
			else if("doctorRemarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("doctorRemarks", field);
				return field;
			}
			
			else if("description".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
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
		
		if(value != null) {
			icdBlockCombo.setValue(value);
		}
	}
	
	public void addICDCode(Long icdBlockKey, ComboBox icdCodeCombo, SelectValue value) {
		fireViewEvent(PEDQueryPresenter.GET_ICD_CODE, icdBlockKey);
		icdCodeCombo.setContainerDataSource(icdCode);
		icdCodeCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		icdCodeCombo.setItemCaptionPropertyId("value");
		
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
    
    public void addBeanToList(NewInitiatePedEndorsementDTO pedValidationDTO) {
    	data.addBean(pedValidationDTO);
//    	data.addItem(pedValidationDTO);
    	manageListeners();
    }
    
   	public List<String> getErrors()
   	{
   		return this.errorMessages;
   	}


}
