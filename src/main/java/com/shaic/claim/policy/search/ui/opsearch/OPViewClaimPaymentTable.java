/**
 * 
 */
package com.shaic.claim.policy.search.ui.opsearch;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpTableDTO;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.narasimhaj
 *
 */
public class OPViewClaimPaymentTable extends GBaseTable<CreateBatchOpTableDTO> {
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNo","claimType", "paymentTypeValue","bankName","accontNo","ifscCode","branchName","chequeDate", 
	"chequeNo"};
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<CreateBatchOpTableDTO>(CreateBatchOpTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() +2);
		table.setHeight("200px");
		
	}
	
	public void addBeanTolist(CreateBatchOpTableDTO tableDto){
		table.addItem(tableDto);
	}

	@Override
	public void tableSelectHandler(CreateBatchOpTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "op-view-payment-";
	}

}
