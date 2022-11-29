package com.shaic.claim.lumen.create;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

public class LumenSearchResultTable extends GBaseTable<LumenSearchResultTableDTO>{
	
	private static final long serialVersionUID = -501535728151604840L;
	
	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"serialNumber","intimationNumber","cpuDesc",
		"policyNumber","productName","insuredPatientName","hospitalName","hospitalType","reasonForAdmission","add"
	}; 

	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<LumenSearchResultTableDTO>(LumenSearchResultTableDTO.class));

		table.removeGeneratedColumn("add");
		table.addGeneratedColumn("add",
				new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button initiateLumen = new Button();{
					initiateLumen.setCaption("Initiate Request");
					initiateLumen.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						public void buttonClick(ClickEvent event) {
							LumenSearchResultTableDTO resultObj  = (LumenSearchResultTableDTO) itemId;
							
							if(SHAConstants.YES_FLAG.equalsIgnoreCase(resultObj.getInsuredDeceasedFlag())) {
								
								SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
							}
							
							fireViewEvent(MenuItemBean.INITIATE_REQUEST, resultObj);	
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
	public void tableSelectHandler(LumenSearchResultTableDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "intiate-lumen-request-";
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
