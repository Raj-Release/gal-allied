package com.shaic.claim.outpatient.registerclaim.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.outpatient.registerclaim.dto.InsuredDetailsDTO;
import com.shaic.domain.Insured;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

public class InsuredDetailsTable extends GEditableTable<InsuredDetailsDTO> {
	private static final long serialVersionUID = 5375199119863277119L;
	
/*	public static final Object[] VISIBLE_COLUMNS = new Object[] {"checkupDate",
		"reasonForCheckup" };*/
	
	public InsuredDetailsTable() {
		super(InsuredDetailsTable.class);
		setUp();
	}

	private List<InsuredDetailsDTO> insuredDetailsDTO = new ArrayList<InsuredDetailsDTO>();
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		fieldMap.put("insuredPatientName", new TableFieldDTO("insuredPatientName",
			ComboBox.class, Insured.class, true));
		fieldMap.put("checkupDate", new TableFieldDTO("checkupDate",
			DateField.class, Date.class, true));
		fieldMap.put("reasonForCheckup", new TableFieldDTO("reasonForCheckup",
				TextField.class, String.class, true, 100, true));
	}*/


	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
	 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
			fieldMap.put("insuredPatientName", new TableFieldDTO("insuredPatientName",
				ComboBox.class, Insured.class, true));
			fieldMap.put("checkupDate", new TableFieldDTO("checkupDate",
				DateField.class, Date.class, true));
			fieldMap.put("reasonForCheckup", new TableFieldDTO("reasonForCheckup",
					TextField.class, String.class, true, 100, true));
		return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
		
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		BeanItemContainer<InsuredDetailsDTO> beanItemContainer = new BeanItemContainer<InsuredDetailsDTO>(
				InsuredDetailsDTO.class);
		beanItemContainer.addAll(this.insuredDetailsDTO);
		table.setContainerDataSource(beanItemContainer);
		 Object[] VISIBLE_COLUMNS = new Object[] {"checkupDate",
		"reasonForCheckup" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setHeight("110px");
		table.setPageLength(table.getItemIds().size() + 1);
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button deleteButton = new Button("Delete");
		    	deleteButton.addClickListener(new Button.ClickListener() {
			        public void buttonClick(ClickEvent event) {
			        	ConfirmDialog dialog = ConfirmDialog
								.show(getUI(),
										"Confirmation",
										"Do you want to Delete ?",
										"No", "Yes", new ConfirmDialog.Listener() {

											public void onClose(ConfirmDialog dialog) {
												if (!dialog.isConfirmed()) {
													deleteRow(itemId);
												} else {
													// User did not confirm
												}
											}
										});
			        } 
			    });
		    	return deleteButton;
		      };
		});
		
	}

	@Override
	public void tableSelectHandler(InsuredDetailsDTO t) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String textBundlePrefixString() {
		return "op-register-claim-";
	}

	public void initTableDTO(List<InsuredDetailsDTO> dto) {
		int i = 1;//For serial NO
		List<InsuredDetailsDTO> dtoList = new ArrayList<InsuredDetailsDTO>();
		for(InsuredDetailsDTO insuredDetailsDTO : dto){
			insuredDetailsDTO.setSerialNumber(i);
			dtoList.add(insuredDetailsDTO);
			i++;
		}
		this.insuredDetailsDTO = dtoList;
	}
	
	public void setVisibleColumn() {
		Object[] VISIBLE_COLUMNS_FOR_FLOATER = new Object[] {"insuredPatientName","checkupDate","reasonForCheckup" };
		this.table.setVisibleColumns(VISIBLE_COLUMNS_FOR_FLOATER);
	}

}
