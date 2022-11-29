package com.shaic.reimbursement.billing.processClaimRequestBenefits;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimRequestBenefitsTable extends GBaseTable<SearchProcessClaimRequestBenefitsTableDTO>{
	private static final long serialVersionUID = -4966316904968477836L;
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "intimationSource", "cpuName", "productName",
		"insuredPatientName", "hospitalName", "hospitalType", "requestedAmt", "balanceSI","treatementType",  "speciality", "reasonForAdmission", "originatorID", "claimType"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchProcessClaimRequestBenefitsTableDTO>(SearchProcessClaimRequestBenefitsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
	}

	@Override
	public void tableSelectHandler(
			SearchProcessClaimRequestBenefitsTableDTO t) {
		//fireViewEvent(MenuPresenter.SHOW_MEDICAL_APPROVAL_PROCESS_CLAIM_REQUEST, t);
		fireViewEvent(MenuPresenter.SHOW_PROCESS_CLAIM_REQUEST_BENEFITS, t);
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-process-claim-request-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
