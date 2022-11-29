

package com.shaic.claim.outpatient.createbatchop;

import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;

/**
 * This table is used only for excel report generation
 * */
public class CreateBatchOpTableForExcel extends GBaseTable<CreateBatchOpTableDTO>{
	
	
	/*private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","intimationNo","claimNo","policyNo","rodNo","paymentStatus","product","paymentCpuCode","paymentTypeValue",
		"ifscCode","beneficiaryAcntNo","branchName","typeOfClaim","approvedAmt","payeeNameStr","gmcProposerName","gmcEmployeeName","payableAt","panNo","providerCode","emailID"};*/
	
	/*private static final Object[] NATURAL_COL_ORDER = new Object[]{"claimNo","policyNo","product","paymentStatus","paymentTypeValue",
		"ifscCode","beneficiaryAcntNo","bankName","branchName","typeOfClaim" };*/
	
	private static final Object[] NATURAL_COL_ORDER = new Object[]{"intimationNo","claimNo","policyNo","billAmt","payableAmt","payeeNameStr","chequeNumber","chequeDate","ifscCode","bankName","totalBillAmt","deductionAmt","totalPayableAmt","remarks"};
	
	
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
		
		table.setContainerDataSource(new BeanItemContainer<CreateBatchOpTableDTO>(CreateBatchOpTableDTO.class));
		generatecolumns();
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}
	@Override
	public void tableSelectHandler(CreateBatchOpTableDTO t) {
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
		return "create-batchop-excel-";
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
					
				CreateBatchOpTableDTO tableDTO = (CreateBatchOpTableDTO) itemId;
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
					
				final CreateBatchOpTableDTO tableDTO = (CreateBatchOpTableDTO)itemId;
				
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
						showEditPaymentDetailsView((CreateBatchOpTableDTO)itemId);
						
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
					CreateBatchOpTableDTO tableDTO = (CreateBatchOpTableDTO)chkBox.getData();
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
		List<CreateBatchOpTableDTO> searchTableDTOList = (List<CreateBatchOpTableDTO>) table.getItemIds();
		for (CreateBatchOpTableDTO searchTableDTO : searchTableDTOList) {
			
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
	
	public List<CreateBatchOpTableDTO> getTableAllItems()
	{
		return (List<CreateBatchOpTableDTO>)table.getItemIds();
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}
	
	public void showEditPaymentDetailsView(CreateBatchOpTableDTO tableDTO)
	{
		fireViewEvent(CreateBatchOpPresenter.SHOW_EDIT_PAYMENT_DETAILS_VIEW_OP,tableDTO);
	}
	
	public void showClaimsDMSView(String intimationNo)
	{
		fireViewEvent(CreateBatchOpPresenter.SHOW_VIEW_DOCUMENTS_OP,intimationNo);
	}
	
	public void addBeanToList(List<CreateBatchOpTableDTO> dtoList)
	{
		int rowCount = 1;
		List<CreateBatchOpTableDTO> finalList = new ArrayList<CreateBatchOpTableDTO>();
		for (CreateBatchOpTableDTO createAndSearchLotTableDTO : dtoList) {
			
			/*if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
			{*/
				createAndSearchLotTableDTO.setSerialNumber(rowCount);
				createAndSearchLotTableDTO.setIfscCode(null);
				rowCount++;
				finalList.add(createAndSearchLotTableDTO);
//			}
		}
		table.addItems(finalList);
	}
	
	public void addFinalListToBean(List<CreateBatchOpTableDTO> dtoList)
	{
		int rowCount = 1;
		List<CreateBatchOpTableDTO> finalList = new ArrayList<CreateBatchOpTableDTO>();
		for (CreateBatchOpTableDTO createAndSearchLotTableDTO : dtoList) {
			
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
	
	 
	 
