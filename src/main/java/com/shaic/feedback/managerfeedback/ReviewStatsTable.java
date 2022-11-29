package com.shaic.feedback.managerfeedback;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.grid.HeightMode;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Grid;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Grid.HeaderRow;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.v7.ui.Table;

public class ReviewStatsTable extends ViewComponent{

	private static final long serialVersionUID = 1L;
	private Table reviewStatsGrid;
	
//	public Object[] VISIBLE_COLUMNS = new Object[] {"reviewStatus","policyReview","merReview","claimReview"};
	
//	BeanItemContainer<ReviewStatsDto> dataContainer = new BeanItemContainer<ReviewStatsDto>(
//					ReviewStatsDto.class);
	
	public void initView(){
		reviewStatsGrid = new Table();	
		
		reviewStatsGrid.addContainerProperty("reviewStatus", String.class, null);
		reviewStatsGrid.addContainerProperty("merReview",  Long.class, null);
		reviewStatsGrid.addContainerProperty("claimsRetailReview",  Long.class, null);
		reviewStatsGrid.addContainerProperty("claimsGmcReview",  Long.class, null);
		//generateColumns();
		reviewStatsGrid.setColumnHeader("reviewStatus", "");
		reviewStatsGrid.setColumnHeader("merReview", "MER");
		reviewStatsGrid.setColumnHeader("claimsRetailReview", "Claims - Retail");
		reviewStatsGrid.setColumnHeader("claimsGmcReview", "Claims - GMC");
		reviewStatsGrid.setPageLength(2);
//		reviewStatsGrid.setSizeFull();

		reviewStatsGrid.setColumnWidth("reviewStatus", 105);
		
		
//		HeaderRow groupingHeader = reviewStatsGrid.prependHeaderRow();
//		 groupingHeader.join(
//				 groupingHeader.getCell("reviewStatus"), groupingHeader.getCell("policyReview"),
//		    groupingHeader.getCell("merReview"), groupingHeader.getCell("claimReview")).setText("Reviewing Pending");
		 
		setCompositionRoot(reviewStatsGrid);
	}
	
	public void addBeanToList(ReviewStatsDto reviewStats) {
		reviewStatsGrid.removeAllItems();
		reviewStatsGrid.addItem(new Object[]{reviewStats.getReviewStatus(), reviewStats.getMerReview(),reviewStats.getClaimsRetailReview(),
				reviewStats.getClaimsGmcReview()},1);
		
	}
	
	public void generateColumns(){
		reviewStatsGrid.removeGeneratedColumn("merReview");
		reviewStatsGrid.addGeneratedColumn("merReview", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button reportedBtn = new Button();
				ReviewStatsDto tableDto = (ReviewStatsDto)itemId;
				reportedBtn.setCaption(null != tableDto.getMerReview()? String.valueOf(tableDto.getMerReview()) : "");
				reportedBtn.setData(tableDto);
				reportedBtn.addStyleName(BaseTheme.BUTTON_LINK);
				reportedBtn.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						ReviewStatsDto tableDto = (ReviewStatsDto)event.getButton().getData();
						fireViewEvent(MenuPresenter.SHOW_PREVIOUS_FEEDBACK_LINK_HOME_PAGE,tableDto,SHAConstants.FB_REPORTED);
					}
				});
			return reportedBtn;
			}
		});
		
		reviewStatsGrid.removeGeneratedColumn("claimsRetailReview");
		reviewStatsGrid.addGeneratedColumn("claimsRetailReview", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button pendingBtn = new Button();
				ReviewStatsDto tableDto = (ReviewStatsDto)itemId;
				pendingBtn.setCaption(null != tableDto.getClaimsRetailReview()?String.valueOf(tableDto.getClaimsRetailReview()) : "");               
				pendingBtn.setData(tableDto);
				pendingBtn.addStyleName(BaseTheme.BUTTON_LINK);
				pendingBtn.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						ReviewStatsDto tableDto = (ReviewStatsDto)event.getButton().getData();
						fireViewEvent(MenuPresenter.SHOW_PREVIOUS_FEEDBACK_LINK_HOME_PAGE,tableDto,SHAConstants.FB_PENDING);
					}
				});
			return pendingBtn;
			}
		});
		
		reviewStatsGrid.removeGeneratedColumn("claimsGmcReview");
		reviewStatsGrid.addGeneratedColumn("claimsGmcReview", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button respondedBtn = new Button();
				ReviewStatsDto tableDto = (ReviewStatsDto)itemId;
				respondedBtn.setCaption(null != tableDto.getClaimsGmcReview()?String.valueOf(tableDto.getClaimsGmcReview()) : "");
				respondedBtn.setData(tableDto);
				respondedBtn.addStyleName(BaseTheme.BUTTON_LINK);
				respondedBtn.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						ReviewStatsDto tableDto = (ReviewStatsDto)event.getButton().getData();
						fireViewEvent(MenuPresenter.SHOW_PREVIOUS_FEEDBACK_LINK_HOME_PAGE,tableDto,SHAConstants.FB_RESPONDED);
					}
				});
			return respondedBtn;
			}
		});
	}

	
}
