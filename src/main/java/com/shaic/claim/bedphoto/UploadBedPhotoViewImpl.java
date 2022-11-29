package com.shaic.claim.bedphoto;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.domain.ReferenceTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class UploadBedPhotoViewImpl extends AbstractMVPView implements UploadBedPhotoView{
	
	@Inject
	private UploadBedPhotoDetailsPageUI uploadBedPhotoPageUI;
	
	public void init(SearchBedPhotoTableDTO tableDto){
		VerticalLayout mainLayout = new VerticalLayout();
		uploadBedPhotoPageUI.init(tableDto ,mainLayout,ReferenceTable.UPLOAD_BED_PHOTO,null);
		mainLayout.addComponent(uploadBedPhotoPageUI);
		mainLayout.setSizeFull();
		mainLayout.setSpacing(Boolean.TRUE);
		setCompositionRoot(mainLayout);
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTableValues(Long intimationKey) {
		uploadBedPhotoPageUI.setUploadTableValues(intimationKey);
	}

	@Override
	public void buildSuccessLayout() {

		Label successLabel = new Label(
				"<b style = 'color: black;'>Uploaded Bed Photo successfully !!! </b>",
				ContentMode.HTML);

		Button homeButton = new Button("Home Page");
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
				fireViewEvent(MenuItemBean.UPLOAD_BED_PHOTO, true);

			}
		});

	}


}
