package com.shaic.claim.submitSpecialist;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.createinpopup.PopupInitiateLumenRequestWizardImpl;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.search.specialist.search.SpecialistCompletedCasesUI;
import com.shaic.claim.search.specialist.search.SubmitSpecialistTableDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;

public class SubmitSpecialistPageViewImpl extends AbstractMVPView implements SubmitSpecialistPageView {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	@Inject
//	private SubmitSpecialistPageTable uploadTranslatedTable;
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	private BeanFieldGroup<SubmitSpecialistDTO> binder;
	
//	private Button submitBtn;
	
	@Inject
	private SubmitSpecialistDTO bean;
	
	private HorizontalLayout buttonHorLayout;
	
//	private Button cancelBtn;
	
	private NewIntimationDto intimationDto;
	
	private ClaimDto claimDto;
	
	@Inject
	private SubmitSpecialistGridForm submitSpecialistGridForm;
	
	private SubmitSpecialistTableDTO searchDto;
	
	@Inject
	private SpecialistCompletedCasesUI specialistCompletedCases;
	
	@Inject
	private ViewDetails viewDetails;
	
	
	private RRCDTO rrcDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	////private static Window popup;
	PreauthDTO preauthDTO = null;
	
	@Inject
	private LumenDbService lumenService;
	
	@Inject
	private Instance<PopupInitiateLumenRequestWizardImpl> initiateLumenRequestWizardInstance;
	
	private PopupInitiateLumenRequestWizardImpl initiateLumenRequestWizardObj;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	@PostConstruct
	public void init() {
	}
	
	@SuppressWarnings("unchecked")
	public void initView(SubmitSpecialistTableDTO bean){
		
		if(bean.getPreauthDTO().getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
		}
		
		if(SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDTO().getNewIntimationDTO().getInsuredDeceasedFlag())) {
			
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}

		this.searchDto=bean;
		this.rrcDTO = bean.getRrcDTO();
		
		fireViewEvent(SubmitSpecialistPagePresenter.SET_FIRST_TABLE,bean);
		
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(rrcDTO);
		//preauthDTO.setRodHumanTask(bean.getHumanTask());
		//
		this.binder = new BeanFieldGroup<SubmitSpecialistDTO>(
				SubmitSpecialistDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		try{
			this.binder.commit();
		    this.bean = this.binder.getItemDataSource().getBean();
		}
		catch(CommitException e){
			e.printStackTrace();
		}
		
//		Pageable pageable = uploadTranslatedTable.getPageable();
//		this.bean.setPageable(pageable);
		
//		uploadTranslatedTable.init("Submit Specialist Adivise",false);
//		uploadTranslatedTable.setReference(new HashedMap());
		
		
		
		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		
//		intimationDetailsCarousel.init(this.intimationDto,
//				this.claimDto,"Submit Specialist Advise");
		
		intimationDetailsCarousel.init(this.intimationDto,
				this.claimDto,"Submit Specialist Advise",bean.getDiagnosis());
		//uploadTranslatedTable.addBeanToList(this.bean);
		
		viewDetails.initView(this.intimationDto.getIntimationId(), ViewLevels.INTIMATION, false,"Submit Specialist Advise");
		
	//	VerticalLayout verticalLayout = new VerticalLayout(intimationDetailsCarousel,viewDetails);
		VerticalLayout verticalLayout = new VerticalLayout(intimationDetailsCarousel, commonButtonsLayout());
		//verticalLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
		VerticalLayout mainsplitPanel = new VerticalLayout();
		
		mainsplitPanel.addComponent(verticalLayout);
		
//		submitBtn=new Button("Submit");
//		cancelBtn=new Button("Cancel");
//		
//		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//		submitBtn.setWidth("-1px");
//		submitBtn.setHeight("-10px");
//		
//		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
//		cancelBtn.setWidth("-1px");
//		cancelBtn.setHeight("-10px");
		
		addListener();
		
//		buttonHorLayout=new HorizontalLayout(submitBtn,cancelBtn);
//		buttonHorLayout.setSpacing(true);
//		
		VerticalLayout mainVertical=new VerticalLayout(submitSpecialistGridForm/*,buttonHorLayout*/);
		mainVertical.setSpacing(true);
//		mainVertical.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		mainsplitPanel.addComponent(mainVertical);
//		setHeight("100%");
//		mainsplitPanel.setSizeFull();
		
		setCompositionRoot(mainsplitPanel);
		
	}
	
	public VerticalLayout commonButtonsLayout()
	{
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		Button btnTrail = new Button("Specialist Trail");
		btnTrail.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				viewDetails.getSpecialityOpinion(intimationDto.getIntimationId());
			}
			
		});
		
		Button btnCompleted = new Button("Completed");
		btnCompleted.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				showPopupForCompletion();
				
			}
			
		});
		
		Button btnLumen = new Button("Initiate Lumen");
		btnLumen.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				invokePreMedicalLumenRequest();
			}
		});
		
		HorizontalLayout crmIconLayout = SHAUtils.newImageCRM(searchDto.getPreauthDTO());
		crmIconLayout.setSpacing(false);
        HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(searchDto.getPreauthDTO());
		
        // GXL2021044
     	HorizontalLayout hsTrafficIcon = SHAUtils.HsTraficImages(searchDto.getPreauthDTO());
     	
		HorizontalLayout hlayout=new HorizontalLayout(crmIconLayout, hopitalFlag,hsTrafficIcon);
		hlayout.setSpacing(true);
		
		HorizontalLayout hLayout = new HorizontalLayout(hlayout, viewDetails);
		hLayout.setComponentAlignment(viewDetails,Alignment.TOP_RIGHT);
		hLayout.setWidth("100%");
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(btnLumen, btnRRC, btnTrail,btnCompleted);
		alignmentHLayout.setSpacing(true);
		
		
		
		crmFlaggedComponents.init(searchDto.getCrcFlaggedReason(), searchDto.getCrcFlaggedRemark());
		
		//R0474
		HorizontalLayout pedLayout = new HorizontalLayout();
		if((searchDto.getPreauthDTO().getInsuredPedDetails() != null && ! searchDto.getPreauthDTO().getInsuredPedDetails().isEmpty()) || (searchDto.getPreauthDTO().getApprovedPedDetails() != null && !searchDto.getPreauthDTO().getApprovedPedDetails().isEmpty())){
			pedLayout.addComponent(getInsuredPedDetailsPanel());
		}
		pedLayout.addComponent(crmFlaggedComponents);
		pedLayout.setSpacing(true);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(hLayout);
		verticalLayout.addComponent(alignmentHLayout);
		verticalLayout.addComponent(pedLayout);
		
		return verticalLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		//fireViewEvent(PEDQueryPresenter.VALIDATE_PROCESS_PED_QUERY_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
		fireViewEvent(SubmitSpecialistPagePresenter.VALIDATE_SUBMIT_SPECIALIST_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_CASHLESS);
			
			
			
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
	
	private void invokePreMedicalLumenRequest(){
		preauthDTO.setNewIntimationDTO(this.intimationDto);
		List<Long> listOfSettledStatus = new ArrayList<Long>();
		/*listOfSettledStatus.add(ReferenceTable.FINANCIAL_SETTLED);
		listOfSettledStatus.add(ReferenceTable.PAYMENT_SETTLED);
		listOfSettledStatus.add(ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS);*/
		if(!listOfSettledStatus.contains(this.claimDto.getStatusId())){
			fireViewEvent(SubmitSpecialistPagePresenter.VALIDATE_SUBMIT_SPECIALIST_USER_LUMEN_REQUEST, preauthDTO);
		}else{
			showErrorMessage("Claim is settled, lumen cannot be initiated");
			return;
		}
	}
	
	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	@Override
	public void buildInitiateLumenRequest(String intimationNumber){
		System.out.println("Specialist Key " + bean.getKey());
		Long stageKey = lumenService.getStageBySpecialistKey(bean.getKey());
		System.out.println("Stage Value SubmitSpecialistPageViewImpl "+stageKey);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Initiate Lumen Request");
		popup.setWidth("75%");
		popup.setHeight("90%");
		LumenSearchResultTableDTO resultObj = lumenService.buildInitiatePage(intimationNumber);
		initiateLumenRequestWizardObj = initiateLumenRequestWizardInstance.get();
		initiateLumenRequestWizardObj.initView(resultObj, popup, "SSA Cashless");
		initiateLumenRequestWizardObj.setSpecialistStage(stageKey);
		
		VerticalLayout containerLayout = new VerticalLayout();
		containerLayout.addComponent(initiateLumenRequestWizardObj);
		popup.setContent(containerLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
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
	

	private void addListener() {
		
//		submitBtn.addClickListener(new Button.ClickListener() {
//			
//			@Override
//			public void buttonClick(ClickEvent event) {
//				
//				System.out.println("Completed");
//				@SuppressWarnings("unused")
//				List<SubmitSpecialistDTO> dtoValues=uploadTranslatedTable.getValues();
//				
//				SubmitSpecialistDTO submitData=uploadTranslatedTable.getValues().get(dtoValues.size()-1);
//				fireViewEvent(SubmitSpecialistPagePresenter.SUBMIT_BUTTON_CLICK,submitData,searchDto);
//			
//			}
//		});
//		cancelBtn.addClickListener(new Button.ClickListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
//				        "No", "Yes", new ConfirmDialog.Listener() {
//
//				            public void onClose(ConfirmDialog dialog) {
//				                if (!dialog.isConfirmed()) {
//				                	fireViewEvent(MenuItemBean.SUBMIT_SPECIALLIST_ADVISE, true);
//				                } else {
//				                    dialog.close();
//				                }
//				            }
//				        });
//				dialog.setStyleName(Reindeer.WINDOW_BLACK);
//			}
//		});
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ListOfValues(SubmitSpecialistDTO tableRows,NewIntimationDto intimationDto,ClaimDto claimDto) {
		
//		uploadTranslatedTable.setTableList(tableRows.getPageItems());
		this.bean = tableRows;
		
		this.submitSpecialistGridForm.init(this.bean, searchDto, SHAConstants.CASHLESS_SPECIALIST);
		
		this.intimationDto=intimationDto;
		this.claimDto=claimDto;
		//uploadTranslatedTable.addBeanToList(this.bean);
		
	}

	@Override
	public void result() {

Label successLabel = new Label("<b style = 'color: black;'>Claim record saved successfully !!! </b>", ContentMode.HTML);
		
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
				fireViewEvent(MenuPresenter.SUBMIT_SPECIALIST_ADVISE_GETTASK,searchDto.getSearchFormDTO(),true);
				
			}
		});
		
	
	}
	
	@Override
	public void reimbursementResult(SubmitSpecialistTableDTO searchDTO) {

		Label successLabel = new Label("<b style = 'color: black;'>Claim record saved successfully !!! </b>", ContentMode.HTML);
		
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
				fireViewEvent(MenuPresenter.SUBMIT_SPECIALIST_ADVISE_GETTASK,searchDTO.getSearchFormDTO(),true);
				
			}
		});
		
	
	}
	
	public void showPopupForCompletion(){
		
		String username = (String) getUI().getSession().getAttribute(BPMClientContext.USERID);
		
		specialistCompletedCases.init(username);
		
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Completed Cases");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(specialistCompletedCases);
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
	
	private Table getInsuredPedDetailsPanel(){
		
		Table table = new Table();
		table.setWidth("100%");
		table.addContainerProperty("pedCode", String.class, null);
		table.addContainerProperty("pedDescription",  String.class, null);
		table.setCaption("PED Details");
		
		int i=0;
//		table.setStyleName(ValoTheme.TABLE_NO_HEADER);
		if(searchDto.getPreauthDTO().getInsuredPedDetails() != null && !searchDto.getPreauthDTO().getInsuredPedDetails().isEmpty()) {
			for (InsuredPedDetails pedDetails : searchDto.getPreauthDTO().getInsuredPedDetails()) {
				if(pedDetails.getPedDescription() != null /*&& !("NIL").equalsIgnoreCase(pedDetails.getPedDescription())*/){
					table.addItem(new Object[]{pedDetails.getPedCode(), pedDetails.getPedDescription()}, i+1);
					i++;
				}
			}
		}
		
		if(searchDto.getPreauthDTO().getApprovedPedDetails() != null && !searchDto.getPreauthDTO().getApprovedPedDetails().isEmpty()){
			for (PreExistingDisease component : searchDto.getPreauthDTO().getApprovedPedDetails()) {
				table.addItem(new Object[]{component.getCode(), component.getValue()}, i+1);
				i++;
			}
		}
		
		
		table.setPageLength(2);
		table.setColumnHeader("pedCode", "PED Code");
		table.setColumnHeader("pedDescription", "Description");
		table.setColumnWidth("pedCode", 80);
		table.setColumnWidth("pedDescription", 320);
		table.setWidth("402px");
////		if(i>0){
//			Panel tablePanel = new Panel(table);
//			return tablePanel;
////		}
////		return null;
		
		return table;
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
