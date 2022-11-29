package com.shaic.claim.medical.opinion;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

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
import com.shaic.claim.ped.outsideprocess.InitiatePedPresenter;
import com.shaic.claim.pedquery.PEDQueryDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class RecordMarketingEscalationUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RecordMarkEscDTO bean;

	
	private BeanFieldGroup<RecordMarkEscDTO> binder;
	
	private ComboBox escalatedRole;
	
	private TextField escalatedBy;
	
	private ComboBox actionTaken;
			
	private DateField escalatedDate;
	
	private ComboBox reasonForEsc;
	
	private TextArea doctorRemarks;
	
	private Button submitBtn;
	
	private Button cancelBtn;
		
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private ClaimService claimService;
	
	@EJB
	DBCalculationService dbCalculationService;
	
	/*private CheckBox chkForMarketingPersonnel;*/
	
	private OptionGroup radioForMarketingPersonnel;
	
	public void init(RecordMarkEscDTO bean){
		
		this.bean = bean;
		
		this.binder = new BeanFieldGroup<RecordMarkEscDTO>(
				RecordMarkEscDTO.class);
		//this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		VerticalLayout buildPreauthSearchLayout = buildPreauthSearchLayout();
		
		setCompositionRoot(buildPreauthSearchLayout);
		
	}
	
	
	private VerticalLayout buildPreauthSearchLayout() {
		
		escalatedRole = binder.buildAndBind("Escalated Role",
				"escalatedRole", ComboBox.class);
		
		escalatedRole.setContainerDataSource(bean.getEscalatedRoleContainer());
		escalatedRole.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		escalatedRole.setItemCaptionPropertyId("value");
		escalatedRole.setNullSelectionAllowed(false);
		escalatedRole.setRequired(true);
		escalatedRole.setWidth("175px");
		//escalatedRole.setData(bean.getClaimDto().getNewIntimationDto().getKey());

		actionTaken = new ComboBox("Action Taken");
		//actionTaken.setContainerDataSource(bean.getEscalatedRoleContainer());
		actionTaken.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		actionTaken.setItemCaptionPropertyId("value");
		actionTaken.setNullSelectionAllowed(false);
		actionTaken.setRequired(true);
		actionTaken.setWidth("175px");
		
		//actionTaken.setContainerDataSource(bean.getPedSuggestionContainer());
		actionTaken.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		actionTaken.setItemCaptionPropertyId("value");
		actionTaken.setRequired(true);
		//actionTaken.setData(bean.getClaimDto().getNewIntimationDto().getKey());
		

		
		escalatedBy = binder.buildAndBind("Escalated by",
				"escalatedBy", TextField.class);
		escalatedBy.setNullRepresentation("");
		escalatedBy.setMaxLength(50);
		escalatedBy.setWidth("175px");
		
		reasonForEsc =new ComboBox("Reason for Escalation");
		reasonForEsc.setContainerDataSource(bean.getEscalationReasonContainer());
		reasonForEsc.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForEsc.setItemCaptionPropertyId("value");
		reasonForEsc.setRequired(true);
		reasonForEsc.setWidth("175px");
		//reasonForEsc.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		
		escalatedDate = (PopupDateField) binder.buildAndBind(
				"Escalated Date", "escalatedDate", PopupDateField.class);
		escalatedDate.setWidth("175px");
		
		/*referenceData.put("icdChapter",bean.getSelectIcdChapterContainer());
		referenceData.put("pedCode", bean.getSelectPedCodeContainer());
		referenceData.put("source", bean.getSelectSourceContainer());*/
		
		doctorRemarks = binder.buildAndBind("Doctor Remarks", "doctorRemarks", TextArea.class);
		doctorRemarks.setMaxLength(4000);
		doctorRemarks.setRequired(true);
		doctorRemarks.setWidth("175px");
		doctorRemarks.setId("escRmrks");
		remarksListener(doctorRemarks,null);
		doctorRemarks.setEnabled(false);
		doctorRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		
		/*chkForMarketingPersonnel= (CheckBox) binder.buildAndBind(" ","mrkPersonnel", CheckBox.class);
		HorizontalLayout MarketingPersonnelFLayout = new HorizontalLayout(chkForMarketingPersonnel);
		MarketingPersonnelFLayout.setCaption("Lack of Understanding of <br/>Marketing Personnel");
		MarketingPersonnelFLayout.setCaptionAsHtml(true);
		MarketingPersonnelFLayout.setMargin(false);
		chkForMarketingPersonnel.setReadOnly(true);*/
		
		radioForMarketingPersonnel = binder.buildAndBind("Lack of Understanding of Marketing Personnel","mrkPersonnel",OptionGroup.class);

		if(reasonForEsc != null){
			SelectValue reasonForEscValue = (SelectValue)reasonForEsc.getValue();
			if(reasonForEscValue != null){
				if((reasonForEscValue.getId().equals(ReferenceTable.DENIAL_RECONSIDERATION_KEY) || reasonForEscValue.getId().equals(ReferenceTable.REJECTION_RECONSIDERATION_KEY)
						|| reasonForEscValue.getId().equals(ReferenceTable.DEDUCTION_RECONSIDERATION_KEY))){
					radioForMarketingPersonnel.setRequired(true);
				}else{
					radioForMarketingPersonnel.setRequired(false);
				}
			}
		}
		
		//bean.setMrkPersonnelFlag("N/A");


		//chkWatchList = binder.buildAndBind("Add to Watchlist", "isAddWatchList", CheckBox.class);
		//Vaadin8-setImmediate() chkWatchList.setImmediate(true);
		
		/*txtWatchlistRemarks = binder.buildAndBind("Add to Watchlist and Discussed with Remarks", "watchlistRemarks", TextArea.class);
		txtWatchlistRemarks.setMaxLength(4000);
		//Vaadin8-setImmediate() txtWatchlistRemarks.setImmediate(true);
		
		txtWatchlistRemarks.setId("watchListRmrks");
		remarksListener(txtWatchlistRemarks,null);
		txtWatchlistRemarks.setData(bean);
		txtWatchlistRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");*/
		
		//fireViewEvent(InitiatePedPresenter.GET_PED_REVIEWER, bean);
		
		/*if(this.bean.getIsWatchListReviewer() != null && !this.bean.getIsWatchListReviewer()){
			if(this.chkWatchList != null){
				if(this.chkWatchList.getValue() != null && this.chkWatchList.getValue()){
					mandatoryFields.add(txtWatchlistRemarks);
				}	
			}
		}*/
		
        submitBtn=new Button();
        submitBtn.setCaption("Submit");
        
        cancelBtn=new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		
		/*sendUnderWrtingBtn=new Button("Submit to PED Department");
		sendUnderWrtingBtn.setVisible(false);
		
		if(this.bean.getIsWatchListReviewer() != null && !this.bean.getIsWatchListReviewer()){
			sendUnderWrtingBtn.setVisible(true);
		}else{
			sendUnderWrtingBtn.setVisible(false);
		}
		*/
		HorizontalLayout btnHorizontal = new HorizontalLayout(submitBtn,cancelBtn);
		btnHorizontal.setSpacing(true);
		
		addListener();
		//intimatePEDfield=new FormLayout(cmbInsuredName,cmbPedSuggestion,txtNameOfPed,txtRemarks);
		
		/*FormLayout watchListLayout = new FormLayout(chkWatchList,txtWatchlistRemarks); 
		HorizontalLayout hLayout = new HorizontalLayout(intimatePEDfield, watchListLayout);
		hLayout.setSpacing(true);
		hLayout.setMargin(true);*/
		
		FormLayout vLayout = new FormLayout(escalatedRole,escalatedBy,actionTaken,radioForMarketingPersonnel);
		
		FormLayout vLayout1 = new FormLayout(escalatedDate,reasonForEsc,doctorRemarks);

			
		HorizontalLayout intimatePEDFLayout=new HorizontalLayout(vLayout,vLayout1);
		
		intimatePEDFLayout.setSpacing(true);

		//intimatePEDFLayout.setComponentAlignment(initiatePEDEndorsementObj, Alignment.BOTTOM_LEFT);
		VerticalLayout mainLayout = new VerticalLayout(intimatePEDFLayout,btnHorizontal);
		
		mainLayout.setComponentAlignment(btnHorizontal, Alignment.BOTTOM_CENTER);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		mandatoryFields.add(escalatedBy);
		mandatoryFields.add(escalatedDate);
		mandatoryFields.add(escalatedRole);
		mandatoryFields.add(actionTaken);
		mandatoryFields.add(reasonForEsc);
		mandatoryFields.add(doctorRemarks);
		
		showOrHideValidation(false);
		
		return mainLayout;
	}
	
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	public void addListener() {
		
		reasonForEsc.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				ComboBox reasonForEscCmb = (ComboBox)event.getProperty();
				
				SelectValue suggestionSelect = reasonForEscCmb.getValue() != null ? (SelectValue)reasonForEscCmb.getValue() : null;
				
				/*if(suggestionSelect.getValue() != null && (suggestionSelect.getValue().equals("Deduction Reconsideration") || suggestionSelect.getValue().equals("Denial Reconsideration") || suggestionSelect.getValue().equals("Rejection Reconsideration"))){
					chkForMarketingPersonnel.setReadOnly(false);
					bean.setMrkPersonnel(Boolean.FALSE);
				}else{
					chkForMarketingPersonnel.setReadOnly(false);
					chkForMarketingPersonnel.setValue(null);
					bean.setMrkPersonnelFlag("N/A");
					chkForMarketingPersonnel.setReadOnly(true);
				}*/
				if(reasonForEsc != null){
					SelectValue reasonForEscValue = (SelectValue)reasonForEsc.getValue();
					if(reasonForEscValue != null){
						if((reasonForEscValue.getId().equals(ReferenceTable.DENIAL_RECONSIDERATION_KEY) || reasonForEscValue.getId().equals(ReferenceTable.REJECTION_RECONSIDERATION_KEY)
								|| reasonForEscValue.getId().equals(ReferenceTable.DEDUCTION_RECONSIDERATION_KEY))){
							radioForMarketingPersonnel.setRequired(true);
						}else{
							radioForMarketingPersonnel.setRequired(false);
						}
					}
				}
				if(suggestionSelect != null && suggestionSelect.getId() != null){
					
					BeanItemContainer<SelectValue> actionTakenCmb = dbCalculationService.getActionTakenValue(suggestionSelect.getId());
					if(actionTakenCmb != null)
					{
						actionTaken.setContainerDataSource(actionTakenCmb);
					}

				VerticalLayout layout = new VerticalLayout(new Label("", ContentMode.HTML));
						
						event.getProperty().setValue(reasonForEscCmb);
					}
				else{
					actionTaken.setContainerDataSource(null);
				}
				}
				
		});
		
		actionTaken.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				ComboBox actionTakenCmb = (ComboBox)event.getProperty();

				SelectValue actionTakenSelect = actionTakenCmb.getValue() != null ? (SelectValue)actionTakenCmb.getValue() : null;
				if(actionTakenSelect != null && actionTakenSelect.getId() != null){

					doctorRemarks.setEnabled(true);						
				}
				else{
					doctorRemarks.setEnabled(false);
				}
			}

		});

		
		escalatedDate.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Date enteredDate = (Date) event.getProperty().getValue();
				if(enteredDate != null) {
					Date   toDate= new Date();
					
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // declare as DateFormat
					Calendar today = Calendar.getInstance();
					Calendar yesterday = Calendar.getInstance();
					yesterday.add(Calendar.DATE, -2);
					Date d = yesterday.getTime();
					Date fromDate = d;
					
					
					
					if (!(enteredDate.after(fromDate) || enteredDate.compareTo(fromDate) == 0) || !(enteredDate.before(toDate) || enteredDate.compareTo(toDate) == 0)) {
						
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						HashMap<String, Button> messageBoxButtons=GalaxyAlertBox.
						createErrorBox("Escalation Date is not in range, Please select Valid Date. </b>", buttonsNamewithType);
						
						 Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
						 okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;

							@Override
							public void buttonClick(ClickEvent event) {
								
							}
						});
						
						VerticalLayout layout = new VerticalLayout(new Label("", ContentMode.HTML));
				
						
						event.getProperty().setValue(null);
					}
				}
				
				
			}
		});
		
		submitBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println(bean);
				submitPed(null);
			}
		});

		cancelBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	clearObject();
				                	fireViewEvent(MenuItemBean.RECORD_MARKETING_ESCALATION, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
		
		radioForMarketingPersonnel
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean dvOption = (Boolean)event.getProperty().getValue();
				if(dvOption){
                	bean.setMrkPersonnel(dvOption);
                	bean.setMrkPersonnelFlag("YES");
                } else {
                	bean.setMrkPersonnel(dvOption);
                	bean.setMrkPersonnelFlag("NO");
                }
				
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
		
		if(this.escalatedRole != null && (this.escalatedRole.getValue() == null)){
			hasError = true;
			eMsg.append("Please select Escalated Role. </br>"); 
		}
		
		if(this.escalatedDate != null && (this.escalatedDate.getValue() == null)){
			hasError = true;
			eMsg.append("Please select Escalated Date. </br>"); 
		}
		
		if(this.escalatedBy != null && (this.escalatedBy.getValue() == null || (this.escalatedBy.getValue().trim()).isEmpty())){
			hasError = true;
			eMsg.append("Escalated by not entered. </br>"); 
		}
		
		if(this.reasonForEsc != null && (this.reasonForEsc.getValue() == null)){
			hasError = true;
			eMsg.append("Please Selected Escalated Reason. </br>"); 
		}
		
		if(this.actionTaken != null && (this.actionTaken.getValue() == null)){
			hasError = true;
			eMsg.append("Please Selected Action Taken. </br>"); 
		}
		
		if(this.doctorRemarks != null && (this.doctorRemarks.getValue() == null  || (this.doctorRemarks.getValue().trim()).isEmpty())){
			hasError = true;
			eMsg.append("Please Enter Remarks. </br>"); 
		}

		if(this.reasonForEsc != null && (this.reasonForEsc.getValue() != null)){

			SelectValue reasonForEscValue = (SelectValue)reasonForEsc.getValue();
			if(reasonForEscValue != null){
				if((reasonForEscValue.getId().equals(ReferenceTable.DENIAL_RECONSIDERATION_KEY) || reasonForEscValue.getId().equals(ReferenceTable.REJECTION_RECONSIDERATION_KEY)
						|| reasonForEscValue.getId().equals(ReferenceTable.DEDUCTION_RECONSIDERATION_KEY)) &&  this.radioForMarketingPersonnel != null && this.radioForMarketingPersonnel.getValue() == null ){
					hasError = true;
					eMsg.append("Please Select Lack of understanding of marketing personnel. </br>"); 
				}
			}

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
		} else{
			
			try {
				//this.binder.commit();
				
				if(escalatedRole != null && escalatedRole.getValue() != null){
					bean.setEscalatedRole((escalatedRole.getValue()).toString());
				}
				if(escalatedBy != null && escalatedBy.getValue() != null){
					bean.setEscalatedBy(((escalatedBy.getValue()).trim()).toString());
				}
				if(escalatedDate != null && escalatedDate.getValue() != null){
					bean.setEscalatedDate(escalatedDate.getValue());
				}
				if(reasonForEsc != null && reasonForEsc.getValue() != null){
					SelectValue selectValueReasonForEsc = (SelectValue)reasonForEsc.getValue();
					bean.setEscalationReason((selectValueReasonForEsc.getId()));
				}
				if(actionTaken != null && actionTaken.getValue() != null){
					SelectValue selectValueActionTaken = (SelectValue)actionTaken.getValue();
					bean.setActionTaken(selectValueActionTaken.getId());
				}
				if(doctorRemarks != null && doctorRemarks.getValue() != null){
					bean.setDoctorRemarks(((doctorRemarks.getValue().trim())).toString());
				}
					
				bean.setIntimationNumber(bean.getPreauthDto().getNewIntimationDTO().getIntimationId());
				
			} catch (Exception e) {
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
	
	private void buildSendToUnderWriting(Boolean isDisscussed){/*
		
		//unbindField(chkDiscuss);
		unbindField(cmbDiscussWith);
		unbindField(txtSuggestion);
		
		//mandatoryFields.remove(chkDiscuss);
		mandatoryFields.remove(cmbDiscussWith);
		mandatoryFields.remove(txtSuggestion);
		
		chkDiscuss = binder.buildAndBind("Discussed with PED Team Leader", "isDiscussed", CheckBox.class);
		//Vaadin8-setImmediate() chkDiscuss.setImmediate(true);
		
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
		
		FormLayout fLayout = new FormLayout(chkDiscuss,cmbDiscussWith,txtSuggestion);
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
	*/}
	
	/*private Button getCancelButton(final ConfirmDialog dialog) {
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
	}*/
	
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
	
	private void getSubmitButtonWithListener(final ConfirmDialog dialog, final Boolean isDiscussed) {/*
		uwSubmitBtn = new Button("Submit");
		uwSubmitBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		uwSubmitBtn.addClickListener(new Button.ClickListener() {
			*//**
			 * 
			 *//*
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
		
		
	*/}
	
	private void submitPed(final ConfirmDialog dialog){

		if (validatePage()) {
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord = (String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			
			bean.setUsername(userName);
			bean.setPassword(passWord);
			if(dialog != null){
				dialog.close();
			}
			
			fireViewEvent(RecMarketingEscalationPresenter.SUBMIT_DATA, bean);
			
			
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
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				dialog.setHeight("75%");
		    	dialog.setWidth("65%");
				txtArea.setReadOnly(false);
				//txtArea.setSizeFull();
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
						
					}
				});
									
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
							   				    	
				dialog.setCaption("Doctor Remarks");
						
				
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

	public void resetSuggestionCmb(){/*
		cmbPedSuggestion.setValue(null);	
	*/}
	
	 public void clearObject() {
		 SHAUtils.setClearReferenceData(referenceData);
			if(this.bean!=null){
				this.bean=null;
			}
		
	 	}
}
