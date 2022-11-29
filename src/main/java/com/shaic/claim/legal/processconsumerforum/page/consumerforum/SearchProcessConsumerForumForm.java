/**
 * 
 */
package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import groovy.swing.factory.BindFactory;

import java.awt.Checkbox;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
//import com.google.gwt.thirdparty.javascript.jscomp.CodingConvention.Bind;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.domain.Claim;
import com.shaic.domain.LegalConsumer;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.State;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Validator;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
public class SearchProcessConsumerForumForm  extends ViewComponent{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6589043418245772350L;

	@EJB
	private MasterService masterService;
	
	@Inject
	private ConsumerFormSearchUI consumerFormSearchUI;
	
	@Inject
	private ViewDetails viewDetails;
	
	protected Button btnSave;
	protected Button btnSaveExit;
	protected Button btnCancel;
	
	private BeanFieldGroup<ConsumerForumDTO> consumerBinder;
	
	private BeanFieldGroup<IntimationDetailsDTO> intimationBinder;
	
	private BeanFieldGroup<CaseDetailsDTO> caseDetailBinder;
	
	private BeanFieldGroup<OrderDetailsDTO> orderDetailBinder;
	
	private BeanFieldGroup<OutOfCourtSettlementDTO> outofsettlementBinder;
	
	Boolean isSatisfy = false ;
	
	
	private BeanItemContainer<SelectValue> depAmtMasterValueByCode = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private BeanItemContainer<SelectValue> statusCase = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private TextField txtIntimationNo;
	private Button btnIntimationSearch;
	private TextField txtPolicyNo;
	private ComboBox cbxRepudiation;
	private TextField txtprovisionAmt;
	private TextField txtProductNameCode;
	private TextField txtInsuredName;
	private TextField txtfinancialYear;
	
	
	private TextField txtdcdrf;
	private TextField txtadvocatename;
	private TextField txtcomplainanceAmt;
	private TextField txtccNo;
	private ComboBox cmbzone;
	private ComboBox cmbstate;
	private DateField datecomplaintDate;
	private OptionGroup chkLegalLock;
	//private CheckBox chkLegalCompleted;
	
//	private ComboBox cmborder;
	private ComboBox cmbresultorder;
	private DateField dateOrder;
	private DateField dateReceipt;
	private DateField datelimitation;
	
	private CheckBox chkawardagainstus;
	//private OptionGroup optsatify;
	private OptionGroup optappeal;
	
	private TextField txtAmount;
	private TextField txtCompensation;
	private TextField txtLitigation;
	private TextField txtAwardReason;
	private TextField txtInterestHistory;
	
	//private ComboBox cmbSource;
	//private CheckBox chkUpdateOftheCase;
	private ComboBox cmbCaseUpdate;
	private TextField txtRecordLastUpdatedRemarks;
	private DateField dateFreshPreviousDateHearing;
	private DateField dateDateofNextHearing;
	private VerticalLayout finalLayout ;
	
	
	private CheckBox chkStateCommision;
	private CheckBox chkNationalCommision;
	private CheckBox chkSupremeCourt;
	private CheckBox chkMandatoryDeposit;
	private ComboBox cmbMandatoryDepAmtSTS;
	private TextField txtMandatoryAmt;
	private TextField txtMandatoryPayeeName;
	private DateField dateMandatoryDate;
	private CheckBox chkGroundForAppeal;
	private CheckBox chkStatusofTheDay;
	private CheckBox chkConditionalDeposit;
	private ComboBox cmbConditionalDepAmtSTS;
	private TextField txtConditionalAmt;
	private TextField txtConditionalPayeeName;
	private DateField dateConditionalDate;
	
	
	private CheckBox chkOutOfCourtSettlement;
	private CheckBox orderDetails;
	private TextField txtSettlementReason;
	private DateField txtSettlementLimitPeriod;
	private TextField txtSettledAmt;
	private TextField txtOfferedAmt;
	private TextField txtSavedAmt;
	private DateField dateSettlementDate;
	private OptionGroup optConsentLetterSent;
	//private Button btnViewConsentLetter;
	
	private TextArea txtWonReason;
	private DateField dateWonLastUpdated;
	
	private ConsumerForumDTO consumerForumDTO = new ConsumerForumDTO();
	
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private BeanItemContainer<SelectValue> order1MasterValueByCode = new BeanItemContainer<SelectValue>(SelectValue.class);

	private TextField txtDiagnosis;

	private ComboBox cbxRecievedFrom;

	private ComboBox cmbmovedTo;

	private DateField dateVakalathFieldDate;

	private OptionGroup chkAwardWon;
	
	private OptionGroup chkAppealPrefferedBy;

	private TextField txtadvocatenameComm;

	private TextField txtFANumber;

	private ComboBox cmbState;

	private DateField dateOfHearing;

	private CheckBox chkReplyField;

	private ComboBox cmbStatus;

	private BeanItemContainer<SelectValue> awardReasonMasterValueByCode = new BeanItemContainer<SelectValue>(SelectValue.class);

	private BeanItemContainer<SelectValue> tmpStateList = new BeanItemContainer<SelectValue>(SelectValue.class);

	private ComboBox cmbStateComp;;
	
	public SearchProcessConsumerForumForm() {/*
		
		btnSave = new Button("Save");
		btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnSaveExit = new Button("Save & Exit");
		btnSaveExit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnCancel = new Button("Exit");
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
	*/}

	public void init() {
		consumerForumDTO = new ConsumerForumDTO();
		initBinder(consumerForumDTO);
		
		Panel mainPanel = new Panel();
		/*mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");*/
		mainPanel.setCaption("Consumer Forum");
		VerticalLayout intimationVerticalLayout = intimationVerticalLayout();
		VerticalLayout caseDetailVerticalLayout = caseDetailVerticalLayout();
		VerticalLayout orderDetailVerticalLayout = orderDetailVerticalLayout();
		VerticalLayout outOfCourtSettlementVerticalLayout = outOfCourtSettlementVerticalLayout();
		VerticalLayout updateCaseVerticalLayout = updateCaseVerticalLayout();
		VerticalLayout buttonLayout = buttonLayout();
		//VerticalLayout awardDetailVerticalLayout = awardDetailVerticalLayout();
		finalLayout = new VerticalLayout(viewDetails,intimationVerticalLayout,caseDetailVerticalLayout,updateCaseVerticalLayout,
				outOfCourtSettlementVerticalLayout,orderDetailVerticalLayout,buttonLayout);
		finalLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		mainPanel.setContent(finalLayout);
		mainPanel.setHeight("100%");
		setCompositionRoot(mainPanel);
	}
	/*public VerticalLayout finalVerticalLayout(Integer index,VerticalLayout intimationVerticalLayout){
		
		VerticalLayout finalLayout = new VerticalLayout(intimationVerticalLayout);
		finalLayout.addComponent(intimationVerticalLayout, 0);
		return finalLayout;
		
	}*/
	
	public VerticalLayout intimationVerticalLayout(){
	/*	btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);*/
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setCaption("");
		
		txtIntimationNo = intimationBinder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cbxRepudiation = intimationBinder.buildAndBind("Repudiation","repudiation",ComboBox.class);
		txtPolicyNo = intimationBinder.buildAndBind("Policy Number","policyNo",TextField.class);
		txtprovisionAmt = intimationBinder.buildAndBind("Provision Amount","provisionAmt",TextField.class);
		txtProductNameCode = intimationBinder.buildAndBind("Product Name","productNo",TextField.class);
		txtInsuredName = intimationBinder.buildAndBind("Insured Name","insuredName",TextField.class);
		txtfinancialYear= intimationBinder.buildAndBind("Financial Year","financialYear",TextField.class);
		txtDiagnosis =intimationBinder.buildAndBind("Diagnosis","diagnosis",TextField.class);
		cbxRecievedFrom =intimationBinder.buildAndBind("Received From","receivedFrom",ComboBox.class);
		txtfinancialYear.setNullRepresentation("");
		txtfinancialYear.setRequired(true);
		cbxRecievedFrom.setReadOnly(true);
		txtprovisionAmt.setNullRepresentation("");
		txtDiagnosis.setReadOnly(true);
		doNumberValidation(txtfinancialYear);
		doNumberValidation(txtprovisionAmt);
		
		btnIntimationSearch = new Button();
		FormLayout searchButton = new FormLayout();
		searchButton.addComponent(btnIntimationSearch);
		searchButton.setMargin(true);
		searchButton.setCaption("");
		searchButton.setWidth("10px");
		btnIntimationSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIntimationSearch.setIcon(new ThemeResource("images/search.png"));
		//lyutIFCS.setSpacing(true);
		
		addCLickListener();
		
		//cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtfinancialYear,cbxRepudiation,txtprovisionAmt,txtDiagnosis);
		formLayoutLeft.setCaption("Intimation Details");
		
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo, txtProductNameCode,txtInsuredName,cbxRecievedFrom);	
		formLayoutReight.setCaption("Policy Details");
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,searchButton,formLayoutReight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(false);
		fieldLayout.setWidth("1000px");
		 //AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 //absoluteLayout_3.addComponent(fieldLayout);		
	/*	absoluteLayout_3.addComponent(btnSave, "top:190.0px;left:140.0px;");
		absoluteLayout_3.addComponent(btnSaveExit, "top:190.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnCancel, "top:190.0px;left:329.0px;");*/
		
		
		 mainVerticalLayout.addComponent(fieldLayout);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setMargin(true);
		 mainVerticalLayout.addStyleName("layout-with-border");
		
		 	mandatoryFields.add(cbxRepudiation);
			mandatoryFields.add(txtPolicyNo);
			mandatoryFields.add(txtProductNameCode);
			mandatoryFields.add(txtInsuredName);
			mandatoryFields.add(txtprovisionAmt);
			mandatoryFields.add(txtfinancialYear);
			//mandatoryFields.add(txtDiagnosis);
		 
		return mainVerticalLayout;
	}

	private void doNumberValidation(TextField field) {
		CSValidator validator = new CSValidator();
		validator.extend(field);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
	}
	
	public VerticalLayout caseDetailVerticalLayout(){

			VerticalLayout mainVerticalLayout = new VerticalLayout();
			mainVerticalLayout.setCaption("");
			
			txtdcdrf = caseDetailBinder.buildAndBind("DCDRF", "dcdrf", TextField.class);
			txtccNo = caseDetailBinder.buildAndBind("CC No","ccNo",TextField.class);
			txtcomplainanceAmt = caseDetailBinder.buildAndBind("Çomplainant Claimed Amount","complainceClaimedAmt",TextField.class);
			txtadvocatename = caseDetailBinder.buildAndBind("Advocate Name","adVocateName",TextField.class);
			cmbstate = caseDetailBinder.buildAndBind("State","state",ComboBox.class);
			/*cmbstate.setCaption("State");
			cmbstate.setRequired(Boolean.TRUE);
			cmbstate.setValidationVisible(true);*/
					//caseDetailBinder.buildAndBind("State","state",ComboBox.class);
			cmbzone = caseDetailBinder.buildAndBind("Zone","zone",ComboBox.class);
			datecomplaintDate= caseDetailBinder.buildAndBind("Complaint Received Date","complaintDate",DateField.class);
			dateWonLastUpdated =consumerBinder.buildAndBind("Record Last Updated","recordLastUpdated",DateField.class);
			//chkLegalLock = caseDetailBinder.buildAndBind("Legal Lock","legalLock",OptionGroup.class);
			//chkLegalCompleted = caseDetailBinder.buildAndBind("Legal Completed","legalCompleted",CheckBox.class);
			
			chkLegalLock = (OptionGroup) caseDetailBinder.buildAndBind(" ","legalLock",OptionGroup.class);
			chkLegalLock.addItems(getReadioButtonOptions());
			chkLegalLock.setItemCaption(true, "Legal Lock");
			chkLegalLock.setItemCaption(false, "Legal Completed");
			chkLegalLock.setStyleName("horizontal");
			//chkLegalLock.setComponentError(null);
			//chkLegalLock.setValidationVisible(Boolean.FALSE);
			chkLegalLock.setRequired(Boolean.TRUE);
			txtcomplainanceAmt.setNullRepresentation("");
			
			//ischkLegalLockListener(chkLegalLock);
			//ischkLegalCompletedListener(chkLegalCompleted);
			txtadvocatename.removeAllValidators();
			
			//dateWonLastUpdated.setValue(new Date());
			dateWonLastUpdated.removeAllValidators();
			dateWonLastUpdated.setReadOnly(Boolean.TRUE);
			doNumberValidation(txtcomplainanceAmt);
			cmbmovedTo = caseDetailBinder.buildAndBind("Moved To","movedTo",ComboBox.class);
			
			stateListener();
			HorizontalLayout fieldLayout3 = new HorizontalLayout();
			HorizontalLayout fieldLayout = new HorizontalLayout(chkLegalLock);
			FormLayout formLayoutLeft1 = new FormLayout(fieldLayout,txtdcdrf,cmbzone,cmbstate,txtccNo);
			formLayoutLeft1.setCaption("Case Details");
			FormLayout formLayoutReight = new FormLayout(cmbmovedTo,txtadvocatename, txtcomplainanceAmt,datecomplaintDate,dateWonLastUpdated);	
			formLayoutReight.setCaption("");
			fieldLayout.setMargin(Boolean.TRUE);
			fieldLayout.setSpacing(Boolean.TRUE);
			HorizontalLayout fieldLayout1 = new HorizontalLayout(formLayoutLeft1,formLayoutReight);
			fieldLayout1.setWidth("100%");
			 mainVerticalLayout.addComponent(fieldLayout3);
			 mainVerticalLayout.addComponent(fieldLayout);
			 mainVerticalLayout.addComponent(fieldLayout1);
			 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
			 mainVerticalLayout.setMargin(true);
			 mainVerticalLayout.addStyleName("layout-with-border");
			 	
			 	mandatoryFields.add(chkLegalLock);
			 	mandatoryFields.add(txtdcdrf);
				mandatoryFields.add(txtccNo);
				mandatoryFields.add(cmbstate);
				mandatoryFields.add(cmbzone);
				
				showOrHideValidation(false);
				
			return mainVerticalLayout;
		}
	
	public void unbindOrderDetailVerticalLayout(){
		
		unbindField(chkAwardWon, consumerBinder);
		unbindField(cmbresultorder, orderDetailBinder);
		unbindField(orderDetails, orderDetailBinder);
		unbindField(dateReceipt, orderDetailBinder);
		unbindField(dateOrder, orderDetailBinder);
		datelimitation.setReadOnly(false);
		unbindField(datelimitation, orderDetailBinder);
		
		mandatoryFields.remove(chkAwardWon);
		mandatoryFields.remove(cmbresultorder);
		mandatoryFields.remove(dateReceipt);
		mandatoryFields.remove(dateOrder);
		mandatoryFields.remove(datelimitation);
	}

	public VerticalLayout orderDetailVerticalLayout(){

		VerticalLayout mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setCaption("");
		//orderDetails.setCaption("Örder Details");
		//cmborder = orderDetailBinder.buildAndBind("Order","orderOutcome",ComboBox.class);
		
		
		orderDetails = orderDetailBinder.buildAndBind("Order Details","orderDetails",CheckBox.class);
		
		cmbresultorder = orderDetailBinder.buildAndBind("Order","order",ComboBox.class);
		dateReceipt= orderDetailBinder.buildAndBind("Date Of Receipt","dateOfReciept",DateField.class);
		dateOrder= orderDetailBinder.buildAndBind("Date of Order/Award","dateOforder",DateField.class);
		datelimitation= orderDetailBinder.buildAndBind("Limitation Date","limitationOfComplainance",DateField.class);
		
		cmbresultorder.removeAllValidators();
		dateReceipt.removeAllValidators();
		dateOrder.removeAllValidators();
		datelimitation.removeAllValidators();
		
		cmbresultorder.setContainerDataSource(order1MasterValueByCode);
		cmbresultorder.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbresultorder.setItemCaptionPropertyId("value");
		
		chkAwardWon = (OptionGroup) consumerBinder.buildAndBind(" ","awardAgainstus",OptionGroup.class);
		chkAwardWon.addItems(getReadioButtonOptions());
		chkAwardWon.setItemCaption(true, "Award");
		chkAwardWon.setItemCaption(false, "Won Case");
		chkAwardWon.setStyleName("horizontal");
		
		
		resultOrderListener(chkAwardWon,mainVerticalLayout);
		limitationDateListener(dateReceipt, datelimitation);
		
		
		final Label dummy11 = new Label();
		final Label dummy12 = new Label();
		//For order
		final Button dummyButton1 = new Button();
		dummyButton1.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		dummyButton1.setWidth("75px");

		final Label order = new Label("Order");
		final VerticalLayout orederLayout = new VerticalLayout();
		orederLayout.addComponents(dummy11,order);
		
		final HorizontalLayout orderVal = new HorizontalLayout();
		orderVal.addComponents(orederLayout,dummyButton1);
		 final HorizontalLayout resultOrder = new HorizontalLayout();
		resultOrder.addComponents(orderVal,cmbresultorder);
		
				
		FormLayout formLayoutLeft1 = new FormLayout(orderDetails,cmbresultorder,dateReceipt);
		FormLayout formLayoutReight = new FormLayout(dateOrder,datelimitation);	
		
		
		addOrderDetailsCLickListener();
	
		
		HorizontalLayout fieldLayout1 = new HorizontalLayout(formLayoutLeft1,formLayoutReight);
		fieldLayout1.setWidth("100%");
		HorizontalLayout fieldLayout2 = new HorizontalLayout(chkAwardWon);
		 mainVerticalLayout.addComponent(fieldLayout1);
		 mainVerticalLayout.addComponent(fieldLayout2);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setMargin(true);
		 mainVerticalLayout.addStyleName("layout-with-border");
		
			//mandatoryFields.add(cmborder);
		
			showOrHideValidation(false);

		return mainVerticalLayout;
	}
	
	

	public VerticalLayout buttonLayout(){
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setCaption("");
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button cancelButton = getCancelButton(dialog);
		Button submitButtonWithListener = getSaveButtonWithListener(dialog);
		Button saveNexitButtonWithListener = getSaveNexitButtonWithListener(dialog);
		HorizontalLayout absoluteLayout_3 =  new HorizontalLayout(submitButtonWithListener,saveNexitButtonWithListener,cancelButton);
		absoluteLayout_3.setWidth("50%");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		mainVerticalLayout.setComponentAlignment(absoluteLayout_3, Alignment.TOP_CENTER);
		return mainVerticalLayout;
	}
	
	public void unbindAwardDetailVerticalLayout(){
		
		unbindField(chkawardagainstus, consumerBinder);
		unbindField(txtAmount, consumerBinder);
		unbindField(txtLitigation, consumerBinder);
		unbindField(txtCompensation, consumerBinder);
		unbindField(txtAwardReason, consumerBinder);
		unbindField(txtInterestHistory, consumerBinder);
		unbindField(optappeal, consumerBinder);
		
		mandatoryFields.remove(chkawardagainstus);
		mandatoryFields.remove(txtAmount);
		mandatoryFields.remove(txtLitigation);
		mandatoryFields.remove(txtCompensation);
		mandatoryFields.remove(txtAwardReason);
		mandatoryFields.remove(txtInterestHistory);
		mandatoryFields.remove(optappeal);
	}
	
	public void unbindAwardDetailVerticalLayoutWithoutappeal(){
		
		unbindField(txtAmount, consumerBinder);
		unbindField(txtLitigation, consumerBinder);
		unbindField(txtCompensation, consumerBinder);
		unbindField(txtAwardReason, consumerBinder);
		unbindField(txtInterestHistory, consumerBinder);
		
		mandatoryFields.remove(txtAmount);
		mandatoryFields.remove(txtLitigation);
		mandatoryFields.remove(txtCompensation);
		mandatoryFields.remove(txtAwardReason);
		mandatoryFields.remove(txtInterestHistory);
	}
	
	public VerticalLayout awardDetailVerticalLayout(){

		VerticalLayout mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setCaption("Award Against Us");
		optappeal = (OptionGroup) consumerBinder.buildAndBind(" ","appeal",OptionGroup.class);
		optappeal.addItems(getReadioButtonOptions());
		optappeal.setItemCaption(true, "Satisfy");
		optappeal.setItemCaption(false, "Appeal");
		optappeal.setStyleName("horizontal");
		HorizontalLayout fieldLayout = new HorizontalLayout(optappeal);
		
		
		
		FormLayout formLayoutLeft1 = new FormLayout(fieldLayout);
		fieldLayout.setMargin(Boolean.TRUE);
		fieldLayout.setSpacing(Boolean.TRUE);
		HorizontalLayout fieldLayout1 = new HorizontalLayout(formLayoutLeft1);
		
		fieldLayout1.setWidth("100%");
		 mainVerticalLayout.addComponent(fieldLayout);
		 mainVerticalLayout.setSpacing(Boolean.TRUE);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setMargin(true);
		 mainVerticalLayout.addStyleName("layout-with-border");
		mandatoryFields.add(optappeal); 
		 
		showOrHideValidation(false);
		//optappeal.setValue(null);
		isAppealListener(optappeal, mainVerticalLayout);
		appealPreferedByListener(chkAppealPrefferedBy);
		return mainVerticalLayout;
	}

	public void unbindUpdateCaseVerticalLayout(){
		
		//unbindField(chkUpdateOftheCase, consumerBinder);
		unbindField(cmbCaseUpdate, consumerBinder);
		unbindField(txtRecordLastUpdatedRemarks, consumerBinder);
		unbindField(dateDateofNextHearing, consumerBinder);
		unbindField(dateFreshPreviousDateHearing, consumerBinder);
		
		//mandatoryFields.remove(chkUpdateOftheCase);
		mandatoryFields.remove(cmbCaseUpdate);
		mandatoryFields.remove(txtRecordLastUpdatedRemarks);
		mandatoryFields.remove(dateFreshPreviousDateHearing);
		mandatoryFields.remove(dateDateofNextHearing);
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	protected void showOrHideValidationResult(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(isVisible);
			field.setValidationVisible(!isVisible);
		}
	}
	
	protected void showOrHideValidationChk(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(isVisible);
			field.setValidationVisible(!isVisible);
		}
	}
	
	public VerticalLayout updateCaseVerticalLayout(){
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setCaption("Updates of the case");
		//chkUpdateOftheCase = consumerBinder.buildAndBind("Update Of the Case","updateOftheCase",CheckBox.class);
		cmbCaseUpdate = consumerBinder.buildAndBind("Case Update","caseUpdate",ComboBox.class);
		txtRecordLastUpdatedRemarks =consumerBinder.buildAndBind("Record Last Updated Remarks","recordLastUpdatedRemarks",TextField.class);
		dateDateofNextHearing= consumerBinder.buildAndBind(" Date of Next Hearing","dateofNextHearing",DateField.class);
		dateFreshPreviousDateHearing= consumerBinder.buildAndBind("Fresh/Previous Date Hearing","freshPreviousDateHearing",DateField.class);
		dateVakalathFieldDate= consumerBinder.buildAndBind("Vakalath Filed Date","vakalathFieldDate",DateField.class);
		FormLayout formLayoutLeft1 = new FormLayout(cmbCaseUpdate,txtRecordLastUpdatedRemarks);
		FormLayout formLayoutReight = new FormLayout(dateFreshPreviousDateHearing,dateDateofNextHearing,dateVakalathFieldDate);	
	//	HorizontalLayout fieldLayout = new HorizontalLayout(chkUpdateOftheCase);
		//fieldLayout.setCaption("");
		HorizontalLayout fieldLayout1 = new HorizontalLayout(formLayoutLeft1,formLayoutReight);
		fieldLayout1.setWidth("100%");
		//mainVerticalLayout.addComponent(fieldLayout);
		 mainVerticalLayout.addComponent(fieldLayout1);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setMargin(true);
		 mainVerticalLayout.addStyleName("layout-with-border");
		 txtRecordLastUpdatedRemarks.removeAllValidators();
		 dateDateofNextHearing.removeAllValidators();
		//mandatoryFields.add(chkUpdateOftheCase);
		mandatoryFields.add(cmbCaseUpdate);
		//mandatoryFields.add(txtRecordLastUpdatedRemarks);
		mandatoryFields.add(dateFreshPreviousDateHearing);
		//mandatoryFields.add(dateVakalathFieldDate);
		
		showOrHideValidation(false);
		
		return mainVerticalLayout;
	}
	
	public void unbindDepositDetailVerticalLayout(){
		unbindField(chkStateCommision, consumerBinder);
		unbindField(chkNationalCommision, consumerBinder);
		unbindField(chkSupremeCourt, consumerBinder);
		unbindField(chkMandatoryDeposit, consumerBinder);
		unbindField(txtMandatoryAmt, consumerBinder);
		
		unbindField(dateMandatoryDate, consumerBinder);
		unbindField(txtMandatoryPayeeName, consumerBinder);
		unbindField(cmbMandatoryDepAmtSTS, consumerBinder);
		unbindField(chkGroundForAppeal, consumerBinder);
		unbindField(chkStatusofTheDay, consumerBinder);
		
		unbindField(chkConditionalDeposit, consumerBinder);
		unbindField(txtConditionalAmt, consumerBinder);
		unbindField(dateConditionalDate, consumerBinder);
		unbindField(txtConditionalPayeeName, consumerBinder);
		unbindField(cmbConditionalDepAmtSTS, consumerBinder);
		
		unbindField(txtFANumber, consumerBinder);
		/*if(cmbState!=null){
			cmbState.setReadOnly(false);
		}
		unbindField(cmbState, consumerBinder);*/
		unbindField(dateOfHearing, consumerBinder);
		unbindField(chkReplyField, consumerBinder);
		unbindField(cmbStatus, consumerBinder);
		
		mandatoryFields.remove(chkStateCommision);
		mandatoryFields.remove(chkNationalCommision);
		mandatoryFields.remove(chkSupremeCourt);
		mandatoryFields.remove(chkMandatoryDeposit);
		mandatoryFields.remove(txtMandatoryAmt);
		
		mandatoryFields.remove(dateMandatoryDate);
		mandatoryFields.remove(txtMandatoryPayeeName);
		mandatoryFields.remove(cmbMandatoryDepAmtSTS);
		mandatoryFields.remove(chkGroundForAppeal);
		mandatoryFields.remove(chkStatusofTheDay);
		
		
	}
	
	public VerticalLayout depositDetailVerticalLayout(){
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		
		chkMandatoryDeposit= consumerBinder.buildAndBind("Mandatory Deposit","mandatoryDeposit",CheckBox.class);
		txtMandatoryAmt = consumerBinder.buildAndBind("Amount","mandatoryAmt",TextField.class);
		dateMandatoryDate = consumerBinder.buildAndBind("Date","mandatoryDate",DateField.class);
		txtMandatoryPayeeName = consumerBinder.buildAndBind("Payee Name","mandatoryPayeeName",TextField.class);
		cmbMandatoryDepAmtSTS = consumerBinder.buildAndBind("Dep. Amt STS","mandatoryDepAmtSts",ComboBox.class);
		chkGroundForAppeal = consumerBinder.buildAndBind("Ground for Appeal filed","groundforAppealField",CheckBox.class);
		chkStatusofTheDay = consumerBinder.buildAndBind("Status of Stay","statusofStay",CheckBox.class);
		chkConditionalDeposit = consumerBinder.buildAndBind("Conditional Deposit","conditionalDeposit",CheckBox.class);
		txtConditionalAmt = consumerBinder.buildAndBind("Amount","conditionalAmt",TextField.class);
		dateConditionalDate = consumerBinder.buildAndBind("Date","conditionalDate",DateField.class);
		txtConditionalPayeeName = consumerBinder.buildAndBind("Payee Name","conditionalPayeeName",TextField.class);
		cmbConditionalDepAmtSTS = consumerBinder.buildAndBind("Dep. Amt STS","conditionalDepAmtSts",ComboBox.class);
		txtMandatoryAmt.setNullRepresentation("");
		txtConditionalAmt.setNullRepresentation("");
		
		cmbMandatoryDepAmtSTS.setContainerDataSource(depAmtMasterValueByCode);
		cmbMandatoryDepAmtSTS.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbMandatoryDepAmtSTS.setItemCaptionPropertyId("value");
		
		
		cmbConditionalDepAmtSTS.setContainerDataSource(depAmtMasterValueByCode);
		cmbConditionalDepAmtSTS.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbConditionalDepAmtSTS.setItemCaptionPropertyId("value");
		
		doNumberValidation(txtMandatoryAmt);
		doNumberValidation(txtConditionalAmt);
		
		
		
		HorizontalLayout fieldLayout1 = new HorizontalLayout(chkMandatoryDeposit);
		fieldLayout1.setCaption("");
		fieldLayout1.setSpacing(Boolean.TRUE);
		HorizontalLayout fieldLayout5 = new HorizontalLayout(txtMandatoryAmt,dateMandatoryDate,txtMandatoryPayeeName,cmbMandatoryDepAmtSTS);
		fieldLayout5.setCaption("");
		fieldLayout5.setSpacing(Boolean.TRUE);
		HorizontalLayout fieldLayout2 = new HorizontalLayout(chkGroundForAppeal,chkStatusofTheDay);
		fieldLayout2.setCaption("");
		fieldLayout2.setSpacing(Boolean.TRUE);
		HorizontalLayout fieldLayout3 = new HorizontalLayout(chkConditionalDeposit);
		fieldLayout3.setCaption("");
		fieldLayout3.setSpacing(Boolean.TRUE);
		HorizontalLayout fieldLayout4 = new HorizontalLayout(txtConditionalAmt,dateConditionalDate,txtConditionalPayeeName,cmbConditionalDepAmtSTS);
		fieldLayout4.setSpacing(Boolean.TRUE);
		
		
		 mainVerticalLayout.addComponent(fieldLayout1);
		 mainVerticalLayout.addComponent(fieldLayout5);
		 mainVerticalLayout.addComponent(fieldLayout2);
		 mainVerticalLayout.addComponent(fieldLayout3);
		 mainVerticalLayout.addComponent(fieldLayout4);
		
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setMargin(true);
		 mainVerticalLayout.setSpacing(Boolean.TRUE);
		 mainVerticalLayout.addStyleName("layout-with-border");
		
			/*mandatoryFields.add(chkStateCommision);
			mandatoryFields.add(chkNationalCommision);
			mandatoryFields.add(chkSupremeCourt);
			mandatoryFields.add(chkMandatoryDeposit);
			mandatoryFields.add(txtMandatoryAmt);
			
			mandatoryFields.add(dateMandatoryDate);
			mandatoryFields.add(txtMandatoryPayeeName);
			mandatoryFields.add(cmbMandatoryDepAmtSTS);
			mandatoryFields.add(chkGroundForAppeal);
			mandatoryFields.add(chkStatusofTheDay);*/
		 
			showOrHideValidation(false);
			
		return mainVerticalLayout;
		
		
	}
	
	public VerticalLayout commissionDetailVerticalLayout(){
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		chkStateCommision = consumerBinder.buildAndBind("State Commision","stateCommission",CheckBox.class);
		chkNationalCommision= consumerBinder.buildAndBind("National Commision","nationalCommission",CheckBox.class);
		chkSupremeCourt= consumerBinder.buildAndBind("Supreme Court","supremeCourt",CheckBox.class);
		txtadvocatenameComm= consumerBinder.buildAndBind("Appeal Advocate Name","advocateName",TextField.class);
		txtFANumber= consumerBinder.buildAndBind("FA Number","faNumber",TextField.class);
		SelectValue value = null;
		if(cmbstate.getValue()!=null){
			value = (SelectValue) cmbstate.getValue();
			consumerForumDTO.setState(value);
		}
		cmbState= consumerBinder.buildAndBind("State","state",ComboBox.class);
		cmbState.setReadOnly(false);
		dateOfHearing= consumerBinder.buildAndBind("Date of Hearing","dateOfHearing",DateField.class);
		chkReplyField= consumerBinder.buildAndBind("Reply Filed","replyField",CheckBox.class);
		cmbStatus= consumerBinder.buildAndBind("Status Of Case","statusOfCase",ComboBox.class);

		cmbStatus.setContainerDataSource(statusCase);
		cmbStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbStatus.setItemCaptionPropertyId("value");
		
		cmbState.setContainerDataSource(tmpStateList);
		cmbState.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbState.setItemCaptionPropertyId("value");
		
		cmbState.setValue(value);
		cmbState.setReadOnly(true);
		
		//HorizontalLayout fieldLayout2 = new HorizontalLayout(txtadvocatenameComm);
		//fieldLayout2.setSpacing(Boolean.TRUE);
		HorizontalLayout fieldLayout = new HorizontalLayout(chkStateCommision,chkNationalCommision,chkSupremeCourt,txtadvocatenameComm);
		//fieldLayout.setComponentAlignment(fieldLayout2, Alignment.TOP_RIGHT);
		//fieldLayout.addComponent(fieldLayout2);
		//fieldLayout.setSpacing(Boolean.TRUE);
		TextField textField = new TextField();
		textField.setReadOnly(Boolean.TRUE);
		textField.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		VerticalLayout layout = new VerticalLayout(textField, chkReplyField);
		HorizontalLayout fieldLayout1 = new HorizontalLayout(txtFANumber,cmbState,dateOfHearing,layout,cmbStatus);
	
		mandatoryFields.add(txtadvocatenameComm);
		fieldLayout.setSizeFull();
		fieldLayout.setCaption("");
		fieldLayout.setSpacing(Boolean.TRUE);
		fieldLayout1.setSizeFull();
		fieldLayout1.setCaption("");
		fieldLayout1.setSpacing(Boolean.TRUE);
		 mainVerticalLayout.addComponent(fieldLayout);
		 mainVerticalLayout.addComponent(fieldLayout1);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setMargin(true);
		 mainVerticalLayout.setSpacing(Boolean.TRUE);
		 mainVerticalLayout.addStyleName("layout-with-border");
		 showOrHideValidation(false);
		 
		return mainVerticalLayout;
	}
	
	public void unbindCaseWonVerticalLayout(){
		unbindField(txtWonReason, consumerBinder);
		
		mandatoryFields.remove(txtWonReason);
	}
	
	public VerticalLayout caseWonVerticalLayout(){
		
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setCaption("");
		//chkWoncase = consumerBinder.buildAndBind("Won Case","wonCase",CheckBox.class);
		txtWonReason = consumerBinder.buildAndBind("Reason","reason",TextArea.class);
		//dateWonLastUpdated =consumerBinder.buildAndBind("Record Last Updated","recordLastUpdated",DateField.class);
		//txtWonLastUpdatedRemark= consumerBinder.buildAndBind("Record Last Updated Remarks","wonRecordLastUpdatedRemarks",TextField.class);
		//dateWonSettlementDate =consumerBinder.buildAndBind("Settlement Date","settlementDate",DateField.class);
		
		//btnViewConsentLetter = new Button();
		FormLayout formLayoutLeft1 = new FormLayout(txtWonReason);
		//FormLayout formLayoutReight = new FormLayout(dateWonLastUpdated,txtWonLastUpdatedRemark);	
		//HorizontalLayout fieldLayout = new HorizontalLayout(chkWoncase);
		//fieldLayout.setCaption("");
		HorizontalLayout fieldLayout1 = new HorizontalLayout(formLayoutLeft1);
		fieldLayout1.setWidth("100%");
		//mainVerticalLayout.addComponent(fieldLayout);
		mainVerticalLayout.addComponent(fieldLayout1);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setMargin(true);
		 mainVerticalLayout.addStyleName("layout-with-border");
		
			mandatoryFields.add(txtWonReason);
		
			showOrHideValidation(false);
			
		return mainVerticalLayout;
	}
	
	public void unbindOutOfCourtSettlementVerticalLayout(){
		
		unbindField(txtSettlementReason, outofsettlementBinder);
		unbindField(txtSettlementLimitPeriod, outofsettlementBinder);
		unbindField(txtSettledAmt, outofsettlementBinder);
		unbindField(txtOfferedAmt, outofsettlementBinder);
		txtSavedAmt.setReadOnly(false);
		unbindField(txtSavedAmt, outofsettlementBinder);
		unbindField(dateSettlementDate, outofsettlementBinder);
		unbindField(optConsentLetterSent, outofsettlementBinder);
		
		//mandatoryFields.remove(chkOutOfCourtSettlement);
		mandatoryFields.remove(txtSettlementReason);
		mandatoryFields.remove(txtSettlementLimitPeriod);
		mandatoryFields.remove(txtSettledAmt);
		mandatoryFields.remove(txtOfferedAmt);
		mandatoryFields.remove(txtSavedAmt);
		mandatoryFields.remove(dateSettlementDate);
		
		
	}

	private void unbindField(Field<?> field , BeanFieldGroup<?> binder) {
		if (field != null ) {
			Object propertyId = binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				binder.unbind(field);
			}
		}
	}
	
	public VerticalLayout outOfCourtSettlementVerticalLayout(){
		
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setCaption("");
		chkOutOfCourtSettlement = outofsettlementBinder.buildAndBind("Out Of Court Settlement","outOfCourtSettlement",CheckBox.class);
		chkOutOfCourtSettlement.removeAllValidators();
		HorizontalLayout fieldLayout = new HorizontalLayout(chkOutOfCourtSettlement);
		fieldLayout.setCaption("");
		mainVerticalLayout.addComponent(fieldLayout);
		
		
		txtSettlementReason = outofsettlementBinder.buildAndBind("Reason","reason",TextField.class);
		txtSettlementLimitPeriod =outofsettlementBinder.buildAndBind("Limitation Date","limitOfPeriod",DateField.class);
		txtSettledAmt= outofsettlementBinder.buildAndBind("Settled Amount","settledAmount",TextField.class);
		txtOfferedAmt= outofsettlementBinder.buildAndBind("Offered Amount","offeredAmt",TextField.class);
		txtSavedAmt = outofsettlementBinder.buildAndBind("Amount Saved","amtSaved",TextField.class);
		dateSettlementDate = outofsettlementBinder.buildAndBind("Settlement Date","settlementDate",DateField.class);
		optConsentLetterSent = outofsettlementBinder.buildAndBind("Consent Letter Received","consentLetterSent",OptionGroup.class);
		
		txtSettledAmt.setNullRepresentation("");
		txtSavedAmt.setNullRepresentation("");
		txtOfferedAmt.setNullRepresentation("");
		
		txtSettlementReason.removeAllValidators();
		txtSettlementLimitPeriod.removeAllValidators();
		txtSettledAmt.removeAllValidators();
		txtOfferedAmt.removeAllValidators();
		txtSavedAmt.removeAllValidators();
		dateSettlementDate.removeAllValidators();

		isOutOfCourtListener(outofsettlementBinder,chkOutOfCourtSettlement, mainVerticalLayout);
		
		doNumberValidation(txtSettledAmt);
		//doNumberValidation(txtSavedAmt);
		doNumberValidation(txtOfferedAmt);
		
		offeredAmountListener(txtOfferedAmt, txtSettledAmt, txtSavedAmt);
		settledAmtListener(txtSettledAmt, txtOfferedAmt, txtSavedAmt);
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		//btnViewConsentLetter = new Button();
		//btnViewConsentLetter.setCaption("View Consent Letter");
		//vl.addComponent(btnViewConsentLetter);
		//vl.setComponentAlignment(btnViewConsentLetter,Alignment.TOP_LEFT);
		
		HorizontalLayout v2 = new HorizontalLayout(vl,optConsentLetterSent);
		v2.setSpacing(Boolean.TRUE);
		FormLayout formLayoutLeft1 = new FormLayout(txtSettlementReason,txtSettlementLimitPeriod);
		FormLayout formLayoutReight = new FormLayout(dateSettlementDate,txtOfferedAmt,txtSettledAmt,txtSavedAmt);	
		HorizontalLayout fieldLayout1 = new HorizontalLayout(formLayoutLeft1,formLayoutReight);
		fieldLayout1.setWidth("100%");
		 mainVerticalLayout.addComponent(fieldLayout1);
		 mainVerticalLayout.addComponent(v2);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setMargin(true);
		 mainVerticalLayout.addStyleName("layout-with-border");
		 
			//mandatoryFields.add(chkOutOfCourtSettlement);
			//mandatoryFields.add(txtSettlementReason);
			//mandatoryFields.add(txtSettlementLimitPeriod);
			//mandatoryFields.add(txtSettledAmt);
			//mandatoryFields.add(txtOfferedAmt);
			//mandatoryFields.add(txtSavedAmt);
			//mandatoryFields.add(dateSettlementDate);
			showOrHideValidation(false);
		return mainVerticalLayout;
	}
	
	private void resultOrderListener(OptionGroup chkAwardWon2, final VerticalLayout mainVerticalLayout) {
		chkAwardWon2.addListener(new Listener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 4410196013822592161L;

			@Override
			public void componentEvent(Event event) {
				// TODO Auto-generated method stub
				OptionGroup component = (OptionGroup) event.getComponent();
				if(component!=null){
					//unbindOutOfCourtSettlementVerticalLayout();
					unbindCaseWonVerticalLayout();
					Boolean value = (Boolean) component.getValue();
					
					if(value!=null){
						unbindField(chkAppealPrefferedBy, consumerBinder);
						mandatoryFields.remove(chkAppealPrefferedBy);
						unbindField(txtadvocatenameComm, consumerBinder);
						mandatoryFields.remove(txtadvocatenameComm);
					if(value){
						unbindAwardDetailVerticalLayout();
						VerticalLayout awardDetailVerticalLayout = awardDetailVerticalLayout();
						System.out.println(finalLayout.getComponentCount());
						if(finalLayout.getComponentCount()==7){
							finalLayout.addComponent(awardDetailVerticalLayout, finalLayout.getComponentCount() -1);
						}else{
							unbindCaseWonVerticalLayout();
							finalLayout.removeComponent(finalLayout.getComponent(finalLayout.getComponentCount() - 2));
							finalLayout.addComponent(awardDetailVerticalLayout, finalLayout.getComponentCount() -1);
						}
					}else if(!value){
						System.out.println(finalLayout.getComponentCount());
						unbindAwardDetailVerticalLayout();
						
						int layoutSize = finalLayout.getComponentCount();
						int i = layoutSize -1;

					 	while( i > 6 ) {
					 		finalLayout.removeComponent(finalLayout.getComponent((finalLayout.getComponentCount()-1) - 1));
					    	i--;
					      }
						
						
						//finalLayout.removeComponent(finalLayout.getComponent(finalLayout.getComponentCount() - 2));
						unbindCaseWonVerticalLayout();
						finalLayout.addComponent(caseWonVerticalLayout(), finalLayout.getComponentCount() -1);
					}
					}
				}
			}
			
		});}
			

		
	
	
	private void isAppealListener(OptionGroup optappeal, final VerticalLayout awardDetailVerticalLayout) {
		if(optappeal!=null){
			
			
		optappeal.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null ) {
					if(finalLayout!=null){
					     	
						 if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "true"){
							 unbindField(chkAppealPrefferedBy, consumerBinder);
							 mandatoryFields.remove(chkAppealPrefferedBy);
							 unbindField(txtadvocatenameComm, consumerBinder);
							 mandatoryFields.remove(txtadvocatenameComm);
							 int layoutSize = awardDetailVerticalLayout.getComponentCount();
								int i = layoutSize;

							 	while( i >= 2 ) {
							 		awardDetailVerticalLayout.removeComponent(awardDetailVerticalLayout.getComponent(layoutSize - 1));
							    	i--;
							      }
							 	
							 	txtAmount = consumerBinder.buildAndBind("Amount","amount",TextField.class);
								txtLitigation = consumerBinder.buildAndBind("Litigation Cost","litigationCost",TextField.class);
								txtCompensation = consumerBinder.buildAndBind("Compensation","compensation",TextField.class);
								txtAwardReason = consumerBinder.buildAndBind("Award Reason","awardReason",TextField.class);
								txtInterestHistory =consumerBinder.buildAndBind("Interest History","interestHistory",TextField.class);
								txtLitigation.setNullRepresentation("");
								
								doNumberValidation(txtAmount);
								doNumberValidation(txtLitigation);
								
								txtAmount.setNullRepresentation("");
								txtAmount.setValidationVisible(Boolean.FALSE);
								txtLitigation.setValidationVisible(Boolean.FALSE);
								txtCompensation.setValidationVisible(Boolean.FALSE);
								txtAwardReason.setValidationVisible(Boolean.FALSE);
								txtInterestHistory.setValidationVisible(Boolean.FALSE);
								
								txtAmount.setRequired(Boolean.TRUE);
								//txtLitigation.setRequired(Boolean.TRUE);
								//txtCompensation.setRequired(Boolean.TRUE);
								//txtAwardReason.setRequired(Boolean.TRUE);
								//txtInterestHistory.setRequired(Boolean.TRUE);
								
								mandatoryFields.add(txtAmount);
								//mandatoryFields.add(txtLitigation);
								//mandatoryFields.add(txtCompensation);
								//mandatoryFields.add(txtAwardReason);
								//mandatoryFields.add(txtInterestHistory);
								
								FormLayout formLayoutLeft = new FormLayout(txtAmount, txtAwardReason);
								FormLayout formLayoutRight = new FormLayout(txtCompensation, txtLitigation);
								FormLayout formLayoutRight1 = new FormLayout(txtInterestHistory);
								 
								HorizontalLayout fieldLayout2 = new HorizontalLayout(formLayoutLeft,formLayoutRight,formLayoutRight1);
								 fieldLayout2.setSpacing(Boolean.TRUE);
								 awardDetailVerticalLayout.addComponent(fieldLayout2);
								 awardDetailVerticalLayout.setSpacing(Boolean.TRUE);
								 int layoutSize2 = finalLayout.getComponentCount();
									int i2 = layoutSize2 -1;

									while( i2 > 7 ) {
								 		finalLayout.removeComponent(finalLayout.getComponent((finalLayout.getComponentCount()-1) - 1));
								    	i2--;
								      }
								 finalLayout.addComponent(awardDetailVerticalLayout, finalLayout.getComponentCount() -1);
																 
						 }else if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "false"){
							 unbindAwardDetailVerticalLayoutWithoutappeal();
							 unbindField(txtadvocatenameComm, consumerBinder);
							 mandatoryFields.remove(txtadvocatenameComm);
							 int componentCount1 = awardDetailVerticalLayout.getComponentCount();
							 if(componentCount1 == 2){
								 	unbindField(chkAppealPrefferedBy, consumerBinder);
								 	mandatoryFields.remove(chkAppealPrefferedBy);
								 	unbindField(txtadvocatenameComm, consumerBinder);
									mandatoryFields.remove(txtadvocatenameComm);
								 	awardDetailVerticalLayout.removeComponent(awardDetailVerticalLayout.getComponent(componentCount1 - 1));
								 	appealPrefferedByLayout(awardDetailVerticalLayout);
								 	 awardDetailVerticalLayout.setSpacing(Boolean.TRUE);
							 }else if(componentCount1 == 1){
								 	appealPrefferedByLayout(awardDetailVerticalLayout);
								 	 awardDetailVerticalLayout.setSpacing(Boolean.TRUE);
							 }
							 
							 //unbindDepositDetailVerticalLayout();
							// VerticalLayout depositDetailVerticalLayout = depositDetailVerticalLayout();
							 //finalLayout.addComponent(depositDetailVerticalLayout);
						 }
						
						
					}
				}
			}
		});}
	}
	
	private void appealPreferedByListener(OptionGroup chkAppealPrefferedBy) {
		if(chkAppealPrefferedBy!=null){
			
			
			chkAppealPrefferedBy.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null ) {
					if(finalLayout!=null){
					     	
						 if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "true"){
							 System.out.println(finalLayout.getComponentCount());
							 unbindDepositDetailVerticalLayout();
							 if(finalLayout.getComponentCount()==8){
								 if(cmbState!=null){
							 			cmbState.setReadOnly(false);
							 			unbindField(cmbState, consumerBinder);
							 		}
									unbindField(txtadvocatenameComm, consumerBinder);
									mandatoryFields.remove(txtadvocatenameComm);
							 VerticalLayout commissionDetailVerticalLayout = commissionDetailVerticalLayout();
							 finalLayout.addComponent(commissionDetailVerticalLayout,finalLayout.getComponentCount()-1);
							 }
							 if(finalLayout.getComponentCount()==9){
								 VerticalLayout depositDetailVerticalLayout = depositDetailVerticalLayout();
								 finalLayout.addComponent(depositDetailVerticalLayout,finalLayout.getComponentCount()-1);
							 }
							 
						 }else if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "false"){
							 System.out.println(finalLayout.getComponentCount());
							 unbindDepositDetailVerticalLayout();
							 if(finalLayout.getComponentCount()>8){
								 finalLayout.removeComponent(finalLayout.getComponent(finalLayout.getComponentCount()-2));
								 finalLayout.removeComponent(finalLayout.getComponent(finalLayout.getComponentCount()-2));
								} 
							 	if(finalLayout.getComponentCount()==8){
							 		if(cmbState!=null){
							 			cmbState.setReadOnly(false);
							 			unbindField(cmbState, consumerBinder);
							 		}
									unbindField(txtadvocatenameComm, consumerBinder);
									mandatoryFields.remove(txtadvocatenameComm);
								 VerticalLayout commissionDetailVerticalLayout = commissionDetailVerticalLayout();
								 finalLayout.addComponent(commissionDetailVerticalLayout,finalLayout.getComponentCount()-1);
							} 
						 }
						
						
					}
				}
			}
		});}
	}
	
	
	private void appealPrefferedByLayout(final VerticalLayout awardDetailVerticalLayout) {
		unbindField(chkAppealPrefferedBy, consumerBinder);
		mandatoryFields.remove(chkAppealPrefferedBy);
		chkAppealPrefferedBy = (OptionGroup) consumerBinder.buildAndBind(" ","appealPrefferedBy",OptionGroup.class);
		chkAppealPrefferedBy.addItems(getReadioButtonOptions());
		chkAppealPrefferedBy.setItemCaption(true, "Star");
		chkAppealPrefferedBy.setItemCaption(false, "Insured");
		chkAppealPrefferedBy.setStyleName("horizontal");
		chkAppealPrefferedBy.setRequired(Boolean.TRUE);
		showOrHideValidation(false);
		mandatoryFields.add(chkAppealPrefferedBy);
		appealPreferedByListener(chkAppealPrefferedBy);
		FormLayout formLayoutLeft = new FormLayout(chkAppealPrefferedBy);
		HorizontalLayout fieldLayout2 = new HorizontalLayout(formLayoutLeft);
		 fieldLayout2.setSpacing(Boolean.TRUE);
		 fieldLayout2.setCaption("Appeal Preffered By");
		 awardDetailVerticalLayout.addComponent(fieldLayout2);
		 awardDetailVerticalLayout.setSpacing(Boolean.TRUE);
		 finalLayout.addComponent(awardDetailVerticalLayout,finalLayout.getComponentCount() -1);
	}
	
	public void getErrorMessage(String eMsg) {

		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	 protected Collection<Boolean> getReadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
			coordinatorValues.add(true);
			coordinatorValues.add(false);
			
			return coordinatorValues;
		}
	
	private void initBinder(ConsumerForumDTO consumerForumDTO)
	{
		this.consumerBinder = new BeanFieldGroup<ConsumerForumDTO>(ConsumerForumDTO.class);
		this.consumerBinder.setItemDataSource(consumerForumDTO);
		this.consumerBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.intimationBinder = new BeanFieldGroup<IntimationDetailsDTO>(
				IntimationDetailsDTO.class);
		IntimationDetailsDTO intimationDetailsDTO = consumerForumDTO.getIntimationDetailsDTO();
		this.intimationBinder.setItemDataSource(intimationDetailsDTO);
		this.intimationBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.caseDetailBinder = new BeanFieldGroup<CaseDetailsDTO>(
				CaseDetailsDTO.class);
		this.caseDetailBinder.setItemDataSource(consumerForumDTO.getCaseDetailsDTO());
		this.caseDetailBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		this.orderDetailBinder = new BeanFieldGroup<OrderDetailsDTO>(
				OrderDetailsDTO.class);
		this.orderDetailBinder.setItemDataSource(consumerForumDTO.getOrderDetailsDTO());
		this.orderDetailBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		this.outofsettlementBinder = new BeanFieldGroup<OutOfCourtSettlementDTO>(
				OutOfCourtSettlementDTO.class);
		this.outofsettlementBinder.setItemDataSource(consumerForumDTO.getOutOfCourtSettlmentDTO());
		this.outofsettlementBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	public void setCBXValue(BeanItemContainer<SelectValue> repudiationMasterValueByCode, BeanItemContainer<SelectValue> tmpZoneList, BeanItemContainer<SelectValue> tmpStateList, 
			BeanItemContainer<SelectValue> orderMasterValueByCode, BeanItemContainer<SelectValue> order1MasterValueByCode, 
			BeanItemContainer<SelectValue> awardReasonMasterValueByCode, BeanItemContainer<SelectValue> depAmtMasterValueByCode, 
			BeanItemContainer<SelectValue> caseUpdateMasterValueByCode, 
			BeanItemContainer<SelectValue> recievedFrom, BeanItemContainer<SelectValue> movedTO, BeanItemContainer<SelectValue> statusCase){
		
		cbxRepudiation.setContainerDataSource(repudiationMasterValueByCode);
		cbxRepudiation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxRepudiation.setItemCaptionPropertyId("value");
		
		
		cmbzone.setContainerDataSource(tmpZoneList);
		cmbzone.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbzone.setItemCaptionPropertyId("value");
		
		cmbstate.setContainerDataSource(tmpStateList);
		cmbstate.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbstate.setItemCaptionPropertyId("value");
		
		cbxRecievedFrom.setReadOnly(false);
		cbxRecievedFrom.setContainerDataSource(recievedFrom);
		cbxRecievedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxRecievedFrom.setItemCaptionPropertyId("value");
		cbxRecievedFrom.setReadOnly(true);
		
		cmbresultorder.setContainerDataSource(order1MasterValueByCode);
		cmbresultorder.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbresultorder.setItemCaptionPropertyId("value");
		
		cmbmovedTo.setContainerDataSource(movedTO);
		cmbmovedTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbmovedTo.setItemCaptionPropertyId("value");
		
		this.awardReasonMasterValueByCode = awardReasonMasterValueByCode;
		this.depAmtMasterValueByCode = depAmtMasterValueByCode;
		this.order1MasterValueByCode = order1MasterValueByCode;
		this.statusCase=statusCase;
		this.tmpStateList =tmpStateList;
		
		cmbCaseUpdate.setContainerDataSource(caseUpdateMasterValueByCode);
		cmbCaseUpdate.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCaseUpdate.setItemCaptionPropertyId("value");
		
	}
	
/*	public void initRepresentativeSearch(
			BeanItemContainer<SelectValue> stateContainer,
			BeanItemContainer<SelectValue> allocationToContainer,BeanItemContainer<SelectValue> assignToConainer,BeanItemContainer<SelectValue> fvrPriorityContainer) {
//		fieldVisitPageRepresentativeNameSearchUI.initRepresentativeNameSearch(this.intimation,SHAConstants.ASSIGN_FVR);
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
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

<<<<<<< HEAD
	}*/
//=======
	//}
	
	public void addCLickListener() {
		btnIntimationSearch.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 4246444072076880275L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();

				popup.setWidth("75%");
				popup.setHeight("90%");
				consumerFormSearchUI.setPopup(popup,SHAConstants.LEGAL_CONSUMER);
				
				consumerFormSearchUI.init();
				popup.setContent(consumerFormSearchUI);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(true);

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
		});
	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		btnCancel = new Button("Exit");
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.LEGAL_HOME, null);
			}
		});
		return btnCancel;
	}
	
	@SuppressWarnings("null")
	private boolean isValid() {
		// TODO Auto-generated method stub
		Boolean hasError = false;
		showOrHideValidation(true);
		try {
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			consumerForumDTO.setUserName(userName);
			
			String eMsg = "";		
			if (!this.consumerBinder.isValid()) {

				for (Field<?> field : this.consumerBinder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg += errMsg.getFormattedHtmlMessage();
					}
					hasError = true;
				}
			}
			
			if (!this.intimationBinder.isValid()) {

				for (Field<?> field : this.intimationBinder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg += errMsg.getFormattedHtmlMessage();
					}
					hasError = true;
				}
			}
			
			if (!this.caseDetailBinder.isValid()) {

				for (Field<?> field : this.caseDetailBinder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg += errMsg.getFormattedHtmlMessage();
					}
					hasError = true;
				}
				
			}

			
			if(this.orderDetailBinder.isValid()) {
				if(orderDetailBinder.getItemDataSource().getBean().getOrderDetails() != null && orderDetailBinder.getItemDataSource().getBean().getOrderDetails().equals(true)){
					 SelectValue selectValue = new SelectValue();
					 selectValue = (SelectValue) cmbresultorder.getValue();
					orderDetailBinder.getItemDataSource().getBean().setOrder(selectValue);
					 orderDetailBinder.getItemDataSource().getBean().setDateOfReciept(dateReceipt.getValue());
					 orderDetailBinder.getItemDataSource().getBean().setDateOforder(dateOrder.getValue());
					 orderDetailBinder.getItemDataSource().getBean().setLimitationOfComplainance(datelimitation.getValue());
					 
					if(orderDetailBinder.getItemDataSource().getBean().getOrder() == null) {
						hasError = true;
						eMsg+= "Please Select Order<br>";
					}
					if(orderDetailBinder.getItemDataSource().getBean().getDateOfReciept() == null) {
						hasError = true;
						eMsg+= "Please Select Date of Receipt<br>";
					}
					if(orderDetailBinder.getItemDataSource().getBean().getDateOforder() == null) {
						hasError = true;
						eMsg+= "Please Select Date of Order/Award<br>";
					}
					if(orderDetailBinder.getItemDataSource().getBean().getLimitationOfComplainance() == null) {
						hasError = true;
						eMsg+= "Please Select Limitation Date<br>";
					}
					
					}
			}
		/*	if (!this.orderDetailBinder.isValid()) {
			
				String checkVal = null;
				for (Field<?> field : this.orderDetailBinder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg += errMsg.getFormattedHtmlMessage();
					}
					hasError = true;
				}
				
			}*/
			
				/*for(Field<?> field : this.orderDetailBinder.getFields()) {
				if(field.getValue() == null) {
					if(checkVal != null && checkVal.equals("true") && field.getValue() == null) {
						ErrorMessage errMsg = ((AbstractField<?>) field)
								.getErrorMessage();
						
						if (errMsg!=null) {
						
							eMsg += errMsg.getFormattedHtmlMessage();
						}
						hasError = true;
					}
				}else if(field != null && field.getValue().equals(true)) {
						checkVal = field.getValue().toString();
					}
				}*/
			

			
			
			if (!this.outofsettlementBinder.isValid()) {

				for (Field<?> field : this.outofsettlementBinder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg += errMsg.getFormattedHtmlMessage();
					}
					hasError = true;
				}
			}
			
			/*if(consumerForumDTO.getCaseDetailsDTO()!=null){
				if(cmbstate!=null && cmbstate.getValue()==null){
					eMsg += "Please Select State";
					hasError = true;
				}
			}*/
			
			if(!hasError){
				consumerBinder.commit();
				intimationBinder.commit();
				caseDetailBinder.commit();
				orderDetailBinder.commit();
				outofsettlementBinder.commit();
				
				/*if(cmbstate!=null && cmbstate.getValue()!=null){
					CaseDetailsDTO caseDetailsDTO2 = consumerForumDTO.getCaseDetailsDTO();
					State state = (State) cmbstate.getValue();
					caseDetailsDTO2.setState(state);
				}*/
			}else{

				setRequired(true);
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

				//hasError = true;
				return hasError;
			
			}
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hasError;
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
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	
	private Button getSaveButtonWithListener(final ConfirmDialog dialog) {
		btnSave = new Button("Save");
		btnSave.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(!isValid()) {
					//if(validatePage()){
						dialog.close();
						fireViewEvent(SearchProcessConsumerForumPresenter.LEGAL_SAVE_CONSUMER, consumerForumDTO);
					//}
				}
			}

			
		});
		return btnSave;
	}
	
	private Button getSaveNexitButtonWithListener(final ConfirmDialog dialog) {
		btnSaveExit = new Button("Save & Exit");
		btnSaveExit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSaveExit.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(!isValid()) {
					dialog.close();
					fireViewEvent(SearchProcessConsumerForumPresenter.LEGAL_SAVE_CONSUMER, consumerForumDTO);
					fireViewEvent(MenuItemBean.LEGAL_HOME, null);
					
					
					//wizard.getNextButton().setEnabled(false);
					//wizard.getFinishButton().setEnabled(true);
					//bean.setIsFirstPageSubmit(true);
				} else {/*
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg += error+"</br>";
					}
					showErrorPopup(eMsg);
				*/}
			}

			
		});
		return btnSaveExit;
	}

	
	
	private void isOutOfCourtListener(final BeanFieldGroup<OutOfCourtSettlementDTO> outofsettlementBinder, CheckBox outofCourtSettlement, final VerticalLayout mainVerticalLayout) {
		if(outofCourtSettlement!=null){
			
			
			outofCourtSettlement.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			
			
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				//Boolean isFinalChecked = false ;
				if(event.getProperty() != null ) {
							
					    	
						 if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "true"){
							 unbindOutOfCourtSettlementVerticalLayout();
							 int j = mainVerticalLayout.getComponentCount();
							 while( j > 1 ) {
								 mainVerticalLayout.removeComponent(mainVerticalLayout.getComponent((mainVerticalLayout.getComponentCount()) - 1));
							    	j--;
							      }
								txtSettlementReason = outofsettlementBinder.buildAndBind("Reason","reason",TextField.class);
								txtSettlementLimitPeriod =outofsettlementBinder.buildAndBind("Limitation Date","limitOfPeriod",DateField.class);
								txtSettledAmt= outofsettlementBinder.buildAndBind("Settled Amount","settledAmount",TextField.class);
								txtOfferedAmt= outofsettlementBinder.buildAndBind("Offered Amount","offeredAmt",TextField.class);
								txtSavedAmt = outofsettlementBinder.buildAndBind("Amount Saved","amtSaved",TextField.class);
								dateSettlementDate = outofsettlementBinder.buildAndBind("Settlement Date","settlementDate",DateField.class);
								optConsentLetterSent = outofsettlementBinder.buildAndBind("Consent Letter Received","consentLetterSent",OptionGroup.class);
								
								txtSettledAmt.setNullRepresentation("");
								txtSavedAmt.setNullRepresentation("");
								txtOfferedAmt.setNullRepresentation("");
								
								
								//mandatoryFields.add(chkOutOfCourtSettlement);
								mandatoryFields.add(txtSettlementReason);
								mandatoryFields.add(txtSettlementLimitPeriod);
								mandatoryFields.add(txtSettledAmt);
								mandatoryFields.add(txtOfferedAmt);
								mandatoryFields.add(txtSavedAmt);
								mandatoryFields.add(dateSettlementDate);
								
								doNumberValidation(txtSettledAmt);
								//doNumberValidation(txtSavedAmt);
								doNumberValidation(txtOfferedAmt);
								
								offeredAmountListener(txtOfferedAmt, txtSettledAmt, txtSavedAmt);
								settledAmtListener(txtSettledAmt, txtOfferedAmt, txtSavedAmt);
								
							 	int componentCount = finalLayout.getComponentCount();
							 	componentCount = componentCount-1;
							 	if(componentCount > 4){
							 		
							 		for(int i = componentCount ; i>= 6; --i){
							 			unbindOrderDetailVerticalLayout();
							 			unbindAwardDetailVerticalLayout();
							 			unbindCaseWonVerticalLayout();
							 			unbindDepositDetailVerticalLayout();
							 			if(finalLayout.getComponentCount()-1==i){
							 				finalLayout.removeComponent(finalLayout.getComponent(i -1));
							 			}
							 			componentCount = finalLayout.getComponentCount() - 1;
							 		}
						     	}
							 	VerticalLayout vl = new VerticalLayout();
								vl.setSizeFull();
							 	HorizontalLayout v2 = new HorizontalLayout(vl,optConsentLetterSent);
								v2.setSpacing(Boolean.TRUE);
							 	FormLayout formLayoutLeft1 = new FormLayout(txtSettlementReason,txtSettlementLimitPeriod);
								FormLayout formLayoutReight = new FormLayout(dateSettlementDate,txtOfferedAmt,txtSettledAmt,txtSavedAmt);	
								HorizontalLayout fieldLayout1 = new HorizontalLayout(formLayoutLeft1,formLayoutReight);
								fieldLayout1.setWidth("100%");
								
								//absoluteLayout_3.setComponentAlignment(btnSaveExit, Alignment.TOP_CENTER);
								//absoluteLayout_3.setComponentAlignment(btnCancel, Alignment.TOP_CENTER);
								//absoluteLayout_3.setWidth("50%");
								 mainVerticalLayout.addComponent(fieldLayout1);
								 mainVerticalLayout.addComponent(v2);
								// mainVerticalLayout.addComponent(absoluteLayout_3);
								// mainVerticalLayout.setComponentAlignment(absoluteLayout_3, Alignment.TOP_CENTER);
								 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
								 mainVerticalLayout.setMargin(true);
								 mainVerticalLayout.addStyleName("layout-with-border");
							 	showOrHideValidation(false);
						 }else if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "false"){
							 unbindField(chkOutOfCourtSettlement, outofsettlementBinder);
							 unbindOutOfCourtSettlementVerticalLayout();
							 int componentCount = finalLayout.getComponentCount();
								 	unbindOrderDetailVerticalLayout();
								 	finalLayout.addComponent(orderDetailVerticalLayout(), componentCount-1);
								 	
								 	txtSettlementReason.setRequired(false);
									txtSettlementLimitPeriod.setRequired(false);
									txtSettledAmt.setRequired(false);
									txtOfferedAmt.setRequired(false);
									txtSavedAmt.setRequired(false);
									dateSettlementDate.setRequired(false);
								 	
								 	//showOrHideValidation(false);
						 }
						
						 showOrHideValidation(false);	
				}else{
					unbindField(chkOutOfCourtSettlement, outofsettlementBinder);
					unbindOutOfCourtSettlementVerticalLayout();
					showOrHideValidation(false);
				}
			}

			
		});}
	}
	
	
	
	private void offeredAmountListener(final TextField offerAmt , final TextField settledAmt, final TextField savedAmt) {
		if(offerAmt!=null){
			
			
			offerAmt.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			
			
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				//Boolean isFinalChecked = false ;
				if(event.getProperty() != null ) {
					if(settledAmt.getValue()!=null && offerAmt.getValue()!=null){
						
						setSavedAmount(settledAmt.getValue(),offerAmt.getValue(),savedAmt);
					}
				}
			}

			
		});}
	}
	
	private void settledAmtListener(final TextField settledAmt , final TextField offerAmt, final TextField savedAmt) {
		if(settledAmt!=null){
			
			
			settledAmt.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			
			
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				//Boolean isFinalChecked = false ;
				if(event.getProperty() != null ) {
					if(settledAmt.getValue()!=null && offerAmt.getValue()!=null){
						
						setSavedAmount(settledAmt.getValue(),offerAmt.getValue(),savedAmt);
					}
				}
			}

			
		});}
	}
	
	
	private void setSavedAmount(String value, String value2,TextField savedAmt) {

		Double settledAmt = SHAUtils.getDoubleFromStringWithComma(value);
		Double offerAmt = SHAUtils.getDoubleFromStringWithComma(value2);
		savedAmt.setReadOnly(false);
		
		savedAmt.setValue(Double.toString(offerAmt - settledAmt));
		/*if(offerAmt - settledAmt<0){
			savedAmt.setValue(Double.toString(0.00));
		}else{
			savedAmt.setValue(Double.toString(offerAmt - settledAmt));
		}*/
		savedAmt.setReadOnly(true);
	}
	
	private void limitationDateListener(final DateField reciptDate , final DateField limitationDate) {
		if(reciptDate!=null){
			
			
			reciptDate.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
			
			
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				//Boolean isFinalChecked = false ;
				if(event.getProperty() != null ) {
					Date noticeDate = reciptDate.getValue();
					if(noticeDate!=null){
						doAddDaysCalculation(noticeDate, SHAConstants.LEGAL_CONSUMER_ADD_DAYS_LIMITATION, limitationDate);
					}
				}
			}

			
		});}
	}
	
	private Date doAddDaysCalculation(Date noticeRecievedDate, String addDays,DateField limitationDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(noticeRecievedDate); // Now use today date.
		c.add(Calendar.DATE, Integer.parseInt(addDays)); // Adding 5 days
		String output = sdf.format(c.getTime());
		limitationDate.setReadOnly(false);
		limitationDate.setValue(c.getTime());
		limitationDate.setReadOnly(true);
		System.out.println(output);
		return c.getTime();
	}
	
	public void stateListener(){
		
		cmbstate.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;
	
				@Override
				public void valueChange(ValueChangeEvent event) {
					if (null != event && null != event.getProperty()
							&& null != event.getProperty().getValue()) {
						SelectValue selectString = (SelectValue) event.getProperty().getValue();
						if(cmbState!=null){
							cmbState.setReadOnly(false);
							cmbState.setValue(selectString);
							cmbState.setReadOnly(true);
						}
					}					
				}
			});
		}
	
	
	public void populateFieldValues(Claim claim, LegalConsumer legalConsumer, String diagnosisName) {
		
		if(legalConsumer==null){
			txtIntimationNo.setValue(claim.getIntimation().getIntimationId());
			txtPolicyNo.setValue(claim.getIntimation().getPolicy().getPolicyNumber());
			txtProductNameCode.setValue(claim.getIntimation().getPolicy().getProductName());
			txtfinancialYear.setValue(claim.getIntimation().getPolicy().getPolicyYear().toString());
			if(claim.getCurrentProvisionAmount()!=null){
				txtprovisionAmt.setValue(claim.getCurrentProvisionAmount().toString());
			}
			txtInsuredName.setValue(claim.getIntimation().getInsured().getInsuredName());
			txtDiagnosis.setReadOnly(Boolean.FALSE);
			txtDiagnosis.setValue(diagnosisName);
			txtDiagnosis.setReadOnly(Boolean.TRUE);
			viewDetails.initView(claim.getIntimation().getIntimationId(),ViewLevels.LEGAL_CLAIMS,SHAConstants.LEGAL_CONSUMER);
			IntimationDetailsDTO intimationDetailsDTO = consumerForumDTO.getIntimationDetailsDTO();
			intimationDetailsDTO.setClaimKey(claim);
		}
		
		if(legalConsumer!=null){
			
			txtIntimationNo.setValue(legalConsumer.getIntimationNumber());
			viewDetails.initView(legalConsumer.getIntimationNumber(),ViewLevels.LEGAL_CLAIMS,SHAConstants.LEGAL_CONSUMER);
			txtPolicyNo.setValue(legalConsumer.getPolicyNumber());
			txtProductNameCode.setValue(legalConsumer.getProductName());
			if(legalConsumer.getFinancialYear()!=null){
				txtfinancialYear.setValue(legalConsumer.getFinancialYear().toString());
			}
			if(legalConsumer.getProvisionAmount()!=null){
				txtprovisionAmt.setValue(legalConsumer.getProvisionAmount().toString());
			}
			txtInsuredName.setValue(legalConsumer.getInsuredName());
			txtDiagnosis.setReadOnly(Boolean.FALSE);
			txtDiagnosis.setValue(diagnosisName);
			txtDiagnosis.setReadOnly(Boolean.TRUE);
			MastersValue repudiationId = legalConsumer.getRepudiationId();
			if(repudiationId!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(repudiationId.getKey());
				selectValue.setValue(repudiationId.getValue());
				cbxRepudiation.setValue(selectValue);
			}
			MastersValue recievedFrom = legalConsumer.getReceievedFrom();
			if(recievedFrom!=null){
				cbxRecievedFrom.setReadOnly(false);
				SelectValue selectValue = new SelectValue();
				selectValue.setId(recievedFrom.getKey());
				selectValue.setValue(recievedFrom.getValue());
				cbxRecievedFrom.setValue(selectValue);
				cbxRecievedFrom.setReadOnly(false);
			}
			IntimationDetailsDTO intimationDetailsDTO = consumerForumDTO.getIntimationDetailsDTO();
			intimationDetailsDTO.setClaimKey(claim);
			
			txtdcdrf.setValue(legalConsumer.getDcdrfRemarks());
			dateWonLastUpdated.setReadOnly(Boolean.FALSE);
			dateWonLastUpdated.setValue(legalConsumer.getRecUpdDate());
			dateWonLastUpdated.setReadOnly(Boolean.TRUE);
			txtadvocatename.setValue(legalConsumer.getAdvocateName());
			if(legalConsumer.getCompClmAmt()!=null){
				txtcomplainanceAmt.setValue(legalConsumer.getCompClmAmt().toString());
			}
			txtccNo.setValue(legalConsumer.getCcNo());
			if(legalConsumer.getZoneId()!=null){
				TmpCPUCode zoneId = legalConsumer.getZoneId();
				SelectValue selectValue = new SelectValue();
				selectValue.setId(zoneId.getKey());
				selectValue.setValue(zoneId.getCpuCode() +"-"+ zoneId.getDescription());
				cmbzone.setValue(selectValue);
			}
			if(legalConsumer.getStateId()!=null){
				State stateID = legalConsumer.getStateId();
				SelectValue selectValue = new SelectValue();
				selectValue.setId(stateID.getKey());
				selectValue.setValue(stateID.getValue());
				cmbstate.setReadOnly(false);
				cmbstate.setValue(selectValue);
			}
			datecomplaintDate.setValue(legalConsumer.getComplaintDate());
			chkLegalLock.setValue(legalConsumer.getLegalLockFlag());
			
			chkOutOfCourtSettlement.setValue(legalConsumer.getOocSettleFlag());
			txtSettlementReason.setValue(legalConsumer.getOocReason());
			txtSettlementLimitPeriod.setValue(legalConsumer.getLimitofPeriod());
			if(legalConsumer.getSettledAmount()!=null){
				txtSettledAmt.setValue(legalConsumer.getSettledAmount().toString());
			}
			if(legalConsumer.getOfferedAmount()!=null){
				txtOfferedAmt.setValue(legalConsumer.getOfferedAmount().toString());
			}
			if(legalConsumer.getSavedAmount()!=null){
				txtSavedAmt.setReadOnly(false);
				txtSavedAmt.setValue(legalConsumer.getSavedAmount().toString());
				txtSavedAmt.setReadOnly(true);
			}
			if(legalConsumer.getSettledDate()!=null){
				dateSettlementDate.setValue(legalConsumer.getSettledDate());
			}
			optConsentLetterSent.setValue(legalConsumer.getConsentLetterFlag());
			
			//chkLegalCompleted.setValue();
			MastersValue orderId = legalConsumer.getOrderId();
			Boolean awardFlag = legalConsumer.getAwardFlag();
			if(awardFlag!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(orderId.getKey());
				selectValue.setValue(orderId.getValue());
				cmbresultorder.setValue(selectValue);
				dateOrder.setValue(legalConsumer.getOrderDate());
				dateReceipt.setValue(legalConsumer.getReceiptDate());
				datelimitation.setReadOnly(Boolean.FALSE);
				datelimitation.setValue(legalConsumer.getComplainceDate());
				datelimitation.setReadOnly(Boolean.TRUE);
				if(awardFlag!=null && !awardFlag){
					if(chkAwardWon!=null){
						chkAwardWon.setValue(legalConsumer.getAwardFlag());
					}
					if(txtWonReason!=null){
						txtWonReason.setValue(legalConsumer.getWonCaseReason());
					}
				}else if(awardFlag!=null && awardFlag){
					if(chkAwardWon!=null){
						chkAwardWon.setValue(legalConsumer.getAwardFlag());
					}
					if(legalConsumer.getAppealFlag()!= null && legalConsumer.getAppealFlag()){

						optappeal.setValue(legalConsumer.getAppealFlag());
						if(legalConsumer.getAwardAgainstAmt()!=null){
							txtAmount.setValue(legalConsumer.getAwardAgainstAmt().toString());
							txtCompensation.setValue(legalConsumer.getCompensation());
							if(legalConsumer.getLitigationCost()!=null){
								txtLitigation.setValue(legalConsumer.getLitigationCost().toString());
							}
							txtInterestHistory.setValue(legalConsumer.getInterestHistory());
							if(legalConsumer.getAwardReasonId()!=null){
								txtAwardReason.setValue(legalConsumer.getAwardReasonId());
							}
						}
						}else if(legalConsumer.getAppealFlag()!= null && !legalConsumer.getAppealFlag()){
						
							optappeal.setValue(legalConsumer.getAppealFlag());
							
							if(legalConsumer.getOrderStatusId()!=null){
								cmbresultorder.setValue(legalConsumer.getOrderStatusId().toString());
							}
							chkAppealPrefferedBy.setValue(legalConsumer.getAppealPrefferedBy());
							dateOrder.setValue(legalConsumer.getOrderDate());
							dateReceipt.setValue(legalConsumer.getReceiptDate());
							
							if(legalConsumer.getAppealPrefferedBy()!=null && legalConsumer.getAppealPrefferedBy()){
								
								chkStateCommision.setValue(legalConsumer.getStateCommFlag());
								chkNationalCommision.setValue(legalConsumer.getNationalCommFlag());
								chkSupremeCourt.setValue(legalConsumer.getSupremeCourtFlag());
								txtadvocatenameComm.setValue(legalConsumer.getAwdAdvocateName());
								txtFANumber.setValue(legalConsumer.getFaNumber());
								//cmbState.setValue(legalConsumer.get);
								dateOfHearing.setValue(legalConsumer.getDateHearing());
								chkReplyField.setValue(legalConsumer.getReplyField());
								if(legalConsumer.getStatusCaseId()!=null){
									MastersValue conAmtStsId = legalConsumer.getStatusCaseId();
									SelectValue selectValue2 = new SelectValue();
									selectValue2.setId(conAmtStsId.getKey());
									selectValue2.setValue(conAmtStsId.getValue());
									cmbStatus.setValue(selectValue2);
								}
								
								chkMandatoryDeposit.setValue(legalConsumer.getMandatoryDepFlag());
								//cmbMandatoryDepAmtSTS.setValue(legalConsumer.getDepAmtStsIs());
								if(legalConsumer.getManDepAmount()!=null){
									txtMandatoryAmt.setValue(legalConsumer.getManDepAmount().toString());
								}
								if(legalConsumer.getDepAmtStsIs()!=null){
									MastersValue conAmtStsId = legalConsumer.getDepAmtStsIs();
									SelectValue selectValue2 = new SelectValue();
									selectValue2.setId(conAmtStsId.getKey());
									selectValue2.setValue(conAmtStsId.getValue());
									cmbMandatoryDepAmtSTS.setValue(selectValue2);
								}
								
								txtMandatoryPayeeName.setValue(legalConsumer.getManPayeeName());
								dateMandatoryDate.setValue(legalConsumer.getManDepDate());
								chkGroundForAppeal.setValue(legalConsumer.getGrndAppealFlag());
								chkStatusofTheDay.setValue(legalConsumer.getStayStatusFlag());
								chkConditionalDeposit.setValue(legalConsumer.getConditionalFlag());
								//chkSupremeCourt.setValue(legalConsumer.getSupremeCourtFlag());
								if(legalConsumer.getConAmtStsId()!=null){
									MastersValue conAmtStsId = legalConsumer.getConAmtStsId();
									SelectValue selectValue2 = new SelectValue();
									selectValue2.setId(conAmtStsId.getKey());
									selectValue2.setValue(conAmtStsId.getValue());
									cmbConditionalDepAmtSTS.setValue(selectValue2);
								}
								if(legalConsumer.getConDepAmt()!=null){
									txtConditionalAmt.setValue(legalConsumer.getConDepAmt().toString());
								}
								txtConditionalPayeeName.setValue(legalConsumer.getConPayeeName());
								dateConditionalDate.setValue(legalConsumer.getConDepDate());
							}else if(legalConsumer.getAppealPrefferedBy()!=null && !legalConsumer.getAppealPrefferedBy()){
								chkStateCommision.setValue(legalConsumer.getStateCommFlag());
								chkNationalCommision.setValue(legalConsumer.getNationalCommFlag());
								chkSupremeCourt.setValue(legalConsumer.getSupremeCourtFlag());
								txtadvocatenameComm.setValue(legalConsumer.getAwdAdvocateName());
								txtFANumber.setValue(legalConsumer.getFaNumber());
								if(legalConsumer.getStateId()!=null){
									State stateID = legalConsumer.getStateId();
									SelectValue selectValue5 = new SelectValue();
									selectValue5.setId(stateID.getKey());
									selectValue5.setValue(stateID.getValue());
									cmbState.setReadOnly(false);
									cmbState.setValue(selectValue5);
									cmbState.setReadOnly(true);
								}
								dateOfHearing.setValue(legalConsumer.getDateHearing());
								chkReplyField.setValue(legalConsumer.getReplyField());
								if(legalConsumer.getStatusCaseId()!=null){
									MastersValue conAmtStsId = legalConsumer.getStatusCaseId();
									SelectValue selectValue2 = new SelectValue();
									selectValue2.setId(conAmtStsId.getKey());
									selectValue2.setValue(conAmtStsId.getValue());
									cmbStatus.setValue(selectValue2);
								}
							}
							
					}
				}
			}
			if(legalConsumer.getCaseStatusId()!=null){
				MastersValue caseStatusId = legalConsumer.getCaseStatusId();
				SelectValue selectValue2 = new SelectValue();
				selectValue2.setId(caseStatusId.getKey());
				selectValue2.setValue(caseStatusId.getValue());
				cmbCaseUpdate.setValue(selectValue2);
			}
			//chkUpdateOftheCase.setValue(legalConsumer.getUpdcaseFlag());
			txtRecordLastUpdatedRemarks.setValue(legalConsumer.getCaseUpdRemarks());
			dateFreshPreviousDateHearing.setValue(legalConsumer.getFrshPrevHearingDt());
			dateDateofNextHearing.setValue(legalConsumer.getNextHearingDate());
			dateVakalathFieldDate.setValue(legalConsumer.getVakalathFieldDate());
			txtadvocatename.setValue(legalConsumer.getAdvocateName());
		}
	}
	
public void addOrderDetailsCLickListener() {
		
		orderDetails
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
					 if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "true"){
						 
						 unbindOrderDetailVerticalLayout();
						 orderDetailBinder.getItemDataSource().getBean().setOrderDetails(true);
						 SelectValue selectValue = new SelectValue();
						 selectValue = (SelectValue) cmbresultorder.getValue();
						 orderDetailBinder.getItemDataSource().getBean().setOrder(selectValue);
						 orderDetailBinder.getItemDataSource().getBean().setDateOfReciept(dateReceipt.getValue());
						 orderDetailBinder.getItemDataSource().getBean().setDateOforder(dateOrder.getValue());
						 orderDetailBinder.getItemDataSource().getBean().setLimitationOfComplainance(datelimitation.getValue());
						 cmbresultorder.setRequired(true);
						 dateReceipt.setRequired(true);
						 dateOrder.setRequired(true);
						 datelimitation.setRequired(true);
						/* cmbresultorder = orderDetailBinder.buildAndBind("ORDER","order",ComboBox.class);
							dateReceipt= orderDetailBinder.buildAndBind("Date of Receipt","dateOfReciept",DateField.class);
							dateOrder= orderDetailBinder.buildAndBind("Date of Order/Award","dateOforder",DateField.class);
							datelimitation= orderDetailBinder.buildAndBind("Limitation Date","limitationOfComplainance",DateField.class);*/
						 
							orderDetails.setValue(true);
						 
						/* mandatoryFields.add(cmbresultorder);
						 mandatoryFields.add(dateReceipt);
						 mandatoryFields.add(dateOrder);
						 mandatoryFields.add(datelimitation);*/
						 
						 	
					 }
					 if(event.getProperty().getValue()!=null && event.getProperty().getValue().toString() == "false"){
						 unbindOrderDetailVerticalLayout();
						 orderDetailBinder.getItemDataSource().getBean().setOrderDetails(false);
						 cmbresultorder.setRequired(false);
						 dateReceipt.setRequired(false);
						 dateOrder.setRequired(false);
						 datelimitation.setRequired(false);
						
						
					 }
				}
		});
}
	
	/*validatePage(){
		
		
	}*/
	
}
