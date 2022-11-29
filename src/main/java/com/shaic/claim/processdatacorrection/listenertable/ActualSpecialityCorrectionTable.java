package com.shaic.claim.processdatacorrection.listenertable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.DiagnosisComboBox;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.premedical.listenerTables.AddProcedurePopup;
import com.shaic.claim.premedical.listenerTables.SpecialityTableListener.ImmediateFieldFactory;
import com.shaic.claim.processdatacorrection.dto.SpecialityCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalPresenter;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.MedicalApprovalDataExtractionPagePresenter;
import com.shaic.domain.MasterService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.Table.Align;

public class ActualSpecialityCorrectionTable extends ViewComponent{

	private Map<SpecialityCorrectionDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<SpecialityCorrectionDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<SpecialityCorrectionDTO> data = new BeanItemContainer<SpecialityCorrectionDTO>(SpecialityCorrectionDTO.class);
	
	private Table table;

	private Button btnAdd;

	private Map<String, Object> referenceData;

	private BeanItemContainer<SelectValue> procedure;

	private String presenterString;

	public List<SpecialityCorrectionDTO> deletedDTO;

	private PreauthDTO bean;

	private List<String> errorMessages;

	private Validator validator;

	private BeanItemContainer<SelectValue> procedCode;

	@Inject
	private AddActualProcedurePopup addProcedurePopUp;

	@EJB
	private MasterService masterService;

	public void init(String presenterString){
		this.presenterString = presenterString;
		deletedDTO = new ArrayList<SpecialityCorrectionDTO>();
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
				
				SpecialityCorrectionDTO specialityDto = new SpecialityCorrectionDTO();
				specialityDto.setHasChanges(true);
				BeanItem<SpecialityCorrectionDTO> addItem = data.addItem(specialityDto);
			}
		});
	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("Actual Speciality Details", data);
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
						SpecialityCorrectionDTO dto = (SpecialityCorrectionDTO) currentItemId;
						if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
							fireViewEvent(DataCorrectionPresenter.DELETE_CORRECTION_SPECIALITY_VALUES,dto.getKey());	
						}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
							fireViewEvent(DataCorrectionHistoricalPresenter.DELETE_CORRECTION_SPECIALITY_VALUES_HIST,dto.getKey());	
						}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
							fireViewEvent(DataCorrectionPriorityPresenter.DELETE_CORRECTION_SPECIALITY_VALUES_PRIORITY,dto.getKey());	
						}
						table.removeItem(currentItemId);
					} 
				});
				deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;
			}
		});

		if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
			table.removeGeneratedColumn("addProcedure");
			table.addGeneratedColumn("addProcedure", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
					final Button addProcedure = new Button("");
					final SpecialityCorrectionDTO dto = (SpecialityCorrectionDTO) itemId;
					addProcedure.setEnabled(true);
					addProcedure.setIcon(FontAwesome.FILE);
					addProcedure.setData(itemId);
					addProcedure.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;



						public void buttonClick(ClickEvent event) {
							if(dto.getActualspecialityType() !=null 
									&& dto.getActualspecialityType().getId()!=null){
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("Add New Procedure");
								popup.setWidth("30%");
								popup.setHeight("30%");
								HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);

								final ComboBox cmbBox = (ComboBox) hashMap.get("actualProcedure");
								addProcedurePopUp.init(dto,cmbBox,popup,presenterString);
								popup.setContent(addProcedurePopUp);
								popup.setClosable(true);
								popup.center();
								popup.setResizable(false);
								popup.addCloseListener(new Window.CloseListener() {
									private static final long serialVersionUID = 1L;
									@Override
									public void windowClose(CloseEvent e) {
										// TODO Auto-generated method stub

									}
								});
								popup.setModal(true);
								UI.getCurrent().addWindow(popup);
							}else{
								SHAUtils.showMessageBoxWithCaption("Please Select Actual Speciality Type", "Information");
							}
						}
					});

					return addProcedure;
				}
			});

			table.setVisibleColumns(new Object[] {"actualspecialityType","actualProcedure", "addProcedure","actualRemarks","Delete"});
			table.setColumnHeader("actualProcedure", "Actual Procedure");
			table.setColumnHeader("addProcedure", "");
			table.setColumnAlignment("actualProcedure",Align.CENTER);
			table.setColumnAlignment("addProcedure",Align.CENTER);
		}else{
			table.setVisibleColumns(new Object[] {"actualspecialityType","actualRemarks","Delete"});
			table.setColumnHeader("actualProcedure", "Actual Procedure");
			table.setColumnHeader("addProcedure", "");
		}

		table.setColumnHeader("actualspecialityType", "Actual Speciality");	
		table.setColumnHeader("actualRemarks", "Actual Remarks");
		table.setColumnAlignment("actualspecialityType",Align.CENTER);	
		table.setColumnAlignment("actualRemarks",Align.CENTER);	
		table.setColumnAlignment("Delete",Align.CENTER);
		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			SpecialityCorrectionDTO specialityDTO = (SpecialityCorrectionDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(specialityDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(specialityDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(specialityDTO);
			}

			if("actualspecialityType".equals(propertyId)){
				GComboBox box = new GComboBox();
				addSpecialityNames(box);
				addProcedureNameListener(box);
				box.setWidth("60%");
				if(specialityDTO.getSpecialityType() !=null){
					box.setDescription(specialityDTO.getSpecialityType().toString());
				}
				tableRow.put("actualspecialityType", box);
				box.setData(specialityDTO);
				return box;
			} else if("actualProcedure".equals(propertyId)){
				GComboBox box = new GComboBox();
				box.setWidth("60%");
				if(specialityDTO.getProcedure() !=null){
					box.setDescription(specialityDTO.getProcedure().toString());
				}
				if(specialityDTO.getActualProcedure() !=null){
					addProcedureValues(specialityDTO.getActualspecialityType().getId(), box, specialityDTO.getActualProcedure());
				}
				box.setData(specialityDTO);
				tableRow.put("actualProcedure", box);
				return box;
			} else if("actualRemarks".equals(propertyId)){
				TextField remarks = new TextField();
				remarks.setNullRepresentation("");
				remarks.setWidth("60%");
				if(specialityDTO.getRemarks() !=null){
					remarks.setDescription(specialityDTO.getRemarks());
				}
				remarks.setData(specialityDTO);		
				tableRow.put("actualRemarks", remarks);
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
		
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	private void addSpecialityNames(ComboBox box) {
	
		BeanItemContainer<SelectValue> procedure = (BeanItemContainer<SelectValue>)referenceData.get("specialityType");
		box.setContainerDataSource(procedure);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
	}

	private void addProcedureNameListener(ComboBox speclbox){
		if(speclbox != null){
			speclbox.addListener(new Listener(){

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					SpecialityCorrectionDTO specialityDTO = (SpecialityCorrectionDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(specialityDTO);
					ComboBox comboBox = (ComboBox) hashMap.get("actualProcedure");
					if (specialityDTO != null) {
						if (specialityDTO.getActualspecialityType() != null && specialityDTO.getActualspecialityType().getValue() != null) {
							if (comboBox != null) {
								addProcedureValues(specialityDTO.getActualspecialityType().getId(), comboBox,specialityDTO.getActualProcedure());
							}
						}
					}
				}

			});
		}

	}

	public void addProcedureValues(Long specId,ComboBox prodcomboBox,SelectValue procValue){
		if(this.presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
			fireViewEvent(DataCorrectionPresenter.GET_CORRECTION_PROCEDURE_VALUES,specId);
		} else if(this.presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
			fireViewEvent(DataCorrectionHistoricalPresenter.GET_CORRECTION_PROCEDURE_VALUES_HIST,specId);
		}else if(this.presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
			fireViewEvent(DataCorrectionPriorityPresenter.GET_CORRECTION_PROCEDURE_VALUES_PRIORITY,specId);
		}

		prodcomboBox.setContainerDataSource(procedCode);
		prodcomboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		prodcomboBox.setItemCaptionPropertyId("value");

		if (procValue != null) {
			prodcomboBox.setValue(procValue);
		}

	}

	public void setProcedure(BeanItemContainer<SelectValue> procdSelectValueContainer) {
		procedCode = procdSelectValueContainer;
	}

	public List<SpecialityCorrectionDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<SpecialityCorrectionDTO> itemIds = (List<SpecialityCorrectionDTO>) this.table.getItemIds();
		if(itemIds.isEmpty()) {
			itemIds = new ArrayList<SpecialityCorrectionDTO>();
		}
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(SpecialityCorrectionDTO specialityDTO) {
		data.addItem(specialityDTO);
		manageListeners();
	}

	public boolean isValid() {

		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<SpecialityCorrectionDTO> itemIds = (Collection<SpecialityCorrectionDTO>) table.getItemIds();
		for (SpecialityCorrectionDTO specialityDTO : itemIds) {

			if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
				if(specialityDTO.getActualProcedure() == null || specialityDTO.getActualProcedure().getValue() == null || 
						(specialityDTO.getActualProcedure() != null && specialityDTO.getActualProcedure().getValue().isEmpty())) {
					hasError = true;
					errorMessages.add("Please Select Procedure in Speciality");
				}
			}			

			Set<ConstraintViolation<SpecialityCorrectionDTO>> validate = validator.validate(specialityDTO);
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<SpecialityCorrectionDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}

		return !hasError;
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}

	public void clearObject() {
		clearDataCorrectionTableItem(tableItem);
		SHAUtils.setClearReferenceData(referenceData);
		data = null;
		errorMessages = null;
		presenterString = null;
		deletedDTO = null;
		procedCode = null;
	}
	
	private void clearDataCorrectionTableItem(Map<SpecialityCorrectionDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
		
	    	Iterator<Entry<SpecialityCorrectionDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
	    	//referenceData.clear();
	    	try{
		        while (iterator.hasNext()) {
		            Map.Entry pair = (Map.Entry)iterator.next();
		            Object object = pair.getValue();
		            object = null;
		            pair = null;	
		        }
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	       referenceData.clear();
	       referenceData = null;
		}
		
		}


}
