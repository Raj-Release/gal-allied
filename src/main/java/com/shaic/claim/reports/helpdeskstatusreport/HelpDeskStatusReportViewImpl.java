package com.shaic.claim.reports.helpdeskstatusreport;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class HelpDeskStatusReportViewImpl extends AbstractMVPView implements HelpDeskStatusReportView{
	@Inject
	private  HelpDeskStatusReportForm helpDeskForm;

	@Inject
	private  HelpDeskStatusReportTable helpDeskTable;

	private VerticalSplitPanel mainPanel;
	
	private  HelpDeskStatusReportFormDTO searchDto;
	
	private ExcelExport excelExport;

	private CheckBox chkSeniorCitizenClaim;
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		helpDeskForm.init();
		helpDeskTable.init("", false, true);
		Panel tablePanel= new Panel(helpDeskTable);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(helpDeskForm);
		mainPanel.setSecondComponent(tablePanel);
		mainPanel.setSplitPosition(53);
		//setHeight("650px");
		mainPanel.setHeight("615px");
		setCompositionRoot(mainPanel);
		helpDeskTable.addSearchListener(this);
		helpDeskForm.addSearchListener(this);
		resetView();
	}

	@Override
	public void resetView() {
		helpDeskForm.refresh();
		helpDeskTable.removeRow();

	}
	

	@Override
	public void doSearch() {
		String err=helpDeskForm.validate();
		if(("").equalsIgnoreCase(err))
		{
		HelpDeskStatusReportFormDTO helpDeskDTO = helpDeskForm.getSearchDTO();
			Pageable pageable = helpDeskTable.getPageable();
			helpDeskDTO.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(
					BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(
					BPMClientContext.PASSWORD);
			fireViewEvent(
					HelpDeskStatusReportPresenter.HELP_DESK_STATUS_REPORT,
					helpDeskDTO, userName, passWord);
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
		helpDeskTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof HelpDeskStatusReportTable) {
				((HelpDeskStatusReportTable) comp).removeRow();
				
			}		}
		
	}

	@Override
	public void list(Page< HelpDeskStatusReportTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			helpDeskTable.setTableList(tableRows);
			helpDeskTable.tablesize();
			helpDeskTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Help Desk Status "
					+ ""
					+ "Report Home");
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
					fireViewEvent(MenuItemBean.HELP_DESK_STATUS_REPORT, null);
				}
			});
		}
		helpDeskForm.enableButtons();
	}

	
	@Override
	public void init(BeanItemContainer<SelectValue> cpu,BeanItemContainer<SelectValue> claimType) {
		helpDeskForm.setDropDownValues(cpu,claimType);

		
	}
	
	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(helpDeskTable.getTable());
		excelExport.setDisplayTotals(false);
		excelExport.excludeCollapsedColumns();
		excelExport.setReportTitle("Help Desk Report");
		excelExport.export();
		
	}

	@Override
	public void resetSearchValue()
	{
		helpDeskForm.clearFields();
		helpDeskTable.resetTable();
	}

}
