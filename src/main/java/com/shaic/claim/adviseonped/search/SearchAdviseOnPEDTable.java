package com.shaic.claim.adviseonped.search;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchAdviseOnPEDTable extends
		GBaseTable<SearchAdviseOnPEDTableDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber", 
			"intimationNo", "claimNo", "policyNo", "insuredPatientName",
			"cpuCode", "hospitalName", "hospitalAddress", "hospitalCity",
			"pedSuggestion", "pedStatus" };*/

	@Override
	public void removeRow() {
		table.removeAllItems();
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<SearchAdviseOnPEDTableDTO>(
				SearchAdviseOnPEDTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber", 
			"intimationNo", "claimNo", "policyNo", "insuredPatientName",
			"cpuCode", "hospitalName", "hospitalAddress", "hospitalCity",
			"pedSuggestion", "pedStatus" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("290px");
		
	}

	@Override
	public void tableSelectHandler(SearchAdviseOnPEDTableDTO t) {
		//TODO
       VaadinSession session = getSession();
		
		Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
		try{
		if(! isActiveHumanTask){
		
			SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
	
			fireViewEvent(MenuPresenter.ADVISE_ON_PED_PAGE, t);
			
		}else{
			getErrorMessage("This record is already opened by another user");
		}
		}catch(Exception e){
			
			Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
			SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
			e.printStackTrace();
		}
		
	}

	@Override
	public String textBundlePrefixString() {
		return "search-adviseonped-";
	}
	protected void tablesize(){
		if(table.size()==1){
		table.setPageLength(table.size()+1);
		}else{
			table.setPageLength(table.size());
		}
		int length =table.getPageLength();
//		if(length>=7){
//			table.setPageLength(7);
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
