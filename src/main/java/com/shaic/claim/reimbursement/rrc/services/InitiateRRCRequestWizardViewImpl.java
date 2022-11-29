package com.shaic.claim.reimbursement.rrc.services;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploadDocumentsPageView;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.newcode.wizard.IWizard;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class InitiateRRCRequestWizardViewImpl extends AbstractMVPView implements
InitiateRRCRequestWizardView {

	
	@Inject
	private Instance<SearchUploadDocumentsPageView> searchUploadDocumentsPageObj;
	private SearchUploadDocumentsPageView searchUploadDocumentsPage;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	@Inject
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private VerticalSplitPanel mainPanel;
	//private Panel mainPanel;
	
	private IWizard wizard;
	
	private FieldGroup binder;
	
	@Inject
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
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		
		
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),"Initiate RRC",rodDTO.getDiagnosis());
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(),0l, ViewLevels.PREAUTH_MEDICAL,"Initiate RRC");
		HorizontalLayout hLayout = new HorizontalLayout(viewDetails);
		hLayout.setWidth("100%");
		hLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		VerticalLayout wizardLayout1 = new VerticalLayout(hLayout);
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		//panel1.setHeight("90px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		wizardLayout2.setSpacing(true);
		
		
		
		PreauthDTO	preauthDto = new PreauthDTO();
	 	preauthDto.setClaimDTO(bean.getRrcDTO().getClaimDto());
	 	preauthDto.setRrcDTO(bean.getRrcDTO());
		preauthDto.setNewIntimationDTO(bean.getRrcDTO().getNewIntimationDTO());
	 	rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance.get();
	 
	 	rewardRecognitionRequestViewObj.initPresenter(SHAConstants.INITIATE_RRC_REQUEST);
	 	rewardRecognitionRequestViewObj.init(preauthDto, popup);
	 	 
	 	VerticalLayout viewLayout = new VerticalLayout();
	 	viewLayout.addComponents(hLayout,rewardRecognitionRequestViewObj);
		mainPanel.setSecondComponent(viewLayout);		
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(30, Unit.PERCENTAGE);
		mainPanel.setHeight("600px");
		
		setCompositionRoot(mainPanel);		
			
				
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;
		
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
