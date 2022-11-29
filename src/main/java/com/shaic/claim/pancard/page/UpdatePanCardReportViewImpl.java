package com.shaic.claim.pancard.page;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.pancard.search.pages.SearchUploadPanCardTableDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class UpdatePanCardReportViewImpl extends AbstractMVPView
		implements UpdatePanCardReportView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private VerticalLayout mainLayout;

	@Inject
	private UpdatePanCardReportUI updatePanCardReportUI;
	
	@Inject
	private RevisedCarousel preauthIntimationDetailsCarousel;
	
	@Inject
	private ViewDetails viewDetails;


	public void init(SearchUploadPanCardTableDTO searchUploadPanCardTableDTO) {
		preauthIntimationDetailsCarousel.init(searchUploadPanCardTableDTO.getNewIntimationDto(), "Update Pan Details");
		viewDetails.initView(searchUploadPanCardTableDTO.getNewIntimationDto().getIntimationId(),0l, ViewLevels.PREAUTH_MEDICAL,"Update Pan Details");
		//HorizontalLayout viewDetailsLayout = new HorizontalLayout(form1, btnViewEODEarlierDetails, viewDetails);
		HorizontalLayout viewDetailsLayout = new HorizontalLayout(viewDetails);
		viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
	//	viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.MIDDLE_RIGHT);
		viewDetailsLayout.setSpacing(true);
		viewDetailsLayout.setMargin(true);
		viewDetailsLayout.setSizeFull();
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponent(preauthIntimationDetailsCarousel);
		mainLayout.addComponent(viewDetailsLayout);
		updatePanCardReportUI.init(searchUploadPanCardTableDTO ,mainLayout,ReferenceTable.UPDATE_PANCARD_SCREEN,null);
		mainLayout.addComponent(updatePanCardReportUI);
		mainLayout.setSizeFull();
		mainLayout.setSpacing(Boolean.TRUE);
		setCompositionRoot(mainLayout);
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}


	@Override
	public void setUploadInvestigationReportUI(
			List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList) {
		updatePanCardReportUI
				.setCompleteLayout(investigationDetailsReimbursementTableDTOList);
	}

	@Override
	public void updateTableValues(Long investigationKey) {

		updatePanCardReportUI.setUploadTableValues(investigationKey);

	}
	
	
	
	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {/*
		
			if (!isValid) {
				Label label = new Label("Same user cannot raise request more than once from same stage", ContentMode.HTML);
				label.setStyleName("errMessage");
				VerticalLayout layout = new VerticalLayout();
				layout.setMargin(true);
				layout.addComponent(label);
				ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);
			} 
		else
		{
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("");
			popup.setWidth("85%");
			popup.setHeight("100%");
			rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance.get();
			//ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
			//documentDetails.setClaimDto(bean.getClaimDTO());
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PROCESS_UPLOAD_INVESTIGATION);
			
			
			
			rewardRecognitionRequestViewObj.init(preauthDTO, popup);
			
			//earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
			popup.setCaption("Reward Recognition Request");
			popup.setContent(rewardRecognitionRequestViewObj);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}
		*/}
	
	@Override
	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {
		// TODO Auto-generated method stub
	//	rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;
		
	}
	
 
	@Override
	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		// TODO Auto-generated method stub
	//	rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}

	

	@Override
	public void result() {

		Label successLabel = new Label(
				"<b style = 'color: black;'>Update Pan card Detail completed successfully !!! </b>",
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
				fireViewEvent(MenuItemBean.UPDATE_PAN_CARD, true);

			}
		});

	}

	@Override
	public void setReference(
			List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList,
			Long countOfAckByClaimKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReferenceForCorosal(ClaimDto claimDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTableValues(MultipleUploadDocumentDTO dto) {
		updatePanCardReportUI.setUploadTableValues(dto);
		
	}

}
