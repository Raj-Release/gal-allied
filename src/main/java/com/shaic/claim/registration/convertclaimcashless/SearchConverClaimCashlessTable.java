package com.shaic.claim.registration.convertclaimcashless;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchConverClaimCashlessTable extends
		GBaseTable<SearchConverClaimCashlessTableDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Object[] VISIBLE_COL_ORDER = new Object[] { 
			"serialNumber","intimationNumber","policyNo","claimType", "cpuCode","insuredPatientName", "hospitalName","hospitalType",
			"strIntimationDate", "claimStatus"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();

	}

	@Override
	public void initTable() {
		//setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<SearchConverClaimCashlessTableDTO>(
				SearchConverClaimCashlessTableDTO.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setHeight("290px");
		
		//table.setSizeUndefined();
	}

	@Override
	public void tableSelectHandler(SearchConverClaimCashlessTableDTO t) {
		//TODO
		
                fireViewEvent(MenuPresenter.SHOW_CONVERT_CLAIM_CASHLESS, t);
		
	}

	@Override
	public String textBundlePrefixString() {
		return "search-convertclaimcashless-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		//int length =table.getPageLength();
//		if(length>=5){
//			table.setPageLength(4);
//		}
		
	}
	
public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
}
