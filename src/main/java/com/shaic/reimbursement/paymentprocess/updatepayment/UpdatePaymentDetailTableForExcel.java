/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.updatepayment;



import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
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
 * @author ntv.vijayar
 *
 * This table is used only for
 * generating excel report. No other validations
 * are done.
 */
public class UpdatePaymentDetailTableForExcel extends GBaseTable<UpdatePaymentDetailTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","chkSelect","viewdetails","intimationNo", "maApprovedDate", "cpucode", 
		"cpuName","paymentCpuCode","paymentCpuName","proposerName","employeeName","payeeName","paymentType","reasonForChange","ifscCode","beneficiaryAcno","bankName",
		"branchName","branchCity","payableCity","emailID","typeOfClaim","approvedAmt",
		"productName"}; 
	
	
	private List<Component> compList = new ArrayList<Component>();
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override  
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<UpdatePaymentDetailTableDTO>(UpdatePaymentDetailTableDTO.class));	
		generatecolumns();
		table.setVisibleColumns(NATURAL_COL_ORDER);

	}

	@Override
	public void tableSelectHandler(
			UpdatePaymentDetailTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "update-payment-detail-";
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
					
				  UpdatePaymentDetailTableDTO tableDTO = (UpdatePaymentDetailTableDTO) itemId;
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
		List<UpdatePaymentDetailTableDTO> searchTableDTOList = (List<UpdatePaymentDetailTableDTO>) table.getItemIds();
		for (UpdatePaymentDetailTableDTO searchTableDTO : searchTableDTOList) {
			
			if(value){
				searchTableDTO.setCheckBoxStatus("true");
				
			}
			else{
				searchTableDTO.setCheckBoxStatus("false");
			}
		}
		if(null != compList && !compList.isEmpty())
		{
			for (Component comp : compList) {
				CheckBox chkBox = (CheckBox) comp;
				chkBox.setValue(value);
			}
		}
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
	
	public void addBeanToList(List<UpdatePaymentDetailTableDTO> dtoList)
	{
		int rowCount = 1;
		List<UpdatePaymentDetailTableDTO> finalList = new ArrayList<UpdatePaymentDetailTableDTO>();
		for (UpdatePaymentDetailTableDTO UpdatePaymentDetailTableDTO : dtoList) {
			if(("true").equalsIgnoreCase(UpdatePaymentDetailTableDTO.getCheckBoxStatus()))
			{
			UpdatePaymentDetailTableDTO.setSerialNumber(rowCount);
			rowCount++;
			finalList.add(UpdatePaymentDetailTableDTO);
			}
		}
		table.addItems(finalList);
	}
	
	public void addBeanToListForShowDetails(List<UpdatePaymentDetailTableDTO> dtoList)
	{
		/*if(null != dtoList && !dtoList.isEmpty())
		{
			table.addItems(dtoList);
		}*/
		
		int rowCount = 1;
		List<UpdatePaymentDetailTableDTO> finalListForShowDetails = new ArrayList<UpdatePaymentDetailTableDTO>();
		for (UpdatePaymentDetailTableDTO UpdatePaymentDetailTableDTO : dtoList) {
			
			//if(("true").equalsIgnoreCase(UpdatePaymentDetailTableDTO.getCheckBoxStatus()))
			{
				UpdatePaymentDetailTableDTO.setSerialNumber(rowCount);
				rowCount++;
				finalListForShowDetails.add(UpdatePaymentDetailTableDTO);
			}
		}
		table.addItems(finalListForShowDetails);
	}
	
	public void addFinalListToBean(List<UpdatePaymentDetailTableDTO> dtoList)
	{
		int rowCount = 1;
		List<UpdatePaymentDetailTableDTO> finalList = new ArrayList<UpdatePaymentDetailTableDTO>();
		for (UpdatePaymentDetailTableDTO UpdatePaymentDetailTableDTO : dtoList) {
			
			//if(("true").equalsIgnoreCase(UpdatePaymentDetailTableDTO.getCheckBoxStatus()))
			{
				UpdatePaymentDetailTableDTO.setSerialNumber(rowCount);
				rowCount++;
				finalList.add(UpdatePaymentDetailTableDTO);
			}
		}
		table.addItems(finalList);
	}


}

