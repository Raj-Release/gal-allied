package com.shaic.claim.reports.agentbrokerreport;

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

public class AgentBrokerReportViewImpl extends AbstractMVPView implements AgentBrokerReportView{
	@Inject
	private AgentBrokerReportForm agentReportForm;

	@Inject
	private AgentBrokerReportTable agentReportTable;

	private VerticalSplitPanel mainPanel;
	
	private AgentBrokerReportFormDTO searchDto;
	
	private ExcelExport excelExport;
	
	VerticalLayout secondLayout;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		agentReportForm.init();
		agentReportTable.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(agentReportForm);
		mainPanel.setSecondComponent(agentReportTable);
		mainPanel.setSplitPosition(25);
		setHeight("725px");
//		mainPanel.setHeight("380px");
//		agentReportTable.setHeight("800px");
		setCompositionRoot(mainPanel);
		agentReportTable.addSearchListener(this);
		agentReportForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void resetView() {
		agentReportForm.refresh();

	}
	
  
	@Override
	public void doSearch() {
		String err=agentReportForm.validate();
		if(err == null)
		{
		AgentBrokerReportFormDTO hospitalDTO = agentReportForm.getSearchDTO();
			Pageable pageable = agentReportTable.getPageable();
			hospitalDTO.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(
					BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(
					BPMClientContext.PASSWORD);
			fireViewEvent(
					AgentBrokerReportPresenter.AGENT_BROKER_REPORT,
					hospitalDTO, userName, passWord);
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
		agentReportTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof AgentBrokerReportTable) {
				((AgentBrokerReportTable) comp).removeRow();
			}
		}

	}

	@Override
	public void list(Page<AgentBrokerReportTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			agentReportTable.setTableList(tableRows);
			//agentReportTable.setPageLength(tableRows.getPageItems().size());
			//agentReportTable.tablesize();
			//agentReportTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Agent/Broker Home");
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
					fireViewEvent(MenuItemBean.AGENT_BROKER_REPORT, null);
				}
			});
		}
		agentReportForm.enableButtons();
	}
	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(agentReportTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
		excelExport.setReportTitle("Agent/Broker Report");
		excelExport.export();
		
	}


}
