package com.shaic.claim.pedrequest.teamlead;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.createinpopup.PopupInitiateLumenRequestWizardImpl;
import com.shaic.claim.pedrequest.SelectPEDRequestListener;
import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApproveDTO;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveTableDTO;
import com.shaic.claim.pedrequest.view.ViewPEDRequestTable;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
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
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
@Alternative

public class PEDRequestDetailsTeamLeadViewImpl extends AbstractMVPView implements PEDRequestDetailsTeamLeadView, SelectPEDRequestListener  {
	
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
	
	private FormLayout dynamicLayout;
	
	private FormLayout ApprovedLayout;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private ComboBox txtPEDSuggestion;
	
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
	

//	@Inject
//	private PEDRequsteApproveTable pedRequestDetailsList;
	
	@Inject
	private PEDRequsteTeamLeadTable pedRequestDetailsList;

//	@Inject
//	private Instance<InitiatePEDEndorsementTable> initiatePEDEndorsementTable;
//	
//	private InitiatePEDEndorsementTable initiatePEDEndorsementTableInstance;
	
//	@Inject
//	private Instance<PEDRequestApproveEditableTable> initiatePEDEndorsementTable;
//	
//	private PEDRequestApproveEditableTable initiatePEDEndorsementObj;
	
//	@Inject
	private PEDRequestDetailsApproveDTO bean;
	
	private BeanFieldGroup<PEDRequestDetailsApproveDTO> binder;
	
	public String submitOption = "";
	
	private SearchPEDRequestApproveTableDTO searchDTO;
	
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private PreauthDTO preauthDTO = null;
	
	private RRCDTO rrcDTO;
	
	private Button btnLumen;
	
	@Inject
	private LumenDbService lumenService;
	
	@Inject
	private Instance<PopupInitiateLumenRequestWizardImpl> initiateLumenRequestWizardInstance;
	
	private PopupInitiateLumenRequestWizardImpl initiateLumenRequestWizardObj;
	
	
	public PEDRequestDetailsTeamLeadViewImpl(){
		
	}
	
	@PostConstruct
	public void initView()
	{ 
		
	}

	@SuppressWarnings("unchecked")
	public void initView(SearchPEDRequestApproveTableDTO searchDTO)
	{
		
		this.searchDTO=searchDTO;
		
		bean = new PEDRequestDetailsApproveDTO();
		
		if(this.searchDTO.getIsPolicyValidate()){		
			policyValidationPopupMessage();
		}
		
		
		fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PED_TL_SET_FIELD_DATA, searchDTO);
		
		submitOption = "";
		
		
		this.binder = new BeanFieldGroup<PEDRequestDetailsApproveDTO>(PEDRequestDetailsApproveDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.rrcDTO = searchDTO.getRrcDTO();
		this.rrcDTO.setStrUserName(searchDTO.getUsername());
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
		//preauthDTO.setStrUserName(searchDTO.getUsername());

//		fireViewEvent(PEDRequestDetailsApprovePresenter.SET_SECOND_TABLE, searchDTO);
		
		
//		HorizontalLayout IntimatePEDLayout=buildIntimatePEDFLayout(); 
//		PEDRequestDetailsTable pedRequestDetailsApproveListInstance = pedRequestDetailsList
//				.get();
//		pedRequestDetailsApproveListInstance.init("", false);
		pedRequestDetailsList.init("PED Request Details",false, false);
		//pedRequestDetailsList.setReference(new HashedMap());

		
		/*userName=(String)UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID);
	    passWord=(String)UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD);*/
		

		
		try{
			this.binder.commit();
		    this.bean = this.binder.getItemDataSource().getBean();
		}
		catch(CommitException e){
			e.printStackTrace();
		}
		Pageable pageable = pedRequestDetailsList.getPageable();
		this.bean.setPageable(pageable);
		fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PED_TL_SET_FIRST_TABLE, searchDTO);
		
		pedRequestDetailsList.setHeight("150px");
		
		//Removed the Processor Remarks -- By Raja on 5th July
	/*	TextArea txtProcessorRemarks =(TextArea) binder.buildAndBind("Processor Remarks", "processorRemarks", TextArea.class);
		txtProcessorRemarks.setNullRepresentation("");
		txtProcessorRemarks.setReadOnly(true);
		FormLayout formProcessorRemarks = new FormLayout(txtProcessorRemarks);
		formProcessorRemarks.setMargin(true);*/
		
		VerticalSplitPanel mainsplitPanel = new VerticalSplitPanel();
		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
	//	intimationDetailsCarousel.init(new NewIntimationDto(), "PED Request");
		intimationDetailsCarousel.init(this.intimationDto,this.claimDto, "Process PED Request", searchDTO.getDiagnosis());
		mainsplitPanel.setFirstComponent(intimationDetailsCarousel);
		
		viewDetails.initView(this.intimationDto.getIntimationId(), ViewLevels.INTIMATION, false,"Process PED Request");
		
		Panel tableVertical=new Panel(pedRequestDetailsList);
		tableVertical.setHeight("180px");

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
		
		HorizontalLayout escalateHLayout = new HorizontalLayout();
		if(this.bean.getEscalateRemarks() != null && ! this.bean.getEscalateRemarks().isEmpty())
		{	
		TextArea escalateremarks = new TextArea("Escalation Remarks");
		escalateremarks.setValue(this.bean.getEscalateRemarks());
		escalateremarks.setData(this.bean);
		escalateremarks.setReadOnly(true);
		escalateremarks.setDescription("Click the Text Box and Press F8 for Detail Popup");
		escalateremarks.setId("escRem");
		escalateremarks.setWidth("100%");
		escalateHLayout.addComponent(escalateremarks);
		handlePopupForEscalationRemarks(escalateremarks,null);
		}
		escalateHLayout.addComponent(insuredPedDetails);
		escalateHLayout.setSpacing(true);
	
//		escalateHLayout.setComponentAlignment(insuredPedDetails, Alignment.BOTTOM_RIGHT);
		escalateHLayout.setWidth("80%");
		
//		VerticalLayout verticalPanel = new VerticalLayout(hLyout,insuredPedDetails);
		VerticalLayout verticalPanel = new VerticalLayout(hLyout,escalateHLayout);
		verticalPanel.setSpacing(true);
		
		editPanel = new Panel();
		editPanel.setHeight("10%");
		//VerticalLayout verticalLayout = new VerticalLayout(viewDetails,pedRequestDetailsList,IntimatePEDLayout,initiatePEDEndorsementTableInstance,txtProcessorRemarks,buildMainLayout(),dynamicLayout,buttonHorLayout);
	//Removed the Processor Remarks -- By Raja on 5th July
		VerticalLayout verticalLayout = new VerticalLayout(verticalPanel,pedRequestDetailsList,editPanel,buildMainLayout(),dynamicLayout,buttonHorLayout);
		verticalLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
	//	verticalLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
		verticalLayout.setMargin(false);
		verticalLayout.setSpacing(true);
		//Vaadin8-setImmediate() verticalLayout.setImmediate(true);

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
	
	public  void handlePopupForEscalationRemarks(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForEscalateRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForEscalation(searchField, getShortCutListenerForEscalationRemarks(searchField));
	    
	  }
	
	public  void handleShortcutForEscalation(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);
				
			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});	
	}	
	private ShortcutListener getShortCutListenerForEscalationRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Escalation Remarks",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				PEDRequestDetailsApproveDTO  pedDTO = (PEDRequestDetailsApproveDTO) txtFld.getData();
				VerticalLayout vLayout =  new VerticalLayout();
				
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setNullRepresentation("");
				txtArea.setValue(txtFld.getValue());
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				
				if(("escRem").equalsIgnoreCase(txtFld.getId()))
				{
					txtArea.setRows(pedDTO.getEscalateRemarks() == null || pedDTO.getEscalateRemarks().length()/80 >= 25 ? 25 : ((pedDTO.getEscalateRemarks().length()/80)%25)+1);
					txtArea.setReadOnly(true);
				}
				else{
					txtArea.setRows(25);
					txtArea.setReadOnly(false);
				}
				
				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {

						if(!("escRem").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(txtArea.getValue());
						}
					}
				});
				
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();

				String caption = "";
				if(("escRem").equalsIgnoreCase(txtFld.getId())){
					caption = "Escalate Remarks";
				}
				else if(("watchRem").equalsIgnoreCase(txtFld.getId())){
					caption = "WatchList Remarks";
				}				
				else if(("refReason").equalsIgnoreCase(txtFld.getId())){
					caption = "Reason For Referring";
				}
				else if(("queryRem").equalsIgnoreCase(txtFld.getId())){
					caption = "Query Remarks";
				}
				else if(("rejRem").equalsIgnoreCase(txtFld.getId())){
					caption = "Rejection Remarks";
				}
				else if(("approveRem").equalsIgnoreCase(txtFld.getId())){
					caption = "Approval Remarks";
				}
				dialog.setCaption(caption);
				
				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.setDraggable(true);
				
				dialog.addCloseListener(new Window.CloseListener() {

					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				
				okBtn.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
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
		
		btnLumen = new Button("Initiate Lumen");		
		btnLumen.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				invokePreMedicalLumenRequest();
			}
		});
		
		
		Button elearnBtn = viewDetails.getElearnButton();
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(btnRRC,btnLumen,elearnBtn);
		alignmentHLayout.setSpacing(true);
		
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(PEDRequestDetailsTeamLeadPresenter.VALIDATE_PED_TL_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PED_TEAM_LEAD);
			
			
			
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
				fireViewEvent(PEDRequestDetailsTeamLeadPresenter.INITIATE_PED_TEAM_LEAD_USER_LUMEN_REQUEST, intimationDto);
			}else{
				showErrorMessage("Claim is settled, lumen cannot be initiated");
				return;
			}
		}else{
			showErrorMessage("Claim is closed, lumen cannot be initiated");
			return;
		}*/
		fireViewEvent(PEDRequestDetailsTeamLeadPresenter.INITIATE_PED_TEAM_LEAD_USER_LUMEN_REQUEST, intimationDto);
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
		initiateLumenRequestWizardObj.initView(resultObj, popup, "PED TL");
		
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
					                	fireViewEvent(MenuItemBean.PROCESS_PED_REQUEST_TL_APPROVE, true);
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
					if (submitOption.equals("query")) {
						bean.setQueryRemarks(txtQueryRemarkProcesser.getValue());

						fireViewEvent(
								PEDRequestDetailsTeamLeadPresenter.PED_TL_QUERY_REMARKS,
								bean,searchDTO);

					} else if (submitOption.equals("refer to Specialist")) {
						
						SelectValue selected=new SelectValue();
						selected=(SelectValue)cmbSpecialistType.getValue();
						bean.setSpecialistType(selected);
						bean.setReasonforReferring(txtReasonforReferring.getValue());
						
						fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PED_TL_SPECIALIST_REMARKS,bean,searchDTO);

					} else if (submitOption.equals("rejection")) {

						bean.setRejectionRemarks(txtReviewRemarks.getValue());
						fireViewEvent(
								PEDRequestDetailsTeamLeadPresenter.PED_TL_REJECTION_REMARKS,
								bean,searchDTO);

					} else if (submitOption.equals("approved")) {

						bean.setApprovalRemarks(txtApproveRemark.getValue());
						fireViewEvent(
								PEDRequestDetailsTeamLeadPresenter.PED_TL_APPROVE_REMARKS,
								bean,searchDTO);

					} else if (submitOption.equals("watchlist")) {
						
						//SelectValue selected=new SelectValue();
//						selected=(SelectValue)cmbWatchList.getValue();
//						bean.setSpecialistType(selected);
						bean.setWatchlistRemarks(txtWatchListRemarks.getValue());
						fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PED_TL_WATCHLIST_REMARKS, bean, searchDTO);
					}
					
				}

			}
		});
		         
	}
	
	public void addListenerForPED(){
		
		 txtPEDSuggestion.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					ComboBox suggestionCmb = (ComboBox)event.getProperty();
					Long intimationKey = (Long) suggestionCmb.getData();
					
					SelectValue suggestionSelect = suggestionCmb.getValue() != null ? (SelectValue)suggestionCmb.getValue() : null;
					if(suggestionSelect != null && suggestionSelect.getId() != null){
					
						if (txtPEDSuggestion.getValue().toString().toLowerCase()
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

						fireViewEvent(PEDRequestDetailsTeamLeadPresenter.TL_GET_PED_ALREADY_AVAILABLE, suggestionSelect.getId(), intimationKey);
						
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

	public void editPanelForPED(PEDRequestDetailsApproveDTO bean) {
		
		this.bean.setPedApprovalName(bean.getPedApprovalName());
		
		this.bean.setPedSuggestion(bean.getPedSuggestion());
		
		this.bean.setRemarks(bean.getRemarks());
		this.bean.setRepudiationLetterDate(bean.getRepudiationLetterDate());
		
		this.bean.setPedInitiateDetails(bean.getPedInitiateDetails());
		
		this.bean.setIsEditPED(bean.getIsEditPED());
		

		txtPEDSuggestion =(ComboBox) binder.buildAndBind("PED Suggestion","pedSuggestion", ComboBox.class);
		
		txtPEDSuggestion.setContainerDataSource(selectValueContainer);
		txtPEDSuggestion.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		txtPEDSuggestion.setItemCaptionPropertyId("value");
		txtPEDSuggestion.setNullSelectionAllowed(false);
		txtPEDSuggestion.setData(this.bean.getIntimationKey());
		
		txtPEDSuggestion.setRequired(true);
		
		mandatoryFields.add(txtPEDSuggestion);
		
		txtPEDSuggestion.setValue(this.bean.getPedSuggestion());
		
		//txtPEDSuggestion =(ComboBox) binder.buildAndBind("PED Suggestion","approvalPedSuggestion", ComboBox.class);
		TextField txtNameofPED =(TextField) binder.buildAndBind("Name of PED", "pedApprovalName", TextField.class);
		txtNameofPED.setRequired(true);
		txtNameofPED.setNullRepresentation("");
		
		txtNameofPED.setRequired(true);
		mandatoryFields.add(txtNameofPED);

		TextField txtRemarks =(TextField) binder.buildAndBind("Remarks", "remarks", TextField.class);
		txtRemarks.setRequired(true);
		txtRemarks.setNullRepresentation("");
		
		txtRemarks.setRequired(true);
		mandatoryFields.add(txtRemarks);

//		txtRepudiationLetterDate =(DateField) binder.buildAndBind("Repudiation Letter Date", "repudiationLetterDate", DateField.class);
//		txtRepudiationLetterDate.setRequired(true);
		
		firstForm = new FormLayout(txtPEDSuggestion,txtNameofPED,txtRemarks);
		firstForm.setSpacing(true);
		
		BeanItemContainer<SelectValue> selectIcdChapterContainer=masterService.getSelectValuesForICDChapter();
		
		BeanItemContainer<SelectValue> selectPedCodeContainer=masterService.getPedDescription();
		
		BeanItemContainer<SelectValue> selectSourceContainer=masterService.getSelectValueContainer(ReferenceTable.PED_SOURCE);
		//referenceData.put("icdCode", selectValueContainer);
		referenceData.put("icdChapter",selectIcdChapterContainer);
		referenceData.put("pedCode", selectPedCodeContainer);
		referenceData.put("source", selectSourceContainer);
		
		
		initiatePEDEndorsementObj = initiatePEDEndorsementTable.get();
		initiatePEDEndorsementObj.init(SHAConstants.PED_TEAM_LEAD);
		initiatePEDEndorsementObj.setReferenceData(referenceData);
		initiatePEDEndorsementObj.setIntimationKey(this.bean.getIntimationKey());
		
		List<ViewPEDTableDTO> pedInitiateDetails = this.bean.getPedInitiateDetails();
		for (ViewPEDTableDTO viewPEDTableDTO : pedInitiateDetails) {
			initiatePEDEndorsementObj.addBeanToList(viewPEDTableDTO);
		}
		
		
//		if(txtPEDSuggestion.getValue().toString().toLowerCase()
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

		mainLayout.addComponent(buildButtonHLayout());
		mainLayout.setComponentAlignment(buttonHLayout, new Alignment(35));

		return mainLayout;
	}

	private HorizontalLayout buildButtonHLayout() {

		queryBtn = new Button("Query");
		queryBtn.addStyleName("querybtn");
		queryBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {
				submitOption="query";
				
				fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PED_TL_QUERY_BUTTON, true);

			}
		});
		
		watchListBtn = new Button("Add to WatchList");
		watchListBtn.addStyleName("querybtn");
		watchListBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				submitOption="watchlist";
				
				fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PED_TL_WATCH_BUTTON,true);
				
			}
		});
		
		refertoSpecialistBtn = new Button("Refer to Specialist");
		refertoSpecialistBtn.addStyleName("querybtn");
		
		//Vaadin8-setImmediate() refertoSpecialistBtn.setImmediate(true);
		
		refertoSpecialistBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				submitOption="refer to Specialist";
				fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PED_TL_REFER_BUTTON, true);
			}
		});

		rejectBtn = new Button("Reject");
		rejectBtn.addStyleName("querybtn");
		rejectBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {
				submitOption="rejection";
				
				fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PED_TL_REJECT_BUTTON, true);
			}
		});
		approvedBtn = new Button("Approve");
		approvedBtn.addStyleName("querybtn");
		approvedBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				submitOption="approved";
				fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PED_TL_APPROVE_BUTTON, true);
			}
		});
		
		//REF701 - Disable Query option in both PED Processor and Approver Screen - - 20/02/2017
		queryBtn.setEnabled(false);
		
		buttonHLayout = new HorizontalLayout(watchListBtn, queryBtn, refertoSpecialistBtn,
					rejectBtn, approvedBtn);
		//buttonHLayout.setWidth("570px");
		buttonHLayout.setMargin(true);
		buttonHLayout.setSpacing(true);
		
		if(this.bean.getIsAlreadyWatchList()){
			watchListBtn.setEnabled(false);
		}else{
			watchListBtn.setEnabled(true);
		}
		
		return buttonHLayout;
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

	@Override
	public void generateFieldBasedOnQueryClick(Boolean isChecked) {
		
		
		unbindField(txtQueryRemarkProcesser);
		unbindField(cmbSpecialistType);
		unbindField(txtReasonforReferring);
		unbindField(txtApproveRemark);
		unbindField(txtReviewRemarks);
//		unbindField(cmbWatchList);
		unbindField(txtWatchListRemarks);
		
//		unbindField(fileUpload);
		
		mandatoryFields.remove(txtQueryRemarkProcesser);
		mandatoryFields.remove(cmbSpecialistType);
		mandatoryFields.remove(txtReasonforReferring);
		mandatoryFields.remove(txtApproveRemark);
		mandatoryFields.remove(txtReviewRemarks);
//		mandatoryFields.remove(cmbWatchList);
		mandatoryFields.remove(txtWatchListRemarks);
		
		
		txtQueryRemarkProcesser = (TextArea) binder.buildAndBind(
				"Query Remark(Approver)", "queryRemarks", TextArea.class);
		//Vaadin8-setImmediate() txtQueryRemarkProcesser.setImmediate(true);
		txtQueryRemarkProcesser.setMaxLength(2000);
		txtQueryRemarkProcesser.setWidth("400px");
		txtQueryRemarkProcesser.setId("queryRem");
		txtQueryRemarkProcesser.setData(bean);
		txtQueryRemarkProcesser.setDescription("Click the Text Box and Press F8 for Detail Popup");
		handlePopupForEscalationRemarks(txtQueryRemarkProcesser,null);
//		CSValidator validator = new CSValidator();
//		validator.extend(txtQueryRemarkProcesser);
//		validator.setRegExp("^[a-zA-Z 0-9.]*$");
//		validator.setPreventInvalidTyping(true);
	
		queryFLayout = new FormLayout(txtQueryRemarkProcesser);
		queryFLayout.setSpacing(true);
		
		mandatoryFields.add(txtQueryRemarkProcesser);
		showOrHideValidation(false);
		
		dynamicLayout.removeAllComponents();
		dynamicLayout.addComponent(queryFLayout);
	}
	
	@Override
	public void generateFieldBasedOnWatchClick(Boolean isChecked) {
		
		
		unbindField(txtQueryRemarkProcesser);
		unbindField(cmbSpecialistType);
		unbindField(txtReasonforReferring);
		unbindField(txtApproveRemark);
		unbindField(txtReviewRemarks);
//		unbindField(cmbWatchList);
		unbindField(txtWatchListRemarks);
		
//		unbindField(fileUpload);
		
		mandatoryFields.remove(txtQueryRemarkProcesser);
		mandatoryFields.remove(cmbSpecialistType);
		mandatoryFields.remove(txtReasonforReferring);
		mandatoryFields.remove(txtApproveRemark);
		mandatoryFields.remove(txtReviewRemarks);
//		mandatoryFields.remove(cmbWatchList);
		mandatoryFields.remove(txtWatchListRemarks);
		
		
		txtWatchListRemarks = (TextArea) binder.buildAndBind(
				"WatchList Remarks", "watchlistRemarks", TextArea.class);
		//Vaadin8-setImmediate() txtWatchListRemarks.setImmediate(true);
		txtWatchListRemarks.setMaxLength(2000);
		txtWatchListRemarks.setWidth("400px");
		txtWatchListRemarks.setRequired(true);
		txtWatchListRemarks.setId("watchRem");
		txtWatchListRemarks.setData(bean);
		txtWatchListRemarks.setDescription("Click the Text Box and Press F8 for Detail Popup");
		handlePopupForEscalationRemarks(txtWatchListRemarks,null);
//		CSValidator validator = new CSValidator();
//		validator.extend(txtQueryRemarkProcesser);
//		validator.setRegExp("^[a-zA-Z 0-9.]*$");
//		validator.setPreventInvalidTyping(true);
		
//		cmbWatchList = (ComboBox) binder.buildAndBind("Add to WatchList",
//				"watchList", ComboBox.class);
//		cmbWatchList.setRequired(true);
//		
//		cmbWatchList.setContainerDataSource(masterService.getSelectValueContainer(ReferenceTable.PED_SOURCE));
//		cmbWatchList.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cmbWatchList.setItemCaptionPropertyId("value");
	
		queryFLayout = new FormLayout(/*cmbWatchList,*/txtWatchListRemarks);
		queryFLayout.setSpacing(true);
		
		mandatoryFields.add(txtWatchListRemarks);
		showOrHideValidation(false);
		
		dynamicLayout.removeAllComponents();
		dynamicLayout.addComponent(queryFLayout);
	
		
	}
	
	@Override
	public void generateFieldBasedOnReferClick(Boolean isChecked) {
		
		unbindField(txtQueryRemarkProcesser);
		unbindField(cmbSpecialistType);
		unbindField(txtReasonforReferring);
		unbindField(txtApproveRemark);
		unbindField(txtReviewRemarks);
//		unbindField(cmbWatchList);
		unbindField(txtWatchListRemarks);
		
		mandatoryFields.remove(txtQueryRemarkProcesser);
		mandatoryFields.remove(cmbSpecialistType);
		mandatoryFields.remove(txtReasonforReferring);
		mandatoryFields.remove(txtApproveRemark);
		mandatoryFields.remove(txtReviewRemarks);
//		mandatoryFields.remove(cmbWatchList);
		mandatoryFields.remove(txtWatchListRemarks);
		

		cmbSpecialistType = (ComboBox) binder.buildAndBind("Specialist Type",
				"specialistType", ComboBox.class);
		cmbSpecialistType.setRequired(true);
		
		cmbSpecialistType.setContainerDataSource(masterService.getConversionReasonByValue(ReferenceTable.SPECIALIST_TYPE));
		cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSpecialistType.setItemCaptionPropertyId("value");
		
		fileUpload  = new Upload("", new Receiver() {
			
			private static final long serialVersionUID = 4775959511314943621L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				// Create upload stream
		        FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
		            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
		            fos = new FileOutputStream(file);
//		        	if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
//		        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, this.key,filename);
//		        	}
		        } catch (final java.io.FileNotFoundException e) {
		            new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
		                .show(com.vaadin.server.Page.getCurrent());
		            return null;
		        }
		        return fos; // Return the output stream to write to
			}
		});	
		fileUpload.addSucceededListener(new SucceededListener() {
			
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				System.out.println("File uploaded" + event.getFilename());
				
				try{
					
					byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
					
					if(null != fileAsbyteArray )
					{
						
						//Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
						//boolean hasSpecialChar = p.matcher(event.getFilename()).find();
					//	if(hasSpecialChar)
						//{
						WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
							Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
							//TO read file after load
							if (flagUploadSuccess.booleanValue())
							{
								String token = "" + uploadStatus.get("fileKey");
								String fileName = event.getFilename();
								bean.setTokenName(token);
								bean.setFileName(fileName);
								tmpViewBtn.setEnabled(true);
								buildSuccessLayout();
//							    thisObj.close();
							}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		fileUpload.setCaption("File Upload");
		fileUpload.setButtonCaption(null);
		
		
		txtReasonforReferring = (TextArea) binder.buildAndBind(
				"Reason for Referring (Approver)", "reasonforReferring",
				TextArea.class);
		txtReasonforReferring.setRequired(true);
		txtReasonforReferring.setMaxLength(2000);
		txtReasonforReferring.setWidth("400px");
		txtReasonforReferring.setId("refReason");
		txtReasonforReferring.setData(bean);
		txtReasonforReferring.setDescription("Click thes Text Box and Press F8 for Detail Popup");
		handlePopupForEscalationRemarks(txtReasonforReferring, null);
		
		
//		CSValidator validator = new CSValidator();
//		validator.extend(txtReasonforReferring);
//		validator.setRegExp("^[a-zA-Z 0-9.]*$");
//		validator.setPreventInvalidTyping(true);
		
		Button uploadBtn = new Button("Upload");
		
		uploadBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				fileUpload.submitUpload();
			}
		});
		
		
		
		
//		referToSpecialistFLayout = new FormLayout(cmbSpecialistType,
//				fileUpload, txtReasonforReferring);
//		referToSpecialistFLayout.setSpacing(true);
//		
//		mandatoryFields.add(cmbSpecialistType);
//		mandatoryFields.add(txtReasonforReferring);
//		
//		showOrHideValidation(false);
//		
//		dynamicLayout.removeAllComponents();
//		dynamicLayout.addComponent(referToSpecialistFLayout);
		
		TextField dummyText = new TextField();
		dummyText.setEnabled(false);
		dummyText.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyText.setHeight("100%");
		
		tmpViewBtn = new Button("View File");
	    tmpViewBtn.setEnabled(false);
	    tmpViewBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
	    tmpViewBtn.addStyleName(ValoTheme.BUTTON_LINK);
	    tmpViewBtn.setWidth("50%");
	    
        tmpViewBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getFileName() != null && bean.getTokenName() != null){
					Window popup = new com.vaadin.ui.Window();
					popup.setWidth("75%");
					popup.setHeight("90%");
					fileViewUI.init(popup,bean.getFileName(), bean.getTokenName());
					popup.setContent(fileViewUI);
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
		   });
		
        TextField dummyText3 = new TextField();
        dummyText3.setEnabled(false);
        dummyText3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        dummyText3.setHeight("100%");
		
		FormLayout form2 = new FormLayout(dummyText,uploadBtn);
		FormLayout form3 = new FormLayout(dummyText3,tmpViewBtn);
		
		HorizontalLayout secondHor = new HorizontalLayout(form2,form3);
//		secondHor.setComponentAlignment(tmpViewBtn, Alignment.MIDDLE_RIGHT);
		secondHor.setWidth("40%");
		referToSpecialistFLayout = new FormLayout(cmbSpecialistType,
				fileUpload, txtReasonforReferring);
//		referToSpecialistFLayout.setComponentAlignment(uploadHor, Alignment.MIDDLE_LEFT);
		referToSpecialistFLayout.setSpacing(true);
		
		HorizontalLayout mainHor = new HorizontalLayout(referToSpecialistFLayout,secondHor);
		mainHor.setWidth("90%");
		
		mandatoryFields.add(cmbSpecialistType);
		mandatoryFields.add(txtReasonforReferring);
		
		showOrHideValidation(false);
		
		dynamicLayout.removeAllComponents();
		dynamicLayout.addComponent(mainHor);
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
	public void generateFieldBasedOnApproveClick(Boolean isChecked) {
		
		unbindField(txtQueryRemarkProcesser);
		unbindField(cmbSpecialistType);
		unbindField(txtReasonforReferring);
		unbindField(txtApproveRemark);
		unbindField(txtReviewRemarks);
//		unbindField(cmbWatchList);
		unbindField(txtWatchListRemarks);
		
		mandatoryFields.remove(txtQueryRemarkProcesser);
		mandatoryFields.remove(cmbSpecialistType);
		mandatoryFields.remove(txtReasonforReferring);
		mandatoryFields.remove(txtApproveRemark);
		mandatoryFields.remove(txtReviewRemarks);
//		mandatoryFields.remove(cmbWatchList);
		mandatoryFields.remove(txtWatchListRemarks);

		
		txtApproveRemark = (TextArea) binder.buildAndBind(
				"Approval Remarks", "approvalRemarks", TextArea.class);
		//Vaadin8-setImmediate() txtApproveRemark.setImmediate(true);
		txtApproveRemark.setMaxLength(2000);
		txtApproveRemark.setWidth("400px");
		txtApproveRemark.setId("approveRem");
		txtApproveRemark.setData(bean);
		txtApproveRemark.setDescription("Click the Text Box and Press F8 for Detail Popup");
		handlePopupForEscalationRemarks(txtApproveRemark,null);
//		CSValidator validator = new CSValidator();
//		validator.extend(txtApproveRemark);
//		validator.setRegExp("^[a-zA-Z 0-9.]*$");
//		validator.setPreventInvalidTyping(true);
		
		
		sendtoApproverFLayout = new FormLayout(txtApproveRemark);
		sendtoApproverFLayout.setSpacing(true);
		
		mandatoryFields.add(txtApproveRemark);
		showOrHideValidation(false);
		
		dynamicLayout.removeAllComponents();
		dynamicLayout.addComponent(sendtoApproverFLayout);
		dynamicLayout.setComponentAlignment(sendtoApproverFLayout, Alignment.MIDDLE_RIGHT);
		
	}

	@Override
	public void generateFieldBasedOnRejectClick(Boolean isChecked) {
		
		unbindField(txtQueryRemarkProcesser);
		unbindField(cmbSpecialistType);
		unbindField(txtReasonforReferring);
		unbindField(txtApproveRemark);
		unbindField(txtReviewRemarks);
//		unbindField(cmbWatchList);
		unbindField(txtWatchListRemarks);
		
		mandatoryFields.remove(txtQueryRemarkProcesser);
		mandatoryFields.remove(cmbSpecialistType);
		mandatoryFields.remove(txtReasonforReferring);
		mandatoryFields.remove(txtApproveRemark);
		mandatoryFields.remove(txtReviewRemarks);
//		mandatoryFields.remove(cmbWatchList);
		mandatoryFields.remove(txtWatchListRemarks);

		
		
		txtReviewRemarks = (TextArea) binder.buildAndBind("Rejection Remarks",
				"rejectionRemarks", TextArea.class);
		//Vaadin8-setImmediate() txtReviewRemarks.setImmediate(true);
		txtReviewRemarks.setMaxLength(2000);
		txtReviewRemarks.setWidth("400px");
		txtReviewRemarks.setId("rejRem");
		txtReviewRemarks.setData(bean);
		txtReviewRemarks.setDescription("Click the Text Box and Press F8 for Detail Popup");
		handlePopupForEscalationRemarks(txtReviewRemarks,null);
//		CSValidator validator = new CSValidator();
//		validator.extend(txtReviewRemarks);
//		validator.setRegExp("^[a-zA-Z 0-9.]*$");
//		validator.setPreventInvalidTyping(true);
		
		
		
		mandatoryFields.add(txtReviewRemarks);
		
		ApprovedLayout = new FormLayout(txtReviewRemarks);
		ApprovedLayout.setSpacing(true);
		
		showOrHideValidation(false);
		
		
		dynamicLayout.removeAllComponents();
		dynamicLayout.addComponent(ApprovedLayout);
	}

	@Override
	public void list(Page<OldPedEndorsementDTO> tableRows) {
		
        pedRequestDetailsList.setTableList(tableRows.getPageItems());
        pedRequestDetailsList.setRowColor(searchDTO.getKey());
	}

	@Override
	public void setReferenceData(PEDRequestDetailsApproveDTO bean,BeanItemContainer<SelectValue> selectValueContainer,NewIntimationDto intimationDto,ClaimDto claimDto) {
		this.bean=bean;
		this.intimationDto=intimationDto;
		this.selectValueContainer=selectValueContainer;
		this.claimDto=claimDto;
		
	}

	@Override
	public void result(Boolean result) {
		
		String results=null;
		
		if(submitOption.equals("query")){
			results="Query Remarks Submitted Successfully";
		}
		else if(submitOption.equals("refer to Specialist")){
			results="Refer to Specialist Submitted Successfully";
		}
		else if(submitOption.equals("rejection")){
			results="PED Request Rejected Successfully";
		}
		else if(submitOption.equals("approved")){
			results="PED Request Approved Successfully";
		}
		else if(submitOption.equals("watchlist")){
			results="WatchList Approved Successfully";
		}
		
//		ConfirmDialog dialog = ConfirmDialog.show(getUI(),results, new ConfirmDialog.Listener() {
//
//            public void onClose(ConfirmDialog dialog) {
//                if (dialog.isConfirmed()) {
//                		fireViewEvent(MenuItemBean.PROCESS_PED_REQUEST_APPROVE, true);
//                } else {
//                    dialog.close();
//                }
//            }
//        });
//		dialog.setStyleName(Reindeer.WINDOW_BLACK);

        Label successLabel = new Label("<b style = 'color: black;'>"+results+" </b>", ContentMode.HTML);
		
		Button homeButton = new Button("PED Request Home");
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
				fireViewEvent(MenuItemBean.PROCESS_PED_REQUEST_TL_APPROVE, true);
				
			}
		});

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
		table.addContainerProperty("pedEffectiveFromDate",  Date.class, null);
		table.setCaption("PED Details");
		
		int i=0;
//		table.setStyleName(ValoTheme.TABLE_NO_HEADER);
		if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty()) {
			for (InsuredPedDetails pedDetails : this.bean.getInsuredPedDetails()) {
				if(pedDetails.getPedDescription() != null /*&& !("NIL").equalsIgnoreCase(pedDetails.getPedDescription())*/){
				table.addItem(new Object[]{pedDetails.getPedCode(), pedDetails.getPedDescription(),pedDetails.getPedEffectiveFromDate()}, i+1);
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
		table.setColumnHeader("pedEffectiveFromDate", "PED Effective from date");
		table.setColumnWidth("pedCode", 100);
		table.setColumnWidth("pedDescription", 280);
		table.setColumnWidth("pedEffectiveFromDate", 200);
		
		return table;
		
//		Panel tablePanel = new Panel(table);
//		return tablePanel;
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		if( submitOption != null && submitOption.equalsIgnoreCase("")){
			eMsg.append("Please select Query or Specialist or Approver </br>");
			hasError = true;
		}
		
		
		if(submitOption != null && submitOption.equals("watchlist")) {
//			SelectValue selected=new SelectValue();
//			selected=(SelectValue)cmbWatchList.getValue();
//			
//			if(selected != null) {
//			String selectValue = selected.toString();
//			
//			if(selectValue !=null && selectValue.equals("Others")) 
//			{
//				eMsg += "Please Enter Watchlist Remarks";
//				hasError = true;
//			}
//			}
		}
		
		
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
		if(txtPEDSuggestion != null && txtPEDSuggestion.getValue() != null && ((SelectValue)txtPEDSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG004)){
			isSumInsuredLocked = Boolean.TRUE;
		}
		
		if(initiatePEDEndorsementObj != null && ! initiatePEDEndorsementObj.isValid(isSumInsuredLocked)){
			List<String> errors = initiatePEDEndorsementObj.getErrors();
			for (String string : errors) {
				eMsg.append(string).append("</br>");
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

	/* private boolean emptyStringValidate(){
		   StringBuffer eMsg = new StringBuffer();
		   Boolean hasError=false;
		   if(!mandatoryFields.isEmpty()){
			   for(int i=0;i<mandatoryFields.size();i++){
				   AbstractField<?> field=(AbstractField<?>)mandatoryFields.get(i);
				   if(("").equals(field.getValue())){
					   setRequired(true);
					   ErrorMessage errMsg = ((AbstractField<?>) field)
								.getErrorMessage();
						if (errMsg != null) {
							eMsg.append(errMsg.getFormattedHtmlMessage());
						}
						Label label = new Label("Please Enter " +field.getCaption(), ContentMode.HTML);
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
						hasError=true;
						
				   }
			   }
			   if(hasError){
				   return false;
			   }
			   else{
				   showOrHideValidation(true);
				   return true;
			   }
		   }
		return false;
	   }*/

	@Override
	public void showEditPanel(PEDRequestDetailsApproveDTO bean) {
		
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
	public void setPEDCode(String pedCode) {
		
			initiatePEDEndorsementObj.setPEDCode(pedCode);		
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
			txtPEDSuggestion.setValue(null);
		}
		
	}	
	
	@Override
	public void resetPEDDetailsTable(ViewPEDTableDTO newInitiatePedDTO){
		initiatePEDEndorsementObj.resetPEDDetailsTable(newInitiatePedDTO);
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
