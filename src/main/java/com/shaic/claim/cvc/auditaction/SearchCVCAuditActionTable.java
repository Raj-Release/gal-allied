package com.shaic.claim.cvc.auditaction;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.main.navigator.ui.OMPMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
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
 * @author GokulPrasath.A
 *
 */
public class SearchCVCAuditActionTable extends GBaseTable<SearchCVCAuditActionTableDTO>{
	
	private final static Object[] VISIBLE_COL_ORDER = new Object[]{
		"serialNumber", "intimationNumber", "transactionNumber", "claimType", "status", "auditBy", "auditDate", "ageing" }; 

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchCVCAuditActionTableDTO>(
				SearchCVCAuditActionTableDTO.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setHeight("335px");
		
	}
	
	 protected void tablesize(){
			table.setPageLength(table.size()+1);
			int length =table.getPageLength();
			if(length>=10){
				table.setPageLength(10);
			}
			
		}

	 @Override
		public void tableSelectHandler(SearchCVCAuditActionTableDTO t) {
		// TODO Auto-generated method stub
		 
		 fireViewEvent(MenuPresenter.CVC_AUDIT_ACTION_PROCESSING_WIZARD, t);
		 
	 }

			
		@Override
		public String textBundlePrefixString() {
		return "cvc-audit-action-processing-";
		}
}
