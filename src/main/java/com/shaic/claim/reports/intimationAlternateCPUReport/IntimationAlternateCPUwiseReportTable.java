package com.shaic.claim.reports.intimationAlternateCPUReport;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

public class IntimationAlternateCPUwiseReportTable extends GBaseTable<IntimationAlternateCPUwiseReportDto> {
	
	
	private static final Object[] COLUMN_HEADER = new Object[] {
		
		"intimationNo","policyNo","claimNo","insuredName","intimatorName","hospitalName","mode","madeBy","status","date","contactNo"
		};
		
	@Inject 
	private ViewDetails viewDetails;
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<IntimationAlternateCPUwiseReportDto>(IntimationAlternateCPUwiseReportDto.class));
			table.setVisibleColumns(COLUMN_HEADER);
			
			table.removeGeneratedColumn("Edit");
			table.addGeneratedColumn("Edit",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						if(((IntimationAlternateCPUwiseReportDto)itemId).getPolicyNo() != null){
						
							final String intimationNo = ((IntimationAlternateCPUwiseReportDto)itemId).getIntimationNo();
							
							final String claimNo = ((IntimationAlternateCPUwiseReportDto)itemId).getClaimNo();
							
							final Button viewDetailsButton = new Button(
									"View");

							viewDetailsButton.setData(claimNo);
							viewDetailsButton
									.addClickListener(new Button.ClickListener() {
										public void buttonClick(
												ClickEvent event) {
											String claimNumber = (String) event
													.getButton()
													.getData();
											if(claimNumber != null){
												viewDetails.viewClaimStatusUpdated(intimationNo);
											}else{
												viewDetails.getViewIntimation(intimationNo);
											}
											
										}
									});
							viewDetailsButton
									.addStyleName(BaseTheme.BUTTON_LINK);
							return viewDetailsButton;
								 
					}else{
					        return "";
					}	
					}
					});
			
			
			table.setColumnCollapsingAllowed(false);
			table.setHeight("480px");
	}

	@Override
	public void tableSelectHandler(IntimationAlternateCPUwiseReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "intimation-alternate-cpu-report-";
	}

}
