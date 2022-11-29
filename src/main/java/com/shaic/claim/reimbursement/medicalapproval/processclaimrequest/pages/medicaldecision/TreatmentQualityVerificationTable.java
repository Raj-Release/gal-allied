package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.TreatmentQualityVerificationDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Table.ColumnHeaderMode;
import com.vaadin.v7.ui.TextField;

public class TreatmentQualityVerificationTable extends GEditableTable<TreatmentQualityVerificationDTO> {
	private static final long serialVersionUID = 5375199119863277119L;
	
	private List<String> errorMessages;
	
	public final Object[] VISIBLE_COLUMNS = new Object[] {"slNo",
		"description", "verified", "remarks" };
	
	public TreatmentQualityVerificationTable() {
		super(TreatmentQualityVerificationTable.class);
		setUp();
	}


	//public final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();




	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		Map<String, TableFieldDTO> fieldMap = new WeakHashMap<String, TableFieldDTO>();
		fieldMap.put("slNo", new TableFieldDTO("slNo", TextField.class , String.class, false));
		fieldMap.put("description", new TableFieldDTO("description",
				TextField.class,String.class, false,250));
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
		table.setContainerDataSource(new BeanItemContainer<TreatmentQualityVerificationDTO>(
				TreatmentQualityVerificationDTO.class));
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnWidth("slNo", 50);
		table.setColumnWidth("description", 500);
		table.setColumnWidth("verified", 200);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT_DEFAULTS_ID);
	}

	@Override
	public void tableSelectHandler(TreatmentQualityVerificationDTO t) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String textBundlePrefixString() {
		return "medical-approval-treatment-verification-";
	}
	
	public boolean isValidTable()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrorsForRemarks());
		@SuppressWarnings("unchecked")
		Collection<TreatmentQualityVerificationDTO> itemIds = (Collection<TreatmentQualityVerificationDTO>) table.getItemIds();
		for (TreatmentQualityVerificationDTO bean : itemIds) {
			if(null != bean.getVerified() && bean.getVerified().getId().equals(ReferenceTable.COMMONMASTER_NO) && (bean.getRemarks() == null || (bean.getRemarks() != null && bean.getRemarks().length() == 0)))
			{
				hasError = true;
				errorMessages.add("Please Enter Treatment Verification Medical Remarks.");
			}
			
		}
		return !hasError;
	}
 
	public List<String> getErrorsForRemarks()
	{
		return this.errorMessages;
	}

}
