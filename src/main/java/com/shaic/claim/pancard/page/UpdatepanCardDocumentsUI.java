package com.shaic.claim.pancard.page;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.ui.VerticalLayout;

public class UpdatepanCardDocumentsUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UploadedPanCardDocumentsTable uploadedPanCardDocumentsTable;
	
	private VerticalLayout mainLayout;
	
	public void init(){
		uploadedPanCardDocumentsTable.init("", false, false);
		mainLayout = new VerticalLayout(uploadedPanCardDocumentsTable);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		setCompositionRoot(mainLayout);
	}
	
	

}
