package com.shaic.paclaim.processrejection.search;

import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.main.navigator.ui.PAMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class PASearchProcessRejectionTable extends
		GBaseTable<SearchProcessRejectionTableDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"intimationNo", "strIntimationDate", "hospitalType", "status", "accidentDeath",
			"preauthStatus" };*/
	@Inject
	SearchProcessRejectionTableDTO rejectionDTO;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<SearchProcessRejectionTableDTO>(
				SearchProcessRejectionTableDTO.class));
	 Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"intimationNo", "strIntimationDate", "hospitalType", "status", "accidentDeath",
			"preauthStatus" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
	}

	@Override
	public void tableSelectHandler(SearchProcessRejectionTableDTO t) {

		   VaadinSession session = getSession();
			
			Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
			
			try{
				if(! isActiveHumanTask){
				
					SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
			
					fireViewEvent(PAMenuPresenter.PA_PROCESS_REJECTION , t);
					
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
		return "search-processrejection-";
	}
	
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
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

}
