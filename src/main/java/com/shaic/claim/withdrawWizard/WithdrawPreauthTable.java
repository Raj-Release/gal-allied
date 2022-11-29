package com.shaic.claim.withdrawWizard;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class WithdrawPreauthTable extends GBaseTable<WithdrawPreauthPageDTO> {
	
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "select",
		"referenceNo", "referenceType", "treatmentType", "requestedAmt",
		"approvedAmt"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<WithdrawPreauthPageDTO>(
				WithdrawPreauthPageDTO.class));
	 Object[] NATURAL_COL_ORDER = new Object[] {/* "select",*/
			"referenceNo", "referenceType", "treatmentType", "requestedAmt",
			"approvedAmt"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size()+4);
		table.setHeight("200px");
		
		table.removeGeneratedColumn("ViewDetails");
		table.addGeneratedColumn("ViewDetails", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button button = new Button("ViewDetails");
		    	button.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
//						oldPedEndorsementDTO = (OldPedEndorsementDTO)itemId;
//						Long key = oldPedEndorsementDTO.getKey();
//			    		ViewPEDEndorsementDetails viewDetailUI = new ViewPEDEndorsementDetails(
//			    				pedService, masterService, preauthService,
//			    			    viewPEDEndoresmentDetailsTable,
//			     				viewPEDEndoresementDetailsService, key);
//			    		viewDetailUI.initView(key);
//			    		UI.getCurrent().addWindow(viewDetailUI);
			        } 
			    });
		    	
		    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	return button;
		      }
		});
		
		table.setPageLength(table.size());
	}

	@Override
	public void tableSelectHandler(WithdrawPreauthPageDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "withdraw-preauth-";
	}

}
