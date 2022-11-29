package com.shaic.claim.reimbursement.billing.wizard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.DateField;

public class PatientCareTable extends GEditableTable<PatientCareDTO> {
	
	private static final long serialVersionUID = 801924126257326201L;

	//private List<String> errorMessages;
	
	public List<String> diagnosisList = new ArrayList<String>();
	

	DateField toDate = null;
	
	public PatientCareTable() {
		super(PatientCareTable.class);
		setUp();
	}

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
			"engagedFrom", "engagedTo" };*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		fieldMap.put("engagedFrom", new TableFieldDTO("engagedFrom",
				DateField.class, Date.class, true));
		fieldMap.put("engagedTo", new TableFieldDTO("engagedTo",
				DateField.class, Date.class, true));
		
	}*/

	public void setTableList(List<PatientCareDTO> tableRows) {
	}

	@Override
	public void removeRow() {
	}

	@Override
	public void initTable() {
		
		table.removeAllItems();
		//errorMessages = new ArrayList<String>();
		table.setContainerDataSource(new BeanItemContainer<PatientCareDTO>(
				PatientCareDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {
			"engagedFrom", "engagedTo" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		//table.setWidth("80%");
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		//table.addListener(tableListener());
		//addListener();
	}
	
/*	private Listener tableListener()
	{
		TableFieldDTO tableFieldDTO = fieldMap.get("engagedFrom");
		final DateField fromDate = (DateField)tableFieldDTO.getField();
		TableFieldDTO tableFieldDTO1 = fieldMap.get("engagedTo");
		toDate = (DateField)tableFieldDTO1.getField();
		if(null != toDate )
		{
		toDate.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				//Boolean isChecked = false;
				Date value = (Date)event.getProperty().getValue();
				calculateDateDiff(fromDate.getValue(), value);
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_BILLING_HOSPITAL_BENEFITS, isChecked);
			}
		});
		}
		
		
	}*/
	
	/*private void addListener()
	{
		TableFieldDTO tableFieldDTO = fieldMap.get("engagedFrom");
		final DateField fromDate = (DateField)tableFieldDTO.getField();
		TableFieldDTO tableFieldDTO1 = fieldMap.get("engagedTo");
		toDate = (DateField)tableFieldDTO1.getField();
		if(null != toDate )
		{
		toDate.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				//Boolean isChecked = false;
				Date value = (Date)event.getProperty().getValue();
				calculateDateDiff(fromDate.getValue(), value);
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_BILLING_HOSPITAL_BENEFITS, isChecked);
			}
		});
		}
	}*/
	/*private void calculateDateDiff(Date fromDate , Date toDate)
	{
		int fDay = fromDate.getDay();
		int tDay = toDate.getDay();
		int diff = fDay - tDay;
		System.out.println("--date diff---"+diff);
	}*/

	@Override
	public String textBundlePrefixString() {
		return "patientcare-";
	}

	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
		
			fieldMap.put("engagedFrom", new TableFieldDTO("engagedFrom",
					DateField.class, Date.class, true));
			fieldMap.put("engagedTo", new TableFieldDTO("engagedTo",
					DateField.class, Date.class, true));
				
		return fieldMap;
	}

	public void validateFields() {
		/*Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}*/

	}

	@Override
	public void tableSelectHandler(PatientCareDTO t) {
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
	
}
