package com.shaic.claim.premedical.listenerTables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.DiagnosisComboBox;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.MedicalApprovalDataExtractionPagePresenter;
import com.shaic.domain.MasterService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class SpecialityTableListener extends ViewComponent{
	
	private Map<SpecialityDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<SpecialityDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<SpecialityDTO> data = new BeanItemContainer<SpecialityDTO>(
			SpecialityDTO.class);
	private Table table;
	
	private Button btnAdd;
	
	private Map<String, Object> referenceData;

	private BeanItemContainer<SelectValue> procedure;
	
	private String presenterString;
	
	public List<SpecialityDTO> deletedDTO;
	
	private PreauthDTO bean;
	
	private List<String> errorMessages;
	
	private Validator validator;
	
	private BeanItemContainer<SelectValue> procedCode;
	
	@Inject
	private AddProcedurePopup addProcedurePopUp;
	
	@EJB
	private MasterService masterService;
	
	public void init(PreauthDTO bean,String presenterString){
		this.bean = bean;
		this.presenterString = presenterString;
		deletedDTO = new ArrayList<SpecialityDTO>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
/*		if(presenterString.equals(SHAConstants.PRE_MEDICAL_ENHANCEMENT)){
			btnLayout.setEnabled(false);
		}*/
		
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(btnLayout);
		layout.setMargin(true);
		initTable(layout);
		
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
				/*SpecialityDTO specialityTableDTO = new SpecialityDTO(presenterString);
				specialityTableDTO.setEnableOrDisable(true);
				BeanItem<SpecialityDTO> addItem = data
						.addItem(specialityTableDTO);*/
				SpecialityDTO specialityDto = new SpecialityDTO();
				BeanItem<SpecialityDTO> addItem = data
						.addItem(specialityDto);
//				manageListeners();
			}
		});
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){
		
		table = new Table("Speciality", data);
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());
		
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button deleteButton = new Button("Delete");
		    	deleteButton.setData(itemId);
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
			        	Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
			        } 
			    });
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });
		
		table.removeGeneratedColumn("addProcedure");
		table.addGeneratedColumn("addProcedure", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button addProcedure = new Button("");
				final SpecialityDTO dto = (SpecialityDTO) itemId;
				if (dto.getEnableOrDisable() != null) {
					addProcedure.setEnabled(true);
				}
				addProcedure.setEnabled(true);
				if(presenterString.equalsIgnoreCase(SHAConstants.PRE_MEDICAL_ENHANCEMENT) && 
						dto.getSpecialityType() != null && dto.getSpecialityType().getValue() != null){
					addProcedure.setEnabled(false);
				}
				addProcedure.setIcon(FontAwesome.FILE);
				addProcedure.setData(itemId);
				addProcedure.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						
						Window popup = new com.vaadin.ui.Window();
						popup.setCaption("Add New Procedure");
						popup.setWidth("30%");
						popup.setHeight("30%");
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
						
						final ComboBox cmbBox = (ComboBox) hashMap.get("procedure");
						
						addProcedurePopUp.init(dto,cmbBox,popup);
						popup.setContent(addProcedurePopUp);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(false);
						popup.addCloseListener(new Window.CloseListener() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;
							@Override
							public void windowClose(CloseEvent e) {
								// TODO Auto-generated method stub
								
							}
						});

						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
						
						
					}
				});
				return addProcedure;
			}
		});
		
		table.setVisibleColumns(new Object[] { "specialityType","procedure", "addProcedure","remarks","Delete" });
		table.setColumnHeader("specialityType", "Speciality");
		table.setColumnHeader("procedure", "Procedure");
		table.setColumnHeader("addProcedure", "");
		table.setColumnHeader("remarks", "Remarks");
		table.setEditable(true);
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
//		manageListeners();
		
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			
			SpecialityDTO specialityDTO = (SpecialityDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(specialityDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(specialityDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(specialityDTO);
			}
			
			if("specialityType".equals(propertyId)){
				GComboBox box = new GComboBox();
				addSpecialityNames(box);
				tableRow.put("specialityType", box);
				final ComboBox procedureValue = (ComboBox) tableRow.get("procedure");
				box.setData(specialityDTO);
				if(presenterString.equalsIgnoreCase(SHAConstants.PRE_MEDICAL_ENHANCEMENT) && 
						specialityDTO.getSpecialityType() != null && specialityDTO.getSpecialityType().getValue() != null){
					box.setEnabled(false);
				}
				addProcedureNameListener(box, procedureValue);
				return box;
			} else if("procedure".equals(propertyId)){
				GComboBox box = new GComboBox();
				DiagnosisComboBox addbox = new DiagnosisComboBox();
				SuggestingContainer diagnosisContainer = new SuggestingContainer(masterService);
				box.setContainerDataSource(diagnosisContainer);
//				box.setEnabled(isEnabled);
				box.setFilteringMode(FilteringMode.CONTAINS);
				box.setTextInputAllowed(true);
				box.setNullSelectionAllowed(true);
				box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				box.setItemCaptionPropertyId("value");
				box.setNewItemsAllowed(true);
				box.setData(specialityDTO);
				tableRow.put("procedure", box);
				if(specialityDTO.getSpecialityType() != null && specialityDTO.getSpecialityType().getValue() !=null
						&& !specialityDTO.getSpecialityType().getValue().isEmpty()){
					addProcedureValues(specialityDTO.getSpecialityType().getId(),box,specialityDTO.getProcedure());
				}
				if(presenterString.equalsIgnoreCase(SHAConstants.PRE_MEDICAL_ENHANCEMENT) && 
						specialityDTO.getSpecialityType() != null && specialityDTO.getSpecialityType().getValue() != null){
					box.setEnabled(false);
				}
				return box;
			} else if("remarks".equals(propertyId)){
				TextField remarks = new TextField();
				remarks.setNullRepresentation("");
				if(specialityDTO.getRemarks() != null){
					remarks.setValue(specialityDTO.getRemarks());
				}
				if(presenterString.equalsIgnoreCase(SHAConstants.PRE_MEDICAL_ENHANCEMENT) && 
						specialityDTO.getSpecialityType() != null && specialityDTO.getSpecialityType().getValue() != null){
					remarks.setEnabled(false);
				}
				return remarks;
				
			} 	else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	protected void manageListeners() {
		
		for (SpecialityDTO specialityDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(specialityDTO);
			final ComboBox procedure = (ComboBox)combos.get("procedure");
			if(specialityDTO.getSpecialityType() != null){
				
			}
		}
	}
	
	private void addSpecialityNames(ComboBox box) {
		BeanItemContainer<SelectValue> procedure = (BeanItemContainer<SelectValue>) referenceData.get("specialityType");
		box.setContainerDataSource(procedure);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
	}
	
	private void addProcedureNameListener(ComboBox speclbox,ComboBox procBox){
		if(speclbox != null){
			speclbox.addListener(new Listener(){

				@Override
				public void componentEvent(Event event) {

					ComboBox component = (ComboBox) event.getComponent();
					SpecialityDTO specialityDTO = (SpecialityDTO) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(specialityDTO);
					ComboBox comboBox = (ComboBox) hashMap.get("procedure");
					if (specialityDTO != null) {
						if (specialityDTO.getSpecialityType() != null && specialityDTO.getSpecialityType().getValue() != null) {
							if (comboBox != null) {
								addProcedureValues(specialityDTO.getSpecialityType().getId(), comboBox,specialityDTO.getProcedure());
							}
						}
					}
				
				}
				
			});
		}
		
	}
	
	public void addProcedureValues(Long specId,ComboBox prodcomboBox,SelectValue procValue){
		if(this.presenterString.equalsIgnoreCase("PreAuth")){
			fireViewEvent(PreauthWizardPresenter.GET_PREAUTH_PROCEDURE_VALUES,specId);
			fireViewEvent(PreauthWizardPresenter.SHOW_PTCA_CABG,true);
		} else if(this.presenterString.equalsIgnoreCase("PreMedicalEnhancement")){
			fireViewEvent(PremedicalEnhancementWizardPresenter.GET_PRE_ENHN_PROCEDURE_VALUES,specId);
		} else if(this.presenterString.equalsIgnoreCase(SHAConstants.PROCESS_ENHANCEMENT)){
			fireViewEvent(PreauthEnhancemetWizardPresenter.GET_PROCESS_ENHN_PROCEDURE_VALUES,specId);
			fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_SHOW_PTCA_CABG, true);
		} else if(SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)){
			fireViewEvent(MedicalApprovalDataExtractionPagePresenter.GET_ZONAL_PROCEDURE_VALUES,specId);
		} else if(SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)){
			fireViewEvent(ClaimRequestDataExtractionPagePresenter.GET_PROCESS_CLAIM_PROCEDURE_VALUES,specId);
		}
		
		prodcomboBox.setContainerDataSource(procedCode);
		prodcomboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		prodcomboBox.setItemCaptionPropertyId("value");
		
		if (procValue != null) {
			prodcomboBox.setValue(procValue);
		}
		
	}
	
	public void setProcedure(
			BeanItemContainer<SelectValue> procdSelectValueContainer) {
		procedCode = procdSelectValueContainer;
	}
	
	public List<SpecialityDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<SpecialityDTO> itemIds = (List<SpecialityDTO>) this.table
				.getItemIds();
		if(itemIds.isEmpty()) {
			itemIds = new ArrayList<SpecialityDTO>();
		}
		List<SpecialityDTO> procedureList = new ArrayList<SpecialityDTO>();
		for (SpecialityDTO specialityDTO : itemIds) {
				if(specialityDTO.getProcedure() != null && specialityDTO.getProcedure().getValue() != null && specialityDTO.getProcedure().getValue().equalsIgnoreCase("others")){
					if(specialityDTO.getAddprocedureId() != null && specialityDTO.getProcedureValue() !=null && !specialityDTO.getProcedureValue().isEmpty()) {
						SelectValue selectValue = new SelectValue();
						selectValue.setId(specialityDTO.getAddprocedureId());
						selectValue.setValue(specialityDTO.getProcedureValue());
						specialityDTO.setProcedure(selectValue);
					}
				}
				procedureList.add(specialityDTO);
			
		}
		return procedureList;
	}
	
	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(SpecialityDTO specialityDTO) {
		data.addItem(specialityDTO);
		manageListeners();
	}
	
	public boolean isValid() {
		
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<SpecialityDTO> itemIds = (Collection<SpecialityDTO>) table
				.getItemIds();
		for (SpecialityDTO specialityDTO : itemIds) {

			if((specialityDTO.getProcedure() == null || specialityDTO.getProcedure().getValue() == null || 
					(specialityDTO.getProcedure() != null && specialityDTO.getProcedure().getValue().isEmpty()))
					&& !(presenterString.equalsIgnoreCase(SHAConstants.PRE_MEDICAL_ENHANCEMENT))) {
				hasError = true;
				errorMessages.add("Please Select Procedure in Speciality");
			}
			
			Set<ConstraintViolation<SpecialityDTO>> validate = validator.validate(specialityDTO);
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<SpecialityDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}
		
		return !hasError;
	}
	
	public List<String> getErrors() {
		return this.errorMessages;
	}

}
