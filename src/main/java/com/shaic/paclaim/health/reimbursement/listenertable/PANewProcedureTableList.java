package com.shaic.paclaim.health.reimbursement.listenertable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

@SuppressWarnings("serial")
public class PANewProcedureTableList extends GEditableTable<ProcedureDTO> {
	
	private List<String> errorMessages;
	
	PreauthDTO bean;
	
	public List<String> diagnosisList = new ArrayList<String>();
	
	public PANewProcedureTableList() {
		super(PANewProcedureTableList.class);
		setUp();
	}

/*	public static final Object[] VISIBLE_COLUMNS = new Object[] {
			"procedureNameValue", "procedureCodeValue", "considerForDayCare", "remarks" };*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		// fieldMap.put("key", new TableFieldDTO("key",TextField.class,
		// Long.class));
		// fieldMap.put("preAuthkey", new
		// TableFieldDTO("preAuthkey",TextField.class, Long.class));
		
		fieldMap.put("procedureNameValue", new TableFieldDTO("procedureNameValue",
				TextField.class, String.class, true, 30,true));
		fieldMap.put("procedureCodeValue", new TableFieldDTO("procedureCodeValue",
				TextField.class, String.class, true, 30,true));
		fieldMap.put("considerForDayCare", new TableFieldDTO("considerForDayCare", ComboBox.class,
				SelectValue.class, false, 100, null, true));
		fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,
				String.class, true, 100));
	}
*/
	public void setTableList(List<ProcedureTableDTO> tableRows) {
	}

	@Override
	public void removeRow() {
	}

	@Override
	public void initTable() {
		errorMessages = new ArrayList<String>();
		table.setContainerDataSource(new BeanItemContainer<ProcedureDTO>(
				ProcedureDTO.class));
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"procedureNameValue", "procedureCodeValue", "considerForDayCare", "remarks" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		//table.setWidth("80%");
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	  ProcedureDTO dto = null;
		    	  if(itemId != null && itemId instanceof ProcedureDTO) {
		    		  dto = (ProcedureDTO) itemId;
		    	  }
		    	Button deleteButton = new Button("Delete");
		    	deleteButton.setData(dto);
		    	deleteButton.addClickListener(new Button.ClickListener() {
			        public void buttonClick(ClickEvent event) {
			        	if(event.getButton().getData() != null && (event.getButton().getData() instanceof ProcedureDTO)) {
			        		ProcedureDTO dto = (ProcedureDTO) event.getButton().getData();
			        		if(dto.getKey() != null) {
			        			if(!bean.getDeletedProcedure().contains(dto)) {
			        				bean.getDeletedProcedure().add(dto);
			        			}
			        		}
			        	}
			        	deleteRow(itemId);
			        } 
			    });
		    	return deleteButton;
		      };
		});
		table.setEditable(true);
	}

	@Override
	public String textBundlePrefixString() {
		return "procedure-";
	}

	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
	 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
			
			fieldMap.put("procedureNameValue", new TableFieldDTO("procedureNameValue",
					TextField.class, String.class, true, 30,true));
			fieldMap.put("procedureCodeValue", new TableFieldDTO("procedureCodeValue",
					TextField.class, String.class, true, 30,true));
			fieldMap.put("considerForDayCare", new TableFieldDTO("considerForDayCare", ComboBox.class,
					SelectValue.class, false, 100, null, true));
			fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,
					String.class, true, 100));
			
		return fieldMap;
	}

	public void validateFields() {
		Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}

	}

	@Override
	public void tableSelectHandler(ProcedureDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}
	
	/*protected void addProcedureNameChangeListener()
	{
		Item item = table.getItem("procedureNameValue");
		ComboBox cmb = (ComboBox)item.
		
		item.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != procedureTableObj)
				{
					String diagnosisValue = (String)event.getProperty().getValue();
					if(!procedureTableObj.diagnosisList.contains(diagnosisValue))
					{
						procedureTableObj.diagnosisList.add(diagnosisValue);
					}
				System.out.println("---the diagnosisListValue----"+procedureTableObj.diagnosisList);
				}
			}
			
			
		});
		
	}*/
	public boolean isValidProcedure()
	{
		boolean hasError = false;
		errorMessages.removeAll(getProcedureErrors());
		Collection<ProcedureDTO> itemIds = (Collection<ProcedureDTO>) table.getItemIds();
		for (ProcedureDTO bean : itemIds) {
			if(bean.getProcedureNameValue() == null || (null != bean.getProcedureNameValue() && bean.getProcedureNameValue().length() <= 0)) {
				hasError = true;
				errorMessages.add("Please Enter New Procedure Name");
			}
			
			if(bean.getProcedureCodeValue() == null || (null != bean.getProcedureCodeValue() && bean.getProcedureCodeValue().length() <= 0)) {
				hasError = true;
				errorMessages.add("Please Enter New Procedure Code");
			}
		}
		return !hasError;
	}
	

	public List<String> getProcedureErrors() {
		return this.errorMessages;
	}
	
	public void setPreauthDTO(PreauthDTO bean) {
		this.bean = bean;
	}
}
