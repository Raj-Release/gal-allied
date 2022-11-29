package com.shaic.claim.pedrequest.approve.bancspedrequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.createinpopup.PopupInitiateLumenRequestWizardImpl;
import com.shaic.claim.pedrequest.SelectPEDRequestListener;
import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApprovePresenter;
import com.shaic.claim.pedrequest.approve.bancspedQuery.BancsSearchPEDRequestApproveTableDTO;
import com.shaic.claim.pedrequest.view.ViewPEDRequestTable;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.PedQueryDetailsTableData;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class BancsPEDRequestDetailsApproveViewImpl extends AbstractMVPView implements BancsPEDRequestDetailsApproveView, SelectPEDRequestListener  {
	
	private static final long serialVersionUID = -9182066388418239095L;
	
	private VerticalLayout mainLayout;
	
	private FormLayout sendtoApproverFLayout;
	
	private TextArea txtReviewRemarks;
	
	private FormLayout referToSpecialistFLayout;
	
	@Inject
	private ViewDetails viewDetails;
	
	private TextArea txtReasonforReferring;
	
//	private TextField fileUpload;
	
	private Upload fileUpload;
	
	private ComboBox cmbSpecialistType;
	
//	private ComboBox cmbWatchList;
	
	private FormLayout queryFLayout;
	
	private TextArea txtApproveRemark;
	
	private TextArea txtEscalateRemark;
	
	private TextArea txtQueryRemarkProcesser;
	
	private DateField txtRepudiationLetterDate;
	
	private TextArea txtWatchListRemarks;
	
	private HorizontalLayout buttonHLayout;
	
	private Button rejectBtn;
	
	private Button refertoSpecialistBtn;
	
	HorizontalLayout intimatePEDFLayout;
	
	private Button queryBtn;
	
	private Button watchListBtn;
	
	private Button approvedBtn;
	
	private Button escalateBtn;
	
	private FormLayout dynamicLayout;
	
	private FormLayout ApprovedLayout;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private ComboBox cmbPEDSuggestion;
	
	private NewIntimationDto intimationDto;
	
	private FormLayout firstForm;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private HorizontalLayout buttonHorLayout;
	private BeanItemContainer<SelectValue> selectValueContainer;
	
	@Inject
	private Instance<ViewPEDRequestTable> initiatePEDEndorsementTable;
	
	private ViewPEDRequestTable initiatePEDEndorsementObj;
	
	//private OldPedEndorsementDTO tableRows;
	
	private ClaimDto claimDto;
	
	//private String userName;
	
	//private String passWord;
	
	private Panel editPanel;
	
	private File file;
	
	@EJB
	private MasterService masterService;
	
	private Button tmpViewBtn;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	@Inject
	private BancsPEDRequsteApproveTable pedRequestDetailsList;
	
	@Inject
	private BancsPEDQueryDetailTable bancsPEDQueryDetailTableList;
	
//	@Inject
	private BancsPEDRequestDetailsApproveDTO bean;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	private BeanFieldGroup<BancsPEDRequestDetailsApproveDTO> binder;
	
	public String submitOption = "";
	
	private BancsSearchPEDRequestApproveTableDTO searchDTO;
	
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private PreauthDTO preauthDTO = null;
	
	private RRCDTO rrcDTO;
	
	@Inject
	private LumenDbService lumenService;
	
	@Inject
	private Instance<PopupInitiateLumenRequestWizardImpl> initiateLumenRequestWizardInstance;
	
	private PopupInitiateLumenRequestWizardImpl initiateLumenRequestWizardObj;
	
	private TextField txtNameofPED;
	
	private TextField txtRemarks;
	
	
	public BancsPEDRequestDetailsApproveViewImpl(){
		
	}
	
	@PostConstruct
	public void initView()
	{ 
		
	}

	@SuppressWarnings("unchecked")
	public void initView(BancsSearchPEDRequestApproveTableDTO searchDTO)
	{	
		
		this.searchDTO=searchDTO;
		
		bean = new BancsPEDRequestDetailsApproveDTO();
		
		if(this.searchDTO.getIsPolicyValidate()){		
			policyValidationPopupMessage();
		}
		
		if(searchDTO.getPreAuthDto().getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), searchDTO.getPreAuthDto().getIsSuspicious(), searchDTO.getPreAuthDto().getClmPrcsInstruction());
	    }
		
		if(SHAConstants.YES_FLAG.equalsIgnoreCase(searchDTO.getPreAuthDto().getNewIntimationDTO().getInsuredDeceasedFlag())) {
			
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		fireViewEvent(BancsPEDRequestDetailsApprovePresenter.BANCS_SET_FIELD_DATA, searchDTO);
		
		submitOption = "";		
		this.bean.setIsEditPED(false);
		
		this.binder = new BeanFieldGroup<BancsPEDRequestDetailsApproveDTO>(BancsPEDRequestDetailsApproveDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.rrcDTO = searchDTO.getRrcDTO();
		this.rrcDTO.setStrUserName(searchDTO.getUsername());
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);

		pedRequestDetailsList.init("PED Request Details",false, false);
		
		bancsPEDQueryDetailTableList.init("PED Query Details",false, false);

		
		try{
			this.binder.commit();
		    this.bean = this.binder.getItemDataSource().getBean();
		}
		catch(CommitException e){
			e.printStackTrace();
		}
		Pageable pageable = pedRequestDetailsList.getPageable();
		this.bean.setPageable(pageable);
		fireViewEvent(BancsPEDRequestDetailsApprovePresenter.BANCS_SET_FIRST_TABLE, searchDTO);
		
		pedRequestDetailsList.setHeight("150px");
		
		Pageable pageable2 = bancsPEDQueryDetailTableList.getPageable();
		this.bean.setPageable(pageable2);
		fireViewEvent(BancsPEDRequestDetailsApprovePresenter.BANCS_SET_SECOND_TABLE, searchDTO);
		
		bancsPEDQueryDetailTableList.setHeight("150px");
		
		VerticalSplitPanel mainsplitPanel = new VerticalSplitPanel();
		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
	//	intimationDetailsCarousel.init(new NewIntimationDto(), "PED Request");
		intimationDetailsCarousel.init(this.intimationDto,this.claimDto, "Process PED Query Request", searchDTO.getDiagnosis());
		mainsplitPanel.setFirstComponent(intimationDetailsCarousel);
		
		viewDetails.initView(this.intimationDto.getIntimationId(), ViewLevels.INTIMATION, false,"Process PED Query Request");
		
		Panel tableVertical=new Panel(pedRequestDetailsList);
		tableVertical.setHeight("180px");
		
		Panel emptyVertical=new Panel();
		emptyVertical.setHeight("50px");
		
		Panel tableVertical2=new Panel(bancsPEDQueryDetailTableList);
		tableVertical2.setHeight("80px");

		dynamicLayout=new FormLayout();
//		queryLayout=new FormLayout(buildQueryFLayout());
//		referLayout=new FormLayout(buildReferToSpecialistFLayout());
//		rejectLayout=new FormLayout(buildRejectionFLayout());
//		approveLayout=new FormLayout(buildApprovedFLayout());
		
		submitBtn=new Button("Submit");
		cancelBtn=new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		buttonHorLayout=new HorizontalLayout(submitBtn,cancelBtn);
		buttonHorLayout.setSpacing(true);
		
		Table insuredPedDetails = getInsuredPedDetailsPanel();
		
		addListener();
		HorizontalLayout hLyout =new HorizontalLayout(commonButtonsLayout(),viewDetails);
		hLyout.setWidth("100%");
		hLyout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
		crmFlaggedComponents.init(searchDTO.getPreAuthDto().getCrcFlaggedReason(),searchDTO.getPreAuthDto().getCrcFlaggedRemark());
			
		HorizontalLayout horizontalPanel = new HorizontalLayout(crmFlaggedComponents,insuredPedDetails);
		horizontalPanel.setSpacing(true);
		horizontalPanel.setComponentAlignment(insuredPedDetails, Alignment.MIDDLE_LEFT);

		VerticalLayout verLayout = new VerticalLayout(hLyout,horizontalPanel);
		
		editPanel = new Panel();
//		//VerticalLayout verticalLayout = new VerticalLayout(viewDetails,pedRequestDetailsList,IntimatePEDLayout,initiatePEDEndorsementTableInstance,txtProcessorRemarks,buildMainLayout(),dynamicLayout,buttonHorLayout);
//		VerticalLayout verticalLayout = new VerticalLayout(verticalPanel,pedRequestDetailsList,editPanel,formProcessorRemarks,buildMainLayout(),dynamicLayout,buttonHorLayout);
		VerticalLayout verticalLayout = new VerticalLayout(verLayout,pedRequestDetailsList,emptyVertical,bancsPEDQueryDetailTableList,editPanel,buildMainLayout(),dynamicLayout,buttonHorLayout);
		verticalLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
	//	verticalLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
		verticalLayout.setMargin(false);
		verticalLayout.setSpacing(true);

		mainsplitPanel.setSecondComponent(verticalLayout);

		mainsplitPanel.setSplitPosition(22, Unit.PERCENTAGE);
		setHeight("100%");
		mainsplitPanel.setSizeFull();
		mainsplitPanel.setHeight("650px");
		setCompositionRoot(mainsplitPanel);
		if(bean.getIsWatchList()){
			alertMessageForPEDWatchList();
		}
		if (searchDTO.getRenewalDue() <= 60 && searchDTO.getRenewalDue() >= 0){
			alertMessageForDueDate();
		}
		
	}
	
	public Boolean alertMessageForPEDWatchList() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_FROM_WATCHLIST + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
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
//		dialog.setCaption("Alert");
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
			}
		});
		return true;
	}
	
	public Boolean alertMessageForDueDate() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_ALERT_MSG_FOR_DUE + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Button viewPolicy = new Button("View Policy Details");
		HorizontalLayout buttonhori = new HorizontalLayout(homeButton,viewPolicy);
		buttonhori.setSpacing(true);
		VerticalLayout layout = new VerticalLayout(successLabel, buttonhori);
		layout.setSpacing(true);
//		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
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
			}
		});
		viewPolicy.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				getPortabilityViewtByPolicyNo(searchDTO.getPolicyNo());
			}
		});
		return true;
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
		
		Button btnLumen = new Button("Initiate Lumen");
		btnLumen.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				invokePreMedicalLumenRequest();
			}
		});
		
		
		Button elearnBtn = viewDetails.getElearnButton();
		HorizontalLayout formLayout = SHAUtils.newImageCRM(searchDTO.getPreAuthDto());
		HorizontalLayout crmLayout = new HorizontalLayout(formLayout);
		crmLayout.setSpacing(true);
		crmLayout.setWidth("40%");
		
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(searchDTO.getPreAuthDto());
		VerticalLayout vlayout=new VerticalLayout(formLayout, hopitalFlag);
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(vlayout,btnRRC,btnLumen,elearnBtn);
		alignmentHLayout.setSpacing(true);
		
		return alignmentHLayout;

	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(BancsPEDRequestDetailsApprovePresenter.BANCS_VALIDATE_PED_APPROVE_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PED_REQUEST_APPROVER);
			
			
			
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
		List<Long> listOfClosedStatus = new ArrayList<Long>();
		listOfClosedStatus.add(ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.FINANCIAL_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.CLAIM_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.BILLING_CLOSED_STATUS);
		listOfClosedStatus.add(140L);		
		listOfClosedStatus.add(ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.ZONAL_REVIEW_PROCESS_CLAIM_REQUEST_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.PREAUTH_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.CREATE_ROD_CLOSED_STATUS);
		listOfClosedStatus.add(214L);
		
		List<Long> listOfSettledStatus = new ArrayList<Long>();
		listOfSettledStatus.add(ReferenceTable.FINANCIAL_SETTLED);
		listOfSettledStatus.add(ReferenceTable.PAYMENT_SETTLED);
		listOfSettledStatus.add(ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS);
		
		/*if(!listOfClosedStatus.contains(this.claimDto.getStatusId())){
			if(!listOfSettledStatus.contains(this.claimDto.getStatusId())){
				fireViewEvent(BancsPEDRequestDetailsApprovePresenter.BANCS_VALIDATE_PED_APPROVE_USER_LUMEN_REQUEST, intimationDto);
			}else{
				showErrorMessage("Claim is settled, lumen cannot be initiated");
				return;
			}
		}else{
			showErrorMessage("Claim is closed, lumen cannot be initiated");
			return;
		}*/
		fireViewEvent(BancsPEDRequestDetailsApprovePresenter.BANCS_VALIDATE_PED_APPROVE_USER_LUMEN_REQUEST, intimationDto);
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
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Initiate Lumen Request");
		popup.setWidth("75%");
		popup.setHeight("90%");
		LumenSearchResultTableDTO resultObj = lumenService.buildInitiatePage(intimationNumber);
		initiateLumenRequestWizardObj = initiateLumenRequestWizardInstance.get();
		initiateLumenRequestWizardObj.initView(resultObj, popup, "PED");
		
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
	
	public void getPortabilityViewtByPolicyNo(String strPolicyNo) {
		String strDmsViewURL = BPMClientContext.PORTABLITY_VIEW_URL;
		BrowserFrame browserFrame = new BrowserFrame("", new ExternalResource(
				strDmsViewURL + strPolicyNo));

		browserFrame.setHeight("600px");
		browserFrame.setWidth("200%");
		Button btnSubmit = new Button("OK");

		btnSubmit.setCaption("CLOSE");
		// //Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		// btnSubmit.setWidth("-1px");
		// btnSubmit.setHeight("-10px");
		// btnSubmit.setDisableOnClick(true);
		// //Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		VerticalLayout vLayout = new VerticalLayout(browserFrame, btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_CENTER);
		vLayout.setSizeFull();
		final Window popup = new com.vaadin.ui.Window();

		popup.setCaption("");
		popup.setWidth("100%");
		popup.setHeight("100%");
		// popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);

		btnSubmit.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				popup.close();

			}

		});

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
	
	
	public void addListener() {
		
		cancelBtn.addClickListener(new Button.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
					        "No", "Yes", new ConfirmDialog.Listener() {

					            public void onClose(ConfirmDialog dialog) {
					                if (!dialog.isConfirmed()) {
					                	
					                	//VaadinSession session = getSession();
										//SHAUtils.releaseHumanTask(searchDTO.getUsername(), searchDTO.getPassword(), searchDTO.getTaskNumber(),session);
					                	releaseHumanTask();
					                	clearObject();
					                	fireViewEvent(MenuItemBean.PROCESS_PED_QUERY_REQUEST, true);
					                } else {
					                    dialog.close();
					                }
					            }
					        });
					dialog.setClosable(false);
					dialog.setStyleName(Reindeer.WINDOW_BLACK);
				}
			});
		
		submitBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(txtRepudiationLetterDate != null){
					if(txtRepudiationLetterDate.getValue()==null){
						unbindField(txtRepudiationLetterDate);
					}
				}
				
				if (validatePage()) {

					fireViewEvent(
							BancsPEDRequestDetailsApprovePresenter.BANCS_APPROVE_REMARKS,bean,searchDTO);
				}

			}
		});
		
         
	}
	
	public void addListenerForPED(){
		
		 cmbPEDSuggestion.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					ComboBox suggestionCmb = (ComboBox)event.getProperty();
					Long intimationKey = (Long) suggestionCmb.getData();
					
					SelectValue suggestionSelect = suggestionCmb.getValue() != null ? (SelectValue)suggestionCmb.getValue() : null;
					if(suggestionSelect != null && suggestionSelect.getId() != null){
						
						if (cmbPEDSuggestion.getValue().toString().toLowerCase()
								.contains("sug 002 - cancel policy")) {
							txtRepudiationLetterDate = (DateField) binder.buildAndBind(
									"Repudiation Letter Date", "repudiationLetterDate",
									DateField.class);
							firstForm.addComponent(txtRepudiationLetterDate);
							mandatoryFields.add(txtRepudiationLetterDate);
							showOrHideValidation(false);
						} else {
							if(txtRepudiationLetterDate != null){
							txtRepudiationLetterDate.setValue(null);
							firstForm.removeComponent(txtRepudiationLetterDate);
							mandatoryFields.remove(txtRepudiationLetterDate);
							unbindField(txtRepudiationLetterDate);
							}
						}
					
						if(cmbPEDSuggestion != null && cmbPEDSuggestion.getValue() != null && ((SelectValue)cmbPEDSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG004)){
							txtNameofPED.setValue(null);
							txtNameofPED.setEnabled(false);
							txtNameofPED.setRequired(false);
							mandatoryFields.remove(txtNameofPED);
							unbindField(txtNameofPED);
							initiatePEDEndorsementObj.disableAdd(Boolean.TRUE);
						}else{
							txtNameofPED.setEnabled(true);
							txtNameofPED.setRequired(true);
							mandatoryFields.add(txtNameofPED);
							showOrHideValidation(false);
							initiatePEDEndorsementObj.disableAdd(Boolean.FALSE);
						}
						
						fireViewEvent(BancsPEDRequestDetailsApprovePresenter.BANCS_GET_PED_ALREADY_AVAILABLE_APPROVER, suggestionSelect.getId(), intimationKey,bean.getKey());
						
						if(initiatePEDEndorsementObj != null){
							initiatePEDEndorsementObj.setSuggestionKey(suggestionSelect.getId());
						}	
					}
				}
			});
	}

	/*private HorizontalLayout buildFooterButton() {
		NativeButton submitBtn = new NativeButton("Submit");
		NativeButton cancelBtn = new NativeButton("Cancel");
		HorizontalLayout footerBtnLayout = new HorizontalLayout(submitBtn,cancelBtn);
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		footerBtnLayout.setSpacing(true);
		footerBtnLayout.setMargin(true);
		footerBtnLayout.setWidth("100%");
		return footerBtnLayout;
	}*/

	public void editPanelForPED(BancsPEDRequestDetailsApproveDTO bean) {
		
		this.bean.setPedApprovalName(bean.getPedApprovalName());
		
		this.bean.setPedSuggestion(bean.getPedSuggestion());
		
		this.bean.setRemarks(bean.getRemarks());
		this.bean.setRepudiationLetterDate(bean.getRepudiationLetterDate());
		
		this.bean.setPedInitiateDetails(bean.getPedInitiateDetails());
		
		this.bean.setIsEditPED(bean.getIsEditPED());
		

		cmbPEDSuggestion =(ComboBox) binder.buildAndBind("PED Suggestion","pedSuggestion", ComboBox.class);
		
		cmbPEDSuggestion.setContainerDataSource(selectValueContainer);
		cmbPEDSuggestion.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPEDSuggestion.setItemCaptionPropertyId("value");
		cmbPEDSuggestion.setNullSelectionAllowed(false);
		cmbPEDSuggestion.setData(this.intimationDto.getKey());
		
		cmbPEDSuggestion.setRequired(true);
		
		mandatoryFields.add(cmbPEDSuggestion);
		
		cmbPEDSuggestion.setValue(this.bean.getPedSuggestion());
		
		//cmbPEDSuggestion =(ComboBox) binder.buildAndBind("PED Suggestion","approvalPedSuggestion", ComboBox.class);
		txtNameofPED =(TextField) binder.buildAndBind("Name of PED", "pedApprovalName", TextField.class);
		//txtNameofPED.setRequired(true);
		txtNameofPED.setNullRepresentation("");
		
		/*txtNameofPED.setRequired(true);
		mandatoryFields.add(txtNameofPED);*/

		txtRemarks =(TextField) binder.buildAndBind("Remarks", "remarks", TextField.class);
		txtRemarks.setRequired(true);
		txtRemarks.setNullRepresentation("");
		
		txtRemarks.setRequired(true);
		mandatoryFields.add(txtRemarks);

//		txtRepudiationLetterDate =(DateField) binder.buildAndBind("Repudiation Letter Date", "repudiationLetterDate", DateField.class);
//		txtRepudiationLetterDate.setRequired(true);
		
		firstForm = new FormLayout(cmbPEDSuggestion,txtNameofPED,txtRemarks);
		firstForm.setSpacing(true);
		
		BeanItemContainer<SelectValue> selectIcdChapterContainer=masterService.getSelectValuesForICDChapter();
		
		BeanItemContainer<SelectValue> selectPedCodeContainer=masterService.getPedDescription();
		
		BeanItemContainer<SelectValue> selectSourceContainer=masterService.getSelectValueContainer(ReferenceTable.PED_SOURCE);
		//referenceData.put("icdCode", selectValueContainer);
		referenceData.put("icdChapter",selectIcdChapterContainer);
		referenceData.put("pedCode", selectPedCodeContainer);
		referenceData.put("source", selectSourceContainer);
		
		
		initiatePEDEndorsementObj = initiatePEDEndorsementTable.get();
		initiatePEDEndorsementObj.init(SHAConstants.PED_QUERY_REQUEST);
		initiatePEDEndorsementObj.setReferenceData(referenceData);
		initiatePEDEndorsementObj.setIntimationKey(this.intimationDto.getKey());
		
		List<ViewPEDTableDTO> pedInitiateDetails = this.bean.getPedInitiateDetails();
		if(pedInitiateDetails != null){
			for (ViewPEDTableDTO viewPEDTableDTO : pedInitiateDetails) {
				initiatePEDEndorsementObj.addBeanToList(viewPEDTableDTO);
			}	
		}
		
		if(cmbPEDSuggestion != null && cmbPEDSuggestion.getValue() != null && ((SelectValue)cmbPEDSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG004)){
			txtNameofPED.setEnabled(false);
			txtNameofPED.setRequired(false);
			mandatoryFields.remove(txtNameofPED);
			unbindField(txtNameofPED);
			initiatePEDEndorsementObj.disableAdd(Boolean.TRUE);
		}
		
//		if(cmbPEDSuggestion.getValue().toString().toLowerCase()
//			.contains("sug 002 - cancel policy"))
//		{
//			firstForm.addComponent(txtRepudiationLetterDate);
//		}
//		else
//		{
//		        unbindField(txtRepudiationLetterDate);
//		        firstForm.removeComponent(txtRepudiationLetterDate);
//		}
		
		VerticalLayout mainVertical = new VerticalLayout(firstForm,initiatePEDEndorsementObj);
		mainVertical.setComponentAlignment(firstForm, Alignment.TOP_RIGHT);
		
		
		Panel mainPanel = new Panel(mainVertical);
		
		editPanel.setContent(mainPanel);
		
		showOrHideValidation(false);
		
		addListenerForPED();
		
		
	}

	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		setWidth("100.0%");
		setHeight("100.0%");
		
		return mainLayout;
	}


	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tableRowSelectionListener(OldPedEndorsementDTO item) {
		System.out.println("--------------------- callledll----------------------"+item.getPedName());
//		bean.setNameofPED(item.getPedName());
//		bean.setRemarks(item.getRemarks());
//		bean.setRepudiationLetterDate(item.getRepudiationLetterDate());
	}


	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> File Uploaded Successfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Ok");
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
			}
		});
	}
	

	@Override
	public void list(Page<OldPedEndorsementDTO> tableRows) {
		
        pedRequestDetailsList.setTableList(tableRows.getPageItems());
        pedRequestDetailsList.setRowColor(searchDTO.getKey());
	}
	
	@Override
	public void listQueryDetails(Page<BancsPEDQueryDetailTableDTO> tableRows) {
		
		bancsPEDQueryDetailTableList.setTableList(tableRows.getPageItems());
		//bancsPEDQueryDetailTableList.setRowColor(searchDTO.getKey());
	}

	@Override
	public void setReferenceData(BancsPEDRequestDetailsApproveDTO bean,BeanItemContainer<SelectValue> selectValueContainer,NewIntimationDto intimationDto,ClaimDto claimDto) {
		this.bean=bean;
		this.intimationDto=intimationDto;
		this.selectValueContainer=selectValueContainer;
		this.claimDto=claimDto;
		
	}


	@Override
	public void setPEDEndorsementTable(OldPedEndorsementDTO tableRows,
			Map<String, Object> referenceData) {
		
		//this.tableRows=tableRows;
		this.referenceData=referenceData;
		
		//initiatePEDEndorsementTableInstance.setReference(referenceData);
//		initiatePEDEndorsementTableInstance.setTableList(tableRows);
//		initiatePEDEndorsementTableInstance.addBeanToList(tableRows.getNewInitiatePedEndorsementDto().get(0));
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private Table getInsuredPedDetailsPanel(){
		
		
		Table table = new Table();
//		table.setWidth("30%");
		table.addContainerProperty("pedCode", String.class, null);
		table.addContainerProperty("pedDescription",  String.class, null);
		table.setCaption("PED Details");
		
		int i=0;
//		table.setStyleName(ValoTheme.TABLE_NO_HEADER);
		if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty()) {
			for (InsuredPedDetails pedDetails : this.bean.getInsuredPedDetails()) {
				if(pedDetails.getPedDescription() != null /*&& !("NIL").equalsIgnoreCase(pedDetails.getPedDescription())*/){
				table.addItem(new Object[]{pedDetails.getPedCode(), pedDetails.getPedDescription()}, i+1);
				i++;
			}}
		}
		
		if(this.bean.getApprovedPedDetails() != null && !this.bean.getApprovedPedDetails().isEmpty()){
			for (PreExistingDisease component : this.bean.getApprovedPedDetails()) {
				table.addItem(new Object[]{component.getCode(), component.getValue()}, i+1);
				i++;
			}
		}
		
		
		table.setPageLength(2);
		table.setColumnHeader("pedCode", "PED Code");
		table.setColumnHeader("pedDescription", "Description");
		table.setColumnWidth("pedCode", 100);
		table.setColumnWidth("pedDescription", 280);
		
		return table;
		
//		Panel tablePanel = new Panel(table);
//		return tablePanel;
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		

		
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}
		
		Boolean isSumInsuredLocked = Boolean.FALSE;
		if(cmbPEDSuggestion != null && cmbPEDSuggestion.getValue() != null && ((SelectValue)cmbPEDSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG004)){
			isSumInsuredLocked = Boolean.TRUE;
		}
		
		
		if(!isSumInsuredLocked && this.txtNameofPED != null && (this.txtNameofPED.getValue() == null || this.txtNameofPED.getValue().isEmpty())){
			hasError = true;
			eMsg.append("Please Enter PED Name. </br>"); 
		}
		
		if(!isSumInsuredLocked){
			if(this.initiatePEDEndorsementObj != null && this.initiatePEDEndorsementObj.getValues().isEmpty()) {
				hasError = true;
				eMsg.append("Please Add Atleast one diagnosis List Details to Proceed Further. </br>"); 
			}}
		
		
		if(initiatePEDEndorsementObj != null && ! initiatePEDEndorsementObj.isValid(isSumInsuredLocked)){
			List<String> errors = initiatePEDEndorsementObj.getErrors();
			for (String string : errors) {
				eMsg.append(string).append("</br>");
			}
		}
		
		if(bancsPEDQueryDetailTableList != null){
			List<BancsPEDQueryDetailTableDTO> values = bancsPEDQueryDetailTableList.getValues();
			this.bean.setPedQueryDetailsTableData(values); 

			Boolean isValid = true;
			for (BancsPEDQueryDetailTableDTO bancsPEDQueryDetailTableDTO : values) {
				if(bancsPEDQueryDetailTableDTO.getReplyRemarks() == null || bancsPEDQueryDetailTableDTO.getReplyRemarks().isEmpty()){
					isValid = false;
					break;
				}
			}
			
			if(!isValid){
				eMsg.append("Please Enter Reply Remark");
				hasError = true;
			}
			
		}
		
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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

			hasError = true;
			return !hasError;
		} else{
			try {
				
				this.binder.commit();
				if(initiatePEDEndorsementObj != null){
				List<ViewPEDTableDTO> values = initiatePEDEndorsementObj.getValues();
				this.bean.setPedInitiateDetails(values);
					if(this.bean.getDeletedDiagnosis().isEmpty()){
						this.bean.setDeletedDiagnosis(initiatePEDEndorsementObj.deletedDTO);
					}
				}
				
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			showOrHideValidation(false);
			return true;
		}
	
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
	
	private void unbindField(Field<?> field) {
		if (field != null) {
			if(binder.getPropertyId(field)!=null){
				this.binder.unbind(field);
			}
			
		}
	}

	@Override
	public void showEditPanel(BancsPEDRequestDetailsApproveDTO bean) {
		
		editPanelForPED(bean);
		
	}
	
	@Override
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer) {
	
		initiatePEDEndorsementObj.setIcdBlock(icdBlockContainer);
	}
	@Override
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer) {
		
		initiatePEDEndorsementObj.setIcdCode(icdCodeContainer);
		
	}

	@Override
	public void setPEDCode(String icdCodeContainer) {
		
		initiatePEDEndorsementObj.setPEDCode(icdCodeContainer);
		
	}

	private void releaseHumanTask(){
		
		Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
     	/*String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);*/
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

	
	
	public void policyValidationPopupMessage() {	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.POLICY_VALIDATION_ALERT + "</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			/*HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");*/

			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();					
				}
			});
		}
	
	@Override
	public void showPEDAlreadyAvailable(String pedAvailableMsg){
		
		showErrorMessage(pedAvailableMsg);
		
		if(!pedAvailableMsg.toLowerCase().contains("icd")){
			cmbPEDSuggestion.setValue(null);
		}
		
	}	
	
	@Override
	public void resetPEDDetailsTable(ViewPEDTableDTO newInitiatePedDTO){
		initiatePEDEndorsementObj.resetPEDDetailsTable(newInitiatePedDTO);
	}

	@Override
	public void result(Boolean result) {
		
    Label successLabel = new Label("<b style = 'color: black;'>"+"Query Submitted Successfully"+" </b>", ContentMode.HTML);
	
	Button homeButton = new Button("PED Approver Home");
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
			clearObject();
			fireViewEvent(MenuItemBean.PROCESS_PED_QUERY_REQUEST, true);
			
		}
	});
}
	
	 public void clearObject() {
		 SHAUtils.setClearReferenceData(referenceData);
			if(initiatePEDEndorsementObj!=null){
				initiatePEDEndorsementObj.clearObject();
				initiatePEDEndorsementObj=null;
			}
			if(selectValueContainer!=null){
				selectValueContainer=null;
			}
			if(preauthDTO!=null){
				preauthDTO=null;
			}
			if(this.bean!=null){
				this.bean=null;
			}
			if(this.intimationDto!=null){
				this.intimationDto=null;
			}
			if(this.claimDto!=null){
				this.claimDto=null;
			}
		
	 	}
}
