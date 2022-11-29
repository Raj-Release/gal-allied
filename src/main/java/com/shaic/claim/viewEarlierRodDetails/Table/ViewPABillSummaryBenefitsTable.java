package com.shaic.claim.viewEarlierRodDetails.Table;

import java.util.Collection;

import com.shaic.arch.table.GBaseTable;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.TableBenefitsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewPABillSummaryBenefitsTable extends GBaseTable<TableBenefitsDTO> {

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"slNo",
		"rodNo", "fileType", "fileName", "classification", "duration", "billNo", "billDate", "billAmount", "deduction", "netAmount", "eligibleAmount", "amtConsidered", "reasonForDeduction" };*/
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<TableBenefitsDTO>(
				TableBenefitsDTO.class));
	 Object[] NATURAL_COL_ORDER = new Object[] {"slNo",
			"rodNo", "fileType", "fileName", "classification", "duration", "billNo", "billDate", "billAmount", "deduction", "netAmount", "eligibleAmount", 
			"amtConsidered", "reasonForDeduction" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");

	}

	public void setTableList(final Collection<TableBenefitsDTO> list) {
		
		table.removeAllItems();
		Double totalBillAmt = new Double(0);
		Double totalDeduction =  new Double(0);
		Double totalNetAmt =  new Double(0);
		Double totalEligibleAmt =  new Double(0);
		Double totalAmtConsidered =  new Double(0);
		
		for(final TableBenefitsDTO bean : list)
		{
			table.addItem(bean);
			if(bean.getBillAmount() != null)
			{
				totalBillAmt = totalBillAmt + bean.getBillAmount();
			}
			if(bean.getDeduction() != null)
			{
				totalDeduction = totalDeduction + bean.getDeduction();
			}
			if(bean.getNetAmount() != null)
			{
				totalNetAmt = totalNetAmt + bean.getNetAmount();
			}
			if(bean.getEligibleAmount() != null)
			{
				totalEligibleAmt = totalEligibleAmt + bean.getEligibleAmount();
			}
			if(bean.getAmtConsidered() != null)
			{
				totalAmtConsidered = totalAmtConsidered + bean.getAmtConsidered();
			}
		}
		table.setColumnFooter("billDate", "Total");
		table.setColumnFooter("billAmount", totalBillAmt.toString());
		table.setColumnFooter("deduction", totalDeduction.toString());
		table.setColumnFooter("netAmount", totalNetAmt.toString());
		table.setColumnFooter("eligibleAmount", totalEligibleAmt.toString());
		table.setColumnFooter("amtConsidered", totalAmtConsidered.toString());
		table.setFooterVisible(true);
		table.sort();
		
	}
	
	
	@Override
	public void tableSelectHandler(TableBenefitsDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-pa-billsummary-benefits-";
	}

}
