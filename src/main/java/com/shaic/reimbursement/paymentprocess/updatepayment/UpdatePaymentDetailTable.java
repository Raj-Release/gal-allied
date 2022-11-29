package com.shaic.reimbursement.paymentprocess.updatepayment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class UpdatePaymentDetailTable extends GBaseTable<UpdatePaymentDetailTableDTO>{


	private Window popup; 
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","chkSelect","viewdetails","intimationNo", "maApprovedDate", "cpucode", 
		"cpuName","paymentCpuCode","paymentCpuName","proposerName","employeeName","payeeName","paymentType","reasonForChange","ifscCode","beneficiaryAcno","bankName",
		"branchName","branchCity","payableCity","emailID","typeOfClaim","approvedAmt",
		"productName"}; 
	
	private final static Object[] SEARCH_BATCH_CIL_ORDER = new Object[]{"serialNumber","chkSelect","accountBatchNo","paymentReqDateValue","userId","typeOfClaim","claimCount","showdetails"};
	
	//public List<Component> compList = null;
	public HashMap<Long,Component> compMap = null;
	public List<UpdatePaymentDetailTableDTO> tableDataList = new ArrayList();
	public List<Long> tableDataKeyList = new ArrayList();
	Double approvedAmnt = 0d;
	
	@Inject
	UpdatePaymentDetailTableForPopUp tableForPopUp;
	

	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<UpdatePaymentDetailTableDTO>(UpdatePaymentDetailTableDTO.class));	
		generatecolumns();
		table.setValidationVisible(false);
		table.setVisibleColumns(NATURAL_COL_ORDER);
	//	table.sort("numberofdays", false);

	}

	@Override
	public void tableSelectHandler(
			UpdatePaymentDetailTableDTO t) {
		// TODO Auto-generated method stub
		
	}
	public void setVisibleColumnForSearchBatch()
	{
		table.setVisibleColumns(SEARCH_BATCH_CIL_ORDER);
	}
	public void setVisiblecolumnForCreateBatch()
	{
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public String textBundlePrefixString() {
		
		return "update-payment-detail-";
	}
	
	
	private void generatecolumns(){
		
	//	compList = new ArrayList<Component>();
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
					
				UpdatePaymentDetailTableDTO tableDTO = (UpdatePaymentDetailTableDTO) itemId;
					CheckBox chkBox = new CheckBox("");
					
					 if(null != tableDataList && !tableDataList.isEmpty())
					  {/*
						  for (UpdatePaymentDetailTableDTO dto : tableDataList) {
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
								   //compMap.put(dto.getClaimPaymentKey(), chkBox);
							  }
							
						}
					  */}
					
					chkBox.setData(tableDTO);
					addListener(chkBox);
					//compMap.put(tableDTO.getClaimPaymentKey(), chkBox);
				//	compList.add(chkBox);
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
					
				final UpdatePaymentDetailTableDTO tableDTO = (UpdatePaymentDetailTableDTO)itemId;
				
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
		

		table.removeGeneratedColumn("showdetails");
		
		table.addGeneratedColumn("showdetails", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
					
				
				Button button = new Button("Show Details");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
					
											
						//fireViewEvent(UpdatePaymentDetailPresenter.CREATE_BATCH_SHOW_DETAILS,(CreateAndSearchLotTableDTO)itemId);		    	
				    				
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
					UpdatePaymentDetailTableDTO tableDTO = (UpdatePaymentDetailTableDTO)chkBox.getData();
					/**
					 * Added for issue#192.
					 * */
					if(null != tableDataList && !tableDataList.isEmpty())
					{/*
						for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : tableDataList) {
							
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
					*/}
					
					
				
					
					
					/*boolean value = (Boolean) event.getProperty().getValue();
					CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO)chkBox.getData();
					if(value)
					{
						tableDTO.setCheckBoxStatus("true");
					}
					else
					{
						tableDTO.setCheckBoxStatus("false");
					}*/
					
				}
			}
		});
	}
	
	/*public void setValueForCheckBox(Boolean value)
	{
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
		
		if(null != compList && !compList.isEmpty())
		{
			for (Component comp : compList) {
				CheckBox chkBox = (CheckBox) comp;
				chkBox.setValue(value);
			}
		}
		//System.out.println("----------");
	}*/
	
	public void setValueForCheckBox(Boolean value)
	{
		List<UpdatePaymentDetailTableDTO> searchTableDTOList = (List<UpdatePaymentDetailTableDTO>) table.getItemIds();
		
		for (UpdatePaymentDetailTableDTO searchTableDTO : searchTableDTOList) {
			//for (CreateAndSearchLotTableDTO searchTabledto : tableDataList){
				//if(searchTabledto.getClaimPaymentKey().equals(searchTableDTO.getClaimPaymentKey()))
					{
						if(value)
						{
							//searchTableDTO.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								//CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								//chkBox.setValue(value);
							}
							//break;
						}
						else
						{
							//searchTableDTO.setCheckBoxStatus("false");
							if(null != compMap && !compMap.isEmpty())
							{
								//CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								//chkBox.setValue(value);
							}
							//break;
						}
						
					}
			//}
		}
		}
	
	public void setValueForSelectAllCheckBox(Boolean value)
	{
		List<UpdatePaymentDetailTableDTO> searchTableDTOList = (List<UpdatePaymentDetailTableDTO>) table.getItemIds();
		
		for (UpdatePaymentDetailTableDTO searchTableDTO : searchTableDTOList) {/*
			for (UpdatePaymentDetailTableDTO searchTabledto : tableDataList){
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
		*/}
		}
	
	public List<UpdatePaymentDetailTableDTO> getTableAllItems()
	{
		return (List<UpdatePaymentDetailTableDTO>)table.getItemIds();
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	public void showEditPaymentDetailsView(UpdatePaymentDetailTableDTO tableDTO)
	{
		fireViewEvent(UpdatePaymentDetailPresenter.CREATE_BATCH_SHOW_EDIT_PAYMENT_DETAILS_VIEW,tableDTO);
	}
	
	public void showClaimsDMSView(String intimationNo)
	{
		fireViewEvent(UpdatePaymentDetailPresenter.CREATE_BATCH_SHOW_VIEW_DOCUMENTS,intimationNo);
		//fireViewEvent(CreateAndSearchLotPresenter.SHOW_VIEW_DOCUMENTS,intimationNo);
	}
	
	public void setFinalTableList(List<UpdatePaymentDetailTableDTO> tableRows) {
		Boolean isListEmpty = false;
//		tableDataList = new ArrayList<CreateAndSearchLotTableDTO>();
	//	List<CreateAndSearchLotTableDTO> dtoList = new ArrayList<CreateAndSearchLotTableDTO>();
		if(null != tableRows && !tableRows.isEmpty())
		{
			for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : tableRows) {
				
				/**
				 * When user tries to navigate from forward to previous page.
				 * already added records shouldn't be added to the tableDataList.
				 * Hence another list where keys are stored is used, where if a key is
				 * already existing in that list, then it won't get added in 
				 * the main list.This is done to avoid duplication.
				 * 
				 * */
				
				if(null != tableDataList && !tableDataList.isEmpty() && null != tableDataKeyList && !tableDataKeyList.isEmpty())
				{
					//if(!tableDataKeyList.contains(createAndSearchLotTableDTO.getClaimPaymentKey()))
						//	{
								tableDataList.add(createAndSearchLotTableDTO);
								//tableDataKeyList.add(createAndSearchLotTableDTO.getClaimPaymentKey());
						//	}
				}
				else
				{
					isListEmpty = true;
					break;
				}
			}
			/**
			 * 
			 * When first page is painted, table data list would be empty
			 * and hence adding all the records and its keys to data list and
			 * key list
			 * 
			 * */
			if(isListEmpty)
			{
				for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : tableRows) {
					tableDataList.add(createAndSearchLotTableDTO);
					//tableDataKeyList.add(createAndSearchLotTableDTO.getClaimPaymentKey());
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
	
	public List<UpdatePaymentDetailTableDTO> getTableItems()
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
	
	public String isValid()
	{
		
		StringBuffer err = new StringBuffer(); 
		/*if(null != tableDataList && !tableDataList.isEmpty())
		{
			
			
			
			for (CreateAndSearchLotTableDTO dto : tableDataList) {	
				
				
			if(null != dto.getNoOfDiffDays() &&  dto.getNoOfDiffDays() >= -3 && (null ==dto.getRemarks() || ("").equalsIgnoreCase(dto.getRemarks())))
			{
				 err += "\nPlease Enter remarks for the Intimation Number:" + dto.getIntimationNo() +"<br>";
			}
			
		}
			
	}*/
		
		List<UpdatePaymentDetailTableDTO> requestTableList = (List<UpdatePaymentDetailTableDTO>) table.getItemIds();
		List<UpdatePaymentDetailTableDTO> finalListForProcessing = null;
		if(null != requestTableList && !requestTableList.isEmpty())
		{/*
			finalListForProcessing = new ArrayList<CreateAndSearchLotTableDTO>();
			for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : tableDataList) {
								
				if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
				{
					finalListForProcessing.add(createAndSearchLotTableDTO);
				}				
		
				}
		*/}
		/*if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
		{
			for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : finalListForProcessing) {
				
			
							if(null != createAndSearchLotTableDTO.getNoOfDiffDays() &&  createAndSearchLotTableDTO.getNoOfDiffDays() >= -3 && (null ==createAndSearchLotTableDTO.getRemarks() || ("").equalsIgnoreCase(createAndSearchLotTableDTO.getRemarks())))
				{
					 err.append("\nPlease Enter remarks for the Intimation Number:").append(createAndSearchLotTableDTO.getIntimationNo()).append("<br>");
				}
			}
			
		}*/
		return err.toString();
	}
	
	//private void generateInterestAmountColumn()
	

		
}
