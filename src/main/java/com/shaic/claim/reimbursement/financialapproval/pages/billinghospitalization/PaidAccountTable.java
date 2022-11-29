package com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PaidAccountTable extends GBaseTable<VerificationAccountDeatilsTableDTO> {

		
		private final static Object COLUM_HEADER_PAID_ACC_DETAILS[] = new Object[] {
			"serialNumber","claimNumber","rodNumber","policyNumber","insuredName","payeeName","accountNumber","ifscCode","paidAmount"};
				
		@Override
		public void removeRow() {
			// TODO Auto-generated method stub
			
			table.removeAllItems();
			
		}

		@SuppressWarnings("deprecation")
		@Override
		public void initTable() {

			BeanItemContainer<VerificationAccountDeatilsTableDTO> paymentDtoContainer = new BeanItemContainer<VerificationAccountDeatilsTableDTO>(VerificationAccountDeatilsTableDTO.class);
		
		    table.setContainerDataSource(paymentDtoContainer);
		    table.setVisibleColumns(COLUM_HEADER_PAID_ACC_DETAILS);
		    table.setPageLength(5);
			
		}

		@Override
		public void tableSelectHandler(VerificationAccountDeatilsTableDTO t) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String textBundlePrefixString() {
			return "paidAccTable-";
		}
		
		@SuppressWarnings("deprecation")
		public void tablesize(){
			table.setPageLength(table.size()+1);
			int length =table.getPageLength();
			if(length>=5){
				table.setPageLength(3);
			}
			
		}
		
}