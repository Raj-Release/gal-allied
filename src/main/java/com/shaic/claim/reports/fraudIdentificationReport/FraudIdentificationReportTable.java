package com.shaic.claim.reports.fraudIdentificationReport;

import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.rrc.services.SearchRRCRequestTableDTO;
import com.shaic.reimbursement.fraudidentification.FraudIdentificationTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class FraudIdentificationReportTable extends GBaseTable<FraudIdentificationTableDTO>{
	private static final Object[] HOSPITAL_IRDA_COL_ORDER = new Object[] {"serialNumber","disable","edit","parameterValue","hospitalInternalCode","hospitalName","hospitalAddress","hospitalCity","effectiveStartDate","effectiveEndtDate","recipientTo","recipientCc","userRemarks"};
	private static final Object[] HOSPITAL_INTERNAL_COL_ORDER = new Object[] {"serialNumber","disable","edit","parameterValue","hospitalName","hospitalAddress","hospitalCity","effectiveStartDate","effectiveEndtDate","recipientTo","recipientCc","userRemarks"};
	private static final Object[] POLICY_NUMBER_COL_ORDER = new Object[] {"serialNumber","disable","edit","parameterValue","productName","policyStartDate","policyEndDate","effectiveStartDate","effectiveEndtDate","recipientTo","recipientCc","userRemarks"};
	private static final Object[] INTERMEDIARY_COL_ORDER = new Object[] {"serialNumber","disable","edit","parameterValue","intermediaryName","effectiveStartDate","effectiveEndtDate","recipientTo","recipientCc","userRemarks"};

@Override
public void removeRow() {
	
}

@Override
public void initTable() {
	
	table.setContainerDataSource(new BeanItemContainer<FraudIdentificationTableDTO>(FraudIdentificationTableDTO.class));
	table.setVisibleColumns(HOSPITAL_INTERNAL_COL_ORDER);
	table.setColumnCollapsingAllowed(false);
	table.setHeight("480px");
}
@Override
public void tableSelectHandler(FraudIdentificationTableDTO t) {
	
}

@Override
public String textBundlePrefixString() {
	return "Fraud Parameter - Details";
}

/*protected void tablesize(){
	table.setPageLength(table.size()+1);
	int length =table.getPageLength();
	if(length>=10){
		table.setPageLength(10);
	}
}*/

public void addBeanToList(List<FraudIdentificationTableDTO> dtoList)
{
	int rowCount = 1;
	List<FraudIdentificationTableDTO> finalList = new ArrayList<FraudIdentificationTableDTO>();
	for (FraudIdentificationTableDTO fraudIdentityTableDTO : dtoList) {
			finalList.add(fraudIdentityTableDTO);
	}
	table.addItems(finalList);
}

}
