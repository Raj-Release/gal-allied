package com.shaic.claim.processRejectionPage;

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
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class ProcessRejectionPageWizardViewImpl extends AbstractMVPView implements ProcessRejectionWizard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;
	
	private ProcessRejectionDTO bean;
	
	private Button viewPreauthBtn;
	
	private Button preauthDocumentBtn;
	
	private RRCDTO rrcDTO;
	
	private PreauthDTO preauthDTO;
	
	@Inject
	private ViewDetails viewDetails;
	
	private SearchProcessRejectionTableDTO searchDTO;
	
	private NewIntimationDto intimationDto;
	
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	@Inject
	private Instance<ProcessRejectionPageViewImpl> processRejectionPageViewImpl;
	
	private ProcessRejectionPageViewImpl processRejectionPageViewObj;
	
	@Inject
	private Instance<ProcessRejectionLetterUI> processRejectionLetterViewImpl;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	private ProcessRejectionLetterUI processRejectionLetterObj;
	
	@Inject
	private Toolbar toolbar;
	
	private VerticalSplitPanel mainPanel;

	@PostConstruct
	public void initView() {

		addStyleName("view");
		setSizeFull();			
	}
	
	@Override
	public void initView(SearchProcessRejectionTableDTO searchDTO){
		
		this.wizard = new IWizardPartialComplete();
		
		this.wizard.getFinishButton().setCaption("Submit");
		
		
		this.searchDTO = searchDTO;
		this.intimationDto = searchDTO.getIntimationDTO();
		
		this.rrcDTO = searchDTO.getRrcDTO();
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
		preauthDTO.setNewIntimationDTO(this.intimationDto); 
		//preauthDTO.setRodHumanTask(searchDTO.getHumanTask());
		
		viewDetails.initView(this.searchDTO.getIntimationNo(),
				ViewLevels.INTIMATION, false,"Process Rejection");
		// mainLayout.addComponent(viewDetails);
		// mainLayout.setComponentAlignment(viewDetails, Alignment.TOP_CENTER);
	/*	bean = new ProcessRejectionDTO();
		bean.setCrmFlagged(searchDTO.getCrmFlagged());
		bean.getNewIntimationDTO().getHospitalDto().setIsPreferredHospital(searchDTO.getIsPreferredHospital());
		bean.getNewIntimationDTO().getInsuredPatient().setInsuredAge(searchDTO.getInsuredAge());*/
		HorizontalLayout formLayout = SHAUtils.newImageCRM(searchDTO.getPreAuthDto());
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(searchDTO.getPreAuthDto());
		HorizontalLayout crmLayout = new HorizontalLayout(formLayout,hopitalFlag);
		crmLayout.setSpacing(true);
		crmLayout.setWidth("100%");
		
		HorizontalLayout hTempLayout = new HorizontalLayout(crmLayout,commonButtonsLayout());
		hTempLayout.setSpacing(true);
		HorizontalLayout viewHorizantal = new HorizontalLayout(hTempLayout,viewDetails);
		viewHorizantal.setWidth("100%");
		viewHorizantal.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		

//		HorizontalLayout crmFlaggedLayout = SHAUtils.crmFlaggedLayout(searchDTO.getCrcFlaggedReason(),searchDTO.getCrcFlaggedRemark());
		crmFlaggedComponents.init(searchDTO.getCrcFlaggedReason(),searchDTO.getCrcFlaggedRemark());
//		crmFlaggedLayout.setComponentAlignment(viewDetails, Alignment.TOP_LEFT);
		
		VerticalLayout viewVertical = new VerticalLayout(viewHorizantal,crmFlaggedComponents);
		viewVertical.setWidth("100%");
		
		
		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.intimationDto, "Process Rejection");

		/*viewVertical.addComponent(viewHorizantal);
		viewHorizantal.setSpacing(true);*/
		
		
		processRejectionPageViewObj = processRejectionPageViewImpl.get();
		processRejectionPageViewObj.initView(searchDTO,this.searchDTO.getProcessRejectionDTO(),this.wizard);
		this.wizard.addStep(processRejectionPageViewObj,"Process Rejection Page");
		
		processRejectionLetterObj = processRejectionLetterViewImpl.get();
		processRejectionLetterObj.init(searchDTO, this.searchDTO.getProcessRejectionDTO(), wizard);
		this.wizard.addStep(processRejectionLetterObj,"Process Rejection Letter");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
		VerticalLayout mainVertical = new VerticalLayout(viewVertical,wizard);
		mainVertical.setSpacing(true);
		
		
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		mainPanel.setSecondComponent(mainVertical);
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);

	}
	
	@Override
	public void savedResult() {
//		ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Successfull", "Claim record saved successfully ",
//		        "Ok",null,new ConfirmDialog.Listener() {
//
//		            /**
//					 * 
//					 */
//					private static final long serialVersionUID = 1L;
//
//					public void onClose(ConfirmDialog dialog) {
//		                if (dialog.isConfirmed()) {
//		                	fireViewEvent(MenuItemBean.PROCESS_PREAUTH_REJECTION, true);
//		                } else {
//		                    dialog.close();
//		                }
//		            }
//		        });
//		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
Label successLabel = new Label("<b style = 'color: black;'>Claim Record Saved Successfully!!! </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Process Rejection Home");
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
				searchDTO.setProcessRejectionDTO(null);
				searchDTO = null;
				fireViewEvent(MenuItemBean.PROCESS_PREAUTH_REJECTION, true);
				
			}
		});
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
		
	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {
		
	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
		
		if (!ProcessRejectionPageViewImpl.DESICION) {
//			fireViewEvent(ProcessRejectionWizardPresenter.SUBMIT_DATA,
//			this.searchDTO.getProcessRejectionDTO(), false, "REJECT", searchDTO,intimationDto);
			
			fireViewEvent(ProcessRejectionWizardPresenter.SUBMIT_DATA,
					this.searchDTO.getProcessRejectionDTO(), false, SHAConstants.OUTCOME_FLP_NON_MED_CONFIRM_REJECTION, searchDTO,intimationDto);
			
		}else{
			fireViewEvent(ProcessRejectionWizardPresenter.SUBMIT_DATA,
			this.searchDTO.getProcessRejectionDTO(), true, SHAConstants.OUTCOME_FLP_NON_MED_WAIVE_REJECTION, searchDTO,intimationDto);
		}
		
	}
		

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		
		ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Confirmation",
						"Are you sure you want to cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									// Confirmed to continue
									releaseHumanTask();
									searchDTO.setProcessRejectionDTO(null);
									searchDTO = null;
									fireViewEvent(MenuItemBean.PROCESS_PREAUTH_REJECTION,
									true);
								} else {
									// User did not confirm
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
		
		Button viewLinkedPolicy = viewDetails.getLinkedPolicyDetails(searchDTO.getPreAuthDto());
		
		HorizontalLayout alignmentHLayout;
		if (this.searchDTO.getIsPremedical()) {
			viewPreauthBtn = new Button("View pre-auth");
			
			viewPreauthBtn.addClickListener(new ClickListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (searchDTO != null
							&& searchDTO.getIntimationDTO() != null
							&& searchDTO.getIntimationDTO().getIntimationId() != null
							&& !searchDTO.getIntimationDTO().getIntimationId()
									.equals("")){
						viewDetails.getPreAuthUpdateDetail(searchDTO.getIntimationDTO().getIntimationId());
					}
				}
			});
			
			if(searchDTO.getPreAuthDto().getNewIntimationDTO() != null && searchDTO.getPreAuthDto().getNewIntimationDTO().getPolicy().getGmcPolicyType() != null &&
					(searchDTO.getPreAuthDto().getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
							searchDTO.getPreAuthDto().getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))) {
				alignmentHLayout = new HorizontalLayout(btnRRC,viewPreauthBtn,viewLinkedPolicy);
			} else {
			 alignmentHLayout = new HorizontalLayout(btnRRC,viewPreauthBtn);
			}
			 
		}
		else
		{
			preauthDocumentBtn = new Button("Pre-auth Document");
			if(searchDTO.getPreAuthDto().getNewIntimationDTO() != null && searchDTO.getPreAuthDto().getNewIntimationDTO().getPolicy().getGmcPolicyType() != null &&
					(searchDTO.getPreAuthDto().getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
							searchDTO.getPreAuthDto().getNewIntimationDTO().getPolicy().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))) {
				alignmentHLayout = new HorizontalLayout(btnRRC,preauthDocumentBtn,viewLinkedPolicy);
			} else {
				alignmentHLayout = new HorizontalLayout(btnRRC,preauthDocumentBtn);
			}
		}
		alignmentHLayout.setSpacing(true);
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(ProcessRejectionPresenter.VALIDATE_PROCESS_REJECTION_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
	}
	
	private void releaseHumanTask(){
		VaadinSession session = getSession();
		Long wrkFlowKey=(Long)session.getAttribute(SHAConstants.WK_KEY);
		DBCalculationService dbService = new DBCalculationService();
 		if(wrkFlowKey != null) {
 			dbService.callUnlockProcedure(session);
 			getSession().setAttribute(SHAConstants.WK_KEY, null);
 		}
	}

}
