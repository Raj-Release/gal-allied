package com.shaic.claim.pedrequest.process;

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
import com.shaic.claim.pedrequest.SelectPEDRequestListener;
import com.shaic.claim.pedrequest.process.search.SearchPEDRequestProcessTableDTO;
import com.shaic.claim.pedrequest.view.ViewPEDRequestTable;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
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
public class PEDRequestDetailsProcessViewImpl extends AbstractMVPView implements
		PEDRequestDetailsProcessView, SelectPEDRequestListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2708366675144306182L;
	private VerticalLayout mainLayout;
	
	@EJB
	private MasterService masterService;
	
	private FormLayout sendtoApproverFLayout;
	
	private FormLayout watchListLayout;
	
	private TextArea txtReviewRemarks;
	
	private FormLayout referToSpecialistFLayout;
	
	private TextArea txtReasonforReferring;
	
	private TextArea txtWatchListRemarks;
	
	private Upload fileUpload;
	
	private ComboBox cmbSpecialistType;
	
//	private ComboBox cmbAddtoWatchList;
	
	private FormLayout dynamicLayout;
	
	//private FormLayout queryFLayout;
	
	@Inject
	private ViewDetails viewDetails;
	
	private ClaimDto claimDto;
	
	private TextArea txtQueryRemarkProcesser;
	
	private HorizontalLayout buttonHLayout;
	
	private Button sendtoApproverBtn;
	
	private Button addtoWatchListBtn;
	
	private Button refertoSpecialistBtn;
	
	private Button queryBtn;
	
	//private FormLayout queryLayout;
	
	//private FormLayout referLayout;
	
	//private FormLayout approveLayout;
	
	private NewIntimationDto intimationDto;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	public String buttonClicked = "";
	
	private HorizontalLayout buttonHorLayout;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private SearchPEDRequestProcessTableDTO searchDTO;

	@Inject
	private PEDRequestProcessTable pedRequestDetailsProcessList;

//	@Inject
//	private PEDRequestDetailsTable pedRequestDetailsProcessList;
	
//	@Inject
	private PEDRequestDetailsProcessDTO bean;

	private BeanFieldGroup<PEDRequestDetailsProcessDTO> binder;

	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	private String userName;
	
	private String passWord;
	
	private File file;
	
	private Button tmpViewBtn;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private PreauthDTO preauthDTO = null;
	
	private RRCDTO rrcDTO;
	
	private ComboBox cmbPEDSuggestion;
	
	private TextField txtPedName;
	
	private TextField txtPedRemarks;
	
	private DateField txtRepudiationLetterDate;
	
	@Inject
	private Instance<ViewPEDRequestTable> initiatePEDEndorsementTable;
	
	private ViewPEDRequestTable initiatePEDEndorsementObj;
	
	private FormLayout firstForm;
	
    private Panel editPanel;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	@PostConstruct
	public void initView() {

	}

	@SuppressWarnings("unchecked")
	public void initView(SearchPEDRequestProcessTableDTO searchDTO) {
		// this.bean=bean;
		bean = new PEDRequestDetailsProcessDTO();
		
		this.searchDTO=searchDTO;
		buttonClicked = "";
		this.rrcDTO = searchDTO.getRrcDTO();
		fireViewEvent(PEDRequestDetailsProcessPresenter.SET_DATA_FIELD, searchDTO);
		
		this.binder = new BeanFieldGroup<PEDRequestDetailsProcessDTO>(
				PEDRequestDetailsProcessDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
		
		pedRequestDetailsProcessList.init("PED Request Details", false, false);
		//pedRequestDetailsProcessList.setReference(new HashedMap());

		pedRequestDetailsProcessList.setHeight("250px");
		
		try{
			this.binder.commit();
		    this.bean = this.binder.getItemDataSource().getBean();
		}
		catch(CommitException e){
			e.printStackTrace();
		}
		Pageable pageable = pedRequestDetailsProcessList.getPageable();
		this.bean.setPageable(pageable);
		fireViewEvent(PEDRequestDetailsProcessPresenter.SET_FIRST_TABLE, searchDTO);
		
//		pedRequestDetailsProcessList.init("", false);
//
//		pedRequestDetailsProcessList.setHeight("100%");
		
//		Panel listPanel=new Panel(pedRequestDetailsProcessList);
//		listPanel.setHeight("200px");

		VerticalSplitPanel mainsplitPanel = new VerticalSplitPanel();
		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
//	intimationDetailsCarousel.init(new NewIntimationDto(), "Process PED Request");
		intimationDetailsCarousel.init(this.intimationDto,this.claimDto, "Process PED Request", searchDTO.getDiagnosis());
		mainsplitPanel.setFirstComponent(intimationDetailsCarousel);
        dynamicLayout=new FormLayout();
        dynamicLayout.setMargin(false);
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
        buttonHorLayout.setMargin(false);
        
        userName=(String)UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID);
        passWord=(String)UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD);
        
        addListener();
        
        viewDetails.initView(this.intimationDto.getIntimationId(), ViewLevels.INTIMATION, false,"Process PED Request");
        
        Panel tableVertical=new Panel(pedRequestDetailsProcessList);
		tableVertical.setHeight("180px");
		
		Table insuredPedPanel = getInsuredPedDetailsPanel();
        
		/*VerticalLayout verticalLayout = new VerticalLayout(
				viewDetails,pedRequestDetailsProcessList, buildMainLayout(),dynamicLayout,buttonHorLayout);*/

		editPanel = new Panel();
		
		HorizontalLayout hLyout =new HorizontalLayout(commonButtonsLayout(),viewDetails);
		hLyout.setWidth("100%");
		hLyout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
		VerticalLayout verticalPanel = new VerticalLayout(hLyout,insuredPedPanel);
		verticalPanel.setSpacing(true);
		verticalPanel.setComponentAlignment(insuredPedPanel, Alignment.BOTTOM_RIGHT);
		
		VerticalLayout verticalLayout = new VerticalLayout(
				verticalPanel,pedRequestDetailsProcessList, editPanel,buildMainLayout(),dynamicLayout,buttonHorLayout);
		
		verticalLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		verticalLayout.setMargin(false);
		verticalLayout.setSpacing(false);

		mainsplitPanel.setSecondComponent(verticalLayout);

		mainsplitPanel.setSplitPosition(22, Unit.PERCENTAGE);
		setHeight("100%");
		mainsplitPanel.setSizeFull();
		mainsplitPanel.setHeight("650px");
		showOrHideValidation(false);
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
		mainLayout.setComponentAlignment(buttonHLayout, new Alignment(48));

		return mainLayout;
	}
	
	private Table getInsuredPedDetailsPanel(){
		
		
		Table table = new Table();
//		table.setWidth("10%");
		table.addContainerProperty("pedCode", String.class, null);
		table.addContainerProperty("pedDescription",  String.class, null);
		table.addContainerProperty("pedEffectiveFromDate",  Date.class, null);
		table.setCaption("PED Details");
		
		int i=0;
//		table.setStyleName(ValoTheme.TABLE_NO_HEADER);
		if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty()) {
			for (InsuredPedDetails pedDetails : this.bean.getInsuredPedDetails()) {
				
				table.addItem(new Object[]{pedDetails.getPedCode(), pedDetails.getPedDescription(),pedDetails.getPedEffectiveFromDate()}, i+1);
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
	
	private void editPanelForPED(PEDRequestDetailsProcessDTO editDTO){
		
		this.bean.setNameofPED(editDTO.getNameofPED());
		this.bean.setPedSuggestion(editDTO.getPedSuggestion());
		this.bean.setRemarks(editDTO.getRemarks());
		this.bean.setRepudiationLetterDate(editDTO.getRepudiationLetterDate());
		this.bean.setIsEditPED(editDTO.getIsEditPED());
		
		this.bean.setPedInitiateDetails(editDTO.getPedInitiateDetails());
		
		BeanItemContainer<SelectValue> selectValueContainer=masterService.getConversionReasonByValue(ReferenceTable.PED_SUGGESTION);
		
		cmbPEDSuggestion =(ComboBox) binder.buildAndBind("PED Suggestion","pedSuggestion", ComboBox.class);
		
		cmbPEDSuggestion.setContainerDataSource(selectValueContainer);
		cmbPEDSuggestion.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPEDSuggestion.setItemCaptionPropertyId("value");
		cmbPEDSuggestion.setRequired(true);
		
		cmbPEDSuggestion.setNullSelectionAllowed(false);
		
		mandatoryFields.add(cmbPEDSuggestion);
		
		
		
		txtPedName = (TextField) binder.buildAndBind("Name of PED", "nameofPED", TextField.class);
		txtPedName.setRequired(true);
		
		mandatoryFields.add(txtPedName);
		
		txtPedRemarks = (TextField) binder.buildAndBind("Remarks", "remarks", TextField.class);
		
		txtPedRemarks.setRequired(true);
		mandatoryFields.add(txtPedRemarks);
		
//		txtRepudiationLetterDate = (DateField)binder.buildAndBind("Repudiation Letter Date", "repudiationLetterDate", DateField.class);
		
		firstForm = new FormLayout(cmbPEDSuggestion,txtPedName,txtPedRemarks);
		firstForm.setSpacing(true);
		
		BeanItemContainer<SelectValue> selectIcdChapterContainer=masterService.getSelectValuesForICDChapter();
		
		BeanItemContainer<SelectValue> selectPedCodeContainer=masterService.getPedDescription();
		
		BeanItemContainer<SelectValue> selectSourceContainer=masterService.getSelectValueContainer(ReferenceTable.PED_SOURCE);
		//referenceData.put("icdCode", selectValueContainer);
		referenceData.put("icdChapter",selectIcdChapterContainer);
		referenceData.put("pedCode", selectPedCodeContainer);
		referenceData.put("source", selectSourceContainer);
		
		initiatePEDEndorsementObj = initiatePEDEndorsementTable.get();
		initiatePEDEndorsementObj.init(SHAConstants.PED_PROCESSOR);
		initiatePEDEndorsementObj.setReferenceData(referenceData);
		
		List<ViewPEDTableDTO> pedInitiateDetails = this.bean.getPedInitiateDetails();
		for (ViewPEDTableDTO viewPEDTableDTO : pedInitiateDetails) {
			initiatePEDEndorsementObj.addBeanToList(viewPEDTableDTO);
		}
		
	
		
		VerticalLayout mainVertical = new VerticalLayout(firstForm,initiatePEDEndorsementObj);
		mainVertical.setComponentAlignment(firstForm, Alignment.TOP_RIGHT);
		
		
		Panel mainPanel = new Panel(mainVertical);
		
		editPanel.setContent(mainPanel);
		
		showOrHideValidation(false);
		
		addListenerForEdit();
		
		cmbPEDSuggestion.setValue(this.bean.getPedSuggestion());
		
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
		
		Button elearnBtn = viewDetails.getElearnButton();
				
		HorizontalLayout alignmentHLayout = new HorizontalLayout(btnRRC,elearnBtn);
		
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(PEDRequestDetailsProcessPresenter.VALIDATE_PED_PROCESS_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PED_REQUEST_PROCESS);
			
			
			
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
									//	SHAUtils.releaseHumanTask(searchDTO.getUsername(), searchDTO.getPassword(), searchDTO.getTaskNumber(),session);
					                	releaseHumanTask();
					                	fireViewEvent(MenuItemBean.PROCESS_PED_REQUEST_PROCESS, true);
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
				if(validatePage()){
				if(buttonClicked.equals("query")){
					
//					if (validatePage()) {
//						if(emptyStringValidate()){
						//bean.setKey(27l);
						bean.setQueryRemarks(txtQueryRemarkProcesser.getValue());
						
						fireViewEvent(
								PEDRequestDetailsProcessPresenter.QUERY_REMARKS,
								bean,searchDTO,userName,passWord);
//						}
//					}
				}
				else if(buttonClicked.equals("refer to specialist")){
					
//					if(validatePage()){
//						if(emptyStringValidate()){
						SelectValue selected=new SelectValue();
						selected=(SelectValue)cmbSpecialistType.getValue();
						bean.setSpecialistType(selected);
						bean.setReasonforReferring(txtReasonforReferring.getValue());
						fireViewEvent(PEDRequestDetailsProcessPresenter.SPECIALIST_REMARKS, bean,searchDTO,userName,passWord);
//						}
				       
//					}
				}
				else if(buttonClicked.equals("send to approve")){
					
//					if (validatePage()) {
//						if(emptyStringValidate()){
						//bean.setKey(27l);
//						bean.setRemarks(txtReviewRemarks.getValue());
						bean.setApprovalRemarks(txtReviewRemarks.getValue());
						fireViewEvent(
								PEDRequestDetailsProcessPresenter.REVIEW_REMARKS,
								bean,searchDTO,userName,passWord);
//						}
//					}
				}
				else if(buttonClicked.equals("watchlist")){
					
//					if(emptyStringValidate()){
						//SelectValue selected=new SelectValue();

						bean.setWatchListRemarks(txtWatchListRemarks.getValue());
						fireViewEvent(PEDRequestDetailsProcessPresenter.WATCH_REMARKS, bean,searchDTO,userName,passWord);
//						}
				}
				
			}
			}
		});

       
	}
	
	private void addListenerForEdit(){
		if(cmbPEDSuggestion != null){
			cmbPEDSuggestion.addValueChangeListener(new ValueChangeListener() {
	
				@Override
				public void valueChange(ValueChangeEvent event) {
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
				}
			});
		}
	}

	private HorizontalLayout buildButtonHLayout() {

		queryBtn = new Button("Query");
		queryBtn.addStyleName("querybtn");
		queryBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				buttonClicked="query";
				fireViewEvent(PEDRequestDetailsProcessPresenter.QUERY_BUTTON, true);

			}
		});

		refertoSpecialistBtn = new Button("Refer to Specialist");
		
		//REF702 - Enable Refer to Specialist option in both PED Processor and Approver Screen - - 20/02/2017
		refertoSpecialistBtn.setEnabled(true);
		
		refertoSpecialistBtn.addStyleName("querybtn");
		refertoSpecialistBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				buttonClicked="refer to specialist";

				fireViewEvent(PEDRequestDetailsProcessPresenter.REFER_BUTTON,true);
			}
		});

		sendtoApproverBtn = new Button("Send to Approver");
		sendtoApproverBtn.addStyleName("querybtn");
		sendtoApproverBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				buttonClicked="send to approve";
				
				fireViewEvent(PEDRequestDetailsProcessPresenter.APPROVE_BUTTON, true);
			}
		});
		
		addtoWatchListBtn = new Button("Add to WatchList");
		addtoWatchListBtn.addStyleName("querybtn");
		addtoWatchListBtn.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				buttonClicked="watchlist";
				
				fireViewEvent(PEDRequestDetailsProcessPresenter.WATCH_BUTTON, true);
			}
		});
		
		//REF701 - Disable Query option in both PED Processor and Approver Screen - - 20/02/2017
		queryBtn.setEnabled(false);
		
		buttonHLayout = new HorizontalLayout(addtoWatchListBtn,queryBtn, refertoSpecialistBtn,
				sendtoApproverBtn);
		//buttonHLayout.setWidth("570px");
//		buttonHLayout.setMargin(true);
		buttonHLayout.setSpacing(true);
		if(bean.getIsAlreadyWatchList()){
			addtoWatchListBtn.setEnabled(false);
		}else{
			addtoWatchListBtn.setEnabled(true);
		}
		return buttonHLayout;
	}

	private void unbindField(Field<?> field) {
		if (field != null) {
			if(binder.getPropertyId(field)!=null){
				this.binder.unbind(field);
			}
			
		}
	}

	@Override
	public void resetView() {
		

	}

	@Override
	public void tableRowSelectionListener(OldPedEndorsementDTO item) {

		System.out
				.println("--------------------- callledll----------------------"
						+ item.getPedName());
		bean.setNameofPED(item.getPedName());
		bean.setRemarks(item.getRemarks());
	}

	@Override
	public void generateFieldBasedOnQueryClick(Boolean isChecked) {
		
			
			unbindField(txtQueryRemarkProcesser);
	        unbindField(cmbSpecialistType);
	    	unbindField(txtReasonforReferring);
	    	unbindField(txtReviewRemarks);
//	    	unbindField(cmbAddtoWatchList);
	    	unbindField(txtWatchListRemarks);
	    	
	    	mandatoryFields.remove(txtQueryRemarkProcesser);
	    	mandatoryFields.remove(cmbSpecialistType);
	    	mandatoryFields.remove(txtReasonforReferring);
	    	mandatoryFields.remove(txtReviewRemarks);
//	    	mandatoryFields.remove(cmbAddtoWatchList);
	    	mandatoryFields.remove(txtWatchListRemarks);
	    	
		
		txtQueryRemarkProcesser =(TextArea) binder.buildAndBind("Query Remark(Processor)", "queryRemarks", TextArea.class);
		txtQueryRemarkProcesser.setRequired(true);
		txtQueryRemarkProcesser.setMaxLength(2000);
		txtQueryRemarkProcesser.setWidth("400px");
		txtQueryRemarkProcesser.setValue("");
//		CSValidator validator = new CSValidator();
//		validator.extend(txtQueryRemarkProcesser);
//		validator.setRegExp("^[a-zA-Z 0-9.]*$");
//		validator.setPreventInvalidTyping(true);
		
		mandatoryFields.add(txtQueryRemarkProcesser);
		
		showOrHideValidation(false);
		//queryFLayout = new FormLayout(txtQueryRemarkProcesser);
		dynamicLayout.removeAllComponents();
		dynamicLayout.addComponent(txtQueryRemarkProcesser);
		
		
	}
	
	@Override
	public void generateFieldBasedOnWatchClick(Boolean isChecked) {
			
			dynamicLayout.removeAllComponents();
			unbindField(txtQueryRemarkProcesser);
	        unbindField(cmbSpecialistType);
	    	unbindField(txtReasonforReferring);
	    	unbindField(txtReviewRemarks);
//	    	unbindField(cmbAddtoWatchList);
	    	unbindField(txtWatchListRemarks);
	    	
	    	mandatoryFields.remove(txtQueryRemarkProcesser);
	    	mandatoryFields.remove(cmbSpecialistType);
	    	mandatoryFields.remove(txtReasonforReferring);
	    	mandatoryFields.remove(txtReviewRemarks);
//	    	mandatoryFields.remove(cmbAddtoWatchList);
	    	mandatoryFields.remove(txtWatchListRemarks);
		
	    	
//	    	cmbAddtoWatchList = (ComboBox) binder.buildAndBind("Reason to Add to WatchList",
//					"watchListReason", ComboBox.class);
//	    	cmbAddtoWatchList.setRequired(true);
//	    	cmbAddtoWatchList.setContainerDataSource(masterService.getConversionReasonByValue(ReferenceTable.PED_SOURCE));
//	    	cmbAddtoWatchList.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//	    	cmbAddtoWatchList.setItemCaptionPropertyId("value");
			
		txtWatchListRemarks =(TextArea) binder.buildAndBind("Watchlist Remarks", "watchListRemarks", TextArea.class);
		txtWatchListRemarks.setMaxLength(2000);
		txtWatchListRemarks.setWidth("400px");
		txtWatchListRemarks.setValue("");
		txtWatchListRemarks.setRequired(true);
	//	CSValidator validator = new CSValidator();
	//	validator.extend(txtQueryRemarkProcesser);
	//	validator.setRegExp("^[a-zA-Z 0-9.]*$");
	//	validator.setPreventInvalidTyping(true);
		
		watchListLayout = new FormLayout(txtWatchListRemarks);
		watchListLayout.setSpacing(true);
		
		
		mandatoryFields.add(txtWatchListRemarks);
		
		showOrHideValidation(false);
		//queryFLayout = new FormLayout(txtQueryRemarkProcesser);
		dynamicLayout.addComponent(watchListLayout);
		
		
}

	@Override
	public void generateFieldBasedOnReferClick(Boolean isChecked) {
		
		unbindField(txtQueryRemarkProcesser);
		unbindField(txtReviewRemarks);
		unbindField(cmbSpecialistType);
		unbindField(txtReasonforReferring);
//		unbindField(cmbAddtoWatchList);
    	unbindField(txtWatchListRemarks);
		
		cmbSpecialistType = (ComboBox) binder.buildAndBind("Specialist Type",
				"specialistType", ComboBox.class);
		cmbSpecialistType.setRequired(true);
		
		cmbSpecialistType.setContainerDataSource(masterService.getConversionReasonByValue(ReferenceTable.SPECIALIST_TYPE));
		cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSpecialistType.setItemCaptionPropertyId("value");
//		fileUpload = (Upload) binder.buildAndBind("File Upload",
//				"uploadFile", Upload.class);
		
		fileUpload  = new Upload("", new Receiver() {
			
			private static final long serialVersionUID = 4775959511314943621L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				// Create upload stream
		        FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
//		        	if(filename != null && ! filename.equalsIgnoreCase("")){
				            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
				            fos = new FileOutputStream(file);
//		        	}
		        	
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
						
						/*Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
						boolean hasSpecialChar = p.matcher(event.getFilename()).find();*/
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
		fileUpload.setButtonCaption(null);
		fileUpload.setCaption("File Upload");
		
		txtReasonforReferring = (TextArea) binder.buildAndBind(
				"Reason for Referring (Processor)", "reasonforReferring",
				TextArea.class);
		txtReasonforReferring.setMaxLength(2000);
		txtReasonforReferring.setValue("");
		txtReasonforReferring.setWidth("400px");
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
		mainHor.setWidth("100%");
		mainHor.setSpacing(true);
		
		mandatoryFields.add(cmbSpecialistType);
		mandatoryFields.add(txtReasonforReferring);
		
		showOrHideValidation(false);
		
		dynamicLayout.removeAllComponents();
		dynamicLayout.addComponent(mainHor);
	}

	@Override
	public void generateFieldBasedOnApproveClick(Boolean isChecked) {
		
		
		unbindField(txtQueryRemarkProcesser);
		unbindField(txtReviewRemarks);
		unbindField(cmbSpecialistType);
		unbindField(txtReasonforReferring);
//		unbindField(cmbAddtoWatchList);
    	unbindField(txtWatchListRemarks);
    	
    	mandatoryFields.remove(txtQueryRemarkProcesser);
    	mandatoryFields.remove(txtReviewRemarks);
    	mandatoryFields.remove(cmbSpecialistType);
    	mandatoryFields.remove(txtReasonforReferring);
//    	mandatoryFields.remove(cmbAddtoWatchList);
    	mandatoryFields.remove(txtWatchListRemarks);
		
		txtReviewRemarks = (TextArea) binder.buildAndBind("Review Remarks",
				"sendApproveRemarks", TextArea.class);
		txtReviewRemarks.setValue("");
		txtReviewRemarks.setRequired(true);
		txtReviewRemarks.setMaxLength(2000);
		txtReviewRemarks.setWidth("400px");
//		CSValidator validator = new CSValidator();
//		validator.extend(txtReviewRemarks);
//		validator.setRegExp("^[a-zA-Z 0-9.]*$");
//		validator.setPreventInvalidTyping(true);
		
		
		sendtoApproverFLayout = new FormLayout(txtReviewRemarks);
		sendtoApproverFLayout.setSpacing(true);
		
		mandatoryFields.add(txtReviewRemarks);
		
		dynamicLayout.removeAllComponents();
		dynamicLayout.addComponent(sendtoApproverFLayout);
	}

	@Override
	public void list(Page<PEDRequestDetailsProcessDTO> resultList) {
		
		pedRequestDetailsProcessList.setTableList(resultList.getPageItems());
		pedRequestDetailsProcessList.setRowColor(searchDTO.getKey());
	}
	@Override
	public void result() {

Label successLabel = new Label("<b style = 'color: black;'>Claim Record Saved Successfully!!! </b>", ContentMode.HTML);
		
		Button homeButton = new Button("PED Processor Home");
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
				fireViewEvent(MenuItemBean.PROCESS_PED_REQUEST_PROCESS, true);
				
			}
		});
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		if(buttonClicked != null && buttonClicked.equalsIgnoreCase("")){
			eMsg.append("Please select Query or Specialist or Approver </br>");
			hasError = true;
		}
		
		if(buttonClicked != null && buttonClicked.equals("watchlist")) {
//			SelectValue selected=new SelectValue();
//			selected=(SelectValue)cmbAddtoWatchList.getValue();
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
		if(cmbPEDSuggestion != null && cmbPEDSuggestion.getValue() != null && ((SelectValue)cmbPEDSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG004)){
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
	
  /* private boolean emptyStringValidate(){
	   String eMsg="";
	   Boolean hasError=false;
	   if(!mandatoryFields.isEmpty()){
		   for(int i=0;i<mandatoryFields.size();i++){
			   AbstractField<?> field=(AbstractField<?>)mandatoryFields.get(i);
			   if(("").equals(field.getValue())){
				   setRequired(true);
				   ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg += errMsg.getFormattedHtmlMessage();
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
	public void setReference(NewIntimationDto intimationDto,ClaimDto claimDto,List<InsuredPedDetails> pedByInsured,Boolean isWatchList,Boolean isAlreadyWatchlist) {
	           this.intimationDto=intimationDto;
	           this.claimDto=claimDto;
	           
	           this.bean.setIsWatchList(isWatchList);
	           
	           this.bean.setIsAlreadyWatchList(isAlreadyWatchlist);
	           
	           this.bean.setInsuredPedDetails(pedByInsured);
	           
		
	}

	@Override
	public void showEditPanel(PEDRequestDetailsProcessDTO bean) {
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

	@Override
	public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		 rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
	 }
	 
	 @Override
	 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		 rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
	 }
}
