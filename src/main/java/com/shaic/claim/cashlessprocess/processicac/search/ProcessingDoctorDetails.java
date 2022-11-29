package com.shaic.claim.cashlessprocess.processicac.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.processdatacorrection.listenertable.SpecialityCorrectionTable.ImmediateFieldFactory;
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
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.Table.Align;

public class ProcessingDoctorDetails extends ViewComponent{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1910148376564000628L;

	private Map<ProcessingDoctorDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ProcessingDoctorDetailsDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ProcessingDoctorDetailsDTO> data = new BeanItemContainer<ProcessingDoctorDetailsDTO>(ProcessingDoctorDetailsDTO.class);

	private Table table;

	private List<String> errorMessages;
	
	public Boolean isValueChanges =false;
	
	public Boolean isView =false;
	
	private Button btnAdd;
	
	public List<ProcessingDoctorDetailsDTO> deletedDTO;

	public void init(Boolean isView){
		
		this.isView = isView;
		this.errorMessages = new ArrayList<String>();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
	
	/*	if(!isView){
			btnAdd = new Button();
			btnAdd.setStyleName("link");
			btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
			HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
			btnLayout.setWidth("100%");
			btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
			layout.addComponent(btnLayout);
			addListener();
		}	*/	
		initTable(layout);		
		
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		layout.addComponent(table);
		setCompositionRoot(layout);

	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("Processing Doctor Details", data);
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());
		
		if(!isView){
			table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;
				@Override
				public Object generateCell(final Table source, final Object itemId, Object columnId) {
					ProcessingDoctorDetailsDTO dto = (ProcessingDoctorDetailsDTO)itemId;
					if(dto.getKey() == null){
						final Button deleteButton = new Button("Delete");
						deleteButton.setData(itemId);
						deleteButton.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 6100598273628582002L;

							public void buttonClick(ClickEvent event) {
								Object currentItemId = event.getButton().getData();
								ProcessingDoctorDetailsDTO dto =  (ProcessingDoctorDetailsDTO)currentItemId;
								if(dto.getKey() != null && dto.getDiagnosis() != null && dto.getDiagnosis().length() > 0) {
									deletedDTO.add((ProcessingDoctorDetailsDTO)currentItemId);
								}
								table.removeItem(currentItemId);
							} 
						});
						deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
						return deleteButton;
					}
					return null;
				}
			});
		}
		
		table.setVisibleColumns(new Object[] { "doctorIdAndName","referToIcacRemarks"});
		
		table.setColumnHeader("doctorIdAndName", "Doctor Id and Name");
		table.setColumnHeader("referToIcacRemarks", "Refer to ICAC Remarks");
		
		table.setColumnAlignment("doctorIdAndName",Align.CENTER);
		table.setColumnAlignment("referToIcacRemarks",Align.CENTER);
		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			ProcessingDoctorDetailsDTO doctorDetailssDTO = (ProcessingDoctorDetailsDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(doctorDetailssDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(doctorDetailssDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(doctorDetailssDTO);
			}

			if("doctorIdAndName".equals(propertyId)){
				TextField box = new TextField();
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				box.setWidth("70%");
				box.setNullRepresentation("");
				tableRow.put("doctorIdAndName", box);
				box.setData(doctorDetailssDTO);
				if(doctorDetailssDTO.getDoctorIdAndName() != null){
					box.setValue(doctorDetailssDTO.getDoctorIdAndName());
				}
				box.setEnabled(false);					
				return box;
			} else if("referToIcacRemarks".equals(propertyId)){
				
				TextField box = new TextField();
				box.setWidth("70%");
				box.setNullRepresentation("");
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				box.setData(doctorDetailssDTO);
				box.setEnabled(false);	
				if(doctorDetailssDTO.getReferToIcacRemarks() != null){
					box.setValue(doctorDetailssDTO.getReferToIcacRemarks());
				}
				return box;
			}else {
				Field<?> field = super.createField(container, itemId,propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	public List<ProcessingDoctorDetailsDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<ProcessingDoctorDetailsDTO> itemIds = (List<ProcessingDoctorDetailsDTO>) this.table.getItemIds();
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(ProcessingDoctorDetailsDTO processingDoctorDetailsDTO) {
		data.addItem(processingDoctorDetailsDTO);
	}
	
	public void addBeansToList(List<ProcessingDoctorDetailsDTO> processingDoctorDetailsDTOs){
		
		for(ProcessingDoctorDetailsDTO processingDoctorDetailsDTO: processingDoctorDetailsDTOs){
			addBeanToList(processingDoctorDetailsDTO);
		}
	}

	public boolean isValid() {
		
		boolean hasChanges =false;
		List<ProcessingDoctorDetailsDTO> correctionDTOs = getValues();
		return hasChanges;
		
	}
	
public String haserror() {
		
		List<ProcessingDoctorDetailsDTO> correctionDTOs = getValues();
		if(correctionDTOs !=null && !correctionDTOs.isEmpty()){/*
			for(ProcessingDoctorDetailsDTO processingDoctorDetailsDTO:correctionDTOs){
				if(processingDoctorDetailsDTO.getActualtreatingDoctorName() !=null
						&& (processingDoctorDetailsDTO.getActualqualification() == null
						|| processingDoctorDetailsDTO.getActualqualification().isEmpty())){
					return "enter Actual Qualification";
				}else if(processingDoctorDetailsDTO.getActualqualification() != null
						&& (processingDoctorDetailsDTO.getActualtreatingDoctorName() == null
						|| processingDoctorDetailsDTO.getActualtreatingDoctorName().isEmpty())){
					return "enter Actual DoctorName";
				}
			}
		*/}
		return null;
		
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {

				ProcessingDoctorDetailsDTO treatingDoctorDTO = new ProcessingDoctorDetailsDTO();
				BeanItem<ProcessingDoctorDetailsDTO> addItem = data.addItem(treatingDoctorDTO);
			}
		});
	}


}
