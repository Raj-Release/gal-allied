package com.shaic.claim.reports.dailyreport;

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

public class DailyReportViewImpl extends AbstractMVPView implements DailyReportView{
	@Inject
	private DailyReportForm dailyReportForm;

	@Inject
	private DailyReportTable dailyReportTable;

	private VerticalSplitPanel mainPanel;
	
	//private DailyReportFormDTO searchDto;
	
	private ExcelExport excelExport;
	
	VerticalLayout secondLayout = null;
	
	//private DateField dateField;
	//private DateField toDateField;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		dailyReportForm.init();
		dailyReportTable.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(dailyReportForm);
		mainPanel.setSecondComponent(dailyReportTable);
		mainPanel.setSplitPosition(25);
		setHeight("725px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		dailyReportTable.addSearchListener(this);
		dailyReportForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void resetView() {
		dailyReportForm.refresh();

	}
	
  
	@Override
	public void doSearch() {
		DailyReportFormDTO bean=dailyReportForm.validate();
		if(bean != null)
		{
//		DailyReportFormDTO hospitalDTO = dailyReportForm.getSearchDTO();
			Pageable pageable = dailyReportTable.getPageable();
			bean.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(
					BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(
					BPMClientContext.PASSWORD);
			fireViewEvent(DailyReportPresenter.DAILY_REPORT,
					bean, userName, passWord);
		}
	}	
	


	@Override
	public void resetSearchResultTableValues() {
		dailyReportTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof DailyReportTable) {
				((DailyReportTable) comp).removeRow();
			}
		}

	}

	@Override
	public void list(Page<DailyReportTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			dailyReportTable.setTableList(tableRows);
			dailyReportTable.tablesize();
			dailyReportTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Daily Report Status  Home");
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
					fireViewEvent(MenuItemBean.DAILY_REPORT, null);
				}
			});
		}
		dailyReportForm.enableButtons();
	}
	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(dailyReportTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
		excelExport.setReportTitle("Daily Report");
		excelExport.export();
		
	}


	

}
