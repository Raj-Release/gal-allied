package com.shaic.reimbursement.claims_alert.search;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.GMVPView;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class ClaimsAlertMasterViewImpl extends AbstractMVPView implements GMVPView, ClaimsAlertMasterView {

	@Inject
	private Instance<ClaimsAlertMasterUI> claimsAlertMasterUIInstance;
	
	private ClaimsAlertMasterUI claimsAlertMasterUIobj;

	private VerticalSplitPanel mainPanel;

	@PostConstruct
	public void init() {
		addStyleName("view");
	}

	@Override
	public void resetView() {
		
		if(claimsAlertMasterUIobj != null) {
			claimsAlertMasterUIobj.init();
		}

	}

	@Override
	public void initView() {

		setSizeFull();
		claimsAlertMasterUIobj = claimsAlertMasterUIInstance.get();
		claimsAlertMasterUIobj.initView();         
		setCompositionRoot(claimsAlertMasterUIobj);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void buildSuccessLayout() {
		
		Label successLabel = new Label(
				"<b style = 'color: green;'> Record Updated Successfully.</b>",
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
				fireViewEvent(MenuItemBean.CLAIMS_ALERT_MASTER_SCREEN, null);
			}
		});



	}

	@Override
	@SuppressWarnings("deprecation")
	public void buildFailureLayout(String message) {

		Label successLabel = new Label("<b style = 'color: black;'>"+message+"</b>", ContentMode.HTML);			
		Button homeButton = new Button("Claims alert Home");
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
				fireViewEvent(MenuItemBean.CLAIMS_ALERT_MASTER_SCREEN, null);

			}
		});


	}

	@Override
	public void generateTableForClaimsAlerts(List<ClaimsAlertTableDTO> claimsAlertTableDTOs,Map<String, Object> referenceData) {

		if(claimsAlertMasterUIobj != null) {
			claimsAlertMasterUIobj.generateTableForClaimAlert(claimsAlertTableDTOs,referenceData);
		}
	}

}
