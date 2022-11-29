package com.shaic.arch.table;

import java.util.Arrays;
import java.util.Collection;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.addon.cdiproperties.annotation.TableProperties;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.cdi.CDIUI;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.Table.CellStyleGenerator;


@SuppressWarnings({"serial","unchecked"})
public abstract class GBaseTable<T> extends ViewComponent{

	@Inject
	@TableProperties(nullSelectionAllowed = false, sizeFull = true, immediate = true, columnCollapsingAllowed = true, columnReorderingAllowed = true, selectable = true)
	protected Table table;

	
	protected HorizontalLayout pager;
	
	private PagerUI pageUI;
	
	private  VerticalLayout layout;
	
	@Inject
	private TextBundle tb;
	
	private Searchable searchable;
	
	FormLayout fLayout;
	
	public Label totalRocordsTxt;
	
	protected int rowCount = 0;
	
	protected boolean serialNumberFlag = false; 
	private HorizontalLayout captionLayout;

	@SuppressWarnings("deprecation")
	public void init(String tableCaption, Boolean isNeedAddButton, Boolean showPagerFlag) {
		preInit();
		setSizeFull();
		pageUI = new PagerUI();
		 
		pager = new HorizontalLayout();
		if (tableCaption != null && !tableCaption.equals(""))
		{
			//table.setCaption(tableCaption);
			
		}
		table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            private static final long serialVersionUID = 2068314108919135281L;

            public void itemClick(ItemClickEvent event) {
            	if (event.isDoubleClick()) {
            		final T bean = (T) event.getItemId();
            		if (bean instanceof AbstractTableDTO)
            		{
            			AbstractTableDTO tableDTO = (AbstractTableDTO) bean;
            			if(getUI() != null){
            				tableDTO.setUsername((String)getUI().getSession().getAttribute(BPMClientContext.USERID));
            				tableDTO.setPassword((String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD));
            			}
            		}
            		if (bean != null) {
            			tableSelectHandler(bean);
            		}
            	}
            }
        });
		
		table.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(final com.vaadin.v7.data.Property.ValueChangeEvent event) {
				
			}
		});
		
		 layout = new VerticalLayout();
		 if(showPagerFlag){
			 layout.addComponent(pageUI);
		     pageUI.addListener(new PagerListener() {
				@Override
				public void changePage() {
					
					searchable.doSearch();
				}
			});
		 }
		 
		if(tableCaption!= null)
		{
			Label lblCaption = new Label(tableCaption);
			captionLayout = new HorizontalLayout(lblCaption);		
			
			
		}
		
		fLayout = new FormLayout();
		layout.addComponent(captionLayout);
		layout.addComponent(fLayout);
		layout.setComponentAlignment(captionLayout , Alignment.TOP_CENTER);
	    layout.addComponent(table);
	    layout.setSpacing(false);
	    setCompositionRoot(layout);   
		initTable();
		localize(null);
		this.serialNumberFlag = Arrays.asList(table.getVisibleColumns()).contains("serialNumber");
	}

	protected void preInit() {
	}
	
	public abstract void removeRow();
	public abstract void initTable();
	

	public void setTableList(final Collection<T> list) {
		table.removeAllItems();
		this.rowCount = 0;
		for (final T bean : list) {
			
			if (serialNumberFlag)
			{
				AbstractTableDTO tableDTO = (AbstractTableDTO) bean;
				rowCount++;
				tableDTO.setSerialNumber(rowCount);
			}
			table.addItem(bean);
		}
		table.sort();
	}
	
	public void setTableList(final Page<T> page) {
		this.pageUI.resetPage();
		this.pageUI.setPageDetails(page);
		this.setTableList(page.getPageItems());
 		
		table.removeAllItems();
		this.rowCount = 0;
		int pageNumber = page.getPageNumber();
		if(pageNumber > 1){
			rowCount =(pageNumber-1) * 10; 	
		}
		for (final T bean : page.getPageItems()) {
			
			if (serialNumberFlag)
			{
				AbstractTableDTO tableDTO = (AbstractTableDTO) bean;
				rowCount++;
				tableDTO.setSerialNumber(rowCount);
			}
			table.addItem(bean);
		}
		table.sort();
		if(fLayout != null && !page.getIsDbSearch()) {
			fLayout.setMargin(false);
			fLayout.removeAllComponents();
			totalRocordsTxt = new Label((page.getTotalRecords() != null ? String.valueOf(page.getTotalRecords()) : "0"));
			totalRocordsTxt.setCaption("Total Number Of Records :");
			fLayout.addComponent(totalRocordsTxt);
		}
		
		table.setPageLength(table.getItemIds().size() + 1);
	}
	
	public void setTableList(final Page<T> page,String dummy) {
		this.pageUI.resetPage();
		this.pageUI.setPageDetails(page);
		//this.setTableList(page.getPageItems());
		table.removeAllItems();
		this.rowCount = 0;
		int pageNumber = page.getPageNumber();
		if(pageNumber > 1){
			rowCount =(pageNumber-1) * page.getPageItems().size(); 	
		}
		for (final T bean : page.getPageItems()) {
			
			if (serialNumberFlag)
			{
				AbstractTableDTO tableDTO = (AbstractTableDTO) bean;
				rowCount++;
				tableDTO.setSerialNumber(rowCount);
			}
			table.addItem(bean);
		}
		table.sort();
		
		if((SHAConstants.CREATE_BATCH).equalsIgnoreCase(dummy) || (SHAConstants.CREATE_AND_SEARCH_LOT).equalsIgnoreCase(dummy)
				|| (SHAConstants.PROCESS_RRC_REQUEST).equalsIgnoreCase(dummy) || (SHAConstants.REVIEW_RRC_REQUEST).equalsIgnoreCase(dummy) || SHAConstants.SEARCH_USER_REALLOCATION.equalsIgnoreCase(dummy))
		{
			if(null != fLayout  ) {
				fLayout.setMargin(false);
				fLayout.removeAllComponents();
				totalRocordsTxt = new Label((page.getTotalRecords() != null ? String.valueOf(page.getTotalRecords()) : "0"));
				totalRocordsTxt.setCaption("Total Number Of Records :");
				fLayout.addComponent(totalRocordsTxt);
			}
		}
		
		table.setPageLength(table.getItemIds().size() + 1);
	}


	public void setHasNextPage(boolean flag)
	{
		this.pageUI.hasMoreRecords(flag);
	}

	public void addSearchListener(Searchable searable)
	{
		this.searchable = searable;
	}
	
	public void addBeanToList(final T bean) {
		
		if (bean instanceof AbstractTableDTO)
		{
			AbstractTableDTO tableDTO = (AbstractTableDTO) bean;
			rowCount++;
			tableDTO.setSerialNumber(rowCount);
		}
		
		table.addItem(bean);
		table.sort();
	}

	public void setValue(final T bean) {
		table.setValue(bean);
	}

	public Item getSelectedItem() {
		return table.getItem(table.getValue());
	}
	
	protected void localize(
            @TextBundleUpdated final ParameterDTO parameterDto) {
        for (final Object propertyId : table.getVisibleColumns()) {
        	if (!propertyId.equals("serialNumber"))
        	{
        		final String header = tb.getText(textBundlePrefixString() + String.valueOf(propertyId).toLowerCase());
                table.setColumnHeader(propertyId, header);	
        	}
        	else
        	{
        		final String header = tb.getText("cmn-serial-number");
                table.setColumnHeader(propertyId, header);	
        	}
        }
    }
	
	public Pageable getPageable()
	{
		if(this.pageUI!=null)
			return this.pageUI.getPageable();
		else
			return null;
	}
	
	public abstract void tableSelectHandler(T t);
	
	public abstract String textBundlePrefixString();
	
	public Table getTable(){
		return this.table;
	}
	
	public void resetTable()
	{
		removeRow();
		if(fLayout != null) {
			fLayout.removeAllComponents();
		}
		this.pageUI.resetPage();
	}

	public VerticalLayout getLayout() {
		return layout;
	}

	public FormLayout getfLayout() {
		return fLayout;
	}

	public HorizontalLayout getCaptionLayout() {
		return captionLayout;
	}	
	
@SuppressWarnings("deprecation")
public void setRowColor(final T bean){
		
		
		table.setCellStyleGenerator(new CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				
				AbstractTableDTO dto1 = (AbstractTableDTO) itemId;
				if(dto1 != null){
				String colourFlag = null != dto1.getColorCode() ? dto1.getColorCode():"";
				//System.out.println("-------No Of times loop executes-----");
				String colourFlagCell = null != dto1.getColorCodeCell() ? dto1.getColorCodeCell():"";
				if(propertyId != null && colourFlagCell.equals("OLIVE")) {
					if(propertyId.equals("crmFlagged")){
						//source.setColumnIcon("crmFlagged", new ThemeResource("../runo/icons/16/ok.png"));
						return "olive";
					}
				}
				if(propertyId != null && colourFlagCell.equals("VIP")) {
					if(propertyId.equals("crmFlagged")){
						//source.setColumnIcon("crmFlagged", new ThemeResource("../runo/icons/16/ok.png"));
						return "vip";
					}
				}
				if(propertyId != null && colourFlagCell.equals("CRMVIP")) {
					if(propertyId.equals("crmFlagged")){
						//source.setColumnIcon("crmFlagged", new ThemeResource("../runo/icons/16/ok.png"));
						return "crmvip";
					}
				}
				if(propertyId != null && colourFlagCell.equals("GREENFLAG")) {
					if(propertyId.equals("crmFlagged")){
						return "greenflag";
					}
				}
				if(propertyId != null && colourFlagCell.equals("GREYFLAG")) {
					if(propertyId.equals("crmFlagged")){
						return "greyflag";
					}
				}
				if(colourFlag.equals("YELLOW")){
					
					return "yellow";
				}
				else if(colourFlag.equals("RED")){
					
					return "red";
				}
				else if(colourFlag.equals("GREEN"))
				{
					return "green";
				}
				else
				{
					return table.getStyleName();
				}
				
			    }
				else{
					return table.getStyleName();
				}
			}
	
		});
		
	}
	
public void setCellColour(final T bean){
	table.setCellStyleGenerator(new CellStyleGenerator() {

		@Override
		public String getStyle(Table source, Object itemId, Object propertyId) {
			AbstractTableDTO dto1 = (AbstractTableDTO) itemId;
			if(dto1 != null){
				String colourFlag = null != dto1.getColorCode() ? dto1.getColorCode():"";
			if(propertyId != null && colourFlag.equals("OLIVE")) {
				if(propertyId.equals("crmFlagged")){
					//source.setColumnIcon("crmFlagged", new ThemeResource("../runo/icons/16/ok.png"));
					return "olive";
				}
			}}
			return table.getStyleName();
		}
		
		
	});
	
	
}

public void setCelldescription(final T bean){
	table.setItemDescriptionGenerator(new ItemDescriptionGenerator() {                             
		public String generateDescription(Component source, Object itemId, Object propertyId) {
			AbstractTableDTO dto1 = (AbstractTableDTO) itemId;
			
			if(dto1 != null){
				String colourFlag = null != dto1.getColorCode() ? dto1.getColorCode():"";
				String colourFlagCell = null != dto1.getColorCodeCell() ? dto1.getColorCodeCell():"";
				String rowDesc = null != dto1.getRowDescRow() ? dto1.getRowDescRow():"";
				
				if(propertyId != null && colourFlagCell.equals("GREENFLAG")) {
					if(propertyId.equals("crmFlagged")){
						  return rowDesc;
					}
				}   
			}
			return table.getStyleName();
			}
		});
	}

}
