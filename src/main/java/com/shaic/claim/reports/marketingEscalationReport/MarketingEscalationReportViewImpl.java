package com.shaic.claim.reports.marketingEscalationReport;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class MarketingEscalationReportViewImpl extends AbstractMVPView
		implements MarketingEscalationReportView {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8773748642488511377L;

	@Inject
	private MarketingEscalationReportForm marketingEscalationReportForm;
	
	@Inject
	private MarketingEscalationReportTable marketingEscalationReportTable;
	
	private ExcelExport excelExport;
	private HorizontalLayout buttonHorLayout;
	
	//private VerticalSplitPanel mainPanel;
	private VerticalLayout mainPanel;
	
	private Button xmlReport;
	
	@PostConstruct
	protected void initView() {
		
		addStyleName("view");
		setSizeFull();
		marketingEscalationReportForm.init();
		marketingEscalationReportTable.init("", false, false);
		
		xmlReport = new Button("Export To Excel");
		xmlReport.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		buttonHorLayout=new HorizontalLayout(xmlReport);
		
		mainPanel = new VerticalLayout(marketingEscalationReportForm,marketingEscalationReportTable,buttonHorLayout);
		mainPanel.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		mainPanel.setSpacing(true);
		setCompositionRoot(mainPanel);
		marketingEscalationReportTable.addSearchListener(this);
		marketingEscalationReportForm.addSearchListener(this);
		resetView();
		addReportListener();
		
	}

	private void addReportListener() {


		xmlReport.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;


				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MarketingEscalationReportPresenter.GENERATE_REPORT, null,null);
				
			}
		});
	
		
	
		
	}

	@Override
	public void doSearch() {

		MarketingEscalationReportFormDTO bean=marketingEscalationReportForm.validate();
		if(bean != null)
		{
			Pageable pageable = marketingEscalationReportTable.getPageable();
			bean.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(
					BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(
					BPMClientContext.PASSWORD);
			fireViewEvent(MarketingEscalationReportPresenter.MARKETING_ESCALATION_REPORT,
					bean, userName, passWord);
		}
	

	}

	@Override
	public void resetSearchResultTableValues() {
		marketingEscalationReportTable.getPageable().setPageNumber(1);
		marketingEscalationReportTable.resetTable();
		marketingEscalationReportTable.removeRow();

	}

	@Override
	public void resetView() {
		if(marketingEscalationReportForm != null){
			marketingEscalationReportForm.refresh();
		}

	}

	public void init() {
		
		resetView();
	}

	@Override
	public void list(Page<MarketingEscalationReportTableDTO> tableRows) {

		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			marketingEscalationReportTable.setTableList(tableRows);
			marketingEscalationReportTable.tablesize();
			marketingEscalationReportTable.setHasNextPage(tableRows.isHasNext());
		} else {
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("No Records found.", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MenuItemBean.MARKETING_ESCALATION_REPORT, null);
				}
			});
		}
		marketingEscalationReportForm.enableButtons();
	
		
	}

	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(marketingEscalationReportTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
		excelExport.setReportTitle("Marketing Escalation Report");
		excelExport.export();
		
	}

}
