package com.shaic.reimbursement.rod.cancelAcknowledgment.search;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODTableDTO;
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

public class SearchCancelAcknowledgementTable extends GBaseTable<SearchCreateRODTableDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "acknowledgementNumber","policyNo", 
		"healthCardIDNumber","insuredPatientName", "cpuCode","hospitalName","dateOfAdmission","status"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchCreateRODTableDTO>(SearchCreateRODTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("305px");
	}
	
	public void alertMessage(final SearchCreateRODTableDTO t, String message) {

   		Label successLabel = new Label(
				"<b style = 'color: red;'>"+ message + "</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("ok");
		homeButton.setData(t);
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				 dialog.close();
//				 fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, t);
			}
		});
	}

	@Override
	public void tableSelectHandler(
			SearchCreateRODTableDTO t) {
		
//	String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
//	String errorMessage = "";
//	if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
//		String get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo());
//		if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
//			errorMessage = "Cheque Status is Dishonoured";
//			alertMessage(t, errorMessage);
//		} else if(get64vbStatus != null && SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus)) {
//			errorMessage = "Cheque Status is Pending";
//			alertMessage(t, errorMessage);
//		} else {
			fireViewEvent(MenuPresenter.SHOW_CANCEL_ACKNOWLEDGEMENT_WIZARD, t);
//		}
//	} else {
//		fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, t);
//	}
//	
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-create-rod-";
	}
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
