package com.shaic.claim.pedrequest.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.pedquery.PEDQueryDTO;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedrequest.teamlead.PEDRequestDetailsTeamLeadPresenter;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.Property;
import com.shaic.main.navigator.ui.Toolbar;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("unused")
public class ViewPEDRequestWindow extends AbstractMVPView implements ViewPEDRequestView {

	private static final long serialVersionUID = -9182066388418239095L;

	@Inject
	private ViewPEDRequestDetailsTable pedRequestDetailsTable;
	
//	@Inject
//	private InitiatePEDEndorsementTable initiatePEDEndorsementTable;


	private PEDQueryDTO bean;

	@Inject
	private ViewpedRequestService viewPedRequestSrevice;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@Inject
	private Instance<ViewPEDRequestTable> initiatePEDEndorsementTable;
	
	private ViewPEDRequestTable initiatePEDEndorsementObj;

//	@Inject
//	private ViewIntiatePEDPage viewIntiatePEDPage;

//	@Inject
//	private ViewPEDTable viewPEDTable;

	@Inject
	private ViewpedRequestService viewpedRequestService;
	
	@Inject
	private Toolbar toolBar;
	
	@EJB
	private IntimationService intimationService;

	private BeanFieldGroup<PEDQueryDTO> binder;

	private VerticalLayout mainLayout;

	private OptionGroup intiatePEDEndorsement;
	

//	@Inject
//	private PEDQueryDTO pedQueryDTO;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private PolicyService policyService;
	

	private ComboBox cmbPedSuggestion;
	
	private TextField txtNameOfPed;
	
	//private TextField txtNameOfPed;
	
	private TextArea txtRemarks;

	private FormLayout dataFormLayout;
	
	private Long preauthKey;
	
	private Long intimationKey;
	
	private Long policyKey;
	
	private Long claimKey;
	
	private DateField txtRepudiationLetterDate;
	
	private Button submitBtn;
	
	private PreauthDTO preauthDto;
	
	private TextArea txtReviewRemarks;

	private FormLayout intimatePEDfield;
	
	VerticalLayout mainVertical;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PEDQueryService pedQueryService;

	@EJB
	private ClaimService claimService;
	
	private String presenter;
	
	private Long stageKey;
	
	private CheckBox chkWatchList;
	
	//private TextArea txtWatchlistRemarks;
	
	private Button sendUnderWrtingBtn;
	
	//private CheckBox chkDiscuss;
	
	private ComboBox cmbDiscussWith;
	
	private TextArea txtSuggestion;
	
	private Button cancelBtn;
	
	private Button submitButton;
	
	private DateField pedEffectiveFromDate;
	
	public void setPresenterString(String presenter){
		this.presenter = presenter;
	}

	@SuppressWarnings("unchecked")
	public void initView(PreauthDTO preauthDto,Long preauthKey,Long intimationKey,Long policyKey,Long claimKey,Long stageKey, Boolean shouldOpenPopup) {
		List<OldPedEndorsementDTO> pageItems = pedQueryService.pedInitiateDetails(claimKey).getPageItems();
		Boolean isPedInitiated = false;
		if(null != preauthDto.getNewIntimationDTO() && null != preauthDto.getNewIntimationDTO().getPolicy()) {
			isPedInitiated = pedQueryService.isPEDInitiated(preauthDto.getNewIntimationDTO().getPolicy().getKey());
		}
		if(!isPedInitiated) {
			createPedDetails(preauthDto, preauthKey, intimationKey, policyKey,
					claimKey, stageKey, pageItems,shouldOpenPopup);
		} else {
			if(shouldOpenPopup) {
				alertMessage(SHAConstants.PED_RAISE_MESSAGE, preauthDto, preauthKey, intimationKey, policyKey, claimKey, stageKey, pageItems, shouldOpenPopup);
			} else {
				createPedDetails(preauthDto, preauthKey, intimationKey, policyKey, claimKey, stageKey, pageItems, shouldOpenPopup);
			}
			
		}
		
	}
	
	
	private void showPopup(Layout layout) {
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(layout);
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
	
	
	public Boolean alertMessage(String message, final PreauthDTO preauthDto, final Long preauthKey, final Long intimationKey,final Long policyKey, final Long claimKey,final Long stageKey, final List<OldPedEndorsementDTO> pageItems, final Boolean shouldOpenPopup) {/*
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
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
				createPedDetails(preauthDto, preauthKey, intimationKey, policyKey,
						claimKey, stageKey, pageItems,shouldOpenPopup);
			}
		});
		return true;
	*/

   		final Boolean isClicked = false;
   		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				createPedDetails(preauthDto, preauthKey, intimationKey, policyKey,
						claimKey, stageKey, pageItems,shouldOpenPopup);
			}
		});
		return true;
		
	}

	private void createPedDetails(PreauthDTO preauthDto, Long preauthKey,
			Long intimationKey, Long policyKey, Long claimKey, Long stageKey,
			List<OldPedEndorsementDTO> pageItems, Boolean shouldOpenPopup) {
		
		this.preauthKey=preauthKey;
		this.intimationKey=intimationKey;
		this.policyKey=policyKey;
		this.claimKey=claimKey;
		this.preauthDto = preauthDto;
		this.stageKey = stageKey;
		
		bean = new PEDQueryDTO();
		
		this.binder = new BeanFieldGroup<PEDQueryDTO>(PEDQueryDTO.class);
		this.binder.setItemDataSource(this.bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		this.bean.setIsEditPED(false);
		
		bean.setPedName(null);
		bean.setPedSuggestion(null);
		bean.setRemarks(null);
		bean.setRepudiationLetterDate(null);
		bean.setReviewRemarks(null);
		bean.setIsAddWatchList(null);
		bean.setWatchlistRemarks(null);
		bean.setIsDiscussed(null);
		bean.setDiscussWith(null);
		bean.setDiscussRemarks(null);
//			Preauth preauth = preauthService.getPreauthById(preauthKey);
		
		pedRequestDetailsTable.init("PED Request", false, false);
		
		mainVertical=new VerticalLayout();
		
		Pageable pageable=pedRequestDetailsTable.getPageable();
		this.bean.setPageable(pageable);
		pedRequestDetailsTable
				.setTableList(pageItems);
		pedRequestDetailsTable.setWidth("100%");
		//pedRequestDetailsTable.setHeight("550px");
		
		//bindFieldGroup("002");
		mainLayout = new VerticalLayout();
		/*Panel tablePanel = new Panel(pedRequestDetailsTable);
		tablePanel.setHeight("180px");
		mainLayout.addComponent(tablePanel);*/
		mainLayout.addComponent(pedRequestDetailsTable);
		
		FormLayout radioForm = new FormLayout();
		intiatePEDEndorsement = new OptionGroup("Initiate PED Endorsement");
		intiatePEDEndorsement.addItem("Yes");
		intiatePEDEndorsement.addItem("No");
		intiatePEDEndorsement.setStyleName("inlineStyle");
		intiatePEDEndorsement.select("No");
		  
		radioForm.addComponent(intiatePEDEndorsement);
		radioForm.setMargin(true);
		radioForm.setSpacing(true);
		mainLayout.addComponent(radioForm);
		
		
		
		 mainLayout.addComponent(mainVertical);
		
		// setting radio buttons
		
		 intiatePEDEndorsement .addValueChangeListener(new ValueChangeListener() { 
		  
		  public void valueChange(
		  com.vaadin.v7.data.Property.ValueChangeEvent event) { if
		  (event.getProperty() != null &&
		  event.getProperty().getValue().toString() == "Yes") {
			  if(! bean.getIsEditPED()){
				  VerticalLayout buildPreauthSearchLayout = buildPreauthSearchLayout(null);
				  mainVertical.addComponent(buildPreauthSearchLayout);
				  mainVertical.setComponentAlignment(buildPreauthSearchLayout, Alignment.BOTTOM_LEFT);
				 // mainVertical.addComponent(viewPEDTable);
				  mainVertical.setSpacing(true);
			  }
		  }else{
//				  ViewNewIntiatePedService a = new ViewNewIntiatePedService();
//			  a.searchNewIntimationTable();
			bean.setPedName(null);
			bean.setPedSuggestion(null);
			bean.setRemarks(null);
			bean.setRepudiationLetterDate(null);
			bean.setIsEditPED(false);
			bean.setIsWatchList(false);
			bean.setIsAddWatchList(null);
			bean.setWatchlistRemarks(null);
			bean.setIsDiscussed(null);
			bean.setDiscussWith(null);
			bean.setDiscussRemarks(null);
			bean.setPedEffFromDate(null);
		    mainVertical.removeAllComponents();
		  //setCompositionRoot(mainLayout);
		  }
		  
		  } });
		  
		  
		  //bindFieldGroup(intimationNo);
		 setCompositionRoot(mainLayout);
//		 if(shouldOpenPopup) {
//			  showPopup(mainLayout);
//		 } 
		
	}

	public void bindFieldGroup(String intimationNo) {
		if (intimationNo != null) {
			try {
				List<OldPedEndorsementDTO> oldPedEndorsementDTO = new ArrayList<OldPedEndorsementDTO>();
				Intimation intimation = intimationService.getIntimationByNo(intimationNo);
				oldPedEndorsementDTO = viewPedRequestSrevice
						.search(intimation.getKey());
				if (oldPedEndorsementDTO != null) {
					pedRequestDetailsTable.setTableList(oldPedEndorsementDTO);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private VerticalLayout buildPreauthSearchLayout(List<ViewPEDTableDTO> tableList) {
		
		unbindField(cmbPedSuggestion);
		unbindField(txtNameOfPed);
		unbindField(txtRemarks);
		unbindField(txtRepudiationLetterDate);
		unbindField(txtReviewRemarks);
		unbindField(chkWatchList);
		unbindField(pedEffectiveFromDate);
		//unbindField(txtWatchlistRemarks);
		
		mandatoryFields.remove(txtReviewRemarks);
		//mandatoryFields.remove(txtWatchlistRemarks);
		
		//unbindField(chkDiscuss);
		unbindField(cmbDiscussWith);
		unbindField(txtSuggestion);
		
		//mandatoryFields.remove(chkDiscuss);
		mandatoryFields.remove(cmbDiscussWith);
		mandatoryFields.remove(txtSuggestion);
		
		cmbPedSuggestion = binder.buildAndBind("PED Suggestion",
				"pedSuggestion", ComboBox.class);
		
		cmbPedSuggestion.setContainerDataSource(masterService
				.getSelectValueContainer(ReferenceTable.PED_SUGGESTION));
		cmbPedSuggestion.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPedSuggestion.setItemCaptionPropertyId("value");
//		cmbPedSuggestion.setNullSelectionAllowed(false);
		cmbPedSuggestion.setData(this.intimationKey);
		
		
		if(this.bean.getPedSuggestion() != null){
			cmbPedSuggestion.setValue(this.bean.getPedSuggestion());
		}
		
		txtNameOfPed = binder.buildAndBind("Name of PED",
				"pedName", TextField.class);
		txtNameOfPed.setNullRepresentation("");
		txtNameOfPed.setMaxLength(100);
		//Vaadin8-setImmediate() txtNameOfPed.setImmediate(true);
		
		txtRemarks = binder.buildAndBind("Remarks", "remarks", TextArea.class);
		txtRemarks.setMaxLength(4000);
		//Vaadin8-setImmediate() txtRemarks.setImmediate(true);
		txtRemarks.setRequired(true);
		txtRemarks.setId("pedRmrks");
		remarksListener(txtRemarks,null);
		txtRemarks.setData(bean);
		txtRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		
		pedEffectiveFromDate = (PopupDateField) binder.buildAndBind(
				"PED Effective from date", "pedEffFromDate", PopupDateField.class);
		
		
		chkWatchList = binder.buildAndBind("Add to Watchlist", "isAddWatchList", CheckBox.class);
		//Vaadin8-setImmediate() chkWatchList.setImmediate(true);
		
		
		/*txtWatchlistRemarks = binder.buildAndBind("Add to Watchlist and Discussed with Remarks", "watchlistRemarks", TextArea.class);
		txtWatchlistRemarks.setMaxLength(4000);
		//Vaadin8-setImmediate() txtWatchlistRemarks.setImmediate(true);
		
		txtWatchlistRemarks.setId("watchListRmrks");
		remarksListener(txtWatchlistRemarks,null);
		txtWatchlistRemarks.setData(bean);
		txtWatchlistRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");*/

		String userName = (String) getUI().getSession()
				.getAttribute(BPMClientContext.USERID);
		
		this.bean.setIsWatchListReviewer(pedQueryService.isUserPedReviewer(userName));
		
		if((stageKey != null && (stageKey.equals(ReferenceTable.CLAIM_REQUEST_STAGE) || stageKey.equals(ReferenceTable.FINANCIAL_STAGE))) || (this.bean.getIsEditPED())){
			chkWatchList.setEnabled(false);
		}
		
		if(this.bean.getIsAddWatchList() != null && this.bean.getIsAddWatchList()){
			chkWatchList.setValue(this.bean.getIsAddWatchList());
		}
		
		BeanItemContainer<SelectValue> selectIcdChapterContainer=masterService.getSelectValuesForICDChapter();
		
		BeanItemContainer<SelectValue> selectPedCodeContainer=masterService.getPedDescription();
		
		BeanItemContainer<SelectValue> selectSourceContainer=masterService.getSelectValueContainer(ReferenceTable.PED_SOURCE);
		//referenceData.put("icdCode", selectValueContainer);
		referenceData.put("icdChapter",selectIcdChapterContainer);
		referenceData.put("pedCode", selectPedCodeContainer);
		referenceData.put("source", selectSourceContainer);
		
		mandatoryFields.add(cmbPedSuggestion);
		//mandatoryFields.add(txtNameOfPed);
		mandatoryFields.add(txtRemarks);
		//mandatoryFields.add(pedEffectiveFromDate);
		
		/*if(this.bean.getIsWatchListReviewer() != null && !this.bean.getIsWatchListReviewer()){
			if(this.chkWatchList != null && this.chkWatchList.isEnabled()){
				if(this.chkWatchList.getValue() != null && this.chkWatchList.getValue()){
					mandatoryFields.add(txtWatchlistRemarks);
				}	
			}
		}*/
		
		
		initiatePEDEndorsementObj=initiatePEDEndorsementTable.get();
		
		initiatePEDEndorsementObj.init(SHAConstants.PED_INITIATOR);
		initiatePEDEndorsementObj.setReferenceData(referenceData);
		initiatePEDEndorsementObj.setIntimationKey(this.intimationKey);
//		initiatePEDEndorsementObj.setSuggestionKey(cmbPedSuggestion.getValue() != null ? ((SelectValue)cmbPedSuggestion.getValue()).getId() : 0l);
		
		if(tableList != null && ! tableList.isEmpty()){
			for (ViewPEDTableDTO viewPEDTableDTO : tableList) {
				initiatePEDEndorsementObj.addBeanToList(viewPEDTableDTO);
			}
		}
	 
		
        submitBtn=new Button();
		if(this.bean.getIsWatchListReviewer() != null && !this.bean.getIsWatchListReviewer()){
			submitBtn.setCaption("Escalate to Cluster Head");
		}else{
			submitBtn.setCaption("Submit");
		}
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		
		sendUnderWrtingBtn=new Button("Submit to PED Department");
		sendUnderWrtingBtn.setVisible(false);
		
		if(this.bean.getIsWatchListReviewer() != null && !this.bean.getIsWatchListReviewer()){
			sendUnderWrtingBtn.setVisible(true);
		}else{
			sendUnderWrtingBtn.setVisible(false);
		}
		
		if(stageKey != null && (stageKey.equals(ReferenceTable.FINANCIAL_STAGE))){
			sendUnderWrtingBtn.setEnabled(false);
		}
		
		if(cmbPedSuggestion != null && cmbPedSuggestion.getValue() != null && ((SelectValue)cmbPedSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG004)){
			txtNameOfPed.setEnabled(false);
			txtNameOfPed.setRequired(false);
			mandatoryFields.remove(txtNameOfPed);
			unbindField(txtNameOfPed);
		}
		
		if(cmbPedSuggestion != null && cmbPedSuggestion.getValue() != null && (((SelectValue)cmbPedSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG004) || ((SelectValue)cmbPedSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG010))){
			initiatePEDEndorsementObj.disableAdd(Boolean.TRUE);
		}
		
		addListener();
		intimatePEDfield=new FormLayout(cmbPedSuggestion,txtNameOfPed,txtRemarks);
		if(this.bean.getPedSuggestion() != null && !this.bean.getPedSuggestion().getId().equals(ReferenceTable.PED_SUGGESTION_SUG001)
				&& !this.bean.getPedSuggestion().getId().equals(ReferenceTable.PED_SUGGESTION_SUG005)){
			pedEffectiveFromDate.setValue(null);
			mandatoryFields.remove(pedEffectiveFromDate);
			unbindField(pedEffectiveFromDate);
			intimatePEDfield.removeComponent(pedEffectiveFromDate);
		}else{
			intimatePEDfield.addComponent(pedEffectiveFromDate);
			showOrHideValidation(false);
			addPedEffFromDateListener(pedEffectiveFromDate);
		}
		FormLayout watchListLayout = new FormLayout(chkWatchList/*,txtWatchlistRemarks*/); 
		HorizontalLayout hLayout = new HorizontalLayout(intimatePEDfield, watchListLayout);
		hLayout.setSpacing(true);
		hLayout.setMargin(true);
		VerticalLayout intimatePEDFLayout = null;
		HorizontalLayout finalactionLayout = new HorizontalLayout();
		finalactionLayout.addComponents(sendUnderWrtingBtn,submitBtn);
		finalactionLayout.setSpacing(true);
		finalactionLayout.setMargin(true);
		if(this.bean.getIsWatchList()){
			txtReviewRemarks =  binder.buildAndBind("Review Remarks", "reviewRemarks", TextArea.class);
			txtReviewRemarks.setNullRepresentation("");
			txtReviewRemarks.setValue("");
			intimatePEDFLayout=new VerticalLayout(hLayout,initiatePEDEndorsementObj,new FormLayout(txtReviewRemarks), finalactionLayout);
			mandatoryFields.add(txtReviewRemarks);
		}else{
			
			intimatePEDFLayout=new VerticalLayout(hLayout,initiatePEDEndorsementObj,finalactionLayout);
		}
		intimatePEDFLayout.setComponentAlignment(initiatePEDEndorsementObj, Alignment.BOTTOM_LEFT);
		intimatePEDFLayout.setComponentAlignment(finalactionLayout, Alignment.BOTTOM_CENTER);
		intimatePEDFLayout.setSpacing(true);
		intimatePEDFLayout.setMargin(true);
		
		showOrHideValidation(false);
		
		return intimatePEDFLayout;
	}
	
	public void addListener() {

		cmbPedSuggestion.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
						
				ComboBox suggestionCmb = (ComboBox)event.getProperty();
				Long intimationKey = (Long) suggestionCmb.getData();
				
				Intimation intimation = intimationService
						.getIntimationByKey(intimationKey);
				
				Long insuredKey = preauthDto.getNewIntimationDTO() != null && preauthDto.getNewIntimationDTO().getInsuredPatient() != null ? preauthDto.getNewIntimationDTO().getInsuredPatient().getKey() : null;
				
				SelectValue suggestionSelect = suggestionCmb.getValue() != null ? (SelectValue)suggestionCmb.getValue() : null;
				if(suggestionSelect != null && suggestionSelect.getId() != null){
					if (suggestionCmb.getValue().toString().toLowerCase()
								.contains("sug 002 - cancel policy")) {
						if(txtRepudiationLetterDate != null){
							unbindField(txtRepudiationLetterDate);
							intimatePEDfield.removeComponent(txtRepudiationLetterDate);
							mandatoryFields.remove(txtRepudiationLetterDate);
						}
							txtRepudiationLetterDate = (DateField) binder.buildAndBind(
									"Repudiation Letter Date", "repudiationLetterDate",
									DateField.class);
							intimatePEDfield.addComponent(txtRepudiationLetterDate);
							mandatoryFields.add(txtRepudiationLetterDate);
							showOrHideValidation(false);
							
							Long policyKey = preauthDto.getNewIntimationDTO().getPolicy().getKey();
							
							//Long insuredKey = preauthDto.getNewIntimationDTO().getInsuredKey();
							
							String policyInsuredAgeingFlag = preauthDto.getPolicyInsuredAgeingFlag(); //dBCalculationService.getPolicyInsuredAgeingFlag(policyKey,insuredKey);
							if(policyInsuredAgeingFlag.equalsIgnoreCase("Y")){
								submitBtn.setEnabled(false);
								sendUnderWrtingBtn.setEnabled(false);
								//uwSubmitBtn.setEnabled(false);

								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								HashMap<String, Button> messageBoxButtons=GalaxyAlertBox.
										createWarningBox("Policy in Moratorium period. Cannot be cancelled </b>", buttonsNamewithType);

								Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
								okButton.addClickListener(new ClickListener() {
									private static final long serialVersionUID = -7148801292961705660L;

									@Override
									public void buttonClick(ClickEvent event) {

									}
								});

								VerticalLayout layout = new VerticalLayout(new Label("", ContentMode.HTML));
							}
							
							else if(policyInsuredAgeingFlag.equalsIgnoreCase("N")){

								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								HashMap<String, Button> messageBoxButtons=GalaxyAlertBox.
										createWarningBox("Please check whether moratorium clause is applicable before initiating cancellation </b>", buttonsNamewithType);

								Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
								okButton.addClickListener(new ClickListener() {
									private static final long serialVersionUID = -7148801292961705660L;

									@Override
									public void buttonClick(ClickEvent event) {

									}
								});

								VerticalLayout layout = new VerticalLayout(new Label("", ContentMode.HTML));
							}
					} else {
						if(txtRepudiationLetterDate != null){
						txtRepudiationLetterDate.setValue(null);
						intimatePEDfield.removeComponent(txtRepudiationLetterDate);
						mandatoryFields.remove(txtRepudiationLetterDate);
						unbindField(txtRepudiationLetterDate);
						}
					}
				
					if(cmbPedSuggestion != null && cmbPedSuggestion.getValue() != null && ((SelectValue)cmbPedSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG004)){
						txtNameOfPed.setValue(null);
						txtNameOfPed.setEnabled(false);
						txtNameOfPed.setRequired(false);
						mandatoryFields.remove(txtNameOfPed);
						unbindField(txtNameOfPed);
					}else{
						txtNameOfPed.setEnabled(true);
						txtNameOfPed.setRequired(true);
						mandatoryFields.add(txtNameOfPed);
						showOrHideValidation(false);
					}
					
					if(cmbPedSuggestion != null && cmbPedSuggestion.getValue() != null && (((SelectValue)cmbPedSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG004) || ((SelectValue)cmbPedSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG010))){
						initiatePEDEndorsementObj.disableAdd(Boolean.TRUE);
					}
					else{
						initiatePEDEndorsementObj.disableAdd(Boolean.FALSE);
					}

					
					if(ReferenceTable.PED_SUGGESTION_SUG002.equals(suggestionSelect.getId()) 
							|| ReferenceTable.PED_SUGGESTION_SUG003.equals(suggestionSelect.getId())
							|| ReferenceTable.PED_SUGGESTION_SUG004.equals(suggestionSelect.getId())
							|| ReferenceTable.PED_SUGGESTION_SUG005.equals(suggestionSelect.getId())
							|| ReferenceTable.PED_SUGGESTION_SUG006.equals(suggestionSelect.getId())
							|| ReferenceTable.PED_SUGGESTION_SUG010.equals(suggestionSelect.getId())){
						
						if(presenter != null && presenter.equalsIgnoreCase(SHAConstants.PED_INITIATOR)){
							fireViewEvent(ViewPEDRequestPresenter.GET_PED_ALREADY_AVAILABLE, suggestionSelect.getId(), intimationKey, insuredKey);
						}
	//					else if(presenter != null && presenter.equalsIgnoreCase(SHAConstants.PED_PROCESSOR)){
	//						fireViewEvent(PEDRequestDetailsProcessPresenter.GET_PED_ALREADY_AVAILABLE, suggestionSelect.getId(), intimationKey);
	//					}else if(presenter != null && presenter.equalsIgnoreCase(SHAConstants.PED_APPROVER)){
	//						fireViewEvent(PEDRequestDetailsApprovePresenter.GET_PED_ALREADY_AVAILABLE, suggestionSelect.getId(), intimationKey);
	//					}
						else if(presenter != null && presenter.equalsIgnoreCase(SHAConstants.PED_TEAM_LEAD)){
							fireViewEvent(PEDRequestDetailsTeamLeadPresenter.TL_GET_PED_ALREADY_AVAILABLE, suggestionSelect.getId(), intimationKey, insuredKey);
						}
						else{
							fireViewEvent(ViewPEDRequestPresenter.GET_PED_ALREADY_AVAILABLE, suggestionSelect.getId(), intimationKey, insuredKey);
						}
					}
					else{
						if(initiatePEDEndorsementObj != null)
							initiatePEDEndorsementObj.checkPEDAlreadyAvailableBySuggestion(suggestionSelect.getId());
					}
					if(initiatePEDEndorsementObj != null){
						initiatePEDEndorsementObj.setSuggestionKey(suggestionSelect.getId());
					}	
					
					if(ReferenceTable.PED_SUGGESTION_SUG001.equals(suggestionSelect.getId())
							|| ReferenceTable.PED_SUGGESTION_SUG005.equals(suggestionSelect.getId()))
					{
						if(pedEffectiveFromDate != null){
							pedEffectiveFromDate.setValue(null);
							unbindField(pedEffectiveFromDate);
							intimatePEDfield.removeComponent(pedEffectiveFromDate);
							mandatoryFields.remove(pedEffectiveFromDate);
						}
						pedEffectiveFromDate = (PopupDateField) binder.buildAndBind(
								"PED Effective from date", "pedEffFromDate", PopupDateField.class);
						intimatePEDfield.addComponent(pedEffectiveFromDate);
						mandatoryFields.add(pedEffectiveFromDate);
						pedEffectiveFromDate.setValue(preauthDto.getDocRecievedDate());
						showOrHideValidation(false);
						addPedEffFromDateListener(pedEffectiveFromDate);
						//addListener();
					} else {
						if(pedEffectiveFromDate != null){
							pedEffectiveFromDate.setValue(null);
							intimatePEDfield.removeComponent(pedEffectiveFromDate);
							mandatoryFields.remove(pedEffectiveFromDate);
							unbindField(pedEffectiveFromDate);
						}
					}

				}
			}
		});

		chkWatchList.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();

					if(value)
					{
						
						sendUnderWrtingBtn.setEnabled(false);
						
						//unbindField(chkDiscuss);
						unbindField(cmbDiscussWith);
						unbindField(txtSuggestion);
						
						//mandatoryFields.remove(chkDiscuss);
						mandatoryFields.remove(cmbDiscussWith);
						mandatoryFields.remove(txtSuggestion);
						if(bean.getIsWatchListReviewer() != null && !bean.getIsWatchListReviewer()){
						buildSendToUnderWriting(Boolean.FALSE);
						//mandatoryFields.add(txtWatchlistRemarks);
						//showOrHideValidation(false);
						//showErrorMessage("Please Enter Remarks for Add to Watchlist & Discussed with.");
						}
					}
					else{
						/*mandatoryFields.remove(txtWatchlistRemarks);
						unbindField(txtWatchlistRemarks);*/
						sendUnderWrtingBtn.setEnabled(true);
					}	 						 
					
				}
			}
		});
		
		pedEffectiveFromDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Date enteredDate = (Date) event.getProperty().getValue();
				if(enteredDate != null) {
					Date policyFromDate = preauthDto.getNewIntimationDTO().getPolicy().getPolicyFromDate();
					Date policyToDate = preauthDto.getNewIntimationDTO().getPolicy().getPolicyToDate();
					
					
					
					if (!(enteredDate.after(policyFromDate) || enteredDate.compareTo(policyFromDate) == 0) || !(enteredDate.before(policyToDate) || enteredDate.compareTo(policyToDate) == 0)) {
						
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						HashMap<String, Button> messageBoxButtons=GalaxyAlertBox.
						createErrorBox("PED Effective from date is not in range between Policy From Date and Policy To Date. </b>", buttonsNamewithType);
						
						 Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
						 okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;

							@Override
							public void buttonClick(ClickEvent event) {
								
							}
						});
						
						VerticalLayout layout = new VerticalLayout(new Label("", ContentMode.HTML));
				
						
						event.getProperty().setValue(preauthDto.getDocRecievedDate());
					}
				}
				
				
			}
		});
		
		sendUnderWrtingBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				buildSendToUnderWriting(Boolean.TRUE);
				
			}
		});
		
		
		submitBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				submitPed(null);
			}
		});
		
		

	}
	
	private void successResult(final ConfirmDialog dialog1){/*
		Label successLabel = null;
		if(dialog1 != null){
			successLabel = new Label("<b style = 'color: black;'>PED request Submit to PED Department </b>", ContentMode.HTML);	
		}else{
			if(chkWatchList != null && chkWatchList.getValue() != null && chkWatchList.getValue() && bean.getReviewRemarks() == null){
				successLabel = new Label("<b style = 'color: black;'>PED Kept under watchlist </b>", ContentMode.HTML);
			}else{
				successLabel = new Label("<b style = 'color: black;'>claim Record Saved Successfully!!! </b>", ContentMode.HTML);	
			}
			
		}
		
		
		Button homeButton = new Button("Ok");
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
				if(dialog1 != null){
					dialog1.close();
//					toolBar.countTool(); -- As Per BA Revised Req sub flow process won't considered as count
				}
			}
		});
	*/

		String successLabel = null;
		if(dialog1 != null){
			successLabel = new String("<b style = 'color: black;'>PED request Submit to PED Department </b>");	
		}else{
			if(chkWatchList != null && chkWatchList.getValue() != null && chkWatchList.getValue() && bean.getReviewRemarks() == null){
				successLabel = new String("<b style = 'color: black;'>PED Kept under watchlist </b>");
			}else{
				successLabel = new String("<b style = 'color: black;'>claim Record Saved Successfully!!! </b>");	
			}
			
		}
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(successLabel, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();	//kk
				if(dialog1 != null){
					dialog1.close();
				}
			}
		});
		
	}
	
	

	private void initBinder() {
		this.binder = new BeanFieldGroup<PEDQueryDTO>(PEDQueryDTO.class);
		this.binder.setItemDataSource(new PEDQueryDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer) {
	
		initiatePEDEndorsementObj.setIcdBlock(icdBlockContainer);
	}
	@Override
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer) {
		
		initiatePEDEndorsementObj.setIcdCode(icdCodeContainer);
		
	}

//	@Override
//	public void setPEDCode(BeanItemContainer<SelectValue> icdCodeContainer) {
//		
//		initiatePEDEndorsementObj.setPEDCode(icdCodeContainer);
//		
//	}
	
	@Override
	public void setPEDCode(String pedCode) {
		
			initiatePEDEndorsementObj.setPEDCode(pedCode);
	}
	
	@Override
	public void showPEDAlreadyAvailable(String pedAvailableMsg) {
		
		showErrorMessage(pedAvailableMsg);
		
		if(!pedAvailableMsg.toLowerCase().contains("icd")){
			cmbPedSuggestion.setValue(null);
		}	
		
	}
	
	@Override
	public void resetPEDDetailsTable(ViewPEDTableDTO newInitiatePedDTO){
		
		initiatePEDEndorsementObj.resetPEDDetailsTable(newInitiatePedDTO);
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
		
		Boolean isSumInsuredLocked = Boolean.FALSE;
		SelectValue sugSelected = cmbPedSuggestion != null ? (SelectValue) cmbPedSuggestion.getValue() : null;
		if(sugSelected != null && sugSelected.getId().equals(ReferenceTable.PED_SUGGESTION_SUG004)){
			isSumInsuredLocked = Boolean.TRUE;
		}
		
		if(sugSelected != null && sugSelected.getId().equals(ReferenceTable.PED_SUGGESTION_SUG010)){
			isSumInsuredLocked = Boolean.TRUE; //For Sug-10  Diag. Table to Be Disabled CR R1212
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
		
		if(!isSumInsuredLocked && this.txtNameOfPed != null && (this.txtNameOfPed.getValue() == null || this.txtNameOfPed.getValue().isEmpty())){
			hasError = true;
			eMsg.append("Please Enter Ped Name. </br>"); 
		}
		
		if(!isSumInsuredLocked){
			if(this.initiatePEDEndorsementObj != null && this.initiatePEDEndorsementObj.getValues().isEmpty()) {
				hasError = true;
				eMsg.append("Please Add Atleast one diagnosis List Details to Proceed Further. </br>"); 
			}}
		
		if (this.initiatePEDEndorsementObj != null){
			boolean isValid = this.initiatePEDEndorsementObj.isValid(isSumInsuredLocked);
			if(!isValid && !isSumInsuredLocked){
				hasError = true;
				List<String> errors = this.initiatePEDEndorsementObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		/*if(this.bean.getIsWatchListReviewer() != null && !this.bean.getIsWatchListReviewer()){
				if(this.chkWatchList != null && this.chkWatchList.getValue() != null && this.chkWatchList.getValue() && this.chkWatchList.isEnabled() && (this.txtWatchlistRemarks != null && (this.txtWatchlistRemarks.getValue() == null || this.txtWatchlistRemarks.getValue().isEmpty()))){
					hasError = true;
					eMsg.append("Please Enter Remarks for Add to Watchlist & Discussed with. </br>");
				}	
		}*/
		
		
		if (hasError) {
			setRequired(true);
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);

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

	@Override
	public void setEditDetailsForPED(OldInitiatePedEndorsement initiate,
			List<ViewPEDTableDTO> pedInitiateDetailsDTOList,Boolean isWatchList) {
		
//		this.bean = new PEDQueryDTO();
         
		mainVertical.removeAllComponents();
		if(initiate.getPedSuggestion() != null){
			SelectValue selected = new SelectValue();
			selected.setId(initiate.getPedSuggestion().getKey());
			selected.setValue(initiate.getPedSuggestion().getValue());
			this.bean.setPedSuggestion(selected);
		}
		
		this.bean.setIsEditPED(true);
		this.bean.setIsWatchList(isWatchList);
		this.bean.setPedName(initiate.getPedName());
		this.bean.setRemarks(initiate.getRemarks());
		this.bean.setRepudiationLetterDate(initiate.getRepudiationLetterDate());
		this.bean.setKey(initiate.getKey());
		this.bean.setAddWatchListFlag(initiate.getAddWatchListFlag());
		this.bean.setWatchlistRemarks(initiate.getWatchListRmrks());
		
		/*bean.setIsDiscussed(null);
		bean.setDiscussWith(null);
		bean.setDiscussRemarks(null);*/
		
		if(initiate.getUwTlFlag() != null){
			this.bean.setDiscussedFlag(initiate.getUwTlFlag());
		}
		if(initiate.getUwSuggestion() != null){
			this.bean.setDiscussRemarks(initiate.getUwSuggestion());
		}
		if(initiate.getUwDiscussWith() != null){
			SelectValue selected = new SelectValue();
			selected.setId(initiate.getUwDiscussWith().getKey());
			selected.setValue(initiate.getUwDiscussWith().getValue());
			this.bean.setDiscussWith(selected);
		}
		
		if(initiate.getPedEffectiveFromDate() != null){
			this.bean.setPedEffFromDate(initiate.getPedEffectiveFromDate());
		}
		
		VerticalLayout buildPreauthSearchLayout = buildPreauthSearchLayout(pedInitiateDetailsDTOList);
		mainVertical.addComponent(buildPreauthSearchLayout);
		mainVertical.setComponentAlignment(buildPreauthSearchLayout, Alignment.BOTTOM_LEFT);
		mainLayout.setSpacing(true);
		intiatePEDEndorsement.select("Yes");
		
	}
	
	public void setEnableorDisbleOptionGroup(){
		intiatePEDEndorsement.setEnabled(false);
	}
	

	private void buildSendToUnderWriting(Boolean isDisscussed){
		
		//unbindField(chkDiscuss);
		unbindField(cmbDiscussWith);
		unbindField(txtSuggestion);
		
		//mandatoryFields.remove(chkDiscuss);
		mandatoryFields.remove(cmbDiscussWith);
		mandatoryFields.remove(txtSuggestion);
		
		/*chkDiscuss = binder.buildAndBind("Discussed with PED Team Leader", "isDiscussed", CheckBox.class);
		//Vaadin8-setImmediate() chkDiscuss.setImmediate(true);*/
		
		cmbDiscussWith = binder.buildAndBind("Discussed with",
				"discussWith", ComboBox.class);
		
		cmbDiscussWith.setContainerDataSource(masterService
				.getSelectValueContainer(ReferenceTable.PED_TL));
		cmbDiscussWith.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDiscussWith.setItemCaptionPropertyId("value");
		cmbDiscussWith.setNullSelectionAllowed(false);
		
		if(this.bean.getDiscussWith() != null){
			cmbDiscussWith.setValue(this.bean.getDiscussWith());
		}
		
		txtSuggestion = binder.buildAndBind("Discussed Remarks", "discussRemarks", TextArea.class);
		txtSuggestion.setMaxLength(4000);
		//Vaadin8-setImmediate() txtSuggestion.setImmediate(true);
		
		if(this.bean.getDiscussRemarks() != null){
			txtSuggestion.setValue(this.bean.getDiscussRemarks());
		}
		
		txtSuggestion.setId("suggestionRmrks");
		remarksListener(txtSuggestion,null);
		txtSuggestion.setData(bean);
		txtSuggestion.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		
		//mandatoryFields.add(chkDiscuss);
		if(cmbPedSuggestion != null && cmbPedSuggestion.getValue() != null && cmbPedSuggestion.getValue().toString().toLowerCase()
				.contains("sug 002 - cancel policy") || !isDisscussed){
			mandatoryFields.add(cmbDiscussWith);
			mandatoryFields.add(txtSuggestion);
			
			showOrHideValidation(false);
		}else{
			mandatoryFields.remove(cmbDiscussWith);
			mandatoryFields.remove(txtSuggestion);
			unbindField(cmbDiscussWith);
			unbindField(txtSuggestion);
		}
		
		VerticalLayout mainLayout = new VerticalLayout();
		
		FormLayout fLayout = new FormLayout(/*chkDiscuss,*/cmbDiscussWith,txtSuggestion);
		mainLayout.addComponent(fLayout);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		getSubmitButtonWithListener(dialog, isDisscussed);
		HorizontalLayout actionLayout = new HorizontalLayout();
		actionLayout.setSpacing(true);
		actionLayout.addComponents(submitButton,getCancelButton(dialog));
		mainLayout.addComponent(actionLayout);
		
		mainLayout.setComponentAlignment(fLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(actionLayout, Alignment.BOTTOM_CENTER);
		mainLayout.setSpacing(true);
		
		showInPopup(mainLayout, dialog);
	}
	
	
	private void showInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("");
		dialog.setClosable(false);

		Panel panel = new Panel();
		panel.setHeight("500px");
		panel.setWidth("850px");
		panel.setContent(layout);
		dialog.setContent(panel);
		dialog.setResizable(false);
		dialog.setModal(true);

		dialog.show(getUI().getCurrent(), null, true);

	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				
				/*mandatoryFields.remove(txtWatchlistRemarks);
				unbindField(txtWatchlistRemarks);*/
				
				//unbindField(chkDiscuss);
				unbindField(cmbDiscussWith);
				unbindField(txtSuggestion);
				
				//mandatoryFields.remove(chkDiscuss);
				mandatoryFields.remove(cmbDiscussWith);
				mandatoryFields.remove(txtSuggestion);
				
				if((stageKey != null && (stageKey.equals(ReferenceTable.CLAIM_REQUEST_STAGE) || stageKey.equals(ReferenceTable.FINANCIAL_STAGE))) || (bean.getIsEditPED())){
					chkWatchList.setEnabled(false);
				}else{
					chkWatchList.setEnabled(true);
				}
				if(bean.getIsAddWatchList() != null && !bean.getIsAddWatchList()){
					if(chkWatchList != null && chkWatchList.getValue() != null && chkWatchList.getValue()){
						chkWatchList.setValue(false);
					}	
				}
				
				
				dialog.close();
				//binder = null;
			}
		});
		return cancelBtn;
	}
	
	private void getSubmitButtonWithListener(final ConfirmDialog dialog, final Boolean isDiscussed) {
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsDiscussed(isDiscussed);
				if(bean.getIsDiscussed()){
					/*mandatoryFields.remove(txtWatchlistRemarks);
					unbindField(txtWatchlistRemarks);*/
					chkWatchList.setValue(Boolean.FALSE);
					chkWatchList.setEnabled(false);
				}
				submitPed(dialog);
			}

		});
		
		
	}
	
	
	private void submitPed(final ConfirmDialog dialog){
		if (validatePage()) {
			SelectValue selected = new SelectValue();
			selected = (SelectValue) cmbPedSuggestion.getValue();
			if (cmbPedSuggestion.getValue().toString().toLowerCase()
					.contains("sug 002 - cancel policy")) {
				preauthDto.setIsCancelPolicy(true);
			}
			bean.setPedSuggestion(selected);
			bean.setPedName(txtNameOfPed.getValue());
			if(txtReviewRemarks != null && txtReviewRemarks.getValue() != null){
				bean.setReviewRemarks(txtReviewRemarks.getValue());
			}
			// pedQueryDTO.setNameofPED(txtNameOfPed.getValue());
			bean.setRemarks(txtRemarks.getValue());
			if (txtRepudiationLetterDate != null) {
				if (txtRepudiationLetterDate.getValue() != null) {
					bean
							.setRepudiationLetterDate(txtRepudiationLetterDate
									.getValue());
				}
			}
			bean.setViewPedTableDTO(initiatePEDEndorsementObj
					.getValues());
			
			bean.setDeletedDTO(initiatePEDEndorsementObj.deletedDTO);
			if(chkWatchList != null && chkWatchList.getValue() != null){
				bean.setIsAddWatchList(chkWatchList.getValue());
				
				/*if(chkWatchList.getValue() && txtWatchlistRemarks != null){
					bean.setWatchlistRemarks(txtWatchlistRemarks.getValue());
				}else{*/
					if(/*chkDiscuss != null && chkDiscuss.getValue() != null && chkDiscuss.getValue() &&*/ cmbDiscussWith != null && cmbDiscussWith.getValue() != null && txtSuggestion != null && txtSuggestion.getValue() != null){
						//bean.setIsDiscussed(chkDiscuss.getValue());
						bean.setDiscussRemarks(txtSuggestion.getValue());
						bean.setDiscussWith((SelectValue)cmbDiscussWith.getValue());
					}
				//}
				
			}else{
				if(/*chkDiscuss != null && chkDiscuss.getValue() != null && chkDiscuss.getValue() &&*/ cmbDiscussWith != null && cmbDiscussWith.getValue() != null && txtSuggestion != null && txtSuggestion.getValue() != null){
					//bean.setIsDiscussed(chkDiscuss.getValue());
					bean.setDiscussRemarks(txtSuggestion.getValue());
					bean.setDiscussWith((SelectValue)cmbDiscussWith.getValue());
				}
			}
			
			if(pedEffectiveFromDate != null && pedEffectiveFromDate.getValue() != null){
				bean.setPedEffFromDate(pedEffectiveFromDate.getValue());
			}
			
			Boolean validation = true;
			// if(null == pedQueryDTO.getViewPedTableDTO()){
			// validation=false;
			// for (ViewPEDTableDTO component :
			// pedQueryDTO.getViewPedTableDTO()) {
			// if(null== component.getIcdBlock() || null
			// ==component.getIcdChapter()||
			// null==component.getIcdCode()){
			// validation=true;
			// }
			// else
			// {
			// validation=true;
			// }
			// }
			// }

//			Preauth preauth = preauthService.getPreauthById(preauthKey);
			
			Preauth preauth = new Preauth();

			Intimation intimation = intimationService
					.getIntimationByKey(intimationKey);

			Policy policy = policyService.getPolicyByKey(policyKey);

			String userName = (String) getUI().getSession()
					.getAttribute(BPMClientContext.USERID);
			String passWord = (String) getUI().getSession()
					.getAttribute(BPMClientContext.PASSWORD);

			Claim claim = claimService.getClaimByKey(claimKey);
			if (validation) {
				
				bean.setStageKey(stageKey);
				
				Boolean result = pedQueryService.submitPedEndorsement(
						bean, preauthKey, intimation, policy,
						claim, userName, passWord,presenter);
				
				if(!bean.getIsEditPED()){
					pedQueryService.submitPEDTaskToDB(claim, intimation, bean);	
				}
				
				if(result){
					mainVertical.removeAllComponents();
					pedRequestDetailsTable.removeRow();
					pedRequestDetailsTable
					.setTableList(pedQueryService.pedInitiateDetails(claim.getKey()).getPageItems());
					intiatePEDEndorsement.select("No");
					
				}

				if (result) {
				successResult(dialog);
				} else {
					//Notification.show("Claim Record not Saved");
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					GalaxyAlertBox.createErrorBox("Claim Record not Saved", buttonsNamewithType);
			
				}
			} else {
				String eMsg = "Please Enter atleast One Record";
				/*Label label = new Label(eMsg, ContentMode.HTML);
				label.setStyleName("errMessage");
				VerticalLayout layout = new VerticalLayout();
				layout.setMargin(true);
				layout.addComponent(label);

				ConfirmDialog dialog1 = new ConfirmDialog();
				dialog1.setCaption("Errors");
				dialog1.setClosable(true);
				dialog1.setContent(layout);
				dialog1.setResizable(false);
				dialog1.setModal(true);
				dialog1.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
			}
		}
	}
	
	private void showErrorMessage(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	
	public  void remarksListener(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForPedRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForRemarks(searchField, getShortCutListenerForRemarks(searchField));
	    
	  }
	
	public  void handleShortcutForRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
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
	
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				//txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				//txtArea.setSizeFull();
				
				
				if(("pedRmrks").equalsIgnoreCase(txtFld.getId()) || ("watchListRmrks").equalsIgnoreCase(txtFld.getId()) || ("suggestionRmrks").equalsIgnoreCase(txtFld.getId())){
					txtArea.setRows(25);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					txtArea.setReadOnly(false);
				}
				
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(("pedRmrks").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
						}else if(("watchListRmrks").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
						}else if(("suggestionRmrks").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				
				String strCaption = "";
				
				if(("pedRmrks").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Remarks";
				}
			    else if(("watchListRmrks").equalsIgnoreCase(txtFld.getId())){
			    	strCaption = "Add to Watchlist and Discussed with Remarks";
			    }
			    else if(("suggestionRmrks").equalsIgnoreCase(txtFld.getId())){
			    	strCaption = "Discussed Remarks";
			    }
			   				    	
				dialog.setCaption(strCaption);
						
				
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);
				
				dialog.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}
	
	 @SuppressWarnings("unused")
		private void addPedEffFromDateListener(
				final DateField pedEffFromdate) {
	   		if (pedEffFromdate != null) {
	   			pedEffFromdate.addListener(new Listener() {
	   				private static final long serialVersionUID = -4865225814973226596L;

	   				@Override
	   				public void componentEvent(Event event) {
	   					DateField component = (DateField) event.getComponent();
	   					Date enteredDate = (Date) component.getValue();
	   					if(enteredDate != null) {
	   						Date policyFromDate = preauthDto.getNewIntimationDTO().getPolicy().getPolicyFromDate();
	   						Date policyToDate = preauthDto.getNewIntimationDTO().getPolicy().getPolicyToDate();
	   						
	   						
	   						
	   						if (!(enteredDate.after(policyFromDate) || enteredDate.compareTo(policyFromDate) == 0) || !(enteredDate.before(policyToDate) || enteredDate.compareTo(policyToDate) == 0)) {
	   							
	   							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	   							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	   							HashMap<String, Button> messageBoxButtons=GalaxyAlertBox.
	   							createErrorBox("PED Effective from date is not in range between Policy From Date and Policy To Date. </b>", buttonsNamewithType);
	   							
	   							 Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
	   							 okButton.addClickListener(new ClickListener() {
	   								private static final long serialVersionUID = -7148801292961705660L;

	   								@Override
	   								public void buttonClick(ClickEvent event) {
	   									
	   								}
	   							});
	   							
	   							VerticalLayout layout = new VerticalLayout(new Label("", ContentMode.HTML));
	   					
	   							
	   							pedEffFromdate.setValue(preauthDto.getDocRecievedDate());
	   						}
	   					}
	   					
	   					
	   				}
	   			});
	   		}
	   	}
	
}
