package com.shaic.reimbursement.uploadrodreports;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class UploadInvestigationReportLayout extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;

	private Label lblUploadInvestigationReport;
	
	private Button btnUpload;

	@Inject
	private UploadInvestigationDocumentsTable uploadInvestigationDocumentsTable;

	public void init() {
		lblUploadInvestigationReport = new Label("Upload Investigation Report");
		lblUploadInvestigationReport.setStyleName(Reindeer.LABEL_H2);
		uploadInvestigationDocumentsTable.init("", false, false);
		btnUpload = new Button("Upload");
		mainLayout = new VerticalLayout(lblUploadInvestigationReport,
				uploadInvestigationDocumentsTable, btnUpload);
		mainLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_RIGHT);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		setCompositionRoot(mainLayout);
	}

}
