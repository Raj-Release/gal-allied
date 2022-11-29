package com.shaic.claim.reports.dispatchDetailsReport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reports.opinionvalidationreport.OpinionValidationReportTableDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class DispatchDetailsReportTable extends GBaseTable<DispatchDetailsReportTableDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3410048715107649795L;
	
	Object[] AWB_VISIBLE_COLUMNS = new Object[] {"serialNumber","policyNo","intimationNo","rodNo","batchNumber","claimType","docReceivedFrom","rodType","courierPartner","awsNumber","chequeDDNumber",
			"settledAmount"};
	
	Object[] DD_VISIBLE_COLUMNS = new Object[] {"serialNumber","policyNo","intimationNo","rodNo","batchNumber","claimType","docReceivedFrom","rodType","courierPartner","awsNumber","chequeDDNumber",
			"settledAmount","chequeDDStatus","chequeDDdate","chequeDDdeliveryTo","chequeDDReturndate","returnRemark"};

	private Long updateType;

	public void setUpdateType(Long updateType) {
		this.updateType = updateType;
	}
	
	@Override
	public void removeRow() {
		table.removeAllItems();	
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<DispatchDetailsReportTableDTO>(DispatchDetailsReportTableDTO.class));
		if(updateType !=null && updateType.equals(ReferenceTable.D_BULK_UPLOAD_TYPE_DD)){
			table.setVisibleColumns(DD_VISIBLE_COLUMNS);
		}else{
			table.setVisibleColumns(AWB_VISIBLE_COLUMNS);
		}
		
		table.setColumnCollapsingAllowed(false);
		table.setHeight("360px");
		
	}

	@Override
	public void tableSelectHandler(DispatchDetailsReportTableDTO t) {	
	}

	@Override
	public String textBundlePrefixString() {
		return "dispatch-details-report-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=10){
			table.setPageLength(10);
		}
	}

}
