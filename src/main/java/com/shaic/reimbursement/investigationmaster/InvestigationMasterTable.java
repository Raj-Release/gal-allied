package com.shaic.reimbursement.investigationmaster;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class InvestigationMasterTable extends GBaseTable<InvestigationMasterTableDTO>{
	
	private final Logger log = LoggerFactory.getLogger(InvestigationMasterTable.class);

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber", "investigatorId","investigatorName", "investigatorType", "mobileNo", 
		"state",  "city",  "statusToDisplay"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<InvestigationMasterTableDTO>(InvestigationMasterTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("");
	}

	@Override
	public void tableSelectHandler(InvestigationMasterTableDTO t) {
		// TODO Auto-generated method stub
		
		fireViewEvent(MenuPresenter.INVESTIGATION_MASTER, t);
	}		

	@Override
	public String textBundlePrefixString() {
		
		return "investigation-master-";
	}
	protected void tablesize(){
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
