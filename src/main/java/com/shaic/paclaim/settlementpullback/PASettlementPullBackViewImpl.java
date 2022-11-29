package com.shaic.paclaim.settlementpullback;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.GMVPView;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.paclaim.settlementpullback.dto.PASearchSettlementPullBackDTO;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PASettlementPullBackViewImpl extends AbstractMVPView implements PASettlementPullBackView, GMVPView{

	private static final long serialVersionUID = 1180651477021369571L;


	@Inject
	private Instance<PASettlementPullBackUI> clearCashlessUIInstance;	 
	
	private PASettlementPullBackUI clearCashlessUI;
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	
	@PostConstruct
	public void init() {
		addStyleName("view");
	        
	}
	
	@Override
	public void resetView() {
		if(clearCashlessUI != null) {
			clearCashlessUI.init();
		}
		
	}

	@Override
	public void initView(PASearchSettlementPullBackDTO searchResult) {
		 setSizeFull();
		 clearCashlessUI = clearCashlessUIInstance.get();
		 clearCashlessUI.init();
		 clearCashlessUI.initView(searchResult, searchResult.getNewIntimationDTO());         
	     setCompositionRoot(clearCashlessUI);
	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Claim record changed successfully !!!!.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Claim Final Approval Cancel");
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
				fireViewEvent(MenuItemBean.PA_SETTLEMENT_PULL_BACK, null);

			}
		});
		
	}

	@Override
	public void buildFailureLayout() {
		Label successLabel = new Label(
				"<b style = 'color: red;'> Clear Cashless is not possible for this Intimation. </b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Clear Cashless Home");
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
				fireViewEvent(MenuItemBean.PA_CLEAR_CASHLESS, null);

			}
		});
		
	}
	

}
