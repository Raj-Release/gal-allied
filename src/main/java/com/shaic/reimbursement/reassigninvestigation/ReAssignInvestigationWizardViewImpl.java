package com.shaic.reimbursement.reassigninvestigation;

import java.util.List;

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
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.IWizard;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
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

public class ReAssignInvestigationWizardViewImpl extends AbstractMVPView
		implements ReAssignInvestigationWizard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;

	@Inject
	private ViewDetails viewDetails;

	private VerticalSplitPanel mainPanel;

	@Inject
	private IWizard wizard;

	@Inject
	private AssignInvestigatorDto assignInvestigatorDto;

	@Inject
	private Instance<ReAssignInvestigatorView> assignInvestigatorViewImpl;

	private ReAssignInvestigatorView assignInvestigatorView;
	
//	@Inject
//	private ConfirmationViewImpl confirmationViewImpl;

	private FieldGroup binder;
	
	private RRCDTO rrcDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	////private static Window popup;
	private PreauthDTO preauthDTO = null;

	private void initBinder() {
		wizard.getFinishButton().setCaption("Submit");
		this.binder = new FieldGroup();
		BeanItem<AssignInvestigatorDto> item = new BeanItem<AssignInvestigatorDto>(
				assignInvestigatorDto);
		this.binder.setItemDataSource(item);
	}

	@PostConstruct
	public void initView() {
		/*
		 * arrScreenName = new String[] {
		 * "documentDetails","acknowledgementReceipt"};
		 */
		mainPanel = new VerticalSplitPanel();
		// captionMap = new HashMap<String, String>();
		this.wizard = new IWizard();
		addStyleName("view");
		setSizeFull();
	}

	public void initView(AssignInvestigatorDto assignInvestigatorDto) {
		try{
			this.wizard = new IWizard();
			this.assignInvestigatorDto = assignInvestigatorDto;
			initBinder();
			this.preauthDTO = new PreauthDTO();
			this.preauthDTO.setRrcDTO(assignInvestigatorDto.getRrcDTO());
		//	this.preauthDTO.setRodHumanTask(assignInvestigatorDto.getHumanTask());
			//this.wizard = new IWizard();
			 //initBinder();
			//mainPanel = new VerticalSplitPanel();

			// setHeight("100");
			assignInvestigatorView = assignInvestigatorViewImpl.get();
			assignInvestigatorView.init(this.assignInvestigatorDto, wizard);
			wizard.addStep(assignInvestigatorView, "Reassign Investigation");

		//	confirmationViewImpl.init(assignInvestigatorDto);
			//wizard.addStep(this.confirmationViewImpl, "Confirmation");

			wizard.setStyleName(ValoTheme.PANEL_WELL);
			wizard.setSizeFull();
			wizard.addListener(this);

			// VerticalLayout wizardLayout = new VerticalLayout(wizard);
			RevisedCarousel intimationDetailsCarousel = commonCarouselInstance
					.get();
			intimationDetailsCarousel.init(this.assignInvestigatorDto.getClaimDto()
					.getNewIntimationDto(), this.assignInvestigatorDto
					.getClaimDto(), "Reassign Investigation",assignInvestigatorDto.getDiagnosisName());

			mainPanel.setFirstComponent(intimationDetailsCarousel);
			// TODO:
			viewDetails.initView(assignInvestigatorDto.getClaimDto().getNewIntimationDto().getIntimationId(),this.assignInvestigatorDto.getRodKey(),
					ViewLevels.PREAUTH_MEDICAL, false,"Assign Investigation");
			HorizontalLayout hLayout = new HorizontalLayout(commonButtonsLayout(),viewDetails);
			hLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
			hLayout.setWidth("100%");
			
			crmFlaggedComponents.init(assignInvestigatorDto.getCrcFlaggedReason(),assignInvestigatorDto.getCrcFlaggedRemark());
			crmFlaggedComponents.setWidth("100%");
			
			VerticalLayout wizardLayout1 = new VerticalLayout(hLayout,crmFlaggedComponents);
			wizardLayout1.setSpacing(true);

			Panel panel1 = new Panel();
			panel1.setContent(wizardLayout1);
			//panel1.setHeight("100px");
			VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
			wizardLayout2.setSpacing(true);
			mainPanel.setSecondComponent(wizardLayout2);

			mainPanel.setSizeFull();
			mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
			mainPanel.setHeight("700px");
			setCompositionRoot(mainPanel);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*public HorizontalLayout commonButtonsLayout()
	{
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		
		HorizontalLayout rrcBtnLayout = new HorizontalLayout(btnRRC);
		VerticalLayout vLayout = new VerticalLayout(viewDetails,rrcBtnLayout);
		vLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		vLayout.setComponentAlignment(rrcBtnLayout, Alignment.TOP_RIGHT);
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(
				vLayout);
		return alignmentHLayout;
	}*/
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(ReAssignInvestigationPresenter.VALIDATE_REASSIGN_INVESTIGATION_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.RE_ASSIGN_INVESTIGATION_INTIATED);
			
			
			
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
	
	
	private HorizontalLayout commonButtonsLayout() {

		TextField acknowledgementNumber = new TextField("Investigation No");
		acknowledgementNumber.setValue(String
				.valueOf(this.assignInvestigatorDto.getInvestigationNo()));
		//Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
		acknowledgementNumber.setWidth("250px");
		acknowledgementNumber.setHeight("20px");
		acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		acknowledgementNumber.setReadOnly(true);
		acknowledgementNumber.setEnabled(false);
		acknowledgementNumber.setNullRepresentation("");
		FormLayout hLayout = new FormLayout(acknowledgementNumber);
		hLayout.setComponentAlignment(acknowledgementNumber,
				Alignment.MIDDLE_LEFT);

		Button viewInvestigationDetails = new Button("View Investigation  details");
		viewInvestigationDetails.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getViewInvestigationDetails(assignInvestigatorDto
						.getClaimDto().getKey(), false);
			}
		});

		FormLayout viewEarlierRODLayout = new FormLayout(viewInvestigationDetails);

		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		
		//viewDetailsForm.addComponent(commonButtonsLayout());
		viewDetailsForm.addComponent(viewDetails);
		
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		HorizontalLayout formLayout = SHAUtils.newImageCRM(assignInvestigatorDto.getPreauthDTO());
		HorizontalLayout crmLayout = new HorizontalLayout(formLayout);
		crmLayout.setSpacing(true);
		crmLayout.setWidth("100%");
		
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(assignInvestigatorDto.getPreauthDTO());
		VerticalLayout hflayout=new VerticalLayout(crmLayout);
		
		HorizontalLayout componentsHLayout = new HorizontalLayout(hflayout,hopitalFlag,btnRRC,viewInvestigationDetails);
		componentsHLayout.setSpacing(true);
		
		return componentsHLayout;
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
		
		List<AssignInvestigatorDto> multipleInvestigatorsList = assignInvestigatorView.getMultiInvestigators();
		if(multipleInvestigatorsList != null && !multipleInvestigatorsList.isEmpty() &&  multipleInvestigatorsList.size()==1){
			this.assignInvestigatorDto.setMultipleInvestigatorList(multipleInvestigatorsList);
			//As part of CR R0676
			assignInvestigatorView.getAssignCountAlert();
			if(assignInvestigatorDto.getIsPrivateInvAllow()){
				fireViewEvent(ReAssignInvestigationPresenter.REASSIGN_INVESTIGATION_SUBMIT, assignInvestigatorDto);
			}
//			fireViewEvent(ReAssignInvestigationPresenter.REASSIGN_INVESTIGATION_SUBMIT, assignInvestigatorDto); //As part of CR R0676
		}
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "No", "Yes", new ConfirmDialog.Listener() {

		            /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void onClose(ConfirmDialog dialog) {
//						assignInvestigatorDto = new AssignInvestigatorDto();
		                if (!dialog.isConfirmed()) {
		                	
		                	wizard.releaseHumanTask();
		                    fireViewEvent(MenuItemBean.SHOW_RE_ASSIGN_INVESTIGATION, null);
		                    
		                } else {
//		                    dialog.close();
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
		Label successLabel = new Label("<b style = 'color: black;'>Reassign Investigation Submitted successfully !!!</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
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
				fireViewEvent(MenuItemBean.SHOW_RE_ASSIGN_INVESTIGATION, null);
				
			}
		});
	}

	/**
	 * Part of CR R0767 
	 */
	@Override
	public void showAssignCountAlert(String alertMsg){
		assignInvestigatorView.showAssignCountAlert(alertMsg);
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
