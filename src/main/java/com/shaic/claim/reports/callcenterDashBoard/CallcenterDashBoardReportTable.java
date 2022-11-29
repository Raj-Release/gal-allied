package com.shaic.claim.reports.callcenterDashBoard;

import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

public class CallcenterDashBoardReportTable extends GBaseTable<CallcenterDashBoardReportDto> {
	
	@Inject
	private ViewDetails viewDetails;
	
	private boolean auditVisible = false;
	
	private static final Object[] COLUMN_HEADER = new Object[] {
		"intimationNo","policyNumber","claimNo","patientName","intimatorName","hospitalName","intimationMode","intimatedBy",
		"status","intimationDate","contactNo" };
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<CallcenterDashBoardReportDto>(CallcenterDashBoardReportDto.class));
			table.setVisibleColumns(COLUMN_HEADER);
			table.setColumnCollapsingAllowed(false);
			table.setHeight("250px");
			table.removeGeneratedColumn("View");
			table.addGeneratedColumn("View", new Table.ColumnGenerator() {
				
				@Override
				public Object generateCell(Table source,final Object itemId, Object columnId) {

				 final Button viewBtn = new Button("View");	
				 CallcenterDashBoardReportDto dto = (CallcenterDashBoardReportDto)itemId;
					viewBtn.addClickListener(new Button.ClickListener() {
						CallcenterDashBoardReportDto dto = (CallcenterDashBoardReportDto)itemId;
						@Override
						public void buttonClick(ClickEvent event) {
							
							if (dto.getClaimNo() != null && dto.getIntimationNo() != null ) {
								
								if(dto.getIntimationNo() != null && auditVisible ){
									viewDetails.viewClaimStatusUpdated(dto.getIntimationNo(),auditVisible);
								}
								else{
									viewDetails.viewClaimStatusUpdated(dto.getIntimationNo());	
								}
							}
							else if (dto.getClaimNo() == null && dto.getIntimationNo() != null) {
								viewDetails.getViewIntimation(dto.getIntimationNo());
							}		
							
						}
					});					
					viewBtn.addStyleName(BaseTheme.BUTTON_LINK);
					if((SHAConstants.INTIMATION_SUBMITTED).equalsIgnoreCase(dto.getStatus())){
					return viewBtn;
					}
					else{
						return "";
					}
				}
			});
						
			
	}

	public void setAudiColVisible(boolean visible){
		this.auditVisible = visible;
	}
	
	@Override
	public void tableSelectHandler(CallcenterDashBoardReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "callcenterDashBoardReport-";
	}

}
