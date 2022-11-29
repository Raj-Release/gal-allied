package com.shaic.claim.aadhar.pages;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class UpdateAadharDetailsLayout extends ViewComponent{
	
	private VerticalLayout mainLayout;

	private Label lblUploadInvestigationReport;
	
	private Button btnUpload;
	
	@Inject
	private UpdateAadharDetailsTable aadharDetailsTable;
	
	public void init() {
		lblUploadInvestigationReport = new Label("Upload Investigation Report");
		lblUploadInvestigationReport.setStyleName(Reindeer.LABEL_H2);
		aadharDetailsTable.init("", false, false);
		btnUpload = new Button("Upload");
		mainLayout = new VerticalLayout(lblUploadInvestigationReport,
				aadharDetailsTable, btnUpload);
		mainLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_RIGHT);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		setCompositionRoot(mainLayout);
	}

}
