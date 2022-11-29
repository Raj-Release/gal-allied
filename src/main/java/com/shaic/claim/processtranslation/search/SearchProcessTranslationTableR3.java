package com.shaic.claim.processtranslation.search;

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

public class SearchProcessTranslationTableR3 extends
GBaseTable<SearchProcessTranslationTableDTO> {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber", 
	"intimationNo", "claimNo", "insuredPatientName",
	"lob", "productCode", "productName" };*/

@Override
public void removeRow() {
table.removeAllItems();
}

@Override
public void initTable() {

table.setContainerDataSource(new BeanItemContainer<SearchProcessTranslationTableDTO>(
		SearchProcessTranslationTableDTO.class));
 Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber", 
	"intimationNo", "claimNo", "insuredPatientName",
	"lob", "productCode", "productName" };
table.setVisibleColumns(NATURAL_COL_ORDER);


}

@Override
public void tableSelectHandler(SearchProcessTranslationTableDTO t) {
	
	
	    VaadinSession session = getSession();
		
		Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
		try{
			if(! isActiveHumanTask){
			
				SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
		
				fireViewEvent(MenuPresenter.UPLOAD_TRANSLATED_DOCUMENT_REIMBURSEMENT, t);
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
return "search-processtranslation-";
}
protected void tablesize(){
table.setPageLength(table.size());
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

