package com.shaic.claim.reports.branchManagerFeedBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.WeakHashMap;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.common.base.Converter;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.client.data.DataSource;
import com.vaadin.v7.client.widget.grid.CellStyleGenerator;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Grid.CellReference;
import com.vaadin.v7.ui.Grid.HeaderCell;
import com.vaadin.v7.ui.Grid.HeaderRow;
import com.vaadin.v7.ui.Grid.SelectionMode;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.renderers.ImageRenderer;
import com.vaadin.v7.ui.themes.BaseTheme;
/**
 * Part of CR R1238
 * @author Lakshminarayana
 *
 */
public class BranchManagerFeedBackReportBranchTable extends ViewComponent {   //  BranchManagerFeedBackReportDto
	
	
	Grid bmFeedbackReportGrid;
	
	public Object[] VISIBLE_COLUMNS = new Object[] {"branchCode", "branchName", "ratingImg",
			"merReported", "merPending", "merResponded", 
			"claimRetailReported", "claimRetailPending", "claimRetailResponded",
			"claimGmcReported", "claimGmcPending", "claimGmcResponded"};
	
	BeanItemContainer<BranchManagerFeedBackReportDto> dataContainer;

	public void initView(){
		dataContainer = new BeanItemContainer<BranchManagerFeedBackReportDto>(BranchManagerFeedBackReportDto.class);
		bmFeedbackReportGrid = new Grid();
		bmFeedbackReportGrid.setSelectionMode(SelectionMode.NONE);
		
		for (Object object : VISIBLE_COLUMNS) {
			bmFeedbackReportGrid.addColumn(object);
		}
		bmFeedbackReportGrid.setContainerDataSource(dataContainer); 
		
		bmFeedbackReportGrid.getColumn("ratingImg").setHeaderCaption("Rating").setWidth(115.0);
		
		HeaderRow groupingHeader = bmFeedbackReportGrid.prependHeaderRow();
		
//		HeaderRow groupingHeader = bmFeedbackReportGrid.getHeaderRow(0);
		bmFeedbackReportGrid.getColumn("merReported").setHeaderCaption("Reported");
		bmFeedbackReportGrid.getColumn("merPending").setHeaderCaption("Pending");
		bmFeedbackReportGrid.getColumn("merResponded").setHeaderCaption("Responded");
		
		bmFeedbackReportGrid.getColumn("claimRetailReported").setHeaderCaption("Reported");
		bmFeedbackReportGrid.getColumn("claimRetailPending").setHeaderCaption("Pending");
		bmFeedbackReportGrid.getColumn("claimRetailResponded").setHeaderCaption("Responded");
		
		bmFeedbackReportGrid.getColumn("claimGmcReported").setHeaderCaption("Reported");
		bmFeedbackReportGrid.getColumn("claimGmcPending").setHeaderCaption("Pending");
		bmFeedbackReportGrid.getColumn("claimGmcResponded").setHeaderCaption("Responded");
		
		groupingHeader.join(
				groupingHeader.getCell("merReported"), groupingHeader.getCell("merPending"),
				groupingHeader.getCell("merResponded")).setText("MER");

		groupingHeader.join(
				 groupingHeader.getCell("claimRetailReported"), groupingHeader.getCell("claimRetailPending"),
				 groupingHeader.getCell("claimRetailResponded")).setText("Claims - Retail ");
			

		 groupingHeader.join(
				 groupingHeader.getCell("claimGmcReported"), groupingHeader.getCell("claimGmcPending"),
				 groupingHeader.getCell("claimGmcResponded")).setText("Claims - GMC");
		 
		 groupingHeader.setStyleName("centerAligned");
		 
		 bmFeedbackReportGrid.setStyleName("defaultGridHeader");
		 bmFeedbackReportGrid.setStyleName("groupColStyle");
		 
		 /*bmFeedbackReportGrid.setCellStyleGenerator(new Grid.CellStyleGenerator() {
			 @Override
	         public String getStyle(Grid.CellReference cellReference) {
				 if ("Policy".equals(cellReference.getPropertyId()) || "MER".equals(cellReference.getPropertyId()) || "Claim".equals(cellReference.getPropertyId())) {
					 // when the current cell is  Policy, align text to right
					 return "centerAligned";
				 } else {
					 // otherwise, align text to left
					 return "leftAligned";
				 }
			 }
		   }); */ 
		 
		 bmFeedbackReportGrid.setWidth("100%");
		 setCompositionRoot(bmFeedbackReportGrid);
		 
		// Set the image renderer
		 bmFeedbackReportGrid.getColumn("ratingImg").setRenderer(new ImageRenderer(),
		     new com.vaadin.v7.data.util.converter.Converter<Resource, String>() {
		         
		         @Override
		         public Class<String> getModelType() {
		             return String.class;
		         }

		         @Override
		         public Class<Resource> getPresentationType() {
		             return Resource.class;
		         }

				@Override
				public String convertToModel(Resource value,
						Class<? extends String> targetType, Locale locale)
						throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
					
					return "NOT NEEDED";
				}

				@Override
				public Resource convertToPresentation(String value,
						Class<? extends Resource> targetType, Locale locale)
						throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
					
					return new ThemeResource("images/" + value);
				}
		 });
	
	}	
	
	public void setTableList(List<BranchManagerFeedBackReportDto> resultList){
		dataContainer.removeAllItems();
		dataContainer.addAll(resultList);
		bmFeedbackReportGrid.setContainerDataSource(dataContainer);
	}
	
	public List<BranchManagerFeedBackReportDto> getTableList(){
		
		List<BranchManagerFeedBackReportDto> resultList = new ArrayList<BranchManagerFeedBackReportDto>();
		BeanItemContainer<BranchManagerFeedBackReportDto> container = (BeanItemContainer<BranchManagerFeedBackReportDto>) bmFeedbackReportGrid.getContainerDataSource();
		
		if(container != null && container.size() >0)
			resultList = container.getItemIds();
		
		return resultList;
	}
	
	
	/*@Override
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
	}*/
	
}
