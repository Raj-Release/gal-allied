package com.shaic.claim.viewEarlierRodDetails.Table;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.MedicalVerificationDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class MoreDetailsMedicalVerificationTable extends GBaseTable<MedicalVerificationDTO> {
	
	
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*public static final Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber","description", "verifiedFlag", "remarks" };*/
	
	

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setPageLength(table.getItemIds().size());
		table.removeAllItems();
		table.setWidth("50%");
		table.setContainerDataSource(new BeanItemContainer<MedicalVerificationDTO>(
				MedicalVerificationDTO.class));
		table.setColumnWidth("verifiedFlag", 150);
		table.setColumnWidth("remarks", 160);
		Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber","description", "verifiedFlag", "remarks" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setPageLength(table.size());
		table.setHeight("150px");
	}
   
	@Override
	public void tableSelectHandler(MedicalVerificationDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "medical-approval-verification-";
	}


}
