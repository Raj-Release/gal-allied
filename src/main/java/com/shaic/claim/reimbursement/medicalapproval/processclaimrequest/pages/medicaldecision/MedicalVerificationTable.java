package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.MedicalVerificationDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

public class MedicalVerificationTable extends GEditableTable<MedicalVerificationDTO> {
	private static final long serialVersionUID = 5375199119863277119L;
	
	private List<String> errorMessages;
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {"slNo",
		"description", "verified", "remarks" };*/
	
	public MedicalVerificationTable() {
		super(MedicalVerificationTable.class);
		setUp();
	}


	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

static {
	fieldMap.put("slNo", new TableFieldDTO("slNo", TextField.class , String.class, false));
		fieldMap.put("description", new TableFieldDTO("description",
				String.class, TextField.class, false, false));
		fieldMap.put("verified", new TableFieldDTO("verified", ComboBox.class,
				SelectValue.class, true));
		fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,
				String.class, true, 100));
}*/


	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
			fieldMap.put("slNo", new TableFieldDTO("slNo", TextField.class , String.class, false));
				fieldMap.put("description", new TableFieldDTO("description",
						String.class, TextField.class, false, false));
				fieldMap.put("verified", new TableFieldDTO("verified", ComboBox.class,
						SelectValue.class, true));
				fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,
						String.class, true, 100));
		return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
		
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		this.errorMessages = new ArrayList<String>();
		table.setContainerDataSource(new BeanItemContainer<MedicalVerificationDTO>(
				MedicalVerificationDTO.class));
		table.setColumnWidth("slNo", 50);
		table.setWidth("100%");
		 Object[] VISIBLE_COLUMNS = new Object[] {"slNo",
			"description", "verified", "remarks" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(MedicalVerificationDTO t) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String textBundlePrefixString() {
		return "medical-approval-verification-";
	}
	
	public boolean isValidTable()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrorsforRemarks());
		@SuppressWarnings("unchecked")
		Collection<MedicalVerificationDTO> itemIds = (Collection<MedicalVerificationDTO>) table.getItemIds();
		for (MedicalVerificationDTO bean : itemIds) {
			if(null != bean.getVerified() && bean.getVerified().getId().equals(ReferenceTable.COMMONMASTER_NO) && (bean.getRemarks() == null || (bean.getRemarks() != null && bean.getRemarks().length() == 0)))
			{
				hasError = true;
				errorMessages.add("Please Enter Medical Verification Medical Remarks.");
			}
			
		}
		return !hasError;
	}
 
	public List<String> getErrorsforRemarks()
	{
		return this.errorMessages;
	}

	

}
