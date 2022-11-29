package com.shaic.claim.fvrGradingTable;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class FvrGradingTable extends GBaseTable<FvrGradingDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	
	
	public static final Object[] COLUMN_HEADER = new Object[] {	"category"		
	};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<FvrGradingDTO>(FvrGradingDTO.class));
		table.setVisibleColumns(COLUMN_HEADER);
	
		table.removeGeneratedColumn("Score");
		table.addGeneratedColumn("Score", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final CheckBox checkBox = new CheckBox();
		    	checkBox.addValueChangeListener(new ValueChangeListener() {
					
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						FvrGradingDTO fvrGradingDTO = (FvrGradingDTO)itemId;
						if(checkBox.getValue() == true){
							fvrGradingDTO.setCategoryValue(true);
						}						
					}
				});
		    	
		    	checkBox.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	checkBox.addStyleName(ValoTheme.BUTTON_LINK);
		    	return checkBox;
		      }
		});
		table.setPageLength(7);
	}

	@Override
	public void tableSelectHandler(FvrGradingDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {		
		return "fvr-grading-";
	}

}