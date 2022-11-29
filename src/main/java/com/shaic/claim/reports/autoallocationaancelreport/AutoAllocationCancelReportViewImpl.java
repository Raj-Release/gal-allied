package com.shaic.claim.reports.autoallocationaancelreport;

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

public class AutoAllocationCancelReportViewImpl extends AbstractMVPView implements AutoAllocationCancelReportView{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private AutoAllocationCancelDetailReport autoallocCancelForm;

	@Inject
	private AutoAllocationCancelReportTable cancelReportTable;

	private VerticalSplitPanel mainPanel;
	
	private CancelSearchDTO searchDto;
	
	private ExcelExport excelExport;
	
	VerticalLayout secondLayout = null;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		autoallocCancelForm.init();
		cancelReportTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(autoallocCancelForm);
		mainPanel.setSecondComponent(cancelReportTable);
		mainPanel.setSplitPosition(25);
		setHeight("725px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		cancelReportTable.addSearchListener(this);
		autoallocCancelForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void resetView() {
		autoallocCancelForm.refresh();

	}
	
  
	@Override
	public void doSearch() {
		String err=autoallocCancelForm.validate();
		if(err == null)
		{
			CancelSearchDTO cancelRemarksDTO = autoallocCancelForm.getSearchDTO();
			Pageable pageable = cancelReportTable.getPageable();
			cancelRemarksDTO.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(AutoAllocationCancelPresenter.SEARCH_CANCEL_STATUS,cancelRemarksDTO, userName, passWord);
		}
		else
		{
			showErrorMessage(err);
		}

	}
	
	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}


	@Override
	public void resetSearchResultTableValues() {
		//cancelReportTable.getPageable().setPageNumber(1);
		
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof AutoAllocationCancelReportTable) {
				((AutoAllocationCancelReportTable) comp).removeRow();
			}
		}

	}

	@Override
	public void list(Page<AutoAllocationCancelDetailReportDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			cancelReportTable.setTableList(tableRows);
//			hospitalTable.tablesize();
			//cancelReportTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Auto Allocation Cancel Report");
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
					fireViewEvent(MenuItemBean.AUTO_ALLOCATION_CANCEL_REPORT, null);
				}
			});
		}
		autoallocCancelForm.enableButtons();
	}
	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(cancelReportTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
		excelExport.setReportTitle("Auto Allocation Cancel Report");
		excelExport.export();
		
	}


}
