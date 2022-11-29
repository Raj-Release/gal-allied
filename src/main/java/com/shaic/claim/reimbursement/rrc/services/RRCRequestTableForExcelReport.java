/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

/**
 * @author ntv.vijayar
 *
 */
public class RRCRequestTableForExcelReport extends GBaseTable<SearchRRCRequestTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","rrcRequestNo", "dateOfRequestForTable" , "requestorName", "cpuCode","intimationNo" ,"initialSumInsured","presentSumInsured","productCode","productName", 
		"pedDisclosed","claimType","diag", "management" , "patientName", "hospitalName","hospitalType","initialPEDRecommendation","pedSuggestions","pedName","amountClaimed", "settledAmount" ,
		"amountSaved","categoryValue","status","statusDate", "initiateRRCRemarks" ,"processRRCRemarks","creditsEmployeeName","creditsEmployeeName2","creditsEmployeeName3","creditsEmployeeName4",
		"creditsEmployeeName5","creditsEmployeeName6","creditsEmployeeName7","lapseEmployeeName","lapseEmployeeName2","lapseEmployeeName3","lapseEmployeeName4","lapseEmployeeName5",
		"lapseEmployeeName6","lapseEmployeeNmae7"
		}; 
	
	//private Map<String, Component> compMap = new HashMap<String, Component>();
	private List<Component> compList = new ArrayList<Component>();
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchRRCRequestTableDTO>(SearchRRCRequestTableDTO.class));
		//generateColumn();
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		
	}
	
	/*private void generateColumn()
	{
		table.removeGeneratedColumn("chkSelect");
		table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
					SearchRRCRequestTableDTO tableDTO = (SearchRRCRequestTableDTO) itemId;
					CheckBox chkBox = new CheckBox("Select All");
					chkBox.setCaption("Select All");
					if(("true").equalsIgnoreCase(tableDTO.getCheckBoxStatus()))
					{
						chkBox.setValue(true);
					}
					else
					{
						chkBox.setValue(false);
					}
					//compList.put("checkBox", chkBox);
					compList.add(chkBox);
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						tableSelectHandler((SearchRRCRequestTableDTO)itemId);
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return chkBox;
			}
		});
		
		table.removeGeneratedColumn("viewdetails");
		table.setPageLength(3);
		
		table.addGeneratedColumn("viewdetails", new Table.ColumnGenerator() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
					
				
				Button button = new Button("View Details");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						//tableSelectHandler((SearchRRCRequestTableDTO)itemId);
						invokeView((SearchRRCRequestTableDTO)itemId);
						//fireViewEvent(MenuPresenter.SHOW_SEARCH_RRC_REQUEST_VIEW, (SearchRRCRequestTableDTO)itemId,this);
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
	
	}*/
	
	public void invokeView(SearchRRCRequestTableDTO t) {
		fireViewEvent(MenuPresenter.SHOW_SEARCH_RRC_REQUEST_VIEW, t,this);
	}

	@Override
	public void tableSelectHandler(
			SearchRRCRequestTableDTO t) {
		
		//Table handler to be devloped.
	//fireViewEvent(MenuPresenter.SHOW_SEARCH_RRC_REQUEST_VIEW, t,this);
	
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-rrc-request-excel-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	public void showSearchRRCRequestView(RRCDTO rrcDTO)
	{
		fireViewEvent(SearchRRCRequestPresenter.SHOW_RRC_REQUEST_VIEW,rrcDTO);
	}
	
	public void setValueForCheckBox(Boolean value)
	{
		List<SearchRRCRequestTableDTO> searchTableDTOList = (List<SearchRRCRequestTableDTO>) table.getItemIds();
		for (SearchRRCRequestTableDTO searchTableDTO : searchTableDTOList) {
			
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
	
	public Table getTableEX(){
		return this.table;
		
	}
	
	public List<SearchRRCRequestTableDTO> getTableAllItems()
	{
		return (List<SearchRRCRequestTableDTO>)table.getItemIds();
	}

	public void addBeanToList(List<SearchRRCRequestTableDTO> dtoList)
	{
		int rowCount = 1;
		List<SearchRRCRequestTableDTO> finalList = new ArrayList<SearchRRCRequestTableDTO>();
		for (SearchRRCRequestTableDTO searchRRCTableDTO : dtoList) {
			
			if(("true").equalsIgnoreCase(searchRRCTableDTO.getCheckBoxStatus()))
			{
				searchRRCTableDTO.setSerialNumber(rowCount);
				rowCount++;
				finalList.add(searchRRCTableDTO);
			}
		}
		table.addItems(finalList);
	}
	
	
	
	public void setTableValues(List<SearchRRCRequestTableDTO> tableRows)
	{
		setTableList(tableRows);	
		for (SearchRRCRequestTableDTO searchRRCRequestTableDTO : tableRows) {
			
			String[] creaditListArray = searchRRCRequestTableDTO.getCreditEmpList();
			String[] lapseListArray = searchRRCRequestTableDTO.getLapseEmpList();
			
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
					
			//	String value =  (String) source.getData();
				TextField txtCredit = new TextField();		
				SearchRRCRequestTableDTO tableDTO = (SearchRRCRequestTableDTO) itemId;
				txtCredit.setData(tableDTO);
				txtCredit.setValue(value);
				//Vaadin8-setImmediate() txtCredit.setImmediate(true);
				
				return txtCredit;
			}
		});
	}
	

	
	
}



