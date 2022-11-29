package com.shaic.claim.reimbursement.opscreen;

import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;

public class OpTable extends GBaseTable<OpTableDTO>{


	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","claimNo","policyNo","product","zone","paymentMode","ifscCode",
		"accountNo","branchName","amountPayable","payeeName","payableAt","panNo","emailID"};
	
	public List<Component> compList = null;
		
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<OpTableDTO>(OpTableDTO.class));
		generatecolumns();
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}
	@Override
	public void tableSelectHandler(OpTableDTO t) {
		fireViewEvent(MenuPresenter.OP_SCREEN, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "opscreen-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(5);
		}
		
	}
	
	private void generatecolumns(){
		compList = new ArrayList<Component>();
		table.removeGeneratedColumn("chkSelect");
		table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				  OpTableDTO tableDTO = (OpTableDTO) itemId;
				  CheckBox chkBox = new CheckBox("");
					chkBox.setData(tableDTO);
					addListener(chkBox);
					compList.add(chkBox);
					//addListener(chkBox);
				return chkBox;
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
					OpTableDTO tableDTO = (OpTableDTO)chkBox.getData();
					if(value)
					{
						tableDTO.setCheckBoxStatus("true");
						//tableDTO.setChkSelect(true);
					}
					else
					{
						tableDTO.setCheckBoxStatus("false");
						//tableDTO.setChkSelect(false);
					}
					
				}
			}
		});
	}
	
	
	public void setValueForCheckBox(Boolean value)
	{
		List<OpTableDTO> searchTableDTOList = (List<OpTableDTO>) table.getItemIds();
		for (OpTableDTO searchTableDTO : searchTableDTOList) {
			
			if(value)
			{
				searchTableDTO.setCheckBoxStatus("true");
				searchTableDTO.setChkSelect(value);
				//chkBox.setValue(value);
			}
			else
			{
				searchTableDTO.setCheckBoxStatus("false");
				searchTableDTO.setChkSelect(value);
				//chkBox.setValue(value);
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
	
	public List<OpTableDTO> getTableAllItems()
	{
		return (List<OpTableDTO>)table.getItemIds();
	}
}
