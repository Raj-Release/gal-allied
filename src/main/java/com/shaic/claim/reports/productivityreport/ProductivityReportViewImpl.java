package com.shaic.claim.reports.productivityreport;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ProductivityReportViewImpl extends AbstractMVPView implements ProductivityReportView{
	@Inject
	private ProductivityReportForm productivityReportForm;

	@Inject
	private ProductivityReportTable productivityReportTable;

	private VerticalLayout mainPanel;
	
	private ExcelExport excelExport;
	
	VerticalLayout secondLayout = null;
	
	//private DateField dateField;
	//private DateField toDateField;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		productivityReportForm.init();
		productivityReportTable.init("", false, false);
		mainPanel = new VerticalLayout();
		mainPanel.addComponent(productivityReportForm);
		setCompositionRoot(mainPanel);
		productivityReportForm.addSearchListener(this);
		setVisible(true);
		resetView();
	}
	
	
	@Override
	public void list(Page<ProductivityReportTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			productivityReportTable.setTableList(tableRows);
			productivityReportTable.tablesize();
			productivityReportTable.setVisible(false);
			mainPanel.addComponent(productivityReportTable);
			excelExport = new  ExcelExport(productivityReportTable.getTable());
			excelExport.excludeCollapsedColumns();
			excelExport.setDisplayTotals(false);
			excelExport.setReportTitle("Productivity Report");
			excelExport.export();
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Productivity Report Status Home");
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
				
				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.PRODUCTIVITY_REPORT, null);
				}
			});
		}
		productivityReportForm.enableButtons();
	}


	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		productivityReportForm.refresh();
	}


	@Override
	public void setupDroDownValues() {
		// TODO Auto-generated method stub
		productivityReportForm.setDropDownValues();
	}


	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}
	
}
