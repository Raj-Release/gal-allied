package com.shaic.claim.fss.filedetailsreport;

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

public class FileDetailsReportViewImpl extends AbstractMVPView implements
		FileDetailsReportView {
	
	
	
	@Inject
	private FileDetailsReportForm fvrForm;

	@Inject
	private FileDetailsReportTable fvrTable;

	private VerticalSplitPanel mainPanel;
	
	private ExcelExport excelExport;
	
	VerticalLayout secondLayout = null;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		fvrForm.init();
		fvrTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(fvrForm);
		mainPanel.setSecondComponent(fvrTable);
		mainPanel.setSplitPosition(38);
		setHeight("650px");
		mainPanel.setHeight("675px");
		setCompositionRoot(mainPanel);
		fvrTable.addSearchListener(this);
		fvrForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void resetView() {
		fvrForm.refresh();

	}
	

	@SuppressWarnings("null")
	@Override
	public void doSearch() {
		String err = fvrForm.validate();
		if(err == null || err.isEmpty())
		{
			FileDetailsReportFormDTO fvrDTO = fvrForm.getSearchDTO();
			Pageable pageable = fvrTable.getPageable();
			fvrDTO.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(
					BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(
					BPMClientContext.PASSWORD);
			fireViewEvent(
					FileDetailsReportPresenter.SEARCH_BUTTON_CLICK_FILE_REQUEST,
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
		fvrTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof FileDetailsReportTable) {
				((FileDetailsReportTable) comp).removeRow();
			}
		}

	}

	@Override
	public void list(Page<FileDetailsReportTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			fvrTable.setTableList(tableRows);
//			fvrTable.tablesize();
			fvrTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("File Details Report Home");
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
					fireViewEvent(MenuItemBean.FILE_DETAILS_REPORT, null);
				}
			});
		}
		fvrForm.enableButtons();
	}

	@Override
	public void init() {

		fvrForm.setDropDownValues();

	}

	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(fvrTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
		excelExport.setReportTitle("File Details Report");
		excelExport.export();
		
	}


	@Override
	public void resetSearchFileReportView() {
		// TODO Auto-generated method stub
		fvrForm.resetSearchFileDetailsFields();
		resetSearchResultTableValues();
	}

}
