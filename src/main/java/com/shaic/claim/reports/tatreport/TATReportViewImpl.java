 package com.shaic.claim.reports.tatreport;

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
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class TATReportViewImpl extends AbstractMVPView implements TATReportView {

	@Inject
	private TATReportForm tatForm;

	@Inject
	private TATReportTable tatTable;

	private VerticalSplitPanel mainPanel;
	
	private TATReportFormDTO searchDto;
	
	private ExcelExport excelExport;
	
	VerticalLayout secondLayout = null;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		tatForm.init();
		tatTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(tatForm);
		mainPanel.setSecondComponent(tatTable);
		mainPanel.setSplitPosition(38);
	//	setHeight("650px");
		mainPanel.setHeight("692px");
		setCompositionRoot(mainPanel);
		tatTable.addSearchListener(this);
		tatForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void resetView() {
		tatForm.refresh();

	}
	

	@Override
	public void doSearch() {
	
		String err=tatForm.validate();
		if(err == null || err.isEmpty())
		{
			TATReportFormDTO fvrDTO = tatForm.getSearchDTO();
			if(fvrDTO.getCpuCode() == null){
				
			}
			Pageable pageable = tatTable.getPageable();
			fvrDTO.setPageable(pageable); 
			String userName = (String) getUI().getSession().getAttribute(
					BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(
					BPMClientContext.PASSWORD);
			fireViewEvent(
					TATReportPresenter.SEARCH_BUTTON_CLICK_TAT,
	                     fvrDTO, userName, passWord);
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
		tatTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof TATReportTable) {
				((TATReportTable) comp).removeRow();
			}
		}
		tatForm.setValidationVisible(false);

	}

	@Override
	public void list(Page<TATReportTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			tatTable.setTableList(tableRows);			
//			fvrTable.tablesize();
			tatForm.validateExportExcel(tableRows);
			tatTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("TAT Report Home");
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
			tatForm.validateExportExcel(tableRows);
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.TAT_REPORT, null);
					
					
				}
			});
		}
		tatForm.enableButtons();
	}


	/*@Override
	public void setDropDownValues(BeanItemContainer<SelectValue> dateType,
			BeanItemContainer<SelectValue> claimsQueueType,
			BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> officeCode,
			BeanItemContainer<SelectValue> tatDate) {
		
		fvrForm.setDropDownValues(dateType, claimsQueueType, cpuCode, officeCode, tatDate);
		
	}*/


	
	@Override
	public void init(BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> OfficeCode) {

		tatForm.setDropDownValues(cpuCode,OfficeCode);

	}


	@Override
	public void setOfficeCodeDropDown(BeanItemContainer<SelectValue> officeCode) {
		tatForm.setOfficeCodeDropDown(officeCode);
		
	}


	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(tatTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
		excelExport.setReportTitle("TAT Report");
		excelExport.export();
		
	}

	

	

}
