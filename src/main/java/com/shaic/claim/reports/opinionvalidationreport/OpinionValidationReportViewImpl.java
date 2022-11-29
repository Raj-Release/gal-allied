package com.shaic.claim.reports.opinionvalidationreport;

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
import com.vaadin.server.VaadinSession;
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

/**
 * @author GokulPrasath.A
 *
 */
public class OpinionValidationReportViewImpl extends AbstractMVPView implements OpinionValidationReportView {
	@Inject
	private OpinionValidationReportForm opinionValidationReportForm;

	@Inject
	private OpinionValidationReportTable opinionValidationReportTable;

	private VerticalSplitPanel mainPanel;
	
	private ExcelExport excelExport;
	
	private String userName;
	
	VerticalLayout secondLayout = null;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		opinionValidationReportForm.init();
		opinionValidationReportTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(opinionValidationReportForm);
		mainPanel.setSecondComponent(opinionValidationReportTable);
		mainPanel.setSplitPosition(25);
		setHeight("725px");
		setCompositionRoot(mainPanel);
		opinionValidationReportTable.addSearchListener(this);
		opinionValidationReportForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void resetView() {
		opinionValidationReportForm.refresh();

	}
	
  
	@Override
	public void doSearch() {
		OpinionValidationReportFormDTO bean=opinionValidationReportForm.validate();
		if(bean != null)
		{
			Pageable pageable = opinionValidationReportTable.getPageable();
			bean.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(
					BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(
					BPMClientContext.PASSWORD);
			fireViewEvent(OpinionValidationReportPresenter.OPINION_VALIDATION_REPORT,
					bean, userName, passWord);
		}
	}	
	


	@Override
	public void resetSearchResultTableValues() {
		opinionValidationReportTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof OpinionValidationReportTable) {
				((OpinionValidationReportTable) comp).removeRow();
			}
		}
		opinionValidationReportForm.setDefaultValues();
	}

	@Override
	public void list(Page<OpinionValidationReportTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			opinionValidationReportTable.setTableList(tableRows);
			opinionValidationReportTable.tablesize();
			opinionValidationReportTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Opinion Validation Report Status Home");
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
					fireViewEvent(MenuItemBean.OPINION_VALIDATION_REPORT, null);
				}
			});
		}
		opinionValidationReportForm.enableButtons();
	}
	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(opinionValidationReportTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
		excelExport.setReportTitle("Opinion Validation Report");
		excelExport.export();
		
	}


	public void init(BeanItemContainer<SelectValue> roleContainer, BeanItemContainer<SelectValue> empNameContainer) {
		userName = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		opinionValidationReportForm.setDropDownValues(userName, roleContainer, empNameContainer);
		resetView();
	}

}
