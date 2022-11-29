package com.shaic.claim.bedphoto;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class UploadBedPhotoDetailsLayout extends ViewComponent {


	
	private VerticalLayout mainLayout;

	private Label lblUploadBedPhoto;
	
	private Button btnUpload;
	
	@Inject
	private UploadBedPhotoTable bedUploadTable;
	
	public void init() {
		lblUploadBedPhoto = new Label("Upload Bed Photo");
		lblUploadBedPhoto.setStyleName(Reindeer.LABEL_H2);
		bedUploadTable.init("", false, false);
		btnUpload = new Button("Upload");
		mainLayout = new VerticalLayout(lblUploadBedPhoto,
				bedUploadTable, btnUpload);
		mainLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_RIGHT);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		setCompositionRoot(mainLayout);
	}


}
