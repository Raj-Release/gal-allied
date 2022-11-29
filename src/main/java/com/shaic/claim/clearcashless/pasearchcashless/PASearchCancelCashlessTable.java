package com.shaic.claim.clearcashless.pasearchcashless;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.clearcashless.dto.SearchClearCashlessDTO;
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

public class PASearchCancelCashlessTable extends GBaseTable<SearchClearCashlessDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo","policyNo","insuredPatientName", "cpuCode","hospitalName","dateOfAdmission","productName","prauthAmount","bsiAmount"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchClearCashlessDTO>(SearchClearCashlessDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("305px");
	}
	
	public void alertMessage(final SearchClearCashlessDTO t, String message) {

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
			SearchClearCashlessDTO t) {
	String screen = "pa-screen";
	t.setScreen(screen);
		VaadinSession session = getSession();
		Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
		try{
			if(!isActiveHumanTask){
				SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
				fireViewEvent(MenuPresenter.SHOW_CLEAR_CASHLESS, t);
			}else{
				getErrorMessage("This record is already opened by another user");
			}
		}catch(Exception e){
			Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
			if(existingTaskNumber != null){
				//BPMClientContext.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(), existingTaskNumber, SHAConstants.SYS_RELEASE);
			}
			System.out.println("Error is occured. Task is to be released");
			e.printStackTrace();
		}
		
		
		
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

	@Override
	public String textBundlePrefixString() {
		
		return "search-clear-cashless-";
	}
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}


}