package com.shaic.claim.cvc.postprocess;

import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.cvc.SearchCVCTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class PostProcessClaimCVCAuditViewImpl extends AbstractMVPView
implements PostProcessClaimCVCAuditView{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;
	
	private RRCDTO rrcDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	@Inject
	private PostProcessClaimCVCPageUI postcvcPageUI;
	
	@Inject
	private Toolbar toolbar;
	
	PreauthDTO preauthDTO = null;
	

	public void init(SearchCVCTableDTO bean) {
	//	rrcDTO = investigationKey.getRrcDTO();
		rrcDTO = new RRCDTO();
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(rrcDTO);
		postcvcPageUI.init(bean);
		mainLayout = new VerticalLayout(postcvcPageUI);
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
		
	}

	@Override
	public void setReferenceForCorosal(ClaimDto claim) {
	//	uploadInvestigationReportUI.setReference(claim);
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
	public void result() {

		Label successLabel = new Label(
				"<b style = 'color: black;'>Post Claim Process Audit record Submitted successfully !!! </b>",
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
				fireViewEvent(MenuItemBean.POST_PROCESS_CVC, true);

			}
		});

	}
		@Override
		public void loadCVCStatusDropDownValues(BeanItemContainer<SelectValue> statusValueContainer,
				BeanItemContainer<SelectValue> ErrorValueContainer,BeanItemContainer<SelectValue> TeamValueContainer,
				BeanItemContainer<SelectValue> monetaryValueContainer,BeanItemContainer<SelectValue> remediationStatusValueContainer,
				BeanItemContainer<SelectValue> processorValueContainer) {
			// TODO Auto-generated method stub
			postcvcPageUI.loadCVCStatusDropDownValues(statusValueContainer,ErrorValueContainer,TeamValueContainer,
					monetaryValueContainer,remediationStatusValueContainer,processorValueContainer)	;
			
		}
}
