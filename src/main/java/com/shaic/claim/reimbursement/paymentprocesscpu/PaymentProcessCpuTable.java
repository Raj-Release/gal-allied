package com.shaic.claim.reimbursement.paymentprocesscpu;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPage;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class PaymentProcessCpuTable extends GBaseTable<PaymentProcessCpuTableDTO>{
	@Inject
	PaymentProcessCpuPage paymentProcessCpuPage;
	private Window popup; 
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "claimNumber", "cpuLotNo", 
		"amount", "chequeNo", "chequeDate","status"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<PaymentProcessCpuTableDTO>(PaymentProcessCpuTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		generateColumn();
	}

	@Override
	public void tableSelectHandler(PaymentProcessCpuTableDTO t) {
		
		
		/***
		 * need to implement webservice for check the 64vb status
		 */
//	fireViewEvent(MenuItemBean.PAYMENT_PROCESS_CPU, t);
		
	}
	
	private void generateColumn()
	{
		
		table.removeGeneratedColumn("viewdetails");
	table.addGeneratedColumn("viewdetails", new Table.ColumnGenerator() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
				
				
			
			Button button = new Button("View Details");
			button.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					PaymentProcessCpuTableDTO tableDto = (PaymentProcessCpuTableDTO)itemId;
					fireViewEvent(MenuItemBean.PAYMENT_PROCESS_CPU_PAGE, tableDto);
					/*PaymentProcessCpuPageDTO bean=null;
					paymentProcessCpuPage.init(bean);
					popup = new com.vaadin.ui.Window();
			    	popup.setWidth("75%");
			    	popup.setHeight("90%");					
					popup.setContent(paymentProcessCpuPage);
//			    	popup.setContent();
			    	popup.setClosable(true);
			    	popup.center();
			    	popup.setResizable(false);
			    	popup.addCloseListener(new Window.CloseListener() {
			    		
			    		@Override
			    		public void windowClose(CloseEvent e) {
			    			System.out.println("Close listener called");
			    		}
			    	});

			    	popup.setModal(true);
			    	UI.getCurrent().addWindow(popup);			    	
			    				*/
				}
			});
			button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
	    	button.setWidth("150px");
	    	button.addStyleName(ValoTheme.BUTTON_LINK);
			return button;
		}
	});
}

       public void invokeView(PaymentProcessCpuTableDTO t) {
	       fireViewEvent(MenuPresenter.PAYMENT_PROCESS_CPU, t,this);
        }

      
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	@Override
	public String textBundlePrefixString() {
		
		return "paymentprocesscpu-";
	}
}
