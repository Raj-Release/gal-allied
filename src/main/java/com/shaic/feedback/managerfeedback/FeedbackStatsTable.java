package com.shaic.feedback.managerfeedback;

import java.util.List;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.ReferenceTable;
import com.shaic.feedback.managerfeedback.previousFeedback.BranchManagerPreviousFeedbackPresenter;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

public class FeedbackStatsTable extends GBaseTable<FeedbackStatsDto>{

	public void setReportColHeader(){
		table.setVisibleColumns(new Object[] {
				"feedbackArea", "reported", "responded", "pending" });
	}
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}

	@Override
	public void initTable() {
		
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<FeedbackStatsDto>(FeedbackStatsDto.class));
		setReportColHeader();
	//	generateColumns();
		table.setColumnWidth("feedbackArea", 120);
		table.setColumnWidth("reported",80);
		table.setColumnWidth("responded", 80);
		table.setColumnWidth("pending", 80);
		table.setPageLength(3);
		
		table.setColumnCollapsingAllowed(false);
		table.setStyleName("container");
		table.setFooterVisible(true);
		calculateTotal();
		
	}

	@Override
	public void tableSelectHandler(FeedbackStatsDto t) {
				
	}

	@Override
	public String textBundlePrefixString() {

		return "branch-manager-feedback-homepage-";
	}
	
	public void calculateTotal(){
		
		Long reportedTotal = 0l;
		Long respondedTotal = 0l;
		Long pendingTotal = 0l;
		List<FeedbackStatsDto> tableList = (List<FeedbackStatsDto>) table.getItemIds();
		if(tableList != null && !tableList.isEmpty()){
	
			for (FeedbackStatsDto tableDto : tableList) {
	
				if(tableDto.getReported() != null){
				reportedTotal += tableDto.getReported();
				}
				if(tableDto.getResponded() != null){
					respondedTotal += tableDto.getResponded();
				}
				if(tableDto.getPending() != null){
					pendingTotal += tableDto.getPending();
				}
			}
		}
		table.setColumnFooter("feedbackArea", "Total Count");
		table.setColumnFooter("reported", String.valueOf(reportedTotal));
		table.setColumnFooter("responded", String.valueOf(respondedTotal));
		table.setColumnFooter("pending", String.valueOf(pendingTotal));
	}
	
	public void generateColumns(){
		table.removeGeneratedColumn("reported");
		table.addGeneratedColumn("reported", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button reportedBtn = new Button();
				FeedbackStatsDto tableDto = (FeedbackStatsDto)itemId;
				reportedBtn.setCaption(null != tableDto.getReported()? String.valueOf(tableDto.getReported()) : "");
				reportedBtn.setData(tableDto);
				reportedBtn.addStyleName(BaseTheme.BUTTON_LINK);
				reportedBtn.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						FeedbackStatsDto tableDto = (FeedbackStatsDto)event.getButton().getData();
						fireViewEvent(MenuPresenter.SHOW_PREVIOUS_FEEDBACK_LINK_HOME_PAGE,tableDto,ReferenceTable.FEEDBACK_REPORTED_KEY);
					}
				});
			return reportedBtn;
			}
		});
		
		table.removeGeneratedColumn("pending");
		table.addGeneratedColumn("pending", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button pendingBtn = new Button();
				FeedbackStatsDto tableDto = (FeedbackStatsDto)itemId;
				pendingBtn.setCaption(null != tableDto.getPending()?String.valueOf(tableDto.getPending()) : "");               
				pendingBtn.setData(tableDto);
				pendingBtn.addStyleName(BaseTheme.BUTTON_LINK);
				pendingBtn.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						FeedbackStatsDto tableDto = (FeedbackStatsDto)event.getButton().getData();
						fireViewEvent(MenuPresenter.SHOW_PREVIOUS_FEEDBACK_LINK_HOME_PAGE,tableDto,0L);
					}
				});
			return pendingBtn;
			}
		});
		
		table.removeGeneratedColumn("responded");
		table.addGeneratedColumn("responded", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button respondedBtn = new Button();
				FeedbackStatsDto tableDto = (FeedbackStatsDto)itemId;
				respondedBtn.setCaption(null != tableDto.getResponded()?String.valueOf(tableDto.getResponded()) : "");
				respondedBtn.setData(tableDto);
				respondedBtn.addStyleName(BaseTheme.BUTTON_LINK);
				respondedBtn.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						FeedbackStatsDto tableDto = (FeedbackStatsDto)event.getButton().getData();
						fireViewEvent(MenuPresenter.SHOW_PREVIOUS_FEEDBACK_LINK_HOME_PAGE,tableDto,ReferenceTable.FEEDBACK_RESPONDED_KEY);
					}
				});
			return respondedBtn;
			}
		});
	}

}
