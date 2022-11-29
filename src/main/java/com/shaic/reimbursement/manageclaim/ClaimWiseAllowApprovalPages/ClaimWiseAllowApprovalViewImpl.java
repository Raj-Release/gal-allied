package com.shaic.reimbursement.manageclaim.ClaimWiseAllowApprovalPages;

import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.manageclaim.searchClaimwiseApproval.SearchClaimWiseAllowApprovalDto;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ClaimWiseAllowApprovalViewImpl extends AbstractMVPView implements ClaimWiseAllowApprovalView{
	
	@Inject
	private Instance<ClaimWiseAllowApprovalPage> claimWiseApprovalPageInstance;
	
	private ClaimWiseAllowApprovalPage claimWiseApprovalPage;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Override
	public void init(SearchClaimWiseAllowApprovalDto dto) {
		
		claimWiseApprovalPage = claimWiseApprovalPageInstance.get();
		claimWiseApprovalPage.init(dto);
		
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(dto.getIntimationDto(),dto.getClaimDto(),"");
		
		VerticalLayout mainVertical = new VerticalLayout(intimationDetailsCarousel,claimWiseApprovalPage);
		mainVertical.setSpacing(true);
		
		
		setCompositionRoot(mainVertical);
	}
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTableList(List<ClaimWiseApprovalDto> listDetails) {
		claimWiseApprovalPage.setTableList(listDetails);
	}

	
	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Claim wise allow approval submitted successfully.</b>",
				ContentMode.HTML);

		Button homeButton = new Button("Home Page");
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
				fireViewEvent(MenuItemBean.CLAIM_WISE_ALLOW_APPROVAL, null);

			}
		});
	}

}
