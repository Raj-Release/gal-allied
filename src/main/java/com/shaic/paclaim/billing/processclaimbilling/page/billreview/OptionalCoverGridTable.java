package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.Grid.HeaderRow;


public class OptionalCoverGridTable extends ViewComponent{

	
	Grid grid ;
	
	public Object[] VISIBLE_COLUMNS = new Object[] {"sNo","optionalCover","eligibleForPolicy","billNo",
			"billDate","noOfDaysClaimed","amountClaimedPerDay","totalClaimed","amtOfClaimPaid",
			"applicableSI","noOfDaysAllowed","maxNoOfDaysPerHospital","maxDaysAllowed","noOfDaysUtilised",
			"noOfDaysAvailable","noOfDaysPayable","allowedAmountPerDay","amtPerDayPayable","siLimit","limit","appAmt","remarks"};
	
	BeanItemContainer<OptionalCoversDTO> data = new BeanItemContainer<OptionalCoversDTO>(
			OptionalCoversDTO.class);
	
	
	public void initView(){
		grid = new Grid();
		
		for (Object object : VISIBLE_COLUMNS) {
			 grid.addColumn(object);
		}
		
		
		grid.setContainerDataSource(data);
		
		grid.setWidth("100%");
		grid.setHeight("70%");
		grid.getColumn("sNo").setHeaderCaption("S NO");
		grid.getColumn("optionalCover").setHeaderCaption("Optional Covers");
		grid.getColumn("eligibleForPolicy").setHeaderCaption("Eligible for Policy");
		grid.getColumn("billNo").setHeaderCaption("Bill No");
		grid.getColumn("billDate").setHeaderCaption("Bill Date");
		grid.getColumn("noOfDaysClaimed").setHeaderCaption("No. of Days Claimed");
		grid.getColumn("amountClaimedPerDay").setHeaderCaption("Amt Claimed per day");
		grid.getColumn("totalClaimed").setHeaderCaption("Total Claimed");
		grid.getColumn("amtOfClaimPaid").setHeaderCaption("Amt of claim Paid");
		grid.getColumn("applicableSI").setHeaderCaption("Applicable SI");
		grid.getColumn("noOfDaysAllowed").setHeaderCaption("No of Days Allowed");
		grid.getColumn("maxNoOfDaysPerHospital").setHeaderCaption("Max No of days per hosp");
		grid.getColumn("maxDaysAllowed").setHeaderCaption("Max no. days allowed (policy Period)");
		grid.getColumn("noOfDaysUtilised").setHeaderCaption("No of Days Utilized");
		grid.getColumn("noOfDaysAvailable").setHeaderCaption("No. of days  Available");
		grid.getColumn("noOfDaysPayable").setHeaderCaption("No of days Payable");
		grid.getColumn("allowedAmountPerDay").setHeaderCaption("Allowed amt per day (as per policy)");
		grid.getColumn("amtPerDayPayable").setHeaderCaption("Amt per day payable");
		grid.getColumn("siLimit").setHeaderCaption("SI limit - (for medical extension)");
		grid.getColumn("limit").setHeaderCaption("Limit (valid claim amt)");
		grid.getColumn("appAmt").setHeaderCaption("App. Amt");
		grid.getColumn("remarks").setHeaderCaption("Remarks");
		
		grid.setEditorEnabled(true);
		grid.addStyleName("generateColumnTable");
		
		
		
		
		HeaderRow groupingHeader = grid.prependHeaderRow();
		 groupingHeader.join(
		    groupingHeader.getCell("noOfDaysClaimed"),
		    groupingHeader.getCell("amountClaimedPerDay"),groupingHeader.getCell("totalClaimed")).setText("Amount Claimed");
		 
		 groupingHeader.join(groupingHeader.getCell("maxDaysAllowed"),groupingHeader.getCell("noOfDaysUtilised"),
				 groupingHeader.getCell("noOfDaysAvailable")).setText("Policy Entitlement");
		 
		 
		 
		 setCompositionRoot(grid);
		
		
		
	}
	
	public void addBeanToList(OptionalCoversDTO optionalCoversDTO) {
		data.addItem(optionalCoversDTO);
//		grid.addRow(optionalCoversDTO);
		grid.setContainerDataSource(data);
	}
}
