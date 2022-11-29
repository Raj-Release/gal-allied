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

public class ProcessingICACTeamResponse extends ViewComponent{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1910148376564000628L;

	private Map<ProcessingICACTeamResponseDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ProcessingICACTeamResponseDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ProcessingICACTeamResponseDTO> data = new BeanItemContainer<ProcessingICACTeamResponseDTO>(ProcessingICACTeamResponseDTO.class);

	private Table table;

	private List<String> errorMessages;
	
	public Boolean isValueChanges =false;
	
	public Boolean isView =false;
	
	private Button btnAdd;
	
	public List<ProcessingICACTeamResponseDTO> deletedDTO;

	public void init(Boolean isView){
		
		this.isView = isView;
		this.errorMessages = new ArrayList<String>();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
	
		/*if(!isView){
			btnAdd = new Button();
			btnAdd.setStyleName("link");
			btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
			HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
			btnLayout.setWidth("100%");
			btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
			layout.addComponent(btnLayout);
			addListener();
		}*/		
		initTable(layout);		
		
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		layout.addComponent(table);
		setCompositionRoot(layout);

	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("ICAC Team Response", data);
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());
		
		if(!isView){
			table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;
				@Override
				public Object generateCell(final Table source, final Object itemId, Object columnId) {
					ProcessingICACTeamResponseDTO dto = (ProcessingICACTeamResponseDTO)itemId;
					if(dto.getKey() == null){
						final Button deleteButton = new Button("Delete");
						deleteButton.setData(itemId);
						deleteButton.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 6100598273628582002L;

							public void buttonClick(ClickEvent event) {
								Object currentItemId = event.getButton().getData();
								ProcessingICACTeamResponseDTO dto =  (ProcessingICACTeamResponseDTO)currentItemId;
								if(dto.getKey() != null && dto.getDiagnosis() != null && dto.getDiagnosis().length() > 0) {
									deletedDTO.add((ProcessingICACTeamResponseDTO)currentItemId);
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
	/*	if(!isView){
			table.setVisibleColumns(new Object[] { "doctorIdName","referIcacRemarks","Delete"});
			table.setColumnAlignment("Delete",Align.CENTER);
		}else{
			table.setVisibleColumns(new Object[] { "doctorIdName","referIcacRemarks"});
		}*/
		
		table.setVisibleColumns(new Object[] {"responseGivenBY","icacResponseRemarks","repliedDate"});

		table.setColumnHeader("responseGivenBY", "Response Given by");
		table.setColumnHeader("icacResponseRemarks", "ICAC Reponse");
		table.setColumnHeader("repliedDate", "Replied Date");
		
		table.setColumnAlignment("responseGivenBY",Align.CENTER);
		table.setColumnAlignment("icacResponseRemarks",Align.CENTER);
		table.setColumnAlignment("repliedDate",Align.CENTER);
		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			ProcessingICACTeamResponseDTO responseTeamDTO = (ProcessingICACTeamResponseDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(responseTeamDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(responseTeamDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(responseTeamDTO);
			}

			if("responseGivenBY".equals(propertyId)){
				TextField box = new TextField();
//				box.setWidth("70%");
				box.setNullRepresentation("");
				tableRow.put("responseGivenBY", box);
				box.setData(responseTeamDTO);
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				if(responseTeamDTO.getResponseGivenBY() != null){
					box.setValue(responseTeamDTO.getResponseGivenBY());
				}
				box.setEnabled(false);					
				return box;
			}else if("icacResponseRemarks".equals(propertyId)){
					TextField box = new TextField();
//					box.setWidth("70%");
					box.setNullRepresentation("");
					box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					tableRow.put("icacResponseRemarks", box);
					box.setData(responseTeamDTO);
					if(responseTeamDTO.getResponseGivenBY() != null){
						box.setValue(responseTeamDTO.getResponseGivenBY());
					}
					box.setEnabled(false);					
					return box;
			}else if("repliedDate".equals(propertyId)){
				TextField box = new TextField();
//				box.setWidth("70%");
				box.setNullRepresentation("");
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("repliedDate", box);
				box.setData(responseTeamDTO);
				box.setEnabled(false);
				return box;
			}else {
				Field<?> field = super.createField(container, itemId,propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	public List<ProcessingICACTeamResponseDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<ProcessingICACTeamResponseDTO> itemIds = (List<ProcessingICACTeamResponseDTO>) this.table.getItemIds();
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(ProcessingICACTeamResponseDTO ProcessingICACTeamResponseDTO) {
		data.addItem(ProcessingICACTeamResponseDTO);
	}
	
	public void addBeansToList(List<ProcessingICACTeamResponseDTO> ProcessingICACTeamResponseDTOs){
		
		for(ProcessingICACTeamResponseDTO ProcessingICACTeamResponseDTO: ProcessingICACTeamResponseDTOs){
			addBeanToList(ProcessingICACTeamResponseDTO);
		}
	}

	public boolean isValid() {
		
		boolean hasChanges =false;
		List<ProcessingICACTeamResponseDTO> correctionDTOs = getValues();
		return hasChanges;
		
	}
	
public String haserror() {
		
		List<ProcessingICACTeamResponseDTO> correctionDTOs = getValues();
		if(correctionDTOs !=null && !correctionDTOs.isEmpty()){/*
			for(ProcessingICACTeamResponseDTO ProcessingICACTeamResponseDTO:correctionDTOs){
				if(ProcessingICACTeamResponseDTO.getDoctorIdName() !=null
						&& (ProcessingICACTeamResponseDTO.getDoctorIdName() == null
						|| ProcessingICACTeamResponseDTO.getDoctorIdName().isEmpty())){
					return "enter Actual Qualification";
				}else if(ProcessingICACTeamResponseDTO.getReferIcacRemarks() != null
						&& (ProcessingICACTeamResponseDTO.getReferIcacRemarks() == null
						|| ProcessingICACTeamResponseDTO.getReferIcacRemarks().isEmpty())){
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

				ProcessingICACTeamResponseDTO treatingDoctorDTO = new ProcessingICACTeamResponseDTO();
				BeanItem<ProcessingICACTeamResponseDTO> addItem = data.addItem(treatingDoctorDTO);
			}
		});
	}


}
