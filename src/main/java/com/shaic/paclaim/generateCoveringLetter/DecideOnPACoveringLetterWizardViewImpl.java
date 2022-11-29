package com.shaic.paclaim.generateCoveringLetter;

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

import com.shaic.arch.view.LoaderPresenter;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.ims.carousel.PARevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Lakshminarayana
 *
 */
public class DecideOnPACoveringLetterWizardViewImpl extends AbstractMVPView implements
DecideOnPACoveringLetterWizard{
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


		@Inject
		private Instance<GenerateCoveringLetterPAViewImpl> decideOnPACoveringLetterViewImpl;
		
		GenerateLetterPAClaimView decideOnPACoveringLetterView;		
		
		@Inject
		private ConfirmPACoveringLetterViewImpl confirmPACoveringLetterViewImpl;
		
		@Inject
		private Instance<PARevisedCarousel> commonCarouselInstance;
		
		@Inject
		private ViewDetails viewDetails;
		
		@Inject
		private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
		
		private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
		
		private VerticalSplitPanel mainPanel;
		
		private IWizardPartialComplete wizard;
		
		private FieldGroup binder;
		
		private GenerateCoveringLetterSearchTableDto bean;
		
		@PostConstruct
		public void initView() {
			mainPanel = new VerticalSplitPanel();
			mainPanel.setCaption("Generate Covering Letter");
			addStyleName("view");
			setSizeFull();			
		}
		
		public void initView(GenerateCoveringLetterSearchTableDto paclaimCoveringLetterDto)
		{
			mainPanel.setCaption("Generate Covering Letter");
			this.bean = paclaimCoveringLetterDto;
			
			this.wizard = new IWizardPartialComplete();
			wizard.getFinishButton().setCaption("Submit");
//			initBinder();
			
			decideOnPACoveringLetterView = decideOnPACoveringLetterViewImpl.get();
			decideOnPACoveringLetterView.init(this.bean,wizard);
			wizard.addStep(decideOnPACoveringLetterView);
			
//			confirmPACoveringLetterViewImpl.init(this.bean);
			confirmPACoveringLetterViewImpl.init(this.bean);
			wizard.addStep(this.confirmPACoveringLetterViewImpl);
			
			wizard.setStyleName(ValoTheme.PANEL_WELL);
			wizard.setSizeFull();
			wizard.addListener(this);
			
			PARevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//			intimationDetailsCarousel.init(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto(), this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto(),  "");
			intimationDetailsCarousel.init(this.bean.getClaimDto().getNewIntimationDto(), "Claim Details");
			mainPanel.setFirstComponent(intimationDetailsCarousel);

			viewDetails.initView(this.bean.getClaimDto().getNewIntimationDto().getIntimationId(), ViewLevels.PREAUTH_MEDICAL, false,"Generate covering letter");
			VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());

			
			VerticalLayout viewDetailsLayout = new VerticalLayout(commonButtonsLayout());

			
//			Panel panel1 = new Panel();
//			panel1.setContent(wizardLayout1);
//			panel1.setHeight("50px");
//			VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
			VerticalLayout wizardLayout2 = new VerticalLayout(viewDetailsLayout, wizard);
			wizardLayout2.setSpacing(true);
			mainPanel.setSecondComponent(wizardLayout2);
			
			
			mainPanel.setSizeFull();
			mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
			mainPanel.setHeight("700px");
			setCompositionRoot(mainPanel);
			setSizeFull();	
				
		}
		
//		private void initBinder() {
//			
//			wizard.getFinishButton().setCaption("Submit");
//			this.binder = new FieldGroup();
//			BeanItem<ClaimQueryDto> item = new BeanItem<ClaimQueryDto>(bean);
//			
//			item.addNestedProperty("reimbursementQueryDto.queryRemarks");
//			item.addNestedProperty("reimbursementQueryDto.queryLetterRemarks");
//			item.addNestedProperty("reimbursementQueryDto.redraftRemarks");
//			item.addNestedProperty("reimbursementQueryDto.rejectionRemarks");
//					
//			binder.setItemDataSource(item);
//		}

		private HorizontalLayout commonButtonsLayout()
//		private VerticalLayout commonButtonsLayout()
		{
			
			Label dummylabel =new Label();
			HorizontalLayout componentsHLayout = new HorizontalLayout(dummylabel, viewDetails);
			componentsHLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
			componentsHLayout.setWidth("100%");
//			VerticalLayout componentsvertical=new VerticalLayout(componentsHLayout);
			return componentsHLayout;
		}
		
		
		@Override
		public void resetView() {
			
		}

		@Override
		public void wizardSave(GWizardEvent event) {

		}

		@Override
		public void activeStepChanged(WizardStepActivationEvent event) {			
			decideOnPACoveringLetterView.getUpdatedBean();	
		}

		@Override
		public void stepSetChanged(WizardStepSetChangedEvent event) {
			
			decideOnPACoveringLetterView.getUpdatedBean();
		}

		@Override
		public void wizardCompleted(WizardCompletedEvent event) {
			
//			DecideOnQueryView decideOnQueryView = decideOnQueryViewImpl.get();
			
//			decideOnQueryView.getUpdatedBean();
//			decideOnQueryView.setUpdatedBean(this.bean);
			
			fireViewEvent(DecideOnPACoveringLetterWizardPresenter.SUBMIT_PA_COVERING_LETTER, this.bean);
			
		}

		@Override
		public void wizardCancelled(WizardCancelledEvent event) {
			
			ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
					"Are you sure you want to cancel ?",
					"No", "Yes", new ConfirmDialog.Listener() {

						private static final long serialVersionUID = 1L;

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isCanceled() && !dialog.isConfirmed()) {
								
								wizard.releaseHumanTask();
								
								fireViewEvent(MenuItemBean.SEARCH_GENERATE_PA_COVERING_LETTER,null);
							}
						}
			});

			dialog.setStyleName(Reindeer.WINDOW_BLACK);
			
		}

		@Override
		public void initData(WizardInitEvent event) {
			
		}

		@Override
		public void buildSuccessLayout() {
			
			VerticalLayout layout = new VerticalLayout();

			Label msg = new Label("<b style = 'color: green;'>Claim record saved successfully !!!.</b>", ContentMode.HTML);
			layout.addComponent(msg);
			layout.setMargin(true);
			layout.setSpacing(true);
			
			
			Button OkBtn = new Button("Generate Covering Letter Home");
			OkBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			OkBtn.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					
				fireViewEvent(LoaderPresenter.LOAD_URL,MenuItemBean.SEARCH_GENERATE_PA_COVERING_LETTER);
				ConfirmDialog dialog = (ConfirmDialog)event.getButton().getParent().getParent().getParent();
				dialog.close();
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
		
		
		
}
