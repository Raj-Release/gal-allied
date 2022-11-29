package com.shaic.claim.processdatacorrection.listenertable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.processdatacorrection.dto.TreatingCorrectionDTO;
import com.shaic.claim.processdatacorrection.listenertable.TreatingCorrectionTabel.ImmediateFieldFactory;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalPresenter;
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
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.Table.Align;

public class ActualTreatingCorrectionTabel extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1910148376564000628L;

	private Map<TreatingCorrectionDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<TreatingCorrectionDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<TreatingCorrectionDTO> data = new BeanItemContainer<TreatingCorrectionDTO>(TreatingCorrectionDTO.class);

	private Table table;

	private List<String> errorMessages;

	public Boolean isValueChanges =false;

	public String presenterString;

	private Button btnAdd;

	public List<TreatingCorrectionDTO> deletedDTO;

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

		table.setWidth("70%");
		table.setPageLength(table.getItemIds().size());

		layout.addComponent(table);
		setCompositionRoot(layout);

	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("Actual Treating Doctor Details", data);
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());

		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				TreatingCorrectionDTO dto = (TreatingCorrectionDTO)itemId;
				final Button deleteButton = new Button("Delete");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						TreatingCorrectionDTO dto =  (TreatingCorrectionDTO)currentItemId;
						if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
							fireViewEvent(DataCorrectionPresenter.DELETE_TREATING_CORRECTION_VALUES,dto.getKey());	
						}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
							fireViewEvent(DataCorrectionHistoricalPresenter.DELETE_TREATING_CORRECTION_VALUES_HIST,dto.getKey());	
						}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
							fireViewEvent(DataCorrectionPriorityPresenter.DELETE_TREATING_CORRECTION_VALUES_PRIORITY,dto.getKey());	
						}
						table.removeItem(currentItemId);
					} 
				});
				deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;

			}
		});

		table.setVisibleColumns(new Object[] { "actualtreatingDoctorName","actualqualification","treatingDoctorSignature","Delete"});

		table.setColumnHeader("actualtreatingDoctorName", "Actual Treating Doctor Name");
		table.setColumnHeader("actualqualification", "Actual Qualification");
		table.setColumnHeader("treatingDoctorSignature", "Treating Doctor Signature");

		table.setColumnAlignment("actualtreatingDoctorName",Align.CENTER);
		table.setColumnAlignment("actualqualification",Align.CENTER);	
		table.setColumnAlignment("treatingDoctorSignature",Align.CENTER);
		table.setColumnAlignment("Delete",Align.CENTER);	

		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			TreatingCorrectionDTO treatingDoctorDTO = (TreatingCorrectionDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(treatingDoctorDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(treatingDoctorDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(treatingDoctorDTO);
			}

			if("actualtreatingDoctorName".equals(propertyId)){

				TextField box = new TextField();
				box.setWidth("70%");
				box.setNullRepresentation("");
				CSValidator designationValid = new CSValidator();
				designationValid.extend(box);
				designationValid.setRegExp("^[a-zA-Z .]*$");
				designationValid.setPreventInvalidTyping(true);
				tableRow.put("actualtreatingDoctorName", box);
				box.setData(treatingDoctorDTO);				
				return box;
			}else if("actualqualification".equals(propertyId)){

				TextField box = new TextField();
				box.setWidth("70%");
				box.setNullRepresentation("");
				CSValidator designationValid = new CSValidator();
				designationValid.extend(box);
				designationValid.setRegExp("^[a-zA-Z .]*$");
				designationValid.setPreventInvalidTyping(true);
				tableRow.put("actualqualification", box);
				box.setData(treatingDoctorDTO);
				return box;
			}else if("treatingDoctorSignature".equals(propertyId)){
				GComboBox box = new GComboBox();
				addDoctorSignatureValue(box);
				tableRow.put("treatingDoctorSignature", box);
				box.setData(treatingDoctorDTO);
				return box;
			}else {
				Field<?> field = super.createField(container, itemId,propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	public List<TreatingCorrectionDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<TreatingCorrectionDTO> itemIds = (List<TreatingCorrectionDTO>) this.table.getItemIds();
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(TreatingCorrectionDTO treatingCorrectionDTO) {
		data.addItem(treatingCorrectionDTO);
	}

	public void addBeansToList(List<TreatingCorrectionDTO> treatingCorrectionDTOs){

		for(TreatingCorrectionDTO treatingCorrectionDTO: treatingCorrectionDTOs){
			addBeanToList(treatingCorrectionDTO);
		}
	}

	public boolean isValid() {

		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		List<TreatingCorrectionDTO> correctionDTOs = getValues();
		if(correctionDTOs !=null && !correctionDTOs.isEmpty()){
			for(TreatingCorrectionDTO treatingCorrectionDTO:correctionDTOs){
				if(treatingCorrectionDTO.getActualtreatingDoctorName() !=null
						&& (treatingCorrectionDTO.getActualqualification() == null
						|| treatingCorrectionDTO.getActualqualification().isEmpty())){
					hasError = true;
					errorMessages.add("Actual Treating Qualification is not entered");
				}else if(treatingCorrectionDTO.getActualqualification() != null
						&& (treatingCorrectionDTO.getActualtreatingDoctorName() == null
						|| treatingCorrectionDTO.getActualtreatingDoctorName().isEmpty())){
					hasError = true;
					errorMessages.add("Actual Treating Doctor Name is not entered");
				}else if((treatingCorrectionDTO.getActualtreatingDoctorName() == null
						|| treatingCorrectionDTO.getActualtreatingDoctorName().isEmpty()) && (treatingCorrectionDTO.getActualqualification() == null
								|| treatingCorrectionDTO.getActualqualification().isEmpty())){
					hasError = true;
					errorMessages.add("Actual Doctor Name and Qualification is not entered");
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

				TreatingCorrectionDTO treatingDoctorDTO = new TreatingCorrectionDTO();
				treatingDoctorDTO.setHasChanges(true);
				BeanItem<TreatingCorrectionDTO> addItem = data.addItem(treatingDoctorDTO);
			}
		});
	}
	
	public void clearObject() {
		clearDataCorrectionTableItem(tableItem);
		data = null;
		errorMessages = null;
		presenterString = null;
		deletedDTO = null;
	}
	
	private void clearDataCorrectionTableItem(Map<TreatingCorrectionDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
		
	    	Iterator<Entry<TreatingCorrectionDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
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
	
	private void addDoctorSignatureValue(ComboBox box) {
		BeanItemContainer<SelectValue> signatureVal = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue selVal = new SelectValue();
		selVal.setId(1l);
		selVal.setValue("Yes");
		SelectValue selValTwo = new SelectValue();
		selValTwo.setId(2l);
		selValTwo.setValue("No");
		signatureVal.addBean(selVal);
		signatureVal.addBean(selValTwo);
		box.setContainerDataSource(signatureVal);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
	}

}
