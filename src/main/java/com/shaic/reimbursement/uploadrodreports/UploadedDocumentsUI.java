package com.shaic.reimbursement.uploadrodreports;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.ui.VerticalLayout;

public class UploadedDocumentsUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UploadedDocumentsTable uploadedDocumentsTable;
	
	private VerticalLayout mainLayout;
	
	public void init(){
		uploadedDocumentsTable.init("", false, false);
		mainLayout = new VerticalLayout(uploadedDocumentsTable);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		setCompositionRoot(mainLayout);
	}
	
	

}
