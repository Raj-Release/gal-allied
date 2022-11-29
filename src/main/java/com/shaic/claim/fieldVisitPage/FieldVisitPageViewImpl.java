package com.shaic.claim.fieldVisitPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PreauthService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.zybnet.autocomplete.server.AutocompleteField;
import com.zybnet.autocomplete.server.AutocompleteQueryListener;
import com.zybnet.autocomplete.server.AutocompleteSuggestionPickedListener;

public class FieldVisitPageViewImpl extends AbstractMVPView implements
		FieldVisitPageView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private FieldVisitDTO bean;

	@Inject
	private TextBundle textBundle;

	@Inject
	private RevisedCashlessCarousel preauthIntimationDetailsCarousel;

	@Inject
	private IntimationService intimationService;

	@Inject
	private Intimation intimation;

	@Inject
	private NewIntimationDto newIntimationDto;

	@Inject
	private PreauthService preAuthService;

	@Inject
	private ClaimService claimService;


	@Inject
	private ClaimDto claimDTO;

	@Inject
	private ViewDetails viewDetails;

	@EJB
	private FieldVisitRequestService fieldVisitSerivice;

	@Inject
	private FieldVisitPageRepresentativeNameSearchUI fieldVisitPageRepresentativeNameSearchUI;

	private List<ViewFVRDTO> fvrDTOList;

	private BeanFieldGroup<FieldVisitDTO> binder;

	private TextArea txtApprovedFVR;

	private TextField txtAllocationDoctor;
	
	private TextField txtAssignTo;
	
	private TextField txtPriority;

	private Button btnAssignFvr;

	private Button btnSkipFvr;

	private AutocompleteField<TmpFVRDTO> representName;

	private TextField txtTelNo;

	private TextField txtMobileNo;

	private TextArea txtExecutiveComments;

	private TextArea txtReason;

	private TextArea txtReasonForAdmission;

	private HorizontalLayout firstHorizontal;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private HorizontalLayout secondHorizontal;

	private FormLayout firstFormLayout;

	private FormLayout secondFormLayout;

	private VerticalLayout mainVertical;

	private Button btnSubmit;

	private Button btnCancel;

	private Button btnViewHospitalDetails;

	private Button btnRepresentativeSearch;

	private TextField txtFVRCount;

	private Button btnViewFVRDetails;

	private HorizontalLayout buttonHorLayout;

	private VerticalLayout contentVertical;

	private static String OPTION = "submit option";

	public final static String ASSIGN = "ASSIGN";

	public final static String SKIP = "SKIP";

	private SearchFieldVisitTableDTO searchFieldVisitTableDTO;

	@Inject
	private TmpFVRDTO tmpFVRDTO;

	//public static Window popup;

	private SearchFieldVisitTableDTO tableDto;
	
	private FormLayout dynamicLayout;

	private BeanItemContainer<SelectValue> stateContainer;

	private BeanItemContainer<SelectValue> allocationToContainer;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private RRCDTO rrcDTO;
	
	private PreauthDTO preauthDTO;
	
	private Window popup;

	@Override
	public void setReferenceData(FieldVisitDTO bean,
			SearchFieldVisitTableDTO searchTableDTO,
			NewIntimationDto newIntimationDto, ClaimDto claimDto,
			List<ViewFVRDTO> fvrDTOList, Intimation intimation) {
		this.searchFieldVisitTableDTO = searchTableDTO;
		this.bean = bean;
		this.newIntimationDto = newIntimationDto;
		this.claimDTO = claimDto;
		this.fvrDTOList = fvrDTOList;
		this.intimation = intimation;
	}

	public void initView(SearchFieldVisitTableDTO searchDto) {
		this.tableDto = searchDto;
		this.rrcDTO = searchDto.getRrcDTO();
		
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
	//	preauthDTO.setRodHumanTask(searchDto.getHumanTask());
		
		fireViewEvent(FieldVisitPagePresenter.FIELD_VISIT, searchDto);
		this.binder = new BeanFieldGroup<FieldVisitDTO>(FieldVisitDTO.class);
		this.binder.setItemDataSource(this.bean);
		setHeight("100%");
		if (newIntimationDto != null && claimDTO != null) {
//			preauthIntimationDetailsCarousel.init(newIntimationDto, claimDTO,
//					"Process Field Visit");
			preauthIntimationDetailsCarousel.init(newIntimationDto, claimDTO,
					"Process Field Visit",searchDto.getDiagnosis());
		}
		
		txtApprovedFVR = (TextArea) binder.buildAndBind(
				"FVR Trigger Points\n (Medical Approver)", "approvedFVR",
				TextArea.class);
		txtApprovedFVR.setNullRepresentation("");
		txtApprovedFVR.setReadOnly(true);
		txtApprovedFVR.setWidth("120%");
		

		txtAllocationDoctor = (TextField) binder.buildAndBind(
				textBundle.getText(textBundlePrefixString()
						+ "allocationDoctor"), "allocateDoctor",
				TextField.class);
		txtAllocationDoctor.setReadOnly(true);
		
		txtAssignTo = (TextField) binder.buildAndBind("Assign To", "assignTo",
				TextField.class);
		txtAssignTo.setReadOnly(true);
		txtAssignTo.setNullRepresentation("");
		
		txtPriority = (TextField) binder.buildAndBind("Priority", "priority",
				TextField.class);
		txtPriority.setReadOnly(true); 
		txtPriority.setNullRepresentation("");

		txtReasonForAdmission = new TextArea("Reason for Admission");

		firstFormLayout = new FormLayout(txtReasonForAdmission,txtAssignTo,txtPriority);
		if(this.bean.getIsFinancialFvr()){
			secondFormLayout = new FormLayout(txtAllocationDoctor);
		}
		else{
			secondFormLayout = new FormLayout(txtApprovedFVR, txtAllocationDoctor);
		}
		firstHorizontal = new HorizontalLayout(firstFormLayout,
				secondFormLayout);
		firstHorizontal.setSpacing(true);
		
		
		btnAssignFvr = new Button("Assign FVR");
		btnAssignFvr.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btnSkipFvr = new Button("Skip FVR");
		btnSkipFvr.setStyleName(ValoTheme.BUTTON_PRIMARY);
		
		secondHorizontal = new HorizontalLayout(btnAssignFvr, btnSkipFvr);
		secondHorizontal.setSpacing(true);
		secondHorizontal.setHeight("5px");
		
		dynamicLayout = new FormLayout();
//		dynamicLayout.setHeight("150px");
		
		btnSubmit = new Button("Submit");
		btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setEnabled(false);
		btnCancel = new Button("Cancel");
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		
		buttonHorLayout = new HorizontalLayout(btnSubmit, btnCancel);
		buttonHorLayout.setSpacing(true);
		
		if(searchDto.getRodKey() != null){
			viewDetails.initView(this.intimation.getIntimationId(), searchDto.getRodKey(), ViewLevels.INTIMATION,"Process Field Visit");
		}
		else{
		viewDetails.initView(this.intimation.getIntimationId(),
		ViewLevels.INTIMATION);
		}
		
		txtFVRCount = new TextField("FVR No");
		
		btnViewFVRDetails = new Button("View FVR Details");
		btnViewHospitalDetails = new Button("View Hospital Details");
		
		
	/*	HorizontalLayout viewDetailsLayout = new HorizontalLayout(
				new FormLayout(txtFVRCount), btnViewFVRDetails, viewDetails);
		viewDetailsLayout.setWidth("100%");*/
		HorizontalLayout viewDetailsLayout = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		viewDetailsLayout.setWidth("100%");
		viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
		
		
//<<<<<<< HEAD
//		mainVertical = new VerticalLayout(preauthIntimationDetailsCarousel,viewDetailsLayout,firstHorizontal,secondHorizontal,dynamicLayout,buttonHorLayout);
//		mainVertical.setSpacing(false);
////		mainVertical.setComponentAlignment(btnViewHospitalDetails, Alignment.TOP_RIGHT);
//=======
		mainVertical = new VerticalLayout(preauthIntimationDetailsCarousel,viewDetailsLayout/*,btnViewHospitalDetails*/,firstHorizontal,secondHorizontal,dynamicLayout,buttonHorLayout);
		mainVertical.setSpacing(true);
		//mainVertical.setComponentAlignment(btnViewHospitalDetails, Alignment.TOP_RIGHT);
//>>>>>>> 87d0467994a0210209ef6df4cdbb2ceb0b50f3ed
		mainVertical.setComponentAlignment(secondHorizontal, Alignment.MIDDLE_CENTER);
	//	secondHorizontal.setHeight("5px");
		mainVertical.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		
		/*if (this.bean != null
				&& this.bean.getStageId() != null
				&& this.bean.getStageId() == ReferenceTable.FIELD_VISIT_REPRESENTATION) {
			btnSkipFvr.setEnabled(false);
		} else {
			btnSkipFvr.setEnabled(true);
		}*/


//		txtFVRCount = new TextField("FVR No");
//		FormLayout fvrCoutnLayout = new FormLayout(txtFVRCount);
//		btnViewFVRDetails = new Button("View FVR Details");
//		btnViewHospitalDetails = new Button("View Hospital Details");
//		FormLayout btnViewFVRDetailsLayout = new FormLayout(btnViewFVRDetails);
//		HorizontalLayout viewDetailsLayout = new HorizontalLayout(
//				fvrCoutnLayout, btnViewFVRDetailsLayout, viewDetails);
//		viewDetailsLayout.setComponentAlignment(fvrCoutnLayout,
//				Alignment.TOP_LEFT);
//		viewDetailsLayout.setComponentAlignment(btnViewFVRDetailsLayout,
//				Alignment.TOP_LEFT);
//		viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
//		viewDetailsLayout.setWidth("100%");
//		contentVertical = new VerticalLayout(viewDetailsLayout,
//				btnViewHospitalDetails, mainVertical, buttonHorLayout);
//		contentVertical.setSizeFull();
//		contentVertical.setSizeUndefined();
//		contentVertical.setSpacing(true);
//		contentVertical.setComponentAlignment(viewDetailsLayout,
//				Alignment.TOP_RIGHT);
//		contentVertical.setComponentAlignment(buttonHorLayout,
//				Alignment.BOTTOM_CENTER);
//		contentVertical.setComponentAlignment(btnViewHospitalDetails,
//				Alignment.TOP_RIGHT);
//		Panel verticalPanel = new Panel();
//		verticalPanel.setContent(contentVertical);
//		verticalPanel.setWidth("100%");
//		verticalPanel.setHeight("570px");
//		contentVertical.setMargin(true);
//		VerticalLayout mainLayout = new VerticalLayout(
//				preauthIntimationDetailsCarousel, verticalPanel);
//		addListener();
//		Panel rootPanel = new Panel();
//		rootPanel.setContent(mainLayout);
//		rootPanel.setWidth("100%");
//		setCompositionRoot(rootPanel);
//		setSizeFull();
		if (bean != null && bean.getFvrCount() != null) {
			txtFVRCount.setValue(String.valueOf(fvrDTOList.size()));
			txtReasonForAdmission
					.setValue(bean.getReasonForAdmission() != null ? bean
							.getReasonForAdmission() : "");
		} else {
			txtFVRCount.setValue("0");
		}
		
		addListener();
		
		setCompositionRoot(mainVertical);
		txtFVRCount.setEnabled(false);
		txtFVRCount.setReadOnly(false);
		txtFVRCount.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtReasonForAdmission.setEnabled(false);
		txtReasonForAdmission.setReadOnly(false);
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
		

		
		
		HorizontalLayout rrcBtnLayout = new HorizontalLayout(btnRRC,btnViewFVRDetails,btnViewHospitalDetails);
		rrcBtnLayout.setHeight("5px");
		rrcBtnLayout.setSpacing(true);
		HorizontalLayout vLayout = new HorizontalLayout(new FormLayout(txtFVRCount));/*,btnViewFVRDetails, viewDetails,rrcBtnLayout);*/
		vLayout.setHeight("45px");
		vLayout.setSpacing(true);
		VerticalLayout vLayout1 = new VerticalLayout(rrcBtnLayout,vLayout);

		//vLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		//vLayout.setComponentAlignment(rrcBtnLayout, Alignment.TOP_RIGHT);
		
		//HorizontalLayout alignmentHLayout = new HorizontalLayout(	/*vLayout*/);
		return vLayout1;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(FieldVisitPagePresenter.VALIDATE_FIELD_VISIT_REP_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
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
			popup = new com.vaadin.ui.Window();
			popup.setCaption("");
			popup.setWidth("85%");
			popup.setHeight("100%");
			rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance.get();
			//ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
			//documentDetails.setClaimDto(bean.getClaimDTO());
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.FIELD_VISIT);
			
			/*PreauthDTO preauthDTO = new PreauthDTO();
			preauthDTO.setRrcDTO(this.rrcDTO);
			*/
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

	@SuppressWarnings("serial")
	public void addListener() {

		btnAssignFvr.addClickListener(new Button.ClickListener() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				/*
				 * btnAssignFvr.setEnabled(false); btnSkipFvr.setEnabled(true);
				 */
				btnSubmit.setEnabled(true);
				OPTION = ASSIGN;
				fireViewEvent(FieldVisitPagePresenter.ASSIGN_FVR, true);
			}
		});

		btnViewFVRDetails.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (newIntimationDto.getIntimationId() != null) {
					viewDetails.getFVRDetails(
							newIntimationDto.getIntimationId(), false);
				}
			}
		});

		btnViewHospitalDetails.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (newIntimationDto.getIntimationId() != null) {
					viewDetails.getViewHospitalDetails(newIntimationDto
							.getIntimationId());
				}
			}
		});

		btnSubmit.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setUsername(UI.getCurrent().getUI().getSession()
						.getAttribute(BPMClientContext.USERID).toString());
				bean.setPassword(UI.getCurrent().getUI().getSession()
						.getAttribute(BPMClientContext.PASSWORD).toString());
				if (OPTION.equals(ASSIGN)) {
					if (validatePage()) {
						if ((representName.getText() != null && !representName
										.getText().equals(""))) {
							if (txtExecutiveComments.getValue() != null
									&& !txtExecutiveComments.getValue().equals(
											"")) {
								bean.setComments(txtExecutiveComments
										.getValue());
								bean.setReasons(txtExecutiveComments.getValue());
								if (tmpFVRDTO != null) {
									if (null != tmpFVRDTO
											.getRepresentativeName()) {
										bean.setName(tmpFVRDTO
												.getRepresentativeName());
										bean.setRepresentativeCode(tmpFVRDTO
												.getRepresentativeCode());
									}
								}
								Map<String, Object> mapDTO = new HashMap<String, Object>();
								mapDTO.put("searchTableDTO",
										searchFieldVisitTableDTO);
								mapDTO.put("fieldVisitDTO", bean);
								fireViewEvent(
										FieldVisitPagePresenter.SUBMIT_EVENT,
										mapDTO, OPTION, tableDto);
							} else {
								ConfirmDialog dialog = ConfirmDialog
										.show(getUI(),
												"Executive Comments can't be empty, Please Enter a valid Executive Comments",
												new ConfirmDialog.Listener() {

													public void onClose(ConfirmDialog dialog) {
														dialog.close();
													}
												});
								dialog.setStyleName(Reindeer.WINDOW_BLACK);
								dialog.getCancelButton().setVisible(false);
							}

						} else {
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Representative Name can't be empty, Please Enter a valid Representative Name",
											new ConfirmDialog.Listener() {

												public void onClose(
														ConfirmDialog dialog) {
													dialog.close();
												}
											});
							dialog.setStyleName(Reindeer.WINDOW_BLACK);
							dialog.getCancelButton().setVisible(false);
						}
					}
				} else if (OPTION.equals(SKIP)) {
					if (validatePage()) {
						bean.setReasons(txtReason.getValue());
						Map<String, Object> mapDTO = new HashMap<String, Object>();
						mapDTO.put("searchTableDTO", searchFieldVisitTableDTO);
						mapDTO.put("fieldVisitDTO", bean);
						fireViewEvent(FieldVisitPagePresenter.SUBMIT_EVENT,
								mapDTO, OPTION, tableDto);
					}
				}
			}
		});
		btnSkipFvr.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				/*
				 * btnAssignFvr.setEnabled(true); btnSkipFvr.setEnabled(false);
				 */
				btnSubmit.setEnabled(true);
				OPTION = SKIP;
				fireViewEvent(FieldVisitPagePresenter.SKIP_FVR, true);
			}
		});

		btnCancel.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Confirmation", "Are you sure You want to Cancel ?",
						"Yes", "No", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if(dialog.isCanceled()){
									dialog.close();
								}else if (dialog.isConfirmed()) {/*
									
									VaadinSession session = getSession();
									SHAUtils.releaseHumanTask(searchFieldVisitTableDTO.getUsername(), searchFieldVisitTableDTO.getPassword(), searchFieldVisitTableDTO.getTaskNumber(),session);
									
									com.shaic.ims.bpm.claim.modelv2.HumanTask reimbusementHumanTask = tableDto
											.getHumanTask();
									if(reimbusementHumanTask != null && reimbusementHumanTask.getPayload() != null){
										fireViewEvent(MenuItemBean.SHOW_PROCESS_FIELD_VISIT, true);
										
									}else{
										fireViewEvent(
												MenuItemBean.FIELD_VISIT_REPESENTATION,
												true);
									}
								*/
									releaseHumanTask();
									fireViewEvent(
											MenuItemBean.FIELD_VISIT_REPESENTATION,
											true);
									
								
								} else {
									dialog.close();
								}
							}
						});
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
	}

	private void handleRepresentativeSelection(TmpFVRDTO suggestion) {
		if (suggestion.getRepresentativeName() != null) {
			this.tmpFVRDTO = suggestion;
			enableTelNoAndMobileNo();
			representName.setText(suggestion.getRepresentativeName());
			if(suggestion.getPhoneNumber() != null){
				txtTelNo.setValue(suggestion.getPhoneNumber().toString());
			}
			if(suggestion.getMobileNumber() != null){
			txtMobileNo.setValue(suggestion.getMobileNumber().toString());
			}
			disableTelNoAndMobileNo();
		} else {
			Notification.show("Representative Name not available");
		}
	}

	@SuppressWarnings({ "unused" })
	protected Void localize(
			@Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
		List<String> header = new ArrayList<String>();
		java.lang.reflect.Field[] declaredFields = this.getClass()
				.getDeclaredFields();
		header.add(textBundle.getText(textBundlePrefixString()
				+ String.valueOf(txtApprovedFVR).toLowerCase()));
		header.add(textBundle.getText(textBundlePrefixString()
				+ String.valueOf(txtAllocationDoctor).toLowerCase()));
		header.add(textBundle.getText(textBundlePrefixString()
				+ String.valueOf(representName).toLowerCase()));
		header.add(textBundle.getText(textBundlePrefixString()
				+ String.valueOf(txtTelNo).toLowerCase()));
		header.add(textBundle.getText(textBundlePrefixString()
				+ String.valueOf(txtMobileNo).toLowerCase()));
		header.add(textBundle.getText(textBundlePrefixString()
				+ String.valueOf(txtExecutiveComments).toLowerCase()));
		header.add(textBundle.getText(textBundlePrefixString()
				+ String.valueOf(txtReason).toLowerCase()));

		txtApprovedFVR.setCaption(header.get(0));
		txtAllocationDoctor.setCaption(header.get(1));
		representName.setCaption(header.get(2));
		txtTelNo.setCaption(header.get(3));
		txtMobileNo.setCaption(header.get(4));
		txtExecutiveComments.setCaption(header.get(5));
		txtExecutiveComments.setMaxLength(200);
		txtReason.setCaption(header.get(6));
		txtReason.setMaxLength(200);

		return null;
	}

	private String textBundlePrefixString() {
		return "Field-VisitRepresentative-";
	}

	@SuppressWarnings({ "unused" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout.iterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof ComboBox) {
				ComboBox field = (ComboBox) c;
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateFieldBasedAssignFVRClick(Boolean isChecked) {
		unbindField(txtReason);
		unbindField(txtApprovedFVR);
		unbindField(txtMobileNo);
		unbindField(txtTelNo);
		unbindField(representName);
		unbindField(txtExecutiveComments);
		secondFormLayout.addComponent(txtAllocationDoctor);
		representName = new AutocompleteField<TmpFVRDTO>();
		//Vaadin8-setImmediate() representName.setImmediate(true);
		representName.setRequired(true);
		representName.setValidationVisible(false);
		representName.setWidth("180px");
		representName
				.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<TmpFVRDTO>() {

					@Override
					public void onSuggestionPicked(TmpFVRDTO suggestion) {
						handleRepresentativeSelection(suggestion);
					}
				});

		txtTelNo = new TextField("Representative Tel No");
		txtTelNo.setEnabled(false);
		txtTelNo.setNullRepresentation("");

		txtMobileNo = new TextField("Representative Mobile No");
		txtMobileNo.setEnabled(false);
		txtMobileNo.setNullRepresentation("");

		txtExecutiveComments = (TextArea) binder.buildAndBind(
				textBundle.getText(textBundlePrefixString()
						+ "executiveComments"), "comments", TextArea.class);
		txtExecutiveComments.setNullRepresentation("");
		txtExecutiveComments.setMaxLength(200);
		txtExecutiveComments.setWidth("400px");

		mandatoryFields.add(txtExecutiveComments);

		showOrHideValidation(false);
		btnRepresentativeSearch = new Button();
		FormLayout representativeNameFormLayout = new FormLayout(representName);
		FormLayout btnRepresentativeSearchform = new FormLayout(btnRepresentativeSearch);
		HorizontalLayout representativeNameLayout = new HorizontalLayout(
				representativeNameFormLayout, btnRepresentativeSearchform);
		representativeNameLayout.setWidth("420px");
		btnRepresentativeSearch.setStyleName("link");
		btnRepresentativeSearch.setIcon(new ThemeResource("images/search.png"));
//		representativeNameLayout.setComponentAlignment(btnRepresentativeSearch,
//				Alignment.MIDDLE_LEFT);
//		representativeNameLayout.setComponentAlignment(
//				representativeNameFormLayout, Alignment.MIDDLE_LEFT);
		representativeNameLayout.setCaption("Representative Name");
//		firstFormLayout.removeAllComponents();
//		secondFormLayout.removeAllComponents();
//		secondFormLayout.addComponent(txtApprovedFVR);
//		secondFormLayout.addComponent(txtAllocationDoctor);
//		firstFormLayout.addComponent(txtReasonForAdmission);
//		firstFormLayout.addComponent(representativeNameLayout);
//		firstFormLayout.addComponent(txtTelNo);
//		firstFormLayout.addComponent(txtMobileNo);
//		firstFormLayout.addComponent(txtExecutiveComments);
//		firstFormLayout.setComponentAlignment(representativeNameLayout,
//				Alignment.MIDDLE_LEFT);
		
		dynamicLayout.removeAllComponents();
		dynamicLayout.addComponent(representativeNameLayout);
		dynamicLayout.addComponent(txtTelNo);
		dynamicLayout.addComponent(txtMobileNo);
		dynamicLayout.addComponent(txtExecutiveComments);
		
		
		btnRepresentativeSearch.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						FieldVisitPagePresenter.SEARCH_REPRESENTATVIE_NAME,
						null);
			}
		});
		setUpAutoRepresentativeName(representName);
	}

	@Override
	public void generateFieldBasedSkipFVRClick(Boolean isChecked) {
		unbindField(txtReason);
		unbindField(txtApprovedFVR);
		unbindField(txtMobileNo);
		unbindField(txtTelNo);
		unbindField(representName);
		unbindField(txtExecutiveComments);
		
//		firstFormLayout.removeAllComponents();
//		firstFormLayout.addComponent(txtReasonForAdmission);

		txtReason = (TextArea) binder.buildAndBind(
				textBundle.getText(textBundlePrefixString() + "reason"),
				"reasons", TextArea.class);
		txtReason.setMaxLength(200);
		txtReason.setNullRepresentation("");
		txtReason.setWidth("400px");
		mandatoryFields.add(txtReason);
		showOrHideValidation(false);

//		secondFormLayout.removeAllComponents();
//		secondFormLayout.addComponent(txtApprovedFVR);
//		secondFormLayout.addComponent(txtAllocationDoctor);
//		secondFormLayout.addComponent(txtReason);
		
		dynamicLayout.removeAllComponents();
		dynamicLayout.addComponent(txtReason);
	}

	@SuppressWarnings("serial")
	@Override
	public void result() {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(),
				"Claim record saved successfully !!!",
				new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
						/*	com.shaic.ims.bpm.claim.modelv2.HumanTask reimbusementHumanTask = tableDto
									.getHumanTask();
							if(reimbusementHumanTask != null && reimbusementHumanTask.getPayload() != null){
								fireViewEvent(MenuItemBean.SHOW_PROCESS_FIELD_VISIT, true);
								
							}else{
								fireViewEvent(
										MenuItemBean.FIELD_VISIT_REPESENTATION,
										true);
							}
							
						*/
							fireViewEvent(
									MenuItemBean.FIELD_VISIT_REPESENTATION,
									true);
							
						} else {
							dialog.close();
						}
					}
				});
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		dialog.getCancelButton().setVisible(false);
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	@SuppressWarnings("static-access")
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		if (OPTION.equals(ASSIGN)) {
			eMsg.append("Please Enter Representative Name");
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
			if (binder.getPropertyId(field) != null) {
				this.binder.unbind(field);
			}

		}
	}

	public void showFVRNotFoundMessage() {
		Notification.show("Field Visit Request, Not Found");
	}

	private void setUpAutoRepresentativeName(AutocompleteField<TmpFVRDTO> search) {
		search.setQueryListener(new AutocompleteQueryListener<TmpFVRDTO>() {
			@Override
			public void handleUserQuery(AutocompleteField<TmpFVRDTO> field,
					String query) {
				List<TmpFVRDTO> tmpFVRDTOList = fieldVisitSerivice
						.getTmpFVRDetails(field, query);
				if (tmpFVRDTOList != null && !tmpFVRDTOList.isEmpty()) {
					for (TmpFVRDTO tmpFVRDTO : tmpFVRDTOList) {
						field.addSuggestion(tmpFVRDTO,
								tmpFVRDTO.getRepresentativeName());
					}
				} else {
					txtMobileNo.setValue("");
					txtTelNo.setValue("");
				}
			}
		});
	}

	@Override
	public void initRepresentativeSearch(
			BeanItemContainer<SelectValue> stateContainer,
			BeanItemContainer<SelectValue> allocationToContainer,BeanItemContainer<SelectValue> assignToConainer,BeanItemContainer<SelectValue> fvrPriorityContainer) {
		fieldVisitPageRepresentativeNameSearchUI.initRepresentativeNameSearch(this.intimation,SHAConstants.ASSIGN_FVR);
		fieldVisitPageRepresentativeNameSearchUI
				.setReferenceDataForStateAndAllocationTo(stateContainer,
						allocationToContainer);
		popup = new com.vaadin.ui.Window();
		popup.setCaption("Search FVR Representative");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(fieldVisitPageRepresentativeNameSearchUI);
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

	@Override
	public void setRepresentativeDetails(TmpFVRDTO tmpFVRDTOList) {
		if (tmpFVRDTOList != null) {
			tmpFVRDTO.setRepresentativeCode(tmpFVRDTOList
					.getRepresentativeCode());
			tmpFVRDTO.setRepresentativeName(tmpFVRDTOList
					.getRepresentativeName());
			tmpFVRDTO.setMobileNumber(tmpFVRDTOList.getMobileNumber());
			tmpFVRDTO.setPhoneNumber(tmpFVRDTOList.getPhoneNumber());
			tmpFVRDTO.setKey(tmpFVRDTOList.getKey());
		}
		representName.setText(tmpFVRDTOList.getRepresentativeName());
		enableTelNoAndMobileNo();
		
		if(tmpFVRDTOList.getPhoneNumber() != null){
			txtTelNo.setValue(tmpFVRDTOList.getPhoneNumber());
		}
		if(tmpFVRDTOList.getMobileNumber() != null){
			txtMobileNo.setValue(tmpFVRDTOList.getMobileNumber());
		}
		
		disableTelNoAndMobileNo();
		if(popup != null){
			popup.close();
		}
		
	}

	private void enableTelNoAndMobileNo() {
		txtTelNo.setEnabled(true);
		txtTelNo.setEnabled(true);
		txtMobileNo.setReadOnly(false);
		txtMobileNo.setReadOnly(false);
	}

	private void disableTelNoAndMobileNo() {
		txtTelNo.setEnabled(false);
		txtTelNo.setEnabled(false);
		txtMobileNo.setReadOnly(true);
		txtMobileNo.setReadOnly(true);
	}
	
	
	private void releaseHumanTask(){

		Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
		String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
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
