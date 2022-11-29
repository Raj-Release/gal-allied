package com.shaic.claim.rod.wizard.cancelAcknowledgement;

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

import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.IWizardPartialComplete;
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
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class CancelAcknowledgementWizardViewImpl extends AbstractMVPView implements CancelAcknowledgementWizardView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instance<CancelDocumentWizardView> cancelAcknowledgmentInstance;
	
	@Inject
	private Instance<CancelAcknowledgementLetterViewImpl> cancelAcknowledgementLetter;
	
	private static final String DOCUMENT_DETAILS = "Document Details";
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
    ////private static Window popup;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	private FieldGroup binder;
	
	private ReceiptOfDocumentsDTO bean;
	
	private IWizardPartialComplete wizard;
	
	private VerticalSplitPanel mainPanel;
	
	@Inject
	private ViewDetails viewDetails;
	
	private void initBinder() {
		
		wizard.getFinishButton().setCaption("Submit");
		
		this.binder = new FieldGroup();
		BeanItem<ReceiptOfDocumentsDTO> item = new BeanItem<ReceiptOfDocumentsDTO>(bean);
		
		this.binder.setItemDataSource(item);	
		
	}
	
	
	@Override
	public void initView(ReceiptOfDocumentsDTO rodDTO){
		
		this.bean = rodDTO;
		this.wizard = new IWizardPartialComplete();
		
		initBinder();
		
		CancelDocumentWizardView cancelAcknowledgementObj = cancelAcknowledgmentInstance.get();
		cancelAcknowledgementObj.init(this.bean,wizard);
		
		wizard.addStep(cancelAcknowledgementObj,DOCUMENT_DETAILS);
		
		CancelAcknowledgementLetterViewImpl cancelAcknowledgementLetterObj = cancelAcknowledgementLetter.get();
		cancelAcknowledgementLetterObj.init(this.bean,wizard);
		
		wizard.addStep(cancelAcknowledgementLetterObj, "confirmation");
		wizard.addListener(this);
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Cancel Acknowledgement",rodDTO.getDiagnosis());
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
//       VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());
		
		VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());    //add commonButtonsLayout
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		//panel1.setHeight("50px");
		//panel1.setHeight("100px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);
		
		
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
		
		
	}
	
	private HorizontalLayout commonButtonsLayout()
	{
		
		FormLayout hLayout = new FormLayout ();
		
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
				ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
				documentDetails.setClaimDto(bean.getClaimDTO());
				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),null);
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
		
		//FormLayout viewEarlierRODLayout = new FormLayout(viewEarlierRODDetails);
		
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		//ComboBox viewDetailsSelect = new ComboBox()
		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(), 0l,ViewLevels.PREAUTH_MEDICAL,"Cancel Acknowledgement");
		viewDetailsForm.addComponent(viewDetails);
	
		HorizontalLayout componentsHLayout = new HorizontalLayout(viewEarlierRODDetails, viewDetailsForm);
		//HorizontalLayout componentsHLayout = new HorizontalLayout(hLayout, viewEarlierRODLayout);
		componentsHLayout.setComponentAlignment(viewDetailsForm,  Alignment.TOP_RIGHT);
		//componentsHLayout.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		componentsHLayout.setSpacing(true);
		componentsHLayout.setWidth("100%");
		
		//rrcBtnLayout.setComponentAlignment(btnRRC, Alignment.TOP_LEFT);
		
		VerticalLayout vLayout = new VerticalLayout(componentsHLayout);
		//vLayout.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		
		vLayout.setWidth("100%");

		HorizontalLayout alignmentHLayout = new HorizontalLayout(vLayout);
		alignmentHLayout.setWidth("100%");
	
		return alignmentHLayout;
	}
	
	@PostConstruct
	public void initView() {
		/*arrScreenName = new String[] {
					"documentDetails","acknowledgementReceipt"};*/
		mainPanel = new VerticalSplitPanel();
		//captionMap = new HashMap<String, String>();
		
		addStyleName("view");
		setSizeFull();			
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardSave(GWizardEvent event) {
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
		
		fireViewEvent(CancelAcknowledgementWizardPresenter.CANCEL_ACKNOWLEDGEMENT, this.bean);
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "No", "Yes", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                	fireViewEvent(MenuItemBean.CANCEL_ACKNOWLEDGEMENT, null);
		                    // Confirmed to continue
		                	//fireViewEvent(MenuItemBean.PROCESS_PRE_MEDICAL, null);
		                	//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
		                } else {
		                	//fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, null);
		                }
		            }
		        });
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label("<b style = 'color: green;'>Acknowledgement Cancelled Sucessfully!!!!!!</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		
		Button homeButton = new Button("Home");
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
				fireViewEvent(MenuItemBean.CANCEL_ACKNOWLEDGEMENT, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});

	}

}
