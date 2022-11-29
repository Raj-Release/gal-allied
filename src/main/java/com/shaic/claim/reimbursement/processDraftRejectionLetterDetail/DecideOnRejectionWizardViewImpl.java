package com.shaic.claim.reimbursement.processDraftRejectionLetterDetail;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Lakshminarayana
 *
 */
public class DecideOnRejectionWizardViewImpl extends AbstractMVPView implements
DecideOnRejectionWizard{
		
		@Inject
		private Instance<DecideOnRejectionView> decideOnRejectionViewImpl;
		
		@Inject
		private ConfirmRejectionViewImpl confirmRejectionViewImpl;
		
		@Inject
		private Instance<RevisedCarousel> commonCarouselInstance;
		
		@Inject
		private Instance<ViewPEDRequestWindow> viewPedRequest;
		
		private ViewPEDRequestWindow viewPEDRequestObj;
		
		@Inject
		private ViewDetails viewDetails;
		
		@Inject
		private CrmFlaggedComponents crmFlaggedComponents;
		
		@Inject
		private Toolbar toolbar;
		
		private Button initiatePedBtn;
		
		private VerticalSplitPanel mainPanel;
		
		private IWizardPartialComplete wizard;
		
		private FieldGroup binder;
		
		private ClaimRejectionDto bean;
		
		@PostConstruct
		public void initView() {

			mainPanel = new VerticalSplitPanel();
			mainPanel.setCaption("Process Draft Rejection Letter");
			addStyleName("view");
			setSizeFull();			
		}
		
		public void initView(ClaimRejectionDto claimQueryDto)
		{
			this.bean = claimQueryDto;
			
			this.wizard = new IWizardPartialComplete();
			initBinder();
			
			
			DecideOnRejectionView decideOnRejectionView = decideOnRejectionViewImpl.get();
			decideOnRejectionView.init(this.bean,wizard);
			
			wizard.addStep(decideOnRejectionView);
			
			confirmRejectionViewImpl.init(this.bean);
			wizard.addStep(this.confirmRejectionViewImpl);
			
			wizard.setStyleName(ValoTheme.PANEL_WELL);
			wizard.setSizeFull();
			wizard.addListener(this);
			
			RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//			intimationDetailsCarousel.init(this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto(), this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto(),  "");
			intimationDetailsCarousel.init(this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto(), this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto(),  "",this.bean.getDiagnosis());
			mainPanel.setFirstComponent(intimationDetailsCarousel);
			viewDetails.initView(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getIntimationId(),bean.getReimbursementRejectionDto().getReimbursementDto().getKey(), ViewLevels.PREAUTH_MEDICAL, false,"Process Draft Rejection Letter");
			HorizontalLayout wizardLayout1 = new HorizontalLayout(commonButtonsLayout(),viewDetails);
			wizardLayout1.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
			wizardLayout1.setWidth("100%");
			Panel panel1 = new Panel();
			panel1.setContent(wizardLayout1);
			panel1.setWidth("100%");
//			panel1.setHeight("50px");
			VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
			wizardLayout2.setSpacing(false);
			mainPanel.setSecondComponent(wizardLayout2);
			
			
			mainPanel.setSizeFull();
			mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
			mainPanel.setHeight("700px");
			setCompositionRoot(mainPanel);
			
				
		}
		
		private void initBinder() {
			
			wizard.getFinishButton().setCaption("Submit");
			this.binder = new FieldGroup();
			BeanItem<ClaimRejectionDto> item = new BeanItem<ClaimRejectionDto>(bean);
			
			item.addNestedProperty("reimbursementRejectionDto.rejectionRemarks");
			item.addNestedProperty("reimbursementRejectionDto.rejectionLetterRemarks");
			item.addNestedProperty("reimbursementRejectionDto.redraftRemarks");
			item.addNestedProperty("reimbursementRejectionDto.disapprovedRemarks");
					
			binder.setItemDataSource(item);
		}

		private VerticalLayout commonButtonsLayout()
		{
			/*FormLayout viewDetailsForm = new FormLayout();
			//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
			viewDetailsForm.setWidth("-1px");
			viewDetailsForm.setHeight("-1px");
			viewDetailsForm.setMargin(false);
			viewDetailsForm.setSpacing(true);
			viewDetailsForm.addComponent(viewDetails);*/
			
			initiatePedBtn = new Button("Initiate PED Endorsement");
			
			if(null != bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto() 
					&& null != bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy() 
					&& null != bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProductType()
					&& null != bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProductType().getKey()
					&& 2904 == bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getProductType().getKey().intValue()){
				initiatePedBtn.setEnabled(false);
			}
			else{
				initiatePedBtn.setEnabled(true);
			}
			
			initiatePedBtn.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
//					if(bean.getIsPEDInitiatedForBtn()) {
//						alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
//					} else {
					PreauthDTO preauthDTO = new PreauthDTO();
					ClaimDto claimDto = bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto();
					NewIntimationDto newIntimationDto = bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto();
					preauthDTO.setClaimDTO(claimDto);
					preauthDTO.setNewIntimationDTO(newIntimationDto);
					
					Long reimbursementKey = bean.getReimbursementRejectionDto().getReimbursementDto().getKey();
					Long stageKey = bean.getReimbursementRejectionDto().getReimbursementDto().getStageSelectValue().getId();
					
					viewPEDRequestObj = viewPedRequest.get();
					viewPEDRequestObj.initView(preauthDTO, reimbursementKey,newIntimationDto.getKey(), newIntimationDto.getPolicy().getKey(), claimDto.getKey(),stageKey,false);
					viewPEDRequestObj.setPresenterString(SHAConstants.REIMBURSEMENT_SCREEN);
					showPopup(new VerticalLayout(viewPEDRequestObj));
//					}
					
//					showPopup(new VerticalLayout(viewPEDRequestObj));
				}
				
			});
			
			HorizontalLayout formLayout = SHAUtils.newImageCRM(bean.getPreAuthDto());
			HorizontalLayout crmLayout = new HorizontalLayout(formLayout);
			crmLayout.setSpacing(true);
//			crmLayout.setWidth("40%");
			
			HorizontalLayout componentsHLayout = new HorizontalLayout(crmLayout,initiatePedBtn);
			componentsHLayout.setSpacing(true);
			
//			HorizontalLayout crmFlaggedLayout = SHAUtils.crmFlaggedLayout(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
			crmFlaggedComponents.init(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
			
			VerticalLayout componentsvertical=new VerticalLayout(componentsHLayout,crmFlaggedComponents);
			return componentsvertical;
		}
		
		
		@Override
		public void resetView() {
			
		}

		@Override
		public void wizardSave(GWizardEvent event) {
			
		}

		@Override
		public void activeStepChanged(WizardStepActivationEvent event) {
			
			String className = event.getActivatedStep().getClass().toString();
			if(className.contains("DecideOnRejectionViewImpl"))	{
				((DecideOnRejectionViewImpl)event.getActivatedStep()).returnPreviousPage();
			}
			
		}

		@Override
		public void stepSetChanged(WizardStepSetChangedEvent event) {
			
		}

		@Override
		public void wizardCompleted(WizardCompletedEvent event) {
		    
			fireViewEvent(DecideOnRejectionWizardPresenter.SUBMIT_PROCESS_DRAFT_REJECTION, this.bean);
		}

		@Override
		public void wizardCancelled(WizardCancelledEvent event) {
			
			ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
					"Are you sure you want to cancel ?",
					"No", "Yes", new ConfirmDialog.Listener() {

						private static final long serialVersionUID = 1L;

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isCanceled() && !dialog.isConfirmed()) {
								
								    releaseHumanTask();
								    bean = null;
									fireViewEvent(MenuItemBean.PROCESS_DRAFT_REJECTION_LETTER, null);
									clearObject();
							}
						}
			});

			dialog.setStyleName(Reindeer.WINDOW_BLACK);
			
		}		

		@Override
		public void buildSuccessLayout() {
			
			VerticalLayout layout = new VerticalLayout();

			Label msg = new Label("<b style = 'color: green;'>Claim record saved successfully !!!.</b>", ContentMode.HTML);
			layout.addComponent(msg);
			layout.setMargin(true);
			layout.setSpacing(true);
						
			Button OkBtn = new Button("Process Draft Rejection Letter Home");
			OkBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			OkBtn.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
				
					if(bean.getReimbursementRejectionDto() != null){
						bean.setReimbursementRejectionDto(null);
					}
					
					bean = null;	
				toolbar.countTool();
				fireViewEvent(MenuItemBean.PROCESS_DRAFT_REJECTION_LETTER, null);
				ConfirmDialog dialog = (ConfirmDialog)event.getButton().getParent().getParent().getParent();
				dialog.close();
				clearObject();
				}
			});
			
			layout.addComponent(OkBtn);
			layout.setComponentAlignment(OkBtn, Alignment.MIDDLE_CENTER);
			
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");
			
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setSizeUndefined();
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
		}
		
		private void showPopup(Layout layout) {
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("");
			popup.setWidth("75%");
			popup.setHeight("85%");
			popup.setContent(layout);
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

		@Override
		public void initData(WizardInitEvent event) {
			
		}		
		
		 private void releaseHumanTask(){
				
				Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
		     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
		 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
		 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

		 		if(existingTaskNumber != null){
		 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
		 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		 		}
		 		
		 		if(wrkFlowKey != null){
		 			DBCalculationService dbService = new DBCalculationService();
		 			dbService.callUnlockProcedure(wrkFlowKey);
		 			getSession().setAttribute(SHAConstants.WK_KEY, null);
		 		}
			}
		 
		 public void clearObject(){
			 DecideOnRejectionView decideOnRejectionView = decideOnRejectionViewImpl.get();
			 decideOnRejectionView.clearObject();
			 confirmRejectionViewImpl.clearObject();
		 }
}
