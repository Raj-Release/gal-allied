package com.shaic.claim.reports.hospitalintimationstatus;

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

public class HospitalIntimationReportStatusViewImpl extends AbstractMVPView implements HospitalIntimationStatusReportView
{
	@Inject
	private HospitalIntimationReportStatusForm hospitalForm;

	@Inject
	private HospitalIntimationReportStatusTable hospitalTable;

	private VerticalSplitPanel mainPanel;
	
	private HospitalIntimationStatusReportFormDTO searchDto;
	
	private ExcelExport excelExport;
	
	VerticalLayout secondLayout = null;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		hospitalForm.init();
		hospitalTable.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(hospitalForm);
		mainPanel.setSecondComponent(hospitalTable);
		mainPanel.setSplitPosition(25);
		setHeight("725px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		hospitalTable.addSearchListener(this);
		hospitalForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void resetView() {
		hospitalForm.refresh();

	}
	
  
	@Override
	public void doSearch() {
		String err=hospitalForm.validate();
		if(err == null)
		{
		HospitalIntimationStatusReportFormDTO hospitalDTO = hospitalForm.getSearchDTO();
			Pageable pageable = hospitalTable.getPageable();
			hospitalDTO.setPageable(pageable);
			String userName = (String) getUI().getSession().getAttribute(
					BPMClientContext.USERID);
			String passWord = (String) getUI().getSession().getAttribute(
					BPMClientContext.PASSWORD);
			fireViewEvent(
					HospitalIntimationReportStatusPresenter.HOSPITAL_INTIMATION_REPORT,
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
		hospitalTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while (componentIter.hasNext()) {
			Component comp = (Component) componentIter.next();
			if (comp instanceof HospitalIntimationReportStatusTable) {
				((HospitalIntimationReportStatusTable) comp).removeRow();
			}
		}

	}

	@Override
	public void list(Page<HospitalIntimationReportStatusTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems()
				&& 0 != tableRows.getPageItems().size()) {
			hospitalTable.setTableList(tableRows);
//			hospitalTable.tablesize();
			hospitalTable.setHasNextPage(tableRows.isHasNext());
		} else {

			Label successLabel = new Label(
					"<b style = 'color: black;'>No Records found.</b>",
					ContentMode.HTML);
			Button homeButton = new Button("Hospital Intimation Status Home");
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
					fireViewEvent(MenuItemBean.HOSPITAL_INTIMATION_STATUS, null);
				}
			});
		}
		hospitalForm.enableButtons();
	}
	@Override
	public void generateReport() {
		excelExport = new  ExcelExport(hospitalTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
		excelExport.setReportTitle("Hospital Intimation Status Report");
		excelExport.export();
		
	}

}
