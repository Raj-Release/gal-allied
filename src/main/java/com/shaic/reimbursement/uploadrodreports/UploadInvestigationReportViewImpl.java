package com.shaic.reimbursement.uploadrodreports;

import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationTableDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class UploadInvestigationReportViewImpl extends AbstractMVPView
		implements UploadInvestigationReportView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;

	@Inject
	private UploadInvestigationReportUI uploadInvestigationReportUI;

	private List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList;
	
	
	private RRCDTO rrcDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	@Inject
	private Toolbar toolbar;
	
	////private static Window popup;
	PreauthDTO preauthDTO = null;
	

	public void init(SearchUploadInvesticationTableDTO investigationKey) {
		rrcDTO = investigationKey.getRrcDTO();
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(rrcDTO);
	//	preauthDTO.setRodHumanTask(investigationKey.getHumanTaskTableDTO());
		//uploadInvestigationReportUI.init(investigationKey);
		uploadInvestigationReportUI.init(investigationKey,preauthDTO);
		mainLayout = new VerticalLayout(uploadInvestigationReportUI);
//		mainLayout.setSizeFull();
		setCompositionRoot(mainLayout);
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReference(
			List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList,
			Long Ackcount) {
		this.investigationDetailsReimbursementTableDTOList = investigationDetailsReimbursementTableDTOList;
		uploadInvestigationReportUI.setAcknowledgmentNumber(Ackcount);
	}

	@Override
	public void setUploadInvestigationReportUI(
			List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList) {
		uploadInvestigationReportUI
				.setCompleteLayout(investigationDetailsReimbursementTableDTOList);
	}

	@Override
	public void setReferenceForCorosal(ClaimDto claim) {
		uploadInvestigationReportUI.setReference(claim);
	}

//	@Override
//	public void updateTableValues(Long investigationKey) {
//
//		uploadInvestigationReportUI.setUploadTableValues(investigationKey);
//
//	}
	
	@Override
	public void updateTableValues(List<UploadedDocumentsDTO> uploadedDocList) {

		uploadInvestigationReportUI.setUploadTableValues(uploadedDocList);

	}
	
	@Override
	public List<UploadedDocumentsDTO> getUploadedDocDtoList(){
		return uploadInvestigationReportUI.getUploadedTableValues();
	}
	
	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		
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
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}
		}
	
	@Override
	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;
		
	}
	
 
	@Override
	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}

	@Override
	public void showSaveResult(){
		
		Label successLabel = new Label(
				"<b style = 'color: black;'>Investigation report Saved successfully !!! </b>",
				ContentMode.HTML);

		Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, okButton);
		layout.setComponentAlignment(okButton, Alignment.BOTTOM_CENTER);
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

		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		
		
	}
	

	@Override
	public void result() {

		Label successLabel = new Label(
				"<b style = 'color: black;'>Investigation report Submitted successfully !!! </b>",
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
				toolbar.countTool();
				fireViewEvent(MenuItemBean.UPLOAD_INVESTIGATION_REPORT, true);

			}
		});

	}
	@Override
	public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		 rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
	 }
	 
	 @Override
	 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		 rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
	 }

}
