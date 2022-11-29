package com.shaic.claim.aadhar.pages;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.aadhar.search.SearchUpdateAadharTableDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.carousel.RevisedCarousel;
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

public class UpdateAadharDetailsViewImpl extends AbstractMVPView implements UpdateAadharDetailsView{
	
	@Inject
	private UpdateAadharDetailsPageUI updateAadharDetailsPageUI;
	
	@Inject
	private RevisedCarousel preauthIntimationDetailsCarousel;
	
	@Inject
	private ViewDetails viewDetails;
	
	public void init(SearchUpdateAadharTableDTO tableDTO){
		preauthIntimationDetailsCarousel.init(tableDTO.getNewIntimationDto(), "Update Aadhar Details");
		viewDetails.initView(tableDTO.getNewIntimationDto().getIntimationId(),0l, ViewLevels.PREAUTH_MEDICAL,"Update Aadhar Details");
		HorizontalLayout viewDetailsLayout = new HorizontalLayout(viewDetails);
		viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);

		viewDetailsLayout.setSpacing(true);
		viewDetailsLayout.setMargin(true);
		viewDetailsLayout.setSizeFull();
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponent(preauthIntimationDetailsCarousel);
		mainLayout.addComponent(viewDetailsLayout);
		updateAadharDetailsPageUI.init(tableDTO ,mainLayout,ReferenceTable.UPDATE_AADHAR_SCREEN,null);
		mainLayout.addComponent(updateAadharDetailsPageUI);
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
		updateAadharDetailsPageUI.setUploadTableValues(intimationKey);
	}

	@Override
	public void result() {

		Label successLabel = new Label(
				"<b style = 'color: black;'>Update Aadhar Details completed successfully !!! </b>",
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
				fireViewEvent(MenuItemBean.UPDATE_AADHAR_DETAILS, true);

			}
		});

	}

	@Override
	public void popforAadhar() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Update Aadhar</b>",
				ContentMode.HTML);

		Button homeButton = new Button("Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

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
			}
		});
	}

	@Override
	public void aadharfailureLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Update Aadhar Not Possible</b>",
				ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

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
			}
		});
	}

}
