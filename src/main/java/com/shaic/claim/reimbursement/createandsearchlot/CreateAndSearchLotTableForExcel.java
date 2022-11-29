

package com.shaic.claim.reimbursement.createandsearchlot;

import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This table is used only for excel report generation
 * */
public class CreateAndSearchLotTableForExcel extends GBaseTable<CreateAndSearchLotTableDTO>{
	
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","intimationNo","claimNo","policyNo","rodNo","paymentStatus","product","paymentCpuCode","paymentTypeValue",
		"ifscCode","beneficiaryAcntNo","branchName","typeOfClaim","approvedAmt","payeeNameStr","gmcProposerName","gmcEmployeeName","payableAt","panNo","providerCode","emailID"};
	
	
	private static final Object[] SEARCH_LOT_COL_ORDER = new Object[] {
		"serialNumber","intimationNo","claimNo","policyNo","rodNo","paymentStatus","product","paymentCpuCode","paymentTypeValue",
		"ifscCode","beneficiaryAcntNo","branchName","typeOfClaim","approvedAmt","payeeNameStr","gmcProposerName","gmcEmployeeName","payableAt","panNo","providerCode",
		"emailID","accountBatchNo","lotNo","serviceTax","totalAmnt","tdsAmt","netAmnt","tdsPercentage","refNo","paymentReqDt","paymentReqUID"};

	private List<Component> compList = new ArrayList<Component>();
	
	
	
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
		 //generatecolumns();
		 table.setVisibleColumns(SEARCH_LOT_COL_ORDER);
	 }
	
	public void setVisibleColumnsForCreateLot(){
		//generatecolumns();
		 table.setVisibleColumns(NATURAL_COL_ORDER);
	 }
	
	@Override
	public String textBundlePrefixString() {
		return "search-create-or-searchlot-";
	}
	
	private void generatecolumns(){

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
					chkBox.setData(tableDTO);
					addListener(chkBox);
					compList.add(chkBox);
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
	
	public void setValueForCheckBox(Boolean value)
	{
		List<CreateAndSearchLotTableDTO> searchTableDTOList = (List<CreateAndSearchLotTableDTO>) table.getItemIds();
		for (CreateAndSearchLotTableDTO searchTableDTO : searchTableDTOList) {
			
			if(value)
				searchTableDTO.setCheckBoxStatus("true");
			else
				searchTableDTO.setCheckBoxStatus("false");
		}
		if(null != compList && !compList.isEmpty())
		{
			for (Component comp : compList) {
				CheckBox chkBox = (CheckBox) comp;
				chkBox.setValue(value);
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
		int rowCount = 1;
		List<CreateAndSearchLotTableDTO> finalList = new ArrayList<CreateAndSearchLotTableDTO>();
		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : dtoList) {
			
			if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
			{
				createAndSearchLotTableDTO.setSerialNumber(rowCount);
				rowCount++;
				finalList.add(createAndSearchLotTableDTO);
			}
		}
		table.addItems(finalList);
	}
	
	public void addFinalListToBean(List<CreateAndSearchLotTableDTO> dtoList)
	{
		int rowCount = 1;
		List<CreateAndSearchLotTableDTO> finalList = new ArrayList<CreateAndSearchLotTableDTO>();
		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : dtoList) {
			
			//if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
			{
				createAndSearchLotTableDTO.setSerialNumber(rowCount);
				rowCount++;
				finalList.add(createAndSearchLotTableDTO);
			}
		}
		table.addItems(finalList);
	}
	
	//public void populateIFSCData
}
	
	 
	 
