package com.shaic.reimbursement.paymentprocess.initiateRecovery;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PaymentInitiateRecoveryResultantTable extends GBaseTable<PaymentInitiateRecoveryTableDTO>{
	
	@Inject
	private InitiateRecoveryDetailsPage recoveryDetailsPage;
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","rodNumber", "billClassification", "payeeName"};
	

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		table.setContainerDataSource(new BeanItemContainer<PaymentInitiateRecoveryTableDTO>(PaymentInitiateRecoveryTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("290px");
		
	}

	@Override
	public void tableSelectHandler(PaymentInitiateRecoveryTableDTO t) {
		// TODO Auto-generated method stub
		Window popup = new com.vaadin.ui.Window();
		recoveryDetailsPage.init(t);
		recoveryDetailsPage.setPopUp(popup);
		popup.setCaption("Recovery Details");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(recoveryDetailsPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		popup = null;
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-initiate-recovery-";
	}

}