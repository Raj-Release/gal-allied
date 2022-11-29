package com.shaic.reimbursement.paymentprocess.createbatch.search;

import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class BatchCpuCountTable extends GBaseTable<CreateAndSearchLotTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","cpuCode","cpuCodeCount","cashlessCount","reimbursementCount"};
		
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<CreateAndSearchLotTableDTO>(CreateAndSearchLotTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("260px");
		table.setFooterVisible(true);
		
	}

	
	@Override
	public String textBundlePrefixString() {
		
		return "batch-cpu-claim-count-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

	@Override
	public void tableSelectHandler(CreateAndSearchLotTableDTO t) {
		// TODO Auto-generated method stub
		
	}
	
	public void setTotalCount(CreateAndSearchLotTableDTO tableDTO){
		table.setColumnFooter("cpuCode", "Total Count");
		table.setColumnFooter("cpuCodeCount", String.valueOf(tableDTO.getTotalCpuCodeCount()));
		table.setColumnFooter("cashlessCount", String.valueOf(tableDTO.getCashlessTotalCount()));
		table.setColumnFooter("reimbursementCount", String.valueOf(tableDTO.getReimbursementTotalCount()));
	}

	
  
}
