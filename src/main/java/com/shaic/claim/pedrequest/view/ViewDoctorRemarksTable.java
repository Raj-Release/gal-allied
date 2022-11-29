package com.shaic.claim.pedrequest.view;

import com.shaic.arch.table.GBaseTable;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;

public class ViewDoctorRemarksTable extends GBaseTable<ViewDoctorRemarksDTO> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*	public static final Object[] NATURAL_COL_ORDER = new Object[] { "select",
		"strNoteDate", "transaction","transactionType", "remarks", "userId" };*/

	@Override
	public void removeRow() {
		
		
		

		
	}

	@Override
	public void initTable() {
		
		setSizeFull();
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<ViewDoctorRemarksDTO>(
				ViewDoctorRemarksDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {/* "select",*/
			"strNoteDate", "userId","transaction","transactionType" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.removeGeneratedColumn("remarks");
		table.addGeneratedColumn("remarks",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						TextArea viewRemarksTxta = new TextArea();
						viewRemarksTxta.setWidth("100%");
						ViewDoctorRemarksDTO rowDto = (ViewDoctorRemarksDTO)itemId;
						viewRemarksTxta.setValue(rowDto.getRemarks());
						viewRemarksTxta.setReadOnly(true);
						viewRemarksTxta.setRows(3);
						viewRemarksTxta.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
						return viewRemarksTxta;
					}
		});
		
		table.setColumnWidth("strNoteDate", 100);
		table.setColumnWidth("userId", 150);
		table.setColumnWidth("transaction", 200);
		table.setColumnWidth("transactionType", 200);		
		
		table.setHeight("460px");
	}

	@Override
	public void tableSelectHandler(ViewDoctorRemarksDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "view-doctorremarks-";
	}
	
	public void setSizeable(){
		table.setHeight("465px");
	}

}
