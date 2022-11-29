package com.shaic.claim.viewEarlierRodDetails.Table;

import com.shaic.arch.table.GBaseTable;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewPABillSummaryOptionalCoverTable extends GBaseTable<OptionalCoversDTO> {
/*
	public static final Object[] NATURAL_COL_ORDER = new Object[] {"sNo","optionalCover","billNo",
			"billDate","noOfDaysClaimed","amountClaimedPerDay","totalClaimed", "amtOfClaimPaid", "applicableSI", "noOfDaysAllowed","maxNoOfDaysPerHospital","maxDaysAllowed","noOfDaysUtilised",
			"noOfDaysAvailable","noOfDaysPayable","allowedAmountPerDay","amtPerDayPayable","siLimit","limit", "balanceSI", "appAmt"};*/
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<OptionalCoversDTO>(
				OptionalCoversDTO.class));

	 Object[] NATURAL_COL_ORDER = new Object[] {"sNo","optionalCover","billNo",
				"billDate","noOfDaysClaimed","amountClaimedPerDay","totalClaimed", "amtOfClaimPaid", "applicableSI", "noOfDaysAllowed","maxNoOfDaysPerHospital",
				"maxDaysAllowed","noOfDaysUtilised",
				"noOfDaysAvailable","noOfDaysPayable","allowedAmountPerDay","amtPerDayPayable","siLimit","limit", "balanceSI", "appAmt"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");

	}

	@Override
	public void tableSelectHandler(OptionalCoversDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-pa-billsummary-optionalcovers-";
	}

}
