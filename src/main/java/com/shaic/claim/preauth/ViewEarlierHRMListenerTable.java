package com.shaic.claim.preauth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.components.GComboBox;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewEarlierHRMListenerTable  extends ViewComponent{

	
private Map<HRMTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<HRMTableDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<HRMTableDTO> container = new BeanItemContainer<HRMTableDTO>(HRMTableDTO.class);
	
	private Table table;
	
	private HRMTableDTO bean;
	
	private Map<String, Object> referenceData;
	private VerticalLayout layout;
	
	private List<HRMTableDTO> hrmDtoList = null;
	
	public void init() {		
		layout = new VerticalLayout();
		initTable();
		table.setWidth("100%");
		table.setHeight("350px");
		table.setPageLength(table.getItemIds().size());
		
	/*	BeanItemContainer<HRMTableDTO> container = new BeanItemContainer<HRMTableDTO>(HRMTableDTO.class);
		
		if(null != hrmDtoList && !hrmDtoList.isEmpty()){
			container.addAll(hrmDtoList);
		}
		table.setContainerDataSource(container);*/
		layout.addComponent(table);	

		setCompositionRoot(layout);
	}
	
	/*public void viewInt(List<HRMTableDTO> hrmDtoList )
	{
		this.hrmDtoList = hrmDtoList;
	}*/
	
	public void initTable(){

		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());	
	
		
			table.setVisibleColumns(new Object[] { "anhOrNanh", "diagnosis", "surgicalProcedure", "claimedAmt","packageAmt", "requestTypeValue" , "docRemarks" ,"assigneeDateAndTime", "hrmReplyRemarks", 
					"replyDateAndTime" , "docUserId" , "docName" , "docDeskNumber"});

			table.setColumnHeader("anhOrNanh", "ANH/NANH");
			table.setColumnHeader("diagnosis", "Diagnosis");
			table.setColumnHeader("surgicalProcedure", "Surgical </br> Procedure");
			table.setColumnHeader("claimedAmt", "Claime</br>Amount");
			table.setColumnHeader("packageAmt", "Package </br> Amount");
			table.setColumnHeader("requestTypeValue", "Request Type");
			table.setColumnHeader("docRemarks", "Doctor</br> Remarks");
			table.setColumnHeader("assigneeDateAndTime","Assignee </br> Date & Time");
			table.setColumnHeader("hrmReplyRemarks", "HRM </br> Reply Remarks");
			table.setColumnHeader("replyDateAndTime", "Reply </br> Date & Time");
			table.setColumnHeader("docUserId", "Dr.User Id");
			table.setColumnHeader("docName", "Dr.Name");
			table.setColumnHeader("docDeskNumber", "Dr.Desk Number");
				
		table.setEditable(true);
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		table.setFooterVisible(true);	
	}
	
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			HRMTableDTO entryDTO = (HRMTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
			
			
			if ("anhOrNanh".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("125px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(50);		
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z , .]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getAnhOrNanh())
				{
				field.setDescription(entryDTO.getAnhOrNanh());
				}
				tableRow.put("anhOrNanh", field);
				return field;
			}
			
			else if ("diagnosis".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getDiagnosis())
				{
				field.setDescription(entryDTO.getDiagnosis());
				}
				tableRow.put("diagnosis", field);

				return field;
			}
			else if ("surgicalProcedure".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getSurgicalProcedure())
				{
				field.setDescription(entryDTO.getSurgicalProcedure());
				}
				tableRow.put("surgicalProcedure", field);

				return field;
			}
			else if ("claimedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getClaimedAmt())
				{
				field.setDescription(String.valueOf(entryDTO.getClaimedAmt()));
				}
				tableRow.put("claimedAmt", field);

				return field;
			}
			else if ("packageAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getPackageAmt())
				{
				field.setDescription(String.valueOf(entryDTO.getPackageAmt()));
				}
				tableRow.put("packageAmt", field);

				return field;
			}
			else if ("requestType".equals(propertyId)) {
				GComboBox box = new GComboBox();
				
				//box.focus();
				box.setWidth("150px");
				tableRow.put("requestType", box);
				box.setData(entryDTO);
				box.setEnabled(false);
				
				return box;
			}	
			else if ("docRemarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				/*CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);*/
				if(null != entryDTO.getDocRemarks())
				{
				field.setDescription(entryDTO.getDocRemarks());
				}
				tableRow.put("docRemarks", field);
				//Vaadin8-setImmediate() field.setImmediate(true);

				return field;
			}			
			else if ("assigneeDateAndTime".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getAssigneeDateAndTime())
				{
				field.setDescription(String.valueOf(entryDTO.getAssigneeDateAndTime()));
				}
				tableRow.put("assigneeDateAndTime", field);

				return field;
			}			
			else if ("hrmReplyRemarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getHrmReplyRemarks())
				{
				field.setDescription(entryDTO.getHrmReplyRemarks());
				}
				tableRow.put("hrmReplyRemarks", field);

				return field;
			}			
			else if ("replyDateAndTime".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getReplyDateAndTime())
				{
				field.setDescription(String.valueOf(entryDTO.getReplyDateAndTime()));
				}
				tableRow.put("replyDateAndTime", field);

				return field;
			}	
			
			else if ("docUserId".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getDocUserId())
				{
				field.setDescription(entryDTO.getDocUserId());
				}
				tableRow.put("docUserId", field);

				return field;
			}	
			
			else if ("docName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getDocName())
				{
				field.setDescription(entryDTO.getDocName());
				}
				tableRow.put("docName", field);

				return field;
			}	
			
			else if ("docDeskNumber".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getAnhOrNanh())
				{
				field.setDescription(entryDTO.getDocDeskNumber());
				}
				tableRow.put("docDeskNumber", field);

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
	
	 public List<HRMTableDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<HRMTableDTO> itemIds = (List<HRMTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 
	 public void addBeanToList(HRMTableDTO hrmTableDto) {
	    	
		 container.addItem(hrmTableDto);


	    }
	 
	 
	
}
