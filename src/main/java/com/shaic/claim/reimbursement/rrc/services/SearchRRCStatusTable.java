
package com.shaic.claim.reimbursement.rrc.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class SearchRRCStatusTable extends GBaseTable<SearchRRCStatusTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"chkSelect","serialNumber","intimationNo", "rrcRequestNo", "cpuCode","dateOfRequestForTable", "requestorId",
		//"requestorName", "rrcRequestType","eligibilityValue","rrcType","viewdetails"}; 
		"requestorName", "rrcRequestType","eligibilityValue","viewdetails"};
	
	//private Map<String, Component> compMap = new HashMap<String, Component>();
	private List<Component> compList = new ArrayList<Component>();
	public HashMap<String,Component> compMap = null;
	//private List<SearchRRCStatusTableDTO>  tableRows = null;
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	/*public void setTableValuelist(List<SearchRRCStatusTableDTO>  tableRows)
	{
		this.tableRows = tableRows;
	}*/
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchRRCStatusTableDTO>(SearchRRCStatusTableDTO.class));
		generateColumn();
		table.setPageLength(3);
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("245px");
		
		
	}
	
	private void generateColumn()
	{
		compMap = new HashMap<String, Component>();
		table.removeGeneratedColumn("chkSelect");
		table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
					SearchRRCStatusTableDTO tableDTO = (SearchRRCStatusTableDTO) itemId;
					CheckBox chkBox = new CheckBox("");
					chkBox.setData(tableDTO);
					addListener(chkBox);
					compList.add(chkBox);
					compMap.put(tableDTO.getRrcRequestNo(), chkBox);
				/*button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						tableSelectHandler((SearchRRCStatusTableDTO)itemId);
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);*/
				return chkBox;
			}
		});
		
		table.removeGeneratedColumn("viewdetails");
		
		
		table.addGeneratedColumn("viewdetails", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
					
				
				Button button = new Button("View Details");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						//tableSelectHandler((SearchRRCStatusTableDTO)itemId);
						invokeView((SearchRRCStatusTableDTO)itemId);
						//fireViewEvent(MenuPresenter.SHOW_SEARCH_RRC_REQUEST_VIEW, (SearchRRCStatusTableDTO)itemId,this);
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
	/*	for (SearchRRCStatusTableDTO SearchRRCStatusTableDTO : tableRows) {
			
			String[] creaditListArray = SearchRRCStatusTableDTO.getCreditEmpList();
			String[] lapseListArray = SearchRRCStatusTableDTO.getLapseEmpList();
			
			for(int i=0;i<creaditListArray.length;i++)
			{
				String columnName = "Credit"+ (i+1);
				dynamicGenerateCreditAndLapse(columnName,creaditListArray[i]);
				
				
		}
			for(int i=0;i<lapseListArray.length;i++)
			{
				String columnName = "Lapse"+ (i+1);
				dynamicGenerateCreditAndLapse(columnName,lapseListArray[i]);
		}
	}*/
		
		
	}
	
	private void addListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();
					SearchRRCStatusTableDTO tableDTO = (SearchRRCStatusTableDTO)chkBox.getData();
					if(value)
					{
						tableDTO.setCheckBoxStatus("true");
					}
					else
					{
						tableDTO.setCheckBoxStatus("false");
					}
					
				}
			}
		});
	}


	public void invokeView(SearchRRCStatusTableDTO t) {
//		fireViewEvent(MenuPresenter, t,this);
		fireViewEvent(MenuPresenter.SHOW_RRC_STATUS_SCREEN_VIEW, t,this);
	}

	@Override
	public void tableSelectHandler(
			SearchRRCStatusTableDTO t) {
		
		//Table handler to be devloped.
	//fireViewEvent(MenuPresenter.SHOW_SEARCH_RRC_REQUEST_VIEW, t,this);
	
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-rrc-request-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	public void showRRCStatusScreenView(RRCDTO rrcDTO)
	{
		fireViewEvent(SearchRRCStatusPresenter.SHOW_RRC_STATUS_VIEW,rrcDTO);
	}
	
	public void setValueForCheckBox(Boolean value,List<SearchRRCStatusTableDTO> totalList)
	{
	//	List<SearchRRCStatusTableDTO> searchTableDTOList = (List<SearchRRCStatusTableDTO>) table.getItemIds();
		if(null != totalList && !totalList.isEmpty())
		{
		for (SearchRRCStatusTableDTO searchTableDTO : totalList) {
			
			if(value)
				searchTableDTO.setCheckBoxStatus("true");
			else
				searchTableDTO.setCheckBoxStatus("false");
			
		/*	Component comp = compMap.get("checkBox");
			CheckBox chkBox = (CheckBox) comp;
			chkBox.setValue(value);*/
		
			//generateColumn();
			/*Item item = table.getItem(searchTableDTO);
			Property itemProperty = item.getItemProperty("chkSelect");
			if (itemProperty != null)
				itemProperty.setValue(value);*/
		}
		}
		if(null != compList && !compList.isEmpty())
		{
			for (Component comp : compList) {
				CheckBox chkBox = (CheckBox) comp;
				chkBox.setValue(value);
			}
		}
		/*Set<Entry<String, Component>> entrySet = compMap.entrySet();
		Iterator entries = entrySet.iterator();
		while(entries.hasNext())
		{
			Entry thisEntry = (Entry) entries.next();
			Component comp = (Component)thisEntry.getValue();
			CheckBox chkBox = (CheckBox)comp;
			chkBox.setValue(value);
			
		}*/
		
	}
	
	public List<SearchRRCStatusTableDTO> getTableAllItems()
	{
		return (List<SearchRRCStatusTableDTO>)table.getItemIds();
	}
	
	
	public void setTableValues(List<SearchRRCStatusTableDTO>  tableRows)
	{
				
		 setTableList(tableRows);	
		for (SearchRRCStatusTableDTO SearchRRCStatusTableDTO : tableRows) {
			
			String[] creaditListArray = SearchRRCStatusTableDTO.getCreditEmpList();
			String[] lapseListArray = SearchRRCStatusTableDTO.getLapseEmpList();
			
			for(int i=0;i<creaditListArray.length;i++)
			{
				String columnName = "Credit"+ (i+1);
				dynamicGenerateCreditAndLapse(columnName,creaditListArray[i]);
				
				
		}
			for(int i=0;i<lapseListArray.length;i++)
			{
				String columnName = "Lapse"+ (i+1);
				dynamicGenerateCreditAndLapse(columnName,lapseListArray[i]);
		}
	}
		
	}
	
	
	public void dynamicGenerateCreditAndLapse(String columnName,final String value)
	{
		table.removeGeneratedColumn(columnName);
		//table.setData(value);
		table.addGeneratedColumn(columnName, new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				//String value =  (String) source.getData();
				TextField txtCredit = new TextField();		
				SearchRRCStatusTableDTO tableDTO = (SearchRRCStatusTableDTO) itemId;
				txtCredit.setData(tableDTO);
				txtCredit.setValue(value);
				//Vaadin8-setImmediate() txtCredit.setImmediate(true);
				
				return txtCredit;
			}
		});
	}
	
	public void setValueForSelectAllCheckBox(Boolean value)
	{
		List<SearchRRCStatusTableDTO> searchTableDTOList = (List<SearchRRCStatusTableDTO>) table.getItemIds();
		
		for (SearchRRCStatusTableDTO searchTableDTO : searchTableDTOList) {
		
			if(null != compMap && !compMap.isEmpty())
			{
				CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getRrcRequestNo());
				chkBox.setValue(value);
			}
								
		}
	}

}



