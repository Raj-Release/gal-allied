package com.shaic.paclaim.reimbursement.draftquery;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;
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
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
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
public class DecideOnDraftPAQueryDetailWizardViewImpl extends AbstractMVPView implements
DecideOnDraftPAQueryWizard{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


		@Inject
		private Instance<DraftPAQueryLetterDetailView> decideOnQueryViewImpl;
		
		
		@Inject
		private ConfirmDraftPAQueryDetailViewImpl confirmQueryViewImpl;
		
		@Inject
		private Instance<RevisedCarousel> commonCarouselInstance;
		
		@Inject
		private ViewDetails viewDetails;
		
		@Inject
		private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
		
		@Inject
		private Toolbar toolbar;
		
		private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
		
		private VerticalSplitPanel mainPanel;
		
		private IWizardPartialComplete wizard;
		
		private FieldGroup binder;
		
		
		private SearchDraftQueryLetterTableDTO bean;
		
		@PostConstruct
		public void initView() {
			mainPanel = new VerticalSplitPanel();
			mainPanel.setCaption("Draft Query Letter");
			addStyleName("view");
			setSizeFull();			
		}
		
		public void initView(SearchDraftQueryLetterTableDTO claimQueryDto)
		{
			mainPanel.setCaption("Draft Query Letter");
			this.bean = claimQueryDto;
			
			this.wizard = new IWizardPartialComplete();
			
			initBinder();

			
			DraftPAQueryLetterDetailView decideOnQueryView = decideOnQueryViewImpl.get();
			decideOnQueryView.init(this.bean,wizard);
			wizard.addStep(decideOnQueryView);
			
			confirmQueryViewImpl.init(this.bean);
			wizard.addStep(this.confirmQueryViewImpl);
			
			wizard.setStyleName(ValoTheme.PANEL_WELL);
			wizard.setSizeFull();
			wizard.addListener(this);
			
			RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//			intimationDetailsCarousel.init(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto(), this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto(),  "");
			intimationDetailsCarousel.init(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto(), this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto(),  "",claimQueryDto.getDiagnosis());
			mainPanel.setFirstComponent(intimationDetailsCarousel);
			
			VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());
			
			Panel panel1 = new Panel();
			panel1.setContent(wizardLayout1);
//			panel1.setHeight("50px");
			VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
			wizardLayout2.setSpacing(true);
			mainPanel.setSecondComponent(wizardLayout2);
			
			
			mainPanel.setSizeFull();
			mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
			mainPanel.setHeight("700px");
			setCompositionRoot(mainPanel);
			
				
		}
		
		private void initBinder() {
			
			wizard.getFinishButton().setCaption("Submit");
			this.binder = new FieldGroup();
			BeanItem<SearchDraftQueryLetterTableDTO> item = new BeanItem<SearchDraftQueryLetterTableDTO>(bean);
			
			item.addNestedProperty("reimbursementQueryDto.queryRemarks");
			item.addNestedProperty("reimbursementQueryDto.queryLetterRemarks");
			item.addNestedProperty("reimbursementQueryDto.redraftRemarks");
			item.addNestedProperty("reimbursementQueryDto.rejectionRemarks");
					
			binder.setItemDataSource(item);
		}

		private VerticalLayout commonButtonsLayout()
		{
			
			TextField acknowledgementNumber = new TextField("Acknowledgement Number");
			acknowledgementNumber.setValue(String.valueOf(this.bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getAcknowledgeNumber()));
			//Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
			acknowledgementNumber.setWidth("250px");
			acknowledgementNumber.setHeight("20px");
			acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			acknowledgementNumber.setReadOnly(true);
			acknowledgementNumber.setEnabled(false);
			acknowledgementNumber.setNullRepresentation("");
			FormLayout hLayout = new FormLayout (acknowledgementNumber);
			hLayout.setMargin(false);
			
			Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
			viewEarlierRODDetails.addClickListener(new ClickListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
						
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("");
					popup.setWidth("75%");
					popup.setHeight("85%");
					earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
					earlierRodDetailsViewObj.init(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getKey(),null);
					popup.setContent(earlierRodDetailsViewObj);
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
			});
			
//			FormLayout viewEarlierRODLayout = new FormLayout(viewEarlierRODDetails);
//			
//			FormLayout viewDetailsForm = new FormLayout();
//			//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
//			viewDetailsForm.setWidth("-1px");
//			viewDetailsForm.setHeight("-1px");
//			viewDetailsForm.setMargin(false);
//			viewDetailsForm.setSpacing(true);
			//ComboBox viewDetailsSelect = new ComboBox()
			viewDetails.initView(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getIntimationId(),bean.getReimbursementQueryDto().getReimbursementDto().getKey(), ViewLevels.PREAUTH_MEDICAL, false,"");
			
//			viewDetailsForm.addComponents(viewDetails);
//			HorizontalLayout horizontalLayout = new HorizontalLayout(viewEarlierRODLayout,viewDetailsForm);
//			
//			VerticalLayout componentsHLayout = new VerticalLayout(horizontalLayout,hLayout);
//			componentsHLayout.setWidth("100%");
		//	componentsHLayout.setComponentAlignment(viewEarlierRODLayout, Alignment.BOTTOM_RIGHT);
			Label dummylabel =new Label();
		//	dummylabel.setWidth("50");
			HorizontalLayout componentsHLayout = new HorizontalLayout(viewEarlierRODDetails, viewDetails);
			componentsHLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
			componentsHLayout.setWidth("100%");
			VerticalLayout componentsvertical=new VerticalLayout(componentsHLayout,hLayout);
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
			
			if(className.contains("DraftPAQueryLetterDetailViewImpl"))	{
//				decideOnQueryViewImpl.get().setUpdatedBean(this.bean);
				this.bean = confirmQueryViewImpl.getUpdatedBean();
				((DraftPAQueryLetterDetailViewImpl)event.getActivatedStep()).returnPreviousPage(this.bean);
			}
			else{
				this.bean = decideOnQueryViewImpl.get().getUpdatedBean();
//				this.bean = ((ConfirmDraftQueryDetailViewImpl)event.getActivatedStep()).getUpdatedBean();	
			}
		}

		@Override
		public void stepSetChanged(WizardStepSetChangedEvent event) {
			
			decideOnQueryViewImpl.get().getUpdatedBean();
		}

		@Override
		public void wizardCompleted(WizardCompletedEvent event) {
			
			DraftPAQueryLetterDetailView decideOnQueryView = decideOnQueryViewImpl.get();
			SearchDraftQueryLetterTableDTO  beanDto = decideOnQueryView.getUpdatedBean();
//			decideOnQueryView.getUpdatedBean();
//			decideOnQueryView.setUpdatedBean(this.bean);
			
			if(!beanDto.getHasError()){
				fireViewEvent(DecideOnDraftPAQueryWizardPresenter.SUBMIT_DRAFT_PA_QUERY_LETTER, this.bean);	
			}			
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
								fireViewEvent(MenuItemBean.PA_DRAFT_QUERY_LETTER_SEARCH, null);
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
			
			
			Button OkBtn = new Button("Search Draft Query Letter Home");
			OkBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			OkBtn.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
				toolbar.countTool();
				fireViewEvent(MenuItemBean.PA_DRAFT_QUERY_LETTER_SEARCH, null);
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

		@Override
		public void submitQuery(ParameterDTO parameter) {
			System.out.println("Inside submit method of Draft wizard IMPL");
			bean = (SearchDraftQueryLetterTableDTO)parameter.getPrimaryParameter();
			fireViewEvent(DraftPAQueryLetterDetailPresenter.SUBMIT_PA_BUTTON_CLICKED,bean);
			
//			decideOnQueryViewImpl
//			decideOnQueryViewImpl.su			
		}
		
		
		
}
