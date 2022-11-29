package com.shaic.claim.reimbursement.createandsearchlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class CreateAndSearchLotTable extends GBaseTable<CreateAndSearchLotTableDTO>{
	
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","chkSelect","editpaymentdetails","viewdetails","intimationNo","claimNo","policyNo","rodNo","paymentStatus","product","cpuCode","paymentType",
		"ifscCode","beneficiaryAcntNo","branchName","typeOfClaim","approvedAmt","payeeName","gmcProposerName","gmcEmployeeName","payableAt","panNo","providerCode","emailID","dbSideRemark"};
	
	
	private static final Object[] SEARCH_LOT_COL_ORDER = new Object[] {
		"serialNumber","chkSelect","viewdetails","intimationNo","batchNumber","claimNo","policyNo","rodNo","paymentStatus","product","cpuCode","paymentType",
		"ifscCode","beneficiaryAcntNo","branchName","typeOfClaim","approvedAmt","payeeName","gmcProposerName","gmcEmployeeName","payableAt","panNo","providerCode",
		"emailID","accountBatchNo","lotNo","serviceTax","totalAmnt","tdsAmt","netAmnt","tdsPercentage","refNo","paymentReqDt","paymentReqUID"};

	//public List<Component> compList = null;
	public HashMap<Long,Component> compMap = null;
	public List<CreateAndSearchLotTableDTO> tableDataList = new ArrayList();
	public List<Long> tableDataKeyList = new ArrayList();
	//private CreateAndSearchLotTableDTO createandSearchLotDto;
	
	//CheckBox chkBox = null;
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<CreateAndSearchLotTableDTO>(CreateAndSearchLotTableDTO.class));
		generatecolumns();
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
	}
	@Override
	public void tableSelectHandler(CreateAndSearchLotTableDTO t) {
		fireViewEvent(MenuPresenter.CREATE_OR_SEARCH_LOT, t);
	}
	
	 public void setVisibleColumnsForSearchLot(){
		 generatecolumns();
		 table.setVisibleColumns(SEARCH_LOT_COL_ORDER);
		 localize(null);
	 }
	
	public void setVisibleColumnsForCreateLot(){
		generatecolumns();
		 table.setVisibleColumns(NATURAL_COL_ORDER);
		 localize(null);
	 }
	
	@Override
	public String textBundlePrefixString() {
		return "search-create-or-searchlot-";
	}
	
	private void generatecolumns(){
		//compList = new ArrayList<Component>();
		compMap = new HashMap<Long, Component>();
		table.removeGeneratedColumn("chkSelect");
		table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				  CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) itemId;
				  CheckBox chkBox = new CheckBox("");
				  if(null != tableDataList && !tableDataList.isEmpty())
				  {
					  for (CreateAndSearchLotTableDTO dto : tableDataList) {
						  if(dto.getClaimPaymentKey().equals(tableDTO.getClaimPaymentKey()))
						  {
							   if(null != dto.getChkSelect())
							  {
								  chkBox.setValue(dto.getChkSelect());
							  }
							   else if(("true").equalsIgnoreCase(dto.getCheckBoxStatus()))
							   {
								   chkBox.setValue(true);
							   }
							   else if(("false").equalsIgnoreCase(dto.getCheckBoxStatus()))
							   {
								   chkBox.setValue(false);
							   }
						  }
						
					}
				  }
				
					chkBox.setData(tableDTO);
					addListener(chkBox);
					//compList.add(chkBox);
					compMap.put(tableDTO.getClaimPaymentKey(), chkBox);
					//addListener(chkBox);
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
					
				final CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO)itemId;
				
				Button button = new Button("View Details");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
					
						showClaimsDMSView(tableDTO.getIntimationNo());
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		

		table.removeGeneratedColumn("editpaymentdetails");
		table.addGeneratedColumn("editpaymentdetails", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
					
				
				Button button = new Button("Edit Payment Details");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
					//To implement claims dms functionality.
						showEditPaymentDetailsView((CreateAndSearchLotTableDTO)itemId);
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});

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
					CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO)chkBox.getData();
					/**
					 * Added for issue#192.
					 * */
					if(null != tableDataList && !tableDataList.isEmpty())
					{
						for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDataList) {
							
							if(createAndSearchLotTableDTO.getClaimPaymentKey().equals(tableDTO.getClaimPaymentKey()))
							{
								if(value)
								{
									createAndSearchLotTableDTO.setCheckBoxStatus("true");
									//tableDTO.setChkSelect(true);
								}
								else
								{
									createAndSearchLotTableDTO.setCheckBoxStatus("false");
									//tableDTO.setChkSelect(false);
								}
							}
						}
					}
					
					
				}
			}
		});
	}
	
	public void setValueForCheckBox(Boolean value)
	{
		/**
		 * The tableData List will hold records selected from 
		 * multiple page. For example, when user clicks search 
		 * button, first 10 records are displayed.This is stored in tableDataList. Size of this list is 10.
		 * Again, when user clicks pagination, (i.e.) next page, again 10 records will be selected and will
		 * be added to the tableDataList. Now the size would be 20. If user wants to send all the records 
		 * which ever he has selected in each page for processing, then tableDataList will be sent.
		 * 
		 * Added for issue 192.
		 * 
		 * */
		
		

		List<CreateAndSearchLotTableDTO> searchTableDTOList = (List<CreateAndSearchLotTableDTO>) table.getItemIds();
		
		for (CreateAndSearchLotTableDTO searchTableDTO : searchTableDTOList) {
			for (CreateAndSearchLotTableDTO searchTabledto : tableDataList){
				if(searchTabledto.getClaimPaymentKey().equals(searchTableDTO.getClaimPaymentKey()))
					{
						if(value)
						{
							searchTabledto.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							break;
						}
						else
						{
							searchTabledto.setCheckBoxStatus("false");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							break;
						}
						
					}
			}
		}
		
		/*if(null != compList && !compList.isEmpty())
		{
			for (Component comp : compList) {
				CheckBox chkBox = (CheckBox) comp;
				chkBox.setValue(value);
			}
		}*/
		
	
		
	}
	
	public void setValueForSelectAllCheckBox(Boolean value)
	{
		List<CreateAndSearchLotTableDTO> searchTableDTOList = (List<CreateAndSearchLotTableDTO>) table.getItemIds();
		
		for (CreateAndSearchLotTableDTO searchTableDTO : searchTableDTOList) {
			for (CreateAndSearchLotTableDTO searchTabledto : tableDataList){
				if(searchTabledto.getClaimPaymentKey().equals(searchTableDTO.getClaimPaymentKey()))
					{
						if(value)
						{
							searchTableDTO.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							break;
						}
						else
						{
							searchTableDTO.setCheckBoxStatus("false");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							break;
						}
						
					}
			}
		}
		}
	
	public void setValueForSelectAllCheckBox(Boolean value,List<CreateAndSearchLotTableDTO> tableDataList)
	{
		List<CreateAndSearchLotTableDTO> searchTableDTOList = (List<CreateAndSearchLotTableDTO>) table.getItemIds();
		
		for (CreateAndSearchLotTableDTO searchTableDTO : searchTableDTOList) {
			for (CreateAndSearchLotTableDTO searchTabledto : tableDataList){
				if(searchTabledto.getClaimPaymentKey().equals(searchTableDTO.getClaimPaymentKey()))
					{
						if(value)
						{
							searchTableDTO.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							break;
						}
						else
						{
							searchTableDTO.setCheckBoxStatus("false");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							break;
						}
						
					}
			}
		}
		}
	
	public List<CreateAndSearchLotTableDTO> getTableAllItems()
	{
		return (List<CreateAndSearchLotTableDTO>)table.getItemIds();
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}
	
	public void showEditPaymentDetailsView(CreateAndSearchLotTableDTO tableDTO)
	{
		fireViewEvent(CreateAndSearchLotPresenter.SHOW_EDIT_PAYMENT_DETAILS_VIEW,tableDTO);
	}
	
	public void showClaimsDMSView(String intimationNo)
	{
		fireViewEvent(CreateAndSearchLotPresenter.SHOW_VIEW_DOCUMENTS,intimationNo);
	}
	
	public void addBeanToList(List<CreateAndSearchLotTableDTO> dtoList)
	{
		//int rowCount = 1;
		List<CreateAndSearchLotTableDTO> finalList = new ArrayList<CreateAndSearchLotTableDTO>();
		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : dtoList) {
			
			if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
			{
				createAndSearchLotTableDTO.setSerialNumber(rowCount);
				//rowCount++;
				finalList.add(createAndSearchLotTableDTO);
			}
		}
		table.addItems(dtoList);
	}

	public void setFinalTableList(List<CreateAndSearchLotTableDTO> tableRows) {/*
		if(null != tableRows && !tableRows.isEmpty())
		{
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableRows) {
				tableDataList.add(createAndSearchLotTableDTO);
			}
		}
		// TODO Auto-generated method stub
		
	*/

		Boolean isListEmpty = false;
	//	List<CreateAndSearchLotTableDTO> dtoList = new ArrayList<CreateAndSearchLotTableDTO>();
		if(null != tableRows && !tableRows.isEmpty())
		{
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableRows) {
				if(null != tableDataList && !tableDataList.isEmpty() && null != tableDataKeyList && !tableDataKeyList.isEmpty())
				{
					if(!tableDataKeyList.contains(createAndSearchLotTableDTO.getClaimPaymentKey()))
							{
								tableDataList.add(createAndSearchLotTableDTO);
								tableDataKeyList.add(createAndSearchLotTableDTO.getClaimPaymentKey());
							}
				}
				else
				{
					isListEmpty = true;
					break;
				}
			}
			if(isListEmpty)
			{
				for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableRows) {
					tableDataList.add(createAndSearchLotTableDTO);
					tableDataKeyList.add(createAndSearchLotTableDTO.getClaimPaymentKey());
				}
				
			}
			/*if(null != dtoList && !dtoList.isEmpty()){
				for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : dtoList) {
					tableDataList.add(createAndSearchLotTableDTO);
				}
			}*/
		}
		// TODO Auto-generated method stub
	
	}
	
	
	
	/*public List<CreateAndSearchLotTableDTO> setCompleteDataList()
	{
		return tableDataList;
	}*/

	
	public List<CreateAndSearchLotTableDTO> getFinalDataList()
	{
		return tableDataList;
	}
	
	public List<CreateAndSearchLotTableDTO> getTableItems()
	{
		return tableDataList;
	}
	
	public void resetTableDataList()
	{
		if(null != tableDataList)
		{
			tableDataList.clear();
		}
		if(null != tableDataKeyList)
		{
			tableDataKeyList.clear();
		}
	}

	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,EditPaymentDetailsView editPaymentView) {
		editPaymentView.populatePreviousPaymentDetails(tableDTO);
	
		
	}

	public void setRowColor(final CreateAndSearchLotTableDTO dto){
		
		
		table.setCellStyleGenerator(new CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				CreateAndSearchLotTableDTO dto1 = (CreateAndSearchLotTableDTO) itemId;
				if(dto1 != null){
					String colourFlag = null != dto1.getRecStatusFlag() ? dto1.getRecStatusFlag():"";
					String batchProcessFlag = null != dto1.getBatchProcessFlag() ? dto1.getBatchProcessFlag():"";
				

				if(colourFlag.equals("E")){

						
					if(batchProcessFlag.equals(SHAConstants.PAYMENT_VALIDATION_ERROR)){
							
							return "orange";
					}
					else
					{
					return "yellow";
					}
				}
				else if(colourFlag.equals("F")){
					
					return "red";
				}
				else if(colourFlag.equals("R"))
				{
					return "amber";
				}
				else
				{
					return "none";
				}
			
			}
				else{
					return "none";
				}
			}
	
		});
		
	}
	
	/*public Object getSelectedRowId(ArrayList<Object> itemIds ){		
		
		for(Object id:itemIds){
			createandSearchLotDto = (CreateAndSearchLotTableDTO)id;		
			return id;
			 
		}		
		return null;
	}*/

	public void setRowColor(String colourFlag) {
		// TODO Auto-generated method stub
		
	}
	
}
	
	 
	 
