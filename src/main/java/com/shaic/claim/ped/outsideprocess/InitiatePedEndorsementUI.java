package com.shaic.claim.ped.outsideprocess;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.pedquery.PEDQueryDTO;
import com.shaic.claim.pedrequest.teamlead.PEDRequestDetailsTeamLeadPresenter;
import com.shaic.claim.pedrequest.view.ViewPEDRequestPresenter;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Speciality;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class InitiatePedEndorsementUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PEDQueryDTO bean;

	
	private BeanFieldGroup<PEDQueryDTO> binder;
	
	private ComboBox cmbInsuredName;
	
	private ComboBox cmbPedSuggestion;
	
	private TextField txtNameOfPed;
	
	private TextArea txtRemarks;
	
	private DateField txtRepudiationLetterDate;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private FormLayout intimatePEDfield;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@Inject
	private Instance<InitiatePedEndorsmentOutsideProcessTable> initiatePEDEndorsementTable;
	
	private InitiatePedEndorsmentOutsideProcessTable initiatePEDEndorsementObj;
	
	//private String presenterString = "";
	private CheckBox chkWatchList;
	
	//private TextArea txtWatchlistRemarks;
	
	private Button sendUnderWrtingBtn;
	
	//private CheckBox chkDiscuss;
	
	private ComboBox cmbDiscussWith;
	
	private TextArea txtSuggestion;
	
	private Button uwCancelBtn;
	
	private Button uwSubmitBtn;
	
	private DateField pedEffectiveFromDate;
	
	private ClaimService claimService;
	
	DBCalculationService dbCalculationService;
	
	public void init(PEDQueryDTO bean){
		
		
		
		
		this.bean = bean;
		
		if(bean.getIsPolicyValidate()){		
			policyValidationPopupMessage();
		}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
	    }
		
		this.binder = new BeanFieldGroup<PEDQueryDTO>(
				PEDQueryDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		VerticalLayout buildPreauthSearchLayout = buildPreauthSearchLayout();
		
		setCompositionRoot(buildPreauthSearchLayout);
		
	}
	
	
	private VerticalLayout buildPreauthSearchLayout() {
		
		cmbInsuredName = binder.buildAndBind("Risk Name",
				"insuredName", ComboBox.class);
		
		cmbInsuredName.setContainerDataSource(bean.getInsuredDetailsContainer());
		cmbInsuredName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInsuredName.setItemCaptionPropertyId("value");
		cmbInsuredName.setNullSelectionAllowed(false);
		cmbInsuredName.setRequired(true);
		cmbInsuredName.setData(bean.getClaimDto().getNewIntimationDto().getKey());

		cmbPedSuggestion = binder.buildAndBind("PED Suggestion",
				"pedSuggestion", ComboBox.class);
		
		cmbPedSuggestion.setContainerDataSource(bean.getPedSuggestionContainer());
		cmbPedSuggestion.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPedSuggestion.setItemCaptionPropertyId("value");
//		cmbPedSuggestion.setNullSelectionAllowed(false);
		cmbPedSuggestion.setRequired(true);
		cmbPedSuggestion.setData(bean.getClaimDto().getNewIntimationDto().getKey());
		
		if(this.bean.getPedSuggestion() != null){
			cmbPedSuggestion.setValue(this.bean.getPedSuggestion());
		}
		
		txtNameOfPed = binder.buildAndBind("Name of PED",
				"pedName", TextField.class);
		txtNameOfPed.setNullRepresentation("");
		txtNameOfPed.setMaxLength(100);
		//Vaadin8-setImmediate() txtNameOfPed.setImmediate(true);
		//txtNameOfPed.setRequired(true);
		
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
		
		//referenceData.put("icdCode", selectValueContainer);
		referenceData.put("icdChapter",bean.getSelectIcdChapterContainer());
		referenceData.put("pedCode", bean.getSelectPedCodeContainer());
		referenceData.put("source", bean.getSelectSourceContainer());

		initiatePEDEndorsementObj=initiatePEDEndorsementTable.get();
		
		initiatePEDEndorsementObj.init(SHAConstants.PED_INITIATE_OUTSIDE_PROCESS);
		initiatePEDEndorsementObj.setReferenceData(referenceData);
		initiatePEDEndorsementObj.setIntimationKey(bean.getClaimDto().getNewIntimationDto().getKey());
		
//		if(tableList != null && ! tableList.isEmpty()){
//			for (ViewPEDTableDTO viewPEDTableDTO : tableList) {
//				initiatePEDEndorsementObj.addBeanToList(viewPEDTableDTO);
//			}
//		}
		chkWatchList = binder.buildAndBind("Add to Watchlist", "isAddWatchList", CheckBox.class);
		//Vaadin8-setImmediate() chkWatchList.setImmediate(true);
		
		/*txtWatchlistRemarks = binder.buildAndBind("Add to Watchlist and Discussed with Remarks", "watchlistRemarks", TextArea.class);
		txtWatchlistRemarks.setMaxLength(4000);
		//Vaadin8-setImmediate() txtWatchlistRemarks.setImmediate(true);
		
		txtWatchlistRemarks.setId("watchListRmrks");
		remarksListener(txtWatchlistRemarks,null);
		txtWatchlistRemarks.setData(bean);
		txtWatchlistRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");*/
		
		fireViewEvent(InitiatePedPresenter.GET_PED_REVIEWER, bean);
		
		/*if(this.bean.getIsWatchListReviewer() != null && !this.bean.getIsWatchListReviewer()){
			if(this.chkWatchList != null){
				if(this.chkWatchList.getValue() != null && this.chkWatchList.getValue()){
					mandatoryFields.add(txtWatchlistRemarks);
				}	
			}
		}*/
		
        submitBtn=new Button();
        if(this.bean.getIsWatchListReviewer() != null && !this.bean.getIsWatchListReviewer()){
        submitBtn.setCaption("Escalate to Cluster Head");}
        else{
        	submitBtn.setCaption("Submit");
        }
        
        cancelBtn=new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		
		sendUnderWrtingBtn=new Button("Submit to PED Department");
		sendUnderWrtingBtn.setVisible(false);
		
		if(this.bean.getIsWatchListReviewer() != null && !this.bean.getIsWatchListReviewer()){
			sendUnderWrtingBtn.setVisible(true);
		}else{
			sendUnderWrtingBtn.setVisible(false);
		}
		
		HorizontalLayout btnHorizontal = new HorizontalLayout(sendUnderWrtingBtn,submitBtn,cancelBtn);
		btnHorizontal.setSpacing(true);
		
		addListener();
		intimatePEDfield=new FormLayout(cmbInsuredName,cmbPedSuggestion,txtNameOfPed,txtRemarks);
		
		FormLayout watchListLayout = new FormLayout(chkWatchList/*,txtWatchlistRemarks*/); 
		HorizontalLayout hLayout = new HorizontalLayout(intimatePEDfield, watchListLayout);
		hLayout.setSpacing(true);
		hLayout.setMargin(true);
		
		VerticalLayout intimatePEDFLayout = null;

			
		intimatePEDFLayout=new VerticalLayout(hLayout,initiatePEDEndorsementObj,btnHorizontal);

		intimatePEDFLayout.setComponentAlignment(initiatePEDEndorsementObj, Alignment.BOTTOM_LEFT);
		intimatePEDFLayout.setComponentAlignment(btnHorizontal, Alignment.BOTTOM_CENTER);
		intimatePEDFLayout.setSpacing(true);
		intimatePEDFLayout.setMargin(true);
		
		mandatoryFields.add(cmbInsuredName);
		mandatoryFields.add(cmbPedSuggestion);
		//mandatoryFields.add(txtNameOfPed);
		mandatoryFields.add(txtRemarks);
		mandatoryFields.add(pedEffectiveFromDate);
		
		showOrHideValidation(false);
		
		return intimatePEDFLayout;
	}
	
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	public void addListener() {
		
		cmbInsuredName.addValueChangeListener(new ValueChangeListener() {

		@Override
		public void valueChange(ValueChangeEvent event) {
			
			ComboBox insuredCmb = (ComboBox)event.getProperty();
			SelectValue suggestionSelect = cmbPedSuggestion.getValue() != null ? (SelectValue)cmbPedSuggestion.getValue() : null;
		
			Long insuredKey = insuredCmb.getValue() != null ? (Long) ((SelectValue)insuredCmb.getValue()).getId() : null;  
			
			if(insuredCmb.getValue() != null) {
				fireViewEvent(InitiatePedPresenter.CHECK_INSURED_STATUS, insuredKey);
			}
			
			if(insuredCmb.getValue() != null && suggestionSelect != null && suggestionSelect.getId() != null){

				Long intimationKey = (Long) insuredCmb.getData();
			
				if(initiatePEDEndorsementObj != null){
					initiatePEDEndorsementObj.setSuggestionKey(suggestionSelect.getId());
					initiatePEDEndorsementObj.setInsuredKey(insuredKey);
				}

				if(ReferenceTable.PED_SUGGESTION_SUG002.equals(suggestionSelect.getId()) 
						|| ReferenceTable.PED_SUGGESTION_SUG003.equals(suggestionSelect.getId())
						|| ReferenceTable.PED_SUGGESTION_SUG004.equals(suggestionSelect.getId())
						|| ReferenceTable.PED_SUGGESTION_SUG005.equals(suggestionSelect.getId())
						|| ReferenceTable.PED_SUGGESTION_SUG006.equals(suggestionSelect.getId())
						|| ReferenceTable.PED_SUGGESTION_SUG010.equals(suggestionSelect.getId())){
					
					fireViewEvent(InitiatePedPresenter.GET_PED_ALREADY_AVAILABLE_INITIATOR, suggestionSelect.getId(), intimationKey, insuredKey);
				}
				else{
					if(initiatePEDEndorsementObj != null)
						initiatePEDEndorsementObj.checkPEDAlreadyAvailableBySuggestion(suggestionSelect.getId());
				}
				
			}	
		}
	});
		

		cmbPedSuggestion.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				ComboBox suggestionCmb = (ComboBox)event.getProperty();
				Long intimationKey = (Long) suggestionCmb.getData();
				
				SelectValue suggestionSelect = suggestionCmb.getValue() != null ? (SelectValue)suggestionCmb.getValue() : null;
				if(suggestionSelect != null && suggestionSelect.getId() != null){
				
					if (cmbPedSuggestion.getValue().toString().toLowerCase()
							.contains("sug 002 - cancel policy")) {
						
						//Long policyKey = bean.getPolicyDto().getKey();
						
						//Long insuredKey = bean.getClaimDto().getNewIntimationDto().getInsuredKey();
						
						String policyInsuredAgeingFlag = bean.getPolicyInsuredAgeingFlag();
								//dbCalculationService.getPolicyInsuredAgeingFlag(policyKey,insuredKey);
						
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
							submitBtn.setEnabled(true);
							sendUnderWrtingBtn.setEnabled(true);
							submitBtn.setEnabled(true);
							sendUnderWrtingBtn.setEnabled(true);
						txtRepudiationLetterDate.setValue(null);
						intimatePEDfield.removeComponent(txtRepudiationLetterDate);
						mandatoryFields.remove(txtRepudiationLetterDate);
						unbindField(txtRepudiationLetterDate);
						}
					}
				
					if(cmbPedSuggestion != null && cmbPedSuggestion.getValue() != null && ((SelectValue)cmbPedSuggestion.getValue()).getId().equals(ReferenceTable.PED_SUGGESTION_SUG004)){
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
					
//					if(ReferenceTable.PED_SUGGESTION_SUG002.equals(suggestionSelect.getId()) || ReferenceTable.PED_SUGGESTION_SUG004.equals(suggestionSelect.getId())){
//						fireViewEvent(InitiatePedPresenter.GET_PED_ALREADY_AVAILABLE_INITIATOR, suggestionSelect.getId(), intimationKey);
					
					SelectValue insuredSelect = (SelectValue)cmbInsuredName.getValue();
					initiatePEDEndorsementObj.setInsuredKey(insuredSelect.getId());

					if(initiatePEDEndorsementObj != null){
						initiatePEDEndorsementObj.setSuggestionKey(suggestionSelect.getId());
					}

					if(ReferenceTable.PED_SUGGESTION_SUG002.equals(suggestionSelect.getId()) 
							|| ReferenceTable.PED_SUGGESTION_SUG003.equals(suggestionSelect.getId())
							|| ReferenceTable.PED_SUGGESTION_SUG004.equals(suggestionSelect.getId())
							|| ReferenceTable.PED_SUGGESTION_SUG005.equals(suggestionSelect.getId())
							|| ReferenceTable.PED_SUGGESTION_SUG006.equals(suggestionSelect.getId())
							|| ReferenceTable.PED_SUGGESTION_SUG010.equals(suggestionSelect.getId())){
						
						fireViewEvent(InitiatePedPresenter.GET_PED_ALREADY_AVAILABLE_INITIATOR, suggestionSelect.getId(), intimationKey, insuredSelect.getId());
					}
					else{
						if(initiatePEDEndorsementObj != null)
							initiatePEDEndorsementObj.checkPEDAlreadyAvailableBySuggestion(suggestionSelect.getId());
					}
					
					if(ReferenceTable.PED_SUGGESTION_SUG001.equals(suggestionSelect.getId())
							|| ReferenceTable.PED_SUGGESTION_SUG005.equals(suggestionSelect.getId()))
					{
						if(pedEffectiveFromDate != null){
							unbindField(pedEffectiveFromDate);
							intimatePEDfield.removeComponent(pedEffectiveFromDate);
							mandatoryFields.remove(pedEffectiveFromDate);
						}
						
						pedEffectiveFromDate = (PopupDateField) binder.buildAndBind(
								"PED Effective from date", "pedEffFromDate", PopupDateField.class);
						intimatePEDfield.addComponent(pedEffectiveFromDate);
						mandatoryFields.add(pedEffectiveFromDate);
						pedEffectiveFromDate.setValue(bean.getDocRecievedDate());
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
		
		pedEffectiveFromDate.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Date enteredDate = (Date) event.getProperty().getValue();
				if(enteredDate != null) {
					Date policyFromDate = bean.getPolicyDto().getPolicyFromDate();
					Date policyToDate = bean.getPolicyDto().getPolicyToDate();
					
					
					
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
				
						
						event.getProperty().setValue(bean.getDocRecievedDate());
					}
				}
				
				
			}
		});
		
		submitBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				submitPed(null);
			}
		});

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
				                	clearObject();
				                	fireViewEvent(MenuItemBean.PED_INITIATE, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
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
							//mandatoryFields.add(txtWatchlistRemarks);
							showOrHideValidation(false);
							buildSendToUnderWriting(Boolean.FALSE);
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
		
		
		sendUnderWrtingBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				buildSendToUnderWriting(Boolean.TRUE);
				
			}
		});
	

	}
	
	private void unbindField(Field<?> field) {
		if (field != null) {
			if(binder.getPropertyId(field)!=null){
				this.binder.unbind(field);
			}
			
		}
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		SelectValue sugSelected = cmbPedSuggestion != null ? (SelectValue) cmbPedSuggestion.getValue() : null;
		Boolean isSumInsuredLocked = Boolean.FALSE;
		if(sugSelected != null && sugSelected.getId().equals(ReferenceTable.PED_SUGGESTION_SUG004)){
			isSumInsuredLocked = Boolean.TRUE;
		}
		
		if(sugSelected != null && sugSelected.getId().equals(ReferenceTable.PED_SUGGESTION_SUG010)){
			isSumInsuredLocked = Boolean.TRUE; //For Sug-10  Diag. Table to Be Disabled CR R1212
		}
		
		if(!isSumInsuredLocked && this.txtNameOfPed != null && (this.txtNameOfPed.getValue() == null || this.txtNameOfPed.getValue().isEmpty())){
			hasError = true;
			eMsg.append("Please Enter Ped Name. </br>"); 
		}
		
		/*if(!isSumInsuredLocked && this.txtRemarks != null && (this.txtRemarks.getValue() == null || this.txtRemarks.getValue().isEmpty())){
			hasError = true;
			eMsg.append("Please Enter Remarks. </br>"); 
		}*/
		
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
		
		if(!isSumInsuredLocked){
			if(this.initiatePEDEndorsementObj != null && this.initiatePEDEndorsementObj.getValues().isEmpty()) {
				hasError = true;
				eMsg.append("Please Add Atleast one diagnosis List Details to Proceed Further. </br>"); 
			}
		}
		
		
		
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
				if(this.chkWatchList != null && this.chkWatchList.getValue() != null && this.chkWatchList.getValue() && (this.txtWatchlistRemarks != null && (this.txtWatchlistRemarks.getValue() == null || this.txtWatchlistRemarks.getValue().isEmpty()))){
					hasError = true;
					eMsg.append("Please Enter Remarks for Add to Watchlist & Discussed with. </br>");
				}	
		}*/
		
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
					bean.setViewPedTableDTO(initiatePEDEndorsementObj
							.getValues());			
				}
				
				if(chkWatchList != null && chkWatchList.getValue() != null){
					bean.setIsAddWatchList(chkWatchList.getValue());
					
					/*if(chkWatchList.getValue() && txtWatchlistRemarks != null){
						bean.setWatchlistRemarks(txtWatchlistRemarks.getValue());
					}else{*/
						if(/*chkDiscuss != null && chkDiscuss.getValue() != null && chkDiscuss.getValue() && */cmbDiscussWith != null && cmbDiscussWith.getValue() != null && txtSuggestion != null && txtSuggestion.getValue() != null){
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
	

	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer) {
	
		initiatePEDEndorsementObj.setIcdBlock(icdBlockContainer);
	}

	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer) {
		
		initiatePEDEndorsementObj.setIcdCode(icdCodeContainer);
		
	}

	public void setPEDCode(String icdCodeContainer) {
		
		initiatePEDEndorsementObj.setPEDCode(icdCodeContainer);
		
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
		
		cmbDiscussWith.setContainerDataSource(bean.getPedTlContainer());
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
		actionLayout.addComponents(uwSubmitBtn,getCancelButton(dialog));
		mainLayout.addComponent(actionLayout);
		
		mainLayout.setComponentAlignment(fLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(actionLayout, Alignment.BOTTOM_CENTER);
		mainLayout.setSpacing(true);
		
		showInPopup(mainLayout, dialog);
	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		uwCancelBtn = new Button("Cancel");
		uwCancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);
		uwCancelBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				//unbindField(chkDiscuss);
				unbindField(cmbDiscussWith);
				unbindField(txtSuggestion);
				
				//mandatoryFields.remove(chkDiscuss);
				mandatoryFields.remove(cmbDiscussWith);
				mandatoryFields.remove(txtSuggestion);
				
				chkWatchList.setEnabled(true);
				
				dialog.close();
				//binder = null;
			}
		});
		return uwCancelBtn;
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
	
	private void getSubmitButtonWithListener(final ConfirmDialog dialog, final Boolean isDiscussed) {
		uwSubmitBtn = new Button("Submit");
		uwSubmitBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		uwSubmitBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsDiscussed(isDiscussed);
				if(bean.getIsDiscussed()){
					chkWatchList.setValue(Boolean.FALSE);
					chkWatchList.setEnabled(false);
				}
				submitPed(dialog);
			}

		});
		
		
	}
	
	private void submitPed(final ConfirmDialog dialog){

		if (validatePage()) {
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord = (String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			bean.setUsername(userName);
			bean.setPassword(passWord);
			
			if(dialog != null){
				dialog.close();
			}
			
			fireViewEvent(InitiatePedPresenter.SUBMIT_DATA, bean);
			
			
		}
	
		
	}
	
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

	public void resetSuggestionCmb(){
		cmbPedSuggestion.setValue(null);	
	}
	
	public void resetPEDDetailsTable(ViewPEDTableDTO newInitiatePedDTO){
		
		initiatePEDEndorsementObj.resetPEDDetailsTable(newInitiatePedDTO);
	}
	
	 public void clearObject() {
		 SHAUtils.setClearReferenceData(referenceData);
			if(initiatePEDEndorsementObj!=null){
				initiatePEDEndorsementObj.clearObject();
				initiatePEDEndorsementObj=null;
			}
			if(this.bean!=null){
				this.bean=null;
			}
		
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
	   						Date policyFromDate = bean.getPolicyDto().getPolicyFromDate();
	   						Date policyToDate = bean.getPolicyDto().getPolicyToDate();
	   						
	   						
	   						
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
	   					
	   							
	   							pedEffFromdate.setValue(bean.getDocRecievedDate());
	   						}
	   					}
	   					
	   					
	   				}
	   			});
	   		}
	   	}
}
