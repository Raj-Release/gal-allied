package com.shaic.claim.lumen.create;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

public class LumenPolicySearchResultTable extends GBaseTable<LumenPolicySearchResultTableDTO>{

	private static final long serialVersionUID = 199517453243501587L;
	
	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"serialNumber","policyNumber","proposerCode",
		"proposerName","noOfInsured","productName","policyStartDate","policyEndDate","premiumAmount","add" 	
	}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<LumenPolicySearchResultTableDTO>(LumenPolicySearchResultTableDTO.class));

		table.removeGeneratedColumn("add");
		table.addGeneratedColumn("add",
				new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button initiateLumen = new Button();{
					initiateLumen.setCaption("Initiate Policy Request");
					initiateLumen.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						public void buttonClick(ClickEvent event) {
							LumenPolicySearchResultTableDTO resultObj  = (LumenPolicySearchResultTableDTO) itemId;
							fireViewEvent(MenuItemBean.INITIATE_POLICY_REQUEST, resultObj);	
						}
					});
				}
				initiateLumen.addStyleName(BaseTheme.BUTTON_LINK);
				return initiateLumen;
			}
		});
		table.setColumnHeader("add", "");
		table.setSizeFull();
	}

	@Override
	public void tableSelectHandler(LumenPolicySearchResultTableDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "intiate-policy-lumen-request-";
	}

	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}

}
