package com.shaic.claim.processdatacorrection.listenertable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.validation.ConstraintViolation;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.wizard.dto.ImplantDetailsDTO;
import com.shaic.claim.processdatacorrection.dto.ImplantCorrectionDTO;
import com.shaic.claim.processdatacorrection.listenertable.ImplantCorrectionTabel.ImmediateFieldFactory;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityPresenter;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.Table.Align;

public class ActualImplantCorrectionTabel extends ViewComponent{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3651791515051529314L;

	private Map<ImplantCorrectionDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ImplantCorrectionDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ImplantCorrectionDTO> data = new BeanItemContainer<ImplantCorrectionDTO>(ImplantCorrectionDTO.class);

	private Table table;

	private List<String> errorMessages;

	public Boolean isValueChanges =false;

	public String presenterString;
	
	private Button btnAdd;

	public void init(String presenterString){

		this.presenterString = presenterString;
		this.errorMessages = new ArrayList<String>();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		layout.addComponent(btnLayout);
		addListener();	
		initTable(layout);

		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		layout.addComponent(table);
		setCompositionRoot(layout);

	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("Implant Details", data);
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
						ImplantCorrectionDTO dto = (ImplantCorrectionDTO) currentItemId;
						if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
							fireViewEvent(DataCorrectionPresenter.DELETE_IMPLANT_CORRECTION_VALUES,dto.getKey());	
						}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
							fireViewEvent(DataCorrectionPriorityPresenter.DELETE_IMPLANT_CORRECTION_VALUES_PRIORITY,dto.getKey());	
						}
						table.removeItem(currentItemId);
					} 
				});
				deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;
			}
		});
		table.setVisibleColumns(new Object[] { "actualImplantName","actualImplantType","actualImplantCost","Delete"});
		table.setColumnHeader("actualImplantName", "Actual Implant Name");
		table.setColumnHeader("actualImplantType", "Actual Implant Type");
		table.setColumnHeader("actualImplantCost", "Actual Implant Cost");

		table.setColumnAlignment("actualImplantName",Align.CENTER);
		table.setColumnAlignment("actualImplantType",Align.CENTER);
		table.setColumnAlignment("actualImplantCost",Align.CENTER);

		table.setEditable(true);

		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			ImplantCorrectionDTO implantCorrectionDTO = (ImplantCorrectionDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(implantCorrectionDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(implantCorrectionDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(implantCorrectionDTO);
			}

			if("actualImplantName".equals(propertyId)){
				TextArea box = new TextArea();
				box.setWidth("70%");
				box.setHeight("21px");
				box.setNullRepresentation("");
				box.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(box,null,getUI(),SHAConstants.IMPLANT_NAME);
				box.setMaxLength(100);
				tableRow.put("actualImplantName", box);
				box.setData(implantCorrectionDTO);
				return box;
			}else if("actualImplantType".equals(propertyId)){
				TextArea box = new TextArea();
				box.setWidth("70%");
				box.setHeight("21px");
				box.setNullRepresentation("");
				box.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(box,null,getUI(),SHAConstants.IMPLANT_TYPE);
				box.setMaxLength(100);
				tableRow.put("actualImplantType", box);
				box.setData(implantCorrectionDTO);
				return box;
			}else if("actualImplantCost".equals(propertyId)){
				TextField box = new TextField();
				box.setWidth("70%");
				box.setNullRepresentation("");
				CSValidator designationValid = new CSValidator();
				designationValid.extend(box);
				designationValid.setRegExp("^[0-9.]*$");
				designationValid.setPreventInvalidTyping(true);
				tableRow.put("actualImplantCost", box);
				box.setData(implantCorrectionDTO);
				return box;
			} else {
				Field<?> field = super.createField(container, itemId,propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	public List<ImplantCorrectionDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<ImplantCorrectionDTO> itemIds = (List<ImplantCorrectionDTO>) this.table.getItemIds();
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(ImplantCorrectionDTO implantCorrectionDTO) {
		data.addItem(implantCorrectionDTO);
	}

	public void addBeansToList(List<ImplantCorrectionDTO> implantCorrectionDTOs){

		for(ImplantCorrectionDTO correctionDTO: implantCorrectionDTOs){
			addBeanToList(correctionDTO);
		}
	}

	/*public boolean isValid() {

		boolean hasChanges =false;
		List<ImplantCorrectionDTO> correctionDTOs = getValues();
		if(correctionDTOs !=null && !correctionDTOs.isEmpty()){
			for(ImplantCorrectionDTO implantCorrectionDTO:correctionDTOs){
				if(implantCorrectionDTO.getImplantName() !=null
						&& !implantCorrectionDTO.getImplantName().equalsIgnoreCase(implantCorrectionDTO.getActualImplantName())){
					implantCorrectionDTO.setHasChanges(true);
					hasChanges =true;
				}else if(implantCorrectionDTO.getImplantType() != null
						&& !implantCorrectionDTO.getImplantType().equalsIgnoreCase(implantCorrectionDTO.getActualImplantType())){
					implantCorrectionDTO.setHasChanges(true);
					hasChanges =true;
				}else if(implantCorrectionDTO.getImplantCost() != null
						&& !implantCorrectionDTO.getImplantCost().equals(implantCorrectionDTO.getActualImplantCost())){
					implantCorrectionDTO.setHasChanges(true);
					hasChanges =true;
				}
			}
		}
		return hasChanges;

	}*/

	public boolean isValid() {

		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<ImplantCorrectionDTO> itemIds = (Collection<ImplantCorrectionDTO>) table.getItemIds();
		if(itemIds == null 
				|| itemIds.isEmpty()){
			hasError = true;
			errorMessages.add("Please Add Atleast one Implant Details.");
		}else{
			for (ImplantCorrectionDTO implantDetailsDTO : itemIds) {
				if(implantDetailsDTO.getActualImplantName() == null || 
						(implantDetailsDTO.getActualImplantName() != null &&  implantDetailsDTO.getActualImplantName().trim().isEmpty())) {
					hasError = true;
					errorMessages.add("Please Enter Implant Name");
				}else if(implantDetailsDTO.getActualImplantType() == null ||
							(implantDetailsDTO.getActualImplantType() != null &&  implantDetailsDTO.getActualImplantType().trim().isEmpty())){
					hasError = true;
					errorMessages.add("Please Enter Implant Type");
				}else if(implantDetailsDTO.getActualImplantCost() == null){
					hasError = true;
					errorMessages.add("Please Enter Implant Cost");
				}
			}
		}
		return !hasError;
	}
	
	public List<String> getErrors() {
		return this.errorMessages;
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				ImplantCorrectionDTO implantCorrectionDTO = new ImplantCorrectionDTO();
				implantCorrectionDTO.setHasChanges(true);
				BeanItem<ImplantCorrectionDTO> addItem = data.addItem(implantCorrectionDTO);
			}
		});
	}

	public void clearObject() {
		clearDataCorrectionTableItem(tableItem);
		data = null;
		errorMessages = null;
		presenterString = null;
	}
	
	private void clearDataCorrectionTableItem(Map<ImplantCorrectionDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
		
	    	Iterator<Entry<ImplantCorrectionDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
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
