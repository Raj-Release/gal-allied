package com.shaic.claim.userproduct.document.search;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class USerActivationViewImpl extends AbstractMVPView implements UserACtivationView{
	@Inject 
	private UserActivationUI productDocumentPage;
	
	private UserManagementDTO bean;
	@Override
	public void submitValues() {

		Label successLabel = new Label(
				"<b style = 'color: green;'> User Mapped Successfully</b>",
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
				fireViewEvent(MenuItemBean.USER_MANAGEMENT,true);

			}
		});
	
		
	}
	
	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(UserManagementDTO productDocTypeDto,
			BeanItemContainer<SelectValue> userTypeContainer,
			BeanItemContainer<SelectValue> documentTypeContainer,
			BeanItemContainer<SelectValue> claimFlagTypeContainer) {

		this.bean = productDocTypeDto;
		
		productDocumentPage.init(bean, userTypeContainer,documentTypeContainer,claimFlagTypeContainer);
		productDocumentPage.setCaption("User Creation");
		VerticalLayout mainVertical = new VerticalLayout(productDocumentPage);
		mainVertical.setSpacing(true);
		setCompositionRoot(mainVertical);
		
	}
	

}
