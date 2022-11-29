package com.shaic.claim.viewEarlierRodDetails.Table;

import com.shaic.arch.table.GBaseTable;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewPABillSummaryAdditionalBenefitsTable extends GBaseTable<AddOnCoversTableDTO> {
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sNo", "addonCovers","noOfchildAgeLess18",
			"allowableChildren","billNo","billDate","billAmount","deduction","netamount", "siAddOnCover", "siLimit","eligibleAmount", "availableSI", "approvedAmount","reasonForDeduction", "whenPayable"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<AddOnCoversTableDTO>(
				AddOnCoversTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {"sNo", "addonCovers","noOfchildAgeLess18",
			"allowableChildren","billNo","billDate","billAmount","deduction","netamount", "siAddOnCover", "siLimit","eligibleAmount", "availableSI", 
			"approvedAmount","reasonForDeduction", "whenPayable"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");

	}

	@Override
	public void tableSelectHandler(AddOnCoversTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-pa-billsummary-additionalbenefit-";
	}

}
