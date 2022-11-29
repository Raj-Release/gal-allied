package com.shaic.claim.processdatacorrection.listenertable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.processdatacorrection.dto.DiganosisCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ProcedureCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.SpecialityCorrectionDTO;
import com.shaic.claim.processdatacorrection.listenertable.ProcedureCorrectionTable.ImmediateFieldFactory;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalPresenter;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityPresenter;
import com.shaic.domain.preauth.ProcedureMaster;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.Table.Align;

public class ActualProcedureCorrectionTable  extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8224234108641261062L;

	private Map<ProcedureCorrectionDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ProcedureCorrectionDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ProcedureCorrectionDTO> data = new BeanItemContainer<ProcedureCorrectionDTO>(ProcedureCorrectionDTO.class);

	private Table table;

	private Map<String, Object> referenceData;

	private List<String> errorMessages;

	private Validator validator;
	
	private String presenterString;
	
	private Boolean iscodechanged = false;
	
	private Boolean isnamechanged = false;
	@PersistenceContext
	protected EntityManager entityManager;

	public void init(String presenterString){
		this.presenterString = presenterString;
		this.errorMessages = new ArrayList<String>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		initTable(layout);

		table.setWidth("70%");
		table.setPageLength(table.getItemIds().size());
		
		layout.addComponent(table);
		setCompositionRoot(layout);

	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("Actual Procedure List", data);
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
						ProcedureCorrectionDTO dto =  (ProcedureCorrectionDTO)currentItemId;
						if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
							fireViewEvent(DataCorrectionPresenter.DELETE_PROCEDURE_CORRECTION_VALUES,dto.getKey());	
						}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
							fireViewEvent(DataCorrectionHistoricalPresenter.DELETE_PROCEDURE_CORRECTION_VALUES_HIST,dto.getKey());	
						}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
							fireViewEvent(DataCorrectionPriorityPresenter.DELETE_PROCEDURE_CORRECTION_VALUES_PRIORITY,dto.getKey());	
						}
						table.removeItem(currentItemId);
					} 
				});
				deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;

			}
		});
		table.setVisibleColumns(new Object[] {"proposedProcedureName","proposedNewProcedureName","proposedProcedureCode","proposedSpeciality","Delete"});
		table.setColumnHeader("proposedProcedureName", "Actual Procedure Name");
		table.setColumnHeader("proposedNewProcedureName", "Actual New Procedure Name");
		table.setColumnHeader("proposedProcedureCode", "Actual Procedure Code");
		table.setColumnHeader("proposedSpeciality", "Actual Speciality Name");
		table.setEditable(true);
		
		table.setColumnAlignment("proposedProcedureName",Align.CENTER);
		table.setColumnAlignment("proposedProcedureCode",Align.CENTER);
		table.setColumnAlignment("proposedNewProcedureName",Align.CENTER);
		table.setColumnAlignment("proposedSpeciality",Align.CENTER);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			ProcedureCorrectionDTO procedureCorrectionDTO = (ProcedureCorrectionDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(procedureCorrectionDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(procedureCorrectionDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(procedureCorrectionDTO);
			}

		 if("proposedProcedureName".equals(propertyId)){
				GComboBox box = new GComboBox();
				addProcedureNameValues(box);
				tableRow.put("proposedProcedureName", box);
				box.setData(procedureCorrectionDTO);
				addProcedureNameListener(box);
				return box;
			} else if("proposedProcedureCode".equals(propertyId)){
				GComboBox box = new GComboBox();
				addProcedureCodeValues(box);
				box.setEnabled(false);
				tableRow.put("proposedProcedureCode", box);
				box.setData(procedureCorrectionDTO);
//				addProcedureCodeListener(box);
				return box;
			} else if ("proposedNewProcedureName".equals(propertyId)) {
				TextField field = new TextField();
				field.setMaxLength(100);
				field.setEnabled(false);
				field.setNullRepresentation("");
				field.setWidth("150px");
				field.setReadOnly(false);
				field.setData(procedureCorrectionDTO);
				tableRow.put("proposedNewProcedureName", field);
				return field;
			}else if ("proposedSpeciality".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(true);
				addProcedureSpecialityValues(box);
				box.setData(procedureCorrectionDTO);
				tableRow.put("proposedSpeciality", box);
				
//				addProcedureCodeListener(box, procedureName);
				return box;
			}
			else {
				Field<?> field = super.createField(container, itemId,propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addProcedureNameValues(ComboBox box) {
		
		BeanItemContainer<SelectValue> procedure = (BeanItemContainer<SelectValue>) referenceData.get("procedureName");
		box.setContainerDataSource(procedure);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
	}
	
	private void addProcedureCodeValues(ComboBox box) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> procedureCode = (BeanItemContainer<SelectValue>) referenceData.get("procedureCode");
		box.setContainerDataSource(procedureCode);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
		
	}
	
	public List<ProcedureCorrectionDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<ProcedureCorrectionDTO> itemIds = (List<ProcedureCorrectionDTO>) this.table.getItemIds();
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(ProcedureCorrectionDTO procedureCorrectionDTO) {
		data.addItem(procedureCorrectionDTO);
	}
	
	public void addBeansToList(List<ProcedureCorrectionDTO> procedureCorrectionDTOs){

		for(ProcedureCorrectionDTO procedureCorrectionDTO: procedureCorrectionDTOs){
			addBeanToList(procedureCorrectionDTO);
		}
	}

	
	@SuppressWarnings("unused")
	private void addProcedureCodeListener(ComboBox procedureCode) {
		if (procedureCode != null) {
			procedureCode.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					if(!isnamechanged){
						iscodechanged =true;
						ComboBox component = (ComboBox) event.getComponent();
						ProcedureCorrectionDTO procedureDTO = (ProcedureCorrectionDTO) component.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(procedureDTO);
						ComboBox procedureNameCombo = (ComboBox) hashMap.get("proposedProcedureName");
						if(null != procedureNameCombo) {
							if(null != procedureDTO.getProposedProcedureCode()
									&& procedureDTO.getProposedProcedureCode().getId() !=null){
								procedureNameCombo.setValue(procedureDTO.getProposedProcedureCode());
							}				
						}
					}
					isnamechanged =false;
				} 
			});
		}
	}
	
	private void addProcedureNameListener(ComboBox procedureName) {
		if (procedureName != null) {
			procedureName.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					if(!iscodechanged){
						isnamechanged = true;
						ComboBox component = (ComboBox) event.getComponent();
						ProcedureCorrectionDTO procedureDTO = (ProcedureCorrectionDTO) component.getData();
						SelectValue selectValue = (SelectValue) component.getValue();
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(procedureDTO);
						ComboBox procedureCode = (ComboBox) hashMap.get("proposedProcedureCode");
						TextField proposednewProceudreName = hashMap
								.get("proposedNewProcedureName") != null ? (TextField) hashMap
										.get("proposedNewProcedureName") : null;
										if(null != selectValue && null != selectValue.getValue() && !selectValue.getValue().isEmpty() )
										{
											if(selectValue.getValue().equalsIgnoreCase("others")){
												if(proposednewProceudreName != null){
													proposednewProceudreName.setEnabled(true);
												}
												if(procedureCode != null){
													procedureCode.setEnabled(false);
												}
											}else if(!selectValue.getValue().equalsIgnoreCase("others")){
												if(proposednewProceudreName != null){
													proposednewProceudreName.setEnabled(false);
													proposednewProceudreName.setValue(null);
												}
										}
										}
										if(null != procedureCode && !selectValue.getValue().equalsIgnoreCase("others")) {
											SelectValue procedureCodevalue = new SelectValue();
											ProcedureMaster procedureMaster = findMasProcByKey(procedureDTO.getProposedProcedureName().getId());
											if(procedureMaster !=null){
												procedureCodevalue.setId(procedureMaster.getKey());
												procedureCodevalue.setValue(procedureMaster.getProcedureCode());
											}
											if(null != procedureCodevalue
													&& procedureCodevalue.getValue() !=null){
												procedureCode.setValue(procedureCodevalue);
											}				
										}
					}
					iscodechanged =false;
				} 
			});
		}
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
	}
	
	private void clearDataCorrectionTableItem(Map<ProcedureCorrectionDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
		
	    	Iterator<Entry<ProcedureCorrectionDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
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
	
	private void addProcedureSpecialityValues(ComboBox box) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> specialityCode = (BeanItemContainer<SelectValue>) referenceData
				.get("surgicalSpeciality");
		box.setContainerDataSource(specialityCode);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
		
	}
	
	public boolean isValid() {

		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<ProcedureCorrectionDTO> itemIds = (Collection<ProcedureCorrectionDTO>) table.getItemIds();
		for (ProcedureCorrectionDTO autualProceduredto : itemIds) {

				if(autualProceduredto.getProposedSpeciality() == null || autualProceduredto.getProposedSpeciality().getValue() == null || 
						(autualProceduredto.getProposedSpeciality() != null && autualProceduredto.getProposedSpeciality().getValue().isEmpty())) {
					hasError = true;
					errorMessages.add("Please Select Actual Speciality Name in Actual Procedure Table");
				}
				if(autualProceduredto.getProposedProcedureName() == null || autualProceduredto.getProposedProcedureName().getValue() == null || 
						(autualProceduredto.getProposedProcedureName() != null && autualProceduredto.getProposedProcedureName().getValue().isEmpty())) {
					hasError = true;
					errorMessages.add("Please Select Actual Procedure Name in Actual Procedure Table");
				}
				if(autualProceduredto.getProposedProcedureName() != null && autualProceduredto.getProposedProcedureName().getValue() != null && !autualProceduredto.getProposedProcedureName().getValue().isEmpty() &&
						autualProceduredto.getProposedProcedureName().getValue().equalsIgnoreCase("Others")	) {
					if(autualProceduredto.getProposedNewProcedureName() == null || (autualProceduredto.getProposedNewProcedureName() != null && autualProceduredto.getProposedNewProcedureName().isEmpty())){
					hasError = true;
					errorMessages.add("Actual Procedure selected as other. Please Enter the New Procedure Name");
					}
				}
				if(autualProceduredto.getProposedProcedureName() != null && autualProceduredto.getProposedProcedureName().getValue() != null && !autualProceduredto.getProposedProcedureName().getValue().isEmpty() &&
						!autualProceduredto.getProposedProcedureName().getValue().equalsIgnoreCase("Others")) {
					if(autualProceduredto.getProposedProcedureCode() == null  || autualProceduredto.getProposedProcedureCode().getValue() == null ||
							(autualProceduredto.getProposedProcedureCode().getValue() != null && autualProceduredto.getProposedProcedureCode().getValue().isEmpty())){
						hasError = true;
						errorMessages.add("Please Select Actual Procedure Code in Actual Procedure Table");
						}	
				}

			Set<ConstraintViolation<ProcedureCorrectionDTO>> validate = validator.validate(autualProceduredto);
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<ProcedureCorrectionDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}

		return !hasError;
	}
	
	@SuppressWarnings("unchecked")
	public ProcedureMaster findMasProcByKey(Long key) {
		Query rodByKey = entityManager.createNamedQuery("ProcedureMaster.findByKey").setParameter("primarykey", key);
		List<ProcedureMaster> procedureMasters = (List<ProcedureMaster>) rodByKey.getResultList();
		if (procedureMasters !=null && !procedureMasters.isEmpty()) {
			return procedureMasters.get(0);
		}
		return null;
	}
}
