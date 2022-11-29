package com.shaic.claim.fvrgrading.search;

import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchFvrReportGradingTable extends
		GBaseTable<SearchFvrReportGradingTableDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Object[] VISIBLE_COL_ORDER = new Object[] { "serialNumber", "intimationNo","policyNo", "insuredPatientName","hospitalName","hospCity",
			"lob", "cpuCode","product", 
			"admissionReason" };

	@Override
	public void removeRow() {
		table.removeAllItems();

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<SearchFvrReportGradingTableDto>(
				SearchFvrReportGradingTableDto.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setHeight("295px");
	}

	@Override
	public void tableSelectHandler(SearchFvrReportGradingTableDto t) {
		
		 	try{
				fireViewEvent(MenuPresenter.SHOW_FIELD_VISIT_REPORT_GRADING, t);
			}catch(Exception e){
				e.printStackTrace();
			}
		
		
		// fireViewEvent(MenuPresenter.SHOW_HOSPITAL, t.getKey());
	}

	@Override
	public String textBundlePrefixString() {
		return "fvrgrading-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
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
