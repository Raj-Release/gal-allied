package com.shaic.claim.lumen.search;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class SearchResultTable extends GBaseTable<LumenRequestDTO>{

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"serialNumber","add","intimationNumber","cpuDesc",
		"policyNumber","productName","insuredPatientName","claimType","initiatedScreen","initiatedBy","initiatedDate","lumenStatus"
	}; 

	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<LumenRequestDTO>(LumenRequestDTO.class));

		table.removeGeneratedColumn("add");
		table.addGeneratedColumn("add",
				new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button initiateLumen = new Button();{
					initiateLumen.setCaption("View Lumen trails");
					initiateLumen.addClickListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
							LumenRequestDTO resultObj  = (LumenRequestDTO) itemId;

							if(SHAConstants.YES_FLAG.equalsIgnoreCase(resultObj.getPatientStatus())) {
								
								SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
							}
							
							fireViewEvent(SearchLumenRequestPresenter.SHOW_VIEW_LUMEN_TRIALS, resultObj);	
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
	public void tableSelectHandler(LumenRequestDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "search-lumen-request-";
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
