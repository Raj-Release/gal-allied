package com.shaic.claim.reports.cpuwiseperformancedetail;

import java.util.Iterator;
import java.util.Map;

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

public class CpuWisePerformanceReportViewImpl extends AbstractMVPView implements CpuWisePerformanceReportView{
	@Inject
	private CpuWisePerformanceReportForm cpuReportForm;

	@Inject
	private CpuWisePerformanceReportTable cpuReportTable;

	private VerticalSplitPanel mainPanel;
	
	//private CpuWisePerformanceReportFormDTO searchDto;
	
	private ExcelExport excelExport;
	
	VerticalLayout secondLayout = null;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		cpuReportForm.init();
		cpuReportTable.init("", false, true); 
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(cpuReportForm);
		mainPanel.setSecondComponent(cpuReportTable);
		mainPanel.setSplitPosition(25);
		setHeight("725px");
		//mainPanel.setHeight("380px");
		setCompositionRoot(mainPanel);
		cpuReportTable.addSearchListener(this);
		cpuReportForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void resetView() {
		cpuReportForm.refresh();

	}
	
  
	@Override
	public void doSearch() {		
	
		CpuWisePerformanceReportFormDTO bean = cpuReportForm.validate();
		
		if(bean != null)
		{
			Pageable pageable = cpuReportTable.getPageable();
			bean.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(
					BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(
					BPMClientContext.PASSWORD);
			fireViewEvent(
					CpuWisePerformanceReportPresenter.CPU_WISE_REPORT,
					bean, userName, passWord);
		}
		
	}
	
	
	

	private Map<String, Object> validate() {
		// TODO Auto-generated method stub
		return null;
	}


	


	@Override
	public void resetSearchResultTableValues() {
		cpuReportTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof CpuWisePerformanceReportTable) {
				((CpuWisePerformanceReportTable) comp).removeRow();
			}
		}

	}

	@Override
	public void list(Page<CpuWisePerformanceReportTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			cpuReportTable.setTableList(tableRows);
//			cpuReportTable.tablesize();
			cpuReportTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Cpu Wise Performance Home");
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
					fireViewEvent(MenuItemBean.CPU_WISE_PERFORMANCE_REPORT, null);
				}
			});
		}
		cpuReportForm.enableButtons();
	}
	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(cpuReportTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
		excelExport.setReportTitle("Cpu Wise Performance Report");
		excelExport.export();
		
	}


	
}
