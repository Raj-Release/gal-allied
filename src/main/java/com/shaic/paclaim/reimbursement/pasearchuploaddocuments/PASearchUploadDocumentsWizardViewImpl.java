package com.shaic.paclaim.reimbursement.pasearchuploaddocuments;

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
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.IWizard;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class PASearchUploadDocumentsWizardViewImpl extends AbstractMVPView implements PASearchUploadDocumentsWizardView{

	
	@Inject
	private Instance<PASearchUploadDocumentsPageView> searchUploadDocumentsPageObj;
	private PASearchUploadDocumentsPageView searchUploadDocumentsPage;
	
	
	private VerticalSplitPanel mainPanel;
	//private Panel mainPanel;
	
	private IWizard wizard;
	
	private FieldGroup binder;
	
	private ReceiptOfDocumentsDTO bean;

	//private  UploadDocumentDTO bean;
	
	private Window popup;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	
	
	private void initBinder() {
		
		wizard.getFinishButton().setCaption("Submit");
		this.binder = new FieldGroup();
		//BeanItem<UploadDocumentDTO> item = new BeanItem<UploadDocumentDTO>(bean);
		BeanItem<ReceiptOfDocumentsDTO> item = new BeanItem<ReceiptOfDocumentsDTO>(bean);
	//	item.addNestedProperty("rrcDTO");
		/*item.addNestedProperty("rrcDTO.employeeName");
		item.addNestedProperty("rrcDTO.employeeId");
		item.addNestedProperty("rrcDTO.employeeZone");
		item.addNestedProperty("rrcDTO.employeeDept");
		item.addNestedProperty("rrcDTO.policyNo");
		item.addNestedProperty("rrcDTO.intimationNo");
		item.addNestedProperty("rrcDTO.productName");
		item.addNestedProperty("rrcDTO.duration");
		item.addNestedProperty("rrcDTO.sumInsured");
		item.addNestedProperty("rrcDTO.hospitalName");
		item.addNestedProperty("rrcDTO.hospitalCity");
		item.addNestedProperty("rrcDTO.hospitalZone");
		item.addNestedProperty("rrcDTO.dateOfAdmission");
		item.addNestedProperty("rrcDTO.dateOfDischarge");
		item.addNestedProperty("rrcDTO.insuredName");
		item.addNestedProperty("rrcDTO.insuredAge");
		item.addNestedProperty("rrcDTO.sex");
		item.addNestedProperty("rrcDTO.claimType");
		item.addNestedProperty("rrcDTO.processingStage");
		item.addNestedProperty("rrcDTO.significantClinicalInformation");
		item.addNestedProperty("rrcDTO.significantClinicalInformationValue");
		
		item.addNestedProperty("rrcDTO.statusKey");
		item.addNestedProperty("rrcDTO.stageKey");
		item.addNestedProperty("rrcDTO.requestorStageKey");
		item.addNestedProperty("rrcDTO.rrcRequestType");
		item.addNestedProperty("rrcDTO.eligibility");
		item.addNestedProperty("rrcDTO.savedAmount");
		item.addNestedProperty("rrcDTO.eligibilityRemarks");
		item.addNestedProperty("rrcDTO.requestOnHoldRemarks");*/
		

		

/*		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");*/
		this.binder.setItemDataSource(item);	
	}
	
	public void initView(ReceiptOfDocumentsDTO rodDTO)
	{
		this.bean = rodDTO;
		//mainPanel = new Panel();
		mainPanel = new VerticalSplitPanel();
		//this.wizard = iWizard.get();
		this.wizard = new IWizard();
		initBinder();	
		searchUploadDocumentsPage = searchUploadDocumentsPageObj.get();
		searchUploadDocumentsPage.init(bean);
		wizard.addStep(this.searchUploadDocumentsPage,"Upload Documents");
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);	
		
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Acknowledge Receipt of Documents",rodDTO.getDiagnosis());
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(),0l, ViewLevels.PA_PROCESS,"Acknowledge Receipt of Documents");
		HorizontalLayout hLayout = new HorizontalLayout(viewDetails);
		hLayout.setWidth("100%");
		hLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		VerticalLayout wizardLayout1 = new VerticalLayout(hLayout);
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		//panel1.setHeight("90px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);
		
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(30, Unit.PERCENTAGE);
		mainPanel.setHeight("600px");
		
		setCompositionRoot(mainPanel);		
		
		
		/*PreauthIntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Acknowledge Receipt of Documents");
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
	//	VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());
		
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		panel1.setHeight("50px");
		//VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		VerticalLayout commonBtnLayout = commonButtonsLayout();
		VerticalLayout wizardLayout2 = new VerticalLayout(commonBtnLayout, wizard);
		wizardLayout2.setComponentAlignment(commonBtnLayout, Alignment.MIDDLE_RIGHT);
		wizardLayout2.setSpacing(true);
		mainPanel.setContent(wizardLayout2);
		
		
		mainPanel.setSizeFull();
		//mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");*/
		
		//setCompositionRoot(wizard);			
		}
	 @SuppressWarnings("static-access")
		public void buildSuccessLayout() {
			Label successLabel = new Label(
					"<b style = 'color: green;'> Upload Of Documents Successfully !!!</b>",
					ContentMode.HTML);

			// Label noteLabel = new
			// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
			// ContentMode.HTML);

			Button homeButton = new Button("OK");
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
					fireViewEvent(MenuItemBean.SEARCH_OR_UPLOAD_DOCUMENTS, null);

				}
			});
		}
	
	 @SuppressWarnings("static-access")
		public void validate() {
		 
		 
	 
	 }
	
	@PostConstruct
	public void initView() {
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
		if(searchUploadDocumentsPage.onAdvance())
		{
			fireViewEvent(PASearchUploadDocumentsWizardPresenter.SUBMIT_SEARCH_OR_UPLOAD_DOCUMENTS, bean);
		}
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		

		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "No", "yes", new ConfirmDialog.Listener() {
			
		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                    
		                	fireViewEvent(MenuItemBean.PA_SEARCH_OR_UPLOAD_DOCUMENTS, null);
		                } else {
		                    // User did not confirm
		                }
		            }
		        });
		
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void buildSuccessLayout(String rrcRequestNo) {
		/*String successMessage = "";
		if(null != rrcRequestNo)
		{
			 successMessage = "RRC RequestNo" + " " + rrcRequestNo + " Successfully submitted processing !!!";
		}
		else {
			 successMessage = "Failure occured while submitting RRC Request.Please contact administrator.!!!";
		}*/
		Label successLabel = new Label(
				"<b style = 'color: green;'>" + rrcRequestNo + "</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
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
				fireViewEvent(MenuItemBean.PROCESS_RRC_REQUEST, null);
			
			}
		});
		
		
	}
}
