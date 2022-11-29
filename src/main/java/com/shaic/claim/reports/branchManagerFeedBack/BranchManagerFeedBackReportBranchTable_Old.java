package com.shaic.claim.reports.branchManagerFeedBack;

import java.util.WeakHashMap;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;
/**
 * Part of CR R1238
 * @author Lakshminarayana
 *
 */
public class BranchManagerFeedBackReportBranchTable_Old extends GBaseTable<BranchManagerFeedBackReportDto>{
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}

	public void setReportColHeader(){
		table.setVisibleColumns(new Object[] {
				"branchCode", "zoneName", "rating", "reported", "responded", "pending" });
	}
	
	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<BranchManagerFeedBackReportDto>(BranchManagerFeedBackReportDto.class));
			setReportColHeader();
			table.removeGeneratedColumn("rating");
			table.addGeneratedColumn("rating", new Table.ColumnGenerator() {
				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {
					Button ratingBtn = new Button();
					BranchManagerFeedBackReportDto tableDto = (BranchManagerFeedBackReportDto)itemId;
					WeakHashMap<Double, String> feedbackRatingMap = SHAUtils.getBMFBRatingImgMap();
					
					String ratingImg = feedbackRatingMap.get(0.0d);
					
					if(tableDto.getRating().doubleValue() >= 0.5d &&  tableDto.getRating().doubleValue() < 1.0d){
						ratingImg = feedbackRatingMap.get(0.5d);
					}
					if(tableDto.getRating().doubleValue() >= 1.0d && tableDto.getRating().doubleValue() < 1.5d){
						ratingImg = feedbackRatingMap.get(1.0d);
					}
					if(tableDto.getRating().doubleValue() >= 1.5d && tableDto.getRating().doubleValue() < 2.0d){
						ratingImg = feedbackRatingMap.get(1.5d);
					}
					if(tableDto.getRating().doubleValue() >= 2.0d && tableDto.getRating().doubleValue() < 2.5d){
						ratingImg = feedbackRatingMap.get(2.0d);
					}
					if(tableDto.getRating().doubleValue() >= 2.5d && tableDto.getRating().doubleValue() < 3.0d){
						ratingImg = feedbackRatingMap.get(2.5d);
					}
					if(tableDto.getRating().doubleValue() >= 3.0d && tableDto.getRating().doubleValue() < 3.5d){
						ratingImg = feedbackRatingMap.get(3.0d);
					}
					if(tableDto.getRating().doubleValue() >= 3.5d && tableDto.getRating().doubleValue() < 4.0d){
						ratingImg = feedbackRatingMap.get(3.5d);
					}
					if(tableDto.getRating().doubleValue() >= 4.0d && tableDto.getRating().doubleValue() < 4.5d){
						ratingImg = feedbackRatingMap.get(4.0d);
					}
					if(tableDto.getRating().doubleValue() >= 4.5d && tableDto.getRating().doubleValue() < 5.0d){
						ratingImg = feedbackRatingMap.get(4.5d);
					}
					if(tableDto.getRating().doubleValue() >= 5.0d){
						ratingImg = feedbackRatingMap.get(5.0d);
					}
					ratingBtn.setIcon(new ThemeResource("images/"+ratingImg));
					ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
					
				return ratingBtn;
				}
			});
			
			table.setColumnWidth("branchCode", 95);
			table.setColumnWidth("zoneName",450);
			table.setColumnWidth("rating", 120);
			table.setColumnWidth("reported", 80);
			table.setColumnWidth("responded", 80);
			table.setColumnWidth("pending", 80);
			table.setColumnCollapsingAllowed(false);
			table.setHeight("250px");
			
	}

	@Override
	public void tableSelectHandler(BranchManagerFeedBackReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "branchmanager-feedback-branch-report-";
	}
	
}
