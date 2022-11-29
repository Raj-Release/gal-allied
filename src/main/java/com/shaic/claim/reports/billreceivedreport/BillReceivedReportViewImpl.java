package com.shaic.claim.reports.billreceivedreport;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class BillReceivedReportViewImpl extends AbstractMVPView implements BillReceivedReportView{
	
	@Inject
	private BillReceivedReportForm billReportForm;

	@Inject
	private BillReceivedReportTable billReportTable;

	private VerticalSplitPanel mainPanel;
	
	private BillReceivedReportFormDTO searchDto;
	
	private ExcelExport excelExport;
	
	
	
	VerticalLayout secondLayout = null;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		billReportForm.init();	
		
		billReportTable.init("BILLS RECEIVED FOR THE DAY", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(billReportForm);
		mainPanel.setSecondComponent(billReportTable);	
		mainPanel.setSplitPosition(25);
		setHeight("710px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		billReportTable.addSearchListener(this);
		billReportForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void resetView() {
		billReportForm.refresh();

	}
	
  
	@Override
	public void doSearch() {
		BillReceivedReportFormDTO bean = billReportForm.validate();
		if(bean != null)
		{
			Pageable pageable = billReportTable.getPageable();
			bean.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(
					BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(
					BPMClientContext.PASSWORD);
			fireViewEvent(BillReceivedReportPresenter.BILL_RECEIVED_REPORT,
					bean, userName, passWord);
		}
		
	}
		

	@Override
	public void resetSearchResultTableValues() {
		billReportTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof BillReceivedReportTable) {
				((BillReceivedReportTable) comp).removeRow();
			}
		}

	}

	@Override
	public void list(Page<BillReceivedReportTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			billReportTable.setTableList(tableRows);
//			billReportTable.tablesize();
			billReportTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Bill Received Status Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);

			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
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
					fireViewEvent(MenuItemBean.BILL_RECIECVED_STATUS_REPORT, null);
				}
			});
		}
		billReportForm.enableButtons();
	}
	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(billReportTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
		excelExport.setReportTitle("Bill Received Status Report");
		excelExport.export();
		
	}


	

}
