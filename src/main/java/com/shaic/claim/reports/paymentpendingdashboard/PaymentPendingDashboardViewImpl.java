package com.shaic.claim.reports.paymentpendingdashboard;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class PaymentPendingDashboardViewImpl extends AbstractMVPView implements PaymentPendingDashboardView{
	@Inject
	private PaymentPendingDashboardForm paymentPendingDashboardForm;

	@Inject
	private PaymentPendingDashboardTable paymentPendingDashboardTable;

	private VerticalLayout mainPanel;
	
	private ExcelExport excelExport;
	
	VerticalLayout secondLayout = null;
	
	//private DateField dateField;
	//private DateField toDateField;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		paymentPendingDashboardForm.init();
		paymentPendingDashboardTable.init("", false, false);
		mainPanel = new VerticalLayout();
		mainPanel.addComponent(paymentPendingDashboardForm);
		setCompositionRoot(mainPanel);
		paymentPendingDashboardForm.addSearchListener(this);
		setVisible(true);
		resetView();
	}
	
	
	@Override
	public void list(Page<PaymentPendingDashboardTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			paymentPendingDashboardTable.setTableList(tableRows);
			paymentPendingDashboardTable.tablesize();
			paymentPendingDashboardTable.setVisible(false);
			mainPanel.addComponent(paymentPendingDashboardTable);
			excelExport = new  ExcelExport(paymentPendingDashboardTable.getTable());
			excelExport.excludeCollapsedColumns();
			excelExport.setDisplayTotals(false);
			excelExport.setReportTitle("Payment Pending Dashboard");
			excelExport.export();
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Payment Pending Dashboard Home");
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
					fireViewEvent(MenuItemBean.PAYMENT_PENDING_DASHBOARD, null);
				}
			});
		}
		paymentPendingDashboardForm.enableButtons();
	}


	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		paymentPendingDashboardForm.refresh();
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

