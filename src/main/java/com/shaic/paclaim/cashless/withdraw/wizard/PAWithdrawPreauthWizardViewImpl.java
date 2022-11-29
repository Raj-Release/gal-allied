package com.shaic.paclaim.cashless.withdraw.wizard;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.GWizardListener;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.ims.carousel.PARevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.cashless.withdraw.search.PASearchWithdrawCashLessProcessTableDTO;
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
import com.vaadin.v7.ui.themes.Reindeer;

public class PAWithdrawPreauthWizardViewImpl extends AbstractMVPView implements PAWithdrawPreauthWizard,GWizardListener {
	
	
	@Inject
	private Instance<PAWithdrawPreauthPage> withdrawPreauthPageInstance;
	
	@Inject
	private Instance<PAWithdrawPreauthPdfPage> withdrawPreauthPdfInstance;
	
	@Inject
	private Instance<PARevisedCarousel> commonCarouselInstance;
	
	private PAWithdrawPreauthPage withdrawPreauthPageObj;
	
	@Inject
	private ViewDetails viewDetails;
	
	private PAWithdrawPreauthPageDTO bean;
	
	private VerticalLayout mainPanel;
	
	private IWizardPartialComplete wizard;
	

	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	@Inject
	private Toolbar toolbar;
	
	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();		
	}
	
	
	public void initView(PAWithdrawPreauthPageDTO bean,PASearchWithdrawCashLessProcessTableDTO tableDto,NewIntimationDto intimationDto,ClaimDto claimDto,BeanItemContainer<SelectValue> selectValueContainer)
	{
		
		this.wizard=new IWizardPartialComplete();
		wizard.getFinishButton().setCaption("Submit");
		this.bean=bean;
		this.bean.setSelectValueContainer(selectValueContainer);
		
		this.bean.setNewIntimationDto(intimationDto);
		this.bean.setClaimDto(claimDto);
		this.bean.setTableDto(tableDto);
		this.bean.setPreauthDto(tableDto.getPreauthDto());
		this.bean.setPreviousPreAuthTableDTO(tableDto.getPreviousPreAuthTableDTO());
		this.bean.setPreauth(tableDto.getPreauth());
		
		viewDetails.initView(intimationDto.getIntimationId(), ViewLevels.PA_PROCESS, false,"Withdraw Pre-auth");
		
		mainPanel = new VerticalLayout();
//		mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		mainPanel.setHeight("100%");
		mainPanel.setWidth("100%");
		PARevisedCarousel preauthIntimation=commonCarouselInstance.get();
		PreauthDTO preauthDto = bean.getPreauthDto();
		preauthDto.setNewIntimationDTO(intimationDto);
		preauthDto.setClaimDTO(claimDto);
		preauthIntimation.init(preauthDto,"Withdraw Pre-auth");
		
		PAWithdrawPreauthPage withdrawPreauthPageObj=withdrawPreauthPageInstance.get();
		withdrawPreauthPageObj.init(this.bean);
		this.withdrawPreauthPageObj=withdrawPreauthPageObj;
		
		PAWithdrawPreauthPdfPage withdrawPreauthPdfObj=withdrawPreauthPdfInstance.get();
		withdrawPreauthPdfObj.init(this.bean);
		
		
		wizard.addStep(withdrawPreauthPageObj,"Withdraw Pre-auth");
		wizard.addStep(withdrawPreauthPdfObj,"Decision Communication");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
		VerticalLayout wizardLayout2 = new VerticalLayout(wizard);
		mainPanel.addComponent(preauthIntimation);
		Label dummyLabel =new Label();
		dummyLabel.setWidth("300px");
		HorizontalLayout commonButtonsLayout = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		commonButtonsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		commonButtonsLayout.setWidth("100%");
		//mainPanel.addComponent(viewDetails);
		mainPanel.addComponent(commonButtonsLayout);
		mainPanel.setComponentAlignment(commonButtonsLayout, Alignment.TOP_LEFT);
		mainPanel.addComponent(wizardLayout2);
		//mainPanel.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
		setCompositionRoot(mainPanel);
	}
	
	public HorizontalLayout commonButtonsLayout()
	{
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(
				btnRRC);
//		alignmentHLayout.setComponentAlignment(vLayout, Alignment.TOP_LEFT);
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		
		fireViewEvent(PAWithdrawPreauthWizardPresenter.VALIDATE_WITHDRAW_PREAUTH_USER_RRC_REQUEST, bean.getPreauthDto());//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PA_PROCESS_WITHDRAW_PREAUTH);
			rewardRecognitionRequestViewObj.init(bean.getPreauthDto(), popup);
			
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
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
		
		fireViewEvent(PAWithdrawPreauthWizardPresenter.WITHDRAW_SUBMITTED_EVENT, this.bean);
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "Cancel", "Ok", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                    // Confirmed to continue
		                	fireViewEvent(MenuItemBean.PA_STANDALONE_WITHDRAW, null);
		                } else {
		                    // User did not confirm
		                }
		            }
		        });
		
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardSave(GWizardEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label("<b style = 'color: green;'>Pre-auth has been withdrawn successfully !!! </b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Withdraw Home");
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
				toolbar.countTool();
				fireViewEvent(MenuItemBean.WITHDRAW_PRE_AUTH, null);
				
			}
		});
	}


	@Override
	public void buildWithdrawFields(
			BeanItemContainer<SelectValue> selectValueContainer) {
		if(this.withdrawPreauthPageObj != null) {
			this.withdrawPreauthPageObj.buildWithdrawLayout(selectValueContainer);
		}
	}


	@Override
	public void buildWithdrawAndRejctFields(
			BeanItemContainer<SelectValue> withdrawContainer,
			BeanItemContainer<SelectValue> rejectionContainer) {
		if(this.withdrawPreauthPageObj != null) {
			this.withdrawPreauthPageObj.buildWithdrawAndRejectLayout(withdrawContainer, rejectionContainer);
		}
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
