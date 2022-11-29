package com.shaic.claim.cashlessprocess.flagreconsiderationrequest.showwizard;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.GMVPView;
import com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search.SearchFlagReconsiderationReqTableDTO;
import com.shaic.claim.clearcashless.dto.SearchClearCashlessDTO;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ReconsiderationFlagRequestViewImpl extends AbstractMVPView implements ReconsiderationFlagRequestView{

	private static final long serialVersionUID = 1180651477021369571L;


	@Inject
	private Instance<ReconsiderationFlagRequestUI> clearCashlessUIInstance;	 
	
	private ReconsiderationFlagRequestUI reconsiderationFlagRequestUI;
	
	
	@PostConstruct
	public void init() {
		addStyleName("view");
	        
	}
	
	@Override
	public void resetView() {
		if(reconsiderationFlagRequestUI != null) {
			reconsiderationFlagRequestUI.init();
		}
		
	}

	@Override
	public void initView(SearchFlagReconsiderationReqTableDTO searchResult) {
		 setSizeFull();
		 reconsiderationFlagRequestUI = clearCashlessUIInstance.get();
		 reconsiderationFlagRequestUI.init();
		 reconsiderationFlagRequestUI.initView(searchResult, searchResult.getNewIntimationDTO(), searchResult.getClaimDto() );         
	     setCompositionRoot(reconsiderationFlagRequestUI);
	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Flag Reconsideration Request completed successfully !!!!.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Flag Reconsideration Request Home");
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
				fireViewEvent(MenuItemBean.FLAG_RECONSIDERATION_REQUEST, null);

			}
		});
		
	}

	@Override
	public void buildFailureLayout() {
		Label successLabel = new Label(
				"<b style = 'color: red;'>Flag Reconsideration Request Falied. </b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Flag Reconsideration Request Home");
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
				fireViewEvent(MenuItemBean.FLAG_RECONSIDERATION_REQUEST, null);

			}
		});
		
	}

	

}
